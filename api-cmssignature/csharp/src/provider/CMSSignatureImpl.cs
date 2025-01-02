using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.asn.cms;
using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    /**
     * CMSSignature adapter for signature common API
     * @author ayetgin
     * @see Signature
     */
    public class CMSSignatureImpl : Signature
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        readonly Context context;

        Dictionary<String, Object> paramaters;
        readonly List<IAttribute> optionalAttributes = new List<IAttribute>();

        private readonly SDValidationData validationData;

        readonly BaseSignedData baseSignedData;
        readonly SignatureContainerEx container;
        readonly ECertificate certificate;
        public Signer signer;
        readonly Signer parentSigner; // if counter

        public CMSSignatureImpl(Context context,
                                SignatureContainerEx container,
                                BaseSignedData parentSignature, Signer self, Signer parentSigner,
                                ECertificate cert,
                                SDValidationData validationData)
        {
            this.context = context;
            paramaters = CMSSigProviderUtil.toSignatureParameters(context);
            baseSignedData = parentSignature;
            this.container = container;
            this.certificate = cert;
            this.parentSigner = parentSigner;
            this.signer = self;
            this.validationData = validationData;
        }

        public void setSigningTime(DateTime? aTime)
        {
            try
            {
                optionalAttributes.Add(new SigningTimeAttr(aTime));
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }

        public void setSignaturePolicy(SignaturePolicyIdentifier policyId)
        {
            try
            {
                optionalAttributes.Add(
                        new SignaturePolicyIdentifierAttr(
                                policyId.getDigestValue(),
                                policyId.getDigestAlg(),
                                policyId.getPolicyId().getValue(),
                                policyId.getPolicyURI(),
                                policyId.getUserNotice()));
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }

        public DateTime? getSigningTime()
        {
            checkIfSignerCreated();
            return signer.getSignerInfo().getSigningTime();
        }

        public SignaturePolicyIdentifier getSignaturePolicy()
        {
            checkIfSignerCreated();
           
            return signer.getSignaturePolicy();
        }
/*
        public DateTime? getSignatureTimestampTime()
        {
            checkIfSignerCreated();

            if (getSignatureType() == SignatureType.ES_BES || getSignatureType() == SignatureType.ES_EPES)
                return null;

            try
            {
                List<EAttribute> tsAttrs = signer.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);

                if (tsAttrs == null || tsAttrs.Count == 0)
                    return null;

                EContentInfo ci = new EContentInfo(tsAttrs[0].getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());

                DateTime? tsTime = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent()).getTime();
                return tsTime;
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
*/
        public List<TimestampInfo> getTimestampInfo(TimestampType type) {
        checkIfSignerCreated();

        try {
            List<EAttribute> tsAttrs = signer.getUnsignedAttribute(CMSSigProviderUtil.convert(type));
            if (type == TimestampType.CONTENT_TIMESTAMP)
            {
                tsAttrs = signer.getSignedAttribute(CMSSigProviderUtil.convert(type));
            }
            if (tsAttrs == null || tsAttrs.Count==0)
                return new List<TimestampInfo>();

            List<TimestampInfo> results = new List<TimestampInfo>(tsAttrs.Count);
            foreach (EAttribute attr in tsAttrs) {
                EContentInfo ci = new EContentInfo(attr.getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());
                ETSTInfo tstinfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                TimestampInfo tsInfo = new TimestampInfoImp(type, sd, tstinfo);
                results.Add(tsInfo);
            }
            return results;
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }

    }

        public List<TimestampInfo> getAllTimestampInfos()
        {
            List<TimestampInfo> all = new List<TimestampInfo>();
            all.AddRange(getTimestampInfo(TimestampType.CONTENT_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.REFERENCES_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V2));
            all.AddRange(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V3)); 
            return all;
        }

        public CertValidationReferences getCertValidationReferences() {
          //null?
        SignatureType? type = getSignatureType();
        if (type == SignatureType.ES_BES || type == SignatureType.ES_EPES || type == SignatureType.ES_T)
            return null;

        List<CertificateSearchCriteria> certsc = new List<CertificateSearchCriteria>();
        List<CRLSearchCriteria> crlsc = new List<CRLSearchCriteria>();
        List<OCSPSearchCriteria> ocspsc = new List<OCSPSearchCriteria>();

        try {
            ECompleteCertificateReferences certificateReferences = signer.getSignerInfo().getCompleteCertificateReferences();
            ECompleteRevocationReferences revocationReferences = signer.getSignerInfo().getCompleteRevocationReferences();
            if (certificateReferences == null || revocationReferences == null)
                return null;

            EOtherCertID[] certIDs = certificateReferences.getCertIDs();
            ECrlOcspRef[] revIDs = revocationReferences.getCrlOcspRefs();

            foreach (EOtherCertID certId in certIDs) {
                CertificateSearchCriteria certificateSearchCriteria = new CertificateSearchCriteria();
                // todo csc.setIssuer();
                certificateSearchCriteria.setIssuer(certId.getIssuerName().toStringWithNewLines());
                certificateSearchCriteria.setDigestValue(certId.getDigestValue());
                certificateSearchCriteria.setDigestAlg(DigestAlg.fromOID(certId.getDigestAlg()));
                certificateSearchCriteria.setSerial(certId.getIssuerSerial());

                certsc.Add(certificateSearchCriteria);
            }

            foreach (ECrlOcspRef crlOcspRef in revIDs) {

                ECrlValidatedID[] crlIds = crlOcspRef.getCRLIds();
                EOcspResponsesID[] ocspIds = crlOcspRef.getOcspResponseIds();

                if (crlIds != null) {
                    foreach (ECrlValidatedID crlId in crlIds) {
                        CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

                        crlSearchCriteria.setDigestValue(crlId.getDigestValue());
                        crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(crlId.getDigestAlg()));
                        if (crlId.getObject().crlIdentifier != null)
                        {
                            crlSearchCriteria.setIssuer(crlId.getCrlissuer().stringValue());
                            crlSearchCriteria.setIssueTime(crlId.getCrlIssuedTime().Value.ToLocalTime());
                            crlSearchCriteria.setNumber(crlId.getCrlNumber());
                        }
                        crlsc.Add(crlSearchCriteria);
                    }
                }

                if (ocspIds != null) {
                    foreach (EOcspResponsesID ocspId in ocspIds) {
                        OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();
                        if (ocspId.getObject().ocspRepHash != null)
                        {
                            ocspSearchCriteria.setDigestValue(ocspId.getDigestValue());
                            ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(ocspId.getDigestAlg()));
                        }
                        EResponderID responder = ocspId.getOcspResponderID();
                        if (responder.getType() == EResponderID._BYKEY)
                            ocspSearchCriteria.setOCSPResponderIDByKey(responder.getResponderIdByKey());
                        else
                            ocspSearchCriteria.setOCSPResponderIDByName(responder.getResponderIdByName().stringValue());
                        ocspSearchCriteria.setProducedAt(ocspId.getProducedAt().Value.ToLocalTime());

                        ocspsc.Add(ocspSearchCriteria);
                    }
                }

            }
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
        return new CertValidationReferences(certsc, crlsc, ocspsc);
    }

        public CertValidationValues getCertValidationValues()
        {
            SignatureType? type = getSignatureType();
            if (!(type == SignatureType.ES_XL || type == SignatureType.ES_XL_Type1 || type == SignatureType.ES_XL_Type2 || type == SignatureType.ES_A))
                return null;
            try
            {
                ERevocationValues revVals = signer.getSignerInfo().getRevocationValues();
                ECertificateValues certVals = signer.getSignerInfo().getCertificateValues();
                if (revVals == null || certVals == null)
                    return null;

                List<ECertificate> certificates = certVals.getCertificates();
                if (certificates == null)
                    certificates = new List<ECertificate>();

                List<ECRL> crls = revVals.getCRLs();
                if (crls == null)
                    crls = new List<ECRL>();

                List<EBasicOCSPResponse> ocspResponses = revVals.getBasicOCSPResponses();
                if (ocspResponses == null)
                    ocspResponses = new List<EBasicOCSPResponse>();

                //certificates = certVals.getCertificates() != null ? certVals.getCertificates() : new ArrayList<ECertificate>() ;

                return new CertValidationValues(certificates, crls, ocspResponses);
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }

        public Signature createCounterSignature(ECertificate certificate)
        {
            checkIfSignerCreated();
            return new CMSSignatureImpl(context,container, baseSignedData, null, this.signer, certificate, validationData);
        }

        public List<Signature> getCounterSignatures()
        {
            List<Signature> counterSignatures = new List<Signature>();
            try
            {
                List<Signer> counterSigners = signer.getCounterSigners();
                foreach (Signer s in counterSigners)
                {
                    Signature sig = new CMSSignatureImpl(context,container, baseSignedData, s, this.signer, null, validationData);
                    counterSignatures.Add(sig);
                }
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
            return counterSignatures;
        }

        public void detachFromParent()
        {
            signer.remove();
        }

        public void addContent(Signable aData, bool includeContent)
        {
            // todo check if same content
            if (!includeContent){
                paramaters[EParameters.P_EXTERNAL_CONTENT]= new SignableISignable(aData);
            }
            if (baseSignedData.getSignedData().getSignerInfoCount() > 0)
            {
                // already has content
                return;
            }
            baseSignedData.addContent(new SignableISignable(aData), includeContent);
        }

        public List<Signable> getContents()
        {
            checkIfSignerCreated();
            Object signableObj = null;
            paramaters.TryGetValue(EParameters.P_EXTERNAL_CONTENT, out signableObj);
            ISignable signable = (ISignable)signableObj;

            if (signable == null)
            {
                try
                {
                    byte[] content = baseSignedData.getSignedData().getEncapsulatedContentInfo().getContent();
                    signable = new SignableByteArray(content);
                }
                catch (NullReferenceException npx)
                {
                    logger.Warn(npx.StackTrace);
                }
            }

            if (signable == null)
                throw new SignatureRuntimeException("Cant get signed content!");
            List<Signable> signableList = new List<Signable>();
            signableList.Add(new CMSSignableImpl(signable));
            return signableList as List<Signable>;
        }
        public IAlgorithm getSignatureAlg()
        {
            try
            {
                DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(signer.getSignerInfo().getDigestAlgorithm());

                Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(signer.getSignerInfo().getSignatureAlgorithm());

                SignatureAlg sigAlg = pair.first();
                //Bazı imza üretiticileri signature alg kısmına sadece asimetrik algoritmayı koyuyorlar.
                if (sigAlg == SignatureAlg.RSA_NONE && digestAlg != null)
                {
                    sigAlg = SignatureAlg.fromName(sigAlg.getAsymmetricAlg().getName() + "-with-" +
                            digestAlg.getName().Replace("-", ""));
                }
                return sigAlg;
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
        public void sign(BaseSigner cryptoSigner)
        {
            if (parentSigner == null)
            {
                baseSignedData.addSigner(ESignatureType.TYPE_BES, certificate, cryptoSigner, optionalAttributes, paramaters);
                List<Signer> signers = baseSignedData.getSignerList();
                signer = signers[signers.Count - 1];
            }
            else
            {
                parentSigner.addCounterSigner(ESignatureType.TYPE_BES, certificate, cryptoSigner, optionalAttributes, paramaters);
                List<Signer> signers = parentSigner.getCounterSigners();
                signer = signers[signers.Count - 1];
            }
        }

        public void upgrade(SignatureType type)
        {
            checkIfSignerCreated();
            if (type != SignatureType.ES_A)
                signer.convert(CMSSigProviderUtil.convert(type), paramaters);
            else
            {
                if (useATSv2())
                    signer.convert(ESignatureType.TYPE_ESAv2, paramaters);
                else
                    signer.convert(ESignatureType.TYPE_ESA, paramaters);
            }
        }

        public SignatureValidationResult verify()
        {
            checkIfSignerCreated();
            tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult svr
                    = new tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult();

            SignatureValidator sv = new SignatureValidator(baseSignedData.getEncoded());
            sv.setCertificates(validationData.getCerts());
            sv.setCRLs(validationData.getCRLs());
            sv.setOCSPs(validationData.getOCSPResponses());

            // update? for if context.data is added later
            paramaters = CMSSigProviderUtil.toSignatureParameters(context);
            if (parentSigner != null)
            {
                paramaters[AllEParameters.P_PARENT_SIGNER_INFO] = parentSigner.getSignerInfo();
            }

            sv.verify(svr, signer, (parentSigner != null), paramaters);

            return svr;
        }

        public void addArchiveTimestamp()
        {
            checkIfSignerCreated();
		if (useATSv2())
			signer.convert(ESignatureType.TYPE_ESAv2, paramaters);
		else
            signer.convert(ESignatureType.TYPE_ESA, paramaters);
        }

        public SignatureType? getSignatureType()
        {
            checkIfSignerCreated();
            return CMSSigProviderUtil.convert(signer.getType());
        }

        public SignatureContainer getContainer()
        {
            return container;
        }
        public SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.CAdES;
        }

        public ECertificate getSignerCertificate()
        {
            checkIfSignerCreated();
            return signer.getSignerCertificate();
        }

        public Object getUnderlyingObject()
        {
            return baseSignedData;
        }
        private bool useATSv2()
        {
            return context.getConfig().getParameters().isUseCAdESATSv2();
        }
        /// signer is created at sign method. Any method depends on signer
        /// throws Exception, if called before signer is created...
        private void checkIfSignerCreated()
        {
            if (signer == null)
                throw new SignatureRuntimeException(
                        "Signature not constructed yet. You cant call this method before signature.sign()");
        }
    }
}

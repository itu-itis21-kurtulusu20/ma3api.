package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponderID;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidator;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;
import tr.gov.tubitak.uekae.esya.api.signature.impl.TimestampInfoImp;

import java.util.*;

/**
 * CMSSignature adapter for signature common API
 *
 * @author ayetgin
 * @see Signature
 */
public class CMSSignatureImpl implements Signature {
    private static Logger logger = LoggerFactory.getLogger(CMSSignatureImpl.class);

    Context context;

    Map<String, Object> params;
    List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();

    private SDValidationData validationData;

    BaseSignedData baseSignedData;
    SignatureContainerEx container;
    ECertificate certificate;
    Signer signer;
    Signer parentSigner; // if counter

    public CMSSignatureImpl(Context context,
                            SignatureContainerEx container,
                            BaseSignedData parentSignature, Signer self, Signer parentSigner,
                            ECertificate cert,
                            SDValidationData validationData) {
        this.context = context;
        params = CMSSigProviderUtil.toSignatureParameters(context);
        baseSignedData = parentSignature;
        this.container = container;
        this.certificate = cert;
        this.parentSigner = parentSigner;
        this.signer = self;
        this.validationData = validationData;
    }

    public void setSigningTime(Calendar aTime) {
        try {
            optionalAttributes.add(new SigningTimeAttr(aTime));
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
    }

    public void setSignaturePolicy(SignaturePolicyIdentifier policyId) {
        try {
            optionalAttributes.add(
                    new SignaturePolicyIdentifierAttr(
                            policyId.getDigestValue(),
                            policyId.getDigestAlg(),
                            policyId.getPolicyId().getValue(),
                            policyId.getPolicyURI(),
                            policyId.getUserNotice()));
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
    }

    public Calendar getSigningTime() {
        checkIfSignerCreated();
        return signer.getSignerInfo().getSigningTime();
    }

    public SignaturePolicyIdentifier getSignaturePolicy() {
        checkIfSignerCreated();

        return signer.getSignaturePolicy();
    }

    public void setCommitmentType(CommitmentType commitmentType) throws CMSSignatureException {
        optionalAttributes.add(new CommitmentTypeIndicationAttr(commitmentType));
    }

    public List<EAttribute> getCommitmentTypeAttributes() {
       return signer.getAttribute(CommitmentTypeIndicationAttr.OID);
    }

    /*
    public Calendar getSignatureTimestampTime()
    {
        checkIfSignerCreated();

        if (getSignatureType()==SignatureType.ES_BES || getSignatureType()==SignatureType.ES_EPES)
            return null;

        try {
            List<EAttribute> tsAttrs = signer.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);

            if (tsAttrs==null || tsAttrs.isEmpty())
                return null;

            EContentInfo ci = new EContentInfo(tsAttrs.get(0).getValue(0));
            ESignedData sd = new ESignedData(ci.getContent());

            Calendar tsTime = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent()).getTime();
            return tsTime;
        }
        catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    } */

    public List<TimestampInfo> getTimestampInfo(TimestampType type) {
        checkIfSignerCreated();        
        try {
            List<EAttribute> tsAttrs = signer.getUnsignedAttribute(CMSSigProviderUtil.convert(type));
            if(type == TimestampType.CONTENT_TIMESTAMP){
            	tsAttrs = signer.getSignedAttribute(CMSSigProviderUtil.convert(type));
            }
            if (tsAttrs == null || tsAttrs.isEmpty())
                return Collections.EMPTY_LIST;

            List<TimestampInfo> results = new ArrayList<TimestampInfo>(tsAttrs.size());
            for (EAttribute attr : tsAttrs) {
                EContentInfo ci = new EContentInfo(attr.getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());
                ETSTInfo tstinfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                TimestampInfo tsInfo = new TimestampInfoImp(type, sd, tstinfo);
                results.add(tsInfo);
            }
            return results;
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }

    }

    public List<TimestampInfo> getAllTimestampInfos() {
        List<TimestampInfo> all = new ArrayList<TimestampInfo>();
        all.addAll(getTimestampInfo(TimestampType.CONTENT_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.REFERENCES_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V2));
        all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V3));
        return all;
    }

    public CertValidationReferences getCertValidationReferences() {

        SignatureType type = getSignatureType();
        if (type==SignatureType.ES_BES || type==SignatureType.ES_EPES || type==SignatureType.ES_T)
            return null;

        List<CertificateSearchCriteria> certsc = new ArrayList<CertificateSearchCriteria>();
        List<CRLSearchCriteria> crlsc = new ArrayList<CRLSearchCriteria>();
        List<OCSPSearchCriteria> ocspsc = new ArrayList<OCSPSearchCriteria>();

        try {
            ECompleteCertificateReferences certificateReferences = signer.getSignerInfo().getCompleteCertificateReferences();
            ECompleteRevocationReferences revocationReferences = signer.getSignerInfo().getCompleteRevocationReferences();
            if(certificateReferences == null || revocationReferences == null)
            	return null;
            
            EOtherCertID[] certIDs = certificateReferences.getCertIDs();
            ECrlOcspRef[] revIDs = revocationReferences.getCrlOcspRefs();

            for (EOtherCertID certId : certIDs) {
                CertificateSearchCriteria certificateSearchCriteria = new CertificateSearchCriteria();
                // todo csc.setIssuer();
                certificateSearchCriteria.setIssuer(certId.getIssuerName().toStringWithNewLines());
                certificateSearchCriteria.setDigestValue(certId.getDigestValue());
                certificateSearchCriteria.setDigestAlg(DigestAlg.fromOID(certId.getDigestAlg()));
                certificateSearchCriteria.setSerial(certId.getIssuerSerial());

                certsc.add(certificateSearchCriteria);
            }

            for (ECrlOcspRef crlOcspRef : revIDs) {

                ECrlValidatedID[] crlIds = crlOcspRef.getCRLIds();
                EOcspResponsesID[] ocspIds = crlOcspRef.getOcspResponseIds();

                if (crlIds != null) {
                    for (ECrlValidatedID crlId : crlIds) {
                        CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

                        crlSearchCriteria.setDigestValue(crlId.getDigestValue());
                        crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(crlId.getDigestAlg()));
                        if (crlId.getObject().crlIdentifier != null) {
                        crlSearchCriteria.setIssuer(crlId.getCrlissuer().stringValue());
                        crlSearchCriteria.setIssueTime(crlId.getCrlIssuedTime().getTime());
                        crlSearchCriteria.setNumber(crlId.getCrlNumber());
                        }
                        crlsc.add(crlSearchCriteria);
                    }
                }

                if (ocspIds != null) {
                    for (EOcspResponsesID ocspId : ocspIds) {
                        OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();
                        if(ocspId.getObject().ocspRepHash != null){
                        ocspSearchCriteria.setDigestValue(ocspId.getDigestValue());
                        ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(ocspId.getDigestAlg()));
                        }
                        EResponderID responder = ocspId.getOcspResponderID();
                        if (responder.getType() == EResponderID._BYKEY)
                            ocspSearchCriteria.setOCSPResponderIDByKey(responder.getResponderIdByKey());
                        else
                            ocspSearchCriteria.setOCSPResponderIDByName(responder.getResponderIdByName().stringValue());
                        ocspSearchCriteria.setProducedAt(ocspId.getProducedAt().getTime());

                        ocspsc.add(ocspSearchCriteria);
                    }
                }

            }
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
        return new CertValidationReferences(certsc, crlsc, ocspsc);
    }

    public CertValidationValues getCertValidationValues() {
        SignatureType type = getSignatureType();
        if (!(type==SignatureType.ES_XL || type==SignatureType.ES_XL_Type1 || type==SignatureType.ES_XL_Type2 || type==SignatureType.ES_A))
            return null;
        try {
            ERevocationValues revVals = signer.getSignerInfo().getRevocationValues();
            ECertificateValues certVals = signer.getSignerInfo().getCertificateValues();
            if(revVals == null || certVals == null)
            	return null;
            
            List<ECertificate> certificates = certVals.getCertificates();
            if(certificates == null)
            	certificates = new ArrayList<ECertificate>();
            
            List<ECRL> crls = revVals.getCRLs();
            if(crls == null)
            	crls = new ArrayList<ECRL>();
            
            List<EBasicOCSPResponse> ocspResponses = revVals.getBasicOCSPResponses();
            if(ocspResponses == null)
            	ocspResponses = new ArrayList<EBasicOCSPResponse>();
            
            //certificates = certVals.getCertificates() != null ? certVals.getCertificates() : new ArrayList<ECertificate>() ;
            
            return new CertValidationValues(certificates, crls, ocspResponses);
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    public Signature createCounterSignature(ECertificate certificate) {
        checkIfSignerCreated();
        return new CMSSignatureImpl(context, container, baseSignedData, null, this.signer, certificate, validationData);
    }

    public List<Signature> getCounterSignatures() {
        List<Signature> counterSignatures = new ArrayList<Signature>();
        try {
            List<Signer> counterSigners = signer.getCounterSigners();
            for (Signer s : counterSigners) {
                Signature sig = new CMSSignatureImpl(context, container, baseSignedData, s, this.signer, null, validationData);
                counterSignatures.add(sig);
            }
        } catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
        return counterSignatures;
    }

    public void detachFromParent() throws SignatureException {
        signer.remove();
    }

    public void addContent(Signable aData, boolean includeContent) throws SignatureException {
        // todo check if same content
        if (!includeContent)
            params.put(EParameters.P_EXTERNAL_CONTENT, new SignableISignable(aData));
        if (baseSignedData.getSignedData().getSignerInfoCount() > 0) {
            // already has content
            return;
        }
        baseSignedData.addContent(new SignableISignable(aData), includeContent);
    }

    public List<Signable> getContents() throws SignatureException {
        checkIfSignerCreated();

        ISignable signable = (ISignable) params.get(EParameters.P_EXTERNAL_CONTENT);
        if (signable == null) {
            try {
                byte[] content = baseSignedData.getSignedData().getEncapsulatedContentInfo().getContent();
                signable = new SignableByteArray(content);
            } catch (NullPointerException npx) {
                logger.error("Error in CMSSignatureImpl", npx);
            }
        }

        if (signable == null)
            throw new SignatureRuntimeException("Cant get signed content!");

        return Arrays.asList((Signable) new CMSSignableImpl(signable));
    }

    public SignatureAlg getSignatureAlg() {
        try {
        DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(signer.getSignerInfo().getDigestAlgorithm());

        Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(signer.getSignerInfo().getSignatureAlgorithm());

        SignatureAlg sigAlg = pair.first();
        //Bazı imza üretiticileri signature alg kısmına sadece asimetrik algoritmayı koyuyorlar.
        if (sigAlg == SignatureAlg.RSA_NONE && digestAlg != null)
        {
            sigAlg = SignatureAlg.fromName(sigAlg.getAsymmetricAlg().getName() + "-with-" +
                    digestAlg.getName().replace("-", ""));
        }
        return sigAlg;
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    public void sign(BaseSigner cryptoSigner) throws SignatureException {
        if (parentSigner == null) {
            /*try {
                System.out.println("signing : "+ Base64.encode(((ISignable) (params.get(EParameters.P_EXTERNAL_CONTENT))).getMessageDigest(DigestAlg.SHA256)));
            } catch (Exception x){
                x.printStackTrace();
            } */
            baseSignedData.addSigner(ESignatureType.TYPE_BES, certificate, cryptoSigner, optionalAttributes, params);

            List<Signer> signers = baseSignedData.getSignerList();
            signer = signers.get(signers.size() - 1);
        } else {
            parentSigner.addCounterSigner(ESignatureType.TYPE_BES, certificate, cryptoSigner, optionalAttributes, params);
            List<Signer> signers = parentSigner.getCounterSigners();
            signer = signers.get(signers.size() - 1);
        }
    }

    public void upgrade(SignatureType type) throws SignatureException {
		checkIfSignerCreated();
		if (type != SignatureType.ES_A)
			signer.convert(CMSSigProviderUtil.convert(type), params);
		else {
			if (useATSv2())
				signer.convert(ESignatureType.TYPE_ESAv2, params);
			else
				signer.convert(ESignatureType.TYPE_ESA, params);
		}
    }

    public SignatureValidationResult verify() throws SignatureException {
        checkIfSignerCreated();
        tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult svr
                = new tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult();

        SignatureValidator sv = new SignatureValidator(baseSignedData.getEncoded());
        sv.setCertificates(validationData.getCerts());
        sv.setCRLs(validationData.getCRLs());
        sv.setOCSPs(validationData.getOCSPResponses());

        // update? for if context.data is added later
        params = CMSSigProviderUtil.toSignatureParameters(context);
        if(parentSigner!=null){
        	params.put(AllEParameters.P_PARENT_SIGNER_INFO, parentSigner.getSignerInfo());
        }
        /*
        try {
            System.out.println("validating : "+ Base64.encode(((ISignable) (params.get(EParameters.P_EXTERNAL_CONTENT))).getMessageDigest(DigestAlg.SHA256)));
        } catch (Exception x){
            x.printStackTrace();
        }  */

        sv.verify(svr, signer, (parentSigner != null), params);

        return svr;
    }

    public void addArchiveTimestamp() throws SignatureException {
		checkIfSignerCreated();
		if (useATSv2())
			signer.convert(ESignatureType.TYPE_ESAv2, params);
		else
			signer.convert(ESignatureType.TYPE_ESA, params);
    }

    public SignatureType getSignatureType() {
        checkIfSignerCreated();
        return CMSSigProviderUtil.convert(signer.getType());
    }

    public SignatureContainer getContainer() {
        return container;
    }

    public SignatureFormat getSignatureFormat() {
        return SignatureFormat.CAdES;
    }

    public ECertificate getSignerCertificate() {
        checkIfSignerCreated();
        return signer.getSignerCertificate();
    }

    public Object getUnderlyingObject() {
        return baseSignedData;
    }
    
    private boolean useATSv2(){
    	return context.getConfig().getParameters().isUseCAdESATSv2();
    }
    
    /// signer is created at sign method. Any method depends on signer
    /// throws Exception, if called before signer is created...
    private void checkIfSignerCreated() {
    	if (signer == null)
    		throw new SignatureRuntimeException(          
    				"Signature not constructed yet. You cant call this method before signature.sign()");
    }
    public Signer getSigner() {
    	return signer;
    }
}

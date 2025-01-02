using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.provider
{
    using SignaturePolicyIdentifier = tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier;
    using SignatureType = tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
    using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult;

    public class SignatureImpl : Signature
    {
        private readonly SignatureContainerEx container;
        private readonly Signature parentSignature;
        private readonly XMLSignature signature;

        /*
        public SignatureImpl(Context context)
        {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.Context c
                    = new tr.gov.tubitak.uekae.esya.api.xmlsignature.Context(context.getBaseURI());
            signature = new XMLSignature(c);
        }  */

        public SignatureImpl(SignatureContainerEx container, XMLSignature signature, SignatureImpl parentSignature)
        {
            this.container = container;
            this.signature = signature;
            this.parentSignature = parentSignature;
        }


        public void setSigningTime(DateTime? aTime)
        {
            signature.SigningTime = aTime;
        }

        public void setSignaturePolicy(SignaturePolicyIdentifier policyId)
        {
            signature.QualifyingProperties.SignedSignatureProperties
                    .SignaturePolicyIdentifier =
                            new tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
                                    .SignaturePolicyIdentifier(signature.Context, policyId);
        }

        /*public void setSigningTime(DateTime? aTime)
        {
            throw new NotImplementedException();
        }*/

        public SignaturePolicyIdentifier getSignaturePolicy()
        {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier spi
                    = signature.QualifyingProperties.SignedSignatureProperties.SignaturePolicyIdentifier;
            if (spi==null)
                return null;
            SignaturePolicyId spid = spi.SignaturePolicyId;
            String spURI=null, userNotice=null;
            foreach (SignaturePolicyQualifier spq in spid.PolicyQualifiers){

                if (spq.URI != null){
                    spURI = spq.URI;
                }
                if (spq.UserNotice != null){
                    userNotice = spq.UserNotice.getExplicitTexts()[0];
                }
            }

            String id = spid.PolicyId.Identifier.Value;
            OID oid = new OID(OIDUtil.fromURN(id));

            SignaturePolicyIdentifier policyIdentifier = new SignaturePolicyIdentifier(
                        oid,
                        spid.DigestMethod.Algorithm, spid.DigestValue,
                        spURI, userNotice);
            return policyIdentifier;
        }

        public DateTime? getSigningTime()
        {
            DateTime? cal = signature.QualifyingProperties.SignedSignatureProperties.SigningTime;
            if (cal == null)
                return null;
            return cal;
        }

        /*public DateTime? getSignatureTimestampTime()
        {
            if (getSignatureType() == SignatureType.ES_BES || getSignatureType() == SignatureType.ES_EPES)
                return null;

            SignatureTimeStamp sts = signature.QualifyingProperties.UnsignedSignatureProperties.getSignatureTimeStamp(0);
            DateTime? c  = null;
            try {
                c = sts.getEncapsulatedTimeStamp(0).Time;
            } catch (Exception x){
                throw x;
            }
            return c;
        }*/

        public List<TimestampInfo> getTimestampInfo(TimestampType type) {
            
            List<TimestampInfo> all = new List<TimestampInfo>();
            
            if (type == TimestampType.CONTENT_TIMESTAMP)
            {
                SignedDataObjectProperties sdop = signature.QualifyingProperties.SignedDataObjectProperties;
                all.AddRange(convert(sdop.getAllDataObjectsTimeStamps(), type));
                all.AddRange(convert(sdop.getIndividualDataObjectsTimeStamps(), type));
                return all;
            }
            
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            
            switch (type){
                case TimestampType.SIGNATURE_TIMESTAMP              : all.AddRange(convert(usp.getSignatureTimeStamps(), type)); break;
                case TimestampType.SIG_AND_REFERENCES_TIMESTAMP     : all.AddRange(convert(usp.getSigAndRefsTimeStamps(), type)); break;
                case TimestampType.REFERENCES_TIMESTAMP             : all.AddRange(convert(usp.getRefsOnlyTimeStamps(), type)); break;
                case TimestampType.ARCHIVE_TIMESTAMP                :
                case TimestampType.ARCHIVE_TIMESTAMP_V2             : all.AddRange(convert(usp.getArchiveTimeStamps(), type)); break; //todo diff v1 - v2
                //case ARCHIVE_TIMESTAMP_V3         : all.addAll(usp.getSigAndRefsTimeStamps(), type); break;
            }
            return all;
        }

        private List<TimestampInfo> convert<T>(IList<T> timestamps, TimestampType type) where T : XAdESTimeStamp{
            List<TimestampInfo> all = new List<TimestampInfo>();
            foreach (XAdESTimeStamp xts in timestamps){
                for (int i=0; i<xts.EncapsulatedTimeStampCount; i++){
                    if(xts.getType()!=type)
                        continue;
                    TimestampInfo info = new TimestampInfoImpl(type, xts.getEncapsulatedTimeStamp(i));
                    all.Add(info);
                }
            }
            return all;
        }

        public List<TimestampInfo> getAllTimestampInfos() {
            List<TimestampInfo> all = new List<TimestampInfo>();
            all.AddRange(getTimestampInfo(TimestampType.CONTENT_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.REFERENCES_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP));
            all.AddRange(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V2));
            //all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V3));  todo
            return all;
        }

        public CertValidationReferences getCertValidationReferences() {
            SignatureType? type = getSignatureType();
            if (type == SignatureType.ES_BES || type == SignatureType.ES_EPES || type == SignatureType.ES_T)
                return null;
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            CompleteCertificateRefs ccr = usp.CompleteCertificateRefs;
            CompleteRevocationRefs crr = usp.CompleteRevocationRefs;

            List<CertificateSearchCriteria> certsc = new List<CertificateSearchCriteria>();
            List<CRLSearchCriteria> crlsc = new List<CRLSearchCriteria>();
            List<OCSPSearchCriteria> ocspsc = new List<OCSPSearchCriteria>();

            for (int i=0; i<ccr.CertificateReferenceCount;i++){
                certsc.Add(ccr.getCertificateReference(i).toSearchCriteria());
            }
            for (int i=0; i<crr.CRLReferenceCount;i++){
                crlsc.Add(crr.getCRLReference(i).toSearchCriteria());
            }
            for (int i=0; i<crr.OCSPReferenceCount;i++){
                ocspsc.Add(crr.getOCSPReference(i).toSearchCriteria());
            }

            return new CertValidationReferences(certsc, crlsc, ocspsc);
        }

        public CertValidationValues getCertValidationValues() {
            SignatureType? type = getSignatureType();
            if (!(type==SignatureType.ES_XL || type==SignatureType.ES_XL_Type1 || type==SignatureType.ES_XL_Type2 || type==SignatureType.ES_A))
                return null;
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            CertificateValues cv = usp.CertificateValues;
            RevocationValues rv = usp.RevocationValues;
            try {
                List<EBasicOCSPResponse> basicOCSPResponses = new List<EBasicOCSPResponse>();
                foreach (EOCSPResponse r in rv.AllOCSPResponses)
                    basicOCSPResponses.Add(r.getBasicOCSPResponse());
                return new CertValidationValues(cv.AllCertificates, rv.AllCRLs, basicOCSPResponses);
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }

        public Signature createCounterSignature(ECertificate certificate)
        {
            XMLSignature counterSignature =  signature.createCounterSignature();
            counterSignature.addKeyInfo(certificate);

            return new SignatureImpl(container, counterSignature, this);
        }

        public List<Signature> getCounterSignatures()
        {
            List<Signature> counterSignatures = new List<Signature>();
            IList<XMLSignature> counterXmlSignatures = signature.AllCounterSignatures;
            foreach (XMLSignature xs in counterXmlSignatures){
                Signature cs = new SignatureImpl(container, xs, this);
                counterSignatures.Add(cs);
            }

            return counterSignatures;
        }

        public void detachFromParent()
        {
            try {
                if (parentSignature==null){
                    container.detachSignature(this);
                }
                else {
                    XMLSignature xmlSignature = (XMLSignature)parentSignature.getUnderlyingObject();
                    UnsignedSignatureProperties usp = xmlSignature.QualifyingProperties.UnsignedSignatureProperties;
                    usp.removeCounterSignature(signature);
                }
            }
            catch (Exception x){
                throw new SignatureException("Cant extract signature form container "+signature.GetType(), x);
            }
        }

        public void addContent(Signable aData, bool embed)
        {
            signature.addDocument(new DocumentSignable(aData), null, null, embed);
        }

        public List<Signable> getContents()
        {
            List<Signable> contents = new List<Signable>();
            SignedInfo si = signature.SignedInfo;
            for (int i=0;i<si.ReferenceCount;i++){
                Reference reference = si.getReference(i);
                if (!(reference.Type != null && Constants.REFERENCE_TYPE_SIGNED_PROPS.Equals(reference.Type))){
                    Document doc = reference.getReferencedDocument();
                    SignableDocument sd = new SignableDocument(doc);
                    contents.Add(sd);
                }
            }
            return contents;
        }

        public IAlgorithm getSignatureAlg()
        {
            return signature.SignatureMethod.MSignatureAlg;
        }

        public void sign(BaseSigner cryptoSigner)
        {
            signature.sign(cryptoSigner);
        }

        public void upgrade(SignatureType type)
        {
            signature.upgrade(type);
            /*
            switch (type){
                case ES_BES: throw new SignatureRuntimeException("Cannot upgrade to BES! (Silly upgrade!)");
                case ES_EPES: throw new SignatureRuntimeException("Cannot upgrade to EPES! (Add PolicyID instead!)");
                case ES_T:
                    switch (signature.getSignatureType()){
                        case XAdES_BES: signature.upgradeToXAdES_T(); break;
                    }
                    break;

                case ES_C:
                    switch (signature.getSignatureType()){
                        case XAdES_BES:
                            signature.upgradeToXAdES_T();
                            signature.upgradeToXAdES_C();
                            break;
                        case XAdES_T:
                            signature.upgradeToXAdES_C();
                            break;
                    }
                    break;
                case ES_X_Type1:
                    switch (signature.getSignatureType()){
                        case XAdES_BES:
                            signature.upgradeToXAdES_T();
                            signature.upgradeToXAdES_C();
                            signature.upgradeToXAdES_X1();
                            break;
                        case XAdES_T:
                            signature.upgradeToXAdES_C();
                            signature.upgradeToXAdES_X1();
                            break;
                        case XAdES_C:
                            signature.upgradeToXAdES_X1();
                            break;
                    }
                    break;

                    signature.upgradeToXAdES_X1(); break;
                case ES_X_Type2: signature.upgradeToXAdES_X2(); break;
                case ES_XL: signature.upgradeToXAdES_XL(); break;
                case ES_A:signature.upgradeToXAdES_A(); break;
            }    */
        }

        public SignatureValidationResult verify()
        {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult vr = signature.verify();
            return (SignatureValidationResult)vr;

        }

        public void addArchiveTimestamp()
        {
            signature.addArchiveTimeStamp();
        }

        public SignatureType? getSignatureType()
        {
            return XMLProviderUtil.convert(signature.SignatureType, signature);
        }

        public SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.XAdES;
        }

        public ECertificate getSignerCertificate()
        {
            return signature.SigningCertificate;
        }
        public SignatureContainer getContainer()
        {
            return container;
        }
        public XMLSignature getInternalSignature(){
            return signature;
        }

        public Object getUnderlyingObject()
        {
            return signature;
        }
    }
}

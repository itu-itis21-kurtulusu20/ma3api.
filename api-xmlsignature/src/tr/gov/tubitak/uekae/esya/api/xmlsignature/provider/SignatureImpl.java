package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;


import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyId;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyQualifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CertificateValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author ayetgin
 */
public class SignatureImpl implements Signature
{
    private SignatureContainerEx container;
    private Signature parentSignature;
    private XMLSignature signature;

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


    public void setSigningTime(Calendar aTime)
    {
        signature.setSigningTime(aTime);
    }

    public void setSignaturePolicy(SignaturePolicyIdentifier policyId)
    {
        signature.getQualifyingProperties().getSignedSignatureProperties()
                .setSignaturePolicyIdentifier(
                        new tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
                                .SignaturePolicyIdentifier(signature.getContext(), policyId));
    }

    public SignaturePolicyIdentifier getSignaturePolicy()
    {
        tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier spi
                = signature.getQualifyingProperties().getSignedSignatureProperties().getSignaturePolicyIdentifier();
        if (spi==null)
            return null;
        SignaturePolicyId spid = spi.getSignaturePolicyId();
        String spURI=null, userNotice=null;
        for (SignaturePolicyQualifier spq : spid.getPolicyQualifiers()){

            if (spq.getURI()!=null){
                spURI = spq.getURI();
            }
            if (spq.getUserNotice()!=null){
                userNotice = spq.getUserNotice().getExplicitTexts().get(0);
            }
        }

        String id = spid.getPolicyId().getIdentifier().getValue();
        OID oid = new OID(OIDUtil.fromURN(id));

        SignaturePolicyIdentifier policyIdentifier = new SignaturePolicyIdentifier(
                oid,
                spid.getDigestMethod().getAlgorithm(), spid.getDigestValue(),
                spURI, userNotice);
        return policyIdentifier;
    }

    public Calendar getSigningTime()
    {
        XMLGregorianCalendar cal = signature.getQualifyingProperties().getSignedSignatureProperties().getSigningTime();
        if (cal==null)
            return null;
        return cal.toGregorianCalendar();
    }

    /*
    public Calendar getSignatureTimestampTime()
    {
        if (getSignatureType()==SignatureType.ES_BES || getSignatureType()==SignatureType.ES_EPES)
            return null;

        SignatureTimeStamp sts = signature.getQualifyingProperties().getUnsignedSignatureProperties().getSignatureTimeStamp(0);
        Calendar c  = null;
        try {
            c = sts.getEncapsulatedTimeStamp(0).getTime();
        } catch (Exception x){
            throw new RuntimeException(x);
        }
        return c;
    }        */

    public List<TimestampInfo> getTimestampInfo(TimestampType type) {
//        there can be timestamps even in BES
//        if (getSignatureType() == SignatureType.ES_BES || getSignatureType() == SignatureType.ES_EPES)
//            return Collections.EMPTY_LIST;
        try {
            List<TimestampInfo> all = new ArrayList<TimestampInfo>();
            if(type == TimestampType.CONTENT_TIMESTAMP) {
                SignedDataObjectProperties sdop = signature.getQualifyingProperties().getSignedDataObjectProperties();
                all.addAll(convert(sdop.getAllDataObjectsTimeStamps(), type));
                all.addAll(convert(sdop.getIndividualDataObjectsTimeStamps(), type));
                return all;
            }
            UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
            if(usp == null )
                return Collections.EMPTY_LIST;
            switch (type){
                case SIGNATURE_TIMESTAMP            : all.addAll(convert(usp.getSignatureTimeStamps(), type)); break;
                case SIG_AND_REFERENCES_TIMESTAMP   : all.addAll(convert(usp.getSigAndRefsTimeStamps(), type)); break;
                case REFERENCES_TIMESTAMP           : all.addAll(convert(usp.getRefsOnlyTimeStamps(), type)); break;
                case ARCHIVE_TIMESTAMP              : all.addAll(convert(usp.getArchiveTimeStamps(), type)); break;
                case ARCHIVE_TIMESTAMP_V2           : all.addAll(convert(usp.getArchiveTimeStamps(), type)); break; //todo diff v1 - v2
                //case ARCHIVE_TIMESTAMP_V3         : all.addAll(usp.getSigAndRefsTimeStamps(), type); break;
            }
            return all;
        }
        catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
    }

    private List<TimestampInfo> convert(List<? extends XAdESTimeStamp> timestamps, TimestampType type){
        List<TimestampInfo> all = new ArrayList<TimestampInfo>();
        for (XAdESTimeStamp xts : timestamps){
            for (int i=0; i<xts.getEncapsulatedTimeStampCount(); i++){
                if (xts.getType()!=type)
                    continue;
                TimestampInfo info = new TimestampInfoImpl(type, xts.getEncapsulatedTimeStamp(i));
                all.add(info);
            }
        }
        return all;
    }

    public List<TimestampInfo> getAllTimestampInfos() {
        List<TimestampInfo> all = new ArrayList<TimestampInfo>();
        all.addAll(getTimestampInfo(TimestampType.CONTENT_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.REFERENCES_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP));
        all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V2));
        //all.addAll(getTimestampInfo(TimestampType.ARCHIVE_TIMESTAMP_V3));  todo
        return all;
    }

    public CertValidationReferences getCertValidationReferences() {
        SignatureType type = getSignatureType();
        if (type==SignatureType.ES_BES || type==SignatureType.ES_EPES || type==SignatureType.ES_T)
            return null;
        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        CompleteCertificateRefs ccr = usp.getCompleteCertificateRefs();
        CompleteRevocationRefs crr = usp.getCompleteRevocationRefs();

        List<CertificateSearchCriteria> certsc = new ArrayList<CertificateSearchCriteria>();
        List<CRLSearchCriteria> crlsc = new ArrayList<CRLSearchCriteria>();
        List<OCSPSearchCriteria> ocspsc = new ArrayList<OCSPSearchCriteria>();

        for (int i=0; i<ccr.getCertificateReferenceCount();i++){
            certsc.add(ccr.getCertificateReference(i).toSearchCriteria());
        }
        for (int i=0; i<crr.getCRLReferenceCount();i++){
            crlsc.add(crr.getCRLReference(i).toSearchCriteria());
        }
        for (int i=0; i<crr.getOCSPReferenceCount();i++){
            ocspsc.add(crr.getOCSPReference(i).toSearchCriteria());
        }

        return new CertValidationReferences(certsc, crlsc, ocspsc);
    }

    public CertValidationValues getCertValidationValues() {
        SignatureType type = getSignatureType();
        if (!(type==SignatureType.ES_XL || type==SignatureType.ES_XL_Type1 || type==SignatureType.ES_XL_Type2 || type==SignatureType.ES_A))
            return null;
        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        CertificateValues cv = usp.getCertificateValues();
        RevocationValues rv = usp.getRevocationValues();
        try {
            List<EBasicOCSPResponse> basicOCSPResponses = new ArrayList<EBasicOCSPResponse>();
            for (EOCSPResponse r : rv.getAllOCSPResponses())
                basicOCSPResponses.add(r.getBasicOCSPResponse());
            return new CertValidationValues(cv.getAllCertificates(), rv.getAllCRLs(), basicOCSPResponses);
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    public Signature createCounterSignature(ECertificate certificate)
            throws SignatureException
    {
        XMLSignature counterSignature =  signature.createCounterSignature();
        counterSignature.addKeyInfo(certificate);

        return new SignatureImpl(container, counterSignature, this);
    }

    public List<Signature> getCounterSignatures()
    {
        List<Signature> counterSignatures = new ArrayList<Signature>();
        List<XMLSignature> counterXmlSignatures = signature.getAllCounterSignatures();
        for (XMLSignature xs : counterXmlSignatures){
            Signature cs = new SignatureImpl(container, xs, this);
            counterSignatures.add(cs);
        }

        return counterSignatures;
    }

    public void detachFromParent() throws SignatureException
    {
        try {
            if (parentSignature==null){
                container.detachSignature(this);
            }
            else {
                XMLSignature xmlSignature = (XMLSignature)parentSignature.getUnderlyingObject();
                UnsignedSignatureProperties usp = xmlSignature.getQualifyingProperties().getUnsignedSignatureProperties();
                usp.removeCounterSignature(signature);
            }
        }
        catch (Exception x){
            throw new SignatureException("Cant extract signature form container "+signature.getClass(), x);
        }
    }

    public void addContent(Signable aData, boolean embed) throws SignatureException
    {
        signature.addDocument(new DocumentSignable(aData), null, null, embed);
    }

    public List<Signable> getContents() throws SignatureException
    {
        List<Signable> contents = new ArrayList<Signable>();
        SignedInfo si = signature.getSignedInfo();
        for (int i=0;i<si.getReferenceCount();i++){
            Reference reference = si.getReference(i);
            if (!(reference.getType()!=null && Constants.REFERENCE_TYPE_SIGNED_PROPS.equals(reference.getType()))){
                Document doc = reference.getReferencedDocument();
                SignableDocument sd = new SignableDocument(doc);
                contents.add(sd);
            }
        }
        return contents;
    }

    public Algorithm getSignatureAlg() {
        return signature.getSignatureMethod().getSignatureAlg();
    }

    public void sign(BaseSigner cryptoSigner) throws SignatureException
    {
        signature.sign(cryptoSigner);
    }

    public void upgrade(SignatureType type) throws SignatureException
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

    public SignatureValidationResult verify() throws SignatureException
    {
        tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult vr = signature.verify();
        return (SignatureValidationResult)vr;

    }

    public void addArchiveTimestamp() throws SignatureException
    {
        signature.addArchiveTimeStamp();
    }

    public SignatureType getSignatureType()
    {
        return XMLProviderUtil.convert(signature.getSignatureType(), signature);
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.XAdES;
    }

    public ECertificate getSignerCertificate()
    {
        return signature.getSigningCertificate();
    }

    public SignatureContainer getContainer() {
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

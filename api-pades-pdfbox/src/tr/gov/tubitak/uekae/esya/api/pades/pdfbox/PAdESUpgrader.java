package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSigProviderUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureImpl;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BES;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.CertificateValidationException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PAdESUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PDFBoxUtil;
import tr.gov.tubitak.uekae.esya.api.signature.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PAdESUtil.isPre;
import static tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PAdESUtil.wrap;
import static tr.gov.tubitak.uekae.esya.api.signature.SignatureType.*;

public class PAdESUpgrader
{
    private Logger logger = LoggerFactory.getLogger(PAdESUpgrader.class);

    private PAdESContainer container;
    private PAdESSignature signature;

    public PAdESUpgrader(PAdESContainer container, PAdESSignature signature)
    {
        this.container = container;
        this.signature = signature;
    }

    public boolean upgrade(SignatureType from, SignatureType to) throws SignatureException
    {
        if (!signature.canBeUpgradable())
            throw new NotSupportedException(CMSSignatureI18n.getMsg(E_KEYS.ONLY_LAST_USER_SIGNATURE_CAN_BE_UPGRADED));

        if (!PAdESUtil.isSupported(to)){
            throw new NotSupportedException(""+ to);
        }
        if (!PAdESUtil.isPre(from, to) && to != ES_A){
            throw new NotSupportedException(from +" to "+ to);
        }

        SignatureType currentType = from;

        boolean upgraded = false;
        if (isPre(currentType, ES_T))
        {
            addDocumentTimestamp();
            currentType = ES_T;
            upgraded = true;
        }

        if (to==ES_T)
            return true;

        if (isPre(currentType, ES_XL))
        {
            upgradeT2XL();
            currentType = ES_XL;
            upgraded = true;
        }

        if (to==ES_XL)
            return true;

        // upgrade to ES_A
        if (isPre(currentType, ES_A)){

            //Upgrade edilmek istenen imzadan sonraki imzalara ait doğrulama verilerinin imza içerisine konulması lazım.
            //Eğer daha yeni XL'a upgrade edilmiş ise zaten doğrulama verileri konulmuştur.
            if(!isPre(from, ES_XL)){
                //Bir imzanın upgrade edilmesi için o imzadan sonra gelen imzaların sadece TimeStamp olmasını daha öncesinde kontrol etmiştik.
                //Önceki zaman damgalarına ait doğrulama verilerinin imza içerisinde olması gerekiyor.

                COSDictionary dss = new COSDictionary();
                if(container.getDocumentSecurityStore() != null)
                    dss.addAll(container.getDocumentSecurityStore().getDss());

                List<Signature> signatures = signature.getContainer().getSignatures();

                for(int i = signature.index + 1; i < signatures.size(); i++){
                    PAdESSignature postSignature = (PAdESSignature)signatures.get(i);
                    SignatureType postSignatureType = postSignature.getSignatureType();
                    if(isPre(postSignatureType, SignatureType.ES_XL)) {
                        dss = collectValidationDataAndAddToDSS(postSignature, dss);
                    }
                }

                setDSS(dss);
                addExtensions();

            }
            addDocumentTimestamp();
            currentType = ES_A;
            upgraded = true;
        }

        if (from==ES_A){
            upgradeA2A(signature);
        }

        return upgraded;
    }


    private void upgradeA2A(PAdESSignature signature) throws SignatureException {

        COSDictionary dss = new COSDictionary();
        if(container.getDocumentSecurityStore() != null)
            dss.addAll(container.getDocumentSecurityStore().getDss());

        dss = collectValidationDataAndAddToDSS(getLastSignature(signature), dss);

        setDSS(dss);

        addExtensions();

        addDocumentTimestamp();
    }

    // find last signature to find last temp file...
    private PAdESSignature getLastSignature(PAdESSignature signature){
        List<Signature> signatures = signature.getContainer().getSignatures();
        return (PAdESSignature)signatures.get(signatures.size()-1);
    }


    private void upgradeT2XL() throws SignatureException {

        COSDictionary dss = new COSDictionary();
        if(container.getDocumentSecurityStore() != null)
            dss.addAll(container.getDocumentSecurityStore().getDss());

        for (Signature s : signature.getContainer().getSignatures()){

            PAdESSignature addVerificationFor = (PAdESSignature)s;
            SignatureType signatureType = addVerificationFor.getSignatureType();

            if(isPre(signatureType, SignatureType.ES_XL))
            {
                dss = collectValidationDataAndAddToDSS(addVerificationFor, dss);
            }
            else if(addVerificationFor.hasVRI() == false)
            {
                //Sertifika tarihi hala geçerli ise VRI eklenebilmesi için aşağıdaki işlem yapılıyor. VRI zorunlu olmadığı için
                //başarısız durumlarda exception fırlatılmıyor.
                ECertificate signerCertificate = addVerificationFor.getSignerCertificate();
                if(signerCertificate.getNotAfter().after(Calendar.getInstance()))
                {
                    try
                    {
                        dss = collectValidationDataAndAddToDSS(addVerificationFor, dss);
                    }
                    catch (SignatureException ex)
                    {
                        logger.warn("Can not add verification data for the signature" +  addVerificationFor.getPdfFieldName()  + ". This operation is done for VRI improving, is is NOT obligatory.", ex);
                    }
                }
            }
        }

        setDSS(dss);
        addExtensions();

        container.updatePDF();
    }



    private void setDSS(COSDictionary dss)
    {
        COSDictionary cosDictionary = container.document.getDocumentCatalog().getCOSObject();

        cosDictionary.setItem("DSS", dss);
        cosDictionary.setNeedToBeUpdated(true);
    }

    private String getSignatureHashKey(PAdESSignature signature) throws SignatureException {

        byte[] digestBytes = null;

        try {
            COSDictionary sigDict = container.document.getSignatureDictionaries().get(signature.index).getCOSObject();
            COSString contents = (COSString) sigDict.getDictionaryObject(COSName.CONTENTS);

            byte[] bytes = contents.getBytes();

            if ("ETSI.RFC3161".equals(sigDict.getCOSName(COSName.SUB_FILTER).getName())) {
                EContentInfo info = new EContentInfo(bytes);
                bytes = info.getEncoded();
            }

            digestBytes = DigestUtil.digest(DigestAlg.SHA1, bytes);
        } catch (Exception ex) {
            throw new SignatureException("Problem in getting hash of signature " + signature.pdfFieldName);
        }

        return StringUtil.toHexString(digestBytes);
    }

    private COSDictionary buildVriRecord(String hashInHex, List<ECertificate> certs, List<ECRL> crls, List<EOCSPResponse> ocsps) {

        COSDictionary vriDictionaryItems = new COSDictionary();
        vriDictionaryItems.setDirect(true);

        if(certs != null && !certs.isEmpty())
            vriDictionaryItems.setItem("Cert", PDFBoxUtil.createCOSArray(certs));

        if(ocsps != null && !ocsps.isEmpty())
            vriDictionaryItems.setItem("OCSP", PDFBoxUtil.createCOSArray(ocsps));

        if(crls != null && !crls.isEmpty())
            vriDictionaryItems.setItem("CRL", PDFBoxUtil.createCOSArray(crls));

        return convertToVriRecord(hashInHex, vriDictionaryItems);
    }

    private COSDictionary convertToVriRecord(String hashInHex, COSDictionary sigVriDictionary) {

        COSDictionary vriRecord = new COSDictionary();
        vriRecord.setItem(hashInHex, sigVriDictionary);

        return vriRecord;
    }

    private void addExtensions()
    {
        COSDictionary dssExtensions = new COSDictionary();
        dssExtensions.setDirect(true);

        PDDocumentCatalog catalog = container.document.getDocumentCatalog();
        catalog.getCOSObject().setItem("Extensions", dssExtensions);

        COSDictionary adbeExtension = new COSDictionary();
        adbeExtension.setDirect(true);
        dssExtensions.setItem("ESIC", adbeExtension);

        adbeExtension.setName("BaseVersion", "1.7");
        adbeExtension.setInt("ExtensionLevel", 5);
    }

    public void addDocumentTimestamp() throws SignatureException
    {
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(COSName.getPDFName("ETSI.RFC3161"));
        signature.setType(COSName.DOC_TIME_STAMP);

        signature.setSignDate(Calendar.getInstance());

        TSSignatureInterface tsSignatureInterface = new TSSignatureInterface(container.getContext());
        try
        {
            container.document.addSignature(signature, tsSignatureInterface);
            container.updatePDF();
        }
        catch (IOException e)
        {
            throw new SignatureException("Can not add DocumentTimestamp", e);
        }
    }


    private COSDictionary collectValidationDataAndAddToDSS(PAdESSignature dssFor, COSDictionary dss) throws SignatureException
    {
        ECertificate certificate = dssFor.getSignerCertificate();
        Context c  = dssFor.getContainer().getContext();

        CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);

        CertificateStatusInfo csi = null;
        try {
            csi = finder.validateCertificate(certificate, CMSSigProviderUtil.toSignatureParameters(c), Calendar.getInstance());
        }
        catch (CMSSignatureException x){
            if (dssFor.isTimestamp()){
                csi = finder.validateCertificate(certificate, CMSSigProviderUtil.toSignatureParameters(c), dssFor.getTimeStampTime());
            }
            else if (x instanceof CertificateValidationException){
                csi = ((CertificateValidationException) x).getCertStatusInfo();
            }
        }

        if(csi.getCertificateStatus() != CertificateStatus.VALID){
            throw new SignatureException("Cannot validate certificate: "+csi.getDetailedMessage());
        }

        CertRevocationInfoFinder crif = new CertRevocationInfoFinder(csi);

        List<ECertificate> certs = crif.getCertificates();
        certs = certs.subList(1, certs.size());
        List<ECRL> crls = crif.getCRLs();
        List<EOCSPResponse> ocsps = wrap(crif.getOCSPs());

        Signer signer = ((CMSSignatureImpl)dssFor.getUnderlyingObject()).getSigner();
        List<EAttribute> tsAttrs = signer.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
        if(tsAttrs != null && !tsAttrs.isEmpty()){
            List<CertRevocationInfoFinder.CertRevocationInfo> list = ((BES)signer).findTSCertificateRevocationValues(tsAttrs.get(0), CMSSigProviderUtil.toSignatureParameters(c));
            List<ECertificate> tsCerts = AttributeUtil.getCertificates(list);
            List<ECRL> tsCrls = AttributeUtil.getCRLs(list);
            List<EOCSPResponse> tsOcsps = AttributeUtil.getOCSPResponses(list);

            tsCerts.removeAll(certs);
            tsCrls.removeAll(crls);
            tsOcsps.removeAll(ocsps);

            certs.addAll(tsCerts);
            crls.addAll(tsCrls);
            ocsps.addAll(tsOcsps);
        }

        certs = PAdESUtil.removeDuplicates(certs);
        crls = PAdESUtil.removeDuplicates(crls);
        ocsps = PAdESUtil.removeDuplicates(ocsps);

        String hashInHex = getSignatureHashKey(dssFor);
        COSDictionary vriRecords = buildVriRecord(hashInHex, certs, crls, ocsps);

        DocSecurityStore docSecurityStore = new DocSecurityStore(dss);
        docSecurityStore.updateCertificates(certs);
        docSecurityStore.updateCrls(crls);
        docSecurityStore.updateOCSPs(ocsps);
        docSecurityStore.updateVRIRecords(vriRecords);

        return docSecurityStore.getDss();
    }
}

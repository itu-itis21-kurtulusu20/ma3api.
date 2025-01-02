package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CertificateValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.EncapsulatedOCSPValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.TimeStampValidationData;

import java.util.*;

/**
 * @author ayetgin
 */
public class CertRevInfoExtractor
{

    public Map<String, Object> collectAllInitialValidationDataFromContextAsParams(Context context)
            throws XMLSignatureException
    {
        Map<String, Object> params = new HashMap<String, Object>();

        List<ECertificate> certificates = new ArrayList<ECertificate>();
        List<ECRL> crls = new ArrayList<ECRL>();
        List<EOCSPResponse> ocspResponses = new ArrayList<EOCSPResponse>();

        for (CertValidationData validationData : context.getAllValidationData()) {
            certificates.addAll(validationData.getCertificates());
            crls.addAll(validationData.getCrls());
            ocspResponses.addAll(validationData.getOcspResponses());

            for (TimeStampValidationData vd : validationData.getTSValidationData().values()) {
                if (vd.getCertificateValues() != null) {
                    certificates.addAll(vd.getCertificateValues().getAllCertificates());
                }
                if (vd.getRevocationValues() != null) {
                    crls.addAll(vd.getRevocationValues().getAllCRLs());
                    ocspResponses.addAll(vd.getRevocationValues().getAllOCSPResponses());
                }
            }
        }

        params.put(AllEParameters.P_INITIAL_CERTIFICATES, certificates);
        params.put(AllEParameters.P_INITIAL_CRLS, crls);
        params.put(AllEParameters.P_INITIAL_OCSP_RESPONSES, ocspResponses);

        return params;
    }

    public Pair<CertificateValues, RevocationValues> extractValidationDataFromCSI(Context context, CertificateStatusInfo csi)
            throws XMLSignatureException
    {
        CertificateValues certValues = new CertificateValues(context);
        RevocationValues revValues = new RevocationValues(context);
        //DigestMethod digestMethod = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
        UniqueCertRevInfo ucri = trace(csi);

        int counter = 0;
        for (ECertificate cert: ucri.getCertificates()){
            if (counter != 0)
                certValues.addCertificate(cert);

            counter++;
        }

        for (ECRL crl : ucri.getCrls()){
            revValues.addCRL(crl);
        }

        for (EOCSPResponse ocsp : ucri.getOcspResponses()){
            revValues.addOCSPResponse(ocsp);
        }

        return new Pair<CertificateValues, RevocationValues>(certValues, revValues);
    }

    public Pair<CertificateValues, RevocationValues> removeDuplicateReferences(Context context, XMLSignature signature, CertificateValues certValues, RevocationValues revValues, ESignedData data)
            throws XMLSignatureException
    {
        CertificateValues resultingCerts = new CertificateValues(context);
        RevocationValues resultingRevs = new RevocationValues(context);

        List<ECertificate> certsInTS = data.getCertificates();
        List<ECertificate> certsExisting = context.getValidationData(signature).getCertificates();

        RevocationValues revocationValues = signature.getQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties().getRevocationValues();
        CertificateValues certificateValues = signature.getQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties().getCertificateValues();

        for (int i=0; i<certValues.getCertificateCount();i++){

            ECertificate cert = certValues.getCertificate(i);

            if ((certsInTS!=null && certsInTS.contains(cert)) || certsExisting.contains(cert))
                continue;

            List<ECertificate> allCerts = new ArrayList<ECertificate>();
            if(certificateValues != null)
                allCerts = certificateValues.getAllCertificates();

            boolean exists = false;

            for(int j=0; j < allCerts.size(); j++){
                if(allCerts.get(j).equals(cert)) {
                    exists = true;
                    break;
                }

            }
            if (!exists)
                resultingCerts.addCertificate(cert);
        }

        List<ECRL> crlsInTS = data.getCRLs();
        List<ECRL> crlsExisting = context.getValidationData(signature).getCrls();

        for (int i=0; i<revValues.getCRLValueCount();i++){

            ECRL crl = revValues.getCRL(i);

            if ((crlsInTS!=null && crlsInTS.contains(crl)) || crlsExisting.contains(crl))
                continue;

            boolean exists = false;

            List<ECRL> allCRLs = new ArrayList<ECRL>();
            if(revocationValues != null)
                allCRLs = revocationValues.getAllCRLs();

            for(int j=0; j < allCRLs.size(); j++){
                if(allCRLs.get(j).equals(crl)) {
                    exists = true;
                    break;
                }

            }
            if (!exists)
                resultingRevs.addCRL(crl);
        }
        for (int i=0; i< revValues.getOCSPValueCount(); i++){
            EncapsulatedOCSPValue encapsulatedOCSPValue = revValues.getOCSPValue(i);
            resultingRevs.addOCSPValue(encapsulatedOCSPValue);
        }


        return new Pair<CertificateValues, RevocationValues>(resultingCerts, resultingRevs);
    }



    /**
     * @return first signatures info!
     */
    public UniqueCertRevInfo trace(SignedDataValidationResult aSDVR)
    {
        if (aSDVR==null)
            throw new XMLSignatureRuntimeException("CMS validation result is null");

        CertificateStatusInfo csi = null;

        for (tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult result : aSDVR.getSDValidationResults()){
            if (result.getCertStatusInfo()!=null) {
                csi = result.getCertStatusInfo();
                return trace(csi);
            }
        }
        return null;
    }

    public UniqueCertRevInfo trace(CertificateStatusInfo csi){
        UniqueCertRevInfo info = new UniqueCertRevInfo();
        _traceResources(csi, info);
        return info;
    }

    private void _traceResources(CertificateStatusInfo csi, UniqueCertRevInfo certRevInfo){
        ECertificate cer = csi.getCertificate();

        List<CertificateStatusInfo> iptalZinciri = new ArrayList<CertificateStatusInfo>();

        CertificateStatusInfo cerStatus = csi;

        while(!certRevInfo.getCertificates().contains(cer))
        {
            certRevInfo.add(cer);

            for(CRLStatusInfo crlStatus : cerStatus.getCRLInfoList())
            {
                if(crlStatus.getCRLStatus() == CRLStatus.VALID)
                {
                    certRevInfo.add(crlStatus.getCRL());
                    iptalZinciri.add(crlStatus.getSigningCertficateInfo());
                }
            }

            for(CRLStatusInfo deltaCrlStatus : cerStatus.getDeltaCRLInfoList())
            {
                if(deltaCrlStatus.getCRLStatus() == CRLStatus.VALID)
                {
                    certRevInfo.add(deltaCrlStatus.getCRL());
                    iptalZinciri.add(deltaCrlStatus.getSigningCertficateInfo());
                }
            }

            for(OCSPResponseStatusInfo ocspStatus : cerStatus.getOCSPResponseInfoList())
            {
                if (ocspStatus.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                {
                    certRevInfo.add(ocspStatus.getOCSPResponse());
                    iptalZinciri.add(ocspStatus.getSigningCertficateInfo());
                }
            }

            //TODO sil de ocsp de null gelirse kontrol et

            cerStatus = cerStatus.getSigningCertficateInfo();
            if (cerStatus!=null)
                cer = cerStatus.getCertificate();
            else
                break;
        }

        for(CertificateStatusInfo current : iptalZinciri)
        {
            _traceResources(current, certRevInfo);
        }

    }
}

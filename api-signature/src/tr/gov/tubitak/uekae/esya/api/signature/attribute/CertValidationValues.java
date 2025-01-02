package tr.gov.tubitak.uekae.esya.api.signature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class CertValidationValues {

    List<ECertificate> certificates;
    List<ECRL> crls ;
    List<EBasicOCSPResponse> ocspResponses;

    public CertValidationValues(List<ECertificate> certificates, List<ECRL> crls, List<EBasicOCSPResponse> ocspResponses) {
        this.certificates = certificates;
        this.crls = crls;
        this.ocspResponses = ocspResponses;
    }

    public CertValidationValues(){
        this.certificates = new ArrayList<ECertificate>();
        this.crls = new ArrayList<ECRL>();
        this.ocspResponses = new ArrayList<EBasicOCSPResponse>();
    }

    public List<ECertificate> getCertificates() {
        return certificates;
    }

    public List<ECRL> getCrls() {
        return crls;
    }

    public List<EBasicOCSPResponse> getOcspResponses() {
        return ocspResponses;
    }

    public void addCertificate(ECertificate cert)
    {
        if(!certificates.contains(cert))
            certificates.add(cert);
    }

    public void addCRL(ECRL crl)
    {
        if(!crls.contains(crl))
            crls.add(crl);
    }

    public void addOCSPResponse(EBasicOCSPResponse ocsp)
    {
        if(!ocspResponses.contains(ocsp))
            ocspResponses.add(ocsp);
    }
}

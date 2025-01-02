package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.ArrayList;
import java.util.List;

/**
* @author ayetgin
*/
public class UniqueCertRevInfo
{
    private List<ECertificate> certificates = new ArrayList<ECertificate>();
    private List<ECRL> crls = new ArrayList<ECRL>();
    private List<EOCSPResponse> ocspResponses = new ArrayList<EOCSPResponse>();

    public void add(ECertificate cert){
        if (!certificates.contains(cert))
            certificates.add(cert);

    }
    public void add(ECRL crl){
        if (!crls.contains(crl))
            crls.add(crl);
    }
    public void add(EOCSPResponse ocspResponse){
        if (!ocspResponses.contains(ocspResponse))
            ocspResponses.add(ocspResponse);
    }


    public List<ECertificate> getCertificates()
    {
        return certificates;
    }

    public List<ECRL> getCrls()
    {
        return crls;
    }

    public List<EOCSPResponse> getOcspResponses()
    {
        return ocspResponses;
    }
}

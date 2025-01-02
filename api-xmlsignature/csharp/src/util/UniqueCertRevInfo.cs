using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
/**
* @author ayetgin
*/
public class UniqueCertRevInfo
{
    private readonly List<ECertificate> certificates = new List<ECertificate>();
    private readonly List<ECRL> crls = new List<ECRL>();
    private readonly List<EOCSPResponse> ocspResponses = new List<EOCSPResponse>();

    public void add(ECertificate cert){
        if (!certificates.Contains(cert))
            certificates.Add(cert);

    }
    public void add(ECRL crl){
        if (!crls.Contains(crl))
            crls.Add(crl);
    }
    public void add(EOCSPResponse ocspResponse){
        if (!ocspResponses.Contains(ocspResponse))
            ocspResponses.Add(ocspResponse);
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
}
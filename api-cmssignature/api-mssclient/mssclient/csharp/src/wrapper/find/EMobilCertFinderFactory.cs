using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find
{
    public static class EMobilCertFinderFactory {
        public static ICertificateFinder GetCertFinder(String certIssuerName)
        {
            EMSSPClientUtil.CheckLicense();
            ICertificateFinder retFinder=null;
            if(certIssuerName.Contains("e-Guven Mobil") ||  certIssuerName.Contains("Elektronik Bilgi Guvenligi A.S.") ){
                retFinder = new EGuvenCertificateFinder();
            } else if(certIssuerName.Contains("KamuSM Mobil"))
            {
                retFinder = new KamuSmCertificateFinder();
            }
            else {
                throw  new Exception("Unknown Mobile Issuer");
            }
            return retFinder;
        }
    }
}

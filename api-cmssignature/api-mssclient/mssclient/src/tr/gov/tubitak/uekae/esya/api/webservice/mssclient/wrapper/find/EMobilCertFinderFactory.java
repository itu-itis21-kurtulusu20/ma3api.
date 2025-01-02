package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 */
public class EMobilCertFinderFactory {
    public static ICertificateFinder getCertFinder(String certIssuerName) throws ESYAException {
        ICertificateFinder retFinder=null;
        if(certIssuerName.contains("e-Guven Mobil") || certIssuerName.contains("Elektronik Bilgi Guvenligi A.S.")){
            retFinder = new EGuvenCertificateFinder();
        } else if(certIssuerName.contains("KamuSM Mobil"))
        {
            retFinder = new KamuSmCertificateFinder();
        }
        else {
            throw new ESYAException("Unknown Mobile Issuer");
        }
        return retFinder;
    }
}

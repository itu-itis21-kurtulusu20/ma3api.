package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Matches a CRL and a certificate according to the Authority Key Identifier
 * (AKI) extensions of both the crl and the certificate. 
 */
public class CRLKeyIDMatcher extends CRLMatcher
{
    private static Logger logger = LoggerFactory.getLogger(CRLKeyIDMatcher.class);

    /**
    * Sertifika-SİL AuthorityKeyIdentifier uzantı eşleştirmesini yapar
    */
    protected boolean _matchCRL(ECertificate aCertificate, ECRL aCRL)
    {
	    if (aCertificate.hasIndirectCRL())
		    return true; // Indirect CRL var. bu şekilde match edemeyiz.

    	//If issuer and crl distributor is different, Do not control.
    	if(! aCertificate.getCRLIssuer().equals(aCertificate.getIssuer()))
    		return true;
        
        EAuthorityKeyIdentifier sertifikaAki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
        if (sertifikaAki == null)
        {
            logger.debug("Cant obtain AKI from certificate");
            return true;
        }

        EAuthorityKeyIdentifier silAki = aCRL.getCRLExtensions().getAuthorityKeyIdentifier();
        if (silAki == null)
        {
            logger.debug("Cant obtain AKI from CRL");
            return true;
        }
        return sertifikaAki.equals(silAki);
    }

}

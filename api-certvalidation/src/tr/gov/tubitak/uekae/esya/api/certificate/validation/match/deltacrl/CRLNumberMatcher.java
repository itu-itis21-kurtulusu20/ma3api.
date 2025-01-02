package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;

import java.math.BigInteger;

/**
 * Matches a delta-CRL and a complete crl according to the CRLNumber extensions
 * of both. The CRLNumber of the complete CRL must be smaller than that of the
 * delta-CRL. 
 */
public class CRLNumberMatcher extends DeltaCRLMatcher
{
    private static final Logger logger = LoggerFactory.getLogger(CRLNumberMatcher.class);

    /**
     * Verilen Base SİL ile delta-SİL'i crlNumber eklentilerine bakarak eşleştirir
     */
    protected boolean _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
    {
        try {
            EExtension crlNumberOfCRL = aCRL.getCRLExtensions().getCRLNumber();
            EExtension crlNumberOfDeltaCRL = aDeltaCRL.getCRLExtensions().getCRLNumber();
            if (crlNumberOfCRL == null){
                logger.debug("Cant find CRL number extension in CRL");
                return true;
            }
            if (crlNumberOfDeltaCRL == null){
                logger.debug("Cant find CRL number extension in delta CRL");
                return true;
            }

            byte[] crlByte = crlNumberOfCRL.getValue();
            byte[] crlDeltaByte = crlNumberOfDeltaCRL.getValue();

            BigInteger crlBigInt = new BigInteger(crlByte);
            BigInteger deltaCrlBigInt = new BigInteger(crlDeltaByte);

            return (crlBigInt.compareTo(deltaCrlBigInt) == -1);
            //return (new BigInteger(crlNumberOfCRL.getValue()).compareTo(new BigInteger(crlNumberOfDeltaCRL.getValue())) == -1);//(2) less than

        }
        catch (Exception exc) {
            logger.debug("Silden alınan CRLNumber decode edilemedi");
            return false;
        }

    }

}

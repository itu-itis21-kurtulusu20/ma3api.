package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;

import java.math.BigInteger;

/**
 * Matches a base CRL and a delta-CRL according to the following criteria stated
 * in RFC 5280;
 *
 * The CRL number of the complete CRL is equal to or greater than the
 * BaseCRLNumber specified in the delta CRL. That is, the complete CRL contains
 * (at a minimum) all the revocation information held by the referenced base
 * CRL.
 *
 * @author IH
 */
public class BaseCRLNumberMatcher extends DeltaCRLMatcher
{
    /**
     * Verilen Base SİL'deki CRLNumber ile ile delta-SİL'deki deltaCRLIndicator
     * eklentilerini eşleştirir
     */
    protected boolean _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
    {
        BigInteger crlNumber = aCRL.getCRLNumber();

        if (crlNumber==null){
            LOGGER.debug("CRL number extension cant found in base CRL");
            return true;
        }

        // delta silimizin delta CRL indicator uzantısını alalım
        EExtension crlIndicatorExt = aDeltaCRL.getCRLExtensions().getDeltaCRLIndicator();
        if (crlIndicatorExt==null){
            LOGGER.debug("CRL number extension cant found in delta CRL");
            return true;
        }
        Asn1BigInteger baseCrlNumber = new Asn1BigInteger();

        try {
            baseCrlNumber.decode(crlIndicatorExt.getValueAsDecodeBuffer());
        }
        catch (Exception aEx) {
            LOGGER.error("Cant decode CRL number extension in delta CRL", aEx);
            return true;
        }

        return crlNumber.compareTo(baseCrlNumber.value) > -1 ;
    }

}

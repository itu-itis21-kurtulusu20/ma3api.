using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl
{
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
    public class BaseCRLNumberMatcher : DeltaCRLMatcher
    {
        /**
         * Verilen Base SİL'deki CRLNumber ile ile delta-SİL'deki deltaCRLIndicator
         * eklentilerini eşleştirir
         */
        protected override bool _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
        {
            BigInteger crlNumber = aCRL.getCRLNumber();

            if (crlNumber == null)
            {
                LOGGER.Debug("CRL number extension cant found in base CRL");
                return true;
            }

            // delta silimizin delta CRL indicator uzantısını alalım
            // eskiden burasi true donuyordu ama delta sil'de bu uzantinin olmamasi hayra
            // alamet olmadigi icin duzelttik, yoksa sil'leri de delta zannediyordu
            // The delta CRL indicator is a critical CRL extension that identifies a
            // CRL as being a delta CRL.
            EExtension crlIndicatorExt = aDeltaCRL.getCRLExtensions().getDeltaCRLIndicator();
            if (crlIndicatorExt == null)
            {
                LOGGER.Debug("CRL number extension cant found in delta CRL");
                return false;
            }
            Asn1BigInteger baseCrlNumber = new Asn1BigInteger();

            try
            {
                baseCrlNumber.Decode(crlIndicatorExt.getValueAsDecodeBuffer());
            }
            catch (Exception aEx)
            {
                LOGGER.Error("Delta Sil Base CRL number extension deceode edilemedi", aEx);
                return false;
            }

            //System.Numerics.BigInteger baseCrlNumber_ = new System.Numerics.BigInteger(baseCrlNumber.mValue.GetData());
            return baseCrlNumber.mValue.Equals(crlNumber);
            //return crlNumber.Value.CompareTo(baseCrlNumber_) > -1;
        }
    }
}

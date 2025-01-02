using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl
{
    /**
     * Matches a delta-CRL and a complete crl according to the CRLNumber extensions
     * of both. The CRLNumber of the complete CRL must be smaller than that of the
     * delta-CRL. 
     */
    public class CRLNumberMatcher : DeltaCRLMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Verilen Base SİL ile delta-SİL'i crlNumber eklentilerine bakarak eşleştirir
         */
        protected override bool _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
        {
            try
            {
                EExtension crlNumberOfCRL = aCRL.getCRLExtensions().getCRLNumber();
                EExtension crlNumberOfDeltaCRL = aDeltaCRL.getCRLExtensions().getCRLNumber();
                if (crlNumberOfCRL == null)
                {
                    logger.Debug("Cant find CRL number extension in CRL");
                    return true;
                }
                if (crlNumberOfDeltaCRL == null)
                {
                    logger.Debug("Cant find CRL number extension in delta CRL");
                    return true;
                }

                byte[] crlByte = crlNumberOfCRL.getValue();
                byte[] crlDeltaByte = crlNumberOfDeltaCRL.getValue();

                BigInteger crlBigInt = new BigInteger(crlByte, 1);
                BigInteger deltaCrlBigInt = new BigInteger(crlDeltaByte, 1);

                // .net 3.5 uyumlulugu dahilinde BigInteger sinifi kaldirilmis, yerine Asn'ye ait
                // BigInteger sinifi kullanilmisti, ama onda da comparison yapilamiyordu, zaten
                // comparison yapilmasi gerekirken equivalence kontrol edilmis eskiden hatali olarak,
                // ben de String uzerinden sayilari compare eden bir kod yazdim, suleyman - 9/18/13
                
                // assumptions:
                // * crl numaralari her zaman pozitiftir
                // * baslarinda anlamsiz olarak fazladan sifir yoktur

                // *** //

                String crlString = crlBigInt.ToString();
                String deltaCrlString = deltaCrlBigInt.ToString();

                // eger crl numarasi uzunlugu delta crl numarasindan kucukse sayi olarak da ondan kucuktur
                if (crlString.Length < deltaCrlString.Length)
                    return true;

                // aksi ise crl daha buyuktur
                if (crlString.Length > deltaCrlString.Length)
                    return false;

                // eger boylari esit ise 
                if (crlString.Length == deltaCrlString.Length)
                {
                    int result = String.CompareOrdinal(crlString, deltaCrlString);

                    // crl ordinal olarak daha kucukse
                    if (result < 0)
                        return true;
                    else
                        return false;
                }

                return false;

                // *** //

                //return crlBigInt.Equals(deltaCrlBigInt);

                //return (new BigInteger(crlNumberOfCRL.getValue()).CompareTo(new BigInteger(crlNumberOfDeltaCRL.getValue())) == -1);//(2) less than

            }
            catch (Exception exc)
            {
                logger.Debug("Silden alınan CRLNumber decode edilemedi");
                return false;
            }

        }
    }
}

using log4net;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross
{
    /**
     * Matches the certificate and its cross certificate by comparing their
     * PublicKeys. 
     */
    public class PublicKeyMatcher: CrossCertificateMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sertifika ile Çapraz sertifika arasında açık anahtarlarına bakarak eşleştirme  yapar
         */
        protected override bool _matchCrossCertificate(ECertificate aSertifika, ECertificate aCaprazSertifika)
        {
            ESubjectPublicKeyInfo sertifikaPublicKey = aSertifika.getSubjectPublicKeyInfo();
            ESubjectPublicKeyInfo caprazPublicKey = aCaprazSertifika.getSubjectPublicKeyInfo();

            return sertifikaPublicKey.Equals(caprazPublicKey);
        }

        /*
        boolean _imzaDogrula(ECertificate aUstSertifika, ECertificate aSertifika)
        {
            boolean sonuc = false;
            byte[] imzalanan;
            byte[] publickey;
            byte[] imzali = aSertifika.getSignature().getData();
            byte[] key;

            if (imzali.length() == 0) {
                logger.debug("Sertifika imza değeri null");
                return false;
            }

            imzalanan = aSertifika.getTBSCertificate().getEncodedBytes();

            try {
                key = aUstSertifika.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes();
                if (key.length() == 0) {
                    logger.debug("Sertifika public key alanı null.");
                    return false;
                }
            }
            catch (Exception aEx) {
                if (logger.isDebugEnabled())
                    logger.debug("Sertifika public key değeri alınırken hata oluştu. : ", aEx);
                return false;
            }

            try {
                AcikAnahtarBilgisi pubKey
                (key);

                AlgorithmIdentifier signatureAlgorithm = aSertifika.getTBSCertificate().signature;

                AlgDetay ad = AlgoritmaBilgileri.getAlgDetay(signatureAlgorithm);
                AlgDetay ozet_ad = (SimetrikAlgInfo(), AsimetrikAlgInfo(), ModAlgInfo(), ad.getOzetAlgInfo(), MesDogKoduAlgInfo())
                ;
                AlgDetay asim_ad = (SimetrikAlgInfo(), ad.getAsimetrikAlgInfo(), ModAlgInfo(), OzetAlgInfo(), MesDogKoduAlgInfo())
                ;

                if (!AlgoritmaBilgileri.getAlgIDListesi().contains(ozet_ad.toString())) {
                    throw new Exception("Bilinmeyen ozet algoritmasi");
                }

                if (!AlgoritmaBilgileri.getAlgIDListesi().contains(asim_ad.toString()))
                {
                    throw new Exception("Bilinmeyen imza algoritmasi");
                }


                AlgorithmIdentifier ozetAlg = AlgoritmaBilgileri::
                getAlgIDListesi()[ozet_ad.toString()];
                AlgorithmIdentifier imzaAlg = AlgoritmaBilgileri::
                getAlgIDListesi()[asim_ad.toString()];


                sonuc = KriptoUtils.imzaDogrula(pubKey, imzaAlg, ozetAlg, imzalanan, imzali);
            }
            catch (Exception aEx) {
                if (logger.isDebugEnabled())
                    logger.debug("Sertifika imzası doğrulanırken hata oluştu. : ", aEx);
                return false;
            }

            return sonuc;
        }             */
    }
}

//using tr.gov.tubitak.uekae.esya.genel.smartkart.sunpkcs11;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    /**
 * Depoya eklenecek sertifikalari Kartta imzalama islemini yapar.
 * @author isilh
 *
 */
    //todo Annotation!
    //@ApiClass
    class CertStoreSigner
    {/*
        private String mIniDosya = "depo.ini";
        private CardType mKartTipi;
        private String mParola = "";
        private String mAnahtarAdi = "";
        private int mSlot = -1;

        public CertStoreSigner()
        {
            try
            {
                LicenseValidator.getInstance().checkLicenceDates(LicenseValidator.Products.API_KALICI);
            }
            catch (LicenseException ex)
            {
                throw new ESYAException("Lisans kontrolu basarisiz.");
            }
            IniFile ini = null;// new IniFile();
            try
            {
                //ini.loadIni(mIniDosya);
                new IniFile(mIniDosya);
            }
            catch (Exception aEx)
            {
                //aEx.printStackTrace();
                Console.WriteLine(aEx.StackTrace);
                throw new ESYAException("Ini dosyası okunamadı:" + mIniDosya, aEx);
            }
            String dll = ini.ParseFileReadValue("kart_dll");
            mKartTipi = CardType.getCardType(dll);
            mParola = ini.ParseFileReadValue("kart_parola");
            mAnahtarAdi = ini.ParseFileReadValue("anahtar_adi");
            mSlot = int.Parse(ini.ParseFileReadValue("kart_slot"));
        }

        public void sign(String aImzalanacakDosya, String aSonucDosyasi)
        {
            DepoASNKokSertifikalar kokler = new DepoASNKokSertifikalar();
            try
            {
                AsnIO.dosyadanOKU(kokler, aImzalanacakDosya);
            }
            catch (Exception aEx)
            {
                //aEx.printStackTrace();
                Console.WriteLine(aEx.StackTrace);
                throw new ESYAException("Dosyadan sertifikalar okunurken hata oluştu", aEx);
            }

            DepoASNImzalar imzalar = new DepoASNImzalar(kokler.elements.Length);

            byte[] pk = null;
            try
            {
                SmartOp op = new SmartOp(mSlot, mKartTipi, mParola);
                PublicKey key = new PublicKey(op.getPublicKey(mAnahtarAdi, AsymmetricAlg.RSA.getName()));
                pk = key.getEncoded();
            }
            catch (Exception aEx)
            {
                //aEx.printStackTrace();
                Console.WriteLine(aEx.StackTrace);
                throw new ESYAException("Karttan açık anahtar okunurken hata oluştu", aEx);
            }

            byte[] pkhash = DigestUtil.digest(DigestAlg.SHA1, pk);

            for (int i = 0; i < kokler.elements.Length; i++)
            {
                DepoASNImza imza = new DepoASNImza();

                //imzalanan
                DepoASNKokSertifika imzalanacak = kokler.elements[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                try
                {
                    imzalanacak.Encode(encBuf);
                }
                catch (Asn1Exception aEx)
                {
                    Console.WriteLine(aEx.StackTrace);
                    throw new ESYAException("Dosyadan okunan sertifika encode edilirken hata oluştu", aEx);
                }
                imza.imzalanan = imzalanacak;

                //rawimza
                DepoASNRawImza raw = new DepoASNRawImza();
                raw.publicKeyHash = new Asn1OctetString(pkhash);
                byte[] imzali = null;

                try
                {
                    SmartOp op = new SmartOp(mSlot, mKartTipi, mParola);                    
                    op.sign(mAnahtarAdi, encBuf.MsgCopy, SignatureAlg.RSA_SHA1.getName());
                }
                catch (Exception aEx)
                {
                    Console.WriteLine(aEx.StackTrace);
                    throw new ESYAException("İmzalama işleminde hata oluştu", aEx);
                }
                if (imzali == null)
                {
                    throw new ESYAException("İmzalama işlemi sonucu null");
                }
                raw.imza = new Asn1OctetString(imzali);

                imza.imza = raw;
                imzalar.elements[i] = imza;
            }

            try
            {
                AsnIO.dosyayaz(imzalar, aSonucDosyasi);
            }
            catch (Exception aEx)
            {
                Console.WriteLine(aEx.StackTrace);
                throw new ESYAException("İmzali dosya yazılırken hata oluştu", aEx);
            }

            //System.out.println("İmzalama işlemi başarıyla tamamlandı");
            Console.WriteLine("İmzalama işlemi başarıyla tamamlandı");
        }



        */
    }
}

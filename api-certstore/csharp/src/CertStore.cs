using System;
using System.Collections.Generic;
using System.Data.Common;
using System.IO;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    
    //todo Annotation!
    //@ApiClass
    public class CertStore
    {                     
        private readonly string DEFAULT_DEPO_DOSYA_ADI = "SertifikaDeposu.svt";
        private readonly string DEFAULT_GUNCELLEME_DOSYA_ADI = "Guncelle.svt";
        private readonly string DEFAULT_IMZALI_KOKLER_DOSYA_ADI = "imzalidosya.dat";
        private readonly string DEPO_DIZIN_ADI = ".sertifikadeposu";
        private readonly string DEPO_DOGRULAMA_STR = "DepoParolasiBelirlenmistir";

        public static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        //private static final byte[] DEPO_PAROLA_SALT = {45,23,-12,34,16,124,56,-40,54,-10,11,42,34,-23,3,41,8,27,32,6,-12,18,-4,27,76,57,43,2,0,6,-3,10};
        private static readonly int DEPO_PAROLA_ITERATION_COUNT = 2000;

        private readonly string _mDepoDosyaAdi;

        private DbConnection _mConn;

        [DllImport("shell32.dll", CharSet = CharSet.Auto)]
        private static extern int SHGetSpecialFolderPath(IntPtr hwndOwner,StringBuilder lpszPath, int nFolder, bool fCreate);


        /*
         * Kullanicinin home dizininde .sertifikadeposu dizini altinda SertifikaDeposu.svt dosyasi varsa,bu depo dosyasi
         * uzerinde islem yapar. Yoksa,bu dosyayi olusturarak gerekli tablolari yaratir.Guncelleme dosyasi olarak,SertifikaDeposu.svt
         * dosyasi ile ayni yerde Guncelleme.svt dosyasini arar,varsa bu dosyadaki guncellemeleri SertifikaDeposu.svt ye aktarir.Aktarim
         * tamamlandiktan sonra Guncelleme.svt dosyasini siler.
         * @throws CertStoreException
         */
        public CertStore()
        {
            CertStoreUtil.CheckLicense();
            const int MAX_PATH = 260;
            StringBuilder userFolder = new StringBuilder(MAX_PATH);

            const int CSIDL_PROFILE = 40;

            SHGetSpecialFolderPath(IntPtr.Zero, userFolder, CSIDL_PROFILE, false);
            string folder = userFolder.ToString() + Path.DirectorySeparatorChar + DEPO_DIZIN_ADI + Path.DirectorySeparatorChar;

            _mDepoDosyaAdi = folder + DEFAULT_DEPO_DOSYA_ADI;

            var mGuncellemeDosyaAdi = folder + DEFAULT_GUNCELLEME_DOSYA_ADI;
            var mYuklemeDosyaAdi = folder + DEFAULT_IMZALI_KOKLER_DOSYA_ADI;

            try
            {
                SetCertStoreConnAndInitialize(_mDepoDosyaAdi);

                _depoGuncelle(mGuncellemeDosyaAdi);
                _depoYukle(mYuklemeDosyaAdi);

            }
            catch (Exception ex)
            {
                throw new CertStoreException("Sertifika Deposunu ilklendirirken hata oluştu", ex);
            }

        }

        /*
         * aDepoDosyaAdi adresli dosya varsa,islemleri bu dosya uzerinde yapar.Yoksa bu dosyayi dizin yapisiyla beraber olusturur 
         * ve gerekli tablolari yaratir.aGuncellemeDosyaAdi adresli dosya varsa,bu dosyadan depo dosyasina aktarim gerceklestirilir.
         * Aktarimdan sonra guncelleme dosyasi silinmez. 
         * @param aDepoDosyaAdi Depo olarak kullanilacak dosyasinin pathi
         * @param aGuncellemeDosyaAdi Depoya yapilacak guncellemeleri iceren dosyanin pathi
         * @throws CertStoreException
         */

        public CertStore(String aDepoDosyaAdi, String aGuncellemeDosyaAdi, String aYuklemeDosyaAdi)
        {
            CertStoreUtil.CheckLicense();

            _mDepoDosyaAdi = aDepoDosyaAdi;

            try
            {
                SetCertStoreConnAndInitialize(_mDepoDosyaAdi);

                _depoGuncelle(aGuncellemeDosyaAdi);

                _depoYukle(aYuklemeDosyaAdi);

            }
            catch (Exception ex)
            {
                throw new CertStoreException("Sertifika Deposunu ilklendirirken hata oluştu", ex);
            }

        }


        public String getStoreName()
        {
            return _mDepoDosyaAdi;
        }


        private void SetCertStoreConnAndInitialize(string certStorFile)
        {
            string directory = Path.GetDirectoryName(certStorFile);
            if (directory == null)
            {
                string mesaj = "Belirtilen depo dosyasının " + certStorFile + " dizini geçerli değil";
                logger.Error(mesaj);
                throw new CertStoreException(mesaj);
            }

            if (!File.Exists(certStorFile))
            {
                if (!Directory.Exists(directory))
                    Directory.CreateDirectory(directory);
                _mConn = CertStoreDBLayer.newConnection(certStorFile);
                SorgulariCalistir();
            }
            else
            {
                _mConn = CertStoreDBLayer.newConnection(certStorFile);
            }
        }

        private void _depoGuncelle(string aGuncellemeDosyaAdi)
        {
            if (aGuncellemeDosyaAdi == null || !File.Exists(aGuncellemeDosyaAdi)) return;

            List<DepoKokSertifika> guncellemeList = null;
            List<DepoSilinecekKokSertifika> silinecekList = null;

            using (DbConnection connection = CertStoreDBLayer.newConnection(aGuncellemeDosyaAdi))
            {
                try
                {
                    DepoVEN venG = CertStoreDBLayer.newDepoVEN(connection);
                    guncellemeList = ((RsItemSource<DepoKokSertifika>) venG.kokSertifikaListele()).toList();
                    silinecekList = ((RsItemSource<DepoSilinecekKokSertifika>) venG.silinecekKokSertifikaListele()).toList();
                }
                catch (Exception aEx)
                {
                    throw new CertStoreException("Güncelleme dosyasından " + aGuncellemeDosyaAdi + " okuma yapılırken veritabanı hatası oluştu.", aEx);
                }
            }

            using (DbTransaction transaction = _mConn.BeginTransaction())
            {
                try
                {
                    DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);

                    foreach (DepoKokSertifika kok in guncellemeList)
                    {
                        try
                        {
                            var ikili = _getSerialANDIssuer(kok.getValue());
                            string issuer = ikili.first();
                            string serial = ikili.second();
                            try
                            {
                                byte[] ozet = DigestUtil.digest(DigestAlg.SHA1, kok.getValue());
                                var depokok = ven.kokSertifikaHasheGoreBul(ozet);
                                if (depokok.getKokGuvenSeviyesi().getIntValue() <= kok.getKokGuvenSeviyesi().getIntValue())
                                    continue;
                            }
                            catch (Exception aEx)
                            {
                                String mesaj = issuer + "issuer isimli "+ serial +" seri numaralı kökün imzası doğrulanamadı";
                                logger.Info(mesaj, aEx);
                            }
                            if (CertStoreUtil.verifyDepoKokSertifika(kok))
                            {
                                if (kok.getKokGuvenSeviyesi().Equals(SecurityLevel.PERSONAL))
                                {
                                    logger.Warn("Issuer i " + issuer + " olan ve seri numarası " + serial + " olan kişisel kök sertifika, sertifika deponuza güncelleme dosyasından eklenmektedir.Dikkat!");
                                }
                                kok.setKokSertifikaNo(0);
                                List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(kok.getValue(), OzneTipi.KOK_SERTIFIKA);
                                ven.kokSertifikaYaz(kok, ozetler);
                            }
                            else
                            {
                                String mesaj = issuer + " issuer isimli " + serial + " seri numaralı kökun imzası doğrulanamadı";
                                logger.Info(mesaj);
                            }
                        }
                        catch (Exception aEx)
                        {
                            try
                            {
                                var ikili = _getSerialANDIssuer(kok.getValue());
                                string mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numaralı kökun güncelleme dosyasından aktarımı sırasında hata oluştu";
                                logger.Info(mesaj, aEx);
                            }
                            catch (Exception bEx)
                            {
                                String mesaj = kok.getKokSertifikaNo() + " nolu kok sertifikanin guncelleme dosyasindan aktarimi sirasinda hata olustu\n" + bEx.Message;
                                logger.Info(mesaj, bEx);
                            }
                        }

                    }

                    foreach (DepoSilinecekKokSertifika silinecek in silinecekList)
                    {
                        try
                        {
                            /*KeyValue*/
                            Pair<String, String> ikili = _getSerialANDIssuer(silinecek.getValue());
                            try
                            {
                                ven.kokSertifikaValueYaGoreBul(silinecek.getValue());
                            }
                            catch (Exception aEx)
                            {
                                string mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numaralı silinecek kök sertifika veritabanında bulunamadı";
                                logger.Info(mesaj, aEx);
                                continue;
                            }

                            if (CertStoreUtil.verifyDepoSilinecekKokSertifika(silinecek))
                            {
                                ven.kokSertifikaSil(silinecek.getKokSertifikaNo());
                            }
                            else
                            {
                                String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numaralı silinecek kök sertifikanın imzası doğrulanamadı";
                                logger.Info(mesaj);
                            }
                        }
                        catch (Exception aEx)
                        {
                            try
                            {
                                /*KeyValue*/
                                Pair<String, String> ikili = _getSerialANDIssuer(silinecek.getValue());
                                String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numaralı kök sertifika silinirken hata oluştu";
                                logger.Info(mesaj, aEx);
                            }
                            catch (Exception bEx)
                            {
                                String mesaj = silinecek.getKokSertifikaNo() + " numarali silinecek kök sertifika ile ilgili hata oluştu\n" + bEx.Message;
                                logger.Info(mesaj, aEx);
                            }
                        }
                    }

                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    string mesaj = "Güncelleme işlemi sırasında VT hatası oluştu.Yapılan VT işlemleri geri alınacak.";
                    logger.Error(mesaj, aEx);
                    throw new CertStoreException(mesaj, aEx);
                }
            }
        }

        private void _depoYukle(String yuklemeDosyaAdi)
        {
            if (yuklemeDosyaAdi == null || !File.Exists(yuklemeDosyaAdi)) return;

            try
            {
                CertStoreRootCertificateOps rootCertOps = new CertStoreRootCertificateOps(this);
                byte[] yuklenecekBytes = AsnIO.dosyadanOKU(yuklemeDosyaAdi);
                rootCertOps.addSignedRootCertificates(yuklenecekBytes);
            }
            catch (Exception aEx)
            {
                string mesaj = "Yeni sertifikaları sertifika deposuna yüklerken hata oluştu.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }
        }


        /**
         * Depo parolasini null a set eder.Depodaki ozel anahtarli sertifikalarin ozelanahtar alanlarini null a set eder.
         * @throws CertStoreException
         */
        public void resetPassword()
        {
            using (DbTransaction transaction = _mConn.BeginTransaction())
            {
                try
                {
                    DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);
                    ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, null);
                    ItemSource<DepoSertifika> depoSertifikaItemSource = ven.ozelAnahtarliSertifikalariListele();
                    DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();
                    while (depoSertifika != null)
                    {
                        depoSertifika.setPrivateKey(null);
                        ven.sertifikaYaz(depoSertifika, false);
                        depoSertifika = depoSertifikaItemSource.nextItem();
                    }
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    string mesaj = "Depo parola sıfırlama işleminde VT hatası oluştu.Yapılan VT işlemleri geri alınacak.";
                    logger.Error(mesaj, aEx);
                    throw new CertStoreException(mesaj, aEx);
                }
            }
        }


        /*
         * Depo parolasini set eder.
         * @param aDepoParola Depoya set edilecek yeni parola
         * @throws CertStoreException
         */
        public void setPassword(String aDepoParola)
        {
            byte[] sifreliveri = null;
            try
            {
                Sifreci sifreci = new Sifreci(aDepoParola);
                sifreliveri = sifreci.GetBirlesikSifreliVeri(Encoding.ASCII.GetBytes(DEPO_DOGRULAMA_STR));
            }
            catch (Exception aEx)
            {
                String mesaj = "Depo Parolası belirleme işleminde hata oluştu.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }

            using (DbTransaction transaction = _mConn.BeginTransaction())
            {
                try
                {
                    DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);
                    ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, sifreliveri);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    String mesaj = "Depo parolası belirleme işleminde VT hatası oluştu.";
                    logger.Error(mesaj, aEx);
                    throw new CertStoreException(mesaj, aEx);
                }
            }
        }

        public bool validatePassword(string aDepoParola)
        {
            try
            {
                DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);
                DepoSistemParametresi sp = ven.sistemParametresiOku(DepoSistemParametresi.PARAM_DEPO_PAROLA);

                if (sp.getParamDegeri() == null)
                {
                    throw new CertStoreException("Depo Parolası set edilmemiş.");
                }
                else if (!(sp.getParamDegeri() is byte[]))
                {
                    throw new CertStoreException("VT den gelen Depo Parolası tipi byte[] değil.");
                }
                else
                {
                    byte[] sifreliveri = (byte[]) sp.getParamDegeri();
                    byte[] sifresizveri = null;
                    try
                    {
                        Sifreci sifreci = new Sifreci(aDepoParola, sifreliveri);
                        sifresizveri = sifreci.BirlesikSifreliVeriCoz();
                    }
                    catch (Exception aEx)
                    {
                        logger.Error("Depo parolası doğrulamada hata oluştu.", aEx);
                        return false;
                    }
                    string sifresizveriStr = Encoding.ASCII.GetString(sifresizveri);

                    if (sifresizveriStr.Equals(DEPO_DOGRULAMA_STR))
                        return true;

                    return false;
                }

            }
            catch (ObjectNotFoundException aEx)
            {
                String mesaj = "Veritabanında depo parolası sistem parametresi bulunamadı.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }
            catch (Exception aEx)
            {
                String mesaj = "Depo Parolası doğrulama işlemi sırasında hata oluştu";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }
        }



        /**
         * Eski depo parolasi dogrulandiktan sonra,yeni depo parolasi set edilir.Depoda ozel anahtari olan sertifikalarin
         * sifreli ozelanahtarlari eski depo parolasiyla cozulerek,yeni depo parolasiyla tekrar sifrelenir ve depoya
         * kaydedilirler.
         * @param aEskiDepoParola
         * @param aYeniDepoParola
         * @throws CertStoreException
         */
        public void changePassword(String aEskiDepoParola, String aYeniDepoParola)
        {
            if (!validatePassword(aEskiDepoParola))
                throw new CertStoreException("Değiştirilmek istenen depo parolası doğrulanamadı");

            {
                using (DbTransaction transaction = _mConn.BeginTransaction())
                {
                    try
                    {
                        DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);
                        ItemSource<DepoSertifika> depoSertifikaItemSource = ven.ozelAnahtarliSertifikalariListele();
                        DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();
                        Sifreci yeniParolaSifreleyici = new Sifreci(aYeniDepoParola);
                        ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, yeniParolaSifreleyici.GetBirlesikSifreliVeri(Encoding.ASCII.GetBytes(DEPO_DOGRULAMA_STR)));
                        while (depoSertifika != null)
                        {

                            byte[] sifreliYeniPriKey = null;
                            try
                            {
                                byte[] sifreliPriKey = depoSertifika.getPrivateKey();
                                Sifreci sifreci = new Sifreci(aEskiDepoParola, sifreliPriKey);
                                byte[] sifresizPriKey = sifreci.BirlesikSifreliVeriCoz();

                                Sifreci sifreciyeni = new Sifreci(aYeniDepoParola);
                                sifreliYeniPriKey = sifreciyeni.GetBirlesikSifreliVeri(sifresizPriKey);
                            }
                            catch (Exception)
                            {
                                sifreliYeniPriKey = null;
                            }

                            depoSertifika.setPrivateKey(sifreliYeniPriKey);
                            ven.sertifikaYaz(depoSertifika, false);
                            depoSertifika = depoSertifikaItemSource.nextItem();
                        }

                        transaction.Commit();
                    }
                    catch (Exception aEx)
                    {
                        transaction.Rollback();
                        String mesaj = "Depo parolası değiştirme işleminde hata oluştu.";
                        logger.Error(mesaj, aEx);
                        throw new CertStoreException(mesaj, aEx);
                    }
                }
            }
        }

        /**
         * aDepoParolasindan elde edilen anahtarla aPrivateKey sifrelenir,sifreli verinin basina iv ve salt degerleri 
         * koyulur.
         * @param aPrivateKey Sifrelenecek veri
         * @param aDepoParola Depo parolasi
         * @return
         * @throws CertStoreException
         */
        public byte[] encryptWithPassword(byte[] aPrivateKey, String aDepoParola)
        {
            try
            {
                bool sonuc = validatePassword(aDepoParola);
                if (!sonuc)
                {
                    String mesaj = "Depo parolasıyla şifreleme işleminde verilen depo parolası doğrulanamadı.";
                    logger.Error(mesaj);
                    throw new CertStoreException(mesaj);
                }
            }
            catch (Exception aEx)
            {
                String mesaj = "Depo parolasıyla şifreleme işleminde verilen depo parolası doğrulama işleminde hata oluştu.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }
            try
            {
                Sifreci sifreci = new Sifreci(aDepoParola);
                return sifreci.GetBirlesikSifreliVeri(aPrivateKey);
            }
            catch (Exception aEx)
            {
                String mesaj = "Depo parolasıyla şifreleme işleminde hata oluştu.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }
        }

        public byte[] decryptWithPassword(byte[] aSifreliVeri, String aDepoParola)
        {
            bool sonuc = validatePassword(aDepoParola);
            if (!sonuc)
            {
                String mesaj = "Depo parolasıyla şifre çözme işleminde verilen depo parolası doğrulanamadı.";
                logger.Error(mesaj);
                throw new CertStoreException(mesaj);
            }
            try
            {
                Sifreci sifreci = new Sifreci(aDepoParola, aSifreliVeri);
                return sifreci.BirlesikSifreliVeriCoz();
            }
            catch (Exception aEx)
            {
                String mesaj = "Depo parolasıyla şifre çözme işleminde hata oluştu.";
                logger.Error(mesaj, aEx);
                throw new CertStoreException(mesaj, aEx);
            }

        }


        private Pair<String, String> _getSerialANDIssuer(byte[] aValue)
        {
            ECertificate esyacer = new ECertificate(aValue);
            String issuer = esyacer.getIssuer().stringValue();
            String serial = esyacer.getSerialNumberHex();
            return new Pair<String, String>(issuer, serial);
        }

        private class Sifreci
        {
            private readonly String mParola = null;
            private readonly byte[] mIV = null;
            private readonly byte[] mSalt = null;
            private readonly byte[] mSifreliVeri = null;

            internal Sifreci(String aParola)
            {
                mIV = new byte[16];
                mSalt = new byte[16];
                mParola = aParola;

                RandomUtil.generateRandom(mIV);
                RandomUtil.generateRandom(mSalt);
            }

            internal Sifreci(string aParola, byte[] aBirlesikSifreliVeri)
            {
                mIV = new byte[16];
                mSalt = new byte[16];
                mSifreliVeri = new byte[aBirlesikSifreliVeri.Length - 32];
                mParola = aParola;

                Array.Copy(aBirlesikSifreliVeri, 0, mIV, 0, 16);
                Array.Copy(aBirlesikSifreliVeri, 16, mSalt, 0, 16);
                Array.Copy(aBirlesikSifreliVeri, 32, mSifreliVeri, 0, mSifreliVeri.Length);
            }



            internal byte[] GetBirlesikSifreliVeri(byte[] aSifrelenecek)
            {
                PBEKeySpec keySpec = KeySpecOlustur();

                byte[] sifreliveri = CipherUtil.encrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(mIV), keySpec, aSifrelenecek);
                byte[] butunveri = new byte[sifreliveri.Length + 32];

                Array.Copy(mIV, 0, butunveri, 0, 16);
                Array.Copy(mSalt, 0, butunveri, 16, 16);
                Array.Copy(sifreliveri, 0, butunveri, 32, sifreliveri.Length);
                return butunveri;

            }


            internal byte[] BirlesikSifreliVeriCoz()
            {
                PBEKeySpec keySpec = KeySpecOlustur();
                return CipherUtil.decrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(mIV), keySpec, mSifreliVeri);
            }

            private PBEKeySpec KeySpecOlustur()
            {
                PBEKeySpec aPBEKeySpec = new PBEKeySpec(mParola.ToCharArray(), mSalt, DEPO_PAROLA_ITERATION_COUNT);
                Asn1OctetString algParam = new Asn1OctetString(mIV);
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                algParam.Encode(encBuf);
                Asn1OpenType ot = new Asn1OpenType(encBuf.MsgCopy);
                AlgorithmIdentifier algId = new AlgorithmIdentifier(CipherAlg.AES256_CBC.getOID(), ot);

                // todo IV ne olacak?, normalde AES te IV yok gibi...
                return aPBEKeySpec;
            }

        }


        private void SorgulariCalistir()
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mConn);
            using (DbTransaction transaction = _mConn.BeginTransaction())
            {
                try
                {
                    ven.sorguCalistir(CertStoreSQLs.HASH_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SERTIFIKA_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.KOKSERTIFIKA_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SIL_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.DIZIN_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.DIZINSERTIFIKA_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.DIZINSIL_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SILINECEKKOKSERTIFIKA_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SISTEMPARAMETRELERI_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.NITELIK_SERTIFIKASI_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.OCSP_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SERTIFIKAOCSPS_TABLE_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SERTIFIKA_INDEX_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.NITELIK_SERTIFIKASI_INDEX_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.KOKSERTIFIKA_INDEX_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.SILINECEKKOKSERTIFIKA_INDEX_CREATE);
                    ven.sorguCalistir(CertStoreSQLs.HASH_INDEX_CREATE);
                    DepoDizin dizin = new DepoDizin();
                    dizin.setDizinAdi("DEPO");
                    ven.dizinYaz(dizin);
                    ven.sorguCalistir(CertStoreSQLs.DEPO_PAROLA_SP_INSERT);
                    ven.sorguCalistir(CertStoreSQLs.VERSIYON_SP_INSERT);
                    ven.sorguCalistir(CertStoreSQLs.LAST_RUN_SQL_SP_INSERT);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Depo dosyası oluşturulduktan sonra ilk sorgular çalıştırılırken veritabanı hatası oluştu", aEx);
                }
            }
        }
        public void closeConnection()
        {
            _mConn.Dispose();
        }

        public void openConnection()
        {
            _mConn = CertStoreDBLayer.newConnection(_mDepoDosyaAdi);
        }
        public DbConnection getConn()
        {
            return _mConn;
        }

    }
}

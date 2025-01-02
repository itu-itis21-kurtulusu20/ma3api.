package tr.gov.tubitak.uekae.esya.api.infra.certstore;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.PBEAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzneTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSilinecekKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSistemParametresi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.DEPO_PAROLA_SP_INSERT;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.DIZINSERTIFIKA_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.DIZINSIL_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.DIZIN_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.HASH_INDEX_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.HASH_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.KOKSERTIFIKA_INDEX_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.KOKSERTIFIKA_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.LAST_RUN_SQL_SP_INSERT;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.NITELIK_SERTIFIKASI_INDEX_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.NITELIK_SERTIFIKASI_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.OCSP_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SERTIFIKAOCSPS_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SERTIFIKA_INDEX_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SERTIFIKA_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SILINECEKKOKSERTIFIKA_INDEX_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SILINECEKKOKSERTIFIKA_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SIL_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.SISTEMPARAMETRELERI_TABLE_CREATE;
import static tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreSQLs.VERSIYON_SP_INSERT;


public class CertStore {

    private static Logger logger = LoggerFactory.getLogger(CertStore.class);

    private static final String DEFAULT_DEPO_DOSYA_ADI = "SertifikaDeposu.svt";
    private static final String DEFAULT_GUNCELLEME_DOSYA_ADI = "Guncelle.svt";
    private static final String DEFAULT_IMZALI_KOKLER_DOSYA_ADI = "imzalidosya.dat";
    private static final String DEPO_DOGRULAMA_STR = "DepoParolasiBelirlenmistir";
    //private static final byte[] DEPO_PAROLA_SALT = {45,23,-12,34,16,124,56,-40,54,-10,11,42,34,-23,3,41,8,27,32,6,-12,18,-4,27,76,57,43,2,0,6,-3,10};
    private static final int DEPO_PAROLA_ITERATION_COUNT = 2000;

    private String mDepoDosyaAdi;
    private String mGuncellemeDosyaAdi;
    private String mYuklemeDosyaAdi;

    private Connection mConn;

    /**
     * Kullanicinin home dizininde .sertifikadeposu dizini altinda SertifikaDeposu.svt dosyasi varsa,bu depo dosyasi
     * uzerinde islem yapar. Yoksa,bu dosyayi olusturarak gerekli tablolari yaratir.Guncelleme dosyasi olarak,SertifikaDeposu.svt
     * dosyasi ile ayni yerde Guncelleme.svt dosyasini arar,varsa bu dosyadaki guncellemeleri SertifikaDeposu.svt ye aktarir.Aktarim
     * tamamlandiktan sonra Guncelleme.svt dosyasini siler.
     *
     * @throws CertStoreException
     */
    public CertStore() throws CertStoreException 
    {
        try 
        {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } 
        catch (LE e)
        {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        String folder = System.getProperty("user.home") +
                System.getProperty("file.separator") +
                CertStoreUtil.DEPO_DIZIN_ADI +
                System.getProperty("file.separator");

        mDepoDosyaAdi = folder +
                DEFAULT_DEPO_DOSYA_ADI;

        mGuncellemeDosyaAdi = folder +
                DEFAULT_GUNCELLEME_DOSYA_ADI;

        mYuklemeDosyaAdi = folder +
                DEFAULT_IMZALI_KOKLER_DOSYA_ADI;

        try {
            File file = new File(mDepoDosyaAdi);
            if (!file.exists()) {
                new File(System.getProperty("user.home") +
                        System.getProperty("file.separator") +
                        CertStoreUtil.DEPO_DIZIN_ADI).mkdir();

                // burayi kapattik cunku mali muhur sertifikasinda xml depo ile dogrulama yapamiyorduk
                // .net'te bu satir yoktu, o dogruluyordu, burayi kapatmak baska hatalara sebep olur mu bilmiyorum
                // ama saver'lar acik ise yine .svt dosyasi olusturuluyordu .net'te, ama burda saver'lari kapatsan da
                // bu satir yuzunden .svt dosyasi olusuyor, hatanin burdan kaynaklandigini dusunduk
                //file.createNewFile();

                mConn = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
                sorgulariCalistir();
            } else {
                mConn = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            }

            File guncellemeFile = new File(mGuncellemeDosyaAdi);
            if (guncellemeFile.exists()) {
                _depoGuncelle(mGuncellemeDosyaAdi);
                guncellemeFile.deleteOnExit();
            }

            File yuklemeFile = new File(mYuklemeDosyaAdi);
            if (yuklemeFile.exists()) {
                _depoYukle(mYuklemeDosyaAdi);
                yuklemeFile.deleteOnExit();
            }


        } catch (Exception e) {
            throw new CertStoreException("Error while initializing certstore file", e);
        }

    }


    /**
     * aDepoDosyaAdi adresli dosya varsa,islemleri bu dosya uzerinde yapar.Yoksa bu dosyayi dizin yapisiyla beraber olusturur
     * ve gerekli tablolari yaratir.aGuncellemeDosyaAdi adresli dosya varsa,bu dosyadan depo dosyasina aktarim gerceklestirilir.
     * Aktarimdan sonra guncelleme dosyasi silinmez.
     *
     * @param aDepoDosyaAdi       Depo olarak kullanilacak dosyasinin pathi
     * @param aGuncellemeDosyaAdi Depoya yapilacak guncellemeleri iceren dosyanin pathi
     * @throws CertStoreException
     */
    public CertStore(String aDepoDosyaAdi, String aGuncellemeDosyaAdi, String aYuklemeDosyaAdi)
            throws CertStoreException {
    	String yuklemeDosyaAdi = null;
        try 
        {
            LV.getInstance().checkLD(Urunler.ORTAK);
            mDepoDosyaAdi = PathUtil.getRawPath(aDepoDosyaAdi);
            mGuncellemeDosyaAdi = PathUtil.getRawPath(aGuncellemeDosyaAdi);
            yuklemeDosyaAdi = PathUtil.getRawPath(aYuklemeDosyaAdi);
        } 
        catch (LE e)
        {
            throw new CertStoreException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }

        try {
            mDepoDosyaAdi = new File(mDepoDosyaAdi).getCanonicalPath();
        } catch (Exception x) {
            throw new CertStoreException("I/O Hatası ", x);
        }


        String fSeparator = System.getProperty("file.separator");
        int index = mDepoDosyaAdi.lastIndexOf(fSeparator);
        if (index == -1) {
            throw new CertStoreException("Belirtilen depo dosyasinin pathi gecerli degil");
        }

        String directoryPart = mDepoDosyaAdi.substring(0, index);

        File directory = new File(directoryPart);
        if (!directory.exists()) {
            boolean sonuc = directory.mkdirs();
            if (sonuc == false) {
                throw new CertStoreException(directoryPart + " dizini yaratilamiyor.");
            }
        }

        File depoFile = new File(mDepoDosyaAdi);
        if (!depoFile.exists()) {
            try {
                depoFile.createNewFile();
            } catch (Exception aEx) {
                throw new CertStoreException(mDepoDosyaAdi + " dosyasi yaratilirken hata olustu", aEx);
            }
            mConn = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            sorgulariCalistir();
        } else {
            mConn = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
        }

        if (mGuncellemeDosyaAdi != null) {
            File guncellemeFile = new File(mGuncellemeDosyaAdi);
            if (guncellemeFile.exists()) {
                _depoGuncelle(mGuncellemeDosyaAdi);
                guncellemeFile.deleteOnExit();
            }
        }

        if (yuklemeDosyaAdi != null) {
            File yuklemeFile = new File(yuklemeDosyaAdi);
            if (yuklemeFile.exists()) {
                try {
                    _depoYukle(aYuklemeDosyaAdi);
                } catch (Exception aEx) {
                    throw new CertStoreException("Error while loading new certificates to cert store", aEx);
                }
                yuklemeFile.deleteOnExit();
            }
        }
    }


    public String getStoreName() {
        return mDepoDosyaAdi;
    }

    private void _depoGuncelle(String aGuncellemeDosyaAdi)
            throws CertStoreException {

        Connection oturumG = null;

        List<DepoKokSertifika> guncellemeList = null;
        List<DepoSilinecekKokSertifika> silinecekList = null;
        try {
            oturumG = DepoVTKatmani.yeniOturum(aGuncellemeDosyaAdi);
            //aiG = oturumG.atomikIsBasla();
            DepoVEN venG = DepoVTKatmani.yeniDepoVEN(oturumG);
            guncellemeList = ((RsItemSource) venG.kokSertifikaListele()).toList();
            silinecekList = ((RsItemSource) venG.silinecekKokSertifikaListele()).toList();
            oturumG.close();
        } catch (Exception aEx) {
            JDBCUtil.rollback(oturumG);
            throw new CertStoreException("Guncelleme dosyasindan okuma yapilirken veritabani hatasi olustu.", aEx);
        } finally {
            JDBCUtil.close(oturumG);
        }

        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mConn);

            for (DepoKokSertifika kok : guncellemeList) {
                try {
                    Pair<String, String> ikili = _getSerialANDIssuer(kok.getValue());
                    String issuer = ikili.first();
                    String serial = ikili.second();
                    DepoKokSertifika depokok = null;
                    try {
                        byte[] ozet = DigestUtil.digest(DigestAlg.SHA1, kok.getValue());
                        depokok = ven.kokSertifikaHasheGoreBul(ozet);
                        if (depokok.getKokGuvenSeviyesi().getIntValue() <= kok.getKokGuvenSeviyesi().getIntValue())
                            continue;
                    } catch (NotFoundException e) {
                        logger.info("Eklenmek istenen sertifika, serifika deposu içerisinde bulunmuyor. Ekleme işlemine devam ediliyor.");
                    }
                    if (CertStoreUtil.verifyDepoKokSertifika(kok)) {
                        if (kok.getKokGuvenSeviyesi().equals(GuvenlikSeviyesi.PERSONAL)) {
                        	javax.swing.JOptionPane.showMessageDialog(null, "Issuer i " + issuer + " olan ve seri numarasi " + serial + " olan kisisel kok sertifika sertifika deponuza guncelleme dosyasindan eklenmektedir.", "Dikkat!", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                        kok.setKokSertifikaNo(null);
                        List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(kok.getValue(), OzneTipi.KOK_SERTIFIKA);
                        ven.kokSertifikaYaz(kok, ozetler);
                    } else {
                        String mesaj = issuer + " issuer isimli " + serial + " seri numarali kokun imzasi dogrulanamadi";
                        logger.info(mesaj);
                    }
                } catch (Exception aEx) {
                    try {
                        Pair<String, String> ikili = _getSerialANDIssuer(kok.getValue());
                        String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numarali kokun guncelleme dosyasindan aktarimi sirasinda hata olustu";
                        logger.info(mesaj, aEx);
                    } catch (Exception bEx) {
                        String mesaj = kok.getKokSertifikaNo() + " nolu kok sertifikanin guncelleme dosyasindan aktarimi sirasinda hata olustu\n" + bEx.getMessage();
                        logger.info(mesaj, bEx);
                    }
                }

            }
            for (DepoSilinecekKokSertifika silinecek : silinecekList) {
                try {
                    DepoKokSertifika deposilinecek = null;
                    Pair<String, String> ikili = _getSerialANDIssuer(silinecek.getValue());
                    try {
                        ven.kokSertifikaValueYaGoreBul(silinecek.getValue());
                    } catch (NotFoundException e) {
                        String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numarali silinecek kok sertifika veritabaninda bulunamadi";
                        logger.info(mesaj, e);
                        continue;
                    }

                    if (CertStoreUtil.verifyDepoSilinecekKokSertifika(silinecek)) {
                        ven.kokSertifikaSil(deposilinecek.getKokSertifikaNo());
                    } else {
                        String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numarali silinecek kok sertifikanin imzasi dogrulanamadi";
                        logger.info(mesaj);
                    }
                } catch (Exception aEx) {
                    try {
                        Pair<String, String> ikili = _getSerialANDIssuer(silinecek.getValue());
                        String mesaj = ikili.first() + " issuer isimli " + ikili.second() + " seri numarali kok sertifika silinirken hata olustu";
                        logger.info(mesaj, aEx);
                    } catch (Exception e) {
                        String mesaj = silinecek.getKokSertifikaNo() + " numarali silinecek kok sertifika ile ilgili hata olustu\n" + e.getMessage();
                        logger.info(mesaj, e);
                    }
                }
            }

            mConn.commit();

        } catch (Exception aEx) {
            String mesaj = "Guncelleme islemi sirasinda VT hatasi olustu.Yapilan VT islemleri geri alinacak.";
            logger.error(mesaj, aEx);
            JDBCUtil.rollback(mConn);
            throw new CertStoreException("Guncelleme isleminde VT hatasi olustu.", aEx);
        }/*
        finally {
            JDBCUtil.close(oturum);
        }*/

    }

    private void _depoYukle(String yuklemeDosyaAdi) throws IOException, CertStoreException {
        CertStoreRootCertificateOps rootCertOps = new CertStoreRootCertificateOps(this);
        byte[] yuklenecekBytes = AsnIO.dosyadanOKU(yuklemeDosyaAdi);
        rootCertOps.addSignedRootCertificates(yuklenecekBytes);
    }


    /**
     * Depo parolasini null a set eder.Depodaki ozel anahtarli sertifikalarin ozelanahtar alanlarini null a set eder.
     *
     * @throws CertStoreException
     */
    public void resetPassword()
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mConn);
            ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, null);
            ItemSource<DepoSertifika> depoSertifikaItemSource = ven.ozelAnahtarliSertifikalariListele();

            DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();
            while (depoSertifika != null) {
                depoSertifika.setPrivateKey(null);
                ven.sertifikaYaz(depoSertifika, false);
            }
            mConn.commit();
        } catch (Exception aEx) {
            JDBCUtil.rollback(mConn);
            String mesaj = "Depo parola sifirlama isleminde VT hatasi olustu.Yapilan VT islemleri geri alinacak.";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        }/*
        finally {
            JDBCUtil.close(oturum);
        }*/
    }


    /**
     * Depo parolasini set eder.
     *
     * @param aDepoParola Depoya set edilecek yeni parola
     * @throws CertStoreException
     */
    public void setPassword(String aDepoParola)
            throws CertStoreException {
        byte[] sifreliveri = null;
        try {
            Sifreci sifreci = new Sifreci(aDepoParola);
            sifreliveri = sifreci.getBirlesikSifreliVeri(DEPO_DOGRULAMA_STR.getBytes());
        } catch (Exception aEx) {
            String mesaj = "Depo Parolasi belirleme isleminde hata olustu.";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        }

        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mConn);
            ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, sifreliveri);
            mConn.commit();
        } catch (Exception aEx) {
            JDBCUtil.rollback(mConn);
            String mesaj = "Depo parolasi belirleme isleminde VT hatasi olustu.";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public boolean validatePassword(String aDepoParola)
            throws CertStoreException {
        Connection oturum = null;
        try {
            oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(oturum);
            DepoSistemParametresi sp = ven.sistemParametresiOku(DepoSistemParametresi.PARAM_DEPO_PAROLA);

            if (sp.getParamDegeri() == null) {
                throw new CertStoreException("Depo Parolasi set edilmemis.");
            } else if (!(sp.getParamDegeri() instanceof byte[])) {
                throw new CertStoreException("VT den gelen Depo Parolasi tipi byte[] degil.");
            } else {
                byte[] sifreliveri = (byte[]) sp.getParamDegeri();
                byte[] sifresizveri = null;
                try {
                    Sifreci sifreci = new Sifreci(aDepoParola, sifreliveri);
                    sifresizveri = sifreci.birlesikSifreliVeriCoz();
                } catch (Exception aEx) {
                    logger.error("Depo parolasi dogrulamada hata olustu.", aEx);
                    return false;
                }
                String sifresizveriSTR = new String(sifresizveri);

                if (sifresizveriSTR.equals(DEPO_DOGRULAMA_STR))
                    return true;

                return false;
            }
        } catch (NotFoundException aEx) {
            String mesaj = "Veritabaninda depoparolasi sistem parametresi bulunamadi.";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        } catch (CertStoreException aEx) {
            String mesaj = "Depo Parolasi dogrulama islemi sirasinda veritabani hatasi olustu";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        } finally {
            try {
                oturum.commit();
                JDBCUtil.close(oturum);
            } catch (Exception e) {
                String mesaj = "Depo Parolasi dogrulama islemi sirasinda veritabani hatasi olustu";
                logger.error(mesaj, e);
                throw new CertStoreException(mesaj, e);
            }
        }
    }


    /**
     * Eski depo parolasi dogrulandiktan sonra,yeni depo parolasi set edilir.Depoda ozel anahtari olan sertifikalarin
     * sifreli ozelanahtarlari eski depo parolasiyla cozulerek,yeni depo parolasiyla tekrar sifrelenir ve depoya
     * kaydedilirler.
     *
     * @param aEskiDepoParola
     * @param aYeniDepoParola
     * @throws CertStoreException
     */
    public void changePassword(String aEskiDepoParola, String aYeniDepoParola)
            throws CertStoreException {
        if (!validatePassword(aEskiDepoParola))
            throw new CertStoreException("Degistirilmek istenen depo parolasi dogrulanamadi");

        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mConn);
            ItemSource<DepoSertifika> depoSertifikaItemSource = ven.ozelAnahtarliSertifikalariListele();

            DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();

            Sifreci yeniParolaSifreleyici = new Sifreci(aYeniDepoParola);
            ven.sistemParametresiUpdate(DepoSistemParametresi.PARAM_DEPO_PAROLA, yeniParolaSifreleyici.getBirlesikSifreliVeri(DEPO_DOGRULAMA_STR.getBytes()));
            while (depoSertifika != null) {
                byte[] sifreliYeniPriKey = null;
                try {
                    byte[] sifreliPriKey = depoSertifika.getPrivateKey();
                    Sifreci sifreci = new Sifreci(aEskiDepoParola, sifreliPriKey);
                    byte[] sifresizPriKey = sifreci.birlesikSifreliVeriCoz();

                    Sifreci sifreciyeni = new Sifreci(aYeniDepoParola);
                    sifreliYeniPriKey = sifreciyeni.getBirlesikSifreliVeri(sifresizPriKey);
                } catch (Exception aEx) {
                    sifreliYeniPriKey = null;
                    logger.warn("Warning in CertStore", aEx);
                }

                depoSertifika.setPrivateKey(sifreliYeniPriKey);
                ven.sertifikaYaz(depoSertifika, false);
            }

            mConn.commit();
        } catch (Exception aEx) {
            String mesaj = "Depo parolasi degistir isleminde hata olustu.";
            logger.error(mesaj, aEx);
            JDBCUtil.rollback(mConn);
            throw new CertStoreException(mesaj, aEx);
        }
        /*
        finally {
            JDBCUtil.close(oturum);
        } */

    }

    /**
     * aDepoParolasindan elde edilen anahtarla aPrivateKey sifrelenir,sifreli verinin basina iv ve salt degerleri
     * koyulur.
     *
     * @param aPrivateKey Sifrelenecek veri
     * @param aDepoParola Depo parolasi
     * @return
     * @throws CertStoreException
     */
    public byte[] encryptWithPassword(byte[] aPrivateKey, String aDepoParola)
            throws CertStoreException {
        try {
            boolean sonuc = validatePassword(aDepoParola);
            if (!sonuc) {
                String mesaj = "Depo parolasiyla sifreleme isleminde verilen depo parolasi dogrulanamadi.";
                logger.error(mesaj);
                throw new CertStoreException("Depo Parolasi dogrulanamadi.");
            }
        } catch (Exception aEx) {
            String mesaj = "Depo parolasiyla sifreleme isleminde verilen depo parolasi dogrulama isleminde hata olustu.";
            logger.error(mesaj, aEx);
            throw new CertStoreException("Depo Parola dogrulama isleminde hata olustu.", aEx);
        }
        try {
            Sifreci sifreci = new Sifreci(aDepoParola);
            return sifreci.getBirlesikSifreliVeri(aPrivateKey);
        } catch (Exception aEx) {
            String mesaj = "Depo parolasiyla sifreleme isleminde hata olustu.";
            logger.error(mesaj, aEx);
            throw new CertStoreException("Depo Parolasiyla sifreleme isleminde hata olustu.", aEx);
        }
    }

    public byte[] decryptWithPassword(byte[] aSifreliVeri, String aDepoParola)
            throws CertStoreException {
        boolean sonuc = validatePassword(aDepoParola);
        if (!sonuc) {
            String mesaj = "Depo parolasiyla sifre cozme isleminde verilen depo parolasi dogrulanamadi.";
            logger.error(mesaj);
            throw new CertStoreException("Depo Parolasi dogrulanamadi.");
        }
        try {
            Sifreci sifreci = new Sifreci(aDepoParola, aSifreliVeri);
            return sifreci.birlesikSifreliVeriCoz();
        } catch (Exception aEx) {
            String mesaj = "Depo parolasiyla sifre cozme isleminde hata olustu.";
            logger.error(mesaj, aEx);
            throw new CertStoreException(mesaj, aEx);
        }

    }

    private Pair<String, String> _getSerialANDIssuer(byte[] aValue)
            throws ESYAException {
        ECertificate esyacer = new ECertificate(aValue);
        String issuer = esyacer.getIssuer().stringValue();
        String serial = esyacer.getSerialNumberHex();
        return new Pair<String, String>(issuer, serial);
    }

    /* private PBECipher _simetrikAnahtarUret(String aDepoParola, byte[] aIV, byte[] aSalt)
        throws CertStoreException
{
    try {
        PBEKeySpec aPBEKeySpec = new PBEKeySpec(aDepoParola.toCharArray(), aSalt, DEPO_PAROLA_ITERATION_COUNT);
        Asn1OctetString algParam = new Asn1OctetString(aIV);
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        algParam.encode(encBuf);
        Asn1OpenType ot = new Asn1OpenType(encBuf.getMsgCopy());
        AlgorithmIdentifier algId = new AlgorithmIdentifier(CipherAlg.AES256_CBC.getOID(), ot);

        PBECipher cipher = new PBECipher(aPBEKeySpec, algId);
        cipher.init();
        return cipher;
    }
    catch (Throwable aEx) {
        String mesaj = "Depo Parolasindan anahtar uretme isleminde hata olustu";
        LOGGER.log(Level.SEVERE, mesaj);
        throw new CertStoreException(mesaj, aEx);
    }
}    */

    class Sifreci {
        private String mParola = null;
        private byte[] mIV = null;
        private byte[] mSalt = null;
        private byte[] mSifreliVeri = null;

        Sifreci(String aParola) {
            mIV = new byte[16];
            mSalt = new byte[16];
            mParola = aParola;

            RandomUtil.generateRandom(mIV);
            RandomUtil.generateRandom(mSalt);
        }

        Sifreci(String aParola, byte[] aBirlesikSifreliVeri) {
            mIV = new byte[16];
            mSalt = new byte[16];
            mSifreliVeri = new byte[aBirlesikSifreliVeri.length - 32];
            mParola = aParola;

            System.arraycopy(aBirlesikSifreliVeri, 0, mIV, 0, 16);
            System.arraycopy(aBirlesikSifreliVeri, 16, mSalt, 0, 16);
            System.arraycopy(aBirlesikSifreliVeri, 32, mSifreliVeri, 0, mSifreliVeri.length);
        }


        byte[] getBirlesikSifreliVeri(byte[] aSifrelenecek)
                throws Exception {
            PBEKeySpec keySpec = keySpecOlustur();

            byte[] sifreliveri = CipherUtil.encrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(mIV), keySpec, aSifrelenecek);
            byte[] butunveri = new byte[sifreliveri.length + 32];

            System.arraycopy(mIV, 0, butunveri, 0, 16);
            System.arraycopy(mSalt, 0, butunveri, 16, 16);
            System.arraycopy(sifreliveri, 0, butunveri, 32, sifreliveri.length);
            return butunveri;

        }


        byte[] birlesikSifreliVeriCoz()
                throws ESYAException {
            PBEKeySpec keySpec = keySpecOlustur();
            return CipherUtil.decrypt(PBEAlg.PBE_AES256_SHA256, new ParamsWithIV(mIV), keySpec, mSifreliVeri);
        }

        private PBEKeySpec keySpecOlustur()
                throws ESYAException {
            PBEKeySpec aPBEKeySpec = new PBEKeySpec(mParola.toCharArray(), mSalt, DEPO_PAROLA_ITERATION_COUNT);
            Asn1OctetString algParam = new Asn1OctetString(mIV);
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            algParam.encode(encBuf);
            Asn1OpenType ot = new Asn1OpenType(encBuf.getMsgCopy());
            AlgorithmIdentifier algId = new AlgorithmIdentifier(CipherAlg.AES256_CBC.getOID(), ot);

            // todo IV ne olacak?, normalde AES te IV yok gibi...
            return aPBEKeySpec;
        }

    }


    private void sorgulariCalistir() throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mConn);
            ven.sorguCalistir(HASH_TABLE_CREATE);
            ven.sorguCalistir(SERTIFIKA_TABLE_CREATE);
            ven.sorguCalistir(KOKSERTIFIKA_TABLE_CREATE);
            ven.sorguCalistir(SIL_TABLE_CREATE);
            ven.sorguCalistir(DIZIN_TABLE_CREATE);
            ven.sorguCalistir(DIZINSERTIFIKA_TABLE_CREATE);
            ven.sorguCalistir(DIZINSIL_TABLE_CREATE);
            ven.sorguCalistir(SILINECEKKOKSERTIFIKA_TABLE_CREATE);
            ven.sorguCalistir(SISTEMPARAMETRELERI_TABLE_CREATE);
            ven.sorguCalistir(NITELIK_SERTIFIKASI_TABLE_CREATE);
            ven.sorguCalistir(OCSP_TABLE_CREATE);
            ven.sorguCalistir(SERTIFIKAOCSPS_TABLE_CREATE);
            ven.sorguCalistir(SERTIFIKA_INDEX_CREATE);
            ven.sorguCalistir(NITELIK_SERTIFIKASI_INDEX_CREATE);
            ven.sorguCalistir(KOKSERTIFIKA_INDEX_CREATE);
            ven.sorguCalistir(SILINECEKKOKSERTIFIKA_INDEX_CREATE);
            ven.sorguCalistir(HASH_INDEX_CREATE);
            DepoDizin dizin = new DepoDizin();
            dizin.setDizinAdi("DEPO");
            ven.dizinYaz(dizin);
            ven.sorguCalistir(DEPO_PAROLA_SP_INSERT);
            ven.sorguCalistir(VERSIYON_SP_INSERT);
            ven.sorguCalistir(LAST_RUN_SQL_SP_INSERT);
            mConn.commit();
        } catch (Exception aEx) {
            if (mConn != null) JDBCUtil.rollback(mConn);
            throw new CertStoreException("Depo dosyasi yaratildiktan sonra ilk sorgular calistirilirken veritabani hatasi olustu", aEx);
        }/* finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public void closeConnection() throws CertStoreException {
        if (mConn != null) {
            try {
                mConn.close();
            } catch (Exception x) {
                throw new CertStoreException("Baglanti kapatilmaya calisilirken hata olustu", x);
            }
        }
    }

    public void openConnection() throws CertStoreException {
        try {
            if (mConn.isClosed()) {
                mConn = DepoVTKatmani.yeniOturum(mDepoDosyaAdi);
            }
        } catch (SQLException e) {
            throw new CertStoreException("Connection nesnesinde Exception!", e);
        }
    }
    public Connection getConn()
    {
        return mConn;
    }
}
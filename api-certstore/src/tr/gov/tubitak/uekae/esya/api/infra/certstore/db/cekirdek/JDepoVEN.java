package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DepoIslemleri;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DizinSILYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DizinSertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DizinYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.KokSertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.NitelikSertifikasiYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzneTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SILYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaOCSPsYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SistemParametresiYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoNitelikSertifikasi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSP;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifikaOcsps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSilinecekKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSistemParametresi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class JDepoVEN implements DepoVEN {
    protected static Logger logger = LoggerFactory.getLogger(JDepoVEN.class);
    DepoIslemleri mIslemler = new DepoIslemleri();
    Connection mConnection;

    public JDepoVEN(Connection aOturum)
            throws CertStoreException {
        super();
        mConnection = aOturum;
    }

    public void sorguCalistir(String aSorgu)
            throws CertStoreException {
        mIslemler.sorguCalistir(mConnection, aSorgu);
    }

    /**
     * DIZIN
     */
    public DepoDizin dizinOku(Long aDizinNo) throws NotFoundException, CertStoreException {
        return (DepoDizin) mIslemler.yukle(mConnection, DepoDizin.class, aDizinNo);
    }

    public void dizinYaz(DepoDizin aDizin) throws CertStoreException {
        mIslemler.yaz(mConnection, aDizin);
    }

    public void dizinSil(Long aDizinNo)
            throws CertStoreException {
        String dizinSertifikalariClause = SertifikaYardimci.COLUMN_SERTIFIKA_NO + " IN (SELECT " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " FROM " +
                DizinSertifikaYardimci.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? )";
        ItemSource<DepoSertifika> sertifikaItemSource = mIslemler.sorgula(mConnection, DepoSertifika.class, dizinSertifikalariClause, new Object[]{aDizinNo});

        String dizinSILleriClause = SILYardimci.COLUMN_SIL_NO + " IN (SELECT " + DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " FROM " +
                DizinSILYardimci.DIZINSIL_TABLO_ADI + " WHERE " + DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " = ? )";
        ItemSource<DepoSIL> sILItemSource = mIslemler.sorgula(mConnection, DepoSIL.class, dizinSILleriClause, new Object[]{aDizinNo});

        try {
            DepoSertifika depoSertifika = sertifikaItemSource.nextItem();
            while (depoSertifika != null) {
                dizindenSertifikaSil(depoSertifika.getSertifikaNo(), aDizinNo);
                depoSertifika = sertifikaItemSource.nextItem();
            }
            sertifikaItemSource.close();

            DepoSIL depoSIL = sILItemSource.nextItem();
            while (depoSIL != null) {
                dizindenSILSil(depoSIL.getSILNo(), aDizinNo);
                depoSIL = sILItemSource.nextItem();
            }
            sILItemSource.close();
        } catch (Exception e) {
            throw new CertStoreException(e);
        }
        mIslemler.sil(mConnection, DepoDizin.class, aDizinNo);

    }

    public DepoDizin dizinBul(String aDizinAdi)
            throws CertStoreException {
        String whereClause = DizinYardimci.COLUMN_DIZIN_AD + " = ? ";
        DepoDizin dizin = mIslemler.tekilSonuc(mConnection, DepoDizin.class, whereClause, new Object[]{aDizinAdi});
        return dizin;
    }

    public void dizinAdiDegistir(Long aDizinNo, String aYeniAd)
            throws CertStoreException {
        String setClause = DizinYardimci.COLUMN_DIZIN_AD + " = ? ";
        String whereClause = DizinYardimci.COLUMN_DIZIN_NO + " = ? ";
        int sonuc = mIslemler.updateSorguCalistir(mConnection, DepoDizin.class, setClause, whereClause, new Object[]{aYeniAd, aDizinNo});
        if (sonuc == 0) {
            throw new CertStoreException(aDizinNo + " nolu dizin veritabaninda olmayabilir");
        }
    }

    public ItemSource<DepoDizin> dizinListele()
            throws CertStoreException {
        String whereClause = "1=1";
        ItemSource<DepoDizin> list = mIslemler.sorgula(mConnection, DepoDizin.class, whereClause, null);
        return list;
    }

    /**
     * SIL
     */

    public DepoSIL sILOku(long aSILNo)
            throws NotFoundException, CertStoreException {
        return (DepoSIL) mIslemler.yukle(mConnection, DepoSIL.class, aSILNo);
    }

    public ItemSource<DepoSIL> sILListele(String aSorgu, Object[] aParams)
            throws CertStoreException {

        return mIslemler.sorgula(mConnection, DepoSIL.class, aSorgu, aParams);
    }

    public ItemSource<DepoSIL> sILListele()
            throws CertStoreException {
        String whereClause = "1=1";
        return mIslemler.sorgula(mConnection, DepoSIL.class, whereClause, null);
    }

    public void sILYaz(DepoSIL aSIL, List<DepoOzet> aOzetler, Long aDizinNo)
            throws CertStoreException {
        Long dizinNo = aDizinNo;
        Long defaultDizinNo = DizinYardimci.getDefaultDizinNo();
        if (aDizinNo == null) {
            dizinNo = defaultDizinNo;
        } else {
            try {
                dizinOku(aDizinNo);
            } catch (NotFoundException e) {
                logger.warn("Warning in JDepoVEN", e);
                dizinNo = defaultDizinNo;
            }
        }

        if (dizinNo == defaultDizinNo) {
            try {
                dizinOku(defaultDizinNo);
            } catch (NotFoundException e) {
                throw new CertStoreException("Default dizin (1 nolu DEPO isimli) veritabaninda bulunamadi", e);
            }
        }

        // todo
        Long vtSILNo = null;
        try {
            vtSILNo = sILNoHasheGoreBul(aOzetler.get(0).getHashValue());
        } catch (NotFoundException e) {
            logger.warn("Warning in JDepoVEN", e);
            mIslemler.yaz(mConnection, aSIL);
            vtSILNo = aSIL.getSILNo();
            ozneOzetIliskiYaz(aOzetler, vtSILNo);

        }

        dizinSILIliskiYaz(dizinNo, vtSILNo);


    }

    public Long sILNoHasheGoreBul(byte[] aHash)
            throws NotFoundException, CertStoreException {
        String whereClause = OzetYardimci.COLUMN_HASH_VALUE + " = ? AND " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ?";
        DepoOzet ozet = (DepoOzet) mIslemler.tekilSonuc(mConnection, DepoOzet.class, whereClause, new Object[]{aHash, OzneTipi.SIL.getIntValue()});
        return ozet.getObjectNo();
    }

    public ItemSource<DepoDizin> sILDizinleriniListele(long aSILNo)
            throws CertStoreException {
        String whereClause = DizinYardimci.COLUMN_DIZIN_NO + " IN (SELECT " + DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " FROM " +
                DizinSILYardimci.DIZINSIL_TABLO_ADI + " WHERE " + DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " = ? )";
        return mIslemler.sorgula(mConnection, DepoDizin.class, whereClause, new Object[]{aSILNo});
    }

    public int dizindenSILSil(long aSILNo, long aDizinNo)
            throws CertStoreException {
        String deleteDizinSIL = DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " = ? AND " + DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " = ?";
        int sonuc = mIslemler.deleteSorguCalistir(mConnection, DizinSILYardimci.class, deleteDizinSIL, new Object[]{aSILNo, aDizinNo});
        if (sonuc == 0) {
            throw new CertStoreException("Verilen silno ve dizinno ya ait kayit bulunamamistir");
        }
        String fieldsClause = DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO;
        String whereClause = DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " =? ";
        boolean sILVar = mIslemler.selectSorguCalistir(mConnection, DizinSILYardimci.class, fieldsClause, whereClause, new Object[]{aSILNo});
        int etkilenen = 0;
        if (!sILVar) {
            etkilenen = mIslemler.sil(mConnection, DepoSIL.class, aSILNo);
            ozetleriSil(OzneTipi.SIL.getIntValue(), aSILNo);
        }

        return etkilenen;
    }

    public void sILTasi(long aSILNo, long aEskiDizinNo, long aYeniDizinNo)
            throws CertStoreException {
        try {
            dizinOku(aYeniDizinNo);
        } catch (NotFoundException aEx) {
            throw new CertStoreException(aYeniDizinNo + " nolu dizin veritabaninda bulunamamistir.", aEx);
        }


        String setClause = DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " = ?";
        String whereClause = DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " = ? AND " + DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " = ?";
        int sonuc = mIslemler.updateSorguCalistir(mConnection, DizinSILYardimci.class, setClause, whereClause, new Object[]{aYeniDizinNo, aEskiDizinNo, aSILNo});
        if (sonuc == 0) {
            throw new CertStoreException("Verilen parametreler icin update islemi yapilamamistir.Parametreler yanlis olabilir");
        }
    }

    public int sILSil(long aSILNo)
            throws CertStoreException {
        int etkilenen = mIslemler.sil(mConnection, DepoSIL.class, aSILNo);
        String whereClause = DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " = ?";
        mIslemler.deleteSorguCalistir(mConnection, DizinSILYardimci.class, whereClause, new Object[]{aSILNo});
        ozetleriSil(OzneTipi.SIL.getIntValue(), aSILNo);
        return etkilenen;
    }


    private void dizinSILIliskiYaz(long aDizinNo, long aSILNo)
            throws CertStoreException 
    {
    	boolean exist = false;
    	List<DepoDizin> dizinler = ((RsItemSource)sILDizinleriniListele(aSILNo)).toList();
    	for (DepoDizin depoDizin : dizinler) 
    	{
    		if(depoDizin.getDizinNo() == aDizinNo)
    		{
    			exist = true;
    			break;
    		}
		}
    	
    	if(exist == false)
    	{
	        String fieldsClause = "(" + DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + "," + DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + ")";
	        String valuesClause = "(?,?)";
	        mIslemler.insertSorguCalistir(mConnection, DizinSILYardimci.class, fieldsClause, valuesClause, new Object[]{aDizinNo, aSILNo});
    	}
    }

    /**
     * KOKSERTIFIKA
     */

    public int kokSertifikaSil(long aKokNo)
            throws CertStoreException {
        int sonuc = mIslemler.sil(mConnection, DepoKokSertifika.class, aKokNo);
        ozetleriSil(OzneTipi.KOK_SERTIFIKA.getIntValue(), aKokNo);
        return sonuc;
    }

    public ItemSource<DepoKokSertifika> kokSertifikaListele()
            throws CertStoreException {
        return mIslemler.sorgula(mConnection, DepoKokSertifika.class, "1=1", null);
    }

    public ItemSource<DepoKokSertifika> kokSertifikaListele(String aSorgu, Object[] aParams)
            throws CertStoreException {
        return mIslemler.sorgula(mConnection, DepoKokSertifika.class, aSorgu, aParams);
    }

    public DepoKokSertifika kokSertifikaHasheGoreBul(byte[] aHash)
            throws NotFoundException, CertStoreException {
        String whereClause = KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                OzetYardimci.OZET_TABLO_ADI + " WHERE " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_HASH_VALUE + " = ?  )";

        return (DepoKokSertifika) mIslemler.tekilSonuc(mConnection, DepoKokSertifika.class, whereClause, new Object[]{OzneTipi.KOK_SERTIFIKA, aHash});
    }

    public DepoKokSertifika kokSertifikaValueYaGoreBul(byte[] aValue)
            throws NotFoundException, CertStoreException {
        String whereClause = KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_VALUE + " = ? ";
        return (DepoKokSertifika) mIslemler.tekilSonuc(mConnection, DepoKokSertifika.class, whereClause, new Object[]{aValue});
    }

    public void kokSertifikaYaz(DepoKokSertifika aKok, List<DepoOzet> aOzetler)
            throws CertStoreException {
        try {
            DepoKokSertifika kok = kokSertifikaHasheGoreBul(aOzetler.get(0).getHashValue());
            int vtkokseviye = kok.getKokGuvenSeviyesi().getIntValue();
            int gelenkokseviye = aKok.getKokGuvenSeviyesi().getIntValue();

            if (vtkokseviye > gelenkokseviye) {
                // todo
                mIslemler.yaz(mConnection, aKok);
            } else if (vtkokseviye == gelenkokseviye) {
                throw new CertStoreException("Depoda ayni hash ve guvenlik seviyesine sahip kok sertifika var.");
            }
        } catch (NotFoundException e) {
            // todo
            logger.warn("Warning in JDepoVEN", e);
            mIslemler.yaz(mConnection, aKok);
            ozneOzetIliskiYaz(aOzetler, aKok.getKokSertifikaNo());
        }

    }

    /**
     * SERTIFIKA
     */
    public DepoSertifika sertifikaOku(long aSertifikaNo)
            throws NotFoundException, CertStoreException {
        return (DepoSertifika) mIslemler.yukle(mConnection, DepoSertifika.class, aSertifikaNo);
    }

    public ItemSource<DepoSertifika> sertifikaListele()
            throws CertStoreException {
        String whereClause = "1=1";
        return mIslemler.sorgula(mConnection, DepoSertifika.class, whereClause, null);
    }

    public ItemSource<DepoDizin> sertifikaDizinleriniListele(long aSertifikaNo)
            throws CertStoreException {
        String whereClause = DizinYardimci.COLUMN_DIZIN_NO + " IN (SELECT " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " FROM " +
                DizinSertifikaYardimci.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ? )";
        return mIslemler.sorgula(mConnection, DepoDizin.class, whereClause, new Object[]{aSertifikaNo});
    }

    public int sertifikaSil(long aSertifikaNo)
            throws CertStoreException {
        int etkilenen = mIslemler.sil(mConnection, DepoSertifika.class, aSertifikaNo);
        String whereClause = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ?";
        mIslemler.deleteSorguCalistir(mConnection, DizinSertifikaYardimci.class, whereClause, new Object[]{aSertifikaNo});
        ozetleriSil(OzneTipi.SERTIFIKA.getIntValue(), aSertifikaNo);
        attributeCertSil(aSertifikaNo);
        DepoSertifikaOcsps depoSertifikaOcsps = null;
        try{
            depoSertifikaOcsps = mIslemler.tekilSonuc(mConnection, SertifikaOCSPsYardimci.class, SertifikaOCSPsYardimci.COLUMN_SERTIFIKA_NO + " = ?", new Object[]{aSertifikaNo});
        }
        catch (NotFoundException e)
        {
            logger.warn("Warning in JDepoVEN", e);
              return etkilenen;
        }
        ocspCevabiSil(depoSertifikaOcsps.getOcspNo());
        return etkilenen;
    }
    
    
    public int nitelikSertifikasiSil(long aNitelikSertifikaNo)
    throws CertStoreException
    {
    	return  mIslemler.sil(mConnection, DepoNitelikSertifikasi.class, aNitelikSertifikaNo);
    }

    public int dizindenSertifikaSil(long aSertifikaNo, long aDizinNo)
            throws CertStoreException {
        String deleteDizinSertifika = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ? AND " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ?";
        int sonuc = mIslemler.deleteSorguCalistir(mConnection, DizinSertifikaYardimci.class, deleteDizinSertifika, new Object[]{aSertifikaNo, aDizinNo});
        if (sonuc == 0) {
            throw new CertStoreException("Verilen sertifikano ve dizinno ya ait kayit bulunamamistir");
        }
        String fieldsClause = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO;
        String whereClause = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " =? ";
        boolean sertifikaVar = mIslemler.selectSorguCalistir(mConnection, DizinSertifikaYardimci.class, fieldsClause, whereClause, new Object[]{aSertifikaNo});
        int etkilenen = 0;
        if (!sertifikaVar) {
            etkilenen = mIslemler.sil(mConnection, DepoSertifika.class, aSertifikaNo);
            ozetleriSil(OzneTipi.SERTIFIKA.getIntValue(), aSertifikaNo);
            attributeCertSil(aSertifikaNo);
        }

        return etkilenen;
    }

    public void ozetleriSil(int aObjectType, long aObjectNo)
            throws CertStoreException {

        String whereClause = OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_OBJECT_NO + " = ? ";
        mIslemler.deleteSorguCalistir(mConnection, DepoOzet.class, whereClause, new Object[]{aObjectType, aObjectNo});

    }
    
    public void attributeCertSil(long aCertObjNo)
    throws CertStoreException
    {
    	String whereClause = NitelikSertifikasiYardimci.COLUMN_SERTIFIKA_ID + " = ? ";
    	mIslemler.deleteSorguCalistir(mConnection, DepoNitelikSertifikasi.class, whereClause, new Object[]{aCertObjNo});
    }

    public void sertifikaTasi(long aSertifikaNo, long aEskiDizinNo, long aYeniDizinNo)
            throws CertStoreException {
        try {
            dizinOku(aYeniDizinNo);
        } catch (NotFoundException e) {
            throw new CertStoreException("Sertifikanin tasinmak istendigi dizin olan," + aYeniDizinNo + " nolu dizin veritabaninda bulunamamistir.", e);
        }

        String setClause = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ?";
        String whereClause = DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? AND " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ?";
        int sonuc = mIslemler.updateSorguCalistir(mConnection, DizinSertifikaYardimci.class, setClause, whereClause, new Object[]{aYeniDizinNo, aEskiDizinNo, aSertifikaNo});
        if (sonuc == 0) {
            throw new CertStoreException("Verilen parametreler icin update islemi yapilamamistir.Parametreler yanlis olabilir");
        }
    }

    public ItemSource<DepoSertifika> sertifikaListele(String aSorgu, Object[] aParams)
            throws CertStoreException {
        return mIslemler.sorgula(mConnection, DepoSertifika.class, aSorgu, aParams);
    }
    
    public ItemSource<DepoNitelikSertifikasi> nitelikSertifikasiListele(String aSorgu,Object[] aParams)
    throws CertStoreException
    {
    	return mIslemler.sorgula(mConnection, DepoNitelikSertifikasi.class, aSorgu, aParams);
    }

    public ItemSource<DepoSertifika> ozelAnahtarliSertifikalariListele()
            throws CertStoreException {
        String whereClause = SertifikaYardimci.COLUMN_SERTIFIKA_PRIVATE_KEY + " IS NOT NULL ";
        return mIslemler.sorgula(mConnection, DepoSertifika.class, whereClause, null);
    }

    public void sertifikaYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler, Long aDizinNo)
            throws CertStoreException {
        Long dizinNo = aDizinNo;
        Long defaultDizin = DizinYardimci.getDefaultDizinNo();

        if (aDizinNo == null) {
            dizinNo = defaultDizin;
        } else {
            try {
                dizinOku(aDizinNo);
            } catch (NotFoundException e) {
                logger.warn("Warning in JDepoVEN", e);
                dizinNo = defaultDizin;
            }
        }

        if (dizinNo == defaultDizin) {
            try {
                dizinOku(defaultDizin);
            } catch (NotFoundException e) {
                throw new CertStoreException("Default dizin (1 nolu DEPO isimli) veritabaninda bulunamadi", e);
            }
        }

        Long vtsertifikaNo = null;
        try {
            vtsertifikaNo = sertifikaNoHasheGoreBul(aOzetler.get(0).getHashValue());
        } catch (NotFoundException e) {
            logger.warn("Warning in JDepoVEN", e);
            sertifikaYaz(aSertifika, true);
            vtsertifikaNo = aSertifika.getSertifikaNo();
            ozneOzetIliskiYaz(aOzetler, vtsertifikaNo);
        }

        dizinSertifikaIliskiYaz(dizinNo, vtsertifikaNo);

    }

    public Long sertifikaNoHasheGoreBul(byte[] aHash)
            throws CertStoreException {
        String whereClause = OzetYardimci.COLUMN_HASH_VALUE + " = ? AND " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ?";
        DepoOzet ozet = (DepoOzet) mIslemler.tekilSonuc(mConnection, DepoOzet.class, whereClause, new Object[]{aHash, OzneTipi.SERTIFIKA.getIntValue()});
        return ozet.getObjectNo();
    }


    public void sertifikaYaz(DepoSertifika aSertifika, boolean aYeniNesne)
            throws CertStoreException {
        // todo
        mIslemler.yaz(mConnection, aSertifika);
    }

    private void ozneOzetIliskiYaz(List<DepoOzet> aOzetler, long aOzneNo)
            throws CertStoreException {
        for (DepoOzet ozet : aOzetler) {
            ozet.setObjectNo(aOzneNo);
            mIslemler.yaz(mConnection, ozet);
        }
    }

    private void dizinSertifikaIliskiYaz(long aDizinNo, long aSertifikaNo)
            throws CertStoreException {
        String fieldsClause = "(" + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + "," + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + ")";
        String valuesClause = "(?,?)";
        mIslemler.insertSorguCalistir(mConnection, DizinSertifikaYardimci.class, fieldsClause, valuesClause, new Object[]{aDizinNo, aSertifikaNo});
    }

    /**
     * SILINECEK KOKSERTIFIKA
     */

    public void silinecekKokSertifikaYaz(DepoSilinecekKokSertifika aSertifika)
            throws CertStoreException {
        // todo
        mIslemler.yaz(mConnection, aSertifika);
    }

    public ItemSource<DepoSilinecekKokSertifika> silinecekKokSertifikaListele()
            throws CertStoreException {
        return mIslemler.sorgula(mConnection, DepoSilinecekKokSertifika.class, "1=1", null);
    }

    /**
     * OCSP
     */
    public DepoOCSP ocspCevabiOku(long aOCSPNo)
            throws NotFoundException, CertStoreException {
        return (DepoOCSP) mIslemler.yukle(mConnection, DepoOCSP.class, aOCSPNo);
    }


    public int ocspCevabiSil(long aOCSPNo)
            throws CertStoreException {
        int sonuc = mIslemler.sil(mConnection, DepoOCSP.class, aOCSPNo);
        ocspSertifikaIliskiSil(aOCSPNo);
        ozetleriSil(OzneTipi.OCSP_RESPONSE.getIntValue(), aOCSPNo);
        ozetleriSil(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue(), aOCSPNo);
        return sonuc;
    }


    public void ocspSertifikaIliskiSil(long aOCSPNo)
            throws CertStoreException {
        String sorgu = SertifikaOCSPsYardimci.COLUMN_OCSP_NO + " = ? ";
        mIslemler.deleteSorguCalistir(mConnection, SertifikaOCSPsYardimci.class, sorgu, new Object[]{aOCSPNo});
    }

    //TODO ocsp cevabi varsa hatami donsun
    public void ocspCevabiYaz(DepoOCSP aOCSPCevabi, List<DepoOzet> aOzetler)
            throws CertStoreException {
        try {
            ocspCevabiNoHasheGoreBul(aOzetler.get(0).getHashValue());
            throw new CertStoreException("OCSP Cevabi veritabaninda var");
        } catch (NotFoundException e) {
            logger.warn("Warning in JDepoVEN", e);
            mIslemler.yaz(mConnection, aOCSPCevabi);
            ozneOzetIliskiYaz(aOzetler, aOCSPCevabi.getOCSPNo());
        }
    }

    public Long ocspCevabiNoHasheGoreBul(byte[] aHash)
            throws CertStoreException {
        // object tipine bakılacaksa hem ocsp hem basicocsp tipine bakmalı...
        String whereClause = OzetYardimci.COLUMN_HASH_VALUE + " = ? ";//AND " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ?";
        DepoOzet ozet = (DepoOzet) mIslemler.tekilSonuc(mConnection, DepoOzet.class, whereClause, new Object[]{aHash/*, OzneTipi.OCSP_BASIC_RESPONSE.getIntValue()*/});
        return ozet.getObjectNo();
    }

    public ItemSource<DepoOCSP> ocspCevabiListele(String aSorgu, Object[] aParams)
            throws CertStoreException {
        return mIslemler.sorgula(mConnection, DepoOCSP.class, aSorgu, aParams);
    }

    //TODO ocsp cevabi da veritabaninda onceden olabilir.Kontrol et
    public void ocspCevabiVeSertifikaYaz(DepoOCSP aOCSP, List<DepoOzet> aOCSPOzetler, DepoSertifika aSertifika, List<DepoOzet> aSertifikaOzetler, ESingleResponse aSingleResponse)
            throws CertStoreException {
        DepoSertifika vtsertifika = null;
        try {
            vtsertifika = (DepoSertifika) mIslemler.tekilSonuc(mConnection, DepoSertifika.class, SertifikaYardimci.COLUMN_SERTIFIKA_VALUE + " = ?", new Object[]{aSertifika.getValue()});
        } catch (NotFoundException e) {
            logger.warn("Warning in JDepoVEN", e);
            sertifikaYaz(aSertifika, aSertifikaOzetler, null);
            //ozneOzetIliskiYaz(aSertifikaOzetler, aSertifika.getmSertifikaNo());  //sertifikaYaz metodu icinde yapiliyor zaten
            vtsertifika = aSertifika;
        }

        ocspCevabiYaz(aOCSP, aOCSPOzetler);
        //ocspSertifikaIliskiYaz(aOCSP.getmOCSPNo(),vtsertifika.getmSertifikaNo());
        DepoSertifikaOcsps dSertifikaOcsps = CertStoreUtil.toDepoSertifikaOcsps(aSingleResponse, aOCSP, vtsertifika);
        ocspSertifikaIliskiYaz(dSertifikaOcsps);
    }

    public void attributeAndPKICertYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler,List<DepoNitelikSertifikasi> aNitelikSertifikalari, Long aDizinNo)
	throws CertStoreException {
    	DepoSertifika vtsertifika = null;
        try {
            vtsertifika = (DepoSertifika) mIslemler.tekilSonuc(mConnection, DepoSertifika.class, SertifikaYardimci.COLUMN_SERTIFIKA_VALUE + " = ?", new Object[]{aSertifika.getValue()});
        } catch (NotFoundException e) {
            logger.warn("Warning in JDepoVEN", e);
            sertifikaYaz(aSertifika, aOzetler, aDizinNo);
            vtsertifika = aSertifika;
        }
        
        for(DepoNitelikSertifikasi ns:aNitelikSertifikalari)
        {
        	ns.setSertifikaNo(vtsertifika.getSertifikaNo());
        	attributeCertYaz(ns);
        }
    }
    
    public void attributeCertYaz(DepoNitelikSertifikasi aNitelikSertifikasi)
    throws CertStoreException
    {
    	 mIslemler.yaz(mConnection, aNitelikSertifikasi);
    }

    //private void ocspSertifikaIliskiYaz(long aOCSPNo,long aSertifikaNo)
    private void ocspSertifikaIliskiYaz(DepoSertifikaOcsps dSertifikaOcsps)
            throws CertStoreException {
        String fieldsClause = "(" + SertifikaOCSPsYardimci.COLUMN_OCSP_NO + "," + SertifikaOCSPsYardimci.COLUMN_SERTIFIKA_NO + "," + SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + "," + SertifikaOCSPsYardimci.COLUMN_STATUS + "," + SertifikaOCSPsYardimci.COLUMN_REVOCATION_TIME + "," + SertifikaOCSPsYardimci.COLUMN_REVOCATION_REASON + ")";
        String valuesClause = "(?,?,?,?,?,?)";
        mIslemler.insertSorguCalistir(mConnection, SertifikaOCSPsYardimci.class, fieldsClause, valuesClause, new Object[]{dSertifikaOcsps.getOcspNo(), dSertifikaOcsps.getSertifikaNo(), dSertifikaOcsps.getThisUpdate(), dSertifikaOcsps.getStatus(), dSertifikaOcsps.getRevocationTime(), dSertifikaOcsps.getRevocationReason()});
    }

    public ItemSource<DepoOzet> ocspOzetleriniListele(Long aOCSPNo)
            throws CertStoreException {
        String whereClause = "( (" + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? OR "+OzetYardimci.COLUMN_OBJECT_TYPE+" = ?) AND " + OzetYardimci.COLUMN_OBJECT_NO + " = ? )";
        return mIslemler.sorgula(mConnection, DepoOzet.class, whereClause, new Object[]{OzneTipi.OCSP_RESPONSE.getIntValue(),OzneTipi.OCSP_BASIC_RESPONSE.getIntValue(), aOCSPNo});
    }

    /**
     * SISTEMPARAMETRELERI
     */
    public HashMap<String, Object> sistemParametreleriniOku()
            throws CertStoreException {
        HashMap<String, Object> parammap = new HashMap<String, Object>();
        List<DepoSistemParametresi> paramlist;
        ItemSource<DepoSistemParametresi> depoSistemParametresiItemSource = mIslemler.sorgula(mConnection, DepoSistemParametresi.class, "1=1", null);
        try {
            DepoSistemParametresi sp = depoSistemParametresiItemSource.nextItem();
            while (sp != null) {
                parammap.put(sp.getParamAdi(), sp.getParamDegeri());
                sp = depoSistemParametresiItemSource.nextItem();
            }
        } catch (Exception e) {
            throw new CertStoreException(e);
        }
        return parammap;
    }

    public DepoSistemParametresi sistemParametresiOku(String aParamAdi)
            throws NotFoundException, CertStoreException {
        String whereClause = SistemParametresiYardimci.COLUMN_SP_PARAM_ADI + " = ?";
        return (DepoSistemParametresi) mIslemler.tekilSonuc(mConnection, DepoSistemParametresi.class, whereClause, new Object[]{aParamAdi});
    }

    public void sistemParametresiUpdate(String aParamAdi, Object aParamDeger)
            throws CertStoreException {
        String setClause = SistemParametresiYardimci.COLUMN_SP_PARAM_DEGERI + " = ?";
        String whereClause = SistemParametresiYardimci.COLUMN_SP_PARAM_ADI + " = ?";
        mIslemler.updateSorguCalistir(mConnection, DepoSistemParametresi.class, setClause, whereClause, new Object[]{aParamDeger, aParamAdi});
    }
}


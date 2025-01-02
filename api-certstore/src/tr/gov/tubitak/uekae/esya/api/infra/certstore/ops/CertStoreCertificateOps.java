package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DizinSertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzneTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.KeyUsageSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CertStoreCertificateOps {
    private static Logger logger = LoggerFactory.getLogger(CertStoreCertificateOps.class);

    private final CertStore mCertStore;
    private static final Locale EN_LOCALE = new Locale("en");

    public CertStoreCertificateOps(final CertStore aDepo) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        mCertStore = aDepo;
    }

    public DepoSertifika readStoreCertificate(long aSertifikaNo)
            throws CertStoreException {
        if (aSertifikaNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            DepoSertifika sertifika = ven.sertifikaOku(aSertifikaNo);
            return sertifika;
        } catch (NotFoundException e) {
            throw new CertStoreException(aSertifikaNo + " nolu sertifika veritabaninda bulunamadi.", e);
        } catch (CertStoreException e) {
            throw new CertStoreException(aSertifikaNo + " nolu sertifika okunurken veritabani hatasi olustu.", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException(aSertifikaNo + " nolu sertifika okunurken veritabani hatasi olustu.", e);
            }
        }
    }

    public List<ECertificate> listCertificates()
            throws CertStoreException {
        ItemSource<DepoSertifika> depoSertifikaItemSource = listStoreCertificates();
        List<DepoSertifika> list = ((RsItemSource) depoSertifikaItemSource).toList();
        List<ECertificate> elist = new ArrayList<ECertificate>();
        for (DepoSertifika sertifika : list) {
            try {
                ECertificate cer = new ECertificate(sertifika.getValue());
                elist.add(cer);
            } catch (Exception e) {
                throw new CertStoreException(sertifika.getSertifikaNo() + " nolu sertifika asn certificate tipine cevrilirken hata olustu.", e);
            }
        }

        return elist;

    }


    public ItemSource<DepoSertifika> listStoreCertificates()
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ItemSource<DepoSertifika> list = ven.sertifikaListele();
            return list;
        } catch (CertStoreException e) {
            throw new CertStoreException("Tum sertifikalar listelenirken VT hatasi olustu", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException("Tum sertifikalar listelenirken VT hatasi olustu", e);
            }
        }
    }

    public List<ECertificate> listCertificates(CertificateSearchTemplate aSAS)
            throws CertStoreException {
        ItemSource<DepoSertifika> depoSertifikaItemSource = listStoreCertificate(aSAS);
        List<ECertificate> elist = new ArrayList<ECertificate>();
        DepoSertifika sertifika;
        try {
            sertifika = depoSertifikaItemSource.nextItem();
            while (sertifika != null) {
                ECertificate cer = new ECertificate(sertifika.getValue());
                elist.add(cer);
                sertifika = depoSertifikaItemSource.nextItem();
            }
        } catch (Exception e) {
            throw new CertStoreException(e);
        }
        return elist;
    }


    public ItemSource<DepoSertifika> listStoreCertificate(CertificateSearchTemplate aSAS)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = _createQuery(aSAS, false);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu, params.toArray());
            return depoSertifikaItemSource;
        } catch (CertStoreException e) {
            throw new CertStoreException("Sablona gore sertifika listeleme sirasinda VT hatasi olustu.", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException("Sablona gore sertifika listeleme sirasinda VT hatasi olustu.", e);
            }
        }
    }

    public ItemSource<DepoSertifika> findFreshestCertificate(CertificateSearchTemplate aSAS)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = _createQuery(aSAS, true);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu, params.toArray());
            return depoSertifikaItemSource;
        } catch (CertStoreException e) {
            throw new CertStoreException("Sablona gore en guncel sertifika bulma sirasinda VT hatasi olustu.", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException("Sablona gore en guncel sertifika bulma sirasinda VT hatasi olustu.", e);
            }
        }
    }

    public void sertifikaTasi(long aSertifikaNo, long aEskiDizinNo, long aYeniDizinNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            if (aSertifikaNo <= 0 || aEskiDizinNo <= 0 || aYeniDizinNo <= 0) {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.sertifikaTasi(aSertifikaNo, aEskiDizinNo, aYeniDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException e) {
            if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSertifikaNo + " nolu sertifikayi " + aEskiDizinNo + " nolu dizinden " +
                    aYeniDizinNo + " nolu dizine tasirken VT hatasi olustu.", e);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }


    public List<DepoDizin> listCertificateDirectories(long aSertifikaNo)
            throws CertStoreException {
        if (aSertifikaNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            List<DepoDizin> list = ((RsItemSource)ven.sertifikaDizinleriniListele(aSertifikaNo)).toList();
            return list;
        } catch (CertStoreException e) {
            throw new CertStoreException(aSertifikaNo + " nolu sertifikanin dizinleri listelenirken veritabani hatasi olustu", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException(aSertifikaNo + " nolu sertifikanin dizinleri listelenirken veritabani hatasi olustu", e);
            }
        }
    }

    public void deleteCertificate(long aSertifikaNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            if (aSertifikaNo <= 0) {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.sertifikaSil(aSertifikaNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException aEx) {
            if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSertifikaNo + " nolu sertifikanin silinirken VT hatasi olustu." +
                    "Yapilan VT islemleri geri alindi.", aEx);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public int deleteCertificate(CertificateSearchTemplate aSAS)
            throws CertStoreException {
        Long dizinNo = aSAS.getDizinNo();
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = _createQuery(aSAS, false);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu, params.toArray());
            int etkilenen = 0;

            DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();

            while(depoSertifika != null)
            {
                if(dizinNo == null)
                    etkilenen += ven.sertifikaSil(depoSertifika.getSertifikaNo());
                else
                    etkilenen += ven.dizindenSertifikaSil(depoSertifika.getSertifikaNo(), dizinNo);

                depoSertifika = depoSertifikaItemSource.nextItem();
            }
            JDBCUtil.commit(mCertStore.getConn());
            return etkilenen;
        } catch (Exception e) {
            if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("Sablona gore sertifika silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        }/* finally {
            JDBCUtil.close(oturum);
        }*/

    }

    public void deleteCertificateFromDirectory(long aSertifikaNo, long aDizinNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            if (aSertifikaNo <= 0 || aDizinNo <= 0) {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.dizindenSertifikaSil(aSertifikaNo, aDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException e) {
            if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSertifikaNo + " nolu sertifika " + aDizinNo +
                    " nolu dizinden silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        } /*finally {
            JDBCUtil.close(oturum);
        } */
    }


    public void writeCertificate(byte[] aCert, long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, null, null, null, null, aDizinNo, null);
    }
    
    public void writeCertificate(byte[] aCert, long aDizinNo, String aX400Address)
    throws CertStoreException {
    	_writeCertificate(aCert, null, null, null, null, aDizinNo, aX400Address);
}

    public void writeCertificate(ECertificate aCert, long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, null, null, null, null, aDizinNo,null);
    }

    public void writeCertificate(byte[] aCert, String aPKCS11Lib, byte[] aPKCS11ID, Long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, aPKCS11Lib, aPKCS11ID, null, null, aDizinNo, null);
    }

    public void writeCertificate(ECertificate aCert, String aPKCS11Lib, byte[] aPKCS11ID, Long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, aPKCS11Lib, aPKCS11ID, null, null, aDizinNo,null);
    }

    public void writeCertificate(byte[] aCert, byte[] aPrivateKey, String aDepoParola, Long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, null, null, aPrivateKey, aDepoParola, aDizinNo,null);
    }

    public void writeCertificate(ECertificate aCert, byte[] aPrivateKey, String aDepoParola, Long aDizinNo)
            throws CertStoreException {
        _writeCertificate(aCert, null, null, aPrivateKey, aDepoParola, aDizinNo,null);
    }

    private void _writeCertificate(byte[] aCert, String aPKCS11Lib, byte[] aPKCS11ID, byte[] aPrivateKey, String aDepoParolasi, Long aDizinNo,String aX400Address)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            DepoSertifika sertifika = null;
            try {
                ECertificate esyacer = new ECertificate(aCert);
                sertifika = CertStoreUtil.eSYASertifikaTODepoSertifika(esyacer);
                if (aPKCS11Lib != null)
                    sertifika.setPKCS11Lib(aPKCS11Lib);
                if (aPKCS11ID != null)
                    sertifika.setPKCS11ID(aPKCS11ID);
                if (aPrivateKey != null && aDepoParolasi != null) {
                    byte[] sifreliParola = mCertStore.encryptWithPassword(aPrivateKey, aDepoParolasi);
                    sertifika.setPrivateKey(sifreliParola);
                }
                if(aX400Address != null)
                	sertifika.setX400Address(aX400Address);
            } catch (Exception e) {
                throw new CertStoreException("Sertifika yazilirken,depo sertifika nesnesine cevrim sirasinda hata olustu", e);
            }
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());

            List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(sertifika.getValue(), OzneTipi.SERTIFIKA);
            ven.sertifikaYaz(sertifika, ozetler, aDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException aEx) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("Sertifika yazilirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    private void _writeCertificate(ECertificate aCert, String aPKCS11Lib, byte[] aPKCS11ID, byte[] aPrivateKey, String aDepoParolasi, long aDizinNo,String aX400Address)
            throws CertStoreException {
        byte[] encoded = null;
        try {
            encoded = aCert.getEncoded();
        } catch (Exception e) {
            throw new CertStoreException("Sertifika encode edilirken hata olustu.", e);
        }
        _writeCertificate(encoded, aPKCS11Lib, aPKCS11ID, aPrivateKey, aDepoParolasi, aDizinNo,null);

    }


    private Pair<String, List<Object>> _createQuery(CertificateSearchTemplate aSAS, boolean aGuncel) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer("");

        sb.append("1=1");

        if (aSAS != null) {
            Long dizinNo = aSAS.getDizinNo();
            if (dizinNo != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_NO + " IN (SELECT " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " FROM " +
                        DizinSertifikaYardimci.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DizinSertifikaYardimci.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? )");
                params.add(dizinNo);
            }

            byte[] hash = aSAS.getHash();
            OzetTipi hashtype = aSAS.getHashType();
            if (hash != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                        OzetYardimci.OZET_TABLO_ADI + " WHERE (" + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_HASH_VALUE + " = ? ");
                params.add(OzneTipi.SERTIFIKA.getIntValue());
                params.add(hash);

                if (hashtype != null) {
                    sb.append(" AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                    params.add(hashtype.getIntValue());
                } else {
                    sb.append(" ))");
                }
            } else {
                if (hashtype != null) {
                    sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                            OzetYardimci.OZET_TABLO_ADI + " WHERE (" + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                    params.add(OzneTipi.SERTIFIKA.getIntValue());
                    params.add(hashtype.getIntValue());
                }
            }


            byte[] value = aSAS.getValue();
            if (value != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_VALUE + " = ? ");
                params.add(value);
            }

            byte[] issuer = aSAS.getIssuer();
            if (issuer != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_ISSUER + " = ? ");
                params.add(issuer);
            }

            Date startDate = aSAS.getStartDate();
            if (startDate != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_START_DATE + " <= ? ");
                params.add(startDate);
            }

            Date endDate = aSAS.getEndDate();
            if (endDate != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_END_DATE + " >= ? ");
                params.add(endDate);
            }

            byte[] serial = aSAS.getSerialNumber();
            if (serial != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_SERIAL + " = ? ");
                params.add(serial);
            }

            byte[] subject = aSAS.getSubject();
            if (subject != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_SUBJECT + " = ? ");
                params.add(subject);
            }

            String eposta = aSAS.getEPosta();
            if (eposta != null) {
                String keposta = eposta.toLowerCase(EN_LOCALE);
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_EPOSTA + " = ?");
                params.add(keposta);
            }


            byte[] subjectkeyid = aSAS.getSubjectKeyID();
            if (subjectkeyid != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_SUBJECT_KEY_ID + " = ?");
                params.add(subjectkeyid);
            }

            KeyUsageSearchTemplate anahtarkullanim = aSAS.getAnahtarKullanim();
            if (anahtarkullanim != null) {
                sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_KEYUSAGE + " LIKE ?");
                params.add(anahtarkullanim.sorguOlustur());
            }
            
            String x400address = aSAS.getX400Address();
            if(x400address != null) {
            	sb.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_X400ADDRESS + " LIKE ?");
                params.add(x400address);
            }
            //boolean guncel = aSAS.getmGuncel();
            if (aGuncel) {
                sb.append(" ORDER BY " + SertifikaYardimci.COLUMN_SERTIFIKA_END_DATE + " DESC ");
            }
        }

        return new Pair<String, List<Object>>(sb.toString(), params);
    }

}

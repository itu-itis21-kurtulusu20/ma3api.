package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
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
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.*;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CRLSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class CertStoreCRLOps {
    private static Logger logger = LoggerFactory.getLogger(CertStoreCRLOps.class);

    private final CertStore mCertStore;

    public CertStoreCRLOps(final CertStore aDepo) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
        mCertStore = aDepo;
    }

    public DepoSIL readStoreCRL(Long aSILNo)
            throws CertStoreException {
        if (aSILNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            DepoSIL sIL = ven.sILOku(aSILNo);
            return sIL;
        } catch (NotFoundException e) {
            throw new CertStoreException(aSILNo + " nolu SIL veritabaninda bulunamadi.", e);
        } catch (CertStoreException e) {
            throw new CertStoreException(aSILNo + " nolu SIL okunurken veritabani hatasi olustu.", e);
        } /*finally {
            try {
                JDBCUtil.close(oturum);
            } catch (CertStoreException aEx) {
                throw new CertStoreException(aSILNo + " nolu SIL okunurken veritabani hatasi olustu.", aEx);
            }
        }*/
    }

    public void writeCRL(ECRL aCRL, Long aDizinNo)
            throws CertStoreException {
        writeCRL(aCRL.getEncoded(), aDizinNo);
    }


    public void writeCRL(byte[] aCRL, Long aDizinNo)
            throws CertStoreException {
        DepoSIL sil = null;
        try {
            sil = CertStoreUtil.asnCRLTODepoSIL(aCRL);
        } catch (Exception e) {
            throw new CertStoreException("Asn sil yapisindan veritabani yapisina cevirim sirasinda hata olustu", e);
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(sil.getValue(), OzneTipi.SIL);
            ven.sILYaz(sil, ozetler, aDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException e) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("SIL yazilirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public ItemSource<DepoSIL> listStoreCRL()
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ItemSource<DepoSIL> depoSILItemSource = ven.sILListele();
            return depoSILItemSource;
        } catch (CertStoreException e) {
            throw new CertStoreException("Tum SIL ler listelenirken VT hatasi olustu", e);
        } finally {
            try {
                JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException("Tum SIL ler listelenirken VT hatasi olustu", e);
            }
        }
    }

    public List<ECRL> listCRL(CRLSearchTemplate aSAS, SILTipi[] aTipler)
            throws CertStoreException {
        ItemSource<DepoSIL> depoSILItemSource = listStoreCRL(aSAS, aTipler);
        List<ECRL> list = new ArrayList<ECRL>();
        try {
            DepoSIL depoSIL = depoSILItemSource.nextItem();
            while (depoSIL != null) {
                list.add(new ECRL(depoSIL.getValue()));
                depoSIL = depoSILItemSource.nextItem();
            }
        } catch (Exception e) {
            throw new CertStoreException(e);
        }
        return list;
    }


    public ItemSource<DepoSIL> listStoreCRL(CRLSearchTemplate aSAS, SILTipi[] aTipler)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = createQuery(aSAS, aTipler);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoSIL> depoSILItemSource = ven.sILListele(sorgu, params.toArray());
            return depoSILItemSource;
        } catch (CertStoreException e) {
            throw new CertStoreException("Sablona gore SIL arama sirasinda VT hatasi olustu.", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException("Sablona gore SIL arama sirasinda VT hatasi olustu.", e);
            }
        }
    }

    public List<DepoDizin> listCRLDirectories(long aSILNo)
            throws CertStoreException {
        if (aSILNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }

        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            List<DepoDizin> list = ((RsItemSource) ven.sILDizinleriniListele(aSILNo)).toList();
            return list;
        } catch (CertStoreException e) {
            throw new CertStoreException(aSILNo + " nolu SIL in dizinleri listelenirken VT hatasi olustu.", e);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException e) {
                throw new CertStoreException(aSILNo + " nolu SIL in dizinleri listelenirken VT hatasi olustu.", e);
            }
        }
    }


    public void deleteCRLFromDirectory(long aSILNo, long aDizinNo)
            throws CertStoreException {
        if (aSILNo <= 0 || aDizinNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.dizindenSILSil(aSILNo, aDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException aEx) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSILNo + " nolu SIL " + aDizinNo + " nolu dizinden silinirken VT hatasi olustu." +
                    "Yapilan VT islemleri geri alinacak.", aEx);
        }/* finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public void moveCRL(long aSILNo, long aEskiDizinNo, long aYeniDizinNo)
            throws CertStoreException {
        if (aSILNo <= 0 || aEskiDizinNo <= 0 || aYeniDizinNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.sILTasi(aSILNo, aEskiDizinNo, aYeniDizinNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException e) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSILNo + " nolu SIL, " + aEskiDizinNo + " nolu dizinden " +
                    aYeniDizinNo + " nolu dizine tasinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        }/* finally {
            JDBCUtil.close(oturum);
        }*/
    }

    public void deleteCRL(long aSILNo)
            throws CertStoreException {
        if (aSILNo <= 0) {
            throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
        }
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ven.sILSil(aSILNo);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException e) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aSILNo + " nolu SIL silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        }/* finally {
            JDBCUtil.close(oturum);
        }*/
    }


    public int deleteCRL(CRLSearchTemplate aSAS)
            throws CertStoreException {
        //Connection oturum = null;
        Long dizinNo = aSAS.getDizinNo();
        SILTipi[] tipler = {SILTipi.BASE, SILTipi.DELTA};
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = createQuery(aSAS, tipler);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoSIL> depoSILItemSource = ven.sILListele(sorgu, params.toArray());
            int etkilenen = 0;

            DepoSIL depoSIL = depoSILItemSource.nextItem();
            while (depoSIL != null) {
                if (dizinNo == null)
                    etkilenen += ven.sILSil(depoSIL.getSILNo());
                else
                    etkilenen += ven.dizindenSILSil(depoSIL.getSILNo(), dizinNo);

                depoSIL = depoSILItemSource.nextItem();
            }

            JDBCUtil.commit(mCertStore.getConn());
            return etkilenen;
        } catch (Exception e) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("Sablona gore SIL silme isleminde hata olustu.", e);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    private Pair<String, List<Object>> createQuery(CRLSearchTemplate aSAS, SILTipi[] aTipler) {
        StringBuffer sb = new StringBuffer("");
        List<Object> params = new ArrayList<Object>();
        sb.append("1=1");

        if (aTipler != null && aTipler.length != 0) {
            sb.append(" AND ");
            sb.append(SILYardimci.COLUMN_SIL_TIPI + " IN ");
            sb.append("(");
            for (int i = 0; i < aTipler.length - 1; i++) {
                sb.append(aTipler[i].getIntValue() + " , ");
            }
            sb.append(aTipler[aTipler.length - 1].getIntValue());
            sb.append(")");
        }

        if (aSAS != null) {
            Long dizinNo = aSAS.getDizinNo();
            if (dizinNo != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_NO + " IN (SELECT " + DizinSILYardimci.COLUMN_DIZINSIL_SIL_NO + " FROM " +
                        DizinSILYardimci.DIZINSIL_TABLO_ADI + " WHERE " + DizinSILYardimci.COLUMN_DIZINSIL_DIZIN_NO + " = ? )");
                params.add(dizinNo);
            }

            byte[] value = aSAS.getValue();
            if (value != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_VALUE + " = ? ");
                params.add(value);
            }

            byte[] hash = aSAS.getHash();
            OzetTipi hashtype = aSAS.getHashType();
            if (hash != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                        OzetYardimci.OZET_TABLO_ADI + " WHERE ( " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_HASH_VALUE + " = ? ");
                params.add(OzneTipi.SIL.getIntValue());
                params.add(hash);

                if (hashtype != null) {
                    sb.append(" AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                    params.add(hashtype.getIntValue());
                } else {
                    sb.append(" ))");
                }
            } else {
                if (hashtype != null) {
                    sb.append(" AND " + SILYardimci.COLUMN_SIL_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                            OzetYardimci.OZET_TABLO_ADI + " WHERE ( " + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                    params.add(OzneTipi.SIL.getIntValue());
                    params.add(hashtype.getIntValue());
                }
            }

            byte[] issuer = aSAS.getIssuer();
            if (issuer != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_ISSUER_NAME + " = ? ");
                params.add(issuer);
            }

            byte[] sILNumber = aSAS.getSILNumber();
            if (sILNumber != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_NUMBER + " = ?");
                params.add(sILNumber);
            }

            Date publishedBefore = aSAS.getPublishedBefore();
            if (publishedBefore != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_THIS_UPDATE + " < ? ");
                params.add(publishedBefore);
            }

            Date publishedAfter = aSAS.getPublishedAfter();
            if (publishedAfter != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_THIS_UPDATE + " >= ? ");
                params.add(publishedAfter);
            }

            Date validAt = aSAS.getValidAt();
            if (validAt != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_THIS_UPDATE + " <= ? AND ? <= " + SILYardimci.COLUMN_SIL_NEXT_UPDATE + " ");
                params.add(validAt);
                params.add(validAt);
            }

            byte[] baseSILNumber = aSAS.getBaseSILNumber();
            if (baseSILNumber != null) {
                sb.append(" AND " + SILYardimci.COLUMN_SIL_BASE_SIL_NUMBER + " = ? ");
                params.add(baseSILNumber);
            }
        }

        return new Pair<String, List<Object>>(sb.toString(), params);
    }

}

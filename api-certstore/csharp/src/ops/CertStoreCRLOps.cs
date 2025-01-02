using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;
using tr.gov.tubitak.uekae.esya.api.infra.db.certstore.core.helper;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    //todo Annotation!
    //@ApiClass
    public class CertStoreCRLOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore mCertStore;

        public CertStoreCRLOps(CertStore aDepo)
        {
            CertStoreUtil.CheckLicense();

            mCertStore = aDepo;
        }

        public DepoSIL readStoreCRL(long aSILNo)
        {
            if (aSILNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                DepoSIL sIL = ven.sILOku(aSILNo);
                return sIL;
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new CertStoreException(aSILNo + " nolu SIL veritabaninda bulunamadi.", aEx);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aSILNo + " nolu SIL okunurken veritabani hatasi olustu.", aEx);
            }
        }

        public void writeCRL(/*CertificateList*/ECRL aCRL, long aDizinNo)
        {
            writeCRL(aCRL.getEncoded(), aDizinNo);
        }


        public void writeCRL(byte[] aCRL, long aDizinNo)
        {
            DepoSIL sil = null;
            try
            {
                sil = CertStoreUtil.asnCRLTODepoSIL(aCRL);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Asn sil yapisindan veritabani yapisina cevirim sirasinda hata olustu", aEx);
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(sil.getValue(), OzneTipi.SIL);
                    ven.sILYaz(sil, ozetler, aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("SIL yazilirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        public ItemSource<DepoSIL> listStoreCRL()
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                ItemSource<DepoSIL> list = ven.sILListele();
                return list;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Tum SIL ler listelenirken VT hatasi olustu", aEx);
            }
        }

        public List<ECRL> listCRL(CRLSearchTemplate aSAS, CRLType[] aTipler)
        {
            ItemSource<DepoSIL> depoSILItemSource = listStoreCRL(aSAS, aTipler);
            List<ECRL> crlList = new List<ECRL>();

            DepoSIL depoSIL = depoSILItemSource.nextItem();
            while (depoSIL != null)
            {
                try
                {
                    ECRL crl = new ECRL(depoSIL.getValue());
                    crlList.Add(crl);
                    depoSIL = depoSILItemSource.nextItem();
                }
                catch (Exception e)
                {
                    throw new CertStoreException(
                        depoSIL.getSILNo() + " nolu sil asn sil tipine cevrilirken hata olustu.", e);
                }
            }

            return crlList;
        }

        public ItemSource<DepoSIL> listStoreCRL(CRLSearchTemplate aSAS, /*SILTipi*/CRLType[] aTipler)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            try
            {
                Pair<String, List<Object>> ikili = createQuery(aSAS, aTipler);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                ItemSource<DepoSIL> depoSILItemSource = ven.sILListele(sorgu, params_.ToArray());
                return depoSILItemSource;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sablona gore SIL arama sirasinda VT hatasi olustu.", aEx);
            }
        }

        public List<DepoDizin> listCRLDirectories(long aSILNo)
        {
            if (aSILNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }

            using (DbConnection connection = CertStoreDBLayer.newConnection(mCertStore.getStoreName()))
            {
                DepoVEN ven = CertStoreDBLayer.newDepoVEN(connection);
                try
                {
                    List<DepoDizin> list = ((RsItemSource<DepoDizin>) (ven.sILDizinleriniListele(aSILNo))).toList();
                    return list;
                }
                catch (Exception aEx)
                {
                    throw new CertStoreException(aSILNo + " nolu SIL in dizinleri listelenirken VT hatasi olustu.", aEx);
                }
            }
        }


        public void deleteCRLFromDirectory(long aSILNo, long aDizinNo)
        {
            if (aSILNo <= 0 || aDizinNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.dizindenSILSil(aSILNo, aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aSILNo + " nolu SIL " + aDizinNo + " nolu dizinden silinirken VT hatasi olustu." +
                            "Yapilan VT islemleri geri alinacak.", aEx);
                }
            }
        }

        public void moveCRL(long aSILNo, long aEskiDizinNo, long aYeniDizinNo)
        {
            if (aSILNo <= 0 || aEskiDizinNo <= 0 || aYeniDizinNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.sILTasi(aSILNo, aEskiDizinNo, aYeniDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aSILNo + " nolu SIL, " + aEskiDizinNo + " nolu dizinden " +
                            aYeniDizinNo + " nolu dizine tasinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        public void deleteCRL(long aSILNo)
        {
            if (aSILNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.sILSil(aSILNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aSILNo + " nolu SIL silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }


        public int deleteCRL(CRLSearchTemplate aSAS)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                long? dizinNo = aSAS.getDizinNo();
                CRLType[] tipler = { CRLType.BASE, CRLType.DELTA };
                try
                {
                    Pair<String, List<Object>> ikili = createQuery(aSAS, tipler);
                    String sorgu = ikili.first();
                    List<Object> params_ = ikili.second();
                    ItemSource<DepoSIL> depoSILItemSource = ven.sILListele(sorgu, params_.ToArray());
                    int etkilenen = 0;

                    DepoSIL depoSIL = depoSILItemSource.nextItem();
                    while (depoSIL != null)
                    {
                        if (dizinNo == null)
                            etkilenen += ven.sILSil(depoSIL.getSILNo());
                        else
                            etkilenen += ven.dizindenSILSil(depoSIL.getSILNo(), dizinNo);

                        depoSIL = depoSILItemSource.nextItem();
                    }
                    transaction.Commit();
                    return etkilenen;
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Sablona gore SIL silme isleminde hata olustu.", aEx);
                }
            }
        }
        private Pair<String, List<Object>> createQuery(CRLSearchTemplate aSAS, CRLType[] aTipler)
        {
            StringBuilder sb = new StringBuilder("");
            List<Object> params_ = new List<Object>();
            sb.Append("1=1");

            if (aTipler != null && aTipler.Length != 0)
            {
                sb.Append(" AND ");
                sb.Append(CRLHelper.COLUMN_SIL_TIPI + " IN ");
                sb.Append("(");
                for (int i = 0; i < aTipler.Length - 1; i++)
                {
                    sb.Append(aTipler[i].getIntValue() + " , ");
                }
                sb.Append(aTipler[aTipler.Length - 1].getIntValue());
                sb.Append(")");
            }

            if (aSAS != null)
            {
                long? dizinNo = aSAS.getDizinNo();
                if (dizinNo != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_NO + " IN (SELECT " + DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " FROM " +
                        DirectoryCRLHelper.DIZINSIL_TABLO_ADI + " WHERE " + DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " = ? )");
                    params_.Add(dizinNo);
                }

                byte[] value = aSAS.getValue();
                if (value != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_VALUE + " = ? ");
                    params_.Add(value);
                }

                byte[] hash = aSAS.getHash();
                OzetTipi hashtype = aSAS.getHashType();
                if (hash != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                            HashHelper.OZET_TABLO_ADI + " WHERE ( " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_VALUE + " = ? ");
                    params_.Add(OzneTipi.SIL.getIntValue());
                    params_.Add(hash);
                    if (hashtype != null)
                    {
                        sb.Append(" AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                        params_.Add(hashtype.getIntValue());
                    }
                    else
                    {
                        sb.Append(" ))");
                    }

                }
                else
                {
                    if (hashtype != null)
                    {
                        sb.Append(" AND " + CRLHelper.COLUMN_SIL_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                                HashHelper.OZET_TABLO_ADI + " WHERE ( " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                        params_.Add(OzneTipi.SIL.getIntValue());
                        params_.Add(hashtype.getIntValue());
                    }
                }

                byte[] issuer = aSAS.getIssuer();
                if (issuer != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_ISSUER_NAME + " = ? ");
                    params_.Add(issuer);
                }

                byte[] sILNumber = aSAS.getSILNumber();
                if (sILNumber != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_NUMBER + " = ?");
                    params_.Add(sILNumber);
                }

                DateTime? publishedBefore = aSAS.getPublishedBefore();

                if (publishedBefore != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_THIS_UPDATE + " < ? ");
                    params_.Add(publishedBefore);
                }

                DateTime? publishedAfter = aSAS.getPublishedAfter();
                if (publishedAfter != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_THIS_UPDATE + " >= ? ");
                    params_.Add(publishedAfter);
                }

                DateTime? validAt = aSAS.getValidAt();
                if (validAt != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_THIS_UPDATE + " <= ? AND ? <= " + CRLHelper.COLUMN_SIL_NEXT_UPDATE + " ");
                    params_.Add(validAt);
                    params_.Add(validAt);
                }

                byte[] baseSILNumber = aSAS.getBaseSILNumber();
                if (baseSILNumber != null)
                {
                    sb.Append(" AND " + CRLHelper.COLUMN_SIL_BASE_SIL_NUMBER + " = ? ");
                    params_.Add(baseSILNumber);
                }
            }

            return new Pair<String, List<Object>>(sb.ToString(), params_);
        }
    }
}

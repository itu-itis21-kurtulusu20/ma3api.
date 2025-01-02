using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    //todo Annotation!
    //@ApiClass
    public class CertStoreCertificateOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore mCertStore;
        //private static readonly Locale EN_LOCALE = new Locale("en");

        public CertStoreCertificateOps(CertStore aDepo)
        {
            CertStoreUtil.CheckLicense();

            mCertStore = aDepo;
        }

        public DepoSertifika readStoreCertificate(long aSertifikaNo)
        {
            if (aSertifikaNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            try
            {
                DepoSertifika sertifika = ven.sertifikaOku(aSertifikaNo);
                return sertifika;
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new CertStoreException(aSertifikaNo + " nolu sertifika veritabaninda bulunamadi.", aEx);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aSertifikaNo + " nolu sertifika okunurken veritabani hatasi olustu.",
                    aEx);
            }

        }

        public List<ECertificate> listCertificates()
        {
            ItemSource<DepoSertifika> depoSertifikaItemSource = listStoreCertificates();

            List<DepoSertifika> list = ((RsItemSource<DepoSertifika>) depoSertifikaItemSource).toList();
            List<ECertificate> elist = new List<ECertificate>();
            foreach (DepoSertifika sertifika in list)
            {
                try
                {
                    ECertificate cer = new ECertificate(sertifika.getValue());
                    elist.Add(cer);
                }
                catch (Exception aEx)
                {
                    throw new CertStoreException(
                        sertifika.getSertifikaNo() + " nolu sertifika asn certificate tipine cevrilirken hata olustu.",
                        aEx);
                }
            }

            return elist;

        }

        public ItemSource<DepoSertifika> listStoreCertificates()
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                ItemSource<DepoSertifika> list = ven.sertifikaListele();
                return list;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Tum sertifikalar listelenirken VT hatasi olustu", aEx);
            }
        }

        public List<ECertificate> listCertificates(CertificateSearchTemplate aSAS)
        {
            ItemSource<DepoSertifika> depoSertifikaItemSource = listStoreCertificate(aSAS);
            List<ECertificate> certList = new List<ECertificate>();

            DepoSertifika depoSertifika = null;
            try
            {
                depoSertifika = depoSertifikaItemSource.nextItem();
                while (depoSertifika != null)
                {
                    ECertificate cer = new ECertificate(depoSertifika.getValue());
                    certList.Add(cer);
                    depoSertifika = depoSertifikaItemSource.nextItem();
                }
            }
            catch (Exception e)
            {
                if (depoSertifika != null)
                    throw new CertStoreException(
                        depoSertifika.getSertifikaNo() + " nolu sertifika asn certificate tipine cevrilirken hata olustu.", e);
                throw new CertStoreException("Depodaki Sertifikalar listelenirken hata oluştu.", e);
            }
            return certList;
        }


        public ItemSource<DepoSertifika> listStoreCertificate(CertificateSearchTemplate aSAS)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                Pair<String, List<Object>> ikili = _createQuery(aSAS, false);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu, params_.ToArray());
                return depoSertifikaItemSource;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sablona gore sertifika listeleme sirasinda VT hatasi olustu.", aEx);
            }
        }

        public ItemSource<DepoSertifika> findFreshestCertificate(CertificateSearchTemplate aSAS)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                Pair<String, List<Object>> ikili = _createQuery(aSAS, true);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu, params_.ToArray());
                return depoSertifikaItemSource;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sablona gore en guncel sertifika bulma sirasinda VT hatasi olustu.",
                    aEx);
            }
        }

        public void sertifikaTasi(long aSertifikaNo, long aEskiDizinNo, long aYeniDizinNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    if (aSertifikaNo <= 0 || aEskiDizinNo <= 0 || aYeniDizinNo <= 0)
                    {
                        throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
                    }
                    ven.sertifikaTasi(aSertifikaNo, aEskiDizinNo, aYeniDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(
                        aSertifikaNo + " nolu sertifikayi " + aEskiDizinNo + " nolu dizinden " +
                        aYeniDizinNo + " nolu dizine tasirken VT hatasi olustu.", aEx);
                }
            }
        }


        public List<DepoDizin> listCertificateDirectories(long aSertifikaNo)
        {
            if (aSertifikaNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                List<DepoDizin> list =
                    ((RsItemSource<DepoDizin>) ven.sertifikaDizinleriniListele(aSertifikaNo)).toList();
                return list;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(
                    aSertifikaNo + " nolu sertifikanin dizinleri listelenirken veritabani hatasi olustu", aEx);
            }
        }

        public void deleteCertificate(long aSertifikaNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    if (aSertifikaNo <= 0)
                    {
                        throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
                    }
                    ven.sertifikaSil(aSertifikaNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aSertifikaNo + " nolu sertifikanin silinirken VT hatasi olustu." +
                                                 "Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        public int deleteCertificate(CertificateSearchTemplate aSAS)
        {
            long? dizinNo = aSAS.getDizinNo();
            using (DbConnection connection = CertStoreDBLayer.newConnection(mCertStore.getStoreName()))
            {
                DepoVEN ven = CertStoreDBLayer.newDepoVEN(connection); 
                using (DbTransaction transaction = connection.BeginTransaction())
                {
                    try
                    {
                        Pair<String, List<Object>> ikili = _createQuery(aSAS, false);
                        String sorgu = ikili.first();
                        List<Object> params_ = ikili.second();
                        ItemSource<DepoSertifika> depoSertifikaItemSource = ven.sertifikaListele(sorgu,
                                                                                                 params_.ToArray());
                        int etkilenen = 0;
                        DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();

                        while (depoSertifika != null)
                        {
                            if (dizinNo == null)
                                etkilenen += ven.sertifikaSil(depoSertifika.getSertifikaNo());
                            else
                                etkilenen += ven.dizindenSertifikaSil(depoSertifika.getSertifikaNo(), dizinNo);

                            depoSertifika = depoSertifikaItemSource.nextItem();
                        }
                        transaction.Commit();
                        return etkilenen;
                    }
                    catch (Exception aEx)
                    {
                        transaction.Rollback();
                        throw new CertStoreException(
                            "Sablona gore sertifika silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                    }
                }
            }

        }

        public void deleteCertificateFromDirectory(long aSertifikaNo, long aDizinNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    if (aSertifikaNo <= 0 || aDizinNo <= 0)
                    {
                        throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
                    }
                    ven.dizindenSertifikaSil(aSertifikaNo, aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aSertifikaNo + " nolu sertifika " + aDizinNo +
                                                 " nolu dizinden silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.",
                                                 aEx);
                }
            }
        }



        public void writeCertificate(byte[] aCert, long aDizinNo)
        {            
            _writeCertificate(aCert, null, null, null, null, aDizinNo, null);
        }

        public void writeCertificate(byte[] aCert, long aDizinNo, String aX400Address)
        {
            _writeCertificate(aCert, null, null, null, null, aDizinNo, aX400Address);
        }
    

        public void writeCertificate(ECertificate aCert, long aDizinNo)
        {
            _writeCertificate(aCert, null, null, null, null, aDizinNo, null);
        }

        public void writeCertificate(byte[] aCert, String aPKCS11Lib, byte[] aPKCS11ID, long aDizinNo)
        {            
            _writeCertificate(aCert, aPKCS11Lib, aPKCS11ID, null, null, aDizinNo, null);
        }

        public void writeCertificate(ECertificate aCert, String aPKCS11Lib, byte[] aPKCS11ID, long aDizinNo)
        {            
            _writeCertificate(aCert, aPKCS11Lib, aPKCS11ID, null, null, aDizinNo, null);
        }

        public void writeCertificate(byte[] aCert, byte[] aPrivateKey, String aDepoParola, long aDizinNo)
        {            
            _writeCertificate(aCert, null, null, aPrivateKey, aDepoParola, aDizinNo, null);
        }

        public void writeCertificate(ECertificate aCert, byte[] aPrivateKey, String aDepoParola, long aDizinNo)
        {
            _writeCertificate(aCert, null, null, aPrivateKey, aDepoParola, aDizinNo, null);
        }
        

        private void _writeCertificate(byte[] aCert, String aPKCS11Lib, byte[] aPKCS11ID, byte[] aPrivateKey, String aDepoParolasi, long aDizinNo, String aX400Address)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    DepoSertifika sertifika = null;
                    try
                    {
                        ECertificate esyacer = new ECertificate(aCert);
                        sertifika = CertStoreUtil.eSYASertifikaTODepoSertifika(esyacer);
                        if (aPKCS11Lib != null)
                            sertifika.setPKCS11Lib(aPKCS11Lib);
                        if (aPKCS11ID != null)
                            sertifika.setPKCS11ID(aPKCS11ID);
                        if (aPrivateKey != null && aDepoParolasi != null)
                        {
                            byte[] sifreliParola = mCertStore.encryptWithPassword(aPrivateKey, aDepoParolasi);
                            sertifika.setPrivateKey(sifreliParola);
                        }
                        if (aX400Address != null)
                            sertifika.setX400Address(aX400Address);

                    }
                    catch (Exception aEx)
                    {
                        throw new CertStoreException("Sertifika yazilirken,depo sertifika nesnesine cevrim sirasinda hata olustu", aEx);
                    }

                    List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(sertifika.getValue(), OzneTipi.SERTIFIKA);
                    ven.sertifikaYaz(sertifika, ozetler, aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Sertifika yazilirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }
         

        private void _writeCertificate(ECertificate aCert, String aPKCS11Lib, byte[] aPKCS11ID, byte[] aPrivateKey, String aDepoParolasi, long aDizinNo, String aX400Address)
        {
            byte[] encoded = null;

            try
            {
                encoded = aCert.getBytes();

            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sertifika encode edilirken hata olustu.", aEx);
            }
            _writeCertificate(encoded, aPKCS11Lib, aPKCS11ID, aPrivateKey, aDepoParolasi, aDizinNo, null);

        }


        private /*Ikili*/Pair<String, List<Object>> _createQuery(CertificateSearchTemplate aSAS, bool aGuncel)
        {
            List<Object> params_ = new List<Object>();
            StringBuilder sb = new StringBuilder("");

            sb.Append("1=1");

            if (aSAS != null)
            {
                long? dizinNo = aSAS.getDizinNo();
                if (dizinNo != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_NO + " IN (SELECT " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " FROM " +
                        DirectoryCertificateHelper.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? )");
                    params_.Add(dizinNo);
                }

                byte[] hash = aSAS.getHash();
                OzetTipi hashtype = aSAS.getHashType();
                if (hash != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                            HashHelper.OZET_TABLO_ADI + " WHERE (" + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_VALUE + " = ? ");
                    params_.Add(OzneTipi.SERTIFIKA.getIntValue());
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
                        sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                                HashHelper.OZET_TABLO_ADI + " WHERE (" + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                        params_.Add(OzneTipi.SERTIFIKA.getIntValue());
                        params_.Add(hashtype.getIntValue());
                    }
                }

                byte[] value = aSAS.getValue();
                if (value != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_VALUE + " = ? ");
                    params_.Add(value);
                }

                byte[] issuer = aSAS.getIssuer();
                if (issuer != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_ISSUER + " = ? ");
                    params_.Add(issuer);
                }
                DateTime? startDate = aSAS.getStartDate();
                if (startDate != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_START_DATE + " = ? ");
                    params_.Add(startDate);
                }

                DateTime? endDate = aSAS.getEndDate();
                if (endDate != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_END_DATE + " = ? ");
                    params_.Add(endDate);
                }

                byte[] serial = aSAS.getSerialNumber();
                if (serial != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_SERIAL + " = ? ");
                    params_.Add(serial);
                }

                byte[] subject = aSAS.getSubject();
                if (subject != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_SUBJECT + " = ? ");
                    params_.Add(subject);
                }

                String eposta = aSAS.getEPosta();
                if (eposta != null)
                {
                    //String keposta = eposta.toLowerCase(EN_LOCALE);
                    String keposta = eposta.ToLower(CertificateHelper.EN_LOCALE);    //todo buna gerek var mı??
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_EPOSTA + " = ?");
                    params_.Add(keposta);
                }

                byte[] subjectkeyid = aSAS.getSubjectKeyID();
                if (subjectkeyid != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_SUBJECT_KEY_ID + " = ?");
                    params_.Add(subjectkeyid);
                }

                KeyUsageSearchTemplate anahtarkullanim = aSAS.getAnahtarKullanim();
                if (anahtarkullanim != null)
                {
                    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_KEYUSAGE + " LIKE ?");
                    params_.Add(anahtarkullanim.sorguOlustur());
                }

                String x400address = aSAS.getX400Address();
                if(x400address != null) {
            	    sb.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_X400ADDRESS + " LIKE ?");
                    params_.Add(x400address);
                }
                //boolean guncel = aSAS.getmGuncel();
                if (aGuncel)
                {
                    sb.Append(" ORDER BY " + CertificateHelper.COLUMN_SERTIFIKA_END_DATE + " DESC ");
                }
            }

            return new /*Ikili*/Pair<String, List<Object>>(sb.ToString(), params_);
        }
    }
}

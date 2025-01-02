using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    public class CertStoreDirectoryOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore mCertStore;

        public CertStoreDirectoryOps(CertStore aDepo)
        {
            CertStoreUtil.CheckLicense();

            mCertStore = aDepo;
        }

        public DepoDizin readDirectory(long aDizinNo)
        {
            if (aDizinNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                DepoDizin dizin = ven.dizinOku(aDizinNo);
                return dizin;
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new CertStoreException(aDizinNo + " nolu dizin veritabaninda bulunamadi.", aEx);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aDizinNo + " nolu dizin okunurken VT hatasi olustu.", aEx);
            }
        }

        public DepoDizin findDirectory(String aDizinAdi)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            try
            {
                DepoDizin dizin = ven.dizinBul(aDizinAdi);
                return dizin;
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new CertStoreException(aDizinAdi + " adli dizin veritabaninda bulunamadi." + aEx);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aDizinAdi + " adli dizin bulunurken VT hatasi olustu." + aEx);
            }
        }

        public void renameDirectory(long aDizinNo, String aYeniAd)
        {
            if (aDizinNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.dizinAdiDegistir(aDizinNo, aYeniAd);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aDizinNo + " nolu dizinin adini " + aYeniAd + " olarak degistirirken VT hatasi olustu." +
                            "Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }


        public void writeDirectory(String aDizinAdi)
        {
            DepoDizin dizin = new DepoDizin();
            dizin.setDizinAdi(aDizinAdi);
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.dizinYaz(dizin);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aDizinAdi + " adli dizin yazilirken VT hatasi olustu." +
                            "Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        public List<DepoDizin> listDirectory()
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            try
            {
                List<DepoDizin> list = new List<DepoDizin>();
                ItemSource<DepoDizin> depoDizinItemSource = ven.dizinListele();
                DepoDizin depoDizin = depoDizinItemSource.nextItem();
                while (depoDizin != null)
                {
                    list.Add(depoDizin);
                    depoDizin = depoDizinItemSource.nextItem();
                }
                return list;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Tum dizinler listelenirken VT hatasi olustu.", aEx);
            }
        }

        public void deleteDirectory(long aDizinNo)
        {
            if (aDizinNo == DirectoryHelper.getDefaultDizinNo())
            {
                throw new CertStoreException(1 + " nolu dizin default dizin oldugu icin silinemez.");
            }

            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.dizinSil(aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aDizinNo + " nolu dizin silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }
    }
}

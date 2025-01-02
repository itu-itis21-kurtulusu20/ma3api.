using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.attrcert;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    class CertStoreAttributeCertificateOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore mCertStore;

        public CertStoreAttributeCertificateOps(CertStore aDepo)
        {
            CertStoreUtil.CheckLicense();

            mCertStore = aDepo;
        }

        public List<Pair<DepoNitelikSertifikasi, DepoSertifika>> listAttributeAndPKICertificates(
            AttributeCertificateSearchTemplate aTemplate)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            try
            {
                List<Pair<DepoNitelikSertifikasi, DepoSertifika>> sonuclar =
                    new List<Pair<DepoNitelikSertifikasi, DepoSertifika>>();
                Pair<String, List<Object>> ikili = _createQuery(aTemplate);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                ItemSource<DepoNitelikSertifikasi> nsItemSource = ven.nitelikSertifikasiListele(sorgu, params_.ToArray());
                DepoNitelikSertifikasi ns = nsItemSource.nextItem();

                while (ns != null)
                {
                    DepoSertifika s = ven.sertifikaOku(ns.getSertifikaNo());
                    sonuclar.Add(new Pair<DepoNitelikSertifikasi, DepoSertifika>(ns, s));
                    ns = nsItemSource.nextItem();
                }

                return sonuclar;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sablona gore nitelik sertifika listeleme sirasinda VT hatasi olustu.", aEx);
            }
        }

        public int deleteAttributeCertificates(AttributeCertificateSearchTemplate aTemplate)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    Pair<String, List<Object>> ikili = _createQuery(aTemplate);
                    String sorgu = ikili.first();
                    List<Object> params_ = ikili.second();
                    ItemSource<DepoNitelikSertifikasi> depoSertifikaItemSource = ven.nitelikSertifikasiListele(sorgu, params_.ToArray());
                    int etkilenen = 0;
                    DepoNitelikSertifikasi depoSertifika = depoSertifikaItemSource.nextItem();

                    while (depoSertifika != null)
                    {
                        etkilenen += ven.nitelikSertifikasiSil(depoSertifika.getNitelikSertifikaNo());
                        depoSertifika = depoSertifikaItemSource.nextItem();
                    }

                    transaction.Commit();
                    return etkilenen;
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Sablona gore nitelik sertifikasi silme sirasinda VT hatasi olustu. Islem geri alindi.", aEx);
                }
            }
        }

        public void writeAttributeAndPKICertificate(ECertificate aCer, List<EAttributeCertificate> aAttrCerts, String aX400Name, long aDizinNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                DepoSertifika ds = CertStoreUtil.eSYASertifikaTODepoSertifika(aCer);
		        ds.setX400Address(aX400Name);
		        List<DepoNitelikSertifikasi> list = new List<DepoNitelikSertifikasi>();
		        foreach(EAttributeCertificate ac in aAttrCerts)
		        {
			        list.Add(CertStoreUtil.asnAttributeCertToDepoNitelikSertifikasi(ac));
		        }
                try
                {
                    List<DepoOzet> sertifikaOzetler = CertStoreUtil.convertToDepoOzet(aCer.getEncoded(), OzneTipi.SERTIFIKA);
                    ven.attributeAndPKICertYaz(ds, sertifikaOzetler, list, aDizinNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Nitelik sertifikasi yazilirken VT hatasi olustu. Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        private Pair<String, List<Object>> _createQuery(AttributeCertificateSearchTemplate aTemplate) {
            StringBuilder sb = new StringBuilder("");
            List<Object> params_ = new List<Object>();
            sb.Append("1=1");
            
            String x500name = aTemplate.getX500Name();
            if (x500name != null) {
                sb.Append(" AND " + NitelikSertifikasiYardimci.COLUMN_HOLDER_DNNAME + " = ? ");
                params_.Add(x500name);
            }

            String rfc822Name = aTemplate.getRfc822Name();
            String x400Name = aTemplate.getX400Address();
            ECertificate cer = aTemplate.getCertificate();
            
            if(rfc822Name != null || x400Name != null || cer != null)
            {
        	    StringBuilder selectFromCertTableSB = new StringBuilder("");
        	    selectFromCertTableSB.Append(" AND "+ NitelikSertifikasiYardimci.COLUMN_SERTIFIKA_ID+ " IN (");
        	    selectFromCertTableSB.Append("SELECT "+CertificateHelper.COLUMN_SERTIFIKA_NO +" FROM "+CertificateHelper.SERTIFIKA_TABLO_ADI+" WHERE ");
        	    selectFromCertTableSB.Append("1=1");
                if(rfc822Name !=null)
                {	
            	    selectFromCertTableSB.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_EPOSTA + " LIKE ? ");
            	    params_.Add(rfc822Name);
                }
                
                if(x400Name !=null)
                {	
            	    selectFromCertTableSB.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_X400ADDRESS + " LIKE ? ");
            	    params_.Add(x400Name);
                }
                
                if(cer !=null)
                {	
            	    selectFromCertTableSB.Append(" AND " + CertificateHelper.COLUMN_SERTIFIKA_SUBJECT_KEY_ID + " = ? ");
            	    params_.Add(cer.getExtensions().getSubjectKeyIdentifier().getValue());
                }
                
                selectFromCertTableSB.Append(" )");
                sb.Append(selectFromCertTableSB);
             }
            
            return new Pair<String, List<Object>>(sb.ToString(), params_);
        }
    }
}

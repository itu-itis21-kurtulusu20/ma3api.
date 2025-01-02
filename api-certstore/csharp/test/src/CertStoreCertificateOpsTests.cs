using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    [TestFixture]
    public class CertStoreCertificateOpsTests : CertStoreTest
    {
        [Test]
        public void testAddRemoveCertificate()
        {
            CertStore sd = null;
            ItemSource<DepoSertifika> depoSertifikaItemSource = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                
                CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);
                dsi.writeCertificate(cer, 1);
               // dsi.writeCertificate(cer, 2);

                depoSertifikaItemSource = dsi.listStoreCertificates();
                DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();
                while (depoSertifika != null)
                {
                    CertificateSearchTemplate certificateSearchTemplate = new CertificateSearchTemplate();
                    dsi.deleteCertificate(depoSertifika.getSertifikaNo().Value);
                    depoSertifika = depoSertifikaItemSource.nextItem();
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            finally
            {
                if (depoSertifikaItemSource != null) depoSertifikaItemSource.close();
                if (sd != null) sd.closeConnection();
            }


        }

        [Test]
        public void testAddMoveCertificate()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);
                dsi.writeCertificate(cer, 1);

                CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
                dirOps.writeDirectory("denemeDizini");

                dsi.sertifikaTasi(1, 1, 2);
            }
            finally
            {
                if (sd != null)
                    sd.closeConnection();
            }

        }
        [Test]
        public void testFindFreshestCertificate()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);
                dsi.writeCertificate(cer, 1);

                CertificateSearchTemplate certificateSearchTemplate = new CertificateSearchTemplate();
                certificateSearchTemplate.setDizin(1);
                List<DepoSertifika> certList =
                    ((RsItemSource<DepoSertifika>)dsi.findFreshestCertificate(certificateSearchTemplate)).toList();
                //Console.WriteLine(certList.Count);
            }
            finally
            {
                sd.closeConnection();
            }
        }
        [Test]
        public void testAddCerts()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);
                CertStoreDirectoryOps directoryOps = new CertStoreDirectoryOps(sd);
                directoryOps.writeDirectory("yeniDizin");

                ECertificate certLevelBCAOK = new ECertificate(TestData.LevelBCAOK);
                dsi.writeCertificate(certLevelBCAOK, 2);

                ECertificate certUSERUG = new ECertificate(TestData.USER_UG_SIGN);

                dsi.writeCertificate(certUSERUG, 2);

                dsi.deleteCertificate(1);

                dsi.writeCertificate(certLevelBCAOK, 2);

                directoryOps.deleteDirectory(2);
            }
            finally
            {
                sd.closeConnection();
            }

        }
    }
}

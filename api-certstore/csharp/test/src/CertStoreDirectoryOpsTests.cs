using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    [TestFixture]
    public class CertStoreDirectoryOpsTests : CertStoreTest
    {
        [Test]
        public void testAddRenameDirectory()
        {

            String dizinAdi1 = "testDizini1";
            String dizinAdi2 = "testDizini2";
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

                CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
                dirOps.writeDirectory(dizinAdi1);
                long dizinNo = dirOps.findDirectory(dizinAdi1).getDizinNo().Value;

                dirOps.renameDirectory(dizinNo, dizinAdi2);
            }
            finally
            {
                sd.closeConnection();
            }
        }

        [Test]
        public void testAddRemoveDirectory()
        {
            String dizinAdi1 = "testDizini1";

            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

                CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
                dirOps.writeDirectory(dizinAdi1);
                dirOps.writeDirectory(dizinAdi1 + "__");

                List<DepoDizin> depoDizinList = dirOps.listDirectory();
                foreach (DepoDizin depoDizin in depoDizinList)
                {
                    if (depoDizin.getDizinNo() == 1) continue;
                    dirOps.deleteDirectory(depoDizin.getDizinNo().Value);
                }
            }
            finally
            {
                sd.closeConnection();    
            }
            
        }

        [Test]
        public void testRemoveNonEmptyDirectory()
        {
            String dizinAdi1 = "testDizini1";

            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

                CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
                dirOps.writeDirectory(dizinAdi1);

                CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);
                dsi.writeCertificate(cer, 2);

                CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);
                ECRL ecrl = new ECRL(Convert.FromBase64String(TestData.KOKSIL));
                crlOps.writeCRL(ecrl.getBytes(), 2);

                List<DepoDizin> depoDizinList = dirOps.listDirectory();
                foreach (DepoDizin depoDizin in depoDizinList)
                {
                    if (depoDizin.getDizinNo() == 1) continue;
                    dirOps.deleteDirectory(depoDizin.getDizinNo().Value);
                }
            }
            finally
            {
                sd.closeConnection();    
            }            
        }
    }
}

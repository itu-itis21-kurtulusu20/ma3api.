using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    [TestFixture]
    public class CertStoreCRLOpsTests : CertStoreTest
    {
        [Test]
        public void testAddRemoveCRL()
        {
            CertStore sd = null;
            ItemSource<DepoSIL> depoSILList = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);

                byte[] crlBytes = Convert.FromBase64String(TestData.KOKSIL);
                ECertificateList crl = new ECertificateList(crlBytes);

                crlOps.writeCRL(crlBytes, 1);

                depoSILList = crlOps.listStoreCRL();
                DepoSIL depoSIL = depoSILList.nextItem();
                while (depoSIL != null)
                {
                    crlOps.deleteCRL(depoSIL.getSILNo().Value);
                    //crlOps.deleteCRLFromDirectory(1, 1);
                    depoSIL = depoSILList.nextItem();

                }
            }
            finally
            {
                depoSILList.close();
                sd.closeConnection();
            }

        }

        [Test]
        public void testAddMoveCRL()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);

                byte[] crlBytes = Convert.FromBase64String(TestData.KOKSIL);
                ECertificateList crl = new ECertificateList(crlBytes);

                crlOps.writeCRL(crlBytes, 1);

                CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
                dirOps.writeDirectory("denemeDizini");

                crlOps.moveCRL(1, 1, 2);
            }
            finally
            {
                sd.closeConnection();    
            }
            
        }
    }
}

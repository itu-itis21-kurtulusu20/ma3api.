using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    [TestFixture]
    class CertStoreRootCertificateOpsTests : CertStoreTest
    {
        [Test]
        public void testAddRemoveRootCertificate()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreRootCertificateOps rootOps = new CertStoreRootCertificateOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);

                rootOps.addPersonalRootCertificate(cer.getObject(), CertificateType.CACERT);

                List<DepoKokSertifika> kokSertifikalar = rootOps.listStoreRootCertificates();
                foreach (DepoKokSertifika kok in kokSertifikalar)
                {
                    rootOps.deleteRootCertificate(kok.getKokSertifikaNo());
                }
            }
            finally
            {
                sd.closeConnection();
            }
        }

    }
}

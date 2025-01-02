using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    [TestFixture]
    public class CertStoreOCSPOpsTests : CertStoreTest
    {
        [Test]
        public void testAddOCSP()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);

                byte[] borBytes = Convert.FromBase64String(TestData.LevelBCAOK_BOR);
                EOCSPResponse eResp = new EOCSPResponse(borBytes);

                ocspOps.writeOCSPResponseAndCertificate(eResp, cer);

                OCSPSearchTemplate ocspSearchTemplate = new OCSPSearchTemplate();
                ocspSearchTemplate.setCertSerialNumber(cer.getSerialNumber().GetData());

                EBasicOCSPResponse basicOCSPResponse = ocspOps.listOCSPResponses(ocspSearchTemplate);

                ocspOps.deleteOCSPResponse(1);
            }
            finally
            {
                sd.closeConnection();
            }

        }
        [Test]
        public void testAddOCSPRemoveCert()
        {
            CertStore sd = null;
            try
            {
                sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
                CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(sd);

                ECertificate cer = new ECertificate(TestData.LevelBCAOK);

                byte[] borBytes = Convert.FromBase64String(TestData.LevelBCAOK_BOR);
                EOCSPResponse eResp = new EOCSPResponse(borBytes);

                ocspOps.writeOCSPResponseAndCertificate(eResp, cer);

                OCSPSearchTemplate ocspSearchTemplate = new OCSPSearchTemplate();
                ocspSearchTemplate.setCertSerialNumber(cer.getSerialNumber().GetData());

                EBasicOCSPResponse basicOCSPResponse = ocspOps.listOCSPResponses(ocspSearchTemplate);

                CertStoreCertificateOps certificateOps = new CertStoreCertificateOps(sd);
                CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();
                certSearchTemplate.setSerial(cer.getSerialNumber().GetData());

                int ret = certificateOps.deleteCertificate(certSearchTemplate);
                //ocspOps.deleteOCSPResponse(1);
            }
            finally
            {
                sd.closeConnection();
            }
        }
    }
}

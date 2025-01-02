package test.esya.api.infra.certstore;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreOCSPOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.OCSPSearchTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/19/11
 * Time: 9:52 AM
 */
public class CertStoreOCSPOpsTest extends CertStoreTests {

    @Test
    public void testAddOCSP() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

            CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);

            byte[] borBytes = Base64.decode(TestsData.LevelBCAOK_BOR);
            EOCSPResponse eResp = new EOCSPResponse(borBytes);

            ocspOps.writeOCSPResponseAndCertificate(eResp, cer);

            OCSPSearchTemplate ocspSearchTemplate = new OCSPSearchTemplate();
            ocspSearchTemplate.setCertSerialNumber(cer.getSerialNumber().toByteArray());

            EBasicOCSPResponse basicOCSPResponse = ocspOps.listOCSPResponses(ocspSearchTemplate);

            ocspOps.deleteOCSPResponse(1);
        } finally {
            sd.closeConnection();
        }

    }

    @Test
    public void testAddOCSPRemoveCert() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);

            byte[] borBytes = Base64.decode(TestsData.LevelBCAOK_BOR);
            EOCSPResponse eResp = new EOCSPResponse(borBytes);

            ocspOps.writeOCSPResponseAndCertificate(eResp, cer);

            OCSPSearchTemplate ocspSearchTemplate = new OCSPSearchTemplate();
            ocspSearchTemplate.setCertSerialNumber(cer.getSerialNumber().toByteArray());

            EBasicOCSPResponse basicOCSPResponse = ocspOps.listOCSPResponses(ocspSearchTemplate);

            CertStoreCertificateOps certificateOps = new CertStoreCertificateOps(sd);
            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();
            certSearchTemplate.setSerial(cer.getSerialNumber().toByteArray());

            int ret = certificateOps.deleteCertificate(certSearchTemplate);
            //ocspOps.deleteOCSPResponse(1);
        } finally {
            sd.closeConnection();
        }
    }
}

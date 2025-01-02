package test.esya.api.infra.certstore;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/18/11
 * Time: 4:51 PM
 */
public class CertStoreRootCertificateOpsTest extends CertStoreTests {

    @Test
    public void testAddRemoveRootCertificate() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreRootCertificateOps rootOps = new CertStoreRootCertificateOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);

            rootOps.addPersonalRootCertificate(cer.getObject(), SertifikaTipi.SMSERTIFIKASI);

            List<DepoKokSertifika> kokSertifikalar = rootOps.listStoreRootCertificates();
            for (DepoKokSertifika kok : kokSertifikalar) {
                rootOps.deleteRootCertificate(kok.getKokSertifikaNo());
            }
        } finally {
            sd.closeConnection();
        }
    }
}

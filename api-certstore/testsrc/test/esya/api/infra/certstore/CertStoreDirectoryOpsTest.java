package test.esya.api.infra.certstore;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreDirectoryOps;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/18/11
 * Time: 4:12 PM
 */
public class CertStoreDirectoryOpsTest extends CertStoreTests {

    @Test
    public void testAddRenameDirectory() throws Exception {

        String dizinAdi1 = "testDizini1";
        String dizinAdi2 = "testDizini2";
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

            CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
            dirOps.writeDirectory(dizinAdi1);
            Long dizinNo = dirOps.findDirectory(dizinAdi1).getDizinNo();

            dirOps.renameDirectory(dizinNo, dizinAdi2);
        } finally {
            sd.closeConnection();
        }
    }

    @Test
    public void testAddRemoveDirectory() throws Exception {

        String dizinAdi1 = "testDizini1";

        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

            CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
            dirOps.writeDirectory(dizinAdi1);
            dirOps.writeDirectory(dizinAdi1 + "__");

            List<DepoDizin> depoDizinList = dirOps.listDirectory();
            for (DepoDizin depoDizin : depoDizinList) {
                if (depoDizin.getDizinNo() == 1) continue;
                dirOps.deleteDirectory(depoDizin.getDizinNo());
            }
        } finally {
            sd.closeConnection();
        }

    }

    @Test
    public void testRemoveNonEmptyDirectory() throws Exception {
        String dizinAdi1 = "testDizini1";

        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);

            CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
            dirOps.writeDirectory(dizinAdi1);

            CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);
            dsi.writeCertificate(cer, 2);

            CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);
            ECRL ecrl = new ECRL(Base64.decode(TestsData.KOKSIL));
            crlOps.writeCRL(ecrl.getEncoded(), new Long(2));

            List<DepoDizin> depoDizinList = dirOps.listDirectory();
            for (DepoDizin depoDizin : depoDizinList) {
                if (depoDizin.getDizinNo() == 1) continue;
                dirOps.deleteDirectory(depoDizin.getDizinNo());
            }
        } finally {
            sd.closeConnection();
        }

    }

}

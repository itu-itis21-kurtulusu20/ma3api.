package test.esya.api.infra.certstore;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificateList;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreDirectoryOps;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/18/11
 * Time: 4:52 PM
 */
public class CertStoreCRLOpsTest extends CertStoreTests {

    @Test
    public void testAddRemoveCRL() throws Exception {
        CertStore sd = null;
        ItemSource<DepoSIL> depoSILList = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);

            byte[] crlBytes = Base64.decode(TestsData.KOKSIL);
            ECertificateList crl = new ECertificateList(crlBytes);

            crlOps.writeCRL(crlBytes, new Long(1));

            depoSILList = crlOps.listStoreCRL();
            DepoSIL depoSIL = depoSILList.nextItem();
            while (depoSIL != null) {
                crlOps.deleteCRL(depoSIL.getSILNo());
                //crlOps.deleteCRLFromDirectory(1, 1);
                depoSIL = depoSILList.nextItem();
            }
        } finally {
            depoSILList.close();
            sd.closeConnection();
        }

    }

    @Test
    public void testAddMoveCRL() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCRLOps crlOps = new CertStoreCRLOps(sd);

            byte[] crlBytes = Base64.decode(TestsData.KOKSIL);
            ECertificateList crl = new ECertificateList(crlBytes);

            crlOps.writeCRL(crlBytes, new Long(1));

            CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
            dirOps.writeDirectory("denemeDizini");

            crlOps.moveCRL(1, 1, 2);
        } finally {
            sd.closeConnection();
        }
    }

}

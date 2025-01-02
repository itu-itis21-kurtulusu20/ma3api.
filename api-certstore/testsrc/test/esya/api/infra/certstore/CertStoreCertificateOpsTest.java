package test.esya.api.infra.certstore;

import junit.framework.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreDirectoryOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/18/11
 * Time: 3:38 PM
 */
public class CertStoreCertificateOpsTest extends CertStoreTests {

    static {
        try {
            Crypto.setProvider(Crypto.PROVIDER_GNU);
        } catch (Exception ex) {
            Assert.fail("PROVIDER_GNU could not be set");
        }
    }

    @Test
    public void testAddRemoveCertificate()
            throws Exception {
        CertStore sd = null;
        ItemSource<DepoSertifika> depoSertifikaItemSource = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);
            dsi.writeCertificate(cer, 1);
            dsi.writeCertificate(cer, 2);

            depoSertifikaItemSource = dsi.listStoreCertificates();
            DepoSertifika depoSertifika = depoSertifikaItemSource.nextItem();
            while (depoSertifika != null) {
                CertificateSearchTemplate certificateSearchTemplate = new CertificateSearchTemplate();
                dsi.deleteCertificate(depoSertifika.getSertifikaNo());
                depoSertifika = depoSertifikaItemSource.nextItem();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (depoSertifikaItemSource != null) depoSertifikaItemSource.close();
            if (sd != null) sd.closeConnection();
        }
    }

    @Test
    public void testAddMoveCertificate() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);
            dsi.writeCertificate(cer, 1);

            CertStoreDirectoryOps dirOps = new CertStoreDirectoryOps(sd);
            dirOps.writeDirectory("denemeDizini");

            dsi.sertifikaTasi(1, 1, 2);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (sd != null)
                sd.closeConnection();
        }
    }

    @Test
    public void testFindFreshestCertificate() throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);

            ECertificate cer = new ECertificate(TestsData.LevelBCAOK);
            dsi.writeCertificate(cer, 1);

            CertificateSearchTemplate certificateSearchTemplate = new CertificateSearchTemplate();
            certificateSearchTemplate.setDizin(new Long(1));
            List<DepoSertifika> certList = ((RsItemSource) dsi.findFreshestCertificate(certificateSearchTemplate)).toList();
            System.out.println(certList.size());
        } finally {
            sd.closeConnection();
        }

    }

    @Test
    public void testAddCerts()
            throws Exception {
        CertStore sd = null;
        try {
            sd = new CertStore(CERT_STORE_FILE_PATH, null, null);
            CertStoreCertificateOps dsi = new CertStoreCertificateOps(sd);
            CertStoreDirectoryOps directoryOps = new CertStoreDirectoryOps(sd);
            directoryOps.writeDirectory("yeniDizin");

            ECertificate certLevelBCAOK = new ECertificate(TestsData.LevelBCAOK);
            dsi.writeCertificate(certLevelBCAOK, 2);

            ECertificate certUSERUG = new ECertificate(TestsData.USER_UG_SIGN);

            dsi.writeCertificate(certUSERUG, 2);

            dsi.deleteCertificate(1);

            dsi.writeCertificate(certLevelBCAOK, 2);

            directoryOps.deleteDirectory(2);

        } finally {
            sd.closeConnection();
        }

    }
}

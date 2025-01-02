package test.esya.api.infra.certstore;

import org.junit.BeforeClass;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 3/17/11
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertStoreTests {

    public final static String CERT_STORE_FILE_PATH = "SertifikaDeposu.svt";
    /*static {
        try {
            Crypto.setProvider(Crypto.PROVIDER_GNU);
        } catch (Exception ex) {
            Assert.fail("PROVIDER_SUN could not be set");
        }
    }*/

    @BeforeClass
    public static void setUp() {
        new File(CERT_STORE_FILE_PATH).delete();
    }

    public void tearDown() {
        //System.out.println("Test bitti");
    }
}
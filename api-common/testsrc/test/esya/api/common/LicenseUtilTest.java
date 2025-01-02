package test.esya.api.common;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by omer.dural on 24.09.2018.
 */
public class LicenseUtilTest {

    @Test
    public void testLicenseUtil() throws Exception {

        LicenseUtil.setLicenseXml(new FileInputStream("T:\\api-parent\\lisans\\lisans.xml"));

        Date expireDate = LicenseUtil.getExpirationDate();

        assertNotNull(expireDate);
        assertNotNull(expireDate.getTime());
        assertTrue(expireDate.getTime() > 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("License expiration date :" + dateFormat.format(expireDate));
    }
}

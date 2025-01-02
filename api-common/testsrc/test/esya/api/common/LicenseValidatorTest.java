package test.esya.api.common;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LicenseValidatorTest {

    @Test(expected = Exception.class)
    public void testNotExistUrun() throws Exception {

        LicenseUtil.setLicenseXml(new FileInputStream("T:\\api-parent\\lisans\\lisans.xml"));

        String attr = null;
        LV licenseValidator = LV.getInstance();

        licenseValidator.checkLD(Urunler.ESYA_ISTEMCI.getID());
        attr = licenseValidator.getUrunAtt(Urunler.ESYA_ISTEMCI.getID(), "IP");
        System.out.println(attr);
    }

    @Test
    public void testLicenseAttribute() throws Exception {

        FileInputStream fis = new FileInputStream("T:\\api-parent\\lisans\\lisans.xml");
        LicenseUtil.setLicenseXml(fis);

        LV licenseValidator = LV.getInstance();
        licenseValidator.checkLD(Urunler.SERTIFIKADOGRULAMA.getID());
        String attJava = licenseValidator.getUrunAtt(Urunler.SERTIFIKADOGRULAMA.getID(), "Java");
        assertEquals("var", attJava);
        String attCSharp = licenseValidator.getUrunAtt(Urunler.SERTIFIKADOGRULAMA.getID(), "CSharp");
        assertEquals("var", attCSharp);
    }

}

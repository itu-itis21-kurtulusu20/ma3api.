package tr.gov.tubitak.uekae.esya.api.pades.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.SampleBase;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.io.File;

public class PadesSampleBase extends SampleBase {

    protected static Logger logger = LoggerFactory.getLogger(PadesSampleBase.class);

    private static String testDataFolder;
    private static File testFile;

    static {
        try {

            testDataFolder = getRootDir() + "/testdata/";
            testFile = new File(testDataFolder + "sample.pdf");

        } catch (Exception e) {
            logger.error("Error in PadesSampleBase", e);
        }
    }

    /**
     * Creates context for signature creation and validation
     *
     * @return created context
     */
    protected PAdESContext createContext() throws ESYAException {
        PAdESContext c = new PAdESContext(new File(testDataFolder).toURI());
        //c.setConfig(new Config(getRootDir() + "/config/esya-signature-config.xml"));
        //return c;

        /*for getting test TimeStamp or qualified TimeStamp account, mail to bilgi@kamusm.gov.tr.
        This configuration, user ID (user_id) and password (password), is invalid.*/

        throw new ESYAException("\n- Zaman Damgası kullanmak için zaman damgası hesap bilgilerini, esya-signature-config-config.xml dosyasındaki <timestamp-server> tagleri arasına giriniz. Varsayılan olarak tanımlı olan user ID (0) ve password (password) geçersizdir. Zaman damgası hesap bilgilerini ayarladıktan sonra \"c.setConfig(new Config(getRootDir() + \"/config/esya-signature-config.xml\"));\" ve  \"return c;\" satırlarını aktifleştiriniz.\n" +
                "- MA3 API sadece KamuSM Zaman Damgası ile çalışabilmektedir.\n" +
                "- Zaman damgası test kullanıcısı talep etmek amacıyla Kamu SM (bilgi[at]kamusm.gov.tr)'ye e-posta gönderilmesi gerekmektedir. İlgili e-posta'nın konu kısmında \"Zamane test kullanıcı talebi\", içeriğinde ise \"Kurum adı, kurum vergi kimlik numarası, kurum adresi, kurum sabit telefon, yetkili kişi adı ve soyadı, cep telefonu numarası, yetkili kişi e-posta\" bilgilerinin yer alması gerekmektedir.\n" +
                " Ayrıntılar için: https://kamusm.bilgem.tubitak.gov.tr/urunler/zaman_damgasi/ucretsiz_zaman_damgasi_istemci_yazilimi.jsp");
    }

    /**
     * Gets the test data folder
     *
     * @return the test data folder
     */
    protected String getTestDataFolder() {
        return testDataFolder;
    }

    /**
     * Gets the test file
     *
     * @return the test file
     */
    protected File getTestFile() {
        return testFile;
    }

}

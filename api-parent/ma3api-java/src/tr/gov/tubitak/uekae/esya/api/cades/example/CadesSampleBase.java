package tr.gov.tubitak.uekae.esya.api.cades.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.SampleBase;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Examples gets the test constants from that class. Default test constants
 */
public class CadesSampleBase extends SampleBase {

    private static Logger logger = LoggerFactory.getLogger(CadesSampleBase.class);

    private static String testDataFolder;
    private static String policyFile;
    private static TSSettings tsSettings;
    private static ValidationPolicy validationPolicy;

    static {

        testDataFolder = getRootDir() + "/testdata/";
        policyFile = getRootDir() + "/config/certval-policy-test.xml";

        try {
            // 2. argümana customerID'nizi girmelisiniz.
            tsSettings = new TSSettings("http://tzd.kamusm.gov.tr", 2, "PASSWORD", DigestAlg.SHA256);
        } catch (Exception e) {
            logger.error("Error in CadesSampleBase", e);
        }
    }

    public String getTestDataFolder() {
        return testDataFolder;
    }

    public synchronized ValidationPolicy getPolicy() throws ESYAException {

        if (validationPolicy == null) {
            try {
                validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(policyFile));
            } catch (FileNotFoundException e) {
                throw new ESYARuntimeException("Policy file could not be found", e);
            }
        }
        return validationPolicy;
    }

    public TSSettings getTSSettings() throws ESYAException {
        //return tsSettings;

        /*for getting test TimeStamp or qualified TimeStamp account, mail to bilgi@kamusm.gov.tr.
        This configuration, user ID (2) and password (PASSWORD), is invalid.*/

        throw new ESYAException("\n- Zaman Damgası kullanmak için zaman damgası hesap bilgilerini, static block içerisinde tanımlı TSSettings nesnesine parametre geçiniz. Varsayılan olarak tanımlı olan user ID (2) ve password (PASSWORD) geçersizdir. Zaman damgası hesap bilgilerini ayarladıktan sonra \"return tsSettings;\" satırını aktifleştiriniz.\n" +
                "- MA3 API sadece KamuSM Zaman Damgası ile çalışabilmektedir.\n" +
                "- Zaman damgası test kullanıcısı talep etmek amacıyla Kamu SM (bilgi[at]kamusm.gov.tr)'ye e-posta gönderilmesi gerekmektedir. İlgili e-posta'nın konu kısmında \"Zamane test kullanıcı talebi\", içeriğinde ise \"Kurum adı, kurum vergi kimlik numarası, kurum adresi, kurum sabit telefon, yetkili kişi adı ve soyadı, cep telefonu numarası, yetkili kişi e-posta\" bilgilerinin yer alması gerekmektedir.\n" +
                " Ayrıntılar için: https://kamusm.bilgem.tubitak.gov.tr/urunler/zaman_damgasi/ucretsiz_zaman_damgasi_istemci_yazilimi.jsp");
    }
}


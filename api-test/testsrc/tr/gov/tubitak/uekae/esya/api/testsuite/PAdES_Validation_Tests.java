package tr.gov.tubitak.uekae.esya.api.testsuite;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Enclosed.class)
public class PAdES_Validation_Tests {

    static String folder = TestSuiteCommonMethods.FOLDER;

    static SignatureContainer signatureContainer;
    static ContainerValidationResult validationResult;

    @RunWith(Parameterized.class)
    public static class PAdES_BES_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"B_1.pdf",         "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"B_4.pdf",         "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"B_5.pdf",         "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"B_6.pdf",         "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"B_7.pdf",         "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"B_8.pdf",         "valid"}, // Geçerli İmza
                    {"B_9.pdf",         "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"B_10.pdf",        "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"B_11.pdf",        "invalid"}, // “Nitelikli Sertifika İbareleri” uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"B_12.pdf",        "invalid"}, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"B_14.pdf",        "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"B_15.pdf",        "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"B_16_1.pdf",      "invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış
                    {"B_16_2.pdf",      "invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
                    {"B_17_1.pdf",      "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış
                    {"B_18.pdf",        "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"B_19.pdf",        "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"B_20.pdf",        "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"B_21.pdf",        "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"B_22.pdf",        "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"B_23.pdf",        "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"B_24_1.pdf",      "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş
                    {"B_27.pdf",        "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"B_28.pdf",        "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"B_29_1.pdf",      "invalid"}, // Alt kök sertifikası SİL’de iptal olmuş
                    {"B_30.pdf",        "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"B_31.pdf",        "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"B_32_1.pdf",      "invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş
                    {"B_33.pdf",        "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"B_34.pdf",        "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"B_35.pdf",        "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"B_36.pdf",        "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"B_37_1.pdf",      "valid"}  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş

                    /*#     API'nin gerçekleştiremeyeceği senaryolar:
                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
                    #     | B_39_s.pdf      | geçersiz  | İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı geçerli seri imzalı dosya |
                    #     API kapsamının dışındaki senaryolar:
                    #     | B_13.pdf        | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | B_25.pdf        | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | B_26.pdf        | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
            });
        }

        public PAdES_BES_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "bes", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_LT_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LT_1.pdf",       "valid"}, //Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"LT_4.pdf",       "invalid"}, //“ESS-Signing-Certificate” içeriği bozulmuş
                    {"LT_5.pdf",       "invalid"}, //“messageDigest” imza özelliği bozulmuş
                    {"LT_6.pdf",       "valid"}, //SHA-1 algoritması kullanılarak imzalanmış
                    {"LT_7.pdf",       "invalid"}, //İmza dosyasının imzası bozulmuş
                    {"LT_8.pdf",       "valid"}, //Geçerli İmza
                    {"LT_9.pdf",       "invalid"}, //“Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"LT_10.pdf",      "invalid"}, //“Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"LT_11.pdf",      "invalid"}, //“Nitelikli Sertifika İbareleri” uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"LT_12.pdf",      "invalid"}, //“Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"LT_14.pdf",      "invalid"}, //Süresi dolmuş sertifika ile imzalanmış
                    {"LT_15.pdf",      "invalid"}, //İmzası bozuk sertifika ile imzalanmış
                    {"LT_16_1.pdf",    "invalid"}, //SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LT_16_2.pdf",    "valid"}, //SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_17_1.pdf",    "invalid"}, //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LT_17_2.pdf",    "valid"}, //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_18.pdf",      "invalid"}, //Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"LT_19.pdf",      "invalid"}, //Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"LT_20.pdf",      "invalid"}, //Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"LT_21.pdf",      "invalid"}, //Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"LT_22.pdf",      "invalid"}, //Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"LT_23.pdf",      "invalid"}, //Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"LT_27.pdf",      "invalid"}, //OCSP cevabı içindeki sertifika farklı
                    {"LT_28.pdf",      "invalid"}, //Alt kök sertifikasının imzası bozuk
                    {"LT_29_1.pdf",    "invalid"}, //Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"LT_29_2.pdf",    "valid"}, //Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_30.pdf",      "invalid"}, //Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"LT_31.pdf",      "invalid"}, //Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"LT_32_1.pdf",    "invalid"}, //Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"LT_32_2.pdf",    "valid"}, //Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_33.pdf",      "invalid"}, //Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"LT_34.pdf",      "invalid"}, //Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"LT_35.pdf",      "invalid"}, //Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"LT_36.pdf",      "invalid"}, //Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"LT_40.pdf",      "invalid"}, //“İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LT_41.pdf",      "invalid"}, //“İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"LT_42.pdf",      "invalid"}, //“İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"LT_43.pdf",      "invalid"}, //“İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LT_44.pdf",      "invalid"}, //“İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LT_45.pdf",      "valid"}, //“İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_46.pdf",      "invalid"}, //“İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LT_47.pdf",      "valid"}, //“İmza ZD” TSC1’den alınmış; Geçerli
                    {"LT_48.pdf",      "invalid"}, //“İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"LT_49.pdf",      "invalid"}  //“İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor

                    /*#     API'nin gerçekleştiremeyeceği senaryolar:
                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
                    #     | LT_24_2.pdf     | geçerli   | Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LT_37_2.pdf     | geçerli   | Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LT_39_s.pdf     | geçersiz  | İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı geçerli seri imzalı dosya |
                    #     API kapsamının dışındaki senaryolar:
                    #     | LT_13.pdf       | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | LT_25.pdf       | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | LT_26.pdf       | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
            });
        }

        public PAdES_LT_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "lt", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_LTA_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LTA_1.pdf",       "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"LTA_4.pdf",       "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"LTA_5.pdf",       "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"LTA_6.pdf",       "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"LTA_7.pdf",       "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"LTA_8.pdf",       "valid"}, // Geçerli imza
                    {"LTA_9.pdf",       "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"LTA_10.pdf",      "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"LTA_11.pdf",      "invalid"}, // “Nitelikli Sertifika İbareleri” uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"LTA_12.pdf",      "invalid"}, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"LTA_14.pdf",      "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"LTA_15.pdf",      "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"LTA_16_1.pdf",    "invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_16_2.pdf",    "valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_17_1.pdf",    "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_17_2.pdf",    "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_18.pdf",      "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"LTA_19.pdf",      "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"LTA_20.pdf",      "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"LTA_21.pdf",      "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"LTA_22.pdf",      "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"LTA_23.pdf",      "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"LTA_27.pdf",      "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"LTA_28.pdf",      "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"LTA_29_1.pdf",    "invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_29_2.pdf",    "valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_30.pdf",      "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"LTA_31.pdf",      "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"LTA_32_1.pdf",    "invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_32_2.pdf",    "valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_33.pdf",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"LTA_34.pdf",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"LTA_35.pdf",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"LTA_36.pdf",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"LTA_40.pdf",      "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LTA_41.pdf",      "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"LTA_42.pdf",      "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"LTA_43.pdf",      "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LTA_44.pdf",      "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_45.pdf",      "valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_46.pdf",      "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LTA_47.pdf",      "valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"LTA_48.pdf",      "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"LTA_49.pdf",      "invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"LTA_84.pdf",      "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LTA_85.pdf",      "invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
                    {"LTA_86.pdf",      "invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"LTA_87.pdf",      "invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LTA_88.pdf",      "invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_89.pdf",      "valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_90.pdf",      "invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LTA_91.pdf",      "valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
                    {"LTA_92.pdf",      "invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"LTA_93.pdf",      "invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"LTA_106.pdf",     "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş

                    /*#   API'nin gerçekleştiremeyeceği senaryolar:
                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
                    #     | LTA_24_2.pdf    | geçerli   | Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LTA_37_2.pdf    | geçerli   | Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LTA_39_s.pdf    | geçersiz  | İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı geçerli seri imzalı dosya |
                    #     | LTA_109.pdf     | geçerli   | “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış |
                    #     API kapsamının dışındaki senaryolar:
                    #     | LTA_13.pdf      | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | LTA_25.pdf      | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | LTA_26.pdf      | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
            });
        }

        public PAdES_LTA_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "lta", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LT_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LT_P4_1.pdf",     "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"LT_P4_4.pdf",     "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor.
                    {"LT_P4_5.pdf",     "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"LT_P4_6.pdf",     "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"LT_P4_7.pdf",     "invalid"}, // “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"LT_P4_8.pdf",     "invalid"}, // “SigningTime” imza özelliği eklenmemiş
                    {"LT_P4_9.pdf",     "invalid"}, // “SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                    {"LT_P4_10.pdf",    "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"LT_P4_12.pdf",    "valid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"LT_P4_13.pdf",    "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"LT_P4_14.pdf",    "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"LT_P4_15.pdf",    "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"LT_P4_16.pdf",    "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"LT_P4_17.pdf",    "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"LT_P4_18.pdf",    "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"LT_P4_19.pdf",    "invalid"}, // “Nitelikli Sertifika İbareleri“ uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"LT_P4_20.pdf",    "invalid"}, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"LT_P4_22.pdf",    "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"LT_P4_23.pdf",    "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"LT_P4_24.pdf",    "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LT_P4_25.pdf",    "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_P4_26.pdf",    "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"LT_P4_27.pdf",    "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"LT_P4_28.pdf",    "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"LT_P4_29.pdf",    "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"LT_P4_34.pdf",    "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"LT_P4_35.pdf",    "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"LT_P4_37.pdf",    "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LT_P4_38.pdf",    "invalid"}, // “İmza ZD” TSA2'den alınmış ZDimzası bozuk
                    {"LT_P4_39.pdf",    "invalid"}, // “İmza ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"LT_P4_40.pdf",    "invalid"}, // “İmza ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LT_P4_41.pdf",    "invalid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LT_P4_42.pdf",    "valid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"LT_P4_43.pdf",    "invalid"}, // “İmza ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LT_P4_44.pdf",    "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
                    {"LT_P4_45.pdf",    "invalid"}, // “İmza ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
                    {"LT_P4_46.pdf",    "invalid"}  // “İmza ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor

                    /*#     API'nin gerçekleştiremeyeceği senaryolar:
                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
                    #     | LT_P4_31.pdf    | geçerli   | Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LT_P4_93_s.pdf  | geçersiz  | İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya |
                    #     API kapsamının dışındaki senaryolar:
                    #     | LT_P4_21.pdf    | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | LT_P4_32.pdf    | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | LT_P4_33.pdf    | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
            });
        }

        public PAdES_P4_LT_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lt", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LTA_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LTA_P4_1.pdf",    "valid"}, //  Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"LTA_P4_4.pdf",    "invalid"}, //  “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor.
                    {"LTA_P4_5.pdf",    "invalid"}, //  “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"LTA_P4_6.pdf",    "valid"}, //  “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"LTA_P4_7.pdf",    "invalid"}, //  “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"LTA_P4_8.pdf",    "invalid"}, //  “SigningTime” imza özelliği eklenmemiş
                    {"LTA_P4_9.pdf",    "invalid"}, //  “SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                    {"LTA_P4_10.pdf",   "invalid"}, //  NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"LTA_P4_12.pdf",   "valid"}, //  “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"LTA_P4_13.pdf",   "invalid"}, //  “ESS-Signing-Certificate” içeriği bozulmuş
                    {"LTA_P4_14.pdf",   "invalid"}, //  “messageDigest” imza özelliği bozulmuş
                    {"LTA_P4_15.pdf",   "valid"}, //  SHA-1 algoritması kullanılarak imzalanmış
                    {"LTA_P4_16.pdf",   "invalid"}, //  İmza dosyasının imzası bozulmuş
                    {"LTA_P4_17.pdf",   "invalid"}, //  “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"LTA_P4_18.pdf",   "invalid"}, //  “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"LTA_P4_19.pdf",   "invalid"}, //  “Nitelikli Sertifika İbareleri“ uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"LTA_P4_20.pdf",   "invalid"}, //  “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"LTA_P4_22.pdf",   "invalid"}, //  Süresi dolmuş sertifika ile imzalanmış
                    {"LTA_P4_23.pdf",   "invalid"}, //  İmzası bozuk sertifika ile imzalanmış
                    {"LTA_P4_24.pdf",   "invalid"}, //  OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_P4_25.pdf",   "valid"}, //  OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_P4_26.pdf",   "invalid"}, //  Kontrol edildiği OCSP cevabının valid"lik süresi dolmuş
                    {"LTA_P4_27.pdf",   "invalid"}, //  Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"LTA_P4_28.pdf",   "invalid"}, //  Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"LTA_P4_29.pdf",   "invalid"}, //  Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"LTA_P4_34.pdf",   "invalid"}, //  OCSP cevabı içindeki sertifika farklı
                    {"LTA_P4_35.pdf",   "invalid"}, //  Alt kök sertifikasının imzası bozuk
                    {"LTA_P4_37.pdf",   "invalid"}, //  “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LTA_P4_38.pdf",   "invalid"}, //  “İmza ZD” TSA2'den alınmış ZDimzası bozuk
                    {"LTA_P4_39.pdf",   "invalid"}, //  “İmza ZD” TSA3'den alınmış; ZDsertifikasının valid"lik süresi dolmuş
                    {"LTA_P4_40.pdf",   "invalid"}, //  “İmza ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LTA_P4_41.pdf",   "invalid"}, //  “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_P4_42.pdf",   "valid"}, //  “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"LTA_P4_43.pdf",   "invalid"}, //  “İmza ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LTA_P4_44.pdf",   "valid"}, //  “İmza ZD” TSC1'den alınmış; Geçerli
                    {"LTA_P4_45.pdf",   "invalid"}, //  “İmza ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
                    {"LTA_P4_46.pdf",   "invalid"}, //  “İmza ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor
                    {"LTA_P4_75.pdf",   "invalid"}, //  “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"LTA_P4_76.pdf",   "invalid"}, //  “Arşiv ZD” TSA2'den alınmış ZDimzası bozuk
                    {"LTA_P4_77.pdf",   "invalid"}, //  “Arşiv ZD” TSA3'den alınmış; ZDsertifikasının valid"lik süresi dolmuş
                    {"LTA_P4_78.pdf",   "invalid"}, //  “Arşiv ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"LTA_P4_79.pdf",   "invalid"}, //  “Arşiv ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"LTA_P4_81.pdf",   "invalid"}, //  “Arşiv ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"LTA_P4_82.pdf",   "valid"}, //  “Arşiv ZD” TSC1'den alınmış; Geçerli
                    {"LTA_P4_83.pdf",   "invalid"}, //  “Arşiv ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
                    {"LTA_P4_84.pdf",   "invalid"}, //  “Arşiv ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor
                    {"LTA_P4_94.pdf",   "valid"}  //  “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış

                    /*#     API'nin gerçekleştiremeyeceği senaryolar:
                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
                    #     | LTA_P4_31.pdf   | geçerli   | Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LTA_P4_80.pdf   | geçerli   | “Arşiv ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş |
                    #     | LTA_P4_85.pdf   | geçersiz  | Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş |
                    #     | LTA_P4_86.pdf   | geçersiz  | Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş |
                    #     | LTA_P4_93_s.pdf | geçersiz  | İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya |
                    #     API kapsamının dışındaki senaryolar:
                    #     | LTA_P4_21.pdf   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | LTA_P4_32.pdf   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | LTA_P4_33.pdf   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
            });
        }

        public PAdES_P4_LTA_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lta", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_BES_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"B_38.pdf",   "invalid" }, // Kök sertifikasının imzası bozuk
            });
        }

        public PAdES_BES_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "bes", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_LT_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LT_38.pdf",   "invalid" }, // Kök sertifikasının imzası bozuk
            });
        }

        public PAdES_LT_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "lt", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_LTA_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LTA_38.pdf",   "invalid" }, // Kök sertifikasının imzası bozuk
            });
        }

        public PAdES_LTA_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "lta", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LT_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LT_P4_36.pdf",   "invalid" }, // Kök sertifikasının imzası bozuk
            });
        }

        public PAdES_P4_LT_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lt", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LTA_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LTA_P4_36.pdf",   "invalid" }, // Kök sertifikasının imzası bozuk
            });
        }

        public PAdES_P4_LTA_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lta", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LT_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LT_P4_11.pdf",   "invalid" }, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
            });
        }

        public PAdES_P4_LT_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lt", expectedResult, policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class PAdES_P4_LTA_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"LTA_P4_11.pdf",   "invalid" }, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
            });
        }

        public PAdES_P4_LTA_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "esya-signature-config-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validatePAdESSignature(pfx, "p4_lta", expectedResult, policyFile);
        }
    }

    public static void validatePAdESSignature(String file, String type, String expectedResult, String policyFile) throws Throwable {

        PAdESContext context = new PAdESContext(new File(TestSuiteCommonMethods.FOLDER).toURI());
        context.setConfig(new Config(TestSuiteCommonMethods.FOLDER + "config\\" + policyFile));
        signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(folder + "data\\validation-pades-" + type + "\\" + file), context);
        Assert.assertNotNull(signatureContainer);
        PAdESSignature padesSignature = (PAdESSignature) signatureContainer.getSignatures().get(0);
        Assert.assertNotNull(padesSignature);

        validationResult = signatureContainer.verifyAll();
        signatureContainer.close();

        Assert.assertNotNull(validationResult);
        System.out.println(validationResult);

        String validationMessage = validationResult.toString();
        ContainerValidationResultType result = validationResult.getResultType();

        if(expectedResult.equals("valid")){
            Assert.assertEquals(validationMessage, ContainerValidationResultType.ALL_VALID, result);
        }
        else {
            Assert.assertNotEquals(validationMessage, ContainerValidationResultType.ALL_VALID, result);
        }
    }
}

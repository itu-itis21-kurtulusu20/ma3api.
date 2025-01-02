package tr.gov.tubitak.uekae.esya.api.testsuite;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

@RunWith(Enclosed.class)
public class CAdES_Validation_Tests {

    static BaseSignedData contentInfo;
    static SignedDataValidationResult validationResult;
    static ValidationPolicy validationPolicy;
    static Hashtable<String, Object> params = new Hashtable<String, Object>();
    static String rootFolder = TestSuiteCommonMethods.FOLDER;


    @RunWith(Parameterized.class)
    public static class CAdES_BES_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"BES_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"BES_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"BES_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"BES_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"BES_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"BES_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                    {"BES_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"BES_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"BES_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"BES_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"BES_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"BES_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"BES_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış
                    {"BES_16_2.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
                    {"BES_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış
                    {"BES_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"BES_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"BES_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"BES_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"BES_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"BES_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"BES_24_1.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş
                    {"BES_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"BES_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"BES_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş
                    {"BES_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"BES_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"BES_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş
                    {"BES_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"BES_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"BES_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"BES_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"BES_37_1.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş
                    {"BES_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"BES_39_p.pdf.p7s" ,"invalid"} // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya

                    /*    API kapsamının dışındaki senaryolar:
                    #     | BES_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | BES_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | BES_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | BES_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | BES_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_BES_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "bes", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_BES_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"BES_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"BES_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"BES_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"BES_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"BES_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"BES_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                    {"BES_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"BES_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"BES_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"BES_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"BES_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"BES_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"BES_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış
                    {"BES_16_2.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
                    {"BES_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış
                    {"BES_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"BES_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"BES_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"BES_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"BES_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"BES_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"BES_24_1.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş
                    {"BES_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"BES_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"BES_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş
                    {"BES_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"BES_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"BES_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş
                    {"BES_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"BES_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"BES_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"BES_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"BES_37_1.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş
                    {"BES_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"BES_39_p.pdf.p7s" ,"invalid"} // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya

                    /*    API kapsamının dışındaki senaryolar:
                    #     | BES_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | BES_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | BES_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | BES_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | BES_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_BES_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "bes", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_EST_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"EST_1.pdf.p7s",   "valid"},   // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"EST_4.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"EST_5.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"EST_6.pdf.p7s",   "valid"},   // SHA-1 algoritması kullanılarak imzalanmış
                    {"EST_7.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"EST_8.pdf.p7s",   "valid"},   // Geçerli imza
                    {"EST_9.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"EST_10.pdf.p7s",  "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"EST_11.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"EST_12.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"EST_14.pdf.p7s",  "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"EST_15.pdf.p7s",  "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"EST_16_1.pdf.p7s","invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                           |
                    {"EST_16_2.pdf.p7s","valid"},   // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                          |
                    {"EST_17_1.pdf.p7s","invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                          |
                    {"EST_17_2.pdf.p7s","valid"},   // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                         |
                    {"EST_18.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş                                                                                    |
                    {"EST_19.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk                                                                                |
                    {"EST_20.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş                                                                            |
                    {"EST_21.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk                                                                           |
                    {"EST_22.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş                                                                                  |
                    {"EST_23.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk                                                                                   |
                    {"EST_24_1.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                          |
                    {"EST_24_2.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                        |
                    {"EST_27.pdf.p7s",  "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"EST_28.pdf.p7s",  "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"EST_29_1.pdf.p7s","invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                                |
                    {"EST_29_2.pdf.p7s","valid"},   // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                               |
                    {"EST_30.pdf.p7s",  "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor                                                                            |
                    {"EST_31.pdf.p7s",  "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor                                                                             |
                    {"EST_32_1.pdf.p7s","invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                               |
                    {"EST_32_2.pdf.p7s","valid"},   // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                              |
                    {"EST_33.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş                                                     |
                    {"EST_34.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk                                                      |
                    {"EST_35.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş                                     |
                    {"EST_36.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk                                                 |
                    {"EST_37_1.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş       |
                    {"EST_37_2.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş      |
                    {"EST_39_s.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya                                                     |
                    {"EST_39_p.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya                                                  |
                    {"EST_40.pdf.p7s",  "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası                                     |
                    {"EST_41.pdf.p7s",  "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"EST_42.pdf.p7s",  "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş                                                               |
                    {"EST_43.pdf.p7s",  "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk                                                            |
                    {"EST_44.pdf.p7s",  "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş                                 |
                    {"EST_45.pdf.p7s",  "valid"},   // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş                                |
                    {"EST_46.pdf.p7s",  "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş                                                      |
                    {"EST_47.pdf.p7s",  "valid"},   // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"EST_48.pdf.p7s",  "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor                                                      |
                    {"EST_49.pdf.p7s",  "invalid"} // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor

                    /*     API kapsamının dışındaki senaryolar:
                    #     | EST_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | EST_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | EST_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | EST_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | EST_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış   */
            });
        }

        public CAdES_EST_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "est", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_EST_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"EST_1.pdf.p7s",   "valid"},   // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"EST_4.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"EST_5.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"EST_6.pdf.p7s",   "valid"},   // SHA-1 algoritması kullanılarak imzalanmış
                    {"EST_7.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"EST_8.pdf.p7s",   "valid"},   // Geçerli imza
                    {"EST_9.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"EST_10.pdf.p7s",  "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"EST_11.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"EST_12.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"EST_14.pdf.p7s",  "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"EST_15.pdf.p7s",  "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"EST_16_1.pdf.p7s","invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                           |
                    {"EST_16_2.pdf.p7s","valid"},   // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                          |
                    {"EST_17_1.pdf.p7s","invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                          |
                    {"EST_17_2.pdf.p7s","valid"},   // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                         |
                    {"EST_18.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş                                                                                    |
                    {"EST_19.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk                                                                                |
                    {"EST_20.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş                                                                            |
                    {"EST_21.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk                                                                           |
                    {"EST_22.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş                                                                                  |
                    {"EST_23.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk                                                                                   |
                    {"EST_24_1.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                          |
                    {"EST_24_2.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                        |
                    {"EST_27.pdf.p7s",  "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"EST_28.pdf.p7s",  "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"EST_29_1.pdf.p7s","invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                                |
                    {"EST_29_2.pdf.p7s","valid"},   // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                               |
                    {"EST_30.pdf.p7s",  "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor                                                                            |
                    {"EST_31.pdf.p7s",  "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor                                                                             |
                    {"EST_32_1.pdf.p7s","invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                               |
                    {"EST_32_2.pdf.p7s","valid"},   // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                              |
                    {"EST_33.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş                                                     |
                    {"EST_34.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk                                                      |
                    {"EST_35.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş                                     |
                    {"EST_36.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk                                                 |
                    {"EST_37_1.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş       |
                    {"EST_37_2.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş      |
                    {"EST_39_s.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya                                                     |
                    {"EST_39_p.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya                                                  |
                    {"EST_40.pdf.p7s",  "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası                                     |
                    {"EST_41.pdf.p7s",  "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"EST_42.pdf.p7s",  "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş                                                               |
                    {"EST_43.pdf.p7s",  "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk                                                            |
                    {"EST_44.pdf.p7s",  "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş                                 |
                    {"EST_45.pdf.p7s",  "valid"},   // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş                                |
                    {"EST_46.pdf.p7s",  "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş                                                      |
                    {"EST_47.pdf.p7s",  "valid"},   // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"EST_48.pdf.p7s",  "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor                                                      |
                    {"EST_49.pdf.p7s",  "invalid"} // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor

                    /*     API kapsamının dışındaki senaryolar:
                    #     | EST_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | EST_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | EST_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | EST_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | EST_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_EST_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "est", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_XLONG_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESXL_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"ESXL_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"ESXL_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"ESXL_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"ESXL_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"ESXL_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                    {"ESXL_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"ESXL_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"ESXL_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"ESXL_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"ESXL_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"ESXL_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"ESXL_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"ESXL_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"ESXL_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"ESXL_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"ESXL_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"ESXL_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"ESXL_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"ESXL_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"ESXL_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESXL_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESXL_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"ESXL_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"ESXL_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"ESXL_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"ESXL_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESXL_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESXL_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESXL_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESXL_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESXL_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESXL_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESXL_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESXL_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESXL_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESXL_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESXL_51.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_52.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESXL_53.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_54.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                    {"ESXL_55.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_56.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
                    {"ESXL_57.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_58.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
                    {"ESXL_59.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_60.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"ESXL_61.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"ESXL_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"ESXL_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri “ içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                    {"ESXL_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL ‘inin olmadığı imza dosyası
                    {"ESXL_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                    {"ESXL_82.pdf.p7s"   ,"invalid"} // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESXL_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | ESXL_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | ESXL_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | ESXL_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | ESXL_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            });
        }

        public CAdES_XLONG_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "xlong", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_XLONG_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESXL_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"ESXL_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"ESXL_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"ESXL_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"ESXL_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"ESXL_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                    {"ESXL_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"ESXL_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"ESXL_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"ESXL_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"ESXL_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"ESXL_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"ESXL_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"ESXL_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"ESXL_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"ESXL_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"ESXL_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"ESXL_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"ESXL_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"ESXL_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"ESXL_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESXL_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESXL_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"ESXL_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"ESXL_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"ESXL_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"ESXL_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESXL_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESXL_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESXL_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESXL_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESXL_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESXL_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESXL_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESXL_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESXL_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESXL_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESXL_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESXL_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESXL_51.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_52.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESXL_53.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_54.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                    {"ESXL_55.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_56.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
                    {"ESXL_57.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_58.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
                    {"ESXL_59.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_60.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"ESXL_61.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"ESXL_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"ESXL_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"ESXL_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri “ içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                    {"ESXL_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL ‘inin olmadığı imza dosyası
                    {"ESXL_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                    {"ESXL_82.pdf.p7s"   ,"invalid"} // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESXL_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | ESXL_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | ESXL_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | ESXL_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | ESXL_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            });
        }

        public CAdES_XLONG_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "xlong", expectedResult, "detached",policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"ESA_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"ESA_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"ESA_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"ESA_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"ESA_8.pdf.p7s"    ,"valid"}, // Geçerli imza
                    {"ESA_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"ESA_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"ESA_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"ESA_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"ESA_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"ESA_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"ESA_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"ESA_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"ESA_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"ESA_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"ESA_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"ESA_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"ESA_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"ESA_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"ESA_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"ESA_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"ESA_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"ESA_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"ESA_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESA_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESA_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESA_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESA_51.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESA_52.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESA_53.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_54.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_55.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_56.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_57.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_58.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_59.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_60.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESA_61.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_62_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_63_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
                    {"ESA_64_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"ESA_65_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"ESA_66_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_67_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_68_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_69_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
                    {"ESA_70_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_71_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"ESA_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"ESA_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                    {"ESA_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL’inin olmadığı imza dosyası
                    {"ESA_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                    {"ESA_82.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
                    {"ESA_84.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_85.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_86.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_87.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_88.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_89.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_90.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_91.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
                    {"ESA_92.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_93.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_95.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"ESA_97.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"ESA_101.pdf.p7s"  ,"invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"ESA_106.pdf.p7s"  ,"valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                    {"ESA_109.pdf.p7s"  ,"valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış

                    /*    API kapsamının dışındaki senaryolar:
                    #     | ESA_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | ESA_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | ESA_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | ESA_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | ESA_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_ESA_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"ESA_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"ESA_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"ESA_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"ESA_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                    {"ESA_8.pdf.p7s"    ,"valid"}, // Geçerli imza
                    {"ESA_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"ESA_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"ESA_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"ESA_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"ESA_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"ESA_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"ESA_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                    {"ESA_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                    {"ESA_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"ESA_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"ESA_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"ESA_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"ESA_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"ESA_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"ESA_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                    {"ESA_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                    {"ESA_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                    {"ESA_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                    {"ESA_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESA_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESA_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESA_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"ESA_51.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                    {"ESA_52.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                    {"ESA_53.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_54.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_55.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_56.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_57.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_58.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_59.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_60.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                    {"ESA_61.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_62_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_63_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
                    {"ESA_64_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"ESA_65_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"ESA_66_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_67_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_68_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_69_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
                    {"ESA_70_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_71_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"ESA_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"ESA_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                    {"ESA_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL’inin olmadığı imza dosyası
                    {"ESA_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                    {"ESA_82.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
                    {"ESA_84.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"ESA_85.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
                    {"ESA_86.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                    {"ESA_87.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                    {"ESA_88.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"ESA_89.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"ESA_90.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                    {"ESA_91.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
                    {"ESA_92.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                    {"ESA_93.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                    {"ESA_95.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"ESA_97.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"ESA_101.pdf.p7s"  ,"invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"ESA_106.pdf.p7s"  ,"valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                    {"ESA_109.pdf.p7s"  ,"valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESA_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | ESA_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | ESA_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | ESA_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | ESA_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_ESA_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_XLONG_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_1.pdf.p7s",    "valid"},    //Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"P4_4.pdf.p7s",    "invalid"},  //“SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                    {"P4_5.pdf.p7s",    "invalid"},  //“SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"P4_6.pdf.p7s",    "valid"},    //“SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"P4_7.pdf.p7s",    "invalid"},  //“ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"P4_8.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği eklenmemiş
                    {"P4_9.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                    {"P4_10.pdf.p7s",   "invalid"},  //NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"P4_12.pdf.p7s",   "invalid"},  //“İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"P4_13.pdf.p7s",   "invalid"},  //“ESS-Signing-Certificate” içeriği bozulmuş
                    {"P4_14.pdf.p7s",   "invalid"},  //“messageDigest” imza özelliği bozulmuş
                    {"P4_15.pdf.p7s",   "valid"},    //SHA-1 algoritması kullanılarak imzalanmış
                    {"P4_16.pdf.p7s",   "invalid"},  //İmza dosyasının imzası bozulmuş
                    {"P4_17.pdf.p7s",   "invalid"},  //“Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"P4_18.pdf.p7s",   "invalid"},  //“Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"P4_19.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_20.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_22.pdf.p7s",   "invalid"},  //Süresi dolmuş sertifika ile imzalanmış
                    {"P4_23.pdf.p7s",   "invalid"},  //İmzası bozuk sertifika ile imzalanmış
                    {"P4_24.pdf.p7s",   "invalid"},  //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_25.pdf.p7s",   "valid"},    //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_26.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabının geçerlilik süresi dolmuş
                    {"P4_27.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"P4_28.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"P4_29.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"P4_30.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_31.pdf.p7s",   "valid"},    //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_34.pdf.p7s",   "invalid"},  //OCSP cevabı içindeki sertifika farklı
                    {"P4_35.pdf.p7s",   "invalid"},  //Alt kök sertifikasının imzası bozuk
                    {"P4_37.pdf.p7s",   "invalid"},  //“İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_38.pdf.p7s",   "invalid"},  //“İmza ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_39.pdf.p7s",   "invalid"},  //“İmza ZD” TSA3'den alınmış; ZD sertifikasının geçerlilik süresi dolmuş
                    {"P4_40.pdf.p7s",   "invalid"},  //“İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"P4_41.pdf.p7s",   "invalid"},  //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_42.pdf.p7s",   "valid"},    //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_43.pdf.p7s",   "invalid"},  //“İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_44.pdf.p7s",   "valid"},    //“İmza ZD” TSC1'den alınmış; Geçerli
                    {"P4_45.pdf.p7s",   "invalid"},  //“İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_46.pdf.p7s",   "invalid"},  //“İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_47.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_48.pdf.p7s",   "invalid"},  //“Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_49.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_50.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_51.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                    {"P4_52.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                    {"P4_53.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"P4_54.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_55.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"P4_57.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"P4_59.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                    {"P4_61.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                    {"P4_93_s.pdf.p7s", "invalid"},  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya
                    {"P4_93_p.pdf.p7s", "invalid"}  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli paralel imzalı dosya

                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | P4_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | P4_21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | P4_32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | P4_33.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            });
        }

        public CAdES_P4_XLONG_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_XLONG_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_1.pdf.p7s",    "valid"},    //Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"P4_4.pdf.p7s",    "invalid"},  //“SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                    {"P4_5.pdf.p7s",    "invalid"},  //“SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"P4_6.pdf.p7s",    "valid"},    //“SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"P4_7.pdf.p7s",    "invalid"},  //“ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"P4_8.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği eklenmemiş
                    {"P4_9.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                    {"P4_10.pdf.p7s",   "invalid"},  //NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"P4_12.pdf.p7s",   "invalid"},  //“İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                    {"P4_13.pdf.p7s",   "invalid"},  //“ESS-Signing-Certificate” içeriği bozulmuş
                    {"P4_14.pdf.p7s",   "invalid"},  //“messageDigest” imza özelliği bozulmuş
                    {"P4_15.pdf.p7s",   "valid"},    //SHA-1 algoritması kullanılarak imzalanmış
                    {"P4_16.pdf.p7s",   "invalid"},  //İmza dosyasının imzası bozulmuş
                    {"P4_17.pdf.p7s",   "invalid"},  //“Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"P4_18.pdf.p7s",   "invalid"},  //“Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"P4_19.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_20.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_22.pdf.p7s",   "invalid"},  //Süresi dolmuş sertifika ile imzalanmış
                    {"P4_23.pdf.p7s",   "invalid"},  //İmzası bozuk sertifika ile imzalanmış
                    {"P4_24.pdf.p7s",   "invalid"},  //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_25.pdf.p7s",   "valid"},    //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_26.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabının geçerlilik süresi dolmuş
                    {"P4_27.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"P4_28.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"P4_29.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"P4_30.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_31.pdf.p7s",   "valid"},    //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_34.pdf.p7s",   "invalid"},  //OCSP cevabı içindeki sertifika farklı
                    {"P4_35.pdf.p7s",   "invalid"},  //Alt kök sertifikasının imzası bozuk
                    {"P4_37.pdf.p7s",   "invalid"},  //“İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_38.pdf.p7s",   "invalid"},  //“İmza ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_39.pdf.p7s",   "invalid"},  //“İmza ZD” TSA3'den alınmış; ZD sertifikasının geçerlilik süresi dolmuş
                    {"P4_40.pdf.p7s",   "invalid"},  //“İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"P4_41.pdf.p7s",   "invalid"},  //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_42.pdf.p7s",   "valid"},    //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_43.pdf.p7s",   "invalid"},  //“İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_44.pdf.p7s",   "valid"},    //“İmza ZD” TSC1'den alınmış; Geçerli
                    {"P4_45.pdf.p7s",   "invalid"},  //“İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_46.pdf.p7s",   "invalid"},  //“İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_47.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_48.pdf.p7s",   "invalid"},  //“Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_49.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_50.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_51.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                    {"P4_52.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                    {"P4_53.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"P4_54.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_55.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"P4_57.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"P4_59.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                    {"P4_61.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                    {"P4_93_s.pdf.p7s", "invalid"},  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya
                    {"P4_93_p.pdf.p7s", "invalid"}  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli paralel imzalı dosya

                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | P4_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | P4_21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | P4_32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | P4_33.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            });
        }

        public CAdES_P4_XLONG_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Attached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A1.pdf.p7s",    "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"P4_A4.pdf.p7s",    "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                    {"P4_A5.pdf.p7s",    "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"P4_A6.pdf.p7s",    "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"P4_A7.pdf.p7s",    "invalid"}, // “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"P4_A8.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği eklenmemiş
                    {"P4_A9.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği ZD tarihinden 3 saat önce eklenmiş
                    {"P4_A10.pdf.p7s",   "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"P4_A12.pdf.p7s",   "invalid"}, // “İmza ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"P4_A13.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"P4_A14.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"P4_A15.pdf.p7s",   "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"P4_A16.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"P4_A17.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"P4_A18.pdf.p7s",   "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"P4_A19.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_A20.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_A22.pdf.p7s",   "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"P4_A23.pdf.p7s",   "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"P4_A24.pdf.p7s",   "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A25.pdf.p7s",   "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A26.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"P4_A27.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"P4_A28.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"P4_A29.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"P4_A30.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A31.pdf.p7s",   "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A34.pdf.p7s",   "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"P4_A35.pdf.p7s",   "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"P4_A37.pdf.p7s",   "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_A38.pdf.p7s",   "invalid"}, // “İmza ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_A39.pdf.p7s",   "invalid"}, // “İmza ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"P4_A40.pdf.p7s",   "invalid"}, // “İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHSimzası bozuk
                    {"P4_A41.pdf.p7s",   "invalid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A42.pdf.p7s",   "valid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A43.pdf.p7s",   "invalid"}, // “İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_A44.pdf.p7s",   "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
                    {"P4_A45.pdf.p7s",   "invalid"}, // “İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_A46.pdf.p7s",   "invalid"}, // “İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_A47.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_A48.pdf.p7s",   "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A49.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_A50.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A51.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
                    {"P4_A52.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A53.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"P4_A54.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A55.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"P4_A57.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"P4_A59.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                    {"P4_A61.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                    {"P4_A75.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_A76.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_A77.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"P4_A78.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"P4_A79.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A80.pdf.p7s",   "valid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A81.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_A82.pdf.p7s",   "valid"}, // “Arşiv ZD” TSC1'den alınmış; Geçerli
                    {"P4_A83.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_A84.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_A86.pdf.p7s",   "invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"P4_A90.pdf.p7s",   "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                    {"P4_A93_s.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
                    {"P4_A93_p.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
                    {"P4_A94.pdf.p7s",   "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_A2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | P4_A3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | P4_A21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | P4_A32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | P4_A33.pdf.p7s   | geçerli   | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_P4_A_Attached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Detached_Signature_Validation {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A1.pdf.p7s",    "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                    {"P4_A4.pdf.p7s",    "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                    {"P4_A5.pdf.p7s",    "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                    {"P4_A6.pdf.p7s",    "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                    {"P4_A7.pdf.p7s",    "invalid"}, // “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                    {"P4_A8.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği eklenmemiş
                    {"P4_A9.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği ZD tarihinden 3 saat önce eklenmiş
                    {"P4_A10.pdf.p7s",   "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
                    {"P4_A12.pdf.p7s",   "invalid"}, // “İmza ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"P4_A13.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                    {"P4_A14.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                    {"P4_A15.pdf.p7s",   "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                    {"P4_A16.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                    {"P4_A17.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                    {"P4_A18.pdf.p7s",   "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                    {"P4_A19.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_A20.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                    {"P4_A22.pdf.p7s",   "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                    {"P4_A23.pdf.p7s",   "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                    {"P4_A24.pdf.p7s",   "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A25.pdf.p7s",   "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A26.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                    {"P4_A27.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                    {"P4_A28.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                    {"P4_A29.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                    {"P4_A30.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A31.pdf.p7s",   "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A34.pdf.p7s",   "invalid"}, // OCSP cevabı içindeki sertifika farklı
                    {"P4_A35.pdf.p7s",   "invalid"}, // Alt kök sertifikasının imzası bozuk
                    {"P4_A37.pdf.p7s",   "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_A38.pdf.p7s",   "invalid"}, // “İmza ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_A39.pdf.p7s",   "invalid"}, // “İmza ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"P4_A40.pdf.p7s",   "invalid"}, // “İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHSimzası bozuk
                    {"P4_A41.pdf.p7s",   "invalid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A42.pdf.p7s",   "valid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A43.pdf.p7s",   "invalid"}, // “İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_A44.pdf.p7s",   "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
                    {"P4_A45.pdf.p7s",   "invalid"}, // “İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_A46.pdf.p7s",   "invalid"}, // “İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_A47.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_A48.pdf.p7s",   "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A49.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                    {"P4_A50.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A51.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
                    {"P4_A52.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A53.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                    {"P4_A54.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                    {"P4_A55.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                    {"P4_A57.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                    {"P4_A59.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                    {"P4_A61.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                    {"P4_A75.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                    {"P4_A76.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA2'den alınmış ZD imzası bozuk
                    {"P4_A77.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                    {"P4_A78.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                    {"P4_A79.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                    {"P4_A80.pdf.p7s",   "valid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                    {"P4_A81.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                    {"P4_A82.pdf.p7s",   "valid"}, // “Arşiv ZD” TSC1'den alınmış; Geçerli
                    {"P4_A83.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                    {"P4_A84.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                    {"P4_A86.pdf.p7s",   "invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                    {"P4_A90.pdf.p7s",   "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                    {"P4_A93_s.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
                    {"P4_A93_p.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
                    {"P4_A94.pdf.p7s",   "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_A2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | P4_A3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | P4_A21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | P4_A32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | P4_A33.pdf.p7s   | geçerli   | Kullanım kısıtı olan sertifika ile imzalanmış */
            });
        }

        public CAdES_P4_A_Detached_Signature_Validation(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Attached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_94.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
                    {"ESA_96.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                    {"ESA_100.pdf.p7s"  ,"invalid"} // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            });
        }

        public CAdES_ESA_Attached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Detached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_94.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
                    {"ESA_96.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                    {"ESA_100.pdf.p7s"  ,"invalid"} // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            });
        }

        public CAdES_ESA_Detached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Attached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A11.pdf.p7s"   ,"invalid"},  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                    {"P4_A85.pdf.p7s"   ,"invalid"}   // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            });
        }

        public CAdES_P4_A_Attached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Detached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A11.pdf.p7s"   ,"invalid"},  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                    {"P4_A85.pdf.p7s"   ,"invalid"}   // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            });
        }

        public CAdES_P4_A_Detached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_XLONG_Attached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_11.pdf.p7s"   ,"invalid"}  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                  });
        }

        public CAdES_P4_XLONG_Attached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_XLONG_Detached_Signature_Validation_With_Missing_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_11.pdf.p7s"   ,"invalid"}  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                  });
        }

        public CAdES_P4_XLONG_Detached_Signature_Validation_With_Missing_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-unavailable-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_BES_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"BES_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
                  });
        }

        public CAdES_BES_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "bes", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_BES_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"BES_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
                  });
        }

        public CAdES_BES_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "bes", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_EST_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"EST_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
                  });
        }

        public CAdES_EST_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "est", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_EST_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"EST_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
                  });
        }

        public CAdES_EST_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "est", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_XLONG_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESXL_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_XLONG_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "xlong", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_XLONG_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESXL_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_XLONG_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "xlong", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_ESA_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_ESA_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"ESA_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_ESA_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "esa", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_P4_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_P4_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4", expectedResult, "detached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Attached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_P4_A_Attached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "attached", policyFile);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_P4_A_Detached_Signature_Validation_With_Invalid_Root_Certificate {

        private String pfx;
        private String expectedResult;
        private String policyFile;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"P4_A36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            });
        }

        public CAdES_P4_A_Detached_Signature_Validation_With_Invalid_Root_Certificate(String pfx, String expectedResult){
            this.pfx = pfx;
            this.expectedResult = expectedResult;
            this.policyFile = "testsuite-policy-invalid-root.xml";
        }

        @Test
        public void runTests() throws Throwable
        {
            validateCAdESSignature(pfx, "p4_a", expectedResult, "detached", policyFile);
        }
    }


    public static void validateCAdESSignature(String file, String type, String expectedResult, String contentMode, String policyFile) throws Throwable {

        FileInputStream fileInputStream = new FileInputStream(rootFolder + "config\\policy\\" + policyFile);
        validationPolicy = PolicyReader.readValidationPolicy(fileInputStream);
        readBaseSignedData(type, file, contentMode);
        Assert.assertNotNull(contentInfo);
        fileInputStream.close();

        params.put(EParameters.P_IGNORE_GRACE, true);
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);
        params.put(EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS, 300L);

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        validationResult = signedDataValidation.verify(contentInfo.getEncoded(), params);
        Assert.assertNotNull(validationResult);
        System.out.println(validationResult);
        //validationResult.getSDValidationResults().get(0).getCheckerResults()

        SignedData_Status expectedStatus;
        if(expectedResult.equals("valid"))
            expectedStatus = SignedData_Status.ALL_VALID;
        else
            expectedStatus = SignedData_Status.NOT_ALL_VALID;

        SignedData_Status status = validationResult.getSDStatus();
        Assert.assertEquals(validationResult.toString(), expectedStatus, status);

        params.clear();
    }

    private static void readBaseSignedData(String type, String file, String contentMode) throws IOException, CMSSignatureException {

        String folder = "data\\validation-cades-" + type + "\\" + contentMode + "\\";

        byte[] bytes = AsnIO.dosyadanOKU(rootFolder + folder + file);
        if (contentMode.equals("detached")) {
            String externalDocumentName = getExternalDocumentName(file);
            params.put(EParameters.P_EXTERNAL_CONTENT, new SignableFile(new File(rootFolder + folder + externalDocumentName)));
        }

        contentInfo = new BaseSignedData(bytes);
    }

    private static String getExternalDocumentName(String dosya) {

        String documentName;

        if (dosya.equals("BES_2.doc.p7s")
                || dosya.equals("BES_13.pdf.p7s")
                || dosya.equals("ESA_2.doc.p7s")
                || dosya.equals("ESA_13.pdf.p7s")
                || dosya.equals("P4_2.doc.p7s")
                || dosya.equals("P4_21.pdf.p7s")
                || dosya.equals("P4_A2.doc.p7s")
                || dosya.equals("P4_A21.pdf.p7s")
                || dosya.equals("ESXL_2.doc.p7s")
                || dosya.equals("ESXL_13.pdf.p7s")) {
            documentName = dosya.replace(".p7s", "");
        } else {
            documentName = "Ortak.pdf";
        }
        return documentName;
    }
}


using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;


namespace tr.gov.tubitak.uekae.esya.api.testsuite
{
    [TestFixture]
    class CAdES_Validation_Tests
    {                  
        static BaseSignedData contentInfo;
        static SignedDataValidationResult validationResult;    
        static ValidationPolicy validationPolicy;
        static Dictionary<String, Object> parameters = new Dictionary<String, Object>();
        static string rootFolder = TestSuiteCommonMethods.FOLDER;

        [TestFixture]
        public class CAdES_BES_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[] {"BES_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[] {"BES_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[] {"BES_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[] {"BES_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[] {"BES_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[] {"BES_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                new object[] {"BES_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[] {"BES_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[] {"BES_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[] {"BES_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[] {"BES_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[] {"BES_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[] {"BES_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış
                new object[] {"BES_16_2.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
                new object[] {"BES_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış
                new object[] {"BES_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[] {"BES_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[] {"BES_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[] {"BES_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[] {"BES_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[] {"BES_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[] {"BES_24_1.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş
                new object[] {"BES_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[] {"BES_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[] {"BES_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş
                new object[] {"BES_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[] {"BES_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[] {"BES_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş
                new object[] {"BES_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[] {"BES_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[] {"BES_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[] {"BES_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[] {"BES_37_1.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş
                new object[] {"BES_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[] {"BES_39_p.pdf.p7s" ,"invalid"} // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya

                /*    API kapsamının dışındaki senaryolar:
                #     | BES_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                #     | BES_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                #     | BES_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                #     | BES_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                #     | BES_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "bes", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_BES_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"BES_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"BES_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"BES_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"BES_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"BES_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"BES_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                new object[]  {"BES_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"BES_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"BES_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"BES_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"BES_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"BES_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"BES_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış
                new object[]  {"BES_16_2.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
                new object[]  {"BES_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış
                new object[]  {"BES_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[]  {"BES_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[]  {"BES_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"BES_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"BES_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"BES_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"BES_24_1.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş
                new object[]  {"BES_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"BES_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"BES_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş
                new object[]  {"BES_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"BES_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"BES_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş
                new object[]  {"BES_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[]  {"BES_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[]  {"BES_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[]  {"BES_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[]  {"BES_37_1.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş
                new object[]  {"BES_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"BES_39_p.pdf.p7s" ,"invalid"} // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya

                    /*    API kapsamının dışındaki senaryolar:
                    #     | BES_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | BES_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | BES_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | BES_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | BES_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            };
            
        
            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "bes", expectedResult, "detached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_EST_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"EST_1.pdf.p7s",   "valid"},   // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"EST_4.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"EST_5.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"EST_6.pdf.p7s",   "valid"},   // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"EST_7.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"EST_8.pdf.p7s",   "valid"},   // Geçerli imza
                new object[]  {"EST_9.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"EST_10.pdf.p7s",  "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"EST_11.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"EST_12.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"EST_14.pdf.p7s",  "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"EST_15.pdf.p7s",  "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"EST_16_1.pdf.p7s","invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                           |
                new object[]  {"EST_16_2.pdf.p7s","valid"},   // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                          |
                new object[]  {"EST_17_1.pdf.p7s","invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                          |
                new object[]  {"EST_17_2.pdf.p7s","valid"},   // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                         |
                new object[]  {"EST_18.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş                                                                                    |
                new object[]  {"EST_19.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk                                                                                |
                new object[]  {"EST_20.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş                                                                            |
                new object[]  {"EST_21.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk                                                                           |
                new object[]  {"EST_22.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş                                                                                  |
                new object[]  {"EST_23.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk                                                                                   |
                new object[]  {"EST_24_1.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                          |
                new object[]  {"EST_24_2.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                        |
                new object[]  {"EST_27.pdf.p7s",  "invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"EST_28.pdf.p7s",  "invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"EST_29_1.pdf.p7s","invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                                |
                new object[]  {"EST_29_2.pdf.p7s","valid"},   // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                               |
                new object[]  {"EST_30.pdf.p7s",  "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor                                                                            |
                new object[]  {"EST_31.pdf.p7s",  "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor                                                                             |
                new object[]  {"EST_32_1.pdf.p7s","invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                               |
                new object[]  {"EST_32_2.pdf.p7s","valid"},   // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                              |
                new object[]  {"EST_33.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş                                                     |
                new object[]  {"EST_34.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk                                                      |
                new object[]  {"EST_35.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş                                     |
                new object[]  {"EST_36.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk                                                 |
                new object[]  {"EST_37_1.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş       |
                new object[]  {"EST_37_2.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş      |
                new object[]  {"EST_39_s.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya                                                     |
                new object[]  {"EST_39_p.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya                                                  |
                new object[]  {"EST_40.pdf.p7s",  "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası                                     |
                new object[]  {"EST_41.pdf.p7s",  "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"EST_42.pdf.p7s",  "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş                                                               |
                new object[]  {"EST_43.pdf.p7s",  "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk                                                            |
                new object[]  {"EST_44.pdf.p7s",  "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş                                 |
                new object[]  {"EST_45.pdf.p7s",  "valid"},   // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş                                |
                new object[]  {"EST_46.pdf.p7s",  "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş                                                      |
                new object[]  {"EST_47.pdf.p7s",  "valid"},   // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"EST_48.pdf.p7s",  "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor                                                      |
                new object[]  {"EST_49.pdf.p7s",  "invalid"} // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor

                    /*     API kapsamının dışındaki senaryolar:
                    #     | EST_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | EST_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | EST_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | EST_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | EST_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış   */
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "est", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_EST_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"EST_1.pdf.p7s",   "valid"},   // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"EST_4.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"EST_5.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"EST_6.pdf.p7s",   "valid"},   // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"EST_7.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"EST_8.pdf.p7s",   "valid"},   // Geçerli imza
                new object[]  {"EST_9.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"EST_10.pdf.p7s",  "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"EST_11.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"EST_12.pdf.p7s",  "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"EST_14.pdf.p7s",  "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"EST_15.pdf.p7s",  "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"EST_16_1.pdf.p7s","invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                           |
                new object[]  {"EST_16_2.pdf.p7s","valid"},   // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                          |
                new object[]  {"EST_17_1.pdf.p7s","invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş                                          |
                new object[]  {"EST_17_2.pdf.p7s","valid"},   // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş                                         |
                new object[]  {"EST_18.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş                                                                                    |
                new object[]  {"EST_19.pdf.p7s",  "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk                                                                                |
                new object[]  {"EST_20.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş                                                                            |
                new object[]  {"EST_21.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk                                                                           |
                new object[]  {"EST_22.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş                                                                                  |
                new object[]  {"EST_23.pdf.p7s",  "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk                                                                                   |
                new object[]  {"EST_24_1.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                          |
                new object[]  {"EST_24_2.pdf.p7s","valid"},   // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                        |
                new object[]  {"EST_27.pdf.p7s",  "invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"EST_28.pdf.p7s",  "invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"EST_29_1.pdf.p7s","invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                                |
                new object[]  {"EST_29_2.pdf.p7s","valid"},   // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                               |
                new object[]  {"EST_30.pdf.p7s",  "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor                                                                            |
                new object[]  {"EST_31.pdf.p7s",  "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor                                                                             |
                new object[]  {"EST_32_1.pdf.p7s","invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş                                               |
                new object[]  {"EST_32_2.pdf.p7s","valid"},   // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş                                              |
                new object[]  {"EST_33.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş                                                     |
                new object[]  {"EST_34.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk                                                      |
                new object[]  {"EST_35.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş                                     |
                new object[]  {"EST_36.pdf.p7s",  "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk                                                 |
                new object[]  {"EST_37_1.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş       |
                new object[]  {"EST_37_2.pdf.p7s","valid"},   // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş      |
                new object[]  {"EST_39_s.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya                                                     |
                new object[]  {"EST_39_p.pdf.p7s","invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya                                                  |
                new object[]  {"EST_40.pdf.p7s",  "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası                                     |
                new object[]  {"EST_41.pdf.p7s",  "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"EST_42.pdf.p7s",  "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş                                                               |
                new object[]  {"EST_43.pdf.p7s",  "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk                                                            |
                new object[]  {"EST_44.pdf.p7s",  "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş                                 |
                new object[]  {"EST_45.pdf.p7s",  "valid"},   // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş                                |
                new object[]  {"EST_46.pdf.p7s",  "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş                                                      |
                new object[]  {"EST_47.pdf.p7s",  "valid"},   // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"EST_48.pdf.p7s",  "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor                                                      |
                new object[]  {"EST_49.pdf.p7s",  "invalid"} // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor

                    /*     API kapsamının dışındaki senaryolar:
                    #     | EST_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | EST_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | EST_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | EST_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | EST_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış */
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "est", expectedResult, "detached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_XLONG_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"ESXL_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"ESXL_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"ESXL_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"ESXL_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"ESXL_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"ESXL_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                new object[]  {"ESXL_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"ESXL_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"ESXL_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESXL_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESXL_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"ESXL_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"ESXL_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[]  {"ESXL_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[]  {"ESXL_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"ESXL_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"ESXL_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"ESXL_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"ESXL_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"ESXL_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"ESXL_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESXL_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESXL_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[]  {"ESXL_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[]  {"ESXL_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[]  {"ESXL_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[]  {"ESXL_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"ESXL_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]  {"ESXL_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESXL_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESXL_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESXL_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESXL_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESXL_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESXL_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESXL_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESXL_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESXL_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESXL_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_51.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_52.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_53.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_54.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_55.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_56.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_57.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_58.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_59.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_60.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESXL_61.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"ESXL_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESXL_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESXL_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri “ içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                new object[]  {"ESXL_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL ‘inin olmadığı imza dosyası
                new object[]  {"ESXL_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                new object[]  {"ESXL_82.pdf.p7s"   ,"invalid"} // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESXL_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | ESXL_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | ESXL_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | ESXL_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | ESXL_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            };
            
            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "xlong", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_XLONG_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]   {"ESXL_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]   {"ESXL_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]   {"ESXL_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]   {"ESXL_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]   {"ESXL_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]   {"ESXL_8.pdf.p7s"    ,"valid"}, // Geçerli İmza
                new object[]   {"ESXL_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]   {"ESXL_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]   {"ESXL_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]   {"ESXL_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]   {"ESXL_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]   {"ESXL_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]   {"ESXL_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[]   {"ESXL_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[]   {"ESXL_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]   {"ESXL_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]   {"ESXL_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]   {"ESXL_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]   {"ESXL_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]   {"ESXL_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]   {"ESXL_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]   {"ESXL_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[]   {"ESXL_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[]   {"ESXL_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[]   {"ESXL_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[]   {"ESXL_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[]   {"ESXL_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]   {"ESXL_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]   {"ESXL_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]   {"ESXL_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]   {"ESXL_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]   {"ESXL_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]   {"ESXL_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]   {"ESXL_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]   {"ESXL_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]   {"ESXL_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]   {"ESXL_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]   {"ESXL_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]   {"ESXL_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_51.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_52.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_53.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_54.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_55.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_56.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_57.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_58.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_59.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_60.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]   {"ESXL_61.pdf.p7s"   ,"invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]   {"ESXL_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]   {"ESXL_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]   {"ESXL_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri “ içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                new object[]   {"ESXL_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL ‘inin olmadığı imza dosyası
                new object[]   {"ESXL_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                new object[]   {"ESXL_82.pdf.p7s"   ,"invalid"} // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESXL_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | ESXL_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | ESXL_13.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | ESXL_25.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | ESXL_26.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
            };
                  
            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "xlong", expectedResult, "detached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_ESA_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"ESA_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"ESA_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"ESA_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"ESA_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"ESA_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"ESA_8.pdf.p7s"    ,"valid"}, // Geçerli imza
                new object[]  {"ESA_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"ESA_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"ESA_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESA_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESA_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"ESA_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"ESA_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[]  {"ESA_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"ESA_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"ESA_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"ESA_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"ESA_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"ESA_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[]  {"ESA_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[]  {"ESA_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[]  {"ESA_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[]  {"ESA_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"ESA_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]  {"ESA_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESA_51.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"ESA_52.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]  {"ESA_53.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_54.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_55.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_56.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_57.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_58.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_59.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_60.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_61.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_62_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_63_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
                new object[]  {"ESA_64_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"ESA_65_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_66_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_67_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_68_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_69_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_70_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_71_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESA_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESA_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                new object[]  {"ESA_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL’inin olmadığı imza dosyası
                new object[]  {"ESA_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                new object[]  {"ESA_82.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
                new object[]  {"ESA_84.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_85.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_86.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_87.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_88.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_89.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_90.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_91.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_92.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_93.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_95.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_97.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_101.pdf.p7s"  ,"invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_106.pdf.p7s"  ,"valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                new object[]  {"ESA_109.pdf.p7s"  ,"valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış

                    /*    API kapsamının dışındaki senaryolar:
                    #     | ESA_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | ESA_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | ESA_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | ESA_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | ESA_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
            };

            [Test, TestCaseSource("TestCases")]          
            public void RunTests(String pfx, String expectedResult) 
            {
               ValidateCAdESSignature(pfx, "esa", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_ESA_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"ESA_1.pdf.p7s"    ,"valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"ESA_4.pdf.p7s"    ,"invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"ESA_5.pdf.p7s"    ,"invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"ESA_6.pdf.p7s"    ,"valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"ESA_7.pdf.p7s"    ,"invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"ESA_8.pdf.p7s"    ,"valid"}, // Geçerli imza
                new object[]  {"ESA_9.pdf.p7s"    ,"invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"ESA_10.pdf.p7s"   ,"invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"ESA_11.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESA_12.pdf.p7s"   ,"invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"ESA_14.pdf.p7s"   ,"invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"ESA_15.pdf.p7s"   ,"invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"ESA_16_1.pdf.p7s" ,"invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_16_2.pdf.p7s" ,"valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_17_1.pdf.p7s" ,"invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_17_2.pdf.p7s" ,"valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_18.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
                new object[]  {"ESA_19.pdf.p7s"   ,"invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_20.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"ESA_21.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_22.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"ESA_23.pdf.p7s"   ,"invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"ESA_24_1.pdf.p7s" ,"invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_24_2.pdf.p7s" ,"valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_27.pdf.p7s"   ,"invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"ESA_28.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"ESA_29_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_29_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_30.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_31.pdf.p7s"   ,"invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_32_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_32_2.pdf.p7s" ,"valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_33.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
                new object[]  {"ESA_34.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
                new object[]  {"ESA_35.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
                new object[]  {"ESA_36.pdf.p7s"   ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
                new object[]  {"ESA_37_1.pdf.p7s" ,"invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_37_2.pdf.p7s" ,"valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_39_s.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"ESA_39_p.pdf.p7s" ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]  {"ESA_40.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_41.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_42.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_43.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_44.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_45.pdf.p7s"   ,"valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_46.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_47.pdf.p7s"   ,"valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_48.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_49.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_50.pdf.p7s"   ,"invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"ESA_51.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
                new object[]  {"ESA_52.pdf.p7s"   ,"invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
                new object[]  {"ESA_53.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_54.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_55.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_56.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_57.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_58.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_59.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_60.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_61.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_62_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_63_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
                new object[]  {"ESA_64_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"ESA_65_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"ESA_66_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_67_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_68_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_69_1.pdf.p7s" ,"valid"}, // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_70_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_71_1.pdf.p7s" ,"invalid"}, // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_72.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESA_74.pdf.p7s"   ,"invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"ESA_76.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
                new object[]  {"ESA_78.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL’inin olmadığı imza dosyası
                new object[]  {"ESA_80.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
                new object[]  {"ESA_82.pdf.p7s"   ,"invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
                new object[]  {"ESA_84.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"ESA_85.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
                new object[]  {"ESA_86.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
                new object[]  {"ESA_87.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"ESA_88.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"ESA_89.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"ESA_90.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"ESA_91.pdf.p7s"   ,"valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
                new object[]  {"ESA_92.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
                new object[]  {"ESA_93.pdf.p7s"   ,"invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
                new object[]  {"ESA_95.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_97.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_101.pdf.p7s"  ,"invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"ESA_106.pdf.p7s"  ,"valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                new object[]  {"ESA_109.pdf.p7s"  ,"valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış

                    /*     API kapsamının dışındaki senaryolar:
                    #     | ESA_2.doc.p7s    | geçersiz | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | ESA_3.pdf.p7s    | geçersiz | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | ESA_13.pdf.p7s   | geçersiz | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | ESA_25.pdf.p7s   | geçerli  | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | ESA_26.pdf.p7s   | geçersiz | Kullanım kısıtı olan sertifika ile imzalanmış */
                
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "esa", expectedResult, "detached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_XLONG_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"P4_1.pdf.p7s",    "valid"},    //Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"P4_4.pdf.p7s",    "invalid"},  //“SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                new object[]  {"P4_5.pdf.p7s",    "invalid"},  //“SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                new object[]  {"P4_6.pdf.p7s",    "valid"},    //“SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                new object[]  {"P4_7.pdf.p7s",    "invalid"},  //“ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                new object[]  {"P4_8.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği eklenmemiş
                new object[]  {"P4_9.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                new object[]  {"P4_10.pdf.p7s",   "invalid"},  //NES’e ait OCSP Cevabı yerine SİL bulunan
                new object[]  {"P4_12.pdf.p7s",   "invalid"},  //“İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_13.pdf.p7s",   "invalid"},  //“ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"P4_14.pdf.p7s",   "invalid"},  //“messageDigest” imza özelliği bozulmuş
                new object[]  {"P4_15.pdf.p7s",   "valid"},    //SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"P4_16.pdf.p7s",   "invalid"},  //İmza dosyasının imzası bozulmuş
                new object[]  {"P4_17.pdf.p7s",   "invalid"},  //“Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"P4_18.pdf.p7s",   "invalid"},  //“Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"P4_19.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_20.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_22.pdf.p7s",   "invalid"},  //Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"P4_23.pdf.p7s",   "invalid"},  //İmzası bozuk sertifika ile imzalanmış
                new object[]  {"P4_24.pdf.p7s",   "invalid"},  //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_25.pdf.p7s",   "valid"},    //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_26.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabının geçerlilik süresi dolmuş
                new object[]  {"P4_27.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"P4_28.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"P4_29.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"P4_30.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_31.pdf.p7s",   "valid"},    //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_34.pdf.p7s",   "invalid"},  //OCSP cevabı içindeki sertifika farklı
                new object[]  {"P4_35.pdf.p7s",   "invalid"},  //Alt kök sertifikasının imzası bozuk
                new object[]  {"P4_37.pdf.p7s",   "invalid"},  //“İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_38.pdf.p7s",   "invalid"},  //“İmza ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_39.pdf.p7s",   "invalid"},  //“İmza ZD” TSA3'den alınmış; ZD sertifikasının geçerlilik süresi dolmuş
                new object[]  {"P4_40.pdf.p7s",   "invalid"},  //“İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"P4_41.pdf.p7s",   "invalid"},  //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_42.pdf.p7s",   "valid"},    //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_43.pdf.p7s",   "invalid"},  //“İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_44.pdf.p7s",   "valid"},    //“İmza ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_45.pdf.p7s",   "invalid"},  //“İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_46.pdf.p7s",   "invalid"},  //“İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_47.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_48.pdf.p7s",   "invalid"},  //“Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_49.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_50.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_51.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_52.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_53.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_54.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_55.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_57.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_59.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                new object[]  {"P4_61.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                new object[]  {"P4_93_s.pdf.p7s", "invalid"},  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya
                new object[]  {"P4_93_p.pdf.p7s", "invalid"}  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli paralel imzalı dosya

                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | P4_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | P4_21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | P4_32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | P4_33.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
                              
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_XLONG_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"P4_1.pdf.p7s",    "valid"},    //Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"P4_4.pdf.p7s",    "invalid"},  //“SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                new object[]  {"P4_5.pdf.p7s",    "invalid"},  //“SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                new object[]  {"P4_6.pdf.p7s",    "valid"},    //“SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                new object[]  {"P4_7.pdf.p7s",    "invalid"},  //“ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                new object[]  {"P4_8.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği eklenmemiş
                new object[]  {"P4_9.pdf.p7s",    "invalid"},  //“SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
                new object[]  {"P4_10.pdf.p7s",   "invalid"},  //NES’e ait OCSP Cevabı yerine SİL bulunan
                new object[]  {"P4_12.pdf.p7s",   "invalid"},  //“İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_13.pdf.p7s",   "invalid"},  //“ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"P4_14.pdf.p7s",   "invalid"},  //“messageDigest” imza özelliği bozulmuş
                new object[]  {"P4_15.pdf.p7s",   "valid"},    //SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"P4_16.pdf.p7s",   "invalid"},  //İmza dosyasının imzası bozulmuş
                new object[]  {"P4_17.pdf.p7s",   "invalid"},  //“Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"P4_18.pdf.p7s",   "invalid"},  //“Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"P4_19.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_20.pdf.p7s",   "invalid"},  //“Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_22.pdf.p7s",   "invalid"},  //Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"P4_23.pdf.p7s",   "invalid"},  //İmzası bozuk sertifika ile imzalanmış
                new object[]  {"P4_24.pdf.p7s",   "invalid"},  //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_25.pdf.p7s",   "valid"},    //OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_26.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabının geçerlilik süresi dolmuş
                new object[]  {"P4_27.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"P4_28.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"P4_29.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"P4_30.pdf.p7s",   "invalid"},  //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_31.pdf.p7s",   "valid"},    //Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_34.pdf.p7s",   "invalid"},  //OCSP cevabı içindeki sertifika farklı
                new object[]  {"P4_35.pdf.p7s",   "invalid"},  //Alt kök sertifikasının imzası bozuk
                new object[]  {"P4_37.pdf.p7s",   "invalid"},  //“İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_38.pdf.p7s",   "invalid"},  //“İmza ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_39.pdf.p7s",   "invalid"},  //“İmza ZD” TSA3'den alınmış; ZD sertifikasının geçerlilik süresi dolmuş
                new object[]  {"P4_40.pdf.p7s",   "invalid"},  //“İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"P4_41.pdf.p7s",   "invalid"},  //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_42.pdf.p7s",   "valid"},    //“İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_43.pdf.p7s",   "invalid"},  //“İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_44.pdf.p7s",   "valid"},    //“İmza ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_45.pdf.p7s",   "invalid"},  //“İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_46.pdf.p7s",   "invalid"},  //“İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_47.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_48.pdf.p7s",   "invalid"},  //“Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_49.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_50.pdf.p7s",   "invalid"},  //“Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_51.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_52.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİLreferans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_53.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_54.pdf.p7s",   "invalid"},  //“İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_55.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_57.pdf.p7s",   "invalid"},  //“Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_59.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                new object[]  {"P4_61.pdf.p7s",   "invalid"},  //“İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                new object[]  {"P4_93_s.pdf.p7s", "invalid"},  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli seri imzalı dosya
                new object[]  {"P4_93_p.pdf.p7s", "invalid"}  //İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası geçerli paralel imzalı dosya

                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi |
                    #     | P4_3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
                    #     | P4_21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
                    #     | P4_32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
                    #     | P4_33.pdf.p7s   | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış | */
                              
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "detached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_A_Attached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"P4_A1.pdf.p7s",    "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"P4_A4.pdf.p7s",    "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                new object[]  {"P4_A5.pdf.p7s",    "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                new object[]  {"P4_A6.pdf.p7s",    "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                new object[]  {"P4_A7.pdf.p7s",    "invalid"}, // “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                new object[]  {"P4_A8.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği eklenmemiş
                new object[]  {"P4_A9.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği ZD tarihinden 3 saat önce eklenmiş
                new object[]  {"P4_A10.pdf.p7s",   "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
                new object[]  {"P4_A12.pdf.p7s",   "invalid"}, // “İmza ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_A13.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"P4_A14.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"P4_A15.pdf.p7s",   "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"P4_A16.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"P4_A17.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"P4_A18.pdf.p7s",   "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"P4_A19.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_A20.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_A22.pdf.p7s",   "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"P4_A23.pdf.p7s",   "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"P4_A24.pdf.p7s",   "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A25.pdf.p7s",   "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A26.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"P4_A27.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"P4_A28.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"P4_A29.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"P4_A30.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A31.pdf.p7s",   "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A34.pdf.p7s",   "invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"P4_A35.pdf.p7s",   "invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"P4_A37.pdf.p7s",   "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_A38.pdf.p7s",   "invalid"}, // “İmza ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_A39.pdf.p7s",   "invalid"}, // “İmza ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"P4_A40.pdf.p7s",   "invalid"}, // “İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"P4_A41.pdf.p7s",   "invalid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A42.pdf.p7s",   "valid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A43.pdf.p7s",   "invalid"}, // “İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_A44.pdf.p7s",   "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_A45.pdf.p7s",   "invalid"}, // “İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_A46.pdf.p7s",   "invalid"}, // “İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_A47.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A48.pdf.p7s",   "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A49.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A50.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A51.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A52.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A53.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A54.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A55.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_A57.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_A59.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                new object[]  {"P4_A61.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                new object[]  {"P4_A75.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_A76.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_A77.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"P4_A78.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"P4_A79.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A80.pdf.p7s",   "valid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A81.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_A82.pdf.p7s",   "valid"}, // “Arşiv ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_A83.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_A84.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_A86.pdf.p7s",   "invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_A90.pdf.p7s",   "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                new object[]  {"P4_A93_s.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
                new object[]  {"P4_A93_p.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
                new object[]  {"P4_A94.pdf.p7s",   "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_A2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | P4_A3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | P4_A21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | P4_A32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | P4_A33.pdf.p7s   | geçerli   | Kullanım kısıtı olan sertifika ile imzalanmış */


                              
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "attached", "testsuite-policy.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_A_Detached_Signature_Validation
        {
            public static Object[] TestCases =
            {
                new object[]  {"P4_A1.pdf.p7s",    "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
                new object[]  {"P4_A4.pdf.p7s",    "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
                new object[]  {"P4_A5.pdf.p7s",    "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
                new object[]  {"P4_A6.pdf.p7s",    "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
                new object[]  {"P4_A7.pdf.p7s",    "invalid"}, // “ESS-Signing-Certificate” SHA-1 olarak belirlenmiş
                new object[]  {"P4_A8.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği eklenmemiş
                new object[]  {"P4_A9.pdf.p7s",    "invalid"}, // “SigningTime” imza özelliği ZD tarihinden 3 saat önce eklenmiş
                new object[]  {"P4_A10.pdf.p7s",   "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
                new object[]  {"P4_A12.pdf.p7s",   "invalid"}, // “İmza ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_A13.pdf.p7s",   "invalid"}, // “ESS-Signing-Certificate” içeriği bozulmuş
                new object[]  {"P4_A14.pdf.p7s",   "invalid"}, // “messageDigest” imza özelliği bozulmuş
                new object[]  {"P4_A15.pdf.p7s",   "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
                new object[]  {"P4_A16.pdf.p7s",   "invalid"}, // İmza dosyasının imzası bozulmuş
                new object[]  {"P4_A17.pdf.p7s",   "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
                new object[]  {"P4_A18.pdf.p7s",   "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
                new object[]  {"P4_A19.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri“ uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_A20.pdf.p7s",   "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
                new object[]  {"P4_A22.pdf.p7s",   "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
                new object[]  {"P4_A23.pdf.p7s",   "invalid"}, // İmzası bozuk sertifika ile imzalanmış
                new object[]  {"P4_A24.pdf.p7s",   "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A25.pdf.p7s",   "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A26.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
                new object[]  {"P4_A27.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
                new object[]  {"P4_A28.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
                new object[]  {"P4_A29.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
                new object[]  {"P4_A30.pdf.p7s",   "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A31.pdf.p7s",   "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A34.pdf.p7s",   "invalid"}, // OCSP cevabı içindeki sertifika farklı
                new object[]  {"P4_A35.pdf.p7s",   "invalid"}, // Alt kök sertifikasının imzası bozuk
                new object[]  {"P4_A37.pdf.p7s",   "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_A38.pdf.p7s",   "invalid"}, // “İmza ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_A39.pdf.p7s",   "invalid"}, // “İmza ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"P4_A40.pdf.p7s",   "invalid"}, // “İmza ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHSimzası bozuk
                new object[]  {"P4_A41.pdf.p7s",   "invalid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A42.pdf.p7s",   "valid"}, // “İmza ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A43.pdf.p7s",   "invalid"}, // “İmza ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_A44.pdf.p7s",   "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_A45.pdf.p7s",   "invalid"}, // “İmza ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_A46.pdf.p7s",   "invalid"}, // “İmza ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_A47.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A48.pdf.p7s",   "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A49.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A50.pdf.p7s",   "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A51.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A52.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A53.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
                new object[]  {"P4_A54.pdf.p7s",   "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
                new object[]  {"P4_A55.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_A57.pdf.p7s",   "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
                new object[]  {"P4_A59.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
                new object[]  {"P4_A61.pdf.p7s",   "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
                new object[]  {"P4_A75.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
                new object[]  {"P4_A76.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA2'den alınmış ZD imzası bozuk
                new object[]  {"P4_A77.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA3'den alınmış; ZD sertifikasının validlik süresi dolmuş
                new object[]  {"P4_A78.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA4'den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
                new object[]  {"P4_A79.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
                new object[]  {"P4_A80.pdf.p7s",   "valid"}, // “Arşiv ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
                new object[]  {"P4_A81.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
                new object[]  {"P4_A82.pdf.p7s",   "valid"}, // “Arşiv ZD” TSC1'den alınmış; Geçerli
                new object[]  {"P4_A83.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
                new object[]  {"P4_A84.pdf.p7s",   "invalid"}, // “Arşiv ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
                new object[]  {"P4_A86.pdf.p7s",   "invalid"}, // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş
                new object[]  {"P4_A90.pdf.p7s",   "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
                new object[]  {"P4_A93_s.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
                new object[]  {"P4_A93_p.pdf.p7s", "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
                new object[]  {"P4_A94.pdf.p7s",   "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
                    /*     API kapsamının dışındaki senaryolar:
                    #     | P4_A2.doc.p7s    | geçersiz  | İmzalanmış makro içeren word belgesi                                                                                                |
                    #     | P4_A3.pdf.p7s    | geçersiz  | “ContentHints” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya                                     |
                    #     | P4_A21.pdf.p7s   | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış                                                                                            |
                    #     | P4_A32.pdf.p7s   | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış                                                                                 |
                    #     | P4_A33.pdf.p7s   | geçerli   | Kullanım kısıtı olan sertifika ile imzalanmış */
                          
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "detached", "testsuite-policy.xml");
            }
        }


        [TestFixture]
        public class CAdES_ESA_Attached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[]   {"ESA_94.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
                new object[]   {"ESA_96.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                new object[]   {"ESA_100.pdf.p7s"  ,"invalid"} // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            };


            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult) 
            {
                ValidateCAdESSignature(pfx, "esa", expectedResult, "attached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_ESA_Detached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[]  {"ESA_94.pdf.p7s"   ,"invalid"}, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
                new object[]  {"ESA_96.pdf.p7s"   ,"invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                new object[]  {"ESA_100.pdf.p7s"  ,"invalid"} // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "esa", expectedResult, "detached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_A_Attached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[]  {"P4_A11.pdf.p7s"   ,"invalid"},  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
                new object[]  {"P4_A85.pdf.p7s"   ,"invalid"}   // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş

            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "attached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_A_Detached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
               new object[] {"P4_A11.pdf.p7s"   ,"invalid"},  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
               new object[] {"P4_A85.pdf.p7s"   ,"invalid"}   // Üzerinde 2 adet Arşiv ZD bulunan dosya. İlk arşiv ZD TSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş

            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "detached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_XLONG_Attached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
              new object[]  {"P4_11.pdf.p7s"   ,"invalid"}  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş

            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "attached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_XLONG_Detached_Signature_Validation_With_Missing_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] { "P4_11.pdf.p7s"   ,"invalid"}  // “İmza ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "detached", "testsuite-policy-unavailable-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_BES_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
               new object[] {"BES_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "bes", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_BES_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"BES_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "bes", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_EST_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"EST_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "est", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }


        [TestFixture]
        public class CAdES_EST_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"EST_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "est", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_XLONG_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"ESXL_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "xlong", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }


        [TestFixture]
        public class CAdES_XLONG_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"ESXL_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "xlong", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_ESA_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"ESA_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "esa", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }


        [TestFixture]
        public class CAdES_ESA_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"ESA_38.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "esa", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"P4_36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }


        [TestFixture]
        public class CAdES_P4_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"P4_36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        [TestFixture]
        public class CAdES_P4_A_Attached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"P4_A36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "attached", "testsuite-policy-invalid-root.xml");
            }
        }


        [TestFixture]
        public class CAdES_P4_A_Detached_Signature_Validation_With_Invalid_Root_Certificate
        {

            public static Object[] TestCases =
            {
                new object[] {"P4_A36.pdf.p7s"   ,"invalid"}  // Kök sertifikasının imzası bozuk
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String expectedResult)
            {
                ValidateCAdESSignature(pfx, "p4_a", expectedResult, "detached", "testsuite-policy-invalid-root.xml");
            }
        }

        public static void ValidateCAdESSignature(String file, String type, String expectedResult, String contentMode, String policyFile) 
        {

            FileStream fileStream = new FileStream(rootFolder + @"config/policy/" + policyFile, FileMode.Open);

            validationPolicy = PolicyReader.readValidationPolicy(fileStream);
            ReadBaseSignedData(type, file, contentMode);
            Assert.IsNotNull(contentInfo);

            fileStream.Close();

            parameters[EParameters.P_IGNORE_GRACE] = true;
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = validationPolicy;
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;
            parameters[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = 2000;
            //parameters[EParameters.P_TRUST_SIGNINGTIMEATTR] = true;

            SignedDataValidation sdv = new SignedDataValidation();
            validationResult = sdv.verify(contentInfo.getEncoded(), parameters);
            Assert.IsNotNull(validationResult);
            Console.WriteLine(validationResult);
            //validationResult.getSDValidationResults().get(0).getCheckerResults()
   
            SignedData_Status expectedStatus;
            if(expectedResult.Equals("valid"))
                expectedStatus = SignedData_Status.ALL_VALID;
            else
                expectedStatus = SignedData_Status.NOT_ALL_VALID;

            SignedData_Status status = validationResult.getSDStatus();
            Assert.AreEqual(expectedStatus, status, validationResult.ToString());

            parameters.Clear();
    }

        private static void ReadBaseSignedData(string type, string file, string contentMode)
        {
            string folder = @"data/validation-cades-" + type + @"/" + contentMode + @"/";
            byte[] bytes = AsnIO.dosyadanOKU(rootFolder + folder + file);

            if (contentMode.Equals("detached"))
            {
                string externalDocumentName = GetExternalDocumentName(file);
                parameters[EParameters.P_EXTERNAL_CONTENT] = new SignableFile(new FileInfo(rootFolder + folder + externalDocumentName));
            }

            contentInfo = new BaseSignedData(bytes);
        }

        private static string GetExternalDocumentName(string file)
        {
            string documentName;

            if (file.Equals("BES_2.doc.p7s")
                    || file.Equals("BES_13.pdf.p7s")
                    || file.Equals("ESA_2.doc.p7s")
                    || file.Equals("ESA_13.pdf.p7s")
                    || file.Equals("P4_2.doc.p7s")
                    || file.Equals("P4_21.pdf.p7s")
                    || file.Equals("P4_A2.doc.p7s")
                    || file.Equals("P4_A21.pdf.p7s")
                    || file.Equals("ESXL_2.doc.p7s")
                    || file.Equals("ESXL_13.pdf.p7s"))
            {
                documentName = file.Replace(".p7s", "");
            }
            else
            {
                documentName = "Ortak.pdf";
            }
            return documentName;
        }
    }
}

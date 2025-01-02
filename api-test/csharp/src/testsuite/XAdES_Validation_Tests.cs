//using System;
//using System.IO;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
//using NUnit.Framework;

//namespace tr.gov.tubitak.uekae.esya.api.testsuite
//{
//    [TestFixture]
//    class XAdES_Validation_Tests
//    {

//        static string rootFolder = TestSuiteCommonMethods.FOLDER;
//        static ValidationResult validationResult;
//        static XMLSignature signature;


//        [TestFixture]
//        public class XAdES_BES_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                new object[]  {"BES_1.xml",        "valid" },  // Geçerli imza (Tüm imzalı özellikler eklenmiş)
//                new object[]  {"BES_4.xml",        "invalid" },  // “SigningCertificate” içeriği bozulmuş
//                new object[]  {"BES_5.xml",        "invalid" },  // “Reference/DigestValue” imza özelliği bozulmuş
//                new object[]  {"BES_6.xml",        "valid" },  // SHA-1 algoritması kullanılarak imzalanmış
//                new object[]  {"BES_7.xml",        "invalid" },  // İmza dosyasının imzası bozulmuş
//                new object[]  {"BES_8.xml",        "valid" },  // Geçerli İmza
//                new object[]  {"BES_9.xml",        "invalid" },  // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                new object[]  {"BES_10.xml",       "invalid" },  // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                new object[]  {"BES_11.xml",       "invalid" },  // “Nitelikli Sertifika İbareleri” uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
//                new object[]  {"BES_12.xml",       "invalid" },  // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                new object[]  {"BES_14.xml",       "invalid" },  // Süresi dolmuş sertifika ile imzalanmış
//                new object[]  {"BES_15.xml",       "invalid" },  // İmzası bozuk sertifika ile imzalanmış
//                new object[]  {"BES_16_1.xml",     "invalid" },  // SİL’de iptal olmuş sertifika ile imzalanmış
//                new object[]  {"BES_16_2.xml",     "invalid" },  // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika beyan edilen imza tarihinden sonra iptal edilmiş
//                new object[]  {"BES_17_1.xml",     "invalid" },  // OCSP’de iptal olmuş sertifika ile imzalanmış
//                new object[]  {"BES_18.xml",       "invalid" },  // Kontrol edildiği SİL’in validlik süresi dolmuş
//                new object[]  {"BES_19.xml",       "invalid" },  // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
//                new object[]  {"BES_20.xml",       "invalid" },  // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                new object[]  {"BES_21.xml",       "invalid" },  // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                new object[]  {"BES_22.xml",       "invalid" },  // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                new object[]  {"BES_23.xml",       "invalid" },  // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                new object[]  {"BES_24_1.xml",     "valid" },  // Kontrol edildiği OCSP sertifikası iptal olmuş
//                new object[]  {"BES_27.xml",       "invalid" },  // OCSP cevabı içindeki sertifika farklı
//                new object[]  {"BES_28.xml",       "invalid" },  // Alt kök sertifikasının imzası bozuk
//                new object[]  {"BES_29_1.xml",     "invalid" },  // Alt kök sertifikası SİL’de iptal olmuş
//                new object[]  {"BES_30.xml",       "invalid" },  // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
//                new object[]  {"BES_31.xml",       "invalid" },  // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
//                new object[]  {"BES_32_1.xml",     "invalid" },  // Alt kök sertifikası OCSP’de iptal olmuş
//                new object[]  {"BES_33.xml",       "invalid" },  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
//                new object[]  {"BES_34.xml",       "invalid" },  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
//                new object[]  {"BES_35.xml",       "invalid" },  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
//                new object[]  {"BES_36.xml",       "invalid" },  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
//                new object[]  {"BES_37_1.xml",     "valid" },  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş
//                new object[]  {"BES_39_s.xml",     "invalid" },  // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
//                new object[]  {"BES_39_p.xml",     "invalid" }  // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya

//                    /*#   API kapsamının dışındaki senaryolar:
//                    #   | BES_2.xml        | geçersiz  | İmzalanmış makro içeren word belgesi |
//                    #   | BES_3.xml        | geçersiz  | “DataObjectFormat/MimeType” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                    #   | BES_13.xml       | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
//                    #   | BES_25.xml       | geçersiz  | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                    #   | BES_26.xml       | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult) 
//            {
//                validateXAdESSignature(pfx, "bes", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_EST_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                    new object[]  {"EST_1.xml",      "valid" }, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
//                    new object[]  {"EST_4.xml",      "invalid" }, // “SigningCertificate” içeriği bozulmuş
//                    new object[]  {"EST_5.xml",      "invalid" }, // “Reference/DigestValue” imza özelliği bozulmuş
//                    new object[]  {"EST_6.xml",      "valid" }, // SHA-1 algoritması kullanılarak imzalanmış
//                    new object[]  {"EST_7.xml",      "invalid" }, // İmza dosyasının imzası bozulmuş
//                    new object[]  {"EST_8.xml",      "valid" }, // Geçerli imza
//                    new object[]  {"EST_9.xml",      "invalid" }, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                    new object[]  {"EST_10.xml",     "invalid" }, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                    new object[]  {"EST_11.xml",     "invalid" }, // “Nitelikli Sertifika İbareleri” uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
//                    new object[]  {"EST_12.xml",     "invalid" }, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                    new object[]  {"EST_14.xml",     "invalid" }, // Süresi dolmuş sertifika ile imzalanmış
//                    new object[]  {"EST_15.xml",     "invalid" }, // İmzası bozuk sertifika ile imzalanmış
//                    new object[]  {"EST_16_1.xml",   "invalid" }, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_16_2.xml",   "valid" }, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_17_1.xml",   "invalid" }, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_17_2.xml",   "valid" }, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_18.xml",     "invalid" }, // Kontrol edildiği SİL’in validlik süresi dolmuş
//                    new object[]  {"EST_19.xml",     "invalid" }, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
//                    new object[]  {"EST_20.xml",     "invalid" }, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                    new object[]  {"EST_21.xml",     "invalid" }, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                    new object[]  {"EST_22.xml",     "invalid" }, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                    new object[]  {"EST_23.xml",     "invalid" }, // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                    new object[]  {"EST_24_1.xml",   "valid" }, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_24_2.xml",   "valid" }, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_27.xml",     "invalid" }, // OCSP cevabı içindeki sertifika farklı
//                    new object[]  {"EST_28.xml",     "invalid" }, // Alt kök sertifikasının imzası bozuk
//                    new object[]  {"EST_29_1.xml",   "invalid" }, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_29_2.xml",   "valid" }, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_30.xml",     "invalid" }, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
//                    new object[]  {"EST_31.xml",     "invalid" }, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
//                    new object[]  {"EST_32_1.xml",   "invalid" }, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_32_2.xml",   "valid" }, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_33.xml",     "invalid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
//                    new object[]  {"EST_34.xml",     "invalid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
//                    new object[]  {"EST_35.xml",     "invalid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
//                    new object[]  {"EST_36.xml",     "invalid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
//                    new object[]  {"EST_37_1.xml",   "valid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_37_2.xml",   "valid" }, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_39_s.xml",   "invalid" }, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
//                    new object[]  {"EST_39_p.xml",   "invalid" }, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
//                    new object[]  {"EST_40.xml",     "invalid" }, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                    new object[]  {"EST_41.xml",     "invalid" }, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
//                    new object[]  {"EST_42.xml",     "invalid" }, // “İmza ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
//                    new object[]  {"EST_43.xml",     "invalid" }, // “İmza ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
//                    new object[]  {"EST_44.xml",     "invalid" }, // “İmza ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                    new object[]  {"EST_45.xml",     "valid" }, // “İmza ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                    new object[]  {"EST_46.xml",     "invalid" }, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                    new object[]  {"EST_47.xml",     "valid" }, // “İmza ZD” TSC1’den alınmış; Geçerli
//                    new object[]  {"EST_48.xml",     "invalid" }, // “İmza ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
//                    new object[]  {"EST_49.xml",     "invalid" } // “İmza ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
                          
//                    /*#   API kapsamının dışındaki senaryolar:
//                        #   | EST_2.xml        | geçersiz  | İmzalanmış makro içeren word belgesi |
//                        #   | EST_3.xml        | geçersiz  | “DataObjectFormat/MimeType” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                        #   | EST_13.xml       | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
//                        #   | EST_25.xml       | geçersiz  | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                        #   | EST_26.xml       | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "est", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_ESXL1_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                new object[]  {"ESXL1_1.xml",       "valid"},  // Geçerli imza (Tüm imzalı özellikler eklenmiş
//                new object[]  {"ESXL1_4.xml",       "invalid"},  // “SigningCertificate” içeriği bozulmuş
//                new object[]  {"ESXL1_5.xml",       "invalid"},  // “Reference/DigestValue” imza özelliği bozulmuş
//                new object[]  {"ESXL1_6.xml",       "valid"},  // SHA-1 algoritması kullanılarak imzalanmış
//                new object[]  {"ESXL1_7.xml",       "invalid"},  // İmza dosyasının imzası bozulmuş
//                new object[]  {"ESXL1_8.xml",       "valid"},  // Geçerli İmza
//                new object[]  {"ESXL1_9.xml",       "invalid"},  // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                new object[]  {"ESXL1_10.xml",      "invalid"},  // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                new object[]  {"ESXL1_11.xml",      "invalid"},  // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
//                new object[]  {"ESXL1_12.xml",      "invalid"},  // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTKtarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                new object[]  {"ESXL1_14.xml",      "invalid"},  // Süresi dolmuş sertifika ile imzalanmış
//                new object[]  {"ESXL1_15.xml",      "invalid"},  // İmzası bozuk sertifika ile imzalanmış
//                new object[]  {"ESXL1_16_1.xml",    "invalid"},  // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_16_2.xml",    "valid"},  // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_17_1.xml",    "invalid"},  // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_17_2.xml",    "valid"},  // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_18.xml",      "invalid"},  // Kontrol edildiği SİL’in validlik süresi dolmuş
//                new object[]  {"ESXL1_19.xml",      "invalid"},  // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
//                new object[]  {"ESXL1_20.xml",      "invalid"},  // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                new object[]  {"ESXL1_21.xml",      "invalid"},  // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                new object[]  {"ESXL1_22.xml",      "invalid"},  // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                new object[]  {"ESXL1_23.xml",      "invalid"},  // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                new object[]  {"ESXL1_24_1.xml",    "invalid"},  // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_24_2.xml",    "valid"},  // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_27.xml",      "invalid"},  // OCSP cevabı içindeki sertifika farklı
//                new object[]  {"ESXL1_28.xml",      "invalid"},  // Alt kök sertifikasının imzası bozuk
//                new object[]  {"ESXL1_29_1.xml",    "invalid"},  // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_29_2.xml",    "valid"},  // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_30.xml",      "invalid"},  // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
//                new object[]  {"ESXL1_31.xml",      "invalid"},  // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
//                new object[]  {"ESXL1_32_1.xml",    "invalid"},  // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_32_2.xml",    "valid"},  // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_33.xml",      "invalid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
//                new object[]  {"ESXL1_34.xml",      "invalid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
//                new object[]  {"ESXL1_35.xml",      "invalid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
//                new object[]  {"ESXL1_36.xml",      "invalid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
//                new object[]  {"ESXL1_37_1.xml",    "invalid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_37_2.xml",    "valid"},  // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_39_s.xml",    "invalid"},  // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
//                new object[]  {"ESXL1_39_p.xml",    "invalid"},  // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
//                new object[]  {"ESXL1_40.xml",      "invalid"},  // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                new object[]  {"ESXL1_41.xml",      "invalid"},  // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
//                new object[]  {"ESXL1_42.xml",      "invalid"},  // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
//                new object[]  {"ESXL1_43.xml",      "invalid"},  // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                new object[]  {"ESXL1_44.xml",      "invalid"},  // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_45.xml",      "valid"},  // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_46.xml",      "invalid"},  // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                new object[]  {"ESXL1_47.xml",      "valid"},  // “İmza ZD” TSC1’den alınmış; Geçerli
//                new object[]  {"ESXL1_48.xml",      "invalid"},  // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
//                new object[]  {"ESXL1_49.xml",      "invalid"},  // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
//                new object[]  {"ESXL1_50.xml",      "invalid"},  // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_51.xml",      "invalid"},  // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_52.xml",      "invalid"},  // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_53.xml",      "invalid"},  // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_54.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_55.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_56.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_57.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_58.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_59.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_60.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
//                new object[]  {"ESXL1_61.xml",      "invalid"},  // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
//                new object[]  {"ESXL1_62_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                new object[]  {"ESXL1_63_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
//                new object[]  {"ESXL1_64_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
//                new object[]  {"ESXL1_65_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                new object[]  {"ESXL1_66_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                new object[]  {"ESXL1_67_1.xml",    "valid"},  // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                new object[]  {"ESXL1_68_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
//                new object[]  {"ESXL1_69_1.xml",    "valid"},  // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
//                new object[]  {"ESXL1_70_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
//                new object[]  {"ESXL1_71_1.xml",    "invalid"},  // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
//                new object[]  {"ESXL1_72.xml",      "invalid"},  // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
//                new object[]  {"ESXL1_74.xml",      "invalid"},  // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
//                new object[]  {"ESXL1_76.xml",      "invalid"},  // “İptal Verisi Değerleri “ içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
//                new object[]  {"ESXL1_78.xml",      "invalid"},  // “İptal Verisi Değerleri” içeriğinde NES SİL‘inin olmadığı imza dosyası
//                new object[]  {"ESXL1_80.xml",      "invalid"},  // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
//                new object[]  {"ESXL1_82.xml",      "invalid"},  // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
//                new object[]  {"ESXL1_95.xml",      "invalid"}  // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
                 
                  
//                  /*#   API kapsamının dışındaki senaryolar:
//                    #   | ESXL1_2.xml       | geçersiz  | İmzalanmış makro içeren word belgesi |
//                    #   | ESXL1_3.xml       | geçersiz  | “DataObjectFormat/MimeType” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                    #   | ESXL1_13.xml      | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
//                    #   | ESXL1_25.xml      | geçersiz  | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                    #   | ESXL1_26.xml      | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esxl1", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_ESA_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                  new object[]  {"ESA_1.xml",       "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
//                  new object[]  {"ESA_4.xml",       "invalid"}, // “SigningCertificate” içeriği bozulmuş
//                  new object[]  {"ESA_5.xml",       "invalid"}, // “Reference/DigestValue” imza özelliği bozulmuş
//                  new object[]  {"ESA_6.xml",       "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
//                  new object[]  {"ESA_7.xml",       "invalid"}, // İmza dosyasının imzası bozulmuş
//                  new object[]  {"ESA_8.xml",       "valid"}, // Geçerli imza
//                  new object[]  {"ESA_9.xml",       "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “İnkâr Edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                  new object[]  {"ESA_10.xml",      "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                  new object[]  {"ESA_11.xml",      "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” uzantısının içeriğinde ETSI ibaresine ait OID bulunmayan sertifika ile imzalanmış
//                  new object[]  {"ESA_12.xml",      "invalid"}, // “Nitelikli Elektronik Sertifika İbareleri” içeriğinde BTKtarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                  new object[]  {"ESA_14.xml",      "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
//                  new object[]  {"ESA_15.xml",      "invalid"}, // İmzası bozuk sertifika ile imzalanmış
//                  new object[]  {"ESA_16_1.xml",    "invalid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_16_2.xml",    "valid"}, // SİL’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_17_1.xml",    "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_17_2.xml",    "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_18.xml",      "invalid"}, // Kontrol edildiği SİL’in validlik süresi dolmuş
//                  new object[]  {"ESA_19.xml",      "invalid"}, // Kontrol edildiği SİL’in üzerindeki ESHS imzası bozuk
//                  new object[]  {"ESA_20.xml",      "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                  new object[]  {"ESA_21.xml",      "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                  new object[]  {"ESA_22.xml",      "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                  new object[]  {"ESA_23.xml",      "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                  new object[]  {"ESA_24_1.xml",    "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_24_2.xml",    "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_27.xml",      "invalid"}, // OCSP cevabı içindeki sertifika farklı
//                  new object[]  {"ESA_28.xml",      "invalid"}, // Alt kök sertifikasının imzası bozuk
//                  new object[]  {"ESA_29_1.xml",    "invalid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_29_2.xml",    "valid"}, // Alt kök sertifikası SİL’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_30.xml",      "invalid"}, // Alt kök sertifikası süresi dolmuş SİL’e referans veriyor
//                  new object[]  {"ESA_31.xml",      "invalid"}, // Alt kök sertifikası imzası bozuk SİL’e referans veriyor
//                  new object[]  {"ESA_32_1.xml",    "invalid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_32_2.xml",    "valid"}, // Alt kök sertifikası OCSP’de iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_33.xml",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının süresi dolmuş
//                  new object[]  {"ESA_34.xml",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP cevabının imzası bozuk
//                  new object[]  {"ESA_35.xml",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının validlik süresi dolmuş
//                  new object[]  {"ESA_36.xml",      "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikasının imzası bozuk
//                  new object[]  {"ESA_37_1.xml",    "invalid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_37_2.xml",    "valid"}, // Alt kök sertifikasının iptal kontrolünün yapıldığı OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_39_s.xml",    "invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid seri imzalı dosya
//                  new object[]  {"ESA_39_p.xml",    "invalid"}, // İlk imzacısının alt kökü iptal olmuş, ikinci imzacısı valid paralel imzalı dosya
//                  new object[]  {"ESA_40.xml",      "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"ESA_41.xml",      "invalid"}, // “İmza ZD” TSA2’den alınmış ZDimzası bozuk
//                  new object[]  {"ESA_42.xml",      "invalid"}, // “İmza ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
//                  new object[]  {"ESA_43.xml",      "invalid"}, // “İmza ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                  new object[]  {"ESA_44.xml",      "invalid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_45.xml",      "valid"}, // “İmza ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_46.xml",      "invalid"}, // “İmza ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"ESA_47.xml",      "valid"}, // “İmza ZD” TSC1’den alınmış; Geçerli
//                  new object[]  {"ESA_48.xml",      "invalid"}, // “İmza ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
//                  new object[]  {"ESA_49.xml",      "invalid"}, // “İmza ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
//                  new object[]  {"ESA_50.xml",      "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_51.xml",      "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_52.xml",      "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_53.xml",      "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_54.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_55.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_56.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_57.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde NES SİL referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_58.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_59.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının OCSP referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_60.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"ESA_61.xml",      "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"ESA_62_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"ESA_63_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSA2’den alınmış ZD imzası bozuk
//                  new object[]  {"ESA_64_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSA3’den alınmış; ZD sertifikasının validlik süresi dolmuş
//                  new object[]  {"ESA_65_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSA4’den alınmış; ZD sertifikasının üzerindeki ESHS imzası bozuk
//                  new object[]  {"ESA_66_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_67_1.xml",    "valid"}, // “İmza ve Referans ZD” TSA5’den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_68_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSB’den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"ESA_69_1.xml",    "valid"}, // “İmza ve Referans ZD” TSC1’den alınmış; Geçerli
//                  new object[]  {"ESA_70_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSC2’den alınmış; ZD sertifikası süresi dolmuş SİL’e referans veriyor
//                  new object[]  {"ESA_71_1.xml",    "invalid"}, // “İmza ve Referans ZD” TSC3’den alınmış; ZD sertifikası imzası bozuk SİL’e referans veriyor
//                  new object[]  {"ESA_72.xml",      "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
//                  new object[]  {"ESA_74.xml",      "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
//                  new object[]  {"ESA_76.xml",      "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL’inin olmadığı imza dosyası
//                  new object[]  {"ESA_78.xml",      "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES SİL’inin olmadığı imza dosyası
//                  new object[]  {"ESA_80.xml",      "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının OCSP’sinin olmadığı imza dosyası
//                  new object[]  {"ESA_82.xml",      "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP’sinin olmadığı imza dosyası
//                  new object[]  {"ESA_84.xml",      "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"ESA_85.xml",      "invalid"}, // “Arşiv ZD” TSA2’den alınmış ZDimzası bozuk
//                  new object[]  {"ESA_86.xml",      "invalid"}, // “Arşiv ZD” TSA3’den alınmış; ZDsertifikasının validlik süresi dolmuş
//                  new object[]  {"ESA_87.xml",      "invalid"}, // “Arşiv ZD” TSA4’den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                  new object[]  {"ESA_88.xml",      "invalid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"ESA_89.xml",      "valid"}, // “Arşiv ZD” TSA5’den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"ESA_90.xml",      "invalid"}, // “Arşiv ZD” TSB’den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"ESA_91.xml",      "valid"}, // “Arşiv ZD” TSC1’den alınmış; Geçerli
//                  new object[]  {"ESA_92.xml",      "invalid"}, // “Arşiv ZD” TSC2’den alınmış; ZDsertifikası süresi dolmuş SİL’e referans veriyor
//                  new object[]  {"ESA_93.xml",      "invalid"}, // “Arşiv ZD” TSC3’den alınmış; ZDsertifikası imzası bozuk SİL’e referans veriyor
//                  new object[]  {"ESA_95.xml",      "invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
//                  new object[]  {"ESA_97.xml",      "invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
//                  new object[]  {"ESA_101.xml",     "invalid"}, // Üzerinde 2 adet Arşiv ZDbulunan dosya. İlk arşiv ZDTSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
//                  new object[]  {"ESA_106.xml",     "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
//                  new object[]  {"ESA_109.xml",     "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
               
//                  /*#     API kapsamının dışındaki senaryolar:
//                    #     | ESA_2.xml       | geçersiz    | İmzalanmış makro içeren word belgesi |
//                    #     | ESA_3.xml       | geçersiz    | “DataObjectFormat/MimeType imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                    #     | ESA_13.xml      | geçersiz    | Eki word olan PDF/A-3 belgesi imzalanmış |
//                    #     | ESA_25.xml      | geçersiz    | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                    #     | ESA_26.xml      | geçersiz    | Kullanım kısıtı olan sertifika ile imzalanmış |*/
    
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esa", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_P4_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                   new object[]  {"P4_1.pdf.xml",      "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
//                   new object[]  {"P4_4.pdf.xml",      "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
//                   new object[]  {"P4_5.pdf.xml",      "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
//                   new object[]  {"P4_6.pdf.xml",      "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
//                   new object[]  {"P4_7.pdf.xml",      "invalid"}, // “SigningCertificate” SHA-1 olarak belirlenmiş
//                   new object[]  {"P4_8.pdf.xml",      "invalid"}, // “SigningTime” imza özelliği eklenmemiş
//                   new object[]  {"P4_9.pdf.xml",      "invalid"}, // “SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
//                   new object[]  {"P4_10.pdf.xml",     "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
//                   new object[]  {"P4_12.pdf.xml",     "invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
//                   new object[]  {"P4_13.pdf.xml",     "invalid"}, // “SigningCertificate” içeriği bozulmuş
//                   new object[]  {"P4_14.pdf.xml",     "invalid"}, // “Reference/DigestValue” imza özelliği bozulmuş
//                   new object[]  {"P4_15.pdf.xml",     "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
//                   new object[]  {"P4_16.pdf.xml",     "invalid"}, // İmza dosyasının imzası bozulmuş
//                   new object[]  {"P4_17.pdf.xml",     "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                   new object[]  {"P4_18.pdf.xml",     "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                   new object[]  {"P4_19.pdf.xml",     "invalid"}, // “Nitelikli Sertifika İbareleri“ uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
//                   new object[]  {"P4_20.pdf.xml",     "invalid"}, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                   new object[]  {"P4_22.pdf.xml",     "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
//                   new object[]  {"P4_23.pdf.xml",     "invalid"}, // İmzası bozuk sertifika ile imzalanmış
//                   new object[]  {"P4_24.pdf.xml",     "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                   new object[]  {"P4_25.pdf.xml",     "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                   new object[]  {"P4_26.pdf.xml",     "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                   new object[]  {"P4_27.pdf.xml",     "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                   new object[]  {"P4_28.pdf.xml",     "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                   new object[]  {"P4_29.pdf.xml",     "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                   new object[]  {"P4_30.pdf.xml",     "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                   new object[]  {"P4_31.pdf.xml",     "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                   new object[]  {"P4_34.pdf.xml",     "invalid"}, // OCSP cevabı içindeki sertifika farklı
//                   new object[]  {"P4_35.pdf.xml",     "invalid"}, // Alt kök sertifikasının imzası bozuk
//                   new object[]  {"P4_37.pdf.xml",     "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                   new object[]  {"P4_38.pdf.xml",     "invalid"}, // “İmza ZD” TSA2'den alınmış ZDimzası bozuk
//                   new object[]  {"P4_39.pdf.xml",     "invalid"}, // “İmza ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
//                   new object[]  {"P4_40.pdf.xml",     "invalid"}, // “İmza ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                   new object[]  {"P4_41.pdf.xml",     "invalid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                   new object[]  {"P4_42.pdf.xml",     "valid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                   new object[]  {"P4_43.pdf.xml",     "invalid"}, // “İmza ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                   new object[]  {"P4_44.pdf.xml",     "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
//                   new object[]  {"P4_45.pdf.xml",     "invalid"}, // “İmza ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
//                   new object[]  {"P4_46.pdf.xml",     "invalid"}, // “İmza ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor
//                   new object[]  {"P4_47.pdf.xml",     "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                   new object[]  {"P4_48.pdf.xml",     "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                   new object[]  {"P4_49.pdf.xml",     "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                   new object[]  {"P4_50.pdf.xml",     "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                   new object[]  {"P4_51.pdf.xml",     "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
//                   new object[]  {"P4_52.pdf.xml",     "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
//                   new object[]  {"P4_53.pdf.xml",     "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
//                   new object[]  {"P4_54.pdf.xml",     "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
//                   new object[]  {"P4_55.pdf.xml",     "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
//                   new object[]  {"P4_57.pdf.xml",     "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
//                   new object[]  {"P4_59.pdf.xml",     "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
//                   new object[]  {"P4_61.pdf.xml",     "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
//                   new object[]  {"P4_63.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSA1'den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                   new object[]  {"P4_64.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSA2'den alınmış ZD imzası bozuk
//                   new object[]  {"P4_65.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
//                   new object[]  {"P4_66.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                   new object[]  {"P4_67.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                   new object[]  {"P4_68.pdf.xml",     "valid"}, // “İmza ve Referans ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                   new object[]  {"P4_69.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
//                   new object[]  {"P4_70.pdf.xml",     "valid"}, // “İmza ve Referans ZD” TSC1'den alınmış; Geçerli
//                   new object[]  {"P4_71.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
//                   new object[]  {"P4_72.pdf.xml",     "invalid"}, // “İmza ve Referans ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
//                   new object[]  {"P4_93_s.pdf.xml",   "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
//                   new object[]  {"P4_93_p.pdf.xml",   "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
                  
               
//                  /*#     Senaryo ile uyumsuz dosyalar
//                    #     Yoksayma sebebi: İlgili imzalı dosya KamuSM eit-wiki'de hatalı halede bulunmaktadır. (eit-wiki güncellemesi bekleniyor)
//                    #     | P4_74.pdf.xml     | geçersiz  | “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş |
//                    #     API kapsamının dışındaki senaryolar:
//                    #     | P4_2.doc.xml      | geçersiz  | İmzalanmış makro içeren word belgesi |
//                    #     | P4_3.pdf.xml      | geçersiz  | “DataObjectFormat/MimeType” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                    #     | P4_21.pdf.xml     | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
//                    #     | P4_32.pdf.xml     | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                    #     | P4_33.pdf.xml     | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
        
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_P4_A_Enveloping_Signature_Validation
//        {
//            public static Object[] TestCases =
//            {
//                  new object[]  {"P4_A1.pdf.xml",     "valid"}, // Geçerli imza (Tüm imzalı özellikler eklenmiş)
//                  new object[]  {"P4_A4.pdf.xml",     "invalid"}, // “SigPolicyId” içeriği P4’e ait OID (2.16.792.1.61.0.1.5070.3.3.1) dışında başka bir OID içeriyor
//                  new object[]  {"P4_A5.pdf.xml",     "invalid"}, // “SigPolicyHash” içeriği P4’e ait hash değeri dışında başka bir değer içeriyor
//                  new object[]  {"P4_A6.pdf.xml",     "valid"}, // “SPUserNotice” içeriği P4’e ait bilgilendirme içeriyor
//                  new object[]  {"P4_A7.pdf.xml",     "invalid"}, // “SigningCertificate” SHA-1 olarak belirlenmiş
//                  new object[]  {"P4_A8.pdf.xml",     "invalid"}, // “SigningTime” imza özelliği eklenmemiş
//                  new object[]  {"P4_A9.pdf.xml",     "invalid"}, // “SigningTime” imza özelliği ZDtarihinden 3 saat önce eklenmiş
//                  new object[]  {"P4_A10.pdf.xml",    "invalid"}, // NES’e ait OCSP Cevabı yerine SİL bulunan
//                  new object[]  {"P4_A12.pdf.xml",    "invalid"}, // “İmza ZD” TSC1’den alınmış; ZDSİL’i imza dosyasına eklenmemiş
//                  new object[]  {"P4_A14.pdf.xml",    "invalid"}, // “Reference/DigestValue” imza özelliği bozulmuş
//                  new object[]  {"P4_A15.pdf.xml",    "valid"}, // SHA-1 algoritması kullanılarak imzalanmış
//                  new object[]  {"P4_A16.pdf.xml",    "invalid"}, // İmza dosyasının imzası bozulmuş
//                  new object[]  {"P4_A17.pdf.xml",    "invalid"}, // “Anahtar Kullanım (keyusage)” uzantısı içeriğinde “inkâr edilemezlik” özelliği olmayan sertifika ile imzalanmış
//                  new object[]  {"P4_A18.pdf.xml",    "invalid"}, // “Sertifika İlkeleri” uzantısının içeriğinde ibare olmayan sertifika ile imzalanmış
//                  new object[]  {"P4_A19.pdf.xml",    "invalid"}, // “Nitelikli Sertifika İbareleri“ uzantısının içeriğinde ETSIibaresine ait OID bulunmayan sertifika ile imzalanmış
//                  new object[]  {"P4_A20.pdf.xml",    "invalid"}, // “Nitelikli Sertifika İbareleri” içeriğinde BTK tarafından belirlenen ibareye ait OID bulunmayan sertifika ile imzalanmış
//                  new object[]  {"P4_A22.pdf.xml",    "invalid"}, // Süresi dolmuş sertifika ile imzalanmış
//                  new object[]  {"P4_A23.pdf.xml",    "invalid"}, // İmzası bozuk sertifika ile imzalanmış
//                  new object[]  {"P4_A24.pdf.xml",    "invalid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"P4_A25.pdf.xml",    "valid"}, // OCSP’de iptal olmuş sertifika ile imzalanmış, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"P4_A26.pdf.xml",    "invalid"}, // Kontrol edildiği OCSP cevabının validlik süresi dolmuş
//                  new object[]  {"P4_A27.pdf.xml",    "invalid"}, // Kontrol edildiği OCSP cevabı üzerindeki ESHS imzası bozuk
//                  new object[]  {"P4_A28.pdf.xml",    "invalid"}, // Kontrol edildiği OCSP sertifikasının süresi dolmuş
//                  new object[]  {"P4_A29.pdf.xml",    "invalid"}, // Kontrol edildiği OCSP sertifikasının imzası bozuk
//                  new object[]  {"P4_A30.pdf.xml",    "invalid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"P4_A31.pdf.xml",    "valid"}, // Kontrol edildiği OCSP sertifikası iptal olmuş, sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"P4_A34.pdf.xml",    "invalid"}, // OCSP cevabı içindeki sertifika farklı
//                  new object[]  {"P4_A35.pdf.xml",    "invalid"}, // Alt kök sertifikasının imzası bozuk
//                  new object[]  {"P4_A37.pdf.xml",    "invalid"}, // “İmza ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"P4_A38.pdf.xml",    "invalid"}, // “İmza ZD” TSA2'den alınmış ZDimzası bozuk
//                  new object[]  {"P4_A39.pdf.xml",    "invalid"}, // “İmza ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
//                  new object[]  {"P4_A40.pdf.xml",    "invalid"}, // “İmza ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                  new object[]  {"P4_A41.pdf.xml",    "invalid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"P4_A42.pdf.xml",    "valid"}, // “İmza ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"P4_A43.pdf.xml",    "invalid"}, // “İmza ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"P4_A44.pdf.xml",    "valid"}, // “İmza ZD” TSC1'den alınmış; Geçerli
//                  new object[]  {"P4_A45.pdf.xml",    "invalid"}, // “İmza ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
//                  new object[]  {"P4_A46.pdf.xml",    "invalid"}, // “İmza ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor
//                  new object[]  {"P4_A47.pdf.xml",    "invalid"}, // “Sertifika Referansları” içeriğinde ESHS kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"P4_A48.pdf.xml",    "invalid"}, // “Sertifika Referansları ” içeriğinde ESHS kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"P4_A49.pdf.xml",    "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"P4_A50.pdf.xml",    "invalid"}, // “Sertifika Referansları” içeriğinde alt kök sertifikasının referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"P4_A51.pdf.xml",    "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"P4_A52.pdf.xml",    "invalid"}, // “İptal Verisi Referansları” içeriğinde ESHS alt kök sertifikasının SİL referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"P4_A53.pdf.xml",    "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin olmadığı imza dosyası
//                  new object[]  {"P4_A54.pdf.xml",    "invalid"}, // “İptal Verisi Referansları” içeriğinde NES OCSP referans bilgisinin hatalı olduğu imza dosyası
//                  new object[]  {"P4_A55.pdf.xml",    "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS kök sertifikasının olmadığı imza dosyası
//                  new object[]  {"P4_A57.pdf.xml",    "invalid"}, // “Sertifika Değerleri” içeriğinde ESHS alt kök sertifikasının olmadığı imza dosyası
//                  new object[]  {"P4_A59.pdf.xml",    "invalid"}, // “İptal Verisi Değerleri” içeriğinde ESHS alt kök sertifikasının SİL'inin olmadığı imza dosyası
//                  new object[]  {"P4_A61.pdf.xml",    "invalid"}, // “İptal Verisi Değerleri” içeriğinde NES OCSP'sinin olmadığı imza dosyası
//                  new object[]  {"P4_A63.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSA1'den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"P4_A64.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSA2'den alınmış ZD imzası bozuk
//                  new object[]  {"P4_A65.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
//                  new object[]  {"P4_A66.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                  new object[]  {"P4_A67.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"P4_A68.pdf.xml",    "valid"}, // “İmza ve Referans ZD” TSA5'den alınmış; ZD sertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"P4_A69.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSB'den alınmış; ZD sertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"P4_A70.pdf.xml",    "valid"}, // “İmza ve Referans ZD” TSC1'den alınmış; Geçerli
//                  new object[]  {"P4_A71.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSC2'den alınmış; ZD sertifikası süresi dolmuş SİL'e referans veriyor
//                  new object[]  {"P4_A72.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSC3'den alınmış; ZD sertifikası imzası bozuk SİL'e referans veriyor
//                  new object[]  {"P4_A74.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD SİL’i imza dosyasına eklenmemiş
//                  new object[]  {"P4_A75.pdf.xml",    "invalid"}, // “Arşiv ZD” TSA1’den alınmış; “TSTInfo” içeriğindeki “messageImprint” özeti bozulmuş imza dosyası
//                  new object[]  {"P4_A76.pdf.xml",    "invalid"}, // “Arşiv ZD” TSA2'den alınmış ZDimzası bozuk
//                  new object[]  {"P4_A77.pdf.xml",    "invalid"}, // “Arşiv ZD” TSA3'den alınmış; ZDsertifikasının validlik süresi dolmuş
//                  new object[]  {"P4_A78.pdf.xml",    "invalid"}, // “Arşiv ZD” TSA4'den alınmış; ZDsertifikasının üzerindeki ESHSimzası bozuk
//                  new object[]  {"P4_A79.pdf.xml",    "invalid"}, // “Arşiv ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden önce iptal edilmiş
//                  new object[]  {"P4_A80.pdf.xml",    "valid"}, // “Arşiv ZD” TSA5'den alınmış; ZDsertifikası iptal olmuş; sertifika imza tarihinden sonra iptal edilmiş
//                  new object[]  {"P4_A81.pdf.xml",    "invalid"}, // “Arşiv ZD” TSB'den alınmış; ZDsertifikası imzası bozuk kök tarafından üretilmiş
//                  new object[]  {"P4_A82.pdf.xml",    "valid"}, // “Arşiv ZD” TSC1'den alınmış; Geçerli
//                  new object[]  {"P4_A83.pdf.xml",    "invalid"}, // “Arşiv ZD” TSC2'den alınmış; ZDsertifikası süresi dolmuş SİL'e referans veriyor
//                  new object[]  {"P4_A84.pdf.xml",    "invalid"}, // “Arşiv ZD” TSC3'den alınmış; ZDsertifikası imzası bozuk SİL'e referans veriyor
//                  new object[]  {"P4_A86.pdf.xml",    "invalid"}, // Üzerinde 2 adet Arşiv ZDbulunan dosya. İlk arşiv ZDTSC1'den alınmış; ZD SİL’i imza dosyasına eklenmemiş
//                  new object[]  {"P4_A90.pdf.xml",    "valid"}, // “Arşiv ZD” sertifikasının süresi imza tarihinden sonra dolmuş
//                  new object[]  {"P4_A93_s.pdf.xml",  "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid seri imzalı dosya
//                  new object[]  {"P4_A93_p.pdf.xml",  "invalid"}, // İlk imzacısının sertifikası iptal olmuş, ikinci imzacı sertifikası valid paralel imzalı dosya
//                  new object[]  {"P4_A94.pdf.xml",    "valid"} // “Arşiv ZD” SHA-1 algoritması kullanılarak imzalanmış
               
          
//                  /*#     API'nin gerçekleştiremeyeceği senaryolar:
//                    #     Yoksayma sebebi: İlgili imzalı dosyalar KamuSM eit-wiki'de mevcut değil
//                    #     | P4_A13.pdf.xml    | geçersiz  | “SigningCertificate” içeriği bozulmuş |
//                    #     API kapsamının dışındaki senaryolar:
//                    #     | P4_A3.pdf.xml     | geçersiz  | “DataObjectFormat/MimeType” imza özelliği JPEG (image/jpeg), imzalanan dosya tipinin PDF olduğu imzalı dosya |
//                    #     | P4_A21.pdf.xml    | geçersiz  | Eki word olan PDF/A-3 belgesi imzalanmış |
//                    #     | P4_A32.pdf.xml    | geçerli   | Maddi limit alanı “0” olan sertifika ile imzalanmış |
//                    #     | P4_A33.pdf.xml    | geçersiz  | Kullanım kısıtı olan sertifika ile imzalanmış |*/
           
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4_a", expectedResult, "xmlsignature-config.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_ESA_Enveloping_Signature_Validation_With_Missing_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"ESA_94.xml",  "invalid" }, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
//                new object[] {"ESA_96.xml",  "invalid" }, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
//                new object[] {"ESA_100.xml", "invalid" }  // Üzerinde 2 adet Arşiv ZDbulunan dosya. İlk arşiv ZDTSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esa", expectedResult, "xmlsignature-config-unavailable-root.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_ESXL1_Enveloping_Signature_Validation_With_Missing_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"ESXL1_94.xml",  "invalid" }, //“İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esxl1", expectedResult, "xmlsignature-config-unavailable-root.xml");
//            }
//        }

//        [TestFixture]
//        public class XAdES_P4_A_Enveloping_Signature_Validation_With_Missing_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[]  {"P4_A11.pdf.xml",    "invalid"}, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
//                new object[]  {"P4_A73.pdf.xml",    "invalid"}, // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
//                new object[]  {"P4_A85.pdf.xml",    "invalid"}  // Üzerinde 2 adet Arşiv ZDbulunan dosya. İlk arşiv ZDTSC1'den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4_a", expectedResult, "xmlsignature-config-unavailable-root.xml");
//            }
//        }

//        public class XAdES_P4_Enveloping_Signature_Validation_With_Missing_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"P4_11.pdf.xml",   "invalid" }, // “İmza ZD” TSC1’den alınmış; ZDkök sertifikası imza dosyasına eklenmemiş
//                new object[] {"P4_73.pdf.xml",   "invalid" }  // “İmza ve Referans ZD” TSC1’den alınmış; ZD kök sertifikası imza dosyasına eklenmemiş

//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4", expectedResult, "xmlsignature-config-unavailable-root.xml");
//            }
//        }

//        public class XAdES_BES_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"BES_38.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "bes", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public class XAdES_EST_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"EST_38.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "est", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public class XAdES_ESXL1_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"ESXL1_38.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esxl1", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public class XAdES_ESA_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] {"ESA_38.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "esa", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public class XAdES_P4_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] { "P4_36.pdf.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public class XAdES_P4_A_Enveloping_Signature_Validation_With_Invalid_Root_Certificate
//        {
//            public static Object[] TestCases =
//            {
//                new object[] { "P4_A36.pdf.xml",   "invalid" }, // Kök sertifikasının imzası bozu
//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String expectedResult)
//            {
//                validateXAdESSignature(pfx, "p4_a", expectedResult, "xmlsignature-config-invalid-root.xml");
//            }
//        }

//        public static void validateXAdESSignature(String file, String type, String expectedResult, String policyFile) 
//        {

//                ReadBaseSignedData(file, type, policyFile);

//                validationResult = signature.verify();
//                Assert.IsNotNull(validationResult);

//                string validationMessage = validationResult.ToString();
//                ValidationResultType validationResultType = validationResult.getType();

//                if (expectedResult.Equals("valid"))
//                {
//                    Assert.IsTrue(validationResultType == ValidationResultType.VALID, validationMessage);
//                }
//                else
//                {
//                    Assert.IsTrue(validationResultType == ValidationResultType.INCOMPLETE || validationResultType == ValidationResultType.INVALID, validationMessage);
//                }
//        }

//        private static void ReadBaseSignedData(String file, String type, String xmlConfig) 
//        {
            
//            string directory = rootFolder + @"data/validation-xades-" + type + @"/enveloping/";

//            Context context = new Context(directory + file);
//            context.Config = new Config(rootFolder + "config\\" + xmlConfig);

//            FileInfo signatureFile = new FileInfo(directory + file);
//            signature = XMLSignature.parse(new FileDocument(signatureFile), context);

//            Assert.IsNotNull(signature);
//        }

//    }
//}

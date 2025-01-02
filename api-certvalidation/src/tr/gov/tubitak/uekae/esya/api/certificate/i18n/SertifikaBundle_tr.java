package tr.gov.tubitak.uekae.esya.api.certificate.i18n;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle_tr;


public class SertifikaBundle_tr extends GenelBundle_tr
{
    private static Object[][] mSertifikaContents =
            {

                    {CertI18n.TAB1,"Sertifika"},
                    {CertI18n.TAB2,"Gelişmiş Görünüm"},
                    {CertI18n.TAB3,"Sertifika Zinciri"},

                    {CertI18n.SERTIFIKA_DOSYA,"Sertifika Dosyaları"},
                    {CertI18n.DATE_AFTER,"Geçerlilik Başlangıcı"},
                    {CertI18n.DATE_BEFORE,"Geçerlilik Sonu"},
                    {CertI18n.SERTIFIKA_GOSTER,"Sertifikayı Göster"},
                    {CertI18n.BC_TYPE,"Özne Türü"},
                    {CertI18n.BC_CA,"Sertifika Makamı"},
                    {CertI18n.BC_CA_DEGIL,"Sertifika Makamı Değil"},
                    {CertI18n.BC_PATH,"Yol Uzunluğu Kısıtlaması"},

                    {CertI18n.POLICY_IDENTIFIER,"İlke Tanımlayıcısı"},
                    {CertI18n.POLICY_ID,"İlke Niteleyici Kimliği"},
                    {CertI18n.POLICY_INFO,"İlke Niteleyici Bilgisi"},
                    {CertI18n.POLICY_QUALIFIER,"Niteleyici"},
                    {CertI18n.POLICY_NOTICE,"Kullanıcı Notu"},
                    {CertI18n.POLICY_CPS,"CPS"},

                    {CertI18n.AKI_ID,"Anahtar Kimliği"},
                    {CertI18n.AKI_ISSUER,"Yetkili Sertifikasını veren"},
                    {CertI18n.AKI_SERIAL,"Yetkili Sertifika seri numarası"},

                    {CertI18n.PM_IDP,"Sertifika Sağlayıcı Alan İlkesi"},
                    {CertI18n.PM_SDP,"Konu Alan İlkesi"},

                    {CertI18n.NITELIKLI_COMPLIANCE,"Bu sertifika, ETSI TS 101 862 standardına göre oluşturulmuş nitelikli elektronik sertifikadır."},
                    {CertI18n.NITELIKLI_TK,"Telekomünikasyon Kurumu tarafından belirlenmiş nitelikli elektronik sertifika profiline uygundur."},
                    {CertI18n.NITELIKLI_MONEY_LIMIT,"Para Limiti"},

                    {CertI18n.KULLANIM,"Kullanım Amaçları"},
                    {CertI18n.BILGI,"Sertifika Bilgileri"},
                    {CertI18n.TIP,"Tip"},
                    {CertI18n.ESHS,"ESHS"},
                    {CertI18n.BASLANGIC_TARIHI,"Başlangıç Tarihi"},
                    {CertI18n.BITIS_TARIHI,"Bitiş Tarihi"},
                    {CertI18n.IMZA_SERTIFIKASI,"İmza Sertifikası"},

                    {CertI18n.KONTROL_SONUCU, "Kontrol Sonucu"},

                    {CertI18n.DELTA_CRL_INDICATOR_KONTROLU, "Delta Sil Belirleyici Kontrolü"},
                    {CertI18n.DELTA_CRL_INDICATOR_VAR , "Delta Sil Belirleyici Var"},
                    {CertI18n.DELTA_CRL_INDICATOR_YOK , "Delta Sil Belirleyici Yok"},

                    {CertI18n.FRESHEST_CRL_KONTROLU , "Freshest Sil Kontrolü"},
                    {CertI18n.FRESHEST_CRL_VAR , "Freshest Sil Var"},
                    {CertI18n.FRESHEST_CRL_YOK , "Freshest Sil Yok"},

                    {CertI18n.SERTIFIKA_KEY_USAGE_KONTROLU , "Sertifika Anahtar Kullanımı Eklentisi Kontrolü"},
                    {CertI18n.SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU , "Sertifika Gelişmiş Anahtar Kullanımı Eklentisi Kontrolü"},

                    {CertI18n.OCSP_CEVABI_KONTROLCU , "OCSP Cevabı Kontrolu"},

                    {CertI18n.BASARILI , "Başarılı"},
                    {CertI18n.MALFORMED_REQUEST , "Malformed Request"},
                    {CertI18n.INTERNAL_ERROR , "Internal Error"},
                    {CertI18n.TRY_LATER , "Try Later"},
                    {CertI18n.SIG_REQUIRED , "Signature Required"},
                    {CertI18n.UNAUTHORIZED , "Unauthorized"},

                    {CertI18n.OCSP_IMZALAYAN_SERTIFIKA_KONTROLU , "OCSP İmzalayıcı Sertifika Kontrolü"},

                    {CertI18n.IMZALAYAN_SERTIFIKA_GECERLI , "İmzalayan Sertifika Geçerli"},
                    {CertI18n.IMZALAYAN_SERTIFIKA_GECERSIZ , "İmzalayan Sertifika Geçersiz"},


                    //OCSPImzaKontrolcu
                    {CertI18n.OCSP_IMZA_KONTROLU , "OCSP Cevabı İmza Kontrolü"},

                    {CertI18n.CEVAP_IMZALI_DEGIL , "OCSP Cevabı İmzalı Değil"},
                    {CertI18n.CEVAP_YAPISI_BOZUK , "OCSP Cevap Yapısı Bozuk"},
                    {CertI18n.CEVAPTA_SERTIFIKA_YOK , "OCSP Cevabında Sertifika Yok"},
                    {CertI18n.SERTIFIKA_OCSP_SERTIFIKASI_DEGIL , "OCSP Cevabındaki Sertifika OCSP Sertifikası Değil"},
                    {CertI18n.DOGRULAMA_KRIPTO_HATASI , "Doğrulama Kripto Hatası"},
                    {CertI18n.IMZA_DOGRULANAMADI , "OCSP Cevabı İmzası Doğrulanamadı"},
                    {CertI18n.IMZA_DOGRULANDI , "OCSP Cevabı İmzası Doğrulandı"},

                    //OCSPdenIptalKontrolcu
                    {CertI18n.OCSPDEN_IPTAL_KONTROLU , "OCSPden İptal Kontrolü"},
                    {CertI18n.OCSP_CEVABI_GECERSIZ, "OCSP Cevabı Geçersiz"},
                    {CertI18n.OCSP_CEVABI_BULUNAMADI , "OCSP Cevabı Bulunamadı"},
                    {CertI18n.SM_SERTIFIKASI_BULUNAMADI , "SM Sertifikası Bulunamadı"},
                    {CertI18n.SERTIFIKA_OCSPDE_GECERLI , "Sertifika OCSPde Geçerli"},
                    {CertI18n.SERTIFIKA_OCSPDE_GECERLI_DEGIL , "Sertifika OCSPde Geçerli Değil"},

                    //SildenIptalKontrolcu
                    {CertI18n.SILDEN_IPTAL_KONTROLU , "Silden İptal Kontrolü"},

                    {CertI18n.SIL_GECERSIZ , "Sil Geçersiz"},
                    {CertI18n.SERTIFIKA_LISTEDE , "Sertifika Silde İptal Edilmiş"},
                    {CertI18n.SERTIFIKA_LISTEDE_DEGIL , "Sertifika Silde Yok"},
                    {CertI18n.SIL_BULUNAMADI , "Sil Bulunamadı"},
                    {CertI18n.BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS, "Base Silde Askıya Alınmış Sertifika Delta Silde Çıkarılmış"},
                    {CertI18n.SERTIFIKA_DELTA_SILDE , "Sertifika Delta Silde İptal Edilmiş"},
                    {CertI18n.SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS , "Sertifika Delta Silde Çıkarılmış"},

                    //SeriNoPozitifKontrolcu
                    {CertI18n.SERI_NO_KONTROLU , "Seri Numarası Kontrolü"},

                    {CertI18n.SERI_NO_POZITIF , "Seri Numarası Pozitif"},
                    {CertI18n.SERI_NO_NEGATIF , "Seri Numarası Negatif"},

                    //SertifikaEklentiKontrolcu
                    {CertI18n.SERTIFIKA_EKLENTI_KONTROLU , "Sertifika Eklenti Kontrolü"},

                    {CertI18n.EKLENTI_YOK , "Sertifikada Eklenti Yok"},
                    {CertI18n.GECERSIZ_EKLENTI , "Sertifikada Geçersiz Bir Eklenti Var"},
                    {CertI18n.AYNI_EXTENSION_BIRDEN_FAZLA , "Sertifikada Aynı Eklentiden Birden Fazla Var"},
                    {CertI18n.EKLENTILER_GECERLI , "Sertifika Eklentileri Geçerli"},

                    //SertifikaTarihKontrolcu
                    {CertI18n.SERTIFIKA_TARIH_KONTROLU , "Sertifika Geçerlilik Tarihi Kontrolü"},

                    {CertI18n.SERTIFIKA_TARIH_GECERLI , "Sertifika Tarihi Geçerli"},
                    {CertI18n.SERTIFIKA_TARIH_GECERSIZ , "Sertifika Tarihi Geçersiz"},
                    {CertI18n.SERTIFIKA_TARIH_BILGISI_BOZUK , "Sertifika Tarih Bilgisi Yapısı Bozuk"},

                    //SignatureAlgAyniMiKontrolcu
                    {CertI18n.SIGNATURE_ALG_AYNIMI_KONTROLU , "İmzalama Algoritması Aynımı Kontrolü"},

                    {CertI18n.SIGNATURE_ALG_AYNI , "İmzalama Algoritması Değerleri Aynı"},
                    {CertI18n.SIGNATURE_ALG_FARKLI , "mzalama Algoritması Değerleri Farklı"},

                    //VersiyonKontrolcu
                    {CertI18n.SERTIFIKA_VERSIYON_KONTROLU , "Sertifika Versiyon Kontrolü"},

                    {CertI18n.EXTENSION_VAR_DOGRU , "Eklenti var, versiyon 3"},
                    {CertI18n.EXTENSION_VAR_YANLIS , "Eklenti var, versiyon 3 değil"},
                    {CertI18n.UID_VAR_DOGRU , "UID var, versiyon 2 veya üstü"},
                    {CertI18n.UID_VAR_YANLIS , "UID var, versiyon 2 veya üstü değil"},
                    {CertI18n.BASIT_ALANLAR_VAR_DOGRU , "Basit Alanlar var, versiyon 1 veya üstü"},
                    {CertI18n.BASIT_ALANLAR_VAR_YANLIS , "Basit Alanlar var, versiyon 1 veya üstü değil"},

                    //BasicConstraintCAKontrolcu
                    {CertI18n.BASIC_CONST_CA_KONTROLU , "Temel Kısıtlamalar CA Kontrolü"},

                    {CertI18n.BASIC_CONST_EKLENTI_YOK , "Temel Kısıtlamalar Eklentisi Yok"},
                    {CertI18n.BASIC_CONST_EKLENTISI_BOZUK , "Temel Kısıtlamalar Eklentisi Bozuk"},
                    {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YOK , "Temel Kısıtlamalar Eklentisi CA Değeri Yok"},
                    {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS , "Temel Kısıtlamalar Eklentisi CA Değeri Yanlış"},
                    {CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU , "Temel Kısıtlamalar Eklentisi CA Değeri Doğru"},

                    //KeyIdentifierKontrolcu
                    {CertI18n.KEY_IDENTIFIER_KONTROLU , "Anahtar Tanımlayıcısı Kontrolü"},

                    {CertI18n.SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK , "Sertifikada Yetkili Anahtar Tanımlayıcısı Yok"},
                    {CertI18n.SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK , "SM Sertifikasında Konu Anahtar Tanımlayıcısı"},
                    {CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ , "Sertifikadaki Yetkili Anahtar Tanımlayıcısı ile SM Sertifikasındaki Konu Anahtar Tanımlayıcısı Uyumsuz"},
                    {CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU , "Sertifikadaki Yetkili Anahtar Tanımlayıcısı ile SM Sertifikasındaki Konu Anahtar Tanımlayıcısı Uyumlu"},

                    //PathLenConstraintKontrolcu
                    {CertI18n.PATH_LEN_CONSTRAINT_KONTROLU , "Yol Uzunluğu Kısıtlayıcısı Kontrolü"},

                    {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK , "Temel Kısıtlar Eklentisinde Yol Uzunluğu Kısıtlayıcısı Yok"},
                    {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF , "Temel Kısıtlar Eklentisinde Yol Uzunluğu Kısıtlayıcısı Değeri Negatif"},
                    {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI , "Temel Kısıtlar Eklentisinde Yol Uzunluğu Kısıtlayıcısı Değeri Aşıldı"},
                    {CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI , "Temel Kısıtlar Eklentisinde Yol Uzunluğu Kısıtlayıcısı Değeri Geçerli"},

                    //SertifikaImzaKontrolcu
                    {CertI18n.SERTIFIKA_IMZA_KONTROLU , "Sertifika İmza Kontrolü"},

                    {CertI18n.SERTIFIKA_IMZALI_DEGIL , "Sertifika İmzalı Değil"},
                    {CertI18n.SERTIFIKA_YAPISI_BOZUK , "Sertifika Yapısı Bozuk"},
                    {CertI18n.SERTIFIKADA_ACIK_ANAHTAR_YOK , "Sertifikada Açık Anahtar Yok"},
                    {CertI18n.SERTIFIKADA_ACIK_ANAHTAR_BOZUK , "Sertifikadaki Açık Anahtar Bozuk"},
                    {CertI18n.SERTIFIKA_IMZA_DOGRULANAMADI , "Sertifika İmzası Doğrulanamadı"},
                    {CertI18n.SERTIFIKA_IMZA_DOGRULANDI , "Sertifika İmzası Doğrulandı"},

                    //SertifikaKeyUsageKontrolcu
                    {CertI18n.ISSUERCERT_KEY_USAGE_KONTROLU , "Yayıncı Sertifika Anahtar Kullanımı Eklentisi Kontrolü"},

                    {CertI18n.KEY_USAGE_YOK , "Anahtar Kullanımı Eklentisi Yok"},
                    {CertI18n.KEY_USAGE_BOZUK , "Anahtar Kullanımı Eklentisi Yapısı Bozuk"},
                    {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL , "Anahtar Kullanımı Eklentisi Sertifika İmzalayıcı Değil"},
                    {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI , "Anahtar Kullanımı Eklentisi Sertifika İmzalayıcı"},
                    {CertI18n.KEY_USAGE_SERTIFIKA_SIFRELEME, "Sertifika Anahtar Kullanımı Eklentisi Şifreleme "},

                    //SertifikaExtendedKeyUsageKontrolcu
                    {CertI18n.EXTENDEDKEY_USAGE_YOK , "Gelişmiş Anahtar Kullanımı Eklentisi Yok"},
                    {CertI18n.EXTENDEDKEY_USAGE_BOZUK , "Gelişmiş Anahtar Kullanımı Eklentisi Geçersiz"},
                    {CertI18n.EXTENDEDKEY_USAGE_GECERLI , "Anahtar Kullanımı Eklentisi Sertifika İmzalayıcı Değil"},

                    {CertI18n.ENCRYPTIONCERT_KEY_USAGE_KONTROLU , "Şifreleme Sertifikası Anahtar Kullanımı Eklentisi Kontrolü"},

                    //SertifikaNameKontrolcu
                    {CertI18n.SERTIFIKA_NAME_KONTROLU , "Sertifika Özne Adı Kontrolü"},

                    {CertI18n.SERTIFIKA_ISSUER_YOK , "Sertifikada Yayıncı Yok"},
                    {CertI18n.SMSERTIFIKA_SUBJECT_YOK , "Yayıncı Sertifikasında Özne Adı Yok"},
                    {CertI18n.ISSUER_SUBJECT_UYUMLU , "Sertifikadaki Yayıncı Adı ile Yayıncı Sertifikasındaki Özne Adı Değerleri Uyumlu"},
                    {CertI18n.ISSUER_SUBJECT_UYUMSUZ , "Sertifikadaki Yayıncı Adı ile Yayıncı Sertifikasındaki Özne Adı Değerleri Uyumsuz"},

                    {CertI18n.SERTIFIKA_NITELIKLI_KONTROLU, "Nitelikli Sertifika Kontrolü"},

                    {CertI18n.SERTIFIKA_NITELIKLI_KONTROLU_BASARILI, "Nitelikli sertifika kontrolü başarılı."},
                    {CertI18n.SERTIFIKA_KEYUSAGE_HATALI, "Sertifika anahtar kullanım alanı hatalı."},
                    {CertI18n.SERTIFIKA_NITELIKLI_IBARESI_YOK, "Sertifikada nitelikli ibaresi bulunamadı."},
                    {CertI18n.SERTIFIKA_KULLANICI_NOTU_YOK, "Sertifikada kullanıcı notu bulunamadı."},
                    //SilImzaKontrolcu
                    {CertI18n.SIL_IMZA_KONTROLU , "Sil İmza Kontrolü"},

                    {CertI18n.SIL_IMZALI_DEGIL , "Sil İmzalı Değil"},
                    {CertI18n.SIL_YAPISI_BOZUK , "Sil Yapısı Bozuk"},
                    {CertI18n.SIL_IMZA_DOGRULANAMADI , "Sil İmzası Doğrulanamadı"},
                    {CertI18n.SIL_IMZA_DOGRULANDI , "Sil İmzası Doğrulandı"},

                    //SilKeyUsageKontrolcu
                    {CertI18n.SIL_KEY_USAGE_KONTROLU , "Sil Anahtar Kullanımı Eklentisi Kontrolü"},

                    {CertI18n.KEY_USAGE_SIL_IMZALAYICI_DEGIL , "Anahtar Kullanımı Eklentisi Sil İmzalayıcı Değil"},
                    {CertI18n.KEY_USAGE_SIL_IMZALAYICI , "Anahtar Kullanımı Eklentisi Sil İmzalayıcı"},

                    //SilNameKontrolcu
                    {CertI18n.SIL_NAME_KONTROLU , "Sil Name Kontrolü"},

                    //SilEklentiKontrolcu
                    {CertI18n.SIL_EKLENTI_KONTROLU , "Sil Eklenti Kontrolü"},

                    //SilTarihKontrolcu
                    {CertI18n.SIL_TARIH_KONTROLU , "Sil Tarih Kontrolü"},

                    {CertI18n.GECERLI, "Sertifika Geçerli."},
                    {CertI18n.IPTAL_KONTROLU_SORUNLU,"İptal Kontrolü Sorunlu."},
                    {CertI18n.NOPATHFOUND, "Sertifika Zinciri tespit edilemedi."},
                    {CertI18n.SERTIFIKA_SORUNLU, "Sertifika Sorunlu."},
                    {CertI18n.ZINCIR_SORUNLU, "Sertifika Zinciri Sorunlu."},
                    {CertI18n.IPTAL_EDILMIS, "Sertifika İptal Edilmiş."},
                    {CertI18n.ASKIDA, "Sertifika Askıda."},
                    {CertI18n.UNKNOWN, "Bilinmiyor."},

                    // path validation result
                    {CertI18n.PVR_SUCCESS							, "Başarılı."},

                    {CertI18n.PVR_SERIALNUMBER_NOT_POSITIVE		    , "Seri Numarası negatif."},
                    {CertI18n.PVR_CERTIFICATE_EXTENSIONS_FAILURE	, "Sertifika Eklenti kontrolü başarısız."},
                    {CertI18n.PVR_CERTIFICATE_EXPIRED				, "Sertifika geçerlilik süresi dolmuş."},
                    {CertI18n.PVR_SIGNATURE_ALGORITHM_DIFFERENT	    , "Sertifika imza algoritması uyuşmuyor."},
                    {CertI18n.PVR_VERSION_CONTROL_FAILURE			, "Sertifika versiyon kontrolü başarısız."},

                    {CertI18n.PVR_REVOCATION_CONTROL_FAILURE		, "Sertifika iptal kontrolü yapılamadı."},
                    {CertI18n.PVR_CERTIFICATE_REVOKED				, "Sertifika iptal edilmiş."},

                    {CertI18n.PVR_SIGNATURE_CONTROL_FAILURE		    , "Sertifika İmza kontrolü başarısız."},
                    {CertI18n.PVR_BASICCONSTRAINTS_FAILURE			, "Sertifika Temel Kısıtlamalar kontrolü başarısız."},
                    {CertI18n.PVR_CDP_CONTROL_FAILURE				, "Sertifika SİL Dağıtım Noktaları kontrolü başarısız."},
                    {CertI18n.PVR_KEYID_CONTROL_FAILURE			    , "Sertifika Anahtar Tanımlayıcısı kontrolü başarısız."},
                    {CertI18n.PVR_NAMECONSTRAINTS_FAILURE			, "Sertifika İsim Kısıtlamaları kontrolü başarısız."},
                    {CertI18n.PVR_PATHLENCONSTRAINTS_FAILURE		, "Sertifika Yol Uzunluğu kontrolü başarısız."},
                    {CertI18n.PVR_POLICYCONSTRAINTS_CONTROL_FAILURE	, "Sertifika Politika Kısıtlamaları kontrolü başarısız."},
                    {CertI18n.PVR_KEYUSAGE_CONTROL_FAILURE			, "Sertifika Anahtar Kullanımı kontrolü başarısız."},
                    {CertI18n.PVR_EXTENDED_KEYUSAGE_CONTROL_FAILURE	, "Sertifika Gelişmiş Anahtar Kullanımı kontrolü başarısız."},
                    {CertI18n.PVR_NAME_CONTROL_FAILURE				, "Sertifika İsim kontrolü başarısız."},

                    {CertI18n.PVR_CRL_EXPIRED						, "SİL geçerlilik süresi dolmuş."},
                    {CertI18n.PVR_CRL_EXTENSIONS_CONTROL_FAILURE	, "SİL Eklentileri kontrolü başarısız."},

                    {CertI18n.PVR_CRL_SIGNATURE_CONTROL_FAILURE	    , "SİL İmza kontrolü başarısız."},
                    {CertI18n.PVR_CRL_KEYUSAGE_CONTROL_FAILURE		, "SİL Anahtar Kullanımı kontrolü başarısız."},
                    {CertI18n.PVR_CRL_NAME_CONTROL_FAILURE			, "SİL İsim kontrolü başarısız."},

                    {CertI18n.PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE  , "OCSP Cevap Durumu kontrolü başarısız."},
                    {CertI18n.PVR_OCSP_SIGNATURE_CONTROL_FAILURE	, "OCSP İmza kontrolü başarısız."},
                    {CertI18n.PVR_OCSP_RESPONSEDATE_EXPIRED 		, "OCSP zamanı geçerli değil."},
                    {CertI18n.PVR_OCSP_RESPONSEDATE_INVALID 		, "OCSP zamanı geçerli değil."},

                    {CertI18n.PVR_CRL_FRESHESTCRL_CONTROL_FAILURE	, "SİL En Güncel SİL Eklentisi kontrolü başarısız."},
                    {CertI18n.PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE	, "SİL Delta Sil Belirteci Eklentisi kontrolü başarısız."},

                    {CertI18n.PVR_POLICYMAPPING_CONTROL_FAILURE		, "Sertifika Politika Eşleştirmeleri kontrolü başarısız."},

                    {CertI18n.PVR_INVALID_PATH						, "Geçersiz Sertifika Zinciri."},
                    {CertI18n.PVR_INCOMPLETE_VALIDATION			    , "Sertifika Doğrulama tamamlanamadı."},
                    {CertI18n.PVR_UNSPECIFIED_FAILURE				, "Tanımlanmamış hata."},

                    {CertI18n.PVR_NO_PATHFOUND				        , "Geçerli zincir bulunamadı."},

                    {CertI18n.CERTIFICATE_VALIDATION_SUCCESSFUL		, "Sertifika doğrulama başarılı." },
                    {CertI18n.CERTIFICATE_VALIDATION_UNSUCCESSFUL	, "Sertifika doğrulaması başarısız. Doğrulama durumu: {0}" },
                    {CertI18n.CERTIFICATE_NO_PATH_FOUND				, "Güvendiğiniz bir sertifika zinciri oluşturulamadı. Sertifikanın kök sertifikası güvenilir sertifikalarınızdan biri olmayabilir."},
                    {CertI18n.CERTIFICATE_CHECKER_FAIL				, "{0} başarısız."},

                    {CertI18n.NOTRUSTEDCERTFOUND, "Hiç güvenilir kök bulunamadı."},

                    {CertI18n.VALID_DATE_INFO 						, "Tarih kontrolü başarılı."},
                    {CertI18n.INVALID_DATE_INFO 					, "Tarih kontrolü başarısız."},
                    {CertI18n.CORRUPT_DATE_INFO 					, "Tarih bilgisine erişilemiyor"},
                    {CertI18n.WRONG_FORMAT_QCC_STATEMENT 			, "Xml dosyasında girilen qualified certificate checker paramtresinin formatı yanlış."}

            };

    private static Object[][] mContents = new Object[mGenelContents.length + mSertifikaContents.length][];

    static	{
        System.arraycopy(mGenelContents, 0, mContents, 0, mGenelContents.length);
        System.arraycopy(mSertifikaContents, 0, mContents, mGenelContents.length, mSertifikaContents.length);
    }

    @Override
    public Object[][] getContents () //NOPMD
    {
        return mContents;
    }
}

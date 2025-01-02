package tr.gov.tubitak.uekae.esya.api.certificate.i18n;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class CertI18n extends GenelDil
{

    private static String BUNDLE_PATH = "tr.gov.tubitak.uekae.esya.api.certificate.i18n.SertifikaBundle";

    private static java.util.ResourceBundle msBundle;

    static {
        msBundle = ResourceBundle.getBundle(BUNDLE_PATH, I18nSettings.getLocale());

        I18nSettings.addLocaleSetListener(new I18nSettings.LocaleChangeListener()
        {
            public void localeChanged(Locale aNewLocale)
            {
                msBundle = ResourceBundle.getBundle(BUNDLE_PATH, aNewLocale);
            }
        });
    }

    public static String message(String aMessageCode)
    {
        return (msBundle.getString(aMessageCode));
    }


    public static String message(String aMessageCode, String ... aArgs)
    {
        MessageFormat formatter = new MessageFormat(msBundle.getString(aMessageCode));
        return (formatter.format(aArgs));
    }

    public static final String TAB1 = "SER.0";
    public static final String TAB2 = "SER.5";
    public static final String TAB3 = "SER.10";
    public static final String SERTIFIKA_DOSYA = "SER.15";
    public static final String DATE_AFTER = "SER.20";
    public static final String DATE_BEFORE = "SER.25";
    public static final String SERTIFIKA_GOSTER = "SER.30";
    public static final String BC_TYPE = "SER.35";
    public static final String BC_CA = "SER.40";
    public static final String BC_CA_DEGIL = "SER.45";
    public static final String BC_PATH = "SER.50";

    public static final String POLICY_IDENTIFIER = "SER.55";
    public static final String POLICY_ID = "SER.60";
    public static final String POLICY_INFO = "SER.65";
    public static final String POLICY_QUALIFIER = "SER.70";
    public static final String POLICY_NOTICE = "SER.75";
    public static final String POLICY_CPS = "SER.80";

    public static final String AKI_ID = "SER.85";
    public static final String AKI_ISSUER = "SER.90";
    public static final String AKI_SERIAL = "SER.95";

    public static final String PM_IDP = "SER.100";
    public static final String PM_SDP = "SER.105";

    public static final String NITELIKLI_COMPLIANCE = "SER.110";
    public static final String NITELIKLI_TK = "SER.115";
    public static final String NITELIKLI_MONEY_LIMIT = "SER.120";

    public static final String KULLANIM = "SER.125";
    public static final String BILGI = "SER.130";
    public static final String TIP = "SER.135";
    public static final String ESHS = "SER.140";
    public static final String BASLANGIC_TARIHI = "SER.145";
    public static final String BITIS_TARIHI = "SER.150";
    public static final String IMZA_SERTIFIKASI = "SER.155";

    //do�rulama sonu�lar�
    public static final String KONTROL_SONUCU = "KONTROL_SONUCU";

    public static final String DELTA_CRL_INDICATOR_KONTROLU = "DELTA_CRL_INDICATOR_KONTROLU";
    public static final String DELTA_CRL_INDICATOR_VAR = "DELTA_CRL_INDICATOR_VAR";
    public static final String DELTA_CRL_INDICATOR_YOK = "DELTA_CRL_INDICATOR_YOK";

    public static final String FRESHEST_CRL_KONTROLU = "FRESHEST_CRL_KONTROLU";
    public static final String FRESHEST_CRL_VAR = "FRESHEST_CRL_VAR";
    public static final String FRESHEST_CRL_YOK = "FRESHEST_CRL_YOK";

    //CevapDurumuKontrolcu
    public static final String OCSP_CEVABI_KONTROLCU = "OCSP_CEVABI_KONTROLCU";

    public static final String BASARILI = "BASARILI";
    public static final String MALFORMED_REQUEST = "MALFORMED_REQUEST";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String TRY_LATER = "TRY_LATER";
    public static final String SIG_REQUIRED = "SIG_REQUIRED";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    //OCSP ImzalayanSertifikaKontrolcu
    public static final String OCSP_IMZALAYAN_SERTIFIKA_KONTROLU = "OCSP_IMZALAYAN_SERTIFIKA_KONTROLU";

    public static final String IMZALAYAN_SERTIFIKA_GECERLI = "IMZALAYAN_SERTIFIKA_GECERLI";
    public static final String IMZALAYAN_SERTIFIKA_GECERSIZ = "IMZALAYAN_SERTIFIKA_GECERSIZ";


    //OCSPImzaKontrolcu
    public static final String OCSP_IMZA_KONTROLU = "OCSP_IMZA_KONTROLU";

    public static final String CEVAP_IMZALI_DEGIL = "CEVAP_IMZALI_DEGIL";
    public static final String CEVAP_YAPISI_BOZUK = "CEVAP_YAPISI_BOZUK";
    public static final String CEVAPTA_SERTIFIKA_YOK = "CEVAPTA_SERTIFIKA_YOK";
    public static final String SERTIFIKA_OCSP_SERTIFIKASI_DEGIL = "SERTIFIKA_OCSP_SERTIFIKASI_DEGIL";
    public static final String DOGRULAMA_KRIPTO_HATASI = "DOGRULAMA_KRIPTO_HATASI";
    public static final String IMZA_DOGRULANAMADI = "IMZA_DOGRULANAMADI";
    public static final String IMZA_DOGRULANDI = "IMZA_DOGRULANDI";

    //OCSPdenIptalKontrolcu
    public static final String OCSPDEN_IPTAL_KONTROLU = "OCSPDEN_IPTAL_KONTROLU";

    public static final String OCSP_CEVABI_GECERSIZ = "OCSP_CEVABI_GECERSIZ";
    public static final String OCSP_CEVABI_BULUNAMADI = "OCSP_CEVABI_BULUNAMADI";
    public static final String SM_SERTIFIKASI_BULUNAMADI = "SM_SERTIFIKASI_BULUNAMADI";
    public static final String SERTIFIKA_OCSPDE_GECERLI = "SERTIFIKA_OCSPDE_GECERLI";
    public static final String SERTIFIKA_OCSPDE_GECERLI_DEGIL = "SERTIFIKA_OCSPDE_GECERLI_DEGIL";

    //SildenIptalKontrolcu
    public static final String SILDEN_IPTAL_KONTROLU = "SILDEN_IPTAL_KONTROLU";

    public static final String SIL_GECERSIZ = "SIL_GECERSIZ";
    public static final String SERTIFIKA_LISTEDE = "SERTIFIKA_LISTEDE";
    public static final String SERTIFIKA_LISTEDE_DEGIL = "SERTIFIKA_LISTEDE_DEGIL";
    public static final String SIL_BULUNAMADI = "SIL_BULUNAMADI";
    public static final String BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS = "BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS";
    public static final String SERTIFIKA_DELTA_SILDE = "SERTIFIKA_DELTA_SILDE";
    public static final String SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS = "SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS";

    //SeriNoPozitifKontrolcu
    public static final String SERI_NO_KONTROLU = "SERI_NO_KONTROLU";

    public static final String SERI_NO_POZITIF = "SERI_NO_POZITIF";
    public static final String SERI_NO_NEGATIF = "SERI_NO_NEGATIF";

    //SertifikaEklentiKontrolcu
    public static final String SERTIFIKA_EKLENTI_KONTROLU = "SERTIFIKA_EKLENTI_KONTROLU";

    public static final String EKLENTI_YOK = "EKLENTI_YOK";
    public static final String GECERSIZ_EKLENTI = "GECERSIZ_EKLENTI";
    public static final String AYNI_EXTENSION_BIRDEN_FAZLA = "AYNI_EXTENSION_BIRDEN_FAZLA";
    public static final String EKLENTILER_GECERLI = "EKLENTILER_GECERLI";

    //SertifikaTarihKontrolcu
    public static final String SERTIFIKA_TARIH_KONTROLU = "SERTIFIKA_TARIH_KONTROLU";

    public static final String SERTIFIKA_TARIH_GECERLI = "SERTIFIKA_TARIH_GECERLI";
    public static final String SERTIFIKA_TARIH_GECERSIZ = "SERTIFIKA_TARIH_GECERSIZ";
    public static final String SERTIFIKA_TARIH_BILGISI_BOZUK = "SERTIFIKA_TARIH_BILGISI_BOZUK";
    
    
    public static final String SERTIFIKA_KEY_USAGE_KONTROLU = "SERTIFIKA_KEY_USAGE_KONTROLU";

    //SignatureAlgAyniMiKontrolcu
    public static final String SIGNATURE_ALG_AYNIMI_KONTROLU = "SIGNATURE_ALG_AYNIMI_KONTROLU";

    public static final String SIGNATURE_ALG_AYNI = "SIGNATURE_ALG_AYNI";
    public static final String SIGNATURE_ALG_FARKLI = "SIGNATURE_ALG_FARKLI";

    //VersiyonKontrolcu
    public static final String SERTIFIKA_VERSIYON_KONTROLU = "SERTIFIKA_VERSIYON_KONTROLU";

    public static final String EXTENSION_VAR_DOGRU = "EXTENSION_VAR_DOGRU";
    public static final String EXTENSION_VAR_YANLIS = "EXTENSION_VAR_YANLIS";
    public static final String UID_VAR_DOGRU = "UID_VAR_DOGRU";
    public static final String UID_VAR_YANLIS = "UID_VAR_YANLIS";
    public static final String BASIT_ALANLAR_VAR_DOGRU = "BASIT_ALANLAR_VAR_DOGRU";
    public static final String BASIT_ALANLAR_VAR_YANLIS = "BASIT_ALANLAR_VAR_YANLIS";

    //BasicConstraintCAKontrolcu
    public static final String BASIC_CONST_CA_KONTROLU = "BASIC_CONST_CA_KONTROLU";

    public static final String BASIC_CONST_EKLENTI_YOK = "BASIC_CONST_EKLENTI_YOK";
    public static final String BASIC_CONST_EKLENTISI_BOZUK = "BASIC_CONST_EKLENTISI_BOZUK";
    public static final String BASIC_CONST_EKLENTI_CA_DEGERI_YOK = "BASIC_CONST_EKLENTI_CA_DEGERI_YOK";
    public static final String BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS = "BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS";
    public static final String BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU = "BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU";

    //KeyIdentifierKontrolcu
    public static final String KEY_IDENTIFIER_KONTROLU = "KEY_IDENTIFIER_KONTROLU";

    public static final String SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK = "SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK";
    public static final String SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK = "SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK";
    public static final String AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ = "AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ";
    public static final String AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU = "AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU";

    //PathLenConstraintKontrolcu
    public static final String PATH_LEN_CONSTRAINT_KONTROLU = "PATH_LEN_CONSTRAINT_KONTROLU";

    public static final String BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK";
    public static final String BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF";
    public static final String BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI";
    public static final String BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI";

    //SertifikaImzaKontrolcu
    public static final String SERTIFIKA_IMZA_KONTROLU = "SERTIFIKA_IMZA_KONTROLU";

    public static final String SERTIFIKA_IMZALI_DEGIL = "SERTIFIKA_IMZALI_DEGIL";
    public static final String SERTIFIKA_YAPISI_BOZUK = "SERTIFIKA_YAPISI_BOZUK";
    public static final String SERTIFIKADA_ACIK_ANAHTAR_YOK = "SERTIFIKADA_ACIK_ANAHTAR_YOK";
    public static final String SERTIFIKADA_ACIK_ANAHTAR_BOZUK = "SERTIFIKADA_ACIK_ANAHTAR_BOZUK";
    public static final String SERTIFIKA_IMZA_DOGRULANAMADI = "SERTIFIKA_IMZA_DOGRULANAMADI";
    public static final String SERTIFIKA_IMZA_DOGRULANDI = "SERTIFIKA_IMZA_DOGRULANDI";

    //SertifikaKeyUsageKontrolcu
    public static final String ISSUERCERT_KEY_USAGE_KONTROLU = "SERTIFIKA_KEY_USAGE_KONTROLU";

    public static final String KEY_USAGE_YOK = "KEY_USAGE_YOK";
    public static final String KEY_USAGE_BOZUK = "KEY_USAGE_BOZUK";
    public static final String KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL = "KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL";
    public static final String KEY_USAGE_SERTIFIKA_IMZALAYICI = "KEY_USAGE_SERTIFIKA_IMZALAYICI";
    public static final String KEY_USAGE_SERTIFIKA_SIFRELEME = "KEY_USAGE_SERTIFIKA_SIFRELEME";



    //SertifikaExtendedKeyUsageKontrolcu
    public static final String SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU = "SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU";

    public static final String EXTENDEDKEY_USAGE_YOK = "EXTENDED_KEY_USAGE_YOK";
    public static final String EXTENDEDKEY_USAGE_BOZUK = "EXTENDED_KEY_USAGE_BOZUK";
    public static final String EXTENDEDKEY_USAGE_GECERLI = "EXTENDED_KEY_USAGE_GECERLI";


    //Şifreleme Sertifikası Key Usage Kontrolü
    public static final String ENCRYPTIONCERT_KEY_USAGE_KONTROLU = "SIFRELEME_SERTIFIKASI_KEY_USAGE_KONTROLU";

    //SertifikaNameKontrolcu
    public static final String SERTIFIKA_NAME_KONTROLU = "SERTIFIKA_NAME_KONTROLU";

    public static final String SERTIFIKA_ISSUER_YOK = "SERTIFIKA_ISSUER_YOK";
    public static final String SMSERTIFIKA_SUBJECT_YOK = "SMSERTIFIKA_SUBJECT_YOK";
    public static final String ISSUER_SUBJECT_UYUMLU = "ISSUER_SUBJECT_UYUMLU";
    public static final String ISSUER_SUBJECT_UYUMSUZ = "ISSUER_SUBJECT_UYUMSUZ";
    
    //SertifikaNitelikliKontrolcu
    public static final String SERTIFIKA_NITELIKLI_KONTROLU = "SERTIFIKA_NITELIKLI_KONTROLU";

    public static final String SERTIFIKA_NITELIKLI_KONTROLU_BASARILI = "SERTIFIKA_NITELIKLI_KONTROLU_BASARILI";
    public static final String SERTIFIKA_KEYUSAGE_HATALI = "SERTIFIKA_KEYUSAGE_HATALI";
    public static final String SERTIFIKA_NITELIKLI_IBARESI_YOK = "SERTIFIKA_NITELIKLI_IBARESI_YOK";
    public static final String SERTIFIKA_KULLANICI_NOTU_YOK = "SERTIFIKA_KULLANICI NOTU YOK";


    //SilImzaKontrolcu
    public static final String SIL_IMZA_KONTROLU = "SIL_IMZA_KONTROLU";

    public static final String SIL_IMZALI_DEGIL = "SIL_IMZALI_DEGIL";
    public static final String SIL_YAPISI_BOZUK = "SIL_YAPISI_BOZUK";
    public static final String SIL_IMZA_DOGRULANAMADI = "SIL_IMZA_DOGRULANAMADI";
    public static final String SIL_IMZA_DOGRULANDI = "SIL_IMZA_DOGRULANDI";

    //SilKeyUsageKontrolcu
    public static final String SIL_KEY_USAGE_KONTROLU = "SIL_KEY_USAGE_KONTROLU";

    public static final String KEY_USAGE_SIL_IMZALAYICI_DEGIL = "KEY_USAGE_SIL_IMZALAYICI_DEGIL";
    public static final String KEY_USAGE_SIL_IMZALAYICI = "KEY_USAGE_SIL_IMZALAYICI";

    //SilNameKontrolcu
    public static final String SIL_NAME_KONTROLU = "SIL_NAME_KONTROLU";

    //SilEklentiKontrolcu
    public static final String SIL_EKLENTI_KONTROLU = "SIL_EKLENTI_KONTROLU";

    //SilTarihKontrolcu
    public static final String SIL_TARIH_KONTROLU = "SIL_TARIH_KONTROLU";


    //geçerlilik sonuçları
    public static final String GECERLI = "GECERLI";
    public static final String IPTAL_KONTROLU_SORUNLU = "IPTAL_KONTROLU_SORUNLU";
    public static final String SERTIFIKA_SORUNLU = "SERTIFIKA_SORUNLU";

    public static final String NOPATHFOUND = "NOPATHFOUND";
    public static final String ZINCIR_SORUNLU = "ZINCIR_SORUNLU";
    public static final String IPTAL_EDILMIS = "IPTAL_EDILMIS";
    public static final String ASKIDA = "ASKIDA";
    public static final String UNKNOWN = "UNKNOWN";

    // Path validaion result
    public static final String PVR_SUCCESS = "Başarılı.";

    public static final String PVR_SERIALNUMBER_NOT_POSITIVE = "Seri Numarası negatif.";
    public static final String PVR_CERTIFICATE_EXTENSIONS_FAILURE = "Sertifika Eklenti kontrolü başarısız.";
    public static final String PVR_CERTIFICATE_EXPIRED = "Sertifika geçerlilik süresi dolmuş.";
    public static final String PVR_SIGNATURE_ALGORITHM_DIFFERENT = "Sertifika imza algoritması uyuşmuyor.";
    public static final String PVR_VERSION_CONTROL_FAILURE = "Sertifika versiyon kontrolü başarısız.";

    public static final String PVR_REVOCATION_CONTROL_FAILURE = "Sertifika iptal kontrolü başarısız.";
    public static final String PVR_CERTIFICATE_REVOKED = "Sertifika iptal edilmiş.";

    public static final String PVR_SIGNATURE_CONTROL_FAILURE = "Sertifika İmza kontrolü başarısız.";
    public static final String PVR_BASICCONSTRAINTS_FAILURE = "Sertifika Temel Kısıtlamalar kontrolü başarısız.";
    public static final String PVR_CDP_CONTROL_FAILURE = "Sertifika SİL Dağıtım Noktaları kontrolü başarısız.";
    public static final String PVR_KEYID_CONTROL_FAILURE = "Sertifika Anahtar Tanımlayıcısı kontrolü başarısız.";
    public static final String PVR_NAMECONSTRAINTS_FAILURE = "Sertifika İsim Kısıtlamaları kontrolü başarısız.";
    public static final String PVR_PATHLENCONSTRAINTS_FAILURE = "Sertifika Yol Uzunluğu kontrolü başarısız.";
    public static final String PVR_POLICYCONSTRAINTS_CONTROL_FAILURE = "Sertifika Politika Kısıtlamaları kontrolü başarısız.";
    public static final String PVR_KEYUSAGE_CONTROL_FAILURE = "Sertifika Anahtar Kullanımı kontrolü başarısız.";
    public static final String PVR_EXTENDED_KEYUSAGE_CONTROL_FAILURE = "Sertifika Gelişmiş Anahtar Kullanımı kontrolü başarısız.";
    public static final String PVR_NAME_CONTROL_FAILURE = "Sertifika İsim kontrolü başarısız.";

    public static final String PVR_CRL_EXPIRED = "SİL geçerlilik süresi dolmuş.";
    public static final String PVR_CRL_EXTENSIONS_CONTROL_FAILURE = "SİL Eklentileri kontrolü başarısız.";

    public static final String PVR_CRL_SIGNATURE_CONTROL_FAILURE = "SİL İmza kontrolü başarısız.";
    public static final String PVR_CRL_KEYUSAGE_CONTROL_FAILURE = "SİL Anahtar Kullanımı kontrolü başarısız.";
    public static final String PVR_CRL_NAME_CONTROL_FAILURE = "SİL İsim kontrolü başarısız.";

    public static final String PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE = "OCSP Cevap Durumu kontrolü başarısız.";
    public static final String PVR_OCSP_SIGNATURE_CONTROL_FAILURE = "OSCP İmza kontrolü başarısız.";
    // todo
    public static final String PVR_OCSP_RESPONSEDATE_EXPIRED = "OCSP zamanı geçerli değil";
    public static final String PVR_OCSP_RESPONSEDATE_INVALID = "OCSP zamanı geçerli değil";


    public static final String PVR_CRL_FRESHESTCRL_CONTROL_FAILURE = "SİL En Güncel SİL Eklentisi kontrolü başarısız.";
    public static final String PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE = "SİL Delta Sil Belirteci Eklentisi kontrolü başarısız.";

    public static final String PVR_POLICYMAPPING_CONTROL_FAILURE = "Sertifika Politika Eşleştirmeleri kontrolü başarısız.";

    public static final String PVR_INVALID_PATH = "Geçersiz Sertifika Zinciri.";
    public static final String PVR_INCOMPLETE_VALIDATION = "Sertifika Doğrulama tamamlanamadı.";
    public static final String PVR_UNSPECIFIED_FAILURE = "Tanımlanmamış hata.";

    public static final String PVR_NO_PATHFOUND = "Geçerli zincir bulunamadı.";
    
    public static final String PVR_QCC_NO_STATEMENT_ID = "Sertifikada nitelikli ibaresi bulunamadı.";
    public static final String PVR_QCC_NO_USER_NOTICE = "Sertifikada kullanıcı notu ibaresi bulunamadı.";

    public static final String PVR_ENCRYPTIONCERT_KEY_USAGE_CONTROL_FAILURE = "Sertifikada şifreleme için kullanım ibaresi bulunamadı.";
    
    public static final String CERTIFICATE_VALIDATION_SUCCESSFUL = "Sertifika doğrulama başarılı.";
    public static final String CERTIFICATE_VALIDATION_UNSUCCESSFUL = "Sertifika doğrulaması başarısız. Doğrulama durumu: {0}";
    public static final String CERTIFICATE_NO_PATH_FOUND = "Güvendiğiniz bir sertifika zinciri oluşturulamadı. Sertifikanın kök sertifikası güvenilir sertifikalarınızdan biri olmayabilir.";

    public static final String NOTRUSTEDCERTFOUND = "Hiç güvenilir kök bulunamadı.";

    public static final String CERTIFICATE_CHECKER_FAIL = "{0} başarısız.";
    
    public static final String VALID_DATE_INFO = "Tarih kontrolü başarılı.";
    public static final String INVALID_DATE_INFO = "Tarih kontrolü başarısız.";
    public static final String CORRUPT_DATE_INFO = "Tarih bilgisine erişilemiyor";

    public static final String WRONG_FORMAT_QCC_STATEMENT = "WRONG_FORMAT_QCC_STATEMENT";

}


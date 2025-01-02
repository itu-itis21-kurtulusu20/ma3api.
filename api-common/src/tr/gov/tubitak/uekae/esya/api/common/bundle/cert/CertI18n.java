package tr.gov.tubitak.uekae.esya.api.common.bundle.cert;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class CertI18n extends GenelDil
{
//	static { new SertifikaBundle(); }
    private static final String BUNDLE_PATH = "tr.gov.tubitak.uekae.esya.api.common.bundle.cert.SertifikaBundle";
    private static java.util.ResourceBundle msRB; //= java.util.ResourceBundle.getBundle("tr.gov.tubitak.uekae.esya.sertifikaislemleri.bundle.SertifikaBundle", java.util.Locale.getDefault());

    static {
        msRB = ResourceBundle.getBundle(BUNDLE_PATH, I18nSettings.getLocale());

        I18nSettings.addLocaleSetListener(new I18nSettings.LocaleChangeListener()
        {
            public void localeChanged(Locale aNewLocale)
            {
                msRB = ResourceBundle.getBundle(BUNDLE_PATH, aNewLocale);
            }
        });
    }

    public static final String TAB1="SER.0";
    public static final String TAB2="SER.5";
    public static final String TAB3="SER.10";
    public static final String SERTIFIKA_DOSYA="SER.15";
    public static final String DATE_AFTER="SER.20";
    public static final String DATE_BEFORE="SER.25";
    public static final String SERTIFIKA_GOSTER="SER.30";
    public static final String BC_TYPE="SER.35";
    public static final String BC_CA="SER.40";
    public static final String BC_CA_DEGIL="SER.45";
    public static final String BC_PATH="SER.50";

    public static final String POLICY_IDENTIFIER="SER.55";
    public static final String POLICY_ID="SER.60";
    public static final String POLICY_INFO="SER.65";
    public static final String POLICY_QUALIFIER="SER.70";
    public static final String POLICY_NOTICE="SER.75";
    public static final String POLICY_CPS="SER.80";

    public static final String AKI_ID="SER.85";
    public static final String AKI_ISSUER="SER.90";
    public static final String AKI_SERIAL="SER.95";

    public static final String PM_IDP="SER.100";
    public static final String PM_SDP="SER.105";

    public static final String NITELIKLI_COMPLIANCE="SER.110";
    public static final String NITELIKLI_TK="SER.115";
    public static final String NITELIKLI_MONEY_LIMIT="SER.120";

    public static final String KULLANIM="SER.125";
    public static final String BILGI="SER.130";
    public static final String TIP="SER.135";
    public static final String ESHS="SER.140";
    public static final String BASLANGIC_TARIHI="SER.145";
    public static final String BITIS_TARIHI="SER.150";
    public static final String IMZA_SERTIFIKASI="SER.155";

    //doğrulama sonuçları
    public static final String KONTROL_SONUCU= "KONTROL_SONUCU";

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
    public static final String SERTIFIKA_SURESI_DOLMUS = "SERTIFIKA_SURESI_DOLMUS";

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

//    public static final String KEY_USAGE_YOK = "KEY_USAGE_YOK";
//    public static final String KEY_USAGE_BOZUK = "KEY_USAGE_BOZUK";
//    public static final String KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL = "KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL";
//    public static final String KEY_USAGE_SERTIFIKA_IMZALAYICI = "KEY_USAGE_SERTIFIKA_IMZALAYICI";


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
    public static final String ZINCIR_SORUNLU = "ZINCIR_SORUNLU";
    public static final String IPTAL_EDILMIS = "IPTAL_EDILMIS";
    public static final String ASKIDA = "ASKIDA";


    public static String message(String aMessageCode)
    {
         return (msRB.getString(aMessageCode));
    }


    public static String message(String aMessageCode, String[] aArgs)
    {
         MessageFormat formatter = new MessageFormat(msRB.getString(aMessageCode));
         return (formatter.format(aArgs));
    }
}


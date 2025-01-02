using System.Reflection;
using System.Resources;
namespace tr.gov.tubitak.uekae.esya.api.common.bundle
{
    public static class Resource
    {
        private static readonly ResourceManager MResources = new ResourceManager("tr.gov.tubitak.uekae.esya.api.common.Properties.Resource", Assembly.GetExecutingAssembly());       

        public static readonly string KRIPTO_GENEL_HATA = "KRIPTO_GENEL_HATA";
        public static readonly string INIT_EDILMIS = "INIT_EDILMIS";
        public static readonly string ARGUMAN_BILINMIYOR = "ARGUMAN_BILINMIYOR";
        public static readonly string OZETALG_0_BILINMIYOR = "OZETALG_0_BILINMIYOR";
        public static readonly string ECDSAANAHTARBOYU_0_BILINMIYOR = "ECDSAANAHTARBOYU_0_BILINMIYOR";
        public static readonly string IMZALG_0_BILINMIYOR = "IMZALG_0_BILINMIYOR";
        public static readonly string CERT_HATALI = "CERT_HATALI";
        public static readonly string BC_TYPE = "BC_TYPE";
        public static readonly string BC_CA = "BC_CA";
        public static readonly string BC_CA_DEGIL = "BC_CA_DEGIL";
        public static readonly string BC_PATH = "BC_PATH";
        public static readonly string POLICY_IDENTIFIER = "POLICY_IDENTIFIER";
        public static readonly string POLICY_INFO = "POLICY_INFO";
        public static readonly string POLICY_ID = "POLICY_ID";
        public static readonly string POLICY_CPS = "POLICY_CPS";
        public static readonly string POLICY_QUALIFIER = "POLICY_QUALIFIER";
        public static readonly string POLICY_NOTICE = "POLICY_NOTICE";
        public static readonly string EKU_SERVER_AUTHENTICATION = "EKU_SERVER_AUTHENTICATION";
        public static readonly string EKU_CLIENT_AUTHENTICATION = "EKU_CLIENT_AUTHENTICATION";
        public static readonly string EKU_CODE_SIGNING = "EKU_CODE_SIGNING";
        public static readonly string EKU_EMAIL_PROTECTION = "EKU_EMAIL_PROTECTION";
        public static readonly string EKU_IPSEC_END_SYSTEM = "EKU_IPSEC_END_SYSTEM";
        public static readonly string EKU_IPSEC_TUNNEL = "EKU_IPSEC_TUNNEL";
        public static readonly string EKU_IPSEC_USER = "EKU_IPSEC_USER";
        public static readonly string EKU_TIME_STAMPING = "EKU_TIME_STAMPING";
        public static readonly string EKU_OCSP_SIGNING = "EKU_OCSP_SIGNING";
        public static readonly string EKU_DVCS = "EKU_DVCS";
        public static readonly string NITELIKLI_COMPLIANCE = "NITELIKLI_COMPLIANCE";
        public static readonly string NITELIKLI_TK = "NITELIKLI_TK";
        public static readonly string NITELIKLI_MONEY_LIMIT = "NITELIKLI_MONEY_LIMIT";
        public static readonly string COUNTRYCITIZENSHIP = "COUNTRYCITIZENSHIP";
        public static readonly string COUNTRYRESIDENCE = "COUNTRYRESIDENCE";
        public static readonly string DATEOFBIRTH = "DATEOFBIRTH";
        public static readonly string GENDER = "GENDER";
        public static readonly string PLACEOFBIRTH = "PLACEOFBIRTH";
        public static readonly string ROLE = "ROLE";

        public static readonly string PM_IDP = "PM_IDP";
        public static readonly string PM_SDP = "PM_SDP";

        public static readonly string BASIC_CONST_CA_KONTROLU = "BASIC_CONST_CA_KONTROLU";
        public static readonly string PATH_LEN_CONSTRAINT_KONTROLU = "PATH_LEN_CONSTRAINT_KONTROLU";

        public static readonly string SIL_IMZALI_DEGIL = "SIL_IMZALI_DEGIL";
        public static readonly string SIL_YAPISI_BOZUK = "SIL_YAPISI_BOZUK";
        public static readonly string SERTIFIKADA_ACIK_ANAHTAR_YOK = "SERTIFIKADA_ACIK_ANAHTAR_YOK";
        public static readonly string SERTIFIKADA_ACIK_ANAHTAR_BOZUK = "SERTIFIKADA_ACIK_ANAHTAR_BOZUK";
        public static readonly string DOGRULAMA_KRIPTO_HATASI = "DOGRULAMA_KRIPTO_HATASI";
        public static readonly string SIL_IMZA_DOGRULANAMADI = "SIL_IMZA_DOGRULANAMADI";
        public static readonly string SIL_IMZA_DOGRULANDI = "SIL_IMZA_DOGRULANDI";
        public static readonly string KONTROL_SONUCU = "KONTROL_SONUCU";
        public static readonly string SIL_IMZA_KONTROLU = "SIL_IMZA_KONTROLU";

        public static readonly string AGREEMENTALG_BILINMIYOR = "AGREEMENTALG_BILINMIYOR";

        public static readonly string AKI_ID = "AKI_ID";
        public static readonly string AKI_ISSUER = "AKI_ISSUER";
        public static readonly string AKI_SERIAL = "AKI_SERIAL";

        public static readonly string BASIC_CONST_EKLENTI_YOK = "BASIC_CONST_EKLENTI_YOK";
        public static readonly string BASIC_CONST_EKLENTISI_BOZUK = "BASIC_CONST_EKLENTISI_BOZUK";
        public static readonly string BASIC_CONST_EKLENTI_CA_DEGERI_YOK = "BASIC_CONST_EKLENTI_CA_DEGERI_YOK";
        public static readonly string BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS = "BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS";
        public static readonly string BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU = "BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU";

        public static readonly string FRESHEST_CRL_VAR = "FRESHEST_CRL_VAR";
        public static readonly string FRESHEST_CRL_YOK = "FRESHEST_CRL_YOK";
        public static readonly string FRESHEST_CRL_KONTROLU = "FRESHEST_CRL_KONTROLU";

        public static readonly string CEVAP_IMZALI_DEGIL = "CEVAP_IMZALI_DEGIL";
        public static readonly string CEVAP_YAPISI_BOZUK = "CEVAP_YAPISI_BOZUK";
        public static readonly string CEVAPTA_SERTIFIKA_YOK = "CEVAPTA_SERTIFIKA_YOK";
        public static readonly string SERTIFIKA_OCSP_SERTIFIKASI_DEGIL = "SERTIFIKA_OCSP_SERTIFIKASI_DEGIL";
        public static readonly string IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU = "IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU";
        public static readonly string IMZA_DOGRULANAMADI = "IMZA_DOGRULANAMADI";
        public static readonly string IMZA_DOGRULANDI = "IMZA_DOGRULANDI";
        public static readonly string OCSP_IMZA_KONTROLU = "OCSP_IMZA_KONTROLU";

        public static readonly string SERTIFIKA_IMZALI_DEGIL = "SERTIFIKA_IMZALI_DEGIL";
        public static readonly string SERTIFIKA_YAPISI_BOZUK = "SERTIFIKA_YAPISI_BOZUK";
        public static readonly string SERTIFIKA_IMZA_DOGRULANAMADI = "SERTIFIKA_IMZA_DOGRULANAMADI";
        public static readonly string SERTIFIKA_IMZA_DOGRULANDI = "SERTIFIKA_IMZA_DOGRULANDI";
        public static readonly string SERTIFIKA_IMZA_KONTROLU = "SERTIFIKA_IMZA_KONTROLU";

        public static readonly string EKLENTI_YOK = "EKLENTI_YOK";
        public static readonly string GECERSIZ_EKLENTI = "GECERSIZ_EKLENTI";
        public static readonly string AYNI_EXTENSION_BIRDEN_FAZLA = "AYNI_EXTENSION_BIRDEN_FAZLA";
        public static readonly string EKLENTILER_GECERLI = "EKLENTILER_GECERLI";

        public static readonly string SERTIFIKA_ISSUER_YOK = "SERTIFIKA_ISSUER_YOK";
        public static readonly string SMSERTIFIKA_SUBJECT_YOK = "SMSERTIFIKA_SUBJECT_YOK";
        public static readonly string ISSUER_SUBJECT_UYUMLU = "ISSUER_SUBJECT_UYUMLU";
        public static readonly string ISSUER_SUBJECT_UYUMSUZ = "ISSUER_SUBJECT_UYUMSUZ";

        public static readonly string EXTENSION_VAR_DOGRU = "EXTENSION_VAR_DOGRU";
        public static readonly string EXTENSION_VAR_YANLIS = "EXTENSION_VAR_YANLIS";
        public static readonly string UID_VAR_DOGRU = "UID_VAR_DOGRU";
        public static readonly string UID_VAR_YANLIS = "UID_VAR_YANLIS";
        public static readonly string BASIT_ALANLAR_VAR_DOGRU = "BASIT_ALANLAR_VAR_DOGRU";
        public static readonly string BASIT_ALANLAR_VAR_YANLIS = "BASIT_ALANLAR_VAR_YANLIS";
                               
        public static readonly string VERSION = "VERSION";
                               
        public static readonly string SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK = "SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK";
        public static readonly string SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK = "SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK";
        public static readonly string AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ = "AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ";
        public static readonly string AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU = "AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU";
        public static readonly string KEY_IDENTIFIER_KONTROLU = "KEY_IDENTIFIER_KONTROLU";
                               
        public static readonly string SIL_GECERSIZ = "SIL_GECERSIZ";
        public static readonly string SERTIFIKA_LISTEDE = "SERTIFIKA_LISTEDE";
        public static readonly string SERTIFIKA_LISTEDE_DEGIL = "SERTIFIKA_LISTEDE_DEGIL";
        public static readonly string SIL_BULUNAMADI = "SIL_BULUNAMADI";
        public static readonly string BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS = "BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS";
        public static readonly string SERTIFIKA_DELTA_SILDE = "SERTIFIKA_DELTA_SILDE";
        public static readonly string SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS = "SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS";
                               
        public static readonly string BASARILI = "BASARILI";
        public static readonly string MALFORMED_REQUEST = "MALFORMED_REQUEST";
        public static readonly string INTERNAL_ERROR = "INTERNAL_ERROR";
        public static readonly string TRY_LATER = "TRY_LATER";
        public static readonly string SIG_REQUIRED = "SIG_REQUIRED";
        public static readonly string UNAUTHORIZED = "UNAUTHORIZED";
        public static readonly string OCSP_CEVABI_KONTROLCU = "OCSP_CEVABI_KONTROLCU";
                               
        public static readonly string DELTA_CRL_INDICATOR_VAR = "DELTA_CRL_INDICATOR_VAR";
        public static readonly string DELTA_CRL_INDICATOR_YOK = "DELTA_CRL_INDICATOR_YOK";
        public static readonly string DELTA_CRL_INDICATOR_KONTROLU = "DELTA_CRL_INDICATOR_KONTROLU";
                               
        public static readonly string BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK";
        public static readonly string BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF";
        public static readonly string BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI";
        public static readonly string BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI = "BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI";
                               
        public static readonly string IMZALAYAN_SERTIFIKA_GECERLI = "IMZALAYAN_SERTIFIKA_GECERLI";
        public static readonly string IMZALAYAN_SERTIFIKA_GECERSIZ = "IMZALAYAN_SERTIFIKA_GECERSIZ";
        public static readonly string OCSP_IMZALAYAN_SERTIFIKA_KONTROLU = "OCSP_IMZALAYAN_SERTIFIKA_KONTROLU";
                               
        public static readonly string GECERLI = "GECERLI";
        public static readonly string IPTAL_KONTROLU_SORUNLU = "IPTAL_KONTROLU_SORUNLU";
        public static readonly string SERTIFIKA_SORUNLU = "SERTIFIKA_SORUNLU";
        public static readonly string NO_TRUSTED_CERT_FOUND = "NO_TRUSTED_CERT_FOUND";
        public static readonly string NOPATHFOUND = "NOPATHFOUND";
        public static readonly string ZINCIR_SORUNLU = "ZINCIR_SORUNLU";
        public static readonly string IPTAL_EDILMIS = "IPTAL_EDILMIS";
        public static readonly string ASKIDA = "ASKIDA";
        public static readonly string UNKNOWN = "UNKNOWN";
                               
        public static readonly string KEY_USAGE_YOK = "KEY_USAGE_YOK";
        public static readonly string KEY_USAGE_BOZUK = "KEY_USAGE_BOZUK";
        public static readonly string KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL = "KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL";
        public static readonly string KEY_USAGE_SERTIFIKA_IMZALAYICI = "KEY_USAGE_SERTIFIKA_IMZALAYICI";
        public static readonly string KEY_USAGE_SERTIFIKA_SIFRELEME = "KEY_USAGE_SERTIFIKA_SIFRELEME";
        public static readonly string SERTIFIKA_KEY_USAGE_KONTROLU = "SERTIFIKA_KEY_USAGE_KONTROLU";
                               
        public static readonly string ENCRYPTIONCERT_KEY_USAGE_KONTROLU = "ENCRYPTIONCERT_KEY_USAGE_KONTROLU";
                               
        //SertifikaExtendedKeyUsageKontrolcu
        public static readonly string SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU = "SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU";
                               
        public static readonly string EXTENDEDKEY_USAGE_YOK = "EXTENDED_KEY_USAGE_YOK";
        public static readonly string EXTENDEDKEY_USAGE_BOZUK = "EXTENDED_KEY_USAGE_BOZUK";
        public static readonly string EXTENDEDKEY_USAGE_GECERLI = "EXTENDED_KEY_USAGE_GECERLI";
                               
        public static readonly string KEY_USAGE_SIL_IMZALAYICI_DEGIL = "KEY_USAGE_SIL_IMZALAYICI_DEGIL";
        public static readonly string KEY_USAGE_SIL_IMZALAYICI = "KEY_USAGE_SIL_IMZALAYICI";
        public static readonly string SIL_KEY_USAGE_KONTROLU = "SIL_KEY_USAGE_KONTROLU";
                               
        public static readonly string OCSP_CEVABI_GECERSIZ = "OCSP_CEVABI_GECERSIZ";
        public static readonly string OCSP_CEVABI_BULUNAMADI = "OCSP_CEVABI_BULUNAMADI";
        public static readonly string SM_SERTIFIKASI_BULUNAMADI = "SM_SERTIFIKASI_BULUNAMADI";
        public static readonly string SERTIFIKA_OCSPDE_GECERLI = "SERTIFIKA_OCSPDE_GECERLI";
        public static readonly string SERTIFIKA_OCSPDE_GECERLI_DEGIL = "SERTIFIKA_OCSPDE_GECERLI_DEGIL";
        public static readonly string OCSPDEN_IPTAL_KONTROLU = "OCSPDEN_IPTAL_KONTROLU";
                               
        public static readonly string SERI_NO_POZITIF = "SERI_NO_POZITIF";
        public static readonly string SERI_NO_NEGATIF = "SERI_NO_NEGATIF";
        public static readonly string SERI_NO_KONTROLU = "SERI_NO_KONTROLU";
                               
        public static readonly string SERTIFIKA_EKLENTI_KONTROLU = "SERTIFIKA_EKLENTI_KONTROLU";
                               
        public static readonly string SERTIFIKA_TARIH_GECERLI = "SERTIFIKA_TARIH_GECERLI";
        public static readonly string SERTIFIKA_TARIH_GECERSIZ = "SERTIFIKA_TARIH_GECERSIZ";
        public static readonly string SERTIFIKA_TARIH_BILGISI_BOZUK = "SERTIFIKA_TARIH_BILGISI_BOZUK";
        public static readonly string SERTIFIKA_SURESI_DOLMUS = "SERTIFIKA_SURESI_DOLMUS";
                               
        public static readonly string SERTIFIKA_NAME_KONTROLU = "SERTIFIKA_NAME_KONTROLU";
                               
        public static readonly string SERTIFIKA_TARIH_KONTROLU = "SERTIFIKA_TARIH_KONTROLU";
                               
        public static readonly string SIGNATURE_ALG_FARKLI = "SIGNATURE_ALG_FARKLI";
        public static readonly string SIGNATURE_ALG_AYNIMI_KONTROLU = "SIGNATURE_ALG_AYNIMI_KONTROLU";
        public static readonly string SIGNATURE_ALG_AYNI = "SIGNATURE_ALG_AYNI";
                               
        public static readonly string SIL_EKLENTI_KONTROLU = "SIL_EKLENTI_KONTROLU";
                               
        public static readonly string SIL_NAME_KONTROLU = "SIL_NAME_KONTROLU";
                               
        public static readonly string SIL_TARIH_KONTROLU = "SIL_TARIH_KONTROLU";
                               
        public static readonly string SILDEN_IPTAL_KONTROLU = "SILDEN_IPTAL_KONTROLU";
                               
        public static readonly string KARTTA_0_ANAHTARI_YOK = "KARTTA_0_ANAHTARI_YOK";
                               
        public static readonly string ASN1_ENCODE_HATASI = "ASN1_ENCODE_HATASI";
                               
        public static readonly string DIGITALSIGNATURE = "DIGITALSIGNATURE";
        public static readonly string NONREPUDIATION = "NONREPUDIATION";
        public static readonly string KEYENCIPHERMENT = "KEYENCIPHERMENT";
        public static readonly string DATAENCIPHERMENT = "DATAENCIPHERMENT";
        public static readonly string KEYAGREEMENT = "KEYAGREEMENT";
        public static readonly string CERTSIGN = "CERTSIGN";
        public static readonly string CRLSIGN = "CRLSIGN";
        public static readonly string ENCIPHERONLY = "ENCIPHERONLY";
        public static readonly string DECIPHERONLY = "DECIPHERONLY";
                               
        // Path validaion resuls
        public static readonly string PVR_SUCCESS = "PVR_SUCCESS";
                               
        public static readonly string PVR_SERIALNUMBER_NOT_POSITIVE = "PVR_SERIALNUMBER_NOT_POSITIVE";
        public static readonly string PVR_CERTIFICATE_EXTENSIONS_FAILURE = "PVR_CERTIFICATE_EXTENSIONS_FAILURE";
        public static readonly string PVR_CERTIFICATE_EXPIRED = "PVR_CERTIFICATE_EXPIRED";
        public static readonly string PVR_SIGNATURE_ALGORITHM_DIFFERENT = "PVR_SIGNATURE_ALGORITHM_DIFFERENT";
        public static readonly string PVR_VERSION_CONTROL_FAILURE = "PVR_VERSION_CONTROL_FAILURE";
                               
        public static readonly string PVR_REVOCATION_CONTROL_FAILURE = "PVR_REVOCATION_CONTROL_FAILURE";
        public static readonly string PVR_CERTIFICATE_REVOKED = "PVR_CERTIFICATE_REVOKED";
                               
        public static readonly string PVR_SIGNATURE_CONTROL_FAILURE = "PVR_SIGNATURE_CONTROL_FAILURE";
        public static readonly string PVR_BASICCONSTRAINTS_FAILURE = "PVR_BASICCONSTRAINTS_FAILURE";
        public static readonly string PVR_CDP_CONTROL_FAILURE = "PVR_CDP_CONTROL_FAILURE";
        public static readonly string PVR_KEYID_CONTROL_FAILURE = "PVR_KEYID_CONTROL_FAILURE";
        public static readonly string PVR_NAMECONSTRAINTS_FAILURE = "PVR_NAMECONSTRAINTS_FAILURE";
        public static readonly string PVR_PATHLENCONSTRAINTS_FAILURE = "PVR_PATHLENCONSTRAINTS_FAILURE";
        public static readonly string PVR_POLICYCONSTRAINTS_CONTROL_FAILURE = "PVR_POLICYCONSTRAINTS_CONTROL_FAILURE.";
        public static readonly string PVR_KEYUSAGE_CONTROL_FAILURE = "PVR_KEYUSAGE_CONTROL_FAILURE";
        public static readonly string PVR_EXTENDED_KEYUSAGE_CONTROL_FAILURE = "PVR_EXTENDED_KEYUSAGE_CONTROL_FAILURE";
        public static readonly string PVR_NAME_CONTROL_FAILURE = "PVR_NAME_CONTROL_FAILURE";
                               
        public static readonly string PVR_CRL_EXPIRED = "PVR_CRL_EXPIRED";
        public static readonly string PVR_CRL_EXTENSIONS_CONTROL_FAILURE = "PVR_CRL_EXTENSIONS_CONTROL_FAILURE";
                               
        public static readonly string PVR_CRL_SIGNATURE_CONTROL_FAILURE = "PVR_CRL_SIGNATURE_CONTROL_FAILURE";
        public static readonly string PVR_CRL_KEYUSAGE_CONTROL_FAILURE = "PVR_CRL_KEYUSAGE_CONTROL_FAILURE";
        public static readonly string PVR_CRL_NAME_CONTROL_FAILURE = "PVR_CRL_NAME_CONTROL_FAILURE";
                               
        public static readonly string PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE = "PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE";
        public static readonly string PVR_OCSP_SIGNATURE_CONTROL_FAILURE = "PVR_OCSP_SIGNATURE_CONTROL_FAILURE";
        public static readonly string OCSP_RESPONSEDATE_EXPIRED = "OCSP_RESPONSEDATE_EXPIRED";
        public static readonly string OCSP_RESPONSEDATE_INVALID = "OCSP_RESPONSEDATE_INVALID";
        public static readonly string PVR_OCSP_DATE_CONTROL_FAILURE = "PVR_OCSP_DATE_CONTROL_FAILURE";
                               
        public static readonly string PVR_CRL_FRESHESTCRL_CONTROL_FAILURE = "PVR_CRL_FRESHESTCRL_CONTROL_FAILURE";
        public static readonly string PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE = "PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE";
                               
        public static readonly string PVR_POLICYMAPPING_CONTROL_FAILURE = "PVR_POLICYMAPPING_CONTROL_FAILURE";
                               
        public static readonly string PVR_INVALID_PATH = "PVR_INVALID_PATH";
        public static readonly string PVR_INCOMPLETE_VALIDATION = "PVR_INCOMPLETE_VALIDATION";
        public static readonly string PVR_UNSPECIFIED_FAILURE = "PVR_UNSPECIFIED_FAILURE";
                               
        public static readonly string PVR_NO_PATHFOUND = "PVR_NO_PATHFOUND";
                               
        public static readonly string PVR_QCC_NO_STATEMENT_ID = "Sertifikada nitelikli ibaresi bulunamadı.";
        public static readonly string PVR_QCC_NO_USER_NOTICE = "Sertifikada kullanıcı notu ibaresi bulunamadı.";
                               
        public static readonly string CERTIFICATE_VALIDATION_SUCCESSFUL = "CERTIFICATE_VALIDATION_SUCCESSFUL";
        public static readonly string CERTIFICATE_VALIDATION_UNSUCCESSFUL = "CERTIFICATE_VALIDATION_UNSUCCESSFUL";
        public static readonly string CERTIFICATE_NO_PATH_FOUND = "CERTIFICATE_NO_PATH_FOUND";
        public static readonly string CERTIFICATE_CHECKER_FAIL = "CERTIFICATE_CHECKER_FAIL";
        public static readonly string SERTIFIKA_NITELIKLI_KONTROLU_BASARILI = "SERTIFIKA_NITELIKLI_KONTROLU_BASARILI";
        public static readonly string SERTIFIKA_KEYUSAGE_HATALI = "SERTIFIKA_KEYUSAGE_HATALI";
        public static readonly string SERTIFIKA_NITELIKLI_IBARESI_YOK = "SERTIFIKA_NITELIKLI_IBARESI_YOK";
        public static readonly string SERTIFIKA_NITELIKLI_KONTROLU = "SERTIFIKA_NITELIKLI_KONTROLU";
        public static readonly string SERTIFIKA_KULLANICI_NOTU_YOK = "SERTIFIKA_KULLANICI_NOTU_YOK";
        public static readonly string IMZA_PAKETI_IMZA_KONTEYNERI_BULUNAMADI = "IMZA_PAKETI_IMZA_KONTEYNERI_BULUNAMADI";
        public static readonly string HATA_IMZA_PAKETI_DESTEKLENMIYOR = "HATA_IMZA_PAKETI_DESTEKLENMIYOR";
        public static readonly string HATA_IMZA_PAKETI_BILINMEYEN_UZANTI_EKSIK_MIMETYPE = "HATA_IMZA_PAKETI_BILINMEYEN_UZANTI_EKSIK_MIMETYPE";
        public static readonly string HATA_IMZA_PAKETI_UZANTI_MIMETYPE_UYUZMAZLIK = "HATA_IMZA_PAKETI_UZANTI_MIMETYPE_UYUZMAZLIK";
                               
        public static readonly string VALID_DATE_INFO = "VALID_DATE_INFO";
        public static readonly string INVALID_DATE_INFO = "INVALID_DATE_INFO";
        public static readonly string CORRUPT_DATE_INFO = "CORRUPT_DATE_INFO";

        public static readonly string SC_SELECTOR_NOT_GIVEN = "SC_SELECTOR_NOT_GIVEN";
        public static readonly string SELECT_SC = "SELECT_SC";

        public static readonly string WRONG_FORMAT_QCC_STATEMENT = "WRONG_FORMAT_QCC_STATEMENT";

        public static readonly string MESAJ_ESNEKZDYOK = "MESAJ_ESNEKZDYOK";

        public static string message(string aMessage)
        {
            return (MResources.GetString(aMessage, I18nSettings.getLocale()));
        }
        public static string message(string aMessage, object[] aArgs)
        {
            return string.Format(message(aMessage), aArgs);            
        }
    }
}

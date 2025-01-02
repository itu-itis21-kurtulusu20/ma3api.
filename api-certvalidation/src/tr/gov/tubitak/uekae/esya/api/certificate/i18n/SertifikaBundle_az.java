package tr.gov.tubitak.uekae.esya.api.certificate.i18n;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle_az;

public class SertifikaBundle_az extends GenelBundle_az
{
     private static Object[][] mSertifikaContents =
     {
          {CertI18n.TAB1,"Sertifikat"},
          {CertI18n.TAB2,"Genişləndirilmiş görünüş"},
          {CertI18n.TAB3,"Sertifikat zənciri"},

          {CertI18n.SERTIFIKA_DOSYA,"Sertifikat dosyeləri"},
          {CertI18n.DATE_AFTER,"Etibarlılığın başlanğıcı"},
          {CertI18n.DATE_BEFORE,"Etibarlılıq sonu"},
          {CertI18n.SERTIFIKA_GOSTER,"Sertifikatı göstər"},
          {CertI18n.BC_TYPE,"Subyekt növü"},
          {CertI18n.BC_CA,"Sertifikat orqanı"},
          {CertI18n.BC_CA_DEGIL,"Sertifikat orqanı deyil"},
          {CertI18n.BC_PATH,"Yol uzunluğunun məhdudlaşdırılması"},

          {CertI18n.POLICY_IDENTIFIER,"Prinsip identifikatoru"},
          {CertI18n.POLICY_ID,"Prinsip spesifikatorunun kimliyi"},
          {CertI18n.POLICY_INFO,"Prinsip spesifikatorunun məlumatı"},
          {CertI18n.POLICY_QUALIFIER,"Spesifikator"},
          {CertI18n.POLICY_NOTICE,"İstifadəçi bildirişi"},
          {CertI18n.POLICY_CPS,"CPS"},

          {CertI18n.AKI_ID,"Açar identifikatoru"},
          {CertI18n.AKI_ISSUER,"Səlahiyyətli istifadəçi sertifikatını verən"},
          {CertI18n.AKI_SERIAL,"Səlahiyyətli istifadəçi sertifikat seriya nömrəsi"},

          {CertI18n.PM_IDP,"Sertifikatı təmin edən sahə prinsipi"},
          {CertI18n.PM_SDP,"Mövzu sahə prinsipi"},

          {CertI18n.NITELIKLI_COMPLIANCE,"Bu Sertifikat, ETSI TƏSDIQ 101 862 standartına görə yaradılmış yüksək keyfiyyətli elektron sertifikatdır."},
          {CertI18n.NITELIKLI_TK,"Telekommunikasiya qurumu tərəfindən müəyyən edilmiş keyfiyyətli elektron sertifikat profilinə uyğundur."},
          {CertI18n.NITELIKLI_MONEY_LIMIT,"Pul Limiti"},

          {CertI18n.KULLANIM,"İstifadə Məqsədləri"},
          {CertI18n.BILGI,"Sertifikat Məlumatları"},
          {CertI18n.TIP,"Tip"},
          {CertI18n.ESHS,"ESHS"},
          {CertI18n.BASLANGIC_TARIHI,"Başlanğıc tarixi"},
          {CertI18n.BITIS_TARIHI,"Bitmə tarixi"},
          {CertI18n.IMZA_SERTIFIKASI,"İmza sertifikatı"},

          {CertI18n.KONTROL_SONUCU,"Yoxlamanın nəticəsi"},

          {CertI18n.DELTA_CRL_INDICATOR_KONTROLU,"Delta CRL identifikator nəzarəti"},
          {CertI18n.DELTA_CRL_INDICATOR_VAR,"Delta CRL identifikator var"},
          {CertI18n.DELTA_CRL_INDICATOR_YOK,"Delta CRL identifikatoru yoxdur"},

          {CertI18n.FRESHEST_CRL_KONTROLU,"Freshest CRL nəzarəti"},
          {CertI18n.FRESHEST_CRL_VAR,"Freshest CRL var"},
          {CertI18n.FRESHEST_CRL_YOK,"Freshest CRL yoxdur"},
          
          {CertI18n.SERTIFIKA_KEY_USAGE_KONTROLU,"Sertifikat açar istifadəsi aksesuarı nəzarəti"},

          {CertI18n.OCSP_CEVABI_KONTROLCU,"OCSP cavabının yoxlanılması"},

          {CertI18n.BASARILI,"Uğurlu"},
          {CertI18n.MALFORMED_REQUEST,"Malformed sorğusu"},
          {CertI18n.INTERNAL_ERROR,"Daxili xəta"},
          {CertI18n.TRY_LATER,"Bir azdan sınayın"},
          {CertI18n.SIG_REQUIRED,"Tələb olunan imza"},
          {CertI18n.UNAUTHORIZED,"Səlahiyyətsiz"},

          {CertI18n.OCSP_IMZALAYAN_SERTIFIKA_KONTROLU,"OCSP imzalayıcı sertifikat nəzarəti"},

          {CertI18n.IMZALAYAN_SERTIFIKA_GECERLI,"İmzalayan sertifikat etibarlı"},
          {CertI18n.IMZALAYAN_SERTIFIKA_GECERSIZ,"İmzalayan sertifikat etibarsız"},
          
          
          {CertI18n.ISSUER_SUBJECT_UYUMLU,"Sertifikatdakı verən ilə SM sertifikatındakı subyekt dəyərləri uyğundur"},


        //OCSPImzaKontrolcu
        {CertI18n.OCSP_IMZA_KONTROLU, "OCSP Response Signature Control"},

        // todo bol ceviri

    // path validation result
    {CertI18n.PVR_SUCCESS							, "Başarılı."},

    {CertI18n.PVR_SERIALNUMBER_NOT_POSITIVE		    , "Seri Numarası negatif."},
    {CertI18n.PVR_CERTIFICATE_EXTENSIONS_FAILURE	, "Sertifikat Eklenti nəzarəti başarısız."},
    {CertI18n.PVR_CERTIFICATE_EXPIRED				, "Sertifikat geçerlilik süresi dolmuş."},
    {CertI18n.PVR_SIGNATURE_ALGORITHM_DIFFERENT	    , "Sertifikat imza algoritması uyuşmuyor."},
    {CertI18n.PVR_VERSION_CONTROL_FAILURE			, "Sertifikat versiyon nəzarəti başarısız."},

    {CertI18n.PVR_REVOCATION_CONTROL_FAILURE		, "Sertifikat iptal nəzarəti başarısız."},
    {CertI18n.PVR_CERTIFICATE_REVOKED				, "Sertifikat iptal edilmiş."},

    {CertI18n.PVR_SIGNATURE_CONTROL_FAILURE		    , "Sertifikat İmza nəzarəti başarısız."},
    {CertI18n.PVR_BASICCONSTRAINTS_FAILURE			, "Sertifikat Temel Kısıtlamalar nəzarəti başarısız."},
    {CertI18n.PVR_CDP_CONTROL_FAILURE				, "Sertifikat SİL Dağıtım Noktaları nəzarəti başarısız."},
    {CertI18n.PVR_KEYID_CONTROL_FAILURE			    , "Sertifikat Anahtar Tanımlayıcısı nəzarəti başarısız."},
    {CertI18n.PVR_NAMECONSTRAINTS_FAILURE			, "Sertifikat İsim Kısıtlamaları nəzarəti başarısız."},
    {CertI18n.PVR_PATHLENCONSTRAINTS_FAILURE		, "Sertifikat Yol Uzunluğu nəzarəti başarısız."},
    {CertI18n.PVR_POLICYCONSTRAINTS_CONTROL_FAILURE	, "Sertifikat Politika Kısıtlamaları nəzarəti başarısız."},
    {CertI18n.PVR_KEYUSAGE_CONTROL_FAILURE			, "Sertifikat Anahtar Kullanımı nəzarəti başarısız."},
    {CertI18n.PVR_NAME_CONTROL_FAILURE				, "Sertifikat İsim kontrolü başarısız."},

    {CertI18n.PVR_CRL_EXPIRED						, "SİL geçerlilik süresi dolmuş."},
    {CertI18n.PVR_CRL_EXTENSIONS_CONTROL_FAILURE	, "SİL Eklentileri nəzarəti başarısız."},

    {CertI18n.PVR_CRL_SIGNATURE_CONTROL_FAILURE	    , "SİL İmza nəzarəti başarısız."},
    {CertI18n.PVR_CRL_KEYUSAGE_CONTROL_FAILURE		, "SİL Anahtar Kullanımı nəzarəti başarısız."},
    {CertI18n.PVR_CRL_NAME_CONTROL_FAILURE			, "SİL İsim nəzarəti başarısız."},

    {CertI18n.PVR_OCSP_RESPONSESTATUS_CONTROL_FAILURE  , "OCSP Cevap Durumu nəzarəti başarısız."},
    {CertI18n.PVR_OCSP_SIGNATURE_CONTROL_FAILURE	, "OCSP İmza nəzarəti başarısız."},
    {CertI18n.PVR_OCSP_RESPONSEDATE_EXPIRED 		, "OCSP tarixi eskimiş."},
    {CertI18n.PVR_OCSP_RESPONSEDATE_INVALID 		, "OCSP tarixi geçersiz."},

    {CertI18n.PVR_CRL_FRESHESTCRL_CONTROL_FAILURE	, "SİL En Güncel SİL Eklentisi nəzarəti başarısız."},
    {CertI18n.PVR_CRL_DELTACRLINDICATOR_CONTROL_FAILURE	, "SİL Delta Sil Belirteci Eklentisi nəzarəti başarısız."},

    {CertI18n.PVR_POLICYMAPPING_CONTROL_FAILURE		, "Sertifikat Politika Eşleştirmeleri nəzarəti uyğun deyil."},

    {CertI18n.PVR_INVALID_PATH						, "Geçersiz Sertifikat Zinciri."},
    {CertI18n.PVR_INCOMPLETE_VALIDATION			    , "Sertifikat Doğrulama tamamlanamadı."},
    {CertI18n.PVR_UNSPECIFIED_FAILURE				, "Tanımlanmamış hata."},

    {CertI18n.PVR_NO_PATHFOUND				        , "Geçerli zincir bulunamadı."},
    {CertI18n.WRONG_FORMAT_QCC_STATEMENT			, "Xml faylına daxil edilmiş qualified certificate checker parametrinin formatı səhvdir."}

     };

     private static Object[][] mContents = new Object[mGenelContents.length + mSertifikaContents.length][];

     static
     {
          System.arraycopy(mGenelContents, 0, mContents, 0, mGenelContents.length);
          System.arraycopy(mSertifikaContents, 0, mContents, mGenelContents.length, mSertifikaContents.length);
     }

     @Override
     public Object[][] getContents () //NOPMD
     {
          return mContents;
     }
}


package tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;

import java.util.ListResourceBundle;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS.*;


public class CmsSignatureBundle_tr extends ListResourceBundle {

	private Object[][] mContents = 
	{
		{ _0_MISSING_PARAMETER.name(),"{0} parametresi eksik." },
		{ _0_WRONG_PARAMETER_TYPE_1_.name(),"{0} parametresinin {1} tipinde olması gerekiyor."},

		// Archive Time Stamp  Attribute Checker Start
		{ NO_ARCHIVE_TSA_IN_SIGNEDDATA.name(),"Arşiv zaman damgası özelliği imza yapısında bulunamadı." },
		{ ARCHIVE_TSA_DECODE_ERROR.name(),"Arşiv zaman damgası özelliği decode edilemedi." },
		{ ARCHIVE_TSA_CHECK_UNSUCCESSFUL.name(),"Arşiv zaman damgası özelliği kontrolü başarısız." },		      
		{ ARCHIVE_TSA_CHECK_SUCCESSFUL.name(),"Arşiv zaman damgası özelliği kontrolü başarılı." },

		//Signing in two steps
		{ NO_UNFINISHED_SIGNATURE.name(),"Yarım kalmış imzalama işlemi bulunamadı." },
        { NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE.name(),"Verilen imza değeri yarım kalmış imza için geçerli değil." },
		
		//Signer
		{ PARENT_SIGNER_ESAv2.name(),"Üst imzacılardan biri arşiv zaman damgası V2 özelliği olan arşiv tipi imza." },
		{ PARENT_SIGNER_ESAv3.name(),"Üst imzacılardan biri arşiv zaman damgası V3 özelliği olan arşiv tipi imza." },

		// Archive Time Stamp V2 Attribute Checker Start
		{ NO_ARCHIVE_TSA_V2_IN_SIGNEDDATA.name(),"Arşiv zaman damgası V2 özelliği imza yapısında bulunamadı." },
		{ ARCHIVE_TSA_V2_DECODE_ERROR.name(),"Arşiv zaman damgası V2 özelliği decode edilemedi." },
		{ ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL.name(),"Arşiv zaman damgası V2 özelliği kontrolü başarısız." },		      
		{ ARCHIVE_TSA_V2_CHECK_SUCCESSFUL.name(),"Arşiv zaman damgası V2 özelliği kontrolü başarılı." },

		// Archive Time Stamp V3 Attribute Checker Start
		{ NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA.name(),"Arşiv zaman damgası V3 özelliği imza yapısında bulunamadı." },
		{ ARCHIVE_TSA_V3_DECODE_ERROR.name(),"Arşiv zaman damgası V3 özelliği decode edilemedi." },
		{ ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL.name(),"Arşiv zaman damgası V3 özelliği kontrolü başarısız." },		      
		{ ARCHIVE_TSA_V3_CHECK_SUCCESSFUL.name(),"Arşiv zaman damgası V3 özelliği kontrolü başarılı." },
		
		// CAsES_C Time Stamp Attribute Checker Start
		{ NO_CADESC_TSA_IN_SIGNEDDATA.name(),"CAdES_C zaman damgası özelliği imza yapısında bulunamadı." },
		{ CADESC_TSA_DECODE_ERROR.name(),"CAdES_C zaman damgası özelliği decode edilemedi." },
		{ CADESC_TSA_CHECK_UNSUCCESSFUL.name(),"CAdES_C zaman damgası özelliği kontrolü başarısız." },		      
		{ CADESC_TSA_CHECK_SUCCESSFUL.name(),"CAdES_C zaman damgası özelliği kontrolü başarılı." },

		//Certificate Checker Start
		{ NO_SIGNING_TIME.name(),"İmzalama zamanı alınırken hata oluştu." },
		{ CERTIFICATE_VALIDATION_SUCCESSFUL.name(),"İmzacı sertifikası doğrulaması başarılı." },
		{ CERTIFICATE_VALIDATION_UNSUCCESSFUL.name(),"İmzacı sertifikası doğrulaması başarısız. Doğrulama durumu: {0}" },
		{ CERTIFICATE_NO_PATH_FOUND.name(), "Güvendiğiniz bir sertifika zinciri oluşturulamadı. Sertifikanın kök sertifikası güvenilir sertifikalarınızdan biri olmayabilir."},
		{ CERTIFICATE_CHECKER_FAIL.name(), "{0} başarısız"},
		{ CERTIFICATE_REVOCATION_MAP_INCOMPLETE.name(), "Bazı referanslar veya değerler eksik"},
		
		//Certificate References Values Match Checker
		{ NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA.name(),"Sertifika referansları özelliği imza yapısında bulunamadı." },
		{ COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR.name(),"Sertifika referansları özelliği decode edilemedi." },
		{ NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA.name(),"Sertifika değerleri özelliği imza yapısında bulunamadı." },
		{ CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR.name(),"Sertifika değerleri özelliği decode edilemedi." },
		{ CertificateRefsValuesMatchChecker_UNSUCCESSFUL.name(),"Sertifika referansları ve değerleri eşleştirme kontrolü başarısız." },		      
		{ CertificateRefsValuesMatchChecker_SUCCESSFUL.name(),"Sertifika referansları ve değerleri eşleştirme kontrolü başarılı." },
		
		
		 //Check All Checker
		{  NO_MUST_ATTRIBUTE_IN_SIGNED_DATA.name(),"Zorunlu özellik imza yapısında bulunamadı."},
		{  NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA.name(),"Zorunlu olmayan özellik(CADES_C veya referans zaman damgası) imza yapısında bulunamadı."},
		{  ALL_CHECKERS_SUCCESSFULL.name(),"Kontrolcülerin sonuçları başarılı."},
		{  ALL_CHECKERS_UNSUCCESSFULL.name(),"Kontrolcülerin sonuçları başarısız."},

		//Check One Checker
		{ NO_CHECKER_SUCCESSFULL.name(),"Zorunlu kontrolcülerden hiç biri çalıştırılamadı."},
		
		//Content Timestamp Checker
		{ NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA.name(),"İçerik Zaman Damgası özelliği imza yapısında bulunamadı."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR.name(),"İçerik Zaman Damgası özelliği decode edilemedi."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"İçerik Zaman Damgası özelliği kontrolü başarılı."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"İçerik Zaman Damgası özelliği kontrolü başarısız."},
		
		//Content Type Checker
		{ NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA.name(),"İçerik Tipi özelliği bulunamadı."},
		{ CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR.name(),"İçerik Tipi özelliği decode edilemedi."},
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"İçerik Tipi özelliği kontrolcüsü başarılı."},
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"İçerik Tipi, EncapsulatedContentInfo daki içerik tipiyle aynı değil.Kontrolcü sonucu başarısız."},
		
		//Crypto Checker
		{ NO_SIGNER_CERTIFICATE_FOUND.name(),"İmza sertifikası bulunamadı."},
		{ SIGNED_ATTRIBUTES_ENCODE_ERROR.name(),"İmzalı özellikler encode edilirken hata oluştu."},
		{ SIGNATURE_VERIFICATION_ERROR.name(),"İmza matematiksel doğrulamada hata oluştu."},
		{ SIGNATURE_VERIFICATION_SUCCESSFUL.name(),"İmza matematiksel doğrulandı."},
		{ SIGNATURE_VERIFICATION_UNSUCCESSFUL.name(),"İmza matematiksel doğrulanamadı."},
		
		//MessageDigest Checker
		{ NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND.name(),"İçerik özeti özelliği bulunamadı."},
		{ MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR.name(),"İçerik özeti özelliği decode edilemedi."},
		{ SIGNER_DIGEST_ALGORITHM_UNKNOWN.name(),"İmzacı özet algoritması bilinmiyor."},
		{ MESSAGE_DIGEST_CHECKER_ERROR.name(),"İçerik özeti kontrolcüsünde hata oluştu."},
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"İçerik özeti özelliği kontrolcüsü başarılı."},
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"İçerik özeti özelliğindeki değer hesaplanan değerle aynı değil.Kontrolcü sonucu başarısız."},
		
		
		//RevocationRefsValuesMatchChecker
		{ REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND.name(),"İptal referansları özelliği imza yapısında bulunamadı."},
		{ REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR.name(),"İptal referansları özelliği decode edilemedi."},
		{ REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND.name(),"İptal değerleri özelliği imza yapısında bulunamadı."},
		{ REVOCATION_VALUES_DECODE_ERROR.name(),"İptal değerleri özelliği decode edilemedi."},
		{ REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL.name(),"İptal referansları ve değerleri eşleştirme kontrolü başarılı."},
		{ REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL.name(),"İptal referansları ve değerleri eşleştirme kontrolü başarısız.."},
		
		//Signature Policy Checker
		{ SIGNATURE_POLICY_ATTRIBUTE_NOT_FOUND.name(),"İmza politikası özelliği bulunamadı."},
		{ SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR.name(),"İmza politikası özelliği decode edilemedi."},
		{ SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR.name(),"İmza politikası kontrolünde özet işleminde hata oluştu."},
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"İmza politikası özelliğindeki özet değeri hesaplanan değerle aynı. Kontrolcü sonucu başarılı."},
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"İmza politikası özelliğindeki özet değeri hesaplanan değerden farklı.Kontrolcü sonucu başarısız."},
		{ SIGNATURE_POLICY_VALUE_NOT_FOUND.name(),"İmza politikası bulunamadı."},
		
		
		//SignatureTimeStamp Checker
		{ SIGNATURE_TS_NOT_FOUND.name(),"İmza zaman damgası özelliği bulunamadı."},
		{ SIGNATURE_TS_DECODE_ERROR.name(),"İmza zaman damgası özelliği decode edilemedi."},
		{ SIGNATURE_TS_CHECK_SUCCESSFUL.name(),"İmza zaman damgası özelliği kontrolü başarılı."},
		{ SIGNATURE_TS_CHECK_UNSUCCESSFUL.name(),"İmza zaman damgası özelliği kontrolü başarısız."},
		
		//SigningTime Checker
		{ SIGNING_TIME_ATTRIBUTE_DECODE_ERROR.name(),"Beyan edilen imza zamanı özelliği decode edilemedi."},
		{ SIGNING_TIME_CHECKER_SUCCESSFUL.name(),"Beyan edilen imza zamanı, zaman damgasındaki zamandan önce. Kontrolcü sonucu başarılı."},
		{ SIGNING_TIME_CHECKER_UNSUCCESSFUL.name(),"Beyan edilen imza zamanı, zaman damgasındaki zamandan sonra olamaz. Beyan edilen zaman: {0}. Zaman Damgası: {1}."},
		{ SIGNING_TIME_EXISTS.name(), "Beyan edilen zaman özelliği var"},
		{ NO_SIGNING_TIME_ATTRIBUTE.name(),"Beyan edilen imza zamanı özelliği bulunamadı."},
		
		
		//TS
		{ TS_DECODE_ERROR.name(),"Zaman damgası verisini çözmede hata."},
		
		//Timestamp Certificate Checker
		{ TS_CERTIFICATE_NOT_FOUND.name(),"Zaman damgası sunucu sertifikası bulunamadı."},
		{ TS_CERTIFICATE_NOT_QUALIFIED.name(),"Sertifika, zaman damgası sunucu sertifikası olamaz."},//TODO pek olmadı
		
		
		//TimestampedCertsCrlsRefsAttrChecker
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND.name(),"Referanslar zaman damgası özelliği bulunamadı."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR.name(),"Referanslar zaman damgası özelliği decode edilemedi."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Referanslar zaman damgası özelliği kontrolü başarılı."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Referanslar zaman damgası özelliği kontrolü başarısız."},
		
		//TimestampMessageDigest Checker
		{ TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR.name(),"Zaman damgasından TSTInfo yapısı decode edilemedi."},
		{ TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR.name(),"Zaman damgası özet değeri hesaplanırken hata oluştu."},
		{ TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL.name(),"Zaman damgası özet değeri kontrolü başarılı."},
		{ TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL.name(),"Zaman damgasından hesaplanan özet değeri ve imzadan hesaplanan özet değeri aynı değil.Kontrolcü sonucu başarısız."},
		
		
		//TimestampSignatureChecker
		{ TS_SIGNATURE_CHECKER_SUCCESSFUL.name(),"Zaman damgası imzası doğrulandı."},
		{ TS_SIGNATURE_CHECKER_UNSUCCESSFUL.name(),"Zaman damgası imzası doğrulanamadı."},
		
		
		 //TimestampTimeChecker
		{ TS_TIME_CHECKER_SIGNATURE_TS_NOT_FOUND.name(),"İmza zaman damgası özelliği bulunamadı."},
		{ TS_TIME_CHECKER_TIME_ERROR.name(),"Zaman damgasından zaman bilgisi alınırken hata oluştu."},
		{ TS_TIME_CHECKER_COMPARISON_ERROR.name(),"Zaman damgalarının, zaman sırası karşılaştırılırken hata oluştu."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_EST.name(),"Arşiv zaman damgasının tarihi, imza zaman damgasınınkinden önce."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC.name(),"Arşiv zaman damgasının tarihi, ESC zaman damgasınınkinden önce."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS.name(),"Arşiv zaman damgasının tarihi, Referanslar zaman damgasınınkinden önce."},
		{ TS_TIME_CHECKER_SUCCESSFUL.name(),"Zaman damgalarının tarihleri doğru sıralı."},
		{ TS_TIME_CHECKER_UNSUCCESSFUL.name(),"Zaman damgalarının tarihleri sıralı değil."},
		
		//SigningCertificate Attribute Checker
		{ SIGNING_CERTIFICATE_DECODE_ERROR.name(),"İmzacı sertifikası özelliği decode edilemedi."},
		{ ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER.name(),"Özellikteki issuer serial alanı, signerinfo daki signeridentifier alanı ile eşleşmiyor."},
		{ CERT_HASH_DOESNOT_MATCH.name(),"Özellikteki sertifika özet alanı, imzacı sertifikanın özetiyle aynı değil."},
		{ SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL.name(),"İmzacı sertifikası özelliği kontrolü başarılı."},
		{ SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR.name(),"İmzacı sertifikası özelliği özet kontrolünde hata."},
		{ ISSUER_SERIAL_DOESNOT_EXISTS.name(),"Özellik issuer serial alanı içermiyor."},
		
		//ParentSignatureChecker
		{ PARENT_SIGNATURE_INVALID.name(), "Ust imza dogrulanamadi"},
		{ PARENT_SIGNATURE_VALID.name(), "Ust imza kontrolu başarılı."},
		
		//ProfileRevocationValueMatcherChecker
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL.name(), "Profil ve iptal verisi uyumlu."},
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL.name(), "Profil ve iptal verisi uyumsuz."},
		{ PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL.name(), "Profil \"{0}\" ve imza tipi \"{1}\"."},
		{ PROFILE_P3_DOESNOT_USE_CRL.name(), "Profil P3 olmasına rağmen CRL kullanılmamış.."},
		{ PROFILE_P4_DOESNOT_USE_OCSP.name(),"Profil P4 olmasına rağmen OCSP kullanılmamış."},
		
		//ProfileAttributesChecker
		{ TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL.name(), "Profil özellikleri Türk standartlarına uygun."},
		{ NOT_A_TURKISH_PROFILE.name(), "Türk profillerinden biri değil.Herhangi bir kontrol yapılmadı."},
		{ PROFILE_POLICY_HASH_NOT_SHA256.name(), "Politika özet algoritması SHA-256 değil."},		
		{ SIGNING_TIME_ATTRIBUTE_MISSING.name(), "Beyan edilen zamanı özelliği bulunamadı."},
		{ SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING.name(), "İmzacı Sertifikası Özelliği V2 bulunamadı."},
		{ TS_TIME_NOT_AFTER_2H.name(), "İmza zaman damgası, beyan edilen zamandan sonraki 2 saat içinde alınmamış."},
		{ SIGNATURE_TIME_ERROR.name(), "İmzadan zaman bilgileri alınırken hata oluştu."},
		
		//ATSHashIndexAttrChecker
		{ ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL.name(), "Arşiv Zaman Damgası V3 Kapsam Kontrolcüsü başarılı."},
		{ UNSIGNED_ATTRIBUTE_NOT_INCLUDED.name(), "İmzasız özelliklerden biri arşiv zaman damgasına dahil değil."},
		{ UNSIGNED_ATTRIBUTE_MISSING.name(), "İmzasız özelliklerden biri çıkarılmış."},		
		
		//Attributes
		{UNKNOWN_COMMITMENT_TYPE.name(), "Bilinmeyen imza amacı."},
		
		//Checker names
		{ ARCHIVE_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"Arşiv Zaman Damgası Özelliği Kontrolcüsü" },
		{ ARCHIVE_TIMESTAMP_V2_ATTRIBUTE_CHECKER.name(),"Arşiv Zaman Damgası V2 Özelliği Kontrolcüsü" },
		{ ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER.name(),"Arşiv Zaman Damgası V3 Özelliği Kontrolcüsü" },		
		{ CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"CAdES_C Zaman Damgası Özelliği Kontrolcüsü" },
		{ CERTIFICATE_VALIDATION_CHECKER.name(),"Sertifika Geçerlilik Kontrolcüsü" },
		{ CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER.name(),"Sertifika Referans - Değer Uyumluluk Kontrolcüsü" },		      
		{ CHECK_ALL_CHECKER.name(),"Kontrolcü Topluluğu" },
		{ CHECK_ONE_CHECKER.name(),"Tek Kontrolcü" },
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"İçerik Zaman Damgası Özelliği Kontrolcüsü" },
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER.name(),"İçerik Tipi Özelliği Kontrolcüsü" },
		{ SIGNATURE_CHECKER.name(),"İmza Matematiksel Doğrulama Kontrolcüsü" },
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER.name(),"İçerik Özet Özelliği Kontrolcüsü" },
		{ REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER.name(),"İptal Bilgisi Referans - Değer Uyumluluk Kontrolcüsü" },
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER.name(),"İmza Politikası Özelliği Kontrolcüsü" },		      
		{ SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER.name(),"İmza Zaman Damgası Kontrolcüsü" },
		{ SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER.name(),"İmzacı Sertifikası Özelliği Kontrolcüsü" },
		{ SIGNING_CERTIFICATE_V2_ATTRIBUTE_CHECKER.name(),"İmzacı Sertifikası Özelliği V2 Kontrolcüsü" },
		{ SIGNING_TIME_CHECKER.name(),"Beyan Edilen Zaman Kontrolcüsü" },
		{ SIGNING_TIME_EXISTENCE_CHECKER.name(),"Beyan Edilen Zaman Varlığı Kontrolcüsü" },
		{ TIMESTAMP_CERTIFICATE_CHECKER.name(),"Zaman Damgası Sertifikası Kontrolcüsü" },
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER.name(),"Referanslar Zaman Damgası Özelliği Kontrolcüsü" },		      
		{ TIMESTAMP_MESSAGE_DIGEST_CHECKER.name(),"Zaman Damgası Özet Kontrolcüsü" },
		{ TIMESTAMP_SIGNATURE_CHECKER.name(),"Zaman Damgası İmza Kontrolcüsü" },
		{ TIMESTAMP_TIME_CHECKER.name(),"Zaman Damgası Zaman Kontrolcüsü" },
		{ PARENT_SIGNATURE_CHECKER.name(), "Üst İmzacı Kontrolcüsü"},
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER.name(), "Profil-İptal Bilgisi Uyumluluk Kontrolcüsü"},
		{ TURKISH_PROFILE_ATTRIBUTES_CHECKER.name(), "Türk Profilleri Özellikleri Kontrolcüsü"},
		{ ATS_HASH_INDEX_ATTRIBUTE_CHECKER.name(), "Arşiv Zaman Damgası V3 Kapsam Kontrolcüsü"},
		

		 //SignedDataValidationResult
		{ SIGNATURE_CHECKED_RESULTS.name(),". İmza kontrol edildi. \nSonuclar:\n"},
		{ SIGNER_CERTIFICATE.name(),"İmzacı Sertifika:"},
		
		//SignatureValidationResult
		{ COUNTER_SIGNATURE_VERIFICATION_RESULTS.name(),"Imzaya Ait Seri Imza Dogrulama Sonuclari:\n"},
		{ COUNTER_SIGNATURE_CHECKED.name(),". seri imza kontrol edildi:\n"},
		{ SIGNATURE_CHECKER_RESULTS.name(),"Imza Kontrolcu Sonuclari:\n"},
		{ PRE_VERIFICATION_DONE.name(),"Ön doğrulama yapılmıştır.\n"},
		
		{ SUB_CHECKER_RESULTS.name(),"\tAlt Kontrolcu Sonuclari:\n"},
		
		{ CERT_EXPIRED_ERROR_IN_TS.name(), "Sertifika süresi dolmuş. Bu imza için zaman damgası alınamıyor."},
		{ CONTENT_AND_SIGNER_DOESNT_MATCH.name(), "Eklenmek istenen veri ile imzacının imzaladığı veri uyuşmuyor."},
		{ EXTERNAL_CONTENT_CANT_ATTACH.name(), "İmzalanan içerik imzaya eklenemedi."},
		{ CONTENT_NULL.name(), "ContentInfo null."},

		{ CERTIFICATE_VALIDATION_EXCEPTION.name(), "Sertifika doğrulama problemi. "},
		
		  //VALIDATE_TIMESTAMP_WHILE_SIGNING
		{  SIGNATURE_TIMESTAMP_INVALID.name(),"Alınan imza zaman damgası geçersiz." },
		{  CADES_C_TIMESTAMP_INVALID.name(),"Alınan CaDES-C zaman damgası geçersiz." },
		{  CERTSCRLS_TIMESTAMP_INVALID.name(), "Alınan referans zaman damgası geçersiz."},
		{  ARCHIVE_TIMESTAMP_INVALID.name(),"Alınan arşiv zaman damgası geçersiz." },

			//Pades Signature
			{ ONLY_LAST_USER_SIGNATURE_CAN_BE_UPGRADED.name(), "Yalnızca son kullanıcı imzası dönüştürülebilir!"}
	};		  

	@Override
	protected Object[][] getContents() {
		return mContents;
	}

}

package tr.gov.tubitak.uekae.esya.api.common.bundle.cert;

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
    
    {CertI18n.OCSP_CEVABI_GECERSIZ , "OCSP Cevabı Geçersiz"},
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
    {CertI18n.BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS , "Base Silde Askıya Alınmış Sertifika Delta Silde Çıkarılmış"},
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
    {CertI18n.SERTIFIKA_SURESI_DOLMUS,"Sertifika süresi dolmuş"},
    
    
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
    
    
//    {CertI18n.KEY_USAGE_YOK , "Anahtar Kullanımı Eklentisi Yok"},
//    {CertI18n.KEY_USAGE_BOZUK , "Anahtar Kullanımı Eklentisi Yapısı Bozuk"},
//    {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL , "Anahtar Kullanımı Eklentisi Sertifika İmzalayıcı Değil"},
//    {CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI , "Anahtar Kullanımı Eklentisi Sertifika İmzalayıcı"},
    
    
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
	
    {CertI18n.GECERLI, "Sertifika Geçerli. "},
    {CertI18n.IPTAL_KONTROLU_SORUNLU,"İptal Kontrolü Sorunlu. "},
    {CertI18n.ZINCIR_SORUNLU, "Sertifika Zinciri Sorunlu. "},
    {CertI18n.IPTAL_EDILMIS, "Sertifika İptal Edilmiş. "},
    {CertI18n.ASKIDA, "Sertifika Askıda. "},
    	
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

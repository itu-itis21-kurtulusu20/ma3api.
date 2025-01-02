#ifndef  __ORTAK_DIL_H__
#define  __ORTAK_DIL_H__

//Dosya İşlemleri Ekran yazıları
#define DIL_DI_IMZALAMA							QObject::trUtf8("İmzalama")
#define DIL_DI_DOGRULAMA						QObject::trUtf8("İmza Doğrulama")
#define DIL_DI_SIFRELEME						QObject::trUtf8("Şifreleme")
#define DIL_DI_COZME							QObject::trUtf8("Şifre Çözme")
#define DIL_DI_DOSYASI_SIFRELENIYOR				QObject::trUtf8("dosyası şifreleniyor")
#define DIL_DI_DOSYASI_COZULUYOR				QObject::trUtf8("dosyası çözülüyor")
#define DIL_DI_DOSYASI_IMZALANIYOR				QObject::trUtf8("dosyası imzalanıyor")
#define DIL_DI_DOSYASI_IMZALANIP_SIFRELENIYOR	QObject::trUtf8("dosyası imzalanıp şifreleniyor")
#define DIL_DI_IMZALI_DOSYASI_DOGRULANIYOR		QObject::trUtf8("imzalı dosyası doğrulanıyor")
#define DIL_DI_DIZINI_SIFRELENIYOR				QObject::trUtf8("dizini şifreleniyor")
#define DIL_DI_DIZINI_IMZALANIYOR				QObject::trUtf8("dizini imzalanıyor")
#define DIL_DI_DIZINININ_SIFRESI_COZULUYOR      QObject::trUtf8("dizinin şifresi çözülüyor")
#define DIL_DI_DIZINININ_IMZASI_DOGRULANIYOR    QObject::trUtf8("dizinin imzası doğrulanıyor")


#define DIL_DI_DOSYA_PBE_SIFRELENIYOR		QObject::trUtf8("dosya parola tabalı şifreleniyor")
#define DIL_DI_DOSYA_SIFRELENIYOR			QObject::trUtf8("dosya şifreleniyor")
#define DIL_DI_DOSYA_IMZALANIYOR			QObject::trUtf8("dosya imzalanıyor")
#define DIL_DI_DOSYA_IMZALANIP_SIFRELENIYOR	QObject::trUtf8("dosya imzalanıp şifreleniyor")





#define DIL_DI_TAMAMLANDI					QObject::trUtf8("tamamlandı")
#define DIL_DI_OZET_ALINIYOR				QObject::trUtf8("Özet alınıyor")
#define DIL_DI_IMZALI_VERI_OLUSTURULUYOR	QObject::trUtf8("İmzalı veri oluşturuluyor")
#define DIL_DI_IMZALI_VERI_OKUNUYOR			QObject::trUtf8("İmzalı veri okunuyor")




//#define ESYADIL(x) QObject::trUtf8(x)
//Anahtar Kullanımı (Key Usage) ile ilgili 
#define DIL_KU_SAYISAL_IMZA_OLUSTURMA		 QObject::trUtf8("Sayısal İmza Oluşturma")
#define DIL_KU_ANAHTAR_SIFRELEME			 QObject::trUtf8("Anahtar Şifreleme")
#define DIL_KU_ANAHTAR_UYUSMASI				 QObject::trUtf8("Anahtar Uyuşması")
#define DIL_KU_INKAR_EDEMEMEZLIK			 QObject::trUtf8("İnkar Edememezlik")
#define DIL_KU_VERI_SIFRELEME				 QObject::trUtf8("Veri Şifreleme")
#define DIL_KU_SIL_IMZALAMA					 QObject::trUtf8("SİL İmzalama")
#define DIL_KU_SERTIFIKA_IMZALAMA			 QObject::trUtf8("Sertifika İmzalama")
#define DIL_KU_SADECE_SIFRELEME				 QObject::trUtf8("Sadece Şifreleme")
#define DIL_KU_SADECE_SIFRE_COZME			 QObject::trUtf8("Sadece Şifre Çözme")

//Sertifika Uzantıları (Extensions) ile ilgili
#define DIL_EXT_ANAHTAR_KULLANIMI				QObject::trUtf8("Anahtar Kullanımı")
#define DIL_EXT_GELISMIS_ANAHTAR_KULLANIMI		QObject::trUtf8("Gelişmiş Anahtar Kullanımı")
#define DIL_EXT_KULLANICI_SERTIFIKA_KODU		QObject::trUtf8("Kullanıcı Sertifika Kodu")
#define DIL_EXT_URETICI_SERTIFIKA_KODU			QObject::trUtf8("Üretici Sertifika Kodu")
#define DIL_EXT_SIL_DAGITIM_NOKTALARI			QObject::trUtf8("SİL Dağıtım Noktaları")
#define DIL_EXT_OZNE_TURU						QObject::trUtf8("Özne Türü")
#define DIL_EXT_YAYINCI							QObject::trUtf8("Yayıncı")
#define DIL_EXT_KULLANICI						QObject::trUtf8("Kullanıcı")
#define DIL_EXT_MESAFE_KISITLAMASI				QObject::trUtf8("Mesafe Kısıtlaması")
#define DIL_EXT_TEMEL_KISITLAR					QObject::trUtf8("Temel Kısıtlar")
#define DIL_EXT_BILINMEYEN_UZANTI				QObject::trUtf8("Bilinmeyen Uzantı")
#define DIL_EXT_YETKILI_ERISIM_NOKTALARI		QObject::trUtf8("Yetkili Erişim Noktaları")
#define DIL_EXT_ILKE_KISITLAMALARI				QObject::trUtf8("İlke Kısıtlamaları")
#define DIL_EXT_ISIM_KISITLAMALARI				QObject::trUtf8("İsim Kısıtlamaları")
#define DIL_EXT_ILKE_ESLESTIRMELERI				QObject::trUtf8("İlke Eşleştirmeleri")
#define DIL_EXT_SERTIFIKA_ILKELERI				QObject::trUtf8("Sertifika İlkeleri")
#define DIL_EXT_YETKILI_ANAHTAR_TANIMLAYICISI	QObject::trUtf8("Yetkili Anahtar Tanımlayıcısı")
#define DIL_EXT_OZNE_ANAHTAR_TANIMLAYICISI		QObject::trUtf8("Özne Anahtar Tanımlayıcısı")
#define DIL_EXT_OZNE_DIGER_ADI					QObject::trUtf8("Özne Diğer Adı")
#define DIL_EXT_YETKILI_DAGITIM_NOKTASI			QObject::trUtf8("Yetkili Dağıtım Noktası")
#define DIL_EXT_SIL_NUMARASI					QObject::trUtf8("SİL Numarası")
#define DIL_EXT_INHIBIT_ANY_POLICY				QObject::trUtf8("AnyPolicy Yasaklığı")


//Sertifika üzerindeki alanlar
#define DIL_SERT_SERTIFIKA						QObject::trUtf8("Sertifika")
#define DIL_SERT_SERTIFIKA_AC					QObject::trUtf8("Sertifika Aç")
#define DIL_SERT_SERTIFIKALAR					QObject::trUtf8("Sertifikalar")
#define DIL_SERT_SERTIFIKA_ZINCIRI				QObject::trUtf8("Sertifika Zinciri")
#define DIL_SERT_SERTIFIKADAKI_ALANLAR			QObject::trUtf8("Sertifikadaki Alanlar")
#define DIL_SERT_ALAN_ICERIGI				QObject::trUtf8("Alan İçeriği")
#define DIL_SERT_SERTIFIKA_SAHIBI			QObject::trUtf8("Sertifika Sahibi")
#define DIL_SERT_GECERLILIK_BASLANGICI		QObject::trUtf8("Geçerlilik Başlangıcı")
#define DIL_SERT_GECERLILIK_SONU			QObject::trUtf8("Geçerlilik Sonu")
#define DIL_SERT_URETICI_ESHS				QObject::trUtf8("Üretici ESHS")
#define DIL_SERT_SERI_NUMARASI				QObject::trUtf8("Seri Numarası")
#define DIL_SERT_SURUM						QObject::trUtf8("Sürüm")
#define DIL_SERT_IMZA_ALGORITMASI			QObject::trUtf8("İmza Algoritması")
#define DIL_SERT_ACIK_ANAHTAR				QObject::trUtf8("Açık Anahtar")
#define DIL_SERT_SERTIFIKAYI_KAYDET			QObject::trUtf8("Sertifikayı Kaydet")
#define DIL_SERT_SERTIFIKA_DOSYASI_KAYDEDILDI QObject::trUtf8("Sertifika Dosyası Kaydedildi")
#define DIL_SERT_SERTIFIKAYI_DOSYAYA_YAZMA_HATASI QObject::trUtf8("Sertifikayı Dosyaya Yazma Hatası")
#define DIL_SERT_SERTIFIKA_YASAMI			QObject::trUtf8("Sertifika Yaşamı")
#define DIL_SERT_SERTIFIKA_GECERLI			QObject::trUtf8("Sertifika Geçerli")
#define DIL_SERT_SERTIFIKA_GECERSIZ			QObject::trUtf8("Sertifika Geçersiz")

//CRL Üzerindeki bazı alanlar
#define DIL_CRL_SIL_GORUNTULEYICI			QObject::trUtf8("SİL Görüntüleyici")
#define DIL_CRL_SERTIFIKA_IPTAL_LISTESI_BILGISI QObject::trUtf8("Sertifika İptal Listesi  Bilgisi")
#define DIL_CRL_IPTAL_EDILMIS_SERTIFIKALAR	QObject::trUtf8("İptal Edilmiş Sertifikalar")
#define DIL_CRL_SIL_NUMARASI				QObject::trUtf8("SİL Numarası")
#define DIL_CRL_YAYINCI_ANAHTAR_BELIRTECI	QObject::trUtf8("Yayıncı Anahtar Belirteci")
#define DIL_CRL_ANAHTAR_ID					QObject::trUtf8("Anahtar ID")
#define DIL_CRL_IPTAL_TARIHI				QObject::trUtf8("İptal Tarihi")
#define DIL_CRL_SERI_NUMARASI				QObject::trUtf8("Seri Numarası")
#define DIL_CRL_IPTAL_NEDENI				QObject::trUtf8("İptal Nedeni")
#define DIL_CRL_DEGERI						QObject::trUtf8("Değeri")
#define DIL_CRL_IPTAL_KAYDI					QObject::trUtf8("İptal Kaydı")
#define DIL_CRL_SERTIFIKA_GORUNTULE			QObject::trUtf8("Sertifika Görüntüle")
#define DIL_CRL_YAYINCI_SERTIFIKASI_BULUNAMADI QObject::trUtf8("Yayıncı sertifikası bulunamadı")
#define DIL_CRL_YAYINCI_SERTIFIKASI_DEPODAN_ALINIRKEN_HATA_OLUSTU QObject::trUtf8("Yayıncı sertifikası depodan alınırken hata oluştu")
#define DIL_CRL_IPTAL_EDILEN_SERTIFIKALARIN_BILGISI_OKUNUYOR QObject::trUtf8("İptal edilen sertifikaların bilgisi okunuyor.")
#define DIL_CRL_SIL_ICINDE_ARA		QObject::trUtf8("SİL İçinde Ara")
#define DIL_CRL_IPTAL_LISTESI_ICINDE_ARAMA QObject::trUtf8("SİL(Sertifika İptal Listesi) İçinde Arama")
#define DIL_CRL_SERTIFIKA_NUMARASI_GIRINIZ QObject::trUtf8("Sertifika Seri Numarasını Giriniz (0x001F şeklinde)")
#define DIL_CRL_SERTIFIKA_IPTAL_EDILMIS_SERI_NO_1 QObject::trUtf8("Sertifika iptal edilmiş. Seri No : %1")
#define DIL_CRL_SERTIFIKA_SIL_ICERISINDE_BULUNAMADI QObject::trUtf8("Sertifika SİL içerisinde bulunamadı")


//Genel Kelimeler
#define DIL_GNL_YOK							QObject::trUtf8("Yok")
#define DIL_GNL_KAYDET						QObject::trUtf8("Kaydet")
#define DIL_GNL_AYRINTILAR					QObject::trUtf8("Ayrıntılar")
#define DIL_GNL_BILGI						QObject::trUtf8("Bilgi")
#define DIL_GNL_TAMAM						QObject::trUtf8("Tamam")
#define DIL_GNL_HATA						QObject::trUtf8("Hata")
#define DIL_GNL_DETAY						QObject::trUtf8("Detay")
#define DIL_GNL_ADI							QObject::trUtf8("Adı")
#define DIL_GNL_URETICI						QObject::trUtf8("Üretici")
#define DIL_GNL_BASLANGIC_TARIHI			QObject::trUtf8("Başlangıç Tarihi")
#define DIL_GNL_BITIS_TARIHI				QObject::trUtf8("Bitiş Tarihi")
#define DIL_GNL_SERI_NO						QObject::trUtf8("Seri No")
#define DIL_GNL_BILINMEYEN_SERI_NO			QObject::trUtf8("Bilinmeyen Seri No")
#define DIL_GNL_KULLANIM_AMACLARI			QObject::trUtf8("Kullanım Amaçları")
#define DIL_GNL_KAYIT_YERI_VE_ADI			QObject::trUtf8("Kayıt Yeri Ve Adı")
#define DIL_GNL_KAYIT_TIPI					QObject::trUtf8("Kayıt Tipi")
#define DIL_GNL_LUTFEN						QObject::trUtf8("Lütfen")
#define DIL_GNL_KART						QObject::trUtf8("Kart")
#define DIL_GNL_KART_OKUYUCU				QObject::trUtf8("Kart Okuyucu")
#define DIL_GNL_GENEL						QObject::trUtf8("Genel")
#define DIL_GNL_GELISMIS					QObject::trUtf8("Gelişmiş")
#define DIL_GNL_BASIT						QObject::trUtf8("Basit")
#define DIL_GNL_1_DAKIKA					QObject::trUtf8("%1 Dakika")
#define DIL_GNL_1_SANIYE					QObject::trUtf8("%1 Saniye")
#define DIL_GNL_1_DOSYA						QObject::trUtf8("%1 Dosya")
#define DIL_GNL_1_DIZIN						QObject::trUtf8("%1 Dizin")
#define DIL_GNL_UZERINDE_ISLEM_YAPILIYOR	QObject::trUtf8(" üzerinde işlem yapılıyor.")
#define DIL_GNL_UYARI						QObject::trUtf8("Uyarı")
#define DIL_GNL_SURUM						QObject::trUtf8("Sürüm")
#define DIL_GNL_1_KULLANICISININ_IMZALAMA_SERTFIKASI_BULUNAMADI QObject::trUtf8("%1 kullanıcısının sertifikası sistemde bulunamadı.")
#define DIL_GNL_ALAN						QObject::trUtf8("Alan")
#define DIL_GNL_DEGER						QObject::trUtf8("Değer")
#define DIL_GNL_PAROLA						QObject::trUtf8("Parola")
#define DIL_GNL_TEKRAR						QObject::trUtf8("Tekrar")


#define DIL_GNL_AD							QObject::trUtf8("Ad")
#define DIL_GNL_SOYAD						QObject::trUtf8("Soyad")
#define DIL_GNL_E_POSTA						QObject::trUtf8("E-Posta")
#define DIL_GNL_BOLUM						QObject::trUtf8("Bölüm")
#define DIL_GNL_GRUP						QObject::trUtf8("Grup")
#define DIL_GNL_KAYIT_ADI					QObject::trUtf8("Kayıt Adı")
#define DIL_GNL_GRUP_ADI					QObject::trUtf8("Grup Adı")
#define DIL_GNL_EKLEME_CIKARMA_HATASI       QObject::trUtf8("Kişi Ekleme/Çıkarma Hatası")
#define DIL_GNL_ILK_ONCE_LISTEDEN_SECIM_YAPINIZ		QObject::trUtf8("İlk önce listeden seçim yapınız")
#define DIL_GNL_BU_EKRANI_BIR_DAHA_GOSTERME QObject::trUtf8("Bu ekranı bir daha gösterme")
#define DIL_GNL_DOSYALARIN_OLUSTURULACAGI_DIZINI_SECIN QObject::trUtf8("Dosyaların oluşturulacağı dizini seçiniz")
#define DIL_GNL_YANLIS_SIFRE_GIRDINIZ_YADA_DOSYA_HATALI  QObject::trUtf8("Yanlış şifre girdiniz ya da dosya hatalı")
#define DIL_GNL_LUTFEN_TEKRAR_DENEYINIZ		QObject::trUtf8("Lütfen tekrar deneyiniz")



//Dosya işlemleri ile ilgili uyarı,bilgi ve hatalar
#define DIL_DSY_DOSYAYA_YAZMA_HATASI		QObject::trUtf8("Dosyaya Yazma Hatası")
#define DIL_DSY_DOSYA_ACMA_HATASI			QObject::trUtf8("Dosya Açma Hatası")
#define DIL_DSY_ACMAYA_CALISTIGINIZ_DOSYA_SU_ANDA_ACIK QObject::trUtf8("Açmaya çalıştığınız dosya şu anda açık")
#define DIL_DOSYA_ADI_1						QObject::trUtf8("Dosya Adı %1")
#define DIL_DSY_BILINMEYEN_DOSYA_FORMATI	QObject::trUtf8("Bilinmeyen dosya formatı")
#define DIL_DSY_ACMAYA_CALISTIGINIZ_DOSYA_UZERINDE_SU_ANDA_ISLEM_YAPILIYOR QObject::trUtf8("Açmaya çalıştığınız dosya üzerinde şu anda işlem yapılıyor.")
#define DIL_DSY_EVETI_SECERSENIZ_UZERINDE_CALISTIGINIZ_DOSYA_BOZULABILIR QObject::trUtf8("Evet'i seçerseniz üzerinde çalıştığınız dosya bozulabilir.")
#define DIL_DSY_DEVAM_ETMEK_ISTIYORMUSUNUZ QObject::trUtf8("Devam etmek istiyor musunuz ?")
#define DIL_DSY_BU_DOSYA_BASKA_BIR_KULLANICI_TARAFINDAN_ACILMIS_DURUMDADIR QObject::trUtf8("Bu dosya başka bir kullanıcı tarafından açılmış durumdadır.")
#define DIL_DSY_GECICI_DOSYA_GERI_YAZILIRKEN_HATA QObject::trUtf8("Geçici Dosya: %1\nOrijinal Dosya: %2\nGeçici dosya, orijinal dosya üzerine geri taşınmak için imzalanırken bir hata oluşmuştur.\nDosyanızda yapmış olduğunuz değişiklikler yukarıda belirtilen geçici dosyada kayıtlıdır.\nAyrıntılı bilgi için sistem yöneticinize başvurunuz\n")

#define DIL_GUI_SERTIFIKA_GORUNTULEYICI		QObject::trUtf8("Sertifika Görüntüleyici")

//Sertifika Sihirbazı ile ilgili olan GUI Labelleri
#define DIL_SSIHIRBAZ_SERTIFIKA_SIHIRBAZI	QObject::trUtf8("Sertifika Sihirbazı")

//KULLANICI LOGIN EKRANI İLE İLGİLİ İFADELER
#define DIL_LOGIN_KULLANICI_GIRISI						  QObject::trUtf8("KULLANICI GİRİŞİ")
#define DIL_LOGIN_BILINMEYEN_KULLANICI_YA_DA_YANLIS_SIFRE QObject::trUtf8("Bilinmeyen Kullanıcı Adı ya da Yanlış Şifre.")
#define DIL_LOGIN_KULLANICI_ADINIZI_VE_SIFRENIZI_GIRINIZ  QObject::trUtf8("Kullanıcı Adınızı ve Şifrenizi Giriniz.")
#define DIL_LOGIN_KULLANICI_ADI_VE_SIFRE_BOS_OLAMAZ		  QObject::trUtf8("Kullanıcı Adı Ve Şifre Boş Olamaz")
#define DIL_LOGIN_SIFRE_BOS_OLAMAZ                        QObject::trUtf8("Şifre Boş Olamaz.")
#define DIL_LOGIN_KULLANICI_ADI							  QObject::trUtf8("Kullanıcı Adı")
#define DIL_LOGIN_SIFRE                                   QObject::trUtf8("Şifre")  

//PAROLA GİRİŞ EKRANI İLE İLGİLİ TERİMLER
#define DIL_PRL_PAROLA_GIRINIZ									QObject::trUtf8("Parola Giriniz.")
#define DIL_PRL_PAROLA_GIRISI									QObject::trUtf8("PAROLA GİRİŞİ")
#define DIL_PRL_PAROLA_DEGISTIRME								QObject::trUtf8("PAROLA DEĞİŞTİRME")
#define DIL_PRL_ESKI_VE_YENI_PAROLAYI_GIRINIZ					QObject::trUtf8("Eski ve Yeni Parolayı Giriniz.")
#define DIL_PRL_PAROLA_ALANLARI_BOS_OLAMAZ						QObject::trUtf8("Parola Alanları Boş Olamaz.")
#define DIL_PRL_PAROLA_GECERSIZ									QObject::trUtf8("Parola Geçersiz")
#define DIL_PRL_YENI_PARALOLAR_AYNI_OLMALI						QObject::trUtf8("Yeni Parolalar Aynı Olmalı")
#define DIL_PRL_PAROLA_GECERLI									QObject::trUtf8("Parola Geçerli")
#define DIL_PRL_ESKI_PAROLA										QObject::trUtf8("Eski Parola")
#define DIL_PRL_YENI_PAROLA										QObject::trUtf8("Yeni Parola")
#define DIL_PRL_YENI_PAROLA_TEKRAR								QObject::trUtf8("Yeni Parola(Tekrar)")

//PAROLA PRENSIPLERIYLE ILGILI ALANLAR.
#define  DIL_PRENSIP_PAROLA_PRENSIPLERI							QObject::trUtf8("Parola Prensipleri")
#define  DIL_PRENSIP_PAROLADA_EN_AZ_1_SAYI_OLMALIDIR			QObject::trUtf8("Parolada en az %1 sayı olmalıdır.")
#define  DIL_PRENSIP_PAROLADA_EN_AZ_1_BUYUK_HARF_OLMALIDIR    QObject::trUtf8("Parolada en az %1 büyük harf olmalıdır.") 
#define  DIL_PRENSIP_PAROLADA_EN_AZ_1_KUCUK_HARF_OLMALIDIR    QObject::trUtf8("Parolada en az %1 küçük harf olmalıdır.")
#define  DIL_PRENSIP_PAROLA_EN_AZ_1_KARAKTER_ICERMELIDIR        QObject::trUtf8("Parola en az %1 karakter içermelidir.")
#define  DIL_PRENSIP_PAROLADA_EN_AZ_1_ALFANUMERIK_OLMAYAN_KARAKTER_OLMALIDIR QObject::trUtf8("Parolada en az %1 Alfanümerik olmayan karakter olmalıdır.")

//Parola değiştirme işlemi ile ilgili ifadeler
#define DIL_PAROLA_DEGISTIRME_BU_KULLANICIYA_ILISKIN_IMZALAMA_SERTIFIKASI_BULUNAMADI QObject::trUtf8("Bu kullanıcıya ilişkin imzalama sertifikası bulunamadı.")
#define DIL_PAROLA_DEGISTIRME_BU_KULLANICI_KART_KULLANILARAK_ILKLENDIRILMEMIS QObject::trUtf8("Bu kullanıcı kart kullanılarak ilklendirilmemiş")
#define DIL_PAROLA_DEGISTIRME_KART_PAROLASI_DEGISTIRILIYOR_LUTFEN_BEKLEYINIZ QObject::trUtf8("Kart Parolası değiştiriliyor.\nLütfen Bekleyiniz...")
#define DIL_PAROLA_DEGISTIRME_KART_PAROLASI_BASARIYLA_DEGISTIRILDI QObject::trUtf8("Kart parolası başarıyla değiştirildi.")
#define DIL_PAROLA_DEGISTIRME_LART_PAROLASI_DEGISTIRILIRKEN_HATA_OLUSTU QObject::trUtf8("Kart parolası değiştirilirken hata oluştu")


//KULLANICI VE SERTIFIKA ARAMA EKRANI İLE İLGİLİ ETİKETLER
#define DIL_SERT_ARAMA_SERTIFIKA_ARAMA QObject::trUtf8("Sertifika Arama")
#define DIL_SERT_ARAMA_BULMAK_ISTEDIGINIZ_KULLANICIYA_ILISKIN_ARAMA_KRITERLERINI_GIRINIZ QObject::trUtf8("Bulmak istediğiniz kullanıcıya ilişkin arama kriterlerini giriniz")
#define DIL_SERT_ARAMA_ARAMA_SONUCU										QObject::trUtf8("Arama Sonucu")
#define DIL_SERT_ARAMA_BULUNAN_KULLANICILAR								QObject::trUtf8("Bulunan Kullanıcılar")
#define DIL_SERT_ARAMA_EKLENECEK_KULLANICILAR							QObject::trUtf8("Eklenecek Kullanıcılar")
#define DIL_SERT_ARAMA_ARAMA_KRITERLERI									QObject::trUtf8("Arama Kriterleri")
#define DIL_SERT_ARAMA_BELIRTILEN_LDAPLARDAN_HICBIRINE_BAGLANILAMADI	QObject::trUtf8("Belirtilen LDAP'lardan hiçbirine bağlanılamadı")
#define DIL_SERT_ARAMA_ARAMA_SIRASINDA_HATA_OLUSTU						QObject::trUtf8("Arama sırasında hata oluştu")
#define DIL_SERT_ARAMA_EN_AZ_BIR_ARAMA_KRITERI_GIRINIZ					QObject::trUtf8("En az bir arama kriteri giriniz.")
#define DIL_SERT_ARAMA_EKLENMEYE_CALISILAN_SERTIFIKA_GECERSIZ                QObject::trUtf8("Eklenmeye çalışılan sertifika geçersiz")
#define DIL_SERT_ARAMA_EKLENMEYE_CALISILAN_SERTIFIKADA_EPOSTA_BILGISI_YOK	 QObject::trUtf8("Eklenmeye çalışılan sertifikada eposta bilgisi yok.")
#define DIL_SERT_ARAMA_SIFRELEME_IMZALAMA_ISLEMLERI_SIRASINDA_SORUN_OLUSTURUR QObject::trUtf8("İmzalama,şifreleme işlemleri sırasında sorun oluşturur.")

//Butonların üzerindeki yazılar
#define DIL_BTN_TAMAM											QObject::trUtf8("TAMAM")
#define DIL_BTN_IPTAL											QObject::trUtf8("İPTAL")
#define DIL_BTN_DEGISTIR										QObject::trUtf8("DEĞİŞTİR")
#define DIL_BTN_DEVAM											QObject::trUtf8("DEVAM")
#define DIL_BTN_ARA												QObject::trUtf8("ARA")
#define DIL_BTN_TEMIZLE											QObject::trUtf8("Temizle")
#define DIL_BTN_GIRIS											QObject::trUtf8("GİRİŞ")
#define DIL_BTN_AYRINTI											QObject::trUtf8("Ayrıntı")
#define DIL_BTN_ARASTIR											QObject::trUtf8("Araştır")
#define DIL_BTN_ELEMANLARI_GOSTER								QObject::trUtf8("Elemanları Göster")
#define DIL_BTN_GOSTER QObject::trUtf8("Göster")
#define DIL_BTN_EKLE QObject::trUtf8("Ekle")
#define DIL_BTN_SIL  QObject::trUtf8("Sil")
#define DIL_BTN_KONTROL QObject::trUtf8("Kontrol")
#define DIL_BTN_ILERI QObject::trUtf8("İleri")
#define DIL_BTN_GERI QObject::trUtf8("Geri")
#define DIL_BTN_SON  QObject::trUtf8("Son")
#define DIL_BTN_GOZAT QObject::trUtf8("Gözat")
#define DIL_BTN_YENIDEN_DENE QObject::trUtf8("Yeniden Dene")

//Seçenekler Ekranındaki Yazılar
#define DIL_SCNK_SECENEKLER										QObject::trUtf8("Dosya İşlemi Seçenekleri")
#define DIL_SCNK_ASIL_DOKUMANI_GUVENLI_SIL						QObject::trUtf8("Asıl dökümanı güvenli sil")
#define DIL_SCNK_DOKUMANLARIN_OZELLIKLERINI_KORU				QObject::trUtf8("Döküman özelliklerini koru")
#define DIL_SCNK_ALT_DIZINLERE_DE_AYNI_ISLEMI_UYGULA			QObject::trUtf8("Alt dizinlere de aynı işlemi uygula")
#define DIL_SCNK_DOKUMANI_GUVENLI_DIZINDE_OLUSTUR QObject::trUtf8("Dökümanı güvenli dizinde oluştur")

#define DIL_SCNK_HEDEF_KAYNAK_AYNI_OLUSTUR	QObject::trUtf8("Hedefi Kaynak ile aynı yere oluştur")
#define DIL_SCNK_VARSAYILAN_YAP	QObject::trUtf8("Varsayılan Yap")

//Şifreleme ekranı ile ilgili ayarlar
#define DIL_SIF_EKR_SECMIS_OLDUGUNUZ___KISI_VE_GRUPLARI_SECINIZ QObject::trUtf8("")
#define DIL_SIF_EKR_SIFRELI_DOKUMANI_GUVENLI_DIZINDE_OLUSTUR QObject::trUtf8("Şifreli dökümanı güvenli dizinde oluştur")
#define DIL_SIF_EKR_EGER_YENI_KISI__BASINIZ QObject::trUtf8("Eğer yeni bir kişi eklemek istiyorsanız  <B> Ekle  </B> butonuna basınız")
#define DIL_SIF_EKR_SERTIFIKA_GORUNTULENMEYE_CALISILIRKEN_HATA_OLUSTU QObject::trUtf8("Sertifika görüntülenmeye çalışılırken hata oluştu")







//Esya_Consol a gönderilecek komutlardaki parametre setleri
//Şimdilik burda belirlendi daha sonra başka bir yere taşınacak.
//<TODO> Komut parametreleri başka bir yere taşınacak
#define DIL_KMT_SIL_GUNCELLE			 QObject::trUtf8("-silg")
#define DIL_KMT_KULLANICI_DEGISTIR		 QObject::trUtf8("-kuld")
#define DIL_KMT_SECENEKLER				 QObject::trUtf8("-sec")
#define DIL_KMT_HAKKINDA				 QObject::trUtf8("-hak")
#define DIL_KMT_DOSYA_BILGISI			 QObject::trUtf8("-dbilgi")
#define DIL_KMT_GRUPLARI_DUZENLE		 QObject::trUtf8("-grupd")
#define DIL_KMT_GRUBA_SIFRELE			 QObject::trUtf8("-grubasif")

#define DIL_KMT_DOSYA_AC				QObject::trUtf8("-dosyaac")
#define DIL_KMT_IMZALA					 QObject::trUtf8("-imz")
#define DIL_KMT_SIFRELE					 QObject::trUtf8("-sif")
#define DIL_KMT_SIFRE_COZ                QObject::trUtf8("-scoz")
#define DIL_KMT_IMZALA_SIFRELE			 QObject::trUtf8("-imzsif")
#define DIL_KMT_KENDINE_IMZALA_SIFRELE	 QObject::trUtf8("-kimzsif")
#define DIL_KMT_GRUBA_IMZALA_SIFRELE	 QObject::trUtf8("-gimzsif")
#define DIL_KMT_PAROLA_TABANLI_SIFRELE	 QObject::trUtf8("-ptabsif")
#define DIL_KMT_PAROLA_TABANLI_SIFRE_COZ QObject::trUtf8("-ptabscoz")
#define DIL_KMT_GUVENLI_SIL				 QObject::trUtf8("-guvsil")
#define DIL_KMT_GUVENLI_COP_KUTUSUNA_AT	 QObject::trUtf8("-guvcopat")

#define DIL_KMT_IMZA_ONAYLA				 QObject::trUtf8("-ionay")
#define DIL_KMT_SIFRE_COZ_IMZA_ONAYLA    QObject::trUtf8("-scozionay")

#define DIL_KMT_USER_CERT				QObject::trUtf8("-ucert")
#define DIL_KMT_USER_FILE				QObject::trUtf8("-ufile")//Şifrelenecek,şifrelenmiş,imzalanacak ya da imzalanmiş dosya yolu
#define DIL_KMT_USER_FOLDER				QObject::trUtf8("-ufolder")//Şifrelenecek,şifrelenmiş,imzalanacak ya da imzalanmiş dizin yolu
#define DIL_KMT_GRUP_ADI				 QObject::trUtf8("-grupadi")
#define DIL_KMT_SIFRE_COZ_BASKA_SIFRELE QObject::trUtf8("-sCozBaskaSif")
#define DIL_KMT_LOG_EKRAN_GOSTER QObject::trUtf8("-log")
#define DIL_KMT_KULLANICI_SIFRELENMEMIS_DOSYALARI_GOSTER QObject::trUtf8("-kulNoSifDosyaGoster")
#define DIL_KMT_SURUM_BILGISI QObject::trUtf8("-surumBilgisi")
//////////////////////////////////////////////////////////////////////////
#define DIL_KMT_KULLANICI_LISTELE	QObject::trUtf8("-kullis")

//////tamamen komut satirindan calisacak komutlar
#define DIL_CMD_C_SIFRELE	QObject::trUtf8("-csif")
#define DIL_CMD_C_SIFRE_COZ	QObject::trUtf8("-cscoz")
#define DIL_CMD_C_IMZALA	QObject::trUtf8("-cimz")
#define DIL_CMD_C_PBE_SIFRELE	QObject::trUtf8("-cptabsif")
#define DIL_CMD_C_PBE_SIFRE_COZ QObject::trUtf8("-cptabscoz")
#define DIL_CMD_C_ESKI_PBE_COZ QObject::trUtf8("-ceskiptabscoz")
#define DIL_CMD_C_GUVENLI_COP_AT QObject::trUtf8("-cguvcopat")
#define DIL_CMD_C_GUVENLI_SIL	QObject::trUtf8("-cguvsil")
#define DIL_CMD_C_SIL_GUNCELLE QObject::trUtf8("-csilg")
#define DIL_CMD_C_SURUM_BILGISI QObject::trUtf8("-csurumBilgisi")
#define DIL_CMD_C_SERTIFIKA_DOGRULAMA QObject::trUtf8("-csertifikaDogrula")
#define DIL_CMD_C_DOSYA_SIFRELEYENLER QObject::trUtf8("-cdosyaSifreleyenler")
//////////////////////////////////////////////////////////////////////////
#define DIL_KMT_BILINMEYEN_KOMUT		QObject::trUtf8("Bilinmeyen Komut")

//Imzalama ile ilgili mesajlar ve progress'e yazılan text ler
#define DIL_IMZ_1_DOSYASI_IMZALANIYOR			QObject::trUtf8("%1 dosyası imzalanıyor.")
#define DIL_IMZ_IMZALAMA_ISLEMI_YAPILIYOR		QObject::trUtf8("İmzalama işlemi yapılıyor.")
#define DIL_IMZ_IMZALAMA_ISLEMI_SONA_ERDI		QObject::trUtf8("İmzalama işlemi sona erdi.")
#define DIL_IMZ_IMZALAMA_SIRASINDA_HATA_OLUSTU  QObject::trUtf8("İmzalama sırasında hata oluştu")
#define DIL_IMZ_SORUN_AKILLI_KARTINIZDAN_YADA_PAROLANIZDAN_KAYNAKLANIYOR_OLABILIR QObject::trUtf8("Sorun akıllı kartınızdan ya da parolanızdan kaynaklanıyor olabilir")
#define DIL_IMZ_IMZALAMA_SERTIFIKASI_GECERLI_DEGIL QObject::trUtf8("İmzalama sertifikası geçerli değil")

#define  DIL_IMZ_IMZA_DOGRULAMA_ISLEMI_SONA_ERDI QObject::trUtf8("İmza doğrulama işlemi sona erdi")
#define  DIL_IMZ_IMZA_DOGRULAMA_ISLEMI_YAPILIYOR QObject::trUtf8("İmza doğrulama işlemi yapılıyor.")
#define  DIL_IMZ_IMZA_DOGRULAMA_SIRASINDA_HATA_OLUSTU QObject::trUtf8("İmza doğrulama sırasında hata oluştu.")
#define  DIL_IMZ_DOSYANIN_IMZASI_DOGRULANAMADI QObject::trUtf8("Dosyanın imzası doğrulanamadı")
//Şifreleme ile ilgili mesajlar
#define DIL_SIF_1_DOSYASI_SIFRELENIYOR			QObject::trUtf8("%1 dosyası şifreleniyor.")
#define DIL_SIF_SIFRELEME_ISLEMI_YAPILIYOR		QObject::trUtf8("Şifreleme işlemi yapılıyor.")
#define DIL_SIF_SIFRELEME_ISLEMI_SONA_ERDI		QObject::trUtf8("Şifreleme işlemi sona erdi.")
#define DIIL_SIF_SIFRELEME_ISLEMI_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Şifreleme işlemi sırasında hata oluştu")
#define DIL_SIF_HICBIR_SIFRELEME_SERTIFIKASI_SECILMEDI	QObject::trUtf8("Hiçbir şifreleme sertifikası seçilmedi")
#define DIL_SIF_SECILEN_SERTIFIKALARDAN_HICBIRI_GECERLI_DEGIL QObject::trUtf8("Seçilen sertifikalardan hiçbiri geçerli değil.")
#define DIL_SIF_SERTIFIKASI_GECERSIZ_OLAN_KULLANICILAR QObject::trUtf8("Sertifikası geçersiz olan kullanıcılar")
#define DIL_SIF_BU_KULLANICILAR_ICIN_SIFRELEME_YAPILMADI QObject::trUtf8("Bu kullanıcılar için şifreleme yapılmadı")
#define DIL_SIF_KULLANICININ_SIFRELEME_SERTIFIKASI_GECERLI_DEGIL QObject::trUtf8("Kullanıcının şifreleme sertifikası geçerli değil")
#define DIL_SIF_KULLANICININ_SIFRELEME_SERTIFIKASI_BULUNAMADI QObject::trUtf8("Kullanıcının şifreleme sertifikası bulunamadı")

#define DIL_SIF_EKLEDIGINIZ_GRUP_YADA_GRUPLARDA_HICBIR_KULLANICI_KAYITLI_DEGILDIR QObject::trUtf8("Seçtiğiniz grup yada gruplarda hiçbir kullanıcı kayıtlı değildir.")
#define DIL_SIF_SECENEKLER_EKRANINI_KULLANARAK_GRUP_AYARLARINIZI_YAPABILIRSINIZ QObject::trUtf8("Seçenekler ekranını kullanarak grup ayarlarını yapabilirsiniz.")
#define DIL_SIFRELEME_SIRASINDA_SORUN_OLUSTU QObject::trUtf8("Şifreleme sırasında sorun oluştu")

//Şifre çözme ile ilgili mesajlar
#define DIL_SIF_SIFRE_COZME_ISLEMI_YAPILIYOR	QObject::trUtf8("Şifre çözme işlemi yapılıyor")
#define DIL_SIF_SIFRE_COZME_ISLEMI_SONA_ERDI	QObject::trUtf8("Şifre çözme işlemi sona erdi.")
#define DIL_SIF_DOSYA_BILINMEYEN_KULLANICILAR_ICIN_SIFRELENMIS_SAG_TIKLAYIP_SIFRESINI_COZMEYI_DENEYIN QObject::trUtf8("Dosya bilinmeyen kullanıcılar için şifrelenmiş.Sağ tıklayıp şifresini çözmeyi deneyin")
#define DIL_SIF_1_DOSYASININ_SIFRESI_COZULURKEN_HATA_OLUSTU QObject::trUtf8("%1 dosyasının şifresi çözülürken hata oluştu.")
#define DIL_SIF_BU_DOSYA_SIZIN_ICIN_SIFRELENMEMIS QObject::trUtf8("Bu dosya sizin için şifrelenmemiş")
#define DIL_SIF_SIFRE_COZME_HATASI					QObject::trUtf8("Şifre çözme hatası")
#define DIL_SIF_COZME_ANAHTAR_BILGISI_OKUNURKEN_SORUN_OLUSTU QObject::trUtf8("Anahtar bilgisi okunurken sorun oluştu.")


//Parola tabanlı şifreleme ile ilgili mesajlar
#define DIL_PTAB_PAROLA_TABANLI_SIFRELENIYOR	QObject::trUtf8("Parola tabanlı şifreleniyor")
#define DIL_PTAB_PAROLA_TABANLI_SIFRELEME_SONA_ERDI	QObject::trUtf8("Parola tabanlı şifreleme sona erdi.")
#define DIL_PTAB_PAROLA_TABANLI_SIFRELEME_SIRASINDA_SORUN_OLUSTU QObject::trUtf8("Parola tabanlı şifreleme sırasında hata oluştu")
#define DIL_PTAB_SIFRE_YANLIS_YADA_DOSYA_BOZUK_OLABILIR QObject::trUtf8("Şifre yanlış yada dosya bozuk olabilir")
#define DIL_PTAB_PAROLA_TABANLI_SIFRE_COZULUYOR QObject::trUtf8("Parola tabanlı şifre çözülüyor")
#define DIL_PTAB_PAROLA_TABANLI_SIFRE_COZME_SONA_ERDI QObject::trUtf8("Parola tabanlı şifre çözme sona erdi")
#define DIL_PTAB_SIFRE_COZME_HATASI				QObject::trUtf8("Parola tabanlı şifre çözme hatası")

//Imzalama&Şifreleme ile ilgili mesajlar
#define DIL_ISIF_1_DOSYASI_IMZALANIP_SIFRELENIYOR	QObject::trUtf8("%1 dosyası imzalanıp && şifreleniyor")
#define DIL_ISIF_IMZALA_SIFRELE_ISLEMI_YAPILIYOR    QObject::trUtf8("İmzala && Şifrele işlemi yapılıyor.")
#define DIL_ISIF_IMZALA_SIFRELE_ISLEMI_SONA_ERDI	QObject::trUtf8("İmzala &&  Şifrele işlemi sona erdi.")
#define DIL_ISIF_IMZALA_SIFRELE_ISLEMI_SIRASINDA_HATA_OLUSTU QObject::trUtf8("İmzala &&  Şifrele işlemi sırasında hata oluştu")

//Şifre Çözme & Imza Doğrula ile ilgili başlıklar
#define DIL_SIDOG_1_DOSYASININ_SIFRESI_COZULUP_DOGRULANIYOR QObject::trUtf8("%1 dosyasının şifresi çözülüp imzası doğrulanıyor")
#define DIL_SIDOG_SIFRE_COZ_IMZA_DOGRULA_ISLEMI_YAPILIYOR QObject::trUtf8("Şifre Çöz && İmza doğrula işlemi yapılıyor")
#define DIL_SIDOG_SIFRE_COZ_IMZA_DOGRULA_ISLEMI_SONA_ERDI QObject::trUtf8("Şifre Çöz && İmza doğrula işlemi sona erdi")
#define DIL_SIDOG_SIFRE_COZ_IMZA_DOGRULA_ISLEMI_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Şifre Çöz && İmza doğrula işlemi sırasında hata oluştu")


//Kartla ilgili işlemler için
#define DIL_KART_PAROLASI_YANLIS_YADA_KARTA_ERISILEMIYOR QObject::trUtf8("Kart parolası yanlış ya da karta erişilemiyor")
#define DIL_KART_PAROLA_DOLDU_DEGISTIRMEK_ISTERMISINIZ QObject::trUtf8("Akıllı kart parolanızın geçerlilik süresi dolmuştur. Parolanızı değiştirmek ister misiniz?")
#define DIL_KART_AKILLI_KARTINIZIN_PAROLASINI_DEGISTIRMENIZ_GEREKIYOR QObject::trUtf8("Akıllı kart parolanızı değiştirmeniz gerekiyor..")
#define DIL_KART_PAROLANIZIN_SURESININ_DOLMASINA_1_GUN_KALDI_DEGISTIRMEK_ISTERMISINIZ QObject::trUtf8("Parolanızın geçerlilik süresinin bitmesine %1 gün kaldı.\n Parolanızı değiştirmek ister misiniz.?")

//Dosya özellikleri ekranı ile ifadeler
#define DIL_OZ_EKRAN_MGM_BILGISI QObject::trUtf8("MGM Bilgisi")
#define DIL_OZ_EKRAN_DOSYADAKI_IMZALAMA_VE_SIFRELEME_BILGISI QObject::trUtf8("Dosyadaki imzalama ve şifreleme bilgisi")
#define DIL_OZ_EKRAN_SIFRELENENLER QObject::trUtf8("Şifrelenenler")
#define DIL_OZ_EKRAN_IMZALAYANLAR  QObject::trUtf8("İmzalayanlar")
#define DIL_OZ_EKRAN_KULLANICI	QObject::trUtf8("Kullanıcı")
#define DIL_OZ_EKRAN_1_KISI_TARAFINDAN_IMZALANMIS QObject::trUtf8("%1 Kişi tarafından imzalanmış")
#define DIL_OZ_EKRAN_1_KISI_ICIN_SIFRELENMIS QObject::trUtf8("%1 Kişi için şifrelenmiş")
#define DIL_OZ_EKRAN_BU_SERI_NUMARALI_KISILERIN__ULASILAMADI QObject::trUtf8("Bu seri numaralı sertifika sahibi hakkındaki bilgilere ulaşılamadı")

//Hakkında ekrani ile ifadeler
#define DIL_HAKKINDA_MA3 QObject::trUtf8("MA3 ( Milli Açık Anahtar Altyapısı)")
#define DIL_HAKKINDA_KERMEN_MGM	QObject::trUtf8("KERMEN MGM")
#define DIL_HAKKINDA_AYRINTILI_BILGI_ICIN_KULLANICI_KLAVUZUNA_BAKINIZ QObject::trUtf8("Ayrıntılı bilgi için kullanıcı kılavuzuna bakınız")
#define DIL_HAKKINDA_TUM_HAKLARI_TUBITAK_UEKAE_YE_AITTIR QObject::trUtf8("Tüm hakları TÜBİTAK-BİLGEM'e aittir .(C)2011")

//Kullanıcı değiştirme ekranı ile ilgili ifadeler
#define DIL_KULL_DEG_AKTIF_KULLANICI_1	QObject::trUtf8("Aktif Kullanıcı : %1" )
#define DIL_KULL_DEG_SU_ANDA_MEVCUT_KULLANICILARIN_LISTESINDEN_ISMINIZI_SECINIZ QObject::trUtf8("Şu anda mevcut kullanıcıların listesinden isminizi seçiniz")
#define DIL_KULL_DEG_MEVCUT_KULLANICILAR	QObject::trUtf8("Mevcut Kullanıcılar")
#define DIL_KULL_DEG_KULLANICI_DEGISTIRILIRKEN_HATA_OLUSTU QObject::trUtf8("Kullanıcı değiştirilirken hata oluştu")
#define DIL_KULL_DEG_BTN_KULLANICI_SIL QObject::trUtf8("Kullanıcı Sil")
#define DIL_KULL_DEG_1_KULLANICISINI_SILMEK_ISTEDIGINIZDEN_EMINMISINIZ QObject::trUtf8("%1 Kullanıcısını silmek istediğinizden emin misiniz?")
#define DIL_KULL_DEG_1_KULLANICISI_SILINDI QObject::trUtf8("%1 Kullanıcısı silindi.")
#define DIL_KULL_DEG_1_KULLANICISI_SILINIRKEN_HATA_OLUSTU QObject::trUtf8("%1 Kullanıcısı silinirken hata oluştu.")

//SERTİFİKA YARDIMCISI ile ilgili ifadeler
#define DIL_SERT_YARD_SERTIFIKA_YARDIMCISI	 QObject::trUtf8("Sertifika Yardımcısı")
#define DIL_SERT_YARD_LUTFEN_DOSYA_ISMI_GIRINIZ	QObject::trUtf8("Lütfen geçerli bir dosya seçiniz")
#define DIL_SERT_YARD_LUFFEN_PAROLAYI_GIRINIZ   QObject::trUtf8("Lütfen Parola Giriniz")
#define DIL_SERT_YARD_PAROLA_HATALI_YADA_EKSIK_GIRDINIZ QObject::trUtf8("Parola hatalı ya da eksik girdiniz.")
#define DIL_SERT_YARD_1_KART_ALGILANAMADI_YADA_TAKILI_DEGIL	QObject::trUtf8("%1 Kart algılanamadı ya da takılı değil.")
#define DIL_SERT_YARD_LUTFEN_KONTROL_ETTIKTEN_SONRA_TEKRAR_DENEYINIZ QObject::trUtf8("Lütfen kontrol ettikten sonra tekrar deneyiniz")
#define DIL_SERT_YARD_SERTIFIKALAR_KARTAN_YUKLENIRKEN_HATA_OLUSTU QObject::trUtf8("Sertifikalar kartan yüklenirken hata oluştu.")
#define DIL_SERT_YARD_DEPO_PAROLASI_GIRISI QObject::trUtf8("Depo Parolası Girişi")
#define DIL_SERT_YARD_DEPO_PAROLASI_GIRINIZ	QObject::trUtf8("Depo Parolası Giriniz")
#define DIL_SERT_YARD_SERTIFIKALAR_PFX_DOSYASINDAN_YUKLENIRKEN_HATA_OLUSUTU QObject::trUtf8("Sertifikalar PFX Dosyasından yüklenirken hata oluştu")
#define DIL_SERT_YARD_KULLANICININ_VARSAYILAN_DIZINI_OLUSTURULAMADI QObject::trUtf8("Kullanıcının varsayılan dizini oluşturulamadı.")
#define DIL_SERT_YARD_SERTIFIKALAR_DEPOYA_YUKLENIRKEN_SORUN_OLUSTU QObject::trUtf8("Sertifikalar depoya yüklenirken sorun oluştu")
#define DIL_SERT_YARD_SERTIFIKALARINIZDA_E_POSTA_BILGISI_BULUNAMADIGINDAN_ILKLENDIRME_YAPILAMADI QObject::trUtf8("Sertifikalarınızda E-Posta bilgisi bulunamadığından ilklendirme yapılamadı")
#define DIL_SERT_YARD_HAKKINDA	QObject::trUtf8("Hakkında")
#define DIL_SERT_YARD_HOSGELDINIZ QObject::trUtf8("Sertifika Yardımcısı'na Hoşgeldiniz")
#define DIL_SERT_YARD_BU_PROGRAMI_KULLANARAK__KULLANABILIRSINIZ QObject::trUtf8("Bu programı kullanarak sertifikalarınızı bilgisayara aktarabilirsiniz. Bu sayede elektronik imzalama ve şifreleme hizmetlerini bu bilgisayarda kullanabilirsiniz")
#define DIL_SERT_YARD_DEVAM_ETMEK_ICIN_ILERI_TUSUNA_BASINIZ  QObject::trUtf8("Devam etmek için lütfen ileri tuşuna basınız...")
#define DIL_SERT_YARD_KULLANICI_TIPI_SECIMI QObject::trUtf8("Kullanıcı Tipi Seçimi")
#define DIL_SERT_YARD_LUTFEN_KULLANICI_TIPINI_SECINIZ QObject::trUtf8("Lütfen Kullanıcı Tipinizi Seçiniz")
#define DIL_SERT_YARD_MIKROSOFT_DEPO_EKLE QObject::trUtf8("Sertifikalarını Mikrosoft deposuna da ekle")
#define DIL_SERT_YARD_FIREFOX_DEPO_EKLE QObject::trUtf8("Sertifikalarını Firefox deposuna da ekle")
#define DIL_SERT_YARD_KARTLI QObject::trUtf8("Kartlı")
#define DIL_SERT_YARD_KARTSIZ QObject::trUtf8("Kartsız")
#define DIL_SERT_YARD_AKILLI_KART_SECIMI QObject::trUtf8("Akıllı Kart Seçimi")
#define DIL_SERT_YARD_LUTFEN_AKILLI_KARTINIZIN_MARKASINI_SECINIZ QObject::trUtf8("Lütfen akıllı kartınızın markasını seçiniz.")
#define DIL_SERT_YARD_LUTFEN_AKILLI_KARTINIZIN_TAKILI_OLDUGU__SECINIZ QObject::trUtf8("Lütfen akıllı kartınızın takılı olduğu kart okuyucuyu seçiniz")
#define DIL_SERT_YARD_KART_OKUYUCU QObject::trUtf8("Kart Okuyucu")
#define DIL_SERT_YARD_KART_OKUYUCU_SECIMI QObject::trUtf8("Kart okuyucu seçimi")
#define DIL_SERT_YARD_PFX_DOSYASININ_YOLU QObject::trUtf8("PFX Dosyasının Yolu")
#define DIL_SERT_YARD_LUTFEN_SERTIFIKA__PFX__SECINIZ  QObject::trUtf8("Lütfen sertifika ve anahtarınızın saklı olduğu pfx uzantılı dosyanın yolunu gösteriniz.")
#define DIL_SERT_YARD_PFX_DOSYASINI_SECINIZ QObject::trUtf8("PFX Dosyasını seçiniz")
#define DIL_SERT_YARD_PFX_DOSYASININ_PAROLASININ_GIRILMESI QObject::trUtf8("PFX Dosyasının Parolasının Girilmesi")
#define DIL_SERT_YARD_LUTFEN_PFX_DOSYASININ_PAROLASINI_GIRINIZ QObject::trUtf8("Lütfen pfx dosyasının parolasını giriniz.")
#define DIL_SERT_YARD_ISLEMLER_BASARIYLA_TAMAMLANDI QObject::trUtf8("İşlemler başarıyla tamamlandı")
#define DIL_SERT_YARD_SISTEME_TANITILDINIZ QObject::trUtf8("Sisteme Tanıtıldınız...") 
#define DIL_SERT_YARD_ARTIK_SIFRELEME_VE_ELEKTRONIK__FAYDANALABILECEKSINIZ QObject::trUtf8("Artık şifreleme ve elektronik imzalama hizmetlerinden bu bilgisayarda faydalanabileceksiniz.")
#define DIL_SERT_YARD_SERTIFIKALARINIZ_YUKLENIYOR__BEKLEYINIZ QObject::trUtf8("Sertifikalarınız sisteme yükleniyor.\nLütfen bekleyiniz.")
#define DIL_SERT_YARD_WEBDEN_SERTIFIKA_AL QObject::trUtf8("Web'den sertifika al")
#define DIL_SERT_YARD_KARTLA_BERABER_PFX_VERILDI_MI QObject::trUtf8("Kartla beraber PFX dosyası verildi mi ?")
#define DIL_SERT_YARD_EVET QObject::trUtf8("Evet Verildi")
#define DIL_SERT_YARD_HAYIR QObject::trUtf8("Hayır Verildi")
#define DIL_SERT_YARD_ESKI_SERTIFIKA_ANAHTAR_AKTARIMI QObject::trUtf8("Eski Sertifika-Anahtar Aktarımı")





//Uzerine yazma ekranı ile ilgili ifadeler
#define DIL_UZYAZEKR_1_TARIHINDE_DEGISTIRILMIS_OLAN_2_DOSYASI_ZATEN_VAR		QObject::trUtf8("Bilgisayarınızda %1 tarihinde değiştirilmiş olan %2 dosyası zaten var.")
#define DIL_UZYAZEKR_UZERINE_YAZMAK_ISTEGINIZDEN_EMINMISINIZ					QObject::trUtf8(" Bu dosyanın üzerine yazmak istediğinizden emin misiniz?")
#define DIL_UZYAZEKR_DOSYAYI_BASKA_DIZINDE_OLUSTURMAK_ISTIYORSANIZ_ASAGIDAKI_KUTUYA_ISMINI_YAZINIZ  QObject::trUtf8("Dosyayı başka bir dizinde oluşturmak istiyorsanız aşağıdaki kutuya yazınız")
#define DIL_UZYAZEKR_HEDEF_DIZINDE_1_ADLI_DIZIN_ZATEN_VAR								QObject::trUtf8("Hedef dizinde %1 adlı dizin zaten var.")
#define DIL_UZYAZ_EKR_UZERINE_YAZ	QObject::trUtf8("Üzerine Yaz")
#define DIL_UZYAZ_SU_ANDA_ACMAYA_CALISTIGINIZ_DOSYA_EN_SON_1_TARIHINDE_DEGISTIRILMISTIR QObject::trUtf8("Şu anda açmaya çalıştığınız dosya en son %1 tarihinde değiştirilmiştir.")

//Grup düzenleme ekranı ile ilgili ifadeler
#define DIL_GRPDUZENLE_YENI_GRUP_ADINI_GIRINIZ QObject::trUtf8("Yeni grup adını giriniz")
#define DIL_GRPDUZENLE_YENI_GRUP_ADI_GIRISI	    QObject::trUtf8("Yeni grup adı girişi")
#define DIL_GRPDUZENLE_SECILI_GRUBU_SILMEK_ISTEDIGINIZDEN_EMINMISINIZ QObject::trUtf8("Seçili grubu silmek istediğinizden emin misiniz?")
#define DIL_GRPDUZENLE_GRUP_ADI_1				QObject::trUtf8("Grup Adı : %1")
#define DIL_GRPDUZENLE_1_GRUBU_SILINMEYE_CALISILIRKEN_HATA_OLUSTU QObject::trUtf8("%1 Grubu silinmeye çalışırken hata oluştu")
#define DIL_GRPDUZENLE_YENI_GRUP_ADI QObject::trUtf8("Yeni Grup Adı")
#define DIL_GRP_GRUP_DUZENLEME QObject::trUtf8("Grup düzenleme")
#define DIL_GRP_GRUP_DUZENLE_LDAP_GRUBU QObject::trUtf8("LDAP Grubu")

//MGM hakkında genel ifadeler
#define DIL_MGM_HICBIR_KULLANICI_ILKLENDIRMESI_YAPILMAMIS QObject::trUtf8("Hiçbir kullanıcı ilklendirmesi yapılmamış")
#define DIL_MGM_SERTIFIKA_YARDIMCISINI_KULLANARAK_ILKLENDIRME_YAPINIZ QObject::trUtf8("Sertifika yardımcısını kullanarak ilklendirme yapınız")
#define DIL_MGM_SIL_GUNCELLENDI	QObject::trUtf8("SİL Güncellendi.")
#define DIL_MGM_SIL_GUNCELLEME_SIRASINDA_SORUN_OLUSTU QObject::trUtf8("SİL Güncelleme sırasında sorun oluştu")
//Kullanıcı secme ekranı ile ilgili	ifadeler
#define DIL_KUL_SEC_KULLANICI_SECME QObject::trUtf8("Kullanıcı Seçme")
#define DIL_KUL_SEC_VAROLAN_LISTEDEN_VEYA__EKLEYEBILIRSINIZ QObject::trUtf8("Varolan listeden veya dizin sisteminden arama yaparak kullanıcı ekleyebilirsiniz")
#define DIL_KUL_SEC_KAYITLI_KULLANICILAR QObject::trUtf8("Kayıtlı Kullanıcılar")
#define DIL_KUL_SEC_SECILI_KULLANICILAR QObject::trUtf8("Seçili Kullanıcılar")

//SHell ile ilgili kısımlar
#define DIL_SHL_ESYA_KONSOL_UYGULAMASI_BASLATILIRKEN_HATA_OLUSTU QObject::trUtf8("EsyaKonsol uygulaması başlatılırken hata oluştu")
#define DIL_SHL_ESYA_ISTEMCI_PAKETININ_KURULUMUNU_KONTROL_EDINIZ QObject::trUtf8("EsyaIstemci paketinin kurulumunu kontrol ediniz.")

//-kullis ile ilgili ifadeler
#define  DIL_KUL_LISTELEME_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Kullanıcıların listelenmesi sırasında hata oluştu.")
/************************************************************************/
/*					Sertifika Doğrulama Kontrolleri                     */
/************************************************************************/
#define DIL_SD_UNKNOWN							QObject::trUtf8("Bilinmiyor")

#define DIL_SD_KONTROL							QObject::trUtf8("Tanımsız Kontrol")
#define DIL_SD_VERSIYONKONTROLU					QObject::trUtf8("Sertifika Versiyon Bilgisi Kontrolü")
#define DIL_SD_SERTIFIKAIMZAKONTROLU			QObject::trUtf8("Sertifika İmza Kontrolü")
#define DIL_SD_SILTARIHKONTROLU					QObject::trUtf8("SİL Tarih Bilgileri Kontrolü")
#define DIL_SD_SILNAMEKONTROLU					QObject::trUtf8("SİL Yayıncı Kuruluş Bilgisi Kontrolü")
#define DIL_SD_SILKEYUSAGEKONTROLU				QObject::trUtf8("SİL Anahtar Kullanımı Eklentisi Kontrolü")
#define DIL_SD_SILIMZAKONTROLU					QObject::trUtf8("SİL İmza Kontrolü")
#define DIL_SD_SILEKLENTIKONTROLU				QObject::trUtf8("SİL Eklentileri Kontrolü")
#define DIL_SD_SILDENIPTALKONTROLU				QObject::trUtf8("SİL'den İptal Kontrolü")
#define DIL_SD_SIGNATUREALGAYNIMIKONTROLU		QObject::trUtf8("İmzalama Algoritması Uyumluluk Kontrolü")
#define DIL_SD_SERTIFIKATARIHKONTROLU			QObject::trUtf8("Sertifika Tarih Bilgileri Kontrolü")
#define DIL_SD_SERTIFIKANAMEKONTROLU			QObject::trUtf8("Sertifika Yayıncı Kuruluş Bilgisi Kontrolü")
#define DIL_SD_SERTIFIKAKEYUSAGEKONTROLU		QObject::trUtf8("Sertifika Anahtar Kullanımı Eklentisi Kontrolü")
#define DIL_SD_SERTIFIKAIMZAKONTROLU			QObject::trUtf8("Sertifika İmza Kontrolü")
#define DIL_SD_SERTIFIKAEKLENTIKONTROLU			QObject::trUtf8("Sertifika Eklentileri Kontrolü")
#define DIL_SD_SERINOPOZITIFKONTROLU			QObject::trUtf8("Sertifika Seri Numarasının pozitif bir tamsayı olduğunun kontrolü.")
#define DIL_SD_PATHLENCONSTRAINTKONTROLU		QObject::trUtf8("Sertifika Yol Uzunluğu Kısıtlaması eklentisi kontrolü.")
#define DIL_SD_OCSPIMZAKONTROLU					QObject::trUtf8("OCSP Cevabı İmza Kontrolü")
#define DIL_SD_OCSPDENIPTALKONTROLU				QObject::trUtf8("OCSP'den İptal Kontrolü")
#define DIL_SD_KEYIDENTIFIERKONTROLU			QObject::trUtf8("Anahtar Tanımlayıcısı Eklentisi Kontrolü")
#define DIL_SD_IMZALAYANSERTIFIKAKONTROLU		QObject::trUtf8("Imzalayan Sertifika geçerlilik Kontrolü")
#define DIL_SD_FRESHESTCRLKONTROLU				QObject::trUtf8("En Güncel SİL Eklentisi Kontrolü")
#define DIL_SD_DELTACRLINDICATORKONTROLU		QObject::trUtf8("DeltaCRL Belirteci Eklentisi Kontrolü")
#define DIL_SD_OCSP_CEVAPDURUMUKONTROLU			QObject::trUtf8("OCSP Cevabı Durumu Kontrolü")
#define DIL_SD_BASICCONSTRAINTCAKONTROLU		QObject::trUtf8("Temel Kısıtlamalar Eklentisi CA değeri Kontrolü")
#define DIL_SD_OTURUM_ACILIYOR_BEKLEYINIZ		QObject::trUtf8("Kartta oturum açılıyor.\n Lütfen bekleyiniz...")


#define DIL_SD_KONTROLSONUCU					QObject::trUtf8("Tanımsız Kontrol Sonucu")


/************************************************************************/
/*				KOK Sertifika Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_KOKSERTIFIKA				QObject::trUtf8("Kök Sertifika Kontrolü.")
#define DIL_SD_KOKSERTIFIKA_BASARISIZ	QObject::trUtf8("Sertifikaya ya da herhangi bir çaprazına güvenilmiyor.")
#define DIL_SD_KOKSERTIFIKA_BASARILI	QObject::trUtf8("Sertifika ya da bir çaprazı güvenilir.")



/************************************************************************/
/*				Sertifika Versiyon Kontrolü Sonuçları                   */
/************************************************************************/

#define DIL_SD_EXTENSION_VAR_DOGRU		QObject::trUtf8("Versiyon bilgisi sorunsuz.")
#define DIL_SD_EXTENSION_VAR_YANLIS		QObject::trUtf8("Versiyon bilgisi sorunlu.")
#define DIL_SD_UID_VAR_DOGRU			QObject::trUtf8("Versiyon bilgisi sorunsuz.")
#define DIL_SD_UID_VAR_YANLIS			QObject::trUtf8("Versiyon bilgisi sorunlu.")
#define DIL_SD_BASIT_ALANLAR_VAR_DOGRU	QObject::trUtf8("Versiyon bilgisi sorunsuz.")
#define DIL_SD_BASIT_ALANLAR_VAR_YANLIS	QObject::trUtf8("Versiyon bilgisi sorunlu.")


/************************************************************************/
/*				SIL Tarih Kontrolü Sonuçları							*/
/************************************************************************/

#define DIL_SD_SIL_TARIH_GECERLI		QObject::trUtf8("SİL Tarihi Geçerli.")
#define DIL_SD_SIL_TARIH_GECERSIZ		QObject::trUtf8("SİL Tarihi Geçersiz.")
#define DIL_SD_SIL_TARIH_BILGISI_BOZUK	QObject::trUtf8("SİL Tarih Bilgisi formatı yanlış.")


/************************************************************************/
/*				Sertifika Tarih Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_TARIH_GECERLI			QObject::trUtf8("Sertifika Tarihi Geçerli.")
#define DIL_SD_SERTIFIKA_TARIH_GECERSIZ			QObject::trUtf8("Sertifika Tarihi Geçersiz.")
#define DIL_SD_SERTIFIKA_TARIH_BILGISI_BOZUK	QObject::trUtf8("Sertifika Tarih Bilgisi formatı yanlış.")



/************************************************************************/
/*				SIL Name Kontrolü Sonuçları							*/
/************************************************************************/

#define DIL_SD_SIL_ISSUER_SUBJECT_UYUMLU		QObject::trUtf8("SİL Yayıncı Kuruluş Bilgisi Uyumlu.")
#define DIL_SD_SIL_ISSUER_SUBJECT_UYUMSUZ		QObject::trUtf8("SİL Yayıncı Kuruluş Bilgisi Uyumsuz.")
#define DIL_SD_SIL_SERTIFIKA_ISSUER_YOK			QObject::trUtf8("SİL Yayıncı Kuruluş Bilgisi Yok.")
#define DIL_SD_SIL_SMSERTIFIKA_SUBJECT_YOK		QObject::trUtf8("Sertifika Özne Bilgisi Yok.")


/************************************************************************/
/*				Sertifika Name Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_ISSUER_SUBJECT_UYUMLU		QObject::trUtf8("Sertifika Yayıncı Kuruluş Bilgisi Uyumlu.")
#define DIL_SD_SERTIFIKA_ISSUER_SUBJECT_UYUMSUZ		QObject::trUtf8("Sertifika Yayıncı Kuruluş Bilgisi Uyumsuz.")
#define DIL_SD_SERTIFIKA_SERTIFIKA_ISSUER_YOK		QObject::trUtf8("Sertifika Yayıncı Kuruluş Bilgisi Yok.")
#define DIL_SD_SERTIFIKA_SMSERTIFIKA_SUBJECT_YOK	QObject::trUtf8("Sertifika Özne Bilgisi Yok.")



/************************************************************************/
/*				SIL Anahtar Kullanımı Eklentisi Kontrolü Sonuçları		*/
/************************************************************************/

#define DIL_SD_SIL_KEY_USAGE_YOK					QObject::trUtf8("Yayıncı Kuruluş sertifikasında Anahtar Kullanımı eklentisi yok.")
#define DIL_SD_SIL_KEY_USAGE_BOZUK					QObject::trUtf8("Yayıncı Kuruluş sertifikasında Anahtar Kullanımı eklentisi bozuk.")
#define DIL_SD_SIL_KEY_USAGE_SIL_IMZALAYICI_DEGIL	QObject::trUtf8("Yayıncı Kuruluş sertifikası anahtarının SİL İmzalama özelliği yok.")
#define DIL_SD_SIL_KEY_USAGE_SIL_IMZALAYICI			QObject::trUtf8("Yayıncı Kuruluş sertifikası anahtarı ile SİL imzalanabilir.")


/************************************************************************/
/*				Sertifika Anahtar Kullanımı Eklentisi Kontrolü Sonuçları		*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_KEY_USAGE_YOK							QObject::trUtf8("Yayıncı Kuruluş sertifikasında Anahtar Kullanımı eklentisi yok.")
#define DIL_SD_SERTIFIKA_KEY_USAGE_BOZUK						QObject::trUtf8("Yayıncı Kuruluş sertifikasında Anahtar Kullanımı eklentisi bozuk.")
#define DIL_SD_SERTIFIKA_KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL	QObject::trUtf8("Yayıncı Kuruluş sertifikası anahtarının Sertifika İmzalama özelliği yok.")
#define DIL_SD_SERTIFIKA_KEY_USAGE_SERTIFIKA_IMZALAYICI			QObject::trUtf8("Yayıncı Kuruluş sertifikası anahtarı ile Sertifika imzalanabilir.")




/************************************************************************/
/*				SIL IMZA Kontrolü Sonuçları								*/
/************************************************************************/

#define DIL_SD_SIL_SIL_IMZALI_DEGIL					QObject::trUtf8("SİL imzalı değil.")
#define DIL_SD_SIL_SIL_YAPISI_BOZUK					QObject::trUtf8("SİL yapısı bozuk.")
#define DIL_SD_SIL_SERTIFIKADA_ACIK_ANAHTAR_YOK		QObject::trUtf8("Yayıncı Kuruluş sertifikasında açık anahtar bulunamadı.")
#define DIL_SD_SIL_SERTIFIKADA_ACIK_ANAHTAR_BOZUK	QObject::trUtf8("Yayıncı Kuruluş sertifikasındaki açık anahtar bozuk.")
#define DIL_SD_SIL_DOGRULAMA_KRIPTO_HATASI			QObject::trUtf8("İmza doğrulamada bir kripto hatası oluştu.")
#define DIL_SD_SIL_IMZA_DOGRULANAMADI				QObject::trUtf8("SİL İmzası doğrulanamadı.")
#define DIL_SD_SIL_IMZA_DOGRULANDI					QObject::trUtf8("SİL İmzası doğrulandı.")


/************************************************************************/
/*				Sertifika IMZA Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_SERTIFIKA_IMZALI_DEGIL			QObject::trUtf8("Sertifika imzalı değil.")
#define DIL_SD_SERTIFIKA_SERTIFIKA_YAPISI_BOZUK			QObject::trUtf8("Sertifika yapısı bozuk.")
#define DIL_SD_SERTIFIKA_SERTIFIKADA_ACIK_ANAHTAR_YOK	QObject::trUtf8("Yayıncı Kuruluş sertifikasında açık anahtar bulunamadı.")
#define DIL_SD_SERTIFIKA_SERTIFIKADA_ACIK_ANAHTAR_BOZUK	QObject::trUtf8("Yayıncı Kuruluş sertifikasındaki açık anahtar bozuk.")
#define DIL_SD_SERTIFIKA_DOGRULAMA_KRIPTO_HATASI		QObject::trUtf8("İmza doğrulamada bir kripto hatası oluştu.")
#define DIL_SD_SERTIFIKA_IMZA_DOGRULANAMADI				QObject::trUtf8("Sertifika İmzası doğrulanamadı.")
#define DIL_SD_SERTIFIKA_IMZA_DOGRULANDI				QObject::trUtf8("Sertifika İmzası doğrulandı.")


/************************************************************************/
/*				OCSP IMZA Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_OCSP_CEVAP_IMZALI_DEGIL			QObject::trUtf8("OCSP Cevabı imzalı değil.")
#define DIL_SD_OCSP_CEVAP_YAPISI_BOZUK			QObject::trUtf8("OCSP Cevabı yapısı bozuk.")
#define DIL_SD_OCSP_CEVAPTA_SERTIFIKA_YOK		QObject::trUtf8("OCSP Cevabında imzalayan sertifika bulunamadı.")
#define DIL_SD_OCSP_SERTIFIKA_OCSP_SERTIFIKASI_DEGIL	QObject::trUtf8("İmzalayan sertifika OCSP İmzalama özelliğine sahip değil.")
#define DIL_SD_OCSP_DOGRULAMA_KRIPTO_HATASI		QObject::trUtf8("İmza doğrulamada bir kripto hatası oluştu.")
#define DIL_SD_OCSP_IMZA_DOGRULANAMADI			QObject::trUtf8("OCSP Cevabı İmzası doğrulanamadı.")
#define DIL_SD_OCSP_IMZA_DOGRULANDI				QObject::trUtf8("OCSP Cevabı İmzası doğrulandı.")



/************************************************************************/
/*				SIL Eklenti Kontrolü Sonuçları							*/
/************************************************************************/

#define DIL_SD_SIL_EKLENTI_YOK					QObject::trUtf8("Eklenti yok.")
#define DIL_SD_SIL_GECERSIZ_EKLENTI				QObject::trUtf8("Geçersiz bir eklenti bulundu.")
#define DIL_SD_SIL_AYNI_EXTENSION_BIRDEN_FAZLA	QObject::trUtf8("Aynı eklenti birden fazla sayıda mevcut.")
#define DIL_SD_SIL_EKLENTILER_GECERLI			QObject::trUtf8("Eklentiler geçerli.")


/************************************************************************/
/*				SIL Eklenti Kontrolü Sonuçları							*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_EKLENTI_YOK					QObject::trUtf8("Eklenti yok.")
#define DIL_SD_SERTIFIKA_GECERSIZ_EKLENTI				QObject::trUtf8("Geçersiz bir eklenti bulundu.")
#define DIL_SD_SERTIFIKA_AYNI_EXTENSION_BIRDEN_FAZLA	QObject::trUtf8("Aynı eklenti birden fazla sayıda mevcut.")
#define DIL_SD_SERTIFIKA_EKLENTILER_GECERLI				QObject::trUtf8("Eklentiler geçerli.")



/************************************************************************/
/*				SIL den İptal Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SIL_GECERSIZ					QObject::trUtf8("SİL geçersiz.")
#define DIL_SD_SERTIFIKA_LISTEDE			QObject::trUtf8("Sertifika SİL'de mevcut.")
#define DIL_SD_SERTIFIKA_LISTEDE_DEGIL		QObject::trUtf8("Sertifika SİL'de mevcut değil.")
#define DIL_SD_SIL_BULUNAMADI				QObject::trUtf8("SİL bulunamadı.")
#define DIL_SD_SERTIFIKA_DELTA_SILDE		QObject::trUtf8("Sertifika DELTASİL'de mevcut.")
#define DIL_SD_BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS	QObject::trUtf8("SİL'de askıda ancak DELTASİL'de mevcut değil.")
#define DIL_SD_SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS				QObject::trUtf8("DELTASİL'de kaldırılmış.")



/************************************************************************/
/*				SignatureAlg Aynımı Kontrolü Sonuçları					*/
/************************************************************************/

#define DIL_SD_SIGNATURE_ALG_AYNI		QObject::trUtf8("İmzalama Algoritması uyuşuyor.")
#define DIL_SD_SIGNATURE_ALG_FARKLI		QObject::trUtf8("İmzalama Algoritması uyuşmuyor.")


/************************************************************************/
/*				SeriNo Pozitif Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SERI_NO_POZITIF		QObject::trUtf8("Seri Numarası pozitif.")
#define DIL_SD_SERI_NO_NEGATIF		QObject::trUtf8("Seri Numarası negatif.")


/************************************************************************/
/*				PathLenConstraint Kontrolü Sonuçları					*/
/************************************************************************/

#define DIL_SD_BASIC_CONST_EKLENTI_YOK						QObject::trUtf8("Temel Kısıtlamalar Eklentisi yok.")
#define DIL_SD_BASIC_CONST_EKLENTISI_BOZUK					QObject::trUtf8("Temel Kısıtlamalar Eklentisi bozuk.")
#define DIL_SD_BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK		QObject::trUtf8("Yol Uzunluğu değeri yok.")
#define DIL_SD_BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF	QObject::trUtf8("Yol Uzunluğu değeri negatif.")
#define DIL_SD_BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI	QObject::trUtf8("Yol Uzunluğu değeri aşıldı.")
#define DIL_SD_BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI	QObject::trUtf8("Yol Uzunluğu değeri geçerli.")

/************************************************************************/
/*				OCSP den Iptal Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_OCSP_OCSP_CEVABI_GECERSIZ		QObject::trUtf8("OCSP Cevabı Geçersiz.")
#define DIL_SD_OCSP_OCSP_CEVABI_BULUNAMADI		QObject::trUtf8("OCSP Cevabı bulunamadı.")
#define DIL_SD_OCSP_SM_SERTIFIKASI_BULUNAMADI	QObject::trUtf8("OCSP Cevabını İmzalayan Sertifika bulunamadı.")
#define DIL_SD_OCSP_SERTIFIKA_GECERLI			QObject::trUtf8("Sertifika Geçerli.")
#define DIL_SD_OCSP_SERTIFIKA_GECERLI_DEGIL		QObject::trUtf8("Sertifika geçerli değil.")
#define DIL_SD_OCSP_SERTIFIKA_LISTEDE			QObject::trUtf8("Sertifika listede.")


/************************************************************************/
/*				KeyIdentifier Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK	QObject::trUtf8("Yetklili Anahtar Tanımlayıcısı Eklentisi bulunamadı.")
#define DIL_SD_SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_BOZUK	QObject::trUtf8("Yetklili Anahtar Tanımlayıcısı Eklentisi bozuk.")
#define DIL_SD_SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK	QObject::trUtf8("Yayıncı Sertifikasında Özne Anahtar Tanımlayıcısı Eklentisi bulunamadı.")
#define DIL_SD_SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_BOZUK	QObject::trUtf8("Yayıncı Sertifikasında Özne Anahtar Tanımlayıcısı Eklentisi bozuk.")
#define DIL_SD_AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ	QObject::trUtf8("Anahtar Tanımlayıcısı Eklentileri uyuşmuyor.")
#define DIL_SD_AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU	QObject::trUtf8("Anahtar Tanımlayıcısı Eklentileri uyuşuyor.")


/************************************************************************/
/*				Imzalayan Sertifika Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_OCSP_GECERLI		QObject::trUtf8("İmzalayan Sertifika geçerli.")
#define DIL_SD_OCSP_GECERSIZ	QObject::trUtf8("İmzalayan Sertifika geçersiz.")


/************************************************************************/
/*				FreshestCRL Kontrolü Sonuçları							*/
/************************************************************************/

#define DIL_SD_FRESHEST_CRL_VAR	QObject::trUtf8("En Güncel SİL Eklentisi var.")
#define DIL_SD_FRESHEST_CRL_YOK	QObject::trUtf8("En Güncel SİL Eklentisi yok.")



/************************************************************************/
/*				DeltaCRLIndicator Kontrolü Sonuçları					*/
/************************************************************************/

#define DIL_SD_DELTA_CRL_INDICATOR_VAR	QObject::trUtf8("DeltaCRL Belirteci Eklentisi var.")
#define DIL_SD_DELTA_CRL_INDICATOR_YOK	QObject::trUtf8("DeltaCRL Belirteci yok.")

/************************************************************************/
/*				OCSP CevapDurumu Kontrolü Sonuçları						*/
/************************************************************************/

#define DIL_SD_OCSP_BASARILI			QObject::trUtf8("OCSP Cevabı başarıyla alındı.")
#define DIL_SD_OCSP_MALFORMED_REQUEST	QObject::trUtf8("OCSP İsteği bozuk yapıda.")
#define DIL_SD_OCSP_INTERNAL_ERROR		QObject::trUtf8("İç Sunucu Hatası.")
#define DIL_SD_OCSP_TRY_LATER			QObject::trUtf8("OCSP Sunucusu meşgul.")
#define DIL_SD_OCSP_SIG_REQUIRED		QObject::trUtf8("OCSP İsteği imzalı olmalı.")
#define DIL_SD_OCSP_UNAUTHORIZED		QObject::trUtf8("Yetkisi OCSP İsteği.")


/************************************************************************/
/*				BasicConstraintsCA Kontrolü Sonuçları					*/
/************************************************************************/

#define DIL_SD_BASIC_CONST_EKLENTI_YOK				QObject::trUtf8("Temel Kısıtlamalar Eklentisi yok.")
#define DIL_SD_BASIC_CONST_EKLENTISI_BOZUK			QObject::trUtf8("Temel Kısıtlamalar Eklentisi bozuk.")
#define DIL_SD_BASIC_CONST_EKLENTI_CA_DEGERI_YOK	QObject::trUtf8("Temel Kısıtlamalar Eklentisi CA değeri yok.")
#define DIL_SD_BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS	QObject::trUtf8("Temel Kısıtlamalar Eklentisi CA değeri yanlış.")
#define DIL_SD_BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU	QObject::trUtf8("Temel Kısıtlamalar Eklentisi CA değeri doğru.")


/************************************************************************/
/*							SILDurumBilgisi								*/
/************************************************************************/

#define DIL_SD_SIL_KONTROL_YAPILMADI			QObject::trUtf8("Kontrol Yapılamadı.")
#define DIL_SD_SIL_GECERLI						QObject::trUtf8("Geçerli.")
#define DIL_SD_SIL_IMZALAYAN_SERTIFIKA_SORUNLU	QObject::trUtf8("İmzalayan Sertifika sorunlu.")
#define DIL_SD_SIL_SIL_SORUNLU					QObject::trUtf8("SİL sorunlu.")


/********************************************************************************/
/*							OCSPCevabıDurumBilgisi								*/
/********************************************************************************/

#define DIL_SD_OCSP_CEVAP_SORUNLU				QObject::trUtf8("OCSP Cevabı Sorunlu.")
#define DIL_SD_OCSP_CEVAP_GECERLI				QObject::trUtf8("OCSP Cevabı Geçerli.")
#define DIL_SD_OCSP_IMZALAYAN_SERTIFIKA_SORUNLU	QObject::trUtf8("İmzalayan Sertifika sorunlu.")


/********************************************************************************/
/*							SertifikaDurumBilgisi								*/
/********************************************************************************/

#define DIL_SD_SERTIFIKA_GECERLI				QObject::trUtf8("Geçerli.")
#define DIL_SD_SERTIFIKA_ZINCIR_SORUNLU			QObject::trUtf8("Zincir Sorunlu.")
#define DIL_SD_SERTIFIKA_IPTAL_KONTROLU_SORUNLU	QObject::trUtf8("İptal Kontrolü sorunlu.")
#define DIL_SD_SERTIFIKA_SERTIFIKA_SORUNLU		QObject::trUtf8("Sertifika Sorunlu.")



/********************************************************************************/
/*							SD_WIDGET											*/
/********************************************************************************/

#define DIL_SD_KONTROL_DETAYI	QObject::trUtf8("Kontrol Detayı")
#define DIL_SD_SONUC			QObject::trUtf8("Sonuç")
#define DIL_SD_SONUC_DETAYI		QObject::trUtf8("Sonuç Detayı")


/************************************************************************/
/*					DEPOYA GUVENILIR KOK SERTIFIKA EKLEME                                                                     */
/************************************************************************/
#define DIL_KOKEKLE_SERTIFIKA_DEPOSUNA_KOK_SERTIFIKA_EKLENDI QObject::trUtf8("Sertifika deposuna kök sertifika eklendi.")
#define DIL_KOKEKLE_SERTIFIKA_DEPOSUNA_KOK_SERTIFIKA_EKLENIRKEN_HATA_OLUSTU QObject::trUtf8("Sertifika deposuna kök sertifika eklenirken hata oluştu")
#define DIL_SERT_DEPO_DEPO_PAROLASI_BELIRLEME	QObject::trUtf8("Depo Parolası Belirleme")
#define DIL_SERT_DEPO_DEPO_PAROLASI_BELIRLEYINIZ			QObject::trUtf8("Depo Parolası Belirleyiniz")
#define DIL_SERT_DEPO_DEPO_PAROLASI_BELIRLEME_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Depo parolası belirleme sırasında hata oluştu")
#define DIL_SERT_DEPO_DEPOYA_ULASMADA_SORUN_OLABILIR			QObject::trUtf8("Depoya ulaşmada sorun olabilir")
#define DIL_SERT_DEPO_SERTIFIKA_DEPOSUNDAN_DIZIN_NO_ALINIRKEN_HATA_OLUSTU QObject::trUtf8("Sertifika Deposundan dizin numarası alınırken hata oluştu")



/************************************************************************/
/*				AYARLARDAN OKUMA İLE İLGİLİ HATALAR                                                                     */
/************************************************************************/
#define DIL_AYAR_AYARLARDAN_GUVENLI_DIZIN_YOLU_OKUNURKEN_HATA_OLUSTU QObject::trUtf8("Ayarlardan güvenli dizin yolu okunurken hata oluştu")
#define DIL_AYAR_AYARLARDAN_SONUC_EKRANINI_GOSTER_OKUNMAYA_CALISIRKEN_HATA_OLUSTU QObject::trUtf8("Ayarlardan sonuc ekranını goster okunmaya calısırken hata olustu")
#define DIL_AYAR_AYARLARDAN_OKUMA_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Ayarlardan okuma sırasında hata oluştu")
#define DIL_AYAR_AYARLARDAN_WEB_SERTIFIKA_SAGLAYICI_ADRESI_OKUNURKEN_HATA_OLUSTU QObject::trUtf8("Ayarlardan Web Sertifika sağlayıcı adresi okunurken hata oluştu")

/************************************************************************/
/*			SERTIFIKA DOGRULAMA İLE İLGİLİ İFADELER                     */
/************************************************************************/
#define DIL_SERT_DOG_SERTIFIKA_DOGRULAM_SIRASINDA_SORUN_OLUSTU QObject::trUtf8("Sertifika doğrulama sırasında sorun oluştu")
#define DIL_SERT_DOG_IMZADAKI_SERTIFIKA_DOGRULANAMADI QObject::trUtf8("İmzadaki sertifika doğrulanamadı")


/************************************************************************/
/*				EXTENDED KEY USAGES                                     */
/************************************************************************/

#define DIL_EKU_ISTEMCI_YETKILENDIRMESI	QObject::trUtf8("İstemci Yetkilendirmesi")
#define DIL_EKU_EPOSTA_KORUMASI			QObject::trUtf8("E-Posta Koruması")
#define DIL_EKU_AKILLIKART_GIRIS		QObject::trUtf8("Akıllı Kart Girişi")

/************************************************************************/
/*		SERTIFIKA KONTROL SONUC EKRANI ILE ILGILI IFADELER				*/
/************************************************************************/
#define DIL_SERT_KONT_EKR_GECERLI  QObject::trUtf8("Geçerli")
#define DIL_SERT_KONT_EKR_GECERSIZ	QObject::trUtf8("Geçersiz")

/************************************************************************/
/*		SORUNLU SERTIFIKALAR EKRANI ILE ILGILI IFADELER                                                                     */
/************************************************************************/
#define DIL_SORUNLU_SERT_ASAGIDAKI_1_KULLANICI__ QObject::trUtf8("Aşağıdaki %1 kullanıcı sertifika almamış ya da geçerliliği yoktur")
#define DIL_SORUNLU_SERT_SERTIFIKASINDA_SORUN_BULUNAN_KULLANICILAR QObject::trUtf8("Sertifikasında sorun bulunan kullanıcılar")



/************************************************************************/
/*       TUGRA PROGRAMI EKRAN YAZILARI (GEÇİCİ)                         */
/************************************************************************/


#define DIL_TUGRA_TITLE QObject::trUtf8("Tuğra")
#define DIL_TUGRA_ABOUT_TEXT QObject::trUtf8("Tugra Döküman İmzalama Arayüzü")

#define DIL_TUGRA_NEW			QObject::trUtf8("&Yeni")
#define DIL_TUGRA_OPEN		QObject::trUtf8("&Aç...")
#define DIL_TUGRA_SAVE		QObject::trUtf8("&Kaydet")
#define DIL_TUGRA_SAVEAS		QObject::trUtf8("&Farklı Kaydet...")
#define DIL_TUGRA_EXIT		QObject::trUtf8("&Çıkış")
#define DIL_TUGRA_CUT			QObject::trUtf8("Kes")
#define DIL_TUGRA_COPY		QObject::trUtf8("Kopyala")
#define DIL_TUGRA_PASTE		QObject::trUtf8("Yapıştır")
#define DIL_TUGRA_CLOSE		QObject::trUtf8("Kapat")
#define DIL_TUGRA_CLOSEALL	QObject::trUtf8("Hepsini Kapat")
#define DIL_TUGRA_TILE		QObject::trUtf8("Döşe")
#define DIL_TUGRA_CASCADE		QObject::trUtf8("CASCADE")
#define DIL_TUGRA_ARRANGE		QObject::trUtf8("ARRANGE")
#define DIL_TUGRA_NEXT		QObject::trUtf8("Sonraki")
#define DIL_TUGRA_PREVIOUS	QObject::trUtf8("Önceki")
#define DIL_TUGRA_ABOUT		QObject::trUtf8("Hakkında")
#define DIL_TUGRA_ABOUTQT		QObject::trUtf8("Qt Hakkında")

#define DIL_TUGRA_FILE	QObject::trUtf8("Dosya")
#define DIL_TUGRA_EDIT	QObject::trUtf8("Düzenle")
#define DIL_TUGRA_WINDOW	QObject::trUtf8("Pencere")
#define DIL_TUGRA_HELP	QObject::trUtf8("Yardım")


#define DIL_TUGRA_PROPERTIES			QObject::trUtf8("Döküman Özellikleri")
#define DIL_TUGRA_DOSYATURU_IMZALI		QObject::trUtf8("İmzalı Döküman")
#define DIL_TUGRA_DOSYATURU_IMZASIZ		QObject::trUtf8("İmzasız Döküman")
#define DIL_TUGRA_DOSYATURU_BILINMEYEN	QObject::trUtf8("...")


#define DIL_TUGRA_READY			QObject::trUtf8("Hazır..")
#define DIL_TUGRA_FILE_LOADED		QObject::trUtf8("Dosya Açıldı..")
#define DIL_TUGRA_FILE_SAVED		QObject::trUtf8("Dosya Kaydedildi..")

#define DIL_TUGRA_FILE_MODIFIED				QObject::trUtf8("dosyası değişti")
#define DIL_TUGRA_FILE_CANNOT_BE_READ		QObject::trUtf8("dosyası okunamadı")
#define DIL_TUGRA_FILE_CANNOT_BE_WRITTEN	QObject::trUtf8("dosyasına yazılamadı")
#define DIL_TUGRA_SAVE_CHANGES_Q			QObject::trUtf8("Değişiklikleri kaydetmek istiyor musunuz ?")
#define DIL_TUGRA_SAVE_CHANGES_C			QObject::trUtf8("Dikkat!")


#define DIL_TUGRA_YES		QObject::trUtf8("Evet")
#define DIL_TUGRA_NO		QObject::trUtf8("Hayır")
#define DIL_TUGRA_IPTAL		QObject::trUtf8("İptal")
#define DIL_TUGRA_HATA		QObject::trUtf8("Hata")

#define DIL_TUGRA_SERI_IMZALA QObject::trUtf8("Seri İmzala")

/************************************************************************/
/*			LDAP Tiplerinin isimleri                                    */
/************************************************************************/
#define DIL_LDAP_TIPI_ACTIVE_DIRECTORY QObject::trUtf8("Active Directory")
#define DIL_LDAP_TIPI_CRITICAL_PATH QObject::trUtf8("Critical Path")
#define DIL_LDAP_TIPI_NETSCAPE QObject::trUtf8("Netscape")
#define DIL_LDAP_TIPI_GENERIC	QObject::trUtf8("Generic")
#define DIL_LDAP_TIPI_BILINMEYEN QObject::trUtf8("Bilinmeyen dizin tipi")

/************************************************************************/
/*	LDAP Bağlantı tipleri ile ilgili olanlar                           */
/************************************************************************/
#define DIL_LDAP_BAGLANTI_GUVENLI QObject::trUtf8("Güvenli Bağlantı")
#define DIL_LDAP_BAGLANTI_NORMAL QObject::trUtf8("Normal Bağlantı")
#define DIL_LDAP_BAGLANTI_BILINMEYEN QObject::trUtf8("Bilinmeyen bağlantı tipi")

/************************************************************************/
/*	GRUP DÜZENLEME EKRANI İLE İLGİLİ İFADELER                                                 */
/************************************************************************/
#define DIL_AYAR_EKR_AYAR_DUZENLEME_EKRANI QObject::trUtf8("Ayar Düzenleme Ekranı")
#define DIL_AYAR_EKR_AYAR_DEGISTIRME_EKRANI QObject::trUtf8("Ayar Değiştirme Ekranı")
#define DIL_AYAR_EKR_KULLANICI_AYARLARI QObject::trUtf8("Kullanıcı Ayarları")
#define DIL_AYAR_EKR_GENEL_AYARLAR QObject::trUtf8("Genel Ayarlar")
#define DIL_AYAR_EKR_LDAP_AYARLARI QObject::trUtf8("LDAP Ayarları")
#define DIL_AYAR_EKR_GRUPLARI_DUZENLE QObject::trUtf8("Grupları Düzenle")
#define DIL_AYAR_EKR_AYAR_KATEGORISI QObject::trUtf8("Ayar Kategorisi")
#define DIL_AYAR_EKR_AYAR_ADI QObject::trUtf8("Ayar Adı")
#define DIL_AYAR_EKR_AYAR_DEGERI QObject::trUtf8("Ayar Değeri")

#define DIL_AYAR_EKR_IP QObject::trUtf8("IP")
#define DIL_AYAR_EKR_PORT QObject::trUtf8("Port")
#define DIL_AYAR_EKR_BAGLANTI_TIPI QObject::trUtf8("Bağlantı Tipi")
#define DIL_AYAR_EKR_BUYUKLUK_LIMITI QObject::trUtf8("Büyüklük Limiti")
#define DIL_AYAR_EKR_ZAMAN_LIMITI QObject::trUtf8("Zaman Limiti")
#define DIL_AYAR_EKR_LDAP_TIPI QObject::trUtf8("LDAP Tipi")
#define DIL_AYAR_EKR_VARSAYILAN QObject::trUtf8("Varsayılan")
#define DIL_AYAR_EKR_ARAMA_TABANI QObject::trUtf8("Arama Tabanı")
#define DIL_AYAR_EKR_LDAP_AYRINTI QObject::trUtf8("LDAP Ayrıntı")

/************************************************************************/
/*	SERTİFİKA SEÇİM EKRANI İLE İLGİLİ İFADELER							*/
/************************************************************************/
#define DIL_SERT_SECIM_EKR_YUKARIDA_ADI_GECEN__SECINIZ  QObject::trUtf8("Yukarıda adı geçen kullanıcının dizin sisteminde birden fazla sertifikası bulunmuştur. Aşağıdaki sertifikalardan ESYA sisteminde kullanacağınız sertifikayı seçiniz.")
#define DIL_SERT_SECIM_EKR_ARAMA_KRITERI QObject::trUtf8("Arama Kriteri")
#define DIL_SERT_SECIM_EKR_SERTIFIKA_LISTESI QObject::trUtf8("Sertifika Listesi")

/************************************************************************/
/* SERTIFIKA KONTROL SONUC EKRANI ILE ILGILI İFADELER                                                                     */
/************************************************************************/
#define DIL_SERT_KONTROL_EKR_SECTIGINIZ_KULLANICILARA__LISTEDIR QObject::trUtf8("Seçtiğiniz kullanıcılara ait sertifikalar için yapılan kontrolllerin sonuçları aşağıdaki listedir.") 
#define DIL_SERT_KONTROL_EKR_HATALI_OLARAK_GORULEN__GEREKMEKTEDIR    QObject::trUtf8("Hatalı olarak görülen kullanıcılar için şifreleme işlemi yapamayacağınızdan dolayı bu kullanıcıları listenizden çıkarmanız gerekmektedir.")
#define 	DIL_SERT_KONTROL_EKR_SERTIFIKA_DURUMU QObject::trUtf8("Sertifika Durumu")

/************************************************************************/
/*    PAROLA TABANLI ŞİFRELEME EKRANI İLE İLGİLİ MESAJLAR                                                                  */
/************************************************************************/
#define DIL_PTAB_SIF_EKR_SECTIGINIZ_DOSYA_VE_DIZINLERI___SIFRELER QObject::trUtf8("Seçtiğiniz dosya ve dizinleri parola tabanlı şifreler")
#define DIL_PTAB_SIF_EKR_PAROLA_KORUMALI_DOKUMANI_EPOSTA_ILE_GONDER QObject::trUtf8("Parola korumalı dökümanı e-posta ile gönder")

/************************************************************************/
/*  PAROLA TABANLI SİFRE ÇÖZME EKRANI İLE İLGİLİ İFADELER                                                                     */
/************************************************************************/
#define DIL_PTAB_SIF_COZ_EKR_DOSYANIN_SIFRESINI__GIRINIZ QObject::trUtf8("Dosyanın şifresini çözmek için parolasını giriniz")
#define DIL_PTAB_SIF_COZ_EKR_COZME_SECENEKLERI QObject::trUtf8("Çözme seçenekleri") 
#define DIL_PTAB_SIF_COZ_EKR_ISLEM_BITINCE__SIL QObject::trUtf8("İşlem bitince şifreli dökümanı sil")
#define DIL_PTAB_SIF_COZ_EKR_DOSYALARIN_KAYDEDILECEGI_DIZIN_ADI QObject::trUtf8("Dosyaların kaydedileceği dizin adı :")

/************************************************************************/
/*  GUVENLI COP KUTUSUNA AT ILE ILGILI FONKSIYONLAR                                                                     */
/************************************************************************/
#define DIL_GUV_COP_AT_1_DOSYA_YADA_DIZIN_OLMADIGINDAN_GUVENLI_COP_KUTUSUNA_ATILAMADI QObject::trUtf8("%1 dosya yada dizin olmadığından güvenli çöp kutusuna atılamadı")
#define DIL_GUV_COP_AT_GUVENLI_COP_KUTUSUNA_ATILIRKEN_HATA_OLUSTU QObject::trUtf8("Güvenli çöp kutusuna atılırken hata oluştu")
#define DIL_GUV_COP_AT_COP_KUTUSUNA_ATILIYOR QObject::trUtf8("Çöp kutusuna atılıyor...")
#define DIL_GUV_COP_AT_SECTIGINIZ_DOSYAYI_YADA_DIZINI_GUVENLI_OLARAK___EMINMISINIZ QObject::trUtf8("Seçtiğiniz dizin ya da dosyayı güvenli olarak çöp kutusuna atmak istediğinizden emin misiniz?")
#define DIL_GUV_COP_AT_NOT_BU_DOSYA_VE_DIZINLER_SIZIN_ICIN_SIFRELENEREK___ATILACAKTIR QObject::trUtf8("Not:Bu dosya ve dizinler sizin için şifrelenip çöp kutusuna atılacaktır.")
#define DIL_GUV_COP_AT_GERI_KAZANMAK_ISTEDINIZ_ZAMAN__GEREKECEKTIR QObject::trUtf8("Geri kazanmak istediğiniz zaman bu dökümanların şifresini çözmeniz gerekecektir.")

/************************************************************************/
/*   GUVENLI SIL ILE ILGILI IŞLEMLER                                    */
/************************************************************************/
#define DIL_GUV_SIL_1_DOSYA_YA_DA_DIZIN_OLMADIGINDAN_GUVENLI_SILINEMEDI QObject::trUtf8("%1 dosya yada dizin olmadığından güvenli silinemedi")
#define DIL_GUV_SIL_GUVENLI_SIL_SIRASINDA_HATA_OLUSTU QObject::trUtf8("Güvenli Sil işlemi sırasında hata oluştu")
#define DIL_GUV_SIL_SECTIGINIZ_DOSYA_VEYA_DIZINLERI_GUVENLI__EMINMISINIZ QObject::trUtf8("Seçtiğiniz dosya veya dizinleri güvenli silmek istediğinizden emin misiniz ?")

/************************************************************************/
/*    ISLEM SONUCU GOSTERME ILE ILGILI IFADELER                                                                     */
/************************************************************************/
#define DIL_ISLEM_SONUC_EKR_DOSYA QObject::trUtf8("Dosya")
#define DIL_ISLEM_SONUC_EKR_DIZIN QObject::trUtf8("Dizin")
#define DIL_ISLEM_SONUC_EKR_GRUP  QObject::trUtf8("Grup")

#define DIL_ISLEM_SONUC_EKR_SIFRELEME QObject::trUtf8("Şifreleme")
#define DIL_ISLEM_SONUC_EKR_IMZALAMA QObject::trUtf8("İmzalama")
#define DIL_ISLEM_SONUC_EKR_IMZALAMA_SIFRELEME QObject::trUtf8("İmzalama && Şifreleme")
#define DIL_ISLEM_SONUC_EKR_IMZA_ONAYLAMA QObject::trUtf8("İmza Onaylama")
#define DIL_ISLEM_SONUC_EKR_SIFRE_COZME QObject::trUtf8("Şifre Çözme")
#define DIL_ISLEM_SONUC_EKR_SIFRE_COZME_IMZA_ONAYLAMA QObject::trUtf8("Şifre Çözme && İmza Onaylama")
#define DIL_ISLEM_SONUC_EKR_PAROLA_TABANLI_SIFRELEME QObject::trUtf8("Parola Tabanlı Şifreleme")
#define DIL_ISLEM_SONUC_EKR_PAROLA_TABANLI_SIFRE_COZME QObject::trUtf8("Parola Tabanlı Şifre Çözme")
#define DIL_ISLEM_SONUC_EKR_GUVENLI_SILME QObject::trUtf8("Güvenli Silme")

#define DIL_ISLEM_SONUC_EKR_BASARILI QObject::trUtf8("Başarılı")
#define DIL_ISLEM_SONUC_EKR_BASARISIZ QObject::trUtf8("Başarısız")
#define DIL_ISLEM_SONUC_EKR_TAMAMLANAMADI QObject::trUtf8("Tamamlanamadı")
#define DIL_ISLEM_SONUC_EKR_IPTAL_EDILDI QObject::trUtf8("İptal Edildi")
#define DIL_ISLEM_SONUC_EKR_KISMEN_BASARILI QObject::trUtf8("Kısmen Başarılı")

#define DIL_ISLEM_SONUC_EKR_1_ISLEMI_BASARIYLA_TAMAMLANDI QObject::trUtf8("%1 işlemi başarıyla tamamlandı.")
#define DIL_ISLEM_SONUC_EKR_1_ISLEMI_SIRASINDA_HATA_OLUSTU QObject::trUtf8("%1 işlemi sırasında hata oluştu.")

#define DIL_ISLEM_SONUC_EKR_BOS_DOSYA_HATASI QObject::trUtf8("Boş dosya hatası")
#define DIL_ISLEM_SONUC_EKR_DISK_DOLU QObject::trUtf8("Disk dolu")
#define DIL_ISLEM_SONUC_EKR_HATALI_FORMAT QObject::trUtf8("Hatalı Format")
#define DIL_ISLEM_SONUC_EKR_DOSYA_ACMA_HATASI QObject::trUtf8("Dosya açma hatası")
#define DIL_ISLEM_SONUC_EKR_OKUMA_YAZMA_HATASI QObject::trUtf8("Okuma yazma hatası")
#define DIL_ISLEM_SONUC_EKR_HATALI_PAROLA QObject::trUtf8("Hatalı Parola")
#define DIL_ISLEM_SONUC_EKR_GECICI_YAZMA_HATASI QObject::trUtf8("Geçici yazma hatası")
#define DIL_ISLEM_SONUC_EKR_BILINMEYEN_HATA QObject::trUtf8("Bilinmeyen hata")


/************************************************************************/
/*   LISANS KONTROLU ILE ILGILI HATALAR                                                                     */
/************************************************************************/
#define DIL_LISANS_LISANS_DOSYASI_BULUNAMADI QObject::trUtf8("Lisans dosyası bulunamadı.")
#define DIL_LISANS_LISANS_DOSYASI_SIFRESI_COZULEMEDI QObject::trUtf8("Lisans dosyasının şifresi çözülemedi")
#define DIL_LISANS_LISANS_DOSYASININ_ICERIGI_DEGISTIRILMIS_OLABILIR QObject::trUtf8("Lisans dosyasının içeriği değiştirilmiş olabilir")
#define DIL_LISANS_LISANS_TARIHI_GECERSIZ QObject::trUtf8("Lisans tarihi geçersiz")

/************************************************************************/
/*   DEPO ILE ILGILI IFADELER                                                                     */
/************************************************************************/
#define DIL_SERTIFIKA_DEPOSU QObject::trUtf8("Sertifika Deposu") 
#define DIL_DEPO_KART_PIN_LOCKED QObject::trUtf8("Kart Parolası Bloke Oldu.") 
#define DIL_DEPO_KART_PIN_INCORRECT QObject::trUtf8("Kart Parolası Yanlış Girildi.") 
#define DIL_DEPO_KARTINIZI_TAKINIZ QObject::trUtf8("Okuyucuda kart takılı değil.") 
/************************************************************************/
/*   DEPO GORUNTULEYICI ILE ILGILI IFADELER                                                                     */
/************************************************************************/
#define DIL_DEPOGORUNTULEYICI_KOK_SERTIFIKALAR QObject::trUtf8("KÖK SERTİFİKALAR") 
#define DIL_DEPOGORUNTULEYICI_KANUNI QObject::trUtf8("KANUNİ") 
#define DIL_DEPOGORUNTULEYICI_KURUMSAL QObject::trUtf8("KURUMSAL") 
#define DIL_DEPOGORUNTULEYICI_KISISEL QObject::trUtf8("KİŞİSEL") 

#define DIL_DEPOGORUNTULEYICI_SERTIFIKALAR			QObject::trUtf8("Sertifikalar")
#define DIL_DEPOGORUNTULEYICI_KOKSERTIFIKALAR		QObject::trUtf8("Kök Sertifikalar")
#define DIL_DEPOGORUNTULEYICI_CRLS					QObject::trUtf8("Siller")


#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_DEPOSU QObject::trUtf8("Sertifika Deposu Gorüntüleyici") 
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_SILINMEYE_DEVAM_EDILSINMI QObject::trUtf8("Sertifika silinecektir. Devam etmek istiyor musunuz?")
#define DIL_DEPOGORUNTULEYICI_DOSYA_DOGRU_GIRILMEDI QObject::trUtf8("Dosya yolu doğru girilmedi.")
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_SECINIZ QObject::trUtf8("Aktarmak istediğiniz sertifikayı seçiniz.")
#define DIL_DEPOGORUNTULEYICI_OZELANAHTAR_YOK QObject::trUtf8("Aktarmak istediğiniz sertifikanın özel anahtarı yoktur.")
#define DIL_DEPOGORUNTULEYICI_CRL_SECINIZ QObject::trUtf8("Aktarmak istediğiniz SİL'i seçiniz.")
#define DIL_DEPOGORUNTULEYICI_KANUNI_VE_KURUMSAL_KOKLER_SILINEMEZ QObject::trUtf8("Kanuni ve Kurumsal kök sertifikalar silinemez.")
#define DIL_DEPOGORUNTULEYICI_KOKSERTIFIKA_SILINSINMI QObject::trUtf8("Kök sertifika silinecektir. Devam etmek istiyor musunuz?")
#define DIL_DEPOGORUNTULEYICI_SIL_SILINSINMI        QObject::trUtf8("SİL silinecektir. Devam etmek istiyor musunuz?")
#define DIL_DEPOGORUNTULEYICI_DIZIN_ADI_GIRILMEDI        QObject::trUtf8("Dizin adı düzgün girilmedi.")
#define DIL_DEPOGORUNTULEYICI_DIZIN_GETIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Veritabanindan dizin getirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_GETIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Veritabanindan sertifika getirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_TUM_SERTIFIKALARI_GETIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Veritabanindan tüm sertifikaları getirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_KOKSERTIFIKA_GETIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Veritabanindan kok sertifika getirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_SIL_GETIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Veritabanindan SİL getirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_PAROLALAR_AYNI_DEGIL        QObject::trUtf8("Girilen parolalar aynı değil.")
#define DIL_DEPOGORUNTULEYICI_DIZIN_BULUNAMADI        QObject::trUtf8("Dizin bulunamadı.")
#define DIL_DEPOGORUNTULEYICI_PAROLA_DEGISTIRME_ISLEMI_BASARISIZ        QObject::trUtf8("Parola değiştirme işlemi başarısız.")
#define DIL_DEPOGORUNTULEYICI_OZEL_ANAHTARLAR_SILINSINMI        QObject::trUtf8("Depodaki tüm özel anahtarlar silinecektir. İşleme devam edilsin mi?")
#define DIL_DEPOGORUNTULEYICI_YENI_DEPO_PAROLASI_GIRILMEDI        QObject::trUtf8("Yeni depo parolası girilmedi.")
#define DIL_DEPOGORUNTULEYICI_PAROLA_SIFIRLAMA_ISLEMI_YAPILAMADI        QObject::trUtf8("Parola sıfırlama işlemi yapılamadı.")
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_BILGILERI_EKSIK_GIRILDI        QObject::trUtf8("Sertifika bilgileri eksik girildi.")
#define DIL_DEPOGORUNTULEYICI_DOSYADAN_SERTIFIKA_EKLENEMEDI        QObject::trUtf8("Dosyadan sertifika eklenemedi.")
#define DIL_DEPOGORUNTULEYICI_KARTTAN_SERTIFIKA_EKLENEMEDI        QObject::trUtf8("Karttan sertifika eklenemedi.")
#define DIL_DEPOGORUNTULEYICI_PFXTEN_SERTIFIKA_EKLENEMEDI        QObject::trUtf8("PFXten sertifika eklenemedi.")
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_PFX_OLARAK_AKTARILAMADI        QObject::trUtf8("Sertifika PFX olarak aktarılamadı.")



#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_DEPODA_VAR        QObject::trUtf8("Sertıfıka depoda mevcut.")
#define DIL_DEPOGORUNTULEYICI_CONSTRAINT_FAILED        QObject::trUtf8("constraint failed")
#define DIL_DEPOGORUNTULEYICI_IMZALI_KOK_EKLENEMEDI        QObject::trUtf8("İmzalı kök sertifika eklenemedi.")
#define DIL_DEPOGORUNTULEYICI_GECERLI_SERTIFIKA_BULUNAMADI        QObject::trUtf8("Geçerli sertifika bulunamadı.")
#define DIL_DEPOGORUNTULEYICI_KOK_EKLENSINMI        QObject::trUtf8("Kök sertifikayı depoya eklemek istiyor musunuz?")
#define DIL_DEPOGORUNTULEYICI_KISISEL_KOK_EKLENEMEDI        QObject::trUtf8("Kişisel kök sertifika eklenemedi.") 
#define DIL_DEPOGORUNTULEYICI_SIL_EKLENEMEDI        QObject::trUtf8("Veritabanina SİL eklenemedi.") 
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_SILINEMEDI        QObject::trUtf8("Veritabanından sertifika silinemedi.") 
#define DIL_DEPOGORUNTULEYICI_KOK_SILINEMEDI        QObject::trUtf8("Veritabanindan kök sertifika silinemedi.") 
#define DIL_DEPOGORUNTULEYICI_DIZIN_EKLENEMEDI        QObject::trUtf8("Veritabanina dizin eklenemedi.") 
#define DIL_DEPOGORUNTULEYICI_DIZIN_SILINEMEDI        QObject::trUtf8("Veritabanindan dizin silinemedi.") 
#define DIL_DEPOGORUNTULEYICI_IMZALI_DOSYA_GIRILMEDI        QObject::trUtf8("İmzali dosya girilmedi.") 
#define DIL_DEPOGORUNTULEYICI_IMAZALANACAK_DOSYA_OLUSTURULAMADI        QObject::trUtf8("İmzalanacak dosya oluşturulamadı.") 
#define DIL_DEPOGORUNTULEYICI_EKLENECEK_DIZIN_ADI_GIRINIZ QObject::trUtf8("Eklenecek dizin adını giriniz.")
#define DIL_DEPOGORUNTULEYICI_SILINECEK_DIZIN_ADI_GIRINIZ QObject::trUtf8("Silinecek dizin adını giriniz.")

#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_SAHIBI		QObject::trUtf8("Sertifika Sahibi")
#define DIL_DEPOGORUNTULEYICI_YAYINCI_KURULUS		QObject::trUtf8("Yayıncı Kuruluş")
#define DIL_DEPOGORUNTULEYICI_SON_GECERLILIK_TARIHI QObject::trUtf8("Son Geçerlilik Tarihi")
#define DIL_DEPOGORUNTULEYICI_ANAHTAR_KULLANIMI		QObject::trUtf8("Anahtar Kullanımı")
#define DIL_DEPOGORUNTULEYICI_EPOSTA				QObject::trUtf8("E-Posta")
#define DIL_DEPOGORUNTULEYICI_KISI_ADI				QObject::trUtf8("Kişi Adı")
#define DIL_DEPOGORUNTULEYICI_IMZALI_KOK_DOSYA_YOLU				QObject::trUtf8("İmzalı Kök Dosya Yolu:")
#define DIL_DEPOGORUNTULEYICI_ANAHTAR_DOSYASI_BULUNAMADI        QObject::trUtf8("Anahtar dosyası bulunamadı.")
#define DIL_DEPOGORUNTULEYICI_SERTIFIKA_DOSYASI_BULUNAMADI        QObject::trUtf8("Sertifika dosyası bulunamadı.")
#define DIL_DEPOGORUNTULEYICI_IMZALANACAK_SERTIFIKA_EKLERKEN_HATA_OLUSTU        QObject::trUtf8("İmzalanacak sertifika eklerken bir hata oluştu.")
#define DIL_DEPOGORUNTULEYICI_PAROLA_DEGISTIRME_ISLEMI_BASARILI        QObject::trUtf8("Parola değiştirme işlemi başarılı.")

#define DIL_PFX_PAROLASI_GIRINIZ	QObject::trUtf8("PFX Parolası Giriniz")
/************************************************************************/
/*   GDM ILE ILGILI IFADELER                                                                     */
/************************************************************************/
#define DIL_GDM QObject::trUtf8("GDM") 

#define DIL_GDM_SIFRELI QObject::trUtf8("Şifreli")
#define DIL_GDM_IMZALI QObject::trUtf8("İmzalı")
#define DIL_GDM_IMZALISIFRELI QObject::trUtf8("İmzalıŞifreli")
#define DIL_GDM_IZLENECEK_DIZIN_BUL QObject::trUtf8("İzlenecek klasör seçiniz.")

#define DIL_GDM_OSTLISTEYEGORE QObject::trUtf8("Listeye göre")
#define DIL_GDM_OSTKULLANICIYASOR QObject::trUtf8("Kullanıcıya sor")
#define DIL_GDM_OSTOKUMAHAKKINAGORE QObject::trUtf8("Okuma hakkına göre")
#define DIL_GDM_OSTAKTIFKULLANICIYAGORE QObject::trUtf8("Aktif kullanıcıya göre")

#define DIL_GDM_YENI_GUVENLI_DIZINI_EKLEMEK_ISTIYORMUSUNUZ QObject::trUtf8("Seçili dizini güvenli dizin olarak eklemek istiyor musunuz?")
#define DIL_GDM_GUVENLI_DIZINI_ZATEN_EKLENMIS QObject::trUtf8("Seçili dizin güvenli dizin olarak zaten eklenmiş.")
#define DIL_GDM_YENI_GUVENLI_DIZINI_TIPINI_DEGISTIRMEK_ISTIYORMUSUNUZ QObject::trUtf8("Seçili güvenli dizinin tipini değiştirmek istiyor musunuz?")
#define DIL_GDM_HATA QObject::trUtf8("GDM işlemleri sırasında hata oluştu. Log Dosyasına bakınız.")
#define DIL_GDM_DEGISIKLIKLER_KAYDEDILSINMI QObject::trUtf8("Değişiklikler kaydedilsin mi?")

#define DIL_GDM_UZERINE_YAZILSINMI_SORUSU QObject::trUtf8("Oluşacak dosya güvenli dizinde zaten var. Kaynak dosya adı değiştirilmeden işleme devam edilirse üzerine yazılacaktır. Devam etmek istiyor musunuz?")
//////////////////////////////////////////////////////////////////////////
// KABUK EKLENTISI ILE ILGILI IFADELER ///////////////////////////////////
#define DIL_SHL_KERMEN											QObject::trUtf8("Kermen")
#define DIL_SHL_IMZALA											QObject::trUtf8("İmzala")
#define DIL_SHL_SIFRELE											QObject::trUtf8("Şifrele")

#define DIL_SHL_IMZALA_SIFRELE									QObject::trUtf8("İmzala && Şifrele")
#define DIL_SHL_KENDINE_IMZALA_SIFRELE							QObject::trUtf8("Kendine İmzala && Şifrele")
#define DIL_SHL_GRUBA_IMZALA_SIFRELE							QObject::trUtf8("Gruba İmzala && Şifrele")
#define DIL_SHL_PAROLA_TABANLI_SIFRELE							QObject::trUtf8("Parola Tabanlı Şifrele")
#define DIL_SHL_PAROLA_TABANLI_SIFRE_COZ						QObject::trUtf8("Parola Tabanlı Şifre Çöz")
#define DIL_SHL_GUVENLI_SIL										QObject::trUtf8("Güvenli Sil")
#define DIL_SHL_GUVENLI_COP_KUTUSUNA_AT							QObject::trUtf8("Güvenli Çöp Kutusuna At")
#define DIL_SHL_DOSYA_BILGISI									QObject::trUtf8("Dosya Bilgisi")

#define DIL_SHL_SIFRE_COZ										QObject::trUtf8("Şifre Çöz")
#define DIL_SHL_IMZA_ONAYLA									    QObject::trUtf8("İmza Onayla")
#define DIL_SHL_SIFRE_COZ_IMZA_ONAYLA							QObject::trUtf8("Şifre Çöz && İmza Onayla")

#define DIL_SHL_SIL_GUNCELLE									QObject::trUtf8("SİL Güncelle")
#define DIL_SHL_KULLANICI_DEGISTIR								QObject::trUtf8("Kullanıcı Değiştir")
#define DIL_SHL_SECENEKLER										QObject::trUtf8("Seçenekler")
#define DIL_SHL_SURUM_BILGISI									QObject::trUtf8("Sürüm Bilgisi")
#define DIL_SHL_HAKKINDA										QObject::trUtf8("Hakkında")
#define DIL_SHL_GRUPLARI_DUZENLE								QObject::trUtf8("Grupları Düzenle")

#define DIL_SHL_MGM_BILGISI	QObject::trUtf8("MGM Bilgisi")
#define DIL_SHL_OZ_EKR_DOSYA_IMZALAMA_VE_SIFRELEME_BILGISI QObject::trUtf8("Dosya İmzalama ve Şifreleme Bilgisi")
#define DIL_SHL_OZ_EKR_IMZALAYANLAR QObject::trUtf8("İmzalayanlar")
#define DIL_SHL_OZ_EKR_SIFRELENENLER QObject::trUtf8("Şifrelenenler")
#define DIL_SHL_OZ_EKR_BU_DOSYA__IMZALANMISTIR QObject::trUtf8("Bu dosya aşağıdaki kişiler tarafından imzalanmıştır")
#define DIL_SHL_OZ_EKR_BU_DOSYA__SIFRELENMISTIR QObject::trUtf8("Bu dosya aşağıdaki kişiler için şifrelenmiştir")
#define DIL_SHL_OZ_EKR_1_KULLANICI_TARAFINDAN_IMZALI QObject::trUtf8("Dosya %1 kullanıcı tarafında imzalı")
#define DIL_SHL_OZ_EKR_1_KULLANICI_ICIN_SIFRELI QObject::trUtf8("Dosya %1 kullanıcı için şifreli")
#define DIL_SHL_OZ_EKR_BU_SERI_NUMARALI__ULASILAMADI QObject::trUtf8("* Bu seri numaralı sertifika ve sahibi hakkındaki bilgilere ulaşılamadı.")
#define DIL_SHL_SIFRE_COZ_BASKA_KULLANICILARA_SIFRELE QObject::trUtf8("Şifre Çöz && Başka Kullanıcılara Şifrele")
#define DIL_SHL_MGM_YARDIMCI QObject::trUtf8("MGM Yardımcı")
#define DIL_SHL_KULLANICIYA_SIFRELENMEMIS_DOSYALARI_GOSTER QObject::trUtf8("Kullanıcıya şifrelenenmemiş dosyaları göster")
#define DIL_SHL_LOG_GOSTER QObject::trUtf8("Log göster")
//////////////////////////////////////////////////////////////////////////
#define DIL_OUTLOOK_MESAJ_FORMATI		QObject::trUtf8("Mesaj Formatı		   :")
#define DIL_OUTLOOK_SMIME				QObject::trUtf8("S/MIME				   :")
#define DIL_OUTLOOK_IMZA				QObject::trUtf8("İmza")
#define DIL_OUTLOOK_IMZA_BILGISI		QObject::trUtf8("İmza Bilgisi		   :")
#define DIL_OUTLOOOK_IMZALAYAN			QObject::trUtf8("İmzalayan			   :")
#define DIL_OUTLOOK_IMZA_DURUMU			QObject::trUtf8("İmza Durumu		   :")
#define DIL_OUTLOOK_IMZALAMA_ZAMANI		QObject::trUtf8("İmzalama Zamanı	   :")
#define DIL_OUTLOOK_OZET_ALGORITMASI	QObject::trUtf8("Özet Algoritması	   :")
#define DIL_OUTLOOK_IMZALAMA_ALGORITMASI QObject::trUtf8("İmzalama Algoritması :")
#define DIL_OUTLOOK_SERTIFIKA_BILGISI	QObject::trUtf8("Sertifika Bilgisi")
#define DIL_OUTLOOK_YAYINLAYAN			QObject::trUtf8("Yayınlayan			   :")
#define DIL_OUTLOOK_SERTIFIKA_DURUMU	QObject::trUtf8("Sertifika Durumu      :")
#define DIL_OUTLOOK_SERTIFIKA_KONTROL_EDILIYOR QObject::trUtf8("Sertifika kontrol ediliyor...")
#define DIL_OUTLOOK_SERTIFIKA_KONTROL_SORUNLU QObject::trUtf8("Sertifika kontrol edilirken hata oluştu")

#define DIL_OUTLOOK_SERTIFIKA_SORUNSUZ QObject::trUtf8("Sertifika Sorunsuz.")

#define DIL_OUTLOOK_SIFRE QObject::trUtf8("Şifre")
#define DIL_OUTLOOK_SIFRELENME_BILGISI QObject::trUtf8("Şifreleme bilgisi")
#define DIL_OUTLOOK_SIFRELEME_DURUMU			 QObject::trUtf8("Şifreleme Durumu			   :")
#define DIL_OUTLOOK_ICERIK_SIFRELEME_ALGORITMASI QObject::trUtf8("İçerik Şifreleme Algoritması :")
#define DIL_OUTLOOK_ANAHTAR_DEGISIM_ALGORITMASI  QObject::trUtf8("Anahtar Değişim Algoritması  :")

#define  DIL_OUTLOOK_SERTIFIKA_GOSTER QObject::trUtf8("Sertifika Göster")
#define  DIL_OUTLOOK_KAPAT			QObject::trUtf8("Kapat")
#define  DIL_OUTLOOK_GECERLI		QObject::trUtf8("Geçerli")

#define DIL_OUTLOOK_1_ALGORITMASI_KULLANILARAK_SIFRELENMISTIR QObject::trUtf8("%1 Algoritması kullanılarak şifrelenmiştir. ")
#define DIL_OUTLOOK_1_ICIN_SIFRELENMISTIR QObject::trUtf8("%1 için şifrelenmiştir.")


#define DIL_OUTLOOK_SERTIFIKA_KONTROLLERI_YAPILIYOR QObject::trUtf8("Sertifika kontrolleri yapılıyor.")
#define DIL_OUTLOOK_LUTFEN_BEKLEYINIZ QObject::trUtf8("Lütfen bekleyiniz...")
#define DIL_OUTLOOK_SIL_GUNCELLEME_ISLEMI_YAPILIYOR QObject::trUtf8("SİL güncelleme işlemi yapılıyor.")
#define DIL_OUTLOOK_LISANS_KONTROLU_YAPILIYOR QObject::trUtf8("Lisans kontrolü yapılıyor.")


#define DIL_OUTLOOK_IMZALAMA_SERTIFIKASI_GECERSIZ QObject::trUtf8("İmzalama sertifikası geçersiz.")
#define DIL_OUTLOOK_SIFRELEME_SERTIFIKASI_GECERSIZ QObject::trUtf8("Şifreleme sertifikası geçersiz.")

#define DIL_1_TARAFINDAN_2_TARIHINDE_3_KULLANILARAK_IMZALANDI QObject::trUtf8("%1 tarafından %2 tarihinde %3 kullanılarak imzalandı.")
#define DIL_OUTLOOK_IMZACI QObject::trUtf8("İmzacı")
#define DIL_OUTLOOK_IMZALAMA_KATMANI QObject::trUtf8("İmzalama Katmanı")
#define DIL_OUTLOOK_IMZALANDI QObject::trUtf8("İmzalandı")
#define DIL_OUTLOOK_IMZA_GECERLI QObject::trUtf8("İmza Geçerli")
#define DIL_OUTLOOK_IMZA_GECERSIZ QObject::trUtf8("İmza Geçersiz")
#define DIL_OUTLOOK_SIFRELEME_KATMANI QObject::trUtf8("Şifreleme Katmanı")
#define DIL_OUTLOOK_KONU QObject::trUtf8("Konu")
#define DIL_GNL_HEDEF_DIZIN QObject::trUtf8("Hedef Dizin")
#define DIL_GNL_GUI_GOSTERME QObject::trUtf8("GUI gösterme")
#define DIL_MGM_YARD_UYARI QObject::trUtf8("%1 dizini içerisinde imzali veya imzali&şifreli dosyalar var. Şifreleme esnasında dosyalar bozulabilir. Yeniden taramak için yeniden tara butonuna basınız.  Devam etmek ister misiniz?")

#define DIL_MGM_YARD_KULLANICIYA_SIFRELENMEMIS_DOSYALAR QObject::trUtf8("Kullanıcıya şifrelenmemiş dosyalar")
#define DIL_MGM_YARD_ASAGIDAKI_1_DOSYA_2_KULLANICILARI_ICIN_SIFRELI_DEGILDIR QObject::trUtf8("Aşağıdaki %1 dosya %2 kullanıcıları için şifreli değildir.")
#define DIL_MGM_YARD_ASAGIDAKI_1_DOSYA_2_KULLANICISI_ICIN_SIFRELI_DEGILDIR QObject::trUtf8("Aşağıdaki %1 dosya %2 kullanıcısı için şifreli değildir.")
#define DIL_BTN_KAPAT QObject::trUtf8("Kapat")
#define DIL_MGM_YARD_DOSYA_YOLU QObject::trUtf8("Dosya Yolu")
#define DIL_MGM_YARD_DOSYANIN_YAZILACAGI_KONUMU_BELIRLEYINIZ QObject::trUtf8("Dosyanın yazılacağı konumu belirleyiniz.")
#define DIL_MGM_YARD_KONUMUNDA_DOSYA_OLUSTURULAMADI QObject::trUtf8(" konumunda dosya oluşturulamadı.")
#define DIL_MGM_YARD_KONUMUNDA_DOSYA_OLUSTURULDU QObject::trUtf8(" konumunda dosya oluşturuldu.")


#define DIL_SURUM_BILGI_EKR_SURUM_BILGILERI QObject::trUtf8("Sürüm Bilgileri")
#define DIL_SURUM_BILGI_EKR_MODUL_ADI QObject::trUtf8("Modül Adı")
#define DIL_SURUM_BILGI_EKR_SURUM QObject::trUtf8("Sürüm")
#define DIL_SURUM_BILGI_EKR_KERMEN_PAKETINDE__SURUMLERI QObject::trUtf8("Kermen paketinde bulunan kütüphane ve programların sürümleri")

#define DIL_SIL_GUNCELLEME_SONUC_EKR_SIL_GUNCELLEME_SONUCU QObject::trUtf8("SİL Güncelleme Sonucu")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_SIL_GUNCELLEME_TAMAMLANDI QObject::trUtf8("SİL Güncelleme tamamlandı.")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_GUNCELLENEN_BULUNAMAYAN_SILLERE_ILISKIN_VERILER_ASAGIDADIR QObject::trUtf8("Yayıncılara ait güncellenen ve bulunamayan SİL bilgileri aşağıdadır.")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_LDAP_ADRESI QObject::trUtf8("LDAP Adresi")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_HTTP_ADRESI QObject::trUtf8("HTTP Adresi")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_YAYINCI_ALANI QObject::trUtf8("Yayıncı Alanı")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_SIL_GECERLI QObject::trUtf8("SİL Geçerli")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_SIL_GECERSIZ QObject::trUtf8("SİL Geçersiz")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_DEPOYA_EKLENDI QObject::trUtf8("Depoya Eklendi")
#define DIL_SIL_GUNCELLEME_SONUC_EKR_SIL_BULUNAMADI QObject::trUtf8("SİL Bulunamadı")
#endif

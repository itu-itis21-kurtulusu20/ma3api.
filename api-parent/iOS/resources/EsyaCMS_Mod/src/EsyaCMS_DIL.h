#ifndef  __ESYACMS_DIL_H__
#define  __ESYACMS_DIL_H__
#include <QObject>

#define DIL_GB_TANIMLANMAMIS	QObject::trUtf8("Tanımlanmamış")
#define DIL_GB_GIZLI			QObject::trUtf8("Gizli")
#define DIL_GB_OZEL				QObject::trUtf8("Özel")
#define DIL_GB_HIZMETEOZEL		QObject::trUtf8("Hizmete Özel")
#define DIL_GB_TASNIFDISI		QObject::trUtf8("Tasnif Dışı")

#define DIL_IMZ_KRIPTO_KONTROLU QObject::trUtf8("İmza kripto kontrolü")
#define DIL_IMZ_OZET_KONTROLU QObject::trUtf8("İmza özet kontrolü")
#define DIL_IMZALI_OZELLIKLER_KONTROLU QObject::trUtf8("İmzalı özellikler kontrolü")

#define LBL_KONTROLADI			QObject::trUtf8("Kontrol Adı        :")
#define LBL_DOGRULAMASONUCU		QObject::trUtf8("Doğrulama Sonucu   :")
#define LBL_KONTROLSONUCU		QObject::trUtf8("Kontrol Sonucu     :")
#define LBL_ACIKLAMA			QObject::trUtf8("Açıklama           :"			)
#define LBL_IMZACIKONTROLLERI	QObject::trUtf8("Imzacı Kontrolleri")

#define A_ALT_IMZACI_KONTROLLERI_SORUNLU	QObject::trUtf8("İmzacı Kontrolleri Sorunlu")
#define A_KONTROLLER_SORUNLU				QObject::trUtf8("Kontroller Sorunlu")
#define A_GECERLI							QObject::trUtf8("Geçerli")
#define A_KONTROL_TAMAMLANAMADI				QObject::trUtf8("Kontrol Tamamlanamadı")


#define A_IMZACI_KONTROLU_1					QObject::trUtf8("%1 : İmzacı Kontrolü")
#define A_IMZALIVERI_KONTROLU				QObject::trUtf8("İmzalı Veri Kontrolü")
#define A_IMZALIVERI_KONTROLU_GECERLI		QObject::trUtf8("İmzali Veri geçerli.")

#define A_BASARILI							QObject::trUtf8("Başarılı")
#define A_BASARISIZ							QObject::trUtf8("Başarısız")

#define A_SIGNING_CERT_NOTFOUND				QObject::trUtf8("İmzaci sertifikası bulunamadı.")
#define A_PARENTDATA_NULL					QObject::trUtf8("Ata İmzalı Veri tanımlı değil.")
#define A_DIGEST_NOT_FOUND					QObject::trUtf8("Imzacı için hesaplanmış mesaj özeti bulunamadı.")
#define A_DIGEST_NOT_MATCH					QObject::trUtf8("Imzacıdaki özeti bilgisi imzalı veri ile uyuşmuyor.")
#define A_PD_NOT_FOUND						QObject::trUtf8("Mesaj içeriği bulunamadı.")
#define A_PDFILE_NOT_FOUND					QObject::trUtf8("Mesaj içerik dosyası bulunamadı.")
#define A_REQUIRED_SIGNED_ATTR_NOT_FOUND_1	QObject::trUtf8("Gerekli İmzalı Özellik bulunamadı: %1.")
#define A_EXCLUDED_SIGNED_ATTR_FOUND_1		QObject::trUtf8("Yasaklanmış bir imzalı özellik bulundu: %1.")
#define A_INVALID_SIGNED_ATTR_FOUND_1		QObject::trUtf8("Geçersiz bir imzalı özellik bulundu: %1.")

#endif

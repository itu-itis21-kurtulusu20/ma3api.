#ifndef _E_XML_LISANS_KONTROLCU_H_
#define _E_XML_LISANS_KONTROLCU_H_

#define EXML_LISANS_KONTROLCU_INSTANCE EXMLLisansKontrolcu::getInstance()
#define EXML_LISANS_KONTROLCU_CALISABILIR_MI(x) EXML_LISANS_KONTROLCU_INSTANCE->fonksiyonCalisabilirMiShort(x)

#include "ELisansIcerigi.h"
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EXMLLisansKontrolcu
{
public:
	enum LISANS_KONTROL_SONUCU
	{
		GECERLI=0,
		DOSYA_BULUNAMADI=1,
		IMZALAYAN_CERT_SORUNLU=2,
		IMZA_SORUNLU=3,
		TIP_UYUMSUZ=4,
		TARIH_GECERLILIK_BASLAMAMIS=5,
		TARIH_GECERLILIK_BITMIS=6
	};
private:
	ELisansIcerigi mLisansIcerigi;
	static EXMLLisansKontrolcu * mpInstance;
	bool mKontrolEdildi;
	bool mLisansGecerli;
	bool mGUIGoster;
	bool _imzaKontroluYap(const QString & iLisansDosyaYolu);
	bool _lisansDosyasiYukle(const QString & iLisansDosyaYolu);
	bool _lisansIcerigiBelirle(const QByteArray & imzasizLisansIcerigi);

	EXMLLisansKontrolcu::LISANS_KONTROL_SONUCU mKontrolSonucu;
	QString mSonHataStr;

	bool _tipKontroluYap();
	bool _tarihKontroluYap();
	void _hataMesajiGoster();
	bool _akilliKartKontroluYap();

public:	
	EXMLLisansKontrolcu::LISANS_KONTROL_SONUCU kontrolSonucuAl() const;
	bool lisansKontrolEt();
	EXMLLisansKontrolcu(void);
	~EXMLLisansKontrolcu(void);
	static EXMLLisansKontrolcu * getInstance();
	bool gecerlilikKontroluYap();
	bool fonksiyonKontroluYap(int iFonksiyonKod);

	QPair<bool,QString> fonksiyonCalisabilirMi(int iFonksiyonKod);
	bool fonksiyonCalisabilirMiShort(int iFonksiyonKod);

	const QMap<int,QString> &  calisamayanFonkListesiAl();
	void setGUIGoster(bool iGUIGoster);
	QString getSonHataStr() const;
	const ELisansIcerigi & getLisansIcerigi() const;
	ELisansIcerigi getLisansIcerigi();
};
NAMESPACE_END
#endif

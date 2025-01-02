#ifndef __EAYARGDMDIZIN_H__
#define __EAYARGDMDIZIN_H__

#include "esyaOrtak.h"
#include <QString>
#include "EAyarGDMOzneBilgisi.h"

NAMESPACE_BEGIN(esya)

/**
 * \ingroup EsyaOrtakDLL
 *
 * GDM altýndaki dizin bilgilerini tutmak için kullanýlan entry sýnýfý 
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 *
 * \par license
 * This code is absolutely free to use and modify. The code is provided "as is" with
 * no expressed or implied warranty. The author accepts no liability if it causes
 * any damage to your computer, causes your pet to fall ill, increases baldness
 * or makes your car start emitting strange noises when you start it up.
 * This code has no bugs, just undocumented features!
 * 
 * \todo 
 *
 * \bug 
 *
 */
class Q_DECL_EXPORT EAyarGDMDizinBilgisi
{
public:
	enum AyarOzneSecimTipi {
		OSTListeyeGore=0, //Bu tip ise DizinOzne tablosuna bakilacak
		OSTKullaniciyaSor=1,
		OSTOkumaHakkinaGore=2,
		OSTAktifKullaniciyaGore=3
	};

	enum AyarDizinTipi {
		DTImzali=0,
		DTSifreli=1,
		DTImzaliSifreli=2
	};

	enum AyarDosyaSecimTipi {
		DST_HEPSI=0,
		DST_SAHIPOLUNAN=1
	};

	enum AyarUzerineYazmaTipi {
		UYT_KULLANICIYASOR=0,
		UYT_UZERINEYAZ=1
	};

	EAyarGDMDizinBilgisi();
	EAyarGDMDizinBilgisi(int iDizinID,
		AyarDizinTipi iDizinTipi,
		const QString &irDizinYolu,
		EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi,
		AyarDosyaSecimTipi iDosyaSecimTipi,
		AyarUzerineYazmaTipi iUzerineYazmaTipi,
		bool iDegistirilebilir);
	
	EAyarGDMDizinBilgisi(const EAyarGDMDizinBilgisi & iDB);

	EAyarGDMDizinBilgisi & operator=(const EAyarGDMDizinBilgisi& );


	int getDizinID() const;
	AyarDizinTipi getDizinTipi() const;
	const QString &getDizinYolu() const;
	EAyarGDMDizinBilgisi::AyarOzneSecimTipi getOzneSecimTipi() const;
	AyarDosyaSecimTipi getDosyaSecimTipi() const;
	AyarUzerineYazmaTipi getUzerineYazmaTipi() const;
	bool getDegistirilebilir() const;

	void setDizinYolu(const QString &) ;
	void setDizinTipi(AyarDizinTipi ) ;
	void setOzneSecimTipi(EAyarGDMDizinBilgisi::AyarOzneSecimTipi ) ;
	void setDosyaSecimTipi(AyarDosyaSecimTipi ) ;
	void setUzerineYazmaTipi(AyarUzerineYazmaTipi ) ;

	//GDM Dizinleri ile ilgili fonksiyonlar	
	static bool dizinVarmi(const QString& irDizinYolu);	
	static bool dizinVarmi(const qlonglong& irDizinID);
	static QList<EAyarGDMDizinBilgisi> gdmOnTanimliDizinleriAl();
	static QList<EAyarGDMDizinBilgisi> gdmDizinleriAl();
	static EAyarGDMDizinBilgisi gdmDizinAl(const QString& irDizinYolu);
	static EAyarGDMDizinBilgisi gdmDizinAl(int iDizinID);
	static void dizinSil(const EAyarGDMDizinBilgisi & iDB);
	static void dizinEkle(EAyarGDMDizinBilgisi::AyarDizinTipi iDizinTipi,const QString &irDizinYolu,EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi, AyarDosyaSecimTipi iDST = DST_HEPSI,AyarUzerineYazmaTipi iUYT = UYT_KULLANICIYASOR);
	static void dizinGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi);
	static void dizinOzneleriniSil(const EAyarGDMDizinBilgisi& irDizin);
	static void dizinOzneleriniGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi, const QList<EAyarGDMOzneBilgisi> & irOzneListesi );

private:
	int mDizinID;
	AyarDizinTipi mDizinTipi;
	QString mDizinYolu;
	EAyarGDMDizinBilgisi::AyarOzneSecimTipi mOzneSecimTipi;
	AyarDosyaSecimTipi mDosyaSecimTipi;
	AyarUzerineYazmaTipi mUzerineYazmaTipi;
	bool mDegistirilebilir;
};

NAMESPACE_END

#endif


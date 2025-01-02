#ifndef __EAYARLAR_H__
#define __EAYARLAR_H__

#include "esyaOrtak.h"
#include <QString>
//#include <QSqlDatabase>
#include <QVariant>
#include "EAyarException.h"
#include <QPair>
#include <QList>

#define KERMEN_GENEL_AYARLAR_FILE_NAME "egenelayarlar.esya" 
#define KERMEN_YEREL_AYARLAR_FILE_NAME "eayarlar.esya"

NAMESPACE_BEGIN(esya)



class Q_DECL_EXPORT EAyar
{
public:
	enum AyarTipleri {AyarBilinmeyenTip,AyarStringTipi=1, AyarTamsayiTipi=2, AyarBooleanTipi=3,AyarTarihTipi=4,AyarAlgoritmaAdiTipi=5,AyarDosyaGizlilikBilgiTipi=6,AyarMailGizlilikSeviyesi=7,AyarLogEsikDegerTipi=8,AyarCalismaDili=9};

	EAyar();	
	EAyar(const QString& irSinif,const QString& irAd,AyarTipleri iTip,const QString& irDeger,bool iDegistirilebilir,bool iGenel,QString iAciklama="",const QString & iBaslik="",bool iEkrandaGoster=true);	
	EAyar(const EAyar & iAyar);
	QString getStringDeger() const;
	int getIntDeger() const;
	bool getBoolDeger() const;

	const QVariant & getDeger() const;
	const QString & getAciklama() const;
	const QString & getSinif() const;
	const QString & getAd() const;
	AyarTipleri getTip() const;
	bool isDegistirilebilir() const;
	bool isGenel() const;
	bool isAyniAyar(const EAyar &) const;

	void setDeger(const QString& irDeger);
	void setDeger(const char *ipString);
	void setDeger(int iDeger);
	void setDeger(bool irDeger);

	void setDegerValue(const QVariant & iValue);


	bool getEkrandaGoster() const;
	QString getBaslik() const;

	void setEkrandaGoster(bool iEkrandaGoster);
	void setBaslik(const QString & iBaslik);

	void setAciklama(const QString & iAciklama){mAciklama=iAciklama;};
	void setIsGenel(bool iIsGenel);	
	void vtyeYaz();
private:
	QString mSinif;
	QString mAd;
	AyarTipleri mTip;
	QVariant mDeger;
	bool mDegistirilebilir;
	bool mGenel;
	QString mAciklama;
	
	//// RMZ //////
	QString mBaslik;
	bool	mEkrandaGoster;
	///////////////

	//void _vtyeYaz();

};
/*


class Q_DECL_EXPORT EAyarlar
{
public:	
	static QString vtName;
	static QString genelvtName;
	static QString getVtName();
	static QString getGenelVtName();
	static void setVtName(const QString & iVtName);
	static void setGenelVtName(const QString & iGenelVtName);
public:
	typedef QList<QPair<QString,QVariant> > ParamList;

	//////////////////////////////////////////////////////////////////////////
	//RMZ tarafýndan eklendi
	//Nedeni sistemin ilk açýlýþýnda hiçbir kullanýcý yokken genel ayarlardan 
	//Bu þekilde okuma yapýlabilir ancak.
	EAyarlar(bool iVarsayilanKullaniciKontroluYap);
	//////////////////////////////////////////////////////////////////////////

	EAyarlar();
	EAyarlar(const QString& irKulEPosta);

	//Kullanici yonetimi
	static QString varsayilanKullaniciAl(int *oKulId=NULL);	

	//RMZ tarafýndan eklendi
	static QString _selectSorguOlustur(const QStringList & iTabloAdlari,const QMap<QString,QString> & iSearhKeyValue,bool isOr=false);
	static void _tablodanSil(const QString & iTabloAdi,const QString & iAlan,const QString & iAlanDegeri);
	/////////////////////////////////////////////////////////////////////////


	//her cesit ayar icin fonksiyonlar
	EAyar ayarAl(const QString& irSinif,const QString&irAd);
	EAyar ayarAl(const QString& irSinif,const QString&irAd,const QString &irDefValue);
	void ayarYaz(const EAyar& irAyar);
	QList<EAyar> ayarlariAl();

	

	void test();

	//static fonksiyonlar
	static bool boolValue(const QVariant&);
	static QString esitlikQuery(const QString &irFieldName,bool iVal);

	//kartlarla ilgili fonksiyonlar
	static QList<EAyarKartlar> kartlariAl();
	static QList<EAyarKartlar* > tumKartlariAl();

	static void yerelvtOlustur(QSqlDatabase& vt);

	static void baglantilariKapat();
private:
	int mKulID;

	static QSqlDatabase mVT;
	static QSqlDatabase mGenelVT;



	static QSqlQuery* _sorguYap(QSqlDatabase& irVT,const QString & irQueryText,const ParamList & iParameters);

	static void _vtDenetle(QSqlDatabase &irVT,const QString &irVTAdi,const QString &irVTDosyaAdi);
	static void _vtDenetlemeleriniYap();

	EAyar *_genelAyarAl(const QString& irSinif,const QString&irAd);
	EAyar *_yerelAyarAl(const QString& irSinif,const QString&irAd);
	void _ayarYaz(const EAyar& irAyar);
	void _ayarDegerDegistir(const EAyar &irAyar);
	void _yeniAyarEkle(const EAyar &irAyar);

	void _ozneDizindeOlmamali(const EAyarGDMOzneBilgisi& irOzne);
	void _ozneGruptaOlmamali(const EAyarGDMOzneBilgisi& irOzne);
	void _grupsaIcindeOzneOlmamali(const EAyarGDMOzneBilgisi& irOzne);

};

*/



NAMESPACE_END

#endif


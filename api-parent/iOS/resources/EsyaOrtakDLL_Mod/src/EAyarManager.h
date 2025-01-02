#ifndef __E_AYAR_MANAGER_H_
#define __E_AYAR_MANAGER_H_

#include "esyaOrtak.h"
#include <QVariant>
#include <QSqlDatabase>
#include "EVeritabani.h"
#include "EAyarlar.h"

NAMESPACE_BEGIN(esya)
class EAyarTool
{
	EAyarTool();
	~EAyarTool();
	static bool getBoolValue();
};

#define AYAR_CONNECTION_NAME "Ayar_Connection"

#define KERMEN_AYAR_TABLO_AD "TBL_AYARLAR"


#define KERMEN_AYAR_TABLO_ALAN_SINIF "Sinif"
#define KERMEN_AYAR_TABLO_ALAN_AD "Ad"
#define KERMEN_AYAR_TABLO_ALAN_TIP "Tip"
#define KERMEN_AYAR_TABLO_ALAN_DEGER "Deger"
#define KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR "Degistirilebilir"
#define KERMEN_AYAR_TABLO_ALAN_Genel "Genel"
#define KERMEN_AYAR_TABLO_ALAN_Aciklama "Aciklama"
#define KERMEN_AYAR_TABLO_ALAN_Baslik "Baslik"
#define KERMEN_AYAR_TABLO_ALAN_EkrandaGoster "EkrandaGoster"


#define KERMEN_AYAR_SORGU_AYAR_HEPSINI_AL_SOL "select * from "
#define KERMEN_AYAR_SORGU_AYAR_AL_SOL "select Tip,Deger,Degistirilebilir,Genel,Aciklama,Baslik,EkrandaGoster from "
#define KERMEN_AYAR_SORGU_AYAR_AL_WHERE " where "
#define KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC " = :"


#define KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SOL "SELECT Sinif,Ad FROM "
#define KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SAG " GROUP BY Sinif,Ad ORDER BY Sinif,Ad"

#define KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SOL "SELECT Sinif FROM "
#define KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SAG " GROUP BY Sinif ORDER BY Sinif"

#define KERMEN_AYAR_SORGU_ALAN_BELIRTEC ":"

#define KERMEN_AYAR_SORGU_AYAR_AL_AND " AND "


#define KERMEN_AYAR_INSERT_SORGU_SOL "INSERT INTO "
#define KERMEN_AYAR_INSERT_SORGU_ALANLAR "Sinif,Ad,Tip,Deger,Degistirilebilir,Genel,Aciklama,Baslik,EkrandaGoster"
#define KERMEN_AYAR_INSERT_SORGU_STM_VALUES " VALUES "


/**
 * \ingroup EsyaOrtak
 *
 * Yerel ve genel ayarlara erismede ortak arayuz sunan sýnýf 
 *
 * \version 1.0
 * first version
 *
 * \date 04-02-2009
 *
 * \author ramazang
 * 
 * 
 * \todo 
 *
 * \bug 
 *
 */ 
class Q_DECL_EXPORT EAyarManager
{		
	static EAyarManager * mInstance;
	
	bool _getBoolValue(const QVariant & iVariant);
	virtual QString _getAlanAdiAyarSinif();
	virtual QString _getAlanAdiAyarAd();		
	QString _ayarAlmaSorguStr();	
	QString _ayarEklemeSorguStr();	
protected:
	virtual QString _getTabloAdi();	
	QString _getUpdateSorgusuStr();	
	QString _getUpdateAllFieldsOnDBQueryStr();
	QString mDBPath;
	ParamList _getUpdateParams(const EAyar & iAyar);
	ParamList _getUpdateAllFieldsOnDBParams(const EAyar & iAyar);
	QSqlDatabase mDb;
	EAyarManager(const QString & iDbPath);	
	virtual QString _getInsertSorguAlanlar();
	virtual QString _getWhereStatement();
	virtual QString _getValuesStatement();

	virtual QString _tumAyarlariAlmaSorguStr();
	virtual QString _sinifAyarlariAlmaSorguStr();

	virtual QString _tumSinifAdAlmaSorguStr();
	virtual QString _tumSinifAlmaSorguStr();

	virtual ParamList getParamList(const QString & iAyarSinif,const QString & iAyarAd);
	virtual ParamList getTumAyarlariAlParamList();

	virtual ParamList siniflaIliskiliAyarlariAlParamList(const QString & iAyarSinif);
	
	virtual ParamList getInsertParamList(const EAyar & iAyar);	
	QString getSQLStmAND();
	QString getSQLParamBelirtec();

	EAyar _getAyarFromQuery(QSqlQuery * ipQuery);
public:			
	QList<EAyar> tumAyarlariAl();
	QList<EAyar> siniflaIliskiliAyarlariAl(const QString & iSinifAdi);
	EAyar getAyar(const QString & iSinifAdi,const QString & iAyarAdi);
	bool  ayarEkle(const EAyar & iAyar);
	virtual bool  ayarGuncelle(const EAyar & iAyar);
	virtual bool  updateAllFieldsOnDB(const EAyar & iAyar);
	bool  tablodanIlgiliKayitlariSil(const QString & iTabloAdi,const QString & iAlanAdi,const QString & iAlanDegeri);

	QList<QPair<QString,QString> >  tumAyarlarSinifAdAl();
	QStringList  tumAyarlarSinifAl();

	static EAyarManager * getInstance(const QString & iDbPath="");
	virtual ~EAyarManager();
	const QString & getDBPath() const { return mDBPath;}	
	EVeritabani vtVer();
};


NAMESPACE_END
#endif


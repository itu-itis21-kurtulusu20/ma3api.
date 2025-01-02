#include "EYerelAyarManager.h"
#include "EAyarKullaniciManager.h"
#include "FileUtil.h"
#include "EsyaOrtak_Ayar.h"
#include <QFile>
#include "EGenelAyarManager.h"

#define SYSTEM_USER_KUL_ID -2

#define KERMEN_AYAR_TABLO_ALAN_KUL_ID "KulID"

#define KERMEN_YEREL_AYAR_DB_FILE_NAME "eayarlar.esya"

NAMESPACE_BEGIN(esya)

void EYerelAyarManager::_genelAyarlardanVersiyonKopyala()
{	
	QString versiyon="";
	try
	{
		EAyar ayar = EGenelAyarManager::getInstance()->getAyar(AYAR_SINIF_SISTEM,AYAR_SISTEM_AYAR_VERSION);
		versiyon=ayar.getStringDeger();
	}	
	catch (EException & exc)
	{
	}	
	if (!versiyon.isEmpty())
	{
		EAyar ayar(AYAR_SINIF_SISTEM,AYAR_SISTEM_AYAR_VERSION,EAyar::AyarStringTipi,versiyon,false,false,"","",false);
		try
		{
			EYerelAyarManager * pYerelAyarMng = (EYerelAyarManager *)EYerelAyarManager::getInstance();
			pYerelAyarMng->setDisaridanVerilenKullaniciKullan(true);
			EAyarKullanici systemUser(SYSTEM_USER_KUL_ID,"SystemUser",false);
			pYerelAyarMng->setDisaridanAyarKullanici(systemUser);
			pYerelAyarMng->ayarEkle(ayar);
			pYerelAyarMng->setDisaridanVerilenKullaniciKullan(false);			
		}
		catch(EException & exc)
		{
		}
	}
}

QString EYerelAyarManager::versiyonAl()
{
	QString versiyon="";
	try
	{
		setDisaridanVerilenKullaniciKullan(true);
		EAyarKullanici systemUser(SYSTEM_USER_KUL_ID,"SystemUser",false);		
		setDisaridanAyarKullanici(systemUser);
		EAyar ayar = getAyar(AYAR_SINIF_SISTEM,AYAR_SISTEM_AYAR_VERSION);
		versiyon = ayar.getStringDeger();
		setDisaridanVerilenKullaniciKullan(false);	
	}
	catch (EException &exc)
	{
	}	
	return versiyon;
}

void EYerelAyarManager::_vtOlustur()
{	
	QStringList tables;
	{
		EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);
		vt.checkVT();
		tables = vt.getTableNameList();
		vt.dbKapat();	
	}

	ParamList params;	
	QString query;
	QSqlQuery * pQuery=NULL;
	EVeritabani db=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{
		if(!tables.contains("TBL_AYARLAR"))
		{
			query = 
				"CREATE TABLE [TBL_AYARLAR] ("
				"[AYARID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
				"[KulID] INTEGER  NOT NULL, "
				"[Sinif] VARCHAR(100)  NOT NULL, "
				"[Ad] VARCHAR(100)  NOT NULL, "
				"[Tip] INTEGER  NOT NULL, "
				"[Deger] VARCHAR(10000)  NOT NULL, "
				"[Degistirilebilir] BOOLEAN  NOT NULL, "
				"[Genel] BOOLEAN  NOT NULL, "
				"[Aciklama] VARCHAR(10000) NULL, "
				"[Baslik] VARCHAR(100) NULL, "
				"[EkrandaGoster] BOOLEAN  NOT NULL"
				"); ";				
			pQuery = db.sorguYap(query,params);			
			DELETE_MEMORY(pQuery);			
		}

		if(!tables.contains("TBL_DIZINBILGISI"))
		{
			query = 
				" CREATE TABLE [TBL_DIZINBILGISI] ( "
				" [DizinID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
				" [KulID] INTEGER  NOT NULL, "
				" [Tip] NUMERIC  NOT NULL, "
				" [DizinYolu] VARCHAR(1000) NOT NULL, "
				" [OzneSecimTipi] NUMERIC  NOT NULL, "
				" [DosyaSecimTipi] NUMERIC NOT NULL, "
				" [UzerineYazmaTipi] NUMERIC NOT NULL, "
				" [Degistirilebilir] BOOLEAN  NOT NULL "
				" ); ";		
			pQuery = db.sorguYap(query,params);				
			DELETE_MEMORY(pQuery);			
		}


		if(!tables.contains("TBL_DIZINOZNE"))
		{
			query = "CREATE TABLE [TBL_DIZINOZNE] ( "
				" [KulID] INTEGER  NOT NULL, "
				" [DizinID] INTEGER  NOT NULL, "
				" [OzneID] INTEGER  NOT NULL, "
				" [Silinebilir] BOOLEAN DEFAULT 'true' NOT NULL "
				" ); ";			
			pQuery = db.sorguYap(query,params);						
			DELETE_MEMORY(pQuery);			
		}

		if(!tables.contains("TBL_VARSAYILANOZNELER"))
		{
			query = "CREATE TABLE [TBL_VARSAYILANOZNELER] ( "
				" [KulID] INTEGER  NOT NULL, "
				" [OzneID] INTEGER  NOT NULL "
				" ); ";				
			
			pQuery = db.sorguYap(query,params);						
			DELETE_MEMORY(pQuery);			
		}

		if(!tables.contains("TBL_GRUPOZNE"))
		{
			query = 
				" CREATE TABLE [TBL_GRUPOZNE] ( "
				" [KulID] INTEGER  NOT NULL, "
				" [GrupOzneID] INTEGER  NOT NULL, "
				" [OzneID] INTEGER  NOT NULL "
				" ); ";			
			pQuery = db.sorguYap(query,params);
			DELETE_MEMORY(pQuery);
		}

		if(!tables.contains(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER))
		{
			query = 
				QString(" CREATE TABLE [%1] ( "
				" [KulID] INTEGER  NOT NULL, "
				" [GrupOzneID] INTEGER  NOT NULL, "
				" [OzneID] INTEGER  NOT NULL "
				" ); ").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER);			
			pQuery = db.sorguYap(query,params);
			DELETE_MEMORY(pQuery);
		}
		if(!tables.contains("TBL_KULLANICILAR"))
		{
			query = 
				" CREATE TABLE [TBL_KULLANICILAR] ( "
				" [KulID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
				" [EPosta] VARCHAR(500)  UNIQUE NOT NULL, "
				" [Varsayilan] BOOLEAN  NOT NULL "
				" ); ";				
			pQuery = db.sorguYap(query,params);
			DELETE_MEMORY(pQuery);			
		}

		if(!tables.contains("TBL_OZNEBILGISI"))
		{
			query = 
				" CREATE TABLE [TBL_OZNEBILGISI] ( "
				" [OzneID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
				" [KulID] INTEGER  NOT NULL, "
				" [OzneTipi] NUMERIC  NOT NULL, "
				" [Ad] VARCHAR(500)  NOT NULL, "
				" [EPosta] VARCHAR(500)  NULL, "
				" [DN] VARCHAR(1000)  NULL "
				" ); ";				
			pQuery = db.sorguYap(query,params);
			DELETE_MEMORY(pQuery);			
		} 

		if(!tables.contains("TBL_ACIKDOSYALAR"))
		{
			query = 
				"CREATE TABLE [TBL_ACIKDOSYALAR] ("
				"[DosyaID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
				"[KulID] INTEGER  NOT NULL, "
				"[DosyaYolu] VARCHAR(10000) UNIQUE NOT NULL, "
				"[Tarih] VARCHAR(30)  NOT NULL, "
				"[Durum] INTEGER  NOT NULL "
				"); ";			
			pQuery = db.sorguYap(query,params);
			DELETE_MEMORY(pQuery);			
		}	
		_genelAyarlardanVersiyonKopyala();
	}
	catch (EException &exc)
	{		
	}	
	DELETE_MEMORY(pQuery);	
}

QMap<QString,EAyarManager *> EYerelAyarManager::mInstanceMap=QMap<QString,EAyarManager*>();
QString EYerelAyarManager::msYerelAyarFilePath;

EYerelAyarManager::EYerelAyarManager(const QString & iDbPath)
:EAyarManager(iDbPath),mDisaridanVerilenKullaniciKullan(false)
{			
	if (!QFile::exists(iDbPath))
	{
		_vtOlustur();
	}
}

EAyarManager * EYerelAyarManager::getInstance(const QString & iDbPath/* = */)
{
	QString dbPath=iDbPath;
	if (dbPath.isEmpty())
	{
		if (EYerelAyarManager::msYerelAyarFilePath.isEmpty())
		{
			EYerelAyarManager::msYerelAyarFilePath = FileUtil::yerelAyarPath()+"/"+KERMEN_YEREL_AYAR_DB_FILE_NAME;
		}
		dbPath = EYerelAyarManager::msYerelAyarFilePath;
	}
	if(!EYerelAyarManager::mInstanceMap.contains(dbPath))
	{
		EAyarManager * pAyarManager = new EYerelAyarManager(dbPath);
		EYerelAyarManager::mInstanceMap.insert(dbPath,pAyarManager);
		return pAyarManager;
	}
	else
	{
		return EYerelAyarManager::mInstanceMap.value(dbPath);
	}	
}

void EYerelAyarManager::freeYerelAyarManager()
{
	QList<EAyarManager *> instanceList = mInstanceMap.values();
	int size = instanceList.size();
	for (int i = 0; i < size; ++i)
	{
		DELETE_MEMORY(instanceList[i]);
	}
	mInstanceMap.clear();
}

EYerelAyarManager::~EYerelAyarManager(void)
{

}

ParamList  EYerelAyarManager::getTumAyarlariAlParamList()
{
	ParamList retParamList =EAyarManager::getTumAyarlariAlParamList();
	retParamList.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiKulId()),_varsayilanKullaniciIDAl()));
	return retParamList;
}

ParamList EYerelAyarManager::siniflaIliskiliAyarlariAlParamList(const QString & iAyarSinif)
{
	ParamList retParamList =EAyarManager::siniflaIliskiliAyarlariAlParamList(iAyarSinif);	
	retParamList.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiKulId()),_varsayilanKullaniciIDAl()));
	return retParamList;
}

ParamList EYerelAyarManager::getParamList(const QString &iAyarSinif, const QString &iAyarAd)
{
	ParamList retParamList =EAyarManager::getParamList(iAyarSinif,iAyarAd);
	//<Burda parametre listesine kullanýcýnýn id sini de vermek lazým
	//Eðer kullanýcý id'si bulunamasa Exception atmak
	retParamList.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiKulId()),_varsayilanKullaniciIDAl()));
	return retParamList;
}

int EYerelAyarManager::_varsayilanKullaniciIDAl()
{		
	if(mDisaridanVerilenKullaniciKullan)
	{
		if(!mDisaridanAyarKullanici.isNull())
		{
			return mDisaridanAyarKullanici.getKulId();
		}
	}
	EAyarKullanici aktifKullanici = EAyarKullaniciManager::getInstance()->varsayilanKullaniciGetir(true);
	return aktifKullanici.getKulId();
}

QString EYerelAyarManager::_getAlanAdiKulId()
{
	return KERMEN_AYAR_TABLO_ALAN_KUL_ID;
}

QString EYerelAyarManager::_getValuesStatement()
{
	QString retValuesStm = EAyarManager::_getValuesStatement();
	retValuesStm = QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_KUL_ID)+","+retValuesStm;
	return retValuesStm ;
}

QString EYerelAyarManager::_sinifAyarlariAlmaSorguStr()
{
	QString retWhereStatement = EAyarManager::_sinifAyarlariAlmaSorguStr();
	retWhereStatement+=KERMEN_AYAR_SORGU_AYAR_AL_AND+_getAlanAdiKulId()+getSQLParamBelirtec()+_getAlanAdiKulId();
	return retWhereStatement;
}

QString EYerelAyarManager::_tumAyarlariAlmaSorguStr()
{
	QString retWhereStatement = EAyarManager::_tumAyarlariAlmaSorguStr();
	retWhereStatement+=KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getAlanAdiKulId()+getSQLParamBelirtec()+_getAlanAdiKulId();
	return retWhereStatement;
}

QString EYerelAyarManager::_tumSinifAdAlmaSorguStr()
{
	QString retStatement = KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SOL+_getTabloAdi();
	retStatement+=KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getAlanAdiKulId()+getSQLParamBelirtec()+_getAlanAdiKulId();
	retStatement+=KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SAG;
	return retStatement;
}

QString EYerelAyarManager::_tumSinifAlmaSorguStr()
{
	QString retStatement = KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SOL+_getTabloAdi();
	retStatement+=KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getAlanAdiKulId()+getSQLParamBelirtec()+_getAlanAdiKulId();
	retStatement+=KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SAG;
	return retStatement;
}

QString EYerelAyarManager::_getWhereStatement()
{
	QString retWhereStatement = EAyarManager::_getWhereStatement();
	retWhereStatement+=getSQLStmAND()+_getAlanAdiKulId()+getSQLParamBelirtec()+_getAlanAdiKulId();
	return retWhereStatement;
}

ParamList EYerelAyarManager::getInsertParamList(const EAyar & iAyar)
{
	ParamList retParamList =EAyarManager::getInsertParamList(iAyar);
	//<Burda parametre listesine kullanýcýnýn id sini de vermek lazým
	//Eðer kullanýcý id'si bulunamasa Exception atmak
	QString key=QString(":%1").arg(_getAlanAdiKulId());
	retParamList.append(qMakePair<QString,QVariant>(key,_varsayilanKullaniciIDAl()));	
	return retParamList;
}

QString EYerelAyarManager::_getInsertSorguAlanlar()
{
	QString retAlanlar = EAyarManager::_getInsertSorguAlanlar();
	retAlanlar=_getAlanAdiKulId()+","+retAlanlar;
	return retAlanlar;
}


void EYerelAyarManager::tumKullaniciIcinAyarSil(const QString & iAyarSinif,const QString & iAyarAd)
{
	ParamList params;	
	QString query;
	QSqlQuery * pQuery=NULL;
	EVeritabani db=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{
		query = QString("DELETE FROM %1 WHERE Sinif='%3' AND Ad='%5'").arg("TBL_AYARLAR").arg(iAyarSinif).arg(iAyarAd);
		pQuery = db.sorguYap(query,params);
		DELETE_MEMORY(pQuery);	
	}
	catch(EException & exc)
	{
		DELETE_MEMORY(pQuery);		
	}
}



bool EYerelAyarManager::_tablodanKullaniciBilgiSilHaric(const QString & iTableName,const QString & iKulIDFieldName,const QStringList & iHaricKulIDList)
{
	ParamList params;	
	QString query;
	QSqlQuery * pQuery=NULL;
	EVeritabani db=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{
		query = QString("DELETE FROM %1 WHERE %2 NOT IN (%3)").arg(iTableName).arg(iKulIDFieldName).arg(iHaricKulIDList.join(","));
		pQuery = db.sorguYap(query,params);
		DELETE_MEMORY(pQuery);	
	}
	catch(EException & exc)
	{
		DELETE_MEMORY(pQuery);	
		return false;
	}
	return true;
}

bool EYerelAyarManager::deleteUserInfoHaric(const QList<EAyarKullanici> & iUsers)
{
	QStringList haricKulIDList;
	for (int k=0;k<iUsers.size();k++)
	{
		haricKulIDList<<QString("%1").arg(iUsers.at(k).getKulId());
	}

	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);
	QStringList tableNames= vt.getTableNameList();
	vt.dbKapat();
	for (int k=0;k<tableNames.size();k++)
	{
		QString tableName = tableNames.at(k);
		_tablodanKullaniciBilgiSilHaric(tableName,KERMEN_AYAR_TABLO_ALAN_KUL_ID,haricKulIDList);
	}	
	return true;
}

void EYerelAyarManager::removeAndDeleteAyarManager(const QString & iDbPath)
{
	if (EYerelAyarManager::mInstanceMap.contains(iDbPath))
	{
		EAyarManager * pAyarMng = EYerelAyarManager::mInstanceMap.take(iDbPath);
		DELETE_MEMORY(pAyarMng);		
	}
}


NAMESPACE_END


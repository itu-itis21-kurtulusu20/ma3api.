#include "EAyarGDMDizinBilgiManager.h"
#include "EYerelAyarManager.h"
#include "EOrtamDegiskeni.h"
#include "EAyarValueTool.h"
#include <QFileInfo>

using namespace esya;
QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMDizinBilgiManager*> EAyarGDMDizinBilgiManager::mpInstanceMap=QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMDizinBilgiManager*>();
EAyarGDMDizinBilgiManager::EAyarGDMDizinBilgiManager(EYerelAyarManager * ipYerelAyarManager,EGenelAyarManager * ipGenelAyarManager)
:mpYerelAyarManager(ipYerelAyarManager),mpGenelAyarManager(ipGenelAyarManager)
{
}

EAyarGDMDizinBilgiManager::~EAyarGDMDizinBilgiManager(void)
{
}

EAyarGDMDizinBilgiManager * EAyarGDMDizinBilgiManager::getInstance(EYerelAyarManager * ipYerelAyarManager /* = NULL */,EGenelAyarManager * ipGenelAyarManager/* =NULL */)
{
	EAyarGDMDizinBilgiManager * retMng=NULL;

	EYerelAyarManager * pYerelAyarMng = ipYerelAyarManager;
	if (pYerelAyarMng == NULL)
	{
		pYerelAyarMng = (EYerelAyarManager *)EYerelAyarManager::getInstance();
	}

	EGenelAyarManager * pGenelAyarMng = ipGenelAyarManager;
	if (pGenelAyarMng == NULL)
	{
		pGenelAyarMng = (EGenelAyarManager *) EGenelAyarManager::getInstance();
	}
	QPair<EYerelAyarManager *,EGenelAyarManager * > key = qMakePair(pYerelAyarMng,pGenelAyarMng);

	if (!mpInstanceMap.contains(key))
	{		
		retMng = new EAyarGDMDizinBilgiManager(key.first,key.second);
		mpInstanceMap.insert(key,retMng);	
	}
	return mpInstanceMap.value(key);
}

bool EAyarGDMDizinBilgiManager::dizinVarmi(const QString& irDizinYolu)
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();
	QString query = "select DizinID from TBL_DIZINBILGISI where DizinYolu=:dizinyolu and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":dizinyolu"),irDizinYolu));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),kulId));

	EVeritabani vt = mpYerelAyarManager->vtVer();
	QSqlQuery * pQuery = NULL;	
	bool bulundu = false;
	try
	{
		pQuery = vt.sorguYap(query,params);
		bulundu = pQuery->first();
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}

	DELETE_MEMORY(pQuery);
	return bulundu;
}

bool EAyarGDMDizinBilgiManager::dizinVarmi(const qlonglong& irDizinID)
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();	
	//Verilen dizin ozne secim tipi listeye bak olmali

	QString query = "select DizinID from TBL_DIZINBILGISI where DizinID=:dizinid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":dizinid"),irDizinID));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),kulId));

	QSqlQuery * pQuery = NULL;
	bool bulundu = false;
	EVeritabani vt = mpYerelAyarManager->vtVer();
	try
	{
		pQuery = vt.sorguYap(query,params);
		bulundu = pQuery->first();
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery)
		throw;
	}

	DELETE_MEMORY(pQuery);
	return bulundu;
}

QString EAyarGDMDizinBilgiManager::getSelectQuery(const QString & iTableName, const QString & iFilter) const
{
	QString query = QString("select DizinID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi, UzerineYazmaTipi, Degistirilebilir from %1").arg(iTableName);		
	if (!iFilter.isEmpty())
		query += " WHERE "+iFilter;

	return query;
}

EAyarGDMDizinBilgisi EAyarGDMDizinBilgiManager::getObjectFromQuery(QSqlQuery* pQuery)const
{
	EAyarGDMDizinBilgisi db(pQuery->value(0).toInt(),
		(EAyarGDMDizinBilgisi::AyarDizinTipi)pQuery->value(1).toInt(),
		EOrtamDegiskeni::degiskenliStrCoz(pQuery->value(2).toString()),
		(EAyarGDMDizinBilgisi::AyarOzneSecimTipi)pQuery->value(3).toInt(),
		(EAyarGDMDizinBilgisi::AyarDosyaSecimTipi)pQuery->value(4).toInt(),
		(EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi)pQuery->value(5).toInt(),
		EAyarValueTool::getBoolDeger(pQuery->value(6)));

	return db;
}


QList<EAyarGDMDizinBilgisi> EAyarGDMDizinBilgiManager::gdmOnTanimliDizinleriAl()
{
	QString query = getSelectQuery("TBL_ONTANIMLIGDMDIZINBILGISI");//QString("select DizinID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi, UzerineYazmaTipi, Degistirilebilir from TBL_ONTANIMLIGDMDIZINBILGISI ");
	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpGenelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,ParamList());
		QList<EAyarGDMDizinBilgisi> liste;

		if(!pQuery->first())
			return liste;

		do
		{
			EAyarGDMDizinBilgisi db = getObjectFromQuery(pQuery);
			liste.append(db);
		}while(pQuery->next());
		DELETE_MEMORY(pQuery);
		return liste;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

QList<EAyarGDMDizinBilgisi> EAyarGDMDizinBilgiManager::gdmDizinleriAl()
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();

	QString query = getSelectQuery("TBL_DIZINBILGISI",QString("KulID = %1").arg(kulId)); //QString("select DizinID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi,UzerineYazmaTipi,Degistirilebilir from TBL_DIZINBILGISI where KulID = %1").arg(kulId);

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,ParamList());
		QList<EAyarGDMDizinBilgisi> liste;

		if(!pQuery->first())
			return liste;

		do
		{
			EAyarGDMDizinBilgisi db = getObjectFromQuery(pQuery);
			liste.append(db);
		}while(pQuery->next());

		DELETE_MEMORY(pQuery);
		return liste;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

EAyarGDMDizinBilgisi EAyarGDMDizinBilgiManager::gdmDizinAl(const QString& irDizinYolu)
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();

	QString query = getSelectQuery("TBL_DIZINBILGISI",QString("KulID = %1 and DizinYolu='%2'").arg(kulId).arg(irDizinYolu));

// 	QString query = QString("select DizinID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi,UzerineYazmaTipi,Degistirilebilir from TBL_DIZINBILGISI where KulID = %1 and DizinYolu='%2'")
// 		.arg(kulId).arg(irDizinYolu);

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,ParamList());

		if(!pQuery->first())
		{
			DELETE_MEMORY(pQuery);
			throwAYAREXCEPTION(EAyarException::DizinBulunamadi,QString("%1 isimli dizin bulunamadi").arg(irDizinYolu));
		}

		EAyarGDMDizinBilgisi db = getObjectFromQuery(pQuery);
		if(pQuery->next())
		{
			AYARERRORLOGYAZ(QString("Birden fazla %1 isimli dizin bulundu!").arg(irDizinYolu));\
		}

		DELETE_MEMORY(pQuery);
		return db;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

EAyarGDMDizinBilgisi EAyarGDMDizinBilgiManager::gdmDizinAl(int iDizinID)
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();

	QString query = getSelectQuery("TBL_DIZINBILGISI",QString("KulID = %1 and DizinID=%2").arg(kulId).arg(iDizinID));
// 	QString query = QString("select DizinID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi,UzerineYazmaTipi,Degistirilebilir from TBL_DIZINBILGISI where KulID = %1 and DizinID=%2")
// 		.arg(kulId).arg(iDizinID);

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,ParamList());

		if(!pQuery->first())
		{
			DELETE_MEMORY(pQuery);
			throwAYAREXCEPTION(EAyarException::DizinBulunamadi,QString("%1 nolu dizin bulunamadi").arg(iDizinID));
		}

		EAyarGDMDizinBilgisi db = getObjectFromQuery(pQuery);
		if(pQuery->next())
		{
			AYARERRORLOGYAZ(QString("Birden fazla %1 nolu dizin bulundu!").arg(iDizinID));\
		}

		DELETE_MEMORY(pQuery);
		return db;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}


void EAyarGDMDizinBilgiManager::dizinOzneleriniSil(const EAyarGDMDizinBilgisi& irDizin)
{
	int kulId = mpYerelAyarManager->kullaniciIDAl();

	QString query = QString("delete from TBL_DIZINOZNE where KulID=%1 and DizinId=%2 and ")
		.arg(kulId).arg(irDizin.getDizinID()) +
		EAyarValueTool::getEsitlikStatementBool("Silinebilir",true);


	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);

}

void EAyarGDMDizinBilgiManager::dizinSil(const EAyarGDMDizinBilgisi & iDB)
{
	dizinOzneleriniSil(iDB);
	int kulId = mpYerelAyarManager->kullaniciIDAl();
	QString query = QString("DELETE FROM TBL_DIZINBILGISI where DizinID=%1 and KulID=%2")
		.arg(iDB.getDizinID()).arg(kulId);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);

}


void EAyarGDMDizinBilgiManager::dizinEkle(EAyarGDMDizinBilgisi::AyarDizinTipi iDizinTipi,const QString &irDizinYolu,EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi, EAyarGDMDizinBilgisi::AyarDosyaSecimTipi iDST,EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi iUYT )
{
	if (dizinVarmi(irDizinYolu))
		throwAYAREXCEPTION(EAyarException::DizinYoluZatenVar,QString("%1 dizinyolu zaten tanimli").arg(irDizinYolu));

	int kulId = mpYerelAyarManager->kullaniciIDAl();

	QString query = QString("insert into TBL_DIZINBILGISI(KulID,Tip,DizinYolu,OzneSecimTipi,DosyaSecimTipi,UzerineYazmaTipi,Degistirilebilir) values (%1,%2,'%3',%4,%5,%6,'true')")
		.arg(kulId).arg(iDizinTipi).arg(irDizinYolu).arg(iOzneSecimTipi).arg(iDST).arg(iUYT);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}


void EAyarGDMDizinBilgiManager::dizinGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi)
{
	if (!dizinVarmi(irDizinBilgisi.getDizinID()))
		throwAYAREXCEPTION(EAyarException::DizinBulunamadi,QString("%1 nolu dizin bulunamadi").arg(irDizinBilgisi.getDizinID()));

	//todo buradaki guncelleme islemi incelnmelei
	int kulId = EAyarKullaniciManager::getInstance()->varsayilanKullaniciGetir(true).getKulId();

	QString query = QString("UPDATE TBL_DIZINBILGISI SET Tip=%1 , DizinYolu='%2' , OzneSecimTipi=%3 , DosyaSecimTipi=%4 , UzerineYazmaTipi=%5 WHERE KulID=%6 AND DizinID=%7").arg(irDizinBilgisi.getDizinTipi()).arg(irDizinBilgisi.getDizinYolu()).arg(irDizinBilgisi.getOzneSecimTipi()).arg(irDizinBilgisi.getDosyaSecimTipi()).arg(irDizinBilgisi.getUzerineYazmaTipi()).arg(kulId).arg(irDizinBilgisi.getDizinID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}


void EAyarGDMDizinBilgiManager::dizinOzneleriniGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi, const QList<EAyarGDMOzneBilgisi> & irOzneListesi )
{
	if (!dizinVarmi(irDizinBilgisi.getDizinID()))
		throwAYAREXCEPTION(EAyarException::DizinBulunamadi,QString("%1 nolu dizin bulunamadi").arg(irDizinBilgisi.getDizinID()));

	dizinOzneleriniSil(irDizinBilgisi);
	EAyarGDMOzneBilgisi::ozneEkleDizine(irDizinBilgisi,irOzneListesi);
}



#include "EAyarGDMOzneBilgiManager.h"
#include "EAyarGDMDizinBilgiManager.h"
#include "EAyarValueTool.h"
#include <QFileInfo>
#include "EVeritabani.h"
using namespace esya;
QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMOzneBilgiManager*> EAyarGDMOzneBilgiManager::mpInstanceMap=QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMOzneBilgiManager*>();
EAyarGDMOzneBilgiManager::EAyarGDMOzneBilgiManager(EYerelAyarManager * ipYerelAyarManager,EGenelAyarManager * ipGenelAyarManager)
:mpYerelAyarManager(ipYerelAyarManager),mpGenelAyarManager(ipGenelAyarManager)
{
}

EAyarGDMOzneBilgiManager::~EAyarGDMOzneBilgiManager(void)
{
}

EAyarGDMOzneBilgiManager * EAyarGDMOzneBilgiManager::getInstance(EYerelAyarManager * ipYerelAyarManager /* = NULL */,EGenelAyarManager * ipGenelAyarManager/* =NULL */)
{
	EAyarGDMOzneBilgiManager * retMng=NULL;

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
		retMng = new EAyarGDMOzneBilgiManager(key.first,key.second);
		mpInstanceMap.insert(key,retMng);	
	}
	return mpInstanceMap.value(key);
}

void EAyarGDMOzneBilgiManager::ozneAdiDegistir(const EAyarGDMOzneBilgisi & iOzneBilgi,const QString& irYeniAd)
{
	QString query = QString("update TBL_OZNEBILGISI set Ad='%1' where OzneID=%2 and KulID=%3").arg(irYeniAd).arg(iOzneBilgi.getOzneID()).arg(_getKulID());
	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

int EAyarGDMOzneBilgiManager::_getKulID()
{
	return mpYerelAyarManager->kullaniciIDAl();
}

void EAyarGDMOzneBilgiManager::ozneyiDizindenCikar(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin)
{
	QString query = 
		QString("delete from TBL_DIZINOZNE where KulID=%1 and DizinId=%2 and OzneID=%3 and ")
		.arg(_getKulID()).arg(irDizin.getDizinID()).arg(iOzneBilgi.getOzneID()) +
		EAyarValueTool::getEsitlikStatementBool("Silinebilir",true);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::ozneyiGruptanCikar(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup)
{
	//Verilen ozne bir grup olmali
	if(irGrup.getOzneTipi() != EAyarGDMOzneBilgisi::OTYerelGrup)
		throwAYAREXCEPTION(EAyarException::OzneTipiHatali,QString("Ozne tipi yerel grup olmali"));
	QString query = 
		QString("delete from TBL_GRUPOZNE where KulID=%1 and GrupOzneId=%2 and OzneID=%3 ")
		.arg(_getKulID()).arg(irGrup.getOzneID()).arg(iOzneBilgi.getOzneID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

bool EAyarGDMOzneBilgiManager::ozneDizindeVarMi(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin)
{
	//Verilen dizin ozne secim tipi listeye bak olmali
	if(irDizin.getOzneSecimTipi() != EAyarGDMDizinBilgisi::OSTListeyeGore)
		throwAYAREXCEPTION(EAyarException::DizinTipiHatali,QString("Dizinin ozne secim tipi listeye bak olmali"));

	QString query = "select Silinebilir from TBL_DIZINOZNE where DizinId=:dizinid and OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":dizinid"),irDizin.getDizinID()));
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneBilgi.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	bool bulundu = false;
	EVeritabani vt = mpYerelAyarManager->vtVer();
	try
	{
		pQuery = vt.sorguYap(query,params);
		bulundu = pQuery->first();
		DELETE_MEMORY(pQuery);
		return bulundu;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);	
		throw;
	}
}

bool EAyarGDMOzneBilgiManager::ozneGrubunIcindeMi(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup)
{
	//Verilen ozne bir grup olmali
	if(irGrup.getOzneTipi() != EAyarGDMOzneBilgisi::OTYerelGrup)
		throwAYAREXCEPTION(EAyarException::OzneTipiHatali,QString("Ozne tipi yerel grup olmali"));

	QString query = "select KulID from TBL_GRUPOZNE where GrupOzneID=:grupid and OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":grupid"),irGrup.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneBilgi.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	bool bulundu = false;
	EVeritabani vt = mpYerelAyarManager->vtVer();
	try
	{
		pQuery = vt.sorguYap(query,params);
		bulundu = pQuery->first();
		DELETE_MEMORY(pQuery);
		return bulundu;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);	
		throw;
	}
}

void EAyarGDMOzneBilgiManager::ozneyiGrubaEkle(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup)
{	
	//Ozne bu grupta olmamali. irGrup yerel grup mu kontrolu de icinde yapiliyor.
	if (ozneGrubunIcindeMi(iOzneBilgi,irGrup))
	{
		throwAYAREXCEPTION(EAyarException::OzneZatenVar,QString("Ozne yerel grupta zaten var!"));
	}
	QString query = QString("insert into TBL_GRUPOZNE(KulID,GrupOzneID,OzneID) values (%1,%2,%3)")
		.arg(_getKulID()).arg(irGrup.getOzneID()).arg(iOzneBilgi.getOzneID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::ozneyiDizineEkle(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin,bool iSilinebilir)
{
	if (ozneDizindeVarMi(iOzneBilgi,irDizin))
	{
		throwAYAREXCEPTION(EAyarException::OzneZatenVar,QString("Ozne dizinde zaten var!"));
	}	

	QString query = QString("insert into TBL_DIZINOZNE(KulID,DizinID,OzneID,Silinebilir) values (%1,%2,%3,'%4')")
		.arg(_getKulID()).arg(irDizin.getDizinID()).arg(iOzneBilgi.getOzneID()).arg(iSilinebilir);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneBul_AdEPosta(const QString &irAd,const QString & iEPosta)
{	
	QString query = QString("select * from TBL_OZNEBILGISI where "
		" Ad='%1' and EPosta='%2' and KulID = '%3'").arg(irAd).arg(iEPosta).arg(_getKulID());
	return _sorgudanOzneleriAl(query,ParamList());
}


QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneBul_Ad(const QString &irAd)
{	
	QString query = QString("select * from TBL_OZNEBILGISI where "
		" Ad='%1'  and KulID = '%2'").arg(irAd).arg(_getKulID());
	return _sorgudanOzneleriAl(query,ParamList());
}


QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneBul_Ad_Tip(const QString& irOzneAdi,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi)
{		
	QString query = "select * from TBL_OZNEBILGISI where Ad=:ozneadi and OzneTipi=:oznetipi and KulID=:kulid";

	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneadi"),irOzneAdi));
	params.append(qMakePair<QString,QVariant>(QString(":ozneatipi"),iOzneTipi));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));
	
	return _sorgudanOzneleriAl(query,params);
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneBul_Tipler(const QList<EAyarGDMOzneBilgisi::AyarOzneTipi> & irOzneTipleri)
{	
	QString query = QString("select * from TBL_OZNEBILGISI "
		" where KulID = %1 ").arg(_getKulID());

	QString tipSecimi = "";
	if(irOzneTipleri.size()>0)
	{
		tipSecimi = QString("AND (OzneTipi=%1 ").arg(irOzneTipleri[0]);
		for(int i = 1; i < irOzneTipleri.size() ; i++)
		{
			tipSecimi += QString("OR OzneTipi=%1 ").arg(irOzneTipleri[i]);
		}
		tipSecimi+=")";
	}

	return _sorgudanOzneleriAl(query+tipSecimi,ParamList());
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneBul_OzneID(int iOzneID)
{	
	QString query = "select * from TBL_OZNEBILGISI where OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneID));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	return _sorgudanOzneleriAl(query,params);	
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::tumOzneleriAl()
{
	QString query = QString("select * from TBL_OZNEBILGISI where KulID = %1").arg(_getKulID());
	return _sorgudanOzneleriAl(query,ParamList());
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::_sorgudanOzneleriAl(const QString & irQueryText,
																		  const ParamList & iParameters)
{
	QList<EAyarGDMOzneBilgisi> retListe;
	EVeritabani vt = mpYerelAyarManager->vtVer();	
	QSqlQuery * pQuery = NULL;
	try
	{
		pQuery = vt.sorguYap(irQueryText,iParameters);
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}

	while (pQuery->next()) 
	{
		retListe << EAyarGDMOzneBilgisi(pQuery->value(0).toInt(),
			(EAyarGDMOzneBilgisi::AyarOzneTipi)pQuery->value(2).toInt(),
			pQuery->value(3).toString(),
			pQuery->value(4).toString(),
			pQuery->value(5).toString());
	}
	DELETE_MEMORY(pQuery);
	return retListe;
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::grupElemanlariniAl(const EAyarGDMOzneBilgisi& irGrup)
{	
	//Verilen ozne bir grup olmali
	if(irGrup.getOzneTipi() != EAyarGDMOzneBilgisi::OTYerelGrup)
		throwAYAREXCEPTION(EAyarException::OzneTipiHatali,QString("Ozne tipi yerel grup olmali"));

	QString query = "select * from TBL_OZNEBILGISI where TBL_OZNEBILGISI.OzneID in "
		"(select TBL_GRUPOZNE.OzneID from TBL_GRUPOZNE where TBL_GRUPOZNE.GrupOzneID = :grupid and TBL_GRUPOZNE.KulID = :kulid1 ) "
		" and KulId = :kulid2";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":grupid"),irGrup.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid1"),_getKulID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid2"),_getKulID()));

	return _sorgudanOzneleriAl(query,params);
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::grupHaricListesiAl(const EAyarGDMOzneBilgisi & iGrupBilgisi)
{		
	QString query = QString("select * from TBL_OZNEBILGISI where TBL_OZNEBILGISI.OzneID in "
		"(select %1.OzneID from %1 where %1.GrupOzneID = :grupid and %1.KulID = :kulid1 )"
		" and KulId = :kulid2").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER);
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":grupid"),iGrupBilgisi.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid1"),_getKulID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid2"),_getKulID()));

	return _sorgudanOzneleriAl(query,params);
}

void EAyarGDMOzneBilgiManager::grupHaricListesineOzneEkle(const EAyarGDMOzneBilgisi & iGrupBilgisi,const EAyarGDMOzneBilgisi& irOzne)
{	
	QList<EAyarGDMOzneBilgisi> haricListesi =grupHaricListesiAl(iGrupBilgisi);
	if (haricListesi.contains(irOzne))
	{
		return;
	}
	
	QString query = QString("insert into %1(KulID,GrupOzneID,OzneID) values (%2,%3,%4)").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER)
		.arg(_getKulID()).arg(iGrupBilgisi.getOzneID()).arg(irOzne.getOzneID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::tumHaricListelerindenOzneSil(const EAyarGDMOzneBilgisi& irOzne)
{
	QString query = 
		QString("delete from %1 where KulID=%2 and (GrupOzneId=%3 or OzneID=%3)").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER)
		.arg(_getKulID()).arg(irOzne.getOzneID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::grupHaricListesindenOzneCikar(const EAyarGDMOzneBilgisi & iGrupBilgisi,const EAyarGDMOzneBilgisi& irOzne)
{	
	QString query = 
		QString("delete from %1 where KulID=%2 and GrupOzneId=%3 and OzneID=%4 ").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER)
		.arg(_getKulID()).arg(iGrupBilgisi.getOzneID()).arg(irOzne.getOzneID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

bool EAyarGDMOzneBilgiManager::haricListelerindeOzneVarMi(const EAyarGDMOzneBilgisi& irOzne)
{	
	bool retValue=false;
	QString query = QString("select KulID from %1 where KulID = :kulid and (OzneID=:ozneid or GrupOzneID=:ozneid)").arg(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER);
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),irOzne.getOzneID()));

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,params);
		if(pQuery->first())
		{
			retValue =true;
		}
		DELETE_MEMORY(pQuery);
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
	return retValue;
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::dizinOzneleriniAl(const EAyarGDMDizinBilgisi& irDizin)
{
	int kulId = EAyarKullaniciManager::getInstance()->varsayilanKullaniciGetir(true).getKulId();
	
	QString query = "select * from TBL_OZNEBILGISI where TBL_OZNEBILGISI.OzneID in "
		"(select OzneID from TBL_DIZINOZNE where DizinID = :dizinid and KulID = :kulid)";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":dizinid"),irDizin.getDizinID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	return _sorgudanOzneleriAl(query,params);
}

void EAyarGDMOzneBilgiManager::ozneYaz(const QString& irAd,const QString& irEPosta,const QString& iDN,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi)
{
	QList<EAyarGDMOzneBilgisi> ozneler = ozneBul_AdEPosta(irAd,irEPosta);
	if (!ozneler.isEmpty())
	{
		// Burada özne varsa üzerine yazacak þekilkde güncelleme yaptýk. 14.06.2013 Yavuz-Dindar
		//throw YENIAYAREXCEPTION(EAyarException::OzneZatenVar,"Ozne zaten ayarlar tablosunda var!");
		const EAyarGDMOzneBilgisi& ob = ozneler[0];
		if ((ob.getOzneTipi() == iOzneTipi) && ( ob.getDN() != iDN  ))
		{
			ozneSil(ozneler[0]);
			ozneYaz(irAd,irEPosta,iDN,iOzneTipi);
		}
		return;
	}
	
	QString query = QString("insert into TBL_OZNEBILGISI(KulID,OzneTipi,Ad,EPosta,DN) values (%1,%2,'%3','%4','%5')")
		.arg(_getKulID()).arg((int)iOzneTipi).arg(irAd).arg(irEPosta).arg(iDN);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

bool EAyarGDMOzneBilgiManager::ozneDizindenSilinebilirMi(const EAyarGDMOzneBilgisi & iOzne,const EAyarGDMDizinBilgisi& irDizin)
{
	QString query = "select Silinebilir from TBL_DIZINOZNE where DizinId=:dizinid and OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":dizinid"),irDizin.getDizinID()));
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzne.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	try
	{
		pQuery = vt.sorguYap(query,params);

		if(!pQuery->first())
		{
			DELETE_MEMORY(pQuery);
			return false;
		}
		do
		{
			if(!EAyarValueTool::getBoolDeger(pQuery->value(0)))
			{
				DELETE_MEMORY(pQuery);
				return false;
			}
		}while(pQuery->next());

		DELETE_MEMORY(pQuery);
		return true;

	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

bool EAyarGDMOzneBilgiManager::ozneHerhangiBirDizindeVarMi(const EAyarGDMOzneBilgisi & iOzne)
{	
	QString query = "select * from TBL_DIZINOZNE where OzneID=:ozneid and KulID=:kulID ";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzne.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulID"),_getKulID()));

	QSqlQuery* pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	bool retValue = false;
	try
	{
		pQuery = vt.sorguYap(query,params);

		if(pQuery->first())
		{
			retValue = true;
		}
		DELETE_MEMORY(pQuery);
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
	return retValue;
}

bool EAyarGDMOzneBilgiManager::ozneHerhangiBirGruptaVarMi(const EAyarGDMOzneBilgisi & iOzne)
{	
	QString query = "select KulID from TBL_GRUPOZNE where OzneID=:ozneid and KulID=:kulID";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzne.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulID"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();

	bool retValue = false;
	try
	{
		pQuery = vt.sorguYap(query,params);
		if(pQuery->first())
		{
			retValue = true;
		}
		DELETE_MEMORY(pQuery);
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
	return retValue;
}

void EAyarGDMOzneBilgiManager::ozneSil(const EAyarGDMOzneBilgisi& irOzne)
{
	QString hataStr = "";
	bool ozneSilinebilirMi = true;
	if(ozneHerhangiBirGruptaVarMi(irOzne))
	{
		hataStr = "Ozne baþka bir grubun içinde olduðundan silinemez.";
		ozneSilinebilirMi = false;
	}

	if(ozneHerhangiBirDizindeVarMi(irOzne))
	{
		hataStr = "Ozne baþka bir dizinde tanýmlý olduðundan silinemez.";
		ozneSilinebilirMi = false;
	}
	
	if(haricListelerindeOzneVarMi(irOzne))
	{
		hataStr = "Ozne hariç listesinden tanýmlý olduðundan silinemez.";
		ozneSilinebilirMi = false;
	}



	if(irOzne.getOzneTipi() == EAyarGDMOzneBilgisi::OTYerelGrup)
	{
		QList<EAyarGDMOzneBilgisi> grupElemanlari = grupElemanlariniAl(irOzne);
		if (!grupElemanlari.isEmpty())
		{
			ozneSilinebilirMi = false;
		}		
	}

	if (!ozneSilinebilirMi)
	{
		throw YENIAYAREXCEPTION(EAyarException::OzneKullanilmis,irOzne.getAd()+"  "+hataStr);
	}

	QString query = 
		QString("delete from TBL_OZNEBILGISI where OzneID=%1 and KulID=%2")
		.arg(irOzne.getOzneID()).arg(_getKulID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::tumAtalariAl(const EAyarGDMOzneBilgisi& iOzneBilgi)
{
	QList<EAyarGDMOzneBilgisi> retAtalar;
	QList<EAyarGDMOzneBilgisi> atalar = ozneyiIcerenGruplariAl(iOzneBilgi);	
	retAtalar=atalar;
	for (int k=0;k<atalar.size();k++)
	{
		EAyarGDMOzneBilgisi ataBilgi = atalar.at(k);
		retAtalar+=tumAtalariAl(ataBilgi);
	}
	return retAtalar;

}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::ozneyiIcerenGruplariAl(const EAyarGDMOzneBilgisi& iOzneBilgi)
{
	QList<EAyarGDMOzneBilgisi> sonuc;

	QString query = "select GrupOzneID from TBL_GRUPOZNE where OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneBilgi.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();
	try
	{
		pQuery = vt.sorguYap(query,params);

		if(pQuery->first())
		{
			do
			{
				sonuc << ozneBul_OzneID(pQuery->value(0).toInt());
			}while(pQuery->next());
		} 
		DELETE_MEMORY(pQuery);
		return sonuc;
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

QList<EAyarGDMDizinBilgisi> EAyarGDMOzneBilgiManager::ozneyiIcerenDizinleriAl(const EAyarGDMOzneBilgisi& iOzneBilgi)
{
	QString query = "select DizinID from TBL_DIZINOZNE where OzneID=:ozneid and KulID=:kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneBilgi.getOzneID()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	QSqlQuery * pQuery = NULL;
	EVeritabani vt = mpYerelAyarManager->vtVer();
	try
	{
		pQuery = vt.sorguYap(query,params);

		QList<EAyarGDMDizinBilgisi> sonuc;
		if(pQuery->first())
		{
			do
			{
				sonuc<<EAyarGDMDizinBilgiManager::getInstance()->gdmDizinAl(pQuery->value(0).toInt());				
			}while(pQuery->next());
		}

		DELETE_MEMORY(pQuery);
		return sonuc;
	}
	catch (...)
	{	
		DELETE_MEMORY(pQuery);
		throw;
	}
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgiManager::gdmVarsayilanOzneleriAl()
{
	QString query = "select * from TBL_OZNEBILGISI where TBL_OZNEBILGISI.OzneID in "
		"(select OzneID from TBL_VARSAYILANOZNELER where KulID = :kulid)";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));

	return _sorgudanOzneleriAl(query,params);
}

bool EAyarGDMOzneBilgiManager::gdmVarsayilanOzneVarmi(int iOzneID)
{
	QString query = "select OzneID from TBL_VARSAYILANOZNELER where KulID = :kulid AND OzneID=:ozneid ";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),_getKulID()));
	params.append(qMakePair<QString,QVariant>(QString(":ozneid"),iOzneID));

	EVeritabani vt = mpYerelAyarManager->vtVer();
	bool bulundu;
	QSqlQuery * pQuery = NULL;
	try
	{
		pQuery = vt.sorguYap(query,params);
		bulundu = pQuery->first();
	}
	catch(...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}

	DELETE_MEMORY(pQuery);

	return bulundu;
}


void EAyarGDMOzneBilgiManager::gdmVarsayilanOzneEkle(int iOzneID)
{
	QList<EAyarGDMOzneBilgisi> bulunanOzneler = ozneBul_OzneID(iOzneID);
	//Ozne tanimli olmali
	if(bulunanOzneler.isEmpty())
		throwAYAREXCEPTION(EAyarException::OzneBulunamadi,QString("Ozne bulunamadi!"));

	if(gdmVarsayilanOzneVarmi(iOzneID))
		throwAYAREXCEPTION(EAyarException::OzneZatenVar,QString("Ozne varsayilan listesinde zatan var!"));


	QString query = QString("insert into TBL_VARSAYILANOZNELER(KulID,OzneID) values (%1,%2)")
		.arg(_getKulID()).arg(iOzneID);


	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::gdmVarsayilanOzneSil(int iOzneID)
{
	QString query = QString("DELETE FROM TBL_VARSAYILANOZNELER WHERE KulID=%1 AND OzneID=%2")
		.arg(_getKulID()).arg(iOzneID);

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

void EAyarGDMOzneBilgiManager::gdmVarsayilanOzneleriSil()
{
	QString query = QString("DELETE FROM TBL_VARSAYILANOZNELER WHERE KulID=%1")
		.arg(_getKulID());

	EVeritabani vt = mpYerelAyarManager->vtVer();
	vt.sorguCalistir(query);
}

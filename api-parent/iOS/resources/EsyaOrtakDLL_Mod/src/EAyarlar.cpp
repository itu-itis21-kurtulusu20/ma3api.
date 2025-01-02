#include "EAyarlar.h"
#include <QDir>
#include "ELogger.h"
#include <QSqlError>
#include <QSqlQuery>
#include <QSqlRecord>
#include <QMultiHash>
#include "FileUtil.h"
#include "PersistenceFacade.h"
#include "EAyarTanimlari.h"
#include "EYerelAyarManager.h"
#include "EAyarValueTool.h"

#define KERMEN_AYAR_TABLO_ADI_AYARLAR       "TBL_AYARLAR"
#define KERMEN_AYAR_TABLO_ADI_KULLANICILAR  "TBL_KULLANICILAR"
#define KERMEN_AYAR_TABLO_ADI_ACIK_DOSYALAR "TBL_ACIKDOSYALAR"
#define KERMEN_AYAR_TABLO_ADI_DIZIN_BILGISI "TBL_DIZINBILGISI"
#define KERMEN_AYAR_TABLO_ADI_DIZIN_OZNE    "TBL_DIZINOZNE"
#define KERMEN_AYAR_TABLO_ADI_GRUP_OZNE     "TBL_GRUPOZNE"
#define KERMEN_AYAR_TABLO_ADI_OZNE_BILGISI  "TBL_OZNEBILGISI"
#define KERMEN_AYAR_TABLO_ADI_VARSAYILAN_OZNELER "TBL_VARSAYILANOZNELER"



#define KERMEN_AYAR_TABLO_ALAN_ADI_KULLANICI_ID "KulID"
#define KERMEN_AYAR_TABLO_ALAN_ADI_KULLANICI_EPOSTA "EPosta"
#define KERMEN_AYAR_TABLO_ALAN_ADI_VARSAYILAN "Varsayilan"


#define EAYARLARDB "eayarlardb"
#define EGENELAYARLARDB "egenelayarlardb"

#define EAYARLARMODUL "EAyarlar"
#define AYARDEBUGLOGYAZ(mesaj) \
	{ \
	qDebug("%s: %s",EAYARLARMODUL,qPrintable(mesaj));\
	}
#define AYARERRORLOGYAZ(mesaj) \
	{ \
	qCritical("%s: %s",EAYARLARMODUL,qPrintable(mesaj));\
	}

#define throwAYAREXCEPTION(neden,mesaj) \
	{\
	AYARERRORLOGYAZ(mesaj);\
	throw YENIAYAREXCEPTION(neden,mesaj);\
	}


NAMESPACE_BEGIN(esya)
/*
static QString genelvtNamebelirle()
{
	return FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYARLAR_FILE_NAME;
}
static QString vtNamebelirle()
{
	return FileUtil::yerelAyarPath()+"/"+KERMEN_YEREL_AYARLAR_FILE_NAME;
}
*/

//QString EAyarlar::vtName = vtNamebelirle();
//QString EAyarlar::genelvtName = genelvtNamebelirle();



//QSqlDatabase EAyarlar::mVT = QSqlDatabase::database(EAYARLARDB);
//QSqlDatabase EAyarlar::mGenelVT = QSqlDatabase::database(EGENELAYARLARDB);
/*
void EAyarlar::baglantilariKapat()
{
	mVT.close();
	mGenelVT.close();
}

void EAyarlar::_vtDenetle(QSqlDatabase &irVT,const QString &irVTAdi,const QString &irVTDosyaAdi)
{
	QSqlDatabase::addDatabase("QSQLITE");
	if(!irVT.isValid())
	{
		AYARDEBUGLOGYAZ(QString("Ayarlar veritabanina ilk olarak ulasmaya calisiyor. VT adi:%1").arg(irVTAdi));
		irVT = QSqlDatabase::addDatabase("QSQLITE",irVTAdi);
		irVT.setDatabaseName(irVTDosyaAdi);		
	}

	if(!irVT.isOpen())
	{
		AYARDEBUGLOGYAZ(QString("Ayarlar icin veritabani acilacak... VT adi:%1").arg(irVTAdi));
		if(!irVT.open())
		{
			QString errText = irVT.lastError().text();
			AYARERRORLOGYAZ(errText);
			throwAYAREXCEPTION((EAyarException::VTAcilamadi),(QString("Ayarlar veritabani acilamadi... VT adi:%1 Hata:%2").arg(irVTAdi).arg(errText)));
		}
	}
}

void EAyarlar::yerelvtOlustur(QSqlDatabase& vt)
{
	ParamList params;
	QStringList tables = vt.tables();
	QString query;
	QSqlQuery * pQuery;

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
		pQuery = _sorguYap(vt,query,params);
		DELETE_MEMORY(pQuery);
	}

	if(!tables.contains("TBL_DIZINBILGISI"))
	{
		query = 
			" CREATE TABLE [TBL_DIZINBILGISI] ( "
			" [DizinID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
			" [KulID] INTEGER  NOT NULL, "
			" [Tip] NUMERIC  NOT NULL, "
			" [DizinYolu] VARCHAR(1000) UNIQUE NOT NULL, "
			" [OzneSecimTipi] NUMERIC  NOT NULL, "
			" [Degistirilebilir] BOOLEAN  NOT NULL "
			" ); ";

		pQuery = _sorguYap(vt,query,params);
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
		pQuery = _sorguYap(vt,query,params);
		DELETE_MEMORY(pQuery);
	}

	if(!tables.contains("TBL_VARSAYILANOZNELER"))
	{
		query = "CREATE TABLE [TBL_VARSAYILANOZNELER] ( "
			" [KulID] INTEGER  NOT NULL, "
			" [OzneID] INTEGER  NOT NULL "
			" ); ";
		pQuery = _sorguYap(vt,query,params);
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
		pQuery = _sorguYap(vt,query,params);
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
		pQuery = _sorguYap(vt,query,params);
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
		pQuery = _sorguYap(vt,query,params);
		DELETE_MEMORY(pQuery);
	} // if(!tables.contains("TBL_OZNEBILGISI")) 


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
		pQuery = _sorguYap(vt,query,params);
		DELETE_MEMORY(pQuery);
	}


}

void EAyarlar::_vtDenetlemeleriniYap()
{
	_vtDenetle(mVT,EAYARLARDB,vtName);
	yerelvtOlustur(mVT);
	_vtDenetle(mGenelVT,EGENELAYARLARDB,genelvtName);
}

EAyarlar::EAyarlar(bool iVarsayilanKullaniciKontroluYap)
{
	if (iVarsayilanKullaniciKontroluYap)
	{
		varsayilanKullaniciAl(&mKulID); // _vtDenetlemeleriniYap(); icerde yapiliyor..
	}
	else
	{
		_vtDenetlemeleriniYap();		
	}
}
EAyarlar::EAyarlar()
{	
	varsayilanKullaniciAl(&mKulID); // _vtDenetlemeleriniYap(); icerde yapiliyor....
}

EAyarlar::EAyarlar(const QString& irKulEPosta)
{
	_vtDenetle(mVT,EAYARLARDB,vtName);
	_vtDenetle(mGenelVT,EGENELAYARLARDB,genelvtName);

	QString query = QString("select KulID from TBL_KULLANICILAR where EPosta='%1'").arg(irKulEPosta);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	if(!pQuery->first())
	{
		DELETE_MEMORY(pQuery);
		throwAYAREXCEPTION(EAyarException::KullaniciHatali,QString("%1 epostali kullanici bulunamadi!").arg(irKulEPosta));
	}

	mKulID = pQuery->value(0).toInt();

	DELETE_MEMORY(pQuery);
}


bool EAyarValueTool::getBoolDeger(const QVariant& irVal)
{
	QString str = irVal.toString().toLower();
	return  !(str == QLatin1String("n") || str == QLatin1String("0") || str == QLatin1String("false") || str == QLatin1String("False") || str == QLatin1String("FALSE") || str.isEmpty());
}

QString EAyarlar::esitlikQuery(const QString &irFieldName,bool iVal)
{
	return 
		(iVal?QString("NOT ("):QString("("))
		+irFieldName + "='N' OR "+irFieldName + "='n' OR " +irFieldName+"='0' OR "+irFieldName+"='false')";
}

QString EAyarlar::varsayilanKullaniciAl(int *oKulID)
{
	_vtDenetlemeleriniYap();

	QString query = QString("select EPosta,KulID from TBL_KULLANICILAR where ")
		+esitlikQuery("Varsayilan",true);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	if(!pQuery->first())
	{
		DELETE_MEMORY(pQuery);
		throwAYAREXCEPTION(EAyarException::KullaniciHatali,QString("Varsayilan Kullanici bulunamadi"));
	}

	QString eposta = pQuery->value(0).toString();
	int id = pQuery->value(1).toInt();

	if(pQuery->next())
	{
		AYARERRORLOGYAZ(QString("Birden Fazla varsayilan Kullanici bulundu"));\
	}

	if(oKulID)
		*oKulID = id; 
	DELETE_MEMORY(pQuery);
	return eposta;
}

void EAyarlar::_tablodanSil(const QString & iTabloAdi,const QString & iAlan,const QString & iAlanDegeri)
{
	QString query = 
		QString("DELETE FROM %1 WHERE %2=%3")
		.arg(iTabloAdi).arg(iAlan).arg(iAlanDegeri);		
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());
	DELETE_MEMORY(pQuery);
}

QString EAyarlar::_selectSorguOlustur(const QStringList & iTabloAdlari,const QMap<QString,QString> & iSearhKeyValue,bool isOr)
{
	QString lQuery = QString("SELECT * FROM %1").arg(iTabloAdlari.join(","));
	if (!iSearhKeyValue.isEmpty())
	{
		lQuery+=" WHERE ";
	}
	bool lIsFirst=true;
	QMapIterator<QString,QString> lMapItr(iSearhKeyValue);
	while (lMapItr.hasNext())
	{		
		lMapItr.next();
		if (lIsFirst)
		{
			lIsFirst = false;
		}
		else
		{
			if (isOr)
			{
				lQuery+=" OR ";
			}
			else
			{
				lQuery+=" AND ";
			}

		}
		lQuery+=QString(" %1='%2'").arg(lMapItr.key()).arg(lMapItr.value());
	}
	return lQuery;
}
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//Acik dosyalarla ilgili fonksiyonlar...
*/
/*
void EAyarlar::acikDosyaEkle(const QString &irDosyaYolu)
{
	
	QString query = QString("insert into TBL_ACIKDOSYALAR(KulID,DosyaYolu,Tarih,Durum) values (%1,'%2','%3',0)")
		.arg(mKulID).arg(irDosyaYolu).arg(QDateTime::currentDateTime().toString(AYAR_DATEFORMAT));

	QSqlQuery* pQuery = _sorguYap(mVT,query,ParamList());
	DELETE_MEMORY(pQuery);

}


EAyarGDMAcikDosya EAyarlar::acikDosyaAl(const QString &irDosyaYolu)
{
	QString query = QString("select DosyaID,DosyaYolu,Tarih,Durum from TBL_ACIKDOSYALAR where KulID = %1 and DosyaYolu='%2'")
		.arg(mKulID).arg(irDosyaYolu);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	if(!pQuery->first())
	{
		DELETE_MEMORY(pQuery);
		throwAYAREXCEPTION(EAyarException::AcikDosyaBulunamadi,QString("%1 dosya yoluna sahip acik dosya bulunamadi").arg(irDosyaYolu));
	}

	EAyarGDMAcikDosya dosya = EAyarGDMAcikDosya(pQuery->value(0).toInt(),
		pQuery->value(1).toString(),
		QDateTime::fromString(pQuery->value(2).toString(),AYAR_DATEFORMAT),
		(EAyarGDMAcikDosya::DosyaDurumu)pQuery->value(3).toInt()
		);

	if(pQuery->next())
	{
		AYARERRORLOGYAZ(QString("Birden fazla %1 dosya yoluna sahip acik dosya bulundu!").arg(irDosyaYolu));\
	}

	DELETE_MEMORY(pQuery);
	return dosya;

}

QList<EAyarGDMAcikDosya> EAyarlar::acikDosyalariAl()
{
	QString query = QString("select DosyaID,DosyaYolu,Tarih,Durum from TBL_ACIKDOSYALAR where KulID = %1").arg(mKulID);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	QList<EAyarGDMAcikDosya> liste;

	if(!pQuery->first())
		return liste;

	do
	{
		liste << EAyarGDMAcikDosya(pQuery->value(0).toInt(),
			pQuery->value(1).toString(),
			QDateTime::fromString(pQuery->value(2).toString(),AYAR_DATEFORMAT),
			(EAyarGDMAcikDosya::DosyaDurumu)pQuery->value(3).toInt()
			);

	}while(pQuery->next());

	DELETE_MEMORY(pQuery);

	return liste;

}
void EAyarlar::acikDosyaSil(const EAyarGDMAcikDosya& irAcikDosya)
{
	QString query = 
		QString("delete from TBL_ACIKDOSYALAR where KulID=%1 and DosyaID=%2 ")
		.arg(mKulID).arg(irAcikDosya.getDosyaID());

	QSqlQuery* pQuery = _sorguYap(mVT,query,ParamList());
	DELETE_MEMORY(pQuery);
}

void EAyarlar::acikDosyaSil(const QString& irAcikDosyaYolu)
{
	QString query = 
		QString("delete from TBL_ACIKDOSYALAR where KulID=%1 and DosyaYolu='%2' ")
		.arg(mKulID).arg(irAcikDosyaYolu);

	QSqlQuery* pQuery = _sorguYap(mVT,query,ParamList());
	DELETE_MEMORY(pQuery);
}


*/
//////////////////////////////////////////////////////////////////////////

/*

QSqlQuery* EAyarlar::_sorguYap(QSqlDatabase& irVT,const QString & irQueryText,const ParamList & iParameters)
{
	if (!irVT.isOpen() && !irVT.open())
		throwAYAREXCEPTION(EAyarException::VTAcilamadi,QString("VERITABANI ACMA HATASI Hata : %1").arg(irVT.lastError().text()));

	QSqlQuery * pQuery = NULL;
	try
	{
		pQuery = new QSqlQuery(irVT);

		if (!pQuery->prepare(irQueryText))
		{
			QString hataText = pQuery->lastError().text();
			DELETE_MEMORY(pQuery);
			throwAYAREXCEPTION(EAyarException::VTAcilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
		}

		for (int i = 0; i < iParameters.size(); i++)
		{
			pQuery->bindValue(iParameters[i].first,iParameters[i].second);
		}

		if (!pQuery->exec())
		{
			QString hataText = pQuery->lastError().text();
			DELETE_MEMORY(pQuery);
			throwAYAREXCEPTION(EAyarException::VTAcilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
		}
	}
	catch (EException & exc)
	{
		DELETE_MEMORY(pQuery);
	}
	return pQuery;
}




EAyar *EAyarlar::_genelAyarAl(const QString& irSinif,const QString&irAd)
{
	QString query = "select Tip,Deger,Degistirilebilir,Genel,Aciklama,Baslik,EkrandaGoster from TBL_AYARLAR where Sinif = :sinif  AND Ad = :ad ";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":sinif"),irSinif));
	params.append(qMakePair<QString,QVariant>(QString(":ad"),irAd));

	QSqlQuery * pQuery = NULL ;
	EAyar* pAyar = NULL ;
	try
	{
		QSqlQuery * pQuery = _sorguYap(mGenelVT,query,params);

		if(!pQuery->first())
		{
			DELETE_MEMORY(pQuery);
			AYARDEBUGLOGYAZ(QString("Query sonucu bos geldi."));
			return NULL;
		}

		pAyar = new EAyar(irSinif,
			irAd,
			(EAyar::AyarTipleri)pQuery->value(0).toInt(),
			pQuery->value(1).toString(),
			boolValue(pQuery->value(2)),
			boolValue(pQuery->value(3)),
			pQuery->value(4).toString(),
			pQuery->value(5).toString(),
			boolValue(pQuery->value(6))
			);	

		if(pQuery->next())
		{
			DELETE_MEMORY(pQuery);
			DELETE_MEMORY(pAyar);
			AYARDEBUGLOGYAZ(QString("Query sonucu birden fazla geldi."));
			return NULL;
		}

	}
	catch (EException &exc)
	{
		
	}
	
	DELETE_MEMORY(pQuery);
	return pAyar;

}


EAyar *EAyarlar::_yerelAyarAl(const QString& irSinif,const QString&irAd)
{
	QString query = "select Tip,Deger,Degistirilebilir,Genel,Aciklama,Baslik,EkrandaGoster from TBL_AYARLAR where Sinif = :sinif  AND Ad = :ad AND KulID= :kulid ";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":sinif"),irSinif));
	params.append(qMakePair<QString,QVariant>(QString(":ad"),irAd));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),mKulID));

	QSqlQuery * pQuery = _sorguYap(mVT,query,params);

	if(!pQuery->first())
	{
		DELETE_MEMORY(pQuery);
		AYARDEBUGLOGYAZ(QString("Query sonucu bos geldi."));
		return NULL;
	}

	EAyar* pAyar = new EAyar(irSinif,
		irAd,
		(EAyar::AyarTipleri)pQuery->value(0).toInt(),
		pQuery->value(1).toString(),
		boolValue(pQuery->value(2)),
		boolValue(pQuery->value(3)),
		pQuery->value(4).toString(),
		pQuery->value(5).toString(),
		boolValue(pQuery->value(6))
		);

	if(pQuery->next())
	{
		DELETE_MEMORY(pQuery);
		DELETE_MEMORY(pAyar);
		AYARDEBUGLOGYAZ(QString("Query sonucu birden fazla geldi."));
		return NULL;
	}

	DELETE_MEMORY(pQuery);
	return pAyar;

}
	//DELETE_MEMORY(pGenelAyar);
//DELETE_MEMORY(pYerelAyar);

EAyar EAyarlar::ayarAl(const QString& irSinif,const QString&irAd)
{
#define EAyarlar_AYAR_AL_BELLEK_BOSALT 
	EAyar * pGenelAyar = NULL;
	EAyar * pYerelAyar = NULL;
	//ayari genelden almaya calisalim.
	pGenelAyar = _genelAyarAl(irSinif,irAd);

	//ayarin genelde bulunmasi durumu:
	if(pGenelAyar)
	{
		//ayar genelse return edelim. Bu durumda locale bakmaya gerek yok.
		if(pGenelAyar->isGenel())
		{
			EAyarlar_AYAR_AL_BELLEK_BOSALT
			return EAyar(*pGenelAyar);
		}
		//ayar genel degilse, ayari localde arayalim
		pYerelAyar = _yerelAyarAl(irSinif,irAd);
		//ayarin localde bulunmasi durumu:
		if(pYerelAyar)
		{
			//localde bulunan ayar ile genelde bulunan ayar deger disinda ayniysa localdeki degeri donelim.
			if(pGenelAyar->isAyniAyar(*pYerelAyar))
			{
				return EAyar(*pYerelAyar);
				EAyarlar_AYAR_AL_BELLEK_BOSALT
			}			
			//ayni degilse, geneldeki degeri donelim. Yani genel local'i dover...
			else
			{
				EAyarlar_AYAR_AL_BELLEK_BOSALT
				return EAyar(*pGenelAyar);
			}
		}
		//ayarin localde bulunaMAMAsi durumu:
		else
		{
			EAyarlar_AYAR_AL_BELLEK_BOSALT
			//geneldeki degeri donelim.
			return EAyar(*pGenelAyar);
		}
	}
	//ayarin genelde bulunMAMAsi durumu:
	else
	{
		//ayari localde arayalim. 
		pYerelAyar = _yerelAyarAl(irSinif,irAd);
		//bulunmasi durumu:
		if(pYerelAyar)
		{
			//ayar genel olarak isaretlenmisse, hata verelim
			if(pYerelAyar->isGenel())
				throwAYAREXCEPTION(EAyarException::GenelYerelleUyumsuz,QString("%1 sinifinda %2 adindaki ayar yerelde genel olarak isaretlenmis.").arg(irSinif).arg(irAd))
			//genel olarak isaretlenmemisse, degeri donelim
			{
				EAyarlar_AYAR_AL_BELLEK_BOSALT
				return EAyar(*pYerelAyar);
			}			
		}
		//bulamazsak exception atalim.
		else
			throwAYAREXCEPTION(EAyarException::AyarBulunamadi,QString("%1 sinifinda %2 adinda ayar bulunamadi.").arg(irSinif).arg(irAd));
	}
}

EAyar EAyarlar::ayarAl(const QString& irSinif,const QString&irAd,const QString &irDefValue)
{
	try
	{
		return ayarAl(irSinif,irAd);
	}
	catch (EAyarException& ex)
	{
		if(ex.sebep() == EAyarException::AyarBulunamadi)
		{
			return EAyar(irSinif,irAd,EAyar::AyarStringTipi,irDefValue,true,false,"");
		}
		throw ex;
	}
}

void EAyarlar::test()
{
	QString query = QString("select * from TBL_DIZINOZNE where ")+esitlikQuery("Silinebilir",true);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	if(!pQuery->first())
		return;

	QString a,b,c;
	QVariant var;
	int i;
	do
	{
		for(i=0;i<3;i++)
		{
			var = pQuery->value(i);
			a = var.toString();
		}
	}while(pQuery->next());

	DELETE_MEMORY(pQuery);
}


void EAyarlar::_ayarDegerDegistir(const EAyar &irAyar)
{
	QString query = "UPDATE TBL_AYARLAR SET Deger = :deger WHERE Sinif = :sinif AND Ad = :ad AND KulID = :kulid";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":deger"),irAyar.getStringDeger()));
	params.append(qMakePair<QString,QVariant>(QString(":sinif"),irAyar.getSinif()));
	params.append(qMakePair<QString,QVariant>(QString(":ad"),irAyar.getAd()));
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),mKulID));

	QSqlQuery * pQuery = _sorguYap(mVT,query,params);

	DELETE_MEMORY(pQuery);
}

void EAyarlar::_yeniAyarEkle(const EAyar &irAyar)
{
	QString query = "insert into TBL_AYARLAR (KulID,Sinif,Ad,Tip,Deger,Degistirilebilir,Genel,Aciklama,Baslik,EkrandaGoster)"
		"values (:kulid,:sinif,:ad,:tip,:deger,:degistirilebilir,:genel,:aciklama,:baslik,:ekrandagoster)";
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":kulid"),mKulID));
	params.append(qMakePair<QString,QVariant>(QString(":sinif"),irAyar.getSinif()));
	params.append(qMakePair<QString,QVariant>(QString(":ad"),irAyar.getAd()));
	params.append(qMakePair<QString,QVariant>(QString(":tip"),irAyar.getTip()));
	params.append(qMakePair<QString,QVariant>(QString(":deger"),irAyar.getStringDeger()));
	params.append(qMakePair<QString,QVariant>(QString(":degistirilebilir"),irAyar.isDegistirilebilir()));
	params.append(qMakePair<QString,QVariant>(QString(":genel"),irAyar.isGenel()));
	params.append(qMakePair<QString,QVariant>(QString(":aciklama"),irAyar.getAciklama()));
	params.append(qMakePair<QString,QVariant>(QString(":baslik"),irAyar.getBaslik()));
	params.append(qMakePair<QString,QVariant>(QString(":ekrandagoster"),irAyar.getEkrandaGoster()));
	QSqlQuery * pQuery = _sorguYap(mVT,query,params);

	DELETE_MEMORY(pQuery);
}

void EAyarlar::_ayarYaz(const EAyar& irAyar)
{
	EAyar *pAyar = _yerelAyarAl(irAyar.getSinif(),irAyar.getAd());
	bool bulundu = (pAyar);
	DELETE_MEMORY(pAyar);
	if(bulundu)
		_ayarDegerDegistir(irAyar);
	else
		_yeniAyarEkle(irAyar);
}

void EAyarlar::ayarYaz(const EAyar& irAyar)
{
	//Once ayni isimdeki ayari vtden alalim.
	EAyar ayar;
	bool ayarAlindi = true;
	try
	{
		ayar = ayarAl(irAyar.getSinif(),irAyar.getAd());
	}
	catch(EAyarException &ex)
	{
		ayarAlindi = false;
	}
	//ayari alabilme durumu:
	if(ayarAlindi)
	{
		//aldigimiz ayar ile elimizdeki ayarin deger haric tum alanlarinin ayni 
		//oldugunu kontrol edelim. Ayni olmayan alan varsa hata verelim.
		if(!ayar.isAyniAyar(irAyar))
			throwAYAREXCEPTION(EAyarException::GenelYerelleUyumsuz,QString("%1 sinifinda %2 adindaki ayar genelde ve yerelde farkli!").arg(ayar.getSinif()).arg(ayar.getAd()));

		//Eger bu ayar genelse, hata verelim.
		if(ayar.isGenel())
			throwAYAREXCEPTION(EAyarException::DegistirilemezAyar,QString("%1 sinifinda %2 adindaki ayar Genel oldugundan degistirilemez!").arg(ayar.getSinif()).arg(ayar.getAd()));
		//Eger bu ayar degistirilemez ise hata verelim
		if(!ayar.isDegistirilebilir())
			throwAYAREXCEPTION(EAyarException::DegistirilemezAyar,QString("%1 sinifinda %2 adindaki ayar Degistirilemez oldugundan degistirilemez!").arg(ayar.getSinif()).arg(ayar.getAd()));

		//kontrolleri gecti, ayari yazalim
		_ayarYaz(irAyar);
	}
	//ayari alamama durumu
	else
	{
		//bize verilen ayar genelse ya da degistirilemezse hata verelim
		if(irAyar.isGenel())
			throwAYAREXCEPTION(EAyarException::DegistirilemezAyar,QString("%1 sinifinda %2 adindaki ayar Genel oldugundan eklenemez!").arg(irAyar.getSinif()).arg(irAyar.getAd()));
		if(!irAyar.isDegistirilebilir())
			throwAYAREXCEPTION(EAyarException::DegistirilemezAyar,QString("%1 sinifinda %2 adindaki ayar Degistirilemez oldugundan eklenemez!").arg(irAyar.getSinif()).arg(irAyar.getAd()));

		//kontrolleri gecti, ayari yazalim
		_ayarYaz(irAyar);
	}
}


QList<EAyar> EAyarlar::ayarlariAl()
{
	QMultiHash<QString,QString> erisim;
	QString sinif,ad;

	QString query = QString("select Sinif,Ad from TBL_AYARLAR where KulID = %1").arg(mKulID);
	QSqlQuery * pQuery = _sorguYap(mVT,query,ParamList());

	//yerel vt'deki ayar adlarini alalim.
	if(pQuery->first())
	{
		do
		{
			sinif = pQuery->value(0).toString();
			ad = pQuery->value(1).toString();

			if(! (erisim.values(sinif)).contains(ad))
				erisim.insert(sinif,ad);
		}while(pQuery->next());
	}
	DELETE_MEMORY(pQuery);

	//genel vt'deki ayar adlarini alalim
	query = QString("select Sinif,Ad from TBL_AYARLAR");
	pQuery = _sorguYap(mGenelVT,query,ParamList());

	if(pQuery->first())
	{
		do
		{
			sinif = pQuery->value(0).toString();
			ad = pQuery->value(1).toString();

			if(! (erisim.values(sinif)).contains(ad))
				erisim.insert(sinif,ad);
		}while(pQuery->next());
	}
	DELETE_MEMORY(pQuery);

	//elimizde ayar adlari var. Simdi de ayarlari alalim.
	QList<QString> sinifListesi = erisim.keys();
	QList<QString> adListesi = erisim.values();
	int i;
	QList<EAyar> liste;
	for(i = 0 ; i<sinifListesi.size();i++)
	{
		liste << ayarAl(sinifListesi[i],adListesi[i]);
	}

	return liste;
}

//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

QList<EAyarKartlar * > EAyarlar::tumKartlariAl()
{
	_vtDenetlemeleriniYap();

	IMapper * lMapper = PersistenceFacade::getInstance()->getMapper(&mGenelVT,KERMEN_CLASS_EAYAR_KARTLAR);
	QList<QObject * > lRetList = lMapper->getAll();
	DELETE_MEMORY(lMapper)
	QList<EAyarKartlar * > lRetListKartlar;
	foreach(QObject * lObj,lRetList)
	{
		lRetListKartlar.append((EAyarKartlar*)lObj);
	}
	return lRetListKartlar;
}
//////////////////////////////////////////////////////////////////////////
QList<EAyarKartlar> EAyarlar::kartlariAl()
{
	_vtDenetlemeleriniYap();



	QString query = QString("select KartID,KartAdi,KartLib,ImzalamaCSPAdi,SifrelemeCSPAdi from TBL_KARTLAR");
	QSqlQuery * pQuery = _sorguYap(mGenelVT,query,ParamList());

	QList<EAyarKartlar> liste;

	if(!pQuery->first())
		return liste;

	do
	{
		liste << EAyarKartlar(
			pQuery->value(0).toInt(),
			pQuery->value(1).toString(),
			pQuery->value(2).toString(),
			pQuery->value(3).toString(),
			pQuery->value(4).toString()
			);
	}while(pQuery->next());

	DELETE_MEMORY(pQuery);

	return liste;

}
*/
//////////////////////////////////
//////////////////////////////////
//////////////////////////////////
EAyar::EAyar(const QString& irSinif,const QString& irAd,AyarTipleri iTip,const QString& irDeger,bool iDegistirilebilir,bool iGenel,QString iAciklama,const QString & iBaslik,bool iEkrandaGoster)
			 :	mSinif(irSinif),
				mAd(irAd),
				mTip(iTip),
				mDeger(QVariant(irDeger)),
				mDegistirilebilir(iDegistirilebilir),
				mGenel(iGenel),
				mAciklama(iAciklama),
				mBaslik(iBaslik),
				mEkrandaGoster(iEkrandaGoster)
{
	QString lDeger = mDeger.toString();
}

EAyar::EAyar(const EAyar & iAyar)
:
mSinif(iAyar.getSinif()),
mAd(iAyar.getAd()),
mTip(iAyar.getTip()),
mDeger(iAyar.getDeger()),
mDegistirilebilir(iAyar.isDegistirilebilir()),
mGenel(iAyar.isGenel()),
mAciklama(iAyar.getAciklama()),
mBaslik(iAyar.getBaslik()),
mEkrandaGoster(iAyar.getEkrandaGoster())
{	
}


const QVariant & EAyar::getDeger() const
{
	return mDeger;
}

EAyar::EAyar()
			 :mSinif("BilinmeyenSinif"),
			 mAd("BilinmeyenAd"),
			 mTip(AyarBilinmeyenTip),
			 mDegistirilebilir(1),
			 mGenel(0),
			 mAciklama(""),
			 mEkrandaGoster(false),
			 mBaslik("")
{
}

QString EAyar::getStringDeger() const
{
	return mDeger.toString();
}

int EAyar::getIntDeger() const
{
	return mDeger.toInt();
}

bool EAyar::getBoolDeger() const
{
	return EAyarValueTool::getBoolDeger(mDeger);	
}

const QString & EAyar::getAciklama() const
{
	return mAciklama;
}
const QString & EAyar::getSinif() const
{
	return mSinif;
}

const QString & EAyar::getAd() const
{
	return mAd;
}

EAyar::AyarTipleri EAyar::getTip() const
{
	return mTip;
}

bool EAyar::isDegistirilebilir() const
{
	return mDegistirilebilir;
}

bool EAyar::isGenel() const
{
	return mGenel;
}

bool EAyar::isAyniAyar(const EAyar &irAyar) const
{
	return (
		(mSinif == irAyar.mSinif) &&
		(mAd == irAyar.mAd) &&
		(mTip == irAyar.mTip) &&
		(mDegistirilebilir == irAyar.mDegistirilebilir) &&
		(mGenel == irAyar.mGenel) &&
		(mAciklama == irAyar.mAciklama)
		);
}
/*
void EAyar::_vtyeYaz()
{
	EAyarlar().ayarYaz(*this);
}
*/
void EAyar::vtyeYaz()
{
	EYerelAyarManager::getInstance()->ayarGuncelle(*this);
}

void EAyar::setDeger(const QString& irDeger)
{
	mDeger = irDeger;
	EYerelAyarManager::getInstance()->ayarGuncelle(*this);	
}

void EAyar::setDeger(const char *ipString)
{
	setDeger(QString(ipString));
}

void EAyar::setDegerValue(const QVariant & iValue)
{
	mDeger = iValue ;
}

void EAyar::setDeger(int iDeger)
{
	mDeger = iDeger;
	EYerelAyarManager::getInstance()->ayarGuncelle(*this);
}

void EAyar::setDeger(bool iDeger)
{
	mDeger = iDeger;
	EYerelAyarManager::getInstance()->ayarGuncelle(*this);
}

bool EAyar::getEkrandaGoster() const
{
	return mEkrandaGoster;
}

QString EAyar::getBaslik() const
{
	return mBaslik;
}

void EAyar::setEkrandaGoster(bool iEkrandaGoster) 
{
	mEkrandaGoster = iEkrandaGoster ;
}

void EAyar::setBaslik(const QString & iBaslik)
{
	mBaslik = iBaslik ;
}

void EAyar::setIsGenel(bool iIsGenel)
{
	mGenel = iIsGenel ;
}

/*
MUHTEMEL TABLO OLUSTURMA SQL LERI

CREATE TABLE [DizinBilgisi] (
[DizinID] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[Tip] NUMERIC  NOT NULL,
[DizinYolu] VARCHAR(1000)  NOT NULL,
[AyarOzneSecimTipi] NUMERIC  NOT NULL,
[Degistirilebilir] BOOLEAN  NOT NULL
)

CREATE TABLE [DizinOzne] (
[DizinID] INTEGER  NOT NULL,
[OzneID] INTEGER  NOT NULL,
[Silinebilir] BOOLEAN DEFAULT 'TRUE' NULL
)


CREATE TABLE [GrupOzne] (
[GrupOzneID] INTEGER  NOT NULL,
[OzneID] INTEGER  NOT NULL
)



CREATE TABLE [OzneBilgisi] (
[OzneID] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[OzneTipi] NUMERIC  NOT NULL,
[Ad] VARCHAR(500)  NOT NULL,
[EPosta] VARCHAR(500)  NULL,
[DN] VARCHAR(1000)  NULL
)



CREATE TABLE [TBL_AYARLAR] (
[AYARID] INTEGER  PRIMARY KEY NOT NULL,
[Sinif] VARCHAR(100)  NOT NULL,
[Ad] VARCHAR(100)  NOT NULL,
[Tip] INTEGER  NOT NULL,
[Deger] VARCHAR(10000)  NOT NULL,
[Degistirilebilir] BOOLEAN  NOT NULL,
[Genel] BOOLEAN  NOT NULL
[Aciklama] VARCHAR(10000) NULL
)
*/
/*
QString EAyarlar::getVtName()
{
	return EAyarlar::vtName;
}

QString EAyarlar::getGenelVtName()
{
	return EAyarlar::genelvtName;
}

void EAyarlar::setVtName(const QString & iVtName)
{
	EAyarlar::vtName = iVtName ;
}
void EAyarlar::setGenelVtName(const QString & iGenelVtName)
{
	EAyarlar::genelvtName = iGenelVtName ;
}
*/
NAMESPACE_END
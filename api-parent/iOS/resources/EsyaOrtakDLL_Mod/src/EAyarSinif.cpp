#include "EAyarSinif.h"
#include "EGenelAyarManager.h"
#include <QSqlDatabase>
#include "EVeritabani.h"
#include "FileUtil.h"

#define AYAR_SNF_TABLE_CONNECTION "Sinif_Tablosu_Baglanti"

#define AYAR_SNF_TABLE_TABLE_NAME "TBL_AYARSINIF"
#define AYAR_SNF_TABLE_ALAN_ADI_AYAR_ADI "SinifAd"

NAMESPACE_BEGIN(esya)

EAyarSinif::EAyarSinif()
{
}

EAyarSinif::EAyarSinif(const QString & iSinifAd,const QString & iSinifBaslik,const QString & iSinifAciklama)
:mSinifAd(iSinifAd),mSinifBaslik(iSinifBaslik),mSinifAciklama(iSinifAciklama)
{
}

EAyarSinif::~EAyarSinif(void)
{
}

QString sinifAlmaSorguOlustur(const QString &iSinifAd)
{
	return "SELECT * FROM "+QString(AYAR_SNF_TABLE_TABLE_NAME)+" WHERE "+QString(AYAR_SNF_TABLE_ALAN_ADI_AYAR_ADI)+"='"+iSinifAd+"'";
}

EAyarSinif EAyarSinif::getAyarSinifFromAd(const QString &iSinifAd)
{
	EAyarSinif retAyarSinif(iSinifAd,"","");

	QString mDbFilePath = FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME;
	EVeritabani vt = EVeritabani::sqLiteVTUret(mDbFilePath);
	QSqlQuery * pQuery = NULL ;

	ParamList params;
	try
	{							
		pQuery = vt.sorguYap(sinifAlmaSorguOlustur(iSinifAd),params);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);
		ESYA_ORTAK_FUNC_ERROR("Sorgulama sonucunda hata olustu"+exc.printStackTrace())		
		return retAyarSinif;
	}	

	if(pQuery && pQuery->next()) 
	{
		retAyarSinif = EAyarSinif(pQuery->value(0).toString(),pQuery->value(1).toString(),pQuery->value(2).toString());		
	}
	DELETE_MEMORY(pQuery);
	return retAyarSinif;
}

QList<EAyarSinif> EAyarSinif::tumAyarSinifAl()
{
	QList<EAyarSinif> retList;
	QString mDbFilePath = FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME;
	EVeritabani vt = EVeritabani::sqLiteVTUret(mDbFilePath);
	QSqlQuery * pQuery = NULL ;

	ParamList params;
	try
	{							
		pQuery = vt.sorguYap("SELECT * FROM "+QString(AYAR_SNF_TABLE_TABLE_NAME),params);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);
		ESYA_ORTAK_FUNC_ERROR("Sorgulama sonucunda hata olustu"+exc.printStackTrace())				
	}	
	
	while(pQuery && pQuery->next()) 
	{
		retList<<EAyarSinif(pQuery->value(0).toString(),pQuery->value(1).toString(),pQuery->value(2).toString());		
	}
	DELETE_MEMORY(pQuery);
	return retList;
}

QString EAyarSinif::getSinifBaslik() const
{
	return mSinifBaslik;
}

QString EAyarSinif::getSinifAciklama() const
{
	return mSinifAciklama;
}

NAMESPACE_END

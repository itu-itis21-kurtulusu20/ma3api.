#include "EAyarManager.h"
#include "EVeritabani.h"
#include "FileUtil.h"
#include <QFile>

#define KERMEN_TABLO_ILGILI_KAYIT_SILME_SORGU "DELETE FROM %1 WHERE %2=%3"

NAMESPACE_BEGIN(esya)

QString EAyarManager::_getInsertSorguAlanlar()
{
	return KERMEN_AYAR_INSERT_SORGU_ALANLAR;
}

QString EAyarManager::_getValuesStatement()
{
	QString retStm;
	retStm = QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_SINIF)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_AD)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_TIP)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_DEGER)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_Genel)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_Aciklama)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_Baslik)+","+
			 QString("%1%2").arg(KERMEN_AYAR_SORGU_ALAN_BELIRTEC).arg(KERMEN_AYAR_TABLO_ALAN_EkrandaGoster);
	return retStm;
}

EAyarManager * EAyarManager::mInstance = NULL;
/**
 * Ayarlarýn olduðu veritabaný yolu ile ilklendirilir.
 * \param iDbPath 
 * Veritabaný dosyasý yolu 
 */
EAyarManager::EAyarManager(const QString & iDbPath)
:mDBPath(iDbPath)
{		
}

EAyar EAyarManager::getAyar(const QString &iSinifAdi, const QString &iAyarAdi)
{	
	EAyar retAyar;
	ParamList params = getParamList(iSinifAdi,iAyarAdi);	
	QSqlQuery * pQuery = NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);		
	try
	{		
		pQuery = vt.sorguYap(_ayarAlmaSorguStr(),params);
		if(!pQuery->first())
		{
			DELETE_MEMORY(pQuery);				
			throw EAyarException(EAyarException::AyarBulunamadi,QString("Ayar ayarlar veritabanýnda bulunamadý. Ayar Adý : %1").arg(iAyarAdi),__FILE__,__LINE__);				
		}
		retAyar = EAyar(iSinifAdi,iAyarAdi,
			(EAyar::AyarTipleri)pQuery->value(0).toInt(),
			pQuery->value(1).toString(),
			_getBoolValue(pQuery->value(2)),
			_getBoolValue(pQuery->value(3)),
			pQuery->value(4).toString(),
			pQuery->value(5).toString(),
			_getBoolValue(pQuery->value(6))
			);			
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);		
		throw exc;
	}	
	DELETE_MEMORY(pQuery);		
	return retAyar;		
}


EAyarManager::~EAyarManager(void)
{	
}

QString EAyarManager::_getAlanAdiAyarSinif()
{
	return KERMEN_AYAR_TABLO_ALAN_SINIF;
}

QString EAyarManager::_getAlanAdiAyarAd()
{
	return KERMEN_AYAR_TABLO_ALAN_AD;
}

#define KERMEN_AYAR_SORGU_TUM_TABLO "SELECT * FROM "

QString EAyarManager::_tumAyarlariAlmaSorguStr()
{
	QString retSorguStr = KERMEN_AYAR_SORGU_AYAR_HEPSINI_AL_SOL+_getTabloAdi();
	return retSorguStr;
}

QString EAyarManager::_sinifAyarlariAlmaSorguStr()
{
	QString retSorguStr = KERMEN_AYAR_SORGU_AYAR_HEPSINI_AL_SOL+_getTabloAdi();
	retSorguStr+=KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getAlanAdiAyarSinif()+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+_getAlanAdiAyarSinif();
	return retSorguStr;
}

QString EAyarManager::_tumSinifAlmaSorguStr()
{
	QString retSorguStr = KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SOL+_getTabloAdi()+KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AL_SAG;
	return retSorguStr;
}

QString EAyarManager::_tumSinifAdAlmaSorguStr()
{
	QString retSorguStr = KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SOL+_getTabloAdi()+KERMEN_AYAR_SORGU_TUM_AYAR_SINIF_AD_AL_SAG;
	return retSorguStr;
}

QString EAyarManager::_ayarAlmaSorguStr()
{	
	QString retSorguStr = KERMEN_AYAR_SORGU_AYAR_AL_SOL+_getTabloAdi()+KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getWhereStatement();	
	return retSorguStr;
}

QString EAyarManager::_ayarEklemeSorguStr()
{
	QString retSorguStr = KERMEN_AYAR_INSERT_SORGU_SOL+_getTabloAdi()+"("+_getInsertSorguAlanlar()+")"+KERMEN_AYAR_INSERT_SORGU_STM_VALUES+"("+_getValuesStatement()+")";	
	return retSorguStr;
}

QString EAyarManager::_getTabloAdi()
{
	return KERMEN_AYAR_TABLO_AD;
}

bool EAyarManager::_getBoolValue(const QVariant & iVariant)
{
	QString str = iVariant.toString().toLower();
	return  !(str == QLatin1String("n") || str == QLatin1String("0") || str == QLatin1String("false") || str == QLatin1String("False") || str == QLatin1String("FALSE") || str.isEmpty());
}

ParamList  EAyarManager::siniflaIliskiliAyarlariAlParamList(const QString & iAyarSinif)
{
	ParamList retParamList;
	retParamList.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiAyarSinif()),iAyarSinif));
	return retParamList;
}

ParamList  EAyarManager::getTumAyarlariAlParamList()
{
	ParamList retParamList;
	return retParamList;
}

ParamList EAyarManager::getParamList(const QString & iAyarSinif,const QString & iAyarAd)
{
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiAyarSinif()),iAyarSinif));
	params.append(qMakePair<QString,QVariant>(QString(":")+_getAlanAdiAyarAd(),iAyarAd));
	return params;
}

/**
 * SQL sorgusunda where den sonraki kýsmý getirir
 * Bunun fonksiyon olarak yapýlmasýnýn amacý yerel ayarlarda ek olarak KulID 'nin de where de gözönünde bulundurulma gerekliliðidir.
 *
 * \return 
 * Where den sonraki kýsmý döner. Deðerler yok. Sonra doldurulacak
 */
QString EAyarManager::_getWhereStatement()
{
	return _getAlanAdiAyarSinif()+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+_getAlanAdiAyarSinif()+KERMEN_AYAR_SORGU_AYAR_AL_AND+_getAlanAdiAyarAd()+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+_getAlanAdiAyarAd();
}

QString EAyarManager::getSQLStmAND()
{
	return KERMEN_AYAR_SORGU_AYAR_AL_AND;
}

QString EAyarManager::getSQLParamBelirtec()
{
	return KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC;
}

bool EAyarManager::ayarEkle(const EAyar & iAyar)
{
	EAyar retAyar;	
	ParamList params = getInsertParamList(iAyar);	
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{
		pQuery = vt.sorguYap(_ayarEklemeSorguStr(),params);
		DELETE_MEMORY(pQuery);				
		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);			
		return false;
	}		
	return true;	
}

ParamList EAyarManager::getInsertParamList(const EAyar & iAyar)
{
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiAyarSinif()),iAyar.getSinif()));
	params.append(qMakePair<QString,QVariant>(QString(":")+_getAlanAdiAyarAd(),iAyar.getAd()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_TIP,iAyar.getTip()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_DEGER,iAyar.getDeger()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR,iAyar.isDegistirilebilir()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Genel,iAyar.isGenel()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Aciklama,iAyar.getAciklama()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Baslik,iAyar.getBaslik()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_EkrandaGoster,iAyar.getEkrandaGoster()));
	return params;
}

#define KERMEN_AYAR_UPDATE_SORGU_SOL "UPDATE "
#define KERMEN_AYAR_UPDATE_SORGU_SET " SET"

QString EAyarManager::_getUpdateSorgusuStr()
{	
	QString retStm = KERMEN_AYAR_UPDATE_SORGU_SOL+_getTabloAdi()+KERMEN_AYAR_UPDATE_SORGU_SET+" "+	KERMEN_AYAR_TABLO_ALAN_DEGER+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_DEGER+""+KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getWhereStatement();
	return retStm;
}

ParamList EAyarManager::_getUpdateParams(const EAyar & iAyar)
{
	ParamList retParamList = getParamList(iAyar.getSinif(),iAyar.getAd());
	retParamList.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_DEGER,iAyar.getDeger()));	
	return retParamList;
}

QString EAyarManager::_getUpdateAllFieldsOnDBQueryStr()
{
	QString retStm = KERMEN_AYAR_UPDATE_SORGU_SOL+_getTabloAdi()+KERMEN_AYAR_UPDATE_SORGU_SET+" "+
// 					_getAlanAdiAyarSinif()+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+_getAlanAdiAyarSinif()+","+
// 					_getAlanAdiAyarAd()+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+_getAlanAdiAyarAd()+","+
					KERMEN_AYAR_TABLO_ALAN_TIP+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_TIP+","+
					KERMEN_AYAR_TABLO_ALAN_DEGER+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_DEGER+","+
					KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR+","+
					KERMEN_AYAR_TABLO_ALAN_Genel+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_Genel+","+
					KERMEN_AYAR_TABLO_ALAN_Aciklama+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_Aciklama+","+
					KERMEN_AYAR_TABLO_ALAN_Baslik+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_Baslik+","+
					KERMEN_AYAR_TABLO_ALAN_EkrandaGoster+""+KERMEN_AYAR_SORGU_AYAR_AL_BELIRTEC+""+KERMEN_AYAR_TABLO_ALAN_EkrandaGoster+
					 ""+KERMEN_AYAR_SORGU_AYAR_AL_WHERE+_getWhereStatement();
	return retStm;
}

ParamList EAyarManager::_getUpdateAllFieldsOnDBParams(const EAyar & iAyar)
{
	ParamList params;
	params.clear();
	params.append(qMakePair<QString,QVariant>(QString(":"+_getAlanAdiAyarSinif()),iAyar.getSinif()));
	params.append(qMakePair<QString,QVariant>(QString(":")+_getAlanAdiAyarAd(),iAyar.getAd()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_TIP,iAyar.getTip()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_DEGER,iAyar.getDeger()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR,iAyar.isDegistirilebilir()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Genel,iAyar.isGenel()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Aciklama,iAyar.getAciklama()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_Baslik,iAyar.getBaslik()));
	params.append(qMakePair<QString,QVariant>(QString(":")+KERMEN_AYAR_TABLO_ALAN_EkrandaGoster,iAyar.getEkrandaGoster()));
	return params;
}


bool EAyarManager::updateAllFieldsOnDB(const EAyar & iAyar)
{
	bool ayarBulundu=false;
	EAyar bulunanAyar;
	try
	{
		bulunanAyar = getAyar(iAyar.getSinif(),iAyar.getAd());
		ayarBulundu=true;
	}
	catch (EException &exc)
	{		
		ayarBulundu = false;
	}

	if (ayarBulundu)
	{//Eðer ayar tabloda bulunmuþ ise primary key olan hariç tüm alanlarý güncelliyoruz.
		ParamList params = _getUpdateAllFieldsOnDBParams(iAyar);	
		EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
		QSqlQuery * pQuery = NULL ;
		try
		{
			pQuery = vt.sorguYap(_getUpdateAllFieldsOnDBQueryStr(),params);
			DELETE_MEMORY(pQuery);				
		}
		catch (EVeritabaniException &exc)
		{		
			DELETE_MEMORY(pQuery);	
			return false;
		}
	}
	return true;	
}

bool  EAyarManager::ayarGuncelle(const EAyar & iAyar)
{		
	bool ayarBulundu=false;
	EAyar bulunanAyar;
	try
	{
		bulunanAyar = getAyar(iAyar.getSinif(),iAyar.getAd());
		ayarBulundu=true;
	}
	catch (EException &exc)
	{		
		ayarBulundu = false;
	}

	if (!ayarBulundu && iAyar.isDegistirilebilir())
	{//Eðer genel ayar ve yerel ayarlarda kayýtlý deðilse eklenecek
		EAyar ekleAyar(iAyar);
		ekleAyar.setIsGenel(false);
		return ayarEkle(ekleAyar);
	}

	if (!iAyar.isDegistirilebilir())
	{		
		return false;
	}
	
	ParamList params = _getUpdateParams(iAyar);	
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	QSqlQuery * pQuery = NULL ;
	try
	{
		pQuery = vt.sorguYap(_getUpdateSorgusuStr(),params);
		DELETE_MEMORY(pQuery);				
	}
	catch (EVeritabaniException &exc)
	{		
		DELETE_MEMORY(pQuery);	
		return false;
	}		
	return true;	
}

QList<EAyar> EAyarManager::siniflaIliskiliAyarlariAl(const QString & iSinifAdi)
{
	QList<EAyar>   retList;		
	ParamList params = siniflaIliskiliAyarlariAlParamList(iSinifAdi);	
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{		
		pQuery = vt.sorguYap(_sinifAyarlariAlmaSorguStr(),params);
		while(pQuery->next())
		{		
			EAyar retAyar = _getAyarFromQuery(pQuery);					
			retList<<retAyar;
		};		
		DELETE_MEMORY(pQuery);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);		
		throw exc;
	}		
	return retList;	
}

EAyar EAyarManager::_getAyarFromQuery(QSqlQuery * pQuery)
{
	EAyar retAyar = EAyar(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_SINIF)).toString(),
		pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_AD)).toString(),
		(EAyar::AyarTipleri)pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_TIP)).toInt(),
		pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_DEGER)).toString(),
		_getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR))),
		_getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Genel))),
		pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Aciklama)).toString(),
		pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Baslik)).toString(),
		_getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_EkrandaGoster))));
	return retAyar;
}

QList<EAyar> EAyarManager::tumAyarlariAl()
{
	QList<EAyar>   retList;		
	ParamList params = getTumAyarlariAlParamList();	
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{		
		pQuery = vt.sorguYap(_tumAyarlariAlmaSorguStr(),params);
		while(pQuery->next())
		{			
			int sinifFieldIndex = pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_SINIF);
			int adFieldIndex = pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_AD);	
			EAyar retAyar = EAyar(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_SINIF)).toString(),
								  pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_AD)).toString(),
								  (EAyar::AyarTipleri)pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_TIP)).toInt(),
								  pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_DEGER)).toString(),
								 _getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_DEGISTIRILEBILIR))),
								 _getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Genel))),
								 pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Aciklama)).toString(),
								 pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_Baslik)).toString(),
								 _getBoolValue(pQuery->value(pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_EkrandaGoster))));
			retList<<retAyar;
		};		
		DELETE_MEMORY(pQuery);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);		
		throw exc;
	}		
	return retList;		
}

QStringList  EAyarManager::tumAyarlarSinifAl()
{
	QStringList retList;	
	ParamList params = getTumAyarlariAlParamList();	
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{		
		pQuery = vt.sorguYap(_tumSinifAdAlmaSorguStr(),params);
		while(pQuery->next())		
		{
			int sinifFieldIndex = pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_SINIF);			
			QString ayarSinif = pQuery->value(sinifFieldIndex).toString();				
			if (!retList.contains(ayarSinif))
			{
				retList.append(ayarSinif);
			}			
		}
		DELETE_MEMORY(pQuery);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);		
		throw exc;
	}		
	return retList;	
}

QList<QPair<QString,QString> >   EAyarManager::tumAyarlarSinifAdAl()
{
	QList<QPair<QString,QString> >  retList;		
	ParamList params = getTumAyarlariAlParamList();	
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{		
		pQuery = vt.sorguYap(_tumSinifAdAlmaSorguStr(),params);
		while(pQuery->next())
		{
			int sinifFieldIndex = pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_SINIF);
			int adFieldIndex = pQuery->record().indexOf(KERMEN_AYAR_TABLO_ALAN_AD);			
			QString ayarSinif = pQuery->value(sinifFieldIndex).toString();
			QString ayarAd = pQuery->value(adFieldIndex).toString();
			QPair<QString,QString> adSinifPair=qMakePair(ayarSinif,ayarAd);
			if (!retList.contains(adSinifPair))
			{
				retList.append(adSinifPair);
			}			
		}
		DELETE_MEMORY(pQuery);		
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);		
		throw exc;
	}		
	return retList;			
}

EVeritabani EAyarManager::vtVer()
{
	return EVeritabani::sqLiteVTUret(mDBPath);
}

bool EAyarManager::tablodanIlgiliKayitlariSil(const QString & iTabloAdi,const QString & iAlanAdi,const QString & iAlanDegeri)
{
	QSqlQuery * pQuery=NULL;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{
		QString silmeSorgu = QString(KERMEN_TABLO_ILGILI_KAYIT_SILME_SORGU).arg(iTabloAdi).arg(iAlanAdi).arg(iAlanDegeri);		
		pQuery = vt.sorguYap(silmeSorgu,ParamList());
		DELETE_MEMORY(pQuery);			

	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);			
		return false;
	}		
	return true;		
}
NAMESPACE_END
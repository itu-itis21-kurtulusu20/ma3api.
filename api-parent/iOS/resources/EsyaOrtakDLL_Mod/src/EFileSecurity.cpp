

//#include <Q3Dns>
#include "EFileSecurity.h"
#include <QEventLoop>
#include <QTimer>
#include "esyaOrtak.h"
#include "ELogger.h"


using namespace esya;

const QString & EFileSecurityInfo::getFileName()const 
{
	return mFileName;
}

const QString & EFileSecurityInfo::getOwner()const
{
	return mOwner;
}

const QStringList & EFileSecurityInfo::getOkuyanGruplar()const
{
	return mOkuyanGruplar;
}

const QStringList & EFileSecurityInfo::getOkuyanKullanicilar()const
{
	return mOkuyanKullanicilar;
}

void EFileSecurityInfo::setFileName(const QString & iFileName)
{
	mFileName = iFileName;
}

void EFileSecurityInfo::setOwner(const QString & iOwner)
{
	mOwner = iOwner;
}

void EFileSecurityInfo::setOkuyanGruplar(const QStringList & iOkuyanGruplar)
{
	mOkuyanGruplar = iOkuyanGruplar;
}


void EFileSecurityInfo::setOkuyanKullanicilar(const QStringList & iOkuyanKullanicilar)
{
	mOkuyanKullanicilar = iOkuyanKullanicilar;
}




//************************************
// Method:    	getFileSecurityInfo
// Access:    	public 
// Description:	Verilen dosyanýn securit info sunu döner.
// Returns:   	int
// Qualifier: 	
// Parameter: 	const QString & iFileName Dosya adi
// Parameter: 	QStringList & oList sonuc listesi
// Parameter: 	QString & oHata Olusabilecek hata degeri
//************************************
EFileSecurityInfo EFileSecurity::getFileSecurityInfo(const QString & iFileName ,QString & oHata)
{
	DEBUGLOGYAZ("MGM",QString("getFileSecurityInfo : DosyaAdi : %1 BASLADI").arg(iFileName));

	EFileSecurityInfo fsi(iFileName);
	QString hata,owner;
	QStringList okuyanGruplar,okuyanKullanicilar;
	int res= 0;
		
#define hataliCik(hata)\
	oHata= QString("FileSecurityInfo Alinamadi. Hata : %1").arg(hata);\
	return fsi;

#if defined(WIN32)
	
	if (mFileSecurity.ownerAl(iFileName,owner,hata) == FAILURE )
	{
		DEBUGLOGYAZ(ESYAORTAK_MOD,QString("Dosya Owner Bilgisine ulaþýlamadý. Hata: %1").arg(hata));
	}

	if ( mFileSecurity.okumaHakkiOlanlariAl(iFileName,okuyanGruplar,okuyanKullanicilar,hata) == FAILURE ) 
	{
		hataliCik(hata);
	}
	
#else 

	mFileSecurity.setFileName(iFileName);
	if( (mFileSecurity.ownerAl(owner,hata) == FAILURE) || (mFileSecurity.okumaHakkiOlanlariAl(okuyanKullanicilar,hata)==FAILURE ) )
		hataliCik(hata);
	

#endif

	fsi.setOwner(owner);
	fsi.setOkuyanGruplar(okuyanGruplar);
	fsi.setOkuyanKullanicilar(okuyanKullanicilar);

 
	DEBUGLOGYAZ("MGM",QString("getFileSecurityInfo : DosyaAdi : %1 BITTI").arg(iFileName));
	return fsi;
}


QString EFileSecurity::getCurrentUserName(QString & oHata)const
{
#define hataliCik(hata)\
	oHata= QString("UserName Alinamadi. Hata : %1").arg(hata);\
	return QString();

#if defined(WIN32)

	return mFileSecurity.getUserName(oHata);

#else 
	hataliCik("Desteklenmeyen Ýþletim Sistemi..");
#endif

}

/*
QString EFileSecurity::getDNS()
{
	QEventLoop q;

	QTimer tT;
	tT.setSingleShot(true);
	QObject::connect(&tT, SIGNAL(timeout()), &q, SLOT(quit()));
	tT.start(60000); // 60s timeout

	Q3Dns dns("1",Q3Dns::Cname);
	QObject::connect(&dns, SIGNAL(resultsReady()),&q, SLOT(quit()));

	q.exec();

	if(!dns.isWorking())// hala sonuc gelmediyse bos donuyoruz
	{
		QStringList qstList =  dns.qualifiedNames();
		if (!qstList.isEmpty())
			return qstList[0].mid(2);
	}

	return QString();
}
*/

EFileSecurity::EFileSecurity()
{

}

EFileSecurity::~EFileSecurity()
{

}

int EFileSecurity::storeOwnerInfo(const QString& iFileName, QString & oHata)
{
	return mFileSecurity.storeOwnerInfo(iFileName,oHata);
}

int EFileSecurity::restoreOwnerInfo(const QString& iFileName, QString & oHata)
{
	return mFileSecurity.restoreOwnerInfo(iFileName,oHata);
	
}

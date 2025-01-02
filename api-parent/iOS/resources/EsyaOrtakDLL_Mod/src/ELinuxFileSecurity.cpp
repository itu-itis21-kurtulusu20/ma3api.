
#ifndef WIN32



#include "ESambaIstemcisi.h"
#include "ELinuxFileSecurity.h"
#include "esyaOrtak.h"
#include "EException.h"
#include "ELogger.h"
#include <QtNetwork/QNetworkInterface>



using namespace esya;

bool					ELinuxFileSecurity::msMountTableConstructed = false;
QMap<QString,QString>	ELinuxFileSecurity::msMountTable; 


ELinuxFileSecurity::ELinuxFileSecurity()
{
	if (!msMountTableConstructed)
	{
		_constructMountTable();
	}
}

ELinuxFileSecurity::~ELinuxFileSecurity()
{

}

QString ELinuxFileSecurity::_findInMountTable(const QString & iFileName)
{
	DEBUGLOGYAZ("MGM",QString("_findMountTable() BASLADI iFileName: %1").arg(iFileName));

	QString mountedPath, tmppath, path = QFileInfo(iFileName).absolutePath();	

	if (msMountTable.contains(path))// MountTablosunda bulundu
	{
		mountedPath = msMountTable[path];
		DEBUGLOGYAZ("MGM",QString("_findMountTable() Dosya yolu MountTablosunda bulundu iFileName: %1 mountedPath: %2").arg(iFileName).arg(mountedPath));
	}
	else
	{
		QDir d(path);
		while ( d.cdUp() && !d.isRoot())
		{
			tmppath = d.absolutePath();
			DEBUGLOGYAZ("MGM",QString("_findMountTable() Ara dizin MountTablosunda araniyor path: %1").arg(tmppath));
			if (msMountTable.contains(tmppath))
			{
				mountedPath = QString("%1%2").arg(msMountTable[tmppath]).arg(path.mid(tmppath.length()));
				DEBUGLOGYAZ("MGM",QString("_findMountTable() Ara dizin MountTablosunda bulundu path: %1 mountedpath: %2").arg(tmppath).arg(mountedPath));
				DEBUGLOGYAZ("MGM",QString("_findMountTable() MsMountTable[%1] : %2").arg(tmppath).arg(msMountTable[tmppath]));
				break;
			}
		}
	}	

	DEBUGLOGYAZ("MGM",QString("_findMountTable() BITTI iFileName: %1 mountedPath: %2").arg(iFileName).arg(mountedPath));
	msMountTable.insert(path,mountedPath);
	return mountedPath;
}

QString ELinuxFileSecurity::localPrefixOlustur()
{
	QString prefix ;
	QList<QHostAddress> haList = QNetworkInterface::allAddresses();
	
	Q_FOREACH(QHostAddress ha,haList )
	{
		DEBUGLOGYAZ("MGM",QString("IP Address :%1").arg(ha.toString()));
		if ( (ha.toString()!=QString("127.0.0.1")) && ( !ha.toString().isEmpty()) )
		{	
			prefix = ha.toString();
			break;
		}
	}
	if (prefix.isEmpty())
		prefix =SAMBALOCALPREFIX;

	return prefix;
}

void ELinuxFileSecurity::setFileName(const QString& iFileName)
{
	DEBUGLOGYAZ("MGM",QString("setFileName() BASLADI iFileName: %1").arg(iFileName));

	mFileName = iFileName;
	
	QString mountedPath = _findInMountTable(mFileName);
	
	if (mountedPath.isEmpty())
	{
		DEBUGLOGYAZ("MGM",QString("setFileName() mount tablsunda bulunmadý iFileName: %1").arg(QFileInfo(mFileName).fileName()));
		mSambaURL = QString("%1/%2").arg(localPrefixOlustur()).arg(QFileInfo(mFileName).fileName());
	}
	else 
		mSambaURL = QString("%1%2/%3").arg(SAMBAPREFIX).arg(mountedPath).arg(QFileInfo(mFileName).fileName());


	DEBUGLOGYAZ("MGM",QString("setFileName() BITTI mFileName: %1 mSambaURL : %2").arg(mFileName).arg(mSambaURL));
	return;
}

void ELinuxFileSecurity::_constructMountTable()
{
	try
	{
		DEBUGLOGYAZ("MGM","_constructMountTable() BASLADI");

		QFile mtf(MOUNTTABLE_FILE);
		if (!mtf.open(QIODevice::ReadOnly | QIODevice::Text))
		{
			ERRORLOGYAZ("MGM","MountTable Dosyasi acilamadi");
			return;
		}

		QStringList items;
		QString line;

		while (!mtf.atEnd())
		{
			line = mtf.readLine();
			items = line.split(" ");
			if (items.count() > 2)
			{
				DEBUGLOGYAZ("MGM",QString("_constructMountTable() MountPoint eklendi mount: %1 source : %2 ").arg(items[0]).arg(items[1]));
				msMountTable.insert(items[1],items[0]);
			}
		}

		msMountTableConstructed = true;
		DEBUGLOGYAZ("MGM","_constructMountTable() BITTI");
		return;
	}
	catch (EException& exc)
	{
		ERRORLOGYAZ("MGM",exc.printStackTrace());
	}
	catch (...)
	{
		ERRORLOGYAZ("MGM","_constructMountTable():bilinmeyen bir istisna oluþtu");
	}

}

int ELinuxFileSecurity::ownerAl( QString & oOwner , QString & oHata )
{
	try
	{
		DEBUGLOGYAZ("MGM",QString("ownerAl() BASLADI mSambaURL : %1").arg(mSambaURL));

		oOwner = mSMB.ownerAl(mSambaURL);

		DEBUGLOGYAZ("MGM",QString("ownerAl() BITTI mSambaURL : %1 Owner : %2").arg(mSambaURL).arg(oOwner));
	}
	catch (EException & exc )
	{
		oHata = exc.printStackTrace();
		return FAILURE;
	}
	catch (...)
	{
		return FAILURE;
	}
	
	return SUCCESS;
}

int ELinuxFileSecurity::okumaHakkiOlanlariAl(QStringList & oOkuyanlar, QString & oHata )
{
	try
	{
		DEBUGLOGYAZ("MGM",QString("ownerAl() BASLADI mSambaURL : %1").arg(mSambaURL));
		
		oOkuyanlar = mSMB.okuyanlariAl(mSambaURL);

		DEBUGLOGYAZ("MGM",QString("ownerAl() BITTI mSambaURL : %1 okuyanSayisi : %2").arg(mSambaURL).arg(oOkuyanlar.size()));
	}
	catch (EException & exc )
	{
		oHata = exc.printStackTrace();
		return FAILURE;
	}
	catch (...)
	{
		return FAILURE;
	}
	
	return SUCCESS;
}


int ELinuxFileSecurity::storeOwnerInfo(const QString & iFileName, QString & oHata)
{
	DEBUGLOGYAZ("EsyaOrtakDLL","OwnerInfo Restore is not supported in LINUX ");
	return 0;
}

int ELinuxFileSecurity::restoreOwnerInfo(const QString & iFileName , QString & oHata)
{
	DEBUGLOGYAZ("EsyaOrtakDLL","OwnerInfo Restore is not supported in LINUX ");
	return 0;
}

#endif

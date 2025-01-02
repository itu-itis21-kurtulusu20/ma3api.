#include "EOrtamDegiskeni.h"
#include <QProcess>
#include <QRegExp>

#ifdef WIN32
#include <ShlObj.h>
#include <WinNT.h>
#include <WinDef.h>
#endif

#define KERMEN_AYAR_ORTAM_DEGISKEN_BELIRTEC_BASLANGIC "[$"
#define KERMEN_AYAR_ORTAM_DEGISKEN_BELIRTEC_BITIS "]"

/**
 * Boþ oluþturur. 
 */
EOrtamDegiskeni::EOrtamDegiskeni(void)
:mIsNull(true)
{
}

/**
 * Yýkýcý metod
 */
EOrtamDegiskeni::~EOrtamDegiskeni(void)
{
}


/**
 * Ortam deðiþkeni adý ve deðeri ile ilklendirilir.
 * \param iDegiskenAdi 
 * Ortam deðiþkeni adý
 * \param iDegiskenDegeri 
 * Deðiþken deðeri
 * \return 
 */
EOrtamDegiskeni::EOrtamDegiskeni(const QString & iDegiskenAdi,const QString & iDegiskenDegeri)
:mIsNull(false),mDegiskenAdi(iDegiskenAdi),mDegiskenDegeri(iDegiskenDegeri)
{
}

/**
 * Verilen ortam deðiþkeni deðerini bulur.
 * \param iDegiskenAdi 
 * Istenen ortam deðiþkeni
 * \return 
 * EOrtamDeðiþkeni döner.
 */
EOrtamDegiskeni EOrtamDegiskeni::degiskenDegerBul(const QString & iDegiskenAdi)
{
	QMap<QString,QString> ortamDegiskenMap=EOrtamDegiskeni::ortamDegiskenMapGetir();
	if (ortamDegiskenMap.contains(iDegiskenAdi))
	{
		return EOrtamDegiskeni(iDegiskenAdi,ortamDegiskenMap.value(iDegiskenAdi));
	}
	else
	{
		return EOrtamDegiskeni();
	}
}

/**
 * Ayarlarda tutulan,içinde ortam deðiþkeni geçen text içindeki ortam deðiþkenlerinin deðerlerini yerleþtirip geri döner
 * "[$SYSTEMDIR]\\test" þeklinde verilen metni "c:\\windows\\sytem32\\test" olarak döner
 * \param iAyarTamDegeri 
 * Ayarlardan okunan tam metin
 * \return 
 * Ortam deðiþkeni bulunup metine yerleþtirilmiþ halini döner
 */
QString EOrtamDegiskeni::degiskenliStrCoz(const QString & iAyarTamDegeri)
{
	QString ayarDegeri=iAyarTamDegeri;
	int ilkBas = ayarDegeri.indexOf(KERMEN_AYAR_ORTAM_DEGISKEN_BELIRTEC_BASLANGIC);
	if (ilkBas!=-1)
	{
		int paternSon = ayarDegeri.indexOf(KERMEN_AYAR_ORTAM_DEGISKEN_BELIRTEC_BITIS);
		if (paternSon!=-1)
		{
			QString degisken = ayarDegeri.mid(ilkBas+2,paternSon-ilkBas-2);
			EOrtamDegiskeni ortamDegiskeni = EOrtamDegiskeni::degiskenDegerBul(degisken);
			if (!ortamDegiskeni.isNull())
			{
				ayarDegeri.replace(ilkBas,paternSon-ilkBas+1,ortamDegiskeni.getDegiskenDegeri()); 				
				ayarDegeri = degiskenliStrCoz(ayarDegeri);
			}
		}
	}
	return ayarDegeri;	
}

/**
 * Ortam deðiþkenlerinin deðiþken adý, deðer map ini döner
 * \return 
 * Deðer map'i döner.
 */
QMap<QString,QString>  EOrtamDegiskeni::ortamDegiskenMapGetir()
{
	QMap<QString,QString> retOrtamDegiskenMap;
	QStringList systemDegiskenleri = QProcess::systemEnvironment();
	Q_FOREACH(QString ortamDegiskenDeger,systemDegiskenleri)
	{
		QStringList degiskenDeger = ortamDegiskenDeger.split("=");
		if (degiskenDeger.size() == 2)
		{
			retOrtamDegiskenMap.insert(degiskenDeger.at(0),degiskenDeger.at(1));
		}		
	}

#ifdef WIN32
	TCHAR szPath[MAX_PATH];
	if(SUCCEEDED(SHGetFolderPath(NULL,CSIDL_PERSONAL,NULL,0,szPath)))
	{
		QString personalFolderPath;
#ifdef UNICODE
		personalFolderPath = QString::fromUtf16((ushort *)szPath);
#else
		personalFolderPath = QString::fromLocal8Bit(szPath);
#endif
		retOrtamDegiskenMap.insert("PERSONAL",personalFolderPath);
	}
#endif
		
	return retOrtamDegiskenMap;
}

/**
 * Boþ olup olmadýðýný döner
 * \return
 * Boþsa true boþ deðilse false döner.
 */
bool EOrtamDegiskeni::isNull()
{
	return mIsNull;
}

/**
 * Deðiþken adýný almak için kullanýlýr.
 * \return 
 * Deðiþken adýný döner
 */
QString EOrtamDegiskeni::getDegiskenAdi() const
{
	return mDegiskenAdi;
}

/**
 * Deðiþken deðerini almak için kullanýlýr.
 * \return 
 * Deðiþken deðerini döner.
 */
QString EOrtamDegiskeni::getDegiskenDegeri() const
{
	return mDegiskenDegeri;
}

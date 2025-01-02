#include "ELoggerFactory.h"
#include "EFileLogger.h"
#include "ESqliteLogger.h"
#include "EAyarlar.h"
#include <QDir>
#include <QSettings>
#include "EAyarAlici.h"
#include "EAyarTanimlari.h"
#include "EAyarKullaniciManager.h"
#include "EOrtamDegiskeni.h"
#include "EsyaOrtakDLL_DIL.h"

#define KERMEN_SQLITE_SUFFIX ".esya"
#define DEFAULT_LOG_FILE_PATH		(QDir::homePath()+"/Kermen.log")
#define DEFAULT_AYAR_LOG_FILE_PATH	(QDir::homePath()+"/KermenAyarlar.log")
#define DEFAULT_LOG_THRESHOLD	ELogEntry::P_ERROR 
#define KERMEN_LOG_AYIRAC "|"
#define KERMEN_LOG_PATH_AYIRAC "="

#define KERMEN_LOG_INI_NAME "KermenLog.ini"
#define KERMEN_LOG_INI_FILE_PATH_NAME "DosyaAdi"
#define KERMEN_LOG_INI_THRESHOLD "EsikDegeri"


NAMESPACE_BEGIN(esya)
ILogger * ELoggerFactory::msLogger=NULL;
ELoggerFactory ELoggerFactory::mLoggerDeleter=ELoggerFactory();
ELoggerFactory::ELoggerFactory(void)
{	
}

ELoggerFactory::~ELoggerFactory(void)
{	
}

/**
 * Ýlk once programýn calýþtýðý yerde config dosyasýnýn olup olmadýðýný kontrol eder. Eðer orda yoksa kullanýcýnýn dizini altýna bakar. Orada bulamazsa boþ döner.
  * \return 
  * Config dosyasýnýn yerini döner
 */
QString ELoggerFactory::_configDosyasiKontrol()
{
	QString retConfigAdresi = "";
	if (QFile::exists(KERMEN_LOG_INI_NAME))
	{		
		retConfigAdresi = KERMEN_LOG_INI_NAME;
	}
	else if (QFile::exists(QDir::homePath()+"/"+KERMEN_LOG_INI_NAME))
	{
		retConfigAdresi = QDir::homePath()+"/"+KERMEN_LOG_INI_NAME;
	}
	else
	{
		retConfigAdresi = "";
	}
	return retConfigAdresi;
}

/**
 * Config dosyasýnda eðer log dosyasýnýn parametreleri belirtilmiþse onlarý alýr.
 * \return 
 * Bulunan path'leri döner.
 */
QStringList ELoggerFactory::congifDosyasindanIsimleriAl()
{
	QString configDosyaYeri = ELoggerFactory::_configDosyasiKontrol();
	QStringList retFilePaths;
	if (configDosyaYeri.isEmpty())
	{
		retFilePaths;
	}	

	QSettings lLogSettings(configDosyaYeri,QSettings::IniFormat);
	QVariant lLogFilePathVar = lLogSettings.value(KERMEN_LOG_INI_FILE_PATH_NAME);
	if (lLogFilePathVar.isValid())
	{
		retFilePaths.append(lLogFilePathVar.toString());	
	}	
	return retFilePaths;
}

ELogEntry::PRIORITY ELoggerFactory::configDosyasindanEsikAl()
{
	QString configDosyaYeri = ELoggerFactory::_configDosyasiKontrol();
	QStringList retFilePaths;
	if (configDosyaYeri.isEmpty())
	{
		throw EException("Config dosyasý bulunamadý");
	}	
	ELogEntry::PRIORITY retPriority=DEFAULT_LOG_THRESHOLD;

	QSettings lLogSettings(configDosyaYeri,QSettings::IniFormat);
	if(lLogSettings.contains(KERMEN_LOG_INI_THRESHOLD))
	{		
		QVariant lLogPiority = lLogSettings.value(KERMEN_LOG_INI_THRESHOLD,DEFAULT_LOG_THRESHOLD);			
		retPriority = ((ELogEntry::PRIORITY)lLogPiority.toInt());
		return retPriority;
	}
	else
	{
		throw EException("Config dosyasýnda esik degeri yok");	
	}			
}

ELogEntry::PRIORITY ELoggerFactory::_ayarlardanEsikAl()
{
	ELogEntry::PRIORITY retPriority = ELogEntry::P_DEBUG ;
	EAyarAlici ayarAlici(AYAR_SNF_LOG,AYAR_LOG_LOG_ESIK_DEGERI);
	EAyar ayarEsikDeger;
	try
	{
		ayarEsikDeger = ayarAlici.ayarBul(); 
		retPriority = (ELogEntry::PRIORITY)ayarEsikDeger.getIntDeger();
	}
	catch (EException &exc)
	{			
	}	
	return retPriority ;
}

QStringList ELoggerFactory::_ayarlardanLogIsimleriniAl()
{
	EAyarAlici ayarAlici(AYAR_SNF_LOG,AYAR_LOG_LOG_DIZIN_YOLU);
	EAyar ayarLogPath;
	try
	{
		ayarLogPath = ayarAlici.ayarBul(); 
	}
	catch (EException &exc)
	{			
	}
	QString lLogName = ayarLogPath.getStringDeger();
	QStringList retPaths=lLogName.split(KERMEN_LOG_AYIRAC);
	return retPaths;
}


QMap<QString,QStringList> ELoggerFactory::getLoggerPaths()
{	
	QStringList lFilePaths;
	QStringList lDBPaths;

	lFilePaths = congifDosyasindanIsimleriAl();
	if (lFilePaths.size()==0)
	{
		QStringList lLogPaths = _ayarlardanLogIsimleriniAl();
		if (lLogPaths.size()==0)
		{
			lFilePaths.append(DEFAULT_LOG_FILE_PATH);
		}
		else
		{
			foreach(QString lPath,lLogPaths)
			{
				QStringList lDegerList = lPath.split(KERMEN_LOG_PATH_AYIRAC);
				if (lDegerList.size()>1)
				{
					if (lDegerList.at(0)==KERMEN_LOG_FILE_ISARET)
					{
						lFilePaths.append(EOrtamDegiskeni::degiskenliStrCoz(lDegerList.at(1)));
					}
					else if (lDegerList.at(0)==KERMEN_LOG_DB_ISARET)
					{
						lDBPaths.append(EOrtamDegiskeni::degiskenliStrCoz(lDegerList.at(1)));
					}						
				}
			}
		}			
	}	

	if (lFilePaths.isEmpty() && lDBPaths.isEmpty())
	{
		lFilePaths.append(DEFAULT_LOG_FILE_PATH);
	}
	QMap<QString,QStringList> lRetMap;
	lRetMap.insert(KERMEN_LOG_FILE_ISARET,lFilePaths);
	lRetMap.insert(KERMEN_LOG_DB_ISARET,lDBPaths);
	return lRetMap;
}

ILogger * ELoggerFactory::getLoggerChain()
{	
	if (ELoggerFactory::msLogger == NULL)
	{		
		ILogger * tmpLogger=NULL;
		ELogEntry::PRIORITY esikDeger=DEFAULT_LOG_THRESHOLD ;

		QMap<QString,QStringList> lLoggerPaths=ELoggerFactory::getLoggerPaths();
		
		QStringList lFilePaths = lLoggerPaths[KERMEN_LOG_FILE_ISARET];
		QStringList lDBPaths = lLoggerPaths[KERMEN_LOG_DB_ISARET];
		foreach(QString lFile,lFilePaths)
		{	
			EFileLogger * lpFileLogger=new EFileLogger(lFile,esikDeger);		
			if (tmpLogger==NULL)
			{				
				ELoggerFactory::msLogger = lpFileLogger ;
				tmpLogger = lpFileLogger;
			}			
			else
			{
				tmpLogger->setSonrakiLogger(lpFileLogger);
				tmpLogger = lpFileLogger ;
			}
			lpFileLogger->maxDosyaBoyuBelirle();
			lpFileLogger->esikDegerBelirle((ELogEntry::PRIORITY)ELoggerFactory::esikSeviyesiAl());
		}
		foreach(QString lDBPath,lDBPaths)
		{			
			ESqliteLogger * lpSqliteLogger=new ESqliteLogger(lDBPath,esikDeger);
			if (tmpLogger==NULL)
			{
				ELoggerFactory::msLogger = lpSqliteLogger ;
				tmpLogger = lpSqliteLogger;				
			}			
			else
			{
				tmpLogger->setSonrakiLogger(lpSqliteLogger);
				tmpLogger = lpSqliteLogger ;				
			}
			lpSqliteLogger->esikDegerBelirle((ELogEntry::PRIORITY)ELoggerFactory::esikSeviyesiAl());
		}				
	}	
	return ELoggerFactory::msLogger;
}




ILogger * EAyarLoggerFactory::msLogger = NULL ;

/**
 * Ayarlardaki loglarýn oluþturacak olan zinciri oluþturur.
 * Burada ayarlarda farklý bir logger kullanýlmasýnýn nedeni diger sýnýflarýn log yazma esnasýnda ayarlarýn kullanýlmasý
 * \return 
 * Log zincirinin baþýný döner.
 */
ILogger * EAyarLoggerFactory::getLoggerChain()
{
	if(EAyarLoggerFactory::msLogger == NULL)
	{	
			ELogEntry::PRIORITY esikDeger=DEFAULT_LOG_THRESHOLD ;
			QStringList logFilePaths;
			try
			{
				logFilePaths = ELoggerFactory::congifDosyasindanIsimleriAl();
			}
			catch (EException &exc)
			{		
			}

			if (logFilePaths.isEmpty())
			{
				logFilePaths.append(DEFAULT_AYAR_LOG_FILE_PATH);
			}

			ILogger * pTmpLogger = NULL ;
			foreach(QString logFileName,logFilePaths)
			{
				EFileLogger * pLogger = new EFileLogger(logFileName,esikDeger);
				if (pTmpLogger == NULL)
				{
					EAyarLoggerFactory::msLogger = pLogger ;
					pTmpLogger = pLogger;
				}
				else
				{
					pTmpLogger->setSonrakiLogger(pLogger);
					pTmpLogger = pLogger ;
				}				
				pLogger->maxDosyaBoyuBelirle();
				pLogger->esikDegerBelirle((ELogEntry::PRIORITY)ELoggerFactory::esikSeviyesiAl());
			}			
	}
	return EAyarLoggerFactory::msLogger;				
}

QString ELoggerFactory::logDosyaDiziniAl()
{
	if (QFile::exists(KERMEN_GENEL_AYARLAR_FILE_NAME))
	{
		return "."; 
	}
	return QDir::homePath();
}

int ELoggerFactory::mEsikSeviyesi=-1;
int ELoggerFactory::esikSeviyesiAl()
{
	if (ELoggerFactory::mEsikSeviyesi == -1)
	{
		ELoggerFactory::mEsikSeviyesi = DEFAULT_LOG_THRESHOLD ;
		try
		{
			ELoggerFactory::mEsikSeviyesi = configDosyasindanEsikAl();
		}
		catch (EException &exc)
		{
			try
			{
				ELoggerFactory::mEsikSeviyesi = _ayarlardanEsikAl();
			}
			catch (EException & exc)
			{
			}			
		}
	}
	return ELoggerFactory::mEsikSeviyesi ; 
}

QList<QPair<QString,QStringList> > ELoggerFactory::getLogModulNames()
{
	QList<QPair<QString,QStringList> > retModulNames;
	ILogger * pLoggerChain = ELoggerFactory::getLoggerChain();
	if (pLoggerChain)
	{		
		QMap<QString,QStringList> defaultLogNames = ELoggerFactory::getLoggerPaths();
		QList<QStringList> logNames = defaultLogNames.values();
		QStringList allLogNames;
		for (int k=0;k<logNames.size();k++)
		{
			allLogNames<<logNames.at(k);
		}
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_KERMEN_GENEL,allLogNames));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_GDM,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_GDM)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_DEPO_GORUNTULEYICI,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_DEPO_GORUNTULEYICI)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_KONSOL,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_KONSOL)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_SERTIFIKA_YARDIMCISI,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_SERTIFIKA_YARDIMCISI)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_OUTLOOK,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_OUTLOOK)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_SIFRECI,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_SIFRECI)));
		retModulNames.append(qMakePair(DIL_LOG_MODUL_NAME_SUR,pLoggerChain->getModulLogFileNames(KERMEN_LOG_MODUL_NAME_SUR)));					
	}
	return retModulNames;
}

NAMESPACE_END

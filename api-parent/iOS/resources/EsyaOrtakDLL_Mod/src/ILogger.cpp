#include "ILogger.h"
#include <QDateTime>
#include "EAyarlar.h"
#include "EAyarKullaniciManager.h"
#include <QFileInfo>
#include "EsyaOrtakDLL_DIL.h"

using namespace esya;
NAMESPACE_BEGIN(esya)


ELogEntry::ELogEntry(const QString & iModulName,const QString & iFileName,int iLineNum,const QString &iMessage,ELogEntry::PRIORITY iPriority,bool iKulAdiEkle,const QDateTime & iLogTime)
:	mNull(false),
	mModulName(iModulName),
	mFileName(iFileName),
	mLineNum(iLineNum),
	mPriority(iPriority),
	mMessage(iMessage),
	mLogTime(iLogTime)
{	
}

ELogEntry::ELogEntry(const ELogEntry & iLog)
:	mNull(iLog.isNull()),
	mModulName(iLog.getModulName()),
	mFileName(iLog.getFileName()),
	mLineNum(iLog.getLineNum()),
	mPriority(iLog.getEsikDegeri()),
	mMessage(iLog.getMessage()),
	mLogTime(iLog.getLogTime())
{	
}

ELogEntry & ELogEntry::operator=(const ELogEntry & iLog)
{
	mNull		= iLog.isNull();
	mModulName	= iLog.getModulName();
	mFileName	= iLog.getFileName();
	mLineNum	= iLog.getLineNum();
	mPriority	= iLog.getEsikDegeri();
	mMessage	= iLog.getMessage();
	mLogTime	= iLog.getLogTime();
	return *this;
}


ELogEntry::~ELogEntry()
{
}

ELogEntry::PRIORITY ELogEntry::getEsikDegeri() const
{
	return mPriority;
}

QString ELogEntry::getModulName() const
{
	return mModulName;
}

QString	ELogEntry::getFileName() const
{
	return mFileName;
}

int ELogEntry::getLineNum() const
{
	return mLineNum;
}

QString ELogEntry::getMessage() const
{
	return mMessage;
}

QString ELogEntry::getUserName() const
{
	return mUserName;
}

QDateTime ELogEntry::getLogTime()const
{
	return mLogTime;
}

QString ELogEntry::toString() const
{
	QString lYazilacak;	
	QDateTime logTime = (mLogTime.isNull()) ?  QDateTime::currentDateTime():mLogTime;
	lYazilacak += logTime.toString(DEFAULT_DATE_TIME_MASK);
	lYazilacak += QString(" %1").arg(mPriority);
	lYazilacak+=" <"+getModulName()+"> "+QFileInfo(getFileName()).fileName()+"("+QString().setNum(getLineNum())+")";
	lYazilacak+="\n";
	QString lEklenecekBosluk = QString().fill(' ',QString(DEFAULT_DATE_TIME_MASK).size()+3);
	QString lTempMessage=getMessage();
	lTempMessage.prepend(lEklenecekBosluk);
	lYazilacak+=lTempMessage;	
	lYazilacak+=LOG_ENTRY_END;
	return lYazilacak;
}

bool ELogEntry::fromString(const QString & iLogText) 
{
	if (iLogText.isEmpty()) 
		return false;
	int headerIndex = iLogText.indexOf("\n");
	
	if (headerIndex<0) return false;
	
	QString temp, header = iLogText.left(headerIndex);	
	
	QStringList headerItems = header.split(" ");

	if (headerItems.size()!= 5) return false;

	QString stLogDate = headerItems[0] + " " + headerItems[1];

	mLogTime = QDateTime::fromString(stLogDate,DEFAULT_DATE_TIME_MASK);
	
	mPriority = (ELogEntry::PRIORITY)headerItems[2].toInt();

	QString stModuleName = headerItems[3];
	stModuleName.remove(0,1);
	stModuleName.remove(stModuleName.length()-1,1);

	mModulName = stModuleName;

	mFileName = headerItems[4].left(headerItems[4].indexOf("("));
	temp = headerItems[4].right(headerItems[4].length()-headerItems[4].indexOf("(")-1);
	temp = temp.left(temp.indexOf(")"));
	mLineNum = temp.toInt();

	mMessage = iLogText.right(iLogText.length()-headerIndex-1);

	return true;
}

QMap<int,QString> ELogEntry::getEsikDegerAdMap()
{	
	QMap<int,QString> retMap;
	retMap.insert(ELogEntry::P_DEEP_DEBUG,DIL_LOG_SEVIYE_AYRINTILI_HATA_AYIKLAMA);
	retMap.insert(ELogEntry::P_DEBUG,DIL_LOG_SEVIYE_HATA_AYIKLAMA);
	retMap.insert(ELogEntry::P_INFO,DIL_LOG_SEVIYE_BILGI);
	retMap.insert(ELogEntry::P_WARNING,DIL_LOG_SEVIYE_UYARI);
	retMap.insert(ELogEntry::P_ERROR,DIL_LOG_SEVIYE_HATA);
	retMap.insert(ELogEntry::P_FATAL,DIL_LOG_SEVIYE_KRITIK_HATA);
	return retMap;
}



ILogger::ILogger(const QString & iLogPath,ELogEntry::PRIORITY iEsikSeviyesi)
:mLogPath(iLogPath),mEsikSeviyesi(iEsikSeviyesi),mpSonrakiLogger(NULL)
{	
}

ILogger::~ILogger()
{
	if (mpSonrakiLogger)
	{
		DELETE_MEMORY(mpSonrakiLogger)
	}
}

void ILogger::writeToLog(const ELogEntry & iLogEntry)
{
	_writeToLog(iLogEntry);
	if (mpSonrakiLogger!=NULL)
	{
		mpSonrakiLogger->writeToLog(iLogEntry);
	}
}

QString ILogger::_getCurrentDateTime()
{
	return QDateTime::currentDateTime().toString(DEFAULT_DATE_TIME_MASK);
}

QString ILogger::_getModulAdiFromPath(QString iModulName,QString iFileFullPath)
{
	QStringList lHepsi = QString(__FILE__).split("\\").filter("_Mod");
	if (lHepsi.size()>0)
	{
		return lHepsi.at(0);
	}
	return iModulName;
}

void ILogger::setSonrakiLogger(ILogger * ipSonrakiLogger)
{
	mpSonrakiLogger = ipSonrakiLogger ;
}

void ILogger::esikDegerBelirle(ELogEntry::PRIORITY iEsikSeviyesi)
{
	mEsikSeviyesi = iEsikSeviyesi;
	if (mpSonrakiLogger)
	{
		mpSonrakiLogger->esikDegerBelirle(iEsikSeviyesi);
	}
}

QStringList ILogger::getModulLogFileNames(const QString & iLogFilePrefixName)
{
	QStringList retList;
	retList<<getModulLogFileName(iLogFilePrefixName);
	if (mpSonrakiLogger)
	{
		retList+=mpSonrakiLogger->getModulLogFileNames(iLogFilePrefixName);
	}
	return retList;
}

QString ILogger::getModulLogFileName(const QString & iLogFilePrefixName)
{
	if (iLogFilePrefixName.isEmpty())
	{
		return mLogPath;
	}
	QString newLogFilePath;
	QFileInfo fileInfo(mLogPath);
	newLogFilePath = fileInfo.absolutePath()+"/"+iLogFilePrefixName+"."+fileInfo.completeSuffix();
	return newLogFilePath;
}

void ILogger::changeLogFilePrefixName(const QString & iLogFilePrefixName)
{	
	mLogPath = getModulLogFileName(iLogFilePrefixName);
	if (mpSonrakiLogger)
	{
		mpSonrakiLogger->changeLogFilePrefixName(iLogFilePrefixName);
	}
}


NAMESPACE_END

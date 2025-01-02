#include "EFileLogger.h"
#include <QFile>
#include <QDateTime>
#include <QTextStream>
#include <QFileInfo>
#include "EsyaOrtak_Ayar.h"
#include "EsyaOrtak_Ortak.h"
#include "EAyarAlici.h"
#include "EAyarTanimlari.h"
#include <QSettings>

#define KERMEN_LOG_INI_NAME "KermenLog.ini"
#define KERMEN_LOG_INI_MAXIMUM_LOG_FILE_SIZE "MaximumLogDosyaBoyutu"
#define MAX_LOG_FILE_SIZE_MB 10
#define KERMEN_MB_SIZE_BYTE 1000000 
NAMESPACE_BEGIN(esya)

EFileLogger::EFileLogger(const QString & iLogFilePath,ELogEntry::PRIORITY iPriority)
:ILogger(iLogFilePath,iPriority)
{	
	QFileInfo file(iLogFilePath);
    //QDir dir = file.absoluteDir();
    //if (!dir.exists())
    //{
    //	dir.mkpath(".");
    //}
	mMaxDosyaBoyu = MAX_LOG_FILE_SIZE_MB * KERMEN_MB_SIZE_BYTE ;
}

void EFileLogger::maxDosyaBoyuBelirle()
{
	mMaxDosyaBoyu = _maxLogDosyaBoyuOku();
}

void EFileLogger::_writeToLog(const ELogEntry & iLogEntry)
{
	if (mEsikSeviyesi>iLogEntry.getEsikDegeri())
	{
		return;
	}
	_dosyaBoyuKontroluYap();
	QFile lLogFile(mLogPath);	
	bool b = lLogFile.open(QFile::Append | QIODevice::Text);	
	QTextStream lOutStream(&lLogFile);
	lOutStream<<iLogEntry.toString()<<endl;	
	lLogFile.close();	
}

EFileLogger::~EFileLogger(void)
{	
}

int EFileLogger::_ayarlardaMaxLogDosyaBoyuOku()
{
	int maxDosyaBoyutMB = MAX_LOG_FILE_SIZE_MB ;
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_LOG,AYAR_ADI_LOG_MAXIMUM_LOG_DOSYA_BOYUTU);
		EAyar  maxBoyutAyar = ayarAlici.ayarBul(false);
		maxDosyaBoyutMB = maxBoyutAyar.getIntDeger();
	}
	catch(EException & exc)
	{
	}
	 return maxDosyaBoyutMB * KERMEN_MB_SIZE_BYTE;
}

int EFileLogger::_maxLogDosyaBoyuOku()
{	
	qDebug("EFileLogger::_ayarlardanMaxLogDosyaBoyuOku-GIRIS");
	QSettings lLogSettings(KERMEN_LOG_INI_NAME,
		QSettings::IniFormat);
	QVariant logMaximumDosyaBoyutu = lLogSettings.value(KERMEN_LOG_INI_MAXIMUM_LOG_FILE_SIZE);			
	if (logMaximumDosyaBoyutu.isValid())
	{
		qDebug(qPrintable(QString("EFileLogger-Maximum Log Dosya Boyutu = %1").arg(logMaximumDosyaBoyutu.toInt())));
		return logMaximumDosyaBoyutu.toInt() * KERMEN_MB_SIZE_BYTE ;	
	}
	else
	{
		return _ayarlardaMaxLogDosyaBoyuOku();
	}	
}

void EFileLogger::_dosyaBoyuKontroluYap()
{
	QFileInfo logFileInfo(mLogPath);	
	if (logFileInfo.size() > mMaxDosyaBoyu)
	{
		QFile::remove(mLogPath);
	}
}

NAMESPACE_END

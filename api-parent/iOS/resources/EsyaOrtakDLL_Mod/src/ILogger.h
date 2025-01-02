#ifndef __I__LOGGER__H__
#define __I__LOGGER__H__

#define DEFAULT_DATE_TIME_MASK "dd-MM-yyyy hh:mm:ss.zzz"

#define KERMEN_LOG_MODUL_NAME_GDM "KermenGDM"
#define KERMEN_LOG_MODUL_NAME_DEPO_GORUNTULEYICI "KermenDepoGoruntuleyici"
#define KERMEN_LOG_MODUL_NAME_KONSOL "KermenKonsol"
#define KERMEN_LOG_MODUL_NAME_SERTIFIKA_YARDIMCISI "KermenSertifikaYardimcisi"
#define KERMEN_LOG_MODUL_NAME_OUTLOOK "KermenOutlook"
#define KERMEN_LOG_MODUL_NAME_SIFRECI "KermenSifreci"
#define KERMEN_LOG_MODUL_NAME_SUR "KermenSur"

#include "esyaOrtak.h"
#include <QString>
#include <QObject>
#include "EAyarlar.h"
#include <QMap>

#define LOG_ENTRY_END "<|>"

using namespace esya;
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT ELogEntry
{
public:
	enum PRIORITY {P_DEEP_DEBUG=0,P_DEBUG=1,P_INFO=2,P_WARNING=3,P_PERFORMANCE_INFO=4,P_ERROR=5,P_FATAL=6} ;
	ELogEntry(const QString & iModulName,const QString & iFileName,int iLineNum,const QString &iMessage,ELogEntry::PRIORITY iPriority, bool iKulAdiEkle=true, const  QDateTime & iLogTime = QDateTime() );
	ELogEntry():mNull(true){};
	ELogEntry(const ELogEntry & );	

	ELogEntry & operator=(const ELogEntry & iLog);

	virtual ~ELogEntry();	

	ELogEntry::PRIORITY getEsikDegeri() const;
	QString getModulName() const;
	QString	getFileName() const;
	int getLineNum() const;
	QString getMessage() const;
	QString getUserName() const;
	QDateTime getLogTime()const;

	bool isNull()const {return mNull;};

	QString toString() const;

	bool fromString(const QString & );
	static QMap<int,QString> getEsikDegerAdMap();

private:
	QString mModulName;
	QString mFileName;
	int mLineNum;
	QString mMessage;
	ELogEntry::PRIORITY mPriority;
	QString mUserName;
	QDateTime mLogTime;
	bool mNull;
};


class ILogger:
	public QObject
{
	Q_OBJECT
	ILogger * mpSonrakiLogger;
public:	
	ILogger(const QString & iLogPath,ELogEntry::PRIORITY iEsikSeviyesi);	
	~ILogger();
	virtual void _writeToLog(const ELogEntry & iLogEntry)=0;
	Q_DECL_EXPORT void writeToLog(const ELogEntry & iLogEntry);

	void setSonrakiLogger(ILogger * ipSonrakiLogger);
	ILogger * getSonrakiLogger();
	void esikDegerBelirle(ELogEntry::PRIORITY iEsikSeviyesi);

	Q_DECL_EXPORT QString getModulLogFileName(const QString & iLogFilePrefixName="");
	Q_DECL_EXPORT QStringList getModulLogFileNames(const QString & iLogFilePrefixName="");
	Q_DECL_EXPORT void changeLogFilePrefixName(const QString & iLogFilePrefixName);
protected:	
	ELogEntry::PRIORITY mEsikSeviyesi;	
	QString mLogPath;
	QString _getCurrentDateTime();
	QString _getModulAdiFromPath(QString iModulName,QString iFileFullPath);		
};
NAMESPACE_END
#endif

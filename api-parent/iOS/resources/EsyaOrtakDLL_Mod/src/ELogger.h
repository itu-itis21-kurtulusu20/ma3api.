/**
 * Log alma sýnýfýmýz
 * 
 * Copyright (c) 2005 by <Dindar Öz/ MA3 Ýstemci>
 */
#ifndef __ELOGGER_H__
#define __ELOGGER_H__
#include "ELoggerFactory.h"
//#include <QDir>
#include "ESeviyeLogYonetici.h"

#define AYRINTILIDEBUGLOGYAZ(modulName,logMesaj)

#define DEBUGLOGYAZ(modulName,logMesaj)


#define INFOLOGYAZ(modulName,logMesaj)


#define WARNINGLOGYAZ(modulName,logMesaj)


#define PERMORMANSLOGYAZ(modulName,logMesaj)



#define ERRORLOGYAZ(modulName,logMesaj)


#define FATALLOGYAZ(modulName,logMesaj)






#define AYAR_DEBUGLOGYAZ(modulName,logMesaj) \
	EAyarLoggerFactory::getLoggerChain()->writeToLog(ELogEntry(modulName,__FILE__,__LINE__,logMesaj,ELogEntry::P_DEBUG,false));

#define AYAR_INFOLOGYAZ(modulName,logMesaj) \
	EAyarLoggerFactory::getLoggerChain()->writeToLog(ELogEntry(modulName,__FILE__,__LINE__,logMesaj,ELogEntry::P_INFO,false));		

#define AYAR_WARNINGLOGYAZ(modulName,logMesaj) \
	EAyarLoggerFactory::getLoggerChain()->writeToLog(ELogEntry(modulName,__FILE__,__LINE__,logMesaj,ELogEntry::P_WARNING,false));		

#define AYAR_ERRORLOGYAZ(modulName,logMesaj) \
	EAyarLoggerFactory::getLoggerChain()->writeToLog(ELogEntry(modulName,__FILE__,__LINE__,logMesaj,ELogEntry::P_ERROR,false));				

#define AYAR_FATALLOGYAZ(modulName,logMesaj) \
	EAyarLoggerFactory::getLoggerChain()->writeToLog(ELogEntry(modulName,__FILE__,__LINE__,logMesaj,ELogEntry::P_FATAL,false));		
/*



#include <QTextStream>
#include <QString>
#include <QFile>
#include <QHash>
#include <QDir>
#include <QFileInfo>
#include "esyaOrtak.h"

#include <QMutex>


#define REG_DIR		"UEKAE" // ini dosyasýnýn bulunduðu klasörü
#define REG_FILE    "MA3"	// ini dosyasýnýn adý


#define DEFAULT_LOG_FILE_PATH	(QDir::homePath()+"/ELogger_LOG.log")
#define DEFAULT_LOG_THRESHOLD	0 


#define DEFAULT_LOG_MODULE		"GENEL" 

#define LOG_FILE_PATH			"LOG_FILE_PATH" 
#define LOG_THRESHOLD			"LOG_THRESHOLD"
#define DEFAULT_TIME_MASK		"hh:mm:ss.zzz : "

#define ERROR_FILE_OPEN 1001

#define INDENT_COUNT 8





#define LOGGER(x) ELogger::getLogger(x)
namespace esya 
{



	enum PRIORITY { P_DEBUG , P_INFO , P_WARNING ,P_ERROR , P_FATAL } ;




	class EException;
	
	class ELogger 
	{

	public:


		int writeMyLog(const QString  &logText, const PRIORITY iPriority = P_ERROR, const QString & iFileName = "", int iLineNumber = 0);
		int writeMyLog(const EException  * pException , const PRIORITY iPriority = P_ERROR);

		static ELogger * getLogger(const QString& aKey = DEFAULT_LOG_MODULE);


		void setThreshold(PRIORITY iThreshold);
		void setTimeMask(const QString &);

		~ELogger(void);

		static QString indent(const QString &iBlock, int iLen);

	private:
		
		static QHash<QString,ELogger*> mInstances;		

		static QString  mTimeMask;	

		QString mLogFileName ;
		QString	mModuleName;
		PRIORITY mThreshold ; 
		ELogger();
		ELogger(const QString& iModuleName);

	};

}

*/
#endif


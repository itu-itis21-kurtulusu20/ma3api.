#ifndef __E_LOGGER_FACTORY__H__
#define __E_LOGGER_FACTORY__H__
#include "esyaOrtak.h"
#include "ILogger.h"
#include <QMap>

#define KERMEN_CHANGE_LOG_FILE_NAME(x) ELoggerFactory::getLoggerChain()->changeLogFilePrefixName(x);

#define KERMEN_LOG_FILE_ISARET "[FILE]"
#define KERMEN_LOG_DB_ISARET "[DB]"
NAMESPACE_BEGIN(esya)
class ELoggerFactory
{
	static ILogger * msLogger;
	static ELoggerFactory mLoggerDeleter;	
	static QString _configDosyasiKontrol();

	static ELogEntry::PRIORITY _ayarlardanEsikAl();
	static QStringList _ayarlardanLogIsimleriniAl();

	static int mEsikSeviyesi;

public:
	static int esikSeviyesiAl();
	static ELogEntry::PRIORITY configDosyasindanEsikAl();
	static QStringList congifDosyasindanIsimleriAl();
	static QString logDosyaDiziniAl();
	Q_DECL_EXPORT ELoggerFactory(void);
	Q_DECL_EXPORT ~ELoggerFactory(void);
	Q_DECL_EXPORT static QMap<QString,QStringList> getLoggerPaths();
	Q_DECL_EXPORT static ILogger * getLoggerChain();
	Q_DECL_EXPORT static QList<QPair<QString,QStringList> > getLogModulNames();
};

class EAyarLoggerFactory
{
	static ILogger * msLogger;
public:
	Q_DECL_EXPORT static ILogger * getLoggerChain();
};
NAMESPACE_END
#endif

#ifndef E_SQLITE_LOGGER_H_
#define E_SQLITE_LOGGER_H_
#include "ILogger.h"
#include <QSqlDatabase>
#include "EDbLoggerThread.h"
#include <QQueue>
using namespace esya;
NAMESPACE_BEGIN(esya)
class ESqliteLogger :
	public ILogger
{
	Q_OBJECT
	QSqlDatabase mDatabase;
	bool mGecerli;
	EDbLoggerThread * mThread;
	//QQueue<DBLogEntry>  mLogQueue;
	//QList<DBLogEntry> mLogList;
	bool _kontrolOlustur();

public:
	ESqliteLogger(const QString & iSqliteFilePath,ELogEntry::PRIORITY iPriority);
	void _writeToLog(const ELogEntry & iLogEntry);
	~ESqliteLogger(void);

signals:
void yeniLogGeldi(QSqlDatabase *ipDatabase,DBLogEntry iLogEntry);
};
NAMESPACE_END

#endif

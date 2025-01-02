#ifndef EDBLOGGERTHREAD_H
#define EDBLOGGERTHREAD_H

#include <QThread>
#include "ELoggerRDMMapper.h"
#include <QSqlDatabase>
#include "esyaOrtak.h"
#include "EException.h"


using namespace esya;
NAMESPACE_BEGIN(esya)
class DBLogEntry
{
public:	
	QString mUserName;
	QString mModulAdi;
	QString mDosyaAdi;
	int mSatirNo;
	QString mLogStr;
	QDateTime mTarih;
	DBLogEntry();	
	DBLogEntry(QString iUserName,QString iModulAdi,QString iDosyaAdi,int iSatirNo,QString iLogStr,QDateTime iTarih);
};

class  EDbLoggerThread : public QThread
{
	Q_OBJECT
public:
	Q_DECL_EXPORT EDbLoggerThread(QObject *parent);
	Q_DECL_EXPORT ~EDbLoggerThread();
	Q_DECL_EXPORT void run();
private:
public slots:
	Q_DECL_EXPORT void onYeniLogGeldi(QSqlDatabase *ipDatabase,DBLogEntry iEntry);
	
};

NAMESPACE_END
#endif // EDBLOGGERTHREAD_H

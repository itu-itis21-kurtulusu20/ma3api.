#include "EDbLoggerThread.h"
#include <QEventLoop>
#include "PersistenceFacade.h"
#include <QFileInfo>
using namespace esya;
NAMESPACE_BEGIN(esya)
DBLogEntry::DBLogEntry(QString iUserName,QString iModulAdi,QString iDosyaAdi,int iSatirNo,QString iLogStr,QDateTime iTarih)
:mUserName(iUserName),
mModulAdi(iModulAdi),
mDosyaAdi(iDosyaAdi),
mSatirNo(iSatirNo),
mLogStr(iLogStr),
mTarih(iTarih)
{

}

DBLogEntry::DBLogEntry()
{

}

EDbLoggerThread::EDbLoggerThread(QObject *parent)
	: QThread(parent)
{
	qRegisterMetaType<DBLogEntry>("DBLogEntry");
}

EDbLoggerThread::~EDbLoggerThread()
{
}

void EDbLoggerThread::run()
{
	QEventLoop lLoop;	
	lLoop.exec();
}

QString _getModulAdiFromPath(QString iModulName,QString iFileFullPath)
{
	QStringList lHepsi = QString(__FILE__).split("\\").filter("_Mod");
	if (lHepsi.size()>0)
	{
		return lHepsi.at(0);
	}
	return iModulName;
};

void EDbLoggerThread::onYeniLogGeldi(QSqlDatabase *ipDatabase,DBLogEntry iLogEntry)
{
	ELoggerRDMMapper lLogger;
	lLogger.setUserName(iLogEntry.mUserName);
	lLogger.setModulAdi(_getModulAdiFromPath(iLogEntry.mModulAdi,iLogEntry.mDosyaAdi));
	lLogger.setDosyaAdi(QFileInfo(iLogEntry.mDosyaAdi).fileName());
	lLogger.setSatirNo(iLogEntry.mSatirNo);
	lLogger.setLogStr(iLogEntry.mLogStr);
	lLogger.setTarih(iLogEntry.mTarih);
	IMapper * lMapper = PersistenceFacade::getInstance()->getMapper(ipDatabase,"ELogger");
	lMapper->put(0,&lLogger);	
	DELETE_MEMORY(lMapper);
}
NAMESPACE_END

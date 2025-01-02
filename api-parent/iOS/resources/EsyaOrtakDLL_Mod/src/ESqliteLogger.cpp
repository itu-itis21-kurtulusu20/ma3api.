#include "ESqliteLogger.h"
#include <QSqlDatabase>
#include "EVeritabani.h"
#include "ELoggerRDMMapper.h"
#include <QFileInfo>
#define  MAX_LOG_ENTRY_SIZE 10
#include "PersistenceFacade.h"
//#include "EDbLoggerThread.h"

NAMESPACE_BEGIN(esya)
ESqliteLogger::ESqliteLogger(const QString & iSqliteFilePath,ELogEntry::PRIORITY iPriority)
:ILogger(iSqliteFilePath,iPriority),mGecerli(false)
{
	mThread = new EDbLoggerThread(this);
	connect(this,SIGNAL(yeniLogGeldi(QSqlDatabase *,DBLogEntry)),mThread,SLOT(onYeniLogGeldi(QSqlDatabase *,DBLogEntry)));
	mThread->start();
	mGecerli = _kontrolOlustur();
}

bool ESqliteLogger::_kontrolOlustur()
{
	mDatabase = QSqlDatabase::database("LogTable");
	QString lVeritabaniAdi="EsyaLogDB";	
	if(!mDatabase.isValid())
	{		
		if(mDatabase.contains(lVeritabaniAdi))
		{
			mDatabase = QSqlDatabase::database(lVeritabaniAdi);
		}
		else
		{
			mDatabase = QSqlDatabase::addDatabase("QSQLITE",lVeritabaniAdi);
			mDatabase.setDatabaseName(mLogPath);
		}
	}

	if(!mDatabase.isOpen())
	{
		if(!mDatabase.open())
		{
			qDebug()<<"Veritabaný açýlamadý";			
			return false;
		}
	}


	ParamList params;
	QStringList tables = mDatabase.tables();
	QString query;
	QSqlQuery * pQuery=NULL;

	if(!tables.contains("TBL_LOG"))
	{
		query = 
			" CREATE TABLE [TBL_LOG] ( "			
			" [LogID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
			" [UserName] VARCHAR(30)  NOT NULL, "
			" [ModulAdi] VARCHAR(30)  NOT NULL, "
			" [DosyaAdi] VARCHAR(30)  NOT NULL, "
			" [SatirNo] INTEGER NOT NULL,"
			" [Log]		 VARCHAR(1000) NOT NULL, "
			" [Tarih]    VARCHAR(30)   NOT NULL"
			" ); ";
		try
		{
			EVeritabani lProxy(mDatabase);
			pQuery = lProxy.sorguYap(query,params);		
		}
		catch (EException & exc)
		{
			qDebug()<<exc.printStackTrace();
		}		
		DELETE_MEMORY(pQuery);
	}				
	return true;
}
void ESqliteLogger::_writeToLog(const ELogEntry & iLogEntry)
{
	if (mEsikSeviyesi>iLogEntry.getEsikDegeri())
	{
		return;
	}
	if (!mGecerli)
	{
		return;
	}
	
	DBLogEntry lLogEntry(iLogEntry.getUserName(),
						iLogEntry.getModulName(),
						iLogEntry.getFileName(),
						iLogEntry.getLineNum(),
						iLogEntry.getMessage(),QDateTime::currentDateTime());
	emit yeniLogGeldi(&mDatabase,lLogEntry);	
}

ESqliteLogger::~ESqliteLogger(void)
{
}

NAMESPACE_END

#include "EVeritabani.h"
#include "EFilter.h"
#include "EMemoryDBMapper.h"
#include <QUuid>

using namespace esya;


QMutex EVeritabani::mNameMutex;



EVeritabani::EVeritabani(void)
{
}

EVeritabani::EVeritabani(const QSqlDatabase & iVT)
{
	QMutexLocker locker(&mNameMutex);
	const QString & dbName = iVT.databaseName();
	if (dbName != QSQLDB_MEMORY_DB_NAME)
	{
		mConnectionName = connectionNameUret();		
		mVT = QSqlDatabase::addDatabase("QSQLITE",mConnectionName);		
		mVT.setDatabaseName(iVT.databaseName());	
	}
	else
	{
		mVT = iVT;
	}	
}

EVeritabani::EVeritabani(const EVeritabani &iVT)
{
	QMutexLocker locker(&mNameMutex);
	mConnectionName = connectionNameUret();	
	mVT = QSqlDatabase::addDatabase("QSQLITE",mConnectionName);		
	mVT.setDatabaseName(iVT.database().databaseName());
}
EVeritabani::~EVeritabani(void)
{
	const QString & dbName = mVT.databaseName();
	if (dbName != QSQLDB_MEMORY_DB_NAME)
	{
		dbKapat();
	}
}


bool EVeritabani::open()
{
	bool retValue = mVT.isOpen() || mVT.open();
	if (!retValue)
	{
		mLastError = mVT.lastError().text();
	}
	return retValue;
}
bool EVeritabani::checkVT()
{
	if (!mVT.isValid() ) 
	{	
		throwVTEXCEPTION(EVeritabaniException::VTTanimsiz,QString("VERITABANI ACMA HATASI Hata : %1").arg(mVT.lastError().text()));
	}
	else if (!mVT.isOpen() && !mVT.open())
	{		
		QString errStr = mVT.lastError().text();
		throwVTEXCEPTION(EVeritabaniException::VTAcilamadi,QString("VERITABANI ACMA HATASI Hata : %1").arg(errStr));	
	}	
	return true;
}

QSqlQuery* EVeritabani::sorguYap(const QString & irQueryText,const ParamList & iParameters)
{	
	checkVT();	

	QSqlQuery * pQuery = new QSqlQuery(mVT);
	QString errSTR = pQuery->lastError().text();
	if (!pQuery->prepare(irQueryText))
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Sql prepare aþamasýnda hata oluþtu. Hata : %1").arg(hataText));
	}

	for (int i = 0; i < iParameters.size(); i++)
	{
		pQuery->bindValue(iParameters[i].first,iParameters[i].second);
	}

	if (!pQuery->exec())
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
	}	
	return pQuery;
}



QSqlQuery* EVeritabani::sorguYap(const QString & irQueryText,EFilter* iFilter,const QString & iAfterWhereQuery/* = */)
{
	checkVT();
	QString sql = irQueryText;
	if (iFilter)
	{
		sql += QString(" WHERE %2").arg(iFilter->toSQL());
	}
	sql+=" "+iAfterWhereQuery;

	QSqlQuery * pQuery = new QSqlQuery(mVT);	
	if (!pQuery->prepare(sql))
	{		
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Sql prepare aþamasýnda hata oluþtu. Hata : %1").arg(hataText));
	}

	if (iFilter)
	{
		QList<QVariant> params = iFilter->getParams();
		for (int i = 0; i < params.size(); i++)
		{
			pQuery->bindValue(i,params[i]);
		}
	}	

	if (!pQuery->exec())
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
	}

	return pQuery;
}

void EVeritabani::dbKapat()
{	
	mVT.close();
	
	mVT = QSqlDatabase();
	QSqlDatabase::removeDatabase(mConnectionName);
}

int EVeritabani::scripttenVTOlustur( const QString & iVTScript/* , QSqlDatabase & oVT */)
{
	checkVT();

	QString stQueries = iVTScript ;
	QStringList queries = stQueries.split(QUERY_SEPERATOR);
	int errorCount = 0;
	
	QSqlQuery query(mVT);
	for (int i = 0 ; i < queries.size();i++)
	{
		QString sql = queries[i].trimmed();
		if (!sql.isEmpty() && !query.exec(sql))
		{
			QSqlError error = query.lastError();
			QString errStr (error.text());
			int index = errStr.indexOf("already exists");
			if(index>=0)
				continue;						

			errStr = QString("Query : %1 \n Hata : %2").arg(queries[i].trimmed()).arg(error.text());
			dbKapat();
			throwVTEXCEPTION(EVeritabaniException::VTOlusturulamadi,QString("Veritabani olusturulurken hata olustu"));
			errorCount++;	
		}		
	}
	dbKapat();

	return SUCCESS;
}


int EVeritabani::dosyadanVTOlustur( const QString & iVTFile/* , QSqlDatabase & oVT */)
{

	QFile vtFile(iVTFile);
	if (!vtFile.open(QIODevice::ReadOnly | QIODevice::Text))
		return FAILURE;

	QString stQueries(vtFile.readAll().data());
	vtFile.close();

	return scripttenVTOlustur(stQueries);
}

const QSqlDatabase & EVeritabani::database()const
{
	return mVT;
}


QDateTime EVeritabani::longToDateTime(const qlonglong & iTime_milsec)
{
	uint u = iTime_milsec/1000 ;// miliseconds i seconds a ceviriyoruz.	

	return QDateTime::fromTime_t(u);
}


qlonglong EVeritabani::dateTimeToLong(const QDateTime & iDT)
{
	return  (qlonglong)iDT.toTime_t()*1000;
}


QString EVeritabani::connectionNameUret()
{
	QString retStr;
	retStr = QUuid::createUuid().toString();	
	return retStr;	
}

EVeritabani EVeritabani::sqLiteVTUret(const QString & iDBPath)
{
	QString cn ;
	QSqlDatabase sqlDb;
	{
		QMutexLocker locker(&mNameMutex);
		cn = connectionNameUret();
		sqlDb = QSqlDatabase::addDatabase("QSQLITE",cn);
	}
	sqlDb.setDatabaseName(iDBPath);
	EVeritabani vt(sqlDb);
	sqlDb = QSqlDatabase();
	QSqlDatabase::removeDatabase(cn);
	return vt;
}

EVeritabani * EVeritabani::sqLiteVTUretPtr(const QString & iDBPath)
{
	QSqlDatabase sqlDb;
	{
		QMutexLocker locker(&mNameMutex);
		QString cn = connectionNameUret();
		sqlDb = QSqlDatabase::addDatabase("QSQLITE",cn);		
		sqlDb.setDatabaseName(iDBPath);
	}
	return new EVeritabani(sqlDb);
}



void EVeritabani::sorguCalistir(const QString & iQuery,const ParamList & iParams )
{
	QSqlQuery* pQuery = NULL;
	try
	{
		pQuery = sorguYap(iQuery,iParams);
		DELETE_MEMORY(pQuery);
	}
	catch (...)
	{
		DELETE_MEMORY(pQuery);
		throw;
	}
}

QStringList EVeritabani::getTableNameList()
{
	QStringList retList;
	mVT.open();
	retList = mVT.tables();
	mVT.close();
	return retList;
	
}

bool EVeritabani::transactionBegin()
{
	return mVT.transaction();
}

bool EVeritabani::transactionCommit()
{
	bool retValue = true;
	if(!mVT.commit())
	{
		QString hata = mVT.lastError().text();
		QSqlDatabase::connectionNames();
		retValue=false;
	}
	return retValue;	
}

bool EVeritabani::transactionRollBack()
{
	return mVT.rollback();
}
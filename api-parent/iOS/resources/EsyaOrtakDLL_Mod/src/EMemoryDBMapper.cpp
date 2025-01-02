#include "EMemoryDBMapper.h"
#include "EVeritabaniException.h"
#include <QSqlQuery>
#include <QSqlError>
#include <QSqlRecord>
#include <QSqlTableModel>


#define SQL_GETTABLES "SELECT sql, tbl_name FROM sqlite_master WHERE type='table' AND sql NOT NULL and name NOT IN ('sqlite_stat1', 'sqlite_sequence')"
#define SQL_SELECT(TableName) QString("SELECT * FROM %1").arg(TableName)


using namespace esya;

EMemoryDBMapper::EMemoryDBMapper()
{

}

EMemoryDBMapper::~EMemoryDBMapper()
{

}

bool EMemoryDBMapper::_createTables(const  QSqlDatabase& iDBsrc, QSqlDatabase& iDBdst)
{
	QSqlQuery tablesQuery(iDBsrc);

	if (!tablesQuery.exec(SQL_GETTABLES))
	{
		QSqlError err = tablesQuery.lastError();
		throw EVeritabaniException(EVeritabaniException::VTTanimsiz,err.text());			
	}

	
	QSqlQuery query(iDBdst);
	while (tablesQuery.next())
	{
		QString tableSQL = tablesQuery.record().value(0).toString();
		QString tableName = tablesQuery.record().value(1).toString();

		if (!query.exec(tableSQL))
		{
			QSqlError err = query.lastError();
			throw EVeritabaniException(EVeritabaniException::VTTanimsiz,err.text());			
		}

		_copyTableData(iDBsrc,iDBdst,tableName);
	}
	return true;
}

bool EMemoryDBMapper::_copyTableData(const  QSqlDatabase& iDBsrc, QSqlDatabase& iDBdst,const QString & iTableName)
{
	QString selectSQL = SQL_SELECT(iTableName);

	QSqlQuery query(iDBsrc);

	if (query.exec(selectSQL))
	{
		QSqlTableModel dstModel(0,iDBdst);
		dstModel.setTable(iTableName);

		while(query.next())
		{
			if (!dstModel.insertRecord(-1,query.record()))
			{
				QSqlError err = dstModel.lastError();
				throw EVeritabaniException(EVeritabaniException::VTTanimsiz,err.text());			
			}
		}
	}
	else 
	{
		QSqlError err = query.lastError();
		throw EVeritabaniException(EVeritabaniException::VTTanimsiz,err.text());			
	}
	return true;
}



QSqlDatabase EMemoryDBMapper::mapToMemory(const QSqlDatabase & iDB)
{
	QSqlDatabase db = iDB;
	if (!db.isValid() ) 
	{	
		throwVTEXCEPTION(EVeritabaniException::VTTanimsiz,QString("VERITABANI ACMA HATASI Hata : %1").arg(iDB.lastError().text()));
	}
	else if (!db.isOpen() && !db.open())
	{		
		QString errStr = db.lastError().text();
		throwVTEXCEPTION(EVeritabaniException::VTAcilamadi,QString("VERITABANI ACMA HATASI Hata : %1").arg(errStr));	
	}
	
	QSqlDatabase memDB = QSqlDatabase::addDatabase("QSQLITE");
	memDB.setDatabaseName(QSQLDB_MEMORY_DB_NAME);
	if (!memDB.open()) 
	{
		throw EVeritabaniException(EVeritabaniException::VTAcilamadi,"");
	}
	
	_createTables(db,memDB);

	return memDB;
}
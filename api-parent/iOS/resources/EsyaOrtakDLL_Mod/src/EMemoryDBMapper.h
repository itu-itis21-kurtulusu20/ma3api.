#ifndef EMEMORYDBMAPPER_H
#define EMEMORYDBMAPPER_H

#include <QSqlDatabase>

#define QSQLDB_MEMORY_DB_NAME ":memory:"

namespace esya
{

	class Q_DECL_EXPORT EMemoryDBMapper 
	{

		bool _createTables(const  QSqlDatabase& iDBsrc, QSqlDatabase& iDBdst);
		bool _copyTableData(const  QSqlDatabase& iDBsrc, QSqlDatabase& iDBdst,const QString & iTableName);

	public:
		EMemoryDBMapper();
		~EMemoryDBMapper();

		QSqlDatabase mapToMemory(const QSqlDatabase & iDB);

	private:
		
	};

}

#endif // EMEMORYDBMAPPER_H

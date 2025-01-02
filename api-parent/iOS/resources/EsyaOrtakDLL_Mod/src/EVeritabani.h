#ifndef __EVERITABANI__
#define __EVERITABANI__


#include <QSqlDatabase>
#include <QSqlError>
#include <QSqlQuery>
#include <QSqlRecord>
#include <QList>
#include <QPair>
#include "EVeritabaniException.h"
#include <QVariant>
#include <QMutex>

#define QUERY_SEPERATOR ";"

namespace esya
{
	class EFilter;
	typedef QList<QPair<QString,QVariant> > ParamList;

	class Q_DECL_EXPORT EVeritabani
	{
		QSqlDatabase mVT;
		QString mLastError;
		QString mConnectionName;
	
		static QMutex mNameMutex;


	public:
		EVeritabani(void);
		EVeritabani(const QSqlDatabase &);
		EVeritabani(const EVeritabani &);


		bool transactionBegin();
		bool transactionCommit();
		bool transactionRollBack();

		const QString & getLastError() const{return mLastError;};

		bool open();
		void close(){return mVT.close();};
		bool isOpen(){return mVT.isOpen();};


		bool checkVT();
		void dbKapat();


		QSqlQuery* sorguYap(const QString & irQueryText,const ParamList & iParameters);
		QSqlQuery* sorguYap(const QString & irQueryText,EFilter* iFilter,const QString & iAfterWhereQuery="");

		void sorguCalistir( const QString & iQuery,const ParamList & iParams = ParamList() );
 

		int dosyadanVTOlustur(const QString & iVTFile);
		int scripttenVTOlustur(const QString & iVTScript);

		const QSqlDatabase & database()const; 
		
		virtual ~EVeritabani(void);


		static QDateTime longToDateTime(const qlonglong & );
		static qlonglong dateTimeToLong(const QDateTime & );

		static QString		connectionNameUret();
		static EVeritabani	sqLiteVTUret(const QString & iDBPath);
		static EVeritabani	* sqLiteVTUretPtr(const QString & iDBPath);

		QStringList			getTableNameList();



	};


}



#endif
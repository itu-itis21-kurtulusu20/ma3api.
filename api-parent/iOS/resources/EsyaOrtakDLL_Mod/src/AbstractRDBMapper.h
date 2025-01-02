#ifndef ABSTRACTRDBMAPPER_H
#define ABSTRACTRDBMAPPER_H

#include "AbstractPersistenceMapper.h"
#include <QSqlQuery>
#include <QSqlDatabase>
#include <QVariantList>
#include <QStringList>
#include "EVeritabani.h"
#include <QSqlQuery>
using namespace esya;
class AbstractRDBMapper : public AbstractPersistenceMapper
{
	QString mTableName;
	QString mIDName;
	QString mOrderFieldName;
	QSqlDatabase * mpSqliteDB;
	bool mCloseDBOnExit;
	void _kontrolEtAc();
	QSqlQuery* _sorguYap(const QString & irQueryText,const ParamList & iParameters);
	QSqlQuery* _sorguYap(const QString & irQueryText,EFilter * ipFilter);
	EFilter * _constructEFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar);
public:
	Q_DECL_EXPORT AbstractRDBMapper();
	Q_DECL_EXPORT AbstractRDBMapper(QSqlDatabase * ipSqliteDB,const QString  & iTableName,const QString & iIDName,QObject *parent=0); 
	Q_DECL_EXPORT ~AbstractRDBMapper();
	Q_DECL_EXPORT void setOrderFieldName(const QString &iOrderFieldName);
	Q_DECL_EXPORT void setCloseDBOnExit(bool iCloseDBOnExit){mCloseDBOnExit=iCloseDBOnExit;};

private:
	Q_DECL_EXPORT QSqlQuery		*   getDBRecord(int iObjID=-1);
protected:
	Q_DECL_EXPORT QObject			*	getObjectFromStorage(int iObjID);
	Q_DECL_EXPORT QList<QObject *>    getAllObjectsFromStorage();
	Q_DECL_EXPORT virtual QObject *   getObjectFromRecord(int iObjID,const QSqlRecord  &iRecord)=0;	

	Q_DECL_EXPORT QList<QObject*> getObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar);
	Q_DECL_EXPORT QList<QObject*> getObjectsFromStorageWithFilter(EFilter * ipFilter);

	Q_DECL_EXPORT int deleteObjectsFromStorageWithFilter(EFilter * ipFilter);

	Q_DECL_EXPORT int deleteObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar);
	Q_DECL_EXPORT int updateObjectsOnStorageWithFilter(const QStringList & iFilterAlanlar,const QVariantList & iFilterAranacaklar,const QStringList & iUpdateAlanlar,const QVariantList & iUpdateDegerler);

	Q_DECL_EXPORT int updateObjectOnDb(int iObjID,QObject * iObj);

	Q_DECL_EXPORT int   putObjectToStorage(int iObjID,QObject * iObj);
	Q_DECL_EXPORT bool removeObjectFromStorage(int iObjID);
	Q_DECL_EXPORT virtual QStringList		getFieldNameList()=0;
	Q_DECL_EXPORT virtual QVariantList	    getFieldValueList()=0;	
	
};

#endif // ABSTRACTRDBMAPPER_H

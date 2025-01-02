#ifndef ABSTRACTPERSISTENCEMAPPER_H
#define ABSTRACTPERSISTENCEMAPPER_H

#include <QObject>
#include "IMapper.h"
#include <QVariantList>
#include <QStringList>
#include "EFilter.h"

using namespace esya;

class AbstractPersistenceMapper : public IMapper
{
public:
	Q_DECL_EXPORT AbstractPersistenceMapper(QObject *parent=0);
	Q_DECL_EXPORT ~AbstractPersistenceMapper();
	Q_DECL_EXPORT QObject * get(int iObjID);
	Q_DECL_EXPORT QList<QObject*> bul(const QStringList & iAlanlar,const QVariantList & iAranacaklar);
	Q_DECL_EXPORT QList<QObject*> find(EFilter * ipFilter);
	Q_DECL_EXPORT int put(int iObjID,QObject * iObj);
	Q_DECL_EXPORT int update(int iObjID,QObject * iObj);
	Q_DECL_EXPORT QList<QObject * > getAll();
	Q_DECL_EXPORT bool deleteFromDB(int iObjID);
	Q_DECL_EXPORT int deleteFromDB(const QStringList & iAlanlar,const QVariantList & iAranacaklar);
	Q_DECL_EXPORT int deleteFromDB(EFilter * ipFilter);
	Q_DECL_EXPORT int batchUpdate(const QStringList & iFilterAlanlar,const QVariantList & iFilterAranacaklar,const QStringList & iUpdateAlanlar,const QVariantList & iUpdateDegerler);
private:
protected:
	Q_DECL_EXPORT virtual QObject * getObjectFromStorage(int iObjID)=0;
	Q_DECL_EXPORT virtual QList<QObject *>    getAllObjectsFromStorage()=0;
	Q_DECL_EXPORT virtual int putObjectToStorage(int iObjID,QObject * iObj)=0;
	Q_DECL_EXPORT virtual bool removeObjectFromStorage(int iObjID)=0;
	Q_DECL_EXPORT virtual QList<QObject*> getObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar)=0;
	Q_DECL_EXPORT virtual QList<QObject*> getObjectsFromStorageWithFilter(EFilter * ipFilter)=0;

	Q_DECL_EXPORT virtual int deleteObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar)=0;
	Q_DECL_EXPORT virtual int deleteObjectsFromStorageWithFilter(EFilter * ipFilter)=0;

	Q_DECL_EXPORT virtual int updateObjectsOnStorageWithFilter(const QStringList & iFilterAlanlar,const QVariantList & iFilterAranacaklar,const QStringList & iUpdateAlanlar,const QVariantList & iUpdateDegerler)=0;
	Q_DECL_EXPORT virtual int updateObjectOnDb(int iObjID,QObject * iObj)=0;
};

#endif // ABSTRACTPERSISTENCEMAPPER_H


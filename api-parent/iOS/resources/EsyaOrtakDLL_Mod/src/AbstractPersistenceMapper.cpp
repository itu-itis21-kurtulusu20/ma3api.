#include "AbstractPersistenceMapper.h"

AbstractPersistenceMapper::AbstractPersistenceMapper(QObject *parent)
	: IMapper(parent)
{

}


QObject * AbstractPersistenceMapper::get(int iObjID)
{
	return getObjectFromStorage(iObjID);
}

int AbstractPersistenceMapper::put(int iObjID,QObject * iObj)
{
	return putObjectToStorage(iObjID,iObj);
}

int AbstractPersistenceMapper::update(int iObjID,QObject * iObj)
{
	return updateObjectOnDb(iObjID,iObj);
}

int AbstractPersistenceMapper::batchUpdate(const QStringList & iFilterAlanlar,const QVariantList & iFilterAranacaklar,const QStringList & iUpdateAlanlar,const QVariantList & iUpdateDegerler)
{
	return updateObjectsOnStorageWithFilter(iFilterAlanlar,iFilterAranacaklar,iUpdateAlanlar,iUpdateDegerler);
}

QList<QObject * > AbstractPersistenceMapper::getAll()
{
	return getAllObjectsFromStorage();
}

bool AbstractPersistenceMapper::deleteFromDB(int iObjID)
{
	return removeObjectFromStorage(iObjID);
}

int AbstractPersistenceMapper::deleteFromDB(EFilter * ipFilter)
{
	return deleteObjectsFromStorageWithFilter(ipFilter);
}

QList<QObject *> AbstractPersistenceMapper::bul(const QStringList & iAlanlar,const QVariantList & iAranacaklar)
{
	return getObjectsFromStorageWithFilter(iAlanlar,iAranacaklar);
}

QList<QObject *> AbstractPersistenceMapper::find(EFilter * ipFilter)
{
	return getObjectsFromStorageWithFilter(ipFilter);
}

int AbstractPersistenceMapper::deleteFromDB(const QStringList & iAlanlar,const QVariantList & iAranacaklar)
{
	return deleteObjectsFromStorageWithFilter(iAlanlar,iAranacaklar);
}
AbstractPersistenceMapper::~AbstractPersistenceMapper()
{

}

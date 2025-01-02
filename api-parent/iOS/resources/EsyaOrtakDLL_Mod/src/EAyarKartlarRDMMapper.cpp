#include "EAyarKartlarRDMMapper.h"
#include <QVariant>
#include <QSqlRecord>
NAMESPACE_BEGIN(esya)

EAyarKartlarRDMMapper::EAyarKartlarRDMMapper()
:AbstractRDBMapper(NULL,"","",0)
{

}
EAyarKartlarRDMMapper::EAyarKartlarRDMMapper(QSqlDatabase * ipSqliteDB,const QString & iTableName,const QString & iIDName,QObject *parent/* =0 */)
: AbstractRDBMapper(ipSqliteDB,iTableName,iIDName,parent)
{
}

EAyarKartlarRDMMapper::~EAyarKartlarRDMMapper(void)
{
}

QObject * EAyarKartlarRDMMapper::getObjectFromRecord(int iObjID,const QSqlRecord &iRecord)
{
	EAyarKartlar * opKartAyar=new EAyarKartlar(iRecord.value(EAyarKartlarRDMMapper_msFieldNameKartID).toInt(),
		iRecord.value(EAyarKartlarRDMMapper_msFieldNameKartAdi).toString(),
		iRecord.value(EAyarKartlarRDMMapper_msFieldNameKartLib).toString(),
		iRecord.value(EAyarKartlarRDMMapper_msFieldNameImzalamaCSPAdi).toString(),
		iRecord.value(EAyarKartlarRDMMapper_msFieldNameSifrelemeCSPAdi).toString()		
		);
	return (QObject *)opKartAyar;
}
QStringList			EAyarKartlarRDMMapper::getFieldNameList()
{
	return QStringList();
}
QVariantList EAyarKartlarRDMMapper::getFieldValueList()
{
	return QVariantList();
}
NAMESPACE_END

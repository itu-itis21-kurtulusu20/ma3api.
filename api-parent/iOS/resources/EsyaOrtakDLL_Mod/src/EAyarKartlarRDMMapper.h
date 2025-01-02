#ifndef __E_AYAR_KARTLAR_ABSTRACT_RDB_MAPPER_H_
#define __E_AYAR_KARTLAR_ABSTRACT_RDB_MAPPER_H_
#include "AbstractRDBMapper.h"
#include "EAyarKartlar.h"

#define  EAyarKartlarRDMMapper_msFieldNameKartID "KartID"
#define  EAyarKartlarRDMMapper_msFieldNameKartAdi "KartAdi"
#define  EAyarKartlarRDMMapper_msFieldNameKartLib "KartLib"
#define  EAyarKartlarRDMMapper_msFieldNameImzalamaCSPAdi "ImzalamaCSPAdi"
#define  EAyarKartlarRDMMapper_msFieldNameSifrelemeCSPAdi "SifrelemeCSPAdi"
#define  EAyarKartlarRDMMapper_msTableName "TBL_KARTLAR"

NAMESPACE_BEGIN(esya)
class EAyarKartlarRDMMapper :
	public AbstractRDBMapper
{
public:	
	Q_DECL_EXPORT  EAyarKartlarRDMMapper();
	EAyarKartlarRDMMapper(QSqlDatabase * ipSqliteDB,const QString  & iTableName,const QString & iIDName,QObject *parent=0);
	Q_DECL_EXPORT ~EAyarKartlarRDMMapper();	
protected:
	QObject *			getObjectFromRecord(int iObjID,const QSqlRecord  &iRecord);
	QStringList			getFieldNameList();
	QVariantList		getFieldValueList();	
};
NAMESPACE_END
#endif

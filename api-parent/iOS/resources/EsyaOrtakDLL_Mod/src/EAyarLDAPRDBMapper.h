#ifndef __E_AYAR_LDAP_RDB_MAPPER_H_
#define __E_AYAR_LDAP_RDB_MAPPER_H_
#include "AbstractRDBMapper.h"
#include "EAyarLdap.h"

#define EAyarLDAPRDBMapper_msFieldNameLdapID "LdapID"
#define EAyarLDAPRDBMapper_msFieldNameIP "IP"
#define EAyarLDAPRDBMapper_msFieldNamePort "Port"
#define EAyarLDAPRDBMapper_msFieldNameBaglantiTipi "BaglantiTipi"
#define EAyarLDAPRDBMapper_msFieldNameSizeLimit "SizeLimit"	
#define EAyarLDAPRDBMapper_msFieldNameTimeLimit "TimeLimit"
#define EAyarLDAPRDBMapper_msFieldNameSearchBase "SearchBase"
#define EAyarLDAPRDBMapper_msFieldNameLdapTipi "LdapTipi"
#define EAyarLDAPRDBMapper_msFieldNameVarsayilan "Varsayilan"

#define EAyarLDAPRDBMapper_msTableName "TBL_LDAP"

NAMESPACE_BEGIN(esya)
class EAyarLDAPRDBMapper :
	public AbstractRDBMapper
{
public:
	Q_DECL_EXPORT  EAyarLDAPRDBMapper();
	Q_DECL_EXPORT EAyarLDAPRDBMapper(QSqlDatabase * ipSqliteDB,const QString  & iTableName,const QString & iIDName,QObject *parent=0);
	Q_DECL_EXPORT ~EAyarLDAPRDBMapper();	
protected:
	QObject *			getObjectFromRecord(int iObjID,const QSqlRecord  &iRecord);
	QStringList			getFieldNameList();
	QVariantList		getFieldValueList();	
};
NAMESPACE_END
#endif

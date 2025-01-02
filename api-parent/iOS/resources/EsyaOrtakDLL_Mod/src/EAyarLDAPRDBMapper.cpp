#include "EAyarLDAPRDBMapper.h"
#include "EAyarLdap.h"
#include <QVariant>
#include <QSqlRecord>
#include "EAyarValueTool.h"
NAMESPACE_BEGIN(esya)

EAyarLDAPRDBMapper::EAyarLDAPRDBMapper()
:AbstractRDBMapper(NULL,"","",0)
{

}
EAyarLDAPRDBMapper::EAyarLDAPRDBMapper(QSqlDatabase * ipSqliteDB,const QString & iTableName,const QString & iIDName,QObject *parent/* =0 */)
: AbstractRDBMapper(ipSqliteDB,iTableName,iIDName,parent)
{
}

EAyarLDAPRDBMapper::~EAyarLDAPRDBMapper(void)
{
}

QObject * EAyarLDAPRDBMapper::getObjectFromRecord(int iObjID,const QSqlRecord &iRecord)
{
	EAyarLdap * opAyarLDAP = new EAyarLdap(iRecord.value(EAyarLDAPRDBMapper_msFieldNameLdapID).toInt(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNameIP).toString(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNamePort).toInt(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNameBaglantiTipi).toInt(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNameSizeLimit).toInt(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNameTimeLimit).toInt(),
	iRecord.value(EAyarLDAPRDBMapper_msFieldNameSearchBase).toString(),
	(EAyarLdap::LDAP_TIPI)iRecord.value(EAyarLDAPRDBMapper_msFieldNameLdapTipi).toInt(),
	EAyarValueTool::getBoolDeger(iRecord.value(EAyarLDAPRDBMapper_msFieldNameVarsayilan)));

	return (QObject *)opAyarLDAP;
}

QStringList	 EAyarLDAPRDBMapper::getFieldNameList()
{
	return QStringList();
}
QVariantList EAyarLDAPRDBMapper::getFieldValueList()
{
	return QVariantList();
}
NAMESPACE_END

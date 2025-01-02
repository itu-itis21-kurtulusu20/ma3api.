#include "PersistenceFacade.h"
#include "AbstractRDBMapper.h"
#include "ELoggerRDMMapper.h"
#include "EAyarKartlarRDMMapper.h"
#include "EAyarLDAPRDBMapper.h"
using namespace esya;

PersistenceFacade * PersistenceFacade::mInstance = NULL ;
PersistenceFacade::PersistenceFacade(QObject *parent)
	: QObject(parent)
{

}

PersistenceFacade::~PersistenceFacade()
{

}

PersistenceFacade * PersistenceFacade::getInstance()
{
	if (PersistenceFacade::mInstance == NULL)
	{
		PersistenceFacade::mInstance = new PersistenceFacade();
	}
	return PersistenceFacade::mInstance;
}

IMapper * PersistenceFacade::getMapper(QSqlDatabase * ipSqliteDb,const QString &iClassName)
{
	IMapper * lMapper=NULL;
	if (iClassName == KERMEN_CLASS_ELOGGER)
	{	
		lMapper = new ELoggerRDMMapper(ipSqliteDb,"TBL_LOG","LogID");		
	}
	else
	if (iClassName == KERMEN_CLASS_EAYAR_KARTLAR)
	{	
		lMapper = new EAyarKartlarRDMMapper(ipSqliteDb,EAyarKartlarRDMMapper_msTableName,EAyarKartlarRDMMapper_msFieldNameKartID);		
	}	
	else
	if (iClassName == KERMEN_CLASS_EAYAR_LDAP)
	{	
		lMapper = new EAyarLDAPRDBMapper(ipSqliteDb,EAyarLDAPRDBMapper_msTableName,EAyarLDAPRDBMapper_msFieldNameLdapID);		
	}		
	return lMapper;
}

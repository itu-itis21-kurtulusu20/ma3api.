#include "DBLayer.h"

#include "DBSession.h"
#include "SqliteDBSession.h"
#include "EVeritabani.h"
using namespace esya;
DBLayer::DBLayer(void)
{
}

DBLayer::~DBLayer(void)
{
}

DBSession * DBLayer::getSession(DBLayer::DB_TYPE iType,const QString & iDBPath)
{
	if (iType == DBLayer::DB_TYPE_CERT_DB)
	{
		EVeritabani * pDb = EVeritabani::sqLiteVTUretPtr(iDBPath);
		return new SqliteDBSession(pDb);
	}
	return NULL;
}

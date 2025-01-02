#ifndef _DB_LAYER_H_
#define _DB_LAYER_H_

#include "EVeritabani.h"
#include "DBSession.h"
NAMESPACE_BEGIN(esya)
class DBLayer
{
public:
	enum DB_TYPE{DB_TYPE_CERT_DB=0};
	DBLayer(void);
	~DBLayer(void);
	static DBSession * getSession(DBLayer::DB_TYPE iType,const QString & iDBPath);
};
NAMESPACE_END
#endif
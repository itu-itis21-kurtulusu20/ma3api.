#ifndef _SQLITE_DB_SESSION_H_
#define _SQLITE_DB_SESSION_H_
#include "DBSession.h"
#include "EVeritabani.h"
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT SqliteDBSession :
	public DBSession
{
public:
	SqliteDBSession(EVeritabani * ipDb);
	~SqliteDBSession(void);
	AtomicWork * startAtomicWork();
};
NAMESPACE_END
#endif

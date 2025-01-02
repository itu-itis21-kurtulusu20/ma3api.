#include "SqliteDBSession.h"
#include "SQliteAtomicWork.h"
using namespace esya;
SqliteDBSession::SqliteDBSession(EVeritabani * ipDb)
:DBSession(ipDb)
{
}

SqliteDBSession::~SqliteDBSession(void)
{
}

AtomicWork * SqliteDBSession::startAtomicWork()
{
	return new SQliteAtomicWork(mpDB);
}

#ifndef _DB_SESSION_H_
#define _DB_SESSION_H_

#include "EVeritabani.h"
#include "AtomicWork.h"
NAMESPACE_BEGIN(esya)
class DBSession
{
public:
	EVeritabani * mpDB;
public:
	DBSession(EVeritabani * ipDb);
	virtual AtomicWork * startAtomicWork()=0;
	bool open();
	bool close();
	~DBSession(void);
};
NAMESPACE_END
#endif
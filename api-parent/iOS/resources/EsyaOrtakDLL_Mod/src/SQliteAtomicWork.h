#ifndef _SQLite_Atomic_Work__H__
#define _SQLite_Atomic_Work__H__

#include "AtomicWork.h"
#include "EVeritabani.h"
NAMESPACE_BEGIN(esya)
class SQliteAtomicWork :
	public AtomicWork
{
	EVeritabani * mpDb;
public:
	SQliteAtomicWork(EVeritabani * ipVeritabani);
	~SQliteAtomicWork(void);
	void finish();
	bool undo();
	void start();
};
NAMESPACE_END
#endif

#ifndef __BASE_TRANSACTION_H__
#define __BASE_TRANSACTION_H__
#include "DBSession.h"
#include "AtomicWork.h"
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT BaseTransaction
{
protected:
	AtomicWork * mpAtomicWork;
	DBSession * mpDbSession;
public:
	BaseTransaction(void);
	virtual ~BaseTransaction(void);
	virtual DBSession * getDBSession()=0;
	void init();
	void end();
	void rollBack();
	virtual void doAtomicWork()=0;
};
NAMESPACE_END;
#endif

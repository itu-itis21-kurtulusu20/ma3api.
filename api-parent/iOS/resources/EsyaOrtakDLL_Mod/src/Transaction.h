#ifndef _TRANSACTION_H_
#define _TRANSACTION_H_

#include "BaseTransaction.h"

NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT Transaction
{
public:
	Transaction(void);
	~Transaction(void);
	static bool doAtomicWork(BaseTransaction * ipTransaction);
};
NAMESPACE_END
#endif

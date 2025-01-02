#include "Transaction.h"
#include "EException.h"

using namespace esya;
Transaction::Transaction(void)
{
}

Transaction::~Transaction(void)
{
}

bool Transaction::doAtomicWork(BaseTransaction * ipTransaction)
{
	try
	{
		ipTransaction->init();				
	}
	catch (EException & aEx)
	{
		return false;
	}

	try
	{
		ipTransaction->doAtomicWork();		
	} catch (EException & aEx)
	{
		ipTransaction->rollBack();
		return false;
	}

	try
	{
		ipTransaction->end();
	}
	catch (EException & exc)
	{
		ipTransaction->rollBack();
		return false;
	}
	return true;
}
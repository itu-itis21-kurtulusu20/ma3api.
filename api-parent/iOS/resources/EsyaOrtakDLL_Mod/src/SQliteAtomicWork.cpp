#include "SQliteAtomicWork.h"
using namespace  esya;
SQliteAtomicWork::SQliteAtomicWork(EVeritabani * ipDb)
:AtomicWork(),mpDb(ipDb)
{
}

SQliteAtomicWork::~SQliteAtomicWork(void)
{
}

void SQliteAtomicWork::finish()
{		
	if(mpDb->transactionCommit())
	{
		mIsFinished = true;
	}
}

void SQliteAtomicWork::start()
{
	mpDb->transactionBegin();
}

bool SQliteAtomicWork::undo()
{
	bool retValue = false;
	if(mpDb->transactionRollBack())
	{
		retValue = true;
		mIsUndone = true;
	}	
	return retValue;
}

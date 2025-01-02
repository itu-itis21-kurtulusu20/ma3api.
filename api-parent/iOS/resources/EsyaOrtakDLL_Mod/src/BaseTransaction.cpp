#include "BaseTransaction.h"

using namespace esya;
BaseTransaction::BaseTransaction(void)
{
}

BaseTransaction::~BaseTransaction(void)
{
}

void BaseTransaction::init()
{
	mpDbSession = getDBSession();
	mpDbSession->open();
	mpAtomicWork = mpDbSession->startAtomicWork();	
	mpAtomicWork->start();
}

void BaseTransaction::end()
{
	mpAtomicWork->finish();
	mpDbSession->close();	
}

void BaseTransaction::rollBack()
{
	mpAtomicWork->undo();
}

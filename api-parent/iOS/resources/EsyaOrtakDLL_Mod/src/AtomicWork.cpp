#include "AtomicWork.h"

AtomicWork::AtomicWork(void)
:mIsUndone(false),mIsFinished(false)
{
}

AtomicWork::~AtomicWork(void)
{
}

bool AtomicWork::isActive()
{
	return !isFinished()&&!isUndone();
}
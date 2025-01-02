#include "DebugSeviyeLogcu.h"
#include "ELoggerFactory.h"
#include <QMutexLocker>

using namespace esya;


DebugSeviyeLogcu::DebugSeviyeLogcu(void)
:AbstractSeviyeLogcu()
{
}

DebugSeviyeLogcu::~DebugSeviyeLogcu(void)
{
}

void DebugSeviyeLogcu::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}

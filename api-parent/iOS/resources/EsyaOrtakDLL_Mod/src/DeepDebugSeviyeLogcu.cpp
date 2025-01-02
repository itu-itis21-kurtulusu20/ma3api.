#include "DeepDebugSeviyeLogcu.h"
#include "ELoggerFactory.h"
#include <QMutexLocker>

using namespace esya;


DeepDebugSeviyeLogcu::DeepDebugSeviyeLogcu(void)
:AbstractSeviyeLogcu()
{
}

DeepDebugSeviyeLogcu::~DeepDebugSeviyeLogcu(void)
{
}

void DeepDebugSeviyeLogcu::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}

#include "PerformanceInfoSeviyeLogcu.h"
#include <QMutexLocker>
#include "ELoggerFactory.h"

PerformanceInfoSeviyeLogcu::PerformanceInfoSeviyeLogcu(void)
:AbstractSeviyeLogcu()
{
}

PerformanceInfoSeviyeLogcu::~PerformanceInfoSeviyeLogcu(void)
{
}

void PerformanceInfoSeviyeLogcu::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}
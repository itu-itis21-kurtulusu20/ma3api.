#include "ErrorSeviyeLogcu.h"
#include <QMutexLocker>
#include "ELoggerFactory.h"

ErrorSeviyeLogcu::ErrorSeviyeLogcu(void)
:AbstractSeviyeLogcu()
{
}

ErrorSeviyeLogcu::~ErrorSeviyeLogcu(void)
{
}

void ErrorSeviyeLogcu::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}
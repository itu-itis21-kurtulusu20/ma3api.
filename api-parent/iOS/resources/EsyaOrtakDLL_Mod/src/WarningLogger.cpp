#include "WarningLogger.h"
#include "ELoggerFactory.h"
#include <QMutexLocker>

using namespace esya;


WarningLogger::WarningLogger(void)
:AbstractSeviyeLogcu()
{
}

WarningLogger::~WarningLogger(void)
{
}

void WarningLogger::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}

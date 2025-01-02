#include "InfoLogger.h"
#include "ELoggerFactory.h"
#include <QMutexLocker>

using namespace esya;


InfoLogger::InfoLogger(void)
:AbstractSeviyeLogcu()
{
}

InfoLogger::~InfoLogger(void)
{
}

void InfoLogger::writeToLog(const ELogEntry & iLogEntry)
{
	QMutexLocker ml(&AbstractSeviyeLogcu::mMutex);
	ELoggerFactory::getLoggerChain()->writeToLog(iLogEntry);
}

#include "ESeviyeLogYonetici.h"
#include "ELoggerFactory.h"
#include "BosSeviyeLogcu.h"
#include "DebugSeviyeLogcu.h"
#include "ErrorSeviyeLogcu.h"
#include "DeepDebugSeviyeLogcu.h"
#include "PerformanceInfoSeviyeLogcu.h"
#include "InfoLogger.h"
#include "WarningLogger.h"
#include <QMutexLocker>

#define DEFAULT_LOG_THRESHOLD	ELogEntry::P_ERROR 
using namespace esya;

QMutex ESeviyeLogYonetici::mpMutex;
ESeviyeLogYonetici::ESeviyeLogYonetici(void)
{
	ELogEntry::PRIORITY esik=(ELogEntry::PRIORITY)ELoggerFactory::esikSeviyesiAl();
	if(esik<=ELogEntry::P_DEEP_DEBUG)
	{
		mpAyrintiliDebugSeviyeLogcu = new DeepDebugSeviyeLogcu();
	}
	else
	{
		mpAyrintiliDebugSeviyeLogcu=new BosSeviyeLogcu();
	}

	if(esik<=ELogEntry::P_DEBUG)
	{
		mpDebugSeviyeLogcu = new DebugSeviyeLogcu();
	}
	else
	{
		mpDebugSeviyeLogcu = new BosSeviyeLogcu();
	}
	if(esik<=ELogEntry::P_INFO)
	{
		mpInfoLogger = new InfoLogger();
	}
	else
	{
		mpInfoLogger = new BosSeviyeLogcu();
	}

	if(esik<=ELogEntry::P_WARNING)
	{
		mpWarningLogger = new WarningLogger();
	}
	else
	{
		mpWarningLogger = new BosSeviyeLogcu();
	}

	if(esik<=ELogEntry::P_PERFORMANCE_INFO)
	{
		mpPermonceInfoSeviyeLogcu = new PerformanceInfoSeviyeLogcu();
	}
	else
	{
		mpPermonceInfoSeviyeLogcu = new BosSeviyeLogcu();
	}
	if(esik<=ELogEntry::P_ERROR)
	{
		mpErrorSeviyeLogcu = new ErrorSeviyeLogcu();
	}
	else
	{
		mpErrorSeviyeLogcu = new BosSeviyeLogcu();
	}
}

ESeviyeLogYonetici::~ESeviyeLogYonetici(void)
{
}


ESeviyeLogYonetici * ESeviyeLogYonetici::mInstance=NULL;
ESeviyeLogYonetici * ESeviyeLogYonetici::getInstance()
{
	QMutexLocker ml(&ESeviyeLogYonetici::mpMutex);
	if(mInstance == NULL)
	{
			mInstance = new ESeviyeLogYonetici();
	}
	return mInstance;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getDebugSeviyeLogcu()
{
	return mpDebugSeviyeLogcu;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getErrorSeviyeLogcu()
{
	return mpErrorSeviyeLogcu;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getAyrintiliDebugSeviyeLogcu()
{
	return mpAyrintiliDebugSeviyeLogcu;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getPermonceInfoSeviyeLogcu()
{
	return mpPermonceInfoSeviyeLogcu;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getInfoLogger()
{
	return mpInfoLogger;
}

AbstractSeviyeLogcu * ESeviyeLogYonetici::getWarningLogger()
{	
	return mpWarningLogger;
}
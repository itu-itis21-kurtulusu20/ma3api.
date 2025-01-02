#include "Scheduler.h"
#include "GUIDGenerator.h"
#include "MemoryTaskCollector.h"
#include "ScheduleTaskExecuter.h"
#include "SchedulingPattern.h"
#include "ESynchroniseManager.h"
#include "ScheduleLauncherThread.h"
#include "ScheduleTimerThread.h"
#include "EsyaOrtak_Ortak.h"

#define MUTEX_NAME_TASK_EXECUTER_OPERATIONS "ScheduleTaskExecutorOperation"
#define MUTEX_NAME_TASK_LAUNCHER_OPERATIONS "ScheduleTaskLauncherOperation"

Scheduler::Scheduler()
:mpScheduleTimerThread(NULL)
{
	mGUID =  GUIDGenerator::generate();
	mStarted = false;
	mpMemoryTaskCollector = new MemoryTaskCollector();
	mpTaskCollectors<<mpMemoryTaskCollector;	
}

Scheduler::~Scheduler(void)
{
	ESYA_ORTAK_FUNC_BEGIN;	
	stopScheduling();
	qDeleteAll(mpTaskCollectors);
	qDeleteAll(mpTaskExecuters);
	qDeleteAll(mpLaunchers);
	DELETE_MEMORY(mpScheduleTimerThread);
	ESYA_ORTAK_FUNC_END;
}
bool Scheduler::getIsStarted()
{
	QMutexLocker locker(&mMutex);
	return mStarted;
}

const QString Scheduler::getGUID()
{
	return mGUID;
}

QString Scheduler::schedule(const QString & iSchedulingPattern,SchedulerTask * ipTask)
{	
	return schedule(new SchedulingPattern(iSchedulingPattern),ipTask);
}

QString Scheduler::schedule(SchedulingPattern * ipSchedulingPattern,SchedulerTask * ipTask)
{
	mpSchedulingPattern =  ipSchedulingPattern ;
	return mpMemoryTaskCollector->add(ipSchedulingPattern,ipTask);
}

void Scheduler::startScheduling(const QDateTime & iStartDt)
{
	QMutexLocker mutexLocker(&mMutex);
	if (mStarted)
	{
		return;
	}
	mpScheduleTimerThread = new ScheduleTimerThread(this);
	mpScheduleTimerThread->setStartDt(iStartDt);
	mpScheduleTimerThread->start();	
	mStarted=true;
}

void Scheduler::stopScheduling()
{
	QMutexLocker mutexLocker(&mMutex);
	if (!mStarted)
	{
		return;
	}	
	if (mpScheduleTimerThread)
	{
		mpScheduleTimerThread->interrupt();
		mpScheduleTimerThread->terminate();
		mpScheduleTimerThread->wait();
		DELETE_MEMORY(mpScheduleTimerThread);
	}	
	mStarted=false;
}

 ScheduleTaskExecuter *  Scheduler::spawnExecutor(SchedulerTask * ipTask)
 {
	 ScheduleTaskExecuter * pTaskExecuter = new ScheduleTaskExecuter(this,ipTask);
	 {
		  KERMEN_WORK_SYNCRONIZED_PARAM(MUTEX_NAME_TASK_EXECUTER_OPERATIONS);
		  mpTaskExecuters.append(pTaskExecuter);
	 }	 
	 pTaskExecuter->start();
	 return pTaskExecuter;
 }

 void Scheduler::notifyExecutorCompleted(ScheduleTaskExecuter * ipTaskExecutor)
 {
	 KERMEN_WORK_SYNCRONIZED_PARAM(MUTEX_NAME_TASK_EXECUTER_OPERATIONS);
	 mpTaskExecuters.removeAll(ipTaskExecutor);	 
 }

 ScheduleLauncherThread * Scheduler::spawnLauncher(long iReferenceTimeInMillis)
 {
	 QList<TaskCollector * > nowCollectors;
	 for (int k=0;k<mpTaskCollectors.size();k++)
	 {
		 nowCollectors<<mpTaskCollectors.at(k);
	 }
	 ScheduleLauncherThread * pLaunherThread = new ScheduleLauncherThread(this,nowCollectors,iReferenceTimeInMillis);
	 {
		KERMEN_WORK_SYNCRONIZED_PARAM(MUTEX_NAME_TASK_LAUNCHER_OPERATIONS);
		mpLaunchers<<pLaunherThread;		
	 }
	 pLaunherThread->start();
	 return pLaunherThread;	 
 }
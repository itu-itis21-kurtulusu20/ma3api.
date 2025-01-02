#include "ScheduleTimerThread.h"
#include "GUIDGenerator.h"
#include "Scheduler.h"
#include <QDateTime>
#include <QDebug>
ScheduleTimerThread::ScheduleTimerThread(Scheduler * ipScheduler)
:QThread(),mpScheduler(ipScheduler),mIsInterrupted(false),mIsSleeping(false)
{
	mGUID = GUIDGenerator::generate();
}

ScheduleTimerThread::~ScheduleTimerThread(void)
{
}

void ScheduleTimerThread::_safeSleep(long millis)
{
	mIsSleeping=true;
	//sleep(millis);
	long done = 0;
	do 
	{
		long before = QDateTime::currentDateTime().toTime_t();
		sleep(millis - done);
		long after = QDateTime::currentDateTime().toTime_t();
		done += (after - before);		
	} while (done < millis);
	mIsSleeping=false;
}

void ScheduleTimerThread::run()
{	
	// What time is it?		
	if (mStartDt.isNull())
	{
		mStartDt = QDateTime::currentDateTime();
	}
	long millis = mStartDt.toTime_t();
	// Calculating next minute.
	long nextMinute = millis+60;//((millis*1000 / 60000) + 1) * 60000;
	// Work until the scheduler is started.
	for (;;) {
		// Coffee break 'till next minute comes!
		long sleepTime = (nextMinute - QDateTime::currentDateTime().toTime_t());
		if (sleepTime > 0) 
		{
			_safeSleep(sleepTime);		
		}
		// What time is it?
		millis = QDateTime::currentDateTime().toTime_t();
		// Launching the launching thread!		
		mpScheduler->spawnLauncher(millis);		
		// Calculating next minute.
		nextMinute = millis+60;//((millis*1000 / 60000) + 1) * 60000;
		if (mIsInterrupted)
		{
			break;
		}
	}
	// Discard scheduler reference.
	mpScheduler = NULL;	
}

void ScheduleTimerThread::exitThread()
{
	if (mIsSleeping)
	{
		exit();
	}
	interrupt();
	exit();
}
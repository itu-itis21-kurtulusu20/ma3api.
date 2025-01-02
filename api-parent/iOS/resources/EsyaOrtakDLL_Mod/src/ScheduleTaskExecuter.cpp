#include "ScheduleTaskExecuter.h"
#include "Scheduler.h"
#include "SchedulerTask.h"
#include "GUIDGenerator.h"

ScheduleTaskExecuter::ScheduleTaskExecuter(Scheduler * ipScheduler,SchedulerTask * ipTask)
:QThread(),mpScheduler(ipScheduler),mpTask(ipTask)
{
	mGUID = GUIDGenerator::generate();
}

ScheduleTaskExecuter::~ScheduleTaskExecuter(void)
{
}

void ScheduleTaskExecuter::run()
{
	mStartDT = QDateTime::currentDateTime();
	mpTask->execute();
};

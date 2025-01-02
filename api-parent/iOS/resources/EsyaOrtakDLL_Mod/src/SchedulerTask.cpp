#include "SchedulerTask.h"
#include "GUIDGenerator.h"

SchedulerTask::SchedulerTask(void)
:QObject()
{
	mGUID = GUIDGenerator::generate();
}

SchedulerTask::~SchedulerTask(void)
{
}

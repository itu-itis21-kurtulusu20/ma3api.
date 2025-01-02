#include "SignalSchedulerTask.h"
#include "GUIDGenerator.h"
#include <QDebug>
#include <QDateTime>

SignalSchedulerTask::SignalSchedulerTask(bool iWriteToDebug/* =false */)
:SchedulerTask(),mWriteToDebug(iWriteToDebug)
{	
}

SignalSchedulerTask::~SignalSchedulerTask(void)
{

}

void SignalSchedulerTask::execute()
{
	if (mWriteToDebug)
	{
		qDebug()<<QString("Task (%1) started at %2").arg(getGUID()).arg(QDateTime::currentDateTime().toString());
	}
	emit taskStarted();
}
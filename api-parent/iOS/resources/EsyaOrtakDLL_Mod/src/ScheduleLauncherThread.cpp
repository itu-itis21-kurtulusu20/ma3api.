#include "ScheduleLauncherThread.h"

ScheduleLauncherThread::ScheduleLauncherThread(Scheduler *ipSheduler,QList<TaskCollector*> iTaskCollectors,long iReferenceTimeInMillis)
:QThread(),mpScheduler(ipSheduler),mTaskCollectors(iTaskCollectors),mReferenceTimeInMillis(iReferenceTimeInMillis),mInterrrupted(false)
{
}

ScheduleLauncherThread::~ScheduleLauncherThread(void)
{
}

void ScheduleLauncherThread::run()
{
	for (int k=0;k<mTaskCollectors.size();k++)
	{
		TaskTable * pTaskTable = mTaskCollectors.at(k)->getTasks();
		int taskTableSize = pTaskTable->getSize();
		for (int j=0;j<taskTableSize;j++)
		{
			if (mInterrrupted)
			{
				break;
			}
			SchedulingPattern * pPattern = pTaskTable->getSchedulingPattern(j);
			if (pPattern->match(mReferenceTimeInMillis))
			{
				SchedulerTask * pTask = pTaskTable->getTask(j);
				mpScheduler->spawnExecutor(pTask);
			}
		}
		if (mInterrrupted)
		{
			break;
		}		
	}	
// 	public void run() {
// outer: for (int i = 0; i < collectors.length; i++) {
// 		TaskTable taskTable = collectors[i].getTasks();
// 		int size = taskTable.size();
// 		for (int j = 0; j < size; j++) {
// 			if (isInterrupted()) {
// 				break outer;
// 			}
// 			SchedulingPattern pattern = taskTable.getSchedulingPattern(j);
// 			if (pattern.match(scheduler.getTimeZone(),
// 				referenceTimeInMillis)) {
// 					Task task = taskTable.getTask(j);
// 					scheduler.spawnExecutor(task);
// 			}
// 		}
// 	   }
// 	   // Notifies completed.
// 	   scheduler.notifyLauncherCompleted(this);
// 	}
}

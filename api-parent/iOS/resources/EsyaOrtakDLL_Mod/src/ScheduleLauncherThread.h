#ifndef _SCHEDULER_LAUNCHER_THREAD_H_
#define _SCHEDULER_LAUNCHER_THREAD_H_

#include <QThread>
#include "Scheduler.h"
#include <QList>
#include "TaskCollector.h"

class ScheduleLauncherThread :
	public QThread
{
	bool mInterrrupted;
	QString mGUID;
	Scheduler * mpScheduler;
	QList<TaskCollector *> mTaskCollectors;
	long mReferenceTimeInMillis;
public:
	ScheduleLauncherThread(Scheduler *ipSheduler,QList<TaskCollector*> iTaskCollectors,long iReferenceTimeInMillis);
	virtual ~ScheduleLauncherThread(void);
	const QString & getGUID() {return mGUID;};
	void interrupt(){mInterrrupted=true;};
	void run();
};
#endif

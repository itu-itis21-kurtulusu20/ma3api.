#ifndef _SCHEDULER_H_
#define _SCHEDULER_H_
#include <QMutex>
#include "SchedulerTask.h"
#include <QList>
#include <QDateTime>
class MemoryTaskCollector;
class ScheduleTaskExecuter;
class SchedulingPattern;
class ScheduleLauncherThread;
class TaskCollector;
class ScheduleTimerThread;

class Q_DECL_EXPORT Scheduler
{
	QString mGUID;
	bool mStarted;
	QMutex mMutex;

	MemoryTaskCollector * mpMemoryTaskCollector;	
	QList<TaskCollector * > mpTaskCollectors;
	QList<ScheduleTaskExecuter * > mpTaskExecuters;

	QList<ScheduleLauncherThread * > mpLaunchers;

	ScheduleTimerThread * mpScheduleTimerThread;
	SchedulingPattern * mpSchedulingPattern;	


public:
	Scheduler();
	virtual ~Scheduler();
	bool getIsStarted();
	const QString getGUID();
	void startScheduling(const QDateTime & iStartDt=QDateTime());
	void stopScheduling();

	ScheduleTaskExecuter *  spawnExecutor(SchedulerTask * ipTask);
	ScheduleLauncherThread * spawnLauncher(long iReferenceTimeInMillis);

	void notifyExecutorCompleted(ScheduleTaskExecuter * ipTaskExecutor);

	QString schedule(const QString & iSchedulingPattern,SchedulerTask * ipTask);
	QString schedule(SchedulingPattern * ipSchedulingPattern,SchedulerTask * ipTask);

	SchedulingPattern * getSchedulingPattern(){return mpSchedulingPattern;};
};
#endif

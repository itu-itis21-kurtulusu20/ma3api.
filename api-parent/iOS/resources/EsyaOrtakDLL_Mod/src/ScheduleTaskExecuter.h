#ifndef _SCHEDULE_TASK_EXECUTER_H_
#define _SCHEDULE_TASK_EXECUTER_H_
#include <QThread>
#include <QDateTime>
#include <QString>
class Scheduler;
class SchedulerTask;

class ScheduleTaskExecuter:public QThread
{
	Q_OBJECT;
	Scheduler * mpScheduler;
	SchedulerTask * mpTask;
	QString mGUID;
	QDateTime mStartDT;
public:
	ScheduleTaskExecuter(Scheduler * ipScheduler,SchedulerTask * ipTask);
	virtual ~ScheduleTaskExecuter(void);
	void run();
};
#endif

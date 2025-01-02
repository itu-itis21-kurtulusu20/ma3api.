#ifndef _E_SIGNAL_SCHEDULER_TASK_H_
#define _E_SIGNAL_SCHEDULER_TASK_H_

#include <QString>
#include "SchedulerTask.h"
#include <QObject>

class SignalSchedulerTask:public SchedulerTask
{	
	Q_OBJECT;
	bool mWriteToDebug;
public:
	Q_DECL_EXPORT SignalSchedulerTask(bool iWriteToDebug=false);
	Q_DECL_EXPORT  virtual ~SignalSchedulerTask(void);	
// 	bool canBePaused(){return false;};
// 	bool canBeStopped() {return false;};		
	Q_DECL_EXPORT void execute();
Q_SIGNALS:
	void taskStarted();
};
#endif

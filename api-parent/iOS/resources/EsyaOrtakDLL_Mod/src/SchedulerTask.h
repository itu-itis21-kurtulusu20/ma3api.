#ifndef _E_SCHEDULER_TASK_H_
#define _E_SCHEDULER_TASK_H_

#include <QString>
#include <QObject>

class SchedulerTask:public QObject
{
	Q_OBJECT;
	QString mGUID;	
public:
	Q_DECL_EXPORT SchedulerTask(void);
	Q_DECL_EXPORT virtual ~SchedulerTask(void);
	Q_DECL_EXPORT const QString getGUID(){return mGUID;};
// 	bool canBePaused(){return false;};
// 	bool canBeStopped() {return false;};		
	Q_DECL_EXPORT virtual void execute()=0;	
};
#endif

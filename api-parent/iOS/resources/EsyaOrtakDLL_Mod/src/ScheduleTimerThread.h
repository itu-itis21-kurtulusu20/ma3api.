#ifndef _SCHEDULE_TIMER_THREAD_H_
#define _SCHEDULE_TIMER_THREAD_H_

#include <QTimer>

#include <QThread>
#include <QString>
#include <QDateTime>
class Scheduler;

class ScheduleTimerThread :
	public QThread
{
	Q_OBJECT;
	bool mIsInterrupted;
	bool mIsSleeping;	
	void _safeSleep(long millis);
	QDateTime mStartDt;
public:
	ScheduleTimerThread(Scheduler * ipScheduler);
	virtual ~ScheduleTimerThread(void);
	void run();
	void setStartDt(const QDateTime & iStartDt){mStartDt=iStartDt;};
	const QString getGUID(){return mGUID;};
	void interrupt(){mIsInterrupted=true;};
	bool isSleeping(){return mIsSleeping;};
	void exitThread();
private:
	QString mGUID;
	Scheduler * mpScheduler;
};
#endif

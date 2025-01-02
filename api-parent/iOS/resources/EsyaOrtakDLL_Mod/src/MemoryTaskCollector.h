#ifndef _MEMORY_TASK_COLLECTOR_H_
#define _MEMORY_TASK_COLLECTOR_H_

#include "TaskCollector.h"

#include "SchedulingPattern.h"
#include "SchedulerTask.h"

class MemoryTaskCollector :
	public TaskCollector
{
	int mSize;
	QList<SchedulingPattern * > mpPatternList;
	QList<SchedulerTask * > mpTaskList;
	QStringList mCoubleGUIDs;
public:
	MemoryTaskCollector(void);
	virtual ~MemoryTaskCollector(void);
	QString add(SchedulingPattern * ipPattern,SchedulerTask * ipTask);
	int getSize(){return mSize;};
	void update(const QString & iGUID,SchedulingPattern * ipPattern);
	void remove(const QString & iGUID);
	SchedulerTask * getTask(const QString & iGUID);
	SchedulingPattern * getSchedulingPattern(const QString & iGUID);
	TaskTable * getTasks();
};
#endif
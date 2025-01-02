#ifndef __TASK_TABLE_H__
#define __TASK_TABLE_H__

#include <QList>
#include "SchedulingPattern.h"
#include "SchedulerTask.h"

class TaskTable
{
	int mSize;
	QList<SchedulingPattern * > mpPatternList;
	QList<SchedulerTask * > mpTaskList;
public:
	TaskTable(void);
	~TaskTable(void);
	void add(SchedulingPattern * ipPattern,SchedulerTask * ipTask);	
	int getSize(){return mSize;};
	SchedulerTask * getTask(int iIndex);
	SchedulingPattern * getSchedulingPattern(int iIndex);	
	void remove(int index);
};
#endif

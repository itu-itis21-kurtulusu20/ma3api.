#include "TaskTable.h"
#include "EException.h"

TaskTable::TaskTable(void)
:mSize(0)
{
}

TaskTable::~TaskTable(void)
{
}

void TaskTable::add(SchedulingPattern * ipPattern,SchedulerTask * ipTask)
{
	mpPatternList.append(ipPattern);
	mpTaskList.append(ipTask);
	mSize++;
}

SchedulerTask * TaskTable::getTask(int iIndex)
{
	if (iIndex>mpTaskList.size()-1)
	{
		return NULL;
	}
	return mpTaskList.at(iIndex);
}

SchedulingPattern * TaskTable::getSchedulingPattern(int iIndex)
{
	if (iIndex>mpPatternList.size()-1)
	{
		return NULL;
	}
	return mpPatternList.at(iIndex);
}

void TaskTable::remove(int iIndex)
{
	if (iIndex<0 || (iIndex>mpPatternList.size()-1) ||(iIndex>mpTaskList.size()-1))
	{
		return;
	}

	mpTaskList.removeAt(iIndex);
	mpPatternList.removeAt(iIndex);
// 	SchedulerTask * pTask = mpTaskList.takeAt(iIndex);
// 	DELETE_MEMORY(pTask);
// 
// 	SchedulingPattern * pPattern = mpPatternList.takeAt(iIndex);
// 	DELETE_MEMORY(pPattern);
	mSize--;	
}

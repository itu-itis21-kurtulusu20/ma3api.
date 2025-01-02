#ifndef _TASK_COLLECTOR_H_
#define _TASK_COLLECTOR_H_

#include "TaskTable.h"

class TaskCollector
{
public:
	TaskCollector(void);
	virtual ~TaskCollector(void);
	virtual TaskTable * getTasks()=0;
};
#endif

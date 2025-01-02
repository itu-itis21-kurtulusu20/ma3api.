#ifndef _MY_SCHEDULER_TEST_TASK_H_
#define _MY_SCHEDULER_TEST_TASK_H_

#include "SchedulerTask.h"

class MyTestSchedulerTask :
	public SchedulerTask
{
public:
	MyTestSchedulerTask(void);
	virtual ~MyTestSchedulerTask(void);
	void execute();
};

#endif
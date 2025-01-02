#include "MemoryTaskCollector.h"
#include "GUIDGenerator.h"
#include "ESynchroniseManager.h"
#include "EException.h"
MemoryTaskCollector::MemoryTaskCollector(void)
{
}

MemoryTaskCollector::~MemoryTaskCollector(void)
{
	qDeleteAll(mpPatternList);
	qDeleteAll(mpTaskList);
}

QString MemoryTaskCollector::add(SchedulingPattern * ipPattern,SchedulerTask * ipTask)
{
	KERMEN_WORK_SYNCRONIZED;
	QString guid = GUIDGenerator::generate();
	mpPatternList.append(ipPattern);
	mpTaskList.append(ipTask);
	mCoubleGUIDs.append(guid);
	return guid;
}

void MemoryTaskCollector::update(const QString & iGUID,SchedulingPattern * ipPattern)
{
	KERMEN_WORK_SYNCRONIZED;
	int index = mCoubleGUIDs.indexOf(iGUID);
	if (index > -1)
	{
		mpPatternList[index]=ipPattern;
	}
}

void MemoryTaskCollector::remove(const QString & iGUID)
	{
	KERMEN_WORK_SYNCRONIZED;
	int index = mCoubleGUIDs.indexOf(iGUID);
	if (index > -1)
	{
		SchedulerTask * pTask = mpTaskList.takeAt(index);
 		DELETE_MEMORY(pTask);
 
 		SchedulingPattern * pPattern = mpPatternList.takeAt(index);
 		DELETE_MEMORY(pPattern);
		mCoubleGUIDs.removeAt(index);		
	}
}

SchedulerTask * MemoryTaskCollector::getTask(const QString & iGUID)
{	
	int index = mCoubleGUIDs.indexOf(iGUID);
	if (index > -1)
	{
		return mpTaskList.at(index);	
	}
	else
	{
		return NULL;
	}
}

SchedulingPattern * MemoryTaskCollector::getSchedulingPattern(const QString & iGUID)
{	
	int index = mCoubleGUIDs.indexOf(iGUID);
	if (index > -1)
	{
		return mpPatternList.at(index);	
	}
	else
	{
		return NULL;
	}
}

TaskTable * MemoryTaskCollector::getTasks()
{
	TaskTable * pRetTaskTable = new TaskTable();
	for (int k=0;k<mpTaskList.size();k++)
	{
		SchedulerTask * pTask = mpTaskList.at(k);
		SchedulingPattern * pPattern = mpPatternList.at(k);
		pRetTaskTable->add(pPattern,pTask);
	}
	return pRetTaskTable;
}
#include "EMemoryLeakDetector.h"
#include "EProcessMemUsage.h"
#include "EProcessInfo.h"
#include <QDebug>
#include <QEventLoop>
#include <QTimer>


#define MEM_LEAK_DETECTOR_LOOP_COUNT 100
#define MEM_LEAK_DETECTOR_MIN_NO_LEAK_COUNT 20
#define MEM_LEAK_DETECTOR_MIN_REPEAT_COUNT 1
#define MEM_LEAK_DETECTOR_SUB_FUNC_CALL_COUNT 100

EMemoryLeakDetector::EMemoryLeakDetector(void)
{
}

EMemoryLeakDetector::~EMemoryLeakDetector(void)
{
}

bool EMemoryLeakDetector::hasMemoryLeak(FUNC  iFuncPtr)
{
	int before =0 ;
	int after=0;
	int diff=0;
	bool hasLeak=true;
	int noLeakCount = 0 ;	
	QMap<int,int> afterMap;
	for (int k=0;k<MEM_LEAK_DETECTOR_LOOP_COUNT/MEM_LEAK_DETECTOR_SUB_FUNC_CALL_COUNT;k++)
	{
		before=EProcessInfo::getCurrentProcessMemoryUsage();		
		{
			for (int m=0;m<MEM_LEAK_DETECTOR_SUB_FUNC_CALL_COUNT;m++)
			{
				(*iFuncPtr)();
			}			
		}
		{
			QEventLoop loop;
			QTimer::singleShot (1000,&loop,SLOT(quit()));
			loop.exec();
		}
		after=EProcessInfo::getCurrentProcessMemoryUsage();
		diff = after - before;		
		qDebug()<<QString("Diff = %1").arg(diff);
		/*
		int afterCount = afterMap[after]+1;
		afterMap[after]=afterCount;
		if (afterCount>=MEM_LEAK_DETECTOR_MIN_REPEAT_COUNT)
		{
			hasLeak=false;
			break;;
		}*/	
	}		
	return hasLeak;
}

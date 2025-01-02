#include "EProcessMemUsage.h"

#include "EProcessInfo.h"

EProcessMemUsage::EProcessMemUsage(void)
{
	reset();
}

EProcessMemUsage::~EProcessMemUsage(void)
{
}

void EProcessMemUsage::reset()
{
	mMemoryUsageK = getCurrentMemoryUsage();	
}

int EProcessMemUsage::getCurrentMemoryUsage()
{	
	int memUsageByte = EProcessInfo::getCurrentProcessMemoryUsage();
	int retMemUsageK=0;
	if (memUsageByte)
	{
		retMemUsageK = memUsageByte/SIZE_OF_K;
	}
	return retMemUsageK;
}

int EProcessMemUsage::getMemoryUsageDiff()
{
	return getCurrentMemoryUsage()-mMemoryUsageK;
}

QString EProcessMemUsage::getMemUsageDiffStr()
{
	QString retStr = "Diff Memory Usage= %1K ,Start Memory Usage= %2K, Current Memory Usage = %3K";
	int current = getCurrentMemoryUsage();
	return retStr.arg(current-mMemoryUsageK).arg(mMemoryUsageK).arg(current);
}

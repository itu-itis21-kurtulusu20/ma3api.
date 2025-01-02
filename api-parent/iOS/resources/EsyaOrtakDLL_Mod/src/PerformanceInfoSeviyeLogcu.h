#ifndef _PERFORMANCE_INFO_SEVIYE_LOGCU_H_
#define _PERFORMANCE_INFO_SEVIYE_LOGCU_H_
#include "AbstractSeviyeLogcu.h"

class PerformanceInfoSeviyeLogcu :
	public AbstractSeviyeLogcu
{
public:
	PerformanceInfoSeviyeLogcu(void);
	~PerformanceInfoSeviyeLogcu(void);
	void writeToLog(const ELogEntry & iLogEntry);
};
#endif
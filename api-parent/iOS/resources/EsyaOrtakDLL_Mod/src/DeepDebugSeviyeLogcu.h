#ifndef  __DEEP_DEBUG__SEVIYE_LOGCU_H__
#define  __DEEP_DEBUG__SEVIYE_LOGCU_H__
#include "AbstractSeviyeLogcu.h"


class DeepDebugSeviyeLogcu :
	public AbstractSeviyeLogcu
{	
public:
	DeepDebugSeviyeLogcu(void);
	~DeepDebugSeviyeLogcu(void);
	void writeToLog(const ELogEntry & iLogEntry);	
};
#endif
#ifndef  __DEBUG__SEVIYE_LOGCU_H__
#define  __DEBUG__SEVIYE_LOGCU_H__
#include "AbstractSeviyeLogcu.h"


class DebugSeviyeLogcu :
	public AbstractSeviyeLogcu
{	
public:
	DebugSeviyeLogcu(void);
	~DebugSeviyeLogcu(void);
	void writeToLog(const ELogEntry & iLogEntry);	
};
#endif
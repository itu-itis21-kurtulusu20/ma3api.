#ifndef __BOS_SEVIYE_LOGCU_H__
#define __BOS_SEVIYE_LOGCU_H__
#include "AbstractSeviyeLogcu.h"

class BosSeviyeLogcu :
	public AbstractSeviyeLogcu
{
public:
	BosSeviyeLogcu(void);
	~BosSeviyeLogcu(void);
	void writeToLog(const ELogEntry & iLogEntry){};	
};
#endif
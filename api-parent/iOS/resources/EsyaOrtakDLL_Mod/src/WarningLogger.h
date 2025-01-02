#ifndef  __WARNING_LOGGER_H_
#define  __WARNING_LOGGER_H_
#include "AbstractSeviyeLogcu.h"


class WarningLogger :
	public AbstractSeviyeLogcu
{	
public:
	WarningLogger(void);
	~WarningLogger(void);
	void writeToLog(const ELogEntry & iLogEntry);	
};
#endif
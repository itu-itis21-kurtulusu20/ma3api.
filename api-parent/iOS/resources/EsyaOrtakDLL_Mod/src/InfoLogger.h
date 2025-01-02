#ifndef  __INFO_LOGGER_H_
#define  __INFO_LOGGER_H_
#include "AbstractSeviyeLogcu.h"


class InfoLogger :
	public AbstractSeviyeLogcu
{	
public:
	InfoLogger(void);
	~InfoLogger(void);
	void writeToLog(const ELogEntry & iLogEntry);	
};
#endif
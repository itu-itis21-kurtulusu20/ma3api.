
#ifndef _ERROR_SEVIYE_LOGCU_H_
#define _ERROR_SEVIYE_LOGCU_H_
#include "AbstractSeviyeLogcu.h"

class ErrorSeviyeLogcu :
	public AbstractSeviyeLogcu
{
public:
	ErrorSeviyeLogcu(void);
	~ErrorSeviyeLogcu(void);
	void writeToLog(const ELogEntry & iLogEntry);
};
#endif
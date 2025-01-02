#ifndef _ABSTRACT_SEVIYE_LOGCU_H_
#define _ABSTRACT_SEVIYE_LOGCU_H_

#include <QMutex>
#include "ILogger.h"
class AbstractSeviyeLogcu
{
protected:
	static QMutex mMutex;
public:
	AbstractSeviyeLogcu(void);
	~AbstractSeviyeLogcu(void);
	virtual void writeToLog(const ELogEntry & iLogEntry)=0;	
};
#endif
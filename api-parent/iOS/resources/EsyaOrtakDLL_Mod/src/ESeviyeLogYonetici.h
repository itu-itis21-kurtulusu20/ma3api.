#ifndef _E_SEVIYE_LOG_YONETICI_H_
#define _E_SEVIYE_LOG_YONETICI_H_

#include "AbstractSeviyeLogcu.h"
#include <QMutex>

class Q_DECL_EXPORT ESeviyeLogYonetici
{
	static QMutex mpMutex;
	AbstractSeviyeLogcu * mpAyrintiliDebugSeviyeLogcu;
	AbstractSeviyeLogcu * mpDebugSeviyeLogcu;
	AbstractSeviyeLogcu * mpErrorSeviyeLogcu;
	AbstractSeviyeLogcu * mpPermonceInfoSeviyeLogcu;
	AbstractSeviyeLogcu * mpInfoLogger;
	AbstractSeviyeLogcu * mpWarningLogger;
	ESeviyeLogYonetici(void);
	static ESeviyeLogYonetici * mInstance;
public:
	static ESeviyeLogYonetici * getInstance();
	~ESeviyeLogYonetici(void);	
	AbstractSeviyeLogcu * getAyrintiliDebugSeviyeLogcu();
	AbstractSeviyeLogcu * getDebugSeviyeLogcu();
	AbstractSeviyeLogcu * getErrorSeviyeLogcu();
	AbstractSeviyeLogcu * getPermonceInfoSeviyeLogcu();
	AbstractSeviyeLogcu * getInfoLogger();
	AbstractSeviyeLogcu * getWarningLogger();
};

#endif
#ifndef ESYNCHRONISEMANAGER_H
#define ESYNCHRONISEMANAGER_H

#include <QMutex>
#include <QMap>
#include <QString>
#include <QMutexLocker>
#include "esyaOrtak.h"
#include <QFileInfo>

#define KERMEN_WORK_SYNCRONIZED QMutexLocker locker(ESynchroniseManager::getInstance()->getFunctionMutex(QString(__E_FILE__)+"_"+QString(__FUNCTION__)));
#define KERMEN_WORK_SYNCRONIZED_PARAM(x) QMutexLocker locker(ESynchroniseManager::getInstance()->getFunctionMutex(x));

class Q_DECL_EXPORT ESynchroniseManager
{
    static QMutex s_InstanceMutex;
    static QMutex s_GetFunctionMutexMutex;
    static ESynchroniseManager * mpInstance;
    QMap<QString,QMutex*> mpFunctionMutexMap;
      ESynchroniseManager();
  public:
    static ESynchroniseManager * getInstance();
    QMutex * getFunctionMutex(const QString & iFunctionNameKey);
    virtual ~ESynchroniseManager();
};

#endif // ESYNCHRONISEMANAGER_H


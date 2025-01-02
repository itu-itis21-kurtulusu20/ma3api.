#include "ESynchroniseManager.h"

QMutex ESynchroniseManager::s_InstanceMutex;
QMutex ESynchroniseManager::s_GetFunctionMutexMutex;

ESynchroniseManager * ESynchroniseManager::mpInstance=NULL;
ESynchroniseManager::ESynchroniseManager()
{
}

ESynchroniseManager * ESynchroniseManager::getInstance()
{
    QMutexLocker locker(&ESynchroniseManager::s_InstanceMutex);
    if( ESynchroniseManager::mpInstance == NULL)
    {
         ESynchroniseManager::mpInstance = new  ESynchroniseManager();
    }
    return  ESynchroniseManager::mpInstance;
}

QMutex *  ESynchroniseManager::getFunctionMutex(const QString & iFunctionNameKey)
{
    QMutexLocker locker(&ESynchroniseManager::s_GetFunctionMutexMutex);
    if(!mpFunctionMutexMap.contains(iFunctionNameKey))
    {
        mpFunctionMutexMap.insert(iFunctionNameKey,new QMutex());
    }
    return mpFunctionMutexMap.value(iFunctionNameKey);
}

ESynchroniseManager::~ESynchroniseManager()
{

}

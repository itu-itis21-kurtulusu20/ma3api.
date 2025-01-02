#include "ESingFilterManager.h"
#include "FileUtil.h"
#include "ESynchroniseManager.h"

ESingFilterManager * ESingFilterManager::mpInstance=NULL;

ESingFilterManager::ESingFilterManager(void)
{	
	mKermenUzantiList<<FILE_EXT_SIGNED<<FILE_EXT_ENCRYPTED<<FILE_EXT_SIGN_ENC<<FILE_EXT_PROTECTED<<FILE_EXT_KERMEN_PROTECTED;
	Q_FOREACH(QString eklenti,mKermenUzantiList)		
	{
		mDirSearcKermenFileFilterList<<QString("*.%1.*").arg(eklenti)<<QString("*.%1").arg(eklenti);
	}
}

ESingFilterManager::~ESingFilterManager(void)
{
}


ESingFilterManager * ESingFilterManager::getInstance()
{
	KERMEN_WORK_SYNCRONIZED;
	if (mpInstance==NULL)
	{
		mpInstance = new ESingFilterManager();
	}
	return mpInstance;
}
const QStringList & ESingFilterManager::getKermenUzantiList() const
{
	return mKermenUzantiList;
}

const QStringList & ESingFilterManager::getDirSearcKermenFileFilter() const
{
	return mDirSearcKermenFileFilterList;
}

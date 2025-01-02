#include "ELangManager.h"
#include "ESynchroniseManager.h"
#include "EsyaOrtakDLL_DIL.h"

#define KERMEN_DIL_SHORT_NAME_TURKCE "tr_TR"
#define KERMEN_DIL_SHORT_NAME_INGILIZCE "en_EN"
#define KERMEN_DIL_SHORT_NAME_AZERICE "az_AZ"
#define KERMEN_DIL_SHORT_NAME_TURKMENCE "tk_TK"
#define KERMEN_DIL_SHORT_NAME_RUSCA "ru_RU"

ELangManager * ELangManager::mpInstance = NULL;
ELangManager::ELangManager(void)
{
	_initLangMap();
}

ELangManager::~ELangManager(void)
{	
}

QMap<QString,QString> ELangManager::getLangMap()
{
	return mLangMap;
}


ELangManager * ELangManager::getInstance()
{
	KERMEN_WORK_SYNCRONIZED;
	if (ELangManager::mpInstance == NULL)
	{
		ELangManager::mpInstance = new ELangManager();
	}
	return ELangManager::mpInstance;
}

void ELangManager::_initLangMap()
{
	mLangMap.clear();
	mLangMap.insert(KERMEN_DIL_SHORT_NAME_TURKCE,DIL_DIL_ADI_TURKCE);
	mLangMap.insert(KERMEN_DIL_SHORT_NAME_INGILIZCE,DIL_DIL_ADI_INGILIZCE);
	mLangMap.insert(KERMEN_DIL_SHORT_NAME_AZERICE,DIL_DIL_ADI_AZERICE);
	mLangMap.insert(KERMEN_DIL_SHORT_NAME_TURKMENCE,DIL_DIL_ADI_TURKMENCE);
	mLangMap.insert(KERMEN_DIL_SHORT_NAME_RUSCA,DIL_DIL_ADI_RUSCA);	
}

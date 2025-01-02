#include "EAyarManagerFactory.h"
#include "EGenelAyarManager.h"
#include "EYerelAyarManager.h"
#include "ESynchroniseManager.h"
NAMESPACE_BEGIN(esya)
EAyarManagerFactory::EAyarManagerFactory(void)
{

}

EAyarManagerFactory::~EAyarManagerFactory(void)
{

}

EAyarManager * EAyarManagerFactory::getAyarManager(EAyarManagerFactory::AYAR_TIPI iAyarTipi)
{
	KERMEN_WORK_SYNCRONIZED;
	EAyarManager * retManager = NULL ;
	if (iAyarTipi == EAyarManagerFactory::Ayar_Tipi_Genel)
	{
		retManager = EGenelAyarManager::getInstance();
	}
	else if (iAyarTipi == EAyarManagerFactory::Ayar_Tipi_Yerel)
	{
		retManager = EYerelAyarManager::getInstance();
	}
	return retManager;
}
NAMESPACE_END

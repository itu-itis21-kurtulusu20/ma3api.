#ifndef __E_AYAR_MANAGER_FACTORY_H_
#define __E_AYAR_MANAGER_FACTORY_H_
#include "esyaOrtak.h"
#include "EAyarManager.h"
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EAyarManagerFactory
{	
public:
	enum AYAR_TIPI {AyarTipi_Hicbiri,Ayar_Tipi_Yerel,Ayar_Tipi_Genel};
	EAyarManagerFactory();
	~EAyarManagerFactory(void);
	static EAyarManager * getAyarManager(EAyarManagerFactory::AYAR_TIPI iAyarTipi);
};
NAMESPACE_END
#endif

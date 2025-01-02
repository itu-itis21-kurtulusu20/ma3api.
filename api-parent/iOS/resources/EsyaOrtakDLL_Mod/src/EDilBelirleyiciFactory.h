#ifndef __E_DIL_BELIRLEYICI_FACTORY_H_
#define __E_DIL_BELIRLEYICI_FACTORY_H_
#include "EDilBelirleyici.h"
NAMESPACE_BEGIN(esya)
#define KERMEN_CALISMA_DILI_BELIRLE(x)\
	EDilBelirleyici * pDilBelirleyici = EDilBelirleyiciFactory::dilBelirleyiciGetir(&x);\
	pDilBelirleyici->calismaDiliUygula();\
	delete pDilBelirleyici;
class  Q_DECL_EXPORT EDilBelirleyiciFactory
{
public:
	EDilBelirleyiciFactory(void);
	~EDilBelirleyiciFactory(void);
	static EDilBelirleyici * dilBelirleyiciGetir(QApplication * ipApp);
};

NAMESPACE_END
#endif
#include "EDilBelirleyiciFactory.h"
#include "EAyarlardanDilBelirleyici.h"
#include "ELokaldenDilBelirleyici.h"
NAMESPACE_BEGIN(esya)

EDilBelirleyiciFactory::EDilBelirleyiciFactory(void)
{
}

EDilBelirleyiciFactory::~EDilBelirleyiciFactory(void)
{
}

EDilBelirleyici * EDilBelirleyiciFactory::dilBelirleyiciGetir(QApplication * ipApp)
{
	//<TODO> Burda neye göre localden yada ayarlardan dilin belirlenebileceðini seçebiliriz
	return new EAyarlardanDilBelirleyici(ipApp);
}
NAMESPACE_END
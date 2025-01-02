#ifndef __E_LOKALDEN_DIL_BELIRLEYICI_H_
#define __E_LOKALDEN_DIL_BELIRLEYICI_H_
#include "EDilBelirleyici.h"
NAMESPACE_BEGIN(esya)
/**
 * \ingroup EsyaOrtak
 *
 * Yerel iþletim sistemi ayarlarýndan programýn çalýþma dilini belirlemek için kullanýlan sýnýf
 * 
 * \date 03-17-2009
 *
 * \author ramazang
 * 
 *
 */
class ELokaldenDilBelirleyici :
	public EDilBelirleyici
{
	void _lokaldenDilBelirle();
public:
	ELokaldenDilBelirleyici(QApplication * ipApp);
	~ELokaldenDilBelirleyici(void);
	void calismaDiliBelirle();
};
NAMESPACE_END
#endif
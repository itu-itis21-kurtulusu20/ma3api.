#ifndef __EKLENTIFABRIKASI__
#define __EKLENTIFABRIKASI__


#include "AY_Eklenti.h"
#include "Extension.h"

using namespace esya;

/**
* \ingroup EsyaASN
* 
* Extension sýnýfýndan AY_Eklenti arayüzünü destekleyen uygun ASN1 wrapper sýnýfýný türeten fabrika sýnýfý
*
*
* \author dindaro
*
*/
class Q_DECL_EXPORT EklentiFabrikasi
{
public:

	static AY_Eklenti * eklentiUret(const Extension &);

};

#endif


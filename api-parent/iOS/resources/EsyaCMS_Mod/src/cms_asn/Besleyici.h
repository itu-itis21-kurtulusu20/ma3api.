#ifndef __BESLEYICI__
#define __BESLEYICI__

#include "BlokIsleyici.h"
#include "TAlg_Paralel_Adaptor.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Kripto iþleme nesnelerini birbirine baðlayabileceðimiz ve verileri blok blok paralel alarak 
	* besleyebileceðimiz bir baðlantý sýnýfý. Blok iþleyici  arayüzünü destekler.
	*
	* \author dindaro
	*
	*/
	class Besleyici : public BlokIsleyici
	{
		
		TAlg_Paralel_Adaptor * pBeslenecekler;
		
	public:
		Besleyici(TAlg_Paralel_Adaptor * ipBeslenecekler);
		
		virtual void blokIsle(const QByteArray& iBlok);

	public:
		~Besleyici(void);
	};
}

#endif


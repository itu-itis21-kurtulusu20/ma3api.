#ifndef __OZETKONTROLCU__
#define __OZETKONTROLCU__

#include "ImzaKontrolcu.h"


namespace esya
{

	class ImzaDogrulamaAlgoritmasi;

	/**
	* \ingroup EsyaCMS
	* 
	* Imzalý yapý üzerindeki özet deðerini kriptografik olarak doðrular.
	* 
	*
	* \author dindaro
	*
	*/
	class OzetKontrolcu : public ImzaKontrolcu
	{
	public:
		static const QString ST_KONTROLADI;

		OzetKontrolcu( ImzaDogrulamaAlgoritmasi * ipDA = NULL);
		OzetKontrolcu( ImzaDogrulamaAlgoritmasi * ipDA ,const ParametreListesi &);

		virtual bool kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS) ;

	public:
		~OzetKontrolcu(void);
	};

}

#endif


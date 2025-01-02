#ifndef __IMZAKRIPTOKONTROLCU__
#define __IMZAKRIPTOKONTROLCU__

#include "ImzaKontrolcu.h"


namespace esya
{
	class ImzaDogrulamaAlgoritmasi;


	/**
	* \ingroup EsyaCMS
	* 
	* Imzalý yapý üzerindeki Signature deðerini kriptografik olarak doðrular.
	* 
	*
	* \author dindaro
	*
	*/
	class ImzaKriptoKontrolcu : public ImzaKontrolcu
	{
	public:
		static const QString ST_KONTROLADI;

		ImzaKriptoKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA = NULL);
		ImzaKriptoKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA , const ParametreListesi &);

		virtual bool kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS) ;

	public:
		~ImzaKriptoKontrolcu(void);
	};

}

#endif


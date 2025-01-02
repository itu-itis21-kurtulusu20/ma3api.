#include "ImzaKontrolcu.h"

using namespace esya;

/**
* \brief
* ImzaKontrolcu constructoru
*/
ImzaKontrolcu::ImzaKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA)
: pDogrulamaAlgoritmasi(ipDA)
{
}


/**
* \brief
* ImzaKontrolcu constructoru
*
* \param 		ImzaDogrulamaAlgoritmasi * ipDA
* Ýmza Doðrulama Algoritmasý
*
* \param 		const ParametreListesi & iKP
* Kontrol Parametreleri
*/
ImzaKontrolcu::ImzaKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA , const ParametreListesi &iKP)
: pDogrulamaAlgoritmasi(ipDA), mKontrolParams(iKP)
{
}

/**
* \brief
* Kontrol parametrelerini döner
*
* \return  const ParametreListesi & 
* Kontrol Parametreleri
*/
const ParametreListesi &	ImzaKontrolcu::getKontrolParams()const 
{
	return mKontrolParams;
}

/**
* \brief
* Kontrol parametrelerini döner
*
* \return  ParametreListesi & 
* Kontrol Parametreleri
*/
ParametreListesi &	ImzaKontrolcu::getKontrolParams()
{
	return mKontrolParams;;
}

/**
* \brief
* Kontrol parametrelerini belirler
*
* \param const ParametreListesi & iKP
* Kontrol Parametreleri
*
*/
void ImzaKontrolcu::setKontrolParams(const ParametreListesi & iKP ) 
{
	mKontrolParams = iKP;
}

/**
* \brief
* Kontrol parametresi ekler
*
* \param 		const QString & iParamName
* Parametre Adý
*
* \param 		const QVariant & iParamValue
* Parametre Deðeri
*
*/
void ImzaKontrolcu::setKontrolParam(const QString & iParamName  , const QVariant & iParamValue) 
{
	mKontrolParams.parametreEkle(iParamName,iParamValue);
}


/**
* \brief
* ImzaKontrolcu destructoru
*/
ImzaKontrolcu::~ImzaKontrolcu(void)
{
}

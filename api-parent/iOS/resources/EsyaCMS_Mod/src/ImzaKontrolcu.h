#ifndef __IMZAKONTROLCU__
#define __IMZAKONTROLCU__

#include "ImzaDogrulamaSonucu.h"
#include "SignedData.h"//#include "StreamedSignedData.h"
#include "ParametreListesi.h"
//#include "aciklamalar.h"
#include "MyMetaTypes.h"


#define PN_CERTIFICATE				"CERTIFICATE"
#define PN_CERTLIST					"CERTLIST"
#define PN_PLAINDATA				"PLAINDATA"
#define PN_PLAINDATAFILENAME		"PLAINDATAFILENAME"
#define PN_SIGNED_ATTR_INC_SET		"SIGNED_ATTR_INC_SET"
#define PN_SIGNED_ATTR_EXC_SET		"SIGNED_ATTR_EXC_SET"
#define PN_SIGNED_ATTR_VALID_SET	"SIGNED_ATTR_VALID_SET"

#define CN_IMZAKONTROLCU "IMZAKONTROLCU"

#define IK_LOGGER LOGGER(CN_IMZAKONTROLCU)

namespace esya
{
	class ImzaDogrulamaAlgoritmasi;

	/**
	* \ingroup EsyaCMS
	* 
	* Ýmza doðrulama iþleminde atomik kontrol iþlemlerini yapan kontrolcu sýnýflarýn ata sýnýfý
	* 
	*
	* \author dindaro
	*
	*/
	class ImzaKontrolcu
	{
	protected:
		ParametreListesi mKontrolParams;

		ImzaDogrulamaAlgoritmasi * pDogrulamaAlgoritmasi;

	public:

		virtual bool kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS) = 0;

		const	ParametreListesi &	getKontrolParams()const ;
				ParametreListesi &	getKontrolParams();
				void				setKontrolParams(const ParametreListesi & ) ;
				void				setKontrolParam(const QString & iParamName,const QVariant &iParamValue ) ;


		ImzaKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA = NULL);
		ImzaKontrolcu(ImzaDogrulamaAlgoritmasi * ipDA , const ParametreListesi &);

	public:
		~ImzaKontrolcu(void);
	};
}

#endif


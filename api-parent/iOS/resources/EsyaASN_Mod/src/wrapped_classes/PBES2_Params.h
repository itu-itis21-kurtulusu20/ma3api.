#ifndef __PBES2_PARAMS__
#define __PBES2_PARAMS__


#include "pkcs5v2.h"
#include "AlgorithmIdentifier.h"


namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT PBES2_Params  : public EASNWrapperTemplate<ASN1T_PKCS5_PBES2_params,ASN1C_PKCS5_PBES2_params>
	{
		AlgorithmIdentifier	mKeyDerivationFunc;
		AlgorithmIdentifier	mEncryptionScheme;

	public:
		PBES2_Params();
		PBES2_Params(const ASN1T_PKCS5_PBES2_params & );
		PBES2_Params(const QByteArray &);
		PBES2_Params(const AlgorithmIdentifier & iKDF, const AlgorithmIdentifier iES);
		PBES2_Params(const PBES2_Params & );
		

		PBES2_Params& operator=(const PBES2_Params& );
		friend bool operator==(const PBES2_Params & ,const PBES2_Params & );
		friend bool operator!=(const PBES2_Params & iRHS, const PBES2_Params & iLHS);


		int copyFromASNObject(const ASN1T_PKCS5_PBES2_params & iPP);
		int copyToASNObject(ASN1T_PKCS5_PBES2_params & ) const;	
		void freeASNObject(ASN1T_PKCS5_PBES2_params & )const;

		virtual ~PBES2_Params(void);

		// GETTERS AND SETTERS

		const AlgorithmIdentifier&	getKeyDerivationFunc()	const;
		const AlgorithmIdentifier&	getEncryptionScheme()	const;
		
		void setKeyDerivationFunc(const AlgorithmIdentifier& );
		void setEncryptionScheme(const AlgorithmIdentifier&	);

	};

}

#endif 


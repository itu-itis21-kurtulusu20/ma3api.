#ifndef __PBKDF2_PARAMS__
#define __PBKDF2_PARAMS__


#include "PBKDF2_Salt.h"


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
	class Q_DECL_EXPORT PBKDF2_Params  : public EASNWrapperTemplate<ASN1T_PKCS5_PBKDF2_params,ASN1C_PKCS5_PBKDF2_params>
	{
		bool				mKeyLengthPresent ;

		PBKDF2_Salt			mSalt;
		int					mIterationCount;
		int					mKeyLength;
		AlgorithmIdentifier mPRF;

	public:
	
		static const AlgorithmIdentifier DEFAULT_PRF;
		
		PBKDF2_Params();
		PBKDF2_Params(const ASN1T_PKCS5_PBKDF2_params & );
		PBKDF2_Params(const PBKDF2_Params & );
		PBKDF2_Params(const  QByteArray &);

		PBKDF2_Params& operator=(const PBKDF2_Params& );
		friend bool operator==(const PBKDF2_Params & ,const PBKDF2_Params & );
		friend bool operator!=(const PBKDF2_Params & iRHS, const PBKDF2_Params & iLHS);


		int copyFromASNObject(const ASN1T_PKCS5_PBKDF2_params & iPP);
		int copyToASNObject(ASN1T_PKCS5_PBKDF2_params & ) const;	
		void freeASNObject(ASN1T_PKCS5_PBKDF2_params & )const;

		virtual ~PBKDF2_Params(void);

		// GETTERS AND SETTERS

		const bool isKeyLengthPresent()	const;

		const PBKDF2_Salt &			getSalt()			const;
		const int					getIterationCount()	const;
		const int					getKeyLength()		const;
		const AlgorithmIdentifier&	getPRF()			const;

		void setKeyLengthPresent(const bool );

		void setSalt(const PBKDF2_Salt	&);
		void setIterationCount(const int &);
		void setKeyLength(const int &);
		void setPRF(const AlgorithmIdentifier&);
	};

}

#endif 


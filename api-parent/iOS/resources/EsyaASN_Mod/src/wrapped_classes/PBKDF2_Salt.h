#ifndef __PBKDF2_SALT__
#define __PBKDF2_SALT__


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
	class Q_DECL_EXPORT PBKDF2_Salt  : public EASNWrapperTemplate<ASN1T_PKCS5_PBKDF2_params_salt,ASN1C_PKCS5_PBKDF2_params_salt>
	{
		int					mType;
		QByteArray			mSpecified;
		AlgorithmIdentifier	mOtherSource;

	public:
		PBKDF2_Salt();
		PBKDF2_Salt(const ASN1T_PKCS5_PBKDF2_params_salt & );
		PBKDF2_Salt(const QByteArray &);
		PBKDF2_Salt(const PBKDF2_Salt & );

		PBKDF2_Salt& operator=(const PBKDF2_Salt& );
		friend bool operator==(const PBKDF2_Salt & ,const PBKDF2_Salt & );
		friend bool operator!=(const PBKDF2_Salt & iRHS, const PBKDF2_Salt & iLHS);

		int copyFromASNObject(const ASN1T_PKCS5_PBKDF2_params_salt & iAN);
		int copyToASNObject(ASN1T_PKCS5_PBKDF2_params_salt & ) const;	
		void freeASNObject(ASN1T_PKCS5_PBKDF2_params_salt & )const;

		virtual ~PBKDF2_Salt(void);

		// GETTERS AND SETTERS

		const int					getType()const;
		const QByteArray&			getSpecified()const;
		const AlgorithmIdentifier&	getOtherSource()const;

		void setType(const int	);
		void setSpecified(const QByteArray& );
		void setOtherSource(const AlgorithmIdentifier&	);

	};

}

#endif 


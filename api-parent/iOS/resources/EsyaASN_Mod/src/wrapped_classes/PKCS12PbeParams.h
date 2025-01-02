#ifndef __PKCS12PBEPARAMS__
#define __PKCS12PBEPARAMS__

#include "pkcs12.h"
#include "ortak.h"

namespace esya
{

	class Q_DECL_EXPORT PKCS12PbeParams  : public EASNWrapperTemplate<ASN1T_PKCS12_PKCS12PbeParams,ASN1C_PKCS12_PKCS12PbeParams>
	{
		QByteArray		mSalt;
		OSUINT32		mIterations;

	public:

		PKCS12PbeParams(void);
		PKCS12PbeParams(const QByteArray& iParams);
		PKCS12PbeParams(const ASN1T_PKCS12_PKCS12PbeParams & iParams);
		PKCS12PbeParams(const PKCS12PbeParams& iParams);
		PKCS12PbeParams(const QByteArray& iParams , const OSUINT32 & );

		PKCS12PbeParams& operator=(const PKCS12PbeParams& iParams);
		friend bool operator==( const PKCS12PbeParams& iRHS, const PKCS12PbeParams& iLHS);
		friend bool operator!=( const PKCS12PbeParams& iRHS, const PKCS12PbeParams& iLHS);

		int copyFromASNObject(const ASN1T_PKCS12_PKCS12PbeParams & iParams);
		int copyToASNObject(ASN1T_PKCS12_PKCS12PbeParams &oParams)const;
		void freeASNObject(ASN1T_PKCS12_PKCS12PbeParams& )const;

		virtual ~PKCS12PbeParams(void);

		// GETTERS AND SETTERS

		const QByteArray& getSalt() const;
		const OSUINT32 getIterations()const ;

		void setSalt(const QByteArray & );
		void setIteration(const OSUINT32 & );
		
	};
}

#endif 


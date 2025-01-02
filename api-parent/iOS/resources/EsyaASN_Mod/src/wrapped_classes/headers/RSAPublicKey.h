#ifndef __RSAPUBLICKEY__
#define __RSAPUBLICKEY__

#include "pkcs1pkcs8.h"
#include "ortak.h"
#include "AlgorithmIdentifier.h"
#include "EException.h"
#include "Attribute.h"

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
	class Q_DECL_EXPORT RSAPublicKey  : public EASNWrapperTemplate<ASN1T_PKCS18_RSAPublicKey,ASN1C_PKCS18_RSAPublicKey>
	{

	protected:
	   QString	mModulus;
	   QString	mPublicExponent;
		
	public:

		RSAPublicKey(void);
		RSAPublicKey( const ASN1T_PKCS18_RSAPublicKey &iRPK);
		RSAPublicKey( const RSAPublicKey &iRPK);
		RSAPublicKey( const QByteArray &iRPK);

		RSAPublicKey & operator=(const RSAPublicKey& iRPK);
		friend bool operator==(const RSAPublicKey& iRHS, const RSAPublicKey& iLHS);
		friend bool operator!=(const RSAPublicKey& iRHS, const RSAPublicKey& iLHS);

		int copyFromASNObject(const ASN1T_PKCS18_RSAPublicKey & iRPK);
		int copyToASNObject(ASN1T_PKCS18_RSAPublicKey & oRPK) const;
		void freeASNObject(ASN1T_PKCS18_RSAPublicKey& oRPK)const;

		virtual ~RSAPublicKey(void);

		// GETTERS AND SETTERS

		const QString &	getModulus() const ;
		const QString &	getPublicExponent() const;

		void setModulus(const QString &	) ;
		void setPublicExponent(const QString &	) ;

	public:
		
	};

}

#endif


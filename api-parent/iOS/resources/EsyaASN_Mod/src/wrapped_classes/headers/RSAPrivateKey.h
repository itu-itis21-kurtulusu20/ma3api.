#ifndef __RSAPRIVATEKEY__
#define __RSAPRIVATEKEY__

#include "pkcs1pkcs8.h"
#include "ortak.h"
#include "AlgorithmIdentifier.h"
#include "EException.h"
#include "Attribute.h"
#include "RSAPublicKey.h"

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
	class Q_DECL_EXPORT RSAPrivateKey  : public EASNWrapperTemplate<ASN1T_PKCS18_RSAPrivateKey,ASN1C_PKCS18_RSAPrivateKey>
	{

	protected:

	   OSUINT32	mVersion;
	   QString	mModulus;
	   QString	mPublicExponent;
	   QString  mPrivateExponent;
	   QString  mPrime1;
	   QString  mPrime2;
	   QString  mExponent1;
	   QString  mExponent2;
	   QString  mCoefficient;
		

	public:

		RSAPrivateKey(void);
		RSAPrivateKey( const ASN1T_PKCS18_RSAPrivateKey &iRPK);
		RSAPrivateKey( const RSAPrivateKey &iRPK);
		RSAPrivateKey( const QByteArray &iRPK);

		RSAPrivateKey & operator=(const RSAPrivateKey& iRPK);
		friend bool operator==(const RSAPrivateKey& iRHS, const RSAPrivateKey& iLHS);
		friend bool operator!=(const RSAPrivateKey& iRHS, const RSAPrivateKey& iLHS);

		int copyFromASNObject(const ASN1T_PKCS18_RSAPrivateKey & iRPK);
		int copyToASNObject(ASN1T_PKCS18_RSAPrivateKey & oRPK) const;
		void freeASNObject(ASN1T_PKCS18_RSAPrivateKey& oRPK)const;

		virtual ~RSAPrivateKey(void);

		// GETTERS AND SETTERS

		const OSUINT32&	getVersion() const ;
		const QString &	getModulus() const ;
		const QString &	getPublicExponent() const;
		const QString & getPrivateExponent() const;
		const QString & getPrime1() const;
		const QString & getPrime2() const;
		const QString & getExponent1() const;
		const QString & getExponent2() const;
		const QString & getCoefficient() const;

		bool matchKey(const RSAPublicKey & rPK);

		static byte toByte(const QString& iHexValue);
		static QByteArray toByteArray(const QString & iHexValue); 
	};

} 

#endif


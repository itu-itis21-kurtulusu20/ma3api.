#ifndef __PKCS7ISSUERSERIAL__
#define __PKCS7ISSUERSERIAL__

#include "ECertificate.h"
#include "GeneralNames.h"

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
	class Q_DECL_EXPORT PKCS7IssuerSerial  : public EASNWrapperTemplate<ASN1T_PKCS7_IssuerSerial,ASN1C_PKCS7_IssuerSerial>
	{
		GeneralNames mIssuer;
		SerialNumber mSerialNumber;
			
	public:

		PKCS7IssuerSerial(void);
		PKCS7IssuerSerial( const ASN1T_PKCS7_IssuerSerial & iIS);
		PKCS7IssuerSerial( const PKCS7IssuerSerial &iIS);
		PKCS7IssuerSerial( const QByteArray &iIS);
		PKCS7IssuerSerial( const GeneralNames& iIssuer,const SerialNumber& iSN);
		PKCS7IssuerSerial( const ECertificate& iIssuer);

		PKCS7IssuerSerial & operator=(const PKCS7IssuerSerial&);
		friend bool operator==(const PKCS7IssuerSerial& iRHS,const PKCS7IssuerSerial& iLHS);
		friend bool operator!=(const PKCS7IssuerSerial& iRHS, const PKCS7IssuerSerial& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_IssuerSerial & iIS);
		int copyToASNObject(ASN1T_PKCS7_IssuerSerial & oIS) const;
		void freeASNObject(ASN1T_PKCS7_IssuerSerial& oISN)const;

		virtual ~PKCS7IssuerSerial(void);

		// GETTERS AND SETTERS
		
		const GeneralNames & getIssuer() const ;	
		const SerialNumber & getSerialNumber() const ;	

		void setIssuer(const GeneralNames & );	
		void setSerialNumber(const SerialNumber & ) ;	
	
	};

}

#endif


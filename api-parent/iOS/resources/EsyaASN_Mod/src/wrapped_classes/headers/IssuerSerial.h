#ifndef __ISSUERSERIAL__
#define __ISSUERSERIAL__

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
	class Q_DECL_EXPORT IssuerSerial  : public EASNWrapperTemplate<ASN1T_ATTRCERT_IssuerSerial,ASN1C_ATTRCERT_IssuerSerial>
	{
		bool mIssuerUIDPresent;
		GeneralNames mIssuer;
		SerialNumber mSerialNumber;
		EBitString	 mIssuerUID;
			
	public:

		IssuerSerial(void);
		IssuerSerial( const ASN1T_ATTRCERT_IssuerSerial & iIS);
		IssuerSerial( const IssuerSerial &iIS);
		IssuerSerial( const QByteArray &iIS);

		IssuerSerial & operator=(const IssuerSerial&);
		friend bool operator==(const IssuerSerial& iRHS,const IssuerSerial& iLHS);
		friend bool operator!=(const IssuerSerial& iRHS, const IssuerSerial& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_IssuerSerial & iIS);
		int copyToASNObject(ASN1T_ATTRCERT_IssuerSerial & oIS) const;
		void freeASNObject(ASN1T_ATTRCERT_IssuerSerial& oISN)const;

		virtual ~IssuerSerial(void);

		// GETTERS AND SETTERS
		
		bool isIssuerUIDPresent()const;
		const GeneralNames & getIssuer() const ;	
		const SerialNumber & getSerialNumber() const ;	
		const EBitString &   getIssuerUID()const;

		void setIssuer(const GeneralNames & );	
		void setSerialNumber(const SerialNumber & ) ;	
		void setIssuerUID(const EBitString &   );
		void setIssuerUIDPresent(bool);
	
	};

}

#endif


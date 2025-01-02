#ifndef __ISSUERANDSERIALNUMBER__
#define __ISSUERANDSERIALNUMBER__

#include "ECertificate.h"



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
	class Q_DECL_EXPORT IssuerAndSerialNumber  : public EASNWrapperTemplate<ASN1T_PKCS7_IssuerAndSerialNumber,ASN1C_PKCS7_IssuerAndSerialNumber>
	{
		Name mIssuer;
		SerialNumber mSerialNumber;

	public:

		IssuerAndSerialNumber(void);
		IssuerAndSerialNumber( const ASN1T_PKCS7_IssuerAndSerialNumber &);
		IssuerAndSerialNumber( const IssuerAndSerialNumber &);
		IssuerAndSerialNumber( const QByteArray &);
		IssuerAndSerialNumber( const ECertificate &);
		IssuerAndSerialNumber( const Name& ,const SerialNumber& );

		IssuerAndSerialNumber & operator=(const IssuerAndSerialNumber&);
		Q_DECL_EXPORT friend bool operator==(const IssuerAndSerialNumber& iRHS,const IssuerAndSerialNumber& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const IssuerAndSerialNumber& iRHS, const IssuerAndSerialNumber& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_IssuerAndSerialNumber & iISN);
		int copyToASNObject(ASN1T_PKCS7_IssuerAndSerialNumber & oISN) const;
		void freeASNObject(ASN1T_PKCS7_IssuerAndSerialNumber& oISN)const;

		virtual ~IssuerAndSerialNumber(void);

		// GETTERS AND SETTERS

		const Name & getIssuer() const ;	
		const SerialNumber& getSerialNumber() const ;	

		bool isEqual(const ECertificate & )const;
		
		QString toString()const;

	};

}

Q_DECL_EXPORT uint qHash(const esya::IssuerAndSerialNumber& key);

#endif


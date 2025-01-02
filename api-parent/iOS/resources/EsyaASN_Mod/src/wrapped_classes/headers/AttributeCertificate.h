#ifndef __ATTRIBUTECERTIFICATE__
#define __ATTRIBUTECERTIFICATE__


#include "AttributeCertificateInfo.h"

namespace esya
{

	class Q_DECL_EXPORT AttributeCertificate : public EASNWrapperTemplate<ASN1T_ATTRCERT_AttributeCertificate,ASN1C_ATTRCERT_AttributeCertificate>
	{
		AttributeCertificateInfo mACInfo;
		AlgorithmIdentifier mSignatureAlgorithm;
		EBitString mSignatureValue;


	public:
		AttributeCertificate(void);
		AttributeCertificate(const QByteArray & iAttributeCertificate);
		AttributeCertificate(const ASN1T_ATTRCERT_AttributeCertificate & iAttributeCertificate );
		AttributeCertificate(const AttributeCertificate& iAttributeCertificate);

		AttributeCertificate& operator=(const AttributeCertificate& iAttributeCertificate);
		friend bool operator==( const AttributeCertificate& iRHS, const AttributeCertificate& iLHS);
		friend bool operator!=( const AttributeCertificate& iRHS, const AttributeCertificate& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AttributeCertificate & iAttributeCertificater);
		int copyToASNObject(ASN1T_ATTRCERT_AttributeCertificate &oAttributeCertificate)const;
		void freeASNObject(ASN1T_ATTRCERT_AttributeCertificate& oAttributeCertificate)const;
		
		virtual ~AttributeCertificate(void);

		// GETTERS AND SETTERS

		const AttributeCertificateInfo& getACInfo()const;
		const AlgorithmIdentifier & getSignatureAlgorithm()const;
		const EBitString & getSignatureValue()const;

		void setACInfo(const AttributeCertificateInfo& iACInfo);
		void setSignatureAlgorithm(const AlgorithmIdentifier& iSignatureAlgorithm);
		void setSignatureValue(const EBitString & iSignatureValue);

	};

}

#endif


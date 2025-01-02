#ifndef __V2FORM__
#define __V2FORM__


#include "ObjectDigestInfo.h"
#include "IssuerSerial.h"


namespace esya
{

	class Q_DECL_EXPORT V2Form : public EASNWrapperTemplate<ASN1T_ATTRCERT_V2Form,ASN1C_ATTRCERT_V2Form>
	{
		bool mBaseCertificateIDPresent;
		bool mObjectDigestInfoPresent;
		bool mIssuerNamePresent;

		IssuerSerial		mBaseCertificateID;
		ObjectDigestInfo	mObjectDigestInfo;
		GeneralNames		mIssuerName;

	public:
		V2Form(void);
		V2Form(const QByteArray & iV2Form);
		V2Form(const ASN1T_ATTRCERT_V2Form & iV2Form );
		V2Form(const V2Form& iV2Form);

		V2Form& operator=(const V2Form& iV2Form);
		friend bool operator==( const V2Form& iRHS, const V2Form& iLHS);
		friend bool operator!=( const V2Form& iRHS, const V2Form& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_V2Form & iV2Formr);
		int copyToASNObject(ASN1T_ATTRCERT_V2Form &oV2Form)const;
		void freeASNObject(ASN1T_ATTRCERT_V2Form& oV2Form)const;

		virtual ~V2Form(void);

		// GETTERS AND SETTERS

		bool isBaseCertificateIDPresent()const;
		bool isObjectDigestInfoPresent()const;
		bool isIssuerNamePresent()const;

		const IssuerSerial & getBaseCertificateID() const;
		const ObjectDigestInfo &  getObjectDigestInfo()const;
		const GeneralNames & getIssuerName() const ;
	
		void setBaseCertificateID(const IssuerSerial & ) ;
		void setObjectDigestInfo(const ObjectDigestInfo &  );
		void setIssuerName(const GeneralNames & );
		
		void setBaseCertificateIDPresent(bool);
		void setObjectDigestInfoPresent(bool);
		void setIssuerNamePresent(bool);
	};

}

#endif


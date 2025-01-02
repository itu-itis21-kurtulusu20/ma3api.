#ifndef __TARGETCERT__
#define __TARGETCERT__


#include "ObjectDigestInfo.h"
#include "IssuerSerial.h"


namespace esya
{

	class Q_DECL_EXPORT TargetCert : public EASNWrapperTemplate<ASN1T_ATTRCERT_TargetCert,ASN1C_ATTRCERT_TargetCert>
	{
		bool mCertDigestInfoPresent;
		bool mTargetNamePresent;

		IssuerSerial		mTargetCertificate;
		ObjectDigestInfo	mCertDigestInfo;
		GeneralName			mTargetName;

	public:
		TargetCert(void);
		TargetCert(const QByteArray & iTargetCert);
		TargetCert(const ASN1T_ATTRCERT_TargetCert & iTargetCert );
		TargetCert(const TargetCert& iTargetCert);

		TargetCert& operator=(const TargetCert& iTargetCert);
		friend bool operator==( const TargetCert& iRHS, const TargetCert& iLHS);
		friend bool operator!=( const TargetCert& iRHS, const TargetCert& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_TargetCert & iTargetCertr);
		int copyToASNObject(ASN1T_ATTRCERT_TargetCert &oTargetCert)const;
		void freeASNObject(ASN1T_ATTRCERT_TargetCert& oTargetCert)const;

		virtual ~TargetCert(void);

		// GETTERS AND SETTERS

		bool isCertDigestInfoPresent()const;
		bool isTargetNamePresent()const;

		const IssuerSerial & getTargetCertificate() const;
		const ObjectDigestInfo &  getCertDigestInfo()const;
		const GeneralName & getTargetName() const ;
	
		void setTargetCertificate(const IssuerSerial & ) ;
		void setCertDigestInfo(const ObjectDigestInfo &  );
		void setTargetName(const GeneralName & );
		
		void setCertDigestInfoPresent(bool);
		void setTargetNamePresent(bool);
	};

}

#endif


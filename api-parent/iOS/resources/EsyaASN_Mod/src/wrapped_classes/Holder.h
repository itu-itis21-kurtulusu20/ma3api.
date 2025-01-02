#ifndef __HOLDER__
#define __HOLDER__


#include "ObjectDigestInfo.h"
#include "IssuerSerial.h"


namespace esya
{

	class Q_DECL_EXPORT Holder : public EASNWrapperTemplate<ASN1T_ATTRCERT_Holder,ASN1C_ATTRCERT_Holder>
	{
		bool mBaseCertificateIDPresent;
		bool mObjectDigestInfoPresent;
		bool mEntityNamePresent;

		IssuerSerial		mBaseCertificateID;
		ObjectDigestInfo	mObjectDigestInfo;
		GeneralNames		mEntityName;

	public:
		Holder(void);
		Holder(const QByteArray & iHolder);
		Holder(const ASN1T_ATTRCERT_Holder & iHolder );
		Holder(const Holder& iHolder);

		Holder& operator=(const Holder& iHolder);
		friend bool operator==( const Holder& iRHS, const Holder& iLHS);
		friend bool operator!=( const Holder& iRHS, const Holder& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_Holder & iHolder);
		int copyToASNObject(ASN1T_ATTRCERT_Holder &oHolder)const;
		void freeASNObject(ASN1T_ATTRCERT_Holder& oHolder)const;

		virtual ~Holder(void);

		// GETTERS AND SETTERS

		bool isBaseCertificateIDPresent()const;
		bool isObjectDigestInfoPresent()const;
		bool isEntityNamePresent()const;

		const IssuerSerial & getBaseCertificateID() const;
		const ObjectDigestInfo &  getObjectDigestInfo()const;
		const GeneralNames & getEntityName() const ;
	
		void setBaseCertificateID(const IssuerSerial & ) ;
		void setObjectDigestInfo(const ObjectDigestInfo &  );
		void setEntityName(const GeneralNames & );
		
		void setBaseCertificateIDPresent(bool);
		void setObjectDigestInfoPresent(bool);
		void setEntityNamePresent(bool);
	};

}

#endif


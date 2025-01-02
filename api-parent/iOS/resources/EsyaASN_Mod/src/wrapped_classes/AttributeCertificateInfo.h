#ifndef __ATTRIBUTECERTIFICATEINFO__
#define __ATTRIBUTECERTIFICATEINFO__

#include <QString>
#include "EASNWrapperTemplate.h"
#include "attrcert.h"
#include "Holder.h"
#include "AttCertIssuer.h"
#include "AttCertValidityPeriod.h"
#include "Attribute.h"
#include "Extension.h"

namespace esya
{

	class Q_DECL_EXPORT AttributeCertificateInfo : public EASNWrapperTemplate<ASN1T_ATTRCERT_AttributeCertificateInfo,ASN1C_ATTRCERT_AttributeCertificateInfo>
	{
		bool mIssuerUniqueIDPresent ;
		bool mExtensionsPresent ;

		int mVersion;
		Holder mHolder;
		AttCertIssuer mIssuer;
		AlgorithmIdentifier mSignature;
		SerialNumber mSerialNumber;
		AttCertValidityPeriod mAttrCertValidityPeriod;
		QList<Attribute> mAttributes;
		EBitString mIssuerUniqueID;
		QList<Extension> mExtensions;

	public:
		AttributeCertificateInfo(void);
		AttributeCertificateInfo(const QByteArray & iAttributeCertificateInfo);
		AttributeCertificateInfo(const ASN1T_ATTRCERT_AttributeCertificateInfo & iAttributeCertificateInfo );
		AttributeCertificateInfo(const AttributeCertificateInfo& iAttributeCertificateInfo);

		AttributeCertificateInfo& operator=(const AttributeCertificateInfo& iAttributeCertificateInfo);
		friend bool operator==( const AttributeCertificateInfo& iRHS, const AttributeCertificateInfo& iLHS);
		friend bool operator!=( const AttributeCertificateInfo& iRHS, const AttributeCertificateInfo& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AttributeCertificateInfo & iAttributeCertificateInfor);
		int copyToASNObject(ASN1T_ATTRCERT_AttributeCertificateInfo &oAttributeCertificateInfo)const;
		void freeASNObject(ASN1T_ATTRCERT_AttributeCertificateInfo& oAttributeCertificateInfo)const;

		virtual ~AttributeCertificateInfo(void);

		// GETTERS AND SETTERS

		bool isIssuerUniqueIDPresent()const ;
		bool isExtensionsPresent()const ;

		int getVersion()const;
		const Holder& getHolder()const;
		const AttCertIssuer & getIssuer()const;
		const AlgorithmIdentifier & getSignature()const;
		const SerialNumber & getSerialNumber()const;
		const AttCertValidityPeriod & getAttrCertValidityPeriod()const;
		const QList<Attribute>& getAttributes()const;
		const EBitString & getIssuerUniqueID()const;
		const QList<Extension> & getExtensions()const;

		void setIssuerUniqueIDPresent(bool );
		void setExtensionsPresent(bool );

		void setVersion(int);
		void setHolder(const Holder& );
		void setIssuer(const AttCertIssuer & );
		void setSignature(const AlgorithmIdentifier & );
		void setSerialNumber(const SerialNumber & );
		void setAttrCertValidityPeriod(const AttCertValidityPeriod & );
		void setAttributes(const QList<Attribute>& );
		void setIssuerUniqueID(const EBitString & );
		void setExtensions(const QList<Extension> & );
	};

}

#endif



#ifndef __RECIPIENTIDENTIFIER__
#define __RECIPIENTIDENTIFIER__

#include "IssuerAndSerialNumber.h"

namespace esya
{

	enum RIDType {	T_IssuerAndSerial		= T_CMS_RecipientIdentifier_issuerAndSerialNumber , 
		T_SubjectKeyIdentifier	= T_CMS_RecipientIdentifier_subjectKeyIdentifier
	};

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT RecipientIdentifier  : public EASNWrapperTemplate<ASN1T_CMS_RecipientIdentifier,ASN1C_CMS_RecipientIdentifier>
	{
		RIDType					mType;
		IssuerAndSerialNumber	mIssuerAndSerialNumber;
		QByteArray				mSubjectKeyIdentifier;

	public:

		RecipientIdentifier(void);
		RecipientIdentifier(const QByteArray & iRID);
		RecipientIdentifier(const ASN1T_CMS_RecipientIdentifier & iRID);
		RecipientIdentifier(const RecipientIdentifier& iRID);
		RecipientIdentifier(const IssuerAndSerialNumber& iISN);

		RecipientIdentifier& operator=(const RecipientIdentifier & );
		Q_DECL_EXPORT friend bool operator==(const RecipientIdentifier & ,const RecipientIdentifier & );
		Q_DECL_EXPORT friend bool operator!=(const RecipientIdentifier & ,const RecipientIdentifier & );


		int copyFromASNObject(const ASN1T_CMS_RecipientIdentifier& iRID);
		int copyToASNObject(ASN1T_CMS_RecipientIdentifier & oRID)const;
		void freeASNObject(ASN1T_CMS_RecipientIdentifier & oRID)const;

		virtual ~RecipientIdentifier(void);

		// GETTERS AND SETTERS

		const RIDType & getType() const ;
		const IssuerAndSerialNumber & getIssuerAndSerialNumber() const;
		const QByteArray & getSubjectKeyIdentifier() const;		

		void setIssuerAndSerialNumber( const IssuerAndSerialNumber & );
		void setSubjectKeyIdentifier( const QByteArray & );

		bool isMatch(const ECertificate & )const;

		QString toString() const;
	};

}


#endif


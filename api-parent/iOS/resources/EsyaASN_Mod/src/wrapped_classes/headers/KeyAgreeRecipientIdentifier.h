
#ifndef __KEYAGREERECIPIENTIDENTIFIER__
#define __KEYAGREERECIPIENTIDENTIFIER__


#include "cms.h"
#include "RecipientKeyIdentifier.h"
#include "IssuerAndSerialNumber.h"



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
	class Q_DECL_EXPORT KeyAgreeRecipientIdentifier  : public EASNWrapperTemplate<ASN1T_CMS_KeyAgreeRecipientIdentifier,ASN1C_CMS_KeyAgreeRecipientIdentifier>
	{
	public:
		enum KeyAgreeRecipientIdentifierType { KARIType_issuerAndSerialNumber =  T_CMS_KeyAgreeRecipientIdentifier_issuerAndSerialNumber , KARIType_rKeyId = T_CMS_KeyAgreeRecipientIdentifier_rKeyId };
	
	protected:
		KeyAgreeRecipientIdentifierType mType;

		IssuerAndSerialNumber	mIssuerAndSerialNumber;
		RecipientKeyIdentifier	mRKeyId;
		


	public:

		KeyAgreeRecipientIdentifier(const KeyAgreeRecipientIdentifier &);
		KeyAgreeRecipientIdentifier(const ASN1T_CMS_KeyAgreeRecipientIdentifier & );
		KeyAgreeRecipientIdentifier(const QByteArray & );
		KeyAgreeRecipientIdentifier(void);
		KeyAgreeRecipientIdentifier(const IssuerAndSerialNumber & iISN);
		KeyAgreeRecipientIdentifier(const ECertificate & , const KeyAgreeRecipientIdentifierType &);


		KeyAgreeRecipientIdentifier & operator=(const KeyAgreeRecipientIdentifier&);
		friend bool operator==(const KeyAgreeRecipientIdentifier & iRHS, const KeyAgreeRecipientIdentifier& iLHS);
		friend bool operator!=(const KeyAgreeRecipientIdentifier & iRHS, const KeyAgreeRecipientIdentifier& iLHS);


		int copyFromASNObject(const ASN1T_CMS_KeyAgreeRecipientIdentifier & );
		int copyToASNObject(ASN1T_CMS_KeyAgreeRecipientIdentifier &)  const;
		void freeASNObject(ASN1T_CMS_KeyAgreeRecipientIdentifier & oExtension)const;

		virtual ~KeyAgreeRecipientIdentifier(void);

		// GETTERS AND SETTERS

		const KeyAgreeRecipientIdentifierType& getType() const;
		const IssuerAndSerialNumber& getIssuerAndSerialNumber() const;
		const RecipientKeyIdentifier& getRKeyId() const ;

		void setIssuerAndSerialNumber(const RecipientKeyIdentifier& iRKI);
		void setRKeyId(const RecipientKeyIdentifier& iRKI);

		virtual QString toString()const;

		bool isEqual(const ECertificate & )const;

	};

}

#endif


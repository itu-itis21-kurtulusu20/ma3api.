
#ifndef __ORIGINATORIDENTIFIERORKEY__
#define __ORIGINATORIDENTIFIERORKEY__

#include "SubjectPublicKeyInfo.h"
#include "SubjectKeyIdentifier.h"
#include "IssuerAndSerialNumber.h"

namespace esya
{
	typedef SubjectPublicKeyInfo OriginatorPublicKey;
	
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT OriginatorIdentifierOrKey : public EASNWrapperTemplate<ASN1T_CMS_OriginatorIdentifierOrKey,ASN1C_CMS_OriginatorIdentifierOrKey>
	{
	public:
		enum OKType {	OKType_IssuerAndSerialNumber = T_CMS_OriginatorIdentifierOrKey_issuerAndSerialNumber,
						OKType_SubjectKeyIdentifier  = T_CMS_OriginatorIdentifierOrKey_subjectKeyIdentifier,		
						OKType_OriginatorKey		 = T_CMS_OriginatorIdentifierOrKey_originatorKey
					};

	protected:
		OKType mType;
		IssuerAndSerialNumber	mIssuerAndSerialNumber;
		SubjectKeyIdentifier	mSubjectKeyIdentifier;
		OriginatorPublicKey		mOriginatorKey;
		

	public:
		OriginatorIdentifierOrKey(void);
		OriginatorIdentifierOrKey(const ASN1T_CMS_OriginatorIdentifierOrKey & iOK);
		OriginatorIdentifierOrKey(const QByteArray & iOK);
		OriginatorIdentifierOrKey(const OriginatorIdentifierOrKey &iOK);

		OriginatorIdentifierOrKey & operator=(const OriginatorIdentifierOrKey& iOK);

		Q_DECL_EXPORT friend bool operator==(const OriginatorIdentifierOrKey & iRHS, const OriginatorIdentifierOrKey& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const OriginatorIdentifierOrKey & iRHS, const OriginatorIdentifierOrKey& iLHS);

		int copyFromASNObject(const ASN1T_CMS_OriginatorIdentifierOrKey& iOK);
		int copyToASNObject(ASN1T_CMS_OriginatorIdentifierOrKey & oOK) const;
		void freeASNObject(ASN1T_CMS_OriginatorIdentifierOrKey& oOK)const;

		virtual ~OriginatorIdentifierOrKey(void);

		// GETTERS AND SETTERS

		OKType						  getType()const ;
		const IssuerAndSerialNumber	& getIssuerAndSerialNumber()const;
		const SubjectKeyIdentifier	& getSubjectKeyIdentifier()const;
		const OriginatorPublicKey	& getOriginatorKey()const;

		void setType(OKType	);
		void setIssuerAndSerialNumber(const IssuerAndSerialNumber & iISA );
		void setSubjectKeyIdentifier(const SubjectKeyIdentifier	& iSKI);
		void setOriginatorKey(const OriginatorPublicKey	& iOPK);

	};

}

#endif


#ifndef __OBJECTDIGESTINFO__
#define __OBJECTDIGESTINFO__

#include "attrcert.h"
#include "ortak.h"
#include "AlgorithmIdentifier.h"
#include "EBitString.h"

namespace esya
{

	class Q_DECL_EXPORT ObjectDigestInfo : public EASNWrapperTemplate<ASN1T_ATTRCERT_ObjectDigestInfo,ASN1C_ATTRCERT_ObjectDigestInfo>
	{
	public:
		enum DigestedObjectType {	DOT_PublicKey = ATTRCERT_ObjectDigestInfo_digestedObjectType::publicKey ,
									DOT_PublicKeyCert = ATTRCERT_ObjectDigestInfo_digestedObjectType::publicKeyCert ,
									DOT_OtherObjectTypes = ATTRCERT_ObjectDigestInfo_digestedObjectType::otherObjectTypes
								};
	protected:
	
		bool mOtherObjectTypeIDPresent;
		
		DigestedObjectType	mDigestedObjectType;
		AlgorithmIdentifier mDigestAlgorithm;
		EBitString			mObjectDigest;
		ASN1TObjId			mOtherObjectTypeID;


	public:
		ObjectDigestInfo(void);
		ObjectDigestInfo(const QByteArray & iDI);
		ObjectDigestInfo(const ASN1T_ATTRCERT_ObjectDigestInfo & iDI );
		ObjectDigestInfo(const ObjectDigestInfo& iDI);

		ObjectDigestInfo& operator=(const ObjectDigestInfo& iDI);
		friend bool operator==( const ObjectDigestInfo& iRHS, const ObjectDigestInfo& iLHS);
		friend bool operator!=( const ObjectDigestInfo& iRHS, const ObjectDigestInfo& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_ObjectDigestInfo & iDI);
		int copyToASNObject(ASN1T_ATTRCERT_ObjectDigestInfo &oDI)const;
		void freeASNObject(ASN1T_ATTRCERT_ObjectDigestInfo& )const;

		virtual ~ObjectDigestInfo(void);

		// GETTERS AND SETTERS

		bool isOtherObjectTypeIDPresent()const;
		const DigestedObjectType &  getDigestedObjectType()const;
		const AlgorithmIdentifier & getDigestAlgorithm() const;
		const EBitString& getObjectDigest() const ;
		const ASN1TObjId& getOtherObjectTypeID() const ;

		void setDigestedObjectType(const DigestedObjectType &  iDOT);
		void setDigestAlgorithm(const AlgorithmIdentifier& iDigestAlg);
		void setObjectDigest(const EBitString & iObjectDigest) ;
		void setOtherObjectTypeID(const ASN1TObjId & iOOTID)  ;
		void setOtherObjectTypeIDPresent(bool );

	};

}

#endif


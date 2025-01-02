
#ifndef __KEYAGREERECIPIENTINFO__
#define __KEYAGREERECIPIENTINFO__


#include "cms.h"
#include "OriginatorIdentifierOrKey.h"
#include "RecipientEncryptedKeys.h"
#include "AlgorithmIdentifier.h"

#include "ECCCMSSharedInfo.h"
#include "AnahtarBilgisi.h"


namespace esya
{
	
	typedef AlgorithmIdentifier KeyEncryptionAlgorithmIdentifier;

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT KeyAgreeRecipientInfo  : public EASNWrapperTemplate<ASN1T_CMS_KeyAgreeRecipientInfo,ASN1C_CMS_KeyAgreeRecipientInfo>
	{
	public:

	
	protected:
		
		bool mUKMPresent;

		int									mVersion;
		QByteArray							mUKM;
		OriginatorIdentifierOrKey			mOriginator;
		KeyEncryptionAlgorithmIdentifier	mKeyEncryptionAlgorithm;
		RecipientEncryptedKeys				mRecipientEncryptedKeys;
		

	public:

		KeyAgreeRecipientInfo(const KeyAgreeRecipientInfo &);
		KeyAgreeRecipientInfo(const ASN1T_CMS_KeyAgreeRecipientInfo & );
		KeyAgreeRecipientInfo(const QByteArray & );
		KeyAgreeRecipientInfo(void);

		KeyAgreeRecipientInfo & operator=(const KeyAgreeRecipientInfo&);
		friend bool operator==(const KeyAgreeRecipientInfo & iRHS, const KeyAgreeRecipientInfo& iLHS);
		friend bool operator!=(const KeyAgreeRecipientInfo & iRHS, const KeyAgreeRecipientInfo& iLHS);


		int copyFromASNObject(const ASN1T_CMS_KeyAgreeRecipientInfo & );
		int copyToASNObject(ASN1T_CMS_KeyAgreeRecipientInfo &)  const;
		void freeASNObject(ASN1T_CMS_KeyAgreeRecipientInfo & oKARI)const;

		virtual ~KeyAgreeRecipientInfo(void);
	
		// GETTERS AND SETTERS

		const bool &isUKMPresent() const;

		int											getVersion()const;
		const QByteArray & 							getUKM()const;
		const OriginatorIdentifierOrKey &			getOriginator()const;
		const KeyEncryptionAlgorithmIdentifier &	getKeyEncryptionAlgorithm()const;
		const RecipientEncryptedKeys &				getRecipientEncryptedKeys()const;

		void setVersion(int	);
		void setUKM(const QByteArray & iUKM );
		void setOriginator(const OriginatorIdentifierOrKey & iOK);
		void setKeyEncryptionAlgorithm(const KeyEncryptionAlgorithmIdentifier &	iKEAlg);
		void setRecipientEncryptedKeys(const RecipientEncryptedKeys & iREKs);

		int getRecipientIndex(const ECertificate & iRecipient) const;
		const QByteArray& getEncryptedKey(const ECertificate & iRecipient)const;

		virtual QString toString()const;

		
		ECCCMSSharedInfo cmsSharedInfoOlustur(const QByteArray & iUKM, const AlgorithmIdentifier & iKEAlg)const;
		QByteArray decryptKey(const ECertificate& iCert, const OzelAnahtarBilgisi& iAB)const;

	};

}

#endif


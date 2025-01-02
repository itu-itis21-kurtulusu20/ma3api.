
#ifndef __KEYTRANSRECIPIENTINFO__
#define __KEYTRANSRECIPIENTINFO__


#include "RecipientIdentifier.h"
#include "AlgorithmIdentifier.h"

#include "AnahtarBilgisi.h"

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
		class Q_DECL_EXPORT KeyTransRecipientInfo : public EASNWrapperTemplate<ASN1T_CMS_KeyTransRecipientInfo,ASN1C_CMS_KeyTransRecipientInfo>
		{
			ASN1T_CMS_CMSVersion	mVersion;
			RecipientIdentifier		mRID;
			AlgorithmIdentifier		mKeyEncryptionAlgorithm;
			QByteArray				mEncryptedKey;
		
		public:

			KeyTransRecipientInfo(void);
			KeyTransRecipientInfo(const QByteArray & iKTRI);
			KeyTransRecipientInfo(const ASN1T_CMS_KeyTransRecipientInfo & iKTRI);
			KeyTransRecipientInfo(const ASN1T_CMS_CMSVersion & ,const RecipientIdentifier&, const AlgorithmIdentifier& , const QByteArray & );
			KeyTransRecipientInfo(const KeyTransRecipientInfo& iKTRI);
			

			KeyTransRecipientInfo & operator=(const KeyTransRecipientInfo & iKTRI);
			friend bool operator==(const KeyTransRecipientInfo & ,const KeyTransRecipientInfo & );
			friend bool operator!=(const KeyTransRecipientInfo & ,const KeyTransRecipientInfo & );
			
			int copyFromASNObject(const ASN1T_CMS_KeyTransRecipientInfo & iKTRI);
			int copyToASNObject(ASN1T_CMS_KeyTransRecipientInfo & oKTRI)const;
			void freeASNObject(ASN1T_CMS_KeyTransRecipientInfo & oKTRI)const;

			virtual ~KeyTransRecipientInfo(void);

			// GETTERS AND SETTERS

			const QByteArray			& getEncryptedKey()	const;
			const AlgorithmIdentifier	& getKeyEncAlg()	const;
			const ASN1T_CMS_CMSVersion	& getVersion()		const;
			const RecipientIdentifier	& getRID()			const;


			void setEncryptedKey(const QByteArray& iEncryptedKey);
			void setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg );
			void setVersion(ASN1T_CMS_CMSVersion iVersion);
			void setRID(const RecipientIdentifier &iRID);


			QByteArray decryptKey(const OzelAnahtarBilgisi& iAB)const;
	};

}

#endif 


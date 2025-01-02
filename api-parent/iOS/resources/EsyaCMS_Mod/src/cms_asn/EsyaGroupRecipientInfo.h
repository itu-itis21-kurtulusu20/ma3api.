
#ifndef __ESYAGROUPRECIPIENTINFO__
#define __ESYAGROUPRECIPIENTINFO__

#include "esya.h"
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
		class EsyaGroupRecipientInfo : public EASNWrapperTemplate<ASN1T_ESYA_EsyaGroupRecipientInfo,ASN1C_ESYA_EsyaGroupRecipientInfo>
		{
			int						mGID;
			int						mGroupIndex;
			int						mGroupSize;
			RecipientIdentifier		mRID;
			AlgorithmIdentifier		mKeyEncryptionAlgorithm;
			QByteArray				mEncryptedKey;
		
		public:

			EsyaGroupRecipientInfo(void);
			EsyaGroupRecipientInfo(const QByteArray & iEGRI);
			EsyaGroupRecipientInfo(const ASN1T_ESYA_EsyaGroupRecipientInfo & iEGRI);
			EsyaGroupRecipientInfo(const EsyaGroupRecipientInfo& iEGRI);
			

			EsyaGroupRecipientInfo & operator=(const EsyaGroupRecipientInfo & iKTRI);
			friend bool operator==(const EsyaGroupRecipientInfo & ,const EsyaGroupRecipientInfo & );
			friend bool operator!=(const EsyaGroupRecipientInfo & ,const EsyaGroupRecipientInfo & );
			
			int copyFromASNObject(const ASN1T_ESYA_EsyaGroupRecipientInfo & iEGRI);
			int copyToASNObject(ASN1T_ESYA_EsyaGroupRecipientInfo & oEGRI)const;
			void freeASNObject(ASN1T_ESYA_EsyaGroupRecipientInfo & oEGRI)const;

			virtual ~EsyaGroupRecipientInfo(void);

			// GETTERS AND SETTERS

			const QByteArray			& getEncryptedKey()	const;
			const AlgorithmIdentifier	& getKeyEncAlg()	const;
			const int					& getGID()			const;
			const int					& getGroupIndex()	const;
			const int					& getGroupSize()	const;
			const RecipientIdentifier	& getRID()			const;


			void setEncryptedKey(const QByteArray& iEncryptedKey);
			void setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg );
			void setGID(int iGID);
			void setGroupIndex(int iGroupIndex);
			void setGroupSize(int iGroupSize);
			void setRID(const RecipientIdentifier &iRID);

			QByteArray decryptKey(const QList< QPair<ECertificate,OzelAnahtarBilgisi> >& iCertKeyList)const;
	};

}

#endif 


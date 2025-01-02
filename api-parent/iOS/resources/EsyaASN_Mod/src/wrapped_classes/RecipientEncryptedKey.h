
#ifndef __RECIPIENTENCRYPTEDKEY__
#define __RECIPIENTENCRYPTEDKEY__


#include "KeyAgreeRecipientIdentifier.h"


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
	class Q_DECL_EXPORT RecipientEncryptedKey  : public EASNWrapperTemplate<ASN1T_CMS_RecipientEncryptedKey,ASN1C_CMS_RecipientEncryptedKey>
	{
	protected:
		KeyAgreeRecipientIdentifier	mRID;
		QByteArray					mEncryptedKey;

	public:

		RecipientEncryptedKey();
		RecipientEncryptedKey(const RecipientEncryptedKey &);
		RecipientEncryptedKey(const ASN1T_CMS_RecipientEncryptedKey & );
		RecipientEncryptedKey(const QByteArray & );
		RecipientEncryptedKey(const KeyAgreeRecipientIdentifier& , const QByteArray & );


		RecipientEncryptedKey & operator=(const RecipientEncryptedKey&);
		friend bool operator==(const RecipientEncryptedKey & iRHS, const RecipientEncryptedKey& iLHS);
		friend bool operator!=(const RecipientEncryptedKey & iRHS, const RecipientEncryptedKey& iLHS);


		int copyFromASNObject(const ASN1T_CMS_RecipientEncryptedKey & );
		int copyToASNObject(ASN1T_CMS_RecipientEncryptedKey &)  const;
		void freeASNObject(ASN1T_CMS_RecipientEncryptedKey & oExtension)const;

		const KeyAgreeRecipientIdentifier&	getRID()const ;
		const QByteArray&					getEncryptedKey()const;

		int  copyREKList(const QList<RecipientEncryptedKey> iList ,ASN1TPDUSeqOfList & oREKs);
		int	copyREKList(const ASN1TPDUSeqOfList & iREKs, QList<RecipientEncryptedKey>& oList);

		virtual ~RecipientEncryptedKey(void);

		// GETTERS AND SETTERS
		
		void setRID(const KeyAgreeRecipientIdentifier& iRID);
		void setEncryptedKey(const QByteArray& iEC);

		virtual QString toString()const;

	};

}

#endif


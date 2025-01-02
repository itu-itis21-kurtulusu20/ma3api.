
#ifndef __RECIPIENTENCRYPTEDKEYS__
#define __RECIPIENTENCRYPTEDKEYS__

#include "RecipientEncryptedKey.h"


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
	class Q_DECL_EXPORT RecipientEncryptedKeys : public EASNWrapperTemplate<ASN1T_CMS_RecipientEncryptedKeys,ASN1C_CMS_RecipientEncryptedKeys>
	{
		QList<RecipientEncryptedKey> mList;

	public:
		RecipientEncryptedKeys(void);
		RecipientEncryptedKeys(const ASN1T_CMS_RecipientEncryptedKeys & iREKList);
		RecipientEncryptedKeys(const QByteArray & iREKList);
		RecipientEncryptedKeys(const RecipientEncryptedKeys &iREKList);

		RecipientEncryptedKeys & operator=(const RecipientEncryptedKeys& iREKList);

		Q_DECL_EXPORT friend bool operator==(const RecipientEncryptedKeys & iRHS, const RecipientEncryptedKeys& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const RecipientEncryptedKeys & iRHS, const RecipientEncryptedKeys& iLHS);


		int copyFromASNObject(const ASN1T_CMS_RecipientEncryptedKeys& iREKList);
		int copyToASNObject(ASN1T_CMS_RecipientEncryptedKeys & oREKList) const;
		void freeASNObject(ASN1T_CMS_RecipientEncryptedKeys& oREKList)const;

		virtual ~RecipientEncryptedKeys(void);

		// GETTERS AND SETTERS

		const QList<RecipientEncryptedKey> &getList() const ;

		void setList(const QList<RecipientEncryptedKey> &iList);
		void addREK(const RecipientEncryptedKey &iREK);
	};

}

#endif


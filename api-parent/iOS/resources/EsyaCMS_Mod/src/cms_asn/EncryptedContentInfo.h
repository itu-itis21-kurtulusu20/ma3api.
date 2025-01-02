#ifndef __ENCRYPTEDCONTENTINFO__
#define __ENCRYPTEDCONTENTINFO__

#include "cms.h"
#include "ELogger.h"
#include "AlgorithmIdentifier.h"

#define DEFAULT_ENCRYPTEDCONTENINFO_CONTENTTYPE CMS_id_data

namespace esya
{

	/**
	* \ingroup EsyaCMS
	* 
	* ASN1 wrapper sýnýfý. Detaylar için cms.asn dökümanýna bakýnýz
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EncryptedContentInfo  : public EASNWrapperTemplate<ASN1T_PKCS7_EncryptedContentInfo,ASN1C_PKCS7_EncryptedContentInfo>
	{

	protected:

		QByteArray				mEncryptedContent;
		ASN1T_CMS_ContentType	mContentType;
		AlgorithmIdentifier		mContentEncAlg;

	public:

		EncryptedContentInfo(void);
		EncryptedContentInfo( const ASN1T_PKCS7_EncryptedContentInfo &);
		EncryptedContentInfo( const EncryptedContentInfo &);
		EncryptedContentInfo( const QByteArray &);
		EncryptedContentInfo( const QByteArray& , const AlgorithmIdentifier& , const ASN1T_CMS_ContentType = DEFAULT_ENCRYPTEDCONTENINFO_CONTENTTYPE );

		EncryptedContentInfo & operator=(const EncryptedContentInfo&);
		friend bool operator==(const EncryptedContentInfo& iRHS,const EncryptedContentInfo& iLHS);
		friend bool operator!=(const EncryptedContentInfo& iRHS, const EncryptedContentInfo& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_EncryptedContentInfo & iECI);
		int copyToASNObject(ASN1T_PKCS7_EncryptedContentInfo & oECI) const;
		void freeASNObject(ASN1T_PKCS7_EncryptedContentInfo& oECI)const;

		QByteArray getEncryptedContent() const ;	
		const AlgorithmIdentifier& getContentEncAlg() const ;	
		const ASN1T_CMS_ContentType & getContentType() const ;
		
		void setContentEncAlg(const AlgorithmIdentifier& iEncAlg);
		void setContentType(const ASN1T_CMS_ContentType& iContentType);
		void setEncryptedContent(const QByteArray& iEncryptedContent);

		virtual ~EncryptedContentInfo(void);

		QByteArray decrypt(const QByteArray& iEncKey)const;
		QByteArray decryptPBE(const QString& iParola)const;
	
	};

}

#endif


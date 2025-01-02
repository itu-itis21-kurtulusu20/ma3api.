
#ifndef __ESYAPASSWORDRECIPIENTINFO__
#define __ESYAPASSWORDRECIPIENTINFO__


#include "cms.h"
#include "esya.h"
#include "AlgorithmIdentifier.h"
#include "DigestInfo.h"
#include "EASNWrapper.h"

namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* Kendi parola tabanlý þifreli dosyalarýmýz için tanýmladýðýmýz Þifreci Bilgisi ASN1 wrapper sýnýfý. Detaylar için cms.asn dökümanýna bakýnýz
	*
	* \author dindaro
	*
	*/
	class ESYAPasswordRecipientInfo : public EASNWrapper
	{
		bool					mKeyDerivationAlgorithmPresent ;

		ASN1T_CMS_CMSVersion	mVersion;
		AlgorithmIdentifier		mKeyDerivationAlgorithm;
		AlgorithmIdentifier		mKeyEncryptionAlgorithm;
		QByteArray				mEncryptedKey;
		DigestInfo				mKeyHash;

	public:
		static const ASN1T_CMS_CMSVersion	DEFAULT_VERSION;
		static const AlgorithmIdentifier	DEFAULT_DIGEST_ALGORITHM ;		

		ESYAPasswordRecipientInfo(void);
		ESYAPasswordRecipientInfo(const QByteArray & iEPWRI);
		ESYAPasswordRecipientInfo(const ASN1T_ESYA_ESYAPasswordRecipientInfo & iEPWRI);
		ESYAPasswordRecipientInfo(const ESYAPasswordRecipientInfo& iEPWRI);
		ESYAPasswordRecipientInfo(const AlgorithmIdentifier& iKeyEncAlg , const AlgorithmIdentifier iKeyDrvAlg , const QByteArray iKey , const QString &iParola);


		ESYAPasswordRecipientInfo & operator=(const ESYAPasswordRecipientInfo & iEPWRI);
		friend bool operator==(const ESYAPasswordRecipientInfo & ,const ESYAPasswordRecipientInfo & );
		friend bool operator!=(const ESYAPasswordRecipientInfo & ,const ESYAPasswordRecipientInfo & );

		int copyFromASNObject(const ASN1T_ESYA_ESYAPasswordRecipientInfo & iEPWRI);
		virtual int constructObject(const QByteArray & iEPWRI);

		int copyToASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo& oEPWRI)const;

		ASN1T_ESYA_ESYAPasswordRecipientInfo* getASNCopy()const;
		static ASN1T_ESYA_ESYAPasswordRecipientInfo * getASNCopyOf(const ESYAPasswordRecipientInfo & iEPWRI);

		static void freeASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo * oEPWRI);
		static void freeASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo & oEPWRI);

		const bool isKeyDrvAlgPresent() const;

		const QByteArray			& getEncryptedKey()	const;
		const AlgorithmIdentifier	& getKeyEncAlg()	const;
		const AlgorithmIdentifier	& getKeyDrvAlg()	const;		
		const int					& getVersion()		const;
		const DigestInfo			& getKeyHash()		const;

		void setKeyDrvAlgPresent(const bool  iKDAP);

		void setVersion(int iVersion);
		void setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg );
		void setKeyDrvAlg( const AlgorithmIdentifier & iKeyDrvAlg );		
		void setEncryptedKey(const QByteArray& iEncryptedKey);
		void setKeyHash(const DigestInfo& iKeyHash);

		QByteArray getEncodedBytes()const;

		QByteArray decryptKey(const QString & ) const;
		QByteArray encryptKey(const QString & , const QByteArray & ) const;

	public:
		~ESYAPasswordRecipientInfo(void);
	};

}

#endif 


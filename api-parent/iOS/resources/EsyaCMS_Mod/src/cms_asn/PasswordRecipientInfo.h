
#ifndef __PASSWORDRECIPIENTINFO__
#define __PASSWORDRECIPIENTINFO__


#include "cms.h"
#include "AlgorithmIdentifier.h"

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
	class PasswordRecipientInfo : public EASNWrapperTemplate<ASN1T_CMS_PasswordRecipientInfo,ASN1C_CMS_PasswordRecipientInfo>
	{
		bool					mKeyDerivationAlgorithmPresent ;

		ASN1T_CMS_CMSVersion	mVersion;
		AlgorithmIdentifier		mKeyDerivationAlgorithm;
		AlgorithmIdentifier		mKeyEncryptionAlgorithm;
		QByteArray				mEncryptedKey;

	public:
		PasswordRecipientInfo(void);
		PasswordRecipientInfo(const QByteArray & iPWRI);
		PasswordRecipientInfo(const ASN1T_CMS_PasswordRecipientInfo & iPWRI);
		PasswordRecipientInfo(const PasswordRecipientInfo& iPWRI);

		PasswordRecipientInfo & operator=(const PasswordRecipientInfo & iPWRI);
		friend bool operator==(const PasswordRecipientInfo & ,const PasswordRecipientInfo & );
		friend bool operator!=(const PasswordRecipientInfo & ,const PasswordRecipientInfo & );

		int copyFromASNObject(const ASN1T_CMS_PasswordRecipientInfo & iPWRI);
		int copyToASNObject(ASN1T_CMS_PasswordRecipientInfo& oKTRI)const;
		void freeASNObject(ASN1T_CMS_PasswordRecipientInfo & oPWRI)const;

		const bool isKeyDrvAlgPresent() const;

		const QByteArray			& getEncryptedKey()	const;
		const AlgorithmIdentifier	& getKeyEncAlg()	const;
		const AlgorithmIdentifier	& getKeyDrvAlg()	const;		
		const ASN1T_CMS_CMSVersion	& getVersion()		const;

		void setKeyDrvAlgPresent(const bool  iKDAP);

		void setVersion(ASN1T_CMS_CMSVersion iVersion);
		void setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg );
		void setKeyDrvAlg( const AlgorithmIdentifier & iKeyDrvAlg );		
		void setEncryptedKey(const QByteArray& iEncryptedKey);

		virtual ~PasswordRecipientInfo(void);
	};

}

#endif 


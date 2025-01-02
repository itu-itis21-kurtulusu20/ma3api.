
#ifndef __ECCCMSSHAREDINFO__
#define __ECCCMSSHAREDINFO__

#include "dercms.h"
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
	class Q_DECL_EXPORT ECCCMSSharedInfo  : public EASNWrapperTemplate<ASN1T_DERCMS_ECC_CMS_SharedInfo,ASN1C_DERCMS_ECC_CMS_SharedInfo>
	{
		bool				mEntityUInfoPresent;
		AlgorithmIdentifier	mKeyInfo;
		QByteArray			mEntityUInfo;
		QByteArray			mSuppPubInfo;

	public:
		ECCCMSSharedInfo(const ECCCMSSharedInfo& iSI);
		ECCCMSSharedInfo(const ASN1T_DERCMS_ECC_CMS_SharedInfo & iSI);
		ECCCMSSharedInfo(const QByteArray & iSI);
		ECCCMSSharedInfo(void);


		ECCCMSSharedInfo& operator=(const ECCCMSSharedInfo&);
		friend bool operator==(const ECCCMSSharedInfo& iRHS,const ECCCMSSharedInfo& iLHS);
		friend bool operator!=(const ECCCMSSharedInfo& iRHS, const ECCCMSSharedInfo& iLHS);


		int copyFromASNObject(const ASN1T_DERCMS_ECC_CMS_SharedInfo& iSI);
		int copyToASNObject(ASN1T_DERCMS_ECC_CMS_SharedInfo & oSI) const;
		void freeASNObject(ASN1T_DERCMS_ECC_CMS_SharedInfo& oSI)const;

		virtual ~ECCCMSSharedInfo(void);
		
		// GETTERS AND SETTERS

		bool isEntityUInfoPresent()const;

		const AlgorithmIdentifier& getKeyInfo() const;
		const QByteArray&  getEntityUInfo()const;
		const QByteArray & getSuppPubInfo() const ;

		void setKeyInfo(const AlgorithmIdentifier& iKeyInfo);
		void setEntityUInfo(const QByteArray&  iEntityUInfo);
		void setSuppPubInfo(const QByteArray & iSuppPubInfo);
			
	};
}

#endif


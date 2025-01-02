
#ifndef __SIGNEDDATA__
#define __SIGNEDDATA__

#include "SignerIdentifier.h"
#include "EncapContentInfo.h"
#include "ECertChoices.h"
#include "RevocationInfoChoice.h"
#include "SignerInfo.h"
#include "cms.h"
#include "ELogger.h"
#include "SignParam.h"


#define MAX_FILE_SIZE 10000

#define CN_SIGNEDDATA "SIGNEDDATA"

#define SD_LOGGER LOGGER(CN_SIGNEDDATA)


namespace esya
{

#define SD_DEFAULT_VERSION			CMS_CMSVersion::v1
#define SD_DEFAULT_DIGEST_ALGORITHM	ALGOS_id_sha256
	
	/**
	* \ingroup EsyaASN
	* 
	* Ýmzalý dosyalar için tanýmlanmýþ ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT SignedData  : public EASNWrapperTemplate<ASN1T_CMS_SignedData,ASN1C_CMS_SignedData>
	{
	   friend class SignerInfo;

	protected:
	
		ASN1T_CMS_CMSVersion		mVersion;
		QList<AlgorithmIdentifier>	mDigestAlgorithms;
		EncapContentInfo			mEncapContentInfo;
		QList<ECertChoices>			mCertificates;
		QList<RevocationInfoChoice>	mCRLs;
		QList<SignerInfo>			mSignerInfos;

		bool						mIsStreamed;
		QString						mPlainDataFileName;
		bool						mAyrikImza;

	public:
		
		static  const ASN1T_CMS_CMSVersion	DEFAULT_VERSION ;
		static  const ASN1TObjId			DEFAULT_DIGEST_ALGORITHM;
		
		static const AlgorithmIdentifier defaultDigestAlgorithm();
		static const ASN1T_CMS_CMSVersion defaultVersion();

		SignedData(void);
		SignedData(const QByteArray & );
		SignedData(const ASN1T_CMS_SignedData & );
		SignedData(const SignedData&);
		SignedData(const ContentInfo & iCI);


		SignedData& operator=(const SignedData& );
		friend bool operator==(const SignedData & ,const SignedData & );
		friend bool operator!=(const SignedData & ,const SignedData & );
		
		int copyFromASNObject(const ASN1T_CMS_SignedData &);
		int copyToASNObject(ASN1T_CMS_SignedData & oSD)const;
		void freeASNObject(ASN1T_CMS_SignedData & oSD)const;

		bool isStreamed()const;
		bool isAyrik()const{ return mAyrikImza;};

		const ASN1T_CMS_CMSVersion			& getVersion()const;
		const EncapContentInfo				& getEncapContentInfo()const;
		const QList<AlgorithmIdentifier>	& getDigestAlgorithms()const;
		const QList<ECertChoices>			& getCertificates()const;
		const QList<RevocationInfoChoice>	& getCRLs()const;
		const QList<SignerInfo>				& getSignerInfos()const;
			  QList<SignerInfo>				& getSignerInfos();

		const QByteArray getPlainData();

		void setVersion(const ASN1T_CMS_CMSVersion & );
		void setEncapContenInfo( const EncapContentInfo & );

		void addDigestAlgorithm(const AlgorithmIdentifier &);
		void addCertificate(const ECertChoices & );
		void addCRL(const RevocationInfoChoice &);
		void addSignerInfo(const SignerInfo& );

		int addParallelBESSigner(	const SignerParam & iSP ,const QByteArray & iPlainData);
		int addParallelBESSigners(	const SignParam & iSP , const QByteArray & iPlainData);

		virtual int addParallelSigner(const SignerInfo& iSigner , const ECertificate & iSignerCert );

				bool getCertFromCertificates(const SignerIdentifier & iSID, ECertificate & oCertificate) const;
		static	bool getCertFromCertificates(const QList<ECertificate>& ,const SignerIdentifier & iSID , ECertificate & oCertificate);


		bool verifyData(const QList<ECertificate> & iCertificates =  QList<ECertificate>() ) const;


		static SignedData fromContentInfo(const QByteArray & iCI);

		static bool imzaliMi(const QByteArray &iData);

		SignerInfo * lastSerialSigner(SignerInfo *);

	public:
		virtual ~SignedData(void);
	};

}

#endif


/**
 * EnvelopedData :
 * �ifreleme ve �ifre ��zme i�lemlerini ger�ekle�tiren s�n�f.
 * PKCS7 format�nda Dosya ve veri �ifreleme/��zme i�lemleri bu s�n�f�n sa�lad��� 
 * metodlar arac�l���yla yap�l�r.
 *  
 * Copyright (c) 2007 by <Dindar �z/ MA3 �stemci  >
 */

#ifndef __ENVELOPEDDATA__
#define __ENVELOPEDDATA__

#include "myerrors.h"
#include <iostream>
#include "OriginatorInfo.h"
#include "RecipientInfo.h"
#include "EncryptedContentInfo.h"
#include "Attribute.h"
#include "SubjectKeyIdentifier.h"
#include "OtherRecipientInfo.h"
#include "ESYAPasswordRecipientInfo.h"
#include "EsyaGroupRecipientInfo.h"
#include "EsyaFileInfoRecipientInfo.h"
#include "esya.h"
#include "ECCCMSSharedInfo.h"
#include "ECertificate.h"
#include "AnahtarBilgisi.h"

#define CN_ENVELOPEDDATA "ENVELOPEDDATA"

#define ED_LOGGER LOGGER(CN_ENVELOPEDDATA)

#define DEFAULT_RECIPIENT_VERSION CMS_CMSVersion::v0

namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* �ifreli dosyalar i�in tan�mlanm�� ASN1 wrapper s�n�f�. Detaylar i�in cms.asn d�k�man�na bak�n�z
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EnvelopedData	 : public EASNWrapperTemplate<ASN1T_CMS_EnvelopedData,ASN1C_CMS_EnvelopedData>
	{
	protected:
			bool mIsEnveloped;
   
			bool					mOriginatorInfoPresent;
			ASN1T_CMS_CMSVersion	mVersion;
			OriginatorInfo			mOriginatorInfo;
			QList<RecipientInfo>	mRecipientInfos;
			EncryptedContentInfo	mEncryptedContentInfo;
			QList<Attribute>		mUnprotectedAttrs;

	public:

		static const ASN1T_CMS_CMSVersion DEFAULT_VERSION;
		static const ASN1T_CMS_CMSVersion DEFAULT_KARI_VERSION;
		static const AlgorithmIdentifier DEFAULT_KEYENCALG;
		static const AlgorithmIdentifier DEFAULT_KEYAGREEMENTALG;
		static const AlgorithmIdentifier DEFAULT_KEYWRAPALG;

		EnvelopedData(void);
		EnvelopedData(const QByteArray &iContent, bool isEnveloped = false);
		EnvelopedData(const ASN1T_CMS_EnvelopedData & iED);
		EnvelopedData(const EnvelopedData & iED);
		EnvelopedData(	const QByteArray &iData , 
						const QList<ECertificate> &iRecipients ,
						const AlgorithmIdentifier &iContEncAlg , 
						const QByteArray &iEncryptionKey  
					 );

		EnvelopedData(	const QByteArray &iData , 
						const QString &iParola,
						const QByteArray & iEncryptionKey,
						const AlgorithmIdentifier &iContEncAlg , 
						const AlgorithmIdentifier &iKeyEncAlg , 
						const AlgorithmIdentifier &iKeyDrvAlg  
					 );


		EnvelopedData& operator=(const EnvelopedData & );
		
		friend bool operator==(const EnvelopedData & , const EnvelopedData & );
		friend bool operator!=(const EnvelopedData & , const EnvelopedData & );

		int copyFromASNObject(const ASN1T_CMS_EnvelopedData & iED);
		int copyToASNObject(ASN1T_CMS_EnvelopedData & oED)const;
		void freeASNObject(ASN1T_CMS_EnvelopedData & oED)const;

		const bool					& isEnveloped()				const;
		const bool					& isOriginatorInfoPresent()	const;
		const ASN1T_CMS_CMSVersion	& getVersion()				const;
		const OriginatorInfo		& getOriginatorInfo()		const;
		const EncryptedContentInfo	& getEncryptedContentInfo()	const;
		const QList<RecipientInfo>	& getRecipientInfos()		const;
		const QList<Attribute>		& getUnprotectedAttrs()		const;


		QList<RecipientInfo>	& getRecipientInfos();
		QList<Attribute>		& getUnprotectedAttrs();

		void setVersion( const ASN1T_CMS_CMSVersion & );
		void setOriginatorInfo (const OriginatorInfo &  );
		void setRecipientInfos( const  QList<RecipientInfo> &iRecipientInfos );
		void setEncryptedContentInfo( const EncryptedContentInfo &iECI);
		void setUnprotectedAttrs(const QList<Attribute> iUPA);

		void addRecipientInfo( const RecipientInfo &iRecipientInfo );
		void addRecipientInfos( const QList<RecipientInfo> &iRecipientInfos );

		void addUnprotectedAttrs(const QList<Attribute> iUAs);
		void addUnprotectedAttr(const Attribute iUA);

		int getRecipientIndeks(const ECertificate & iCert)const ;

		QByteArray decryptData( const QByteArray & iKey) const;		
		
		QByteArray decryptData(const ECertificate & iCert , const OzelAnahtarBilgisi& iAB ) const;
		QByteArray decryptKey(const ECertificate & iCert , const OzelAnahtarBilgisi& iAB)const;

		QByteArray decryptData(const QString &)const ;
		QByteArray decryptKey(const QString &)const ;

		QByteArray decryptData(const QList< QPair<ECertificate,OzelAnahtarBilgisi> > & iCertKeyList ) const;
		QByteArray decryptKey(const QList< QPair<ECertificate,OzelAnahtarBilgisi> > & iCertKeyList ) const;


		void removeRecipients( const QList<ECertificate> &iRecipientCerts);
		void removeRecipient( const ECertificate &iRecipientCert);

		void addRecipients( const ECertificate &iDecryptCert,const OzelAnahtarBilgisi & iOAB , const QList<ECertificate> & iCertList );
		void addRecipient(  const ECertificate &iDecryptCert,const OzelAnahtarBilgisi & iOAB , const ECertificate & iCert );

		void setRecipients( const ECertificate &iDecryptCert,const OzelAnahtarBilgisi & iOAB , const QList<ECertificate> & iCertList );

		static QByteArray encryptGroupKey( const ECertificate& iCert, const QByteArray &iKey,int iGroupIndex,int iGroupSize);
		static QByteArray encryptKey( const ECertificate& iCert, const QByteArray iKey);

		int getESYAPWRI(ESYAPasswordRecipientInfo &oEPWRI)const ;
		int getESYAFIRI(ESYAFileInfoRecipientInfo &oEFIRI)const ;

		void setESYAFIRI(const ESYAFileInfoRecipientInfo &iEFIRI) ;

		virtual ~EnvelopedData(void);



		static ECCCMSSharedInfo cmsSharedInfoOlustur(const QByteArray & iUKM, const AlgorithmIdentifier & iKEAlg);

		static QList<RecipientInfo> buildEGRIs(const QList<ECertificate> &iGroupCertList,const QByteArray &iEncryptionKey, int iGID);
		static QList<RecipientInfo> buildRecipientInfos(const QList<ECertificate> &iRecipients , const QByteArray &iEncryptionKey  );
		static bool		buildRecipientInfo(const ECertificate &iRecipient , const QByteArray &iEncryptionKey ,RecipientInfo&  iRI);

		static bool buildEGRI(const ECertificate& iCert,const QByteArray iEncryptionKey,int iGID,int iGroupIndex,int iGroupSize,EsyaGroupRecipientInfo& oEGRI);
		static bool  buildKARI(const ECertificate& iCert,const QByteArray& iCEK,KeyAgreeRecipientInfo& iKARI);
	
};


} 

#endif


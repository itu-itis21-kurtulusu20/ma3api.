
#ifndef __SIGNERINFO__
#define __SIGNERINFO__


#include <QByteArray>
#include <QString>
#include "ESeqOfList.h"
#include "AlgorithmIdentifier.h"
#include "SignerIdentifier.h"
#include "Attribute.h"
#include "ECertChoices.h"
#include "ASN1CUTCTime.h"
#include "algorithms.h"
#include "SignerParam.h"
#include "EASNWrapper.h"

#define CN_SIGNERINFO "SIGNERINFO"

#define SI_LOGGER LOGGER(CN_SIGNERINFO)

#define UTC_FORMAT "yyMMddhhmmssZ"

#define DEFAULT_SIGNERINFO_VERSION  CMS_CMSVersion::v1

namespace esya 
{

	class SignedData;
	class StreamedSignedData;

	/**
	* \brief
	* CMS �mzac� Data yap�s�
	* 
	*/
	class Q_DECL_EXPORT SignerInfo  : public EASNWrapperTemplate<ASN1T_CMS_SignerInfo,ASN1C_CMS_SignerInfo>
	{
		friend class SignedData;

		bool				mSignatureChanged;
		SignerInfo*			mParent;
		SignedData*			mParentData;

		int					mVersion;
		SignerIdentifier	mSID;
		AlgorithmIdentifier	mDigestAlgorithm;
		AlgorithmIdentifier	mSignatureAlgorithm;
		QList<Attribute>	mSignedAttrs;
		QList<Attribute>	mUnsignedAttrs;
	  
		QByteArray			mSignature;	

		QList<SignerInfo>	mSigners;

		void _fillSerialSigners();
		
		Attribute&	_getCounterSignatureAttribute();
        int			_updateSignature (BaseSigner* signer, const ECertificate & iSignerCert, bool isSerial);
		int			_updateSigner();

		bool _verifyParallel(bool isStreamed, const ECertificate &iCert )const;
		bool _verifySerial(const ECertificate &iCert )const;
	
	public:

		static const ASN1T_CMS_CMSVersion DEFAULT_VERSION ;

		SignerInfo(const SignerInfo & );
		SignerInfo(const ASN1T_CMS_SignerInfo & iSignerInfo,SignerInfo * iParent = NULL, SignedData*  iParentData = NULL);
		SignerInfo(const QByteArray &,SignerInfo * iParent = NULL,SignedData*  iParentData = NULL);
		SignerInfo(SignerInfo * iParent = NULL, SignedData* iParentData = NULL);
		

		SignerInfo & operator=(const SignerInfo & );
		friend bool operator==(const SignerInfo & iRHS, const SignerInfo & iLHS);
		friend bool operator!=(const SignerInfo & iRHS, const SignerInfo & iLHS);

		int copyFromASNObject(const ASN1T_CMS_SignerInfo& );
		int copyToASNObject(ASN1T_CMS_SignerInfo &) const;
		void freeASNObject(ASN1T_CMS_SignerInfo & oSignerInfo)const;

		int copySignerInfos(const ASN1T_CMS_SignerInfos & iSignerInfos, QList<SignerInfo>& oList,SignerInfo* iParent = NULL ,SignedData* iParentData = NULL);
		int copySignerInfos(const QList<SignerInfo> iList ,ASN1T_CMS_SignerInfos & oSignerInfos);	
		int copySignerInfos(const QByteArray & iASNBytes,  QList<SignerInfo>& oList , SignerInfo* iParent = NULL ,SignedData* iParentData = NULL);
		int copySignerInfos(const QList<SignerInfo> iList ,QByteArray & oASNBytes);

		const int& getVersion() const;
		const SignerIdentifier& getSID()const ;
		const AlgorithmIdentifier& getDigestAlgorithm() const;
		const AlgorithmIdentifier& getSignatureAlgorithm() const;
		const QList<Attribute> &  getSignedAttributes() const ;
		const QList<Attribute> &  getUnsignedAttributes() const ;
		const QByteArray& getSignature()const;

		void setVersion(const int);
		void setSID(const SignerIdentifier&);
		void setDigestAlgorithm(const AlgorithmIdentifier&);
		void setSignatureAlgorithm(const AlgorithmIdentifier&);
		void setSignature(const QByteArray &);

		int  addSignedAttribute( const QByteArray &iAttrBytes , const ASN1TObjId &iAttrType);
		int  addUnsignedAttribute( const QByteArray &iAttrBytes , const ASN1TObjId &iAttrType);
		int  addUnsignedAttributes( const QList< QPair<ASN1TObjId,QByteArray> >& iSignedAttributes);

		void addSignedAttribute( const Attribute& );
		void addUnsignedAttribute(const Attribute&);

		int addContentTypeSignedAttr();
		int addMsgDigestSignedAttr( const QByteArray& );		
		int addMsgDigestSignedAttr( const QString& );		
		int addMsgDigestSignedAttr( ASN1T_CMS_Digest& );		
		int addTimeStampUnsignedAttr( const QByteArray& );		
		int addTimeSignedAttr();
		int addSigningCertSignedAttr(const ECertificate &iSignerCert);
        int addSigningCertV2SignedAttr(const ECertificate &iSignerCert);
		int addSMIMEEncryptionKeyPreferenceSignedAttribute(const ECertificate& iEncCert);
		int addSMIMECapabilitiesSignedAttribute();
 
	
		QList<Attribute> getSignedAttributes(const ASN1TObjId & ) const;
		QList<Attribute> getUnsignedAttributes(const ASN1TObjId & ) const ;

		QList<Attribute> &  getUnsignedAttributes();

		QByteArray getMessageDigest() const;
		bool hasDigest(const QByteArray& iDigest)const;
		QByteArray getSignedAttrsBytes()const;

		QDateTime getSigningTime() const;

		const	QList<SignerInfo> &getSigners()const ;
				QList<SignerInfo> &getSigners();

		const	SignerInfo* getParent() const;
				SignerInfo* getParent();
		
		const	SignedData* getParentData() const;
				SignedData* getParentData();

		void	setParent(SignerInfo* iParent);
		void	setParentData(SignedData* iParentData);


		int addSerialSigner( const SignerInfo & iSigner, const ECertificate & iCert);

        int addSerialBESSigner(	int  iVersion,
                                BaseSigner *signer,
								const ECertificate & iCert ,
								const AlgorithmIdentifier& iDigestAlg );


		static SignerInfo * constructBESSigner(	const SignerParam & iSP,  
												const bool isSerial , 
												const bool isStreamed , 
												SignerInfo * iParentSigner , 
												SignedData *iParentData);

		bool verifySigner(const QList<ECertificate>& iCertificates ,bool isStreamed,  bool iVerifyChildren ) const;
		bool verifyChildren(const QList<ECertificate>& iCertificates ,bool isStreamed )const;

	public:
		~SignerInfo(void);
	};

}

#endif


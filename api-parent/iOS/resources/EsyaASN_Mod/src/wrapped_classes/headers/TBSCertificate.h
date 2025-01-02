
#ifndef __TBSCERTIFICATE__
#define __TBSCERTIFICATE__

//#include "cms.h"
#include "Name.h"
#include "Extension.h"
#include "ETime.h"
#include "AlgorithmIdentifier.h"
#include "SubjectPublicKeyInfo.h"

#include "SerialNumber.h"


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
	class Q_DECL_EXPORT TBSCertificate  : public EASNWrapperTemplate<ASN1T_EXP_TBSCertificate,ASN1C_EXP_TBSCertificate>
	{
	   bool mIssuerUniqueIDPresent ;
	   bool mSubjectUniqueIDPresent ;
	   bool mExtensionsPresent ;
	   
	   int mVersion;
	   SerialNumber mSerialNumber;
	   AlgorithmIdentifier mSignature;
	   Name mIssuer;
	   Name mSubject;
	   //ASN1T_EXP_Validity validity;
	   ETime mNotBefore;
	   ETime mNotAfter;

	   SubjectPublicKeyInfo mSubjectPublicKeyInfo;
	   EBitString	mIssuerUniqueID;
	   EBitString	mSubjectUniqueID;
	   
	   QList<Extension> mExtensions;

	public :
		TBSCertificate(void);
		TBSCertificate(const QByteArray & );
		TBSCertificate(const ASN1T_EXP_TBSCertificate & );
		TBSCertificate(const TBSCertificate&);

		TBSCertificate & operator=(const TBSCertificate & );
		friend bool operator==(const TBSCertificate & ,const TBSCertificate & );
		friend bool operator!=(const TBSCertificate & ,const TBSCertificate & );
		
		int copyFromASNObject(const ASN1T_EXP_TBSCertificate &);
		int copyToASNObject(ASN1T_EXP_TBSCertificate & oTBSCertificate)const;
		void freeASNObject(ASN1T_EXP_TBSCertificate & oTBSCertificate)const;

		virtual ~TBSCertificate();

		// GETTERS AND SETTERS

		bool isIssuerUniqueIDPresent()const ;
		bool isSubjectUniqueIDPresent()const ;
		bool isExtensionsPresent() const;
	   
		int getVersion()const;
		const SerialNumber& getSerialNumber()const;
		const AlgorithmIdentifier & getSignature()const;
		const Name &getIssuer()const;
		const Name &getSubject()const;
		   
		//ASN1T_EXP_Validity validity;
		const ETime &getNotBefore()const;
		const ETime &getNotAfter()const;
		
		const SubjectPublicKeyInfo &getSubjectPublicKeyInfo()const;

		const EBitString&	getIssuerUniqueID()const;
		const EBitString&	getSubjectUniqueID()const;
		const QList<Extension>& getExtensions()const;

		void  setSerialNumber(const SerialNumber & );

		void setVersion(int iVersion);
		void setSubject(const Name & iSubject);
		void setIssuer(const Name & iIssuer);
		void setExtensions(const QList<Extension>& iExtensions);
		void setSubjectPublicKeyInfo(const SubjectPublicKeyInfo & iSPKI);
		void setSignature(const AlgorithmIdentifier & iSignature);

		void setNotBefore(const ETime &);
		void setNotAfter(const ETime &);

		int getExtension(ASN1OBJID iExtnID, Extension& oExtension )const;
	};

}

#endif


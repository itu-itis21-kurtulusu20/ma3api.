#ifndef __TBSCERTLIST__
#define __TBSCERTLIST__

#include "cms.h"
#include "ortak.h"
#include "AttributeValue.h"
#include "Name.h"
#include "Extension.h"
#include "ETime.h"
#include "AlgorithmIdentifier.h"
#include "RevokedCertificatesElement.h"
//#include "ASN1Stream.h"
#include "OSRTStream.h"


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
	class Q_DECL_EXPORT TBSCertList  : public EASNWrapperTemplate<ASN1T_EXP_TBSCertList,ASN1C_EXP_TBSCertList>
	{
		friend class ECertificateList;	   
		
	   bool		mVersionPresent;
	   bool		mNextUpdatePresent;
	   int		mVersion;
	   Name		mIssuer;
	   ETime	mThisUpdate;
	   ETime	mNextUpdate;
	   AlgorithmIdentifier	mSignature;
	   QList<Extension>	mCRLExtensions;
	   QList<RevokedCertificatesElement> mRevokedCertificates;


	public:

		TBSCertList();
		TBSCertList(const ASN1T_EXP_TBSCertList & );
		TBSCertList(const TBSCertList & );
		TBSCertList(const  QByteArray &);

		TBSCertList& operator=(const TBSCertList& );
		friend bool operator==( const TBSCertList & iRHS , const TBSCertList& iLHS);
		friend bool operator!=( const TBSCertList & iRHS , const TBSCertList& iLHS);

		int copyFromASNObject(const ASN1T_EXP_TBSCertList & iTCL);
		int copyToASNObject(ASN1T_EXP_TBSCertList & ) const;	
		void freeASNObject(ASN1T_EXP_TBSCertList & )const;

		virtual ~TBSCertList(void);

		// GETTERS AND SETTERS

		const bool& isVersionPresent()const;
		const bool& isNextUpdatePresent()const;
		const int&		getVersion()const ;
		const Name& getIssuer()const;
		const ETime&	getThisUpdate()const;
		const ETime&	getNextUpdate()const;
		const AlgorithmIdentifier&	getSignature()const ;
		const QList<Extension>&		getCRLExtensions()const;
		const QList<RevokedCertificatesElement> & getRevokedCertificates()const;

		void setVersion(const int iVersion);
		void setVersionPresent(const bool iVersionPresent);
		void setIssuer(const Name & iIssuer);
		void setThisUpdate(const ETime & iThisUpdate);
		void setNextUpdate(const ETime & iNextUpdate);
		void setNextUpdatePresent(const bool & iNextUpdatePresent);
		void setSignature(const AlgorithmIdentifier & iSignature);
		void setCRLExtensions(const QList<Extension>& iCRLExtensions);
		void setRevokedCertificates(const QList<RevokedCertificatesElement> & iRevokedCertificates);

		int getExtension(ASN1OBJID iExtnID, Extension& oExtension )const;

		int readFromStream(OSRTStream * pStream );
	};

}

#endif


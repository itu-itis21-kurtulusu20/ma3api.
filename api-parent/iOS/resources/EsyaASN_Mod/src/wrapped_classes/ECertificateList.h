
#ifndef __ECERTIFICATELIST__
#define __ECERTIFICATELIST__

#include "TBSCertList.h"
#include "EBitString.h"
#include "CertificateIssuer.h"
#include "EASNStreamingUtils.h"



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
	class ECertificate;

	class Q_DECL_EXPORT ECertificateList  : public EASNWrapperTemplate<ASN1T_EXP_CertificateList,ASN1C_EXP_CertificateList>
	{
	protected:

		TBSCertList			mTBSCertList;
		EBitString			mSignature;
		AlgorithmIdentifier mSignatureAlgorithm;

		QByteArray	mRevokedData;	
		QByteArray	mCRLData;
		bool		mFullyLoaded;

	public:

		ECertificateList(void);
		ECertificateList(const QByteArray & );
		ECertificateList(const ASN1T_EXP_CertificateList & );
		ECertificateList(const ECertificateList&);
		ECertificateList(const QString&);

		ECertificateList & operator=(const ECertificateList & );
		Q_DECL_EXPORT friend bool operator==(const ECertificateList & ,const ECertificateList & );
		Q_DECL_EXPORT friend bool operator!=(const ECertificateList & ,const ECertificateList & );
		

		int copyFromASNObject(const ASN1T_EXP_CertificateList &);
		int copyToASNObject(ASN1T_EXP_CertificateList & oCertificateList)const;
		void freeASNObject(ASN1T_EXP_CertificateList & oCertificateList)const;

		int copyCRL(const ASN1T_PKCS7_CertificateRevocationLists & iCRL, QList<ECertificateList>& oList);
		int copyCRL(const QList<ECertificateList> iList ,ASN1T_PKCS7_CertificateRevocationLists & oCRL);	

		virtual~ECertificateList(void);

		// GETTERS AND SETTERS

		const TBSCertList & getTBSCertList() const ;
		const AlgorithmIdentifier & getSignatureAlgorithm() const;
		const EBitString & getSignature()const ;

		int indexOf(const ECertificate& iCert,const QDateTime& iRevocationTime = QDateTime());

		QString getCRLNumber()const;

		bool isIndirectCRL()const;

		CertificateIssuer getCertificateIssuer(const RevokedCertificatesElement & iRCE);


		const QList<RevokedCertificatesElement>& getRevokedCertificates();

		QString toString() const;

		void lazyLoad(const QByteArray & );
		void fullyLoad();
		//const QByteArray&  getCRLData()const;
		bool isLazyLoaded()const;

		QByteArray getTBSBytes()const;

	private:
		void _loadIssuer(EASNFileInputStream*);
		void _loadVersion(EASNFileInputStream* );
		void _loadSignature(EASNFileInputStream* pIN);
		void _loadThisUpdate(EASNFileInputStream* pIN);
		void _loadNextUpdate(EASNFileInputStream* pIN);
		void _loadCRLExtensions(EASNFileInputStream* pIN);
		void _loadCRLSignature(EASNFileInputStream* pIN);
		void _loadCRLSignatureAlgorithm(EASNFileInputStream* pIN);

	};

}
#endif


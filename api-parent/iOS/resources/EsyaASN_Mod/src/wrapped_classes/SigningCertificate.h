#ifndef __SIGNINGCERTIFICATE__
#define __SIGNINGCERTIFICATE__

#include "ESSCertID.h"
#include "PolicyInformation.h"

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
	class Q_DECL_EXPORT SigningCertificate  : public EASNWrapperTemplate<ASN1T_PKCS7_SigningCertificate,ASN1C_PKCS7_SigningCertificate>
	{
		bool						mPoliciesPresent;
		QList<PolicyInformation>	mPolicies;
		QList<ESSCertID>			mCerts;
			
	public:
		SigningCertificate(void);
		SigningCertificate( const ASN1T_PKCS7_SigningCertificate & iSC);
		SigningCertificate( const SigningCertificate &iSC);
		SigningCertificate( const QByteArray &iSC);

		SigningCertificate & operator=(const SigningCertificate&);
		Q_DECL_EXPORT friend bool operator==(const SigningCertificate& iRHS,const SigningCertificate& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const SigningCertificate& iRHS, const SigningCertificate& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_SigningCertificate & iSC);
		int copyToASNObject(ASN1T_PKCS7_SigningCertificate & oSC) const;
		void freeASNObject(ASN1T_PKCS7_SigningCertificate& oSC)const;

		virtual ~SigningCertificate(void);

		// GETTERS AND SETTERS
		
		bool isPoliciesPresent()const;
		const QList<PolicyInformation> & getPolicies() const ;	
		const QList<ESSCertID> & getCerts() const ;	

		void setPolicies(const QList<PolicyInformation> & );	
		void setCerts(const QList<ESSCertID> & ) ;	

		void appendPolicy(const PolicyInformation & );	
		void appendCert(const ESSCertID& ) ;	


	};

}

#endif


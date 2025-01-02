#ifndef __SIGNINGCERTIFICATEV2__
#define __SIGNINGCERTIFICATEV2__

#include "ESSCertIDv2.h"
#include "PolicyInformation.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
    * \author dindaro and suleymanu
	*
	*/
    class Q_DECL_EXPORT SigningCertificateV2  : public EASNWrapperTemplate<ASN1T_PKCS7_SigningCertificateV2,ASN1C_PKCS7_SigningCertificateV2>
	{
		bool						mPoliciesPresent;
		QList<PolicyInformation>	mPolicies;
        QList<ESSCertIDv2>			mCerts;
			
	public:
        SigningCertificateV2(void);
        SigningCertificateV2( const ASN1T_PKCS7_SigningCertificateV2 & iSCv2);
        SigningCertificateV2( const SigningCertificateV2 &iSCv2);
        SigningCertificateV2( const QByteArray &iSCv2);

        SigningCertificateV2 & operator=(const SigningCertificateV2 &);
        Q_DECL_EXPORT friend bool operator==(const SigningCertificateV2& iRHS, const SigningCertificateV2& iLHS);
        Q_DECL_EXPORT friend bool operator!=(const SigningCertificateV2& iRHS, const SigningCertificateV2& iLHS);

        int copyFromASNObject(const ASN1T_PKCS7_SigningCertificateV2 & iSCv2);
        int copyToASNObject(ASN1T_PKCS7_SigningCertificateV2 & oSCv2) const;
        void freeASNObject(ASN1T_PKCS7_SigningCertificateV2 & oSCv2)const;

        virtual ~SigningCertificateV2(void);

		// GETTERS AND SETTERS
		
		bool isPoliciesPresent()const;
		const QList<PolicyInformation> & getPolicies() const ;	
        const QList<ESSCertIDv2> & getCerts() const ;

		void setPolicies(const QList<PolicyInformation> & );	
        void setCerts(const QList<ESSCertIDv2> & );

		void appendPolicy(const PolicyInformation & );	
        void appendCert(const ESSCertIDv2& );


	};

}

#endif


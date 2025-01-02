
#ifndef __ECERTCHOICES__
#define __ECERTCHOICES__

#include "ECertificate.h"

namespace esya
{

	enum CertType { T_Certificate = T_CMS_CertificateChoices_certificate , 
		T_ExtCert	  = T_CMS_CertificateChoices_extendedCertificate ,
		T_AttrCertV1  = T_CMS_CertificateChoices_v1AttrCert ,
		T_AttrCertV2  = T_CMS_CertificateChoices_v2AttrCert,
		T_Other		  = T_CMS_CertificateChoices_other};

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT ECertChoices  : public EASNWrapperTemplate<ASN1T_CMS_CertificateChoices,ASN1C_CMS_CertificateChoices>
	{
		CertType		mType;
		ECertificate	mCertificate;

	public:

		ECertChoices(void);
		ECertChoices(const QByteArray & );
		ECertChoices(const ASN1T_CMS_CertificateChoices & );
		ECertChoices(const ECertChoices&);
		ECertChoices(const ECertificate&);

		ECertChoices & operator=(const ECertChoices & );
		Q_DECL_EXPORT friend bool operator==(const ECertChoices & ,const ECertChoices & );
		Q_DECL_EXPORT friend bool operator!=(const ECertChoices & ,const ECertChoices & );


		int copyFromASNObject(const ASN1T_CMS_CertificateChoices & iCC);
		int copyToASNObject(ASN1T_CMS_CertificateChoices & oCC)const;
		void freeASNObject(ASN1T_CMS_CertificateChoices & oCC)const;

		int copyCS(const ASN1T_CMS_CertificateSet & iCS, QList<ECertChoices>& oList);
		int copyCS(const QList<ECertChoices> & iList ,ASN1T_CMS_CertificateSet& oCS);	
		int copyCS(const QByteArray & iASNBytes, QList<ECertChoices>& oList);
		int copyCS(const QList<ECertChoices>& oList, QByteArray & oASNBytes);

		virtual~ECertChoices(void);

		const CertType & getType() const ;
		const ECertificate & getCertificate() const;

	};

}

#endif


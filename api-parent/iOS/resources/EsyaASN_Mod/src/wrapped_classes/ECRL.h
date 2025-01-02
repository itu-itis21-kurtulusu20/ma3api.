
#ifndef __ECRL__
#define __ECRL__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "ECertificateList.h"


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
	class Q_DECL_EXPORT ECRL  : public EASNWrapperTemplate<ASN1T_PKCS7_CertificateRevocationLists,ASN1C_PKCS7_CertificateRevocationLists>
	{
		QList<ECertificateList> mCRL;

	public:

		ECRL(const ECRL &);
		ECRL(const ASN1T_PKCS7_CertificateRevocationLists & iCRL);
		ECRL(const QByteArray & );
		ECRL(const QString & );
		ECRL(void);
		

		ECRL& operator=(const ECRL&);
		friend bool operator==(const ECRL& iRHS, const ECRL& iLHS);
		friend bool operator!=(const ECRL& iRHS, const ECRL& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_CertificateRevocationLists& iCRL);
		int copyToASNObject(ASN1T_PKCS7_CertificateRevocationLists & oCRL) const;
		void freeASNObject(ASN1T_PKCS7_CertificateRevocationLists & oCRL)const;

		virtual ~ECRL(void);

		const QList<ECertificateList> &getCRL()const ;
	};

} 
#endif


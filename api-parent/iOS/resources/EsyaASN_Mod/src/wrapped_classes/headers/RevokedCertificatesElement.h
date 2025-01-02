
#ifndef __REVOKEDCERTIFICATESELEMENT__
#define __REVOKEDCERTIFICATESELEMENT__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "ETime.h"
#include "Extension.h"
#include "Implicit.h"
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
	class Q_DECL_EXPORT RevokedCertificatesElement  : public EASNWrapperTemplate<ASN1T_EXP_TBSCertList_revokedCertificates_element,ASN1C_EXP_TBSCertList_revokedCertificates_element>
	{
		QString			 mUserCertificate;
		ETime			 mRevocationDate;
		QList<Extension> mCRLEntryExtensions;

	public:

		RevokedCertificatesElement(const RevokedCertificatesElement &);
		RevokedCertificatesElement(const ASN1T_EXP_TBSCertList_revokedCertificates_element &);
		RevokedCertificatesElement(const QByteArray & );
		RevokedCertificatesElement(void);


		RevokedCertificatesElement & operator=(const RevokedCertificatesElement& iRCE);
		friend bool operator==(const RevokedCertificatesElement & iRHS,const RevokedCertificatesElement & iLHS);
		friend bool operator!=(const RevokedCertificatesElement & iRHS, const RevokedCertificatesElement & iLHS);

		int copyFromASNObject(const ASN1T_EXP_TBSCertList_revokedCertificates_element & );
		int copyToASNObject(ASN1T_EXP_TBSCertList_revokedCertificates_element & oRCE) const;
		void freeASNObject(ASN1T_EXP_TBSCertList_revokedCertificates_element & oRCE)const;

		int copyRCEs(const ASN1T_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element & iRCEs, QList<RevokedCertificatesElement>& oList);
		int copyRCEs(const QList<RevokedCertificatesElement> iList ,ASN1T_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element & oRCEs);	
		int copyRCEs(const QByteArray& iList ,QList<RevokedCertificatesElement> & oRCEs);

		virtual ~RevokedCertificatesElement(void);

		// GETTERS AND SETTERS

		const QString& getUserCertificate() const;
		const ETime& getRevocationDate() const;
		const QList<Extension>& getCRLEntryExtensions() const ;

		void setUserCertificate(const QString&);
		void setRevocationDate(const ETime&);
		void setCRLEntryiExtensions(const QList<Extension>&);

		int silNedeniAl() const;

		int readFromStream(OSRTStream * pStream );
		
	};

}

#endif


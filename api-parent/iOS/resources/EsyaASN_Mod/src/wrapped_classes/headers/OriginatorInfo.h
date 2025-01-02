#ifndef __ORIGINATORINFO__
#define __ORIGINATORINFO__

#include "ECertChoices.h"
#include "RevocationInfoChoice.h"

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
	class Q_DECL_EXPORT OriginatorInfo  : public EASNWrapperTemplate<ASN1T_CMS_OriginatorInfo,ASN1C_CMS_OriginatorInfo>
	{
		QList<ECertChoices>			mCerts;
		QList<RevocationInfoChoice> mCRLs;

	public:

		OriginatorInfo(void);
		OriginatorInfo( const ASN1T_CMS_OriginatorInfo &iORI);
		OriginatorInfo( const QByteArray &iORI);
		OriginatorInfo( const OriginatorInfo& iORI);

		OriginatorInfo & operator=(const OriginatorInfo & iORI);
		Q_DECL_EXPORT friend bool operator==(const OriginatorInfo & ,const OriginatorInfo & );
		Q_DECL_EXPORT friend bool operator!=(const OriginatorInfo & ,const OriginatorInfo & );
		

		int copyFromASNObject(const ASN1T_CMS_OriginatorInfo & iORI);
		int copyToASNObject(ASN1T_CMS_OriginatorInfo & oORI)const;
		void freeASNObject(ASN1T_CMS_OriginatorInfo & oORI)const;

		virtual ~OriginatorInfo(void);

		// GETTERS AND SETTERS

		const QList<ECertChoices>			& getCerts()const;
		const QList<RevocationInfoChoice>	& getCRLs()const;

		void setCerts(const QList<ECertChoices> & iCerts );
		void setCRLs(const QList<RevocationInfoChoice> & iCRLs );		
		
		void addCert(const ECertChoices & iCert);
		void addCRL(const RevocationInfoChoice & iCRL);

	};

}
#endif



#ifndef __ENCAPCONTENTINFO__
#define __ENCAPCONTENTINFO__

#include "pkcs12.h"
#include "ortak.h"
#include "cms.h"
#include "EException.h"
#include "EASNWrapperTemplate.h"

namespace esya
{
	/**
	* \ingroup EsyaCMS
	* 
	* ASN1 wrapper sýnýfý. Detaylar için cms.asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EncapContentInfo  : public EASNWrapperTemplate<ASN1T_CMS_EncapsulatedContentInfo,ASN1C_CMS_EncapsulatedContentInfo>
	{

		bool					mEContentPresent;
		ASN1T_CMS_ContentType	mEContentType;
		QByteArray				mEContent; 

	public:

		EncapContentInfo(void);
		EncapContentInfo(const QByteArray&);
		EncapContentInfo(const ASN1T_CMS_EncapsulatedContentInfo & );
		EncapContentInfo(const EncapContentInfo & );
		EncapContentInfo(const QByteArray & ,  const ASN1T_CMS_ContentType &);
		EncapContentInfo(const ASN1T_CMS_ContentType &);
		EncapContentInfo(ASN1T_PKCS7_Data & iData);


		EncapContentInfo& operator=(const EncapContentInfo & );
		
		int copyFromASNObject(const ASN1T_CMS_EncapsulatedContentInfo &);
		int copyToASNObject(ASN1T_CMS_EncapsulatedContentInfo &) const;
		
		void freeASNObject(ASN1T_CMS_EncapsulatedContentInfo &)const;


		QByteArray getEContent()const ;
		const ASN1T_CMS_ContentType & getEContentType()const ;
		bool getEContentPresent()const ;


		void setEContentPresent(const bool );
		void setEContent(const  QByteArray &iData );
		void setEContentType(const  ASN1T_CMS_ContentType & iEContenType );


	public:
		virtual ~EncapContentInfo(void);
	};

}

#endif


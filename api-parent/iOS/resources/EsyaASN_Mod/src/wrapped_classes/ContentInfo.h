
#ifndef __CONTENTINFO__
#define __CONTENTINFO__

#include "pkcs12.h"
#include "cms.h"
#include "ortak.h"
#include "pkcs7.h"
#include "EException.h"

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
	class Q_DECL_EXPORT ContentInfo : public EASNWrapperTemplate<ASN1T_PKCS7_ContentInfo,ASN1C_PKCS7_ContentInfo>
	{

		ASN1T_PKCS7_ContentType mContentType;
		
		QByteArray mContent; 

	public:

		ContentInfo(void);
		ContentInfo(const QByteArray&);
		ContentInfo(const ASN1T_PKCS7_ContentInfo & );
		ContentInfo(const ContentInfo & );
		ContentInfo(const QByteArray & ,  const ASN1T_PKCS7_ContentType &);
		ContentInfo(ASN1T_PKCS7_Data & iData);
		ContentInfo(const QString & iContentFile);

		ContentInfo& operator=(const ContentInfo& iCI);
		Q_DECL_EXPORT friend bool operator==( const ContentInfo& iRHS, const ContentInfo& iLHS);
		Q_DECL_EXPORT friend bool operator!=( const ContentInfo& iRHS, const ContentInfo& iLHS);
		
		int copyFromASNObject(const ASN1T_PKCS7_ContentInfo &);
		int copyFromASNObject(const ASN1T_CMS_ContentInfo &);
		int copyToASNObject(ASN1T_PKCS7_ContentInfo &) const;
		int copyToASNObject(ASN1T_CMS_ContentInfo &) const;
		void freeASNObject(ASN1T_PKCS7_ContentInfo &)const;
		void freeASNObject(ASN1T_CMS_ContentInfo &)const;

		void freeContenInfos(ASN1TPDUSeqOfList & oCIs);
		int  copyContentInfos(const QList<ContentInfo> iList ,ASN1TPDUSeqOfList & oCIs);
		int	copyContentInfos(const ASN1TPDUSeqOfList & iCIs, QList<ContentInfo>& oList);

		virtual~ContentInfo(void);

		// GETTERS AND SETTERS

		const QByteArray& getContent()const ;
		const ASN1T_PKCS7_ContentType & getContentType()const ;

		void setContent(const  QByteArray &iData );
		void setContentType(const  ASN1T_PKCS7_ContentType & iContenType );


	};

}

#endif


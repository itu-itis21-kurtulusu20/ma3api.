#ifndef __ENCRYPTEDDATA__
#define __ENCRYPTEDDATA__

#include "pkcs12.h"
#include "ortak.h"
#include "EException.h"
#include "ContentInfo.h"
#include "cms.h"
#include <QList>
#include "Attribute.h"
#include "EncryptedContentInfo.h"

namespace esya
{

	/**
	* \ingroup EsyaCMS
	* 
	* ASN1 wrapper sýnýfý. Detaylar için cms.asn dökümanýna bakýnýz
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EncryptedData  : public EASNWrapperTemplate<ASN1T_CMS_EncryptedData,ASN1C_CMS_EncryptedData>
	{
		EncryptedContentInfo mEncryptedContentInfo;
		ASN1T_CMS_CMSVersion mVersion;
		QList<Attribute> mUnprotectedAttributes;


	public:
		EncryptedData(void);
		EncryptedData( const ASN1T_CMS_EncryptedData &);
		EncryptedData( const EncryptedData &);
		EncryptedData( const QByteArray &);

		EncryptedData & operator=(const EncryptedData&);
		friend bool operator==(const EncryptedData& iRHS,const EncryptedData& iLHS);
		friend bool operator!=(const EncryptedData& iRHS, const EncryptedData& iLHS);

		int copyFromASNObject(const ASN1T_CMS_EncryptedData & iEncData);
		int copyToASNObject(ASN1T_CMS_EncryptedData & oEncData) const;
		void freeASNObject(ASN1T_CMS_EncryptedData & oEncData)const;

		EncryptedContentInfo getEncryptedContentInfo() const ;	
		const ASN1T_CMS_CMSVersion & getVersion()const ;
		const QList<Attribute> & getUnprotectedAttributes()const ;
	
		void setEncryptedContentInfo(const EncryptedContentInfo &);
		void setVersion(const int);
		void setUnprotectedAttributes(const QList<Attribute> & );

		QByteArray getPlainData();

		virtual ~EncryptedData(void);
	};

}

#endif
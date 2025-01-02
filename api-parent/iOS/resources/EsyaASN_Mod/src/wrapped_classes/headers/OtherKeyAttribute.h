#ifndef __OTHERKEYATTRIBUTE__
#define __OTHERKEYATTRIBUTE__

#include "pkcs12.h"
#include "ortak.h"
#include "ContentInfo.h"
#include "ESeqOfList.h"
#include "algorithms.h"
#include "myasndefs.h"

#define NULL_PARAMS QByteArray("\5\0",2)

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
	class Q_DECL_EXPORT OtherKeyAttribute : public EASNWrapperTemplate<ASN1T_CMS_OtherKeyAttribute,ASN1C_CMS_OtherKeyAttribute>
	{
		bool mKeyAttrPresent;

		QByteArray mKeyAttribute;
		ASN1TObjId mKeyAttrID;


	public:
		OtherKeyAttribute(void);
		OtherKeyAttribute(const QByteArray & );
		OtherKeyAttribute(const QByteArray & , const ASN1TObjId &);
		OtherKeyAttribute(ASN1TObjId & );
		OtherKeyAttribute(const ASN1T_CMS_OtherKeyAttribute & );
		OtherKeyAttribute(const OtherKeyAttribute& );

		OtherKeyAttribute& operator=(const OtherKeyAttribute& );
		friend bool operator==(const OtherKeyAttribute & iRHS, const OtherKeyAttribute& iLHS);
		friend bool operator!=(const OtherKeyAttribute & iRHS, const OtherKeyAttribute& iLHS);

		int copyFromASNObject(const ASN1T_CMS_OtherKeyAttribute & ) ;
		int copyToASNObject(ASN1T_CMS_OtherKeyAttribute & oAlgorithmIdentifier) const;
		void freeASNObject(ASN1T_CMS_OtherKeyAttribute & oOtherKeyAttribute)const;

		virtual ~OtherKeyAttribute(void);

		// GETTERS AND SETTERS

		bool isKeyAttrPresent()const;

		const ASN1TObjId&  getKeyAttrID() const ;
		const QByteArray&  getKeyAttribute()const ;
		
		void setKeyAttrPresent(bool);		
		void setKeyAttrID(const ASN1TObjId& );
		void setKeyAttribute(const QByteArray& );
	};

}

#endif

#ifndef __ATTRIBUTETYPEANDVALUE__
#define __ATTRIBUTETYPEANDVALUE__

#include "cms.h"
#include "EException.h"
#include "AttributeValue.h"
#include "EASNToStringUtils.h"
#include "ortak.h"

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
	class Q_DECL_EXPORT AttributeTypeAndValue : public EASNWrapperTemplate<ASN1T_EXP_AttributeTypeAndValue,ASN1C_EXP_AttributeTypeAndValue>
	{
		
		ASN1T_EXP_AttributeType mAttributeType;

		AttributeValue mAttributeValue;

	public:

		AttributeTypeAndValue();
		AttributeTypeAndValue(const ASN1T_EXP_AttributeType & , const AttributeValue &);
		AttributeTypeAndValue(const ASN1T_EXP_AttributeTypeAndValue & iAttribute);
		AttributeTypeAndValue(const AttributeTypeAndValue & iAttributeTypeAndValue);
		AttributeTypeAndValue(const  QByteArray &iAttributeTypeAndValue);


		AttributeTypeAndValue & operator=(const AttributeTypeAndValue& iAttributeTypeAndValue);
		friend bool operator==(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS);
		friend bool operator!=(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS);


		int copyFromASNObject(const ASN1T_EXP_AttributeTypeAndValue & );
		int copyToASNObject(ASN1T_EXP_AttributeTypeAndValue & ) const ;
		void freeASNObject(ASN1T_EXP_AttributeTypeAndValue & )const;

		~AttributeTypeAndValue(void);

		// GETTERS AND SETTERS

		void setAttributeType(const  ASN1T_EXP_AttributeType &iAttrType);
		void setAttributeValue(const  AttributeValue &iAttrValue);

		const ASN1T_EXP_AttributeType& getAttributeType() const ;
		const AttributeValue & getAttributeValue() const ;


	//	int copyToASNObject(ASN1T_EXP_Attribute & oAttribute);
		QString toString(bool iNormalized = false) const;


	};


}

#endif


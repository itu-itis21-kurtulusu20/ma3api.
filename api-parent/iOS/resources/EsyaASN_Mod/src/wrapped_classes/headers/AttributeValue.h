#ifndef __ATTRIBUTEVALUE__
#define __ATTRIBUTEVALUE__

#include "cms.h"
#include "EException.h"
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
	class Q_DECL_EXPORT AttributeValue :public EASNWrapperTemplate<ASN1T_EXP_AttributeValue,ASN1C_EXP_AttributeValue>
	{
	public:
		QByteArray mValue;
		
		AttributeValue(void);
		AttributeValue(const QByteArray &iAttributeValue );
		AttributeValue(const ASN1T_EXP_AttributeValue &iAttributeValue );
		AttributeValue(const AttributeValue &iAttributeValue );

		AttributeValue & operator= (const AttributeValue & iAttributeValue);
		friend bool operator==(const AttributeValue & iRHS, const AttributeValue & iLHS);
		friend bool operator!=(const AttributeValue & iRHS, const AttributeValue & iLHS);

		int copyToASNObject(ASN1T_EXP_AttributeValue & oExtension) const;
		int copyFromASNObject(const ASN1T_EXP_AttributeValue & );
		void freeASNObject(ASN1T_EXP_AttributeValue & iAttrVal)const;

		virtual ~AttributeValue(void);

		// GETTERS AND SETTERS

		QByteArray getValue() const ;

		void setValue(const QByteArray& iValue);

	};

}

#endif 


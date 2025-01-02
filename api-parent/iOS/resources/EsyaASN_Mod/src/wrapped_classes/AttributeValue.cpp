#include "AttributeValue.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

AttributeValue::AttributeValue(void)
{
}

AttributeValue::~AttributeValue(void)
{
}

AttributeValue::AttributeValue(const QByteArray &iAttributeValue )
{
	mValue = iAttributeValue;
}

AttributeValue::AttributeValue(const ASN1T_EXP_AttributeValue &iAttributeValue )
{
	mValue = QByteArray((char*)iAttributeValue.data,iAttributeValue.numocts);
}

AttributeValue::AttributeValue(const AttributeValue &iAttributeValue )
{
	mValue = iAttributeValue.getValue();	
}

AttributeValue & AttributeValue::operator= (const AttributeValue & iAttributeValue)
{
	mValue = iAttributeValue.getValue();
	return *this;
}

QByteArray AttributeValue::getValue() const 
{
	return mValue;
}

void AttributeValue::setValue(const QByteArray& iValue)
{
	mValue = iValue;
}


int AttributeValue::copyFromASNObject(const ASN1T_EXP_AttributeValue & iAttributeValue)
{
	mValue = QByteArray((const char*)iAttributeValue.data,iAttributeValue.numocts);
	return SUCCESS;
}


int AttributeValue::copyToASNObject( ASN1T_EXP_AttributeValue & oAttrValue)const 
{
	oAttrValue.data	= (OSOCTET*)myStrDup(mValue.data(),mValue.size());
	oAttrValue.numocts = mValue.size();
	return SUCCESS;
}

void AttributeValue::freeASNObject(ASN1T_EXP_AttributeValue & iAttrVal) const
{
	if ( iAttrVal.numocts > 0 )
	{
		DELETE_MEMORY_ARRAY(iAttrVal.data);
	}
}

bool operator==(const AttributeValue & iRHS, const AttributeValue & iLHS)
{
	return (iRHS.getValue() == iLHS.getValue());
	
}

bool operator!=(const AttributeValue & iRHS, const AttributeValue & iLHS)
{
	return !(iRHS == iLHS);
}
NAMESPACE_END

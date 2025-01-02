#include "AttributeTypeAndValue.h"
#include "ESeqOfList.h"

using namespace esya;

AttributeTypeAndValue::AttributeTypeAndValue(void)
{
}

AttributeTypeAndValue::AttributeTypeAndValue(const  ASN1T_EXP_AttributeType &iAttrType,const  AttributeValue & iAttrValue)
{
	mAttributeType = iAttrType;
	mAttributeValue = iAttrValue;
}

AttributeTypeAndValue::AttributeTypeAndValue(const ASN1T_EXP_AttributeTypeAndValue & iAttributeTypeAndValue)
{
	copyFromASNObject(iAttributeTypeAndValue);
}

AttributeTypeAndValue::AttributeTypeAndValue(const AttributeTypeAndValue & iAttributeTypeAndValue)
:	mAttributeType(iAttributeTypeAndValue.getAttributeType()),
	mAttributeValue(iAttributeTypeAndValue.getAttributeValue())
{
}

AttributeTypeAndValue & AttributeTypeAndValue::operator=(const AttributeTypeAndValue& iAttributeTypeAndValue)
{
	mAttributeType = iAttributeTypeAndValue.getAttributeType();
	mAttributeValue = iAttributeTypeAndValue.getAttributeValue();			
	return *this;
}


const ASN1T_EXP_AttributeType& AttributeTypeAndValue::getAttributeType()const
{
	return mAttributeType;
}


const AttributeValue & AttributeTypeAndValue::getAttributeValue() const 
{
	return mAttributeValue;
}

AttributeTypeAndValue::AttributeTypeAndValue(const QByteArray &iAttributeTypeAndValue)
{
	constructObject(iAttributeTypeAndValue);
}


AttributeTypeAndValue::~AttributeTypeAndValue(void)
{
}


void AttributeTypeAndValue::setAttributeType(const  ASN1T_EXP_AttributeType &iAttrType)
{
	mAttributeType = iAttrType;
}

void AttributeTypeAndValue::setAttributeValue(const AttributeValue& iAttrValue)
{
	mAttributeValue = iAttrValue;
}


int AttributeTypeAndValue::copyToASNObject(ASN1T_EXP_AttributeTypeAndValue & oAttrTV) const 
{
	oAttrTV.type = mAttributeType;
	oAttrTV.value.data =(OSOCTET*) myStrDup(mAttributeValue.getValue().data(),mAttributeValue.getValue().size());
	oAttrTV.value.numocts = mAttributeValue.getValue().size();
	return SUCCESS;
}

void AttributeTypeAndValue::freeASNObject(ASN1T_EXP_AttributeTypeAndValue & iAttrTypeAndValue) const
{
	if( iAttrTypeAndValue.value.numocts>0)
		DELETE_MEMORY_ARRAY(iAttrTypeAndValue.value.data)
}

bool esya::operator==(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS)
{
	if (iRHS.getAttributeType() != iLHS.getAttributeType())
	{
		return false;
	}
	if (iRHS.getAttributeValue() != iLHS.getAttributeValue())
	{
		return false;
	}
	return true;
}

bool esya::operator!=(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS)
{
	return !(iRHS == iLHS);
}


int AttributeTypeAndValue::copyFromASNObject(const ASN1T_EXP_AttributeTypeAndValue & iAttributeTypeAndValue)
{
	mAttributeType = iAttributeTypeAndValue.type;
	mAttributeValue = AttributeValue(iAttributeTypeAndValue.value);
	return SUCCESS;
}


QString AttributeTypeAndValue::toString(bool iNormalized) const
{
	QPair<QString,QString> pair = EASNToStringUtils::tv2String(getAttributeType(),getAttributeValue().getValue(),iNormalized);
	
	

	/*todo: burasý yazýlacak*/
	return QString( QString("%1=%2").arg(pair.first).arg(pair.second));
}


#include "Attribute.h"
#include "ESeqOfList.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

Attribute::Attribute(void)
{
}

Attribute::Attribute(const  ASN1T_EXP_AttributeType &iAttrType)
:mAttributeType(iAttrType)
{
}

Attribute::Attribute(const ASN1T_EXP_Attribute & iAttribute)
{
	copyFromASNObject(iAttribute);
}

Attribute::Attribute(const Attribute & iAttribute)
:	mAttributeType(iAttribute.getType())
{
	mAttributeValues.clear();
	for ( int i = 0; i < iAttribute.getAttributeValues().size(); i++ )
	{
		mAttributeValues.append(iAttribute.getAttributeValues()[i]);
	}

	return ;
}

Attribute& Attribute::operator=(const Attribute& iAttribute)
{
	mAttributeType = iAttribute.getType();
	mAttributeValues.clear();
	for ( int i = 0; i < iAttribute.getAttributeValues().size(); i++ )
	{
		mAttributeValues.append(iAttribute.getAttributeValues()[i]);
	}
	return *this;
}

bool operator==(const Attribute & iRHS, const Attribute & iLHS)
{
	return ( ( iRHS.getType()				== iLHS.getType()			) && 
		( iRHS.getAttributeValues()	== iLHS.getAttributeValues())	 );

}

bool operator!=(const Attribute & iRHS, const Attribute & iLHS)
{
	return !(iRHS == iLHS);
}

const ASN1T_EXP_AttributeType& Attribute::getType()const
{
	return mAttributeType;
}


const QList<AttributeValue> & Attribute::getAttributeValues() const 
{
	return mAttributeValues;
}

QList<AttributeValue> & Attribute::getAttributeValues()
{
	return mAttributeValues;
}


Attribute::Attribute(const QByteArray &iAttribute)
{
	constructObject(iAttribute);
}


Attribute::~Attribute(void)
{
}


void Attribute::setType(const  ASN1T_EXP_AttributeType &iAttrType)
{
	mAttributeType = iAttrType;
}

int Attribute::copyToASNObject(ASN1T_EXP_Attribute & oAttribute) const
{
	oAttribute.type = getType();

	for (int i = 0 ; i< mAttributeValues.size(); i++)
	{
		ASN1T_EXP_AttributeValue * pAttrVal = mAttributeValues[i].getASNCopy();
		ESeqOfList::append(oAttribute.values,pAttrVal);
	}
	return SUCCESS;
}


void Attribute::freeASNObject(ASN1T_EXP_Attribute & iAttribute)const
{
	AttributeValue().freeASNObjects(iAttribute.values);
}

int Attribute::copyFromASNObject(const ASN1T_EXP_Attribute & iAttribute)
{
	mAttributeType = iAttribute.type;
	mAttributeValues.clear();
	for ( int i = 0; i < iAttribute.values.count; i++ )
	{
		ASN1T_EXP_AttributeValue * pAttrVal = (ASN1T_EXP_AttributeValue *)ESeqOfList::get(iAttribute.values,i);
		AttributeValue attrVal(*pAttrVal);
		mAttributeValues.append(attrVal);
	}

	return SUCCESS;
}

int Attribute::addAttributeValue(const ASN1T_EXP_AttributeValue &iAttributeValue)
{
	AttributeValue attrVal(iAttributeValue);
	mAttributeValues.append(attrVal);
	return SUCCESS;	
}

int Attribute::addAttributeValue(const QByteArray &iAttributeValue)
{
	AttributeValue attrVal(iAttributeValue);
	mAttributeValues.append(attrVal);
	return SUCCESS;	
}

int Attribute::addAttributeValue(const AttributeValue &iAttributeValue)
{
	mAttributeValues.append(iAttributeValue);
	return SUCCESS;	
}

int Attribute::copyAttributeList(const ASN1TPDUSeqOfList & iAttrList, QList<Attribute>& oList)
{
	return copyASNObjects<Attribute>(iAttrList,oList);
}

/**
Listedeke extensionlarýn ASN kopyalarýný oluþturur.
Oluþturulan Extensionlarýn hafýzasý daha sonra freeASNObject metodu ile
geri verilmeli
*/
int Attribute::copyAttributeList(const QList<Attribute> iList ,ASN1TPDUSeqOfList & oAttrList)
{
	return copyASNObjects<Attribute>(iList,oAttrList);
}

int	Attribute::copyAttributeList(const QByteArray & iASNBytes, QList<Attribute>& oList)
{
	return copyASNObjects<ASN1T_CMS_UnprotectedAttributes,ASN1C_CMS_UnprotectedAttributes,Attribute>(iASNBytes,oList);
}

int	Attribute::copyAttributeList(const QList<Attribute>& iList,QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_CMS_UnprotectedAttributes,ASN1C_CMS_UnprotectedAttributes,Attribute>(iList,oASNBytes);
}

const ASN1TObjId & Attribute::getAttributeType()const
{
	return mAttributeType;
}

int Attribute::findAttribute(const QList<Attribute> iAttrList , const ASN1TObjId iAttrType , Attribute & oAttr )
{
	for ( int i= 0; i<iAttrList.size(); i++)
	{
		if ( iAttrList[i].getAttributeType() == iAttrType )
		{		
			oAttr = iAttrList[i];
			return i;
		}
	}
	return FAILURE;
}

NAMESPACE_END

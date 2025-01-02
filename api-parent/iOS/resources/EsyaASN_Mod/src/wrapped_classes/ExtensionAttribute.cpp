#include "ExtensionAttribute.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

ExtensionAttribute::ExtensionAttribute()
{
}

ExtensionAttribute::ExtensionAttribute(const ExtensionAttribute &iEA)
:	mExtensionAttributeType(iEA.getExtensionAttributeType()),
	mExtensionAttributeValue(iEA.getExtensionAttributeValue())
{
	
}

ExtensionAttribute::ExtensionAttribute(const ASN1T_EXP_ExtensionAttribute & iEA)
{
	copyFromASNObject(iEA);
}

ExtensionAttribute::ExtensionAttribute(const QByteArray & iEA)
{
	constructObject(iEA);
}

ExtensionAttribute::ExtensionAttribute(const int & eaType, const QByteArray &eaValue)
: mExtensionAttributeType(eaType),mExtensionAttributeValue(eaValue)
{
}


ExtensionAttribute & ExtensionAttribute::operator=(const ExtensionAttribute& iEA)
{
	mExtensionAttributeType		= iEA.getExtensionAttributeType();
	mExtensionAttributeValue	= iEA.getExtensionAttributeValue();
	return *this;
}

int ExtensionAttribute::copyFromASNObject(const ASN1T_EXP_ExtensionAttribute& iEA)
{
	mExtensionAttributeType = iEA.extension_attribute_type;
	mExtensionAttributeValue = QByteArray((char*)iEA.extension_attribute_value.data,iEA.extension_attribute_value.numocts);
	return SUCCESS;
}

int ExtensionAttribute::getExtensionAttributeType()const 
{
	return mExtensionAttributeType;
}

const QByteArray& ExtensionAttribute::getExtensionAttributeValue() const
{
	return mExtensionAttributeValue;
}

void ExtensionAttribute::setExtensionAttributeValue(const QByteArray& iValue)
{
	mExtensionAttributeValue = iValue;
}

void ExtensionAttribute::setExtensionAttributeType(const int & iType) 
{
	mExtensionAttributeType = iType;	
}

int ExtensionAttribute::copyToASNObject(ASN1T_EXP_ExtensionAttribute & oEA) const
{
	oEA.extension_attribute_type	=  mExtensionAttributeType;

	oEA.extension_attribute_value.numocts = mExtensionAttributeValue.size();
	oEA.extension_attribute_value.data	= (OSOCTET*) myStrDup(mExtensionAttributeValue.data(),mExtensionAttributeValue.size());	

	return SUCCESS;
}


void ExtensionAttribute::freeASNObject(ASN1T_EXP_ExtensionAttribute & oEA)const
{
	if (oEA.extension_attribute_value.data && oEA.extension_attribute_value.numocts>0)
	{
		DELETE_MEMORY_ARRAY(oEA.extension_attribute_value.data)
	}
	oEA.extension_attribute_value.numocts = 0 ;
}


int ExtensionAttribute::copyExtensionAttributes(const ASN1T_EXP_ExtensionAttributes & iEAs, QList<ExtensionAttribute>& oList)const
{
	return copyASNObjects<ExtensionAttribute>(iEAs,oList);
}

int ExtensionAttribute::copyExtensionAttributes(const QList<ExtensionAttribute> iList ,ASN1T_EXP_ExtensionAttributes & oEAs)const
{
	return copyASNObjects<ExtensionAttribute>(iList,oEAs);
}

int	ExtensionAttribute::copyExtensionAttributes(const QByteArray & iASNBytes, QList<ExtensionAttribute>& oList)const
{
	return copyASNObjects<ASN1T_EXP_ExtensionAttributes,ASN1C_EXP_ExtensionAttributes,ExtensionAttribute>(iASNBytes,oList);
}

int	ExtensionAttribute::copyExtensionAttributes(const QList<ExtensionAttribute>& iList,QByteArray & oASNBytes)const
{
	return copyASNObjects<ASN1T_EXP_ExtensionAttributes,ASN1C_EXP_ExtensionAttributes,ExtensionAttribute>(iList,oASNBytes);
}

void ExtensionAttribute::freeExtensionAttributes(ASN1T_EXP_ExtensionAttributes & oEAs)const
{
	freeASNObjects(oEAs);
}

bool operator==(const ExtensionAttribute & iRHS,const ExtensionAttribute & iLHS)
{
	if	(	(iRHS.getExtensionAttributeType() != iLHS.getExtensionAttributeType())
		||	(iRHS.getExtensionAttributeValue() != iLHS.getExtensionAttributeValue())  )
		return false;

	return true;
}

bool operator!=(const ExtensionAttribute & iRHS, const ExtensionAttribute & iLHS)
{
	return !(iRHS == iLHS);
}

ExtensionAttribute::~ExtensionAttribute(void)
{
}

NAMESPACE_END

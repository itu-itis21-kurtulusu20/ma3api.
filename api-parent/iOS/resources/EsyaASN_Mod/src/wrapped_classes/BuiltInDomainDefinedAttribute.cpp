#include "BuiltInDomainDefinedAttribute.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

BuiltInDomainDefinedAttribute::BuiltInDomainDefinedAttribute(void)
{
}

BuiltInDomainDefinedAttribute::BuiltInDomainDefinedAttribute( const ASN1T_EXP_BuiltInDomainDefinedAttribute &iBDDA)
{
	copyFromASNObject(iBDDA);
}


BuiltInDomainDefinedAttribute::BuiltInDomainDefinedAttribute( const QString &iType, const QString& iValue)
: mValue(iValue),mType(iType)
{
}

BuiltInDomainDefinedAttribute::BuiltInDomainDefinedAttribute( const BuiltInDomainDefinedAttribute &iBDDA)
: mType(iBDDA.getType()), mValue(iBDDA.getValue())
{
}

BuiltInDomainDefinedAttribute::BuiltInDomainDefinedAttribute( const QByteArray &iBDDA)
{
	constructObject(iBDDA);
}

BuiltInDomainDefinedAttribute & BuiltInDomainDefinedAttribute::operator=(const BuiltInDomainDefinedAttribute& iBDDA)
{
	mType = iBDDA.getType();
	mValue= iBDDA.getValue();
	return(*this);

}

bool operator==(const BuiltInDomainDefinedAttribute& iRHS,const BuiltInDomainDefinedAttribute& iLHS)
{
	return ( iRHS.getValue() == iLHS.getValue() );
}

bool operator!=(const BuiltInDomainDefinedAttribute& iRHS, const BuiltInDomainDefinedAttribute& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int BuiltInDomainDefinedAttribute::copyFromASNObject(const ASN1T_EXP_BuiltInDomainDefinedAttribute & iBDDA)
{
	mType = QString(iBDDA.type);
	mValue= QString(iBDDA.value);
	return SUCCESS;
}

int BuiltInDomainDefinedAttribute::copyToASNObject(ASN1T_EXP_BuiltInDomainDefinedAttribute & oBDDA ) const
{
	oBDDA.type  = myStrDup(mType);
	oBDDA.value = myStrDup(mValue);
	return SUCCESS;
}
	
void BuiltInDomainDefinedAttribute::freeASNObject(ASN1T_EXP_BuiltInDomainDefinedAttribute& oSN)const
{
	DELETE_MEMORY_ARRAY(oSN.type);
	DELETE_MEMORY_ARRAY(oSN.value);
}

const QString& BuiltInDomainDefinedAttribute::getType() const 
{
	return mType;
}

void BuiltInDomainDefinedAttribute::setType(const QString & iType)  
{
	mType = iType;
}

const QString& BuiltInDomainDefinedAttribute::getValue() const 
{
	return mValue;
}
	
void BuiltInDomainDefinedAttribute::setValue(const QString & iValue)  
{
	mValue = iValue;
}


int BuiltInDomainDefinedAttribute::copyBDDAs(const ASN1T_EXP_BuiltInDomainDefinedAttributes & iBDDAs, QList<BuiltInDomainDefinedAttribute>& oList)const
{
	return copyASNObjects<BuiltInDomainDefinedAttribute>(iBDDAs,oList);
}

int BuiltInDomainDefinedAttribute::copyBDDAs(const QList<BuiltInDomainDefinedAttribute> iList ,ASN1T_EXP_BuiltInDomainDefinedAttributes & oBDDAs)const
{
	return copyASNObjects<BuiltInDomainDefinedAttribute>(iList,oBDDAs);
}

int	BuiltInDomainDefinedAttribute::copyBDDAs(const QByteArray & iASNBytes, QList<BuiltInDomainDefinedAttribute>& oList)const
{
	return copyASNObjects<ASN1T_EXP_BuiltInDomainDefinedAttributes,ASN1C_EXP_BuiltInDomainDefinedAttributes,BuiltInDomainDefinedAttribute>(iASNBytes,oList);
}

int	BuiltInDomainDefinedAttribute::copyBDDAs(const QList<BuiltInDomainDefinedAttribute>& iList,QByteArray & oASNBytes)const
{
	return copyASNObjects<ASN1T_EXP_BuiltInDomainDefinedAttributes,ASN1C_EXP_BuiltInDomainDefinedAttributes,BuiltInDomainDefinedAttribute>(iList,oASNBytes);
}

void BuiltInDomainDefinedAttribute::freeBDDAs(ASN1T_EXP_BuiltInDomainDefinedAttributes& oBDDAs)const
{
	freeASNObjects(oBDDAs);
}


BuiltInDomainDefinedAttribute::~BuiltInDomainDefinedAttribute()
{
}
NAMESPACE_END

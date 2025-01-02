#include "ORAddress.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

ORAddress::ORAddress(void)
{
}

ORAddress::ORAddress( const ASN1T_EXP_ORAddress &iOA)
{
	copyFromASNObject(iOA);
}

ORAddress::ORAddress( const ORAddress &iOA)
:	mBuiltInDomainDefinedAttributesPresent(iOA.isBuiltInDomainDefinedAttributesPresent()), 
	mExtensionAttributesPresent(iOA.isExtensionAttributesPresent()),
	mBuiltInStandardAttributes(iOA.getBuiltInStandardAttributes())
{
	mBuiltInDomainDefinedAttributes = iOA.getBuiltInDomainDefinedAttributes(); 
	mExtensionAttributes = iOA.getExtensionAttributes();
}

ORAddress::ORAddress( const QByteArray &iOA)
{
	constructObject(iOA);
}

ORAddress & ORAddress::operator=(const ORAddress& iOA)
{
	mBuiltInDomainDefinedAttributesPresent = iOA.isBuiltInDomainDefinedAttributesPresent(); 
	mExtensionAttributesPresent = iOA.isExtensionAttributesPresent();
	
	mBuiltInStandardAttributes = iOA.getBuiltInStandardAttributes();
	mBuiltInDomainDefinedAttributes = iOA.getBuiltInDomainDefinedAttributes();
	mExtensionAttributes = iOA.getExtensionAttributes();

	return(*this);
}

bool operator==(const ORAddress& iRHS,const ORAddress& iLHS)
{
	if (	( iRHS.isBuiltInDomainDefinedAttributesPresent() != iLHS.isBuiltInDomainDefinedAttributesPresent() )		||
			( iRHS.isExtensionAttributesPresent() != iLHS.isExtensionAttributesPresent() ) )
		return false;
	
	if ( iRHS.getBuiltInStandardAttributes() != iLHS.getBuiltInStandardAttributes() ) 
		return false;

	if ( iRHS.isBuiltInDomainDefinedAttributesPresent() && 
		( iRHS.getBuiltInDomainDefinedAttributes() != iLHS.getBuiltInDomainDefinedAttributes()) )
		return false;

	if ( iRHS.isExtensionAttributesPresent() && 
		( iRHS.getExtensionAttributes() != iLHS.getExtensionAttributes()) )
		return false;

	return true;
}

bool operator!=(const ORAddress& iRHS, const ORAddress& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int ORAddress::copyFromASNObject(const ASN1T_EXP_ORAddress & iOA)
{
	mBuiltInDomainDefinedAttributesPresent = iOA.m.built_in_domain_defined_attributesPresent; 
	mExtensionAttributesPresent = iOA.m.extension_attributesPresent;

	mBuiltInStandardAttributes.copyFromASNObject(iOA.built_in_standard_attributes);

	if (mBuiltInDomainDefinedAttributesPresent) 
	{
		BuiltInDomainDefinedAttribute().copyBDDAs(iOA.built_in_domain_defined_attributes,mBuiltInDomainDefinedAttributes);
	}
	if (mExtensionAttributesPresent)
	{
		ExtensionAttribute().copyExtensionAttributes(iOA.extension_attributes,mExtensionAttributes);
	}

	return SUCCESS;
}

int ORAddress::copyToASNObject(ASN1T_EXP_ORAddress & oOA ) const
{
	oOA.m.built_in_domain_defined_attributesPresent = mBuiltInDomainDefinedAttributesPresent ; 
	oOA.m.extension_attributesPresent = mExtensionAttributesPresent ;

	mBuiltInStandardAttributes.copyToASNObject(oOA.built_in_standard_attributes);

	if (mBuiltInDomainDefinedAttributesPresent) 
	{
		BuiltInDomainDefinedAttribute().copyBDDAs(mBuiltInDomainDefinedAttributes,oOA.built_in_domain_defined_attributes);
	}
	if (mExtensionAttributesPresent)
	{
		ExtensionAttribute().copyExtensionAttributes(mExtensionAttributes,oOA.extension_attributes);
	}

	return SUCCESS;
}
	
void ORAddress::freeASNObject(ASN1T_EXP_ORAddress& oOA)const
{
	if (oOA.m.built_in_domain_defined_attributesPresent) 
	{
		BuiltInDomainDefinedAttribute().freeASNObjects(oOA.built_in_domain_defined_attributes);
	}
	if (oOA.m.extension_attributesPresent)
	{
		ExtensionAttribute().freeASNObjects(oOA.extension_attributes);
	}

	mBuiltInStandardAttributes.freeASNObject(oOA.built_in_standard_attributes);
}

bool ORAddress::isBuiltInDomainDefinedAttributesPresent()const
{
	return mBuiltInDomainDefinedAttributesPresent;
}
bool ORAddress::isExtensionAttributesPresent()const
{
	return mExtensionAttributesPresent;

}

const BuiltInStandardAttributes&	ORAddress::getBuiltInStandardAttributes()const
{
	return mBuiltInStandardAttributes;
}
const QList<BuiltInDomainDefinedAttribute>&	ORAddress::getBuiltInDomainDefinedAttributes()const
{
	return mBuiltInDomainDefinedAttributes;
}
const QList<ExtensionAttribute>& ORAddress::getExtensionAttributes()const
{
	return mExtensionAttributes;
}

void ORAddress::setBuiltInStandardAttributes(const BuiltInStandardAttributes& iBSA)
{
	mBuiltInStandardAttributes = iBSA;
}

void ORAddress::setBuiltInDomainDefinedAttributes(const QList<BuiltInDomainDefinedAttribute>& iBDDA)
{
	mBuiltInDomainDefinedAttributes = iBDDA;
	mBuiltInDomainDefinedAttributesPresent = true;
}
void ORAddress::setExtensionAttributes(const QList<ExtensionAttribute>& iEAs)
{
	mExtensionAttributes = iEAs;
	mExtensionAttributesPresent = true;
}

ORAddress::~ORAddress()
{
}
NAMESPACE_END

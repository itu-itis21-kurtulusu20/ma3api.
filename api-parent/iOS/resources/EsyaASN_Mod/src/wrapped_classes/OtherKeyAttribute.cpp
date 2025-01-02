#include "OtherKeyAttribute.h"

namespace esya
{

OtherKeyAttribute::OtherKeyAttribute(void)
: mKeyAttrPresent(false)
{
}

OtherKeyAttribute::OtherKeyAttribute(const QByteArray & iOKA)
{
	constructObject(iOKA);
}

OtherKeyAttribute::OtherKeyAttribute(const QByteArray & iKeyAttribute, const ASN1TObjId & iKeyAttrID)
:	mKeyAttribute(iKeyAttribute),
	mKeyAttrPresent(true),
	mKeyAttrID(iKeyAttrID)
{
}

OtherKeyAttribute::OtherKeyAttribute(ASN1TObjId & iKeyAttrID)
:	mKeyAttrPresent(false),
	mKeyAttrID(iKeyAttrID)
{
}

OtherKeyAttribute::OtherKeyAttribute(const ASN1T_CMS_OtherKeyAttribute & iOKA)
{
	copyFromASNObject(iOKA);
}

OtherKeyAttribute::OtherKeyAttribute(const OtherKeyAttribute& iOKA)
:	mKeyAttribute(iOKA.getKeyAttribute()),
	mKeyAttrPresent(iOKA.isKeyAttrPresent()),
	mKeyAttrID(iOKA.getKeyAttrID())
{
}

OtherKeyAttribute& OtherKeyAttribute::operator=(const OtherKeyAttribute& iOKA)
{
	mKeyAttribute	= iOKA.getKeyAttribute();
	mKeyAttrPresent	= iOKA.isKeyAttrPresent();
	mKeyAttrID		= iOKA.getKeyAttrID();

	return *this;
}

bool operator==(const OtherKeyAttribute & iRHS, const OtherKeyAttribute& iLHS)
{
	if (	(	iRHS.getKeyAttrID()		!= iLHS.getKeyAttrID()		) ||
			(	iRHS.isKeyAttrPresent()	!= iLHS.isKeyAttrPresent()	)		)
		return false;

	if (	  iRHS.isKeyAttrPresent() && 
			( iRHS.getKeyAttribute() != iLHS.getKeyAttribute() )	)
		return false;

	return true;
}

bool operator!=(const OtherKeyAttribute & iRHS, const OtherKeyAttribute& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int OtherKeyAttribute::copyFromASNObject(const ASN1T_CMS_OtherKeyAttribute & iOKA) 
{
	mKeyAttrPresent = iOKA.m.keyAttrPresent;
	mKeyAttrID		= iOKA.keyAttrId;

	if (iOKA.m.keyAttrPresent)
		mKeyAttribute = QByteArray((const char*)iOKA.keyAttr.data,iOKA.keyAttr.numocts);
	
	return SUCCESS;
}

int OtherKeyAttribute::copyToASNObject(ASN1T_CMS_OtherKeyAttribute & oOKA) const
{
	oOKA.keyAttrId = mKeyAttrID;
	oOKA.m.keyAttrPresent = mKeyAttrPresent;

	if (mKeyAttrPresent)
	{
		oOKA.keyAttr.data = (ASN1OCTET*) myStrDup(mKeyAttribute.data(),mKeyAttribute.size());
		oOKA.keyAttr.numocts = mKeyAttribute.size();
	}

	return SUCCESS;
}

void OtherKeyAttribute::freeASNObject(ASN1T_CMS_OtherKeyAttribute & oOKA)const
{
	if ( oOKA.m.keyAttrPresent && oOKA.keyAttr.numocts > 0 )
	{
		DELETE_MEMORY_ARRAY(oOKA.keyAttr.data)
	}
}

bool OtherKeyAttribute::isKeyAttrPresent()const
{
	return mKeyAttrPresent; 
}

const ASN1TObjId&  OtherKeyAttribute::getKeyAttrID() const 
{
	return mKeyAttrID;
}

const QByteArray&  OtherKeyAttribute::getKeyAttribute()const 
{
	return mKeyAttribute;
}


void OtherKeyAttribute::setKeyAttrPresent(bool iKAPresent )
{
	mKeyAttrPresent = iKAPresent;
}

void OtherKeyAttribute::setKeyAttrID(const ASN1TObjId& iKAID)
{
	mKeyAttrID = iKAID;
}

void OtherKeyAttribute::setKeyAttribute(const QByteArray& iKA)
{
	mKeyAttribute = iKA;
}





OtherKeyAttribute::~OtherKeyAttribute(void)
{
}


}
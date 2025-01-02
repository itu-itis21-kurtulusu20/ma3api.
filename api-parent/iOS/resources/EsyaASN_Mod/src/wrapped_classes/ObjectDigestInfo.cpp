#include "ObjectDigestInfo.h"

using namespace esya;

ObjectDigestInfo::ObjectDigestInfo(void)
: mOtherObjectTypeIDPresent(false)
{
}

ObjectDigestInfo::ObjectDigestInfo(const QByteArray & iODI)
{
	constructObject(iODI);	
}

ObjectDigestInfo::ObjectDigestInfo(const ASN1T_ATTRCERT_ObjectDigestInfo & iODI )
{
	copyFromASNObject(iODI);
}

ObjectDigestInfo::ObjectDigestInfo(const ObjectDigestInfo& iODI)
:	mDigestedObjectType(iODI.getDigestedObjectType()),
	mDigestAlgorithm(iODI.getDigestAlgorithm()),
	mObjectDigest(iODI.getObjectDigest()),
	mOtherObjectTypeIDPresent(iODI.isOtherObjectTypeIDPresent()),
	mOtherObjectTypeID(iODI.getOtherObjectTypeID())
{
}

ObjectDigestInfo& ObjectDigestInfo::operator=(const ObjectDigestInfo& iODI)
{
	mDigestedObjectType = iODI.getDigestedObjectType();
	mDigestAlgorithm	= iODI.getDigestAlgorithm();
	mObjectDigest		= iODI.getObjectDigest();
	if (mOtherObjectTypeIDPresent = iODI.isOtherObjectTypeIDPresent())
		mOtherObjectTypeID = iODI.getOtherObjectTypeID();
	return *this;
}

bool esya::operator==( const ObjectDigestInfo& iRHS, const ObjectDigestInfo& iLHS)
{
	if ( ( iRHS.getDigestedObjectType() != iLHS.getDigestedObjectType() ) || 
	     ( iRHS.getDigestAlgorithm()	!= iLHS.getDigestAlgorithm() ) ||
		 ( iRHS.getObjectDigest()		!= iLHS.getObjectDigest() ) ||
		 ( iRHS.isOtherObjectTypeIDPresent() != iLHS.isOtherObjectTypeIDPresent() ) )
		return false;

	if ( iRHS.isOtherObjectTypeIDPresent() && ( iRHS.getOtherObjectTypeID() != iLHS.getOtherObjectTypeID() ) )
		return false;

	return true;
}

bool esya::operator!=( const ObjectDigestInfo& iRHS, const ObjectDigestInfo& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int ObjectDigestInfo::copyFromASNObject(const ASN1T_ATTRCERT_ObjectDigestInfo & iODI)
{
	mDigestedObjectType = (DigestedObjectType) iODI.digestedObjectType;
	mDigestAlgorithm.copyFromASNObject(iODI.digestAlgorithm);
	mObjectDigest.copyFromASNObject(iODI.objectDigest);
	mOtherObjectTypeIDPresent = (iODI.m.otherObjectTypeIDPresent == 1 );
	if (mOtherObjectTypeIDPresent )
		mOtherObjectTypeID = iODI.otherObjectTypeID;
	
	return SUCCESS;
}

int ObjectDigestInfo::copyToASNObject(ASN1T_ATTRCERT_ObjectDigestInfo &oODI)const
{
	oODI.digestedObjectType = (ASN1T_ATTRCERT_ObjectDigestInfo_digestedObjectType)mDigestedObjectType;
	mDigestAlgorithm.copyToASNObject(oODI.digestAlgorithm);
	mObjectDigest.copyToASNObject(oODI.objectDigest);
	oODI.m.otherObjectTypeIDPresent = mOtherObjectTypeIDPresent ? 1:0 ;
	if (mOtherObjectTypeIDPresent )
		oODI.otherObjectTypeID = mOtherObjectTypeID;

	return SUCCESS;
}

void ObjectDigestInfo::freeASNObject(ASN1T_ATTRCERT_ObjectDigestInfo& oODI)const
{
	AlgorithmIdentifier().freeASNObject(oODI.digestAlgorithm);
	EBitString::freeASNObject(oODI.objectDigest);
}

bool ObjectDigestInfo::isOtherObjectTypeIDPresent()const
{
	return mOtherObjectTypeIDPresent;
}

const ObjectDigestInfo::DigestedObjectType &  ObjectDigestInfo::getDigestedObjectType()const
{
	return mDigestedObjectType;
}

const AlgorithmIdentifier & ObjectDigestInfo::getDigestAlgorithm() const
{
	return mDigestAlgorithm;
}

const EBitString& ObjectDigestInfo::getObjectDigest() const 
{
	return mObjectDigest;
}

const ASN1TObjId& ObjectDigestInfo::getOtherObjectTypeID() const 
{
	return mOtherObjectTypeID;
}

void ObjectDigestInfo::setDigestedObjectType(const DigestedObjectType &  iDOT)
{
	mDigestedObjectType = iDOT;
}

void ObjectDigestInfo::setDigestAlgorithm(const AlgorithmIdentifier& iDigestAlg)
{
	mDigestAlgorithm = iDigestAlg;
}

void ObjectDigestInfo::setObjectDigest(const EBitString & iObjectDigest) 
{
	mObjectDigest = iObjectDigest;
}

void ObjectDigestInfo::setOtherObjectTypeID(const ASN1TObjId & iOOTID)  
{
	mOtherObjectTypeIDPresent = true;
	mOtherObjectTypeID = iOOTID;
}

void ObjectDigestInfo::setOtherObjectTypeIDPresent(bool  iOOTIDPresent)
{
	mOtherObjectTypeIDPresent = iOOTIDPresent;
}

ObjectDigestInfo::~ObjectDigestInfo(void)
{

}
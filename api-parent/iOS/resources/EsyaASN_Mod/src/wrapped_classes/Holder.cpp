#include "Holder.h"

using namespace esya;

Holder::Holder(void)
:	mBaseCertificateIDPresent(false),
	mObjectDigestInfoPresent(false),
	mEntityNamePresent(false)
{
}

Holder::Holder(const QByteArray & iHolder)
{
	constructObject(iHolder);
}

Holder::Holder(const ASN1T_ATTRCERT_Holder & iHolder )
{
	copyFromASNObject(iHolder);
}

Holder::Holder(const Holder& iHolder)
{
	if ( mBaseCertificateIDPresent = iHolder.isBaseCertificateIDPresent() )
	{
		mBaseCertificateID = iHolder.getBaseCertificateID();
	}
	if ( mObjectDigestInfoPresent = iHolder.isObjectDigestInfoPresent() )
	{
		mObjectDigestInfo = iHolder.getObjectDigestInfo();
	}
	if ( mEntityNamePresent = iHolder.isEntityNamePresent() )
	{
		mEntityName = iHolder.getEntityName();
	}
}

Holder& Holder::operator=(const Holder& iHolder)
{
	if ( mBaseCertificateIDPresent = iHolder.isBaseCertificateIDPresent() )
	{
		mBaseCertificateID = iHolder.getBaseCertificateID();
	}
	if ( mObjectDigestInfoPresent = iHolder.isObjectDigestInfoPresent() )
	{
		mObjectDigestInfo = iHolder.getObjectDigestInfo();
	}
	if ( mEntityNamePresent = iHolder.isEntityNamePresent() )
	{
		mEntityName = iHolder.getEntityName();
	}

	return *this;
}

bool esya::operator==( const Holder& iRHS, const Holder& iLHS)
{
	if (	( iRHS.isBaseCertificateIDPresent() != iLHS.isBaseCertificateIDPresent() ) ||
			( iRHS.isObjectDigestInfoPresent()	!= iLHS.isObjectDigestInfoPresent() ) ||
			( iRHS.isEntityNamePresent()		!= iLHS.isEntityNamePresent() ) )
			return false;

	if ( ( iRHS.isBaseCertificateIDPresent() ) && 
		 ( iRHS.getBaseCertificateID() != iLHS.getBaseCertificateID() ) )
		return false;

	if ( ( iRHS.isObjectDigestInfoPresent() ) && 
		( iRHS.getObjectDigestInfo() != iLHS.getObjectDigestInfo() ) )
		return false;

	if ( ( iRHS.isEntityNamePresent() ) && 
		( iRHS.getEntityName() != iLHS.getEntityName() ) )
		return false;

	return true;
}

bool esya::operator!=( const Holder& iRHS, const Holder& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int Holder::copyFromASNObject(const ASN1T_ATTRCERT_Holder & iHolder)
{
	if ( mBaseCertificateIDPresent = ( iHolder.m.baseCertificateIDPresent == 1 ) )
	{
		mBaseCertificateID.copyFromASNObject(iHolder.baseCertificateID);
	}
	if ( mObjectDigestInfoPresent = ( iHolder.m.objectDigestInfoPresent == 1 ) )
	{
		mObjectDigestInfo.copyFromASNObject(iHolder.objectDigestInfo);
	}
	if ( mEntityNamePresent = ( iHolder.m.entityNamePresent == 1 ) )
	{
		mEntityName.copyFromASNObject(iHolder.entityName);
	}

	return SUCCESS;
}

int Holder::copyToASNObject(ASN1T_ATTRCERT_Holder &oHolder)const
{
	oHolder.m.baseCertificateIDPresent	= mBaseCertificateIDPresent ? 1:0;
	oHolder.m.objectDigestInfoPresent	= mObjectDigestInfoPresent	? 1:0;
	oHolder.m.entityNamePresent			= mEntityNamePresent		? 1:0;

	if ( mBaseCertificateIDPresent )
	{
		mBaseCertificateID.copyToASNObject(oHolder.baseCertificateID);
	}
	if ( mObjectDigestInfoPresent )
	{
		mObjectDigestInfo.copyToASNObject(oHolder.objectDigestInfo);
	}
	if ( mEntityNamePresent )
	{
		mEntityName.copyToASNObject(oHolder.entityName);
	}

	return SUCCESS;
}

void Holder::freeASNObject(ASN1T_ATTRCERT_Holder& oHolder)const
{
	if ( oHolder.m.baseCertificateIDPresent )
	{
		IssuerSerial().freeASNObject(oHolder.baseCertificateID);
	}
	if ( oHolder.m.objectDigestInfoPresent )
	{
		ObjectDigestInfo().freeASNObject(oHolder.objectDigestInfo);
	}
	if ( oHolder.m.entityNamePresent )
	{
		GeneralNames().freeASNObject(oHolder.entityName);
	}
}

bool Holder::isBaseCertificateIDPresent()const
{
	return  mBaseCertificateIDPresent;
}

bool Holder::isObjectDigestInfoPresent()const
{
	return mObjectDigestInfoPresent;
}

bool Holder::isEntityNamePresent()const
{
	return mEntityNamePresent;
}

const IssuerSerial & Holder::getBaseCertificateID() const
{
	return mBaseCertificateID;
}

const ObjectDigestInfo &  Holder::getObjectDigestInfo()const
{
	return mObjectDigestInfo;
}

const GeneralNames & Holder::getEntityName() const 
{
	return mEntityName;
}

void Holder::setBaseCertificateID(const IssuerSerial & iBCID) 
{
	mBaseCertificateIDPresent = true;
	mBaseCertificateID = iBCID;
}

void Holder::setObjectDigestInfo(const ObjectDigestInfo & iObjectDigestInfo )
{
	mObjectDigestInfoPresent = true;
	mObjectDigestInfo = iObjectDigestInfo;
}

void Holder::setEntityName(const GeneralNames & iEntityName)
{
	mEntityNamePresent = true;
	mEntityName = iEntityName;
}

void Holder::setBaseCertificateIDPresent(bool b)
{
	mBaseCertificateIDPresent = b;
}

void Holder::setObjectDigestInfoPresent(bool b)
{
	mObjectDigestInfoPresent = b;
}

void Holder::setEntityNamePresent(bool b)
{
	mEntityNamePresent = b;
}


Holder::~Holder(void)
{
}
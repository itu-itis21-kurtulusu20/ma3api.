#include "V2Form.h"

using namespace esya;

V2Form::V2Form(void)
:	mBaseCertificateIDPresent(false),
	mObjectDigestInfoPresent(false),
	mIssuerNamePresent(false)
{
}

V2Form::V2Form(const QByteArray & iV2Form)
{
	constructObject(iV2Form);
}

V2Form::V2Form(const ASN1T_ATTRCERT_V2Form & iV2Form )
{
	copyFromASNObject(iV2Form);
}

V2Form::V2Form(const V2Form& iV2Form)
{
	if ( mBaseCertificateIDPresent = iV2Form.isBaseCertificateIDPresent() )
	{
		mBaseCertificateID = iV2Form.getBaseCertificateID();
	}
	if ( mObjectDigestInfoPresent = iV2Form.isObjectDigestInfoPresent() )
	{
		mObjectDigestInfo = iV2Form.getObjectDigestInfo();
	}
	if ( mIssuerNamePresent = iV2Form.isIssuerNamePresent() )
	{
		mIssuerName = iV2Form.getIssuerName();
	}
}

V2Form& V2Form::operator=(const V2Form& iV2Form)
{
	if ( mBaseCertificateIDPresent = iV2Form.isBaseCertificateIDPresent() )
	{
		mBaseCertificateID = iV2Form.getBaseCertificateID();
	}
	if ( mObjectDigestInfoPresent = iV2Form.isObjectDigestInfoPresent() )
	{
		mObjectDigestInfo = iV2Form.getObjectDigestInfo();
	}
	if ( mIssuerNamePresent = iV2Form.isIssuerNamePresent() )
	{
		mIssuerName = iV2Form.getIssuerName();
	}

	return *this;
}

bool esya::operator==( const V2Form& iRHS, const V2Form& iLHS)
{
	if (	( iRHS.isBaseCertificateIDPresent() != iLHS.isBaseCertificateIDPresent() ) ||
			( iRHS.isObjectDigestInfoPresent()	!= iLHS.isObjectDigestInfoPresent() ) ||
			( iRHS.isIssuerNamePresent()		!= iLHS.isIssuerNamePresent() ) )
			return false;

	if ( ( iRHS.isBaseCertificateIDPresent() ) && 
		 ( iRHS.getBaseCertificateID() != iLHS.getBaseCertificateID() ) )
		return false;

	if ( ( iRHS.isObjectDigestInfoPresent() ) && 
		( iRHS.getObjectDigestInfo() != iLHS.getObjectDigestInfo() ) )
		return false;

	if ( ( iRHS.isIssuerNamePresent() ) && 
		( iRHS.getIssuerName() != iLHS.getIssuerName() ) )
		return false;

	return true;
}

bool esya::operator!=( const V2Form& iRHS, const V2Form& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int V2Form::copyFromASNObject(const ASN1T_ATTRCERT_V2Form & iV2Form)
{
	if ( mBaseCertificateIDPresent = ( iV2Form.m.baseCertificateIDPresent == 1 ) )
	{
		mBaseCertificateID.copyFromASNObject(iV2Form.baseCertificateID);
	}
	if ( mObjectDigestInfoPresent = ( iV2Form.m.objectDigestInfoPresent == 1 ) )
	{
		mObjectDigestInfo.copyFromASNObject(iV2Form.objectDigestInfo);
	}
	if ( mIssuerNamePresent = ( iV2Form.m.issuerNamePresent == 1 ) )
	{
		mIssuerName.copyFromASNObject(iV2Form.issuerName);
	}

	return SUCCESS;
}

int V2Form::copyToASNObject(ASN1T_ATTRCERT_V2Form &oV2Form)const
{
	oV2Form.m.baseCertificateIDPresent	= mBaseCertificateIDPresent ? 1:0;
	oV2Form.m.objectDigestInfoPresent	= mObjectDigestInfoPresent	? 1:0;
	oV2Form.m.issuerNamePresent			= mIssuerNamePresent		? 1:0;

	if ( mBaseCertificateIDPresent )
	{
		mBaseCertificateID.copyToASNObject(oV2Form.baseCertificateID);
	}
	if ( mObjectDigestInfoPresent )
	{
		mObjectDigestInfo.copyToASNObject(oV2Form.objectDigestInfo);
	}
	if ( mIssuerNamePresent )
	{
		mIssuerName.copyToASNObject(oV2Form.issuerName);
	}

	return SUCCESS;
}

void V2Form::freeASNObject(ASN1T_ATTRCERT_V2Form& oV2Form)const
{
	if ( oV2Form.m.baseCertificateIDPresent )
	{
		IssuerSerial().freeASNObject(oV2Form.baseCertificateID);
	}
	if ( oV2Form.m.objectDigestInfoPresent )
	{
		ObjectDigestInfo().freeASNObject(oV2Form.objectDigestInfo);
	}
	if ( oV2Form.m.issuerNamePresent )
	{
		GeneralNames().freeASNObject(oV2Form.issuerName);
	}
}

bool V2Form::isBaseCertificateIDPresent()const
{
	return  mBaseCertificateIDPresent;
}

bool V2Form::isObjectDigestInfoPresent()const
{
	return mObjectDigestInfoPresent;
}

bool V2Form::isIssuerNamePresent()const
{
	return mIssuerNamePresent;
}

const IssuerSerial & V2Form::getBaseCertificateID() const
{
	return mBaseCertificateID;
}

const ObjectDigestInfo &  V2Form::getObjectDigestInfo()const
{
	return mObjectDigestInfo;
}

const GeneralNames & V2Form::getIssuerName() const 
{
	return mIssuerName;
}

void V2Form::setBaseCertificateID(const IssuerSerial & iBCID) 
{
	mBaseCertificateIDPresent = true;
	mBaseCertificateID = iBCID;
}

void V2Form::setObjectDigestInfo(const ObjectDigestInfo & iObjectDigestInfo )
{
	mObjectDigestInfoPresent = true;
	mObjectDigestInfo = iObjectDigestInfo;
}

void V2Form::setIssuerName(const GeneralNames & iIssuerName)
{
	mIssuerNamePresent = true;
	mIssuerName = iIssuerName;
}

void V2Form::setBaseCertificateIDPresent(bool b)
{
	mBaseCertificateIDPresent = b;
}

void V2Form::setObjectDigestInfoPresent(bool b)
{
	mObjectDigestInfoPresent = b;
}

void V2Form::setIssuerNamePresent(bool b)
{
	mIssuerNamePresent = b;
}

V2Form::~V2Form(void)
{
}
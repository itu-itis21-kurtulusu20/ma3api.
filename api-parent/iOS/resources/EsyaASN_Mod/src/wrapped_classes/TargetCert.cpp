#include "TargetCert.h"

using namespace esya;

TargetCert::TargetCert(void)
:	mCertDigestInfoPresent(false),
	mTargetNamePresent(false)
{
}

TargetCert::TargetCert(const QByteArray & iTargetCert)
{
	constructObject(iTargetCert);
}

TargetCert::TargetCert(const ASN1T_ATTRCERT_TargetCert & iTargetCert )
{
	copyFromASNObject(iTargetCert);
}

TargetCert::TargetCert(const TargetCert& iTargetCert)
:	mTargetCertificate(iTargetCert.getTargetCertificate())
{
	if ( mCertDigestInfoPresent = iTargetCert.isCertDigestInfoPresent() )
	{
		mCertDigestInfo = iTargetCert.getCertDigestInfo();
	}
	if ( mTargetNamePresent = iTargetCert.isTargetNamePresent() )
	{
		mTargetName = iTargetCert.getTargetName();
	}
}

TargetCert& TargetCert::operator=(const TargetCert& iTargetCert)
{
	mTargetCertificate = iTargetCert.getTargetCertificate();

	if ( mCertDigestInfoPresent = iTargetCert.isCertDigestInfoPresent() )
	{
		mCertDigestInfo = iTargetCert.getCertDigestInfo();
	}
	if ( mTargetNamePresent = iTargetCert.isTargetNamePresent() )
	{
		mTargetName = iTargetCert.getTargetName();
	}

	return *this;
}

bool esya::operator==( const TargetCert& iRHS, const TargetCert& iLHS)
{
	if (	( iRHS.getTargetCertificate() != iLHS.getTargetCertificate() )			||
			( iRHS.isCertDigestInfoPresent()	!= iLHS.isCertDigestInfoPresent() ) ||
			( iRHS.isTargetNamePresent()		!= iLHS.isTargetNamePresent() ) )
			return false;

	if ( ( iRHS.isCertDigestInfoPresent() ) && 
		( iRHS.getCertDigestInfo() != iLHS.getCertDigestInfo() ) )
		return false;

	if ( ( iRHS.isTargetNamePresent() ) && 
		( iRHS.getTargetName() != iLHS.getTargetName() ) )
		return false;

	return true;
}

bool esya::operator!=( const TargetCert& iRHS, const TargetCert& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int TargetCert::copyFromASNObject(const ASN1T_ATTRCERT_TargetCert & iTargetCert)
{
	mTargetCertificate.copyFromASNObject(iTargetCert.targetCertificate);

	if ( mCertDigestInfoPresent = ( iTargetCert.m.certDigestInfoPresent == 1 ) )
	{
		mCertDigestInfo.copyFromASNObject(iTargetCert.certDigestInfo);
	}
	if ( mTargetNamePresent = ( iTargetCert.m.targetNamePresent == 1 ) )
	{
		mTargetName.copyFromASNObject(iTargetCert.targetName);
	}

	return SUCCESS;
}

int TargetCert::copyToASNObject(ASN1T_ATTRCERT_TargetCert &oTargetCert)const
{
	oTargetCert.m.certDigestInfoPresent	= mCertDigestInfoPresent	? 1:0;
	oTargetCert.m.targetNamePresent			= mTargetNamePresent		? 1:0;

	mTargetCertificate.copyToASNObject(oTargetCert.targetCertificate);

	if ( mCertDigestInfoPresent )
	{
		mCertDigestInfo.copyToASNObject(oTargetCert.certDigestInfo);
	}

	if ( mTargetNamePresent )
	{
		mTargetName.copyToASNObject(oTargetCert.targetName);
	}

	return SUCCESS;
}

void TargetCert::freeASNObject(ASN1T_ATTRCERT_TargetCert& oTargetCert)const
{
	IssuerSerial().freeASNObject(oTargetCert.targetCertificate);

	if ( oTargetCert.m.certDigestInfoPresent )
	{
		ObjectDigestInfo().freeASNObject(oTargetCert.certDigestInfo);
	}
	if ( oTargetCert.m.targetNamePresent )
	{
		GeneralName().freeASNObject(oTargetCert.targetName);
	}
}

bool TargetCert::isCertDigestInfoPresent()const
{
	return mCertDigestInfoPresent;
}

bool TargetCert::isTargetNamePresent()const
{
	return mTargetNamePresent;
}

const IssuerSerial & TargetCert::getTargetCertificate() const
{
	return mTargetCertificate;
}

const ObjectDigestInfo &  TargetCert::getCertDigestInfo()const
{
	return mCertDigestInfo;
}

const GeneralName & TargetCert::getTargetName() const 
{
	return mTargetName;
}

void TargetCert::setTargetCertificate(const IssuerSerial & iBCID) 
{
	mTargetCertificate = iBCID;
}

void TargetCert::setCertDigestInfo(const ObjectDigestInfo & iCertDigestInfo )
{
	mCertDigestInfoPresent = true;
	mCertDigestInfo = iCertDigestInfo;
}

void TargetCert::setTargetName(const GeneralName & iTargetName)
{
	mTargetNamePresent = true;
	mTargetName = iTargetName;
}


void TargetCert::setCertDigestInfoPresent(bool b)
{
	mCertDigestInfoPresent = b;
}

void TargetCert::setTargetNamePresent(bool b)
{
	mTargetNamePresent = b;
}

TargetCert::~TargetCert(void)
{
}
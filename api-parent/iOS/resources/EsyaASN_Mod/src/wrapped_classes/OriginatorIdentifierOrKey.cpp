#include "OriginatorIdentifierOrKey.h"

using namespace esya;

OriginatorIdentifierOrKey::OriginatorIdentifierOrKey(void)
{
	mType = OKType_OriginatorKey;
}

OriginatorIdentifierOrKey::OriginatorIdentifierOrKey(const ASN1T_CMS_OriginatorIdentifierOrKey & iOK)
{
	copyFromASNObject(iOK);
}

OriginatorIdentifierOrKey::OriginatorIdentifierOrKey(const QByteArray & iOK)
{
	constructObject(iOK);
}

OriginatorIdentifierOrKey::OriginatorIdentifierOrKey(const OriginatorIdentifierOrKey &iOK)
:	mType(iOK.getType()),
	mIssuerAndSerialNumber(iOK.getIssuerAndSerialNumber()),
	mSubjectKeyIdentifier(iOK.getSubjectKeyIdentifier()),
	mOriginatorKey(iOK.getOriginatorKey())
{
}

OriginatorIdentifierOrKey & OriginatorIdentifierOrKey::operator=(const OriginatorIdentifierOrKey& iOK)
{
	mType					= iOK.getType();
	mIssuerAndSerialNumber	= iOK.getIssuerAndSerialNumber();
	mSubjectKeyIdentifier	= iOK.getSubjectKeyIdentifier();
	mOriginatorKey			= iOK.getOriginatorKey();
	return (*this);
}

bool esya::operator==(const OriginatorIdentifierOrKey & iRHS, const OriginatorIdentifierOrKey& iLHS)
{
	if (iRHS.getType() != iLHS.getType())
		return false;
	switch (iRHS.getType())
	{
	case OriginatorIdentifierOrKey::OKType_IssuerAndSerialNumber:
		{
			if (iRHS.getIssuerAndSerialNumber() == iLHS.getIssuerAndSerialNumber())
				return true;
			break;
		}
	case OriginatorIdentifierOrKey::OKType_SubjectKeyIdentifier:
		{
			if (iRHS.getSubjectKeyIdentifier() == iLHS.getSubjectKeyIdentifier())
				return true;
			break;
		}
	case OriginatorIdentifierOrKey::OKType_OriginatorKey:
		{
			if (iRHS.getOriginatorKey() == iLHS.getOriginatorKey())
				return true;
			break;
		}
	}
	return false;
}

bool esya::operator!=(const OriginatorIdentifierOrKey & iRHS, const OriginatorIdentifierOrKey& iLHS)
{
	return ( !(iRHS == iLHS) );
}

int OriginatorIdentifierOrKey::copyFromASNObject(const ASN1T_CMS_OriginatorIdentifierOrKey& iOK)
{
	mType = (OKType)iOK.t;

	switch (mType)
	{
	case OriginatorIdentifierOrKey::OKType_IssuerAndSerialNumber:
		{
			if (iOK.u.issuerAndSerialNumber)
				mIssuerAndSerialNumber.copyFromASNObject(*iOK.u.issuerAndSerialNumber);
			break;
		}
	case OriginatorIdentifierOrKey::OKType_SubjectKeyIdentifier:
		{
			if (iOK.u.subjectKeyIdentifier)
				mSubjectKeyIdentifier.copyFromASNObject(*iOK.u.subjectKeyIdentifier);
			break;
		}
	case OriginatorIdentifierOrKey::OKType_OriginatorKey:
		{
			if (iOK.u.originatorKey)
				mOriginatorKey.copyFromASNObject(* (ASN1T_EXP_SubjectPublicKeyInfo*)iOK.u.originatorKey);
			break;
		}
	}

	return SUCCESS;
}

int OriginatorIdentifierOrKey::copyToASNObject(ASN1T_CMS_OriginatorIdentifierOrKey & oOK) const
{
	oOK.t = mType;
	switch (mType)
	{
	case OriginatorIdentifierOrKey::OKType_IssuerAndSerialNumber:
		{
			oOK.u.issuerAndSerialNumber = mIssuerAndSerialNumber.getASNCopy();
			break;
		}
	case OriginatorIdentifierOrKey::OKType_SubjectKeyIdentifier:
		{
			oOK.u.subjectKeyIdentifier = mSubjectKeyIdentifier.getASNCopy();
			break;
		}
	case OriginatorIdentifierOrKey::OKType_OriginatorKey:
		{
			oOK.u.originatorKey = (ASN1T_CMS_OriginatorPublicKey*)mOriginatorKey.getASNCopy();
			break;
		}
	}

	return SUCCESS;

}

void OriginatorIdentifierOrKey::freeASNObject(ASN1T_CMS_OriginatorIdentifierOrKey& oOK)const
{
	switch ((OKType)oOK.t)
	{
	case OKType_IssuerAndSerialNumber:
		{
			IssuerAndSerialNumber().freeASNObjectPtr(oOK.u.issuerAndSerialNumber);
			break;
		}
	case OKType_SubjectKeyIdentifier:
		{
			SubjectKeyIdentifier().freeASNObjectPtr(oOK.u.subjectKeyIdentifier);
			break;
		}
	case OKType_OriginatorKey:
		{
			OriginatorPublicKey().freeASNObjectPtr((ASN1T_EXP_SubjectPublicKeyInfo*)oOK.u.originatorKey);
			break;
		}
	}

}

OriginatorIdentifierOrKey::OKType OriginatorIdentifierOrKey::getType()const 
{
	return mType;
}

const IssuerAndSerialNumber	& OriginatorIdentifierOrKey::getIssuerAndSerialNumber()const
{
	return mIssuerAndSerialNumber;
}

const SubjectKeyIdentifier	& OriginatorIdentifierOrKey::getSubjectKeyIdentifier()const
{
	return mSubjectKeyIdentifier;
}

const OriginatorPublicKey	& OriginatorIdentifierOrKey::getOriginatorKey()const
{
	return mOriginatorKey;
}

void OriginatorIdentifierOrKey::setType(OriginatorIdentifierOrKey::OKType	iType)
{
	mType = iType;
}

void OriginatorIdentifierOrKey::setIssuerAndSerialNumber(const IssuerAndSerialNumber & iISA )
{
	mIssuerAndSerialNumber = iISA;
}

void OriginatorIdentifierOrKey::setSubjectKeyIdentifier(const SubjectKeyIdentifier	& iSKI)
{
	mSubjectKeyIdentifier = iSKI;
}

void OriginatorIdentifierOrKey::setOriginatorKey(const OriginatorPublicKey	& iOPK)
{
	mType = OKType_OriginatorKey;
	mOriginatorKey = iOPK;
}

OriginatorIdentifierOrKey::~OriginatorIdentifierOrKey(void)
{
}
#include "RecipientIdentifier.h"
#include "ExtensionGenerator.h"

using namespace esya;

RecipientIdentifier::RecipientIdentifier(void)
{
}

RecipientIdentifier::RecipientIdentifier(const QByteArray & iRID)
{
	constructObject(iRID);	
}

RecipientIdentifier::RecipientIdentifier(const ASN1T_CMS_RecipientIdentifier & iRID)
{
	copyFromASNObject(iRID);
}

RecipientIdentifier::RecipientIdentifier(const IssuerAndSerialNumber& iISN)
:	mType(T_IssuerAndSerial),
	mIssuerAndSerialNumber(iISN)
{
}

RecipientIdentifier::RecipientIdentifier(const RecipientIdentifier& iRID)
{
	mType = iRID.getType();
	if	( mType == T_IssuerAndSerial )
		mIssuerAndSerialNumber = iRID.getIssuerAndSerialNumber();
	else if ( mType == T_SubjectKeyIdentifier )
		mSubjectKeyIdentifier = iRID.getSubjectKeyIdentifier();
}

RecipientIdentifier& RecipientIdentifier::operator=(const RecipientIdentifier & iRID)
{
	mType = iRID.getType();
	if	( mType == T_IssuerAndSerial )
		mIssuerAndSerialNumber = iRID.getIssuerAndSerialNumber();
	else if ( mType == T_SubjectKeyIdentifier )
		mSubjectKeyIdentifier = iRID.getSubjectKeyIdentifier();
	return *this;
}

bool esya::operator==(const RecipientIdentifier & iRHS,const RecipientIdentifier & iLHS)
{
	if ( iRHS.getType() != iLHS.getType() )	
		return false;
	if ( iRHS.getType() == T_IssuerAndSerial )
	{
		if ( iRHS.getIssuerAndSerialNumber() != iLHS.getIssuerAndSerialNumber() )
			return false;
	}
	else if ( iRHS.getType() == T_SubjectKeyIdentifier )
	{
		if ( iRHS.getSubjectKeyIdentifier() != iLHS.getSubjectKeyIdentifier() )
			return false;
	}

	return false;
}

bool esya::operator!=(const RecipientIdentifier & iRHS,const RecipientIdentifier & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int RecipientIdentifier::copyFromASNObject(const ASN1T_CMS_RecipientIdentifier& iRID)
{
	mType = (RIDType) iRID.t;
	if ( mType == T_IssuerAndSerial )
		mIssuerAndSerialNumber.copyFromASNObject(*iRID.u.issuerAndSerialNumber);
	else if ( mType == T_SubjectKeyIdentifier )
		mSubjectKeyIdentifier = QByteArray((const char*) (*iRID.u.subjectKeyIdentifier).data,(*iRID.u.subjectKeyIdentifier).numocts);

	return SUCCESS;
}

int RecipientIdentifier::copyToASNObject(ASN1T_CMS_RecipientIdentifier & oRID)const
{
	oRID.t = mType;
	if ( mType == T_IssuerAndSerial )
		oRID.u.issuerAndSerialNumber = mIssuerAndSerialNumber.getASNCopy();
	else if ( mType == T_SubjectKeyIdentifier )
	{
		oRID.u.subjectKeyIdentifier				= new ASN1T_CMS_SubjectKeyIdentifier();
		oRID.u.subjectKeyIdentifier->data		= (OSOCTET*) myStrDup(mSubjectKeyIdentifier.data(),mSubjectKeyIdentifier.size());
		oRID.u.subjectKeyIdentifier->numocts	= mSubjectKeyIdentifier.size(); 
	}

	return SUCCESS;
}

void RecipientIdentifier::freeASNObject(ASN1T_CMS_RecipientIdentifier & oRID)const
{
	if ( oRID.t == T_IssuerAndSerial )
		IssuerAndSerialNumber().freeASNObjectPtr(oRID.u.issuerAndSerialNumber);
	else if ( oRID.t == T_SubjectKeyIdentifier )
	{
		DELETE_MEMORY_ARRAY(oRID.u.subjectKeyIdentifier->data)
		DELETE_MEMORY(oRID.u.subjectKeyIdentifier) 
	}
}


const RIDType & RecipientIdentifier::getType() const 
{
	return mType;
}

const IssuerAndSerialNumber & RecipientIdentifier::getIssuerAndSerialNumber() const
{
	return mIssuerAndSerialNumber;
}

const QByteArray & RecipientIdentifier::getSubjectKeyIdentifier() const
{
	return mSubjectKeyIdentifier;
}

void RecipientIdentifier::setIssuerAndSerialNumber( const IssuerAndSerialNumber & iISN)
{
	mType = T_IssuerAndSerial;
	mIssuerAndSerialNumber = iISN;
}

void RecipientIdentifier::setSubjectKeyIdentifier( const QByteArray & iSKI)
{
	mType = T_SubjectKeyIdentifier;
	mSubjectKeyIdentifier = iSKI;
}

bool RecipientIdentifier::isMatch(const ECertificate & iCert)const
{
	if (mType == T_IssuerAndSerial)
		return (mIssuerAndSerialNumber.getIssuer() == iCert.getTBSCertificate().getIssuer() && mIssuerAndSerialNumber.getSerialNumber() == iCert.getTBSCertificate().getSerialNumber());
	else 
	{
		SubjectKeyIdentifier ski;
		int res = ExtensionGenerator::getSKIExtension(iCert.getTBSCertificate().getExtensions(),ski);
		if (res<0)
		{
			return false;
		}
		return (ski.getEncodedBytes() == mSubjectKeyIdentifier);
	}
	return false;
}

QString RecipientIdentifier::toString() const
{
	if (mType == T_IssuerAndSerial )
	{
		return mIssuerAndSerialNumber.toString();
	}
	else
	{
		return "SUBJECTKEYIDENTIFIER";
	}
}


RecipientIdentifier::~RecipientIdentifier(void)
{
}

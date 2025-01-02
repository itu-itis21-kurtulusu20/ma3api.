#include "KeyAgreeRecipientIdentifier.h"
#include "ExtensionGenerator.h"

using namespace esya;

KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(void)
{
}


KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(const KeyAgreeRecipientIdentifier & iSID)
:	mType(iSID.getType()), 
	mIssuerAndSerialNumber(iSID.getIssuerAndSerialNumber()),
	mRKeyId(iSID.getRKeyId())
{
}

KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(const ASN1T_CMS_KeyAgreeRecipientIdentifier & iSID)
{
	copyFromASNObject(iSID);
}

KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(const QByteArray & iSID)
{
	constructObject(iSID);	
}

KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(const IssuerAndSerialNumber & iISN )
:mType(KeyAgreeRecipientIdentifier::KARIType_issuerAndSerialNumber),mIssuerAndSerialNumber(iISN)
{
}

KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifier(const ECertificate & iCert, const KeyAgreeRecipientIdentifierType & iType)
{
	mType = iType;
	if (mType == KARIType_issuerAndSerialNumber )
	{
		mIssuerAndSerialNumber = IssuerAndSerialNumber(iCert);
	}
	else
	{
		Extension skiExt;
		int res = iCert.getTBSCertificate().getExtension(IMP_id_ce_subjectKeyIdentifier,skiExt);
		if (res != SUCCESS)
			throw EException("SubjectKeyIdentifier uzantýsý bulunamadý.",__FILE__,__LINE__);
		
		mRKeyId.setSKI(SubjectKeyIdentifier(skiExt.getExtensionValue()));
	}
}



KeyAgreeRecipientIdentifier & KeyAgreeRecipientIdentifier::operator=(const KeyAgreeRecipientIdentifier &iKARI)
{
	mType					= iKARI.getType(); 
	mIssuerAndSerialNumber	= iKARI.getIssuerAndSerialNumber();

	mRKeyId = iKARI.getRKeyId();
	return (*this);
}

bool esya::operator==(const KeyAgreeRecipientIdentifier & iRHS, const KeyAgreeRecipientIdentifier& iLHS)
{
	if (iRHS.getType() == iLHS.getType())
	{
		if (iRHS.getType() == KeyAgreeRecipientIdentifier::KARIType_issuerAndSerialNumber )
		{
			return (iRHS.getIssuerAndSerialNumber() == iLHS.getIssuerAndSerialNumber() );
		}
		else
		{
			return (iRHS.getRKeyId()== iLHS.getRKeyId());
		}
	}
	return false;
}

bool esya::operator!=(const KeyAgreeRecipientIdentifier & iRHS, const KeyAgreeRecipientIdentifier& iLHS)
{
	return (!(iRHS==iLHS));
}


int KeyAgreeRecipientIdentifier::copyFromASNObject(const ASN1T_CMS_KeyAgreeRecipientIdentifier & iKARI)
{
	mType = (KeyAgreeRecipientIdentifierType)iKARI.t;
	if (mType == KARIType_issuerAndSerialNumber)
	{
		mIssuerAndSerialNumber.copyFromASNObject(*iKARI.u.issuerAndSerialNumber);
	}
	else
	{
		mRKeyId.copyFromASNObject(*iKARI.u.rKeyId);
	}
	return SUCCESS;
}

int KeyAgreeRecipientIdentifier::copyToASNObject(ASN1T_CMS_KeyAgreeRecipientIdentifier & oKARI ) const
{
	oKARI.t = mType;
	if (mType == KARIType_issuerAndSerialNumber )
	{
		oKARI.u.issuerAndSerialNumber = mIssuerAndSerialNumber.getASNCopy() ;
	}
	else
	{
		oKARI.u.rKeyId = mRKeyId.getASNCopy();
	}
	return SUCCESS;
}
	
void KeyAgreeRecipientIdentifier::freeASNObject(ASN1T_CMS_KeyAgreeRecipientIdentifier & oKARI)const
{
	if (oKARI.t == T_CMS_RecipientIdentifier_issuerAndSerialNumber )
	{
		if (oKARI.u.issuerAndSerialNumber)
		{
			IssuerAndSerialNumber().freeASNObjectPtr(oKARI.u.issuerAndSerialNumber);
		}
	}
	else
	{
		if (oKARI.u.rKeyId)
		{ 
			RecipientKeyIdentifier().freeASNObjectPtr(oKARI.u.rKeyId);
		}
	}	
}


const KeyAgreeRecipientIdentifier::KeyAgreeRecipientIdentifierType& KeyAgreeRecipientIdentifier::getType() const
{
	return mType;
}

const IssuerAndSerialNumber& KeyAgreeRecipientIdentifier::getIssuerAndSerialNumber() const
{
	return mIssuerAndSerialNumber;
}


const RecipientKeyIdentifier & KeyAgreeRecipientIdentifier::getRKeyId() const 
{
	return mRKeyId;
}

QString KeyAgreeRecipientIdentifier::toString() const
{
	if (mType == KARIType_issuerAndSerialNumber )
	{
		return QString("Issuer: %1 \n SerialNumber: %2").arg(mIssuerAndSerialNumber.getIssuer().toString(),mIssuerAndSerialNumber.getSerialNumber().getValue());
	}
	else
	{
		return "RECIPIENTKEYIDENTIFIER";
	}
}

bool KeyAgreeRecipientIdentifier::isEqual(const ECertificate & iRecipient)const
{
	if (mType == KARIType_issuerAndSerialNumber)
	{
		return mIssuerAndSerialNumber.isEqual(iRecipient);
	}
	else if (mType == KARIType_rKeyId)
	{
		SubjectKeyIdentifier ski;
		int found = ExtensionGenerator::getSKIExtension(iRecipient.getTBSCertificate().getExtensions(),ski);
		if (found>=0)
		{
			return ( mRKeyId.getSKI() == ski);	
		}
	}
	return false;
}

KeyAgreeRecipientIdentifier::~KeyAgreeRecipientIdentifier(void)
{
}

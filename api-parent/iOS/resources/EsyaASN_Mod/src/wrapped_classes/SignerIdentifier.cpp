#include "SignerIdentifier.h"
#include "ExtensionGenerator.h"

using namespace esya;

SignerIdentifier::SignerIdentifier(void)
{
}


SignerIdentifier::SignerIdentifier(const SignerIdentifier & iSID)
:	mType(iSID.getType()), 
	mIssuer(iSID.getIssuer()),
	mSerialNumber(iSID.getSerialNumber()),
	mSubjectKeyIdentifier(iSID.getSubjectKeyIdentifier())
{
}

SignerIdentifier::SignerIdentifier(const ASN1T_CMS_SignerIdentifier & iSID)
{
	copyFromASNObject(iSID);
}

SignerIdentifier::SignerIdentifier(const QByteArray & iSID)
{
	constructObject(iSID);	
}

SignerIdentifier::SignerIdentifier(const Name & iIssuer, const SerialNumber &iSerialNumber)
:mType(issuerAndSerialNumber),mIssuer(iIssuer),mSerialNumber(iSerialNumber)
{
}

SignerIdentifier::SignerIdentifier(const ECertificate & iCert, const SignerIdentifierType & iType)
{
	mType = iType;
	if (mType == T_CMS_SignerIdentifier_issuerAndSerialNumber )
	{
		mIssuer = iCert.getTBSCertificate().getIssuer();
		mSerialNumber = iCert.getTBSCertificate().getSerialNumber();
	}
	else
	{
		Extension skiExt;
		int res = iCert.getTBSCertificate().getExtension(IMP_id_ce_subjectKeyIdentifier,skiExt);
		if (res != SUCCESS)
			throw EException("SubjectKeyIdentifier uzantýsý bulunamadý.",__FILE__,__LINE__);
		
		mSubjectKeyIdentifier = skiExt.getExtensionValue();;
	}
}



SignerIdentifier & SignerIdentifier::operator=(const SignerIdentifier &iSID)
{
	mType			= iSID.getType(); 
	mIssuer			= iSID.getIssuer();
	mSerialNumber	=	iSID.getSerialNumber();
	mSubjectKeyIdentifier = iSID.getSubjectKeyIdentifier();
	return (*this);
}

bool esya::operator==(const SignerIdentifier & iRHS, const SignerIdentifier& iLHS)
{
	if (iRHS.getType() == iLHS.getType())
	{
		if (iRHS.getType() == T_CMS_SignerIdentifier_issuerAndSerialNumber )
		{
			return (iRHS.getIssuer() == iLHS.getIssuer() && iRHS.getSerialNumber() == iLHS.getSerialNumber());
		}
		else
		{
			return (iRHS.getSubjectKeyIdentifier()== iLHS.getSubjectKeyIdentifier());
		}
	}
	return false;
}

bool esya::operator!=(const SignerIdentifier & iRHS, const SignerIdentifier& iLHS)
{
	return (!(iRHS==iLHS));
}


int SignerIdentifier::copyFromASNObject(const ASN1T_CMS_SignerIdentifier & iSID)
{
	mType = (SignerIdentifierType)iSID.t;
	if (mType == T_CMS_SignerIdentifier_issuerAndSerialNumber )
	{
		mIssuer.copyFromASNObject(iSID.u.issuerAndSerialNumber->issuer);
		mSerialNumber.copyFromASNObject(iSID.u.issuerAndSerialNumber->serialNumber);
	}
	else
	{
		mSubjectKeyIdentifier = QByteArray((char*)iSID.u.subjectKeyIdentifier->data,iSID.u.subjectKeyIdentifier->numocts);
	}
	return SUCCESS;
}

int SignerIdentifier::copyToASNObject(ASN1T_CMS_SignerIdentifier & oSID ) const
{
	oSID.t = mType;
	if (mType == T_CMS_SignerIdentifier_issuerAndSerialNumber )
	{
		oSID.u.issuerAndSerialNumber = new ASN1T_PKCS7_IssuerAndSerialNumber();
		mIssuer.copyToASNObject(oSID.u.issuerAndSerialNumber->issuer) ;
		mSerialNumber.copyToASNObject(oSID.u.issuerAndSerialNumber->serialNumber);
	}
	else
	{
		oSID.u.subjectKeyIdentifier = new ASN1T_CMS_SubjectKeyIdentifier();
		oSID.u.subjectKeyIdentifier->data = (OSOCTET*) myStrDup(mSubjectKeyIdentifier.data(),mSubjectKeyIdentifier.size());
		oSID.u.subjectKeyIdentifier->numocts = mSubjectKeyIdentifier.size();
	}
	return SUCCESS;
}
	
void SignerIdentifier::freeASNObject(ASN1T_CMS_SignerIdentifier & oSID)const
{
	if (oSID.t == T_CMS_SignerIdentifier_issuerAndSerialNumber )
	{
		if (oSID.u.issuerAndSerialNumber)
		{
			Name().freeASNObject(oSID.u.issuerAndSerialNumber->issuer);
			SerialNumber().freeASNObject(oSID.u.issuerAndSerialNumber->serialNumber);
		}
		DELETE_MEMORY(oSID.u.issuerAndSerialNumber)
	}
	else
	{
		if (oSID.u.subjectKeyIdentifier)
		{ 
			DELETE_MEMORY_ARRAY( oSID.u.subjectKeyIdentifier->data )
		}
		DELETE_MEMORY(oSID.u.subjectKeyIdentifier)
	}	
}

const SignerIdentifier::SignerIdentifierType& SignerIdentifier::getType() const
{
	return mType;
}

const Name& SignerIdentifier::getIssuer() const
{
	return mIssuer;
}

const SerialNumber & SignerIdentifier::getSerialNumber() const 
{
	return mSerialNumber;
}

const QByteArray & SignerIdentifier::getSubjectKeyIdentifier() const 
{
	return mSubjectKeyIdentifier;
}

QString SignerIdentifier::toString() const
{
	if (mType == T_CMS_SignerIdentifier_issuerAndSerialNumber )
	{
		return QString("Issuer: %1 \n SerialNumber: %2").arg(mIssuer.toString(),mSerialNumber.getValue());
	}
	else
	{
		return "SUBJECTKEYIDENTIFIER";
	}
}

bool SignerIdentifier::isMatch(const ECertificate & iCert)const
{
	if (mType == issuerAndSerialNumber)
		return (mIssuer == iCert.getTBSCertificate().getIssuer() && mSerialNumber == iCert.getTBSCertificate().getSerialNumber());
	else 
	{
		SubjectKeyIdentifier ski;
		int res = ExtensionGenerator::getSKIExtension(iCert.getTBSCertificate().getExtensions(),ski);
		if (res<0)
		{
			return false;
		}
		return (ski == mSubjectKeyIdentifier);
	}
	return false;
}

SignerIdentifier::~SignerIdentifier(void)
{
}

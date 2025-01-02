#include "IssuerAndSerialNumber.h"

using namespace esya;


uint qHash(const IssuerAndSerialNumber& key)
{
	return (key.getIssuer().getList().size() + key.getSerialNumber().getValue().length());
}

IssuerAndSerialNumber::IssuerAndSerialNumber(void)
{
}

IssuerAndSerialNumber::IssuerAndSerialNumber( const ASN1T_PKCS7_IssuerAndSerialNumber &iISN)
{
	copyFromASNObject(iISN);
}

IssuerAndSerialNumber::IssuerAndSerialNumber( const IssuerAndSerialNumber &iISN)
:	mSerialNumber(iISN.getSerialNumber()),
	mIssuer(iISN.getIssuer())
{
}

IssuerAndSerialNumber::IssuerAndSerialNumber( const ECertificate &iCert)
:	mSerialNumber(iCert.getTBSCertificate().getSerialNumber()),
	mIssuer(iCert.getTBSCertificate().getIssuer())
{
}

IssuerAndSerialNumber::IssuerAndSerialNumber( const QByteArray &iISN)
{
	constructObject(iISN);
}

IssuerAndSerialNumber::IssuerAndSerialNumber( const Name& iI,const SerialNumber& iSN)
:	mSerialNumber(iSN),
	mIssuer(iI)
{
}

IssuerAndSerialNumber & IssuerAndSerialNumber::operator=(const IssuerAndSerialNumber& iISN)
{
	mSerialNumber	= iISN.getSerialNumber();
	mIssuer			= iISN.getIssuer();
	return(*this);

}

bool esya::operator==(const IssuerAndSerialNumber& iRHS,const IssuerAndSerialNumber& iLHS)
{
	return (	(iRHS.getSerialNumber() == iLHS.getSerialNumber()) &&
				(iRHS.getIssuer()		== iLHS.getIssuer())		);
}

bool esya::operator!=(const IssuerAndSerialNumber& iRHS, const IssuerAndSerialNumber& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int IssuerAndSerialNumber::copyFromASNObject(const ASN1T_PKCS7_IssuerAndSerialNumber & iISN)
{
	mSerialNumber.copyFromASNObject(iISN.serialNumber);
	mIssuer.copyFromASNObject(iISN.issuer);
	return SUCCESS;
}

int IssuerAndSerialNumber::copyToASNObject(ASN1T_PKCS7_IssuerAndSerialNumber & oISN ) const
{
	mSerialNumber.copyToASNObject(oISN.serialNumber); 
	mIssuer.copyToASNObject(oISN.issuer);
	return SUCCESS;
}
	
void IssuerAndSerialNumber::freeASNObject(ASN1T_PKCS7_IssuerAndSerialNumber& oISN)const
{
	SerialNumber().freeASNObject(oISN.serialNumber);
	Name().freeASNObject(oISN.issuer);
}

const SerialNumber &IssuerAndSerialNumber::getSerialNumber() const 
{
	return mSerialNumber;
}
	
const Name &IssuerAndSerialNumber::getIssuer() const 
{
	return mIssuer;
}

bool IssuerAndSerialNumber::isEqual(const ECertificate & iCert)const
{
	return (	( mIssuer == iCert.getTBSCertificate().getIssuer()				) && 
				( mSerialNumber == iCert.getTBSCertificate().getSerialNumber()	)	);

}


QString IssuerAndSerialNumber::toString()const
{
	return QString(" %1(%2)").arg(mIssuer.toTitle().arg(mSerialNumber.getValue()));
}

IssuerAndSerialNumber::~IssuerAndSerialNumber()
{
}

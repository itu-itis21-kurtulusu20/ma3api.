#include "ECRL.h"

using namespace esya;

ECRL::ECRL(void)
{
}

ECRL::ECRL(const ECRL &iCRL)
:mCRL(iCRL.getCRL())
{
}

ECRL::ECRL(const ASN1T_PKCS7_CertificateRevocationLists & iCRL)
{
	copyFromASNObject(iCRL);
}

ECRL::ECRL(const QByteArray & iCRL)
{
	constructObject(iCRL);
}

ECRL::ECRL(const QString & iCRLFile)
{
	loadFromFile(iCRLFile);
}


ECRL& ECRL::operator=(const ECRL&iCRL)
{
	mCRL = iCRL.getCRL();
	return *this;
}

bool esya::operator==(const ECRL& iRHS, const ECRL& iLHS)
{
	return ( iRHS.getCRL() == iLHS.getCRL() );
}

bool esya::operator!=(const ECRL& iRHS, const ECRL& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int ECRL::copyFromASNObject(const ASN1T_PKCS7_CertificateRevocationLists& iCRL)
{
	ECertificateList().copyCRL(iCRL,mCRL);
	return SUCCESS;
}

int ECRL::copyToASNObject(ASN1T_PKCS7_CertificateRevocationLists & oCRL) const
{
	ECertificateList().copyCRL(mCRL,oCRL);
	return SUCCESS;
}

void ECRL::freeASNObject(ASN1T_PKCS7_CertificateRevocationLists & oCRL)const
{
	ECertificateList().freeASNObjects(oCRL);
}

const QList<ECertificateList> &ECRL::getCRL()const 
{
	return mCRL;
}

ECRL::~ECRL(void)
{
}

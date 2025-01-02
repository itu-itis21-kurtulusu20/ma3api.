#include "PKCS7IssuerSerial.h"

using namespace esya;

namespace esya
{

	PKCS7IssuerSerial::PKCS7IssuerSerial(void)
	{
	}

	PKCS7IssuerSerial::PKCS7IssuerSerial( const ASN1T_PKCS7_IssuerSerial & iIS)
	{
		copyFromASNObject(iIS);
	}

	PKCS7IssuerSerial::PKCS7IssuerSerial( const QByteArray &iIS)
	{
		constructObject(iIS);
	}

	PKCS7IssuerSerial::PKCS7IssuerSerial( const PKCS7IssuerSerial &iIS)
	:	mIssuer(iIS.getIssuer()),
		mSerialNumber(iIS.getSerialNumber())
	{
	}

	PKCS7IssuerSerial::PKCS7IssuerSerial( const GeneralNames&iIssuer,const SerialNumber& iSN)
	:	mIssuer(iIssuer),
		mSerialNumber(iSN)
	{
	}

	PKCS7IssuerSerial::PKCS7IssuerSerial( const ECertificate& iCert)
	{
		GeneralName gn;
		gn.setDirectoryName(iCert.getTBSCertificate().getIssuer());
		mIssuer.appendGeneralName(gn);
		mSerialNumber = iCert.getTBSCertificate().getSerialNumber();
	}

	PKCS7IssuerSerial::~PKCS7IssuerSerial()
	{	
	}


	PKCS7IssuerSerial & PKCS7IssuerSerial::operator=(const PKCS7IssuerSerial& iIS)
	{
		mIssuer				= iIS.getIssuer();
		mSerialNumber		= iIS.getSerialNumber();
		return *this;
	}

	bool operator==(const PKCS7IssuerSerial& iRHS,const PKCS7IssuerSerial& iLHS)
	{
		return  (	( iRHS.getIssuer()			== iLHS.getIssuer() ) &&
					( iRHS.getSerialNumber()	== iLHS.getSerialNumber() ) );
	}

	bool operator!=(const PKCS7IssuerSerial& iRHS, const PKCS7IssuerSerial& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int PKCS7IssuerSerial::copyFromASNObject(const ASN1T_PKCS7_IssuerSerial & iIS)
	{
		mIssuer.copyFromASNObject(iIS.issuer);
		mSerialNumber.copyFromASNObject(iIS.serialNumber);
		return SUCCESS;
	}

	int PKCS7IssuerSerial::copyToASNObject(ASN1T_PKCS7_IssuerSerial & oIS) const
	{
		mIssuer.copyToASNObject(oIS.issuer);
		mSerialNumber.copyToASNObject(oIS.serialNumber);
		return SUCCESS;
	}

	void PKCS7IssuerSerial::freeASNObject(ASN1T_PKCS7_IssuerSerial& oIS) const
	{
		GeneralNames().freeASNObject(oIS.issuer);
		SerialNumber().freeASNObject(oIS.serialNumber);
	}

	const GeneralNames & PKCS7IssuerSerial::getIssuer() const 
	{
		return mIssuer;
	}

	const SerialNumber & PKCS7IssuerSerial::getSerialNumber() const 
	{
		return mSerialNumber;
	}

	void PKCS7IssuerSerial::setIssuer(const GeneralNames & iIssuer)
	{
		mIssuer = iIssuer;
	}

	void PKCS7IssuerSerial::setSerialNumber(const SerialNumber & iSN) 
	{
		mSerialNumber = iSN;
	}

}
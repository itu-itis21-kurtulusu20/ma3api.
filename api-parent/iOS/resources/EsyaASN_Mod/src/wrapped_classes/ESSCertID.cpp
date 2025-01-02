#include "ESSCertID.h"

using namespace esya;

namespace esya
{

	ESSCertID::ESSCertID(void)
	: mIssuerSerialPresent(false)
	{
	}

	ESSCertID::ESSCertID( const ASN1T_PKCS7_ESSCertID & iESSCertID)
	: mIssuerSerialPresent(false)
	{
		copyFromASNObject(iESSCertID);
	}

	ESSCertID::ESSCertID( const QByteArray &iESSCertID)
	: mIssuerSerialPresent(false)
	{
		constructObject(iESSCertID);
	}

	ESSCertID::ESSCertID( const ESSCertID &iESSCertID)
	:	mIssuerSerialPresent(iESSCertID.isIssuerSerialPresent()),
		mIssuerSerial(iESSCertID.getIssuerSerial()),
		mHash(iESSCertID.getHash())
	{
	}

	ESSCertID::~ESSCertID()
	{	
	}


	ESSCertID & ESSCertID::operator=(const ESSCertID& iESSCertID)
	{
		mIssuerSerialPresent	= iESSCertID.isIssuerSerialPresent();
		mIssuerSerial			= iESSCertID.getIssuerSerial();
		mHash					= iESSCertID.getHash();
		return *this;
	}

	bool operator==(const ESSCertID& iRHS,const ESSCertID& iLHS)
	{
		if ( ( iRHS.getHash()				!= iLHS.getHash() ) ||
			 ( iRHS.isIssuerSerialPresent()	!= iLHS.isIssuerSerialPresent() ))
			 return false;

		if ( ( iRHS.isIssuerSerialPresent()) &&
			 ( iRHS.getIssuerSerial() != iLHS.getIssuerSerial()) )
			return false;

		return true;
	}

	bool operator!=(const ESSCertID& iRHS, const ESSCertID& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int ESSCertID::copyFromASNObject(const ASN1T_PKCS7_ESSCertID & iESSCertID)
	{
		mIssuerSerialPresent = iESSCertID.m.issuerSerialPresent;
		if (mIssuerSerialPresent)
			mIssuerSerial.copyFromASNObject(iESSCertID.issuerSerial);
		
		mHash.copyFromASNObject(iESSCertID.certHash);
		return SUCCESS;
	}

	int ESSCertID::copyToASNObject(ASN1T_PKCS7_ESSCertID & oESSCertID) const
	{
		oESSCertID.m.issuerSerialPresent = mIssuerSerialPresent;
		if (mIssuerSerialPresent)
			mIssuerSerial.copyToASNObject(oESSCertID.issuerSerial);
		
		mHash.copyToASNObject(oESSCertID.certHash);
		return SUCCESS;
	}

	void ESSCertID::freeASNObject(ASN1T_PKCS7_ESSCertID& oESSCertID) const
	{
		if (oESSCertID.m.issuerSerialPresent)
			PKCS7IssuerSerial().freeASNObject(oESSCertID.issuerSerial);
		
		PKCS7Hash().freeASNObject(oESSCertID.certHash);
	}

	bool ESSCertID::isIssuerSerialPresent()const
	{
		return mIssuerSerialPresent;
	}

	const PKCS7IssuerSerial & ESSCertID::getIssuerSerial() const 
	{
		return mIssuerSerial;
	}

	const PKCS7Hash & ESSCertID::getHash() const 
	{
		return mHash;
	}

	void ESSCertID::setIssuerSerial(const PKCS7IssuerSerial & iIssuerSerial)
	{
		mIssuerSerialPresent = true;
		mIssuerSerial = iIssuerSerial;
	}

	void ESSCertID::setHash(const PKCS7Hash & iHash) 
	{
		mHash = iHash;
	}

}
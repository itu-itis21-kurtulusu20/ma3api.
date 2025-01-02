#include "IssuerSerial.h"

using namespace esya;

namespace esya
{

	IssuerSerial::IssuerSerial(void)
	: mIssuerUIDPresent(false)
	{
	}

	IssuerSerial::IssuerSerial( const ASN1T_ATTRCERT_IssuerSerial & iIS)
	{
		copyFromASNObject(iIS);
	}

	IssuerSerial::IssuerSerial( const QByteArray &iIS)
	{
		constructObject(iIS);
	}

	IssuerSerial::IssuerSerial( const IssuerSerial &iIS)
	:	mIssuer(iIS.getIssuer()),
		mSerialNumber(iIS.getSerialNumber()),
		mIssuerUIDPresent(iIS.isIssuerUIDPresent()),
		mIssuerUID(iIS.getIssuerUID())
	{
	}

	IssuerSerial::~IssuerSerial()
	{	
	}


	IssuerSerial & IssuerSerial::operator=(const IssuerSerial& iIS)
	{
		mIssuer				= iIS.getIssuer();
		mSerialNumber		= iIS.getSerialNumber();
		mIssuerUIDPresent	= iIS.isIssuerUIDPresent();
		mIssuerUID			= iIS.getIssuerUID();
		return *this;
	}

	bool operator==(const IssuerSerial& iRHS,const IssuerSerial& iLHS)
	{
		if ( ( iRHS.getIssuer()			!= iLHS.getIssuer() ) ||
			 ( iRHS.getSerialNumber()	!= iLHS.getSerialNumber() ) ||
			 ( iRHS.isIssuerUIDPresent()!= iLHS.isIssuerUIDPresent() ) )
			 return false;

		if ( iRHS.isIssuerUIDPresent() && ( iRHS.getIssuerUID() != iLHS.getIssuerUID()))
			return false;

		return true;
	}

	bool operator!=(const IssuerSerial& iRHS, const IssuerSerial& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int IssuerSerial::copyFromASNObject(const ASN1T_ATTRCERT_IssuerSerial & iIS)
	{
		mIssuer.copyFromASNObject(iIS.issuer);
		mSerialNumber.copyFromASNObject(iIS.serial);
		mIssuerUIDPresent = iIS.m.issuerUIDPresent;
		if (mIssuerUIDPresent)
			mIssuerUID.copyFromASNObject(iIS.issuerUID);

		return SUCCESS;
	}

	int IssuerSerial::copyToASNObject(ASN1T_ATTRCERT_IssuerSerial & oIS) const
	{
		mIssuer.copyToASNObject(oIS.issuer);
		mSerialNumber.copyToASNObject(oIS.serial);
		oIS.m.issuerUIDPresent = mIssuerUIDPresent ? 1:0;

		if (mIssuerUIDPresent)
			mIssuerUID.copyToASNObject(oIS.issuerUID);

		return SUCCESS;
	}

	void IssuerSerial::freeASNObject(ASN1T_ATTRCERT_IssuerSerial& oIS) const
	{
		GeneralNames().freeASNObject(oIS.issuer);
		SerialNumber().freeASNObject(oIS.serial);
		if (oIS.m.issuerUIDPresent)
			EBitString::freeASNObject(oIS.issuerUID);
	}

	bool IssuerSerial::isIssuerUIDPresent()const
	{
		return mIssuerUIDPresent;
	}

	const GeneralNames & IssuerSerial::getIssuer() const 
	{
		return mIssuer;
	}

	const SerialNumber & IssuerSerial::getSerialNumber() const 
	{
		return mSerialNumber;
	}

	const EBitString &   IssuerSerial::getIssuerUID()const
	{
		return mIssuerUID;
	}

	void IssuerSerial::setIssuer(const GeneralNames & iIssuer)
	{
		mIssuer = iIssuer;
	}

	void IssuerSerial::setSerialNumber(const SerialNumber & iSN) 
	{
		mSerialNumber = iSN;
	}

	void IssuerSerial::setIssuerUID(const EBitString &  iIssuerUID)
	{
		mIssuerUID = iIssuerUID;
	}

	void IssuerSerial::setIssuerUIDPresent(bool iIssuerUIDPresent)
	{
		mIssuerUIDPresent = iIssuerUIDPresent;
	}
	
}
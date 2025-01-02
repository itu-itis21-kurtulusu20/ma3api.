
#include "ACClearAttrs.h"
#include "ESeqOfList.h"


using namespace esya;

namespace esya
{

	ACClearAttrs::ACClearAttrs(void)
	{
	}

	ACClearAttrs::ACClearAttrs(const QByteArray & iACClearAttrs)
	{
		constructObject(iACClearAttrs);
	}

	ACClearAttrs::ACClearAttrs(const ASN1T_ATTRCERT_ACClearAttrs & iACClearAttrs )
	{
		copyFromASNObject(iACClearAttrs);
	}

	ACClearAttrs::ACClearAttrs(const ACClearAttrs& iACClearAttrs)
	:	mACIssuer(iACClearAttrs.getACIssuer()),
		mACSerial(iACClearAttrs.getACSerial()),
		mAttrs(iACClearAttrs.getAttrs())
	{
	}

	ACClearAttrs& ACClearAttrs::operator=(const ACClearAttrs& iACClearAttrs)
	{
		mACIssuer	= iACClearAttrs.getACIssuer();
		mACSerial	= iACClearAttrs.getACSerial();
		mAttrs		= iACClearAttrs.getAttrs();

		return *this;
	}

	bool operator==( const ACClearAttrs& iRHS, const ACClearAttrs& iLHS)
	{
		return  (	( iRHS.getACIssuer()	!= iLHS.getACIssuer() )	&&  
					( iRHS.getACSerial()	!= iLHS.getACSerial() )	&&
					( iRHS.getAttrs()		!= iLHS.getAttrs()	)		);
	}

	bool operator!=( const ACClearAttrs& iRHS, const ACClearAttrs& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int ACClearAttrs::copyFromASNObject(const ASN1T_ATTRCERT_ACClearAttrs & iACClearAttrs)
	{
		mACIssuer.copyFromASNObject(iACClearAttrs.acIssuer);
		mACSerial = iACClearAttrs.acSerial;
		Attribute().copyAttributeList(iACClearAttrs.attrs,mAttrs);

		return SUCCESS;
	}

	int ACClearAttrs::copyToASNObject(ASN1T_ATTRCERT_ACClearAttrs &oACClearAttrs) const 
	{
		mACIssuer.copyToASNObject(oACClearAttrs.acIssuer);
		oACClearAttrs.acSerial = mACSerial;
		Attribute().copyAttributeList(mAttrs,oACClearAttrs.attrs);

		return SUCCESS;
	}


	void ACClearAttrs::freeASNObject(ASN1T_ATTRCERT_ACClearAttrs& oACClearAttrs)
	{
		GeneralName().freeASNObject(oACClearAttrs.acIssuer);
		Attribute().freeASNObjects(oACClearAttrs.attrs);
	}

	const GeneralName& ACClearAttrs::getACIssuer()const
	{
		return mACIssuer;
	}

	const int& ACClearAttrs::getACSerial()const
	{
		return mACSerial;
	}

	const QList<Attribute>&	ACClearAttrs::getAttrs()const
	{
		return mAttrs;
	}

	void ACClearAttrs::setACIssuer(const GeneralName&  iACIssuer)
	{
		mACIssuer = iACIssuer;
	}

	void ACClearAttrs::setACSerial(const int& iACSerial)
	{
		mACSerial = iACSerial;
	}

	void ACClearAttrs::setAttrs(const QList<Attribute>& iAttrs)
	{
		mAttrs = iAttrs;
	}

	void ACClearAttrs::appendAttribute(const Attribute& iAttr)
	{
		mAttrs.append(iAttr);
	}

	ACClearAttrs::~ACClearAttrs(void)
	{
	}

}
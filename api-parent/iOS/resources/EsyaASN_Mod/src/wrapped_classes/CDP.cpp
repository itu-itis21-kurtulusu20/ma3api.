#include "CDP.h"

using namespace esya;

CDP::CDP(void)
: mCRLIssuer(NULL)
{
}

CDP::CDP(const QString& iAdresTipi, const QString & iCDPAdresi, const Name* iCRLIssuer, const QString & iReasonFlags)
:	mAdresTipi(iAdresTipi),
	mCDPAdresi(iCDPAdresi),
	mReasonFlags(iReasonFlags),
	mCRLIssuer(NULL)
{
	if (iCRLIssuer)
		mCRLIssuer = new Name(*iCRLIssuer);

}

CDP::CDP(const CDP& iCDP)
:	mAdresTipi(iCDP.adresTipiAl()),
	mCDPAdresi(iCDP.cdpAdresiAl()),
	mReasonFlags(iCDP.reasonFlagsAl()),
	mCRLIssuer(NULL)
{
	if (iCDP.crlIssuerAl())
		mCRLIssuer = new Name(*(iCDP.crlIssuerAl()));
}

CDP & CDP::operator=(const CDP& iCDP) 
{
	mAdresTipi		= iCDP.adresTipiAl();
	mCDPAdresi		= iCDP.cdpAdresiAl();
	mReasonFlags	= iCDP.reasonFlagsAl();
	mCRLIssuer		= NULL;

	if (iCDP.crlIssuerAl() && iCDP.crlIssuerAl() != mCRLIssuer )
		mCRLIssuer		= new Name(*(iCDP.crlIssuerAl()));

	return *this;
}

bool esya::operator==(const CDP& iRHS , const CDP& iLHS)
{
	if ( !(	( iRHS.adresTipiAl()	== iLHS.adresTipiAl()	) &&
			( iRHS.cdpAdresiAl()	== iLHS.cdpAdresiAl()	) &&
			( iRHS.reasonFlagsAl()	== iLHS.reasonFlagsAl()	)	) )
			return false;

	if ( iRHS.crlIssuerAl())
	{
		if (!iLHS.crlIssuerAl() || *(iLHS.crlIssuerAl()) != *(iRHS.crlIssuerAl()))
			return false;
	}

	return true;
}

bool esya::operator!=(const CDP& iRHS, const CDP& iLHS)
{
	return ( !(iRHS==iLHS) );
}

const QString & CDP::adresTipiAl()const
{
	return mAdresTipi;
}
const QString & CDP::cdpAdresiAl()const
{
	return mCDPAdresi;
}
const Name	* CDP::crlIssuerAl()const
{
	return mCRLIssuer;
}
const QString & CDP::reasonFlagsAl()const
{
	return mReasonFlags;
}

void CDP::adresTipiBelirle(const QString & iAdresTipi)
{
	mAdresTipi = iAdresTipi;
}

void CDP::cdpAdresiBelirle(const QString & iCDPAdresi )
{
	mCDPAdresi = iCDPAdresi;
}

void CDP::crlIssuerBelirle(const Name* iCRLIssuer)
{
	if ( mCRLIssuer != iCRLIssuer )
	{
		DELETE_MEMORY(mCRLIssuer)
		mCRLIssuer = new Name(*iCRLIssuer);
	}
}

void CDP::reasonFlagsBelirle(const QString & iReasonFlags)
{
	mReasonFlags = iReasonFlags;
}	


CDP::~CDP(void)
{
	DELETE_MEMORY(mCRLIssuer)
}

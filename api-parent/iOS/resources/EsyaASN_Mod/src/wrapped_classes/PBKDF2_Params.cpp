#include "PBKDF2_Params.h"

using namespace esya;

const AlgorithmIdentifier PBKDF2_Params::DEFAULT_PRF = AlgorithmIdentifier(PKCS5_id_hmacWithSHA256);

PBKDF2_Params::PBKDF2_Params(void)
:	mPRF(DEFAULT_PRF),
	mKeyLengthPresent(false)
{
}

PBKDF2_Params::PBKDF2_Params(const ASN1T_PKCS5_PBKDF2_params & iPP)
{
	copyFromASNObject(iPP);
}

PBKDF2_Params::PBKDF2_Params(const  QByteArray &iPP)
{
	constructObject(iPP);
}

PBKDF2_Params::PBKDF2_Params(const PBKDF2_Params & iPP)
:	mKeyLengthPresent(iPP.isKeyLengthPresent()),
	mSalt(iPP.getSalt()),
	mIterationCount(iPP.getIterationCount()),
	mPRF(iPP.getPRF())
{
	if (mKeyLengthPresent)
		mKeyLength	= iPP.getKeyLength(); 
}


PBKDF2_Params& PBKDF2_Params::operator=(const PBKDF2_Params& iPP)
{
	mKeyLengthPresent	= iPP.isKeyLengthPresent();
	mSalt				= iPP.getSalt();
	mIterationCount		= iPP.getIterationCount();
	mPRF				= iPP.getPRF(); 
	if (mKeyLengthPresent)
		mKeyLength	= iPP.getKeyLength(); 

	return *this;
}

bool esya::operator==(const PBKDF2_Params & iRHS,const PBKDF2_Params & iLHS)
{
	if ( ( iRHS.isKeyLengthPresent()	!= iLHS.isKeyLengthPresent()	) || 
		 ( iRHS.getPRF()				!= iLHS.getPRF()				) ||
		 ( iRHS.getIterationCount()		!= iLHS.getIterationCount()		) ||
		 ( iRHS.getSalt()				!= iLHS.getSalt()				)	)
		 return false;

	if ( iRHS.isKeyLengthPresent() && (iRHS.getKeyLength() != iLHS.getKeyLength())  )
		return false;

	return true;
}

bool esya::operator!=(const PBKDF2_Params & iRHS, const PBKDF2_Params & iLHS)
{
	return ( !( iRHS == iLHS ) ) ;
}	


int PBKDF2_Params::copyFromASNObject(const ASN1T_PKCS5_PBKDF2_params & iPP)
{
	mSalt.copyFromASNObject(iPP.salt);
	
	mKeyLengthPresent	= iPP.m.keyLengthPresent;
	mIterationCount		= iPP.iterationCount;

	if (mKeyLengthPresent)
		mKeyLength	= iPP.keyLength; 
	if (iPP.m.prfPresent)
		mPRF.copyFromASNObject(iPP.prf); 
	else mPRF = DEFAULT_PRF; 

	return SUCCESS;
	
}

int PBKDF2_Params::copyToASNObject(ASN1T_PKCS5_PBKDF2_params & oPP) const
{
	mSalt.copyToASNObject(oPP.salt);

	oPP.m.keyLengthPresent	= mKeyLengthPresent;
	oPP.m.prfPresent		= ( mPRF != DEFAULT_PRF );
	oPP.iterationCount		= mIterationCount;

	if (mKeyLengthPresent)
		oPP.keyLength = mKeyLength; 
	if (oPP.m.prfPresent)
		mPRF.copyToASNObject(oPP.prf); 

	return SUCCESS;
}

void PBKDF2_Params::freeASNObject(ASN1T_PKCS5_PBKDF2_params & oPP)const
{
	PBKDF2_Salt().freeASNObject(oPP.salt);

	if (oPP.m.prfPresent)
		AlgorithmIdentifier().freeASNObject(oPP.prf); 

}

const bool PBKDF2_Params::isKeyLengthPresent()	const
{
	return mKeyLengthPresent;
}

const PBKDF2_Salt &	PBKDF2_Params::getSalt()const
{
	return mSalt;
}

const int PBKDF2_Params::getIterationCount()const
{
	return mIterationCount;
}

const int PBKDF2_Params::getKeyLength()	const
{
	return mKeyLength;
}

const AlgorithmIdentifier&	PBKDF2_Params::getPRF()const
{
	return mPRF;
}

void PBKDF2_Params::setSalt(const PBKDF2_Salt& iSalt)
{
	mSalt = iSalt;
}

void PBKDF2_Params::setIterationCount(const int & iIC)
{
	mIterationCount = iIC;
}

void PBKDF2_Params::setKeyLength(const int &iKeyLength)
{
	setKeyLengthPresent(true);
	mKeyLength = iKeyLength;
}

void PBKDF2_Params::setPRF(const AlgorithmIdentifier& iPRF)
{
	mPRF = iPRF;
}

void PBKDF2_Params::setKeyLengthPresent(const bool iKeyLengthPresent)
{
	mKeyLengthPresent = iKeyLengthPresent;
}

PBKDF2_Params::~PBKDF2_Params(void)
{
}

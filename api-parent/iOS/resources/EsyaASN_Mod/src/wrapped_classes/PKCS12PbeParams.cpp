#include "PKCS12PbeParams.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

PKCS12PbeParams::PKCS12PbeParams(void)
{
}

PKCS12PbeParams::PKCS12PbeParams(const QByteArray& iParams)
{
	constructObject(iParams);
}

PKCS12PbeParams::PKCS12PbeParams(const ASN1T_PKCS12_PKCS12PbeParams & iParams)
{
	copyFromASNObject(iParams);
}

PKCS12PbeParams::PKCS12PbeParams(const QByteArray & iSalt, const OSUINT32 & iIterations)
: mSalt(iSalt) , mIterations(iIterations)
{
}

PKCS12PbeParams::PKCS12PbeParams(const PKCS12PbeParams& iParams)
:	mSalt(iParams.getSalt()),
	mIterations(iParams.getIterations())
{
}

PKCS12PbeParams& PKCS12PbeParams::operator=(const PKCS12PbeParams& iParams)
{
	mSalt		= iParams.getSalt();
	mIterations = iParams.getIterations();
	return (*this);
}

bool operator==( const PKCS12PbeParams& iRHS, const PKCS12PbeParams& iLHS)
{
	return (	iRHS.getSalt() == iLHS.getSalt() && 
				iRHS.getIterations() == iLHS.getIterations() );
}

bool operator!=( const PKCS12PbeParams& iRHS, const PKCS12PbeParams& iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}

int PKCS12PbeParams::copyFromASNObject(const ASN1T_PKCS12_PKCS12PbeParams & iParams)
{
	mSalt = toByteArray(iParams.salt);
	mIterations = iParams.iterations;
	return SUCCESS;
}

int PKCS12PbeParams::copyToASNObject(ASN1T_PKCS12_PKCS12PbeParams &oParams)const
{
	oParams.salt.data = (OSOCTET*)myStrDup( mSalt.data(),mSalt.size());
	oParams.salt.numocts = mSalt.size();
	oParams.iterations = mIterations;
	return SUCCESS;
}

void PKCS12PbeParams::freeASNObject(ASN1T_PKCS12_PKCS12PbeParams& oParams)const
{
	DELETE_MEMORY_ARRAY(oParams.salt.data)
}

const QByteArray& PKCS12PbeParams::getSalt() const
{
	return mSalt;
}

const OSUINT32 PKCS12PbeParams::getIterations()const 
{
	return mIterations;
}

void PKCS12PbeParams::setSalt(const QByteArray & iSalt)
{
	mSalt = iSalt;
}
void PKCS12PbeParams::setIteration(const OSUINT32 &iIterations )
{
	mIterations = iIterations;
}

PKCS12PbeParams::~PKCS12PbeParams(void)
{
}
NAMESPACE_END

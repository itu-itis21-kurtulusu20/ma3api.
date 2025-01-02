#include "Curve.h"

using namespace esya;

Curve::Curve(void)
: mSeedPresent(false)
{
}

Curve::Curve(const QByteArray & iCurve)
{
	constructObject(iCurve);
}

Curve::Curve(const ASN1T_ALGOS_Curve & iCurve)
{
	copyFromASNObject(iCurve);
}

Curve::Curve(const Curve& iCurve)
:	mA(iCurve.getA()),
	mB(iCurve.getB()),
	mSeedPresent(iCurve.isSeedPresent()),
	mSeed(iCurve.getSeed())
{
}

Curve& Curve::operator=(const Curve& iCurve)
{
	mA				= iCurve.getA();
	mB				= iCurve.getB();
	mSeedPresent	= iCurve.isSeedPresent();
	mSeed			= iCurve.getSeed();
	return *this;
}

bool esya::operator==(const Curve & iRHS, const Curve& iLHS)
{
	return  (	(iRHS.getA()			== iLHS.getA())		&&
				(iRHS.getB()			== iLHS.getB())		&&
				(iRHS.getSeed()			== iLHS.getSeed())	&&
				(iRHS.isSeedPresent()	== iLHS.isSeedPresent()));
}

bool esya::operator!=(const Curve & iRHS, const Curve& iLHS)
{
	return (!(iRHS == iLHS));
}

int Curve::copyFromASNObject(const ASN1T_ALGOS_Curve & iCurve) 
{
	if (iCurve.a.numocts > 0 )
		mA = QByteArray((const char*)iCurve.a.data,iCurve.a.numocts);
	if (iCurve.b.numocts > 0 )
		mB = QByteArray((const char*)iCurve.b.data,iCurve.b.numocts);

	mSeedPresent = iCurve.m.seedPresent;
	if (mSeedPresent)
		mSeed = EBitString(iCurve.seed);
	
	return SUCCESS;
}

const QByteArray & Curve::getA()const 
{
	return mA;
}

const QByteArray & Curve::getB()const 
{
	return mB;
}

const EBitString &Curve:: getSeed() const
{
	return mSeed;
}

bool Curve::isSeedPresent()const
{
	return mSeedPresent;
}

void Curve::setA(const QByteArray & iA)
{
	mA = iA;
}

void Curve::setB(const QByteArray & iB)
{
	mB = iB;
}

void Curve::setSeed(const EBitString & iSeed)
{
	mSeed = iSeed;
}

void Curve::setSeedPresent(bool iSeedPresent)
{
	mSeedPresent = iSeedPresent;
}

int Curve::copyToASNObject(ASN1T_ALGOS_Curve & oCurve) const
{
	if (mA.size()>0)
	{
		oCurve.a.data = (OSOCTET*)myStrDup(mA.data(),mA.size());
		oCurve.a.numocts = mA.size(); 
	}
	if (mB.size()>0)
	{
		oCurve.b.data = (OSOCTET*)myStrDup(mB.data(),mB.size());
		oCurve.b.numocts = mB.size(); 
	}

	oCurve.m.seedPresent = (mSeedPresent ? 1:0);
	if (mSeedPresent)
	{
		mSeed.copyToASNObject(oCurve.seed);
	}

	return SUCCESS;
}

void Curve::freeASNObject(ASN1T_ALGOS_Curve & oCurve)const
{
	if (oCurve.a.numocts >0&& oCurve.a.data)
	{
		DELETE_MEMORY_ARRAY(oCurve.a.data);
		oCurve.a.numocts = 0;
	}
	if (oCurve.b.numocts >0&& oCurve.b.data)
	{
		DELETE_MEMORY_ARRAY(oCurve.b.data);
		oCurve.b.numocts = 0;
	}
	if (oCurve.m.seedPresent)
	{
		EBitString::freeASNObject(oCurve.seed);
	}
}

Curve::~Curve()
{
}
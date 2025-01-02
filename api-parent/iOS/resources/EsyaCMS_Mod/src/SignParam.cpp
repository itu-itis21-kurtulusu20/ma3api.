#include "SignParam.h"

using namespace esya;

SignParam::SignParam(void)
: mAyrikImza(false)
{
}

SignParam::SignParam(bool iAyrikImza, const QList<SignerParam>& iSignerParams)
:	mAyrikImza(iAyrikImza),
	mSignerParams(iSignerParams)
{

}

SignParam::SignParam(const SignParam & iSP)
:	mAyrikImza(iSP.isAyrikImza()),
	mSignerParams(iSP.getSignerParams())
{
}


SignParam & SignParam::operator=(const SignParam& iSP )
{
	mAyrikImza		= iSP.isAyrikImza();
	mSignerParams	= iSP.getSignerParams();
	return *this;
}


bool esya::operator==(const SignParam& iRHS, const SignParam& iLHS)
{
	return (	iRHS.isAyrikImza() == iLHS.isAyrikImza() &&
				iRHS.getSignerParams() == iLHS.getSignerParams());
}

const QList<SignerParam>& SignParam::getSignerParams() const
{
	return mSignerParams;
}

const bool  SignParam::isAyrikImza() const 
{
	return mAyrikImza;
}

void SignParam::setAyrikImza(const bool iAI) 
{
	mAyrikImza = iAI;
}
void SignParam::setSignerParams( const QList<SignerParam>& iSP)
{
	mSignerParams = iSP;	
}
void SignParam::addSignerParam( const SignerParam& iSP)
{
	mSignerParams.append(iSP);
}

const QList<AlgorithmIdentifier> SignParam::getDigestAlgs()const 
{
	QList<AlgorithmIdentifier> digestAlgs;
	for (int i = 0; i < mSignerParams.size(); i++ )
	{
		digestAlgs.append(mSignerParams[i].getDigestAlg());
	}
	return digestAlgs;
}

void SignParam::fillDigests(const QList<DigestInfo>& iDigests)
{
	for (int i  = 0 ;  i< mSignerParams.size(); i++)
	{
		QByteArray digest = DigestInfo::findDigest(iDigests,mSignerParams[i].getDigestAlg());
		mSignerParams[i].setDigest(digest);
	}
}

SignParam::~SignParam(void)
{
}

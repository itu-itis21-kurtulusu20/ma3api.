#include "SubjectPublicKeyInfo.h"

using namespace esya;

SubjectPublicKeyInfo::SubjectPublicKeyInfo(void)
{
}

SubjectPublicKeyInfo::SubjectPublicKeyInfo(const QByteArray & iSPKI)
{
	constructObject(iSPKI);
}

SubjectPublicKeyInfo::SubjectPublicKeyInfo(const ASN1T_EXP_SubjectPublicKeyInfo & iSPKI)
{
	copyFromASNObject(iSPKI);
}

SubjectPublicKeyInfo::SubjectPublicKeyInfo(const AlgorithmIdentifier & iAlgorithm, const EBitString& iSubjectPublicKey)
:	mAlgorithm(iAlgorithm), mSubjectPublicKey(iSubjectPublicKey)
{

}


SubjectPublicKeyInfo::SubjectPublicKeyInfo(const SubjectPublicKeyInfo& iSPKI)
:	mAlgorithm(iSPKI.getAlgorithm()),
	mSubjectPublicKey(iSPKI.getSubjectPublicKey())
{
}

SubjectPublicKeyInfo & SubjectPublicKeyInfo::operator=(const SubjectPublicKeyInfo & iSPKI)
{
	mAlgorithm			= iSPKI.getAlgorithm();
	mSubjectPublicKey	= iSPKI.getSubjectPublicKey();
	return *this;
}

bool esya::operator==(const SubjectPublicKeyInfo &iRHS ,const SubjectPublicKeyInfo& iLHS)
{
	return (	(iRHS.getAlgorithm() == iLHS.getAlgorithm()) && 
				(iRHS.getSubjectPublicKey() == iLHS.getSubjectPublicKey()) );
}

bool esya::operator!=(const SubjectPublicKeyInfo& iRHS,const SubjectPublicKeyInfo& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int SubjectPublicKeyInfo::copyFromASNObject(const ASN1T_EXP_SubjectPublicKeyInfo & iSPKI)
{
	mAlgorithm.copyFromASNObject(iSPKI.algorithm);	
	mSubjectPublicKey.copyFromASNObject(iSPKI.subjectPublicKey);
	return SUCCESS;
}

const AlgorithmIdentifier& SubjectPublicKeyInfo::getAlgorithm()const
{
	return mAlgorithm;
}

AlgorithmIdentifier& SubjectPublicKeyInfo::getAlgorithm()
{
	return mAlgorithm;
}

const EBitString& SubjectPublicKeyInfo::getSubjectPublicKey()const
{
	return mSubjectPublicKey;
}

void SubjectPublicKeyInfo::setAlgorithm(const AlgorithmIdentifier& iAlgorithm)
{
	mAlgorithm = iAlgorithm;
}

void SubjectPublicKeyInfo::setSubjectPublicKey(const EBitString& iPublicKey)
{
	mSubjectPublicKey = iPublicKey;
}

int SubjectPublicKeyInfo::copyToASNObject(ASN1T_EXP_SubjectPublicKeyInfo & oSPKI)const
{
	mAlgorithm.copyToASNObject(oSPKI.algorithm);
	mSubjectPublicKey.copyToASNObject(oSPKI.subjectPublicKey);
	return SUCCESS;
}

void SubjectPublicKeyInfo::freeASNObject(ASN1T_EXP_SubjectPublicKeyInfo& oSPKI)const
{
	AlgorithmIdentifier().freeASNObject(oSPKI.algorithm);
	EBitString::freeASNObject(oSPKI.subjectPublicKey);
}

SubjectPublicKeyInfo::~SubjectPublicKeyInfo(void)
{
}

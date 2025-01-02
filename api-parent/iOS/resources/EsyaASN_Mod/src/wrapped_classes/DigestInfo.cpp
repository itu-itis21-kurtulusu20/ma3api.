#include "DigestInfo.h"

using namespace esya;

DigestInfo::DigestInfo(void)
{
}

DigestInfo::DigestInfo(const QByteArray & iDI)
{
	constructObject(iDI);
}

DigestInfo::DigestInfo(const AlgorithmIdentifier& iDigestAlg, const QByteArray & iDigest)
:	mDigestAlgorithm(iDigestAlg),
	mDigest(iDigest)
{
}

DigestInfo::DigestInfo(const ASN1T_PKCS7_DigestInfo & iDI)
{
	copyFromASNObject(iDI);
}

DigestInfo::DigestInfo(const DigestInfo& iDI)
:	mDigestAlgorithm(iDI.getDigestAlgorithm()),
	mDigest(iDI.getDigest())
{
}


DigestInfo& DigestInfo::operator=(const DigestInfo& iDI)
{
	mDigestAlgorithm = iDI.getDigestAlgorithm(); 
	mDigest			 = iDI.getDigest();
	return (*this);
}

bool esya::operator==( const DigestInfo& iRHS, const DigestInfo& iLHS)
{
	return (iRHS.getDigest() == iLHS.getDigest() && iRHS.getDigestAlgorithm() == iLHS.getDigestAlgorithm()) ;
}

bool esya::operator!=( const DigestInfo& iRHS, const DigestInfo& iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}

int DigestInfo::copyFromASNObject(const ASN1T_PKCS7_DigestInfo & iDI)
{
	mDigestAlgorithm.copyFromASNObject(iDI.digestAlgorithm);
	mDigest = toByteArray(iDI.digest);
	return SUCCESS;
}

int DigestInfo::copyToASNObject(ASN1T_PKCS7_DigestInfo &oDI)const
{
	mDigestAlgorithm.copyToASNObject(oDI.digestAlgorithm);
	oDI.digest.data= (OSOCTET*)myStrDup(mDigest.data(),mDigest.size());
	oDI.digest.numocts = mDigest.size();
	return SUCCESS;
}

void DigestInfo::freeASNObject(ASN1T_PKCS7_DigestInfo& oDI)const
{
	AlgorithmIdentifier().freeASNObject(oDI.digestAlgorithm);	
	DELETE_MEMORY_ARRAY(oDI.digest.data)
}

const AlgorithmIdentifier & DigestInfo::getDigestAlgorithm() const
{
	return mDigestAlgorithm;
}

const QByteArray DigestInfo::getDigest() const 
{
	return mDigest;
}

void DigestInfo::setDigestAlgorithm(const AlgorithmIdentifier& iDigestAlg)
{
	mDigestAlgorithm = iDigestAlg;
}

void DigestInfo::setDigest(const QByteArray & iDigest) 
{
	mDigest = iDigest;
}

QByteArray DigestInfo::findDigest(const  QList<DigestInfo> & iDigestList , const AlgorithmIdentifier & iDigestAlg)
{
	for (int i = 0; i<iDigestList.size();i++)
	{
		if ( iDigestList[i].getDigestAlgorithm() == iDigestAlg )
			return iDigestList[i].getDigest();
	}
	return QByteArray();
}

DigestInfo::~DigestInfo(void)
{
}

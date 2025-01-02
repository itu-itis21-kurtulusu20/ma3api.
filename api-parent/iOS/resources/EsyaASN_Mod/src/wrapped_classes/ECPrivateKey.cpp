
#include "ECPrivateKey.h"

using namespace esya;

ECPrivateKey::ECPrivateKey(void)
:	mParametersPresent(false), 
	mPublicKeyPresent(false)
{
}

ECPrivateKey::ECPrivateKey( const ASN1T_ALGOS_ECPrivateKey &iEPK)
{
	copyFromASNObject(iEPK);
}

ECPrivateKey::ECPrivateKey( const ECPrivateKey &iEPK)
:	mPrivateKey(iEPK.getPrivateKey()),
	mPublicKey(iEPK.getPublicKey()),
	mPublicKeyPresent(iEPK.isPublicKeyPresent()),
	mParameters(iEPK.getParameters()),
	mParametersPresent(iEPK.isParametersPresent()),
	mVersion(iEPK.getVersion())
{
}

ECPrivateKey::ECPrivateKey( const QByteArray &iEPK)
{
	constructObject(iEPK);
}

ECPrivateKey & ECPrivateKey::operator=(const ECPrivateKey& iEPK)
{
	mPrivateKey			= iEPK.getPrivateKey();
	mPublicKey			= iEPK.getPublicKey();
	mPublicKeyPresent	= iEPK.isPublicKeyPresent();
	mParameters			= iEPK.getParameters();
	mParametersPresent	= iEPK.isParametersPresent();
	mVersion			= iEPK.getVersion();
	return *this;
}


bool esya::operator==(const ECPrivateKey& iRHS, const ECPrivateKey& iLHS)
{
	return (	( iRHS.getPrivateKey()	== iLHS.getPrivateKey() ) &&
				( iLHS.getPublicKey()			== iLHS.getPublicKey() )  &&
				( iLHS.isPublicKeyPresent()	== iLHS.isPublicKeyPresent()) &&
				( iLHS.getParameters()			== iLHS.getParameters() ) &&
				( iLHS.isParametersPresent()	== iLHS.isParametersPresent() ) && 
				( iLHS.getVersion()			== iLHS.getVersion() ) );
}

bool esya::operator!=(const ECPrivateKey& iRHS, const ECPrivateKey& iLHS)
{
	return ( !(iRHS == iLHS));
}

int ECPrivateKey::copyFromASNObject(const ASN1T_ALGOS_ECPrivateKey & iEPK)
{
	mVersion = iEPK.version;
	mPrivateKey = QByteArray((const char*)iEPK.privateKey.data,iEPK.privateKey.numocts);

	mParametersPresent	= iEPK.m.parametersPresent;
	mPublicKeyPresent	= iEPK.m.publicKeyPresent;

	if (mParametersPresent)
	{
		mParameters.copyFromASNObject(iEPK.parameters);
	}
	if (mPublicKeyPresent)
	{
		mPublicKey.copyFromASNObject(iEPK.publicKey);
	}
	return SUCCESS;
}

int ECPrivateKey::copyToASNObject(ASN1T_ALGOS_ECPrivateKey & oEPK) const
{
	oEPK.version = mVersion;
	if (!mPrivateKey.isEmpty())
	{
		oEPK.privateKey.data = (ASN1OCTET*)myStrDup(mPrivateKey.data(),mPrivateKey.size());
		oEPK.privateKey.numocts = mPrivateKey.size();
	}

	oEPK.m.parametersPresent = (mParametersPresent	? 1:0);
	oEPK.m.publicKeyPresent= (mPublicKeyPresent	? 1:0);

	if (mParametersPresent)
	{
		mParameters.copyToASNObject(oEPK.parameters);
	}
	if (mPublicKeyPresent)
	{
		mPublicKey.copyToASNObject(oEPK.publicKey);
	}
	return SUCCESS;
}

void ECPrivateKey::freeASNObject(ASN1T_ALGOS_ECPrivateKey& oEPK)const
{
	if (oEPK.privateKey.numocts>0)
		DELETE_MEMORY_ARRAY(oEPK.privateKey.data);

	if (oEPK.m.parametersPresent)
		ECParameters().freeASNObject(oEPK.parameters);

	if (oEPK.m.publicKeyPresent)
		EBitString::freeASNObject(oEPK.publicKey);
}

const bool	ECPrivateKey::isParametersPresent()const 
{
	return mParametersPresent;
}

const bool	ECPrivateKey::isPublicKeyPresent()const 
{
	return mPublicKeyPresent;
}

const OSUINT8 ECPrivateKey::getVersion()const
{
	return mVersion;
}

const QByteArray &	ECPrivateKey::getPrivateKey()const
{
	return mPrivateKey;
}

const ECParameters& ECPrivateKey::getParameters()const
{
	return mParameters;
}

const EBitString&	ECPrivateKey::getPublicKey()const
{
	return mPublicKey;
}

void ECPrivateKey::setParametersPresent(bool iParametersPresent)
{
	mParametersPresent = iParametersPresent;
}

void ECPrivateKey::setPublicKeyPresent(bool iPublicKeyPresent)
{
	mPublicKeyPresent = iPublicKeyPresent;
}

void ECPrivateKey::setVersion(const OSUINT8 iVersion)
{
	mVersion = iVersion;
}

void ECPrivateKey::setPrivateKey(const QByteArray &iPrivateKey)
{
	mPrivateKey = iPrivateKey;
}

void ECPrivateKey::setParameters(const ECParameters& iParameters)
{
	mParameters = iParameters;
}

void ECPrivateKey::setPublicKey(const EBitString&	iPublicKey)
{
	mPublicKey = iPublicKey;
}

ECPrivateKey::~ECPrivateKey(void)
{
}

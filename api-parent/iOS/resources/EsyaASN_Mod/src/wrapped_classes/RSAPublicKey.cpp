#include "RSAPublicKey.h"

using namespace esya;

RSAPublicKey::RSAPublicKey(void)
{
}

RSAPublicKey::RSAPublicKey( const ASN1T_PKCS18_RSAPublicKey &iRPK)
{
	copyFromASNObject(iRPK);
}

RSAPublicKey::RSAPublicKey( const RSAPublicKey &iRPK)
:	mModulus(iRPK.getModulus()),
	mPublicExponent(iRPK.getPublicExponent())
{
}

RSAPublicKey::RSAPublicKey( const QByteArray &iRPK)
{
	constructObject(iRPK);
}

RSAPublicKey & RSAPublicKey::operator=(const RSAPublicKey& iRPK)
{
	mModulus			= iRPK.getModulus();
	mPublicExponent		= iRPK.getPublicExponent();
	return * this;
}

bool esya::operator==(const RSAPublicKey& iRHS, const RSAPublicKey& iLHS)
{
	return (	iRHS.getModulus()			==	iLHS.getModulus()			&&
				iRHS.getPublicExponent()	==	iLHS.getPublicExponent()		);
}

bool esya::operator!=(const RSAPublicKey& iRHS, const RSAPublicKey& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int RSAPublicKey::copyFromASNObject(const ASN1T_PKCS18_RSAPublicKey & iRPK)
{
	mModulus			= QString(iRPK.modulus);
	mPublicExponent		= QString(iRPK.publicExponent);
	return SUCCESS;
}

int RSAPublicKey::copyToASNObject(ASN1T_PKCS18_RSAPublicKey & oRPK) const
{
	oRPK.modulus			= myStrDup(mModulus);
	oRPK.publicExponent		= myStrDup(mPublicExponent);	
	return SUCCESS;
}

void RSAPublicKey::freeASNObject(ASN1T_PKCS18_RSAPublicKey& oRPK)const
{
	DELETE_MEMORY_ARRAY(oRPK.modulus)
	DELETE_MEMORY_ARRAY(oRPK.publicExponent)
}

const QString &	RSAPublicKey::getModulus() const 
{
	return mModulus;
}

const QString &	RSAPublicKey::getPublicExponent() const
{
	return mPublicExponent;
}

void RSAPublicKey::setModulus(const QString & iModulus	) 
{
	mModulus = iModulus;
}

void RSAPublicKey::setPublicExponent(const QString &iPubExp	) 
{
	mPublicExponent = iPubExp;
}


RSAPublicKey::~RSAPublicKey(void)
{
}

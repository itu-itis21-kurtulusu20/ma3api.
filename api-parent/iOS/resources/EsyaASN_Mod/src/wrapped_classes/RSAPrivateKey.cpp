#include "RSAPrivateKey.h"

using namespace esya;
NAMESPACE_BEGIN(esya)
RSAPrivateKey::RSAPrivateKey(void)
{
}

RSAPrivateKey::RSAPrivateKey( const ASN1T_PKCS18_RSAPrivateKey &iRPK)
{
	copyFromASNObject(iRPK);
}

RSAPrivateKey::RSAPrivateKey( const RSAPrivateKey &iRPK)
:	mVersion(iRPK.getVersion()),
	mModulus(iRPK.getModulus()),
	mPublicExponent(iRPK.getPublicExponent()),
	mPrivateExponent(iRPK.getPrivateExponent()),
	mPrime1(iRPK.getPrime1()),
	mPrime2(iRPK.getPrime2()),
	mExponent1(iRPK.getExponent1()),
	mExponent2(iRPK.getExponent2()),
	mCoefficient(iRPK.getCoefficient())
{
}

RSAPrivateKey::RSAPrivateKey( const QByteArray &iRPK)
{
	constructObject(iRPK);
}

RSAPrivateKey & RSAPrivateKey::operator=(const RSAPrivateKey& iRPK)
{
	mVersion			= iRPK.getVersion();
	mModulus			= iRPK.getModulus();
	mPublicExponent		= iRPK.getPublicExponent();
	mPrivateExponent	= iRPK.getPrivateExponent();
	mPrime1				= iRPK.getPrime1();
	mPrime2				= iRPK.getPrime2();
	mExponent1			= iRPK.getExponent1();
	mExponent2			= iRPK.getExponent2();
	mCoefficient		= iRPK.getCoefficient();
	return * this;
}

bool operator==(const RSAPrivateKey& iRHS, const RSAPrivateKey& iLHS)
{
	return (	iRHS.getVersion()			==	iLHS.getVersion()			&&
				iRHS.getModulus()			==	iLHS.getModulus()			&&
				iRHS.getPublicExponent()	==	iLHS.getPublicExponent()	&&
				iRHS.getPrivateExponent()	==	iLHS.getPrivateExponent()	&&
				iRHS.getPrime1()			==	iLHS.getPrime1()			&&
				iRHS.getPrime2()			==	iLHS.getPrime2()			&&
				iRHS.getExponent1()			==	iLHS.getExponent1()			&&
				iRHS.getExponent2()			==	iLHS.getExponent2()			&&
				iRHS.getCoefficient()		==  iLHS.getCoefficient()			);
}

bool operator!=(const RSAPrivateKey& iRHS, const RSAPrivateKey& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int RSAPrivateKey::copyFromASNObject(const ASN1T_PKCS18_RSAPrivateKey & iRPK)
{
	mVersion			= iRPK.version;
	mModulus			= QString(iRPK.modulus);
	mPublicExponent		= QString(iRPK.publicExponent);
	mPrivateExponent	= QString(iRPK.privateExponent);
	mPrime1				= QString(iRPK.prime1);
	mPrime2				= QString(iRPK.prime2);
	mExponent1			= QString(iRPK.exponent1);
	mExponent2			= QString(iRPK.exponent2);
	mCoefficient		= QString(iRPK.coefficient);
	return SUCCESS;
}

int RSAPrivateKey::copyToASNObject(ASN1T_PKCS18_RSAPrivateKey & oRPK) const
{
	oRPK.version			= mVersion;
	oRPK.modulus			= myStrDup(mModulus);
	oRPK.publicExponent		= myStrDup(mPublicExponent);	
	oRPK.privateExponent	= myStrDup(mPrivateExponent);
	oRPK.prime1				= myStrDup(mPrime1);
	oRPK.prime2				= myStrDup(mPrime2);
	oRPK.exponent1			= myStrDup(mExponent1);
	oRPK.exponent2			= myStrDup(mExponent2);
	oRPK.coefficient		= myStrDup(mCoefficient);
	return SUCCESS;
}

void RSAPrivateKey::freeASNObject(ASN1T_PKCS18_RSAPrivateKey& oRPK)const
{
	DELETE_MEMORY_ARRAY(oRPK.modulus)
	DELETE_MEMORY_ARRAY(oRPK.publicExponent)
	DELETE_MEMORY_ARRAY(oRPK.privateExponent)
	DELETE_MEMORY_ARRAY(oRPK.exponent1)
	DELETE_MEMORY_ARRAY(oRPK.exponent2)
	DELETE_MEMORY_ARRAY(oRPK.prime1)
	DELETE_MEMORY_ARRAY(oRPK.prime2)
	DELETE_MEMORY_ARRAY(oRPK.coefficient)
}

const OSUINT32&	RSAPrivateKey::getVersion() const 
{
	return mVersion;
}

const QString &	RSAPrivateKey::getModulus() const 
{
	return mModulus;
}

const QString &	RSAPrivateKey::getPublicExponent() const
{
	return mPublicExponent;
}

const QString & RSAPrivateKey::getPrivateExponent() const
{
	return mPrivateExponent;
}

const QString & RSAPrivateKey::getPrime1() const
{
	return mPrime1;
}

const QString & RSAPrivateKey::getPrime2() const
{
	return mPrime2;
}

const QString & RSAPrivateKey::getExponent1() const
{
	return mExponent1;
}

const QString & RSAPrivateKey::getExponent2() const
{
	return mExponent2;
}

const QString & RSAPrivateKey::getCoefficient() const
{
	return mCoefficient;	
}

bool RSAPrivateKey::matchKey(const RSAPublicKey & rPK)
{
	return (	(	this->getPublicExponent()	== rPK.getPublicExponent()	)	&& 
				(	this->getModulus()			== rPK.getModulus()			)		);
}

byte RSAPrivateKey::toByte(const QString& iHexValue)
{
	bool converted;
	byte b = iHexValue.toInt(&converted,16);
	return b;
}

QByteArray RSAPrivateKey::toByteArray(const QString & iHexValue)
{
	QString hexVal = iHexValue.right(iHexValue.length()-2);
	QByteArray bytes;
	while (hexVal.length()>1)
	{
		QString byteStr = hexVal.right(2);	
		hexVal.remove(hexVal.length()-2,2);
		bytes.prepend(toByte(byteStr));		
	}
	return bytes;
}


RSAPrivateKey::~RSAPrivateKey(void)
{
}
NAMESPACE_END

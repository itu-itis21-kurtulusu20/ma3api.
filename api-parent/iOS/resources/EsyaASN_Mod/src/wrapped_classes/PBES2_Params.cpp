#include "PBES2_Params.h"

using namespace esya;

PBES2_Params::PBES2_Params(void)
{
}

PBES2_Params::PBES2_Params(const ASN1T_PKCS5_PBES2_params & iPP)
{
	copyFromASNObject(iPP)	;
}

PBES2_Params::PBES2_Params(const  QByteArray &iPP)
{
	constructObject(iPP);
}

PBES2_Params::PBES2_Params(const AlgorithmIdentifier & iKDF, const AlgorithmIdentifier iES)
:	mKeyDerivationFunc(iKDF),
	mEncryptionScheme(iES)
{
}

PBES2_Params::PBES2_Params(const PBES2_Params & iPP)
:	mKeyDerivationFunc(iPP.getKeyDerivationFunc()),
	mEncryptionScheme(iPP.getEncryptionScheme())
{
}
 
PBES2_Params& PBES2_Params::operator=(const PBES2_Params& iPP)
{
	mKeyDerivationFunc	= iPP.getKeyDerivationFunc();
	mEncryptionScheme	= iPP.getEncryptionScheme();
	return *this;
}

bool esya::operator==(const PBES2_Params & iRHS,const PBES2_Params & iLHS)
{
	return ( ( iRHS.getKeyDerivationFunc()	== iLHS.getKeyDerivationFunc()	) && 
			 ( iRHS.getEncryptionScheme()	== iLHS.getEncryptionScheme()	)	);
}

bool esya::operator!=(const PBES2_Params & iRHS, const PBES2_Params & iLHS)
{

	return ( !( iRHS == iLHS ) );
}


int PBES2_Params::copyFromASNObject(const ASN1T_PKCS5_PBES2_params & iPP)
{
	mKeyDerivationFunc.copyFromASNObject(iPP.keyDerivationFunc);
	mEncryptionScheme.copyFromASNObject(iPP.encryptionScheme);
	return SUCCESS;
}

int PBES2_Params::copyToASNObject(ASN1T_PKCS5_PBES2_params & oPP) const
{
	mKeyDerivationFunc.copyToASNObject(oPP.keyDerivationFunc);
	mEncryptionScheme.copyToASNObject(oPP.encryptionScheme);
	return SUCCESS;
}

void PBES2_Params::freeASNObject(ASN1T_PKCS5_PBES2_params & oPP) const
{
	AlgorithmIdentifier().freeASNObject(oPP.keyDerivationFunc);
	AlgorithmIdentifier().freeASNObject(oPP.encryptionScheme);
}

const AlgorithmIdentifier&	PBES2_Params::getKeyDerivationFunc()	const
{
	return mKeyDerivationFunc;
}

const AlgorithmIdentifier&	PBES2_Params::getEncryptionScheme()	const
{
	return mEncryptionScheme;
}

void PBES2_Params::setKeyDerivationFunc(const AlgorithmIdentifier& iKeyDerivationFunc)
{
	mKeyDerivationFunc = iKeyDerivationFunc;
}

void PBES2_Params::setEncryptionScheme(const AlgorithmIdentifier& iEncryptionScheme)
{
	mEncryptionScheme = iEncryptionScheme;
}

PBES2_Params::~PBES2_Params(void)
{
}

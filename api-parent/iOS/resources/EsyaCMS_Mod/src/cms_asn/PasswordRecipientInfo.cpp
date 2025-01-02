#include "PasswordRecipientInfo.h"

using namespace esya;

PasswordRecipientInfo::PasswordRecipientInfo(void)
{
}

PasswordRecipientInfo::PasswordRecipientInfo(const QByteArray & iPWRI)
{
	constructObject(iPWRI);
}

PasswordRecipientInfo::PasswordRecipientInfo(const ASN1T_CMS_PasswordRecipientInfo & iPWRI)
{
	copyFromASNObject(iPWRI);
}

PasswordRecipientInfo::PasswordRecipientInfo(const PasswordRecipientInfo& iPWRI)
:	mVersion(iPWRI.getVersion()),
	mKeyDerivationAlgorithm(iPWRI.getKeyDrvAlg()),
	mKeyDerivationAlgorithmPresent(iPWRI.isKeyDrvAlgPresent()),
	mKeyEncryptionAlgorithm(iPWRI.getKeyEncAlg()),
	mEncryptedKey(iPWRI.getEncryptedKey())
{
}

PasswordRecipientInfo & PasswordRecipientInfo::operator=(const PasswordRecipientInfo & iPWRI)
{
	mVersion						= iPWRI.getVersion();
	mKeyDerivationAlgorithm			= iPWRI.getKeyDrvAlg();
	mKeyDerivationAlgorithmPresent	= iPWRI.isKeyDrvAlgPresent();
	mKeyEncryptionAlgorithm			= iPWRI.getKeyEncAlg();
	mEncryptedKey					= iPWRI.getEncryptedKey();
	return * this;
}

bool esya::operator==(const PasswordRecipientInfo & iRHS , const PasswordRecipientInfo & iLHS )
{
	if ( iRHS.isKeyDrvAlgPresent() != iLHS.isKeyDrvAlgPresent() ) return false;
		 
	if ( ( iRHS.isKeyDrvAlgPresent()) && 
		 ( iRHS.getKeyDrvAlg() != iLHS.getKeyDrvAlg()))
		 return false;

	return ( ( iRHS.getVersion()		== iLHS.getVersion()		) &&	
			 ( iRHS.getKeyEncAlg()		== iLHS.getKeyEncAlg()		) &&
			 ( iRHS.getEncryptedKey()	== iLHS.getEncryptedKey()	)	);
}

bool esya::operator!=(const PasswordRecipientInfo & iRHS,const PasswordRecipientInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}	

int PasswordRecipientInfo::copyFromASNObject(const ASN1T_CMS_PasswordRecipientInfo & iPWRI)
{
	mVersion = iPWRI.version;
	mKeyDerivationAlgorithm.copyFromASNObject(iPWRI.keyDerivationAlgorithm);
	mKeyEncryptionAlgorithm.copyFromASNObject(iPWRI.keyEncryptionAlgorithm);
	mEncryptedKey = toByteArray(iPWRI.encryptedKey);
	return SUCCESS;
}

int PasswordRecipientInfo::copyToASNObject(ASN1T_CMS_PasswordRecipientInfo& oPWRI)const
{
	mKeyDerivationAlgorithm.copyToASNObject(oPWRI.keyDerivationAlgorithm);
	mKeyEncryptionAlgorithm.copyToASNObject(oPWRI.keyEncryptionAlgorithm);
	oPWRI.version				= mVersion;
	oPWRI.encryptedKey.data		= (OSOCTET*)myStrDup(mEncryptedKey.data() , mEncryptedKey.size());
	oPWRI.encryptedKey.numocts	= mEncryptedKey.size();
	return SUCCESS;
}

void PasswordRecipientInfo::freeASNObject(ASN1T_CMS_PasswordRecipientInfo & oPWRI)const
{
	if (oPWRI.m.keyDerivationAlgorithmPresent)
		AlgorithmIdentifier().freeASNObject(oPWRI.keyDerivationAlgorithm);

	AlgorithmIdentifier().freeASNObject(oPWRI.keyEncryptionAlgorithm);
	DELETE_MEMORY_ARRAY(oPWRI.encryptedKey.data)
}

const bool PasswordRecipientInfo::isKeyDrvAlgPresent() const
{
	return mKeyDerivationAlgorithmPresent;
}

const QByteArray & PasswordRecipientInfo::getEncryptedKey()	const
{
	return mEncryptedKey;
}

const AlgorithmIdentifier	& PasswordRecipientInfo::getKeyEncAlg()	const
{
	return mKeyEncryptionAlgorithm;
}

const AlgorithmIdentifier	& PasswordRecipientInfo::getKeyDrvAlg()	const
{
	return mKeyDerivationAlgorithm;
}

const ASN1T_CMS_CMSVersion	& PasswordRecipientInfo::getVersion() const
{
	return mVersion;
}


void PasswordRecipientInfo::setKeyDrvAlgPresent(const bool  iKDAP)
{
	mKeyDerivationAlgorithmPresent = iKDAP;
}

void PasswordRecipientInfo::setVersion(ASN1T_CMS_CMSVersion iVersion)
{
	mVersion = iVersion;
}

void PasswordRecipientInfo::setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg )
{
	mKeyEncryptionAlgorithm = iKeyEncAlg;
}

void PasswordRecipientInfo::setKeyDrvAlg( const AlgorithmIdentifier & iKeyDrvAlg )
{
	setKeyDrvAlgPresent(true);
	mKeyDerivationAlgorithm = iKeyDrvAlg;
}

void PasswordRecipientInfo::setEncryptedKey(const QByteArray& iEncryptedKey)
{
	mEncryptedKey = iEncryptedKey;
}

PasswordRecipientInfo::~PasswordRecipientInfo(void)
{
}

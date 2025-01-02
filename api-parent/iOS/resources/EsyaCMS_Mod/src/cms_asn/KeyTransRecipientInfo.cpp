#include "KeyTransRecipientInfo.h"

#include "KriptoUtils.h"

using namespace esya;


KeyTransRecipientInfo::KeyTransRecipientInfo(void)
{
}

KeyTransRecipientInfo::KeyTransRecipientInfo(const QByteArray & iKTRI)
{
	constructObject(iKTRI);
}

KeyTransRecipientInfo::KeyTransRecipientInfo(const ASN1T_CMS_KeyTransRecipientInfo & iKTRI)
{
	copyFromASNObject(iKTRI);
}

KeyTransRecipientInfo::KeyTransRecipientInfo(const ASN1T_CMS_CMSVersion &iVersion ,const RecipientIdentifier& iRID, const AlgorithmIdentifier& iKeyEncAlg, const QByteArray & iEncryptedKey)
:	mVersion(iVersion),
	mRID(iRID),
	mKeyEncryptionAlgorithm(iKeyEncAlg),
	mEncryptedKey(iEncryptedKey)
{
}

KeyTransRecipientInfo::KeyTransRecipientInfo(const KeyTransRecipientInfo& iKTRI)
:	mVersion(iKTRI.getVersion()),
	mRID(iKTRI.getRID()),
	mKeyEncryptionAlgorithm(iKTRI.getKeyEncAlg()),
	mEncryptedKey(iKTRI.getEncryptedKey())
{
}

KeyTransRecipientInfo & KeyTransRecipientInfo::operator=(const KeyTransRecipientInfo & iKTRI)
{
	mVersion				= iKTRI.getVersion();
	mRID					= iKTRI.getRID();
	mKeyEncryptionAlgorithm = iKTRI.getKeyEncAlg();
	mEncryptedKey			= iKTRI.getEncryptedKey();
	return *this;
}

bool esya::operator==(const KeyTransRecipientInfo & iRHS,const KeyTransRecipientInfo & iLHS)
{
	return (	( iRHS.getVersion()			== iLHS.getVersion()		) && 
				( iRHS.getRID()				== iLHS.getRID()			) && 
				( iRHS.getKeyEncAlg()		== iLHS.getKeyEncAlg()		) &&
				( iRHS.getEncryptedKey()	== iLHS.getEncryptedKey()	)		);
}

bool esya::operator!=(const KeyTransRecipientInfo & iRHS,const KeyTransRecipientInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int KeyTransRecipientInfo::copyFromASNObject(const ASN1T_CMS_KeyTransRecipientInfo & iKTRI)
{
	mRID.copyFromASNObject(iKTRI.rid);
	mKeyEncryptionAlgorithm.copyFromASNObject(iKTRI.keyEncryptionAlgorithm);
	mVersion				= iKTRI.version;
	mEncryptedKey			= QByteArray((const char*)iKTRI.encryptedKey.data,iKTRI.encryptedKey.numocts);
	
	return SUCCESS;
}

int KeyTransRecipientInfo::copyToASNObject(ASN1T_CMS_KeyTransRecipientInfo & oKTRI)const
{
	oKTRI.encryptedKey.data = (OSOCTET*)myStrDup(mEncryptedKey.data(),mEncryptedKey.size());
	oKTRI.encryptedKey.numocts = mEncryptedKey.size();

	mKeyEncryptionAlgorithm.copyToASNObject(oKTRI.keyEncryptionAlgorithm);
	mRID.copyToASNObject(oKTRI.rid);

	oKTRI.version = mVersion;

	return SUCCESS;
}

void KeyTransRecipientInfo::freeASNObject(ASN1T_CMS_KeyTransRecipientInfo & oKTRI)const
{
	RecipientIdentifier().freeASNObject(oKTRI.rid);
	AlgorithmIdentifier().freeASNObject(oKTRI.keyEncryptionAlgorithm);
	if ( oKTRI.encryptedKey.numocts > 0 )
		DELETE_MEMORY_ARRAY(oKTRI.encryptedKey.data)
}

const QByteArray & KeyTransRecipientInfo::getEncryptedKey()	const
{
	return mEncryptedKey;
}

const AlgorithmIdentifier & KeyTransRecipientInfo::getKeyEncAlg()const
{
	return mKeyEncryptionAlgorithm;
}

const ASN1T_CMS_CMSVersion	& KeyTransRecipientInfo::getVersion()const
{
	return mVersion;
}
const RecipientIdentifier	& KeyTransRecipientInfo::getRID()const
{
	return mRID;
}

void KeyTransRecipientInfo::setEncryptedKey(const QByteArray& iEncryptedKey)
{
	mEncryptedKey = iEncryptedKey;
}

void KeyTransRecipientInfo::setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg )
{
	mKeyEncryptionAlgorithm = iKeyEncAlg;
}

void KeyTransRecipientInfo::setVersion(ASN1T_CMS_CMSVersion iVersion)
{
	mVersion = iVersion;
}

void KeyTransRecipientInfo::setRID(const RecipientIdentifier &iRID)
{
	mRID = iRID;
}

KeyTransRecipientInfo::~KeyTransRecipientInfo(void)
{
}

QByteArray KeyTransRecipientInfo::decryptKey(const OzelAnahtarBilgisi& iAB)const
{
	QByteArray key = KriptoUtils::decryptDataAsymmetric(mEncryptedKey,iAB,mKeyEncryptionAlgorithm);
	return key;
}
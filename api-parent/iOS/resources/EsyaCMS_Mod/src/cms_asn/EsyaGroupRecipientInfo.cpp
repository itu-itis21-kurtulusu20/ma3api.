#include "EsyaGroupRecipientInfo.h"

#include "KriptoUtils.h"

using namespace esya;


EsyaGroupRecipientInfo::EsyaGroupRecipientInfo(void)
{
}

EsyaGroupRecipientInfo::EsyaGroupRecipientInfo(const QByteArray & iEGRI)
{
	constructObject(iEGRI);
}

EsyaGroupRecipientInfo::EsyaGroupRecipientInfo(const ASN1T_ESYA_EsyaGroupRecipientInfo & iEGRI)
{
	copyFromASNObject(iEGRI);
}

EsyaGroupRecipientInfo::EsyaGroupRecipientInfo(const EsyaGroupRecipientInfo& iEGRI)
:	mGID(iEGRI.getGID()),
	mGroupIndex(iEGRI.getGroupIndex()),
	mGroupSize(iEGRI.getGroupSize()),
	mRID(iEGRI.getRID()),
	mKeyEncryptionAlgorithm(iEGRI.getKeyEncAlg()),
	mEncryptedKey(iEGRI.getEncryptedKey())
{
}

EsyaGroupRecipientInfo & EsyaGroupRecipientInfo::operator=(const EsyaGroupRecipientInfo & iEGRI)
{
	mGID					= iEGRI.getGID();
	mGroupIndex				= iEGRI.getGroupIndex();
	mGroupSize				= iEGRI.getGroupSize();
	mRID					= iEGRI.getRID();
	mKeyEncryptionAlgorithm = iEGRI.getKeyEncAlg();
	mEncryptedKey			= iEGRI.getEncryptedKey();
	return *this;
}

bool esya::operator==(const EsyaGroupRecipientInfo & iRHS,const EsyaGroupRecipientInfo & iLHS)
{
	return (	( iRHS.getGID()				== iLHS.getGID()			) && 
				( iRHS.getGroupIndex()		== iLHS.getGroupIndex()		) && 
				( iRHS.getGroupSize()		== iLHS.getGroupSize()		) && 
				( iRHS.getRID()				== iLHS.getRID()			) && 
				( iRHS.getKeyEncAlg()		== iLHS.getKeyEncAlg()		) &&
				( iRHS.getEncryptedKey()	== iLHS.getEncryptedKey()	)		);
}

bool esya::operator!=(const EsyaGroupRecipientInfo & iRHS,const EsyaGroupRecipientInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int EsyaGroupRecipientInfo::copyFromASNObject(const ASN1T_ESYA_EsyaGroupRecipientInfo & iEGRI)
{
	mRID.copyFromASNObject(iEGRI.rid);
	mKeyEncryptionAlgorithm.copyFromASNObject(iEGRI.keyEncryptionAlgorithm);
	mGID			= iEGRI.gid;
	mGroupIndex		= iEGRI.groupIndex;
	mGroupSize		= iEGRI.groupSize;
	mEncryptedKey	= QByteArray((const char*)iEGRI.encryptedKey.data,iEGRI.encryptedKey.numocts);
	
	return SUCCESS;
}

int EsyaGroupRecipientInfo::copyToASNObject(ASN1T_ESYA_EsyaGroupRecipientInfo & oEGRI)const
{
	oEGRI.encryptedKey.data = (OSOCTET*)myStrDup(mEncryptedKey.data(),mEncryptedKey.size());
	oEGRI.encryptedKey.numocts = mEncryptedKey.size();

	mKeyEncryptionAlgorithm.copyToASNObject(oEGRI.keyEncryptionAlgorithm);
	mRID.copyToASNObject(oEGRI.rid);

	oEGRI.gid= mGID;
	oEGRI.groupIndex = mGroupIndex;
	oEGRI.groupSize = mGroupSize;

	return SUCCESS;
}

void EsyaGroupRecipientInfo::freeASNObject(ASN1T_ESYA_EsyaGroupRecipientInfo & oEGRI)const
{
	RecipientIdentifier().freeASNObject(oEGRI.rid);
	AlgorithmIdentifier().freeASNObject(oEGRI.keyEncryptionAlgorithm);
	if ( oEGRI.encryptedKey.numocts > 0 )
		DELETE_MEMORY_ARRAY(oEGRI.encryptedKey.data)
}

const QByteArray & EsyaGroupRecipientInfo::getEncryptedKey()	const
{
	return mEncryptedKey;
}

const AlgorithmIdentifier & EsyaGroupRecipientInfo::getKeyEncAlg()const
{
	return mKeyEncryptionAlgorithm;
}

const int	& EsyaGroupRecipientInfo::getGID()const
{
	return mGID;
}

const int	& EsyaGroupRecipientInfo::getGroupIndex()const
{
	return mGroupIndex;
}

const int	& EsyaGroupRecipientInfo::getGroupSize()const
{
	return mGroupSize;
}


const RecipientIdentifier	& EsyaGroupRecipientInfo::getRID()const
{
	return mRID;
}

void EsyaGroupRecipientInfo::setEncryptedKey(const QByteArray& iEncryptedKey)
{
	mEncryptedKey = iEncryptedKey;
}

void EsyaGroupRecipientInfo::setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg )
{
	mKeyEncryptionAlgorithm = iKeyEncAlg;
}

void EsyaGroupRecipientInfo::setGID(int iGID)
{
	mGID = iGID;
}

void EsyaGroupRecipientInfo::setGroupIndex(int iGroupIndex)
{
	mGroupIndex = iGroupIndex;
}

void EsyaGroupRecipientInfo::setGroupSize(int iGroupSize)
{
	mGroupSize = iGroupSize;
}

void EsyaGroupRecipientInfo::setRID(const RecipientIdentifier &iRID)
{
	mRID = iRID;
}

EsyaGroupRecipientInfo::~EsyaGroupRecipientInfo(void)
{
}


QByteArray EsyaGroupRecipientInfo::decryptKey(const QList< QPair<ECertificate,OzelAnahtarBilgisi> >& iCertKeyList)const
{
	QByteArray key;
	for (int i= 0; i<iCertKeyList.size();i++ )
	{
		if ( mRID.isMatch(iCertKeyList[i].first))
		{
			key = KriptoUtils::decryptDataAsymmetric(mEncryptedKey,iCertKeyList[i].second,mKeyEncryptionAlgorithm);
			break;
		}
	}
	return key;
}

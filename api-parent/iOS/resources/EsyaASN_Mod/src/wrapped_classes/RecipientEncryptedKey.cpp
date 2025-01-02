#include "RecipientEncryptedKey.h"

using namespace esya;

RecipientEncryptedKey::RecipientEncryptedKey()
{
}

RecipientEncryptedKey::RecipientEncryptedKey(const RecipientEncryptedKey & iREK)
:	mEncryptedKey( iREK.getEncryptedKey()),
	mRID(iREK.getRID())
{
}

RecipientEncryptedKey::RecipientEncryptedKey(const ASN1T_CMS_RecipientEncryptedKey & iREK)
{
	copyFromASNObject(iREK);
}

RecipientEncryptedKey::RecipientEncryptedKey(const QByteArray & iREK)
{
	constructObject(iREK);
}

RecipientEncryptedKey::RecipientEncryptedKey(const KeyAgreeRecipientIdentifier& iRID, const QByteArray & iEK)
:	mEncryptedKey( iEK),
	mRID(iRID)
{
}


RecipientEncryptedKey & RecipientEncryptedKey::operator=(const RecipientEncryptedKey&iREK)
{
	mEncryptedKey = iREK.getEncryptedKey();
	mRID = iREK.getRID();
	return *this;
}

bool esya::operator==(const RecipientEncryptedKey & iRHS, const RecipientEncryptedKey& iLHS)
{
	return (iRHS.getRID() == iLHS.getRID() && iRHS.getEncryptedKey() == iLHS.getEncryptedKey() );
}

bool esya::operator!=(const RecipientEncryptedKey & iRHS, const RecipientEncryptedKey& iLHS)
{
	return (iRHS.getRID() != iLHS.getRID() || iRHS.getEncryptedKey() != iLHS.getEncryptedKey() );
}


int RecipientEncryptedKey::copyFromASNObject(const ASN1T_CMS_RecipientEncryptedKey & iREK)
{
	mEncryptedKey = QByteArray((const char*)iREK.encryptedKey.data,iREK.encryptedKey.numocts);
	mRID.copyFromASNObject(iREK.rid);
	return SUCCESS;
}

int RecipientEncryptedKey::copyToASNObject(ASN1T_CMS_RecipientEncryptedKey & oREK)  const
{
	mRID.copyToASNObject(oREK.rid);

	oREK.encryptedKey.numocts = mEncryptedKey.size();
	oREK.encryptedKey.data = (ASN1OCTET*)myStrDup(mEncryptedKey.data(),mEncryptedKey.size());

	return SUCCESS;
}

void RecipientEncryptedKey::freeASNObject(ASN1T_CMS_RecipientEncryptedKey & oREK)const
{
	DELETE_MEMORY_ARRAY(oREK.encryptedKey.data);
	KeyAgreeRecipientIdentifier().freeASNObject(oREK.rid);
}

int  RecipientEncryptedKey::copyREKList(const QList<RecipientEncryptedKey> iList ,ASN1TPDUSeqOfList & oREKs)
{
	return copyASNObjects<RecipientEncryptedKey>(iList,oREKs);
}

int	RecipientEncryptedKey::copyREKList(const ASN1TPDUSeqOfList & iREKs, QList<RecipientEncryptedKey>& oList)
{
	return copyASNObjects<RecipientEncryptedKey>(iREKs,oList);
}

const KeyAgreeRecipientIdentifier&	RecipientEncryptedKey::getRID()const 
{
	return mRID;
}
const QByteArray& RecipientEncryptedKey::getEncryptedKey()const
{
	return mEncryptedKey;
}


void RecipientEncryptedKey::setRID(const KeyAgreeRecipientIdentifier& iRID)
{
	mRID = iRID;
}

void RecipientEncryptedKey::setEncryptedKey(const QByteArray& iEK)
{
	mEncryptedKey = iEK; 
}

QString RecipientEncryptedKey::toString()const
{
	return QString("RECIPIENTENCRYPTEDKEY");
}

RecipientEncryptedKey::~RecipientEncryptedKey(void)
{
}
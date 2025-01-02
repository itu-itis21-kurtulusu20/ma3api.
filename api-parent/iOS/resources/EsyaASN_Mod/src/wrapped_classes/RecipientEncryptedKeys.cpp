#include "RecipientEncryptedKeys.h"

using namespace esya;

RecipientEncryptedKeys::RecipientEncryptedKeys(void)
{
}

RecipientEncryptedKeys::RecipientEncryptedKeys(const ASN1T_CMS_RecipientEncryptedKeys & iREKList)
{
	copyFromASNObject(iREKList);
}

RecipientEncryptedKeys::RecipientEncryptedKeys(const QByteArray & iREKList)
{
	constructObject(iREKList);
}

RecipientEncryptedKeys::RecipientEncryptedKeys(const RecipientEncryptedKeys &iREKList)
: mList(iREKList.getList())
{
}

RecipientEncryptedKeys & RecipientEncryptedKeys::operator=(const RecipientEncryptedKeys& iREKList)
{
	mList = iREKList.getList();
	return (*this);
}

bool esya::operator==(const RecipientEncryptedKeys & iRHS, const RecipientEncryptedKeys& iLHS)
{
	return (iRHS.getList() == iLHS.getList());
}

bool esya::operator!=(const RecipientEncryptedKeys & iRHS, const RecipientEncryptedKeys& iLHS)
{
	return (iRHS.getList() != iLHS.getList());
}

int RecipientEncryptedKeys::copyFromASNObject(const ASN1T_CMS_RecipientEncryptedKeys& iREKList)
{
	RecipientEncryptedKey().copyREKList(iREKList,mList);
	return SUCCESS;	
}

int RecipientEncryptedKeys::copyToASNObject(ASN1T_CMS_RecipientEncryptedKeys & oREKList) const
{
	RecipientEncryptedKey().copyREKList(mList,oREKList);
	return SUCCESS;
}

void RecipientEncryptedKeys::freeASNObject(ASN1T_CMS_RecipientEncryptedKeys& oREKList)const
{
	RecipientEncryptedKey().freeASNObjects(oREKList);
}



const QList<RecipientEncryptedKey> &RecipientEncryptedKeys::getList() const 
{
	return mList;
}

void RecipientEncryptedKeys::setList(const QList<RecipientEncryptedKey> &iList)
{
	mList = iList;
}

void RecipientEncryptedKeys::addREK(const RecipientEncryptedKey &iREK)
{
	mList<<iREK;
}

RecipientEncryptedKeys::~RecipientEncryptedKeys(void)
{
}

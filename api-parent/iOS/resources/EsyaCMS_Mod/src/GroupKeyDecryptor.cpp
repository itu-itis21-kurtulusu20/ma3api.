#include "GroupKeyDecryptor.h"
#include "EnvelopedData.h"

using namespace esya;

GroupKeyDecryptor::GroupKeyDecryptor(EnvelopedData* ipParentData,const QList< QPair<ECertificate,OzelAnahtarBilgisi> >& iCertKeyList)
: KeyDecryptor(ipParentData), mCertKeyList(iCertKeyList)
{
}

GroupKeyDecryptor::~GroupKeyDecryptor()
{

}

QByteArray GroupKeyDecryptor::_decryptKey()
{
	return mpParentData->decryptKey(mCertKeyList);;
}

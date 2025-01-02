#include "AsymetricKeyDecryptor.h"
#include "EnvelopedData.h"

using namespace esya;

AsymetricKeyDecryptor::AsymetricKeyDecryptor(EnvelopedData* ipParentData, const ECertificate & iCert, const OzelAnahtarBilgisi & iOA)
	: KeyDecryptor(ipParentData), mCert(iCert),mOA(iOA)
{

}

AsymetricKeyDecryptor::~AsymetricKeyDecryptor()
{

}

QByteArray AsymetricKeyDecryptor::_decryptKey()
{
	return mpParentData->decryptKey(mCert,mOA);
}
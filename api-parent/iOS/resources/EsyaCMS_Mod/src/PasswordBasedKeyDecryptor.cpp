#include "PasswordBasedKeyDecryptor.h"
#include "EnvelopedData.h"

using namespace esya;

PasswordBasedKeyDecryptor::PasswordBasedKeyDecryptor(EnvelopedData* ipParentData,const QString& iPassword)
	: KeyDecryptor(ipParentData),mPassword(iPassword)
{

}

PasswordBasedKeyDecryptor::~PasswordBasedKeyDecryptor()
{

}

QByteArray PasswordBasedKeyDecryptor::_decryptKey()
{
	return mpParentData->decryptKey(mPassword);
}
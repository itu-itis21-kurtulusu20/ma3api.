#include "KeyDecryptor.h"

#include "EnvelopedData.h"

using namespace esya;

KeyDecryptor::KeyDecryptor(EnvelopedData* ipParentData)
: mpParentData(ipParentData)
{
}

KeyDecryptor::~KeyDecryptor()
{

}

QByteArray KeyDecryptor::decryptKey() 
{
	return _decryptKey();
}
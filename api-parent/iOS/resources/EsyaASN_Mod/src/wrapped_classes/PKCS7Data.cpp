#include "PKCS7Data.h"

using namespace esya;

PKCS7Data::PKCS7Data(void)
{
}

PKCS7Data::PKCS7Data(const QByteArray &iData)
{
	constructObject(iData);
}

PKCS7Data::PKCS7Data(const ASN1T_PKCS7_Data &iData)
{
	QByteArray qbData((char*)iData.data,iData.numocts);
	mData = qbData;
}

PKCS7Data::PKCS7Data(const PKCS7Data& iData)
: mData(iData.getData())
{
	
}

int PKCS7Data::copyFromASNObject(const ASN1T_PKCS7_Data &iData)
{
	mData = QByteArray((char*)iData.data,iData.numocts);
	return SUCCESS;
}

int PKCS7Data::copyToASNObject(ASN1T_PKCS7_Data &oData)const 
{
	oData.data =(OSOCTET*) myStrDup(mData.data(),mData.size());
	oData.numocts = mData.size();
	return SUCCESS;
}

void PKCS7Data::freeASNObject( ASN1T_PKCS7_Data & oData)const
{
	DELETE_MEMORY_ARRAY(oData.data)
}

QByteArray PKCS7Data::getData()const
{
	return mData;
}

void PKCS7Data::setData(const QByteArray& iData)
{
	mData = iData;
}

PKCS7Data::~PKCS7Data(void)
{
}

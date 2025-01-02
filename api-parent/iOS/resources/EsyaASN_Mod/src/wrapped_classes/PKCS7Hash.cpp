#include "PKCS7Hash.h"

using namespace esya;

namespace esya
{

	PKCS7Hash::PKCS7Hash(void)
	{
	}

	PKCS7Hash::PKCS7Hash( const ASN1T_PKCS7_Hash & iHash)
	{
		copyFromASNObject(iHash);
	}

	PKCS7Hash::PKCS7Hash( const QByteArray &iHash)
	{
		constructObject(iHash);
	}

	PKCS7Hash::PKCS7Hash( const PKCS7Hash &iHash)
	:	mData(iHash.getData())
	{
	}

	PKCS7Hash::~PKCS7Hash()
	{	
	}


	PKCS7Hash & PKCS7Hash::operator=(const PKCS7Hash& iHash)
	{
		mData				= iHash.getData();
		return *this;
	}

	bool operator==(const PKCS7Hash& iRHS,const PKCS7Hash& iLHS)
	{
		return  ( iRHS.getData() == iLHS.getData() );
	}

	bool operator!=(const PKCS7Hash& iRHS, const PKCS7Hash& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int PKCS7Hash::copyFromASNObject(const ASN1T_PKCS7_Hash & iHash)
	{
		if (iHash.numocts > 0)
			mData = QByteArray((const char*)iHash.data,iHash.numocts);
		return SUCCESS;
	}

	int PKCS7Hash::copyToASNObject(ASN1T_PKCS7_Hash & oHash) const
	{
		oHash.numocts = mData.size();
		if (oHash.numocts > 0)
		{
			oHash.data = (OSOCTET*)myStrDup(mData.data(),mData.size());
		}
		return SUCCESS;
	}

	void PKCS7Hash::freeASNObject(ASN1T_PKCS7_Hash& oHash) const
	{
		if (oHash.numocts>0)
			DELETE_MEMORY_ARRAY(oHash.data);
	}

	const QByteArray & PKCS7Hash::getData() const 
	{
		return mData;
	}

	void PKCS7Hash::setData(const QByteArray & iData) 
	{
		mData= iData;
	}

}
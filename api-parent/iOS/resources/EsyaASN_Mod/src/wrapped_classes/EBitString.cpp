#include "EBitString.h"
#include "cms.h"

using namespace esya;
NAMESPACE_BEGIN(esya)
EBitString::EBitString(void)
: mNumBits(0)
{
}

EBitString::EBitString(const ASN1OCTET iOctets[] ,int iNumBits)
{
	int a = (iNumBits%8) ?  (iNumBits/8+1):iNumBits/8;
	mData = QByteArray((const char*)iOctets,a);
	mNumBits = iNumBits;
}

EBitString::EBitString(const ASN1TDynBitStr& iBitStr)
{
	mNumBits = iBitStr.numbits;
	int a = (mNumBits%8) ?  (mNumBits/8+1):mNumBits/8;
	mData = QByteArray((const char*)iBitStr.data,a);

}

EBitString::EBitString(const QByteArray& iBitStr)
{
	constructObject(iBitStr);
}

EBitString::EBitString(const EBitString& iBitString)
:	mData(iBitString.getData()), mNumBits(iBitString.getNumBits())
{
}

EBitString::EBitString(const QByteArray& iData, int  iNumBits)
: mData(iData), mNumBits(iNumBits)
{

}


EBitString& EBitString::operator=(const EBitString& iBitString)
{
	mData		= iBitString.getData();
	mNumBits	= iBitString.getNumBits();
	return *this;
}

bool operator==(const EBitString&  iRHS, const EBitString& iLHS)
{
	return (	( iRHS.getData()	== iLHS.getData())		&&
		( iRHS.getNumBits() == iLHS.getNumBits())		);
}

bool operator!=(const EBitString& iRHS, const EBitString& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int EBitString::copyToASNObject(ASN1TDynBitStr & oBitStr)const
{
	oBitStr.data = (OSOCTET*)myStrDup(mData.data(),mData.size());

	oBitStr.numbits = mNumBits;

	return SUCCESS;
}

int EBitString::copyFromASNObject(const ASN1TDynBitStr & iBitStr)
{
	mNumBits = iBitStr.numbits;
	int a = (mNumBits%8) ?  (mNumBits/8+1):mNumBits/8;
	mData = QByteArray((const char*)iBitStr.data,a);
	return SUCCESS;
}

ASN1TDynBitStr* EBitString::getASNCopy()const
{
	return getASNCopyOf(*this);
}

ASN1TDynBitStr* EBitString::getASNCopyOf(const EBitString & iBitString)
{
	ASN1TDynBitStr* pBitString = new ASN1TDynBitStr();
	iBitString.copyToASNObject(* pBitString);
	return pBitString;
}

void EBitString::freeASNObject(ASN1TDynBitStr * oBitStr)
{
	EBitString::freeASNObject(*oBitStr);
	DELETE_MEMORY(oBitStr)
}

int EBitString::constructObject(const QByteArray & iBits)
{
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iBits.data(),iBits.size());
	ASN1T_CMS_Signature bits;
	ASN1C_CMS_Signature cBits(bits);

	int stat = cBits.DecodeFrom(decBuf);
	if (stat != ASN_OK)
	{
		throw EException(QString("BitString Decode edilemedi. Hata : %1").arg(stat),__FILE__, __LINE__ );
	}
	return copyFromASNObject(bits);
}

QByteArray EBitString::getEncodedBytes()const
{
	ASN1BEREncodeBuffer encBuf;
	ASN1T_CMS_Signature *pSig = getASNCopy();
	ASN1C_CMS_Signature cSig(*pSig);


	int stat = cSig.EncodeTo(encBuf);
	if (stat < ASN_OK)
	{
		throw EException("BitString Encode edilemedi");
	}

	freeASNObject(pSig);
	return QByteArray((char*) encBuf.getMsgPtr(),stat);
}

void EBitString::freeASNObject(ASN1TDynBitStr & oBitStr)
{
	DELETE_MEMORY_ARRAY(oBitStr.data)
}

QByteArray EBitString::getData()const
{
	return mData;
}


void EBitString::setData(const QByteArray& iData)
{
	mData = iData;
}

int EBitString::getNumBits()const
{
	return mNumBits;
}

void EBitString::setNumBits(int iNumBits)
{
	mNumBits = iNumBits;
}


QString	EBitString::toString()const
{
	QString str;

	QBitArray bits = toBitArray();
	for (int i = 0; i<mNumBits;i++)
	{	
		str += bits[i] ? "1":"0";
	}
	return str;
}

QBitArray EBitString::_toBitArray(ASN1OCTET iOctet, int iNumBits)
{
	QBitArray qB(iNumBits,false);
	int a = iOctet;
	for (int i = 0 ; i < qB.size() ; i++)
	{
		bool b = (iOctet & 0x80);
		qB[i] = b;
		iOctet <<= 1;
	}
	return qB;
}

ASN1OCTET EBitString::_toOctet(const QBitArray& bits, int iIndex)
{
	ASN1OCTET octet = 0;
	int mult = 1;
	for (int i = iIndex ; i < iIndex+8 ;i++ )
	{
		if ( i< bits.size() )
			octet += mult*bits[i];
		mult*=2;
	}
	return octet;
}



QBitArray EBitString::toBitArray() const 
{
	if (mNumBits == 0 ) return QBitArray();

	QBitArray qB(mNumBits,false);

	int a = mNumBits / 8;
	int d = mNumBits % 8;

	for (int i = 0 ; i < a ; i++)
	{
		QBitArray block = _toBitArray(mData[i],8);
		for (int j = i*8 ; j<(i+1)*8; j++)
		{
			qB[j]= block[j-(i*8)];
		}
	}
	if ( d > 0 )	
	{
		QBitArray block = _toBitArray(mData[a],d);
		for (int j = a*8 ; j<mNumBits; j++)
		{
			qB[j]= block[j-a*8];
		}
	}
	return qB;
}
NAMESPACE_END

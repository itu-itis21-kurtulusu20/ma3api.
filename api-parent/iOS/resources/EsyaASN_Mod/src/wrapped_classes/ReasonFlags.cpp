#include "ReasonFlags.h"
#include "EBitString.h"

using namespace esya;


ReasonFlags::ReasonFlags(void)
{
}

ReasonFlags::ReasonFlags(const ReasonFlags &iRF)
: mNumBits(iRF.getNumBits())
{
	memcpy(mData,iRF.getData(),DATASIZE);
}

ReasonFlags::ReasonFlags(const int iNumBits , const OSUINT32 * &iData)
: mNumBits(iNumBits) 
{
	memcpy(mData,iData,DATASIZE);
}

ReasonFlags::ReasonFlags(const ASN1T_IMP_ReasonFlags & iRF)
{
	copyFromASNObject(iRF);
}

ReasonFlags::ReasonFlags(const QByteArray & iRF)
{
	constructObject(iRF);
}

ReasonFlags & ReasonFlags::operator=(const ReasonFlags& iRF)
{
	mNumBits = iRF.getNumBits();
	memcpy(mData,iRF.getData(),DATASIZE);
	return (*this);
}

bool esya::operator==(const ReasonFlags& iRHS, const ReasonFlags& iLHS)
{
	return ( iRHS.getNumBits() == iLHS.getNumBits() && !memcmp(iRHS.getData(),iLHS.getData(),ReasonFlags::DATASIZE) );
}

bool esya::operator!=(const ReasonFlags& iRHS, const ReasonFlags& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int ReasonFlags::copyFromASNObject(const ASN1T_IMP_ReasonFlags& iKU)
{
	mNumBits = iKU.numbits;
	memcpy(mData,iKU.data,DATASIZE);
	return SUCCESS;
}

int ReasonFlags::copyToASNObject(ASN1T_IMP_ReasonFlags & oRF) const
{
	oRF.numbits = mNumBits;
	memcpy(oRF.data,mData,DATASIZE);
	return SUCCESS;
}

void ReasonFlags::freeASNObject(ASN1T_IMP_ReasonFlags & oRF) const
{
	oRF.numbits = 0;
}

const int& ReasonFlags::getNumBits() const
{
	return mNumBits;
}

const OSOCTET* ReasonFlags::getData() const
{
	return mData;
}

QBitArray ReasonFlags::getRFBits() const
{
	return EBitString(mData,mNumBits).toBitArray();
}

QString ReasonFlags::toBitString() const
{
	QString str;
	QBitArray bits = getRFBits();
	for (int i = 0; i < bits.size(); i++ )
	{
		str += bits[i] ? "1":"0";	
	}
	return str;
}

QString ReasonFlags::toString() const
{
	return toBitString();
}

ReasonFlags::~ReasonFlags(void)
{
}

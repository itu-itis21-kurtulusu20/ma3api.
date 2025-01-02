#include "AnotherName.h"
#include "ortak.h"
NAMESPACE_BEGIN(esya)
//using namespace esya;

AnotherName::AnotherName(void)
{
}

AnotherName::AnotherName(const ASN1T_IMP_AnotherName & iAN)
{
	copyFromASNObject(iAN);
}
AnotherName::AnotherName(const AnotherName & iAN)
: mTypeID(iAN.getTypeID()) , mValue(iAN.getValue())
{
}

AnotherName::AnotherName(const  QByteArray &iAN)
{
	constructObject(iAN);
}

AnotherName::AnotherName(const ASN1TObjId& iTypeID , const  QByteArray & iValue)
: mTypeID(iTypeID), mValue(iValue)
{
}

AnotherName& AnotherName::operator=(const AnotherName& iAN )
{
	mTypeID = iAN.getTypeID();
	mValue	= iAN.getValue();
	return *this;
}

bool operator==(const AnotherName & iRHS,const AnotherName & iLHS)
{
	return (	(iRHS.getTypeID()	== iLHS.getTypeID()) &&
				(iRHS.getValue()	== iLHS.getValue())		);
}

bool operator!=(const AnotherName & iRHS, const AnotherName & iLHS)
{
	return !( iRHS == iLHS );
}


int AnotherName::copyFromASNObject(const ASN1T_IMP_AnotherName & iAN)
{
	mTypeID = iAN.type_id ;
	mValue	= QByteArray((const char*)iAN.value.data,iAN.value.numocts);
	return SUCCESS;
}

int AnotherName::copyToASNObject(ASN1T_IMP_AnotherName & oAN) const
{
	oAN.type_id = mTypeID;
	oAN.value.data = (OSOCTET*)myStrDup(mValue.data(),mValue.size());
	oAN.value.numocts = mValue.size();
	return SUCCESS;
}

void AnotherName::freeASNObject(ASN1T_IMP_AnotherName &oAN )const
{
	DELETE_MEMORY_ARRAY(oAN.value.data)
}

const ASN1TObjId& AnotherName::getTypeID()const
{
	return mTypeID;
}

const QByteArray& AnotherName::getValue()const
{
	return mValue;
}

AnotherName::~AnotherName(void)
{
}
NAMESPACE_END

#include "InhibitAnyPolicy.h"
#include "ortak.h"
#include "OrtakDil.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

InhibitAnyPolicy::InhibitAnyPolicy(void)
{
}

InhibitAnyPolicy::InhibitAnyPolicy(const QByteArray & iInhibitAnyPolicy)
{
	constructObject(iInhibitAnyPolicy);
}

InhibitAnyPolicy::InhibitAnyPolicy(const ASN1T_IMP_InhibitAnyPolicy & iInhibitAnyPolicy)
{
	copyFromASNObject(iInhibitAnyPolicy);
}

InhibitAnyPolicy::InhibitAnyPolicy(const InhibitAnyPolicy& iInhibitAnyPolicy)
: mValue(iInhibitAnyPolicy.getValue())
{
}

InhibitAnyPolicy::InhibitAnyPolicy(const int & iValue)
: mValue(iValue)
{
}

InhibitAnyPolicy & InhibitAnyPolicy::operator=(const InhibitAnyPolicy& iInhibitAnyPolicy)
{
	mValue = iInhibitAnyPolicy.getValue();
	return (*this);
}

bool operator==( const InhibitAnyPolicy& iRHS, const InhibitAnyPolicy& iLHS)
{
	return ( iRHS.getValue() == iLHS.getValue() );
}

bool operator!=( const InhibitAnyPolicy& iRHS, const InhibitAnyPolicy& iLHS)
{
	return ( !(iRHS == iLHS) );
}


int InhibitAnyPolicy::copyFromASNObject(const ASN1T_IMP_InhibitAnyPolicy & iInhibitAnyPolicy)
{
	mValue = iInhibitAnyPolicy;
	return SUCCESS;
}

int InhibitAnyPolicy::copyToASNObject(ASN1T_IMP_InhibitAnyPolicy &oInhibitAnyPolicy)const
{
	oInhibitAnyPolicy = mValue;
	return SUCCESS;
}

void InhibitAnyPolicy::freeASNObject(ASN1T_IMP_InhibitAnyPolicy& oInhibitAnyPolicy)const
{
}

const int& InhibitAnyPolicy::getValue() const 
{
	return mValue;
}


InhibitAnyPolicy::~InhibitAnyPolicy(void)
{
}

QString InhibitAnyPolicy::toString()const
{
	return  QString().setNum(mValue);
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString InhibitAnyPolicy::eklentiAdiAl() const 
{
	return DIL_EXT_INHIBIT_ANY_POLICY;
}

QString InhibitAnyPolicy::eklentiKisaDegerAl()	const 
{
	return toString();
}

QString InhibitAnyPolicy::eklentiUzunDegerAl()	const 
{
	return toString();
}

AY_Eklenti* InhibitAnyPolicy::kendiniKopyala() const 
{
	return (AY_Eklenti* )new InhibitAnyPolicy(*this);
}
NAMESPACE_END

#include "CRLNumber.h"
#include "ortak.h"
#include "OrtakDil.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

CRLNumber::CRLNumber(void)
{
}

CRLNumber::CRLNumber(const QByteArray & iCRLNumber)
{
	constructObject(iCRLNumber);
}

CRLNumber::CRLNumber(const ASN1T_IMP_CRLNumber & iCRLNumber)
{
	copyFromASNObject(iCRLNumber);
}

CRLNumber::CRLNumber(const CRLNumber& iCRLNumber)
: mValue(iCRLNumber.getValue())
{
}

CRLNumber::CRLNumber(const QString & iValue)
: mValue(iValue)
{
}

CRLNumber & CRLNumber::operator=(const CRLNumber& iCRLNumber)
{
	mValue = iCRLNumber.getValue();
	return (*this);
}

bool operator==( const CRLNumber& iRHS, const CRLNumber& iLHS)
{
	return ( iRHS.getValue() == iLHS.getValue() );
}

bool operator!=( const CRLNumber& iRHS, const CRLNumber& iLHS)
{
	return ( !(iRHS == iLHS) );
}


int CRLNumber::copyFromASNObject(const ASN1T_IMP_CRLNumber & iCRLNumber)
{
	mValue = QString(iCRLNumber);
	return SUCCESS;
}

int CRLNumber::copyToASNObject(ASN1T_IMP_CRLNumber &oCRLNumber)const
{
	oCRLNumber = myStrDup( mValue );
	return SUCCESS;
}

void CRLNumber::freeASNObject(ASN1T_IMP_CRLNumber& oCRLNumber) const
{
	DELETE_MEMORY_ARRAY(oCRLNumber)
	return;
}

const QString& CRLNumber::getValue() const 
{
	return mValue;
}

CRLNumber::~CRLNumber(void)
{
}

QString CRLNumber::toString()const
{
	return mValue;
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString CRLNumber::eklentiAdiAl() const 
{
	return DIL_EXT_SIL_NUMARASI;
}

QString CRLNumber::eklentiKisaDegerAl()	const 
{
	return toString();
}

QString CRLNumber::eklentiUzunDegerAl()	const 
{
	return toString();
}

AY_Eklenti* CRLNumber::kendiniKopyala() const 
{
	return (AY_Eklenti* )new CRLNumber(*this);
}
NAMESPACE_END

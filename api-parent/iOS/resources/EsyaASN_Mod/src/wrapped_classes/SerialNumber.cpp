#include "SerialNumber.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

SerialNumber::SerialNumber(void)
{
}

SerialNumber::SerialNumber( const ASN1T_EXP_CertificateSerialNumber &iSN)
{
	copyFromASNObject(iSN);
}

bool SerialNumber::operator<(const SerialNumber& iRHS)const
{
	return ( mValue < iRHS.getValue() );
}

SerialNumber::SerialNumber( const QString &iSN)
: mValue(iSN)
{

}

SerialNumber::SerialNumber( const SerialNumber &iSN)
: mValue(iSN.getValue())
{
}

SerialNumber::SerialNumber( const QByteArray &iSN)
{
	constructObject(iSN);
}

SerialNumber & SerialNumber::operator=(const SerialNumber& iSN)
{
	mValue= iSN.getValue();
	return(*this);

}

bool operator==(const SerialNumber& iRHS,const SerialNumber& iLHS)
{
	return ( iRHS.getValue() == iLHS.getValue() );
}

bool operator!=(const SerialNumber& iRHS, const SerialNumber& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int SerialNumber::copyFromASNObject(const ASN1T_EXP_CertificateSerialNumber & iSN)
{
	mValue= QString(iSN);
	return SUCCESS;
}

int SerialNumber::copyToASNObject(ASN1T_EXP_CertificateSerialNumber & oSN ) const
{
	oSN = myStrDup(mValue);
	return SUCCESS;
}
	
void SerialNumber::freeASNObject(ASN1T_EXP_CertificateSerialNumber& oSN)const
{
	DELETE_MEMORY_ARRAY(oSN);
}

QString SerialNumber::getValue() const 
{
	return mValue;
}
	
void SerialNumber::setValue(const QString & iSN)  
{
	mValue = iSN;
}
/*
long SerialNumber::toLong()const
{
	QString serino = mValue;
	int base = 10;
	if ( serino.indexOf("0x",0,Qt::CaseInsensitive)>=0)
	{
		serino.remove("0x",Qt::CaseInsensitive);
		base = 16;
	}
	
	return serino.toLong(0,base);
}*/

SerialNumber::~SerialNumber()
{
}
NAMESPACE_END

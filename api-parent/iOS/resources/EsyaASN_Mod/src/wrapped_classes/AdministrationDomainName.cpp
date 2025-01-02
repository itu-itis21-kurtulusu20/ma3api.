#include "AdministrationDomainName.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

AdministrationDomainName::AdministrationDomainName(void)
{
}

AdministrationDomainName::AdministrationDomainName( const ASN1T_EXP_AdministrationDomainName&iADN)
{
	copyFromASNObject(iADN);
}


AdministrationDomainName::AdministrationDomainName( const AdministrationDomainName::AdministrationDomainNameType &iType, const QString& iCode)
: mCode(iCode),mType(iType)
{
}

AdministrationDomainName::AdministrationDomainName( const AdministrationDomainName &iADN)
: mType(iADN.getType()), mCode(iADN.getCode())
{
}

AdministrationDomainName::AdministrationDomainName( const QByteArray &iADN)
{
	constructObject(iADN);
}

AdministrationDomainName & AdministrationDomainName::operator=(const AdministrationDomainName& iADN)
{
	mType = iADN.getType();
	mCode= iADN.getCode();
	return(*this);
}

bool operator==(const AdministrationDomainName& iRHS,const AdministrationDomainName& iLHS)
{
	return ( (iRHS.getType() == iLHS.getType()) &&(iRHS.getCode() == iLHS.getCode()) );
}

bool operator!=(const AdministrationDomainName& iRHS, const AdministrationDomainName& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int AdministrationDomainName::copyFromASNObject(const ASN1T_EXP_AdministrationDomainName & iADN)
{
	mType = (AdministrationDomainNameType)(iADN.t);
	switch (mType)
	{
	case ADNT_NUMERIC:
		{
			mCode = QString(iADN.u.numeric);
			break;
		}
	case ADNT_PRINTABLE:
		{
			mCode = QString(iADN.u.printable);
			break;
		}
	}
	return SUCCESS;
}

int AdministrationDomainName::copyToASNObject(ASN1T_EXP_AdministrationDomainName & oADN) const
{
	oADN.t  = mType;

	switch (mType)
	{
	case ADNT_NUMERIC:
		{
			oADN.u.numeric= myStrDup(mCode);
			break;
		}
	case ADNT_PRINTABLE:
		{
			oADN.u.printable= myStrDup(mCode);
			break;
		}
	}

	return SUCCESS;
}
	
void AdministrationDomainName::freeASNObject(ASN1T_EXP_AdministrationDomainName& oADN)const
{
	switch (mType)
	{
	case ADNT_NUMERIC:
		{
			DELETE_MEMORY_ARRAY(oADN.u.numeric);
			break;
		}
	case ADNT_PRINTABLE:
		{
			DELETE_MEMORY_ARRAY(oADN.u.printable);
			break;
		}
	}
	

}

const AdministrationDomainName::AdministrationDomainNameType& AdministrationDomainName::getType() const 
{
	return mType;
}

void AdministrationDomainName::setType(const AdministrationDomainName::AdministrationDomainNameType & iType)  
{
	mType = iType;
}

const QString& AdministrationDomainName::getCode() const 
{
	return mCode;
}
	
void AdministrationDomainName::setCode(const QString & iCode)  
{
	mCode = iCode;
}


AdministrationDomainName::~AdministrationDomainName()
{
}
NAMESPACE_END

#include "OrganizationalUnitName.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

OrganizationalUnitName::OrganizationalUnitName(void)
{
}

OrganizationalUnitName::OrganizationalUnitName( const ASN1T_EXP_OrganizationalUnitName &iOUN)
{
	copyFromASNObject(iOUN);
}

OrganizationalUnitName::OrganizationalUnitName( const QString &iOUN)
: mValue(iOUN)
{

}

OrganizationalUnitName::OrganizationalUnitName( const OrganizationalUnitName &iOUN)
: mValue(iOUN.getValue())
{
}

OrganizationalUnitName::OrganizationalUnitName( const QByteArray &iOUN)
{
	constructObject(iOUN);
}

OrganizationalUnitName & OrganizationalUnitName::operator=(const OrganizationalUnitName& iOUN)
{
	mValue= iOUN.getValue();
	return(*this);

}

bool operator==(const OrganizationalUnitName& iRHS,const OrganizationalUnitName& iLHS)
{
	return ( iRHS.getValue() == iLHS.getValue() );
}

bool operator!=(const OrganizationalUnitName& iRHS, const OrganizationalUnitName& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int OrganizationalUnitName::copyFromASNObject(const ASN1T_EXP_OrganizationalUnitName & iOUN)
{
	mValue= QString(iOUN);
	return SUCCESS;
}

int OrganizationalUnitName::copyToASNObject(ASN1T_EXP_OrganizationalUnitName & oOUN ) const
{
	oOUN = myStrDup(mValue);
	return SUCCESS;
}
	
void OrganizationalUnitName::freeASNObject(ASN1T_EXP_OrganizationalUnitName& oOUN)const
{
	DELETE_MEMORY_ARRAY(oOUN);
}


int OrganizationalUnitName::copyOUNs(const ASN1T_EXP_OrganizationalUnitNames & iOUNs, QList<OrganizationalUnitName>& oList)const
{
	return copyASNObjects<OrganizationalUnitName>(iOUNs,oList);
}

int OrganizationalUnitName::copyOUNs(const QList<OrganizationalUnitName> iList ,ASN1T_EXP_OrganizationalUnitNames & oOUNs)const
{
	return copyASNObjects<OrganizationalUnitName>(iList,oOUNs);
}

int	OrganizationalUnitName::copyOUNs(const QByteArray & iASNBytes, QList<OrganizationalUnitName>& oList)const
{
	return copyASNObjects<ASN1T_EXP_OrganizationalUnitNames,ASN1C_EXP_OrganizationalUnitNames,OrganizationalUnitName>(iASNBytes,oList);
}

int	OrganizationalUnitName::copyOUNs(const QList<OrganizationalUnitName>& iList,QByteArray & oASNBytes)const
{
	return copyASNObjects<ASN1T_EXP_OrganizationalUnitNames,ASN1C_EXP_OrganizationalUnitNames,OrganizationalUnitName>(iList,oASNBytes);
}

void OrganizationalUnitName::freeOUNs(ASN1T_EXP_OrganizationalUnitNames & oOUNs)const
{
	freeASNObjects(oOUNs);
}


QString OrganizationalUnitName::getValue() const 
{
	return mValue;
}
	
void OrganizationalUnitName::setValue(const QString & iOUN)  
{
	mValue = iOUN;
}

OrganizationalUnitName::~OrganizationalUnitName()
{
}
NAMESPACE_END

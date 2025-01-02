#include "SMIMECapabilities.h"

namespace esya
{


SMIMECapabilities::SMIMECapabilities(void)
{
}

SMIMECapabilities::SMIMECapabilities(const ASN1T_CMS_SMIMECapabilities & iCapabilities)
{
	copyFromASNObject(iCapabilities);
}

SMIMECapabilities::SMIMECapabilities(const QByteArray &iCapabilities)
{
	constructObject(iCapabilities);
}

SMIMECapabilities::SMIMECapabilities(const QList<SMIMECapability>& iList)
:	mList(iList)
{
}

SMIMECapabilities::SMIMECapabilities(const SMIMECapabilities&iCapabilities)
:	mList(iCapabilities.getList())
{
}

SMIMECapabilities & SMIMECapabilities::operator=(const SMIMECapabilities&iCapabilities)
{
	mList = iCapabilities.getList();
	return *this;
}

bool operator==(const SMIMECapabilities & iRHS,const SMIMECapabilities & iLHS)
{
	return ( iRHS.getList() == iLHS.getList() );
}

bool operator!=(const SMIMECapabilities & iRHS, const SMIMECapabilities & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int SMIMECapabilities::copyFromASNObject(const ASN1T_CMS_SMIMECapabilities& iCapabilities)
{
	SMIMECapability().copyCapabilities(iCapabilities,mList);
	return SUCCESS;
}

int SMIMECapabilities::copyToASNObject(ASN1T_CMS_SMIMECapabilities& oCapabilities)const
{
	SMIMECapability().copyCapabilities(mList,oCapabilities);
	return SUCCESS;
}

void SMIMECapabilities::freeASNObject(ASN1T_CMS_SMIMECapabilities & oCapabilities ) const
{
	SMIMECapability().freeASNObjects(oCapabilities);
}

const QList<SMIMECapability> &SMIMECapabilities::getList() const
{
	return mList;
}

void SMIMECapabilities::setList(const QList<SMIMECapability> & iList)
{
	mList = iList;
}

void SMIMECapabilities::appendCapability( const SMIMECapability & iCapability )
{
	mList.append(iCapability);
}

SMIMECapabilities::~SMIMECapabilities(void)
{
}

}

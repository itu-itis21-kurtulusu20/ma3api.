#include "ProxyInfo.h"

namespace esya
{


ProxyInfo::ProxyInfo(void)
{
}

ProxyInfo::ProxyInfo(const ASN1T_ATTRCERT_ProxyInfo & iProxyInfo)
{
	copyFromASNObject(iProxyInfo);
}

ProxyInfo::ProxyInfo(const QByteArray &iProxyInfo)
{
	constructObject(iProxyInfo);
}

ProxyInfo::ProxyInfo(const QList<Targets>& iList)
:	mList(iList)
{
}

ProxyInfo::ProxyInfo(const ProxyInfo&iProxyInfo)
:	mList(iProxyInfo.getList())
{
}

ProxyInfo & ProxyInfo::operator=(const ProxyInfo& iProxyInfo)
{
	mList = iProxyInfo.getList();
	return *this;
}

bool operator==(const ProxyInfo & iRHS,const ProxyInfo & iLHS)
{
	return ( iRHS.getList() == iLHS.getList() );
}

bool operator!=(const ProxyInfo & iRHS, const ProxyInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int ProxyInfo::copyFromASNObject(const ASN1T_ATTRCERT_ProxyInfo& iProxyInfo)
{
	Targets().copyProxyInfo(iProxyInfo,mList);
	return SUCCESS;
}

int ProxyInfo::copyToASNObject(ASN1T_ATTRCERT_ProxyInfo& oProxyInfo)const
{
	Targets().copyProxyInfo(mList,oProxyInfo);
	return SUCCESS;
}

void ProxyInfo::freeASNObject(ASN1T_ATTRCERT_ProxyInfo & oProxyInfo ) const
{
	Targets().freeASNObjects(oProxyInfo);
}

const QList<Targets> &ProxyInfo::getList() const
{
	return mList;
}

void ProxyInfo::setList(const QList<Targets> & iList)
{
	mList = iList;
}

void ProxyInfo::appendTargets( const Targets & iTargets )
{
	mList.append(iTargets);
}

ProxyInfo::~ProxyInfo(void)
{
}

}

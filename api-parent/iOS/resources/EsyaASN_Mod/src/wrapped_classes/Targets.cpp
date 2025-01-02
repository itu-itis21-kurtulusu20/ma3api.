#include "Targets.h"

namespace esya
{

Targets::Targets(void)
{
}

Targets::Targets(const ASN1T_ATTRCERT_Targets & iTargets)
{
	copyFromASNObject(iTargets);
}

Targets::Targets(const QByteArray &iTargets)
{
	constructObject(iTargets);
}

Targets::Targets(const QList<Target>& iList)
:	mList(iList)
{
}

Targets::Targets(const Targets&iTargets)
:	mList(iTargets.getList())
{
}

Targets & Targets::operator=(const Targets& iTargets)
{
	mList = iTargets.getList();
	return *this;
}

bool operator==(const Targets & iRHS,const Targets & iLHS)
{
	return ( iRHS.getList() == iLHS.getList() );
}

bool operator!=(const Targets & iRHS, const Targets & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int Targets::copyFromASNObject(const ASN1T_ATTRCERT_Targets& iTargets)
{
	Target().copyTargets(iTargets,mList);
	return SUCCESS;
}

int Targets::copyToASNObject(ASN1T_ATTRCERT_Targets& oTargets)const
{
	Target().copyTargets(mList,oTargets);
	return SUCCESS;
}

void Targets::freeASNObject(ASN1T_ATTRCERT_Targets & oTargets ) const
{
	Target().freeASNObjects(oTargets);
}

int Targets::copyProxyInfo(const ASN1T_ATTRCERT_ProxyInfo & iProxyInfo, QList<Targets>& oList)
{
	return copyASNObjects<Targets>(iProxyInfo,oList);
}

int Targets::copyProxyInfo(const QList<Targets> iList ,ASN1T_ATTRCERT_ProxyInfo & oProxyInfo)
{
	return copyASNObjects<Targets>(iList,oProxyInfo);
}

int Targets::copyProxyInfo(const QByteArray & iASNBytes, QList<Targets>& oList)
{
	return copyASNObjects<ASN1T_ATTRCERT_ProxyInfo,ASN1C_ATTRCERT_ProxyInfo,Targets>(iASNBytes,oList);
}

const QList<Target> &Targets::getList() const
{
	return mList;
}

void Targets::setList(const QList<Target> & iList)
{
	mList = iList;
}

void Targets::appendTarget( const Target & iTarget )
{
	mList.append(iTarget);
}

Targets::~Targets(void)
{
}

}

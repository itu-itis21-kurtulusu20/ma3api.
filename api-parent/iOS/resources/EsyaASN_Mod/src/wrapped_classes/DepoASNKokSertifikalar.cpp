
#include "DepoASNKokSertifikalar.h"

using namespace esya;

DepoASNKokSertifikalar::DepoASNKokSertifikalar(void)
{
}

DepoASNKokSertifikalar::DepoASNKokSertifikalar(const ASN1T_SD_DepoASNKokSertifikalar & iKSList)
{
	copyFromASNObject(iKSList);
}

DepoASNKokSertifikalar::DepoASNKokSertifikalar(const QByteArray & iKSList)
{
	constructObject(iKSList);
}

DepoASNKokSertifikalar::DepoASNKokSertifikalar(const DepoASNKokSertifikalar &iKSList)
: mList(iKSList.getList())
{
}

DepoASNKokSertifikalar & DepoASNKokSertifikalar::operator=(const DepoASNKokSertifikalar& iKSList)
{
	mList = iKSList.getList();
	return *this;
}

bool esya::operator==(const DepoASNKokSertifikalar & iRHS, const DepoASNKokSertifikalar& iLHS)
{
	return ( iRHS.getList() ==  iLHS.getList() );
}

bool esya::operator!=(const DepoASNKokSertifikalar & iRHS, const DepoASNKokSertifikalar& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNKokSertifikalar::copyFromASNObject(const ASN1T_SD_DepoASNKokSertifikalar& iKSList)
{
	DepoASNKokSertifika().copyDepoASNKokSertifikaList(iKSList,mList);
	return SUCCESS;
}

int DepoASNKokSertifikalar::copyToASNObject(ASN1T_SD_DepoASNKokSertifikalar & oKSList) const
{
	DepoASNKokSertifika().copyDepoASNKokSertifikaList(mList,oKSList);
	return SUCCESS;
}

void DepoASNKokSertifikalar::freeASNObject(ASN1T_SD_DepoASNKokSertifikalar& oKSList)const
{
	DepoASNKokSertifika().freeASNObjects(oKSList);
}

const QList<DepoASNKokSertifika> &DepoASNKokSertifikalar::getList() const 
{
	return mList;
}

void DepoASNKokSertifikalar::setList(const QList<DepoASNKokSertifika> &iList)
{
	mList = iList;
}

void DepoASNKokSertifikalar::addKokSertifika(const DepoASNKokSertifika &iKS)
{
	mList.append(iKS);
}


DepoASNKokSertifikalar::~DepoASNKokSertifikalar(void)
{
}

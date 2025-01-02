
#include "DepoASNImzalar.h"

using namespace esya;

DepoASNImzalar::DepoASNImzalar(void)
{
}

DepoASNImzalar::DepoASNImzalar(const ASN1T_SD_DepoASNImzalar & iImzaList)
{
	copyFromASNObject(iImzaList);
}

DepoASNImzalar::DepoASNImzalar(const QByteArray & iImzaList)
{
	constructObject(iImzaList);
}

DepoASNImzalar::DepoASNImzalar(const DepoASNImzalar &iImzaList)
:	mList(iImzaList.getList())
{
}

DepoASNImzalar & DepoASNImzalar::operator=(const DepoASNImzalar& iImzaList)
{
	mList = iImzaList.getList();
	return *this;
}

bool esya::operator==(const DepoASNImzalar & iRHS, const DepoASNImzalar& iLHS)
{
	return ( iRHS.getList() == iLHS.getList() );
}

bool esya::operator!=(const DepoASNImzalar & iRHS, const DepoASNImzalar& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNImzalar::copyFromASNObject(const ASN1T_SD_DepoASNImzalar& iImzaList)
{
	DepoASNImza().copyDepoASNImzaList(iImzaList,mList);
	return SUCCESS;
}

int DepoASNImzalar::copyToASNObject(ASN1T_SD_DepoASNImzalar & oImzaList) const
{
	DepoASNImza().copyDepoASNImzaList(mList,oImzaList);
	return SUCCESS;
}

void DepoASNImzalar::freeASNObject(ASN1T_SD_DepoASNImzalar& oImzaList) const
{
	DepoASNImza().freeASNObjects(oImzaList);
}

const QList<DepoASNImza> &DepoASNImzalar::getList() const 
{
	return mList;
}

void DepoASNImzalar::setList(const QList<DepoASNImza> &iList)
{
	mList = iList;
}

void DepoASNImzalar::addImza(const DepoASNImza &iImza)
{
	mList.append(iImza);
}


DepoASNImzalar::~DepoASNImzalar(void)
{
}

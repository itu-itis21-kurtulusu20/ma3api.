#include "GeneralNames.h"

namespace esya
{


GeneralNames::GeneralNames(void)
{
}

GeneralNames::GeneralNames(const ASN1T_IMP_GeneralNames & iGeneralNames)
{
	copyFromASNObject(iGeneralNames);
}

GeneralNames::GeneralNames(const QByteArray &iGeneralNames)
{
	constructObject(iGeneralNames);
}

GeneralNames::GeneralNames(const QList<GeneralName>& iList)
:	mList(iList)
{
}

GeneralNames::GeneralNames(const GeneralNames&iGeneralNames)
:	mList(iGeneralNames.getList())
{
}

GeneralNames & GeneralNames::operator=(const GeneralNames&iCapabilities)
{
	mList = iCapabilities.getList();
	return *this;
}

bool operator==(const GeneralNames & iRHS,const GeneralNames & iLHS)
{
	return ( iRHS.getList() == iLHS.getList() );
}

bool operator!=(const GeneralNames & iRHS, const GeneralNames & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int GeneralNames::copyFromASNObject(const ASN1T_IMP_GeneralNames& iGeneralNames)
{
	GeneralName().copyGeneralNames(iGeneralNames,mList);
	return SUCCESS;
}

////////////////// OBJE KOPYALAMA RUTINLERI ////////////////////

int GeneralNames::copyToASNObject(ASN1T_IMP_GeneralNames& oGeneralNames)const
{
	GeneralName().copyGeneralNames(mList,oGeneralNames);
	return SUCCESS;
}

void GeneralNames::freeASNObject(ASN1T_IMP_GeneralNames & oGeneralNames )const
{
	GeneralName().freeASNObjects(oGeneralNames);
}

const QList<GeneralName> &GeneralNames::getList() const
{
	return mList;
}

void GeneralNames::setList(const QList<GeneralName> & iList)
{
	mList = iList;
}

void GeneralNames::appendGeneralName( const GeneralName & iGN )
{
	mList.append(iGN);
}

GeneralNames::~GeneralNames(void)
{
}

}

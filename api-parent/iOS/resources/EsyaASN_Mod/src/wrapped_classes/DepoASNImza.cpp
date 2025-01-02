
#include "DepoASNImza.h"

using namespace esya;

DepoASNImza::DepoASNImza(void)
{
}

DepoASNImza::DepoASNImza(const ASN1T_SD_DepoASNImza & iImza)
{
	copyFromASNObject(iImza);
}

DepoASNImza::DepoASNImza(const QByteArray & iImza)
{
	constructObject(iImza);
}

DepoASNImza::DepoASNImza(const DepoASNKokSertifika &iKS,const DepoASNRawImza & iRI)
:	mImzalanan(iKS),
	mImza(iRI)
{
}

DepoASNImza::DepoASNImza(const DepoASNImza&iImza)
:	mImzalanan(iImza.getImzalanan()),
	mImza(iImza.getImza())
{
}

DepoASNImza & DepoASNImza::operator=(const DepoASNImza & iImza)
{
	mImzalanan	= iImza.getImzalanan();
	mImza		= iImza.getImza();
	return *this;
}

bool esya::operator==(const DepoASNImza& iRHS, const DepoASNImza& iLHS)
{
	return (	( iRHS.getImzalanan()	== iLHS.getImzalanan()	) && 
				( iRHS.getImza()		== iLHS.getImza()		)	);
}

bool esya::operator!=(const DepoASNImza& iRHS, const DepoASNImza& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int DepoASNImza::copyFromASNObject(const ASN1T_SD_DepoASNImza& iImza)
{
	mImzalanan.copyFromASNObject(iImza.imzalanan);
	mImza.copyFromASNObject(iImza.imza);
	return SUCCESS;
}

int DepoASNImza::copyToASNObject(ASN1T_SD_DepoASNImza & oImza) const
{
	mImzalanan.copyToASNObject(oImza.imzalanan);
	mImza.copyToASNObject(oImza.imza);
	return SUCCESS;
}

void DepoASNImza::freeASNObject(ASN1T_SD_DepoASNImza& oImza)const
{
	DepoASNKokSertifika().freeASNObject(oImza.imzalanan);
	DepoASNRawImza().freeASNObject(oImza.imza);
}

int  DepoASNImza::copyDepoASNImzaList(const QList<DepoASNImza> iList ,ASN1TPDUSeqOfList & oImzas)
{
	return copyASNObjects<DepoASNImza>(iList,oImzas);
}

int	DepoASNImza::copyDepoASNImzaList(const ASN1TPDUSeqOfList & iImzas, QList<DepoASNImza>& oList)
{
	return copyASNObjects<DepoASNImza>(iImzas,oList);
}

const DepoASNKokSertifika	& DepoASNImza::getImzalanan()	const
{
	return mImzalanan;
}

const DepoASNRawImza & DepoASNImza::getImza() const
{
	return mImza;
}

void DepoASNImza::setImzalanan(const DepoASNKokSertifika	& iKS) 
{
	mImzalanan = iKS;
}
void DepoASNImza::setImza(const DepoASNRawImza & iImza) 
{
	mImza	= iImza;
}

DepoASNImza::~DepoASNImza(void)
{
}

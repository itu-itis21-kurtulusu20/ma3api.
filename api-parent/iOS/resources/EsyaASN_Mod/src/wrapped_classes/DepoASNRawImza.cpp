#include "DepoASNRawImza.h"

using namespace esya;

NAMESPACE_BEGIN(esya)

DepoASNRawImza::DepoASNRawImza(void)
{
}

DepoASNRawImza::DepoASNRawImza(const ASN1T_SD_DepoASNRawImza & iRI)
{
	copyFromASNObject(iRI);
}

DepoASNRawImza::DepoASNRawImza(const QByteArray & iRI)
{
	constructObject(iRI);
}

DepoASNRawImza::DepoASNRawImza(const QByteArray &iPKHash ,const QByteArray & iImza)
:	mPublicKeyHash(iPKHash),
	mImza(iImza)
{
}

DepoASNRawImza::DepoASNRawImza(const DepoASNRawImza&iKS)
:	mPublicKeyHash(iKS.getPublicKeyHash()),
	mImza(iKS.getImza())
{
}

DepoASNRawImza & DepoASNRawImza::operator=(const DepoASNRawImza & iRI)
{
	mPublicKeyHash	= iRI.getPublicKeyHash();
	mImza			= iRI.getImza();
	return *this;
}

bool operator==(const DepoASNRawImza& iRHS, const DepoASNRawImza& iLHS)
{
	return (	( iRHS.getPublicKeyHash()	== iLHS.getPublicKeyHash()	) &&
				( iRHS.getImza()			== iLHS.getImza()			)	);
}

bool operator!=(const DepoASNRawImza& iRHS, const DepoASNRawImza& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNRawImza::copyFromASNObject(const ASN1T_SD_DepoASNRawImza& iRI)
{
	mPublicKeyHash	= toByteArray(iRI.publicKeyHash);
	mImza			= toByteArray(iRI.imza);
	return SUCCESS;
}

int DepoASNRawImza::copyToASNObject(ASN1T_SD_DepoASNRawImza & oRI) const
{
	oRI.publicKeyHash.numocts =  mPublicKeyHash.size();
	oRI.publicKeyHash.data = (OSOCTET*) myStrDup(mPublicKeyHash.data(), oRI.publicKeyHash.numocts);

	oRI.imza.numocts =  mImza.size();
	oRI.imza.data = (OSOCTET*) myStrDup(mImza.data(), oRI.imza.numocts);

	return SUCCESS;
}

void DepoASNRawImza::freeASNObject(ASN1T_SD_DepoASNRawImza& oRI)const
{
	if ( oRI.publicKeyHash.numocts > 0 ) 
		DELETE_MEMORY_ARRAY(oRI.publicKeyHash.data)

	if ( oRI.imza.numocts > 0 ) 
		DELETE_MEMORY_ARRAY(oRI.imza.data)
}

int  DepoASNRawImza::copyDepoASNRawImzaList(const QList<DepoASNRawImza> iList ,ASN1TPDUSeqOfList & oRIs)
{
	return copyASNObjects<DepoASNRawImza>(iList,oRIs);
}

int	DepoASNRawImza::copyDepoASNRawImzaList(const ASN1TPDUSeqOfList & iRIs, QList<DepoASNRawImza>& oList)
{
	return copyASNObjects<DepoASNRawImza>(iRIs,oList);
}

const QByteArray & DepoASNRawImza::getPublicKeyHash() const
{
	return mPublicKeyHash;
}

const QByteArray & DepoASNRawImza::getImza() const
{
	return mImza;
}

void DepoASNRawImza::setPublicKeyHash(const QByteArray & iPKHash) 
{
	mPublicKeyHash = iPKHash;
}

void DepoASNRawImza::setImza(const QByteArray & iImza) 
{	
	mImza = iImza;
}

DepoASNRawImza::~DepoASNRawImza(void)
{
}
NAMESPACE_END

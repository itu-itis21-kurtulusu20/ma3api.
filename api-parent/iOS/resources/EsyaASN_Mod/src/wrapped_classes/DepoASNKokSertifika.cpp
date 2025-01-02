#include "DepoASNKokSertifika.h"

using namespace esya;

DepoASNKokSertifika::DepoASNKokSertifika(void)
:	mType(KS_eklenecekSertifika),
	mEklenecekSertifika(NULL),
	mSilinecekSertifika(NULL)
{
}

DepoASNKokSertifika::DepoASNKokSertifika(const ASN1T_SD_DepoASNKokSertifika & iKS)
:	mEklenecekSertifika(NULL),
	mSilinecekSertifika(NULL)
{
	copyFromASNObject(iKS);
}

DepoASNKokSertifika::DepoASNKokSertifika(const QByteArray & iKS)
:	mEklenecekSertifika(NULL),
	mSilinecekSertifika(NULL)
{
	constructObject(iKS);
}

DepoASNKokSertifika::DepoASNKokSertifika(DepoASNEklenecekKokSertifika *iEKS)
:	mType(KS_eklenecekSertifika),
	mEklenecekSertifika(iEKS),
	mSilinecekSertifika(NULL)
{
}

DepoASNKokSertifika::DepoASNKokSertifika(DepoASNSilinecekKokSertifika *iSKS)
:	mType(KS_silinecekSertifika),
	mEklenecekSertifika(NULL),
	mSilinecekSertifika(iSKS)
{
}

DepoASNKokSertifika::DepoASNKokSertifika(const DepoASNKokSertifika &iKS)
:	mEklenecekSertifika(NULL),
	mSilinecekSertifika(NULL)
{
	*this = iKS;
}

DepoASNKokSertifika & DepoASNKokSertifika::operator=(const DepoASNKokSertifika & iKS)
{
	if ( mEklenecekSertifika != iKS.getEklenecekSertifika() )
		DELETE_MEMORY(mEklenecekSertifika)
	if ( mSilinecekSertifika != iKS.getSilinecekSertifika() )
		DELETE_MEMORY(mSilinecekSertifika)

	mType				= iKS.getType();
	if ( mType == KS_eklenecekSertifika )
	{
		mEklenecekSertifika = new DepoASNEklenecekKokSertifika(*(iKS.getEklenecekSertifika())) ;
		mSilinecekSertifika	= NULL;
	}else
	{
		mEklenecekSertifika = NULL;
		mSilinecekSertifika = new DepoASNSilinecekKokSertifika(*(iKS.getSilinecekSertifika())) ;
	}
	return *this;
}

bool esya::operator==(const DepoASNKokSertifika & iRHS, const DepoASNKokSertifika& iLHS)
{
	if ( iRHS.getType() != iLHS.getType() ) return false;

	if ( iRHS.getType() == DepoASNKokSertifika::KS_eklenecekSertifika )
	{
		return( iRHS.getEklenecekSertifika() == iLHS.getEklenecekSertifika() );
	}else
	{
		return( iRHS.getSilinecekSertifika() == iLHS.getSilinecekSertifika() );	
	}
}

bool esya::operator!=(const DepoASNKokSertifika& iRHS, const DepoASNKokSertifika& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int DepoASNKokSertifika::copyFromASNObject(const ASN1T_SD_DepoASNKokSertifika& iKS )
{
	DELETE_MEMORY(mEklenecekSertifika)
	DELETE_MEMORY(mSilinecekSertifika)

	mType = (KS_Type)iKS.t;

	switch (iKS.t)
	{
	case KS_eklenecekSertifika: 
		{
			mEklenecekSertifika = new DepoASNEklenecekKokSertifika(*(iKS.u.eklenecekSertifika));
			break;
		}
	case KS_silinecekSertifika: 
		{
			mSilinecekSertifika = new DepoASNSilinecekKokSertifika(*(iKS.u.silinecekSertifika));
			break;
		}
	default: 
		{
			throw EException("Tanýnmayan KökSertifika tipi");
			break;
		}
	}
	return SUCCESS;
}

int DepoASNKokSertifika::copyToASNObject(ASN1T_SD_DepoASNKokSertifika & oKS) const
{
	oKS.t = mType;

	switch (oKS.t)
	{
	case KS_eklenecekSertifika: 
		{
			oKS.u.eklenecekSertifika =  mEklenecekSertifika->getASNCopy() ;
			break;
		}
	case KS_silinecekSertifika: 
		{
			oKS.u.silinecekSertifika = mSilinecekSertifika->getASNCopy();
			break;
		}
	default: 
		{
			throw EException("Tanýnmayan KökSertifika tipi");
			break;
		}
	}
	return SUCCESS;
}

void DepoASNKokSertifika::freeASNObject(ASN1T_SD_DepoASNKokSertifika& oKS)const
{
	switch (oKS.t)
	{
	case KS_eklenecekSertifika: 
		{
			DepoASNEklenecekKokSertifika().freeASNObjectPtr(oKS.u.eklenecekSertifika);
			break;
		}
	case KS_silinecekSertifika: 
		{
			DepoASNSilinecekKokSertifika().freeASNObjectPtr(oKS.u.silinecekSertifika);
			break;
		}
	default: 
		{
			break;
		}
	}	
}

int  DepoASNKokSertifika::copyDepoASNKokSertifikaList(const QList<DepoASNKokSertifika> iList ,ASN1TPDUSeqOfList & oKSs)
{
	return copyASNObjects<DepoASNKokSertifika>(iList,oKSs);
}

int	DepoASNKokSertifika::copyDepoASNKokSertifikaList(const ASN1TPDUSeqOfList & iKSs, QList<DepoASNKokSertifika>& oList)
{
	return copyASNObjects<DepoASNKokSertifika>(iKSs,oList);
}


DepoASNKokSertifika::KS_Type	DepoASNKokSertifika::getType() const
{
	return mType;
}

const	DepoASNEklenecekKokSertifika* DepoASNKokSertifika::getEklenecekSertifika() const 
{
	return mEklenecekSertifika;
}

DepoASNEklenecekKokSertifika* DepoASNKokSertifika::getEklenecekSertifika() 
{
	return mEklenecekSertifika;
}

const	DepoASNSilinecekKokSertifika* DepoASNKokSertifika::getSilinecekSertifika() const 
{
	return mSilinecekSertifika;
}

DepoASNSilinecekKokSertifika* DepoASNKokSertifika::getSilinecekSertifika() 
{
	return mSilinecekSertifika;
}


void DepoASNKokSertifika::setEklenecekSertifika(DepoASNEklenecekKokSertifika *iEklenecekSertifika)
{
	DELETE_MEMORY(mSilinecekSertifika)
	if (	mEklenecekSertifika != iEklenecekSertifika )
		DELETE_MEMORY(mEklenecekSertifika)
	
	mEklenecekSertifika = iEklenecekSertifika;
}

void DepoASNKokSertifika::setSilinecekSertifika(DepoASNSilinecekKokSertifika *iSilinecekSertifika)
{
	DELETE_MEMORY(mEklenecekSertifika)
		if (	mSilinecekSertifika != iSilinecekSertifika )
			DELETE_MEMORY(mEklenecekSertifika)

	mSilinecekSertifika = iSilinecekSertifika;
}

DepoASNKokSertifika::~DepoASNKokSertifika(void)
{
	DELETE_MEMORY(mEklenecekSertifika)
	DELETE_MEMORY(mSilinecekSertifika)
}

#include "Eklenti.h"

using namespace esya;

Eklenti::Eklenti(void)
:	pEklenti(NULL),
	mKritik(false)
{
}

Eklenti::Eklenti(const bool & iKritik, const AY_Eklenti * ipEklenti)
:	pEklenti(NULL),
	mKritik(iKritik)
{
	setEklenti(ipEklenti);
}

Eklenti::Eklenti(const Extension & iExtension)
:	pEklenti(NULL)
{
	fromExtension(iExtension);
}

Eklenti::Eklenti(const Eklenti & iEklenti)
:	pEklenti(NULL),
	mKritik(iEklenti.isKritik())
{
	setEklenti(iEklenti.getEklenti());
}


Eklenti& Eklenti::operator=(const Eklenti & iEklenti)
{
	mKritik = iEklenti.isKritik();
	setEklenti(iEklenti.getEklenti());
	return *this;
}

bool esya::operator==(const Eklenti & iRHS, const Eklenti & iLHS )
{
	return (	( iRHS.isKritik() == iLHS.isKritik() ) &&
				( iRHS.getEklenti() == iLHS.getEklenti() )	);
}

bool	Eklenti::isKritik()const 
{
	return mKritik;
}
void	Eklenti::setKritik(const bool& iKritik)
{
	mKritik = iKritik;
}


void Eklenti::fromExtension(const Extension & iExtension)
{
	AY_Eklenti * pNewEklenti = EklentiFabrikasi::eklentiUret(iExtension);
	setEklenti(pNewEklenti);
	mKritik = iExtension.isCritical();
	DELETE_MEMORY(pNewEklenti);
}

const AY_Eklenti* Eklenti::getEklenti()const
{
	return pEklenti;
}

AY_Eklenti* Eklenti::getEklenti()
{
	return pEklenti;
}
void Eklenti::setEklenti(const AY_Eklenti* ipEklenti)
{
	if (pEklenti == ipEklenti ) return ;
	DELETE_MEMORY(pEklenti);

	pEklenti = ipEklenti->kendiniKopyala();
}


Eklenti::~Eklenti(void)
{
	DELETE_MEMORY(pEklenti)
}

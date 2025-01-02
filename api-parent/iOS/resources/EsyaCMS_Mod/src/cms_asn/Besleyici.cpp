#include "Besleyici.h"

using namespace esya;

Besleyici::Besleyici(TAlg_Paralel_Adaptor * ipBeslenecekler)
{
	pBeslenecekler = ipBeslenecekler;
}

void Besleyici::blokIsle(const QByteArray& iBlok)
{
	pBeslenecekler->besle(iBlok);
}

Besleyici::~Besleyici(void)
{
}

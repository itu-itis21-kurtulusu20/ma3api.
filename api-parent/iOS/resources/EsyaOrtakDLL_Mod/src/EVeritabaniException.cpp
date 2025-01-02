#include "EVeritabaniException.h"

using namespace esya;


EVeritabaniException::EVeritabaniException(EVeritabaniException::Sebep neden, const QString &iErrorDetail, const QString &iFileName, int iLineNumber)
:	EException(iErrorDetail, iFileName, iLineNumber),
	mSebep(neden)
{
}


EVeritabaniException::Sebep EVeritabaniException::sebep()
{
	return mSebep;
}

EVeritabaniException::~EVeritabaniException(void)
{
}

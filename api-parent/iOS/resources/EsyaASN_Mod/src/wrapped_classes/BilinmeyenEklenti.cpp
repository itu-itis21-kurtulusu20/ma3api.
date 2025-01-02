#include "BilinmeyenEklenti.h"
#include "EASNToStringUtils.h"

using namespace esya;

BilinmeyenEklenti::BilinmeyenEklenti(void)
{
}

BilinmeyenEklenti::BilinmeyenEklenti(const ASN1TObjId & iEklentiTipi, const QByteArray &iEklentiDegeri )
:	mEklentiTipi(iEklentiTipi),
	mEklentiDegeri(iEklentiDegeri)
{
}

BilinmeyenEklenti::BilinmeyenEklenti(const BilinmeyenEklenti & iEklenti)
:	mEklentiTipi(iEklenti.mEklentiTipi),
	mEklentiDegeri(iEklenti.mEklentiDegeri)
{
}

BilinmeyenEklenti & BilinmeyenEklenti::operator=(const BilinmeyenEklenti& iEklenti)
{
	mEklentiTipi	= iEklenti.mEklentiTipi;
	mEklentiDegeri	= iEklenti.mEklentiDegeri;
	return * this;
}

bool  esya::operator==(const BilinmeyenEklenti& iRHS,const BilinmeyenEklenti& iLHS )
{
	return  (	( iRHS.getEklentiTipi()		== iLHS.getEklentiTipi()	) &&
				( iRHS.getEklentiDegeri()	== iLHS.getEklentiDegeri()	)	 );

}

const ASN1TObjId& BilinmeyenEklenti::getEklentiTipi() const
{
	return mEklentiTipi;
}

const QByteArray& BilinmeyenEklenti::getEklentiDegeri() const
{
	return mEklentiDegeri;
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/


QString BilinmeyenEklenti::eklentiAdiAl()			const 
{
	return EASNToStringUtils::type2String(mEklentiTipi);
}

QString BilinmeyenEklenti::eklentiKisaDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mEklentiDegeri);
}

QString BilinmeyenEklenti::eklentiUzunDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mEklentiDegeri);
}

AY_Eklenti* BilinmeyenEklenti::kendiniKopyala() const 
{
	return new BilinmeyenEklenti(*this);
}


BilinmeyenEklenti::~BilinmeyenEklenti(void)
{
}

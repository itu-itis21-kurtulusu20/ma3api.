#include "EAyarGDMAcikDosya.h"
#include "EsyaOrtak_Ortak.h"

NAMESPACE_BEGIN(esya)

/**
 * Acik dosyanýn bilgileri ile ilklendirilir.
 * \param iDosyaID 
 * Dosyanýn veritabanýnda tutulan ID si
 * \param irDosyaYolu 
 * Dosyanýn diskteki yolu
 * \param iTarih 
 * Dosyanýn eklenme tarihi
 * \param iDurum 
 * Dosyanýn durumu 
 */
EAyarGDMAcikDosya::EAyarGDMAcikDosya(int iDosyaID,
									 const QString& irDosyaYolu,
									 const QDateTime& iTarih,
									 DosyaDurumu iDurum)
									 :mDosyaID(iDosyaID),
									 mDosyaYolu(irDosyaYolu),
									 mTarih(iTarih),
									 mDurum(iDurum)
									 
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

EAyarGDMAcikDosya::~EAyarGDMAcikDosya()
{	
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

int EAyarGDMAcikDosya::getDosyaID() const
{
	return mDosyaID;
}

const QString& EAyarGDMAcikDosya::getDosyaYolu() const
{
	return mDosyaYolu;
}

const QDateTime& EAyarGDMAcikDosya::getTarih() const
{
	return mTarih;
}

EAyarGDMAcikDosya::DosyaDurumu EAyarGDMAcikDosya::getDurum() const
{
	return mDurum;
}

bool operator==(const EAyarGDMAcikDosya& iRHS, const EAyarGDMAcikDosya& iLHS)
{
	return (iRHS.getDosyaYolu()	== iLHS.getDosyaYolu() );
}

QDataStream & operator>>( QDataStream & in, EAyarGDMAcikDosya & oAD )
{
	in>>oAD.mDosyaID>>oAD.mDosyaYolu>>oAD.mTarih;
	return in;
}

QDataStream & operator<<( QDataStream & out, const EAyarGDMAcikDosya & iAD )
{
	out<<iAD.mDosyaID<<iAD.mDosyaYolu<<iAD.mTarih;
	return out;
}


NAMESPACE_END


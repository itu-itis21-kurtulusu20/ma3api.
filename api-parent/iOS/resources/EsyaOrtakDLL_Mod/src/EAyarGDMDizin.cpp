#include "EAyarGDMDizin.h"
#include "EsyaOrtak_Ortak.h"
#include "EAyarGDMDizinBilgiManager.h"


NAMESPACE_BEGIN(esya)
/**
 * GDM Dizini bilgileri ile ilklendilir.
 * \param iDizinID 
 * Dizinin veritabanýndaki ID si
 * \param iDizinTipi 
 * Dizinin tipi - Imzali,þifreli,imzali&þifreli
 * \param &irDizinYolu 
 * Dizinin diskteki yeri
 * \param iOzneSecimTipi
 * Neye göre üzerinde iþlem yapýlacaðý. Listeye,haklara ya da þeçime göre
 * \param iDegistirilebilir 
 * Deðiþtirilebilir bi dizin mi   
 */
EAyarGDMDizinBilgisi::EAyarGDMDizinBilgisi(int iDizinID, 
								   AyarDizinTipi iDizinTipi, 
								   const QString &irDizinYolu, 
								   EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi,
								   EAyarGDMDizinBilgisi::AyarDosyaSecimTipi iDosyaSecimTipi,
								   EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi iUzerineYazmaTipi,
								   bool iDegistirilebilir)
								   :mDizinID(iDizinID),
								   mDizinTipi(iDizinTipi),
								   mDizinYolu(irDizinYolu),
								   mOzneSecimTipi(iOzneSecimTipi),
								   mDosyaSecimTipi(iDosyaSecimTipi),
								   mUzerineYazmaTipi(iUzerineYazmaTipi),
								   mDegistirilebilir(iDegistirilebilir)
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

EAyarGDMDizinBilgisi::EAyarGDMDizinBilgisi(const EAyarGDMDizinBilgisi & iDB)
:	mDizinID(iDB.getDizinID()),
	mDizinTipi(iDB.getDizinTipi()),
	mDizinYolu(iDB.getDizinYolu()),
	mOzneSecimTipi(iDB.getOzneSecimTipi()),
	mUzerineYazmaTipi(iDB.getUzerineYazmaTipi()),
	mDosyaSecimTipi(iDB.getDosyaSecimTipi()),
	mDegistirilebilir(iDB.getDegistirilebilir())
{

}

EAyarGDMDizinBilgisi & EAyarGDMDizinBilgisi::operator=(const EAyarGDMDizinBilgisi& iDB)
{
	mDizinID		= iDB.getDizinID();
	mDizinTipi		= iDB.getDizinTipi();
	mDizinYolu		= iDB.getDizinYolu();
	mOzneSecimTipi	= iDB.getOzneSecimTipi();
	mDosyaSecimTipi = iDB.getDosyaSecimTipi();
	mUzerineYazmaTipi = iDB.getUzerineYazmaTipi();
	mDegistirilebilir = iDB.getDegistirilebilir();
	
	return *this;
}


/**
 *  Default olarak bir dizin nesnesi oluþturmak için . Ýmzalý,listeden seçim yapýlan ve deðiþtirilemez olan 
 */
EAyarGDMDizinBilgisi::EAyarGDMDizinBilgisi()
										   :mDizinTipi(EAyarGDMDizinBilgisi::DTImzali),
											mOzneSecimTipi(EAyarGDMDizinBilgisi::OSTListeyeGore),
											mDosyaSecimTipi(EAyarGDMDizinBilgisi::DST_HEPSI),
											mDegistirilebilir(false),
											mDizinID(-1)
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}


int EAyarGDMDizinBilgisi::getDizinID() const 
{
	return mDizinID; 
}

EAyarGDMDizinBilgisi::AyarDizinTipi EAyarGDMDizinBilgisi::getDizinTipi() const 
{
	return mDizinTipi; 
}

const QString &EAyarGDMDizinBilgisi::getDizinYolu() const 
{
	return mDizinYolu; 
} 

EAyarGDMDizinBilgisi::AyarOzneSecimTipi EAyarGDMDizinBilgisi::getOzneSecimTipi() const
{
	return mOzneSecimTipi; 
}

EAyarGDMDizinBilgisi::AyarDosyaSecimTipi EAyarGDMDizinBilgisi::getDosyaSecimTipi() const
{
	return mDosyaSecimTipi; 
}

bool EAyarGDMDizinBilgisi::getDegistirilebilir() const 
{
	return mDegistirilebilir; 
};

EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi EAyarGDMDizinBilgisi::getUzerineYazmaTipi() const
{
	return mUzerineYazmaTipi;
}



void EAyarGDMDizinBilgisi::setDizinYolu(const QString &iDizinYolu)
{
	mDizinYolu = iDizinYolu;
}

void EAyarGDMDizinBilgisi::setDizinTipi(AyarDizinTipi iTip )
{
	mDizinTipi = iTip;
}

void EAyarGDMDizinBilgisi::setOzneSecimTipi(EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi) 
{
	mOzneSecimTipi = iOzneSecimTipi;
}

void EAyarGDMDizinBilgisi::setDosyaSecimTipi(EAyarGDMDizinBilgisi::AyarDosyaSecimTipi iDosyaSecimTipi) 
{
	mDosyaSecimTipi = iDosyaSecimTipi;
}

void EAyarGDMDizinBilgisi::setUzerineYazmaTipi(EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi iUzerineYazmaTipi) 
{
	mUzerineYazmaTipi = iUzerineYazmaTipi;
}

bool EAyarGDMDizinBilgisi::dizinVarmi(const QString& irDizinYolu)
{
	return EAyarGDMDizinBilgiManager::getInstance()->dizinVarmi(irDizinYolu);
}
bool EAyarGDMDizinBilgisi::dizinVarmi(const qlonglong& irDizinID)
{
	return EAyarGDMDizinBilgiManager::getInstance()->dizinVarmi(irDizinID);
}
QList<EAyarGDMDizinBilgisi> EAyarGDMDizinBilgisi::gdmOnTanimliDizinleriAl()
{
	return EAyarGDMDizinBilgiManager::getInstance()->gdmOnTanimliDizinleriAl();
}
QList<EAyarGDMDizinBilgisi> EAyarGDMDizinBilgisi::gdmDizinleriAl()
{
	return EAyarGDMDizinBilgiManager::getInstance()->gdmDizinleriAl();
}

EAyarGDMDizinBilgisi EAyarGDMDizinBilgisi::gdmDizinAl(const QString& irDizinYolu)
{
	return EAyarGDMDizinBilgiManager::getInstance()->gdmDizinAl(irDizinYolu);
}

EAyarGDMDizinBilgisi EAyarGDMDizinBilgisi::gdmDizinAl(int iDizinID)
{
	return EAyarGDMDizinBilgiManager::getInstance()->gdmDizinAl(iDizinID);
}

void EAyarGDMDizinBilgisi::dizinSil(const EAyarGDMDizinBilgisi & iDB)
{
	EAyarGDMDizinBilgiManager::getInstance()->dizinSil(iDB);
}

void EAyarGDMDizinBilgisi::dizinEkle(EAyarGDMDizinBilgisi::AyarDizinTipi iDizinTipi,const QString &irDizinYolu,EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi, AyarDosyaSecimTipi iDST,EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi iUYT )
{
	EAyarGDMDizinBilgiManager::getInstance()->dizinEkle(iDizinTipi,irDizinYolu,iOzneSecimTipi,iDST,iUYT);
}

void EAyarGDMDizinBilgisi::dizinGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi)
{
	EAyarGDMDizinBilgiManager::getInstance()->dizinGuncelle(irDizinBilgisi);
}
void EAyarGDMDizinBilgisi::dizinOzneleriniSil(const EAyarGDMDizinBilgisi& irDizin)
{
	EAyarGDMDizinBilgiManager::getInstance()->dizinOzneleriniSil(irDizin);
}

void EAyarGDMDizinBilgisi::dizinOzneleriniGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi, const QList<EAyarGDMOzneBilgisi> & irOzneListesi )
{	
	if (!EAyarGDMDizinBilgisi::dizinVarmi(irDizinBilgisi.getDizinID()))
		throwAYAREXCEPTION(EAyarException::DizinBulunamadi,QString("%1 nolu dizin bulunamadi").arg(irDizinBilgisi.getDizinID()));

	EAyarGDMDizinBilgisi::dizinOzneleriniSil(irDizinBilgisi);
	EAyarGDMOzneBilgisi::ozneEkleDizine(irDizinBilgisi,irOzneListesi);

}

NAMESPACE_END

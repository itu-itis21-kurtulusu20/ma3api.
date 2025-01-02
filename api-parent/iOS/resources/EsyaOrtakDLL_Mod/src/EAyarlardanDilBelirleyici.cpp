#include "EAyarlardanDilBelirleyici.h"
#include "EGenelAyarManager.h"
#include "EAyarTanimlari.h"
#include "EAyarAlici.h"
NAMESPACE_BEGIN(esya)

EAyarlardanDilBelirleyici::EAyarlardanDilBelirleyici(QApplication * ipApp)
:EDilBelirleyici(ipApp)
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN	
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}

EAyarlardanDilBelirleyici::~EAyarlardanDilBelirleyici(void)
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}

/**
 * Ayarlardan sistemin çalýþacaðý dil adýný alýr.
 */
void EAyarlardanDilBelirleyici::calismaDiliBelirle()
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN
	_ayarlardanDilBelirle();
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}

/**
 * Ayarlardan dil adýný okur. IslemOzellikleri sýnýfýnýn içindeki ProgramDili deðerinden alýr.
 */
void EAyarlardanDilBelirleyici::_ayarlardanDilOku()
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN;	
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_ISLEMOZELLIKLERI,AYAR_ISLEMOZELLIKLERI_PROGRAM_DILI);
		EAyar dilAyar = ayarAlici.ayarBul(false);
		mDilSecenek = dilAyar.getStringDeger();
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR("Ayarlardan Güvenli dizin yolu okunmaya çalýþýlýrken hata oluþtu"+exc.printStackTrace());		
	}	
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END	
}

void EAyarlardanDilBelirleyici::_ayarlardanDilBelirle()
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN	
	_ayarlardanDilOku();
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}
NAMESPACE_END
#include "ELokaldenDilBelirleyici.h"
#include <QLocale>
NAMESPACE_BEGIN(esya)

ELokaldenDilBelirleyici::ELokaldenDilBelirleyici(QApplication * ipApp)
:EDilBelirleyici(ipApp)
{
	ESYA_ORTAK_FUNC_BEGIN
	ESYA_ORTAK_FUNC_END
}

ELokaldenDilBelirleyici::~ELokaldenDilBelirleyici(void)
{
	ESYA_ORTAK_FUNC_BEGIN
	ESYA_ORTAK_FUNC_END
}

/**
 * Localdeki tanýmlý system dil adýný alýr.
 */
void ELokaldenDilBelirleyici::_lokaldenDilBelirle()
{
	ESYA_ORTAK_FUNC_BEGIN	
	mDilSecenek = QLocale::system().name(); 
	ESYA_ORTAK_FUNC_END
}

/**
 * Yerel ayarlarýn dilini alýp Kermen 'i çalýþma dilini belirler.
 */
void ELokaldenDilBelirleyici::calismaDiliBelirle()
{
	ESYA_ORTAK_FUNC_BEGIN
	_lokaldenDilBelirle();
	ESYA_ORTAK_FUNC_END
}
NAMESPACE_END

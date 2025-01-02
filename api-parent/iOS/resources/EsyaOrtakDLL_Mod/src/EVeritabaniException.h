
#ifndef __EVERITABANIEXCEPTION_H__
#define __EVERITABANIEXCEPTION_H__

#include "EException.h"
#include <QString>

NAMESPACE_BEGIN(esya)


#define EVTMODUL "EVeritabani"

#define VTDEBUGLOGYAZ(mesaj) \
{ \
	qDebug("%s: %s",EVTMODUL,qPrintable(mesaj));\
}
#define VTERRORLOGYAZ(mesaj) \
{ \
	qCritical("%s: %s",EVTMODUL,qPrintable(mesaj));\
}


#define YENIVTEXCEPTION(neden,mesaj) EVeritabaniException(neden,mesaj,__FILE__,__LINE__)

#define throwVTEXCEPTION(neden,mesaj) \
{\
	VTERRORLOGYAZ(mesaj);\
	throw YENIVTEXCEPTION(neden,mesaj);\
}



class Q_DECL_EXPORT EVeritabaniException:
	public EException
{
public:
	enum Sebep {	VTTanimsiz, 
					VTAcilamadi,
					VTOlusturulamadi,
					QueryCalistirilamadi,
					GecersizQuery,
					OzneBulunamadi,
					OzneEklenemedi,
					OzneSilinemedi,
					OzneZatenVar
	}; 

	EVeritabaniException(Sebep neden, const QString &iErrorDetail, const QString &iFileName = "", int iLineNumber = 0);
	~EVeritabaniException();

	Sebep sebep();

private:
	Sebep mSebep;
};

NAMESPACE_END
#endif


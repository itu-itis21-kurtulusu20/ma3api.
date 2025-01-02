#include "EDilBelirleyici.h"
#include "FileUtil.h"
#include <QTranslator>

NAMESPACE_BEGIN(esya)
#define  KERMEN_DEFAULT_CALISMA_DIL "tr_TR"
#define  KERMEN_DIL_DOSYASI_PREFIX "KermenDIL_"
#define KERMEN_LINUX_DEFAULT_SYSTEM_DIR "/usr/bin"

QList<QTranslator * > EDilBelirleyici::sTranslators=QList<QTranslator *>();

EDilBelirleyici::EDilBelirleyici(QApplication * ipApp)
:mpApp(ipApp)
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN
	mDilSecenek = KERMEN_DEFAULT_CALISMA_DIL;
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}

EDilBelirleyici::~EDilBelirleyici(void)
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_BEGIN
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}

void EDilBelirleyici::_removeTranslators()
{
	Q_FOREACH(QTranslator * pTranslator,sTranslators)
	{
		if(mpApp && pTranslator)
		{
			mpApp->removeTranslator(pTranslator);
		}		
	}
}

void EDilBelirleyici::calismaDiliUygula()
{
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
	calismaDiliBelirle();
	QString lDilDosyasiTamAdi = QString("%1%2").arg(KERMEN_DIL_DOSYASI_PREFIX).arg(mDilSecenek);
	QString lSystemDir;
#ifdef WIN32
	 lSystemDir = FileUtil::getWinSystem32Path();
#else
	lSystemDir = KERMEN_LINUX_DEFAULT_SYSTEM_DIR;
#endif	
	_removeTranslators();
	QTranslator *pTranslator=new QTranslator();
	if(pTranslator->load(lDilDosyasiTamAdi,lSystemDir))
	{
		mpApp->installTranslator(pTranslator); 
		sTranslators.append(pTranslator);
	}
	else
	{
		DELETE_MEMORY(pTranslator)
	}

	QTranslator *pCurrentPathTranslator=new QTranslator();
	if(pCurrentPathTranslator->load(lDilDosyasiTamAdi,QDir::currentPath()))
	{
		mpApp->installTranslator(pCurrentPathTranslator); 
		sTranslators.append(pCurrentPathTranslator);
	}
	else
	{
		DELETE_MEMORY(pCurrentPathTranslator);
	}
	E_DIL_BELIRLEYICI_ESYA_ORTAK_FUNC_END
}
NAMESPACE_END
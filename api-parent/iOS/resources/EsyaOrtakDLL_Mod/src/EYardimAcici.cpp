#include "EYardimAcici.h"
#include "FileUtil.h"

using namespace  esya;
#define  KERMEN_YARDIM_DOSYA_ADI "KermenYardim.chm"

EYardimAcici::EYardimAcici(const QString & iYardimDosyaYolu)
:mYardimDosyaYolu(iYardimDosyaYolu),mpProcess(NULL)
{
}

EYardimAcici::~EYardimAcici(void)
{
}

void EYardimAcici::yardimGoruntule(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi)
{	
	return _yardimGoster(iYardimTipi);
}

void EYardimAcici::yardimGoster(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi)
{
	QString genelAyarPath = FileUtil::genelAyarPath();
	QString dosyaYolu =genelAyarPath+"/"+KERMEN_YARDIM_DOSYA_ADI ;
	EYardimAcici(dosyaYolu).yardimGoruntule(iYardimTipi);
}

void EYardimAcici::_yardimGoster(EYardimAcici::YARDIM_MODUL_TIPI iYardimTipi)
{	
	_dosyaGoruntule(mYardimDosyaYolu);	
}

void EYardimAcici::_dosyaGoruntule(const QString & iDosyaYolu,const QStringList & iParametreList)
{
	QStringList lArguments;
	lArguments<<"/C"<<iDosyaYolu;
	lArguments<<iParametreList;
	DELETE_MEMORY(mpProcess);
	mpProcess = new QProcess();	
	mpProcess->start("cmd",lArguments);			
}
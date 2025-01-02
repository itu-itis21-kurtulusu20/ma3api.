#ifndef __ESYA_ORTAK__ORTAK_H
#define __ESYA_ORTAK__ORTAK_H
#include "ELogger.h"
#include "esyaOrtak.h"

#include <QSqlDatabase>

#define ESYA_AYAR_FUNC_BEGIN(x) 	AYAR_DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" -GIRIS"));
#define ESYA_AYAR_FUNC_DEBUG(x,y) 	AYAR_DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" -GIRIS"));
#define ESYA_AYAR_FUNC_END(x)		AYAR_DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" -TAMAMLANDI."));
#define ESYA_AYAR_FUNC_ERROR(x,y)	AYAR_ERRORLOGYAZ(x,QString("%1 : %2 - %3").arg(__EFUNC__).arg(" -HATA OLUSTU.").arg(y));


#define ESYA_ORTAK_FUNC_BEGIN ESYA_AYAR_FUNC_BEGIN(ESYAORTAK_MOD)
#define ESYA_ORTAK_FUNC_DEBUG(x) AYAR_DEBUGLOGYAZ(ESYAORTAK_MOD,QString("%1 : %2").arg(__EFUNC__).arg(x)) 
#define ESYA_ORTAK_FUNC_END	  ESYA_AYAR_FUNC_END(ESYAORTAK_MOD)
#define ESYA_ORTAK_FUNC_ERROR(x) ESYA_AYAR_FUNC_ERROR(ESYAORTAK_MOD,x)

#define KERMEN_TUM_DBLERI_KAPAT QStringList conNames = QSqlDatabase::connectionNames();\
								  Q_FOREACH(QString conName,conNames)\
								  	  QSqlDatabase::database(conName,false).close();
#endif
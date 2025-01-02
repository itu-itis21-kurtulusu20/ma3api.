#ifndef __E_KABUK_APPLICATION_MANAGER_H___
#define __E_KABUK_APPLICATION_MANAGER_H___
#include "esyaOrtak.h"
#include <QString>
#include <QApplication>

#ifdef WIN32
#define CIKIS_YAP return 0;
#else
#define CIKIS_YAP int * ptrCIKISYAP=NULL;\
                *ptrCIKISYAP=3;
#endif

NAMESPACE_BEGIN(esya)
/**
 *
 * KabukEklentisi icerisindeki QApplication ile ilgili i�lemleri yapar.
 * Ayr�ca hata mesaj� bunun �zerinden g�sterilir.
 * Nedeni EsyaOrtak_GUI ye ba��ml� olmas�n� engellemek
 *
 * \version 1.0
 * first version
 *
 * \date 03-20-2009
 *
 * \author ramazang
 *
 *
 * \todo
 *
 * \bug
 *
 */
class Q_DECL_EXPORT EApplicationManager
{
public:
	static QApplication * sGenelAppManager;
	EApplicationManager(void);
	~EApplicationManager(void);
	static void qtLibraryListesineSystem32At();
	static QApplication * qtApplicationBaslat();
	static void qtAppKontrolEtYoksaBaslat();
	static void hataMesajGoster(const QString & iHataMesaji);
	static void calismaDiliBelirle();
	static void textCodecBelirle();
	static void styleBelirle();

	static void qtApplicationKontroluYap();
};
NAMESPACE_END
#endif

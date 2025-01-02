#ifndef _E_AYAR_GDM_DIZIN_BILGI_MANAGER_H_
#define _E_AYAR_GDM_DIZIN_BILGI_MANAGER_H_

#include <QMap>
#include "EYerelAyarManager.h"
#include "EGenelAyarManager.h"
#include "EAyarGDMDizin.h"
#include "EAyarGDMOzneBilgisi.h"

#define EAYARLARMODUL "EAyarlar"
#define AYARDEBUGLOGYAZ(mesaj) \
{ \
	qDebug("%s: %s",EAYARLARMODUL,qPrintable(mesaj));\
}
#define AYARERRORLOGYAZ(mesaj) \
{ \
	qCritical("%s: %s",EAYARLARMODUL,qPrintable(mesaj));\
}

#define throwAYAREXCEPTION(neden,mesaj) \
{\
	AYARERRORLOGYAZ(mesaj);\
	throw YENIAYAREXCEPTION(neden,mesaj);\
}

NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EAyarGDMDizinBilgiManager
{
	static QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMDizinBilgiManager*> mpInstanceMap;
	EYerelAyarManager * mpYerelAyarManager;
	EGenelAyarManager * mpGenelAyarManager;
public:
	EAyarGDMDizinBilgiManager(EYerelAyarManager * ipYerelAyarManager,EGenelAyarManager * ipGenelAyarManager);
	static EAyarGDMDizinBilgiManager * getInstance(EYerelAyarManager * ipYerelAyarManager = NULL,EGenelAyarManager * ipGenelAyarManager=NULL);
	~EAyarGDMDizinBilgiManager(void);

	EAyarGDMDizinBilgisi getObjectFromQuery(QSqlQuery* pQuery)const;
	QString getSelectQuery(const QString & iTableName, const QString & iFilter = "") const;

	bool dizinVarmi(const QString& irDizinYolu);
	bool dizinVarmi(const qlonglong& irDizinID);	
	QList<EAyarGDMDizinBilgisi> gdmOnTanimliDizinleriAl();
	QList<EAyarGDMDizinBilgisi> gdmDizinleriAl();
	EAyarGDMDizinBilgisi gdmDizinAl(const QString& irDizinYolu);
	EAyarGDMDizinBilgisi gdmDizinAl(int iDizinID);

	void dizinSil(const EAyarGDMDizinBilgisi & iDB);
	void dizinEkle(EAyarGDMDizinBilgisi::AyarDizinTipi iDizinTipi,const QString &irDizinYolu,EAyarGDMDizinBilgisi::AyarOzneSecimTipi iOzneSecimTipi, EAyarGDMDizinBilgisi::AyarDosyaSecimTipi iDST = EAyarGDMDizinBilgisi::DST_HEPSI,EAyarGDMDizinBilgisi::AyarUzerineYazmaTipi iUYT= EAyarGDMDizinBilgisi::UYT_KULLANICIYASOR );
	void dizinGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi);
	void dizinOzneleriniSil(const EAyarGDMDizinBilgisi& irDizin);
	void dizinOzneleriniGuncelle(const EAyarGDMDizinBilgisi & irDizinBilgisi, const QList<EAyarGDMOzneBilgisi> & irOzneListesi );


};
NAMESPACE_END
#endif


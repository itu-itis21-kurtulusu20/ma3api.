#ifndef _E_AYAR_ALICI_H_
#define  _E_AYAR_ALICI_H_

#include "esyaOrtak.h"
#include "EAyarlar.h"

#include "EAyarListenerThread.h"
#include <QMutex>

NAMESPACE_BEGIN(esya)

class Q_DECL_EXPORT EAyarCacheManager
{
	static EAyarCacheManager * mInstance;
	static QMutex mInstanceMutex;
	static QMutex mReadWriteMutex;

	QMap<QPair<QString,QString>,EAyar> mAyarMap;

	EAyarListenerThread * mpAyarListener;	
private:
	EAyarCacheManager(bool iStartListenerThread=true);

	void _ayariCacheEkle(const EAyar & iAyar);

public:
	~EAyarCacheManager();
	static EAyarCacheManager * getInstance(bool iStartListenerThread=true);
	EAyar getAyarFromCache(const QString & iAyarSinifi,const QString & iAyarAdi);
	void  ayariCacheEkle(const EAyar & iAyar);
	void  updateCache();
};
/**
 * \ingroup EsyaOrtak
 *
 * Ayarlara ula�mada di�er modullerin kulland��� s�n�f . Verilen ayar ad� ve s�n�f �zerinden ayar�n genel ya da yerel ayarlardan �ekilmesi i�lemleriyle u�ra��r. DI�ar�dan kullanan ayar�n genel mi yoksa yerel mi oldu�unu sorgu esnas�nda bilmez.
 E�er ayar genelde yerel e bak�lmadan ayar� d�ner.
 *
 * \version 1.0
 * first version
 *
 * \date 04-03-2009
 *
 * \author ramazang
 *
 *
 * \todo
 *
 * \bug
 *
 */
class Q_DECL_EXPORT EAyarAlici
{
	QString mAyarSinifi;
	QString mAyarAdi;
	static QMutex msAyarBulMutex;

	bool _genelAyarAl(EAyar * oAyar);
	bool _yerelAyarAl(EAyar * oAyar);
public:
	EAyarAlici(const QString & iAyarSinifi,const QString & iAyarAdi);
	~EAyarAlici(void);

	EAyar veritabanindanGetir(bool iCacheGuncelle);
	EAyar ayarBul(bool iAyariCachedeSakla=true);
	static QList<EAyar> tumAyarlariAl();
//Tum genel ayarları Sinif-Ayarlistesi şeklinde döner
	static QMap<QString,QList<EAyar> > tumGenelAyarlariAl();
};
NAMESPACE_END
#endif

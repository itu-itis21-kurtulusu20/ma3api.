#ifndef _E_AYAR_SINIF_H_
#define _E_AYAR_SINIF_H_
#include "EsyaOrtak_Ortak.h"
#include <QString>
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EAyarSinif
{
	QString mSinifAd;
	QString mSinifBaslik;
	QString mSinifAciklama;
public:
	EAyarSinif();
	EAyarSinif(const QString & iSinifAd,const QString & iSinifBaslik,const QString & iSinifAciklama);
	~EAyarSinif(void);
	QString getSinifBaslik() const;
	QString getSinifAciklama() const;
	QString getSinifAd() const{return mSinifAd;};
	static EAyarSinif getAyarSinifFromAd(const QString & iSinifAd);
	static QList<EAyarSinif> tumAyarSinifAl();
};

NAMESPACE_END
#endif

#ifndef __E_AYAR_KULLANICI_MANAGER_H_
#define __E_AYAR_KULLANICI_MANAGER_H_
#include "esyaOrtak.h"
#include <QSqlDatabase>
#include <QList>
#include <QMap>

NAMESPACE_BEGIN(esya)
/**
 * \ingroup EsyaOrtak
 *
 * Yerel ayarlar veritabanýnda tutulan sisteme sertifikalarýný yükleyen kullanýcýlarý tutan yapý. 
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
class Q_DECL_EXPORT EAyarKullanici
{
	int mKulID;
	QString mTitle;
	QString mKulEPosta;	
	bool mIsVarsayilan;

	bool mIsNull;
public:
	EAyarKullanici();
	EAyarKullanici(int iKulID,const QString & iKulEPosta,bool iIsVarsayilan);
	void setKulID(int iKulID);
	void setKulEPosta(const QString & iKulEPosta);
	void setIsVarsayilan(bool iIsVarsayilan);

	int		 getKulId() const;
	QString	 getKulEPosta() const;
	bool	 getIsVarsayilan() const;
	bool	 isNull();
	void	 setIsNull(bool iIsNull);
	~EAyarKullanici();
};

/**
 * \ingroup EsyaOrtak
 *
 * Sistemdeki ayarlardan yapýlan kullanýcý ekleme,cýkarma,varsayýlan yapma ..vb iþlemleri yöneten sýnýf 
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
//<TODO> Buna kullanýcý silme ozelligi eklenecek
class Q_DECL_EXPORT EAyarKullaniciManager
{
	EAyarKullanici mAktifKullanici;
	QSqlDatabase mDb;
	QString mDBPath;
	static QMap<QString,EAyarKullaniciManager *> mInstanceMap;
	
	QSqlQuery * _dbSorguYap(const QSqlDatabase & iDb,const QString & iSorguStr);
	QList<EAyarKullanici> _kullaniciAramaSorgusuYap(const QString & iSorguStr);

	QString _varsayilanKullaniciQuery();
	QString _tumKullanicilarQuery();
	QString _varsayilanOlmayanKullanicilarQuery();

	QString _varsayilanYapQuery(const QString & iKulEPosta);
	QString _digerleriniPasifYapQuery(const QString & iKulEPosta);
	QString _ePostadanKullaniciAramaQuery(const QString & iKulEposta);
	QString _kullaniciEkleQuery(const QString & iKulEPosta);
	
public:
	EAyarKullaniciManager(const QString & iDbPath);
	~EAyarKullaniciManager(void);	
	static EAyarKullaniciManager * getInstance(const QString & iDbPath=NULL);

	EAyarKullanici		  varsayilanKullaniciGetir(bool iMutlakaDBdenOku=true);
	QList<EAyarKullanici> tumKullanicilariGetir();
	QList<EAyarKullanici> varsayilanOlmayanKullanicilariGetir();
	bool				  varsayilanYap(const QString & iKulEPosta);
	bool				  kullaniciEkle(const QString & iKulEPosta);
	bool				  kullaniciSil(const QString & iKulEPosta);
	void cacheTemizle();
	static void removeKullaniciManager(const QString & iDbPath);
};
NAMESPACE_END
#endif
#ifndef __E_YEREL_AYAR_MANAGER_H_
#define __E_YEREL_AYAR_MANAGER_H_

#include "esyaOrtak.h"
#include "EAyarManager.h"
#include "EAyarKullaniciManager.h"

#define KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER "TBL_GRUP_HARIC_OZNELER"

NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EYerelAyarManager:public EAyarManager
{
	EAyarKullanici mDisaridanAyarKullanici;
	static QString msYerelAyarFilePath;
	static QMap<QString,EAyarManager *> mInstanceMap;	
	int _varsayilanKullaniciIDAl();
	QString _getAlanAdiKulId();	

	void _genelAyarlardanVersiyonKopyala();

	void _vtOlustur();

	ParamList siniflaIliskiliAyarlariAlParamList(const QString & iAyarSinif);

	ParamList getParamList(const QString & iAyarSinif,const QString & iAyarAd);
	ParamList  getTumAyarlariAlParamList();
	ParamList getInsertParamList(const EAyar & iAyar);
	QString _getWhereStatement();
	QString _getValuesStatement();
	QString _getInsertSorguAlanlar();
	QString _tumAyarlariAlmaSorguStr();	

	QString _sinifAyarlariAlmaSorguStr();

	QString _tumSinifAdAlmaSorguStr();
	QString _tumSinifAlmaSorguStr();

	bool _tablodanKullaniciBilgiSilHaric(const QString & iTableName,const QString & iKulIDFieldName,const QStringList & iHaricKulIDList);
	bool mDisaridanVerilenKullaniciKullan;
public:	
	void tumKullaniciIcinAyarSil(const QString & iAyarSinif,const QString & iAyarAd);
	static void removeAndDeleteAyarManager(const QString & iDbPath);
	EYerelAyarManager(const QString & iDbPath);
	~EYerelAyarManager(void);
	static EAyarManager * getInstance(const QString & iDbPath="");	
	static void freeYerelAyarManager();	
	bool deleteUserInfoHaric(const QList<EAyarKullanici> & iUsers);
	void setDisaridanAyarKullanici(const EAyarKullanici & iDisaridanAyarKullanici){mDisaridanAyarKullanici = iDisaridanAyarKullanici;};
	void setDisaridanVerilenKullaniciKullan(bool iDisaridanVerilenKullaniciKullan){mDisaridanVerilenKullaniciKullan=iDisaridanVerilenKullaniciKullan;};
	int kullaniciIDAl(){return _varsayilanKullaniciIDAl();};
	QString versiyonAl();
};
NAMESPACE_END
#endif
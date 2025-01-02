#ifndef _E_AYAR_GDM_OZNE_BILGI_MANAGER_H_
#define _E_AYAR_GDM_OZNE_BILGI_MANAGER_H_

#include "EYerelAyarManager.h"
#include "EGenelAyarManager.h"
#include "EAyarGDMOzneBilgisi.h"

NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EAyarGDMOzneBilgiManager
{
	static QMap<QPair<EYerelAyarManager *,EGenelAyarManager * >,EAyarGDMOzneBilgiManager*> mpInstanceMap;
	EYerelAyarManager * mpYerelAyarManager;
	EGenelAyarManager * mpGenelAyarManager;
	int _getKulID();	
	QList<EAyarGDMOzneBilgisi> _sorgudanOzneleriAl(const QString & irQueryText,const ParamList & iParameters);
public:
	EAyarGDMOzneBilgiManager(EYerelAyarManager * ipYerelAyarManager,EGenelAyarManager * ipGenelAyarManager);
	static EAyarGDMOzneBilgiManager * getInstance(EYerelAyarManager * ipYerelAyarManager = NULL,EGenelAyarManager * ipGenelAyarManager=NULL);
	~EAyarGDMOzneBilgiManager(void);
	
	void ozneAdiDegistir(const EAyarGDMOzneBilgisi & iOzneBilgi,const QString& irYeniAd);
	void ozneyiDizindenCikar(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin);
	void ozneyiGruptanCikar(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup);
	bool ozneDizindeVarMi(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin);
	bool ozneGrubunIcindeMi(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup);
	void ozneyiGrubaEkle(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMOzneBilgisi& irGrup);
	void ozneyiDizineEkle(const EAyarGDMOzneBilgisi & iOzneBilgi,const EAyarGDMDizinBilgisi& irDizin,bool iSilinebilir);

	QList<EAyarGDMOzneBilgisi> ozneBul_AdEPosta(const QString &irAd,const QString & iEPosta);
	QList<EAyarGDMOzneBilgisi> ozneBul_Ad(const QString &irAd);
	QList<EAyarGDMOzneBilgisi> ozneBul_Ad_Tip(const QString& irOzneAdi,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi);
	QList<EAyarGDMOzneBilgisi> ozneBul_OzneID(int iOzneID);
	QList<EAyarGDMOzneBilgisi> ozneBul_Tipler(const QList<EAyarGDMOzneBilgisi::AyarOzneTipi> & irOzneTipleri);
	QList<EAyarGDMOzneBilgisi> tumOzneleriAl();

	QList<EAyarGDMOzneBilgisi> grupElemanlariniAl(const EAyarGDMOzneBilgisi& irGrup);	
	QList<EAyarGDMOzneBilgisi> dizinOzneleriniAl(const EAyarGDMDizinBilgisi& irDizin);

	QList<EAyarGDMOzneBilgisi> tumAtalariAl(const EAyarGDMOzneBilgisi& iOzneBilgi);
	QList<EAyarGDMOzneBilgisi> ozneyiIcerenGruplariAl(const EAyarGDMOzneBilgisi& iOzneBilgi);
	QList<EAyarGDMDizinBilgisi> ozneyiIcerenDizinleriAl(const EAyarGDMOzneBilgisi& iOzneBilgi);


	bool ozneHerhangiBirDizindeVarMi(const EAyarGDMOzneBilgisi & iOzne);
	bool ozneHerhangiBirGruptaVarMi(const EAyarGDMOzneBilgisi & iOzne);

	void ozneYaz(const QString& irAd,const QString& irEPosta,const QString& iDN,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi);
	bool ozneDizindenSilinebilirMi(const EAyarGDMOzneBilgisi & iOzne,const EAyarGDMDizinBilgisi& irDizin);

	QList<EAyarGDMOzneBilgisi> grupHaricListesiAl( const EAyarGDMOzneBilgisi & iGrupBilgisi);
	void grupHaricListesineOzneEkle(const EAyarGDMOzneBilgisi & iGrupBilgisi,const EAyarGDMOzneBilgisi& irOzne);
	void tumHaricListelerindenOzneSil(const EAyarGDMOzneBilgisi& irOzne);
	void grupHaricListesindenOzneCikar(const EAyarGDMOzneBilgisi & iGrupBilgisi,const EAyarGDMOzneBilgisi& irOzne);
	bool haricListelerindeOzneVarMi(const EAyarGDMOzneBilgisi& irOzne);

	void ozneSil(const EAyarGDMOzneBilgisi& irOzne);


	QList<EAyarGDMOzneBilgisi> gdmVarsayilanOzneleriAl();
	void gdmVarsayilanOzneleriSil();
	void gdmVarsayilanOzneSil(int iOzneID);
	void gdmVarsayilanOzneEkle(int iOzneID);
	bool gdmVarsayilanOzneVarmi(int iOzneID);

};
NAMESPACE_END
#endif

#ifndef __EAYARGDMOZNEBILGISI_H__
#define __EAYARGDMOZNEBILGISI_H__

#include "esyaOrtak.h"
#include "EAyarException.h"

NAMESPACE_BEGIN(esya)

class EAyarGDMDizinBilgisi;

/**
 * \ingroup EsyaOrtakDLL
 *
 * GDM altýndaki kullanýcý ve gruplarýn tutulmasý için kullanýlan entry sýnýfý 
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 * 
 * \todo 
 *
 * \bug 
 *
 */
class Q_DECL_EXPORT EAyarGDMOzneBilgisi
{
public:	

	enum AyarOzneTipi {
		OTYerelKullanici=0, //ad ve eposta set edilmeli
		OTYerelGrup=1, //ad set edilmeli
		OTLdapGrup=2   //ad, eposta ve DN set edilmeli
	};


	EAyarGDMOzneBilgisi();
	EAyarGDMOzneBilgisi(int iOzneID,
		AyarOzneTipi iOzneTipi,
		const QString &irAd,
		const QString &irEPosta,
		const QString &irDN);

	int getOzneID() const;
	const QString &getAd() const;
	const QString &getEPosta() const;
	const QString &getDN() const;
	AyarOzneTipi getOzneTipi() const;
	bool isNull()const{return mOzneID==-1;};

	void ozneAdiDegistir(const QString& irYeniAd);
	bool operator==(const EAyarGDMOzneBilgisi& irDiger) const;

	void sil();
	void silTamamen();
	void dizindenCikar(const EAyarGDMDizinBilgisi& irDizin)const;
	void gruptanCikar(const EAyarGDMOzneBilgisi& irGrup)const;
	void dizineEkle(const EAyarGDMDizinBilgisi& irDizin,bool iSilinebilir);
	void grubaEkle(const EAyarGDMOzneBilgisi& irGrup);
	bool isOzneOznede(const EAyarGDMOzneBilgisi& irGrup);
	bool isOzneDizinde(const EAyarGDMDizinBilgisi& irDizin);
	bool isDizindenSilinebilir(const EAyarGDMDizinBilgisi&);

	QList<EAyarGDMDizinBilgisi> dizinlerOzneyiBarindiran()const;
	QList<EAyarGDMOzneBilgisi> gruplarOzneyiBarindiran()const;
	
	static bool ozneVarmi(const QString& irOzneAdi,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi);
	static bool ozneVarmi(int iOzneID);

	static QList<EAyarGDMOzneBilgisi> gdmOzneleriAl();
	static QList<EAyarGDMOzneBilgisi> gdmOzneleriAlOzneTipineGore(const QList<EAyarGDMOzneBilgisi::AyarOzneTipi> & irOzneTipleri);

	/**
	* Sadece grup olan (OTYerelGrup ve OTLdapGrup ozne tipindeki) ozneleri doner.
	* \return EAyarGDMOzneBilgisi listesi olarak grup olan ozneler.
	*/
	static QList<EAyarGDMOzneBilgisi> gdmOzneleriAlGrupOlanlari();

	/**
	* OTYerelGrup tipindeki ozneler icinde baska ozneler barindirabilirler. Bu fonksiyon,
	* verilen bir grubun (OTYerelGrup tipindeki bir oznenin) icinde bulunan oznelerin
	* listesini doner.
	* \param EAyarGDMOzneBilgisi& Icindeki ozneler bulunacak grup (OTYerelGrup tipindeki bir ozne)
	* \return Gruptaki ozneleri EAyarGDMOzneBilgisi listesi olarak doner.
	*/
	static QList<EAyarGDMOzneBilgisi> gdmOzneleriAlOzneden(const EAyarGDMOzneBilgisi& irOzne);

	/**
	* Verilen dizin ile bagli olan ozneleri doner. Verilen dizinin ozne secim tipi OSTListeyeGore
	* olmalidir.
	* \param EAyarGDMDizinBilgisi& Hangi dizin icin oznelerin istendigi.
	* \return Verilen dizin icin kullanilacak ozneleri EAyarGDMOzneBilgisi listesi olarak doner.
	*/
	static QList<EAyarGDMOzneBilgisi> gdmOzneleriAlDizinden(const EAyarGDMDizinBilgisi&);


	/**
	* OTYerelGrup tipindeki ozneler icinde baska ozneler barindirabilirler. Bu fonksiyon,
	* verilen bir grubun (OTYerelGrup tipindeki bir oznenin) icinde bulunan kullanici tipindeki
	* öznelerin listesini doner. Bu fonksiyon recursive calisir.
	* \param EAyarGDMOzneBilgisi& Icindeki ozneler bulunacak grup (OTYerelGrup tipindeki bir ozne)
	* \return Gruptaki ozneleri EAyarGDMOzneBilgisi listesi olarak doner.
	*/
	static QList<EAyarGDMOzneBilgisi> gdmKullanicilariAlOzneden(const EAyarGDMOzneBilgisi&);


	static EAyarGDMOzneBilgisi gdmOzneAl(int iOzneNo);
	
	/**
	* Verilen isimdeki ozneyi doner.
	* \param &irAd Istenen oznenin adi.
	* \return Adi verilen ozne. Verilen isimde ozne bulunamazsa Exception atar.
	*/
	static EAyarGDMOzneBilgisi gdmOzneAlAdinaGore(const QString &irAd);
	static EAyarGDMOzneBilgisi gdmOzneAlAdVeEPostayaGore(const QString &irAd,const QString & iEPosta);


	static void ozneYazYerelKullanici(const QString& irAd,const QString& irEPosta);
	static void ozneYazYerelGrup(const QString& irAd);
	static void ozneYazLdapGrup(const QString& irAd,const QString& irEPosta,const QString& irDN);

	static void ozneEkleDizine(const EAyarGDMDizinBilgisi& iDizin,const QList<EAyarGDMOzneBilgisi>& iOzneler,bool iSilinebilir = true);
	
	/************************************************************************/
	/*    Grup Haric Listesi için fonksiyonlar                              */
	/************************************************************************/
	//Burdaki iþlemler metodu çaðýrýlan nesneyi grup olarak görür ve bundan ekleme,cýkarma yapar.
	QList<EAyarGDMOzneBilgisi> haricListesiAl() const;
	void haricListesineEkle(const EAyarGDMOzneBilgisi& irOzne);
	void haricListesindenCikar(const EAyarGDMOzneBilgisi& irOzne);

	void _haricListesindeVarMiKontrolu()const;
	void _haricListesindenSil();


private:
	int mOzneID;
	QString mAd;
	QString mEPosta;
	QString mDN;
	AyarOzneTipi mOzneTipi;

	void _setAd(const QString& irYeniAd);

	static void _ozneOlmamali(const QString& irAd);
};

inline uint qHash(const EAyarGDMOzneBilgisi & ozne)
{
	return qHash(ozne.getAd())^qHash(ozne.getDN())^qHash(ozne.getEPosta())^ozne.getOzneTipi()^ozne.getOzneID();
}

NAMESPACE_END



#endif


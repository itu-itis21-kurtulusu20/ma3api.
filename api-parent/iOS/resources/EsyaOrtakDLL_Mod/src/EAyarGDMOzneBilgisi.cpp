#include "EAyarGDMOzneBilgisi.h"
#include "EsyaOrtak_Ortak.h"
#include "EAyarGDMDizin.h"
#include "EAyarGDMOzneBilgiManager.h"
/**
 * Boþ bir özne bilgisi oluþturur 
 */
NAMESPACE_BEGIN(esya)
EAyarGDMOzneBilgisi::EAyarGDMOzneBilgisi()
										 : mOzneID(-1),
										 mAd("bilinmeyenBirOzne")
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

/**
 * Özne bilgilerinden oluþturulur.
 * \param iOzneID 
 * Öznenin veritabanýndaki ID si
 * \param iOzneTipi 
 * Öznenin tipi , Kullanýcý,LDAP Grup,Yerel Grup
 * \param &irAd 
 * Öznenin adý
 * \param &irEPosta 
 * Öznenin EPosta adresi
 * \param &irDN 
 * LDAP gruplarý için DN 
 */
EAyarGDMOzneBilgisi::EAyarGDMOzneBilgisi(int iOzneID,
				AyarOzneTipi iOzneTipi,
				const QString &irAd,
				const QString &irEPosta,
				const QString &irDN)
				: mOzneID(iOzneID),
				mOzneTipi(iOzneTipi),
				mAd(irAd),
				mEPosta(irEPosta),
				mDN(irDN)
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

int EAyarGDMOzneBilgisi::getOzneID() const 
{
	return mOzneID; 
}

const QString &EAyarGDMOzneBilgisi::getAd() const 
{
	return mAd; 
}

const QString &EAyarGDMOzneBilgisi::getEPosta() const
{
	return mEPosta; 
} 

const QString &EAyarGDMOzneBilgisi::getDN() const 
{
	return mDN; 
}

EAyarGDMOzneBilgisi::AyarOzneTipi EAyarGDMOzneBilgisi::getOzneTipi() const 
{
	return mOzneTipi; 
}

bool EAyarGDMOzneBilgisi::operator==(const EAyarGDMOzneBilgisi& irDiger) const
{
	return (
		(irDiger.mOzneID == mOzneID) &&
		(irDiger.mOzneTipi == mOzneTipi) &&
		(irDiger.mAd == mAd) &&
		(irDiger.mEPosta == mEPosta) &&
		(irDiger.mDN == mDN) 
		);
}

void EAyarGDMOzneBilgisi::_setAd(const QString& irYeniAd)
{
	mAd = irYeniAd;
}


bool EAyarGDMOzneBilgisi::ozneVarmi(const QString& irOzneAdi,EAyarGDMOzneBilgisi::AyarOzneTipi iOzneTipi)
{
	QList<EAyarGDMOzneBilgisi> bulunanOzneler = EAyarGDMOzneBilgiManager::getInstance()->ozneBul_Ad_Tip(irOzneAdi,iOzneTipi);
	return !bulunanOzneler.isEmpty();	
}


bool EAyarGDMOzneBilgisi::ozneVarmi(int iOzneID)
{
	QList<EAyarGDMOzneBilgisi> bulunanOzneler = EAyarGDMOzneBilgiManager::getInstance()->ozneBul_OzneID(iOzneID);
	return !bulunanOzneler.isEmpty();	
}


QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmOzneleriAl()
{
	return EAyarGDMOzneBilgiManager::getInstance()->tumOzneleriAl();
}


QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmOzneleriAlOzneTipineGore(const QList<EAyarGDMOzneBilgisi::AyarOzneTipi> & irOzneTipleri)
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneBul_Tipler(irOzneTipleri);	
}

bool EAyarGDMOzneBilgisi::isDizindenSilinebilir(const EAyarGDMDizinBilgisi& irDizin)
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneDizindenSilinebilirMi(*this,irDizin);	
}


QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmOzneleriAlGrupOlanlari()
{
	QList<EAyarGDMOzneBilgisi::AyarOzneTipi> tipler;
	tipler << EAyarGDMOzneBilgisi::OTYerelGrup << EAyarGDMOzneBilgisi::OTLdapGrup;
	return EAyarGDMOzneBilgisi::gdmOzneleriAlOzneTipineGore(tipler);
}

/**
 * Gruba yeni bir haric eleman eklemek için kullanýlýr.
 * \param irOzne 
 * Eklenecek olan özne
 */
void EAyarGDMOzneBilgisi::haricListesineEkle(const EAyarGDMOzneBilgisi& irOzne)
{
	EAyarGDMOzneBilgiManager::getInstance()->grupHaricListesineOzneEkle(*this,irOzne);
}


/**
 * Haric Listesi tablosundan bu nesnenin tüm kaydýný siler.
 */
void EAyarGDMOzneBilgisi::_haricListesindenSil()
{
	EAyarGDMOzneBilgiManager::getInstance()->tumHaricListelerindenOzneSil(*this);
}
/**
 * Verilen elemaný bu öznenin haric listesinden çýkarýr
 * \param irOzne 
 */
void EAyarGDMOzneBilgisi::haricListesindenCikar(const EAyarGDMOzneBilgisi& irOzne)
{	
	EAyarGDMOzneBilgiManager::getInstance()->grupHaricListesindenOzneCikar(*this,irOzne);	
}

/**
 * Grubun haric listesini almak için kullanýlýr.
 * \return 
 * Özne listesini döner
 */
QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::haricListesiAl()const
{	
	return EAyarGDMOzneBilgiManager::getInstance()->grupHaricListesiAl(*this);	
}

/**
 * Bu nesnenin silinmeden onceki kontrolu icin herhangi bir haric liste kaydý olup olmadýðýný bulur
 */
void EAyarGDMOzneBilgisi::_haricListesindeVarMiKontrolu()const
{
	EAyarGDMOzneBilgiManager::getInstance()->haricListelerindeOzneVarMi(*this);	
}



QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmOzneleriAlOzneden(const EAyarGDMOzneBilgisi& irOzne)
{	
	return EAyarGDMOzneBilgiManager::getInstance()->grupElemanlariniAl(irOzne);	
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmOzneleriAlDizinden(const EAyarGDMDizinBilgisi& irDizin)
{
	return EAyarGDMOzneBilgiManager::getInstance()->dizinOzneleriniAl(irDizin);
}



void _listeyeKullaniciEkle( const QList<EAyarGDMOzneBilgisi> & iKaynakList , QList<EAyarGDMOzneBilgisi> & oHedefList)
{
	foreach(EAyarGDMOzneBilgisi ozne,iKaynakList)
	{
		if (ozne.getOzneTipi()!= EAyarGDMOzneBilgisi::OTYerelKullanici || oHedefList.contains(ozne))
			continue;

		oHedefList.append(ozne);
	}
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gdmKullanicilariAlOzneden(const EAyarGDMOzneBilgisi& irOzne)
{
	QList<EAyarGDMOzneBilgisi> kullaniciList;

	QList<EAyarGDMOzneBilgisi> ozneList =gdmOzneleriAlOzneden(irOzne);

	foreach(EAyarGDMOzneBilgisi ozne,ozneList)
	{
		if ( ozne.getOzneTipi() == EAyarGDMOzneBilgisi::OTYerelKullanici)
		{
			if (kullaniciList.contains(ozne))
				continue;
		}
		if ( ozne.getOzneTipi() == EAyarGDMOzneBilgisi::OTYerelGrup)
		{
			_listeyeKullaniciEkle(gdmKullanicilariAlOzneden(ozne),kullaniciList);
		}
	}

	return kullaniciList;
}



EAyarGDMOzneBilgisi EAyarGDMOzneBilgisi::gdmOzneAl(int iOzneNo)
{
	EAyarGDMOzneBilgisi retOzne;
	QList<EAyarGDMOzneBilgisi> bulunanlar = EAyarGDMOzneBilgiManager::getInstance()->ozneBul_OzneID(iOzneNo);
	if (!bulunanlar.isEmpty())
	{
		retOzne = bulunanlar.at(0);
	}
	return retOzne;	
}

EAyarGDMOzneBilgisi EAyarGDMOzneBilgisi::gdmOzneAlAdVeEPostayaGore(const QString &irAd,const QString & iEPosta)
{
	EAyarGDMOzneBilgisi retOzne;
	QList<EAyarGDMOzneBilgisi> bulunanlar = EAyarGDMOzneBilgiManager::getInstance()->ozneBul_AdEPosta(irAd,iEPosta);
	if (!bulunanlar.isEmpty())
	{
		retOzne = bulunanlar.at(0);
	}
	return retOzne;
}

EAyarGDMOzneBilgisi EAyarGDMOzneBilgisi::gdmOzneAlAdinaGore(const QString &irAd)
{
	EAyarGDMOzneBilgisi retOzne;
	QList<EAyarGDMOzneBilgisi> bulunanlar = EAyarGDMOzneBilgiManager::getInstance()->ozneBul_Ad(irAd);
	if (!bulunanlar.isEmpty())
	{
		retOzne = bulunanlar.at(0);
	}
	return retOzne;	
}



void EAyarGDMOzneBilgisi::_ozneOlmamali(const QString& irAd)
{
	try
	{
		gdmOzneAlAdinaGore(irAd);
	}
	catch (EAyarException &e)
	{
		if(e.sebep() == EAyarException::OzneBulunamadi)
			return;
	}

	throw YENIAYAREXCEPTION(EAyarException::OzneZatenVar,"Ozne zaten ayarlar tablosunda var!");
}

void EAyarGDMOzneBilgisi::ozneYazYerelKullanici(const QString& irAd,const QString& irEPosta)
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneYaz(irAd,irEPosta,"",EAyarGDMOzneBilgisi::OTYerelKullanici);	
}

void EAyarGDMOzneBilgisi::ozneYazYerelGrup(const QString& irAd)
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneYaz(irAd,"","",EAyarGDMOzneBilgisi::OTYerelGrup);
}

void EAyarGDMOzneBilgisi::ozneYazLdapGrup(const QString& irAd,const QString& irEPosta,const QString& irDN)
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneYaz(irAd,irEPosta,irDN,EAyarGDMOzneBilgisi::OTLdapGrup);
}

/**
* Öznenin adýný deðiþtirmek için kullanýlr.
* \param irYeniAd 
* Öznenin yeni adý.
*/
void EAyarGDMOzneBilgisi::ozneAdiDegistir(const QString &irYeniAd)
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneAdiDegistir(*this,irYeniAd);	
}

bool EAyarGDMOzneBilgisi::isOzneDizinde(const EAyarGDMDizinBilgisi& irDizin)
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneDizindeVarMi(*this,irDizin);	
}

bool EAyarGDMOzneBilgisi::isOzneOznede(const EAyarGDMOzneBilgisi& irGrup)
{
	QList<EAyarGDMOzneBilgisi> altOzneler = EAyarGDMOzneBilgiManager::getInstance()->grupElemanlariniAl(*this);
	return (altOzneler.contains(irGrup));	
}



void EAyarGDMOzneBilgisi::grubaEkle(const EAyarGDMOzneBilgisi& irGrup)
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneyiGrubaEkle(*this,irGrup);		
}

void EAyarGDMOzneBilgisi::dizineEkle(const EAyarGDMDizinBilgisi& irDizin,bool iSilinebilir)
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneyiDizineEkle(*this,irDizin,iSilinebilir);			
}


void EAyarGDMOzneBilgisi::ozneEkleDizine(const EAyarGDMDizinBilgisi& iDizin,const QList<EAyarGDMOzneBilgisi>& iOzneler,bool iSilinebilir)
{
	foreach( EAyarGDMOzneBilgisi ozne , iOzneler)
	{
		ozne.dizineEkle(iDizin,iSilinebilir );
	}
}

void EAyarGDMOzneBilgisi::gruptanCikar(const EAyarGDMOzneBilgisi& irGrup)const
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneyiGruptanCikar(*this,irGrup);	
}

void EAyarGDMOzneBilgisi::dizindenCikar(const EAyarGDMDizinBilgisi& irDizin)const
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneyiDizindenCikar(*this,irDizin);	
}

void EAyarGDMOzneBilgisi::sil()
{
	EAyarGDMOzneBilgiManager::getInstance()->ozneSil(*this);		
}

QList<EAyarGDMOzneBilgisi> EAyarGDMOzneBilgisi::gruplarOzneyiBarindiran()const
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneyiIcerenGruplariAl(*this);		
}


QList<EAyarGDMDizinBilgisi> EAyarGDMOzneBilgisi::dizinlerOzneyiBarindiran()const
{
	return EAyarGDMOzneBilgiManager::getInstance()->ozneyiIcerenDizinleriAl(*this);		
}

void EAyarGDMOzneBilgisi::silTamamen()
{

	QList<EAyarGDMOzneBilgisi> gruplar = gruplarOzneyiBarindiran();
	foreach(EAyarGDMOzneBilgisi grup, gruplar)
	{
		gruptanCikar(grup);
	}

	QList<EAyarGDMDizinBilgisi> dizinler = dizinlerOzneyiBarindiran();
	foreach(EAyarGDMDizinBilgisi dizin, dizinler)
	{
		dizindenCikar(dizin);
	}


	if(getOzneTipi() == EAyarGDMOzneBilgisi::OTYerelGrup )
	{

		QList<EAyarGDMOzneBilgisi> ozneler = EAyarGDMOzneBilgisi::gdmOzneleriAlOzneden(*this);
		foreach(EAyarGDMOzneBilgisi ozne,ozneler)
		{
			ozne.gruptanCikar(*this);
		}
	}
	_haricListesindenSil();
	sil();
}

NAMESPACE_END

#define KERMEN_YEREL_KULLANICI_DB_FILE_NAME "eayarlar.esya" 

#define KERMEN_AYAR_TABLO_ADI_AYARLAR       "TBL_AYARLAR"
#define KERMEN_AYAR_TABLO_ADI_KULLANICILAR  "TBL_KULLANICILAR"
#define KERMEN_AYAR_TABLO_ADI_ACIK_DOSYALAR "TBL_ACIKDOSYALAR"
#define KERMEN_AYAR_TABLO_ADI_DIZIN_BILGISI "TBL_DIZINBILGISI"
#define KERMEN_AYAR_TABLO_ADI_DIZIN_OZNE    "TBL_DIZINOZNE"
#define KERMEN_AYAR_TABLO_ADI_GRUP_OZNE     "TBL_GRUPOZNE"
#define KERMEN_AYAR_TABLO_ADI_OZNE_BILGISI  "TBL_OZNEBILGISI"
#define KERMEN_AYAR_TABLO_ADI_VARSAYILAN_OZNELER "TBL_VARSAYILANOZNELER"

#define KERMEN_KULLANICI_TABLO_ALAN_ADI_VARSAYILAN "Varsayilan"
#define KERMEN_KULLANICI_TABLO_ALAN_ADI_EPOSTA     "EPosta"
#define KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID		"KulID"
#define KERMEN_KULLANICI_TABLO_ALAN_ADI_Varsayilan  "Varsayilan"		

#define KERMEN_KULLANICI_SORGU_SOL "SELECT KulID,EPosta,Varsayilan from TBL_KULLANICILAR"
#define KERMEN_KULLANICI_SORGU_WHERE_STATEMENT " WHERE "

#define KERMEN_KULLANICI_UPDATE_SORGU_SOL "UPDATE TBL_KULLANICILAR set Varsayilan='true' WHERE EPosta='%1'"
#define KERMEN_KULLANICI_UPDATE_DIGERLERINI_PASIF_YAP "UPDATE TBL_KULLANICILAR set Varsayilan='false' where not EPosta='%1'"


#define KERMEN_KULLANICI_EKLE_SORGU "INSERT INTO TBL_KULLANICILAR (EPosta,Varsayilan) values ('%1','false')"


#include "EAyarKullaniciManager.h"
#include "EAyarValueTool.h"
#include "EVeritabani.h"
#include "FileUtil.h"
#include "EsyaOrtak_Ortak.h"
#include "EYerelAyarManager.h"

#define E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN // ESYA_ORTAK_FUNC_BEGIN
#define E_AYAR_KULLANICI_MANAGER_FUNC_END // ESYA_ORTAK_FUNC_END

#define AYAR_KULLANICI_CONNECTION "Ayar_Kullanici_Connection"

NAMESPACE_BEGIN(esya)

EAyarKullanici::EAyarKullanici()
:mKulEPosta(""),mKulID(-1)
{
	mIsNull = true;
}

/**
 * Mevcut bir kullanýcý bilgileri ilk ilklendirilir.
 * \param iKulID 
 * Kullanicinin veritabanýndaki ID si
 * \param iKulEPosta 
 * Kullanýcýnýn Eposta adresi
 * \param iIsVarsayilan 
 * Varsayilan kullanýcý olup olmadýðý
 * \return 
 */
EAyarKullanici::EAyarKullanici(int iKulID,const QString & iKulEPosta,bool iIsVarsayilan)
:mKulID(iKulID),mKulEPosta(iKulEPosta),mIsVarsayilan(iIsVarsayilan)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	mIsNull = false;
	E_AYAR_KULLANICI_MANAGER_FUNC_END
}

void EAyarKullanici::setKulID(int iKulID)
{	
	mKulID = iKulID ;
}

void EAyarKullanici::setKulEPosta(const QString & iKulEPosta)
{
	mKulEPosta = iKulEPosta ;
}

void EAyarKullanici::setIsVarsayilan(bool iIsVarsayilan)
{
	mIsVarsayilan = iIsVarsayilan ;
}

int EAyarKullanici::getKulId() const
{
	return mKulID;
}

QString EAyarKullanici::getKulEPosta() const
{
	return mKulEPosta.toLower();
}
bool EAyarKullanici::getIsVarsayilan() const
{
	return mIsVarsayilan ;
}

EAyarKullanici::~EAyarKullanici()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	E_AYAR_KULLANICI_MANAGER_FUNC_END
}

/**
 * Herhangi bir kullanýcý bilgisi tutup tutmadýðýný döner.
 * \return 
 * Herhangi bir kullanýcý bilgisi tutup tutmadýðýný döner.
 */
bool EAyarKullanici::isNull()
{
	return mIsNull;
}

void EAyarKullanici::setIsNull(bool iIsNull)
{
	mIsNull = iIsNull ;
}


QMap<QString,EAyarKullaniciManager *> EAyarKullaniciManager::mInstanceMap=QMap<QString,EAyarKullaniciManager *>();
/**
 * Kullanýcýlarýn tutulduðu veritabaný ile ilklendirilir.
 * \param iDbPath 
 * Veritabaný dosya yolu 
 */
EAyarKullaniciManager::EAyarKullaniciManager(const QString & iDbPath)
:mDBPath(iDbPath)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN	
	E_AYAR_KULLANICI_MANAGER_FUNC_END
}

EAyarKullaniciManager::~EAyarKullaniciManager(void)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	E_AYAR_KULLANICI_MANAGER_FUNC_END
}

/**
 * Verilen sorguyu veritabanýnda çalýþtýrýr
 * \param iSorguStr 
 * Çalýþtýrýlak olan sorgu
 * \return 
 * Oluþan Query sonucunun döner.
 */
QSqlQuery * EAyarKullaniciManager::_dbSorguYap(const QSqlDatabase & iDb,const QString & iSorguStr)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	//mDb.open();
	QSqlQuery * pQuery = NULL ;
	ParamList params;	
	try
	{
		EVeritabani veritabani(iDb);
		pQuery = veritabani.sorguYap(iSorguStr,params);		
	}
	catch (EVeritabaniException &exc)
	{		
		DELETE_MEMORY(pQuery);				
		QSqlDatabase::removeDatabase(AYAR_KULLANICI_CONNECTION);
		ESYA_ORTAK_FUNC_ERROR("Sorgulama sonucunda hata olustu"+exc.printStackTrace())
		throw exc;
	}			
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return pQuery;
}

/**
 * Verilen sorgu ile kullanýcýlarý çeker.
 * \param iSorguStr 
 * KUllanýlacan olan sorgu stringi
 * \return 
 * Sorgu sonucunda bulunan kullanýcýlarý döner.
 */
QList<EAyarKullanici> EAyarKullaniciManager::_kullaniciAramaSorgusuYap(const QString & iSorguStr)
{	
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QList<EAyarKullanici> retKullaniciList;	
	QSqlQuery * pQuery = NULL ;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);	
	try
	{		
		pQuery = vt.sorguYap(iSorguStr,ParamList());		
	}
	catch (EException & exc) 
	{
		DELETE_MEMORY(pQuery);	
		ESYA_ORTAK_FUNC_ERROR("Kullanýcý getirme sýrasýnda hata olustu"+exc.printStackTrace())
		return retKullaniciList;
	}	
	while (pQuery->next()) 
	{
		EAyarKullanici kullaniciAyar(pQuery->value(0).toInt(),pQuery->value(1).toString(),EAyarValueTool::getBoolDeger(pQuery->value(2)));	
		retKullaniciList<<kullaniciAyar;	
	}
	DELETE_MEMORY(pQuery);	
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retKullaniciList;		
}

/**
 * Varsayýlan kullanýcý (Þu anda sistemdeki aktif kullanýcý) 'yý almak için kullanýlr.
 * \return 
 * Kullanýcý bilgilerini döner.
 */
EAyarKullanici EAyarKullaniciManager::varsayilanKullaniciGetir(bool iMutlakaDBdenOku/* =false */)
{
	 E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN;
	if(!iMutlakaDBdenOku)
	{
		if (!mAktifKullanici.isNull())
		{
			return mAktifKullanici;
		}
	}	

	EAyarKullanici retKullanici;
	QList<EAyarKullanici> retUsers = _kullaniciAramaSorgusuYap(_varsayilanKullaniciQuery());	
	if (retUsers.size()>1)
	{
		ESYA_ORTAK_FUNC_ERROR("Birden fazla Varsayýlan kullanýcý olamaz")
		throw EException("Birden fazla Varsayýlan kullanýcý olamaz",__FILE__,__LINE__);
	}
	if (retUsers.size() == 0)
	{
		ESYA_ORTAK_FUNC_ERROR("Hiçbir kullanýcý bulunamadý")
		return EAyarKullanici();
	}
	retKullanici = retUsers.at(0);
	retUsers.clear();
	mAktifKullanici = retKullanici ;
	mAktifKullanici.setIsNull(false);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retKullanici;	
}

/**
 * Sistemdeki tüm ilklendirilmiþ kullanýcýlarý almak için kullanýlýr
 *
 * \return 
 * Bulunan kullanýcýlarýn listesini döner.
 */
QList<EAyarKullanici> EAyarKullaniciManager::tumKullanicilariGetir()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QList<EAyarKullanici> retKulList=_kullaniciAramaSorgusuYap(_tumKullanicilarQuery());
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retKulList;
}

/**
 * Sistemdeki varsayýlan kullanýcý dýþýndaki tüm kullanýcýlarý almak icin kullanýlýr.
 *
 * \return 
 * Kullanýcý listesini döner.
 */
QList<EAyarKullanici> EAyarKullaniciManager::varsayilanOlmayanKullanicilariGetir()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QList<EAyarKullanici> retKulList = _kullaniciAramaSorgusuYap(_varsayilanOlmayanKullanicilarQuery());
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retKulList;
}

/**
 * Belirtilen Eposta ya sahip kullanýcýyý aktif kullanýcý yapmak icin kullanýlýr.
 * Yani aktif kullanýcýyý deðiþtirmek için kullanýlýr.
 * \param iKulEPosta 
 * Aktif yapýlacak kullanýcý eposta adresi
 */
bool EAyarKullaniciManager::varsayilanYap(const QString & iKulEPosta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN;
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);			
	QSqlQuery * pQuery = NULL ;
	try
	{
		pQuery = vt.sorguYap(_varsayilanYapQuery(iKulEPosta),ParamList());
		if (!pQuery)
		{
			DELETE_MEMORY(pQuery);		
			ESYA_ORTAK_FUNC_ERROR("%1 kullanicisi varsayilan kullanici yapilamadi-Sorgu sýrasýnda hata oluþtu")
				throw EException(QString("%1 kullanicisi varsayilan kullanici yapilamadi-Sorgu sýrasýnda hata oluþtu").arg(iKulEPosta),__FILE__,__LINE__);		
			return false;
		}
		int rows = pQuery->numRowsAffected();
		DELETE_MEMORY(pQuery);	
		if(rows != 1) 
		{
			ESYA_ORTAK_FUNC_ERROR("%1 kullanicisi varsayilan kullanici yapilamadi-Etkilenen kayýt sayýsý 0")
			throw EException(QString("%1 kullanicisi varsayilan kullanici yapilamadi").arg(iKulEPosta),__FILE__,__LINE__);				
			return false;
		}
		pQuery = vt.sorguYap(_digerleriniPasifYapQuery(iKulEPosta),ParamList());		
		rows = pQuery->numRowsAffected();
		DELETE_MEMORY(pQuery);	

	}
	catch (EException &exc)
	{	
		DELETE_MEMORY(pQuery);	
	}
	
	mAktifKullanici.setKulEPosta(iKulEPosta);
	mAktifKullanici.setIsNull(false);
	mAktifKullanici.setIsVarsayilan(true);
	return true;
	E_AYAR_KULLANICI_MANAGER_FUNC_END
}

/**
 * Kullanýcý yöneticisini almak için kullanýlýr.
 * \return 
 * Yöneticiyi döner.
 */
EAyarKullaniciManager * EAyarKullaniciManager::getInstance(const QString & iDbPath/* =NULL */)
{
	QString dbPath = iDbPath;
	if (dbPath.isEmpty())
	{
		dbPath = FileUtil::yerelAyarPath()+"/"+KERMEN_YEREL_KULLANICI_DB_FILE_NAME;
	}
	if (!mInstanceMap.contains(dbPath))
	{
		EAyarKullaniciManager * pAyarManager = new EAyarKullaniciManager(dbPath);
		mInstanceMap.insert(dbPath,pAyarManager);
		return pAyarManager;
	}
	else
	{
		return mInstanceMap.value(dbPath);
	}
	//ESYA_ORTAK_FUNC_END	
}

/**
 * Kullanýcýný aktif kullanýcý yapmak icin kullanýlacak sorguyu olusturur
 * \param iKulEPosta 
 * Aktif hale getirilmek istenen kullanýcý eposta adresi
 * \return 
 * Olusan sorguyu döner
 */
QString EAyarKullaniciManager::_varsayilanYapQuery(const QString & iKulEPosta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_UPDATE_SORGU_SOL).arg(iKulEPosta);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retSorgu;
}

/**
 * Belirtilen kullanýcýlar dýþýndaki tüm kullanýcýlarý pasif yapar.
 * \param iKulEPosta 
 * Aktif hale gelmiþ olan kullanýcý
 * \return 
 * Sorguyu döner
 */
QString EAyarKullaniciManager::_digerleriniPasifYapQuery(const QString & iKulEPosta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_UPDATE_DIGERLERINI_PASIF_YAP).arg(iKulEPosta);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retSorgu ;
}


QString EAyarKullaniciManager::_ePostadanKullaniciAramaQuery(const QString & iKulEposta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN;	
	QString retSorgu = QString(KERMEN_KULLANICI_SORGU_SOL)+QString(KERMEN_KULLANICI_SORGU_WHERE_STATEMENT)+QString("%1='%2'").arg(KERMEN_KULLANICI_TABLO_ALAN_ADI_EPOSTA).arg(iKulEposta);
	E_AYAR_KULLANICI_MANAGER_FUNC_END;
	return retSorgu;
}
/**
 * Aktif kullanýcýyý almak icin kullanýlan sorguyu olusturur.
 *
 * \return 
 * Sorguyu döner.
 */
QString EAyarKullaniciManager::_varsayilanKullaniciQuery()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_SORGU_SOL)+QString(KERMEN_KULLANICI_SORGU_WHERE_STATEMENT)+EAyarValueTool::getEsitlikStatementBool(KERMEN_KULLANICI_TABLO_ALAN_ADI_VARSAYILAN,true);
	E_AYAR_KULLANICI_MANAGER_FUNC_END;
	return retSorgu;
}

/**
 * Tüm kullanýcýlarý almak icin kullanýlan sorguyu getirir.
 * \return 
 * Sorguyu döner
 */
QString EAyarKullaniciManager::_tumKullanicilarQuery()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_SORGU_SOL);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retSorgu;
}

/**
 * Varsayýlan kullanýcý dýþýndaki kullanýcýlarý almak icin kullanýlan sorguyu olusturur.
 * \return 
 * Sorguyu döner.
 */
QString EAyarKullaniciManager::_varsayilanOlmayanKullanicilarQuery()
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_SORGU_SOL)+QString(KERMEN_KULLANICI_SORGU_WHERE_STATEMENT)+EAyarValueTool::getEsitlikStatementBool(KERMEN_KULLANICI_TABLO_ALAN_ADI_VARSAYILAN,false);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retSorgu;
}

bool EAyarKullaniciManager::kullaniciSil(const QString &iKulEPosta)
{
	//////////////////////////////////////////////////////////////////////////
	QList<EAyarKullanici> buluananKullanicilar = _kullaniciAramaSorgusuYap(_ePostadanKullaniciAramaQuery(iKulEPosta));
	if (buluananKullanicilar.isEmpty())
	{
		throw EException(QString("Kullanýcýlar tablosunda %1 Epostaya sahip kullanýcý bulunamadýðýndan silme iþlemi yapýlamadý.").arg(iKulEPosta));
	}

	Q_FOREACH(EAyarKullanici kullanici,buluananKullanicilar)
	{
		QString lKulIdStr = QString("%1").arg(kullanici.getKulId());
		//Ayarlar tablosundan kullanýcýnýn bilgileri siliniyor
		EAyarManager * pYerelAyarManager = EYerelAyarManager::getInstance();
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_AYARLAR,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);		
	
		//Açýk dosyalardan kullanýcýya ait bilgiler siliniyor.
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_ACIK_DOSYALAR,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);

		//GDM ile ilgili bilgiler siliniyor
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_GRUP_OZNE,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_DIZIN_OZNE,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_VARSAYILAN_OZNELER,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_DIZIN_BILGISI,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_OZNE_BILGISI,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);

		//Haric Tablosundan siliniyor.
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_YEREL_AYAR_TABLO_ADI_GRUP_HARIC_OZNELER,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);


		//Son olarak kullanýcýlar tablosundan siliniyor
		pYerelAyarManager->tablodanIlgiliKayitlariSil(KERMEN_AYAR_TABLO_ADI_KULLANICILAR,KERMEN_KULLANICI_TABLO_ALAN_ADI_KulID,lKulIdStr);
		//Eðer varsayýlansa herhangi bir kullanýcý varsayýlan olarak atanýyor
		if (kullanici.getIsVarsayilan())
		{
			QList<EAyarKullanici> tumKullanicilar = tumKullanicilariGetir();
			if (!tumKullanicilar.isEmpty())
			{
				EAyarKullanici kullanici=tumKullanicilar.at(0);
				varsayilanYap(kullanici.getKulEPosta());
			}			
		}
	}
	return true;
}

bool EAyarKullaniciManager::kullaniciEkle(const QString & iKulEPosta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN;
	QList<EAyarKullanici> tumKullanicilar = tumKullanicilariGetir();
	Q_FOREACH(EAyarKullanici kullanici,tumKullanicilar)
	{
		if (kullanici.getKulEPosta() == iKulEPosta)
		{
			return true;			
		}
	}	
	EVeritabani vt=EVeritabani::sqLiteVTUret(mDBPath);			
	QSqlQuery * pQuery = NULL ;
	try
	{
		pQuery = vt.sorguYap(_kullaniciEkleQuery(iKulEPosta),ParamList());
		if (!pQuery)
		{
			DELETE_MEMORY(pQuery);		
			ESYA_ORTAK_FUNC_ERROR("%1 kullanicisi eklenemedi-Sorgu sýrasýnda hata oluþtu");
			throw EException(QString("%1 kullanicisi eklenemedi-Sorgu sýrasýnda hata oluþtu").arg(iKulEPosta),__FILE__,__LINE__);		
			return false;
		}
		int rows = pQuery->numRowsAffected();
		DELETE_MEMORY(pQuery);	
		if(rows != 1) 
		{
			ESYA_ORTAK_FUNC_ERROR("%1 kullanicisi eklenemedi.-Etkilenen kayýt sayýsý 0")			
			return false;
		}		

	}
	catch (EException &exc)
	{	
		DELETE_MEMORY(pQuery);	
		ESYA_ORTAK_FUNC_ERROR(QString("%1 kullanicisi eklenemedi. Iþlemler sýrasýnda hata oluþtu. Hata = %1").arg(exc.printStackTrace()));
		return false;
	}
	return true;	
}

QString EAyarKullaniciManager::_kullaniciEkleQuery(const QString & iKulEPosta)
{
	E_AYAR_KULLANICI_MANAGER_FUNC_BEGIN
	QString retSorgu = QString(KERMEN_KULLANICI_EKLE_SORGU).arg(iKulEPosta);
	E_AYAR_KULLANICI_MANAGER_FUNC_END
	return retSorgu;
}

void EAyarKullaniciManager::cacheTemizle()
{
	mAktifKullanici=EAyarKullanici();
}

static void removeKullaniciManager(const QString & iDbPath);
NAMESPACE_END

#include "EKullaniciAyarYonetici.h"
#include "EAyarKullaniciManager.h"
#include "EAyarAlici.h"
#include "EAyarTanimlari.h"
#include "EAyarlar.h"
#include "FileUtil.h"

#define KERMEN_SERTIFIKA_DOGRULAMA_CEVRIMICI_POLITIKA_DOSYA_ADI "KermenCevrimIciPolitika.dat"
#define KERMEN_SERTIFIKA_DOGRULAMA_CEVRIMDISI_POLITIKA_DOSYA_ADI "KermenCevrimDisiPolitika.dat"

NAMESPACE_BEGIN(esya)
EKullaniciAyarYonetici::EKullaniciAyarYonetici(const QString & iKullaniciEMail /* =  */)
{
	mKullaniciEMail = iKullaniciEMail ;
	if (mKullaniciEMail == "")
	{
		mKullaniciEMail = EAyarKullaniciManager::getInstance()->varsayilanKullaniciGetir().getKulEPosta();
	}
}

EKullaniciAyarYonetici::~EKullaniciAyarYonetici(void)
{
}


bool EKullaniciAyarYonetici::getWorkingAsPortable()const
{
	bool workingAsPortable=false;
	EAyarAlici ayarAlici(AYAR_SNF_ISLEMOZELLIKLERI,AYAR_ISLEMOZELLIKLERI_PORTABLE_CALIS);
	try
	{
		EAyar ayar = ayarAlici.ayarBul();
		workingAsPortable = ayar.getBoolDeger();
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR(QString("Ayarlardan SINIF = %1 , AYAR = %2 alýnmaya çalýþýlýrken hata oluþtu").arg(AYAR_SNF_ISLEMOZELLIKLERI).arg(AYAR_ISLEMOZELLIKLERI_PORTABLE_CALIS));
	}	
	return workingAsPortable;
}

/**
 * Ayarlardan kullanýcýnýn cevrimdýþý çalýþýlýp çalýþmayacaðýný çeker
 *
 * \return 
 * Çevrimdýþý çalýþýlýp çalýþýlmayacaðýný döner.
 */
bool EKullaniciAyarYonetici::getSertifikaDogrulama_CevrimDisiCalis()
{
	bool retCevrimDisiCalis=false;
	EAyarAlici ayarAlici(AYAR_SNF_SERTDOGRULAMA,AYAR_SERTDOGRULAMA_CEVRIMDISI_CALIS);
	try
	{
		EAyar ayar = ayarAlici.ayarBul();
		retCevrimDisiCalis = ayar.getBoolDeger();
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR(QString("Ayarlardan SINIF = %1 , AYAR = %2 alýnmaya çalýþýlýrken hata oluþtu").arg(AYAR_SNF_SERTDOGRULAMA).arg(AYAR_SERTDOGRULAMA_CEVRIMDISI_CALIS));
	}	
	return retCevrimDisiCalis;
}


/**
 * Ayarlardan kullanýcýnýn cevrimdýþý durum için sertifika doðrulama parametresini getirir.
 * \return 
 * Dogrulama kullanýlacak olan politikayý XML olarak döner.
 */
QString EKullaniciAyarYonetici::getSertifikaDogrulama_CevrimDisiDogrulamaParametresi()
{
	QString retDogrulamaPolitikasi = "";
	QString dosyaYolu = FileUtil::genelAyarPath()+"/"+KERMEN_SERTIFIKA_DOGRULAMA_CEVRIMDISI_POLITIKA_DOSYA_ADI;
	QFile file(dosyaYolu);
	if(file.open(QIODevice::ReadOnly))
	{
		QByteArray data = file.readAll();
		retDogrulamaPolitikasi = QString(data);
		file.close();
	}
	else
	{	
		ESYA_ORTAK_FUNC_ERROR(QString("Cevrimdýþý doðrulama politikasý dosyasý açýlamadý. Dosya YOlu = %1").arg(dosyaYolu));
	}	
	return retDogrulamaPolitikasi;
}

/**
* Ayarlardan kullanýcýnýn cevrimiçi durum için sertifika doðrulama parametresini getirir.
* \return 
* Çevrimiçiyken Dogrulamada kullanýlacak olan politikayý XML olarak döner.
*/
QString EKullaniciAyarYonetici::getSertifikaDogrulama_CevrimIciDogrulamaParametresi()
{
	QString retDogrulamaPolitikasi = "";
	QString dosyaYolu = FileUtil::genelAyarPath()+"/"+KERMEN_SERTIFIKA_DOGRULAMA_CEVRIMICI_POLITIKA_DOSYA_ADI;
	QFile file(dosyaYolu);
	if(file.open(QIODevice::ReadOnly))
	{
		QByteArray data = file.readAll();
		retDogrulamaPolitikasi = QString(data);
		file.close();
	}
	else
	{	
		ESYA_ORTAK_FUNC_ERROR(QString("Cevrimici doðrulama politikasý dosyasý açýlamadý. Dosya YOlu = %1").arg(dosyaYolu));
	}	
	return retDogrulamaPolitikasi;
}


/**
 * Kullanýcýnýn cevrimiçi ya da cevrimdýþý calýþýp çalýþmadýðýna bakarak uygun sertifika dogrulama politikasýný döner.
 * \return 
 * Kullanýlacak olan politikayý XML olarak döner.
 */
QString EKullaniciAyarYonetici::getSertifikaDogrulamaPolitikasi()
{
	QString retDogrulamaPolitikasi = "";
	bool cevrimDisi = getSertifikaDogrulama_CevrimDisiCalis();
	if (cevrimDisi)
	{
		retDogrulamaPolitikasi = getSertifikaDogrulama_CevrimDisiDogrulamaParametresi();
	}
	else
	{
		retDogrulamaPolitikasi = getSertifikaDogrulama_CevrimIciDogrulamaParametresi();
	}
	return retDogrulamaPolitikasi;
}

/**
 * Kullanýcýnýn cevrimiçi ya da cevrimdýþý çalýþacaðýný belirler
 * \param iCevrimDisiCalis 
 */
void EKullaniciAyarYonetici::setSertifikaDogrulama_CevrimDisiCalis(bool iCevrimDisiCalis)
{	
	EAyarAlici ayarAlici(AYAR_SNF_SERTDOGRULAMA,AYAR_SERTDOGRULAMA_CEVRIMDISI_CALIS);
	try
	{
		EAyar ayar = ayarAlici.ayarBul();
		ayar.setDeger(iCevrimDisiCalis);		
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR(QString("Ayarlardan SINIF = %1 , AYAR = %2 belirlenmeye calýsýlýrken hata").arg(AYAR_SNF_SERTDOGRULAMA).arg(AYAR_SERTDOGRULAMA_CEVRIMDISI_CALIS)+exc.printStackTrace());
	}		
}

//Sadece belirli bir tip LDAP ile çalýþmayý desteklemek için 
EAyarLdap::LDAP_TIPI EKullaniciAyarYonetici::getCalisilacakLDAPTipi()
{
	EAyarAlici ayarAlici(AYAR_SNF_MGM,AYAR_MGM_CALISILACAK_LDAP_TIPI);
	EAyarLdap::LDAP_TIPI calisilacakLDAPTipi=EAyarLdap::LDAP_TIPI_BILINMEYEN;
	try
	{
		EAyar ayar = ayarAlici.ayarBul();
		calisilacakLDAPTipi = (EAyarLdap::LDAP_TIPI)ayar.getIntDeger();		
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR(QString("Ayarlardan SINIF = %1 , AYAR = %2 alýnmaya calýsýlýrken hata").arg(EAyarLdap::LDAP_TIPI_BILINMEYEN).arg(AYAR_MGM_CALISILACAK_LDAP_TIPI)+exc.printStackTrace());
	}	
	return calisilacakLDAPTipi;
}

void	EKullaniciAyarYonetici::setCalisilacakLDAPTipi(EAyarLdap::LDAP_TIPI iCalisilacakLDAPTipi)
{
	EAyar ayar;
	EAyarAlici ayarAlici(AYAR_SNF_MGM,AYAR_MGM_CALISILACAK_LDAP_TIPI);	
	try
	{
		ayar = ayarAlici.ayarBul();		
		ayar.setDeger(iCalisilacakLDAPTipi);
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR(QString("Ayarlardan SINIF = %1 , AYAR = %2 alýnmaya calýsýlýrken hata").arg(EAyarLdap::LDAP_TIPI_BILINMEYEN).arg(AYAR_MGM_CALISILACAK_LDAP_TIPI)+exc.printStackTrace());
		try
		{
			EAyar yeniAyar(AYAR_SNF_MGM,AYAR_MGM_CALISILACAK_LDAP_TIPI,EAyar::AyarTamsayiTipi,"",true,false,"");
			yeniAyar.setDeger(iCalisilacakLDAPTipi);			
		}
		catch (EException &exc)
		{		
			ESYA_ORTAK_FUNC_ERROR("Ayarlara yazma sýrasýnda hata oluþtu"+exc.printStackTrace())
		}		
	}	
}

NAMESPACE_END

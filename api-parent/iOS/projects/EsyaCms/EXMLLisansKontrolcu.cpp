#include "EXMLLisansKontrolcu.h"
#define LISANS_DOSYA_ADI "lisans.dat"
#include <QBuffer>
#include <QByteArray>
#include <QStandardPaths>
#include <QDir>
#include "ECMSUtil.h"
//#include "EDosyaHandle.h"
//#include "FileUtil.h"
//#include "MesajKutusu.h"
//#include "OrtakDil.h"
//#include "EsyaGenelGUI_Ortak.h"
//#include "EsyaGenelGUI_DIL.h"
#include "EASNToStringUtils.h"

#include "PerformansOlcumleri.h"
#include "Kronometre.h"

#include "ImzaDogrulamaSonucu.h"
#include "Logger.h"
#include "CMSException.h"

#define KERMEN_LISANS_URUN_ID 61//50

#define ESYA_LISANS_CERT_BASE64_VALUE "MIIBzzCCAS6gAwIBAgIBLDAMBggqhkjOPQQDAgUAMCExHzAdBgNVBAMMFk1BMyBM\
aXNhbnMgU3VudWN1c3UgMDEwHhcNMTAwMjE3MTQzMDM3WhcNMzAwMjE3MTQzMDM3\
WjAhMR8wHQYDVQQDDBZNQTMgTGlzYW5zIFN1bnVjdXN1IDAxMIGbMBAGByqGSM49\
AgEGBSuBBAAjA4GGAAQBoACcxRITKVA19jlVCt71UKC/g8WYnxEaGL6972BCJbCC\
inwU+X6+zRe50T+t3p9g7YNuVnoK3zTxj2Nxqo1Xw1ABnmOGg5pUm9MPkv7OR8he\
uMoqbkqA1XNjnyjun5nOTnV7BYua8mTpeX1nvySwTbf4UCFKfEEy/nLn4+WEHbDu\
mlWjEjAQMA4GA1UdDwEB/wQEAwIGwDAMBggqhkjOPQQDAgUAA4GMADCBiAJCAXDj\
SHG/1QD0Mf4fhCXPiA6ebziAFfq+f3SMLd0QH4k2Rt0pqHoDL7UtxiKyNc3zrPts\
gtaa3S/3g9lKJGyyA4iLAkIBItPdNCzN+8na9tJe6GlyPOUds0vs2Vh45wBPWbty\
jIFbsvp3YZWcJvPdrJIOW138ubuNGl5JiHr4tgH7HaJewnI="

//#define KERMEN_LISANS_DOSYA_YERI QString("%1/%2").arg(FileUtil::genelAyarPath()).arg(LISANS_DOSYA_ADI)
//#define KERMEN_LISANS_DOSYA_YERI QString("../Documents/lisans.dat")
#define KERMEN_LISANS_DOSYA_YERI QString("lisans.dat")

/**
 * Yapýcý metod 
 */
EXMLLisansKontrolcu::EXMLLisansKontrolcu(void)
:mLisansGecerli(false),mKontrolEdildi(false)
{
	mKontrolSonucu = EXMLLisansKontrolcu::GECERLI;
}

/**
 * Yýkýcý metod 
 */
EXMLLisansKontrolcu::~EXMLLisansKontrolcu(void)
{
}

EXMLLisansKontrolcu * EXMLLisansKontrolcu::mpInstance=NULL;


/**
 * Lisans dosyasýný yükleyip imzasýný kontrol eder.
 * \return 
 * Hata oluþmuþsa false döner.
 */
bool EXMLLisansKontrolcu::lisansKontrolEt()
{
	ESYA_GENEL_GUI_FUNC_BEGIN;
	mKontrolEdildi = true ;
	mLisansGecerli = _lisansDosyasiYukle(KERMEN_LISANS_DOSYA_YERI);
	if (mLisansGecerli)
	{
        Logger::log("Lisans dosyasi basarili bir sekilde yuklendi, imza kontrolu yapilacak");
		mLisansGecerli = _imzaKontroluYap(KERMEN_LISANS_DOSYA_YERI);
	}
    else
        Logger::log("Lisans dosyasi yuklenemedi!!!");
	ESYA_GENEL_GUI_FUNC_END;
	return mLisansGecerli;
}

/**
 * Lisans içeriði ile ilgili kontrolleri yapar
 * \return 
 * Kontroller sonucu hata oluþmusa false döner.
 */
bool EXMLLisansKontrolcu::gecerlilikKontroluYap()
{
	ESYA_GENEL_GUI_FUNC_BEGIN;
	if (mKontrolEdildi)
	{
        Logger::log("Kontrol edilmis");
		if(!mLisansGecerli)
		{
            //_hataMesajiGoster();
            throw CMSException("Lisans daha once kontrol edilmis ve gecersiz bulunmus");
		}
		ESYA_GENEL_GUI_FUNC_END;
		return mLisansGecerli;
	}
        //PERMORMANSLOGYAZ(ESYAGENELGUI_MOD,QString::fromLocal8Bit("XML Lisans kontrolü basladý."));
		Kasio::start(PERFORMANS_XML_LISANS_KONTROLU);

    Logger::log("Kontrol edilmemis, simdi edilecek");
	mLisansGecerli= lisansKontrolEt();
	if (mLisansGecerli)
	{
        Logger::log("Kontrol basarili, simdi tip kontrolu olacak");
		mLisansGecerli = _tipKontroluYap();
	}
	if (mLisansGecerli)
	{
        Logger::log("Tip kontrolu basarili, simdi tarih kontrolu olacak");
		mLisansGecerli = _tarihKontroluYap();
	}
//	if (mLisansGecerli)
//	{
//		 mLisansGecerli = _akilliKartKontroluYap();
//	}
	mKontrolEdildi = true ;
		Kasio::pause(PERFORMANS_XML_LISANS_KONTROLU);
        //PERMORMANSLOGYAZ(ESYAGENELGUI_MOD,QString::fromLocal8Bit("XML Lisans kontrolü tamamlandý.")+Kasio::toString(PERFORMANS_XML_LISANS_KONTROLU));
		Kasio::remove(PERFORMANS_XML_LISANS_KONTROLU);
	ESYA_GENEL_GUI_FUNC_END;
	return mLisansGecerli;
}

bool EXMLLisansKontrolcu::fonksiyonKontroluYap(int iFonksiyonKod)
{
	QPair<bool,QString> res = EXMLLisansKontrolcu::getInstance()->fonksiyonCalisabilirMi(iFonksiyonKod);

//	if (!res.first && mGUIGoster)
//	{
//		MesajKutusu::mesajGoster_s(DIL_GNL_HATA,DIL_LISANS_KONTROL_FONKSIYON_HATASI.arg(res.second),"",MesajKutusu::Error);
//	}

	return res.first;
}

/**
 * Lisans Kontrolu almak için kullanýlýr.
 * \return 
 * Singleton lisans kontrolcuyu döner.
 */
EXMLLisansKontrolcu * EXMLLisansKontrolcu::getInstance()
{
	if (EXMLLisansKontrolcu::mpInstance == NULL)
	{
		EXMLLisansKontrolcu::mpInstance = new EXMLLisansKontrolcu();
	}
	return EXMLLisansKontrolcu::mpInstance;

}

/**
 * Imzasýz XML verisinden lisans doðrulamada kullanýlacak yapýyý oluþturur
 * \param imzasizLisansIcerigi 
 * Ýmzasýz XML icerigi
 * \return 
 * Hata oluþmuþsa false döner
 */
bool EXMLLisansKontrolcu::_lisansIcerigiBelirle(const QByteArray & imzasizLisansIcerigi)
{
	mLisansIcerigi.xmlLisansIcerigindenIlklendir(imzasizLisansIcerigi);
	return true;
}

/**
 * Verilen dosya yolundan lisans dosyasýný yüklemeye çalýþýr.
 * \param iLisansDosyaYolu 
 * Lisans dosyasý yolu.
 * \return 
 * Yükleme sonunda hata oluþup oluþmadýðýný döner.
 */
bool EXMLLisansKontrolcu::_lisansDosyasiYukle(const QString & iLisansDosyaYolu)
{
	ESYA_GENEL_GUI_FUNC_BEGIN;

    QString path = QStandardPaths::standardLocations(QStandardPaths::DataLocation).value(0);
    QDir dir(path);
    if (!dir.exists())
        dir.mkpath(path);
    if (!path.isEmpty() && !path.endsWith("/"))
        path += "/";

    //std::cout << "MA3LOG" << path.toUtf8().constData() << "sig.der" << std::endl;
    //QFile file(path+"log.txt");

    if (!QFile::exists(path+iLisansDosyaYolu))
	{
        Logger::log("Lisans dosyasi yok!!");
        throw CMSException("Lisans dosyasi bulunamadi");

        //		mSonHataStr = DIL_1_KONUMUNDA_LISANS_DOSYASI_BULUNAMADI.arg(iLisansDosyaYolu);
		_hataMesajiGoster();
		mKontrolSonucu = EXMLLisansKontrolcu::DOSYA_BULUNAMADI;
		ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
		return false;
	}
    Logger::log("Lisans dosyasi var");
	ESYA_GENEL_GUI_FUNC_END;
	return true;
}

/**
 * Lisans dosyasý imza kontrolu yapar.
 * \param iLisansDosyaYolu 
 * Lisans dosyasý yolu.
 * \return 
 * Hata oluþmuþsa false döner.
 */
bool EXMLLisansKontrolcu::_imzaKontroluYap(const QString & iLisansDosyaYolu)
{
    //*
    ESYA_GENEL_GUI_FUNC_BEGIN;
    try
	{
		ECertificate lisansImzCert;
		try
		{
            Logger::log("Trying to read the cert");
			lisansImzCert=ECertificate(QByteArray::fromBase64(ESYA_LISANS_CERT_BASE64_VALUE));
		}		
		catch (EException & exc)
		{
            Logger::log("Lisansi imzalayan sertifika decode edilemedi");
            throw CMSException("Lisansi imzalayan sertifika decode edilemedi");

            //mSonHataStr = DIL_LISANSI_IMZALAYAN_SERTIFIKA_DECODE_EDILEMEDI;
			_hataMesajiGoster();
			mKontrolSonucu = EXMLLisansKontrolcu::IMZALAYAN_CERT_SORUNLU;
			ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
			return false;
		}		

        Logger::log("I guess cert is read");

//        EDosyaHandle sdFH(iLisansDosyaYolu);

//        StreamedSignedData ssd;
//        QBuffer outputBuffer;
//        outputBuffer.open(QIODevice::ReadWrite);
//        ssd.loadFromFile(sdFH.getFilePtr(),&outputBuffer);

        // benim dosya okumam, stream yok
        QByteArray license;
        //QString fileName = "../Documents/" + QString(LISANS_DOSYA_ADI);

        QString path = QStandardPaths::standardLocations(QStandardPaths::DataLocation).value(0);
        QDir dir(path);
        if (!dir.exists())
            dir.mkpath(path);
        if (!path.isEmpty() && !path.endsWith("/"))
            path += "/";

        QFile file(path+"lisans.dat");
//        SignedData ssd;
//        ContentInfo ci;
//        if(file.exists()) {
            Logger::log("Lisansi bulduk");
            file.open(QIODevice::ReadOnly);
            license = file.readAll();
            file.close();
            ContentInfo ci(license);
            Logger::log("content info len: " + ci.getContent().length());
            SignedData ssd(ci.getContent());

            //
//            QString fileName = "../Documents/content.xml";
//            QFile file(fileName);
//            file.open(QIODevice::ReadWrite);
//            file.write(ci.getContent());
//            file.close();
            //
//        }
//        else {
//            Logger::log("Lisansi bulamadik");
//            return false;
//        }
        //

		ImzaDogrulamaSonucu dogrulamaSonucu;
		QList<ECertificate> imzKontrolCerts;
		imzKontrolCerts<<lisansImzCert;
		ECMSUtil::verifyBES(ssd,imzKontrolCerts,QByteArray(),dogrulamaSonucu);

        Logger::log("veryBES i atlattik");
		
		if (dogrulamaSonucu.getDogrulamaSonucu() != ImzaDogrulamaSonucu::GECERLI)
		{
            Logger::log("Lisans dosyasi imzasi gecersiz");
            throw CMSException("Lisans dosyasi imzasi gecersiz");

            //mSonHataStr = DIL_LISANS_DOSYASI_IMZASI_GECERSIZ;
			_hataMesajiGoster();			
			mKontrolSonucu = EXMLLisansKontrolcu::IMZA_SORUNLU;
			ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
			return false;
		}

		bool gecerliSertifikaIleImzali=true;
		QList<ECertificate> imzadakiCerts = dogrulamaSonucu.getTBVCerts();
		for (int k=0;k<imzadakiCerts.size();k++)
		{
			ECertificate imzCert = imzadakiCerts.at(k);
			if (!imzKontrolCerts.contains(imzCert))
			{
				gecerliSertifikaIleImzali = false;
			}
		}

		if(!gecerliSertifikaIleImzali)
		{
            Logger::log("Gecerli sertifika ile imzali degil!!!");
            throw CMSException("Lisansi imzalayan sertifika gecersiz");

            //mSonHataStr = DIL_IMZADAKI_SERTIFIKA_GECERSIZ;
			_hataMesajiGoster();			
			mKontrolSonucu = EXMLLisansKontrolcu::IMZALAYAN_CERT_SORUNLU;
			ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
			return false;
		}


        Logger::log("Lisans icerigi belirlemeye kadar geldik");
//        bool retValue = _lisansIcerigiBelirle(outputBuffer.data());
        bool retValue = _lisansIcerigiBelirle(ssd.getEncapContentInfo().getEContent());
		ESYA_GENEL_GUI_FUNC_END;
		return retValue;
	}
	catch (EException &exc)
	{
        Logger::log("Lisans dosyasi imzasi gecersiz");
        throw CMSException("Lisans dosyasi imzasi gecersiz");

        //mSonHataStr = DIL_LISANS_DOSYASI_IMZASI_GECERSIZ;
		_hataMesajiGoster();
		mKontrolSonucu = EXMLLisansKontrolcu::IMZA_SORUNLU;
		ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
		return false;
	}	
    //*/
}

bool EXMLLisansKontrolcu::_tipKontroluYap()
{
	const QList<EUrunBilgi> & urunBilgileri = mLisansIcerigi.getUrunBilgileri();
    Logger::log("urunBilgileri size: " + QString::number(urunBilgileri.size()));
	for (int k=0;k<urunBilgileri.size();k++)
	{
		EUrunBilgi urunBilgi = urunBilgileri.at(k);
        Logger::log("urunID: " + QString::number(urunBilgi.getUrunID()));
		if (urunBilgi.getUrunID() == KERMEN_LISANS_URUN_ID)
		{
			return true;
		}
	}

    Logger::log("Lisans dosyasi iOS icin lisans icermiyor");
    throw CMSException("Lisans dosyasi iOS icin lisans icermiyor");

//	mSonHataStr = DIL_LISANS_LISANS_DOSYASI_KERMEN_ICIN_LISANS_ICERMEMEKTEDIR;
	_hataMesajiGoster();			
	mKontrolSonucu = EXMLLisansKontrolcu::TIP_UYUMSUZ;
	ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
	return false;
	return false;
}

/**
 * Lisans içeriðinin tarih bakýmýndan kontrol eder.
 * Henüz baþlamýþ olup olmadýðý ve bitmiþ olup olmadýðý kontrolleri yapýlýr.
 * \return 
 * Tarih geçersizse false, geçerli ise true
 * Hata oluþmusa kontrol sonucu kontrolSonucuAl() fonksiyonuyla alýnabilir.
 */
bool EXMLLisansKontrolcu::_tarihKontroluYap()
{
	ESYA_GENEL_GUI_FUNC_BEGIN;
	QDateTime dtSimdi = QDateTime::currentDateTime();
	if (mLisansIcerigi.getBaslangicTarihi()>dtSimdi)
	{//Lisans geçerliliði hala baþlamamýþ

        Logger::log("Lisans gecerliligi henuz baslamamis");
        throw CMSException("Lisans gecerliligi henuz baslamamis");

//		mSonHataStr =DIL_LISANS_KONTROL_LISANS_GECERLILIGI_HENUZ_BASLAMAMIS_1.arg(mLisansIcerigi.getBaslangicTarihi().toString(ESYA_LISANS_TARIH_FORMAT));
		_hataMesajiGoster();
		mKontrolSonucu = EXMLLisansKontrolcu::TARIH_GECERLILIK_BASLAMAMIS;
		ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
		return false;
	}
	if (mLisansIcerigi.getBitisTarihi()<dtSimdi)
	{//Lisans süresi dolmuþ.

        Logger::log("Lisans suresi dolmus");
        throw CMSException("Lisans suresi dolmus");

//		mSonHataStr = DIL_LISANS_KONTROL_LISANS_SURESI_DOLMUS_1.arg(mLisansIcerigi.getBitisTarihi().toString(ESYA_LISANS_TARIH_FORMAT));
		_hataMesajiGoster();
		mKontrolSonucu = EXMLLisansKontrolcu::TARIH_GECERLILIK_BITMIS;
		ESYA_GENEL_GUI_FUNC_ERROR(mSonHataStr);
		return false;
	}
	ESYA_GENEL_GUI_FUNC_END;
	return true;
}

/**
 *
 * \return 
 * Hata oluþmussa hatayý almak için kullanýlýr.
 */
EXMLLisansKontrolcu::LISANS_KONTROL_SONUCU EXMLLisansKontrolcu::kontrolSonucuAl() const
{
	return mKontrolSonucu;
}

/**
 * Lisans kontrolleri sýrasýnda çýkan hatalarýn mesaj kutusu olarak kullanýcýya gösterilip gösterilmeyeceðini belirtir.
 * \param iGUIGoster 
 * GUI gösterilip gösterilmeyeceði.
 */
void EXMLLisansKontrolcu::setGUIGoster(bool iGUIGoster)
{	
	mGUIGoster = iGUIGoster ;
}

/**
 * Lisans kontrolleri sýrasýnda oluþan son hatanýn metnini almak için kullanýlýr.
 * \return 
 * Son hatayý döner.
 */
QString EXMLLisansKontrolcu::getSonHataStr() const
{
	return mSonHataStr;
}

/**
 * OLusan hata gösterilmesi istendiyse kullanýcýya gösterir.
 */
void EXMLLisansKontrolcu::_hataMesajiGoster()
{
	if (mGUIGoster && (!mSonHataStr.isEmpty()))
	{
//		MesajKutusu::mesajGoster_s(DIL_GNL_HATA,mSonHataStr,"",MesajKutusu::Error);
	}
}

const ELisansIcerigi & EXMLLisansKontrolcu::getLisansIcerigi() const
{
	return mLisansIcerigi;
}

ELisansIcerigi EXMLLisansKontrolcu::getLisansIcerigi()
{
	return mLisansIcerigi;
}

//bool EXMLLisansKontrolcu::_akilliKartKontroluYap()
//{
//	ESYA_GENEL_GUI_FUNC_BEGIN;
//	bool retValue = true;
//	const QList<QPair<QString,QString> > &  kartKontrolListesi = mLisansIcerigi.getKartKontrolListesi();
//	if (kartKontrolListesi.isEmpty())
//	{
//		ESYA_GENEL_GUI_FUNC_END_PARAM("Kart kontrol listesi bos")
//		return retValue;
//	}
	
//	while(1)
//	{
//		QList<QByteArray> atrList;
//		try
//		{
//			atrList = EPKCS11::getCardsATR();
//		}
//		catch(EException & exc)
//		{
//			ESYA_GENEL_GUI_FUNC_ERROR("Takýlý kartlarýn ATR leri okunurkan hata oluþtu"+exc.printStackTrace());
//			retValue = false;
//		}
//		QStringList kartIsimListesi;
//		bool sonuc=false;
//		for (int k=0;k<kartKontrolListesi.size();k++)
//		{
//			QPair<QString,QString> kartAtr = kartKontrolListesi.at(k);
//			if (!kartIsimListesi.contains(kartAtr.first))
//			{
//				kartIsimListesi<<kartAtr.first;
//			}
//			for (int l=0;l<atrList.size();l++)
//			{
//				QByteArray atrByte = atrList.at(l);
//				QString atrKartStr = EASNToStringUtils::byteArrayToStr(atrByte,false);
//				if (atrKartStr.compare(kartAtr.second,Qt::CaseInsensitive)==0)
//				{
//					sonuc = true;
//					break;
//				}
//			}
//		}
//		if (!sonuc)
//		{
//			ESYA_GENEL_GUI_FUNC_ERROR("Takýlý kartlarýn ATR leri lisanstakilerden deðil");
//			QString araStr="<ul>";
//			for(int k=0;k<kartIsimListesi.size();k++)
//			{
//				QString kartAdi=kartIsimListesi.at(k);
//				QString kartAd = "<li>"+kartAdi+"</li>";
//				araStr+=kartAd;
//			}
//			araStr+="</ul>";

//			int retCode = MesajKutusu().retryMesajGoster(DIL_GNL_HATA,QString(DIL_LISANS_KONTROL_KART_YOK_UYARI).arg(araStr));
//			if (retCode == QDialog::Rejected)
//			{
//				ESYA_GENEL_GUI_FUNC_END_PARAM("Kullanýcý iptal etti");
//				retValue = false;
//				break;
//			}
//		}
//		else
//		{
//			retValue = true ;
//			break;
//		}
//	}
//	ESYA_GENEL_GUI_FUNC_END;
//	return retValue;
//}

QPair<bool,QString> EXMLLisansKontrolcu::fonksiyonCalisabilirMi(int iFonksiyonKod)
{
	bool kontrolSonuc=true;
	if (!mKontrolEdildi)
	{
		kontrolSonuc = gecerlilikKontroluYap();
		if (!kontrolSonuc)
		{
			return qMakePair(false,QString());
		}
	}

	const QMap<int,QString> & fonkKisitListesi = mLisansIcerigi.getFonksiyonKisitListesi();
	if (fonkKisitListesi.contains(iFonksiyonKod))
	{
		return qMakePair(false,fonkKisitListesi.value(iFonksiyonKod));
	}
	else
	{
		return qMakePair(true,QString());
	}
}

bool EXMLLisansKontrolcu::fonksiyonCalisabilirMiShort(int iFonksiyonKod)
{
	return fonksiyonCalisabilirMi(iFonksiyonKod).first;
}

const QMap<int,QString> &  EXMLLisansKontrolcu::calisamayanFonkListesiAl()
{
	return mLisansIcerigi.getFonksiyonKisitListesi();
}

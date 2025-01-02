#include "ELisansIcerigi.h"
#include <QDomDocument>
#include "Logger.h"

NAMESPACE_BEGIN(esya)
EUrunBilgi::EUrunBilgi(const int & iUrunID)
:mUrunID(iUrunID)
{	
}
EUrunBilgi::EUrunBilgi(const int & iUrunID,const QMap<QString,QString> & iOzellikListesi)
:mUrunID(iUrunID),mOzellikListesi(iOzellikListesi)
{
}

EUrunBilgi::~EUrunBilgi()
{	
}

ELisansIcerigi::ELisansIcerigi()
{

}
/**
 * Lisans dosyasýnýn içeriðiyle ilklendirilir
 */
ELisansIcerigi::ELisansIcerigi(const QString & iTumLisansData)
{	
	_initFromTumLisansData(iTumLisansData);
}

ELisansIcerigi::~ELisansIcerigi(void)
{	
}

/**
 * Lisans dosyasý içeriðinden elemanlarýný oluþturur
 */
void ELisansIcerigi::_initFromTumLisansData( QString iTumLisansData)
{	   
	ESYA_GENEL_GUI_FUNC_BEGIN;
	QStringList lTumListe = iTumLisansData.split("~~",QString::SkipEmptyParts);	
	if (lTumListe.isEmpty())
	{
		ESYA_GENEL_GUI_FUNC_ERROR("Lisans dosyasýnýn içeriði sorunlu - Parse edilemedi")
		return;
	} 
	mKurum = lTumListe.at(0);	
	mYetkili = lTumListe.at(1);		   	
	mEPosta = lTumListe.at(2);	
	mTelefon = lTumListe.at(3);
	mBaslangicTarihi = QDateTime::fromString(lTumListe.at(4),"dd/MM/yyyy");
	mBitisTarihi = QDateTime::fromString(lTumListe.at(5),"dd/MM/yyyy");		
	ESYA_GENEL_GUI_FUNC_END;
}

/**
 * Dýþarýdan verilen dosya içeriðinden elemanlarýný set eder 
 */
void ELisansIcerigi::setTumLisansData(QString iTumLisansData)
{
	ESYA_GENEL_GUI_FUNC_BEGIN
	_initFromTumLisansData(iTumLisansData);
	ESYA_GENEL_GUI_FUNC_END
}

QString ELisansIcerigi::getYetkili()
{
	return mYetkili;
}

QString ELisansIcerigi::getKurum()
{
	return mKurum;
}

QString ELisansIcerigi::getEPosta()
{
	return mEPosta;
}

QString ELisansIcerigi::getTelefon()
{
	return mTelefon;
}

QDateTime ELisansIcerigi::getBaslangicTarihi()
{
	return mBaslangicTarihi;
}

QDateTime ELisansIcerigi::getBitisTarihi()
{
	return mBitisTarihi;
}
/*
<Lisans>
	<LisansID></LisansID>
	<LisansTipi></LisansTipi>
	<OrtakAlanlar>
		<BaslangicTarihi></BaslangicTarihi>
		<BitisTarihi></BitisTarihi>
		<Kurum>
			<KurumAdi></ KurumAdi >
			<KurumYetkilisi></ KurumYetkilisi>
			<EPosta></EPosta>
			<TelNo></TelNo>
		</Kurum>
	<OrtakAlanlar>
	<Sunucu>
		<IP></IP>
		<BilgisayarAdi></BilgisayarAdi>
		<KullaniciSayisi></KullaniciSayisi>
		<KayitciSayisi></KayitciSayisi>		
		<KokAltKok><KokAltKok>		
	</Sunucu>
	<API>
		<Domain></Domain>
	</API>
</Lisans>
*/
#define KERMEN_LISANS_TAG_ADI_LISANS_ID "LisansID"
#define KERMEN_LISANS_TAG_ADI_BASLANGIC_TARIHI "BaslangicTarihi"
#define KERMEN_LISANS_TAG_ADI_BITIS_TARIHI "BitisTarihi"
#define KERMEN_LISANS_TAG_ADI_KURUM_ADI "KurumAdi"
#define KERMEN_LISANS_TAG_ADI_KURUM_YETKILISI "Yetkili"
#define KERMEN_LISANS_TAG_ADI_EPOSTA "EPosta"
#define KERMEN_LISANS_TAG_ADI_TEL_NO "TelNo"
#define KERMEN_LISANS_TAG_ADI_KART_BILGI "KartBilgi"
#define KERMEN_LISANS_TAG_ADI_ATR_BILGI "ATRBilgi"
#define KERMEN_LISANS_TAG_ADI_KART_MARKA "KartMarka"
#define KERMEN_LISANS_TAG_ADI_ATR "ATR"

#define KERMEN_LISANS_TAG_ADI_URUN_BILGI "UrunBilgi"
#define KERMEN_LISANS_TAG_ADI_URUN_ID "UrunID"


#define KERMEN_LISANS_TAG_ADI_FONKSIYON_KISITLARI "FonksiyonKisitlari"
#define KERMEN_LISANS_TAG_ADI_KISIT "Kisit"
#define KERMEN_LISANS_TAG_ADI_KOD "Kod"
#define KERMEN_LISANS_TAG_ADI_AD "Ad"

void ELisansIcerigi::xmlLisansIcerigindenIlklendir(const QByteArray & iXMLLisansFile)
{
	ESYA_GENEL_GUI_FUNC_BEGIN;
	QDomDocument document;
	document.setContent(iXMLLisansFile);
	
	/*QString lisansTipiStr = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_LISANS_TIPI);
	int lisansTipi = lisansTipiStr.toInt();*/

	QString lisansIDStr = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_LISANS_ID);

	QString baslangicTarihStr = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_BASLANGIC_TARIHI);
	QDateTime baslangicTarih = QDateTime::fromString(baslangicTarihStr,ESYA_LISANS_TARIH_FORMAT);

	QString bitisTarihStr = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_BITIS_TARIHI);
	QDateTime bitisTarihi = QDateTime::fromString(bitisTarihStr,ESYA_LISANS_TARIH_FORMAT);

	QString kurumAdi = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_KURUM_ADI);
	QString kurumYetkilisi = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_KURUM_YETKILISI);
	QString ePosta = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_EPOSTA);
	QString telNo = _getStringValueTagName(document,KERMEN_LISANS_TAG_ADI_TEL_NO);

	//////////////////////////////////////////////////////////////////////////
	//Ürün bilgileri burada dolduruluyor.
	QList<int> urunIDList ;
	QDomNodeList urunBilgiList = document.elementsByTagName(KERMEN_LISANS_TAG_ADI_URUN_BILGI);
    Logger::log("parsing - urun bilgi size: " + QString::number(urunBilgiList.size()));
	for (int k=0;k<urunBilgiList.size();k++)
	{
		QString urunID;	
		QDomNode urunBilgiNode = urunBilgiList.at(k);
		QString tmp = urunBilgiNode.nodeName();
        Logger::log("urun bilgi node name: " + tmp);
		QDomNodeList childNodes = urunBilgiNode.childNodes();
        Logger::log("child node size: " + QString::number(childNodes.size()));
		for (int l=0;l<childNodes.size();l++)
		{			

			QDomNode node = childNodes.at(l);
			QString nodeName=node.nodeName();
            Logger::log("child node name: " + nodeName);
			QDomNodeList valueNodes = node.childNodes();									
			if (!valueNodes.isEmpty())
			{
                Logger::log("child nodes of child node is not empty, thats ok");
				QDomNodeList childNodesX = node.childNodes();	
				QString nodeName=node.nodeName();
                Logger::log("iste bu 50 mi diye bakiyoruz: " + nodeName);
				for (int x=0;x<childNodesX.size();x++)
				{
					QDomNode urunIDNode =childNodesX.at(x);  					
					QString urunIDNodeTxtValue = urunIDNode.nodeValue();				
					if (nodeName == KERMEN_LISANS_TAG_ADI_URUN_ID)
					{
						urunID = urunIDNodeTxtValue;
					}	
				}							
			}
            else
                Logger::log("child nodes of child node is EMPTY");
		}	
		mUrunBilgileri<<EUrunBilgi(urunID.toInt());
	}	
	//////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////		
 	QDomNodeList list = document.elementsByTagName(KERMEN_LISANS_TAG_ADI_ATR_BILGI);
	for (int k=0;k<list.size();k++)
	{
		QString kartMarka;
		QString atr;

		QDomNode atrBilgiNode = list.at(k);
		QString tmp = atrBilgiNode.nodeName();
		QDomNodeList childNodes = atrBilgiNode.childNodes();
		for (int l=0;l<childNodes.size();l++)
		{			
			QDomNode node = childNodes.at(l);
			QDomNodeList valueNodes = node.childNodes();									
			if (!valueNodes.isEmpty())
			{
				QDomNodeList childNodesX = node.childNodes();	
				QString nodeName=node.nodeName();
				for (int x=0;x<childNodesX.size();x++)
				{
					QDomNode txtMarkaAtrNode =childNodesX.at(x);  					
					QString nodeMarkaAtrTxtValue = txtMarkaAtrNode.nodeValue();				
					if (nodeName == KERMEN_LISANS_TAG_ADI_KART_MARKA)
					{
						kartMarka = nodeMarkaAtrTxtValue;
					}
					else if (nodeName == KERMEN_LISANS_TAG_ADI_ATR)
					{
						atr = nodeMarkaAtrTxtValue;
					}
				}
			}
		}
		if (!kartMarka.isEmpty() && !atr.isEmpty())
		{
			mKartKontrolListesi.append(qMakePair(kartMarka,atr));
		}		
	}	
	//////////////////////////////////////////////////////////////////////////

	//////////////////* Fonksiyon kýsýt listesi okunup oluþturuluyor *///////////////  
	//////////////////////////////////////////////////////////////////////////		
	QDomNodeList kisitList = document.elementsByTagName(KERMEN_LISANS_TAG_ADI_KISIT);
	for (int k=0;k<kisitList.size();k++)
	{
		QString fonkKod;
		QString fonkAd;

		QDomNode kisitListNode = kisitList.at(k);
		QString tmp = kisitListNode.nodeName();
		QDomNodeList childNodes = kisitListNode.childNodes();
		for (int l=0;l<childNodes.size();l++)
		{			
			QDomNode node = childNodes.at(l);
			QDomNodeList valueNodes = node.childNodes();									
			if (!valueNodes.isEmpty())
			{
				QDomNodeList childNodesX = node.childNodes();	
				QString nodeName=node.nodeName();
				for (int x=0;x<childNodesX.size();x++)
				{
					QDomNode txtMarkaAtrNode =childNodesX.at(x);  					
					QString nodeMarkaAtrTxtValue = txtMarkaAtrNode.nodeValue();				
					if (nodeName == KERMEN_LISANS_TAG_ADI_KOD)
					{
						fonkKod = nodeMarkaAtrTxtValue;
					}
					else if (nodeName == KERMEN_LISANS_TAG_ADI_AD)
					{
						fonkAd = nodeMarkaAtrTxtValue;
					}
				}
			}
		}
		if (!fonkKod.isEmpty() && !fonkAd.isEmpty())
		{
			mFonksiyonKisitListesi.insert(fonkKod.toInt(),fonkAd);			
		}		
	}	
	//////////////////////////////////////////////////////////////////////////
	mLisansID = lisansIDStr;
	mBaslangicTarihi = baslangicTarih;
	mBitisTarihi = bitisTarihi;
	mKurum = kurumAdi;
	mYetkili = kurumYetkilisi ;
	mEPosta = ePosta ;
	mTelefon = telNo ;
	ESYA_GENEL_GUI_FUNC_END;
}

QString ELisansIcerigi::_getStringValueTagName(const QDomDocument & iDocument,const QString & iTagName)
{	
	QString retValueStr="";
	QDomNodeList list = iDocument.elementsByTagName(iTagName);
	if (!list.isEmpty())
	{
		QDomNode node = list.item(0);
		QDomNodeList childNodes = node.childNodes();
		if (!childNodes.isEmpty())
		{
			QDomNode childNode = childNodes.at(0);
			retValueStr = childNode.nodeValue();
		}
	}
	return retValueStr;
}

QString ELisansIcerigi::getLisansID() const
{
	return mLisansID;
}


const QList<QPair<QString,QString> > & ELisansIcerigi::getKartKontrolListesi()
{
	return mKartKontrolListesi;
}
NAMESPACE_END

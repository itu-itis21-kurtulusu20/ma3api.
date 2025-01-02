#include "ImzaDogrulamaSonucu.h"
#include <QRegExp>
#include "aciklamalar.h"
#include "EsyaCMS_DIL.h"
using namespace esya;

/**
* \brief
* ImzaDogrulamaSonucu constructoru. Ýþaretçi memberlar NULL olarak ilklendirilir.
*/
ImzaDogrulamaSonucu::ImzaDogrulamaSonucu(void)
:	mDogrulamaSonucu(KONTROL_TAMAMLANAMADI),
	pSID(NULL),
	pSignerCert(NULL)
{
}

/**
* \brief
* ImzaDogrulamaSonucu kopya constructoru. 
*
* \param const ImzaDogrulamaSonucu &iIDS
* Ýmza Doðrulama Sonucu
*/
ImzaDogrulamaSonucu::ImzaDogrulamaSonucu(const ImzaDogrulamaSonucu &iIDS)
:	mKontrolAdi( iIDS.getKontrolAdi()),
	mAciklama( iIDS.getAciklama()),
	mDogrulamaSonucu(iIDS.getDogrulamaSonucu()),
	mKontrolDetaylari( iIDS.getKontrolDetaylari()),
	mAltDogrulamaSonuclari( iIDS.getAltDogrulamaSonuclari()),
	pSID(NULL),
	pSignerCert(NULL)
{
	if (pSID && pSID!=iIDS.getSID())
		DELETE_MEMORY(pSID);
	if (pSignerCert && pSignerCert!=iIDS.getSignerCert())
		DELETE_MEMORY(pSID);

	if (iIDS.getSID()  )
		pSID = new SignerIdentifier(*iIDS.getSID());

	if (iIDS.getSignerCert())
		pSignerCert = new ECertificate(*iIDS.getSignerCert());
}

/**
* \brief
* ImzaDogrulamaSonucu kopya constructoru. 
* 
* \param 		const QString & iKontrolAdi
* Kontrol Adý
*
* \param 		const QString & iAciklama
* Açýklama
*
* \param 		const IDS_Type iDS
* Ýmza Geçerlilik Durumu
*
* \param 		const QList<KontrolcuSonucu> & iKD
* Kontrol Detaylarý
*
* \param 		const QList<ImzaDogrulamaSonucu> & iADS
* Alt Ýmza Doðrulama Sonuçlarý
*
* \param 		const SignerIdentifier * ipSID
* Ýmzacý Tanýmlayýcýsý
*
* \param 		const ECertificate * ipSignerCert
* Ýmzacý Serttifikasý	
*/
ImzaDogrulamaSonucu::ImzaDogrulamaSonucu(	const QString & iKontrolAdi ,
											const QString & iAciklama,
											const IDS_Type iDS ,
											const QList<KontrolcuSonucu>  & iKD,
											const QList<ImzaDogrulamaSonucu>  & iADS,
											const SignerIdentifier* ipSID ,
											const ECertificate* ipSignerCert)
:	mKontrolAdi( iKontrolAdi ),
	mAciklama( iAciklama),
	mDogrulamaSonucu( iDS ),
	mKontrolDetaylari( iKD),
	mAltDogrulamaSonuclari( iADS ),
	pSID(NULL),
	pSignerCert(NULL)
{
	if (ipSID)
		pSID = new SignerIdentifier(*ipSID);

	if (ipSignerCert)
		pSignerCert = new ECertificate(*ipSignerCert);
}

ImzaDogrulamaSonucu& ImzaDogrulamaSonucu::operator=(const ImzaDogrulamaSonucu &iIDS)
{
	mKontrolAdi			= iIDS.getKontrolAdi();
	mAciklama			= iIDS.getAciklama();
	mDogrulamaSonucu	= iIDS.getDogrulamaSonucu();
	mKontrolDetaylari	= iIDS.getKontrolDetaylari();
	mAltDogrulamaSonuclari = iIDS.getAltDogrulamaSonuclari();
	
	if (pSID && pSID!=iIDS.getSID())
		DELETE_MEMORY(pSID);
	if (pSignerCert && pSignerCert!=iIDS.getSignerCert())
		DELETE_MEMORY(pSID);

	if (iIDS.getSID()  )
		pSID = new SignerIdentifier(*iIDS.getSID());

	if (iIDS.getSignerCert())
		pSignerCert = new ECertificate(*iIDS.getSignerCert());

	return *this;
}


/**
* \brief
* Kontrol Adý alanýný döner
*
* \return const QString & 
* Kontrol Adý
*/
const QString &ImzaDogrulamaSonucu::getKontrolAdi() const 
{
	return mKontrolAdi;
}

/**
* \brief
* Kontrol Adý alanýný döner
*
* \return QString & 
* Kontrol Adý
*/
QString &ImzaDogrulamaSonucu::getKontrolAdi()  
{
	return mKontrolAdi;
}

/**
* \brief
* Kontrol Adý alanýný belirler
*
* \param 		const QString & iKontrolAdi
* Kontrol Adý
*/
void ImzaDogrulamaSonucu::setKontrolAdi(const QString & iKontrolAdi)
{
	mKontrolAdi =iKontrolAdi ;
}

/**
* \brief
* Açýklama alanýný döner
*
* \return const QString &
* Açýklama
*/
const QString &ImzaDogrulamaSonucu::getAciklama() const 
{
	return mAciklama;
}

/**
* \brief
* Açýklama alanýný döner
*
* \return QString &
* Açýklama
*/
QString &ImzaDogrulamaSonucu::getAciklama()  
{
	return mAciklama;
}

/**
* \brief
* Ýmzacý Tanýmlayýcýsýný ödner
*
* \return	const SignerIdentifier * 
* Ýmzacý Tanýmlayýcýsý
*
*/
const SignerIdentifier * ImzaDogrulamaSonucu::getSID() const
{
	return pSID;
}

/**
* \brief
* Ýmzacý Tanýmlayýcýsýný ödner
*
* \return SignerIdentifier * 
* Ýmzacý Tanýmlayýcýsý
*
*/
SignerIdentifier * ImzaDogrulamaSonucu::getSID() 
{
	return pSID;
}

/**
* \brief
* Ýmzacý Tanýmlayýcýsýný belirler
*
* \param 		const SignerIdentifier * ipSID
* Ýmzacý Tanýmlayýcýsý
*
*/
void ImzaDogrulamaSonucu::setSID( const SignerIdentifier * ipSID)   
{
	if ( pSID != ipSID)
	{
		if (pSID)
			DELETE_MEMORY(pSID);
		if (ipSID)
			pSID = new SignerIdentifier(*ipSID);
	}
}

/**
* \brief
* Ýmzacý Sertifikasýný döner
*
* \return const ECertificate * 
* Ýmzacý Sertifikasý
*/
const ECertificate * ImzaDogrulamaSonucu::getSignerCert() const  
{
	return pSignerCert;
}

/**
* \brief
* Ýmzacý Sertifikasýný döner
*
* \return ECertificate * 
* Ýmzacý Sertifikasý
*/
ECertificate * ImzaDogrulamaSonucu::getSignerCert() 
{
	return pSignerCert;
}

/**
* \brief
* Ýmzacý Sertifikasýný belirler
*
* \param 		const ECertificate * ipSignerCert
* Ýmzacý Sertifikasý
*/
void ImzaDogrulamaSonucu::setSignerCert( const ECertificate * ipSignerCert)   
{
	if ( pSignerCert != ipSignerCert)
	{
		if (pSignerCert)
			DELETE_MEMORY(pSignerCert);
		if (ipSignerCert)
			pSignerCert = new ECertificate(*ipSignerCert);
	}
}

/**
* \brief
* Açýklama alanýný belirler
*
* \param 		const QString & iAciklama
* Açýklama
*
*/
void ImzaDogrulamaSonucu::setAciklama(const QString & iAciklama)
{
	mAciklama =iAciklama;
}

/**
* \brief
* Ýmza geçerlilik durumunu döner
*
* \return  		const ImzaDogrulamaSonucu::IDS_Type & 
* Ýmza geçerlilik durumu
*
*/
const ImzaDogrulamaSonucu::IDS_Type & ImzaDogrulamaSonucu::getDogrulamaSonucu() const
{
	return mDogrulamaSonucu;
}

/**
* \brief
* Ýmza geçerlilik durumunu döner
*
* \return ImzaDogrulamaSonucu::IDS_Type & 
* Ýmza geçerlilik durumu
*
*/
ImzaDogrulamaSonucu::IDS_Type & ImzaDogrulamaSonucu::getDogrulamaSonucu()
{
	return mDogrulamaSonucu ;
}

/**
* \brief
* Ýmza geçerlilik durumunu belirler
*
* \param const ImzaDogrulamaSonucu::IDS_Type & iDS
* Ýmza geçerlilik durumu
*
*/
void  ImzaDogrulamaSonucu::setDogrulamaSonucu(const ImzaDogrulamaSonucu::IDS_Type & iDS)
{
	mDogrulamaSonucu= iDS;
}

/**
* \brief
* Alt Ýmza Doðrulama Sonuçlarýný döner
*
* \return	const QList<ImzaDogrulamaSonucu>& 
* Alt imza doðrulama sonuçlarý
*
*/
const	QList<ImzaDogrulamaSonucu>&	ImzaDogrulamaSonucu::getAltDogrulamaSonuclari() const 
{
	return mAltDogrulamaSonuclari;
}


/**
* \brief
* Alt Ýmza Doðrulama Sonuçlarýný döner
*
* \return 	QList<ImzaDogrulamaSonucu>& 
* Alt imza doðrulama sonuçlarý
*
*/
QList<ImzaDogrulamaSonucu>&	ImzaDogrulamaSonucu::getAltDogrulamaSonuclari()  
{
	return mAltDogrulamaSonuclari;
}

/**
* \brief
* Alt Ýmza Doðrulama Sonuçlarýný belirler
*
* \param 		const QList<ImzaDogrulamaSonucu>& iADS
* Alt imza doðrulama sonuçlarý
*
*/
void ImzaDogrulamaSonucu::setAltDogrulamaSonuclari(const QList<ImzaDogrulamaSonucu>& iADS)
{
	mAltDogrulamaSonuclari = iADS;
}


/**
* \brief
* Yeni bir alt imza doðrulama sonucu ekler
*
* \param 		const ImzaDogrulamaSonucu & iADS
* Alt imza doðrulama sonucu
*
*/
void ImzaDogrulamaSonucu::addAltDogrulamaSonucu(const ImzaDogrulamaSonucu & iADS)
{
	mAltDogrulamaSonuclari.append(iADS);
}

/**
* \brief
* Kontrol detaylarýný döner
*
* \return const QList<KontrolcuSonucu> & 
* Kontrol Detaylarý 
*/
const QList<KontrolcuSonucu>& ImzaDogrulamaSonucu::getKontrolDetaylari() const 
{
	return mKontrolDetaylari;
}

/**
* \brief
* Kontrol detaylarýný döner
*
* \return QList<KontrolcuSonucu> & 
* Kontrol Detaylarý 
*/
QList<KontrolcuSonucu>&	ImzaDogrulamaSonucu::getKontrolDetaylari() 
{
	return mKontrolDetaylari;
}

/**
* \brief
* Kontrol detaylarýný belirler
*
* \param 		const QList<KontrolcuSonucu> & iKD
* Kontrol Detaylarý 
*/
void ImzaDogrulamaSonucu::setKontrolDetaylari(const QList<KontrolcuSonucu>& iKD )
{
	mKontrolDetaylari = iKD;
}
/**
* \brief
* Yeni bir kontrol detayý ekler.
*
* \param 		const KontrolcuSonucu & iKD
* Kontrol Detayý
*
*/
void ImzaDogrulamaSonucu::addKontrolDetayi(const KontrolcuSonucu& iKD)
{
	mKontrolDetaylari.append(iKD);
}

/**
* \brief
* Verilen metni verilen uzuluk kadar saða hizalandýrýr.
*
* \param 		const QString & iBlock
* Metin
*
* \param 		int iLen
* Hizalama miktarý 
*
* \return   	QT_NAMESPACE::QString
*/
QString ImzaDogrulamaSonucu::_indent(const QString &iBlock, int iLen)
{
	QString res = iBlock;
	QString after;
	after.fill(QChar(' '),iLen);
	res.replace(QRegExp("\n"),"\n" + after);
	res = after+res;
	return res;
}

/**
* \brief
* Ýmza geçerlilik durumunu yazýya çevirir
*
* \return   	QT_NAMESPACE::QString
*/
QString ImzaDogrulamaSonucu::getDogrulamaSonucuAsString() const
{
	switch (mDogrulamaSonucu)
	{
	case GECERLI: 
		{
			return A_GECERLI;
		}
	case ALT_IMZACI_KONTROLLERI_SORUNLU: 
		{
			return A_ALT_IMZACI_KONTROLLERI_SORUNLU;
		}
	case KONTROLLER_SORUNLU: 
		{
			return A_KONTROLLER_SORUNLU;
		}
	case KONTROL_TAMAMLANAMADI: 
		{
			return A_KONTROL_TAMAMLANAMADI;
		}
	}
}


/**
* \brief
* Ýmza doðrulama sonucunu yazýya çevirir
* 
* \return   	QT_NAMESPACE::QString
*/
QString ImzaDogrulamaSonucu::toString() const
{
	QString st_sonuc;

	st_sonuc += QString("%1 [%2]\n").arg(LBL_KONTROLADI).arg(getKontrolAdi());
	st_sonuc += QString("%1 %2\n").arg(LBL_DOGRULAMASONUCU).arg(getDogrulamaSonucuAsString());
	st_sonuc += QString("%1 %2\n").arg(LBL_ACIKLAMA).arg(getAciklama());
	
	const QList<KontrolcuSonucu> & kontrolDetaylari = getKontrolDetaylari();
	if (kontrolDetaylari.size() > 0 )
	{
		st_sonuc += LBL_KONTROL_DETAYLARI;
		st_sonuc += LBL_EMPTY_LINE;
		for (int i = 0 ; i<kontrolDetaylari.size(); i++)
		{
			st_sonuc += _indent(kontrolDetaylari[i].toString(),INDENT_COUNT);
			st_sonuc += LBL_EMPTY_LINE;
		}
		st_sonuc += LBL_LINE;
	}

	const QList<ImzaDogrulamaSonucu> & ads = getAltDogrulamaSonuclari();
	if (ads.size() > 0 )
	{
		st_sonuc += LBL_LINE;
		for (int i = 0 ; i<ads.size(); i++)
		{
			st_sonuc += _indent(ads[i].toString(),INDENT_COUNT);
			st_sonuc += LBL_EMPTY_LINE;
		}
		st_sonuc += LBL_LINE;
	}
	return st_sonuc;
}


/**
* \brief
* ImzaDogrulamaSonucu destructoru
*/
ImzaDogrulamaSonucu::~ImzaDogrulamaSonucu(void)
{
	DELETE_MEMORY(pSID);
	DELETE_MEMORY(pSignerCert);
}

/**
* \brief
* Ýmza aðacýnda yer alan ve doðrulanmasý gereken sertifikalarýn listesini döner.
*
* \param 		QList<ECertificate> & oTBVCerts
* Sertifika Listesi
*
*/
QList<ECertificate> ImzaDogrulamaSonucu::getTBVCerts()
{
	QList<ECertificate> tbvCerts;

	_collectTBVCerts(tbvCerts);

	return tbvCerts;
}

/**
* \brief
* Ýmza aðacýnda yer alan ve doðrulanmasý gereken sertifikalarýn listesini oluþturur.
*
* \param 		QList<ECertificate> & oTBVCerts
* Sertifika Listesi
*
*/
void ImzaDogrulamaSonucu::_collectTBVCerts(QList<ECertificate>  & oTBVCerts)
{
	if (pSignerCert && oTBVCerts.indexOf(*pSignerCert)<0 )
		oTBVCerts.append(*pSignerCert);

	for (int i = 0 ; i < mAltDogrulamaSonuclari.size() ; i++ )
	{
		mAltDogrulamaSonuclari[i]._collectTBVCerts(oTBVCerts);
	}
}


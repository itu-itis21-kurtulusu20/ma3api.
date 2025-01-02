#include "ImzaDogrulamaAlgoritmasi.h"
#include "EsyaCMS_DIL.h"

using namespace esya;

/**
* \brief
* ImzaDogrulamaAlgoritmasi constructoru
*
*/
ImzaDogrulamaAlgoritmasi::ImzaDogrulamaAlgoritmasi(const QList<ECertificate> & iCertList)
: mCertList(iCertList)
{
}


/**
* \brief
* Doðrulamada kullanýlacak sertifika listesini döner
*
* \return 	const QList<ECertificate> & 
* Sertifika Listesi
*
*/
const QList<ECertificate> & ImzaDogrulamaAlgoritmasi::getCertList() const 
{
	return mCertList;
}

/**
* \brief
* Doðrulamada kullanýlacak sertifika listesini belirler
*
* \param 		const QList<ECertificate> & iCertList
* Sertifika Listesi
*
*/
void ImzaDogrulamaAlgoritmasi::setCertList(const QList<ECertificate> &iCertList)
{
	mCertList = iCertList;
}

/**
* \brief
* Paralel Ýmza Kontrolcüleri döner
*
* \return   	const QList<ImzaKontrolcu*>&
* Paralel Ýmza Kontrolcüler
*/
QList<ImzaKontrolcu*>& ImzaDogrulamaAlgoritmasi::getParalelKontrolculer()
{
	return mParalelKontrolculer;
}

/**
* \brief
* Paralel Ýmza Kontrolcüleri döner
*
* \return   	const QList<ImzaKontrolcu*>&
* Paralel Ýmza Kontrolcüler
*/
const QList<ImzaKontrolcu*>&  ImzaDogrulamaAlgoritmasi::getParalelKontrolculer() const
{
	return mParalelKontrolculer;
}

/**
* \brief
* Ýmza Doðrulama Algoritamasýna bir paralel imza kontrolcü ekler.
*
* \param 		ImzaKontrolcu * pIK
* Ýmza Kontrolcü
*
*/
void ImzaDogrulamaAlgoritmasi::addParalelKontrolcu(ImzaKontrolcu * pIK)
{
	mParalelKontrolculer.append(pIK);
}

/**
* \brief
* Seri Ýmza Kontrolcüleri döner
*
* \return   	const QList<ImzaKontrolcu*>&
* Seri Ýmza Kontrolcüler
*/
QList<ImzaKontrolcu*>& ImzaDogrulamaAlgoritmasi::getSeriKontrolculer()
{
	return mParalelKontrolculer;
}

/**
* \brief
* Seri Ýmza Kontrolcüleri döner
*
* \return   	const QList<ImzaKontrolcu*>&
* Seri Ýmza Kontrolcüler
*/
const QList<ImzaKontrolcu*>&  ImzaDogrulamaAlgoritmasi::getSeriKontrolculer() const
{
	return mParalelKontrolculer;
}

/**
* \brief
* Ýmza Doðrulama Algoritamasýna bir seri imza kontrolcü ekler.
*
* \param 		ImzaKontrolcu * pIK
* Ýmza Kontrolcü
*
*/
void ImzaDogrulamaAlgoritmasi::addSeriKontrolcu(ImzaKontrolcu * pIK)
{
	mSeriKontrolculer.append(pIK);
}


/**
* \brief
* Verilen Ýmza Kontrolcü listesini boþaltýr.
*
* \param 		QList<ImzaKontrolcu * > & iKontrolculer
* Ýmza Kontrolcu Listesi 
*
*/
void ImzaDogrulamaAlgoritmasi::_freeKontrolculer(QList<ImzaKontrolcu*> & iKontrolculer)
{
	for (int i = 0 ; i<iKontrolculer.size(); i++ )
	{
		DELETE_MEMORY(iKontrolculer[i]);
	}
}


/**
* \brief
* Ýmzalý veriyi doðrular. Bütün imzacýlar için Ýmza Doðrulama Algoritmasýndaki bütün Ýmza Kontrolcülerin _kontrolYap() metodunu sýrayla çalýþtýrýr.
*
* \param 		const SignedData & iSD
* Ýmzalý Veri
*
* \param 		ImzaDogrulamaSonucu & oIDS
* Ýmza Doðrulama sonucu
*
* \throws 		
*
* \remark
*
* \return   	bool
*/
bool ImzaDogrulamaAlgoritmasi::kontrolYap( const SignedData & iSD, ImzaDogrulamaSonucu & oIDS)
{
	oIDS.setKontrolAdi(A_IMZALIVERI_KONTROLU);

	bool verified = true;
	for (int i = 0 ; i<iSD.getSignerInfos().size(); i++ )
	{
		ImzaDogrulamaSonucu ids;
		bool res = _kontrolYap(PARALEL,iSD.getSignerInfos()[i],ids);
		oIDS.getAltDogrulamaSonuclari().append(ids);
		if (verified)  verified = res; 
	}

	if(!verified)
	{
		oIDS.setDogrulamaSonucu(ImzaDogrulamaSonucu::ALT_IMZACI_KONTROLLERI_SORUNLU);
		oIDS.setAciklama(A_ALT_IMZACI_KONTROLLERI_SORUNLU);
	}
	else
	{
		oIDS.setDogrulamaSonucu(ImzaDogrulamaSonucu::GECERLI);
		oIDS.setAciklama(A_IMZALIVERI_KONTROLU_GECERLI);
	}
	return verified;
}


/**
* \brief
* Ýmzacý imzasýný doðrular. Ýmza Doðrulama Algoritmasýndaki bütün Ýmza Kontrolcülerin _kontrolYap() metodunu sýrayla çalýþtýrýr.
*
* \param 		ImzaDogrulamaAlgoritmasi::K_Type iType
* Ýmza Tipi
*
* \param 		const SignerInfo & iSI
* Ýmzacý
*
* \param 		ImzaDogrulamaSonucu & oIDS
* Ýmza Doðrulama Sonucu
*
* \return   	bool
* true: Doðrulama Baþarýlý false : Doðrulama Baþarýsýz
*/
bool ImzaDogrulamaAlgoritmasi::_kontrolYap( ImzaDogrulamaAlgoritmasi::K_Type iType, const SignerInfo& iSI, ImzaDogrulamaSonucu & oIDS)
{
	bool verified = true;
	const SignerIdentifier & sid = iSI.getSID();

	oIDS.setKontrolAdi(QString(A_IMZACI_KONTROLU_1).arg(iSI.getSID().toString()));
	oIDS.setSID(&sid);

	ECertificate cert;
	if (( iSI.getParentData()->getCertFromCertificates(sid,cert) || SignedData::getCertFromCertificates(mCertList,sid,cert) ) )
	{
		// Signing Cert Bulundu;
		oIDS.setSignerCert(&cert);
	}


	const QList<ImzaKontrolcu*> & kontrolculer = (iType == PARALEL) ? mParalelKontrolculer : mSeriKontrolculer;

	for (int i = 0 ; i<kontrolculer.size(); i++ )
	{
		KontrolcuSonucu ks;
		bool res = kontrolculer[i]->kontrolYap(iSI,ks);
		oIDS.getKontrolDetaylari().append(ks);
		if (verified) verified = res;
	}
	if(!verified)
	{
		oIDS.setDogrulamaSonucu(ImzaDogrulamaSonucu::KONTROLLER_SORUNLU);
		oIDS.setAciklama(A_KONTROLLER_SORUNLU);
	}
	for (int i = 0 ; i<iSI.getSigners().size(); i++ )
	{
		ImzaDogrulamaSonucu ids;
		bool res = _kontrolYap(SERI,iSI.getSigners()[i],ids);
		oIDS.getAltDogrulamaSonuclari().append(ids);
		if (verified) verified = res; 
	}
	if(!verified && oIDS.getDogrulamaSonucu() != ImzaDogrulamaSonucu::KONTROLLER_SORUNLU)
	{
		oIDS.setDogrulamaSonucu(ImzaDogrulamaSonucu::ALT_IMZACI_KONTROLLERI_SORUNLU);
		oIDS.setAciklama(A_ALT_IMZACI_KONTROLLERI_SORUNLU);
	}
	
	if (verified)
	{
		oIDS.setDogrulamaSonucu(ImzaDogrulamaSonucu::GECERLI);
		oIDS.setAciklama(A_GECERLI);
	}

	return verified;
}

/**
* \brief
* ImzaDogrulamaAlgoritmasi destructoru
*
*/
ImzaDogrulamaAlgoritmasi::~ImzaDogrulamaAlgoritmasi(void)
{
	_freeKontrolculer(mParalelKontrolculer);
	_freeKontrolculer(mSeriKontrolculer);
}

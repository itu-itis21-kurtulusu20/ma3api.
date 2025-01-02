#include "ImzaKriptoKontrolcu.h"
#include "KriptoUtils.h"
#include "ImzaDogrulamaAlgoritmasi.h"
#include "EsyaCMS_DIL.h"

using namespace esya;

const QString ImzaKriptoKontrolcu::ST_KONTROLADI = "IMZA KRIPTO KONTROLU";


ImzaKriptoKontrolcu::ImzaKriptoKontrolcu(ImzaDogrulamaAlgoritmasi* ipDA)
: ImzaKontrolcu(ipDA)
{
}

ImzaKriptoKontrolcu::ImzaKriptoKontrolcu(ImzaDogrulamaAlgoritmasi* ipDA, const ParametreListesi & iKP)
: ImzaKontrolcu(ipDA,iKP)
{
}

/**
* \brief
* Imzalý yapý üzerindeki Signature deðerini kriptografik olarak doðrular.
*
* \param 		const SignerInfo & iSI
* Ýmzacý
*
* \param 		KontrolcuSonucu & oKS
* Kontrol Detayý
*
* \return   	bool
*/
bool ImzaKriptoKontrolcu::kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS) 
{
	oKS.setKontrolAdi(DIL_IMZ_KRIPTO_KONTROLU);

	ECertificate cert;
	const SignedData * pSD = iSI.getParentData();
	SignerIdentifier sid = iSI.getSID();
	if (!pSD || !pDogrulamaAlgoritmasi)
	{ 
		DEBUGLOGYAZ(ESYACMS_MOD,A_PARENTDATA_NULL );		
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
		oKS.setAciklama(A_PARENTDATA_NULL);
		return false;
	}
	const QList<esya::ECertificate> &certList = pDogrulamaAlgoritmasi->getCertList();
	if (!( pSD->getCertFromCertificates(sid,cert) || SignedData::getCertFromCertificates(certList,sid,cert) ) )
	{
		// Signing Cert Bulunamadý;
		DEBUGLOGYAZ(ESYACMS_MOD,"Ýmzacý sertifikasý bulunamadý." );		
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
		oKS.setAciklama(A_SIGNING_CERT_NOTFOUND);
		return false;
	}
	
	QString st_sonuc ;
	QByteArray imzaliVeri;
	


	imzaliVeri = iSI.getSignedAttrsBytes(); 


	bool verified =  KriptoUtils::verifySignature(cert,imzaliVeri,iSI.getSignature(),iSI.getDigestAlgorithm());

	if (!verified)
	{
		st_sonuc = QString("%1 %2").arg(ST_KONTROLADI).arg(A_BASARISIZ);
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
	}
	else
	{
		st_sonuc = QString("%1 %2").arg(ST_KONTROLADI).arg(A_BASARILI);
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARILI);
	}
	DEBUGLOGYAZ(ESYACMS_MOD,st_sonuc);
	oKS.setAciklama(st_sonuc);
	return verified;

}

ImzaKriptoKontrolcu::~ImzaKriptoKontrolcu(void)
{
}

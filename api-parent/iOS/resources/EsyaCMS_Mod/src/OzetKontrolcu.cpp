#include "OzetKontrolcu.h"
#include "KriptoUtils.h"
#include "EsyaCMS_DIL.h"

#include "Logger.h"

using namespace esya;

const QString OzetKontrolcu::ST_KONTROLADI = "OZET KONTROLÜ";

OzetKontrolcu::OzetKontrolcu( ImzaDogrulamaAlgoritmasi * ipDA )
: ImzaKontrolcu(ipDA)
{
}

OzetKontrolcu::OzetKontrolcu( ImzaDogrulamaAlgoritmasi * ipDA ,const ParametreListesi & iKP)
: ImzaKontrolcu(ipDA,iKP)
{
}


/**
* \brief
* Imzalý yapý üzerindeki özet deðerini kriptografik olarak doðrular.
*
* \param 		const SignerInfo & iSI
* Ýmzacý
*
* \param 		KontrolcuSonucu & oKS
* Kontrol Detayý
*
* \return   	bool
*/
bool OzetKontrolcu::kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS)
{
	oKS.setKontrolAdi(DIL_IMZ_OZET_KONTROLU);

	QByteArray digest;
	const SignedData* pSD = iSI.getParentData();
	const SignerInfo *parentSigner = iSI.getParent();

	// Ata Ýmzacý tanýmlý ise  bu bir seri imzadýr
	if (parentSigner)
	{
        Logger::log("Parent signer var");
        digest = KriptoUtils::calculateDigest(parentSigner->getSignature(),"SHA-1"/*iSI.getDigestAlgorithm()*/);
	}
	else if ( pSD->getEncapContentInfo().getEContentPresent() ) // Imzalý veri de EContent Var mý?
	{
        Logger::log("Encap content info var");
		if (pSD->isStreamed()) // Imzalý Veri bir Stream objesi mi
		{
            Logger::log("Imzali veri bir stream objesi");
            /*
			int res = ((StreamedSignedData*)pSD)->getDigest(iSI.getDigestAlgorithm(),digest);
			if (res!= SUCCESS )
			{
				DEBUGLOGYAZ(ESYACMS_MOD,A_DIGEST_NOT_FOUND);		
				oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
				oKS.setAciklama(A_DIGEST_NOT_FOUND);
				return false;			
			}
            //*/
		}
		else  // Stream deðilse özet veri içindeki econtent deðeri üzerinden hesaplanabilir.
		{
            Logger::log("Imzali veri stream degil");
            digest = KriptoUtils::calculateDigest(pSD->getEncapContentInfo().getEContent(),"SHA-1"/*iSI.getDigestAlgorithm()*/);
		}
	}
	else // Ayrýk Imza ise plain data parametre olarak verilmiþ olmalý
	{
        /*
		QVariant pdParam = mKontrolParams.parametreDegeriAl(PN_PLAINDATA); 
		if (!pdParam.isNull()) //PlanData ByteArray olarak verilmiþ mi?
		{
			QByteArray pdBytes = pdParam.toByteArray();
			digest = KriptoUtils::calculateDigest(pdBytes,iSI.getDigestAlgorithm());
		}
		else // Plain Data Dosya olarak verilmiþ olabilir.
		{
			pdParam = mKontrolParams.parametreDegeriAl(PN_PLAINDATAFILENAME);
			if (!pdParam.isNull())
			{
				QString pdFileName = pdParam.toString();
				QFile pdFile(pdFileName);
				if (!pdFile.open(QIODevice::ReadOnly))
				{
					DEBUGLOGYAZ(ESYACMS_MOD,A_DIGEST_NOT_FOUND);		
					oKS.setKontrolSonucu(KontrolcuSonucu::KONTROL_TAMAMLANAMADI);
					oKS.setAciklama(A_PDFILE_NOT_FOUND);
					return false;			
				}
				digest = KriptoUtils::calculateStreamDigest(pdFile,iSI.getDigestAlgorithm());
			}
			else // Plain Data verilmemiþse doðrulama hatasý dön
			{
				DEBUGLOGYAZ(ESYACMS_MOD,A_DIGEST_NOT_FOUND);		
				oKS.setKontrolSonucu(KontrolcuSonucu::KONTROL_TAMAMLANAMADI);
				oKS.setAciklama(A_PD_NOT_FOUND);
				return false;			
			}
		}
        //*/
	}

	if ( iSI.hasDigest(digest)) // Özetler uyuþuyor mu?
	{
		DEBUGLOGYAZ(ESYACMS_MOD,QString("%1 %2").arg(ST_KONTROLADI).arg(A_BASARILI));		
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARILI);
		oKS.setAciklama(QString("%1 %2").arg(ST_KONTROLADI).arg(A_BASARILI));
		return true;			
	}
	else // özetler uyuþmuyorsa doðrulama hatasý dön
	{
		DEBUGLOGYAZ(ESYACMS_MOD,A_DIGEST_NOT_FOUND);		
		oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
		oKS.setAciklama(A_DIGEST_NOT_MATCH);
		return false;			
	}
}

OzetKontrolcu::~OzetKontrolcu(void)
{
}

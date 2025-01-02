#include "SignedAttributesKontrolcu.h"
#include "EASNToStringUtils.h"
#include "EsyaCMS_DIL.h"

using namespace esya;


const QString SignedAttributesKontrolcu::ST_KONTROLADI = "IMZALI ÖZELLIKLER KONTROLÜ";

SignedAttributesKontrolcu::SignedAttributesKontrolcu(ImzaDogrulamaAlgoritmasi* ipDA)
: ImzaKontrolcu(ipDA)
{
}

SignedAttributesKontrolcu::SignedAttributesKontrolcu(ImzaDogrulamaAlgoritmasi* ipDA, const ParametreListesi & iKP)
: ImzaKontrolcu(ipDA,iKP)
{
}

/**
* \brief
* Imzalý yapý üzerindeki imzalý özelliklerin doðruluðunu kontrol eder.
*
* \param 		const SignerInfo & iSI
* Ýmzacý
*
* \param 		KontrolcuSonucu & oKS
* Kontrol Detayý
*
* \return   	bool
*/
bool SignedAttributesKontrolcu::kontrolYap(const SignerInfo & iSI, KontrolcuSonucu & oKS)
{
	oKS.setKontrolAdi(DIL_IMZALI_OZELLIKLER_KONTROLU);
	QVariant incAttrParam = mKontrolParams.parametreDegeriAl(PN_SIGNED_ATTR_INC_SET);
	if (!incAttrParam.isNull())
	{
		QList<ASN1TObjId> attributes = incAttrParam.value< QList<ASN1TObjId> >();
		for (int i = 0 ; i < attributes.size(); i++ )
		{
			if (iSI.getSignedAttributes(attributes[i]).size() == 0 )
			{
				QString st_sonuc = QString(A_REQUIRED_SIGNED_ATTR_NOT_FOUND_1).arg(EASNToStringUtils::type2String(attributes[i])); 
				DEBUGLOGYAZ(ESYASERTIFIKADOGRULAMA_MOD,st_sonuc);		
				oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
				oKS.setAciklama(st_sonuc);
				return false;
			}		
		}
	}

	QVariant excAttrParam = mKontrolParams.parametreDegeriAl(PN_SIGNED_ATTR_EXC_SET);
	if (!excAttrParam.isNull())
	{
		QList<ASN1TObjId> attributes = excAttrParam.value< QList<ASN1TObjId> >();
		for (int i = 0 ; i < attributes.size(); i++ )
		{
			if (iSI.getSignedAttributes(attributes[i]).size() != 0 )
			{
				QString st_sonuc = QString(A_EXCLUDED_SIGNED_ATTR_FOUND_1).arg(EASNToStringUtils::type2String(attributes[i])); 
				DEBUGLOGYAZ(ESYASERTIFIKADOGRULAMA_MOD,st_sonuc);		
				oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
				oKS.setAciklama(st_sonuc);
				return false;
			}		
		}
	}

	QVariant validAttrParam = mKontrolParams.parametreDegeriAl(PN_SIGNED_ATTR_VALID_SET);
	if (!excAttrParam.isNull())
	{
		QList<ASN1TObjId> validAttributes = excAttrParam.value< QList<ASN1TObjId> >();
		const QList<Attribute> signedAttributes = iSI.getSignedAttributes();
		for (int sa = 0 ; sa < signedAttributes.size(); sa++ )
		{
			bool found =false;
			for (int va = 0 ; va < validAttributes.size(); va++ )
			{
				if ( signedAttributes[sa].getType() == validAttributes[va])
				{
					found= true;
					break;
				}
			}
			if (!found)
			{
				QString st_sonuc = QString(A_INVALID_SIGNED_ATTR_FOUND_1).arg(EASNToStringUtils::type2String(signedAttributes[sa].getType())); 
				DEBUGLOGYAZ(ESYASERTIFIKADOGRULAMA_MOD, st_sonuc);		
				oKS.setKontrolSonucu(KontrolcuSonucu::BASARISIZ);
				oKS.setAciklama(st_sonuc);
				return false;
			}
		}
	}

	QString st_sonuc = QString("%1 %2").arg(ST_KONTROLADI).arg(A_BASARILI); 
	DEBUGLOGYAZ(ESYASERTIFIKADOGRULAMA_MOD,st_sonuc );		
	oKS.setKontrolSonucu(KontrolcuSonucu::BASARILI);
	oKS.setAciklama(st_sonuc);
	return true;
}


SignedAttributesKontrolcu::~SignedAttributesKontrolcu(void)
{
}

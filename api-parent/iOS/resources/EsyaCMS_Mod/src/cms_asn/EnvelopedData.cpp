#include "EnvelopedData.h"
#include "ExtensionGenerator.h"
#include "KriptoUtils.h"
#include "cmslibrary_global.h"
#include "EASNException.h"
#include "ECMSException.h"
#include "KeyAgreeRecipientInfo.h"
#include "AlgoritmaBilgileri.h"
#include "ECCCMSSharedInfo.h"
#include "ECDHKeyAgreement.h"
#include "FileUtil.h"
#include "EKeyWrap.h"




using namespace esya;
 
const ASN1T_CMS_CMSVersion EnvelopedData::DEFAULT_VERSION = CMS_CMSVersion::v0;
const ASN1T_CMS_CMSVersion EnvelopedData::DEFAULT_KARI_VERSION = CMS_CMSVersion::v3;
const AlgorithmIdentifier EnvelopedData::DEFAULT_KEYENCALG = AlgorithmIdentifier(AES_id_aes256_CBC);
const AlgorithmIdentifier EnvelopedData::DEFAULT_KEYAGREEMENTALG = AlgorithmIdentifier(ALGOS_dhSinglePass_stdDH_sha1kdf_scheme);
const AlgorithmIdentifier EnvelopedData::DEFAULT_KEYWRAPALG = AlgorithmIdentifier(QByteArray(NULL_PARAMS),ALGOS_aes128_keywrap);


EnvelopedData::EnvelopedData(void)
: mIsEnveloped(false) , mOriginatorInfoPresent(false) , mVersion(DEFAULT_VERSION)
{

}

/**
 * \brief
 * EnvelopedData sýnýfý constructor ý.
 * 
 * \param iDataContent
 * Verinin kendisi.
 * 
 * \param isEncrypted
 * Verinin þifreli olup olmadýðý bilgisi. true : Þifreli / false : þifresiz
 * 
 * \throws <ECMSException>
 * Þifreli verinin ASN1 parse iþlemi sýrasýnda hata oluþursa ECMSException oluþur.
 * 
 * Þifreli ya da açýk bir veri için EnvelopedData objesi oluþturur. Þifreli veri 
 * ise ASN1 yapýlarýný çözümleyerek uygun alanlarýn atanmasýný saðlar.
 * 
 *\remarks
 * Write remarks for EnvelopedData here.
 * 
 * \see
 * Separate items with the '|' character.
 */
EnvelopedData::EnvelopedData(const QByteArray &iContent,bool isEnveloped)
: mIsEnveloped(isEnveloped) 
{
	if (! isEnveloped )
	{
		/* plain datayý ekle*/
		return ;
	}
	constructObject(iContent);	
}

EnvelopedData::EnvelopedData(const ASN1T_CMS_EnvelopedData & iED)
: mIsEnveloped(true)
{
	copyFromASNObject(iED);
}

EnvelopedData::EnvelopedData(const EnvelopedData & iED)
:	mIsEnveloped(iED.isEnveloped()),
	mVersion(iED.getVersion()),
	mOriginatorInfoPresent(iED.isOriginatorInfoPresent()),
	mOriginatorInfo(iED.getOriginatorInfo()),
	mRecipientInfos(iED.getRecipientInfos()),
	mEncryptedContentInfo(iED.getEncryptedContentInfo()),
	mUnprotectedAttrs(iED.getUnprotectedAttrs())
{
}

EnvelopedData& EnvelopedData::operator=(const EnvelopedData & iED)
{
	mIsEnveloped			= iED.isEnveloped();
	mVersion				= iED.getVersion();
	mOriginatorInfoPresent	= iED.isOriginatorInfoPresent();
	mOriginatorInfo			= iED.getOriginatorInfo();
	mRecipientInfos			= iED.getRecipientInfos();
	mEncryptedContentInfo	= iED.getEncryptedContentInfo();
	mUnprotectedAttrs		= iED.getUnprotectedAttrs();
	return *this;
}

bool esya::operator==(const EnvelopedData & iRHS, const EnvelopedData & iLHS)
{
	return (	( iRHS.isEnveloped()				== iLHS.isEnveloped()				) &&
				( iRHS.isOriginatorInfoPresent()	== iLHS.isOriginatorInfoPresent()	) &&
				( iRHS.getVersion()					== iLHS.getVersion()				) &&
				( iRHS.getOriginatorInfo()			== iLHS.getOriginatorInfo()			) &&
				( iRHS.getRecipientInfos()			== iLHS.getRecipientInfos()			) &&
				( iRHS.getEncryptedContentInfo()	== iLHS.getEncryptedContentInfo()   ) &&
				( iRHS.getUnprotectedAttrs()		== iLHS.getUnprotectedAttrs()		)		);
}

bool esya::operator!=(const EnvelopedData & iRHS, const EnvelopedData & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int EnvelopedData::copyFromASNObject(const ASN1T_CMS_EnvelopedData & iED)
{
	mVersion = iED.version;
	mOriginatorInfoPresent = iED.m.originatorInfoPresent;
	if ( mOriginatorInfoPresent )
		mOriginatorInfo.copyFromASNObject(iED.originatorInfo);
	if (iED.m.unprotectedAttrsPresent)
		Attribute().copyAttributeList(iED.unprotectedAttrs,mUnprotectedAttrs);
	
	mEncryptedContentInfo.copyFromASNObject(iED.encryptedContentInfo);
	RecipientInfo().copyRIs(iED.recipientInfos,mRecipientInfos);
	return SUCCESS;
}

int EnvelopedData::copyToASNObject(ASN1T_CMS_EnvelopedData & oED)const
{
	oED.version = mVersion;
	mEncryptedContentInfo.copyToASNObject(oED.encryptedContentInfo);
	RecipientInfo().copyRIs(mRecipientInfos,oED.recipientInfos);
	oED.m.unprotectedAttrsPresent = mUnprotectedAttrs.size()>0;
	oED.m.originatorInfoPresent = mOriginatorInfoPresent;

	if (oED.m.unprotectedAttrsPresent)
		Attribute().copyAttributeList(mUnprotectedAttrs,oED.unprotectedAttrs);
	if (oED.m.originatorInfoPresent)
		mOriginatorInfo.copyToASNObject(oED.originatorInfo);

	return SUCCESS;

}

void EnvelopedData::freeASNObject(ASN1T_CMS_EnvelopedData & oED)const
{
	EncryptedContentInfo().freeASNObject(oED.encryptedContentInfo);
	RecipientInfo().freeASNObjects(oED.recipientInfos);
	if (oED.m.unprotectedAttrsPresent)
		Attribute().freeASNObjects(oED.unprotectedAttrs);
	if (oED.m.originatorInfoPresent)
		OriginatorInfo().freeASNObject(oED.originatorInfo);
}

const bool & EnvelopedData::isEnveloped()	const
{
	return mIsEnveloped;
}

const bool & EnvelopedData::isOriginatorInfoPresent()	const
{
	return mOriginatorInfoPresent;
}

const ASN1T_CMS_CMSVersion	& EnvelopedData::getVersion()const
{
	return mVersion;
}
const OriginatorInfo & EnvelopedData::getOriginatorInfo() const
{
	return mOriginatorInfo;
}

const EncryptedContentInfo& EnvelopedData::getEncryptedContentInfo()const
{
	return mEncryptedContentInfo;
}

const QList<RecipientInfo>& EnvelopedData::getRecipientInfos()const
{
	return mRecipientInfos;
}

QList<RecipientInfo>& EnvelopedData::getRecipientInfos()
{
	return mRecipientInfos;
}


const QList<Attribute>& EnvelopedData::getUnprotectedAttrs() const
{
	return mUnprotectedAttrs;
}


void EnvelopedData::setVersion( const ASN1T_CMS_CMSVersion & iVersion)
{
	mVersion = iVersion;
}

void EnvelopedData::setOriginatorInfo (const OriginatorInfo &  iORI)
{
	mOriginatorInfoPresent = true;
	mOriginatorInfo = iORI;
}

void EnvelopedData::setRecipientInfos( const  QList<RecipientInfo> &iRIs )
{
	mRecipientInfos = iRIs;
}

void EnvelopedData::setEncryptedContentInfo( const EncryptedContentInfo &iECI)
{
	mEncryptedContentInfo = iECI;
}

void EnvelopedData::setUnprotectedAttrs(const QList<Attribute> iUAs)
{
	mUnprotectedAttrs = iUAs;		
}

void EnvelopedData::addRecipientInfo( const RecipientInfo &iRecipientInfo )
{
	mRecipientInfos.append(iRecipientInfo);
}

void EnvelopedData::addRecipientInfos( const QList<RecipientInfo> &iRecipientInfos )
{
	mRecipientInfos += iRecipientInfos;
}

void EnvelopedData::addUnprotectedAttrs(const QList<Attribute> iUAs)
{
	mUnprotectedAttrs += iUAs;
}

void EnvelopedData::addUnprotectedAttr(const Attribute iUA)
{
	mUnprotectedAttrs.append(iUA);
}

EnvelopedData::~EnvelopedData(void)
{
}


/********************************************************************/

/**
* \brief
* Verilen datayý(Simetrik Anahtar) asimetrik þifreler
* 
* \param iCert
* Þifrelemede kullanýlacak Sertifika
*
* \remarks
*
* 
* \see
*
*/
QByteArray EnvelopedData::encryptKey( const ECertificate& iCert, const QByteArray iKey)
{
	CMS_FUNC_BEGIN

	QByteArray encryptedKey;
	QByteArray pubKey = iCert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes();
	AlgorithmIdentifier encAlg = iCert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm();
	AcikAnahtarBilgisi aa(pubKey);
	return  KriptoUtils::encryptDataAsymmetric(iKey,aa,encAlg);

	CMS_FUNC_END
}


/********************************************************************/

/**
* \brief
* Verilen datayý(Simetrik Anahtar) asimetrik þifreler
* 
* \param iCert
* Þifrelemede kullanýlacak Sertifika
*
* \remarks
*
* 
* \see
*
*/
QByteArray EnvelopedData::encryptGroupKey( const ECertificate& iCert, const QByteArray &iKey,int iGroupIndex,int iGroupSize)
{
	CMS_FUNC_BEGIN

	QByteArray encryptedKey;
	QByteArray pubKey = iCert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes();
	AlgorithmIdentifier encAlg = iCert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm();
	AcikAnahtarBilgisi aa(pubKey);
	
	int blockSize = iKey.size()/iGroupSize;
	QByteArray partialKey = iKey.mid(blockSize*iGroupIndex,blockSize*(iGroupIndex+1));
	
	return  KriptoUtils::encryptDataAsymmetric(partialKey,aa,encAlg);

	CMS_FUNC_END
}


/**
 * \brief
 * EnvelopedData objesi oluþturur..
 * 
 * \param iData
 * Þifrelenecek Veri.
 * 
 * \param iRecipients
 * Verinin kimlere göre þifreleneceðini gösteren yapý.
 * 
 * \param iContEncAlg
 * Content Encryption Algorithm.
 * 
 * \param iEncryptionKey
 * Simetric þifreleme anahtarý
 * 
 * \param oData
 * Þifrelenen datanýn kopyalanacaðý yapý.
 * 
 * \returns
 * baþarýlý olursa SUCCESS döner.
 * 
 * \throws <EException>
 * Verinin çözülmesi sýrasýnda bir istisna oluþursa bu istisnayý yukarýya atar.
 * 
 * .
 * 
 * \remarks
 *
 * 
 * \see
 *
 */
EnvelopedData::EnvelopedData(	const QByteArray &iData , 
								const QList<ECertificate> &iRecipients ,
								const AlgorithmIdentifier &iContEncAlg , 
								const QByteArray &iEncryptionKey  
							)
							:	mOriginatorInfoPresent(false),
								mVersion(CMS_CMSVersion::v0)
{
	CMS_FUNC_BEGIN

	EncryptedContentInfo eci( KriptoUtils::encryptDataSymmetric(iData,iEncryptionKey,iContEncAlg),
								iContEncAlg	);
	setEncryptedContentInfo( eci ) ;

	addRecipientInfos(EnvelopedData::buildRecipientInfos(iRecipients,iEncryptionKey));

	CMS_FUNC_END
}

bool EnvelopedData::buildEGRI(const ECertificate& iCert,const QByteArray iEncryptionKey,int iGID,int iGroupIndex,int iGroupSize,EsyaGroupRecipientInfo& oEGRI)
{
	AlgDetay ad = AlgoritmaBilgileri::getAlgDetay(iCert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm());

	if (ad.getAlgTipi() == AlgDetay::AT_ASIMETRIK_SIFRELEME)
	{
		IssuerAndSerialNumber isn(iCert);
		oEGRI.setRID(RecipientIdentifier(isn));
		oEGRI.setGID(iGID);
		oEGRI.setGroupIndex(iGroupIndex);
		oEGRI.setGroupSize(iGroupSize);
		oEGRI.setEncryptedKey(encryptGroupKey(iCert,iEncryptionKey,iGroupIndex,iGroupSize));
		oEGRI.setKeyEncAlg(iCert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm());

		return true;
	}
	else throw ECMSException(QString("EnvelopedData:buildEGRI(...) : Recipient Key-Enc-Alg invalid."));;
}

QList<RecipientInfo> EnvelopedData::buildEGRIs(const QList<ECertificate>& iGroupCertList,const QByteArray &iEncryptionKey, int iGID)
{
	QList<RecipientInfo> riList;
	bool built = false;
	int groupSize = iGroupCertList.size();
	for(int i = 0 ; i < iGroupCertList.size();i++)
	{
		EsyaGroupRecipientInfo egri;
		built = buildEGRI(iGroupCertList[i],iEncryptionKey,iGID,i,groupSize,egri); 
		if (!built)
			throw ECMSException(QString("EnvelopedData:buildEGRIs(...) : Group Recipient Info olusturulamadi."));
		OtherRecipientInfo ori(ESYA_id_esyagrri,egri.getEncodedBytes());
		RecipientInfo ri(ori);
		riList<<ri;
	}
	return riList;
}

bool EnvelopedData::buildRecipientInfo(const ECertificate &iRecipient , const QByteArray &iEncryptionKey , RecipientInfo& iRI )
{
	AlgDetay ad = AlgoritmaBilgileri::getAlgDetay(iRecipient.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm());

	if (ad.getAlgTipi() == AlgDetay::AT_ASIMETRIK_SIFRELEME)
	{
		IssuerAndSerialNumber isn(iRecipient);
		iRI.setKTRI( KeyTransRecipientInfo(  DEFAULT_RECIPIENT_VERSION, 
									RecipientIdentifier(isn),
									iRecipient.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm(), 
									encryptKey(iRecipient, iEncryptionKey)));

		return true;
	}
	else if (ad.getAlgTipi()== AlgDetay::AT_ANAHTAR_ANLASMASI)
	{
		ASN1T_CMS_CMSVersion version = CMS_CMSVersion::v3;
		KeyAgreeRecipientInfo kari; 
		bool kariBuilt = EnvelopedData::buildKARI(iRecipient,iEncryptionKey,kari);
		if (kariBuilt)
		{
			iRI.setKARI(kari);
			return true;
		}
	}

	return false;
}


QList<RecipientInfo> EnvelopedData::buildRecipientInfos(	const QList<ECertificate> &iRecipients ,
															const QByteArray &iEncryptionKey  
														)
{
	CMS_FUNC_BEGIN

	QList<RecipientInfo> riList;

	for (int i = 0 ; i<iRecipients.size();i++ )
	{
		RecipientInfo rInfo;
		bool riBuilt =  buildRecipientInfo(iRecipients[i],iEncryptionKey,rInfo);
		if (riBuilt)
			riList.append(rInfo);
	}

	CMS_FUNC_END
	return riList;
}



EnvelopedData::EnvelopedData(	const QByteArray &iData , 
								const QString &iParola,
								const QByteArray & iEncryptionKey,
								const AlgorithmIdentifier &iContEncAlg , 
								const AlgorithmIdentifier &iKeyEncAlg , 
								const AlgorithmIdentifier &iKeyDrvAlg    )
		:	mOriginatorInfoPresent(false),
			mVersion(CMS_CMSVersion::v0)
{

	CMS_FUNC_BEGIN

	EncryptedContentInfo eci( KriptoUtils::encryptDataSymmetric(iData,iEncryptionKey,iContEncAlg), iContEncAlg	);	

	setEncryptedContentInfo( eci ) ;
	
	ESYAPasswordRecipientInfo epwri(iKeyEncAlg,iKeyDrvAlg,iEncryptionKey,iParola);
	RecipientInfo rInfo( OtherRecipientInfo(  ESYA_id_esyapwri, epwri.getEncodedBytes())); 
	
	addRecipientInfo(rInfo);

	CMS_FUNC_END
}


/********************************************************************/

/**
* \brief
* Þifreli veride verilen sertifikaya sahip þifrecinin indeksini döner
* 
* \param iCert
* Þifrelemede kullanýlmýþ Sertifika
*
* \remarks
*
* 
* \see
*
*/
int EnvelopedData::getRecipientIndeks(const ECertificate & iCert)const 
{
	for ( int i = 0 ; i < mRecipientInfos.size(); i++ )
	{
		if ( mRecipientInfos[0].getType() == RecipientInfo::T_KTRI ) 
		{
			const KeyTransRecipientInfo & ktri = mRecipientInfos[i].getKTRI();
			const RecipientIdentifier & rID =  ktri.getRID();
			if (rID.getType() == T_IssuerAndSerial )
			{
				IssuerAndSerialNumber isn(iCert);
				if (rID.getIssuerAndSerialNumber() == isn)
					return i;
			}
			else if (rID.getType() == T_SubjectKeyIdentifier )
			{
				SubjectKeyIdentifier ski;
				int res = ExtensionGenerator::getSKIExtension( iCert.getTBSCertificate().getExtensions(),ski);
				if ((res == SUCCESS)&& ( rID.getSubjectKeyIdentifier() == ski.getEncodedBytes()))
					return i;
			}
		}
		else if ( mRecipientInfos[0].getType() == RecipientInfo::T_KARI)
		{
			const KeyAgreeRecipientInfo & kari = mRecipientInfos[i].getKARI();
			if (kari.getRecipientIndex(iCert)>=0)
				return i;
		}
	}
	
	return -1;
}

int EnvelopedData::getESYAPWRI(ESYAPasswordRecipientInfo &oEPWRI)const 
{
	for ( int i = 0 ; i < mRecipientInfos.size(); i++ )
	{
		if ( mRecipientInfos[i].getType() == RecipientInfo::T_ORI ) 
		{
			const OtherRecipientInfo & ori = mRecipientInfos[i].getORI();
			if (ori.getORIType() == (ASN1TObjId)ESYA_id_esyapwri )
			{
				oEPWRI.constructObject(ori.getORIValue());
				return i;
			}
		}
	}

	return FAILURE;
}

int EnvelopedData::getESYAFIRI(ESYAFileInfoRecipientInfo &oEFIRI)const 
{
	for ( int i = 0 ; i < mRecipientInfos.size(); i++ )
	{
		if ( mRecipientInfos[i].getType() == RecipientInfo::T_ORI ) 
		{
			const OtherRecipientInfo & ori = mRecipientInfos[i].getORI();
			if (ori.getORIType() == (ASN1TObjId)ESYA_id_esyafiri )
			{
				oEFIRI.constructObject(ori.getORIValue());
				return i;
			}
		}
	}

	return FAILURE;
}

void EnvelopedData::setESYAFIRI(const ESYAFileInfoRecipientInfo &iEFIRI) 
{
	Q_FOREACH(RecipientInfo ri,mRecipientInfos)
	{
		if ( ri.getType() == RecipientInfo::T_ORI ) 
		{
			const OtherRecipientInfo & ori = ri.getORI();
			if (ori.getORIType() == (ASN1TObjId)ESYA_id_esyafiri )
			{
				mRecipientInfos.removeAll(ri);			
			}
		}
	}

	addRecipientInfo(RecipientInfo(OtherRecipientInfo(ESYA_id_esyafiri,iEFIRI.getEncodedBytes())));
	return ;
}



QByteArray EnvelopedData::decryptData(const QByteArray  & iKey) const
{
	CMS_FUNC_BEGIN
	
	QByteArray plainData	= mEncryptedContentInfo.decrypt(iKey);
	return plainData;

	CMS_FUNC_END
}

QByteArray EnvelopedData::decryptData(const QString & iParola) const
{
	CMS_FUNC_BEGIN

	QByteArray key			= decryptKey(iParola);	
	QByteArray plainData	= decryptData(key);
	return plainData;

	CMS_FUNC_END
}

QByteArray EnvelopedData::decryptKey(const QString &iParola) const 
{
	ESYAPasswordRecipientInfo epwri;
	
	if ( getESYAPWRI(epwri) == FAILURE )
	{
		throw ECMSException(QString("EnvelopedData:decryptKey(const QString &iParola) : Dosya Parola tabanlý þifreli deðil."));
	}

	return epwri.decryptKey(iParola);

}


/**
* \brief
* Þifreli veriden þifresiz veriyi çýkarýr
* 
* \param iCert
* Þifrelemede kullanýlmýþ Sertifika
*
* \param iAB
* Þifre çözmede kullanýlacak Anahtar Bilgisi yapýsý
*
* 
* \see
*
*/
QByteArray EnvelopedData::decryptData(const ECertificate & iCert , const OzelAnahtarBilgisi& iAB) const
{
	CMS_FUNC_BEGIN

	QByteArray key			= decryptKey(iCert,iAB);	
	QByteArray plainData	= decryptData(key);
	return plainData;

	CMS_FUNC_END
}

/**
* \brief
* Þifreli veriden simetrik þifreleme anahtarýný çýkarýr
* 
* \param iCert
* Þifrelemede kullanýlmýþ Sertifika
*
* \param iAB
* Þifre çözmede kullanýlacak Anahtar Bilgisi yapýsý
*
* 
* \see
*
*/
QByteArray EnvelopedData::decryptKey(const ECertificate & iCert , const OzelAnahtarBilgisi& iAB) const
{
	CMS_FUNC_BEGIN

	int indeks = getRecipientIndeks(iCert);
	QByteArray encryptedKey , symmetricKey;
	QByteArray key;

	if (indeks < 0)
	{
		throw ECMSException("Þifreli Veri bu sertifika için þifrelenmemiþ.");
	}
	if ( mRecipientInfos[indeks].getType() == RecipientInfo::T_KTRI)
	{
		key = mRecipientInfos[indeks].getKTRI().decryptKey(iAB);
	}
	else if (mRecipientInfos[indeks].getType() == RecipientInfo::T_KARI)
	{
		key = mRecipientInfos[indeks].getKARI().decryptKey(iCert,iAB);
	}
	else 
	{
		throw ECMSException("Desteklenmeyen Þifreci Tipi");
	}

	CMS_FUNC_END
	return key;
}


QByteArray  EnvelopedData::decryptData(const QList< QPair<ECertificate,OzelAnahtarBilgisi> > & iCertKeyList ) const
{
	QByteArray key			= decryptKey(iCertKeyList);	
	QByteArray plainData	= decryptData(key);
	return plainData;
}

QByteArray  EnvelopedData::decryptKey(const QList< QPair<ECertificate,OzelAnahtarBilgisi> > & iCertKeyList ) const
{
	QByteArray key;
	QHash<int,QByteArray> partialKeys;
	for (int i= 0 ;i< mRecipientInfos.size();i++)
	{
		const RecipientInfo& ri = mRecipientInfos[i];
		if ( ri.getType() != RecipientInfo::T_ORI || ri.getORI().getORIType() != (ASN1TObjId)ESYA_id_esyagrri )
			throw ECMSException("Gecersiz alici tipi");
	
		EsyaGroupRecipientInfo gri(ri.getORI().getORIValue());
		
		QByteArray key = gri.decryptKey(iCertKeyList);
		if (key.isEmpty())
			throw ECMSException("Gecersiz alici tipi");
		
		partialKeys[gri.getGroupIndex()] = key;
	}
	if (partialKeys.size()!= mRecipientInfos.size())
		throw ECMSException("Gecersiz alici tipi");
	for (int i= 0; i<partialKeys.size();i++)
	{
		if (partialKeys[i].isEmpty())
			throw ECMSException("Eksik kýsmi anahtar ");
		key += partialKeys[i];
	}

	return key;
}




void EnvelopedData::addRecipients( const ECertificate &iDecryptCert, const OzelAnahtarBilgisi & iOAB,const QList<ECertificate> & iCertList )
{
	CMS_FUNC_BEGIN

	QByteArray key = decryptKey(iDecryptCert,iOAB);

	for (int i = 0 ; i < iCertList.size(); i++  )
	{
		RecipientInfo rInfo;
		if (EnvelopedData::buildRecipientInfo(iCertList[i],key,rInfo))
			addRecipientInfo(rInfo);
	}
	CMS_FUNC_END
}

void EnvelopedData::setRecipients( const ECertificate &iDecryptCert, const OzelAnahtarBilgisi & iOAB,const QList<ECertificate> & iCertList )
{
	CMS_FUNC_BEGIN

	QByteArray key = decryptKey(iDecryptCert,iOAB);

	mRecipientInfos.clear();

	for (int i = 0 ; i < iCertList.size(); i++  )
	{
		RecipientInfo rInfo;
		if (EnvelopedData::buildRecipientInfo(iCertList[i],key,rInfo))
			addRecipientInfo(rInfo);
	}
	CMS_FUNC_END
}

void EnvelopedData::addRecipient( const ECertificate &iDecryptCert,const OzelAnahtarBilgisi & iOAB,  const ECertificate & iCert )
{
	CMS_FUNC_BEGIN

	QByteArray key = decryptKey(iDecryptCert,iOAB);

	RecipientInfo rInfo;
	bool riBuilt = EnvelopedData::buildRecipientInfo(iCert,key,rInfo);
	if (riBuilt)
		addRecipientInfo(rInfo);

	CMS_FUNC_END
}

void EnvelopedData::removeRecipients( const QList<ECertificate> &iRecipientCerts)
{
	CMS_FUNC_BEGIN

	Q_FOREACH(ECertificate cert , iRecipientCerts)
	{
		removeRecipient(cert);
	}

	CMS_FUNC_END
}

void EnvelopedData::removeRecipient( const ECertificate &iRecipientCert)
{
	CMS_FUNC_BEGIN

	int i = getRecipientIndeks(iRecipientCert);
	if (i>=0)
	{
		mRecipientInfos.removeAt(i);
	}
	CMS_FUNC_END
}

/**
* \brief
* Þifreli veriden simetrik þifreleme anahtarýný çýkarýr
* 
* \param iCert
* Þifrelemede kullanýlmýþ Sertifika
*
* \param iAB
* Þifre çözmede kullanýlacak Anahtar Bilgisi yapýsý
*
* 
* \see
*
*/
bool  EnvelopedData::buildKARI(const ECertificate& iCert,const QByteArray& iCEK,KeyAgreeRecipientInfo& iKARI)
{
	iKARI.setVersion(DEFAULT_KARI_VERSION);
	
	AlgorithmIdentifier keAlg(DEFAULT_KEYWRAPALG.getEncodedBytes(),DEFAULT_KEYAGREEMENTALG.getAlgorithm());
	iKARI.setKeyEncryptionAlgorithm(keAlg);	
	
	AlgDetay ad = AlgoritmaBilgileri::getAlgDetay(DEFAULT_KEYWRAPALG);
	int ukmLength = ad.getSimetrikAlgInfo().getAnahtarBoyu();

	if (ad.getSimetrikAlgInfo().getAlgAdi() != ST_AES)
	{
		// Þimdilik sadece aes wrap destekleniyor
		throw ECMSException("Desteklenmeyen Key Wrap Algoritmasý");
	}

	QByteArray ukm;// = KriptoUtils::ivUret(ukmLength); Bouncy Castle uyumluluðu için ukm koymuyoruz þimdilik

	AcikAnahtarBilgisi rcPubKey(iCert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes());	
	
	ECCCMSSharedInfo si = iKARI.cmsSharedInfoOlustur(ukm,DEFAULT_KEYWRAPALG);
	
	ECDHKeyAgreement ka(rcPubKey);	
	
	QByteArray epPub,epPriv;

	ka.generateKeyPair(epPub,epPriv);
	OzelAnahtarBilgisi oa(epPriv);

	QByteArray kek = ka.agree(epPriv,keAlg,ad.getSimetrikAlgInfo().getAnahtarBoyu(),si.getEncodedBytes());


	EKeyWrap kw(DEFAULT_KEYWRAPALG,kek);
	QByteArray encryptedKey = 	kw.wrapKey(iCEK); 
	
	RecipientEncryptedKey rek(KeyAgreeRecipientIdentifier(IssuerAndSerialNumber(iCert)),encryptedKey);
	RecipientEncryptedKeys reks;
	reks.addREK(rek);

	//iKARI.setUKM(ukm);

	OriginatorPublicKey opk(epPub);
	opk.getAlgorithm().setParameters(QByteArray(NULL_PARAMS));
	OriginatorIdentifierOrKey orik;
	orik.setOriginatorKey(opk);
	iKARI.setOriginator(orik);
	iKARI.setRecipientEncryptedKeys(reks);

	return true;	
}
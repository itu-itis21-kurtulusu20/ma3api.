#include "ESYAPasswordRecipientInfo.h"
#include "KriptoUtils.h"
#include "PKCS5PBE2.h"
#include "PBES2_Params.h"
#include "ECMSException.h"

using namespace esya;

const ASN1T_CMS_CMSVersion ESYAPasswordRecipientInfo::DEFAULT_VERSION = CMS_CMSVersion::v0;
const AlgorithmIdentifier ESYAPasswordRecipientInfo::DEFAULT_DIGEST_ALGORITHM = AlgorithmIdentifier(ALGOS_sha_1);
																									 


ESYAPasswordRecipientInfo::ESYAPasswordRecipientInfo(void)
{
}

ESYAPasswordRecipientInfo::ESYAPasswordRecipientInfo(const QByteArray & iEPWRI)
{
	constructObject(iEPWRI);
}

ESYAPasswordRecipientInfo::ESYAPasswordRecipientInfo(const ASN1T_ESYA_ESYAPasswordRecipientInfo & iEPWRI)
{
	copyFromASNObject(iEPWRI);
}


ESYAPasswordRecipientInfo::ESYAPasswordRecipientInfo(const AlgorithmIdentifier& iKeyEncAlg , const AlgorithmIdentifier iKeyDrvAlg , const QByteArray iKey , const QString &iParola )
:	mVersion(DEFAULT_VERSION),
	mKeyDerivationAlgorithm(iKeyDrvAlg),
	mKeyDerivationAlgorithmPresent(true),
	mKeyEncryptionAlgorithm(iKeyEncAlg)
{
	mKeyHash.setDigest( KriptoUtils::calculateDigest(iKey,DEFAULT_DIGEST_ALGORITHM) );		
	mKeyHash.setDigestAlgorithm(DEFAULT_DIGEST_ALGORITHM);
	mEncryptedKey	= encryptKey(iParola,iKey);
}

ESYAPasswordRecipientInfo::ESYAPasswordRecipientInfo(const ESYAPasswordRecipientInfo& iEPWRI)
:	mVersion(iEPWRI.getVersion()),
	mKeyDerivationAlgorithm(iEPWRI.getKeyDrvAlg()),
	mKeyDerivationAlgorithmPresent(iEPWRI.isKeyDrvAlgPresent()),
	mKeyEncryptionAlgorithm(iEPWRI.getKeyEncAlg()),
	mEncryptedKey(iEPWRI.getEncryptedKey()),
	mKeyHash(iEPWRI.getKeyHash())
{
}

ESYAPasswordRecipientInfo & ESYAPasswordRecipientInfo::operator=(const ESYAPasswordRecipientInfo & iEPWRI)
{
	mVersion						= iEPWRI.getVersion();
	mKeyDerivationAlgorithm			= iEPWRI.getKeyDrvAlg();
	mKeyDerivationAlgorithmPresent	= iEPWRI.isKeyDrvAlgPresent();
	mKeyEncryptionAlgorithm			= iEPWRI.getKeyEncAlg();
	mEncryptedKey					= iEPWRI.getEncryptedKey();
	mKeyHash						= iEPWRI.getKeyHash();
	return * this;
}

bool esya::operator==(const ESYAPasswordRecipientInfo & iRHS , const ESYAPasswordRecipientInfo & iLHS )
{
	if ( iRHS.isKeyDrvAlgPresent() != iLHS.isKeyDrvAlgPresent() ) return false;

	if (	( iRHS.isKeyDrvAlgPresent()) && 
			( iRHS.getKeyDrvAlg() != iLHS.getKeyDrvAlg()))
		return false;

	return (	( iRHS.getVersion()			== iLHS.getVersion()		) &&	
				( iRHS.getKeyEncAlg()		== iLHS.getKeyEncAlg()		) &&
				( iRHS.getKeyHash()			== iLHS.getKeyHash()		) &&				
				( iRHS.getEncryptedKey()	== iLHS.getEncryptedKey()	)	);
}

bool esya::operator!=(const ESYAPasswordRecipientInfo & iRHS,const ESYAPasswordRecipientInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}	

int ESYAPasswordRecipientInfo::copyFromASNObject(const ASN1T_ESYA_ESYAPasswordRecipientInfo & iEPWRI)
{
	mVersion = iEPWRI.version;
	mKeyDerivationAlgorithm.copyFromASNObject(iEPWRI.keyDerivationAlgorithm);
	mKeyEncryptionAlgorithm.copyFromASNObject(iEPWRI.keyEncryptionAlgorithm);
	mKeyHash.copyFromASNObject(iEPWRI.keyHash);	
	mEncryptedKey = toByteArray(iEPWRI.encryptedKey);
	return SUCCESS;
}

int ESYAPasswordRecipientInfo::constructObject(const QByteArray & iPWRI)
{
	ASN1BERDecodeBuffer decBuf((ASN1OCTET*)iPWRI.data(),iPWRI.size());
	ASN1T_ESYA_ESYAPasswordRecipientInfo pwri;
	ASN1C_ESYA_ESYAPasswordRecipientInfo cPWRI(decBuf,pwri);
	int stat = cPWRI.Decode();
	if ( stat!= ASN_OK )
	{
		throw EException(QString("PWRI Decode edilemedi Hata : ").arg(stat),__FILE__,__LINE__);
	}

	copyFromASNObject(pwri);

	return SUCCESS;	
}

int ESYAPasswordRecipientInfo::copyToASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo& oEPWRI)const
{
	oEPWRI.m.keyDerivationAlgorithmPresent =  mKeyDerivationAlgorithmPresent;
	if (oEPWRI.m.keyDerivationAlgorithmPresent)
		mKeyDerivationAlgorithm.copyToASNObject(oEPWRI.keyDerivationAlgorithm);
	
	mKeyEncryptionAlgorithm.copyToASNObject(oEPWRI.keyEncryptionAlgorithm);
	mKeyHash.copyToASNObject(oEPWRI.keyHash);
	oEPWRI.version				= mVersion;
	oEPWRI.encryptedKey.data	= (OSOCTET*)myStrDup(mEncryptedKey.data() , mEncryptedKey.size());
	oEPWRI.encryptedKey.numocts	= mEncryptedKey.size();
	return SUCCESS;
}

ASN1T_ESYA_ESYAPasswordRecipientInfo* ESYAPasswordRecipientInfo::getASNCopy()const
{
	return getASNCopyOf(*this);
}

ASN1T_ESYA_ESYAPasswordRecipientInfo * ESYAPasswordRecipientInfo::getASNCopyOf(const ESYAPasswordRecipientInfo & iEPWRI)
{
	ASN1T_ESYA_ESYAPasswordRecipientInfo * pPWRI = new ASN1T_ESYA_ESYAPasswordRecipientInfo(); 
	iEPWRI.copyToASNObject(*pPWRI);
	return pPWRI;
}

void ESYAPasswordRecipientInfo::freeASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo * oEPWRI)
{
	freeASNObject(*oEPWRI);
	DELETE_MEMORY(oEPWRI)
}

void ESYAPasswordRecipientInfo::freeASNObject(ASN1T_ESYA_ESYAPasswordRecipientInfo & oEPWRI)
{
	if (oEPWRI.m.keyDerivationAlgorithmPresent)
		AlgorithmIdentifier().freeASNObject(oEPWRI.keyDerivationAlgorithm);

	AlgorithmIdentifier().freeASNObject(oEPWRI.keyEncryptionAlgorithm);
	DigestInfo().freeASNObject(oEPWRI.keyHash);

	DELETE_MEMORY_ARRAY(oEPWRI.encryptedKey.data)
}

const bool ESYAPasswordRecipientInfo::isKeyDrvAlgPresent() const
{
	return mKeyDerivationAlgorithmPresent;
}

const QByteArray & ESYAPasswordRecipientInfo::getEncryptedKey()	const
{
	return mEncryptedKey;
}

const AlgorithmIdentifier	& ESYAPasswordRecipientInfo::getKeyEncAlg()	const
{
	return mKeyEncryptionAlgorithm;
}

const AlgorithmIdentifier	& ESYAPasswordRecipientInfo::getKeyDrvAlg()	const
{
	return mKeyDerivationAlgorithm;
}

const int	& ESYAPasswordRecipientInfo::getVersion() const
{
	return mVersion;
}


const DigestInfo & ESYAPasswordRecipientInfo::getKeyHash() const
{
	return mKeyHash;
}


void ESYAPasswordRecipientInfo::setKeyDrvAlgPresent(const bool  iKDAP)
{
	mKeyDerivationAlgorithmPresent = iKDAP;
}

void ESYAPasswordRecipientInfo::setVersion(int iVersion)
{
	mVersion = iVersion;
}

void ESYAPasswordRecipientInfo::setKeyEncAlg( const AlgorithmIdentifier & iKeyEncAlg )
{
	mKeyEncryptionAlgorithm = iKeyEncAlg;
}

void ESYAPasswordRecipientInfo::setKeyDrvAlg( const AlgorithmIdentifier & iKeyDrvAlg )
{
	setKeyDrvAlgPresent(true);
	mKeyDerivationAlgorithm = iKeyDrvAlg;
}

void ESYAPasswordRecipientInfo::setEncryptedKey(const QByteArray& iEncryptedKey)
{
	mEncryptedKey = iEncryptedKey;
}


void ESYAPasswordRecipientInfo::setKeyHash(const DigestInfo& iKeyHash)
{
	mKeyHash = iKeyHash;
}


QByteArray ESYAPasswordRecipientInfo::getEncodedBytes()const
{
	ASN1BEREncodeBuffer encBuf;
	ASN1T_ESYA_ESYAPasswordRecipientInfo * pPWRI = getASNCopy();
	ASN1C_ESYA_ESYAPasswordRecipientInfo	cPWRI(*pPWRI);

	int  stat = cPWRI.EncodeTo(encBuf);
	if ( stat< ASN_OK )
	{
		throw EException(QString("ESYAPWRI Encode edilemedi Hata : ").arg(stat),__FILE__,__LINE__);
	}

	freeASNObject(pPWRI);
	return QByteArray((char*) encBuf.getMsgPtr(),stat);
}


QByteArray ESYAPasswordRecipientInfo::decryptKey(const QString  & iParola) const
{
	try
	{
		PBES2_Params pbesParams( mKeyDerivationAlgorithm , mKeyEncryptionAlgorithm );
		AlgorithmIdentifier algID (pbesParams.getEncodedBytes(),(ASN1TObjId)PKCS5_id_PBES2 );

		PKCS5PBE2 pkcs5(algID,iParola,SIFRECOZME);
		pkcs5.besle(mEncryptedKey);
		pkcs5.bitir();

		QByteArray key = pkcs5.bosalt();

		QByteArray keyHash = KriptoUtils::calculateDigest(key,mKeyHash.getDigestAlgorithm());

		if (keyHash != mKeyHash.getDigest() )
			throw ECMSException("Parola Yanlýþ",ECMSException::CHT_HataliParola);
		return key;
	}
	catch (ECMSException&  exc)
	{
		throw exc;
	}
	catch (EException&  exc)
	{
		throw exc.append("ESYAPasswordRecipientInfo::decryptKey() ");
	}

}

QByteArray ESYAPasswordRecipientInfo::encryptKey(const QString &iParola,const QByteArray& iKey)const
{
	PBES2_Params pbesParams( mKeyDerivationAlgorithm , mKeyEncryptionAlgorithm );
	AlgorithmIdentifier algID ( pbesParams.getEncodedBytes(), (ASN1TObjId)PKCS5_id_PBES2 );

	PKCS5PBE2 pkcs5(algID,iParola);
	pkcs5.besle(iKey);
	pkcs5.bitir();

	return pkcs5.bosalt();
}





ESYAPasswordRecipientInfo::~ESYAPasswordRecipientInfo(void)
{
}

#include "KeyAgreeRecipientInfo.h"

#include "AlgoritmaBilgileri.h"
#include "ECMSException.h"
#include "ECDHKeyAgreement.h"
#include "EKeyWrap.h"

using namespace esya;

KeyAgreeRecipientInfo::KeyAgreeRecipientInfo(const KeyAgreeRecipientInfo &iKARI)
:	mVersion(iKARI.getVersion()),
	mUKMPresent(iKARI.isUKMPresent()),
	mOriginator(iKARI.getOriginator()),
	mUKM(iKARI.getUKM()),
	mKeyEncryptionAlgorithm(iKARI.getKeyEncryptionAlgorithm()),
	mRecipientEncryptedKeys(iKARI.getRecipientEncryptedKeys())
{
}

KeyAgreeRecipientInfo::KeyAgreeRecipientInfo(const ASN1T_CMS_KeyAgreeRecipientInfo & iKARI)
{
	copyFromASNObject(iKARI);
}

KeyAgreeRecipientInfo::KeyAgreeRecipientInfo(const QByteArray & iKARI)
{
	constructObject(iKARI);
}

KeyAgreeRecipientInfo::KeyAgreeRecipientInfo(void)
:	mUKMPresent(false)
{
}

KeyAgreeRecipientInfo & KeyAgreeRecipientInfo::operator=(const KeyAgreeRecipientInfo&iKARI)
{
	mVersion				= iKARI.getVersion();
	mUKMPresent				= iKARI.isUKMPresent();
	mUKM					= iKARI.getUKM();
	mOriginator				= iKARI.getOriginator();
	mKeyEncryptionAlgorithm = iKARI.getKeyEncryptionAlgorithm();
	mRecipientEncryptedKeys = iKARI.getRecipientEncryptedKeys();
	return (*this);
}

bool esya::operator==(const KeyAgreeRecipientInfo & iRHS, const KeyAgreeRecipientInfo & iLHS)
{
	if (iRHS.isUKMPresent() != iLHS.isUKMPresent()) 
		return false;
	if (iRHS.isUKMPresent() && (iRHS.getUKM() != iLHS.getUKM()))
		return false;

	return ( ( iRHS.getVersion()				== iLHS.getVersion() )					&&
			 ( iRHS.getOriginator()				== iLHS.getOriginator() )				&&
			 ( iRHS.getKeyEncryptionAlgorithm() == iLHS.getKeyEncryptionAlgorithm() )	&&
			 ( iRHS.getRecipientEncryptedKeys() == iRHS.getRecipientEncryptedKeys() )		);
}

bool esya::operator!=(const KeyAgreeRecipientInfo & iRHS, const KeyAgreeRecipientInfo& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int KeyAgreeRecipientInfo::copyFromASNObject(const ASN1T_CMS_KeyAgreeRecipientInfo & iKARI)
{
	mVersion = iKARI.version;
	mOriginator.copyFromASNObject(iKARI.originator);
	mRecipientEncryptedKeys.copyFromASNObject(iKARI.recipientEncryptedKeys);
	mKeyEncryptionAlgorithm.copyFromASNObject(iKARI.keyEncryptionAlgorithm);
	mUKMPresent = iKARI.m.ukmPresent;
	if ( mUKMPresent )
	{
		mUKM = QByteArray((const char*)iKARI.ukm.data,iKARI.ukm.numocts);
	}

	return SUCCESS;
}

int KeyAgreeRecipientInfo::copyToASNObject(ASN1T_CMS_KeyAgreeRecipientInfo & oKARI)  const
{
	oKARI.version = mVersion;
	mOriginator.copyToASNObject(oKARI.originator);
	mRecipientEncryptedKeys.copyToASNObject(oKARI.recipientEncryptedKeys);
	mKeyEncryptionAlgorithm.copyToASNObject(oKARI.keyEncryptionAlgorithm);
	oKARI.m.ukmPresent = mUKMPresent;
	if ( mUKMPresent )
	{
		oKARI.ukm.numocts	= mUKM.size();
		oKARI.ukm.data		= (ASN1OCTET*)myStrDup(mUKM.data(),mUKM.size());
	}

	return SUCCESS;
}

void KeyAgreeRecipientInfo::freeASNObject(ASN1T_CMS_KeyAgreeRecipientInfo & oKARI)const
{
	OriginatorIdentifierOrKey().freeASNObject(oKARI.originator);
	KeyEncryptionAlgorithmIdentifier().freeASNObject(oKARI.keyEncryptionAlgorithm);
	RecipientEncryptedKeys().freeASNObject(oKARI.recipientEncryptedKeys);
	if (oKARI.m.ukmPresent)
	{
		DELETE_MEMORY_ARRAY(oKARI.ukm.data);
	}
}

const bool &KeyAgreeRecipientInfo::isUKMPresent() const
{
	return mUKMPresent;
}

int	KeyAgreeRecipientInfo::getVersion()const
{
	return mVersion;
}

const QByteArray & KeyAgreeRecipientInfo::getUKM()const
{
	return mUKM;
}

const OriginatorIdentifierOrKey & KeyAgreeRecipientInfo::getOriginator()const
{
	return mOriginator;
}

const KeyEncryptionAlgorithmIdentifier & KeyAgreeRecipientInfo::getKeyEncryptionAlgorithm()const
{
	return mKeyEncryptionAlgorithm;
}

const RecipientEncryptedKeys & KeyAgreeRecipientInfo::getRecipientEncryptedKeys()const
{
	return mRecipientEncryptedKeys;
}

void KeyAgreeRecipientInfo::setVersion(int 	iVersion)
{
	mVersion = iVersion;
}

void KeyAgreeRecipientInfo::setUKM(const QByteArray & iUKM )
{
	mUKMPresent = true;
	mUKM = iUKM;
}

void KeyAgreeRecipientInfo::setOriginator(const OriginatorIdentifierOrKey & iOK)
{
	mOriginator = iOK;
}

void KeyAgreeRecipientInfo::setKeyEncryptionAlgorithm(const KeyEncryptionAlgorithmIdentifier &	iKEAlg)
{
	mKeyEncryptionAlgorithm = iKEAlg;
}

void KeyAgreeRecipientInfo::setRecipientEncryptedKeys(const RecipientEncryptedKeys & iREKs)
{
	mRecipientEncryptedKeys = iREKs;
}


QString KeyAgreeRecipientInfo::toString()const
{
	return "KEYAGREERECIPIENTINFO";
}


int KeyAgreeRecipientInfo::getRecipientIndex(const ECertificate & iRecipient) const
{
	for(int i = 0; i < mRecipientEncryptedKeys.getList().size();i++)
	{
		const KeyAgreeRecipientIdentifier & karid = mRecipientEncryptedKeys.getList()[i].getRID(); 
		if (karid.isEqual(iRecipient))
			return i;
	}
	return -1;
}

const QByteArray& KeyAgreeRecipientInfo::getEncryptedKey(const ECertificate & iRecipient)const
{
	int index = getRecipientIndex(iRecipient);
	if ( index >=0 )
		return mRecipientEncryptedKeys.getList()[index].getEncryptedKey();

	return QByteArray();
}

KeyAgreeRecipientInfo::~KeyAgreeRecipientInfo(void)
{
}


QByteArray _keyLenToByteArray(int iKeyLen)
{
	QByteArray bytes(4,0);

	char b = 0;

	for(int i = 3 ; i>0 && iKeyLen>0 ; i--)
	{
		b = iKeyLen%256;
		bytes[i] = b;
		iKeyLen/=256; 
	}

	return bytes;
}

ECCCMSSharedInfo KeyAgreeRecipientInfo::cmsSharedInfoOlustur(const QByteArray & iUKM, const AlgorithmIdentifier & iKEAlg)const
{
	AlgDetay ad = AlgoritmaBilgileri::getAlgDetay(iKEAlg);

	int keylen = ad.getSimetrikAlgInfo().getAnahtarBoyu();

	ECCCMSSharedInfo si;
	si.setEntityUInfo(iUKM);

	byte* bytes = (byte*)&keylen ;
	QByteArray bKeyLen((char*)bytes,4); 

	si.setSuppPubInfo(_keyLenToByteArray(keylen*8));// todo: burasý düzeltilecek
	si.setKeyInfo(iKEAlg);
	return si;
}


QByteArray KeyAgreeRecipientInfo::decryptKey(const ECertificate& iCert, const OzelAnahtarBilgisi& iAB)const
{
	QByteArray encryptedKey = getEncryptedKey(iCert);

	AlgorithmIdentifier keyWrapAlg(AlgorithmIdentifier(mKeyEncryptionAlgorithm.getParameters()));
	AlgDetay ad = AlgoritmaBilgileri::getAlgDetay(keyWrapAlg);

	int keylen = ad.getSimetrikAlgInfo().getAnahtarBoyu();

	keyWrapAlg.setParameters(QByteArray(NULL_PARAMS));
	ECCCMSSharedInfo si	= cmsSharedInfoOlustur(mUKM, keyWrapAlg);

	if (mOriginator.getType() != OriginatorIdentifierOrKey::OKType_OriginatorKey)
		throw ECMSException("Desteklenmeyen OriginatorInfo tipi");

	SubjectPublicKeyInfo oriPubKeyInfo = mOriginator.getOriginatorKey();
	oriPubKeyInfo.getAlgorithm().setParameters(iCert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm().getParameters());
	AcikAnahtarBilgisi oriPubKey(oriPubKeyInfo.getEncodedBytes());

	AlgorithmIdentifier ecAlg(mOriginator.getOriginatorKey().getAlgorithm());
	AlgorithmIdentifier kdfAlg(mKeyEncryptionAlgorithm);

	ECDHKeyAgreement ka(oriPubKey);//(kdfAlg,keylen , si.getEncodedBytes(),AcikAnahtarBilgisi(oriPubKey),iAB);

	QByteArray sharedSecret = ka.agree(iAB,kdfAlg,keylen,si.getEncodedBytes());//ka.getSharedSecretKey(); // kripto katmanýna gidecez.

	// key encryptedkeyi sharedSecretle unwrap ederek hesaplýycaz.
	EKeyWrap kw(keyWrapAlg,sharedSecret);
	QByteArray key = kw.unwrapKey(encryptedKey);

	return key;
}

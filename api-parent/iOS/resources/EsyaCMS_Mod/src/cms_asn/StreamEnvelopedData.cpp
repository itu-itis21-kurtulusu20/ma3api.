#include "StreamEnvelopedData.h"
#include "TAlg_Paralel_Adaptor.h"
#include "Besleyici.h"
#include "myasndefs.h"
#include "cmslibrary_global.h"
#include "SifreCozucu.h"
#include "Sifreleyici.h"
#include "ESynchroniseManager.h"
#include "ECMSException.h"

#include "GroupKeyDecryptor.h"
#include "PasswordBasedKeyDecryptor.h"
#include "AsymetricKeyDecryptor.h"

#include "KeyDecryptor.h"

#include "rtxBuffer.h"

using namespace esya;


StreamEnvelopedData::StreamEnvelopedData(const QByteArray & iContEncKey, const AlgorithmIdentifier& iContEncAlg )
:	QObject(0),
	EnvelopedData(), 
	pIN(NULL),
	mEocCount(0),
	mAB(AnahtarBilgisi(OzelAnahtarBilgisi(),AcikAnahtarBilgisi())),
	mEncryptionKey(iContEncKey),
	mTamamlandi(0),
	mpKeyDecryptor(NULL)
{
	CMS_FUNC_BEGIN

	mEncryptedContentInfo.setContentEncAlg(iContEncAlg);
	mEncryptedContentInfo.setContentType(CMS_id_encryptedData);

	CMS_FUNC_END
}

StreamEnvelopedData::StreamEnvelopedData(const QByteArray & iContEncKey, const AlgorithmIdentifier& iContEncAlg ,const QString& iParola)
:	QObject(0),
	EnvelopedData(), 
	pIN(NULL),
	mEocCount(0),
	mAB(AnahtarBilgisi(OzelAnahtarBilgisi(),AcikAnahtarBilgisi())),
	mEncryptionKey(iContEncKey),
	mTamamlandi(0),
	mType(EDT_PASSWORDBASED),
	mParola(iParola),
	mpKeyDecryptor(NULL)
{
	CMS_FUNC_BEGIN

	mEncryptedContentInfo.setContentEncAlg(iContEncAlg);
	mEncryptedContentInfo.setContentType(CMS_id_encryptedData);

	CMS_FUNC_END
}

void StreamEnvelopedData::decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const QList< QPair<ECertificate,OzelAnahtarBilgisi> >  &iCertKeyList)
{
	CMS_FUNC_BEGIN

	pIN			= iEnvDataStream; 
	mPDStream	= oPDStream;
	mpKeyDecryptor =  new GroupKeyDecryptor(this,iCertKeyList);



	if ( !pIN )
	{
		throw EException("Kaynak stream NULL olamaz.");
	}
	else
	{
		_readContentInfo();
	}

	CMS_FUNC_END
}


void StreamEnvelopedData::decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const QString &iParola )
{
	CMS_FUNC_BEGIN

	pIN			= iEnvDataStream; 
	mPDStream	= oPDStream;
	mpKeyDecryptor =  new PasswordBasedKeyDecryptor(this,iParola);			


	if ( !pIN )
	{
		throw EException("Kaynak stream NULL olamaz.");
	}
	else
	{
		_readContentInfo();
	}

	CMS_FUNC_END
}

void StreamEnvelopedData::decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const ECertificate &iCert, 
											const AnahtarBilgisi& iAB )
{
	CMS_FUNC_BEGIN

	pIN			= iEnvDataStream; 
	mPDStream	= oPDStream;
	mpKeyDecryptor =  new AsymetricKeyDecryptor(this,iCert,iAB.getOzelAnahtar());			
	
	if ( !pIN )
	{
		throw EException("Kaynak stream NULL olamaz.");
	}
	else
	{
		_readContentInfo();
	}

	CMS_FUNC_END
}




/**
 * \brief
 * EnvelopedData sýnýfý constructor ý.
 * 
 * \param iEnvDataFileName
 * Enveloped Data Dosyasýnýn adresi.
 * 
 * \param iEncDataFileName
 * Eðer bir dosya adý belirtilirse yapý içindeki simetrik þifreli veri bu dosyaya yazýlýr.
 * burasý boþ string simetrik þifreli veri dosyasý oluþturulmaz.
 * 
 * Þifreli ya da açýk bir dosya için EnvelopedData objesi oluþturur. Þifreli dosya 
 * ise ASN1 yapýlarýný çözümleyerek uygun alanlarýn atanmasýný saðlar.
 * 
 */
StreamEnvelopedData::StreamEnvelopedData(EASNFileInputStream* iEnvDataStream,QIODevice *oPlainDataStream,const ECertificate &iCert, const AnahtarBilgisi& iAB)
:	QObject(0),
	mEocCount(0) ,
	mTamamlandi(0),
	mAB(iAB),
	mEncDataStream(NULL),
	mType(EDT_ASYMMETRIC)
{
	CMS_FUNC_BEGIN

	decryptStream(iEnvDataStream,oPlainDataStream,iCert,iAB);

	CMS_FUNC_END
}


/**
*
*
*/
StreamEnvelopedData::StreamEnvelopedData(EASNFileInputStream* iEnvDataStream)
:	QObject(0),
	mEocCount(0) ,
	mTamamlandi(0),
	mAB(AnahtarBilgisi(OzelAnahtarBilgisi(),AcikAnahtarBilgisi())),
	mType(EDT_ASYMMETRIC),
	mPDStream(NULL),
	mEncDataStream(NULL),
	pIN(iEnvDataStream)
{
	CMS_FUNC_BEGIN

	if ( !pIN )
	{
		throw EException("Kaynak stream NULL olamaz.");
	}
	else
	{
		_readHeader();
		mEncDataByteIndex = pIN->byteIndex();
		pIN->close();
	}

	CMS_FUNC_END
}



/**
* \brief
* StreamEnvelopedData sýnýfý constructor ý.
* 
* \param iEnvDataFileName
* Enveloped Data Dosyasýnýn adresi.
* 
* \param iEncDataFileName
* Eðer bir dosya adý belirtilirse yapý içindeki simetrik þifreli veri bu dosyaya yazýlýr.
* burasý boþ string simetrik þifreli veri dosyasý oluþturulmaz.
* 
* Þifreli ya da açýk bir dosya için EnvelopedData objesi oluþturur. Þifreli dosya 
* ise ASN1 yapýlarýný çözümleyerek uygun alanlarýn atanmasýný saðlar.
* 
*/
StreamEnvelopedData::StreamEnvelopedData(EASNFileInputStream* iEnvDataStream,QIODevice *oPlainDataStream,const QString iParola)
:	QObject(0),
	mEocCount(0) ,
	mTamamlandi(0),
	mParola(iParola),
	mType(EDT_PASSWORDBASED),
	mEncDataStream(NULL),
	mAB(AnahtarBilgisi(OzelAnahtarBilgisi(),AcikAnahtarBilgisi()))
{
	CMS_FUNC_BEGIN

	decryptStream(iEnvDataStream,oPlainDataStream,iParola);
	
	CMS_FUNC_END
}

/**
* \brief
* StreamEnvelopedData sýnýfý constructor ý.
* 
*/
StreamEnvelopedData::StreamEnvelopedData(void)
:	QObject(0) , 
	pIN(NULL),
	mEocCount(0), 
	mAB(AnahtarBilgisi(OzelAnahtarBilgisi(),AcikAnahtarBilgisi())),
	mEncDataStream(NULL),
	mPDStream(NULL),
	mTamamlandi(0)
{
	CMS_FUNC_BEGIN
	CMS_FUNC_END
}


/**
* \brief
* Zarf yapýsýndaki dosyadan CMS baþlýðýný okur
*
*/
void StreamEnvelopedData::_readHeader()
{
	CMS_FUNC_BEGIN

	KERMEN_WORK_SYNCRONIZED;

	ASN1TAG tag; 
	int len;

	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_CONTENTINFO);

	_readContentType();
	_readContentHeader();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS ContentInfo yapýsýný okur
*
*/
void StreamEnvelopedData::_readContentInfo()
{
	CMS_FUNC_BEGIN

	ASN1TAG tag; 
	int len;

	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_CONTENTINFO);

	_readContentType();
	_readContent();

	// ContentInfo Bitti sonu kapatýlmalý.
	if ( len == ASN_K_INDEFLEN )  pIN->pIN()->decodeEoc();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS ContentType yapýsýný okur
*
*/
void StreamEnvelopedData::_readContentType()
{
	CMS_FUNC_BEGIN

	ASN1TObjId objID;
	pIN->decodeObjID(&objID);
	if ( objID != (ASN1TObjId)CMS_id_envelopedData )
	{
		throw EException(CMS_NOT_A_ENVELOPED_CONTENT,__FILE__,__LINE__);
	}		

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS ContentHeader yapýsýný okur
*
*/
void StreamEnvelopedData::_readContentHeader()
{
	CMS_FUNC_BEGIN

	ASN1TAG tag; 
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_CONTENT);

	_readEnvelopedDataHeader();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS Content yapýsýný okur
*
*/
void StreamEnvelopedData::_readContent()
{
	CMS_FUNC_BEGIN

	ASN1TAG tag; 
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_CONTENT);

	_readEnvelopedData();

	// Content Bitti sonu kapatýlmalý.
	if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EnvelopedDataHeader yapýsýný okur
*
*/
void StreamEnvelopedData::_readEnvelopedDataHeader()
{
	CMS_FUNC_BEGIN

	ASN1TAG tag; 
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_ENVELOPEDEDDATA);

	_readVersion();
	_readOriginatorInfo();
	_readRecipientInfos();
	_readEncryptedContentInfoHeader();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EnvelopedData yapýsýný okur
*
*/
void StreamEnvelopedData::_readEnvelopedData()
{
	CMS_FUNC_BEGIN

	ASN1TAG tag; 
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_ENVELOPEDEDDATA);

	_readVersion();
	_readOriginatorInfo();
	_readRecipientInfos();
	_readEncryptedContentInfo();
	_readUnProtectedAttributes();

	// EnvelopedData Bitti sonu kapatýlmalý.
	if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS Version yapýsýný okur
*
*/
void StreamEnvelopedData::_readVersion()
{
	CMS_FUNC_BEGIN
	
	pIN->decodeUInt(&mVersion);

	mTamamlandi = pIN->byteIndex();
	EMIT_PROGRESS

	CMS_FUNC_END
}


/**
* \brief
* Zarf yapýsýndaki dosyadan CMS OriginatorInfo yapýsýný okur
*
*/
void StreamEnvelopedData::_readOriginatorInfo()
{
	CMS_FUNC_BEGIN
	
	ASN1TAG tag; 
	int len;

	pIN->peekTagLen(tag,len);

	if (tag != TAG_ORIGINATORINFO ) // OriginatorInfo SEQUENCE
	{
		DEBUGLOGYAZ(ESYACMS_MOD, CMS_NO_ORIGINATOR_INFO_ADDED);
		mOriginatorInfoPresent = false;
		return;
	}

	QByteArray oiBytes;
	pIN->decodeOpenType(&oiBytes);	

	mOriginatorInfo.constructObject(oiBytes);
	mOriginatorInfoPresent = true;	

	mTamamlandi = pIN->byteIndex();
	EMIT_PROGRESS

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS RecipientInfos yapýsýný okur
*
*/
void StreamEnvelopedData::_readRecipientInfos()
{
	CMS_FUNC_BEGIN
	
	QByteArray riBytes;
	pIN->decodeOpenType(&riBytes);
	RecipientInfo().copyRIs(riBytes,mRecipientInfos);
	mTamamlandi = pIN->byteIndex();
	EMIT_PROGRESS

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EncryptedContentInfoHeader yapýsýný okur
*
*/
void StreamEnvelopedData::_readEncryptedContentInfoHeader()
{
	CMS_FUNC_BEGIN

		ASN1TAG tag;
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_ENCRYPTEDCONTENTINFO);

	_readEncryptedContentInfoType();
	_readContentEncryptionAlgorithm();


	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EncryptedContentInfo yapýsýný okur
*
*/
void StreamEnvelopedData::_readEncryptedContentInfo()
{
	CMS_FUNC_BEGIN
	
	ASN1TAG tag;
	int len;
	pIN->peekTagLen(tag,len);
	pIN->skipTag(TAG_ENCRYPTEDCONTENTINFO);

	_readEncryptedContentInfoType();
	_readContentEncryptionAlgorithm();
	_readEncryptedContent();

	//EncryptedContentInfo Bitti sonu kapatýlmalý.
	if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EncryptedContentInfoType yapýsýný okur
*
*/
void StreamEnvelopedData::_readEncryptedContentInfoType()
{
	CMS_FUNC_BEGIN
	
	ASN1TObjId objID;
	pIN->decodeObjID(&objID);
	mEncryptedContentInfo.setContentType(objID);
	mTamamlandi = pIN->byteIndex();
	EMIT_PROGRESS;

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS EncryptedContent yapýsýný okur. Bu okuma sýrasýnda þifre çözme iþlemi gerçekleþtirilir
*
*/
void StreamEnvelopedData::_readEncryptedContent()
{
	CMS_FUNC_BEGIN		

	ASN1TAG tag;
	int len;
	QByteArray bytes;

	const AlgorithmIdentifier & contEncAlg = mEncryptedContentInfo.getContentEncAlg();



	pIN->pIN()->mark(10000);
	pIN->decodeTagLen(&tag,&len);

	if (!mpKeyDecryptor )
		throw ECMSException("KeyDecryptor NULL olamaz");
	
	mEncryptionKey =  mpKeyDecryptor->decryptKey();

	SifreCozucu cozucu(contEncAlg,mEncryptionKey );


	if ( tag == TAG_A0_READ )
	{
		if (len == ASN_K_INDEFLEN)
		{
			
			while (tag != TAG_ENDOF_OCTETSTR ) 
			{
				pIN->decodePrimitiveOctetString(ASN1EXPL,&bytes);							
 				cozucu.besle(bytes);
 				mPDStream->write(cozucu.bosalt());				
				pIN->peekTagLen(tag,len);
				mTamamlandi = pIN->byteIndex();
				EMIT_PROGRESS
			}				
			cozucu.bitir();
			mPDStream->write(cozucu.bosalt());			
		}
		else 
		{
			TAlg_Paralel_Adaptor a;
			a.ekle(&cozucu);
			Besleyici b(&a);
			pIN->processDefLenOctetString(b);
			cozucu.bitir();
			mPDStream->write(cozucu.bosalt());
			mTamamlandi = pIN->byteIndex();
			EMIT_PROGRESS
		}
	}
	else if ( tag == TAG_80_READ )
	{
		pIN->pIN()->reset();
		pIN->decodePrimitiveOctetString(ASN1IMPL,&bytes);
		cozucu.besle(bytes);
		cozucu.bitir();
		mPDStream->write(cozucu.bosalt());
		mTamamlandi = pIN->byteIndex();
		EMIT_PROGRESS
	}
	else 
	{
		throw EException("Geçersiz ASN yapýsý",__FILE__,__LINE__);
	}

	CMS_FUNC_END
}


/**
* \brief
* Zarf yapýsýndaki dosyadan CMS ContentEncryptionAlgorithm yapýsýný okur
*
*/
void StreamEnvelopedData::_readContentEncryptionAlgorithm()
{
	CMS_FUNC_BEGIN		
	
	QByteArray ceaBytes;
	pIN->decodeOpenType(&ceaBytes);
	mEncryptedContentInfo.setContentEncAlg(AlgorithmIdentifier(ceaBytes));

	CMS_FUNC_END
}

/**
* \brief
* Zarf yapýsýndaki dosyadan CMS UnProtectedAttributes yapýsýný okur
*
*/
void StreamEnvelopedData::_readUnProtectedAttributes()
{
	CMS_FUNC_BEGIN		
	
	ASN1TAG tag;
	int len;
	ASN1CCB ccb;
	if ( XD_CHKEND(pIN->pIN()->getCtxtPtr(),&ccb) )
		return ;
	pIN->peekTagLen(tag,len);
	if ( tag == TAG_UNPROTECTEDATTRS )
	{
		QByteArray uaBytes;
		pIN->decodeOpenType(&uaBytes);
		Attribute().copyAttributeList(uaBytes,mUnprotectedAttrs);
	}

	CMS_FUNC_END
}



/**
* \brief
* Verilen girdi stream nesnesinden düz(þifresiz) veriyi okuyarak þifreli zarf yapýsýný oluþturur
*
* \param 		QIODevice * iPDStream
* Þifresiz veri girdi stream nesnesi
*
* \param 		QIODevice * iEncDataStream
* Simetrik þifreli veri stream nesnesi
*
* \param 		QIODevice * oEDStream
* ASimetrik þifreli (zarf yapýsý) veri stream nesnesi
*
*/
void StreamEnvelopedData::constructSED(QIODevice* iPDStream , QIODevice* iEncDataStream , QIODevice*oEDStream)
{
	CMS_FUNC_BEGIN		
	
	if (!iEncDataStream &&  !iPDStream)
		throw EException("Kaynak stream NULL olamaz");

	EMIT_PROGRESS
	mPDStream = iPDStream;
	mEncDataStream =  iEncDataStream;
	OutputStream os(oEDStream);
	mOUT = os;;
	_writeContentInfo();

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeContentInfo()
{
	CMS_FUNC_BEGIN		
	
	EASNStreamingUtils::writeTagLen(mOUT,TAG_CONTENTINFO_WRITE,INDEFLEN);
	_writeContentType();
	_writeContent();
	if (mPDStream)
		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU	

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeContentType()
{
	CMS_FUNC_BEGIN		
	
	ASN1OBJID  contentType = CMS_id_envelopedData;   
	EASNStreamingUtils::writeObjID(mOUT,contentType);

	CMS_FUNC_END
}


void StreamEnvelopedData::_writeContent()
{
	CMS_FUNC_BEGIN		
	
	EASNStreamingUtils::writeTagLen(mOUT,TAG_CONTENT_WRITE,INDEFLEN);
	_writeEnvelopedData();
	if (mPDStream)
		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU	

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeEnvelopedData()
{
	CMS_FUNC_BEGIN		
	
	EASNStreamingUtils::writeTagLen(mOUT,TAG_ENVELOPEDEDDATA_WRITE,INDEFLEN);
	_writeVersion();
	_writeOriginatorInfo();
	_writeRecipientInfos();
	if (mPDStream) // Geri kalan kýsým baþtan oluþturulacak..
	{	_writeEncryptedContentInfo();
		_writeUnprotectedAttributes();
		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU	
	}
	else // Geri kalan kýsmý önceki enveloped data dosyasýndan yazýlacak..
	{	
		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENCRYPTEDCONTENTINFO_WRITE,INDEFLEN);
		_writeEncryptedContentInfoType();
		_writeContentEncryptionAlgorithm();
		
		if (!mEncDataStream ) throw EException("Kaynak Stream NULL olamaz");
		_writeFromEncDataStream();
// 		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU
// 		EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU
	}

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeVersion()
{
	CMS_FUNC_BEGIN		
	
	ASN1BEREncodeBuffer encBuf;
	int stat = asn1E_CMS_CMSVersion (encBuf.getCtxtPtr(), &mVersion, ASN1EXPL);
	if (stat < 0) 
	{
		throw EException(CMS_ASNE_ENVELOPEDDATA_VERSION,__FILE__,__LINE__);
	}
	EASNStreamingUtils::write(mOUT,encBuf.getMsgPtr(),stat);

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeOriginatorInfo()
{
	CMS_FUNC_BEGIN		
	
	if ( !mOriginatorInfoPresent )
	{
		DEBUGLOGYAZ(ESYACMS_MOD, CMS_NO_ORIGINATOR_INFO_ADDED);
		return;
	}

	QByteArray oiBytes = mOriginatorInfo.getEncodedBytes();
	EASNStreamingUtils::write(mOUT,oiBytes);	

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeUnprotectedAttributes()
{
	CMS_FUNC_BEGIN		
	
	if ( mUnprotectedAttrs.size() == 0 )
	{
		DEBUGLOGYAZ(ESYACMS_MOD,CMS_NO_UNPROTECTEDATTRIBUTES_ADDED);
		return;
	}

	QByteArray uaBytes;
	Attribute().copyAttributeList(mUnprotectedAttrs,uaBytes);
	EASNStreamingUtils::write(mOUT,uaBytes);

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeRecipientInfos()
{
	CMS_FUNC_BEGIN		
	
	QByteArray riBytes;
	RecipientInfo().copyRIs(mRecipientInfos,riBytes);
	EASNStreamingUtils::write(mOUT,riBytes);

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeContentEncryptionAlgorithm()
{
	CMS_FUNC_BEGIN		
	
	QByteArray ceaBytes = mEncryptedContentInfo.getContentEncAlg().getEncodedBytes();
	EASNStreamingUtils::write(mOUT,ceaBytes);	

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeEncryptedContentInfo()
{
	CMS_FUNC_BEGIN		

	EASNStreamingUtils::writeTagLen(mOUT,TAG_ENCRYPTEDCONTENTINFO_WRITE,INDEFLEN);

	_writeEncryptedContentInfoType();
	_writeContentEncryptionAlgorithm();
	_writeEncryptedContent();

	EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeEncryptedContentInfoType()
{
	CMS_FUNC_BEGIN		
	
	ASN1OBJID contentType = CMS_id_data;   
	EASNStreamingUtils::writeObjID(mOUT,contentType);

	CMS_FUNC_END
}

void StreamEnvelopedData::_writeEncryptedContent()
{
	CMS_FUNC_BEGIN
	
	EASNStreamingUtils::writeTagLen(mOUT,TAG_ENCAPCONTENT_WRITE,INDEFLEN);
	_encryptContent();
	EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU	

	CMS_FUNC_END
}
 


void StreamEnvelopedData::_encryptContent()
{
	CMS_FUNC_BEGIN
	
	QByteArray line,encLine;
	Sifreleyici sifreleyici(mEncryptedContentInfo.getContentEncAlg(),mEncryptionKey);
	
	bool emptyData = true;
	while (!mPDStream->atEnd())
	{
		line = mPDStream->read(BLOK_SIZE);
		if (!line.isEmpty())	
			emptyData = false;
		sifreleyici.besle(line);
		encLine =  sifreleyici.bosalt();
		if (!encLine.isEmpty())
			EASNStreamingUtils::writePrimitiveOctetString(mOUT,encLine);
		mTamamlandi += line.size();
		EMIT_PROGRESS
	}
	if (emptyData)
		throw EException("Boþ bir data envelope yapýlamaz.");

	sifreleyici.bitir();
	EASNStreamingUtils::writePrimitiveOctetString(mOUT,sifreleyici.bosalt());

	CMS_FUNC_END
}


void StreamEnvelopedData::_writeFromEncDataStream()
{
	if (!mEncDataStream )
		throw EException("Kaynak Stream NULL olamaz");

	if ( !mEncDataStream->isOpen() && !mEncDataStream->open(QIODevice::ReadOnly))
		throw EException("Kaynak Stream açýlamadý");

	mEncDataStream->reset();
	mEncDataStream->seek(mEncDataByteIndex);

	QByteArray bytes;
	while ( ! mEncDataStream->atEnd() )
	{
		bytes = mEncDataStream->read(BLOK_SIZE);
		EASNStreamingUtils::write(mOUT,bytes);
		mTamamlandi += bytes.size();
		EMIT_PROGRESS
	}

	mEncDataStream->close();
}

/**
* \brief
* Dosyayý PKCS5 e göre þifreler..
* 
* \param iDataFile
* Þifrelenecek Dosya adresi.
* 
* \param iParola
* Dosyanýn kendisine göre þifreleneceði parola.
* 
* \param iContEncAlg
* Content Encryption Algorithm.
* 
* \param iEncryptionKey
* Simetric þifreleme anahtarý
* 
* \returns
* baþarýlý olursa SUCCESS döner.
* 
* \throws <ECMSException>
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

int StreamEnvelopedData::encryptStream(	QIODevice * iPDStream, 
										QIODevice * oEDStream, 
										const QString &iParola ,
										const AlgorithmIdentifier &iContEncAlg , 
										const QByteArray &iEncryptionKey  ,
										const AlgorithmIdentifier &iKeyDrvAlg , 
										const AlgorithmIdentifier &iKeyEncAlg 
										)
{
	CMS_FUNC_BEGIN
	
	StreamEnvelopedData envData(iEncryptionKey,iContEncAlg,iParola);

	ESYAPasswordRecipientInfo epwri(iKeyEncAlg,iKeyDrvAlg,iEncryptionKey,iParola);
	RecipientInfo rInfo( OtherRecipientInfo(ESYA_id_esyapwri, epwri.getEncodedBytes()) );			
	envData.addRecipientInfo(rInfo);


	envData.constructSED(iPDStream,NULL,oEDStream);
	return SUCCESS;

	CMS_FUNC_END
}



/**
 * \brief
 * Dosyayý PKCS7 ye göre þifreler..
 * 
 * \param iDataFile
 * Þifrelenecek Dosya adresi.
 * 
 * \param iRecipients
 * Dosyanýn kimlere göre þifreleneceðini gösteren dosya.
 * 
 * \param iContEncAlg
 * Content Encryption Algorithm.
 * 
 * \param iEncryptionKey
 * Simetric þifreleme anahtarý
 * 
 * \param oEnvelopedDataFile
 * Þifrelenen dosyanýn kopyalanacaðý dosya adresi.
 * 
 * \returns
 * baþarýlý olursa SUCCESS döner.
 * 
 * \throws <ECMSException>
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

int StreamEnvelopedData::encryptStream(	QIODevice * iPDStream, 
										QIODevice * oEDStream, 
										const QList<ECertificate> &iRecipients ,
										const AlgorithmIdentifier &iContEncAlg , 
										const QByteArray &iEncryptionKey  
									  )
{
	CMS_FUNC_BEGIN
	
	StreamEnvelopedData envData(iEncryptionKey,iContEncAlg);

	envData.addRecipientInfos(EnvelopedData::buildRecipientInfos(iRecipients,iEncryptionKey));

	envData.constructSED(iPDStream,NULL,oEDStream);
	return SUCCESS;

	CMS_FUNC_END
}


/**
 * \brief
 * Dosyayý PKCS7 ye göre þifreler..
 * 
 * \param iDataFile
 * Þifrelenecek Dosya adresi.
 * 
 * \param iRecipients
 * Dosyanýn kimlere göre þifreleneceðini gösteren dosya.
 * 
 * \param iContEncAlg
 * Content Encryption Algorithm.
 * 
 * \param iEncryptionKey
 * Simetric þifreleme anahtarý
 * 
 * \param oEnvelopedDataFile
 * Þifrelenen dosyanýn kopyalanacaðý dosya adresi.
 * 
 * \returns
 * baþarýlý olursa SUCCESS döner.
 * 
 * \throws <ECMSException>
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

int StreamEnvelopedData::encryptStreamToGroup(	QIODevice * iPDStream, 
												QIODevice * oEDStream, 
												const QList<ECertificate> &iRecipients ,
												const AlgorithmIdentifier &iContEncAlg , 
												const QByteArray &iEncryptionKey  
											  )
{
	CMS_FUNC_BEGIN
	
	StreamEnvelopedData envData(iEncryptionKey,iContEncAlg);

	envData.addRecipientInfos(EnvelopedData::buildEGRIs(iRecipients,iEncryptionKey,0));

	envData.constructSED(iPDStream,NULL,oEDStream);
	return SUCCESS;

	CMS_FUNC_END
}

/*
int StreamEnvelopedData::decryptFile(const ECertificate & iCert , const AnahtarBilgisi& iAB, const QString & iPlainDataFileName) const
{
	try
	{
		QByteArray	key	= decryptKey(iCert,iAB);	
		int			res	= KriptoUtils::decryptFileSymmetric(mEncDataFileName,iPlainDataFileName,key,mEncryptedContentInfo.getContentEncAlg());
		return res;
	}
	catch( EException &exc )
	{
		throw exc.append("decryptData() : Þifre Çözme sýrasýnda hata oluþtu", __FILE__,__LINE__);
	}
}
*/

void	StreamEnvelopedData::setTamamlandi(const int iTamamlandi)
{
	CMS_FUNC_BEGIN

	mTamamlandi = iTamamlandi;

	CMS_FUNC_END
}


StreamEnvelopedData::~StreamEnvelopedData(void)
{
	mEncryptionKey.fill('0'); // Memory zeroization
}

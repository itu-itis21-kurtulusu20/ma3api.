
#include "StreamedSignedData.h"
#include "Ozetci.h"
#include "myasndefs.h"
#include "EASNStreamingUtils.h"
#include "KriptoUtils.h"
#include "IOYazici.h"
#include "Kronometre.h"
#include "cmslibrary_global.h"
#include "OrtakDil.h"
#include "FileUtil.h"
#include "EFileHandle.h"

//
#include "TAlg_Paralel_Adaptor.h"
//

using namespace esya;


void StreamedSignedData::loadFromFile(FILE* iSDFile , QIODevice* oPDStream )
{
    CMS_FUNC_BEGIN

    if (!iSDFile)
    {
        throw EException("StreamedSignedData::loadFromFile() : Kaynak Dosya objesi NULL olamaz",__FILE__,__LINE__);
    }

    mPDStream = oPDStream;
    mSDFile = iSDFile;

    EMIT_PROGRESS_TEXT(QString("%1..").arg(DIL_DI_IMZALI_VERI_OKUNUYOR));
    try
    {
        pIN = EASNStreamingUtils::createFileInputStream(mSDFile);
        _readContentInfo();
        DELETE_MEMORY(pIN);
    }
    catch( EException &exc)
    {
        DELETE_MEMORY(pIN);
        exc.append("StreamedSignedData::loadFromFile()",__FILE__,__LINE__);
        throw exc;
    }

    CMS_FUNC_END
}


StreamedSignedData::StreamedSignedData(const QString &iSDFileName , QIODevice* oPDStream )
:	pIN(NULL) ,
    mEocCount(0),
    mTamamlandi(0),
    mSignersConstructed(false)
{
    CMS_FUNC_BEGIN

    mIsStreamed = true;
    int asn1Status = 0;
    EMIT_PROGRESS;

    EFileHandle sdh(iSDFileName);

    try
    {
        loadFromFile(sdh.getFilePtr(),oPDStream);
    }
    catch(EException &exc)
    {
        throw exc;
    }

    CMS_FUNC_END
}

StreamedSignedData::StreamedSignedData( EASNFileInputStream & iSDStream,QIODevice* oPDStream)
:	pIN(NULL) ,
    mPDStream(oPDStream),
    mTamamlandi(0),
    mEocCount(0),
    mSignersConstructed(false)
{
    CMS_FUNC_BEGIN

    mIsStreamed = true;
    int asn1Status = 0;

    pIN = &iSDStream;
    _readContentInfo();

    CMS_FUNC_END
}

StreamedSignedData::StreamedSignedData( void)
:	SignedData(),
    pIN(NULL) ,
    mPDStream(NULL),
    mEocCount(0),
    mTamamlandi(0),
    mSignersConstructed(false)
{
    CMS_FUNC_BEGIN

    mIsStreamed = true;

    CMS_FUNC_END
}



int StreamedSignedData::getDigest(const AlgorithmIdentifier &digestAlg,QByteArray & qbDigest) const
{
    CMS_FUNC_BEGIN

    for (int i = 0 ; i< mDigestList.size();i++)
    {
        AlgorithmIdentifier alg = mDigestList[i].first;
        if ( (ASN1OBJID)alg.getAlgorithm()  == (ASN1OBJID)digestAlg.getAlgorithm() )
        {
            qbDigest = mDigestList[i].second;
            return SUCCESS;
        }
    }
    return FAILURE;;

    CMS_FUNC_END
}

void StreamedSignedData::setTamamlandi(const int iTamamlandi)
{
    CMS_FUNC_BEGIN

    mTamamlandi = iTamamlandi;

    CMS_FUNC_END
}

int StreamedSignedData::addParallelBESSigners(
                                                SignParam & iSP,
                                                QIODevice& iPDSource
                                             )
{
    CMS_FUNC_BEGIN

    mTamamlandi = 0;
    EMIT_PROGRESS_TEXT( QString("%1..").arg(DIL_DI_OZET_ALINIYOR))
    EMIT_PROGRESS

    QByteArray line;
    QList<AlgorithmIdentifier> digestAlgs = iSP.getDigestAlgs();

    QList<DigestInfo> digestList;
    TAlg_Paralel_Adaptor ozetciler;
    KriptoUtils::ozetcileriOlustur(digestAlgs,ozetciler);

    while (! iPDSource.atEnd())
    {
        line = iPDSource.read(OCTETSTR_BLOCK_SIZE);
        mTamamlandi += line.size();
        //ERRORLOGYAZ("CMS","Ozet alma progresi atýldý");
        ozetciler.besle(line);
        EMIT_PROGRESS
    }
    ozetciler.bitir();

    for (int i = 0 ; i< digestAlgs.size();i++)
    {
        digestList.append(DigestInfo(digestAlgs[i],ozetciler.bosalt(i)));
    }
    KriptoUtils::ozetcileriYokEt(ozetciler);

    _appendDigestList(digestList);
    iSP.fillDigests(digestList);
	

    addParallelBESSigners(iSP.getSignerParams());

    iPDSource.reset();
    return SUCCESS;

    CMS_FUNC_END
}

int StreamedSignedData::addParallelBESSigners(	const QList<SignerParam> & iSPs )
{
    CMS_FUNC_BEGIN

    for (int i = 0 ; i< iSPs.size(); i++)
    {
        SignerInfo* pSigner = SignerInfo::constructBESSigner(iSPs[i],false,true,NULL,this);
        addParallelSigner(*pSigner,iSPs[i].getCert());
        DELETE_MEMORY(pSigner);
    }
    return SUCCESS;

    CMS_FUNC_END
}

int StreamedSignedData::addParallelBESSigner(	const SignerParam & iSP,
                                                QIODevice& iPDSource
                                            )
{
    CMS_FUNC_BEGIN

    SignParam sp;
    sp.addSignerParam(iSP);
    return addParallelBESSigners(sp,iPDSource);

    CMS_FUNC_END
}

int StreamedSignedData::addParallelSigner(const SignerInfo& iSigner, const ECertificate & iSignerCert)
{
    CMS_FUNC_BEGIN

    SignerInfo  signer(iSigner);
    signer.setParent(NULL);
    signer.setParentData(this);
    mSignerInfos.append(signer);
    addDigestAlgorithm(signer.getDigestAlgorithm());
    addCertificate(ECertChoices(iSignerCert));

    return SUCCESS;

    CMS_FUNC_END
}

void StreamedSignedData::fillDigestAlgorithms( const SignParam & iSP )
{
    const QList<SignerParam> & signers = iSP.getSignerParams();
    for (int i = 0 ; i<signers.size();i++ )
    {
        addDigestAlgorithm(signers[i].getDigestAlg());
    }

}



void StreamedSignedData::addDigestAlgorithm( const AlgorithmIdentifier & iDigestAlg)
{
    for (int i = 0 ; i < mDigestAlgorithms.size() ; i++ )
    {
        if ( iDigestAlg == mDigestAlgorithms[i])
            return ;
    }
    mDigestAlgorithms.append(iDigestAlg);
}

int StreamedSignedData::verifyFile(const QList<ECertificate> &iCertificates )const
{
    CMS_FUNC_BEGIN

    for (int i = 0 ; i < mSignerInfos.size(); i++ )
    {
        if ( !mSignerInfos[i].verifySigner(iCertificates,true,true) )
        {
            ERRORLOGYAZ(ESYACMS_MOD ,QString("Ýmzacý : %1 için doðrulama gerçekleþtirilemedi.").arg(mSignerInfos[i].getSID().toString()));
            return false;
        }
    }
    return true;

    CMS_FUNC_END
}

QByteArray StreamedSignedData::calculateDigest(const QString & iKaynakAdi,  const AlgorithmIdentifier &iDigestAlg ,bool isSigned )
{
    CMS_FUNC_BEGIN

    QByteArray digest;
    if (getDigest(iDigestAlg.getAlgorithm(),digest) != SUCCESS )
    {
        if (! isSigned)
        {
            QFile pdStream(iKaynakAdi);
            digest = KriptoUtils::calculateStreamDigest(pdStream,iDigestAlg);
            return digest;
        }
        else
        {
            TAlg_Paralel_Adaptor ozetciler;
            ozetciler.ekle( new Ozetci(iDigestAlg.getAlgorithm()));

            pIN = EASNStreamingUtils::createFileInputStream(iKaynakAdi);
            _seek2EncapContent();	// Dosyada EncapContent in baþladýðý yere git.
            _readEncapContent(ozetciler); // Özeti hesapla
            unsigned char buf[MAX_BUF_SIZE] ;
            digest = ozetciler.bosalt(0);
            _ozetcileriYokEt(ozetciler);
            return digest;
        }
    }

    CMS_FUNC_END
}


/************************************************************************/
/*                        STREAM READ FUNCTIONS                         */
/************************************************************************/


void StreamedSignedData::_readContentInfo()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;

    pIN->peekTagLen(tag,len);
    pIN->skipTag(TAG_CONTENTINFO);

    _readContentType();
    _readContent();

    // ContentInfo Bitti sonu kapatýlmalý.
    if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

    CMS_FUNC_END
}

void StreamedSignedData::_readContentType()
{
    CMS_FUNC_BEGIN

    ASN1TObjId objID;
    pIN->decodeObjID(&objID);
    if ( objID != (ASN1TObjId)CMS_id_signedData )
    {
        throw EException(CMS_NOT_A_SIGNED_CONTENT,__FILE__,__LINE__);
    }

    CMS_FUNC_END
}

void StreamedSignedData::_readContent()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;
    pIN->peekTagLen(tag,len);
    pIN->skipTag(TAG_CONTENT);

    _readSignedData();

    // Content Bitti sonu kapatýlmalý.
    if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

    CMS_FUNC_END
}

void StreamedSignedData::_seek2EncapContent()
{
    CMS_FUNC_BEGIN

    pIN->skipTag(TAG_CONTENTINFO);
    pIN->decodeObjID(NULL); // ContentType
    pIN->skipTag(TAG_CONTENT);
    pIN->skipTag(TAG_SIGNEDDATA);
    pIN->decodeUInt(NULL); // Version
    pIN->decodeOpenType(NULL);// DigestAlgs
    pIN->skipTag(TAG_ENCAPCONTENTINFO);
    pIN->decodeObjID(NULL); // EncapContentInfoType

        // Now we are at the start of encapcontent;

    CMS_FUNC_END
}

void StreamedSignedData::_readSignedData()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;

    pIN->peekTagLen(tag,len);
    pIN->skipTag(TAG_SIGNEDDATA);

    _readVersion();
    _readDigestAlgorithms();
    _readEncapContentInfo();
    _readCertificateSet();
    _readCRLs();
    _readSignerInfos();

    // SignedData Bitti sonu kapatýlmalý.
    if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

    CMS_FUNC_END
}

void StreamedSignedData::_readVersion()
{
    CMS_FUNC_BEGIN

    pIN->decodeUInt((unsigned int*)&mVersion);

    CMS_FUNC_END
}


void StreamedSignedData::_readDigestAlgorithms()
{
    CMS_FUNC_BEGIN
/*
    ASN1T_PKCS7_DigestAlgorithmIdentifiers digAlgs;
    ASN1C_PKCS7_DigestAlgorithmIdentifiers cDigAlgs(digAlgs);

    //int stat = pIN->decodeObj(cDigAlgs);
    *pIN >> cDigAlgs;

    AlgorithmIdentifier::copyAIs(digAlgs,mDigestAlgorithms);
*/
	
    QByteArray daBytes;
    pIN->decodeOpenType(&daBytes);
    AlgorithmIdentifier().copyAIs(daBytes, mDigestAlgorithms);

    CMS_FUNC_END
}

void StreamedSignedData::_readEncapContentInfo()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;
    pIN->peekTagLen(tag,len);


    pIN->skipTag(TAG_ENCAPCONTENTINFO);
    _readEncapContentInfoType();
    _readEncapContent();

    // Encap Content Info Bitti sonu kapatýlmalý.
    if ( len == ASN_K_INDEFLEN )  pIN->decodeEoc();

    CMS_FUNC_END
}

void StreamedSignedData::_readEncapContentInfoType()
{
    CMS_FUNC_BEGIN

    ASN1TObjId objID;
    pIN->decodeObjID(&objID);
    mEncapContentInfo.setEContentType(objID);

    CMS_FUNC_END
}


/**
*	Dosyada EncapContent var mý kontrol eder.
*
*
*/
bool StreamedSignedData::_checkEncapContent()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;
    pIN->peekTagLen(tag,len);

    if ( tag == TAG_ENCAPCONTENT )
    {
        int stat = pIN->pIN()->mark(10000);
        pIN->decodeTagLen(&tag,&len);
        pIN->decodeTagLen(&tag,&len);
        stat = pIN->pIN()->reset();
        if ( tag == TAG_PRIMITIVE_OCTETSTR || tag == TAG_CONSTRUCTED_OCTETSTR )
        {
            pIN->peekTagLen(tag,len);
            return true;
        }
        else
        {
            return false;
        }
    }
    return false;

    CMS_FUNC_END
}

void StreamedSignedData::_readEncapContent()
{
    CMS_FUNC_BEGIN
	
    if (!_checkEncapContent())
    {
        mEncapContentInfo.setEContentPresent(false);
        mAyrikImza = true;
        return;
    }
    else {
        mAyrikImza = false;
        mEncapContentInfo.setEContentPresent(true);
    }
    TAlg_Paralel_Adaptor ozetciler;
    _ozetcileriOlustur(ozetciler);

    _readEncapContent(ozetciler);
	
    _fillDigestList(ozetciler);
    _ozetcileriYokEt(ozetciler);

    CMS_FUNC_END

}


/************************************************************************/
/*                                                                      */
/************************************************************************/

/**
* \brief
* InDefiniteLength OctetString okur. streamin pointerinin bulunduðu yerden baþlayarak
* blok blok veri okur ve okuduðu veriyi blok iþleyiciye gönderir.
* !!! POINTERI ILERLETIR !!!
*
*/

void StreamedSignedData::processEncapContent_Constructed(EASNFileInputStream *pIN , BlokIsleyici& iBP )
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;
    QByteArray bytes;
    pIN->decodeTagLen(&tag,&len);
    pIN->peekTagLen(tag,len);

    while ( tag != TAG_ENDOF_OCTETSTR )
    {
        pIN->decodePrimitiveOctetString(ASN1EXPL,&bytes);
        iBP.blokIsle(bytes);
        pIN->peekTagLen(tag,len);
        mTamamlandi += pIN->byteIndex();
        EMIT_PROGRESS
    }

    CMS_FUNC_END
}



void StreamedSignedData::processEncapContent(EASNFileInputStream *pIN , BlokIsleyici& iBP )
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;
    QByteArray bytes;
    pIN->peekTagLen(tag,len);

    while (tag != TAG_ENDOF_OCTETSTR )
    {
        if (tag == TAG_PRIMITIVE_OCTETSTR)
        {
            pIN->decodePrimitiveOctetString(ASN1EXPL,&bytes);
            iBP.blokIsle(bytes);
            mTamamlandi = pIN->byteIndex();
            EMIT_PROGRESS
        }
        else if (tag == TAG_CONSTRUCTED_OCTETSTR)  // tag : 24
        {
            if ( len  == ASN_K_INDEFLEN ) // TAG_24 with INDEFINITE length
            {
                processEncapContent_Constructed(pIN,iBP);
                pIN->decodeEoc();
				
            }
            else // TAG_24 with DEFINITE length
            {
                pIN->processConstructedDefLenOctetString(iBP);
                mTamamlandi = pIN->byteIndex();
                EMIT_PROGRESS
            }
        }
        pIN->peekTagLen(tag,len);
    }

    CMS_FUNC_END
}

void StreamedSignedData::writeOctetString(OutputStream& pOUT, QIODevice & iIN , ASN1TagType tagging, bool iAyrikImza, TAlg_Paralel_Adaptor * pOzetciler )
{
    CMS_FUNC_BEGIN

    if (tagging == ASN1EXPL && !iAyrikImza )
        EASNStreamingUtils::writeTagLen(pOUT,TAG_24,INDEFLEN);

    if ( !iIN.isOpen())
    {
        throw EException("IODevice açýk deðil.",__FILE__,__LINE__);
    }
    while (!iIN.atEnd())
    {
        QByteArray line = iIN.read(OCTETSTR_BLOCK_SIZE);
        mTamamlandi += line.size();
		
        if ( !iAyrikImza)
            EASNStreamingUtils::writePrimitiveOctetString(pOUT,line);

        if ( pOzetciler )
            pOzetciler->besle(line);

        EMIT_PROGRESS
    }

    /* INDEFLEN Sonlandýrýlmalý*/
    if (tagging == ASN1EXPL && !iAyrikImza )
        EASNStreamingUtils::writeTagLen(pOUT,TAG_ENDOF_OCTETSTR,0);

    CMS_FUNC_END
}



/************************************************************************/
/*                                                                      */
/************************************************************************/

void StreamedSignedData::_readEncapContent(TAlg_Paralel_Adaptor & iOzetciler)
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len;

    pIN->peekTagLen(tag,len);
    pIN->skipTag(TAG_ENCAPCONTENT);

    Besleyici ob = Besleyici(&iOzetciler);
    if( len == ASN_K_INDEFLEN)
    {
        StreamedSignedData::processEncapContent(pIN,ob);
        pIN->decodeEoc();
    }
    else
    {
        pIN->processDefLenOctetString(ob);
        emit (tamamlandi(mTamamlandi += len) );
    }

    iOzetciler.bitir();
    mEncapContentInfo.setEContentPresent(true);

    CMS_FUNC_END
}


void StreamedSignedData::_ozetcileriOlustur(TAlg_Paralel_Adaptor & iOzetciler)
{
    CMS_FUNC_BEGIN

    int listSize = mDigestAlgorithms.size();
    if ( listSize == 0 )
    {
        throw EException(CMS_NO_DIGEST_ALG_FOUND,__FILE__,__LINE__);
    }

    for (int i = 0; i < listSize; i++)
    {
        Ozetci * pOzetci = new Ozetci(mDigestAlgorithms[i]);
        iOzetciler.ekle(pOzetci);
    }

    if (mPDStream)
    {
        IOYazici * pYazici = new IOYazici(mPDStream);
        iOzetciler.ekle(pYazici);
    }

    CMS_FUNC_END
}

void StreamedSignedData::_ozetcileriYokEt(TAlg_Paralel_Adaptor & iOzetciler)
{
    CMS_FUNC_BEGIN

    for (int i = 0 ; i<iOzetciler.size(); i++ )
    {
        const TemelAlgoritma * pOzetci = iOzetciler[i];
        DELETE_MEMORY(pOzetci);
    }

    CMS_FUNC_END
}

void StreamedSignedData::_appendDigestList(QList<DigestInfo> & iDigestList)
{
    CMS_FUNC_BEGIN

    QByteArray oDigest;

    for (int i=0 ; i < iDigestList.size() ; i++ )
    {
        if (getDigest(iDigestList[i].getDigestAlgorithm(),oDigest) == FAILURE )
            mDigestList.append(qMakePair(iDigestList[i].getDigestAlgorithm(),iDigestList[i].getDigest()));
    }

    CMS_FUNC_END
}


void StreamedSignedData::_fillDigestList(TAlg_Paralel_Adaptor & iOzetciler)
{
    CMS_FUNC_BEGIN

    for (int i=0 ; i < mDigestAlgorithms.size() ; i++ )
    {
        mDigestList.append(qMakePair(mDigestAlgorithms[i],iOzetciler.bosalt(i)));
    }

    CMS_FUNC_END
}

void StreamedSignedData::_readCertificateSet()
{
    CMS_FUNC_BEGIN

    ASN1TAG tag;
    int len,stat;
    pIN->peekTagLen(tag,len);

    if ( tag != TAG_A0_READ)// Certificate SET bulunamadý
        return;

    QByteArray csBytes;
	
    pIN->decodeOpenType(&csBytes);
    ECertChoices().copyCS(csBytes,mCertificates);

    CMS_FUNC_END
}

void StreamedSignedData::_readCRLs()
{
    CMS_FUNC_BEGIN

#define TAG_A1_READ 0xA0000001

    ASN1TAG tag;
    int len,stat;
    pIN->peekTagLen(tag,len);

    if ( tag != TAG_A1_READ)// Certificate SET bulunamadý
        return;

    QByteArray crlsBytes;

    pIN->decodeOpenType(&crlsBytes);
    try
    {
        RevocationInfoChoice().copyRICs(crlsBytes,mCRLs);
    }
    catch (EException& exc)
    {
        // CRLs okunamadý
    }


    CMS_FUNC_END
}

void StreamedSignedData::_readSignerInfos()
{
    CMS_FUNC_BEGIN

    QByteArray siBytes;
    pIN->decodeOpenType(&siBytes);
    SignerInfo().copySignerInfos(siBytes,mSignerInfos,NULL,this);

    for (int i = 0 ; i<mSignerInfos.size();i++ )
    {
        mSignerInfos[i].setParentData(this);
    }

    mSignersConstructed = true;
    CMS_FUNC_END
}



/************************************************************************/
/*                        STREAM WRITE FUNCTIONS                        */
/************************************************************************/


int StreamedSignedData::constructSSD( QIODevice * iPDStream , QIODevice * iSDStream, const SignParam * iSP)
{
    CMS_FUNC_BEGIN
    if (iSP)
    {
        mSignParam			= *iSP;
        mAyrikImza			= iSP->isAyrikImza();
    }
    mPDStream			= iPDStream;
    OutputStream os(iSDStream) ;
    mOUT = os;
    EMIT_PROGRESS_TEXT(QString("%1..").arg(DIL_DI_IMZALI_VERI_OLUSTURULUYOR))
    EMIT_PROGRESS
    try
    {
        _writeContentInfo();
        mPDStream = NULL;
    }
    catch (EException & exc)
    {
        mPDStream = NULL;
        exc.append("_constructSignedContent()",__FILE__,__LINE__);
        throw exc;
    }
    return SUCCESS;

    CMS_FUNC_END
}

void StreamedSignedData::_writeContentInfo()
{
    CMS_FUNC_BEGIN

    EASNStreamingUtils::writeTagLen(mOUT,TAG_CONTENTINFO_WRITE,INDEFLEN);
    _writeContentType();
    _writeContent();
    EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

    CMS_FUNC_END
}

void StreamedSignedData::_writeContentType()
{
    CMS_FUNC_BEGIN

    ASN1OBJID  contentType = CMS_id_signedData;
    EASNStreamingUtils::writeObjID(mOUT,contentType);

    CMS_FUNC_END
}


void StreamedSignedData::_writeContent()
{
    CMS_FUNC_BEGIN
	
    EASNStreamingUtils::writeTagLen(mOUT,TAG_CONTENT_WRITE,INDEFLEN);
    _writeSignedData();
    EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

    CMS_FUNC_END
}

void StreamedSignedData::_writeSignedData()
{
    CMS_FUNC_BEGIN

    EASNStreamingUtils::writeTagLen(mOUT,TAG_SIGNEDDATA_WRITE,INDEFLEN);
    _writeVersion();
    _writeDigestAlgorithms();
    _writeEncapContentInfo();
    _writeCertificates();
    _writeSignerInfos();
    EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

    CMS_FUNC_END
}

void StreamedSignedData::_writeVersion()
{
    CMS_FUNC_BEGIN

    ASN1BEREncodeBuffer encBuf;
    int stat = asn1E_CMS_CMSVersion (encBuf.getCtxtPtr(), &mVersion, ASN1EXPL);
    if (stat < 0)
    {
        throw EException(CMS_ASNE_SIGNEDDATA_VERSION,__FILE__,__LINE__);
    }
    EASNStreamingUtils::write(mOUT,encBuf.getMsgPtr(),stat);

    CMS_FUNC_END
}

void StreamedSignedData::_writeDigestAlgorithms()
{
    CMS_FUNC_BEGIN
	
    fillDigestAlgorithms(mSignParam);

    QByteArray daBytes;
    AlgorithmIdentifier().copyAIs(mDigestAlgorithms,daBytes);
    EASNStreamingUtils::write( mOUT,(ASN1OCTET*)daBytes.data(),daBytes.size());

    CMS_FUNC_END
}

void StreamedSignedData::_writeEncapContentInfo()
{
    CMS_FUNC_BEGIN

	
    EASNStreamingUtils::writeTagLen(mOUT,TAG_ENCAPCONTENTINFO_WRITE,INDEFLEN);

    _writeEncapContentInfoType();
    _writeEncapContent();

    EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

    CMS_FUNC_END
}

void StreamedSignedData::_writeEncapContentInfoType()
{
    CMS_FUNC_BEGIN

    ASN1OBJID contentType = CMS_id_data;
    EASNStreamingUtils::writeObjID(mOUT,contentType);

    CMS_FUNC_END

}

void StreamedSignedData::_writeEncapContent()
{
    CMS_FUNC_BEGIN

    if (!mPDStream && !mSignersConstructed && !mAyrikImza)
        throw EException("PlainData Kaynaðý tanýmlý deðil.",__FILE__,__LINE__);

    TAlg_Paralel_Adaptor ozetciler;
    QList<DigestInfo> digestList;
    const QList<AlgorithmIdentifier>& digestAlgs = mSignParam.getDigestAlgs();;
    if (!mSignersConstructed )
    {
        KriptoUtils::ozetcileriOlustur(digestAlgs,ozetciler);
    }
    if(!mAyrikImza) EASNStreamingUtils::writeTagLen(mOUT,TAG_ENCAPCONTENT_WRITE,INDEFLEN);
    writeOctetString(mOUT,*mPDStream,ASN1EXPL,mAyrikImza,&ozetciler);
    if(!mAyrikImza) EASNStreamingUtils::writeTagLen(mOUT,TAG_ENDOF_OCTETSTR,0); // INDEFLEN SONU

    ozetciler.bitir();

    if (!mSignersConstructed)
    {
        for (int i = 0 ; i< digestAlgs.size();i++)
        {
            digestList.append(DigestInfo(digestAlgs[i],ozetciler.bosalt(i)));
        }
        KriptoUtils::ozetcileriYokEt(ozetciler);
        _appendDigestList(digestList);
        mSignParam.fillDigests(digestList);
        const QList<SignerParam> & signers = mSignParam.getSignerParams();
        addParallelBESSigners(signers);
    }

    CMS_FUNC_END
}


void StreamedSignedData::_writeCertificates()
{
    CMS_FUNC_BEGIN

    if ( mCertificates.size()== 0 )
    {
        ERRORLOGYAZ(ESYACMS_MOD,CMS_NO_CERTIFICATE_ADDED);
        return;
    }
    QByteArray csBytes;
    ECertChoices().copyCS(mCertificates,csBytes);
    EASNStreamingUtils::write(mOUT, csBytes);

    CMS_FUNC_END
}


int StreamedSignedData::_writeSignerInfos()
{
    CMS_FUNC_BEGIN


    if (mSignerInfos.size() == 0)
    {
        throw EException(CMS_NO_SIGNERINFO_ADDED,__FILE__,__LINE__);
    }
    QByteArray siBytes;
    SignerInfo().copySignerInfos(mSignerInfos,siBytes);
    EASNStreamingUtils::write(mOUT,siBytes);

    CMS_FUNC_END
}

StreamedSignedData::~StreamedSignedData(void)
{
}

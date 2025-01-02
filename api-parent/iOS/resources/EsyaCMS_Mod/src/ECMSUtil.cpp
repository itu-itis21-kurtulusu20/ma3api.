#include "ECMSUtil.h"

//#include "SignedAttributesKontrolcu.h"
#include "OzetKontrolcu.h"
#include "ImzaKriptoKontrolcu.h"
//#include "PBKDF2_Params.h"

#include "cmslibrary_global.h"

//#include "FileUtil.h"
//#include "EFileHandle.h"
//#include "AlgoritmaBilgileri.h"
#include "KriptoUtils.h"

using namespace esya;

ECMSUtil::ECMSUtil(void)
{
}

///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iSignParam
//* �mzalama Parametrelesi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::_signBES(const QByteArray & iContent,const SignParam & iSP  , QByteArray & oSignedData )
//{
//	CMS_FUNC_BEGIN

//	SignedData signedData;

//	if (iSP.isAyrikImza())
//		signedData.setEncapContenInfo(EncapContentInfo(CMS_id_data));
//	else
//		signedData.setEncapContenInfo(EncapContentInfo(iContent,CMS_id_data));

//	signedData.addParallelBESSigners(iSP,iContent);

//	ContentInfo signedContent(signedData.getEncodedBytes(),CMS_id_signedData);
//	oSignedData = signedContent.getEncodedBytes();

//	return SUCCESS;

//	CMS_FUNC_END
//}

///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::_signBES(const QByteArray & iContent,  const ECertificate &iSignerCert,const AnahtarBilgisi& iAB, QByteArray & oSignedData, bool iAyrikImza,const QList< QPair<ASN1TObjId,QByteArray> > & iAdditionalUnSignedAttributes )
//{
//	CMS_FUNC_BEGIN

//	SignerParam signer(SignedData::DEFAULT_VERSION, iSignerCert, SignedData::DEFAULT_DIGEST_ALGORITHM,iAB,iAdditionalUnSignedAttributes);
//	SignParam sp(false,QList<SignerParam>());
//	sp.addSignerParam(signer);
//	sp.setAyrikImza(iAyrikImza);

//	_signBES(iContent,sp,oSignedData);

//	CMS_FUNC_END
//	return SUCCESS;
//}



///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::_signBES(const QByteArray & iContent,  const ECertificate &iSignerCert,const AnahtarBilgisi& iAB, QByteArray & oSignedData, bool iAyrikImza)
//{
//	CMS_FUNC_BEGIN

//        QList< QPair<ASN1TObjId,QByteArray> > additionalUnSignedAttributes = QList< QPair<ASN1TObjId,QByteArray> >();
//	_signBES(iContent,iSignerCert,iAB,oSignedData,iAyrikImza,additionalUnSignedAttributes);

//	CMS_FUNC_END
//	return SUCCESS;
//}

///**
//* \brief
//* Verilen dosyaya BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iSignParam
//* �mzalama Parametreleri
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::_signFileBES(const QString & iContentFile, const SignParam & iSP,  const QString & oSignedDataFile  )
//{
//	CMS_FUNC_BEGIN

//	QFile iPDFile(iContentFile);
//	QFile iSDFile(oSignedDataFile);

//	if (!iPDFile.open(QIODevice::ReadOnly))
//	{
//		throw ECMSException("Kaynak Dosya a��lamad�.");
//	}

//	if (!iSDFile.open(QIODevice::WriteOnly))
//	{
//		throw ECMSException("Dosya olu�turulamad�.");
//	}

//	StreamedSignedData signedData;
//	signedData.constructSSD(&iPDFile,&iSDFile,&iSP);
//	iSDFile.close();

//	CMS_FUNC_END
//	return SUCCESS;
//}

///**
//* \brief
//* Verilen dosyaya BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::_signFileBES(const QString & iContentFile,  const ECertificate &iSignerCert, const AnahtarBilgisi& iAB,const QString & oSignedDataFile , bool iAyrikImza)
//{
//	CMS_FUNC_BEGIN

//	SignParam sp(iAyrikImza,QList<SignerParam>());
//	sp.addSignerParam(SignerParam(SignerInfo::DEFAULT_VERSION,iSignerCert,SignedData::DEFAULT_DIGEST_ALGORITHM,iAB));

//	_signFileBES(iContentFile,sp,oSignedDataFile);


//	CMS_FUNC_END
//	return SUCCESS;
//}
///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::signBES(const QByteArray & iData,  const ECertificate & iCert,const AnahtarBilgisi& iAB , QByteArray & oSignedData,bool iAyrikImza, const QList< QPair<ASN1TObjId,QByteArray> > & iUnsignedAttributes)
//{
//	CMS_FUNC_BEGIN

//	int ret = _signBES(iData, iCert, iAB ,oSignedData,iAyrikImza,iUnsignedAttributes);

//	CMS_FUNC_END
//	return ret;
//}

///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::signBES(const QByteArray & iData,  const ECertificate & iCert,const AnahtarBilgisi& iAB , QByteArray & oSignedData,bool iAyrikImza)
//{
//	CMS_FUNC_BEGIN

//        QList< QPair<ASN1TObjId,QByteArray> >  unsignedAttributes = QList< QPair<ASN1TObjId,QByteArray> >();
//        int ret = signBES(iData, iCert, iAB ,oSignedData,iAyrikImza,unsignedAttributes);

//	CMS_FUNC_END
//	return ret;
//}



///**
//* \brief
//* Verilen dataya BES imzas� atar.
//*
//* \param aData
//* �mzalanacak Veri
//*
//* \param iSignParam
//* �mzalama Parametrelesi
//*
//* \param oSignedData
//* �mzal� Verinin kopyalanaca�� yap�
//*
//*/
//int ECMSUtil::signBES(const QByteArray & iData,  const SignParam & iSignParam , QByteArray & oSignedData)
//{
//	CMS_FUNC_BEGIN

//	int ret = _signBES(iData, iSignParam, oSignedData);

//	CMS_FUNC_END
//	return ret;
//}

///**
//* \brief
//* Verilen dosyaya BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::signFileBES(const QString & iDataFile,  const ECertificate & iCert, const AnahtarBilgisi &iAB ,const QString & oSignedDataFile ,bool iAyrikImza)
//{
//	CMS_FUNC_BEGIN

//	int ret = _signFileBES(iDataFile, iCert, iAB ,oSignedDataFile,iAyrikImza);

//	CMS_FUNC_END
//	return ret;
//}


///**
//* \brief
//* Verilen dosyaya BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iSignParam
//* �mzalama Parametreleri
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::signFileBES(const QString & iDataFile,  const SignParam & iSignParam ,const QString & oSignedDataFile)
//{
//	CMS_FUNC_BEGIN

//	int ret = _signFileBES(iDataFile, iSignParam, oSignedDataFile );
	
//	CMS_FUNC_END
//	return ret;
//}

///**
//* \brief
//* Verilen dosyaya BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::constructSSD(QIODevice * iPDStream, QIODevice * iSDStream, const ECertificate & iCert, const AnahtarBilgisi & iAB, bool iAyrikImza)
//{
//	CMS_FUNC_BEGIN

//	StreamedSignedData signedData;
//	SignParam sp;
//	sp.addSignerParam(SignerParam(SignerInfo::DEFAULT_VERSION,iCert,SignedData::DEFAULT_DIGEST_ALGORITHM,iAB));
//	sp.setAyrikImza(iAyrikImza);
//	//signedData.addParallelBESSigners(sp, *iPDStream);
//	signedData.constructSSD(iPDStream,iSDStream,&sp);

//	CMS_FUNC_END
//	return SUCCESS;
//}


///**
//* \brief
//* Verilen imzal� dosyan�n ilk paralel imzac�s�n�n sonuna seri BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::seriImzaciEkle(const QString &  iPDFile, const QString& iSDFile, const ECertificate & iCert, const AnahtarBilgisi & iAB,const AlgorithmIdentifier & iDigestAlg)
//{
//	CMS_FUNC_BEGIN;

//	StreamedSignedData signedData(iSDFile,NULL);

//	SignerInfo* pLSS = signedData.lastSerialSigner(NULL);

//	if (!pLSS)
//		return FAILURE;
	

//	pLSS->addSerialBESSigner(SignedData::DEFAULT_VERSION,iAB,iCert,iDigestAlg);

//	QFile* pPDStream = NULL;
//	QFile sdStream(iSDFile);
	
//	sdStream.open(QIODevice::WriteOnly);

//	if (!iPDFile.isEmpty())
//	{
//		pPDStream = new QFile(iPDFile);
//		pPDStream->open(QIODevice::ReadOnly);
//	}

//	signedData.constructSSD(pPDStream,&sdStream,NULL);
	
//	pPDStream->close();
//	sdStream.close();
//	DELETE_MEMORY(pPDStream);

//	CMS_FUNC_END
//	return SUCCESS;
//}


///**
//* \brief
//* Verilen imzal� dosyan�n ilk paralel imzac�s�n�n sonuna seri BES imzas� atar.
//*
//* \param aDataFile
//* �mzalanacak Veri Dosyas�
//*
//* \param iCert
//* �mzalayan Sertifika
//*
//* \param iAB
//* �mzalama s�ras�nda kullan�lacak Kapal� Anahtar Bilgisi
//*
//* \param oSignedDataFile
//* �mzal� Verinin kopyalanaca�� dosya
//*
//*/
//int ECMSUtil::seriImzaciEkle(QByteArray& ioSignedData, const ECertificate & iCert, const AnahtarBilgisi & iAB,const AlgorithmIdentifier & iDigestAlg)
//{
//	CMS_FUNC_BEGIN;

//	SignedData signedData = SignedData::fromContentInfo(ioSignedData);

//	SignerInfo* pLSS = signedData.lastSerialSigner(NULL);

//	if (!pLSS)
//		return FAILURE;


//	pLSS->addSerialBESSigner(SignedData::DEFAULT_VERSION,iAB,iCert,iDigestAlg);

//	ContentInfo signedContent(signedData.getEncodedBytes(),CMS_id_signedData);
//	ioSignedData = signedContent.getEncodedBytes();

//	CMS_FUNC_END
//	return SUCCESS;
//}



///**
//* \brief
//* Verilen imzal� verinin imzas�n� do�rular.
//*
//* \param iSignedContent
//* �mzal� Veri
//*
//* \param iCertificates
//* Do�rulamada kullan�lacak sertifikalar�n listesi.
//*
//*
//*/
//bool ECMSUtil::verifySignedData(const QByteArray & iSignedContent, const QList<ECertificate>& iCertificates )
//{
//	CMS_FUNC_BEGIN

//	bool ret = false;
//	SignedData signedData = SignedData::fromContentInfo(iSignedContent);
//	ret = signedData.verifyData(iCertificates);
//	if (ret == false)
//	{
//		ERRORLOGYAZ(ESYACMS_MOD,"verifySignedData() : Imzal� veri do�rulanamad�.");
//		return false;
//	}

//	CMS_FUNC_END
//	return true;
//}


///**
//* \brief
//* Verilen imzal� dosyan�n imzas�n� do�rular.
//*
//* \param iPlainDataFile
//* �mzal� dosyan�n i�indeki verinin okunup yaz�laca�� dosya ad�
//*
//* \param iPlainDataFile
//* �mzal� dosya ad�
//*
//* \param iCertificates
//* Do�rulamada kullan�lacak sertifikalar�n listesi.
//*
//*
//*/
//bool ECMSUtil::verifySignedFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates )
//{
//	CMS_FUNC_BEGIN

//	bool ret = false;
//	StreamedSignedData ssd(iSignedFile);
//	ret = ssd.verifyFile(iCertificates);
//	if (ret == false)
//	{
//		ERRORLOGYAZ(ESYACMS_MOD,"verifySignedData() : Imzal� veri do�rulanamad�.");
//		return false;
//	}

//	CMS_FUNC_END
//	return true;
//}


//void ECMSUtil::verifyBESData(const QByteArray & iSignedContent, const QList<ECertificate>& iCertificates ,ImzaDogrulamaSonucu & oSonuc, const QByteArray & iPlainData)
//{
//	CMS_FUNC_BEGIN

//	SignedData sd = SignedData::fromContentInfo(iSignedContent);
//	verifyBES(sd,iCertificates,iPlainData,oSonuc);

//	CMS_FUNC_END
//}

/**
* \brief
* �mza do�rulamas� yapar
*
* \param 		const SignedData & iSD
* �mzal� veri nesnesi
*
* \param 		const QList<ECertificate> & iCertificates
* �mza Do�rulamada kullan�lacak sertifika listesi
*
* \param 		const QByteArray & iPlainData
* �mzalanm�� metin (plaindata) 
*
* \param 		ImzaDogrulamaSonucu & oSonuc
* �mza Do�rulama Sonucu
*
* \throws 		
*
* \remark
*
* \return   	void
*/
void ECMSUtil::verifyBES(const SignedData & iSD , const QList<ECertificate>& iCertificates, const QByteArray & iPlainData ,ImzaDogrulamaSonucu & oSonuc )
{
	CMS_FUNC_BEGIN

	bool ret = false;
	// IMZALI �ZELLIKLER KONTROLCUSU
	ParametreListesi sakParams;
	QList<ASN1TObjId> incAttributes;
	incAttributes.append(CMS_id_contentType);
	incAttributes.append(CMS_id_messageDigest);
	incAttributes.append(PKCS7_id_aa_signingCertificate);


	QVariant incAttrsParam;
	incAttrsParam.setValue(incAttributes);
	sakParams.parametreEkle(PN_SIGNED_ATTR_INC_SET,incAttrsParam);

	ParametreListesi okParams;
	if (iSD.isStreamed())
		okParams.parametreEkle(PN_PLAINDATAFILENAME,QVariant(iPlainData));
	else okParams.parametreEkle(PN_PLAINDATA,QVariant(iPlainData));
	


	ImzaDogrulamaAlgoritmasi verifier(iCertificates);
    verifier.addParalelKontrolcu(new ImzaKriptoKontrolcu(&verifier));
	verifier.addParalelKontrolcu(new OzetKontrolcu(&verifier,okParams));
	//verifier.addParalelKontrolcu(new SignedAttributesKontrolcu(&verifier,sakParams));

    verifier.addSeriKontrolcu(new ImzaKriptoKontrolcu(&verifier));
	verifier.addSeriKontrolcu(new OzetKontrolcu(&verifier));

	verifier.kontrolYap(iSD,oSonuc);

	CMS_FUNC_END
}


///**
//* \brief
//* �mzal� dosya imzas�n� do�rular
//*
//* \param 		const QString & iSignedFile
//* �mzal� Dosya Yolu
//*
//* \param 		const QList<ECertificate> & iCertificates
//* �mza Do�rulamada kullan�lacak sertifika listesi
//*
//* \param 		ImzaDogrulamaSonucu & oSonuc
//* �mza Do�rulama Sonucuc
//*
//* \param 		QIODevice * oPDStream
//* �mzalanm�� verinin (plaindata) ��kar�laca�� dosya streami
//*
//* \throws
//*
//* \remark
//* oPDStream NULL verilirse sadece do�rulama yapar.
//*
//* \return   	void
//*/
//void ECMSUtil::verifyBESFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates , ImzaDogrulamaSonucu & oSonuc ,  QIODevice* oPDStream  )
//{
//	CMS_FUNC_BEGIN

//	StreamedSignedData ssd(iSignedFile,oPDStream);
//	verifyBES(ssd,iCertificates,QByteArray(),oSonuc);

//	CMS_FUNC_END
//}

///**
//* \brief
//* �mzal� dosya imzas�n� do�rular
//*
//* \param 		const QString & iSignedFile
//* �mzal� Dosya Yolu
//*
//* \param 		const QList<ECertificate> & iCertificates
//* �mza Do�rulamada kullan�lacak sertifika listesi
//*
//* \param 		ImzaDogrulamaSonucu & oSonuc
//* �mza Do�rulama Sonucuc
//*
//* \param 		const QString & oPDFile
//* �mzalanm�� verinin (plaindata) ��kar�laca�� dosya yolu
//*
//* \throws
//*
//* \remark
//* oPDStream NULL verilirse sadece do�rulama yapar.
//*
//* \return   	void
//*/
//void ECMSUtil::verifyBESFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates , ImzaDogrulamaSonucu & oSonuc ,  const QString& iPDFile  )
//{
//	CMS_FUNC_BEGIN

//	StreamedSignedData ssd(iSignedFile,NULL);
//	verifyBES(ssd,iCertificates,iPDFile.toLocal8Bit(),oSonuc);

//	CMS_FUNC_END
//}


///**
//* \brief
//* Verilen asimetrik �ifreli veriyi ��zer
//*
//* \param iEnvelopedData
//* �ifreli Veri
//*
//* \param iCert
//* Verinin kendisine �ifrelendi�i sertifika
//*
//* \param iAB
//* �ifre ��zerken  kullan�lacak Kapal� Anahtar Bilgisi
//*
//*
//*/
//QByteArray ECMSUtil::decryptEnvelopedData(const QByteArray& iEnvelopedData, const ECertificate &iCert, const OzelAnahtarBilgisi & iAB )
//{
//	CMS_FUNC_BEGIN

//	ContentInfo cInfo(iEnvelopedData);
//	if (cInfo.getContentType() != (ASN1TObjId)CMS_id_envelopedData )
//	{
//		throw ECMSException("Kaynak veri EnvelopedData yap�s�nda de�il.",__FILE__,__LINE__);
//	}
//	EnvelopedData envData(cInfo.getContent(),true);
//	QByteArray plainData = envData.decryptData(iCert,iAB);

//	CMS_FUNC_END
//	return plainData;
//}


///**
//* \brief
//* Verilen parola tabanl� �ifreli veriyi ��zer
//*
//* \param iEnvelopedData
//* �ifreli Veri
//*
//* \param iParola
//* Verinin kendisine �ifrelendi�i parola
//*
//*/
//QByteArray ECMSUtil::decryptEnvelopedData(const QByteArray& iEnvelopedData, const QString &iParola )
//{
//	CMS_FUNC_BEGIN
	
//	ContentInfo cInfo(iEnvelopedData);
//	if (cInfo.getContentType() != (ASN1TObjId)CMS_id_envelopedData )
//	{
//		throw ECMSException("Kaynak veri EnvelopedData yap�s�nda de�il.",__FILE__,__LINE__);
//	}
//	EnvelopedData envData(cInfo.getContent(),true);
//	QByteArray plainData = envData.decryptData(iParola);

//	CMS_FUNC_END
//	return plainData;
//}


///**
//* \brief
//* Verilen asimetrik �ifreli dosyay� ��zer
//*
//* \param iEnvDataFileName
//* Asimetrik �ifreli Veri dosyas�
//*
//* \param iEncDataFileName
//* Asimetrik �ifreli verinin i�indeki Simetrik �ifreli k�s�m
//*
//* \param iPlainDataFileName
//* �ifresiz veri dosyas�
//*
//*
//*/
//int ECMSUtil::decryptEnvelopedFile(	const QString& iEnvDataFileName,
//									const QString& iPlainDataFileName,
//									const ECertificate& iCert,
//									const OzelAnahtarBilgisi& iAB )
//{
//	CMS_FUNC_BEGIN
	
//	EASNFileInputStream* edStream = EASNStreamingUtils::createFileInputStream(iEnvDataFileName);
//	QFile pdStream(iPlainDataFileName);
//	pdStream.open(QIODevice::WriteOnly);
//	StreamEnvelopedData sED(edStream,&pdStream,iCert,AnahtarBilgisi(iAB,AcikAnahtarBilgisi(iCert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes())));
//	DELETE_MEMORY(edStream);

//	CMS_FUNC_END
//	return SUCCESS;
//}


///**
//* \brief
//* Verilen parola tabanl�  �ifreli dosyay� ��zer
//*
//* \param iEnvDataFileName
//* Asimetrik �ifreli Veri dosyas�
//*
//* \param iPlainDataFileName
//* �ifresiz veri dosyas�
//*
//* \param iParola
//* Dosya Parolas�
//*
//*
//*/
//int ECMSUtil::decryptEnvelopedFile(	const QString& iEnvDataFileName,
//									const QString& iPlainDataFileName,
//									const QString& iParola )
//{
//	CMS_FUNC_BEGIN

//	EASNFileInputStream * edStream = EASNStreamingUtils::createFileInputStream(iEnvDataFileName);
//	QFile pdStream(iPlainDataFileName);
//	pdStream.open(QIODevice::WriteOnly);
//	StreamEnvelopedData sED(edStream,&pdStream,iParola);
//	DELETE_MEMORY(edStream);
	
//	CMS_FUNC_END
//	return SUCCESS;
//}



///**
//* \brief
//* Asimetrik �ifreli Dosya olu�turur
//*
//* \param iPlainDataFileName
//* �ifrelenecek Veri Dosyas�
//*
//* \param iEncDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iEnvDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iCert
//* �ifrelemede kullan�lacak sertifika
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//StreamEnvelopedData * ECMSUtil::createEnvelopedFile(	const ECertificate & iCert,
//														const AlgorithmIdentifier& iContEncAlg
//													)
//{
//	CMS_FUNC_BEGIN

//	QList<ECertificate> certList;
//	certList.append(iCert);
	
//	StreamEnvelopedData * pSED =  createEnvelopedFile( certList , iContEncAlg );
	
//	CMS_FUNC_END
//	return pSED;
//}

///**
//* \brief
//* Asimetrik �ifreli Dosya olu�turur
//*
//* \param iPlainDataFileName
//* �ifrelenecek Veri Dosyas�
//*
//* \param iEncDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iEnvDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iCert
//* �ifrelemede kullan�lacak sertifika
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//StreamEnvelopedData * ECMSUtil::createGroupEnvelopedFile(	const QList<ECertificate>& iCertList,
//															const AlgorithmIdentifier& iContEncAlg
//														)
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
		
//	const  AlgDetay & algDetay = AlgoritmaBilgileri::getAlgDetay(iContEncAlg);
//	QByteArray encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);

//	StreamEnvelopedData * pSED = new StreamEnvelopedData(encKey,contEncAlg);

//	pSED->addRecipientInfos(EnvelopedData::buildEGRIs(iCertList,encKey,1));

//	CMS_FUNC_END
//	return pSED;
//}


///**
//* \brief
//* Asimetrik �ifreli Dosya olu�turur
//*
//* \param iPlainDataFileName
//* �ifrelenecek Veri Dosyas�
//*
//* \param iEncDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iEnvDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iCert
//* �ifrelemede kullan�lacak sertifika
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//StreamEnvelopedData * ECMSUtil::createEnvelopedFile(	const QList<ECertificate>& iCertList,
//														const AlgorithmIdentifier& iContEncAlg
//													)
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
		
//	const  AlgDetay & algDetay = AlgoritmaBilgileri::getAlgDetay(iContEncAlg);
//	QByteArray encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);

//	StreamEnvelopedData * pSED = new StreamEnvelopedData(encKey,contEncAlg);

//	for (int i = 0 ; i<iCertList.size();i++ )
//	{
//		RecipientInfo rInfo;
//		bool rInfoBuilt = EnvelopedData::buildRecipientInfo(iCertList[i],encKey,rInfo) ;
//		if 	(rInfoBuilt)
//			pSED->addRecipientInfo(rInfo);
//	}

//	CMS_FUNC_END
//	return pSED;
//}


///**
//* \brief
//* Asimetrik �ifreli Dosya olu�turur
//*
//* \param iPlainDataFileName
//* �ifrelenecek Veri Dosyas�
//*
//* \param iEncDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iEnvDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iCert
//* �ifrelemede kullan�lacak sertifika
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
///*StreamEnvelopedData * ECMSUtil::createEnvelopedFile(	const QString & iEnvDataFilePath )
//{
//	CMS_FUNC_BEGIN

//	EASNFileInputStream *fileStream = EASNStreamingUtils::createFileInputStream(iEnvDataFilePath);
	
//	StreamEnvelopedData * pSED = new StreamEnvelopedData(fileStream);

//	CMS_FUNC_END
//	return pSED;
//}*/


///**
//* \brief
//* Asimetrik �ifreli Dosya olu�turur
//*
//* \param iPlainDataFileName
//* �ifrelenecek Veri Dosyas�
//*
//* \param iEncDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iEnvDataFileName
//* Simetrik �ifreli Veri Dosyas�
//*
//* \param iCert
//* �ifrelemede kullan�lacak sertifika
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//StreamEnvelopedData * ECMSUtil::createEnvelopedFile(	const QString& iParola,
//														const AlgorithmIdentifier& iContEncAlg  ,
//														const AlgorithmIdentifier& iKeyEncAlg  ,
//														const AlgorithmIdentifier& iKeyDrvAlg   )
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
//	const  AlgDetay & algDetay = AlgoritmaBilgileri::getAlgDetay(iContEncAlg);
//	QByteArray encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);


//	AlgorithmIdentifier keyEncAlg(iKeyEncAlg);
//	AlgDetay algDetayKEK	= AlgoritmaBilgileri::getAlgDetay(iKeyEncAlg);
//	QByteArray	ivKEK		= KriptoUtils::ivUret(algDetayKEK.getModAlgInfo().getBlokBoyu());
//	keyEncAlg.setParamsAsOctets(ivKEK);

//	AlgorithmIdentifier keyDrvAlg(iKeyDrvAlg);
//	PBKDF2_Params keyDerParams;
//	PBKDF2_Salt salt;
//	salt.setSpecified(KriptoUtils::ivUret(PBE_SALT_LENGTH));
//	keyDerParams.setSalt(salt);
//	keyDerParams.setIterationCount(PBE_ITERATION_COUNT);
//	keyDrvAlg.setParameters(keyDerParams.getEncodedBytes());


//	StreamEnvelopedData * pSED = new StreamEnvelopedData(encKey,contEncAlg,iParola);

//	ESYAPasswordRecipientInfo epwri(keyEncAlg,keyDrvAlg,encKey,iParola);
//	RecipientInfo rInfo( OtherRecipientInfo( ESYA_id_esyapwri,epwri.getEncodedBytes() ) );
//	pSED->addRecipientInfo(rInfo);

//	CMS_FUNC_END
//	return pSED;
//}



///**
//* \brief
//* Veriyi CMS zarf yap�s� i�erisinde asimetrik �ifreler
//*
//* \param 		const QByteArray & iPlainData
//* �ifrelenecek veri
//*
//* \param 		const QList<ECertificate> & iCertList
//* �ifreleme Sertifikalar� Listesi
//*
//* \param 		const AlgorithmIdentifier & iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \throws
//*
//* \remark
//*
//* \return   	QT_NAMESPACE::QByteArray
//*/
//QByteArray ECMSUtil::envelopeData(	const QByteArray			&iPlainData,
//									const QList<ECertificate>	&iCertList,
//									const AlgorithmIdentifier	&iContEncAlg )
//{
//	CMS_FUNC_TRY_BEGIN

//	EnvelopedData * pED = ECMSUtil::createEnvelopedData(iPlainData,iCertList,iContEncAlg);
//	ContentInfo cInfo(pED->getEncodedBytes(),CMS_id_envelopedData);
//	DELETE_MEMORY(pED)

//	QByteArray envData = cInfo.getEncodedBytes();

//	return envData;

//	CMS_FUNC_TRY_END
//}


///**
//* \brief
//* Veriyi CMS zarf yap�s� i�erisinde asimetrik �ifreler
//*
//* \param 		const QByteArray & iPlainData
//* �ifrelenecek veri
//*
//* \param 		const QList<ECertificate> & iCertList
//* �ifreleme Sertifikalar� Listesi
//*
//* \param 		const AlgorithmIdentifier & iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \throws
//*
//* \remark
//*
//* \return   	QT_NAMESPACE::QByteArray
//*/
//QByteArray ECMSUtil::groupEnvelopeData(	const QByteArray			&iPlainData,
//										const QList<ECertificate>	&iCertList,
//										const AlgorithmIdentifier	&iContEncAlg )
//{
//	CMS_FUNC_TRY_BEGIN

//	EnvelopedData * pED = ECMSUtil::createGroupEnvelopedData(iPlainData,iCertList,iContEncAlg);
//	ContentInfo cInfo(pED->getEncodedBytes(),CMS_id_envelopedData);
//	DELETE_MEMORY(pED)

//	QByteArray envData = cInfo.getEncodedBytes();

//	return envData;

//	CMS_FUNC_TRY_END
//}


///**
//* \brief
//* Veriyi CMS zarf yap�s� i�erisinde parola tabanl� �ifreler
//*
//* \param 		const QByteArray & iPlainData
//* �ifrelenecek veri
//*
//* \param 		const QString & iParola
//* �ifreleme Parolas�
//*
//* \param 		const AlgorithmIdentifier & iContEncAlg
//* Simetrik �ifreleme Anahtar�
//*
//* \param 		const AlgorithmIdentifier & iKeyEncAlg
//* PKCS-5 Anahtar �ifreleme Algoritmas�
//*
//* \param 		const AlgorithmIdentifier & iKeyDrvAlg
//* PKCS-5 Anahtar �retme Algoritmas�
//*
//* \throws
//*
//* \remark
//*
//* \return   	QT_NAMESPACE::QByteArray
//*/
//QByteArray ECMSUtil::envelopeData(	const QByteArray	&iPlainData,
//									const QString		&iParola,
//									const AlgorithmIdentifier & iContEncAlg ,
//									const AlgorithmIdentifier & iKeyEncAlg ,
//									const AlgorithmIdentifier & iKeyDrvAlg )
//{
//	CMS_FUNC_TRY_BEGIN

//	EnvelopedData * pED = ECMSUtil::createEnvelopedData(iPlainData,iParola,iContEncAlg,iKeyEncAlg,iKeyDrvAlg);
//	ContentInfo cInfo(pED->getEncodedBytes(),CMS_id_envelopedData);
//	DELETE_MEMORY(pED)
//	QByteArray envData = cInfo.getEncodedBytes();

//	return envData;

//	CMS_FUNC_TRY_END
//}

///**
//* \brief
//* Birden fazla sertifika i�in Asimetrik �ifreli Veri olu�turur
//*
//* \param iPlainData
//* �ifrelenecek Veri
//*
//* \param iCertList
//* �ifrelemede kullan�lacak sertifika listesi
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//EnvelopedData * ECMSUtil::createEnvelopedData(	const QByteArray& iPlainData,
//												const ECertificate & iCert,
//												const AlgorithmIdentifier& iContEncAlg )
//{
//	CMS_FUNC_BEGIN

//	QList<ECertificate> certList;
//	certList.append(iCert);
//	EnvelopedData * pED = createEnvelopedData(iPlainData, certList , iContEncAlg );

//	CMS_FUNC_END
//	return pED;
//}


///**
//* \brief
//* Birden fazla sertifika i�in Asimetrik �ifreli Veri olu�turur
//*
//* \param iPlainData
//* �ifrelenecek Veri
//*
//* \param iCertList
//* �ifrelemede kullan�lacak sertifika listesi
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//EnvelopedData * ECMSUtil::createEnvelopedData(	const QByteArray& iPlainData,
//												const QList<ECertificate>& iCertList ,
//												const AlgorithmIdentifier& iContEncAlg )
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
//	AlgDetay algDetay	= AlgoritmaBilgileri::getAlgDetay(contEncAlg);
//	QByteArray	encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray	iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);

//	EnvelopedData * pEnvData	= new EnvelopedData(iPlainData,iCertList,contEncAlg,encKey);
	
//	CMS_FUNC_END
//	return pEnvData;
//}


///**
//* \brief
//* Gruba Asimetrik �ifreli Veri olu�turur
//*
//* \param iPlainData
//* �ifrelenecek Veri
//*
//* \param iCertList
//* �ifrelemede kullan�lacak sertifika listesi
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyLen
//* Simetrik �ifreleme Anahtar Uzunlu�u
//*
//*/
//EnvelopedData * ECMSUtil::createGroupEnvelopedData(	const QByteArray& iPlainData,
//													const QList<ECertificate>& iCertList ,
//													const AlgorithmIdentifier& iContEncAlg )
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
//	AlgDetay algDetay	= AlgoritmaBilgileri::getAlgDetay(contEncAlg);
//	QByteArray	encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray	iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);

//	EnvelopedData * pEnvData	= new EnvelopedData(iPlainData,QList<ECertificate>(),contEncAlg,encKey);
	
//	pEnvData->addRecipientInfos(EnvelopedData::buildEGRIs(iCertList,encKey,1));

//	CMS_FUNC_END
//	return pEnvData;
//}


///**
//* \brief
//* Parola Tabanl� �ifreli Veri olu�turur
//*
//* \param iPlainData
//* �ifrelenecek Veri
//*
//* \param iParam
//* �ifrelemede kullan�lacak sertifika listesi
//*
//* \param iContEncAlg
//* Simetrik �ifreleme Algoritmas�
//*
//* \param iKeyEncAlg
//* Anahtar �ifreleme Algoritmas�
//*
//*
//* \param iKeyDrvAlg
//* Paroladan Anahtan Elde Etme Algoritmas�
//*
//*/
//EnvelopedData * ECMSUtil::createEnvelopedData(	const QByteArray& iPlainData,
//												const QString& iParola ,
//												const AlgorithmIdentifier& iContEncAlg,
//												const AlgorithmIdentifier& iKeyEncAlg,
//												const AlgorithmIdentifier& iKeyDrvAlg)
//{
//	CMS_FUNC_BEGIN

//	AlgorithmIdentifier contEncAlg(iContEncAlg);
//	AlgDetay algDetay	= AlgoritmaBilgileri::getAlgDetay(contEncAlg);
//	QByteArray	encKey	= KriptoUtils::simetrikKeyUret(algDetay.getSimetrikAlgInfo().getAnahtarBoyu());
//	QByteArray	iv		= KriptoUtils::ivUret(algDetay.getModAlgInfo().getBlokBoyu());
//	contEncAlg.setParamsAsOctets(iv);

//	AlgorithmIdentifier keyEncAlg(iKeyEncAlg);
//	AlgDetay algDetayKEK	= AlgoritmaBilgileri::getAlgDetay(iKeyEncAlg);
//	QByteArray	ivKEK		= KriptoUtils::ivUret(algDetayKEK.getModAlgInfo().getBlokBoyu());
//	keyEncAlg.setParamsAsOctets(ivKEK);

//	AlgorithmIdentifier keyDrvAlg(iKeyDrvAlg);
//	PBKDF2_Params keyDerParams;
//	PBKDF2_Salt salt;
//	salt.setSpecified(KriptoUtils::ivUret(PBE_SALT_LENGTH));
//	keyDerParams.setSalt(salt);
//	keyDerParams.setIterationCount(PBE_ITERATION_COUNT);
//	keyDrvAlg.setParameters(keyDerParams.getEncodedBytes());

//	EnvelopedData * pEnvData	= new EnvelopedData(iPlainData,iParola, encKey,contEncAlg,keyEncAlg,keyDrvAlg);

//	CMS_FUNC_END
//	return pEnvData;
//}

///**
//* \brief
//* Verilen asimetrik �ifreli dosyan�n �ifrecilerini0 d�ner
//*
//* \param iEnvDataFileName
//* Asimetrik �ifreli Veri dosyas�
//*
//*
//*/
//QList<RecipientInfo> ECMSUtil::getEnvelopedFileRecipients(	const QString& iEnvDataFileName )
//{
//	CMS_FUNC_BEGIN

//	EFileHandle fileHandle(iEnvDataFileName,"rb",false);
//	EASNFileInputStream* edStream= EASNStreamingUtils::createFileInputStream(fileHandle.getFilePtr());
//	try
//	{
//		StreamEnvelopedData sED(edStream);
//		QList<RecipientInfo> ret = sED.getRecipientInfos();
//		DELETE_MEMORY(edStream);

//		CMS_FUNC_END
//		return ret;
//	}
//	catch (EException& exc)
//	{
//		DELETE_MEMORY(edStream);
//		throw exc;
//	}

//}


///**
//* \brief
//* Verilen asimetrik �ifreli dosyan�n �ifrecilerini0 d�ner
//*
//* \param iEnvDataFileName
//* Asimetrik �ifreli Veri dosyas�
//*
//*
//*/
//int ECMSUtil::getEnvelopedFileRecipientIndex(	const QString& iEnvDataFileName ,const ECertificate& iCert)
//{
//	CMS_FUNC_BEGIN

//	EFileHandle fileHandle(iEnvDataFileName,"rb",false);
//	EASNFileInputStream* edStream= EASNStreamingUtils::createFileInputStream(fileHandle.getFilePtr());
//	try
//	{
//		StreamEnvelopedData sED(edStream);
//		int ri = sED.getRecipientIndeks(iCert);
//		DELETE_MEMORY(edStream);

//		CMS_FUNC_END
//		return ri;
//	}
//	catch (EException& exc)
//	{
//		DELETE_MEMORY(edStream);
//		throw exc;
//	}

//}

///**
//* \brief
//* Verilen asimetrik �ifreli dosyan�n �ifrecilerini0 d�ner
//*
//* \param iEnvDataFileName
//* Asimetrik �ifreli Veri dosyas�
//*
//*
//*/
//AlgorithmIdentifier ECMSUtil::getContentEncryptionAlgorithm( const QString& iEnvDataFileName )
//{
//	CMS_FUNC_BEGIN
//	EFileHandle fileHandle(iEnvDataFileName,"rb",false);
//	EASNFileInputStream* edStream= EASNStreamingUtils::createFileInputStream(fileHandle.getFilePtr());
//	try
//	{
//		StreamEnvelopedData sED(edStream);
//		AlgorithmIdentifier retAlg =  sED.getEncryptedContentInfo().getContentEncAlg();
//		DELETE_MEMORY(edStream);

//		CMS_FUNC_END
//		return retAlg;
//	}
//	catch (EException& exc)
//	{
//		DELETE_MEMORY(edStream);
//		throw exc;
//	}
//}



ECMSUtil::~ECMSUtil(void)
{
}

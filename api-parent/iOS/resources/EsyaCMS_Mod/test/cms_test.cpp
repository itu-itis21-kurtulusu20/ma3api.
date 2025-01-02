#include "cms_test.h"
#include "Kronometre.h"
#include "FileUtil.h"
#include "EsyaCMSManagedExport.h"
#include <QRegExp>

#include "EFileHandle.h"
#include "KriptoUtils.h"



using namespace esya;

/*
extern "C" Q_DECL_IMPORT int __stdcall besImzala(	const char* ipDllName, 
												 const char * ipLabel,
												 int iSlotNo, 
												 const char *ipKartParola,
												 const char * ipCertBytes,
												 int	iCertBytesSize,
												 const char * ipImzalanacak, 
												 int iImzalanacakSize,
												 char* opImzaSonucu,
												 int * ioImzaSonucuSize);

extern "C" Q_DECL_EXPORT int __stdcall besDosyaImzala(	const char* ipDllName, 
													  const char * ipLabel,
													  int iSlotNo, 
													  const char *ipKartParola,
													  const char * ipCertBytes,
													  int	iCertBytesSize,
													  const char * ipImzalanacakDosyaAdi, 
													  const char * ipImzaliDosyaAdi);

extern "C" Q_DECL_EXPORT int __stdcall denemeSimple();
*/

CMS_Test::CMS_Test(QObject *parent)
	: QObject(parent)
{

}

CMS_Test::~CMS_Test()
{

}



// QByteArray CMS_Test::loadFromFile(const QString & iFileName)
// {
// 	QByteArray bytes;
// 	QFile file(iFileName);
// 	if (!file.open(QIODevice::ReadOnly))
// 		return QByteArray();
// 
// 	bytes = file.readAll();
// 	file.close();
// 	return bytes;
// }


void CMS_Test::_cleanOutputFolder()
{
	FileUtil::deleteFolder(CMS_TEST_OUTPUTPATH,true);

	QDir dir;
	dir.mkpath(CMS_TEST_OUTPUTPATH);
}

void CMS_Test::init()
{
	_cleanOutputFolder();
}

void CMS_Test::cleanupTestCase()
{
	_cleanOutputFolder();
}


void CMS_Test::testStreamSignedDataObjectInDefLen()
{
	QString signedDataFilePath	= CMS_TEST_INPUT("IMZALI_indeflen.dat"); 

	try
	{
		StreamedSignedData ssd(signedDataFilePath);
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		FAIL;
	}	
}


void CMS_Test::testStreamSignedDataObjectDefLen()
{
	QString signedDataFilePath	= CMS_TEST_INPUT("IMZALI_deflen.dat"); 

	try
	{
		StreamedSignedData ssd(signedDataFilePath);
		PASS;
	}
	catch( EException & exc)
	{

		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}	


}




void CMS_Test::testSignDataParallel()
{
	QString signedDataFilePath	= CMS_TEST_OUTPUT("IMZALI_nostream_parallel.imz.txt"); 
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_sig.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_sig.dat");


	try
	{
		QByteArray signedData , data = FileUtil::readFromFile(plainDataFilePath);
		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 
		AcikAnahtarBilgisi aa(cert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes());
		AnahtarBilgisi ab(oa,aa);

		SignerParam signer(CMS_CMSVersion::v1 ,cert, AlgorithmIdentifier(ALGOS_sha_1) , ab );
		signer.setSMIMEAttributesIncluded(true);
		SignParam sp;
		sp.addSignerParam(signer);
		
		int res = ECMSUtil::signBES(data,sp,signedData);
		
		QVERIFY(res == SUCCESS);

		FileUtil::writeToFile(signedDataFilePath,signedData);
		
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}


void CMS_Test::testSignDataSerial()
{
	QString signedDataFilePath	= CMS_TEST_OUTPUT("IMZALI_nostream_serial.imz.txt"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_sig.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_sig.dat");

	try
	{
		QByteArray signedData , data = FileUtil::readFromFile(plainDataFilePath);
		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 
		AcikAnahtarBilgisi aa(cert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes());
		AnahtarBilgisi ab(oa,aa);
		AlgorithmIdentifier digestAlg(ALGOS_sha_1);


		int res = ECMSUtil::signBES(data,cert,ab,signedData);
		QVERIFY(res == SUCCESS);

		res = ECMSUtil::seriImzaciEkle(signedData,cert,ab,digestAlg);
		QVERIFY(res == SUCCESS);

		FileUtil::writeToFile(signedDataFilePath,signedData);
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}

void CMS_Test::testSignFileParallelSmall()
{
	QString signedDataFilePath	= CMS_TEST_OUTPUT("IMZALI_stream_parallel_small.imz.txt"); 
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_sig.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_sig.dat");

	try
	{
		ZAMANCI_BASLAT("testSignFileParallelSmall()")

		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 
		AcikAnahtarBilgisi aa(cert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes());
		AnahtarBilgisi ab(oa,aa);

		int res = ECMSUtil::signFileBES(plainDataFilePath,cert,ab,signedDataFilePath,false);

		QVERIFY(res == SUCCESS);

		ZAMANCI_BITIR("testSignFileParallelSmall()")
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}

void CMS_Test::testSignFileParallelLarge()
{
	QString signedDataFilePath	= CMS_TEST_OUTPUT("IMZALI_stream_parallel_large.imz.txt"); 
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_large.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_sig.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_sig.dat");

	try
	{
		ZAMANCI_BASLAT("testSignFileParallelLarge()")

		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 
		AcikAnahtarBilgisi aa(cert.getTBSCertificate().getSubjectPublicKeyInfo().getEncodedBytes());
		AnahtarBilgisi ab(oa,aa);

		int res = ECMSUtil::signFileBES(plainDataFilePath,cert,ab,signedDataFilePath,false);

		QVERIFY(res == SUCCESS);
		QVERIFY(QFile::exists(signedDataFilePath));

		ZAMANCI_BITIR("testSignFileParallelLarge()")
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}



void CMS_Test::testVerifySignedData()
{
	QString signedDataFilePath	= CMS_TEST_INPUT("IMZALI_small.dat");
	QString dsFilePath			= CMS_TEST_OUTPUT("dogrulamasonucu_nostream.txt");

	
	try
	{
		QByteArray signedData = FileUtil::readFromFile(signedDataFilePath) ;
		ImzaDogrulamaSonucu ids;
		ECMSUtil::verifyBESData(signedData,QList<ECertificate>(),ids);

		QVERIFY(ids.getDogrulamaSonucu() == ImzaDogrulamaSonucu::GECERLI);
		FileUtil::writeToFile(dsFilePath,ids.toString().toLocal8Bit());		
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}



void CMS_Test::testVerifySignedFileSmall()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_small.txt"); 
	QString signedDataFilePath	= CMS_TEST_INPUT("IMZALI_small.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("PLAIN_stream_small.txt");
	QString dsFilePath			= CMS_TEST_OUTPUT("dogrulamasonucu_stream_small.txt");


	try
	{
		ImzaDogrulamaSonucu ids;
		QFile pdFile(plainDataFilePath);
		pdFile.open(QIODevice::WriteOnly);
		ECMSUtil::verifyBESFile(signedDataFilePath,QList<ECertificate>(),ids,&pdFile );
		pdFile.close();

		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));
		QVERIFY(ids.getDogrulamaSonucu() == ImzaDogrulamaSonucu::GECERLI);
	
		FileUtil::writeToFile(dsFilePath,ids.toString().toLocal8Bit());	

		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}


void CMS_Test::testVerifySignedFileLarge()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_large.txt");	
	QString signedDataFilePath	= CMS_TEST_INPUT("IMZALI_large.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("PLAIN_stream_large.txt");
	QString dsFilePath			= CMS_TEST_OUTPUT("dogrulamasonucu_stream_large.txt");


	try
	{
		ZAMANCI_BASLAT("testVerifySignedFileLarge()");
		
		ImzaDogrulamaSonucu ids;
		QFile pdFile(plainDataFilePath);
		pdFile.open(QIODevice::WriteOnly);
		ECMSUtil::verifyBESFile(signedDataFilePath,QList<ECertificate>(),ids,&pdFile );
		FileUtil::writeToFile(dsFilePath,ids.toString().toLocal8Bit());	
		pdFile.close();

		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));

		ZAMANCI_BITIR("testVerifySignedFileLarge()");
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}


void CMS_Test::testCreateEnvelopedData()
{
	QString envDataFilePath		= CMS_TEST_OUTPUT("ENCRYPTED_nostream.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");


	try
	{
		ECertificate cert(certPath);
		QByteArray data = FileUtil::readFromFile(plainDataFilePath);
		EnvelopedData * pED = ECMSUtil::createEnvelopedData(data,cert,AlgorithmIdentifier(QByteArray(),ALGOS_des_EDE3_CBC));
		QByteArray content = pED->getEncodedBytes();
		ContentInfo cInfo(content,CMS_id_envelopedData);
		cInfo.write2File(envDataFilePath);
		DELETE_MEMORY(pED);
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}




void CMS_Test::testCreateEnvelopedFileSmall()
{

	QString envDataFilePath		= CMS_TEST_OUTPUT("ENCRYPTED_stream_small.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");

	try
	{
		ZAMANCI_BASLAT("testCreateEnvelopedFileSmall()");

		QList<ECertificate> certList; 
		certList.append(ECertificate(certPath));

		QFile pdFile(plainDataFilePath); pdFile.open(QIODevice::ReadOnly);
		QFile edFile(envDataFilePath); edFile.open(QIODevice::WriteOnly);
		
		StreamEnvelopedData *pSED = ECMSUtil::createEnvelopedFile(	certList,
																	AlgorithmIdentifier(ALGOS_des_EDE3_CBC) );
		
		pSED->constructSED(&pdFile,NULL,&edFile);

	
		pdFile.close();
		edFile.close();

		DELETE_MEMORY(pSED);

		ZAMANCI_BITIR("testCreateEnvelopedFileSmall()");
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}



void CMS_Test::testCreateEnvelopedFileLarge()
{
	QString envDataFilePath		= CMS_TEST_OUTPUT("ENCRYPTED_stream_large.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_large.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");

	try
	{
		ZAMANCI_BASLAT("testCreateEnvelopedFileLarge()");

		QList<ECertificate> certList; 
		certList.append(ECertificate(certPath));

		QFile pdFile(plainDataFilePath); pdFile.open(QIODevice::ReadOnly);
		QFile edFile(envDataFilePath); edFile.open(QIODevice::WriteOnly);

		StreamEnvelopedData *pSED = ECMSUtil::createEnvelopedFile(	certList,
			AlgorithmIdentifier(ALGOS_des_EDE3_CBC) );

		pSED->constructSED(&pdFile,NULL,&edFile);

		pdFile.close();
		edFile.close();

		DELETE_MEMORY(pSED);

		ZAMANCI_BITIR("testCreateEnvelopedFileLarge()");
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}





void CMS_Test::testDecryptEnvelopedData()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_small.txt"); 
	QString envDataFilePath		= CMS_TEST_INPUT("ENCRYPTED_small.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("DECRYPTED_nostream.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_enc.dat");	

	try
	{
		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 		

		QByteArray envData		= FileUtil::readFromFile(envDataFilePath);
		QByteArray plainData	= ECMSUtil::decryptEnvelopedData(envData,cert,oa);
		
		QByteArray verifyData	= FileUtil::readFromFile(testDataFilePath);

		QVERIFY(verifyData==plainData);


		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}

}


void CMS_Test::testDecryptEnvelopedFileSmall()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_small.txt"); 
	QString envDataFilePath		= CMS_TEST_INPUT("ENCRYPTED_small.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("DECRYPTED_stream_small.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_enc.dat");

	try
	{
		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 	

		int res = ECMSUtil::decryptEnvelopedFile(envDataFilePath,plainDataFilePath,cert,oa);
		QVERIFY(res == SUCCESS);
		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));

		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}

void CMS_Test::testDecryptEnvelopedFileLarge()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_large.txt");
	QString envDataFilePath		= CMS_TEST_INPUT("ENCRYPTED_large.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("DECRYPTED_stream_large.txt");
	QString certPath			= CMS_TEST_SERTIFIKA("cert_enc.cer");
	QString privKeyPath			= CMS_TEST_SERTIFIKA("pk_enc.dat");

	try
	{
		ZAMANCI_BASLAT("testDecryptEnvelopedFileLarge()");

		ECertificate cert(certPath);
		OzelAnahtarBilgisi oa(FileUtil::readFromFile(privKeyPath)); 	

		int res = ECMSUtil::decryptEnvelopedFile(envDataFilePath,plainDataFilePath,cert,oa);

		ZAMANCI_BITIR("testDecryptEnvelopedFileLarge()");

		QVERIFY(res== SUCCESS);
		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}



void CMS_Test::testCreatePBEEnvelopedData()
{
	QString envDataFilePath		= CMS_TEST_OUTPUT("PBE_ENCRYPTED_nostream.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");

	try
	{
		QByteArray	data	= FileUtil::readFromFile(plainDataFilePath);
		EnvelopedData * pED = ECMSUtil::createEnvelopedData(data,PBE_PASSWORD,AlgorithmIdentifier(QByteArray(),ALGOS_des_EDE3_CBC),PBE_DEFAULT_KEY_ENC_ALG,PBE_DEFAULT_KEY_DRV_ALG);
		QByteArray content = pED->getEncodedBytes();
		ContentInfo cInfo(content,CMS_id_envelopedData);
		DELETE_MEMORY(pED);
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}



void CMS_Test::testCreatePBEEnvelopedFileSmall()
{

	QString envDataFilePath		= CMS_TEST_OUTPUT("PBE_ENCRYPTED_stream_small.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_small.txt");

	try
	{
		QFile pdFile(plainDataFilePath); pdFile.open(QIODevice::ReadOnly);
		QFile edFile(envDataFilePath); edFile.open(QIODevice::WriteOnly);

		StreamEnvelopedData *pSED = ECMSUtil::createEnvelopedFile(	PBE_PASSWORD,
																	AlgorithmIdentifier(ALGOS_des_EDE3_CBC),
																	PBE_DEFAULT_KEY_ENC_ALG,
																	PBE_DEFAULT_KEY_DRV_ALG );

		pSED->constructSED(&pdFile,NULL,&edFile);

		edFile.close();
		pdFile.close();

		DELETE_MEMORY(pSED);

		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}


void CMS_Test::testCreatePBEEnvelopedFileLarge()
{

	QString envDataFilePath		= CMS_TEST_OUTPUT("PBE_ENCRYPTED_stream_large.m3s.dat"); //QString("%1deneme.m3s.dat").arg(TEST_PATH);
	QString plainDataFilePath	= CMS_TEST_INPUT("PLAIN_large.txt");

	try
	{
		ZAMANCI_BASLAT("testCreatePBEEnvelopedFileLarge()");
		
		QFile pdFile(plainDataFilePath); pdFile.open(QIODevice::ReadOnly);
		QFile edFile(envDataFilePath); edFile.open(QIODevice::WriteOnly);

		StreamEnvelopedData *pSED = ECMSUtil::createEnvelopedFile(	PBE_PASSWORD,
																	AlgorithmIdentifier(ALGOS_des_EDE3_CBC),
																	PBE_DEFAULT_KEY_ENC_ALG,
																	PBE_DEFAULT_KEY_DRV_ALG );

		pSED->constructSED(&pdFile,NULL,&edFile);

		pdFile.close();
		edFile.close();

		DELETE_MEMORY(pSED);
		ZAMANCI_BITIR("testCreatePBEEnvelopedFileLarge()");
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}




void CMS_Test::testDecryptPBEEnvelopedData()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_small.txt");
	QString envDataFilePath		= CMS_TEST_INPUT("PBE_ENCRYPTED_small.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("PBE_DECRYPTED_nostream.txt");

	try
	{
		QByteArray envData		= FileUtil::readFromFile(envDataFilePath);
		QByteArray plainData	= ECMSUtil::decryptEnvelopedData(envData,PBE_PASSWORD);
		
		QByteArray verifyData = FileUtil::readFromFile(testDataFilePath);
		
		QVERIFY(plainData == verifyData);
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}

}

void CMS_Test::testDecryptPBEEnvelopedFileSmall()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_small.txt");
	QString envDataFilePath		= CMS_TEST_INPUT("PBE_ENCRYPTED_small.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("PBE_DECRYPTED_stream_small.txt");

	try
	{
		int res = ECMSUtil::decryptEnvelopedFile(envDataFilePath,plainDataFilePath,PBE_PASSWORD);

		QVERIFY(res==SUCCESS);
		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}

void CMS_Test::testDecryptPBEEnvelopedFileLarge()
{
	QString testDataFilePath	= CMS_TEST_VERIFY("PLAIN_large.txt");
	QString envDataFilePath		= CMS_TEST_INPUT("PBE_ENCRYPTED_large.dat"); 
	QString plainDataFilePath	= CMS_TEST_OUTPUT("PBE_DECRYPTED_stream_large.txt");

	try
	{
		ZAMANCI_BASLAT("testDecryptPBEEnvelopedFileLarge()");

		int res = ECMSUtil::decryptEnvelopedFile(envDataFilePath,plainDataFilePath,PBE_PASSWORD);

		QVERIFY(res==SUCCESS);
		QVERIFY(FileUtil::fileCompare(testDataFilePath,plainDataFilePath));

		ZAMANCI_BITIR("testDecryptPBEEnvelopedFileLarge");
		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}
}

void CMS_Test::testASNError()
{
	//QString certPath			= CMS_TEST_SERTIFIKA("sertifika.dat");

	QString ciPath			= "e:/work/rmz/mio.dat";

	try
	{
		QByteArray	ciBytes		= FileUtil::readFromFile(ciPath);
		QByteArray	plainBytes	= FileUtil::readFromFile("e:/work/rmz/mioplain.dat");		

		ImzaDogrulamaSonucu ids;
// 		ECMSUtil::verifyBESData(ciBytes,QList<ECertificate>(),ids,plainBytes);
// 
// 		ImzaDogrulamaSonucu aids = ids.getAltDogrulamaSonuclari()[0];
// 
// 		KontrolcuSonucu ks1 = aids.getKontrolDetaylari()[0];
// 		KontrolcuSonucu ks2 = aids.getKontrolDetaylari()[1];

		SignedData sd = SignedData::fromContentInfo(ciBytes);
		QByteArray signature = sd.getSignerInfos()[0].getSignature();
		ECertificate cert = sd.getCertificates()[0].getCertificate();
		
		bool verified =  KriptoUtils::verifySignature(cert,plainBytes,signature,sd.getSignerInfos()[0].getDigestAlgorithm());
		
		cert.write2File("e:/work/mioCert.cer");


		PASS;
	}
	catch( EException & exc)
	{
		PRINT_OUT_EXCEPTION(exc);
		ERRORLOGYAZ(MODULE_NAME,exc.printStackTrace());
		FAIL;
	}

}






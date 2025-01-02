#ifndef CMS_TEST_H
#define CMS_TEST_H

#include <QObject>
#include <QtTest>
#include "ECMSUtil.h"
#include <iostream>


#define ZAMANCI_BASLAT(x) Kasio::getInstance()->start(x);
#define ZAMANCI_BITIR(x)  Kasio::getInstance()->pause(x);  Kasio::printToDebug(x);


#define PRINT_OUT_EXCEPTION(EXC)	std::cout<<"\n\n\n";\
									std::cout<<EXC.printStackTrace().toLocal8Bit().constData();



#define PASS 	QVERIFY(true)
#define FAIL 	QVERIFY(false)

#define CMS_TEST_PATH "../../test/testData/"
#define MAKE_TEST_PATH(x) QString("%1"x).arg(CMS_TEST_PATH)

#define CMS_TEST_INPUTPATH		QString("%1inputs").arg(CMS_TEST_PATH)
#define CMS_TEST_INPUT(x)		QString("%1/"x).arg(CMS_TEST_INPUTPATH)

#define CMS_TEST_OUTPUTPATH		QString("%1outputs").arg(CMS_TEST_PATH)
#define CMS_TEST_OUTPUT(x)		QString("%1/"x).arg(CMS_TEST_OUTPUTPATH)

#define CMS_TEST_VERIFYPATH		QString("%1verify").arg(CMS_TEST_PATH)
#define CMS_TEST_VERIFY(x)		QString("%1/"x).arg(CMS_TEST_VERIFYPATH)

#define CMS_TEST_SERTIFIKAPATH	QString("%1sertifika").arg(CMS_TEST_PATH)
#define CMS_TEST_SERTIFIKA(x)	QString("%1/"x).arg(CMS_TEST_SERTIFIKAPATH)




#define MODULE_NAME "CMS_TEST"

#define PBE_PASSWORD "PASSWORD"

#define PBE_KEY_ENC_ALG AlgorithmIdentifier()
#define PBE_KEY_DRV_ALG AlgorithmIdentifier()

class CMS_Test : public QObject
{
	Q_OBJECT

public:
    CMS_Test(QObject *parent);
    ~CMS_Test();

	//QByteArray loadFromFile(const QString & iFileName);

	void CMS_Test::_cleanOutputFolder();

private slots:

	void init();
	void cleanupTestCase();

	void testSignDataParallel();
	
	void testASNError();

	void testDecryptEnvelopedData();
	void testDecryptEnvelopedFileSmall();
	void testDecryptEnvelopedFileLarge();		

	void testCreateEnvelopedFileSmall();
	void testCreateEnvelopedFileLarge();	
	void testCreateEnvelopedData();

	void testDecryptPBEEnvelopedData();
	void testDecryptPBEEnvelopedFileSmall();
	void testDecryptPBEEnvelopedFileLarge();	

	void testCreatePBEEnvelopedData();	
	void testCreatePBEEnvelopedFileSmall();
	void testCreatePBEEnvelopedFileLarge();

	void testStreamSignedDataObjectDefLen();
	void testStreamSignedDataObjectInDefLen();

	void testVerifySignedData();	
	void testVerifySignedFileSmall();
	void testVerifySignedFileLarge();

	void testSignDataSerial();
	void testSignFileParallelSmall();
	
	void testSignFileParallelLarge();	

	//////////////////////////////////////////////////////////////////////////
	
};

#endif // CMS_TEST_H

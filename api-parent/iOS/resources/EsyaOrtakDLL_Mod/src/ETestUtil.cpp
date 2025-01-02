
#include "ETestUtil.h"
#include "QDateTime"
#include "Kronometre.h"
#include <iostream>
#include <QtTest>

NAMESPACE_BEGIN(esya)

void esyaTest(QObject *pTestObject, const char *iTestName , int argc, char **argv)
{
	QDateTime once = QDateTime::currentDateTime();
	QString format("dd MMM yyyy hh:mm:ss.zzz");
	std::cout << std::endl << std::endl << std::endl;
	std::cout << "[" << iTestName << "] baslangic :" << (const char *)once.toString(format).toAscii().constData() << std::endl;

	QTest::qExec(pTestObject,argc,argv);

	QDateTime sonra = QDateTime::currentDateTime();
	std::cout << "[" << iTestName << "] bitis     :" << (const char *)sonra.toString(format).toAscii().constData() << std::endl;
	int ms = once.time().msecsTo(sonra.time());
	int sn = ms / 1000;
	ms -= sn * 1000;
	int dk = sn/60;
	sn -= dk*60;

	std::cout << "[" << iTestName << "] "<< dk << " dk " << sn << " sn " << ms << " ms surmus.... ------------------------------" << std::endl;
}

NAMESPACE_END

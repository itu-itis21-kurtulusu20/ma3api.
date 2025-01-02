
#include "EFileLockerTest.h"
#include <QFile>
#include <iostream>

EFileLockerTest::EFileLockerTest()
{

}

EFileLockerTest::~EFileLockerTest()
{

}

#define lockDosya1 "kilitlenecekDosya1.txt"
#define lockDosya2 "kilitlenecekDosya2.txt"

void EFileLockerTest::initTestCase()
{
	//locklanacak dosya olusturalim.
	QFile dosya1(lockDosya1);
	dosya1.open(QIODevice::WriteOnly);
	dosya1.write("kilitlenecek ilk dosya\n oluyorlar kendileri\n",43);
	dosya1.close();

	QFile dosya2(lockDosya2);
	dosya2.open(QIODevice::WriteOnly);
	dosya2.write("kilitlenecek ikinci dosya\n oluyorlar kendileri\n",46);
	dosya2.close();
}

void EFileLockerTest::genelTestler()
{


#if defined(WIN32)

	EFileLocker kilit1(lockDosya1);
	EFileLocker kilit2(lockDosya2);
	EFileLocker kilit1ileAyni(lockDosya1);

	//////////////////////////////////////////////////////////////////////////
	QVERIFY(kilit1.lock());

	QVERIFY(!kilit1ileAyni.lock());
	QVERIFY(kilit1ileAyni.unlock());

	QVERIFY(kilit1.unlock());
	//////////////////////////////////////////////////////////////////////////
	QVERIFY(kilit1ileAyni.lock());
	QVERIFY(kilit1ileAyni.unlock());
	//////////////////////////////////////////////////////////////////////////
	QVERIFY(kilit1.lock());

	QVERIFY(kilit2.unlock());
	QVERIFY(kilit2.lock());
	QVERIFY(kilit2.unlock());

	QVERIFY(kilit1.unlock());
	//////////////////////////////////////////////////////////////////////////
	QVERIFY(kilit1.lock());
	QVERIFY(kilit1.lock());
	QVERIFY(kilit1.unlock());
	//////////////////////////////////////////////////////////////////////////
	//Destructor calistiginda unlock ediyor mu?
	{
		EFileLocker kilit1yeniden(lockDosya1);
		QVERIFY(kilit1yeniden.lock());
	}
	QVERIFY(kilit1ileAyni.lock());
	QVERIFY(kilit1ileAyni.unlock());


#else
    QWARN("Linux'da, EFileLocker kullanilarak, bir dosya tekrar locklanamiyor!");
    QWARN("Dolayisiyla bu test anlamsiz!");

#endif

}

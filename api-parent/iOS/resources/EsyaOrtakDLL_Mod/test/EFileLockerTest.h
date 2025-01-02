#ifndef __EFILELOCKERTEST_H__
#define __EFILELOCKERTEST_H__

#include <QObject>
#include <QtTest>
#include "esyaOrtak.h"
#include "EFileLockerTest.h"
#include "EFileLocker.h"

using namespace esya;
class EFileLockerTest: public QObject
{
	Q_OBJECT
public:
	EFileLockerTest(void);
	~EFileLockerTest(void);

private slots:
	//void init(); // will be called before each testfunction is executed.
	//void cleanup(); // will be called after every testfunction.
	void initTestCase(); // will be called before the first testfunction is executed.

	void genelTestler();
};


#endif

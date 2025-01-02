#ifndef __TEST_E_PROCESS_INFO_H_
#define __TEST_E_PROCESS_INFO_H_
#include <QObject>
#include <QtTest>
#include "esyaOrtak.h"
#include "ELogger.h"

#ifdef WIN32 || WIN64
#include "EProcessInfo.h"
#endif

using namespace esya;
NAMESPACE_BEGIN(esya)

class TestEProcessInfo: public QObject
{
	Q_OBJECT
public:
	TestEProcessInfo(void);
	~TestEProcessInfo(void);

private slots:	
	void komutParamDosyalariAl();
	void testTumProcessleriAl();
	void programCalisiyorMuKontrol();
	void programinCalisanInfolariniAl();	
};

NAMESPACE_END
#endif

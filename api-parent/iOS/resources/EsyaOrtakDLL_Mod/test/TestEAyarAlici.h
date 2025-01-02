#ifndef _TEST_EAYARALICI_H_
#define _TEST_EAYARALICI_H_
#include <QtTest>
class TestEAyarAlici: public QObject
{
	Q_OBJECT
public:
	TestEAyarAlici(void);
	~TestEAyarAlici(void);
private slots:
	void testAyarListener();
	};
#endif
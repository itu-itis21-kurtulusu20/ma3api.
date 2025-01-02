#ifndef _TEST_EAYAR_KARTLAR_H_
#define _TEST_EAYAR_KARTLAR_H_
#include <QTest>
class TestEAyarKartlar : public QObject
{
	Q_OBJECT
public:
	TestEAyarKartlar(void);
	~TestEAyarKartlar(void);
private slots:
	void testTumKartlariAl();
};
#endif
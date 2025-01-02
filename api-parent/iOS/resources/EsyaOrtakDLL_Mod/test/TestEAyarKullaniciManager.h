#ifndef _TEST_EAYARKULLANICI_MANAGER_H_
#define _TEST_EAYARKULLANICI_MANAGER_H_
#include <QtTest>
class TestEAyarKullaniciManager: public QObject
{
	Q_OBJECT
public:
	TestEAyarKullaniciManager(void);
	~TestEAyarKullaniciManager(void);
private slots:
	void testKullaniciEkle();
	void testKullaniciSil();
};
#endif
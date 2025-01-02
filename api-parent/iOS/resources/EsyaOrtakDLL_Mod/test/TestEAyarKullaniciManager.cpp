#include "TestEAyarKullaniciManager.h"
#include "EAyarKullaniciManager.h"
using namespace esya;

TestEAyarKullaniciManager::TestEAyarKullaniciManager(void)
{
}

TestEAyarKullaniciManager::~TestEAyarKullaniciManager(void)
{

}

void TestEAyarKullaniciManager::testKullaniciEkle()
{
		bool sonuc = EAyarKullaniciManager::getInstance()->kullaniciEkle("test1@test.com");
}

void TestEAyarKullaniciManager::testKullaniciSil()
{
	bool sonuc = EAyarKullaniciManager::getInstance()->kullaniciSil("test1@test.com");
}

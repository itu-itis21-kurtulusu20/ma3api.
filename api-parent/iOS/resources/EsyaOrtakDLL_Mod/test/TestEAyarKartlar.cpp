#include "TestEAyarKartlar.h"
#include "EAyarKartlar.h"
using namespace esya;

TestEAyarKartlar::TestEAyarKartlar(void)
{
}

TestEAyarKartlar::~TestEAyarKartlar(void)
{
}


void TestEAyarKartlar::testTumKartlariAl()
{
	QList<EAyarKartlar> liste = EAyarKartlar::tumKartlariAl();
}
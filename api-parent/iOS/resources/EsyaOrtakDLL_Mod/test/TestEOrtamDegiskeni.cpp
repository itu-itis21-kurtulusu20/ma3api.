#include "TestEOrtamDegiskeni.h"
#include "EOrtamDegiskeni.h"

TestEOrtamDegiskeni::TestEOrtamDegiskeni()
	: QObject(NULL)
{

}

TestEOrtamDegiskeni::~TestEOrtamDegiskeni()
{

}

void TestEOrtamDegiskeni::testDegiskenliStrCoz()
{
	QString deger =  EOrtamDegiskeni::degiskenliStrCoz("[$HOMEDRIVE]\\Kurumsal\\[$COMPUTERNAME]");
}

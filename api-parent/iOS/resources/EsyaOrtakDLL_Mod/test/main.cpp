#include <QApplication>
#include "ETestUtil.h"

#include <iostream>
#include "EException.h"

#include "FileUtilTest.h"
#include "EFileLockerTest.h"
#include "TestILogger.h"
#include "TestEFilter.h"
#include "EApplicationManager.h"

#include "TestEProcessInfo.h"
#include "TestEAyarKullaniciManager.h"
#include "TestEAyarKartlar.h"
#include "TestEAyarLDAP.h"
#include "TestEOrtamDegiskeni.h"
#include "TestEVeritabani.h"
#include "TestEAyarAlici.h"
#include "TestScheduler.h"

using namespace esya;

int main(int argc, char *argv[])
{	
	EApplicationManager::qtApplicationBaslat();
	KERMEN_CHANGE_LOG_FILE_NAME("TestFGFG");
	ESYATEST(TestILogger);
	//QApplication a(argc, argv);

	ESYATEST(TestScheduler);
	

	ESYATEST(FileUtilTest);
	
	ESYATEST(TestEAyarAlici);
	
	ESYATEST(TestEVeritabani);

	ESYATEST(TestEOrtamDegiskeni);
	ESYATEST(TestEAyarLDAP);
	ESYATEST(TestEAyarKullaniciManager);
	ESYATEST(TestEAyarKartlar);
	try 
	{	
		FileUtil::FileType type =  FileUtil::getEntryTypeByNameIcon("c:\\is2");
	//	EApplicationManager::qtLibraryListesineSystem32At();
	//	EApplicationManager::calismaDiliBelirle();		
		//ESYATEST(TestEProcessInfo);
	//	ESYATEST(TestEAyarAcikDosyaManager);
	//	ESYATEST(EAyarlarTest);
		//ESYATEST(TestEFilter);
		//ESYATEST(TestILogger);
		/*ESYATEST(EFileLockerTest);
		
		*/

	}
	catch (char *st)
	{
		std::cout << "HATA:" << st << std::endl;
	}
	catch(EException &x)
	{
		std::cout << x.printStackTrace().toLocal8Bit().constData() << "\n";
	}
	catch(...)
	{
		std::cout<< "Bilinmeyen HATA!\n";
	}

	return 0;//a.exec();
}



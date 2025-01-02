
#include "TestEProcessInfo.h"
#include "EProcessInfo.h"

TestEProcessInfo::TestEProcessInfo()
{

}

TestEProcessInfo::~TestEProcessInfo()
{

}

void TestEProcessInfo::testTumProcessleriAl()
{
	#ifdef WIN32 || WIN64
	QMultiMap<QString,EProcessInfo> calisanlar = EProcessInfo::currentProcesses();	
	QStringList calisanlarListStr = calisanlar.keys();
	qDebug(qPrintable(calisanlarListStr.join("\n")));
	#endif

}
void TestEProcessInfo::programCalisiyorMuKontrol()
{
#ifdef WIN32 || WIN64
	bool calisiyorMu = EProcessInfo::programCalisiyorMu("explorer");
	if (calisiyorMu)
	{
		qDebug("Explorer cal���yor");
	}	
	else
	{
		qDebug("Explorer cal��m�yor");
	}
#endif
}
void TestEProcessInfo::programinCalisanInfolariniAl()
{
#ifdef WIN32 || WIN64
	QList<EProcessInfo> calisanlar = EProcessInfo::programinCalisanOrnekleri("explorer");
	qDebug("Calisan explorer cal�s�");
	foreach(EProcessInfo processInfo,calisanlar)
	{
		qDebug(qPrintable(processInfo.getKomutSatiri()));
	}
#endif
}

void TestEProcessInfo::komutParamDosyalariAl()
{
#ifdef WIN32 || WIN64
	QMultiMap<QString,EProcessInfo> calisanlar = EProcessInfo::currentCmdFiles();	
	QStringList calisanlarListStr = calisanlar.keys();
	while(calisanlarListStr.contains("c:\\esyaistemci.log",Qt::CaseInsensitive))
	{
		QThread::currentThread()->wait(3000);
		calisanlar = EProcessInfo::currentCmdFiles();	
		calisanlarListStr = calisanlar.keys();
	}
	
	qDebug(qPrintable(calisanlarListStr.join("\n")));
#endif
}

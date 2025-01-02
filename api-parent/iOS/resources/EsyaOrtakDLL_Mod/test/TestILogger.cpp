#include "TestILogger.h"
#include "ESqliteLogger.h"
#include "EFileLogger.h"
#include "ELoggerFactory.h"
#include "ELogger.h"
#include "EAyarlar.h"

using namespace esya;

TestILogger::TestILogger(QObject *parent)
	: QObject(parent)
{

}
void TestILogger::testCompositeLogger()
{
	//EAyarlar::kullaniciSil("a@ug.uekae");
	/*
	ECompositeLogger * lpLogger = ELoggerFactory::getLogger();
	QObject::connect(lpLogger,SIGNAL(logaEkle(const QString & ,const QString & ,int ,const QString & )),this,SLOT(onLogaEkle(const QString & ,const QString & ,int ,const QString & )));
	}	
*/
	QList<QPair<QString,QStringList> > logFileNameList = ELoggerFactory::getLogModulNames();
	for(int k=0;k<logFileNameList.size();k++)
	{
		QPair<QString,QStringList> logPair = logFileNameList.at(k);
		QString modulName = logPair.first;
		QStringList modulNames = logPair.second;
	}
	for (int k=0;k<100;k++)
	{
		DEBUGLOGYAZ(QString("EsyaTestModulu-%1").arg(k),"Bu bir deneme debug mesajidir")
	}
	ERRORLOGYAZ("","Bu error log")
}

void TestILogger::onLogaEkle(const QString & iModulAdi,const QString & iDosyaAdi,int iSatirNo,const QString & iLogMessage)
{
	QWARN("Girid");
}
TestILogger::~TestILogger()
{

}

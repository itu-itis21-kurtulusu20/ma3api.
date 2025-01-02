#include "TestEAyarAlici.h"
#include "EAyarAlici.h"
#include "EAyarTanimlari.h"

#include <QTimer>
#include <QEventLoop>

using namespace esya;

TestEAyarAlici::TestEAyarAlici(void)
{
}

TestEAyarAlici::~TestEAyarAlici(void)
{

}

void TestEAyarAlici::testAyarListener()
{
	int k=0;
	while (1)
	{
		k++;
		try
		{
			EAyarAlici alici(AYAR_SNF_LOG,AYAR_LOG_LOG_ESIK_DEGERI);
			EAyar ayar = alici.ayarBul(true);
			qDebug()<<ayar.getStringDeger()<<k;
			
			QEventLoop loop;
			QTimer::singleShot(500,&loop,SLOT(quit()));
			loop.exec();
		}
		catch (...)
		{
			qDebug("Ayar okunamadý");
		}
	}
}


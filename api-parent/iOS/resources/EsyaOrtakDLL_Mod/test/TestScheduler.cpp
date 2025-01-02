#include "TestScheduler.h"
#include "Scheduler.h"
#include <QEventLoop>
#include "MyTestSchedulerTask.h"
#include <QDebug>
#include "EException.h"

using namespace esya;

TestScheduler::TestScheduler(QObject *parent)
	: QObject(parent)
{

}
void TestScheduler::testOneADay()
{		
	try
	{
		QEventLoop loop;
		MyTestSchedulerTask myTask;
		Scheduler scheduler;
		scheduler.schedule("33 16 * * *",&myTask);
		scheduler.startScheduling();
		loop.exec();
	}	
	catch (EException &exc)
	{
		QString hata = exc.printStackTrace();
		qDebug()<<hata;
	}
	
}


TestScheduler::~TestScheduler()
{
}

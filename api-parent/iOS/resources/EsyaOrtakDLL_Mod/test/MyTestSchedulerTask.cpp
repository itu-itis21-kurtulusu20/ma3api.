#include "MyTestSchedulerTask.h"
#include <QDateTime>
#include <QDebug>

MyTestSchedulerTask::MyTestSchedulerTask(void)
{
}

MyTestSchedulerTask::~MyTestSchedulerTask(void)
{
}

void MyTestSchedulerTask::execute()
{
	QDateTime curDt=QDateTime::currentDateTime();
	qDebug()<<QString::fromLocal8Bit("Task Çalýþtý. Baþlama = %1").arg(curDt.toString());
}
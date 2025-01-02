#include "TestEVeritabani.h"

#include "EFilter.h"
#include "EVeritabani.h"
#include <QtConcurrentRun>

#include "EMemoryDBMapper.h"

using namespace esya;

#define VTPATH "d:/testVeritabani.esya"
#define TESTTABLE "TBL_Test"


TestEVeritabani::TestEVeritabani(QObject *parent)
	: QObject(parent)
{

} 

TestEVeritabani::~TestEVeritabani()
{

}


void TestEVeritabani::initTestCase()
{
	EVeritabani vt = EVeritabani::sqLiteVTUret(VTPATH);	

	try
	{
		if (!vt.getTableNameList().contains(TESTTABLE))
		{
			QString sqlStr = QString("CREATE TABLE %1( TNO INTEGER PRIMARY KEY, TText VARCHAR )").arg(TESTTABLE); 
			vt.sorguCalistir(sqlStr);
		}
		vt.sorguCalistir(QString("DELETE FROM %1").arg(TESTTABLE));	
	
		vt.dbKapat();
	}
	catch (EException&  exc)
	{
		qDebug(qPrintable(exc.printStackTrace()));	
		vt.dbKapat();
	}
	catch (...)
	{
		vt.dbKapat();
	}
}

void TestEVeritabani::multipleInsert()
{
	EVeritabani vt1 = EVeritabani::sqLiteVTUret(VTPATH);

	try
	{
		for (int i = 0 ; i<10 ; i++)
		{
			vt1.sorguCalistir(QString("INSERT INTO %1(TText) VALUES('test1from_%2')").arg(TESTTABLE).arg((long)QThread::currentThreadId()));
		}
		vt1.dbKapat();
	}
	catch (EException&  exc)
	{
		qDebug(qPrintable(exc.printStackTrace()));	
		vt1.dbKapat();
	}
	catch (...)
	{
		vt1.dbKapat();
	}
}

void TestEVeritabani::testMultipleInsertNoThread()
{
	TestEVeritabani::multipleInsert();
}

void TestEVeritabani::testMultipleInsertThread()
{
	QFuture<void> fList[10];

	for (int  i= 0 ; i<10 ; i++)
	{
		fList[i] = QtConcurrent::run(&TestEVeritabani::multipleInsert);
	}
	
	for (int  i= 0 ; i<10 ; i++)
	{
		fList[i].waitForFinished();
	}

}


void TestEVeritabani::multipleRead()
{
	EVeritabani vt1 = EVeritabani::sqLiteVTUret(VTPATH);
	EVeritabani vt2 = EVeritabani::sqLiteVTUret(VTPATH);
	EVeritabani vt3 = EVeritabani::sqLiteVTUret(VTPATH);

	try
	{
		for (int i = 0 ; i<10 ; i++)
		{
			vt1.sorguCalistir(QString("SELECT * FROM %1").arg(TESTTABLE));
			vt2.sorguCalistir(QString("SELECT * FROM %1").arg(TESTTABLE));
			vt3.sorguCalistir(QString("SELECT * FROM %1").arg(TESTTABLE));
		}
		vt1.dbKapat();vt2.dbKapat();vt3.dbKapat();
	}
	catch (EException&  exc)
	{
		qDebug(qPrintable(exc.printStackTrace()));	
		vt1.dbKapat();vt2.dbKapat();vt3.dbKapat();
	}
	catch (...)
	{
		vt1.dbKapat();vt2.dbKapat();vt3.dbKapat();
	}
}

void TestEVeritabani::testMultipleReadNoThread()
{
	multipleRead();
}

void TestEVeritabani::testMultipleReadThread()
{
	QFuture<void> fList[5];

	for (int  i= 0 ; i<5 ; i++)
	{
		fList[i] = QtConcurrent::run(&TestEVeritabani::multipleRead);
	}

	for (int  i= 0 ; i<5 ; i++)
	{
		fList[i].waitForFinished();
	}

}

void TestEVeritabani::testMemoryMapper()
{
	EVeritabani vt = EVeritabani::sqLiteVTUret("E:/prsnl/work/sertifikadeposu.svt");	
	
	EMemoryDBMapper dm;

	QSqlDatabase memDB = dm.mapToMemory(vt.database());

	QSqlQuery query(memDB);

	bool b = query.exec("SELECT * FROM SERTIFIKA");

	while (query.next())
	{
		QSqlRecord rec = query.record();
		QString sn = rec.value(1).toString();
		QString dn = rec.value(2).toString();
	}
}

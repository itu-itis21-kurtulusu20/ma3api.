#include <QCoreApplication>
#include <QApplication>
#include <QtTest>

#include "cms_test.h"


int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
	

	CMS_Test tester(&a);

	QTest::qExec(&tester,argc,argv); 	

	return a.exec();
}

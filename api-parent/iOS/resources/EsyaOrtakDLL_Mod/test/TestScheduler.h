#ifndef __TEST_SCHEDULER_H_
#define __TEST_SCHEDULER_H_

#include <QObject>
#include "QtTest"
#include <QString>

class TestScheduler : public QObject
{
	Q_OBJECT

public:
	TestScheduler(QObject *parent=0);
	~TestScheduler();

private:
private slots:
	void testOneADay();	
};

#endif

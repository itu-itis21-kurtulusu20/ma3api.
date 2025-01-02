#ifndef TESTEFILTER_H
#define TESTEFILTER_H

#include <QObject>

class TestEFilter : public QObject
{
	Q_OBJECT

public:
	TestEFilter(QObject *parent= NULL);
	~TestEFilter();

private:

private slots:
	void testSimpleUsage();
	
};

#endif // TESTEFILTER_H

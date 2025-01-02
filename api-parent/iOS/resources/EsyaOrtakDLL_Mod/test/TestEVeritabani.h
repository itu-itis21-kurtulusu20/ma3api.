#ifndef TESTEVERITABANI_H
#define TESTEVERITABANI_H

#include <QObject>

class TestEVeritabani : public QObject
{
	Q_OBJECT

public:
	TestEVeritabani(QObject *parent= NULL);
	~TestEVeritabani();

	static void multipleInsert();
	static void multipleRead();

private:

private slots:
	
	void initTestCase();

	void testMemoryMapper();

	void testMultipleInsertThread();
	void testMultipleReadThread();

	void testMultipleReadNoThread();
	void testMultipleInsertNoThread();



	
};

#endif // TESTEFILTER_H

#ifndef TESTEORTAMDEGISKENI_H
#define TESTEORTAMDEGISKENI_H

#include <QTest>

class TestEOrtamDegiskeni : public QObject
{
	Q_OBJECT

public:
	TestEOrtamDegiskeni();
	~TestEOrtamDegiskeni();

	private slots:
		void testDegiskenliStrCoz();
	
};

#endif // TESTEORTAMDEGISKENI_H

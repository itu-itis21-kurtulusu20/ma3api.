#ifndef TESTILOGGER_H
#define TESTILOGGER_H

#include <QObject>
#include "QtTest"
#include <QString>

class TestILogger : public QObject
{
	Q_OBJECT

public:
	TestILogger(QObject *parent=0);
	~TestILogger();

private:
private slots:
	void testCompositeLogger();
public slots:
void onLogaEkle(const QString & iModulAdi,const QString & iDosyaAdi,int iSatirNo,const QString & iLogMessage);
	
};

#endif // TESTILOGGER_H

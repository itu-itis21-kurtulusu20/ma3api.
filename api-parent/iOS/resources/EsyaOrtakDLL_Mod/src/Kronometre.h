#ifndef __KRONOMETRE_H__
#define __KRONOMETRE_H__

#include "QElapsedTimer/qelapsedtimer.h"
#include "esyaOrtak.h"
#include <QHash>
#include <QString>


NAMESPACE_BEGIN(esya)

class Q_DECL_EXPORT Kronometre
{
public:
	Kronometre(const QString &iName);

	void sifirla();
	void baslat();
	void durdur();
	QString toString();
	QString toAvgString();

private:
	QString mName;
	int mMiliSan;
	int mCalismaSayisi;
	QElapsedTimer mZaman;	

};

class Q_DECL_EXPORT Kasio
{
	static Kasio * mpInstance;
	Kasio(){};
public:
	Kronometre* get(const QString &iName);

	static Kasio * getInstance();	
	static void remove(const QString &iName);

	static void start(const QString &iName);
	static void pause(const QString &iName);	
	static QString toString(const QString &iName);
	static QString toAvgString(const QString &iName);
	static void printAvgToDebug(const QString &iName);
	static void printToDebug(const QString &iName);

	static void pauseAll();
	static void startAll();
private:
	QHash<QString,Kronometre *> mListe;
};

NAMESPACE_END

#endif


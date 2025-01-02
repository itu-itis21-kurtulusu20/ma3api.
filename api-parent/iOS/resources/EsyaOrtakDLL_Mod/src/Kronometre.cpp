#include "Kronometre.h"
#include <iostream>
#include <QtDebug>
#include "ESynchroniseManager.h"
#include "ESure.h"
#include "EsyaOrtakDLL_DIL.h"

#define KRTIMEFORMAT "dd MMM yyyy hh:mm:ss.zzz"
using namespace esya;

Kronometre::Kronometre(const QString &iName)
{
	mName = iName;
	sifirla();

	//std::cout << std::endl << std::endl << std::endl;
	//std::cout << "[" << iTestName << "] baslangic :" << (const char *)once.toString(format).toAscii().constData() << std::endl;
}

void Kronometre::sifirla()
{
	mMiliSan = 0;
	mCalismaSayisi = 0;
}

void Kronometre::baslat()
{	
	mZaman.start();
}

void Kronometre::durdur()
{
	mMiliSan += mZaman.elapsed();
	mCalismaSayisi ++;
}

QString Kronometre::toAvgString()
{	
	QString st = ("Kronometre "+mName+" %1").arg(mCalismaSayisi) + " defa calisti: ";
	ESure sure(mMiliSan);
	st+=QString("%1 : %2").arg(DIL_GECEN_SURE).arg(sure.toString());
	ESure sureOrtalama((double)mMiliSan / (double)mCalismaSayisi);
	st+=QString(". %1 %2 : %3").arg(DIL_ORTALAMA).arg(DIL_GECEN_SURE).arg(sureOrtalama.toString());	
	return st;
}

QString Kronometre::toString()
{
	QString st;
	ESure sure(mMiliSan);
	st+=QString("%1 : %2").arg(DIL_GECEN_SURE).arg(sure.toString());
	return st;
}


Kasio * Kasio::mpInstance=NULL;
Kasio * Kasio::getInstance()
{
	KERMEN_WORK_SYNCRONIZED;
	if(mpInstance == NULL)
	{
		mpInstance = new Kasio();
	}
	return mpInstance;
}

void Kasio::remove(const QString &iName)
{
	if (getInstance()->mListe.contains(iName))
	{
		Kronometre * pKronometre = getInstance()->mListe.value(iName);
		DELETE_MEMORY(pKronometre);
		getInstance()->mListe.remove(iName);	
	}
}

Kronometre* Kasio::get(const QString &iName)
{
	if(!mListe.contains(iName))
	{
		mListe[iName] = new Kronometre(iName);
	}

	return mListe[iName];
}

void Kasio::pause(const QString &iName)
{
	getInstance()->get(iName)->durdur();
}

void Kasio::start(const QString &iName)
{
	getInstance()->get(iName)->baslat();	
}

QString Kasio::toString(const QString &iName)
{
	return getInstance()->get(iName)->toString();
}

QString Kasio::toAvgString(const QString &iName)
{
	return getInstance()->get(iName)->toAvgString();
}

void Kasio::printAvgToDebug(const QString &iName)
{
	qDebug() << getInstance()->get(iName)->toAvgString();
}

void Kasio::printToDebug(const QString &iName)
{
	qDebug() << getInstance()->get(iName)->toString();
}

void Kasio::pauseAll()
{	
	QList<Kronometre * > tumKronometreler = getInstance()->mListe.values();
	for (int k=0;k<tumKronometreler.size();k++)
	{
		Kronometre * pKronometre = tumKronometreler.at(k);
		pKronometre->durdur();
	}
}

void Kasio::startAll()
{
	QList<Kronometre * > tumKronometreler = getInstance()->mListe.values();
	for (int k=0;k<tumKronometreler.size();k++)
	{
		Kronometre * pKronometre = tumKronometreler.at(k);		
		pKronometre->baslat();
	}
}
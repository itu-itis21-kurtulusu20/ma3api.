#include "ParametreListesi.h"

using namespace esya;

ParametreListesi::ParametreListesi(void)
{
}

ParametreListesi::ParametreListesi(const ParametreListesi& iPL)
{
	mParametreListesi = iPL.parametreListesiAl();
}

ParametreListesi& ParametreListesi::operator=(const ParametreListesi& iPL)
{
	mParametreListesi = iPL.parametreListesiAl();
	return *this;
}

ParametreListesi::ParametreListesi(const QHash<QString, QVariant> & aParametreListesi)
{
	mParametreListesi = aParametreListesi;
}
	
void ParametreListesi::parametreEkle(const QString & aParamIsmi, const QVariant & aParamDeger)
{
	mParametreListesi[aParamIsmi] = aParamDeger;
}

const QHash<QString,QVariant> & ParametreListesi::parametreListesiAl()const
{
	return mParametreListesi;
}

QHash<QString,QVariant> & ParametreListesi::parametreListesiAl()
{
	return mParametreListesi;
}

const QVariant ParametreListesi::parametreDegeriAl(const QString aParamIsmi) const 
{
	return mParametreListesi[aParamIsmi];
}

QString	ParametreListesi::parametreDegeriStringAl	(const QString &aParamIsmi) const
{
	return mParametreListesi[aParamIsmi].toString();
}

int	ParametreListesi::parametreDegeriIntegerAl(const QString &aParamIsmi) const
{
	return mParametreListesi[aParamIsmi].toInt();	
}

long ParametreListesi::parametreDegeriLongAl(const QString &aParamIsmi) const
{
	return mParametreListesi[aParamIsmi].toLongLong();
}

bool ParametreListesi::parametreDegeriBooleanAl(const QString &aParamIsmi) const
{
	return mParametreListesi[aParamIsmi].toBool();
}

QString	ParametreListesi::parametreDegeriStringAl	(const QString &aParamIsmi, const QString& iDefaultValue) const
{
	if (mParametreListesi.contains(aParamIsmi))
		return mParametreListesi[aParamIsmi].toString();
	else return iDefaultValue;
}

int		ParametreListesi::parametreDegeriIntegerAl(const QString &aParamIsmi, int iDefaultValue) const
{
	if (mParametreListesi.contains(aParamIsmi))
		return mParametreListesi[aParamIsmi].toInt();
	else return iDefaultValue;
}

long	ParametreListesi::parametreDegeriLongAl	(const QString &aParamIsmi, long iDefaultValue) const
{
	if (mParametreListesi.contains(aParamIsmi))
		return mParametreListesi[aParamIsmi].toLongLong();
	else return iDefaultValue;
}

bool	ParametreListesi::parametreDegeriBooleanAl(const QString &aParamIsmi, bool iDefaultValue) const
{
	if (mParametreListesi.contains(aParamIsmi))
		return mParametreListesi[aParamIsmi].toBool();
	else return iDefaultValue;
}

ParametreListesi::~ParametreListesi(void)
{
}

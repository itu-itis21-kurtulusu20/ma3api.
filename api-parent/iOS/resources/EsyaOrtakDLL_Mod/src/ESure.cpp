#include "ESure.h"
#include <QTime>
#include "EsyaOrtakDLL_DIL.h"

ESure::ESure()
{
	mGun=0;
	mSaat=0;
	mDakika=0;
	mSaniye=0;
	mMiliSaniye=0;
}

ESure::ESure(const ESure & other)
{
	mGun = other.mGun;
	mSaat=other.mSaat;
	mDakika=other.mDakika;
	mSaniye=other.mSaniye;
	mMiliSaniye=other.mMiliSaniye;
}

ESure::ESure(int iMiliSaniye)
{
	mGun=0;
	mSaat=0;
	mDakika=0;
	mSaniye=0;
	mMiliSaniye=0;
	setMiliSaniye(iMiliSaniye);
}

ESure::ESure(int iSaat,int iDakika,int iSaniye,int iMiliSaniye)
{
	setSaat(iSaat);
	setDakika(iDakika);
	setSaniye(iSaniye);
	setMiliSaniye(iMiliSaniye);
}

ESure::~ESure(void)
{
}

void ESure::miliSaniyeEkle(int iMiliSaniye)
{
	mMiliSaniye+=iMiliSaniye;
	setMiliSaniye(mMiliSaniye);
}
void ESure::saniyeEkle(int iSaniye)
{
	mSaniye+=iSaniye;
	setSaniye(mSaniye);
}

void ESure::dakikaEkle(int iDakika)
{
	mDakika+=iDakika;
	setDakika(mDakika);
}

void ESure::saatEkle(int iSaat)
{	
	mSaat+=iSaat;
	setSaat(mSaat);
}

void ESure::setSaat(int iSaat)
{	
	int gun = iSaat/24;
	int saat = iSaat - gun*24;
	mSaat = saat;
	gunEkle(gun);	
}

void ESure::setDakika(int iDakika)
{	
	int saat = iDakika / 60 ;
	int dakika = iDakika - saat*60;
	mDakika = dakika;
	saatEkle(saat);
}

void ESure::setSaniye(int iSaniye)
{
	int dakika = iSaniye / 60 ;
	int saniye = iSaniye - dakika*60;
	mSaniye = saniye;
	dakikaEkle(dakika);
}

void ESure::setMiliSaniye(int iMiliSaniyes)
{
	int saniye = iMiliSaniyes / 1000 ;
	int miliSaniye = iMiliSaniyes - saniye*1000;
	mMiliSaniye = miliSaniye;
	saniyeEkle(saniye);
}

ESure ESure::gecenSureVerMSec(const QDateTime & iBaslangic,const QDateTime & iBitis)
{	
	ESure sure;
	int miliSaniye = iBaslangic.time().msecsTo(iBitis.time());
	sure.setMiliSaniye(miliSaniye);
	return sure;
}

ESure ESure::gecenSureVer(const QDateTime & iBaslangic,const QDateTime & iBitis)
{	
	ESure sure;
	int secDiff = iBaslangic.secsTo(iBitis);
	sure.setSaniye(secDiff);
	return sure;
}

QString ESure::toString(bool iMilisaniyeEkle) const
{
	QString retStr;
	if (mGun>0)
	{
		retStr+=QString("%1 %2").arg(mGun).arg(DIL_GUN);
	}
	if (mSaat>0)
	{
		if(!retStr.isEmpty())
			retStr+=",";
		retStr+=QString("%1 %2").arg(mSaat).arg(DIL_SAAT);
	}
	if (mDakika>0)
	{
		if(!retStr.isEmpty())
			retStr+=",";
		retStr+=QString("%1 %2").arg(mDakika).arg(DIL_DAKIKA);
	}	
	if (mSaniye>0)
	{
		if(!retStr.isEmpty())
			retStr+=";";
		retStr+=QString("%1 %2").arg(mSaniye).arg(DIL_SANIYE);
	}
	if (iMilisaniyeEkle)
	{
		if (mMiliSaniye>0)
		{
			if(!retStr.isEmpty())
				retStr+=";";
			retStr+=QString("%1 %2").arg(mMiliSaniye).arg(DIL_MILISANIYE);
		}
	}	
	return retStr;
}

bool ESure::isEmpty()
{
	return ((mGun == 0)&&
			(mSaat == 0)&&
			(mDakika == 0)&&
			(mSaniye == 0)&&
			(mMiliSaniye == 0));			
}
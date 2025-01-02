#include "ELoggerRDMMapper.h"
#include <QSqlRecord>
#include <QVariant>
#include "ILogger.h"

ELoggerRDMMapper::ELoggerRDMMapper()
:AbstractRDBMapper(NULL,"","",0)
{

}
ELoggerRDMMapper::ELoggerRDMMapper(QSqlDatabase * ipSqliteDB,const QString  & iTableName,const QString & iIDName,QObject *parent)
	: AbstractRDBMapper(ipSqliteDB,iTableName,iIDName,parent)
{
}

ELoggerRDMMapper::~ELoggerRDMMapper()
{
}

void ELoggerRDMMapper::setID(int iID)
{
	mID = iID ;
}

void ELoggerRDMMapper::setLogStr(const QString &iLogStr)
{
	mLogStr = iLogStr ;
}

QString ELoggerRDMMapper::getLogStr() const
{
	return mLogStr;
}

int ELoggerRDMMapper::getID() const
{
	return mID ;
}

QString ELoggerRDMMapper::getUserName() const
{
	return mUserName;
}

QString ELoggerRDMMapper::getModulAdi() const
{
	return mModulAdi;
}

QString ELoggerRDMMapper::getDosyaAdi() const
{
	return mDosyaAdi;
}

int	 ELoggerRDMMapper::getSatirNo()  const
{
	return mSatirNo;
}

QDateTime ELoggerRDMMapper::getTarih()  const
{
	return mTarih;
}

void  ELoggerRDMMapper::setUserName(const QString & iUserName)
{
	mUserName = iUserName ;
}

void  ELoggerRDMMapper::setModulAdi(const QString & iModulAdi)
{
	mModulAdi = iModulAdi ;
}

void  ELoggerRDMMapper::setDosyaAdi(const QString & iDosyaAdi)
{
	mDosyaAdi = iDosyaAdi ;
}

void  ELoggerRDMMapper::setSatirNo ( int iSatirNo)
{
	mSatirNo = iSatirNo ;
}

void  ELoggerRDMMapper::setTarih(const QDateTime & iTarih)
{
	mTarih = iTarih ;
}

QObject * ELoggerRDMMapper::getObjectFromRecord(int iObjID,const QSqlRecord  &iRecord)
{
	ELoggerRDMMapper * lRetLogger = new ELoggerRDMMapper();
	lRetLogger->setID(iObjID);
	lRetLogger->setUserName(iRecord.value(ELoggerRDMMapper_msFieldNameUserName).toString());
	lRetLogger->setModulAdi(iRecord.value(ELoggerRDMMapper_msFieldNameModulAdi).toString());
	lRetLogger->setDosyaAdi(iRecord.value(ELoggerRDMMapper_msFieldNameDosyaAdi).toString());
	lRetLogger->setSatirNo(iRecord.value(ELoggerRDMMapper_msFieldNameSatirNo).toInt());
	lRetLogger->setLogStr(iRecord.value(ELoggerRDMMapper_msFieldNameLogstr).toString());
	lRetLogger->setTarih(QDateTime::fromString(iRecord.value(ELoggerRDMMapper_msFieldNameLogstr).toString(),DEFAULT_DATE_TIME_MASK));
	return (QObject *)lRetLogger;
}

QVariantList ELoggerRDMMapper::getFieldValueList()
{
	QVariantList lRetValueList;
	lRetValueList	<<QVariant::fromValue(mUserName)
					<<QVariant::fromValue(mModulAdi)
					<<QVariant::fromValue(mDosyaAdi)
					<<QVariant::fromValue(mSatirNo)
					<<QVariant::fromValue(mLogStr)
					<<QVariant::fromValue(mTarih.toString(DEFAULT_DATE_TIME_MASK));				
	return lRetValueList;
}

QStringList ELoggerRDMMapper::getFieldNameList()
{
	QStringList lRetNameList;
	lRetNameList	<<ELoggerRDMMapper_msFieldNameUserName
					<<ELoggerRDMMapper_msFieldNameModulAdi
					<<ELoggerRDMMapper_msFieldNameDosyaAdi
					<<ELoggerRDMMapper_msFieldNameSatirNo
					<<ELoggerRDMMapper_msFieldNameLogstr
					<<ELoggerRDMMapper_msFieldNameTarih ;
	return lRetNameList;
}

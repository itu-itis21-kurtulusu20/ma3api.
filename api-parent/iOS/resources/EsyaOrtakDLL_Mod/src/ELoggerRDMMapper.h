#ifndef ELOGGERRDMMAPPER_H
#define ELOGGERRDMMAPPER_H

#include "AbstractRDBMapper.h"
#include <QStringList>
#include <QVariantList>
#include <QDateTime>

#define  ELoggerRDMMapper_msFieldNameLogID "LogID"
#define  ELoggerRDMMapper_msFieldNameUserName "UserName"
#define  ELoggerRDMMapper_msFieldNameModulAdi "ModulAdi"
#define  ELoggerRDMMapper_msFieldNameDosyaAdi "DosyaAdi"
#define  ELoggerRDMMapper_msFieldNameSatirNo "SatirNo"
#define  ELoggerRDMMapper_msFieldNameLogstr "Log"
#define  ELoggerRDMMapper_msFieldNameTarih "Tarih"

class ELoggerRDMMapper : public AbstractRDBMapper
{		
	QString mUserName;
	QString mModulAdi;
	QString mDosyaAdi;
	int mSatirNo;
	QString mLogStr;
	QDateTime mTarih;

	int mID;
	Q_OBJECT

public:	
	Q_DECL_EXPORT  ELoggerRDMMapper();
	ELoggerRDMMapper(QSqlDatabase * ipSqliteDB,const QString  & iTableName,const QString & iIDName,QObject *parent=0);
	Q_DECL_EXPORT ~ELoggerRDMMapper();
	
	Q_DECL_EXPORT int	  getID()		const;
	Q_DECL_EXPORT QString getUserName() const;
	Q_DECL_EXPORT QString getModulAdi() const;
	Q_DECL_EXPORT QString getDosyaAdi() const;
	Q_DECL_EXPORT int	  getSatirNo()  const;
	Q_DECL_EXPORT QString getLogStr()	const;
	Q_DECL_EXPORT QDateTime getTarih()  const;

	
	Q_DECL_EXPORT void	setID(int iID);
	Q_DECL_EXPORT void  setUserName(const QString & iUserName);
	Q_DECL_EXPORT void  setModulAdi(const QString & iModulAdi);
	Q_DECL_EXPORT void  setDosyaAdi(const QString & iDosyaAdi);
	Q_DECL_EXPORT void  setSatirNo ( int iSatirNo);	
	Q_DECL_EXPORT void	setLogStr(const QString & iLogStr);
	Q_DECL_EXPORT void setTarih(const QDateTime & iTarih);
	
protected:
	QObject *			getObjectFromRecord(int iObjID,const QSqlRecord  &iRecord);
	QStringList			getFieldNameList();
	QVariantList		getFieldValueList();	

private:
	
};

#endif // ELOGGERRDMMAPPER_H

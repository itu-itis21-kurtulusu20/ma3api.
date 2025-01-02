#include "AbstractRDBMapper.h"
#include "EAyarlar.h"
#include "ELogger.h"
#include "EsyaOrtak_Ortak.h"

using namespace esya;
AbstractRDBMapper::AbstractRDBMapper(QSqlDatabase * ipSqliteDB,const QString & iTableName,const QString & iIDName,QObject *parent/* =0 */)
: AbstractPersistenceMapper(parent),mTableName(iTableName),mpSqliteDB(ipSqliteDB),mIDName(iIDName),mOrderFieldName(""),mCloseDBOnExit(true)
{

}

AbstractRDBMapper::~AbstractRDBMapper()
{

}

QList<QObject *> AbstractRDBMapper::getAllObjectsFromStorage()
{
	ESYA_ORTAK_FUNC_BEGIN;
	QList<QObject *> lRetObjList;
	QSqlQuery * lResult=getDBRecord();
	if (lResult)
	{
		while (lResult->next())
		{
			QSqlRecord lRecord = lResult->record();
			lRetObjList.append(getObjectFromRecord(-1,lRecord));
		}	
		DELETE_MEMORY(lResult);
	}
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return lRetObjList;
}

QObject * AbstractRDBMapper::getObjectFromStorage(int iObjID)
{
	ESYA_ORTAK_FUNC_BEGIN;
	QObject * pRetObject  = NULL ;
	QSqlQuery * lResult=getDBRecord(iObjID);
	if (lResult)
	{
		if (!lResult->next())
		{
			return NULL;
		}
		QSqlRecord lRecord = lResult->record();
		pRetObject =  getObjectFromRecord(iObjID,lRecord);
		DELETE_MEMORY(lResult);
		if (mCloseDBOnExit)
		{
			mpSqliteDB->close();
		}	
	}
	ESYA_ORTAK_FUNC_END;
	return pRetObject;
}

QSqlQuery * AbstractRDBMapper::getDBRecord(int iObjID)
{	
	ESYA_ORTAK_FUNC_BEGIN;
	_kontrolEtAc();
	QString lQueryStr;
	if (iObjID == -1)
	{
		lQueryStr = QString("SELECT * FROM %1").arg(mTableName);
	}
	else
	{
		lQueryStr = QString("SELECT * FROM %1 WHERE %2='%3'").arg(mTableName).arg(mIDName).arg(iObjID);
	}	
	if (!mOrderFieldName.isEmpty())
	{
		lQueryStr += QString(" ORDER BY %1 ASC").arg(mOrderFieldName);
	}
	QSqlQuery * pRetQuery = _sorguYap(lQueryStr,ParamList());
	ESYA_ORTAK_FUNC_END;
	return pRetQuery;
}

int AbstractRDBMapper::deleteObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar)
{
	ESYA_ORTAK_FUNC_BEGIN;
	int retNumRowsAffected=0;
	EFilter * pFilter = _constructEFilter(iAlanlar,iAranacaklar);
	retNumRowsAffected = deleteObjectsFromStorageWithFilter(pFilter);
	DELETE_MEMORY(pFilter);
	ESYA_ORTAK_FUNC_END;
	return retNumRowsAffected;	
}

int AbstractRDBMapper::deleteObjectsFromStorageWithFilter(EFilter * ipFilter)
{
	ESYA_ORTAK_FUNC_BEGIN;
	int retNumRowsAffected=0;
	QString queryStr = QString("DELETE FROM %1").arg(mTableName);
	QSqlQuery * sqlQuery = _sorguYap(queryStr,ipFilter);
	if(sqlQuery)
	{
		retNumRowsAffected = sqlQuery->numRowsAffected();
		DELETE_MEMORY(sqlQuery);		
	}
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return retNumRowsAffected;
}

EFilter * AbstractRDBMapper::_constructEFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar)
{
	ESYA_ORTAK_FUNC_BEGIN;
	QList<EFilter*> pFilterList;
	for (int k=0;k<iAlanlar.size();k++)
	{
		if (k>iAranacaklar.size())
		{
			continue;
		}
		QString alan = iAlanlar.at(k);
		QVariant deger = iAranacaklar.at(k);
		MatchFilter * pMatchFilter = new MatchFilter(alan,deger);
		pFilterList<<pMatchFilter;
	}
	ANDFilter * pAndFilter = new ANDFilter(pFilterList);
	ESYA_ORTAK_FUNC_END;
	return pAndFilter;
}

QList<QObject*>  AbstractRDBMapper::getObjectsFromStorageWithFilter(const QStringList & iAlanlar,const QVariantList & iAranacaklar)
{	
	ESYA_ORTAK_FUNC_BEGIN;
	QList<QObject*> retObjList;
	EFilter * pFilter = _constructEFilter(iAlanlar,iAranacaklar);
	retObjList = getObjectsFromStorageWithFilter(pFilter);
	DELETE_MEMORY(pFilter);
	ESYA_ORTAK_FUNC_END;
	return retObjList;
}

QList<QObject*> AbstractRDBMapper::getObjectsFromStorageWithFilter(EFilter * ipFilter)
{
	ESYA_ORTAK_FUNC_BEGIN;
	QList<QObject*> retObjList;		
	QString queryStr = QString("SELECT * FROM %1").arg(mTableName);

	QSqlQuery * sqlQuery = _sorguYap(queryStr,ipFilter);
	if(sqlQuery)
	{
		while(sqlQuery->next())	
		{
			QSqlRecord lRecord =sqlQuery->record();
			QObject * pRetObject =  getObjectFromRecord(0,lRecord);
			retObjList<<pRetObject;
		}
		DELETE_MEMORY(sqlQuery);
	}
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return retObjList;
}

int AbstractRDBMapper::updateObjectsOnStorageWithFilter(const QStringList & iFilterAlanlar,const QVariantList & iFilterAranacaklar,const QStringList & iUpdateAlanlar,const QVariantList & iUpdateDegerler)
{
	ESYA_ORTAK_FUNC_BEGIN;
	int retNumRowsAffected=0;
	
	ParamList paramList;
	QStringList updateStatementList;	
	for(int k=0;k<iUpdateAlanlar.size();k++)
	{
		QString updateAlanAdi =iUpdateAlanlar.at(k);
		updateStatementList<<QString("%1=:%2U").arg(updateAlanAdi).arg(updateAlanAdi);
		paramList<<qMakePair(QString(":%1U").arg(updateAlanAdi),iUpdateDegerler.at(k));
	}
	QString updateStm = updateStatementList.join(",");		

	QStringList whereStatementList;	
	for(int k=0;k<iFilterAlanlar.size();k++)
	{
		QString alanAdi =iFilterAlanlar.at(k);
		whereStatementList<<QString("%1=:%2").arg(alanAdi).arg(alanAdi);
		paramList<<qMakePair(QString(":%1").arg(alanAdi),iFilterAranacaklar.at(k));
	}
	QString whereStm=whereStatementList.join(" AND ");
	if (!whereStatementList.isEmpty())
	{
		whereStm = "WHERE "+whereStm;
	}
	QString queryStr = QString("UPDATE %1 SET %2 %3").arg(mTableName).arg(updateStm).arg(whereStm);	
	QSqlQuery *sqlQuery=_sorguYap(queryStr,paramList);
	if (sqlQuery)
	{
		retNumRowsAffected = sqlQuery->numRowsAffected();
	}	
	DELETE_MEMORY(sqlQuery);	
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return retNumRowsAffected;
}

int AbstractRDBMapper::updateObjectOnDb(int iObjID,QObject * iObj)
{
	ESYA_ORTAK_FUNC_BEGIN;
	int retNumRowsAffected=0;	
	ParamList paramList;

	AbstractRDBMapper * pRDBMapper = (AbstractRDBMapper *)iObj;
	if (!pRDBMapper)
	{
		return retNumRowsAffected;
	}

	QStringList updateSorguList;	
	QStringList  alanlar =pRDBMapper->getFieldNameList();
	QVariantList degerler = pRDBMapper->getFieldValueList();
	for(int k=0;k<alanlar.size();k++)
	{
		QString alanAdi =alanlar.at(k);
		updateSorguList<<QString("%1=:%2").arg(alanAdi).arg(alanAdi);
		paramList<<qMakePair(QString(":%1").arg(alanAdi),degerler.at(k));
	}
	
	QString updateStm = updateSorguList.join(",");
	QString whereStm = QString("%1=%2").arg(mIDName).arg(iObjID);
	QString queryStr = QString("UPDATE %1 SET %2 WHERE %3").arg(mTableName).arg(updateStm).arg(whereStm);
	
	QSqlQuery *sqlQuery=_sorguYap(queryStr,paramList);
	if (sqlQuery)
	{
		retNumRowsAffected = sqlQuery->numRowsAffected();
	}	
	DELETE_MEMORY(sqlQuery);	
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return retNumRowsAffected;
}

int AbstractRDBMapper::putObjectToStorage(int iObjID, QObject *iObj)
{	
	ESYA_ORTAK_FUNC_BEGIN;
	int retNumRowsAffected=-1;

	QVariantList lFieldValueList=((AbstractRDBMapper *)iObj)->getFieldValueList();
	QStringList	 lFieldNameList=((AbstractRDBMapper *)iObj)->getFieldNameList();	
	
	QStringList  lPlaceHolders;
	for (int k=0;k<lFieldNameList.size();k++)
	{
		lPlaceHolders.append("?");
	}
	
	QString lQueryStr=QString("INSERT INTO %1(%2) VALUES (%3)").arg(mTableName).arg(lFieldNameList.join(",")).arg(lPlaceHolders.join(","));

	_kontrolEtAc();
	QSqlQuery lSqlQuery(*mpSqliteDB);
	lSqlQuery.prepare(lQueryStr);
	foreach (QVariant lVariant,lFieldValueList)
	{
		QString lDegerStr = lVariant.toString();
		lSqlQuery.addBindValue(lVariant);
	}
	
	bool  lExecReturn = lSqlQuery.exec();
	retNumRowsAffected = lSqlQuery.numRowsAffected();
	if (!lExecReturn)
	{
		QString lHata = lSqlQuery.lastError().text();
		qDebug()<<lHata;
	}
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return retNumRowsAffected;
}

bool AbstractRDBMapper::removeObjectFromStorage(int iObjID)
{
	ESYA_ORTAK_FUNC_BEGIN;
	QString lQueryStr = QString("DELETE FROM %1 WHERE %2='%3'").arg(mTableName).arg(mIDName).arg(iObjID);
	QSqlQuery *sqlQuery=_sorguYap(lQueryStr,ParamList());
	DELETE_MEMORY(sqlQuery);
	if (mCloseDBOnExit)
	{
		mpSqliteDB->close();
	}	
	ESYA_ORTAK_FUNC_END;
	return true;
}

AbstractRDBMapper::AbstractRDBMapper()
{
}

void AbstractRDBMapper::_kontrolEtAc()
{
	ESYA_ORTAK_FUNC_BEGIN;
	if(!mpSqliteDB->isValid())
	{
		QString errorStr = mpSqliteDB->lastError().text();		
		throwVTEXCEPTION(EVeritabaniException::VTAcilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(errorStr));
	}

	if(!mpSqliteDB->isOpen())
	{
		if(!mpSqliteDB->open())
		{			
			QString errorStr = mpSqliteDB->lastError().text();			
			throwVTEXCEPTION(EVeritabaniException::VTAcilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(errorStr));
		}
	}
	ESYA_ORTAK_FUNC_END;
}

QSqlQuery* AbstractRDBMapper::_sorguYap(const QString & irQueryText,const ParamList & iParameters)
{	
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_DEBUG(QString::fromLocal8Bit("Çalýþtýrýlan sorgu = %1").arg(irQueryText));
	_kontrolEtAc();

	QSqlQuery * pQuery = new QSqlQuery(*mpSqliteDB);
	QString errSTR = pQuery->lastError().text();
	if (!pQuery->prepare(irQueryText))
	{
		QString errSTR = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		return NULL;
	}

	for (int i = 0; i < iParameters.size(); i++)
	{
		pQuery->bindValue(iParameters[i].first,iParameters[i].second);
	}

	if (!pQuery->exec())
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
	}	
	ESYA_ORTAK_FUNC_END;
	return pQuery;
}

QSqlQuery* AbstractRDBMapper::_sorguYap(const QString & irQueryText,EFilter * ipFilter)
{

	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_DEBUG(QString::fromLocal8Bit("Çalýþtýrýlan sorgu = %1").arg(irQueryText));
	_kontrolEtAc();	
	QString sql = irQueryText;
	if (ipFilter)
	{
		sql += QString(" WHERE %2").arg(ipFilter->toSQL());
	}
	QSqlQuery * pQuery = new QSqlQuery(*mpSqliteDB);
	QString errSTR = pQuery->lastError().text();
	if (!pQuery->prepare(sql))
	{
		QString errSTR = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		return NULL;
	}

	if (ipFilter)
	{
		QList<QVariant> params = ipFilter->getParams();
		for (int i = 0; i < params.size(); i++)
		{
			pQuery->bindValue(i,params[i]);
		}
	}

	if (!pQuery->exec())
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throwVTEXCEPTION(EVeritabaniException::QueryCalistirilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText));
	}	
	ESYA_ORTAK_FUNC_END;
	return pQuery;	
}

void AbstractRDBMapper::setOrderFieldName(const QString &iOrderFieldName)
{	
	mOrderFieldName = iOrderFieldName;
}
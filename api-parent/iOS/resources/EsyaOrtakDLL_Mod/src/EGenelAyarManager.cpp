#include "EGenelAyarManager.h"
#include "FileUtil.h"

NAMESPACE_BEGIN(esya)

QString EGenelAyarManager::msDefaultGenelAyarFilePath;
QMap<QString,EAyarManager *> EGenelAyarManager::mInstanceMap=QMap<QString,EAyarManager*>();
EGenelAyarManager::EGenelAyarManager(const QString & iDbPath)
:EAyarManager(iDbPath)
{	
}

EAyarManager * EGenelAyarManager::getInstance(const QString & iDbPath/* = */)
{
	QString dbPath=iDbPath;
	if (dbPath.isEmpty())
	{
		if (EGenelAyarManager::msDefaultGenelAyarFilePath.isEmpty())
		{
			EGenelAyarManager::msDefaultGenelAyarFilePath=FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME;
		}
		dbPath = EGenelAyarManager::msDefaultGenelAyarFilePath;
	}
	if(!EGenelAyarManager::mInstanceMap.contains(dbPath))
	{
		EAyarManager * pAyarManager = new EGenelAyarManager(dbPath);
		EGenelAyarManager::mInstanceMap.insert(dbPath,pAyarManager);
		return pAyarManager;
	}
	else
	{
		return EGenelAyarManager::mInstanceMap.value(dbPath);
	}	
}
void EGenelAyarManager::freeGenelAyarManager()
{	
	QList<EAyarManager *> instanceList = mInstanceMap.values();
	int size = instanceList.size();
	for (int i = 0; i < size; ++i)
	{
		DELETE_MEMORY(instanceList[i]);
	}
	mInstanceMap.clear();
}
bool EGenelAyarManager::ayarGuncelle(const EAyar & iAyar)
{		
	//Hiçbir koþul olmaksýzýn genel ayarlarda deðiþiklik yapma hakkýna sahip
	//mDb.open();
	ParamList params = _getUpdateParams(iAyar);	
	EVeritabani vt = EVeritabani::sqLiteVTUret(mDBPath);
	QSqlQuery * pQuery = NULL;
	try
	{		
		pQuery = vt.sorguYap(_getUpdateSorgusuStr(),params);
		DELETE_MEMORY(pQuery);				
	}
	catch (EVeritabaniException &exc)
	{
		DELETE_MEMORY(pQuery);
		throw exc.append("Genel ayarda guncelleme yapýlýrken hata oluþtu");		
	}		
	return true;	
}

EGenelAyarManager::~EGenelAyarManager(void)
{
}


NAMESPACE_END

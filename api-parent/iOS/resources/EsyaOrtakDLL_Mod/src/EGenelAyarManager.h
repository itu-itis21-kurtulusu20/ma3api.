#ifndef __E_GENEL_AYAR_MANAGER_H_
#define __E_GENEL_AYAR_MANAGER_H_
#include "EAyarlar.h"
#include "esyaOrtak.h"
#include "EAyarManager.h"

#define KERMEN_GENEL_AYAR_DB_FILE_NAME "egenelayarlar.esya"

NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EGenelAyarManager:public EAyarManager
{
	static QString msDefaultGenelAyarFilePath;
	static QMap<QString,EAyarManager *> mInstanceMap;
public:	
	EGenelAyarManager(const QString & iDbPath);
	~EGenelAyarManager(void);
	static EAyarManager * getInstance(const QString & iDbPath="");
	static void freeGenelAyarManager();
	bool ayarGuncelle(const EAyar & iAyar);
};
NAMESPACE_END
#endif
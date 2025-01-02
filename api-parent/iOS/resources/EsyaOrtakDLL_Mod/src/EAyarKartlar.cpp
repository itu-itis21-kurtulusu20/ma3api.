#include "EAyarKartlar.h"
#include "EsyaOrtak_Ortak.h"
#include "PersistenceFacade.h"
#include "FileUtil.h"
#include "EGenelAyarManager.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

/**
 * Kart bilgileriyle ilklendirilir.
 * \param iKartID 
 * Kartýn veritabaný ID si
 * \param &irKartAdi 
 * Kartýn adý
 * \param &irKartLib 
 * Kartýn kullandýðý dll dosya adý
 * \param &iImzalamaCSPAdi 
 * Mikrosoft için imzalama CSP adý
 * \param &iSifrelemeCSPAdi 
 * Mikrosoft için þifreleme CSP adi
 * \return 
 */
EAyarKartlar::EAyarKartlar(int iKartID, const QString &irKartAdi, const QString &irKartLib, const QString &iImzalamaCSPAdi, const QString &iSifrelemeCSPAdi ,QObject * parent)
		  :QObject(parent),
		  mKartID(iKartID),
		  mKartAdi(irKartAdi),
		  mKartLib(irKartLib),
		  mImzalamaCSPAdi(iImzalamaCSPAdi),
		  mSifrelemeCSPAdi(iSifrelemeCSPAdi)
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

EAyarKartlar::EAyarKartlar(const EAyarKartlar & iAyarKartlar,QObject * parent)
:QObject(parent)
{
	mKartID = iAyarKartlar.getKartID();
	mKartAdi = iAyarKartlar.getKartAdi();
	mKartLib = iAyarKartlar.getKartLib();
	mImzalamaCSPAdi = iAyarKartlar.getImzalamaCSPAdi();
	mSifrelemeCSPAdi = iAyarKartlar.getSifrelemeCSPAdi();
}

EAyarKartlar & EAyarKartlar::operator=(const EAyarKartlar & iAyarKartlar)
{
	mKartID = iAyarKartlar.getKartID();
	mKartAdi = iAyarKartlar.getKartAdi();
	mKartLib = iAyarKartlar.getKartLib();
	mImzalamaCSPAdi = iAyarKartlar.getImzalamaCSPAdi();
	mSifrelemeCSPAdi = iAyarKartlar.getSifrelemeCSPAdi();
	return *this;
}
int EAyarKartlar::getKartID() const
{
	return mKartID;
}

const QString &EAyarKartlar::getKartAdi() const
{
	return mKartAdi;
}

const QString &EAyarKartlar::getKartLib() const
{
	return mKartLib;
}

const QString &EAyarKartlar::getImzalamaCSPAdi() const
{
	return mImzalamaCSPAdi;
}

const QString &EAyarKartlar::getSifrelemeCSPAdi() const
{
	return mSifrelemeCSPAdi;
}

QList<EAyarKartlar> EAyarKartlar::tumKartlariAl()
{
	EVeritabani vt=EVeritabani::sqLiteVTUret(FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME);	
	const QSqlDatabase & db = vt.database();
	IMapper * pKartlarMapper = PersistenceFacade::getInstance()->getMapper((QSqlDatabase *)&db,KERMEN_CLASS_EAYAR_KARTLAR);
	QList<QObject *> objListesi = pKartlarMapper->getAll();
	QList<EAyarKartlar> kartListesi;
	Q_FOREACH(QObject * pObj,objListesi)
	{
		EAyarKartlar * pKart = qobject_cast<EAyarKartlar *>(pObj);		
		kartListesi<<EAyarKartlar(*pKart);
		DELETE_MEMORY(pKart);
	}
	DELETE_MEMORY(pKartlarMapper);
	return kartListesi;
}

QList<EAyarKartlar *> EAyarKartlar::tumKartlariAlPtr()
{
	QList<EAyarKartlar *> retKartListesi;

	EVeritabani vt=EVeritabani::sqLiteVTUret(FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME);	
	const QSqlDatabase & db = vt.database();
	IMapper * pKartlarMapper = PersistenceFacade::getInstance()->getMapper((QSqlDatabase *)&db,KERMEN_CLASS_EAYAR_KARTLAR);
	QList<QObject *> objListesi = pKartlarMapper->getAll();	
	Q_FOREACH(QObject * pObj,objListesi)
	{
		EAyarKartlar * pKart = qobject_cast<EAyarKartlar *>(pObj);		
		retKartListesi<<pKart;
	}
	DELETE_MEMORY(pKartlarMapper);
	return retKartListesi;
}
NAMESPACE_END

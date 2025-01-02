#include "EAyarLdap.h"
#include "EsyaOrtak_Ortak.h"
#include "OrtakDil.h"
#include "PersistenceFacade.h"
#include "FileUtil.h"
#include "EGenelAyarManager.h"
using namespace esya;
NAMESPACE_BEGIN(esya)

/**
 * Kullanilan LDAP özellikleri ile ilklendirilir.
 * \param iLdapID 
 * LDAP ayarýn veritabanýndaki ID si
 * \param &irIP 
 * LDAP IP numarasý
 * \param iPort 
 * LPAP port numarasý	
 * \param iBaglantiTipi 
 * LDAP baðlantý tipi
 * \param iSizeLimit 
 * LDAP büyüklük limiti
 * \param iTimeLimit 
 * LDAP sorgu zaman limiti
 * \param &irSearchBase 
 * Kullanýlacak olan arama tabaný	
 * \param iLdapTipi 
 * LDAP tipi
 * \param iVarsayilan 
 * Varsayýlan LDAP olup olmadýðý
 * \return 
 */
EAyarLdap::EAyarLdap(int iLdapID,
		  const QString &irIP,
		  int iPort,
		  int iBaglantiTipi,
		  int iSizeLimit,
		  int iTimeLimit,
		  const QString &irSearchBase,
		  EAyarLdap::LDAP_TIPI iLdapTipi,
		  bool iVarsayilan,
		  QObject * parent
		  )
		  :
		  QObject(parent),
		  mLdapID(iLdapID),
		  mIP(irIP),
		  mPort(iPort),
		  mBaglantiTipi(iBaglantiTipi),
		  mSizeLimit(iSizeLimit),
		  mTimeLimit(iTimeLimit),
		  mSearchBase(irSearchBase),
		  mLdapTipi(iLdapTipi),
		  mVarsayilan(iVarsayilan)
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

EAyarLdap::EAyarLdap(const EAyarLdap & iAyarLdap)
{
	mLdapID=iAyarLdap.getLdapID();
	mIP=iAyarLdap.getIP();
	mPort=iAyarLdap.getPort();
	mBaglantiTipi=iAyarLdap.getBaglantiTipi();
	mSizeLimit=iAyarLdap.getSizeLimit();
	mTimeLimit=iAyarLdap.getTimeLimit();
	mSearchBase=iAyarLdap.getSearchBase();
	mLdapTipi=iAyarLdap.getLdapTipi();
	mVarsayilan=iAyarLdap.isVarsayilan();
}

EAyarLdap & EAyarLdap::operator =(const EAyarLdap & iAyarLdap)
{
	mLdapID=iAyarLdap.getLdapID();
	mIP=iAyarLdap.getIP();
	mPort=iAyarLdap.getPort();
	mBaglantiTipi=iAyarLdap.getBaglantiTipi();
	mSizeLimit=iAyarLdap.getSizeLimit();
	mTimeLimit=iAyarLdap.getTimeLimit();
	mSearchBase=iAyarLdap.getSearchBase();
	mLdapTipi=iAyarLdap.getLdapTipi();
	mVarsayilan=iAyarLdap.isVarsayilan();
	return *this;
}

bool EAyarLdap::operator ==(const EAyarLdap & iAyarLdap)
{
	bool retValue = false;
	retValue = (mIP==iAyarLdap.getIP())&& 
				(mPort==iAyarLdap.getPort())&&
				(mBaglantiTipi==iAyarLdap.getBaglantiTipi())&&
				(mSizeLimit==iAyarLdap.getSizeLimit())&&
				(mTimeLimit=iAyarLdap.getTimeLimit())&&
				(mSearchBase==iAyarLdap.getSearchBase())&&
				(mLdapTipi==iAyarLdap.getLdapTipi());
	return retValue;
}

bool EAyarLdap::operator<(const EAyarLdap & iAyarLdap)
{
	return (mLdapID<iAyarLdap.getLdapID());
}

EAyarLdap::~EAyarLdap()
{
	ESYA_ORTAK_FUNC_BEGIN;
	ESYA_ORTAK_FUNC_END;
}

int EAyarLdap::getLdapID() const
{
	return mLdapID;
}

const QString &EAyarLdap::getIP() const
{
	return mIP;
}

int EAyarLdap::getPort() const
{
	return mPort;
}

int EAyarLdap::getBaglantiTipi() const
{
	return mBaglantiTipi;
}

int EAyarLdap::getSizeLimit() const
{
	return mSizeLimit;
}

int EAyarLdap::getTimeLimit() const
{
	return mTimeLimit;
}

const QString &EAyarLdap::getSearchBase() const
{
	return mSearchBase;
}

EAyarLdap::LDAP_TIPI EAyarLdap::getLdapTipi() const
{
	return mLdapTipi;
}

bool EAyarLdap::isVarsayilan() const
{
	return mVarsayilan;
}

/**
* LDAP tpini strin olarak geri döner.
*
* \param iLDAPTipi 
* LDAP tipi
* \return
String olarak ldap tipini döner.
*/
QString EAyarLdap::lDAPTipiToStr(EAyarLdap::LDAP_TIPI iLDAPTipi)
{			   
	switch(iLDAPTipi)
	{
	case EAyarLdap::LDAP_TIPI_BILINMEYEN:
		return DIL_LDAP_TIPI_GENERIC;
		break;
	case  EAyarLdap::LDAP_TIPI_AD:  
		return DIL_LDAP_TIPI_ACTIVE_DIRECTORY;
		break;
	case EAyarLdap::LDAP_TIPI_CP:
		return DIL_LDAP_TIPI_CRITICAL_PATH;
		break;
	case EAyarLdap::LDAP_TIPI_NS:
		return DIL_LDAP_TIPI_NETSCAPE;
		break;
	
	default:
		return DIL_LDAP_TIPI_BILINMEYEN;
		break;
	}
}

/**
 * Kullanýlabilecek LDAP'larýn isimlerinin listesini döner.
 * \return 
 * Ýsim listesi döner.
 */
QStringList EAyarLdap::lDAPTipiStrList()
{
	QStringList retItems;
	retItems<<DIL_LDAP_TIPI_BILINMEYEN
		<<DIL_LDAP_TIPI_ACTIVE_DIRECTORY
		<<DIL_LDAP_TIPI_CRITICAL_PATH
		<<DIL_LDAP_TIPI_NETSCAPE
		<<DIL_LDAP_TIPI_GENERIC;
	return retItems;
}

/**
*  Baðlantý tipini string olarak döner.
*/
QString EAyarLdap::lDAPBaglantiTipiToStr(EAyarLdap::LDAP_TIPI iLDAPBaglantiTipi)
{
	switch(iLDAPBaglantiTipi)
	{
	case 0:
		return DIL_LDAP_BAGLANTI_NORMAL;
		break;
	case 1:
		return DIL_LDAP_BAGLANTI_GUVENLI;
		break;
	default:
		return DIL_LDAP_BAGLANTI_BILINMEYEN;
		break;
	}
}



/**
 * LDAP baglantý tipinin listesini döner.
 * \return 
 * Listeyi döner
 */
QStringList EAyarLdap::lDAPBaglantiTipiStrList()
{
	QStringList retList;
	retList<<DIL_LDAP_BAGLANTI_NORMAL
		  <<DIL_LDAP_BAGLANTI_GUVENLI
		  <<DIL_LDAP_BAGLANTI_BILINMEYEN;
	return retList;
}

QList<EAyarLdap> EAyarLdap::tumLDAPlariAl()
{
	QList<EAyarLdap> retLDAPList;
	EVeritabani vt=EVeritabani::sqLiteVTUret(FileUtil::genelAyarPath()+"/"+KERMEN_GENEL_AYAR_DB_FILE_NAME);	
	const QSqlDatabase & db = vt.database();
	IMapper * pLDAPMapper = PersistenceFacade::getInstance()->getMapper((QSqlDatabase *)&db,KERMEN_CLASS_EAYAR_LDAP);
	QList<QObject *> objListesi = pLDAPMapper->getAll();	
	bool isFirst=true;
	Q_FOREACH(QObject * pObj,objListesi)
	{
		EAyarLdap * pLDAP = qobject_cast<EAyarLdap *>(pObj);
		if (pLDAP)
		{/*
			if (isFirst)
			{	
				EAyarLdap domainLDAP(-1,"",pLDAP->getPort(),pLDAP->getBaglantiTipi(),pLDAP->getSizeLimit(),pLDAP->getTimeLimit(),pLDAP->getSearchBase(),pLDAP->getLdapTipi(),pLDAP->isVarsayilan());
				retLDAPList<<domainLDAP;
			}*/
			retLDAPList<<EAyarLdap(*pLDAP);
			DELETE_MEMORY(pLDAP);
		}
	}
	DELETE_MEMORY(pLDAPMapper);	
	return retLDAPList;
}
NAMESPACE_END

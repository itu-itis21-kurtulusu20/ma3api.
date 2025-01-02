#ifndef __EAYARLDAP_H__
#define __EAYARLDAP_H__

#include "esyaOrtak.h"
#include <QString>
#include <QSqlDatabase>
#include <QVariant>
#include "EAyarException.h"
#include <QObject>

using namespace esya;
NAMESPACE_BEGIN(esya)

/**
 * \ingroup EsyaOrtakDLL
 *
 * LDAP ile ilgili ayarlarý tutmak için kullanýlan entry sýnýfý
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 * 
 * \todo 
 *
 * \bug 
 *
 */
class  EAyarLdap:public QObject
{
	Q_OBJECT
public:
	enum LDAP_TIPI {LDAP_TIPI_BILINMEYEN,LDAP_TIPI_AD,LDAP_TIPI_CP,LDAP_TIPI_NS};
	Q_DECL_EXPORT EAyarLdap(int iLdapID,const QString &irIP,int iPort,int iBaglantiTipi,int iSizeLimit,int iTimeLimit,const QString &irSearchBase,EAyarLdap::LDAP_TIPI iLdapTipi,bool iVarsayilan = false,QObject * parent = NULL);
	Q_DECL_EXPORT EAyarLdap(const EAyarLdap & iAyarLdap);
	Q_DECL_EXPORT EAyarLdap & operator=(const EAyarLdap & iAyarLdap);
	Q_DECL_EXPORT bool operator==(const EAyarLdap & iAyarLdap);
	Q_DECL_EXPORT bool operator<(const EAyarLdap & iAyarLdap);	
	Q_DECL_EXPORT ~EAyarLdap();

	Q_DECL_EXPORT int getLdapID() const;
	Q_DECL_EXPORT void setLdapID(int iLDAPID){mLdapID=iLDAPID;};
	Q_DECL_EXPORT const QString &getIP() const;
	Q_DECL_EXPORT int getPort() const;
	Q_DECL_EXPORT int getBaglantiTipi() const;
	Q_DECL_EXPORT int getSizeLimit() const;
	Q_DECL_EXPORT int getTimeLimit() const;
	Q_DECL_EXPORT const QString &getSearchBase() const;
	Q_DECL_EXPORT EAyarLdap::LDAP_TIPI getLdapTipi() const;
	Q_DECL_EXPORT bool isVarsayilan() const;

	Q_DECL_EXPORT static QString lDAPTipiToStr(EAyarLdap::LDAP_TIPI iLDAPTipi);
	Q_DECL_EXPORT static QString lDAPBaglantiTipiToStr(EAyarLdap::LDAP_TIPI iLDAPBaglantiTipi);
	Q_DECL_EXPORT static QStringList lDAPTipiStrList();
	Q_DECL_EXPORT static QStringList lDAPBaglantiTipiStrList();

	Q_DECL_EXPORT static QList<EAyarLdap> tumLDAPlariAl();

private:
	int mLdapID;
	QString mIP;
	int mPort;
	int mBaglantiTipi;
	int mSizeLimit;
	int mTimeLimit;
	QString mSearchBase;
	EAyarLdap::LDAP_TIPI mLdapTipi;
	bool mVarsayilan;
};

NAMESPACE_END
#endif

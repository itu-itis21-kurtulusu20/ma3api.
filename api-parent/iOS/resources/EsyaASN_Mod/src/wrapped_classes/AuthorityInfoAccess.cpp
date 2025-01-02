#include "AuthorityInfoAccess.h"
#include "OrtakDil.h"

using namespace esya;

AuthorityInfoAccess::AuthorityInfoAccess(void)
{
}

AuthorityInfoAccess::AuthorityInfoAccess(const ASN1T_IMP_AuthorityInfoAccessSyntax &iAIA)
{
	copyFromASNObject(iAIA);
}

AuthorityInfoAccess::AuthorityInfoAccess(const QByteArray &iAIA)
{
	constructObject(iAIA);
}

AuthorityInfoAccess::AuthorityInfoAccess(const AuthorityInfoAccess&iAIA)
: mList(iAIA.getList())
{
}

AuthorityInfoAccess::AuthorityInfoAccess(const QList<AccessDescription>&iAIA)
: mList(iAIA)
{
}

AuthorityInfoAccess & AuthorityInfoAccess::operator=(const AuthorityInfoAccess&iAIA)
{
	mList = iAIA.getList();
	return *this;
}

bool esya::operator==(const AuthorityInfoAccess & iRHS, const AuthorityInfoAccess& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const AuthorityInfoAccess & iRHS, const AuthorityInfoAccess& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int AuthorityInfoAccess::copyFromASNObject(const ASN1T_IMP_AuthorityInfoAccessSyntax &iAIA)
{
	AccessDescription().copyADs(iAIA,mList);
	return SUCCESS;
}


int AuthorityInfoAccess::copyToASNObject(ASN1T_IMP_AuthorityInfoAccessSyntax& oAIA)const
{
	AccessDescription().copyADs(mList,oAIA);
	return SUCCESS;
}

void AuthorityInfoAccess::freeASNObject(ASN1T_IMP_AuthorityInfoAccessSyntax & oAIA) const
{
	AccessDescription().freeASNObjects(oAIA);
}


const QList<AccessDescription> &AuthorityInfoAccess::getList() const
{
	return mList;
}

QString AuthorityInfoAccess::toString() const
{
	return QString();
}



QList<QString> AuthorityInfoAccess::adresleriAl( const QString & iAdresTipi)
{
	QList<QString> adresListesi;
	for (int i = 0 ; i < mList.size() ; i++)
	{
		if ( mList[i].getAccessMethod() == EXP_id_ad_caIssuers )	
		{
			if (mList[i].getAccessLocation().toString().startsWith(iAdresTipi,Qt::CaseInsensitive))
				adresListesi.append(mList[i].getAccessLocation().toString());
		}
	}
	return adresListesi;
}

QList<QString> AuthorityInfoAccess::ocspAdresleriAl()
{
	QList<QString> adresListesi;
	for (int i = 0 ; i < mList.size() ; i++)
	{
		if ( mList[i].getAccessMethod() == EXP_id_ad_ocsp )	
		{
			adresListesi.append(mList[i].getAccessLocation().toString());
		}
	}
	return adresListesi;
}


QStringList AuthorityInfoAccess::toStringList() const 
{
	QList<QString> lAiaAdresleri = AccessDescription::adresleriAl(mList,"");
	lAiaAdresleri<<AccessDescription::ocspAdresleriAl(mList);

	QString lAiaAdres;
	QStringList lAiaAdresList;

	for (int i = 0; i <lAiaAdresleri.size();i++)
	{
		lAiaAdres=QString("[%1]\n%2").arg(i+1).arg(lAiaAdresleri[i]);
		lAiaAdresList.append(lAiaAdres); 
	}

	return lAiaAdresList;
}



/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString AuthorityInfoAccess::eklentiAdiAl()			const 
{
	return DIL_EXT_YETKILI_ERISIM_NOKTALARI;
}

QString AuthorityInfoAccess::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString AuthorityInfoAccess::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* AuthorityInfoAccess::kendiniKopyala() const 
{
	return (AY_Eklenti* )new AuthorityInfoAccess(*this);
}


AuthorityInfoAccess::~AuthorityInfoAccess(void)
{
}

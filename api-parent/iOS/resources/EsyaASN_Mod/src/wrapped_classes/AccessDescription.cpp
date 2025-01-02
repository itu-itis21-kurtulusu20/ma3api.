#include "AccessDescription.h"

using namespace esya;

AccessDescription::AccessDescription(void)
{
}

AccessDescription::AccessDescription(const AccessDescription &iAD)
:	mAccessMethod(iAD.getAccessMethod()),
	mAccessLocation(iAD.getAccessLocation())
{
}

AccessDescription::AccessDescription(const ASN1T_IMP_AccessDescription & iAD)
{
	copyFromASNObject(iAD);
}

AccessDescription::AccessDescription(const QByteArray & iAD)
{
	constructObject(iAD);	
}


AccessDescription::AccessDescription(const ASN1OBJID & iAccessMethod ,const GeneralName& iAccessLocation)
:	mAccessMethod(iAccessMethod),
	mAccessLocation(iAccessLocation)
{
}

AccessDescription & AccessDescription::operator=(const AccessDescription& iAD)
{
	mAccessMethod	= iAD.getAccessMethod();
	mAccessLocation = iAD.getAccessLocation();
	return (*this);
}

bool esya::operator==(const AccessDescription& iRHS, const AccessDescription& iLHS)
{
	return ( (iRHS.getAccessMethod() == iLHS.getAccessMethod()) && (iRHS.getAccessLocation()==iLHS.getAccessLocation()) );
}

bool esya::operator!=(const AccessDescription& iRHS, const AccessDescription& iLHS)
{
	return (!(iRHS==iLHS));
}

int AccessDescription::copyFromASNObject(const ASN1T_IMP_AccessDescription& iAD)
{
	mAccessMethod = iAD.accessMethod;
	mAccessLocation.copyFromASNObject(iAD.accessLocation);
	return SUCCESS;
}

int AccessDescription::copyToASNObject(ASN1T_IMP_AccessDescription & oAD) const
{
	oAD.accessMethod = mAccessMethod;
	mAccessLocation.copyToASNObject(oAD.accessLocation);
	return SUCCESS;
}


void AccessDescription::freeASNObject(ASN1T_IMP_AccessDescription & oAD)const
{
	GeneralName().freeASNObject(oAD.accessLocation);
}

int AccessDescription::copyADs(const ASN1T_IMP_AuthorityInfoAccessSyntax & iADs, QList<AccessDescription>& oList)
{
	return copyASNObjects<AccessDescription>(iADs,oList);
}

int AccessDescription::copyADs(const QList<AccessDescription> iList ,ASN1T_IMP_AuthorityInfoAccessSyntax & oADs)
{
	return copyASNObjects<AccessDescription>(iList,oADs);
}


const ASN1OBJID& AccessDescription::getAccessMethod() const
{
	return mAccessMethod;
}

const GeneralName& AccessDescription::getAccessLocation() const
{
	return mAccessLocation;
}


QList<QString> AccessDescription::adresleriAl( const QList<AccessDescription> & iAIA , const QString & iAdresTipi)
{
	QList<QString> adresListesi;
	for (int i = 0 ; i<iAIA.size() ; i++)
	{
		if ( iAIA[i].getAccessMethod() == EXP_id_ad_caIssuers )	
		{
			if (iAIA[i].getAccessLocation().toString().startsWith(iAdresTipi,Qt::CaseInsensitive))
				adresListesi.append(iAIA[i].getAccessLocation().toString());
		}
	}
	return adresListesi;
}

QList<QString> AccessDescription::ocspAdresleriAl( const QList<AccessDescription> & iAIA)
{
	QList<QString> adresListesi;
	for (int i = 0 ; i<iAIA.size() ; i++)
	{
		if ( iAIA[i].getAccessMethod() == EXP_id_ad_ocsp )	
		{
			adresListesi.append(iAIA[i].getAccessLocation().toString());
		}
	}
	return adresListesi;
}


AccessDescription::~AccessDescription(void)
{
	
}

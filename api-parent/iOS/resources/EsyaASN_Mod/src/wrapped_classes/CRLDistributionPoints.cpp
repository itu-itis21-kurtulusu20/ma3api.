#include "CRLDistributionPoints.h"
#include "OrtakDil.h"

using namespace esya;

CRLDistributionPoints::CRLDistributionPoints(void)
{
}

CRLDistributionPoints::CRLDistributionPoints(const ASN1T_IMP_CRLDistributionPoints &iCDP)
{
	copyFromASNObject(iCDP);
}

CRLDistributionPoints::CRLDistributionPoints(const QByteArray &iCDP)
{
	constructObject(iCDP);
}

CRLDistributionPoints::CRLDistributionPoints(const CRLDistributionPoints&iCDP)
: mList(iCDP.getList())
{
}

CRLDistributionPoints::CRLDistributionPoints(const QList<DistributionPoint>&iCDP)
: mList(iCDP)
{
}

CRLDistributionPoints & CRLDistributionPoints::operator=(const CRLDistributionPoints&iCDP)
{
	mList = iCDP.getList();
	return *this;
}

bool esya::operator==(const CRLDistributionPoints & iRHS, const CRLDistributionPoints& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const CRLDistributionPoints & iRHS, const CRLDistributionPoints& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int CRLDistributionPoints::copyFromASNObject(const ASN1T_IMP_CRLDistributionPoints &iCDP)
{
	DistributionPoint().copyCDPs(iCDP,mList);
	return SUCCESS;
}

int CRLDistributionPoints::copyToASNObject(ASN1T_IMP_CRLDistributionPoints& oCDP)const
{
	DistributionPoint().copyCDPs(mList,oCDP);
	return SUCCESS;
}

void CRLDistributionPoints::freeASNObject(ASN1T_IMP_CRLDistributionPoints & oCDP)const
{
	DistributionPoint().freeASNObjects(oCDP);
}

/////////////////////////////////////////////////////////////////

const QList<DistributionPoint> &CRLDistributionPoints::getList() const
{
	return mList;
}

QString CRLDistributionPoints::toString() const
{
	return QString();
}


int CRLDistributionPoints::DPtoCDP(const DistributionPoint & aDP , const Name & iIssuer , CDP & oCDP)
{
	if (!aDP.isDPNPresent() ) return FAILURE;
	
	const DistributionPointName & dpName = aDP.getDPN();
	
	switch (dpName.getType())
	{
	case DistributionPointName::FULLNAME:
		{
			/*If the DistributionPointName contains multiple values, each name
			describes a different mechanism to obtain the same CRL. For example,
			the same CRL could be available for retrieval through both LDAP and
			HTTP.*/
			const QList<GeneralName> & gnArray = dpName.getFullName();

			if ( gnArray.size() == 0 )
			{
				return FAILURE;
			} else
			{
				for (int j = 0; j < gnArray.size(); j++)
				{
					QString adres = gnArray[j].toString(); 
					QString tip;
					if(adres.startsWith(AT_HTTP,Qt::CaseInsensitive))
					{
						tip = AT_HTTP;
					}else if(adres.startsWith(AT_LDAP,Qt::CaseInsensitive))
					{
						tip = AT_LDAP;
					}else
					{
						tip = AT_DN;
					}
					QString reasonStr(""); 
					if (aDP.isReasonsPresent())
						reasonStr = aDP.getReasons().toString();

					if (aDP.isCRLIssuerPresent())
					{
						try
						{
							const QList<GeneralName> & issuers = aDP.getCRLIssuer();
							if (issuers.size() > 0 ) 
							{
								GeneralName gn = issuers[0];
								const Name& issuer = gn.getDirectoryName();
								oCDP = CDP(tip, adres, &issuer, reasonStr );
							}
						} catch (EException &aEx)
						{
							return FAILURE;
						}
					}
					else oCDP = CDP(tip, adres, NULL, reasonStr );
				}
			}
			break;
		}
	case DistributionPointName::NAMER2CRLISSUER:
		{
			/*If the DistributionPointName contains the single value
			nameRelativeToCRLIssuer, the value provides a distinguished name
			fragment. The fragment is appended to the X.500 distinguished name
			of the CRL issuer to obtain the distribution point name. If the
			cRLIssuer field in the DistributionPoint is present, then the name
			fragment is appended to the distinguished name that it contains;
			otherwise, the name fragment is appended to the certificate issuer
			distinguished name. The DistributionPointName MUST NOT use the
			nameRealtiveToCRLIssuer alternative when cRLIssuer contains more than
			one distinguished name.*/
			Name  crlIssuer ,issuer;
			if (aDP.isCRLIssuerPresent())
			{
				try
				{
					const QList<GeneralName> & issuers = aDP.getCRLIssuer();
					if (issuers.size() > 0 ) 
					{
						GeneralName gn = issuers[0];
						crlIssuer = gn.getDirectoryName();
						issuer = crlIssuer;
					}
				} catch (EException &aEx)
				{
					return FAILURE;
				}
			}else
			{
				crlIssuer = iIssuer;
			}
			//issuer alalým
			const QList<RelativeDistinguishedName>& issuerDN = crlIssuer.getList();
			//dn parcasini alalým
			const RelativeDistinguishedName&  dnFragment = dpName.getNameR2CRLIssuer();
			//append edelim
			QList<RelativeDistinguishedName> fullDN;
			fullDN += issuerDN;
			fullDN.append(dnFragment);
			Name point(fullDN);
			
			if (aDP.isCRLIssuerPresent())
				oCDP = CDP(AT_DN, point.toString(), &issuer, aDP.getReasons().toString());
			else oCDP = CDP(AT_DN, point.toString(), NULL, aDP.getReasons().toString());
		}
	default:
		return FAILURE;
	}
	return SUCCESS;

}


CRLDistributionPoints::~CRLDistributionPoints(void)
{
}



QStringList CRLDistributionPoints::toStringList()const 
{
	QString lCrlAdresStr="";
	QStringList lAdresList;

	for (int i = 0 ; i < mList.size() ;i++)
	{
		lCrlAdresStr=QString("[%1]\n").arg(i+1);

		QList<GeneralName> lFullName = mList[i].getDPN().getFullName(); 

		for (int j = 0;j<lFullName.size();j++)

		{
			lCrlAdresStr+=lFullName.at(j).toString();
		}
		lAdresList.append(lCrlAdresStr);
	}

	return lAdresList;
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString CRLDistributionPoints::eklentiAdiAl() const 
{
	return DIL_EXT_SIL_DAGITIM_NOKTALARI;
}

QString CRLDistributionPoints::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString CRLDistributionPoints::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* CRLDistributionPoints::kendiniKopyala() const 
{
	return (AY_Eklenti* )new CRLDistributionPoints(*this);
}

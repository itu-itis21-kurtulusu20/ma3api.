#include "DistributionPoint.h"

using namespace esya;

DistributionPoint::DistributionPoint(void)
{
}

DistributionPoint::DistributionPoint(const DistributionPoint &iDP)
:	mDPNPresent(iDP.isDPNPresent()),
	mDPN(iDP.getDPN()),
	mReasonsPresent(iDP.isReasonsPresent()),
	mReasons(iDP.getReasons()),
	mCRLIssuerPresent(iDP.isCRLIssuerPresent()),
	mCRLIssuer(iDP.getCRLIssuer())
{
}

DistributionPoint::DistributionPoint(const ASN1T_IMP_DistributionPoint & iDP )
{
	copyFromASNObject(iDP);
}

DistributionPoint::DistributionPoint(const QByteArray & iDP)
{
	constructObject(iDP);	
}

DistributionPoint & DistributionPoint::operator=(const DistributionPoint& iDP)
{
	mDPNPresent			= iDP.isDPNPresent();
	mReasonsPresent		= iDP.isReasonsPresent();
	mCRLIssuerPresent	= iDP.isCRLIssuerPresent();
	if (mDPNPresent )	 
		mDPN				= iDP.getDPN();
	if (mReasonsPresent) 
		mReasons			= iDP.getReasons();
	if (mCRLIssuerPresent) 
		mCRLIssuer			= iDP.getCRLIssuer();
	return (*this);
}

bool esya::operator==(const DistributionPoint& iRHS, const DistributionPoint& iLHS)
{
	if (	iRHS.isDPNPresent()			!= iLHS.isDPNPresent()
		||  iRHS.isReasonsPresent()		!= iLHS.isReasonsPresent()
		||	iRHS.isCRLIssuerPresent()	!= iLHS.isCRLIssuerPresent())
		return false;

	if ( iRHS.isDPNPresent() )
	{
		if ( iRHS.getDPN() != iLHS.getDPN() )
			return false;
	}
	if ( iRHS.isReasonsPresent() )
	{
		if ( iRHS != iLHS  )
			return false;
	}
	if ( iRHS.isCRLIssuerPresent() )
	{
		if ( iRHS.getCRLIssuer() != iLHS.getCRLIssuer() )
			return false;
	}
	return true;
}	

bool esya::operator!=(const DistributionPoint& iRHS, const DistributionPoint& iLHS)
{
	return (!( iRHS == iLHS ));
}


int DistributionPoint::copyFromASNObject(const ASN1T_IMP_DistributionPoint& iDP)
{
	mDPNPresent			= iDP.m.distributionPointPresent;
	mReasonsPresent		= iDP.m.reasonsPresent;
	mCRLIssuerPresent	= iDP.m.cRLIssuerPresent;

	if ( mDPNPresent )
	{
		mDPN.copyFromASNObject(iDP.distributionPoint);
	}
	if ( mReasonsPresent )
	{
		mReasons.copyFromASNObject(iDP.reasons);
	}
	if ( mCRLIssuerPresent )
	{
		GeneralName().copyGeneralNames(iDP.cRLIssuer,mCRLIssuer);
	}
	return SUCCESS;
}

int DistributionPoint::copyToASNObject(ASN1T_IMP_DistributionPoint & oDP) const
{
	oDP.m.distributionPointPresent	= mDPNPresent;
	oDP.m.reasonsPresent			= mReasonsPresent;
	oDP.m.cRLIssuerPresent			= mCRLIssuerPresent	;	

	if ( mDPNPresent )
	{
		mDPN.copyToASNObject(oDP.distributionPoint);
	}
	if ( mReasonsPresent )
	{
		mReasons.copyToASNObject(oDP.reasons);
	}
	if ( mCRLIssuerPresent )
	{
		GeneralName().copyGeneralNames(mCRLIssuer,oDP.cRLIssuer);
	}
	return SUCCESS;
}

void DistributionPoint::freeASNObject(ASN1T_IMP_DistributionPoint & oDP)const
{
	if ( oDP.m.distributionPointPresent)
	{
		DistributionPointName().freeASNObject(oDP.distributionPoint);
	}
	if ( oDP.m.cRLIssuerPresent )
	{
		GeneralName().freeASNObjects(oDP.cRLIssuer);
	}
	if (oDP.m.reasonsPresent)
	{
		ReasonFlags().freeASNObject(oDP.reasons);
	}
}

int DistributionPoint::copyCDPs(const ASN1T_IMP_CRLDistributionPoints & iCDPs, QList<DistributionPoint>& oList)
{
	return copyASNObjects<DistributionPoint>(iCDPs,oList);
}

int DistributionPoint::copyCDPs(const QList<DistributionPoint> iList , ASN1T_IMP_CRLDistributionPoints& oCDPs)
{
	return copyASNObjects<DistributionPoint>(iList,oCDPs);
}

const bool DistributionPoint::isDPNPresent()const
{
	return mDPNPresent;
}

const bool DistributionPoint::isReasonsPresent()const
{
	return mReasonsPresent;
}

const bool DistributionPoint::isCRLIssuerPresent()const
{
	return mCRLIssuerPresent;
}

const DistributionPointName& DistributionPoint::getDPN() const
{
	return mDPN;
}

const ReasonFlags& DistributionPoint::getReasons() const
{
	return mReasons;
}

const QList<GeneralName> & DistributionPoint::getCRLIssuer() const
{
	return mCRLIssuer;
}

QList<QString> DistributionPoint::cdpAdresleriAl(const Name & aIssuer) const
{
	QList<QString> cdp;
	switch (mDPN.getType())
	{
		case DistributionPointName::FULLNAME:
			{
				/*If the DistributionPointName contains multiple values, each name
				describes a different mechanism to obtain the same CRL. For example,
				the same CRL could be available for retrieval through both LDAP and
				HTTP.*/
				const QList<GeneralName> gnArray = mDPN.getFullName();

				for (int j = 0; j < gnArray.size(); j++)
				{
					QString adres = gnArray[j].toString(); 
					cdp.append(adres);
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
				Name crlIssuer;
				if (mCRLIssuerPresent)
				{
				}else
				{
					crlIssuer = aIssuer;
				}
				//issuer alalým
			}
	default:
		break;
	}
	return cdp;		
}


DistributionPoint::~DistributionPoint(void)
{
}

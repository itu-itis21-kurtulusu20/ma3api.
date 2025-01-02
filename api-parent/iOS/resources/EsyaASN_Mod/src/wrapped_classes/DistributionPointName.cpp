#include "DistributionPointName.h"

using namespace esya;

DistributionPointName::DistributionPointName(void)
{
}

DistributionPointName::DistributionPointName(const DistributionPointName & iDPN)
:	mType( iDPN.getType() ),
	mFullName(iDPN.getFullName()),
	mNameR2CRLIssuer(iDPN.getNameR2CRLIssuer())
{
}

DistributionPointName::DistributionPointName(const ASN1T_IMP_DistributionPointName & iDPN)
{
	copyFromASNObject(iDPN);
}

DistributionPointName::DistributionPointName(const QByteArray & iDPN)
{
	constructObject(iDPN);
}

DistributionPointName & DistributionPointName::operator=(const DistributionPointName& iDPN)
{
	mType = iDPN.getType();
	mFullName = iDPN.getFullName();
	mNameR2CRLIssuer = iDPN.getNameR2CRLIssuer();
	return (*this);
}

bool esya::operator==(const DistributionPointName& iRHS, const DistributionPointName& iLHS)
{
	if (iRHS.getType() != iLHS.getType()) 
		return false;
	if (iRHS.getType() == DistributionPointName::FULLNAME )
	{
		if (iRHS.getFullName().size() != iLHS.getFullName().size()) 
				return false;
		for (int i = 0 ; i < iRHS.getFullName().size() ; i++ )
		{
			if (iRHS.getFullName()[i] != iLHS.getFullName()[i])
				return false;
		}
		return true;
	}
	if (iRHS.getType() == DistributionPointName::NAMER2CRLISSUER )
	{
		return (iRHS.getNameR2CRLIssuer() == iLHS.getNameR2CRLIssuer());
	}
	return false;
}

bool esya::operator!=(const DistributionPointName& iRHS, const DistributionPointName& iLHS)
{
	return (!(iRHS == iLHS));
}

int DistributionPointName::copyFromASNObject(const ASN1T_IMP_DistributionPointName& iDPN)
{
	mType = (DPN_Type) iDPN.t;
	switch (mType)
	{
		case FULLNAME:
			{
				GeneralName().copyGeneralNames(*iDPN.u.fullName,mFullName);
				break;
			}
		case NAMER2CRLISSUER:
			{
				mNameR2CRLIssuer = RelativeDistinguishedName(*iDPN.u.nameRelativeToCRLIssuer);
				break;
			}
	}
	return SUCCESS;
}

int DistributionPointName::copyToASNObject(ASN1T_IMP_DistributionPointName & oDPN) const
{
	oDPN.t = mType;
	switch (mType)
	{
		case FULLNAME:
			{
				oDPN.u.fullName = new ASN1T_IMP_GeneralNames();
				GeneralName().copyGeneralNames(mFullName , *oDPN.u.fullName);
				break;
			}
		case NAMER2CRLISSUER:
			{
				oDPN.u.nameRelativeToCRLIssuer = mNameR2CRLIssuer.getASNCopy();				
				break;
			}
	}
	return SUCCESS;
}

void DistributionPointName::freeASNObject(ASN1T_IMP_DistributionPointName & oDPN)const
{
	switch (oDPN.t)
	{
		case FULLNAME:
			{
				GeneralName().freeASNObjects(*oDPN.u.fullName);
				DELETE_MEMORY(oDPN.u.fullName)
				break;
			}
		case NAMER2CRLISSUER:
			{
				RelativeDistinguishedName().freeASNObjectPtr(oDPN.u.nameRelativeToCRLIssuer);
				break;
			}
	}
}

const DistributionPointName::DPN_Type& DistributionPointName::getType() const
{
	return mType;
}

const QList<GeneralName>& DistributionPointName::getFullName() const
{
	return mFullName;
}

const RelativeDistinguishedName & DistributionPointName::getNameR2CRLIssuer() const
{
	return mNameR2CRLIssuer;
}

DistributionPointName::~DistributionPointName(void)
{
}

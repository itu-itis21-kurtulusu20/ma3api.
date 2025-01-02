#include "CertStatus.h"

using namespace esya;

CertStatus::CertStatus(void)
: pRI(NULL)
{
}

CertStatus::CertStatus(const CertStatus &iCS)
:	mType(iCS.getType()),
	pRI(NULL)
{
	if ( mType == CS_Revoked)
		pRI =new RevokedInfo( *iCS.getRevokedInfo() );

}

CertStatus::CertStatus(const ASN1T_OCSP_CertStatus & iCS)
: pRI(NULL)
{
	copyFromASNObject(iCS);
}

CertStatus::CertStatus(const QByteArray & iCS)
: pRI(NULL)
{
	constructObject(iCS);
}


CertStatus & CertStatus::operator=(const CertStatus & iCS)
{
	if ( pRI != iCS.getRevokedInfo())
		DELETE_MEMORY(pRI)

	pRI = new RevokedInfo(*iCS.getRevokedInfo());
	mType = iCS.getType();
	return *this;
}

bool esya::operator==(const CertStatus & iRHS, const CertStatus & iLHS)
{
	if ( iRHS.getType() != iLHS.getType() ) 
		return false;
	if (iRHS.getType() == CertStatus::CS_Revoked ) 
		return ( *iRHS.getRevokedInfo() == *iLHS.getRevokedInfo()); 
	return true;
}

bool esya::operator!=(const CertStatus & iRHS, const CertStatus & iLHS)
{
	return ( !( iRHS == iLHS) ); 
}


int CertStatus::copyFromASNObject(const ASN1T_OCSP_CertStatus& iCS)
{
	mType= (CertStatus::CS_Type)iCS.t;
	if ( iCS.t == CS_Revoked)
		pRI = new RevokedInfo( *iCS.u.revoked );
	return SUCCESS;
}
	
int CertStatus::copyToASNObject(ASN1T_OCSP_CertStatus & oCS) const
{
	oCS.t = mType;
	if ( mType == CS_Revoked)
		oCS.u.revoked = pRI->getASNCopy(); 
	return SUCCESS;
}
	
void CertStatus::freeASNObject(ASN1T_OCSP_CertStatus& oCS)const
{
	if (oCS.t == CS_Revoked)
	{
		RevokedInfo().freeASNObjectPtr(oCS.u.revoked);
	}
}

const CertStatus::CS_Type& CertStatus::getType() const
{
	return mType;
}

const RevokedInfo* CertStatus::getRevokedInfo() const
{
	return pRI;
}

void CertStatus::setType(const CS_Type & iType)
{
	mType = iType;
}

void CertStatus::setRevokedInfo(RevokedInfo* iRI)
{
	if (pRI != iRI)
		DELETE_MEMORY(pRI)

	pRI = iRI;
}

CertStatus::~CertStatus(void)
{
	DELETE_MEMORY(pRI)
}


QString CertStatus::toString()const
{
	switch (mType)
	{
		case CS_Good :
			{
				return ST_CS_GOOD;
			}
		case CS_Revoked:
			{
				return ST_CS_REVOKED;
			}
		case CS_Unknown:
			{
				return ST_CS_UNKNOWN;
			}
	}
}


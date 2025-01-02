#include "RevocationInfoChoice.h"

using namespace esya;


RevocationInfoChoice::RevocationInfoChoice(void)
{
}

RevocationInfoChoice::RevocationInfoChoice(const QByteArray & iRIC)
{
	constructObject(iRIC);
}

RevocationInfoChoice::RevocationInfoChoice(const ASN1T_CMS_RevocationInfoChoice & iRIC)
{
	copyFromASNObject(iRIC);
}

RevocationInfoChoice::RevocationInfoChoice(const RevocationInfoChoice& iRIC)
: mType(iRIC.getType()) , mCertificateList(iRIC.getCertificateList())
{
}

RevocationInfoChoice& RevocationInfoChoice::operator=(const RevocationInfoChoice & iRIC)
{
	mType				= iRIC.getType();
	mCertificateList	= iRIC.getCertificateList();
	return (*this);
}

bool esya::operator==(const RevocationInfoChoice & iRHS,const RevocationInfoChoice & iLHS )
{
	if (iRHS.getType() == iLHS.getType() && iRHS.getType() == T_CertificateList )	
	{
		if ( iRHS.getCertificateList() == iLHS.getCertificateList() ) 
			return true;
	}

	return false;
}

bool esya::operator!=(const RevocationInfoChoice & iRHS,const RevocationInfoChoice & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int RevocationInfoChoice::copyFromASNObject(const ASN1T_CMS_RevocationInfoChoice & iRIC)
{
	mType = (RIType)iRIC.t;
	if ( mType == T_CertificateList && iRIC.u.crl)
	{
		mCertificateList.copyFromASNObject(*iRIC.u.crl);
	}
	else 
	{
		throw EException("Desteklenmeyen RI Tipi",__FILE__,__LINE__);
	}
	return SUCCESS;
}

int RevocationInfoChoice::copyToASNObject(ASN1T_CMS_RevocationInfoChoice & oRIC)const
{
	oRIC.t = mType;
	if ( mType == T_CertificateList )
	{
		oRIC.u.crl = mCertificateList.getASNCopy();
	}
	return SUCCESS;
}

void RevocationInfoChoice::freeASNObject(ASN1T_CMS_RevocationInfoChoice& oRIC)const
{
	if ( oRIC.t == T_CertificateList && oRIC.u.crl)
	{
		ECertificateList().freeASNObjectPtr(oRIC.u.crl);
	}
}

int RevocationInfoChoice::copyRICs(const ASN1T_CMS_RevocationInfoChoices & iRICs, QList<RevocationInfoChoice>& oList)
{
	return copyASNObjects<RevocationInfoChoice>(iRICs,oList);
}

int RevocationInfoChoice::copyRICs(const QList<RevocationInfoChoice> & iList ,ASN1T_CMS_RevocationInfoChoices& oRICs)
{
	return copyASNObjects<RevocationInfoChoice>(iList,oRICs);
}

int	RevocationInfoChoice::copyRICs(const QByteArray & iASNBytes, QList<RevocationInfoChoice>& oList)
{
	return copyASNObjects<ASN1T_CMS_RevocationInfoChoices,ASN1C_CMS_RevocationInfoChoices,RevocationInfoChoice>(iASNBytes,oList);
}

int	RevocationInfoChoice::copyRICs(const QList<RevocationInfoChoice>& iList,QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_CMS_RevocationInfoChoices,ASN1C_CMS_RevocationInfoChoices,RevocationInfoChoice>(iList,oASNBytes);
}


const RIType & RevocationInfoChoice::getType() const 
{
	return mType;
}

const ECertificateList& RevocationInfoChoice::getCertificateList() const
{
	return mCertificateList;
}

RevocationInfoChoice::~RevocationInfoChoice(void)
{
}

#include "OriginatorInfo.h"

using namespace esya;

OriginatorInfo::OriginatorInfo(void)
{
}

OriginatorInfo::OriginatorInfo( const ASN1T_CMS_OriginatorInfo &iORI)
{
	copyFromASNObject(iORI);
}

OriginatorInfo::OriginatorInfo( const QByteArray &iORI)
{
	constructObject(iORI);
}

OriginatorInfo::OriginatorInfo( const OriginatorInfo& iORI)
:	mCerts(iORI.getCerts()),
	mCRLs(iORI.getCRLs())
{
}

OriginatorInfo & OriginatorInfo::operator=(const OriginatorInfo & iORI)
{
	mCerts	= iORI.getCerts();
	mCRLs	= iORI.getCRLs();
	return *this;
}

bool esya::operator==(const OriginatorInfo & iRHS,const OriginatorInfo & iLHS)
{
	return (	(	iRHS.getCerts() == iLHS.getCerts()	) &&
				(	iRHS.getCRLs()	== iLHS.getCRLs()	)		);
}

bool esya::operator!=(const OriginatorInfo & iRHS,const OriginatorInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int OriginatorInfo::copyFromASNObject(const ASN1T_CMS_OriginatorInfo & iORI)
{
	if (iORI.m.certsPresent)
		ECertChoices().copyCS(iORI.certs,mCerts);
	if (iORI.m.crlsPresent)
		RevocationInfoChoice().copyRICs(iORI.crls,mCRLs);
	return SUCCESS;
}

int OriginatorInfo::copyToASNObject(ASN1T_CMS_OriginatorInfo & oORI)const
{
	oORI.m.certsPresent = mCerts.size()>0;
	oORI.m.crlsPresent	= mCRLs.size()>0;

	if (oORI.m.certsPresent)
		ECertChoices().copyCS(mCerts,oORI.certs);
	if (oORI.m.crlsPresent)
		RevocationInfoChoice().copyRICs(mCRLs,oORI.crls);

	return SUCCESS;
}

void OriginatorInfo::freeASNObject(ASN1T_CMS_OriginatorInfo & oORI)const
{
	if ( oORI.m.certsPresent )
		ECertChoices().freeASNObjects(oORI.certs);
	if ( oORI.m.crlsPresent ) 
		RevocationInfoChoice().freeASNObjects(oORI.crls);
}

const QList<ECertChoices> & OriginatorInfo::getCerts()const
{
	return mCerts;
}

const QList<RevocationInfoChoice> & OriginatorInfo::getCRLs()const
{
	return mCRLs;
}

void OriginatorInfo::setCerts(const QList<ECertChoices> & iCerts )
{
	mCerts = iCerts;
}

void OriginatorInfo::setCRLs(const QList<RevocationInfoChoice> & iCRLs )
{
	mCRLs = iCRLs;
}

void OriginatorInfo::addCert(const ECertChoices & iCert)
{
	mCerts.append(iCert);
}

void OriginatorInfo::addCRL(const RevocationInfoChoice & iCRL)
{
	mCRLs.append(iCRL);
}

OriginatorInfo::~OriginatorInfo(void)
{
}

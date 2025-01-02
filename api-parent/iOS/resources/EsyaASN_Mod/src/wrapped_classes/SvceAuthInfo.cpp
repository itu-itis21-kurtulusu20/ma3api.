#include "SvceAuthInfo.h"

using namespace esya;

SvceAuthInfo::SvceAuthInfo(void)
:	mAuthInfoPresent(false)
{
}

SvceAuthInfo::SvceAuthInfo(const QByteArray & iSvceAuthInfo)
{
	constructObject(iSvceAuthInfo);
}

SvceAuthInfo::SvceAuthInfo(const ASN1T_ATTRCERT_SvceAuthInfo & iSvceAuthInfo )
{
	copyFromASNObject(iSvceAuthInfo);
}

SvceAuthInfo::SvceAuthInfo(const SvceAuthInfo& iSvceAuthInfo)
:	mService(iSvceAuthInfo.getService()),
	mIdent(iSvceAuthInfo.getIdent())
{
	if (mAuthInfoPresent = iSvceAuthInfo.isAuthInfoPresent())
		mAuthInfo = iSvceAuthInfo.getAuthInfo();
}

SvceAuthInfo& SvceAuthInfo::operator=(const SvceAuthInfo& iSvceAuthInfo)
{
	mService = iSvceAuthInfo.getService();
	mIdent = iSvceAuthInfo.getIdent();

	if (mAuthInfoPresent = iSvceAuthInfo.isAuthInfoPresent())
		mAuthInfo = iSvceAuthInfo.getAuthInfo();

	return *this;
}

bool esya::operator==( const SvceAuthInfo& iRHS, const SvceAuthInfo& iLHS)
{
	if ( ( iRHS.getService()!= iLHS.getService() ) ||
		 ( iRHS.getIdent()	!= iLHS.getIdent() )	) 
		return false;

	if ( iRHS.isAuthInfoPresent() != iLHS.isAuthInfoPresent() )
		return false;

	if (	iRHS.isAuthInfoPresent() &&
		 (  iRHS.getAuthInfo() != iLHS.getAuthInfo() ) )
		return false;
	
	return true;
}

bool esya::operator!=( const SvceAuthInfo& iRHS, const SvceAuthInfo& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int SvceAuthInfo::copyFromASNObject(const ASN1T_ATTRCERT_SvceAuthInfo & iSvceAuthInfo)
{
	mService.copyFromASNObject(iSvceAuthInfo.service);	
	mIdent.copyFromASNObject(iSvceAuthInfo.ident);	
	
	mAuthInfoPresent= ( iSvceAuthInfo.m.authInfoPresent == 1 ); 
	if (mAuthInfoPresent)
	{
		mAuthInfo = QByteArray((const char*)iSvceAuthInfo.authInfo.data,iSvceAuthInfo.authInfo.numocts);
	}
	return SUCCESS;
}

int SvceAuthInfo::copyToASNObject(ASN1T_ATTRCERT_SvceAuthInfo &oSvceAuthInfo)const
{
	mService.copyToASNObject(oSvceAuthInfo.service);	
	mIdent.copyToASNObject(oSvceAuthInfo.ident);	

	oSvceAuthInfo.m.authInfoPresent = mAuthInfoPresent ? 1:0 ; 
	if (mAuthInfoPresent)
	{
		oSvceAuthInfo.authInfo.data = (OSOCTET*)myStrDup(mAuthInfo.data(),mAuthInfo.size()) ;
		oSvceAuthInfo.authInfo.numocts = mAuthInfo.size();
	}

	return SUCCESS;
}

void SvceAuthInfo::freeASNObject(ASN1T_ATTRCERT_SvceAuthInfo& oSvceAuthInfo)const
{
	GeneralName().freeASNObject(oSvceAuthInfo.service);	
	GeneralName().freeASNObject(oSvceAuthInfo.ident);	

	if (oSvceAuthInfo.m.authInfoPresent ==1)
	{
		DELETE_MEMORY_ARRAY(oSvceAuthInfo.authInfo.data);
		oSvceAuthInfo.authInfo.numocts = 0;
	}
}

bool SvceAuthInfo::isAuthInfoPresent()const
{
	return mAuthInfoPresent;
}

const GeneralName & SvceAuthInfo::getService()const 
{
	return mService;
}

const GeneralName & SvceAuthInfo::getIdent()const
{
	return mIdent;
}

const QByteArray &  SvceAuthInfo::getAuthInfo()const
{
	return mAuthInfo;
}

void SvceAuthInfo::setAuthInfoPresent(bool iAIP)
{
	mAuthInfoPresent = iAIP;
}

void SvceAuthInfo::setService(const GeneralName & iService)
{
	mService = iService;
}

void SvceAuthInfo::setIdent(const GeneralName & iIdent)
{
	mIdent = iIdent;
}

void SvceAuthInfo::setAuthInfo(const QByteArray &  iAuthInfo)
{
	mAuthInfoPresent = !iAuthInfo.isEmpty(); 
	mAuthInfo = iAuthInfo;
}

SvceAuthInfo::~SvceAuthInfo(void)
{
}
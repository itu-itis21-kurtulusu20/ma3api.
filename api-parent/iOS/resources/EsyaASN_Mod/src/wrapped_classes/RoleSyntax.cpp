#include "RoleSyntax.h"


using namespace esya;

namespace esya
{
	RoleSyntax::RoleSyntax(void)
	:	mRoleAuthorityPresent(false)
	{
	}

	RoleSyntax::RoleSyntax(const QByteArray & iRoleSyntax)
	{
		constructObject(iRoleSyntax);
	}

	RoleSyntax::RoleSyntax(const ASN1T_ATTRCERT_RoleSyntax & iRoleSyntax )
	{
		copyFromASNObject(iRoleSyntax);
	}

	RoleSyntax::RoleSyntax(const RoleSyntax& iRoleSyntax)
	:	mRoleName(iRoleSyntax.getRoleName())
	{
		if (mRoleAuthorityPresent = iRoleSyntax.isRoleAuthorityPresent())
			mRoleAuthority = iRoleSyntax.getRoleAuthority();
	}

	RoleSyntax& RoleSyntax::operator=(const RoleSyntax& iRoleSyntax)
	{
		mRoleName = iRoleSyntax.getRoleName();

		if (mRoleAuthorityPresent = iRoleSyntax.isRoleAuthorityPresent())
			mRoleAuthority = iRoleSyntax.getRoleAuthority();

		return *this;
	}

	bool operator==( const RoleSyntax& iRHS, const RoleSyntax& iLHS)
	{
		if ( ( iRHS.getRoleName()!= iLHS.getRoleName() ) ||
			 ( iRHS.isRoleAuthorityPresent() != iLHS.isRoleAuthorityPresent() )	) 
			return false;

		if (	iRHS.isRoleAuthorityPresent() &&
			 (  iRHS.getRoleAuthority() != iLHS.getRoleAuthority() ) )
			return false;
		
		return true;
	}

	bool operator!=( const RoleSyntax& iRHS, const RoleSyntax& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int RoleSyntax::copyFromASNObject(const ASN1T_ATTRCERT_RoleSyntax & iRoleSyntax)
	{
		mRoleName.copyFromASNObject(iRoleSyntax.roleName);	

		mRoleAuthorityPresent= ( iRoleSyntax.m.roleAuthorityPresent == 1 ); 
		if (mRoleAuthorityPresent)
		{
			mRoleAuthority.copyFromASNObject(iRoleSyntax.roleAuthority);
		}
		return SUCCESS;
	}

	int RoleSyntax::copyToASNObject(ASN1T_ATTRCERT_RoleSyntax &oRoleSyntax)const
	{
		mRoleName.copyToASNObject(oRoleSyntax.roleName);	

		oRoleSyntax.m.roleAuthorityPresent = mRoleAuthorityPresent ? 1:0 ; 
		if (mRoleAuthorityPresent)
		{
			mRoleAuthority.copyToASNObject(oRoleSyntax.roleAuthority);
		}

		return SUCCESS;
	}

	void RoleSyntax::freeASNObject(ASN1T_ATTRCERT_RoleSyntax& oRoleSyntax) const
	{
		GeneralName().freeASNObject(oRoleSyntax.roleName);	

		if (oRoleSyntax.m.roleAuthorityPresent ==1)
		{
			GeneralNames().freeASNObject(oRoleSyntax.roleAuthority);
		}
	}

	bool RoleSyntax::isRoleAuthorityPresent()const
	{
		return mRoleAuthorityPresent;
	}

	const GeneralName & RoleSyntax::getRoleName()const 
	{
		return mRoleName;
	}

	const GeneralNames & RoleSyntax::getRoleAuthority()const 
	{
		return mRoleAuthority;
	}


	void RoleSyntax::setRoleAuthorityPresent(bool iRAP)
	{
		mRoleAuthorityPresent = iRAP;
	}

	void RoleSyntax::setRoleName(const GeneralName & iRoleName)
	{
		mRoleName = iRoleName;
	}

	void RoleSyntax::setRoleAuthority(const GeneralNames & iRoleAuthority)
	{
		mRoleAuthorityPresent = true;
		mRoleAuthority = iRoleAuthority;
	}

	RoleSyntax::~RoleSyntax(void)
	{
	}

}
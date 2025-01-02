#ifndef __ROLESYNTAX__
#define __ROLESYNTAX__


#include "GeneralNames.h"


namespace esya
{

	class Q_DECL_EXPORT RoleSyntax : public EASNWrapperTemplate<ASN1T_ATTRCERT_RoleSyntax,ASN1C_ATTRCERT_RoleSyntax>
	{
		bool mRoleAuthorityPresent;

		GeneralName		mRoleName;
		GeneralNames	mRoleAuthority;		

	public:
		RoleSyntax(void);
		RoleSyntax(const QByteArray & iRoleSyntax);
		RoleSyntax(const ASN1T_ATTRCERT_RoleSyntax & iRoleSyntax );
		RoleSyntax(const RoleSyntax& iRoleSyntax);

		RoleSyntax& operator=(const RoleSyntax& iRoleSyntax);
		friend bool operator==( const RoleSyntax& iRHS, const RoleSyntax& iLHS);
		friend bool operator!=( const RoleSyntax& iRHS, const RoleSyntax& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_RoleSyntax & iRoleSyntax);
		int copyToASNObject(ASN1T_ATTRCERT_RoleSyntax &oRoleSyntax)const;
		void freeASNObject(ASN1T_ATTRCERT_RoleSyntax& oRoleSyntax)const;

		virtual ~RoleSyntax(void);

		// GETTERS AND SETTERS

		bool isRoleAuthorityPresent()const;
		
		const GeneralName & getRoleName()const ;
		const GeneralNames & getRoleAuthority()const;

		void setRoleAuthorityPresent(bool iRAP);

		void setRoleName(const GeneralName & iRoleName);
		void setRoleAuthority(const GeneralNames & iRoleAuthority);
	};

}

#endif


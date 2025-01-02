#ifndef __SVCEAUTHINFO__
#define __SVCEAUTHINFO__


#include "GeneralNames.h"


namespace esya
{

	class SvceAuthInfo : public EASNWrapperTemplate<ASN1T_ATTRCERT_SvceAuthInfo,ASN1C_ATTRCERT_SvceAuthInfo>
	{
		bool mAuthInfoPresent;

		GeneralName mService;
		GeneralName mIdent;
		QByteArray  mAuthInfo;		

	public:
		SvceAuthInfo(void);
		SvceAuthInfo(const QByteArray & iSvceAuthInfo);
		SvceAuthInfo(const ASN1T_ATTRCERT_SvceAuthInfo & iSvceAuthInfo );
		SvceAuthInfo(const SvceAuthInfo& iSvceAuthInfo);

		SvceAuthInfo& operator=(const SvceAuthInfo& iSvceAuthInfo);
		friend bool operator==( const SvceAuthInfo& iRHS, const SvceAuthInfo& iLHS);
		friend bool operator!=( const SvceAuthInfo& iRHS, const SvceAuthInfo& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_SvceAuthInfo & iSvceAuthInfo);
		int copyToASNObject(ASN1T_ATTRCERT_SvceAuthInfo &oSvceAuthInfo)const;
		void freeASNObject(ASN1T_ATTRCERT_SvceAuthInfo& oSvceAuthInfo)const;

		virtual ~SvceAuthInfo(void);

		// GETTERS AND SETTERS

		bool isAuthInfoPresent()const;
		
		const GeneralName & getService()const ;
		const GeneralName & getIdent()const;
		const QByteArray &  getAuthInfo()const;		

		void setAuthInfoPresent(bool iPAP);

		void setService(const GeneralName & iService);
		void setIdent(const GeneralName & iIdent);
		void setAuthInfo(const QByteArray &  iAuthInfo);		
	};

}

#endif


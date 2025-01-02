
#ifndef __BUILTINSTANDARDATTRIBUTES__
#define __BUILTINSTANDARDATTRIBUTES__

#include "CountryName.h"
#include "ortak.h"
#include "EException.h"

#include "CountryName.h"				
#include "AdministrationDomainName.h"
#include "NetworkAddress.h"			
#include "TerminalIdentifier.h"		
#include "PrivateDomainName.h"		
#include "OrganizationName.h"		
#include "NumericUserIdentifier.h"	
#include "PersonalName.h"			
#include "OrganizationalUnitName.h"	


namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT BuiltInStandardAttributes  : public EASNWrapperTemplate<ASN1T_EXP_BuiltInStandardAttributes,ASN1C_EXP_BuiltInStandardAttributes>
	{
		bool mCountryNamePresent;
		bool mAdministrationDomainNamePresent;
		bool mNetworkAddressPresent;
		bool mTerminalIdentifierPresent;
		bool mPrivateDomainNamePresent;
		bool mOrganizationNamePresent;
		bool mNumericUserIdentifierPresent;
		bool mPersonalNamePresent;
		bool mOrganizationalUnitNamesPresent;

		CountryName						mCountryName;
		AdministrationDomainName		mAdministrationDomainName;
		NetworkAddress					mNetworkAddress;
		TerminalIdentifier				mTerminalIdentifier;
		PrivateDomainName				mPrivateDomainName;
		OrganizationName				mOrganizationName;
		NumericUserIdentifier			mNumericUserIdentifier;
		PersonelName					mPersonalName;
		QList<OrganizationalUnitName>	mOrganizationalUnitNames;
		

	public:

		BuiltInStandardAttributes(void);
		BuiltInStandardAttributes( const ASN1T_EXP_BuiltInStandardAttributes &);
		BuiltInStandardAttributes( const BuiltInStandardAttributes &);
		BuiltInStandardAttributes( const QByteArray &);

		BuiltInStandardAttributes & operator=(const BuiltInStandardAttributes&);

		Q_DECL_EXPORT friend bool operator==(const BuiltInStandardAttributes& iRHS,const BuiltInStandardAttributes& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const BuiltInStandardAttributes& iRHS, const BuiltInStandardAttributes& iLHS);

		int copyFromASNObject(const ASN1T_EXP_BuiltInStandardAttributes & iBSA);
		int copyToASNObject(ASN1T_EXP_BuiltInStandardAttributes & oBSA) const;
		void freeASNObject(ASN1T_EXP_BuiltInStandardAttributes& oBSA)const;

		virtual ~BuiltInStandardAttributes(void);


		// GETTERS AND SETTERS

		bool isCountryNamePresent()const;
		bool isAdministrationDomainNamePresent()const;
		bool isNetworkAddressPresent()const;
		bool isTerminalIdentifierPresent()const;
		bool isPrivateDomainNamePresent()const;
		bool isOrganizationNamePresent()const;
		bool isNumericUserIdentifierPresent()const;
		bool isPersonalNamePresent()const;
		bool isOrganizationalUnitNamesPresent()const;

		const CountryName& 					getCountryName()const;
		const AdministrationDomainName&		getAdministrationDomainName()const;
		const NetworkAddress&				getNetworkAddress()const;
		const TerminalIdentifier&			getTerminalIdentifier()const;
		const PrivateDomainName&			getPrivateDomainName()const;
		const OrganizationName&				getOrganizationName()const;
		const NumericUserIdentifier&		getNumericUserIdentifier()const;
		const PersonelName&					getPersonalName()const;
		const QList<OrganizationalUnitName>&getOrganizationalUnitNames()const;	

		void setCountryName(const CountryName&);
		void setAdministrationDomainName(const AdministrationDomainName&);
		void setNetworkAddress(const NetworkAddress&);
		void setTerminalIdentifier(const TerminalIdentifier&);
		void setPrivateDomainName(const PrivateDomainName&);
		void setOrganizationName(const OrganizationName&);
		void setNumericUserIdentifier(const NumericUserIdentifier&);
		void setPersonalName(const PersonelName&);
		void setOrganizationalUnitNames(const QList<OrganizationalUnitName>&);	


	};

}

#endif


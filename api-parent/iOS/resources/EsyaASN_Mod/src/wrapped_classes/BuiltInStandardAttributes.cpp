#include "BuiltInStandardAttributes.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

BuiltInStandardAttributes::BuiltInStandardAttributes(void)
{
}

BuiltInStandardAttributes::BuiltInStandardAttributes( const ASN1T_EXP_BuiltInStandardAttributes &iBSA)
{
	copyFromASNObject(iBSA);
}

BuiltInStandardAttributes::BuiltInStandardAttributes( const BuiltInStandardAttributes &iBSA)
:	mCountryNamePresent(iBSA.isCountryNamePresent()), 
	mAdministrationDomainNamePresent(iBSA.isAdministrationDomainNamePresent()),
	mNetworkAddressPresent(iBSA.isNetworkAddressPresent()),
	mTerminalIdentifierPresent(iBSA.isTerminalIdentifierPresent()),
	mPrivateDomainNamePresent(iBSA.isPrivateDomainNamePresent()),
	mOrganizationNamePresent(iBSA.isOrganizationNamePresent()),
	mNumericUserIdentifierPresent(iBSA.isNumericUserIdentifierPresent())	,
	mPersonalNamePresent(iBSA.isPersonalNamePresent()),
	mOrganizationalUnitNamesPresent(iBSA.isOrganizationalUnitNamesPresent())
{
	mCountryName = iBSA.getCountryName(); 
	mAdministrationDomainName = iBSA.getAdministrationDomainName();
	mNetworkAddress = iBSA.getNetworkAddress();
	mTerminalIdentifier = iBSA.getTerminalIdentifier();
	mPrivateDomainName = iBSA.getPrivateDomainName();
	mOrganizationName = iBSA.getOrganizationName();
	mNumericUserIdentifier = iBSA.getNumericUserIdentifier();
	mPersonalName = iBSA.getPersonalName();
	mOrganizationalUnitNames = iBSA.getOrganizationalUnitNames();
}

BuiltInStandardAttributes::BuiltInStandardAttributes( const QByteArray &iBSA)
{
	constructObject(iBSA);
}

BuiltInStandardAttributes & BuiltInStandardAttributes::operator=(const BuiltInStandardAttributes& iBSA)
{
	mCountryNamePresent = iBSA.isCountryNamePresent(); 
	mAdministrationDomainNamePresent = iBSA.isAdministrationDomainNamePresent();
	mNetworkAddressPresent = iBSA.isNetworkAddressPresent();
	mTerminalIdentifierPresent = iBSA.isTerminalIdentifierPresent();
	mPrivateDomainNamePresent = iBSA.isPrivateDomainNamePresent();
	mOrganizationNamePresent = iBSA.isOrganizationNamePresent();
	mNumericUserIdentifierPresent = iBSA.isNumericUserIdentifierPresent();
	mPersonalNamePresent = iBSA.isPersonalNamePresent();
	mOrganizationalUnitNamesPresent = iBSA.isOrganizationalUnitNamesPresent();

	mCountryName = iBSA.getCountryName(); 
	mAdministrationDomainName = iBSA.getAdministrationDomainName();
	mNetworkAddress = iBSA.getNetworkAddress();
	mTerminalIdentifier = iBSA.getTerminalIdentifier();
	mPrivateDomainName = iBSA.getPrivateDomainName();
	mOrganizationName = iBSA.getOrganizationName();
	mNumericUserIdentifier = iBSA.getNumericUserIdentifier();
	mPersonalName = iBSA.getPersonalName();
	mOrganizationalUnitNames = iBSA.getOrganizationalUnitNames();

	return(*this);
}

bool operator==(const BuiltInStandardAttributes& iRHS,const BuiltInStandardAttributes& iLHS)
{
	if (	( iRHS.isCountryNamePresent() != iLHS.isCountryNamePresent() )		||
			( iRHS.isAdministrationDomainNamePresent() != iLHS.isAdministrationDomainNamePresent() ) ||
			( iRHS.isNetworkAddressPresent() != iLHS.isNetworkAddressPresent() ) ||
			( iRHS.isTerminalIdentifierPresent() != iLHS.isTerminalIdentifierPresent() ) ||
			( iRHS.isPrivateDomainNamePresent() != iLHS.isPrivateDomainNamePresent() ) ||
			( iRHS.isOrganizationNamePresent() != iLHS.isOrganizationNamePresent() ) ||
			( iRHS.isNumericUserIdentifierPresent() != iLHS.isNumericUserIdentifierPresent() ) ||
			( iRHS.isPersonalNamePresent() != iLHS.isPersonalNamePresent() ) ||
			( iRHS.isOrganizationalUnitNamesPresent() != iLHS.isOrganizationalUnitNamesPresent() )
		)
		return false;
	
	if ( iRHS.isCountryNamePresent() && 
		( iRHS.getCountryName() != iLHS.getCountryName()) )
		return false;

	if ( iRHS.isAdministrationDomainNamePresent() && 
		( iRHS.getAdministrationDomainName() != iLHS.getAdministrationDomainName()) )
		return false;

	if ( iRHS.isNetworkAddressPresent() && 
		( iRHS.getNetworkAddress() != iLHS.getNetworkAddress()) )
		return false;

	if ( iRHS.isTerminalIdentifierPresent() && 
		( iRHS.getTerminalIdentifier() != iLHS.getTerminalIdentifier()) )
		return false;
	
	if ( iRHS.isPrivateDomainNamePresent() && 
		( iRHS.getPrivateDomainName() != iLHS.getPrivateDomainName()) )
		return false;

	if ( iRHS.isOrganizationNamePresent() && 
		( iRHS.getOrganizationName() != iLHS.getOrganizationName()) )
		return false;

	if ( iRHS.isNumericUserIdentifierPresent() && 
		( iRHS.getNumericUserIdentifier() != iLHS.getNumericUserIdentifier()) )
		return false;

	if ( iRHS.isPersonalNamePresent() && 
		( iRHS.getPersonalName() != iLHS.getPersonalName()) )
		return false;

	if ( iRHS.isOrganizationalUnitNamesPresent() && 
		( iRHS.getOrganizationalUnitNames() != iLHS.getOrganizationalUnitNames()) )
		return false;

	return true;

}

bool operator!=(const BuiltInStandardAttributes& iRHS, const BuiltInStandardAttributes& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int BuiltInStandardAttributes::copyFromASNObject(const ASN1T_EXP_BuiltInStandardAttributes & iBSA)
{
	mCountryNamePresent = iBSA.m.country_namePresent; 
	mAdministrationDomainNamePresent = iBSA.m.administration_domain_namePresent;
	mNetworkAddressPresent = iBSA.m.network_addressPresent;
	mTerminalIdentifierPresent = iBSA.m.terminal_identifierPresent;
	mPrivateDomainNamePresent = iBSA.m.private_domain_namePresent;
	mOrganizationNamePresent = iBSA.m.organization_namePresent;
	mNumericUserIdentifierPresent = iBSA.m.numeric_user_identifierPresent;
	mPersonalNamePresent = iBSA.m.personal_namePresent;
	mOrganizationalUnitNamesPresent = iBSA.m.organizational_unit_namesPresent;

	if (mCountryNamePresent) 
	{
		mCountryName.copyFromASNObject(iBSA.country_name);
	}
	if (mAdministrationDomainNamePresent)
	{
		mAdministrationDomainName.copyFromASNObject(iBSA.administration_domain_name);
	}

	if (mNetworkAddressPresent)
	{
		mNetworkAddress.copyFromASNObject(iBSA.network_address);
	}
	if (mTerminalIdentifierPresent)
	{
		mTerminalIdentifier.copyFromASNObject(iBSA.terminal_identifier);
	}

	if (mPrivateDomainNamePresent)
	{
		mPrivateDomainName.copyFromASNObject((const ASN1T_EXP_AdministrationDomainName&)iBSA.private_domain_name);
	}
	if (mOrganizationNamePresent)
	{
		mOrganizationName.copyFromASNObject(iBSA.organization_name);
	}
	if (mNumericUserIdentifierPresent)
	{
		mNumericUserIdentifier.copyFromASNObject(iBSA.numeric_user_identifier);
	}
	if (mPersonalNamePresent)
	{
		mPersonalName.copyFromASNObject(iBSA.personal_name);
	}
	if (mOrganizationalUnitNamesPresent)
	{	
		OrganizationalUnitName().copyOUNs(iBSA.organizational_unit_names,mOrganizationalUnitNames);
	}

	return SUCCESS;
}

int BuiltInStandardAttributes::copyToASNObject(ASN1T_EXP_BuiltInStandardAttributes & oBSA ) const
{
	oBSA.m.country_namePresent = mCountryNamePresent; 
	oBSA.m.administration_domain_namePresent = mAdministrationDomainNamePresent ;
	oBSA.m.network_addressPresent = mNetworkAddressPresent  ;
	oBSA.m.terminal_identifierPresent = mTerminalIdentifierPresent  ;
	oBSA.m.private_domain_namePresent = mPrivateDomainNamePresent ;
	oBSA.m.organization_namePresent = mOrganizationNamePresent  ;
	oBSA.m.numeric_user_identifierPresent = mNumericUserIdentifierPresent;
	oBSA.m.personal_namePresent = mPersonalNamePresent  ;
	oBSA.m.organizational_unit_namesPresent = mOrganizationalUnitNamesPresent  ;

	if (mCountryNamePresent) 
	{
		mCountryName.copyToASNObject(oBSA.country_name);
	}
	if (mAdministrationDomainNamePresent)
	{
		mAdministrationDomainName.copyToASNObject(oBSA.administration_domain_name);
	}

	if (mNetworkAddressPresent)
	{
		mNetworkAddress.copyToASNObject(oBSA.network_address);
	}
	if (mTerminalIdentifierPresent)
	{
		mTerminalIdentifier.copyToASNObject(oBSA.terminal_identifier);
	}

	if (mPrivateDomainNamePresent)
	{
		mPrivateDomainName.copyToASNObject((ASN1T_EXP_AdministrationDomainName&)oBSA.private_domain_name);
	}
	if (mOrganizationNamePresent)
	{
		mOrganizationName.copyToASNObject(oBSA.organization_name);
	}
	if (mNumericUserIdentifierPresent)
	{
		mNumericUserIdentifier.copyToASNObject(oBSA.numeric_user_identifier);
	}
	if (mPersonalNamePresent)
	{
		mPersonalName.copyToASNObject(oBSA.personal_name);
	}
	if (mOrganizationalUnitNamesPresent)
	{	
		OrganizationalUnitName().copyOUNs(mOrganizationalUnitNames,oBSA.organizational_unit_names);
	}

	return SUCCESS;
}
	
void BuiltInStandardAttributes::freeASNObject(ASN1T_EXP_BuiltInStandardAttributes& oBSA)const
{
	if (oBSA.m.country_namePresent) 
	{
		mCountryName.freeASNObject(oBSA.country_name);
	}
	if (oBSA.m.administration_domain_namePresent)
	{
		mAdministrationDomainName.freeASNObject(oBSA.administration_domain_name);
	}

	if (oBSA.m.network_addressPresent)
	{
		mNetworkAddress.freeASNObject(oBSA.network_address);
	}
	if (oBSA.m.terminal_identifierPresent)
	{
		mTerminalIdentifier.freeASNObject(oBSA.terminal_identifier);
	}

	if (oBSA.m.private_domain_namePresent)
	{
		mPrivateDomainName.freeASNObject((ASN1T_EXP_AdministrationDomainName&)oBSA.private_domain_name);
	}
	if (oBSA.m.organization_namePresent)
	{
		mOrganizationName.freeASNObject(oBSA.organization_name);
	}
	if (oBSA.m.numeric_user_identifierPresent)
	{
		mNumericUserIdentifier.freeASNObject(oBSA.numeric_user_identifier);
	}
	if (oBSA.m.personal_namePresent)
	{
		mPersonalName.freeASNObject(oBSA.personal_name);
	}
	if (oBSA.m.organizational_unit_namesPresent)
	{	
		OrganizationalUnitName().freeASNObjects(oBSA.organizational_unit_names);
	}
}

bool BuiltInStandardAttributes::isCountryNamePresent()const
{
	return mCountryNamePresent;
}
bool BuiltInStandardAttributes::isAdministrationDomainNamePresent()const
{
	return mAdministrationDomainNamePresent;

}
bool BuiltInStandardAttributes::isNetworkAddressPresent()const
{
	return mNetworkAddressPresent;
}
bool BuiltInStandardAttributes::isTerminalIdentifierPresent()const
{
	return mTerminalIdentifierPresent;
}
bool BuiltInStandardAttributes::isPrivateDomainNamePresent()const
{
	return mPrivateDomainNamePresent;
}
bool BuiltInStandardAttributes::isOrganizationNamePresent()const
{
	return mOrganizationNamePresent;
}
bool BuiltInStandardAttributes::isNumericUserIdentifierPresent()const
{
	return mNumericUserIdentifierPresent;
}
bool BuiltInStandardAttributes::isPersonalNamePresent()const
{
	return mPersonalNamePresent;
}
bool BuiltInStandardAttributes::isOrganizationalUnitNamesPresent()const
{
	return mOrganizationalUnitNamesPresent;
}

const CountryName& 					BuiltInStandardAttributes::getCountryName()const
{
	return mCountryName;
}
const AdministrationDomainName&		BuiltInStandardAttributes::getAdministrationDomainName()const
{
	return mAdministrationDomainName;
}
const NetworkAddress&				BuiltInStandardAttributes::getNetworkAddress()const
{
	return mNetworkAddress;
}
const TerminalIdentifier&			BuiltInStandardAttributes::getTerminalIdentifier()const
{
	return mTerminalIdentifier;
}
const PrivateDomainName&			BuiltInStandardAttributes::getPrivateDomainName()const
{
	return mPrivateDomainName;
}
const OrganizationName&				BuiltInStandardAttributes::getOrganizationName()const
{
	return mOrganizationName;
}
const NumericUserIdentifier&		BuiltInStandardAttributes::getNumericUserIdentifier()const
{
	return mNumericUserIdentifier;
}
const PersonelName&					BuiltInStandardAttributes::getPersonalName()const
{
	return mPersonalName;
}
const QList<OrganizationalUnitName>&BuiltInStandardAttributes::getOrganizationalUnitNames()const
{
	return mOrganizationalUnitNames;
}

void BuiltInStandardAttributes::setCountryName(const CountryName& iCN)
{
	mCountryName = iCN;
	mCountryNamePresent = true;
}
void BuiltInStandardAttributes::setAdministrationDomainName(const AdministrationDomainName& iADN)
{
	mAdministrationDomainName = iADN;
	mAdministrationDomainNamePresent = true;
}
void BuiltInStandardAttributes::setNetworkAddress(const NetworkAddress& iNA)
{
	mNetworkAddress = iNA;
	mNetworkAddressPresent = true;
}
void BuiltInStandardAttributes::setTerminalIdentifier(const TerminalIdentifier& iTI)
{
	mTerminalIdentifier = iTI;
	mTerminalIdentifierPresent = true;
}
void BuiltInStandardAttributes::setPrivateDomainName(const PrivateDomainName& iPDN)
{
	mPrivateDomainName = iPDN;
	mPrivateDomainNamePresent = true;
}
void BuiltInStandardAttributes::setOrganizationName(const OrganizationName& iON)
{
	mOrganizationName = iON;
	mOrganizationNamePresent = true;
}
void BuiltInStandardAttributes::setNumericUserIdentifier(const NumericUserIdentifier& iNUI)
{
	mNumericUserIdentifier = iNUI;
	mNumericUserIdentifierPresent = true;
}

void BuiltInStandardAttributes::setPersonalName(const PersonelName& iPN)
{
	mPersonalName = iPN;
	mPersonalNamePresent = true;
}

void BuiltInStandardAttributes::setOrganizationalUnitNames(const QList<OrganizationalUnitName>& iOUNs)
{
	mOrganizationalUnitNames = iOUNs;
	mOrganizationalUnitNamesPresent = true;
}


BuiltInStandardAttributes::~BuiltInStandardAttributes()
{
}
NAMESPACE_END

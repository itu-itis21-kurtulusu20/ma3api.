#include "PolicyInformation.h"

using namespace esya;

PolicyInformation::PolicyInformation(void)
{
}

PolicyInformation::PolicyInformation(const ASN1T_IMP_PolicyInformation & iPI)
{
	copyFromASNObject(iPI);
}

PolicyInformation::PolicyInformation(const QByteArray & iPI )
{
	constructObject(iPI);
}

PolicyInformation::PolicyInformation(const ASN1OBJID & iPolicyIdentifier, const QList<PolicyQualifierInfo> &iPolicyQualifiers)
:	mPolicyIdentifier(iPolicyIdentifier),
	mPolicyQualifiers(iPolicyQualifiers)
{
}

PolicyInformation::PolicyInformation(const PolicyInformation &iPI)
:	mPolicyIdentifier(iPI.getPolicyIdentifier()),
	mPolicyQualifiers(iPI.getPolicyQualifiers())
{
}

PolicyInformation& PolicyInformation::operator=(const PolicyInformation& iPI)
{
	mPolicyIdentifier = iPI.getPolicyIdentifier();
	mPolicyQualifiers = iPI.getPolicyQualifiers();
	return *this;
}

bool esya::operator==(const PolicyInformation & iRHS, const PolicyInformation & iLHS)
{
	return (	( iRHS.getPolicyIdentifier()	== iLHS.getPolicyIdentifier()	) && 
				( iRHS.getPolicyQualifiers()	== iLHS.getPolicyQualifiers()	)	);
}

bool esya::operator!=(const PolicyInformation & iRHS, const PolicyInformation & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PolicyInformation::copyFromASNObject(const ASN1T_IMP_PolicyInformation& iPI)
{
	mPolicyIdentifier = iPI.policyIdentifier;
	if (iPI.m.policyQualifiersPresent)
		PolicyQualifierInfo().copyQualifiers(iPI.policyQualifiers,mPolicyQualifiers);
	return SUCCESS;
}

int PolicyInformation::copyToASNObject(ASN1T_IMP_PolicyInformation & oPI) const
{
	oPI.policyIdentifier = mPolicyIdentifier;
	oPI.m.policyQualifiersPresent = ( mPolicyQualifiers.size() > 0 ) ;
	if (oPI.m.policyQualifiersPresent)
		PolicyQualifierInfo().copyQualifiers(mPolicyQualifiers, oPI.policyQualifiers);
	return SUCCESS;
}

void PolicyInformation::freeASNObject(ASN1T_IMP_PolicyInformation & oPI)const
{
	if (oPI.m.policyQualifiersPresent)
		PolicyQualifierInfo().freeASNObjects(oPI.policyQualifiers);
}

int PolicyInformation::copyPIs(const ASN1T_IMP_CertificatePolicies & iPIs, QList<PolicyInformation>& oList)
{
	return copyASNObjects<PolicyInformation>(iPIs,oList);
}

int PolicyInformation::copyPIs(const QList<PolicyInformation> iList ,ASN1T_IMP_CertificatePolicies & oPIs)
{
	return copyASNObjects<PolicyInformation>(iList,oPIs);
}

int PolicyInformation::copyPIs(const QByteArray & iASNBytes, QList<PolicyInformation>& oList)
{
	return copyASNObjects<ASN1T_IMP_CertificatePolicies,ASN1C_IMP_CertificatePolicies,PolicyInformation>(iASNBytes,oList);
}

int PolicyInformation::copyPIs(const QList<PolicyInformation>& iList , QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_IMP_CertificatePolicies,ASN1C_IMP_CertificatePolicies,PolicyInformation>(iList,oASNBytes);
}

const QList<PolicyQualifierInfo>&	PolicyInformation::getPolicyQualifiers() const
{
	return mPolicyQualifiers;
}

const ASN1TObjId& PolicyInformation::getPolicyIdentifier() const
{
	return mPolicyIdentifier;
}

PolicyInformation::~PolicyInformation(void)
{
}

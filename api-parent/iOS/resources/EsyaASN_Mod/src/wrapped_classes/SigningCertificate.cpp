#include "SigningCertificate.h"

using namespace esya;

namespace esya
{

	SigningCertificate::SigningCertificate(void)
	: mPoliciesPresent(false)
	{
	}

	SigningCertificate::SigningCertificate( const ASN1T_PKCS7_SigningCertificate & iSC)
	: mPoliciesPresent(false)
	{
		copyFromASNObject(iSC);
	}

	SigningCertificate::SigningCertificate( const QByteArray &iSC)
	: mPoliciesPresent(false)
	{
		constructObject(iSC);
	}

	SigningCertificate::SigningCertificate( const SigningCertificate &iSC)
	:	mPoliciesPresent(iSC.isPoliciesPresent()),
		mPolicies(iSC.getPolicies()),
		mCerts(iSC.getCerts())
	{
	}

	SigningCertificate::~SigningCertificate()
	{	
	}


	SigningCertificate & SigningCertificate::operator=(const SigningCertificate& iSC)
	{
		mPoliciesPresent	= iSC.isPoliciesPresent();
		mPolicies			= iSC.getPolicies();
		mCerts				= iSC.getCerts();
		return *this;
	}

	bool operator==(const SigningCertificate& iRHS,const SigningCertificate& iLHS)
	{
		if ( ( iRHS.getCerts()				!= iLHS.getCerts() ) ||
			 ( iRHS.isPoliciesPresent()	!= iLHS.isPoliciesPresent() ))
			 return false;

		if ( ( iRHS.isPoliciesPresent()) &&
			 ( iRHS.getPolicies() != iLHS.getPolicies()) )
			return false;

		return true;
	}

	bool operator!=(const SigningCertificate& iRHS, const SigningCertificate& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int SigningCertificate::copyFromASNObject(const ASN1T_PKCS7_SigningCertificate & iSC)
	{
		mPoliciesPresent = iSC.m.policiesPresent;
		if (mPoliciesPresent)
			PolicyInformation().copyASNObjects<PolicyInformation>(iSC.policies,mPolicies);
		
		ESSCertID().copyASNObjects<ESSCertID>(iSC.certs,mCerts);
		return SUCCESS;
	}

	int SigningCertificate::copyToASNObject(ASN1T_PKCS7_SigningCertificate & oSC) const
	{
		oSC.m.policiesPresent = mPoliciesPresent;
		if (mPoliciesPresent)
			PolicyInformation().copyASNObjects<PolicyInformation>(mPolicies,oSC.policies);
		
		ESSCertID().copyASNObjects<ESSCertID>(mCerts,oSC.certs);
		return SUCCESS;
	}

	void SigningCertificate::freeASNObject(ASN1T_PKCS7_SigningCertificate& oSC) const
	{
		if (oSC.m.policiesPresent)
			PolicyInformation().freeASNObjects(oSC.policies);
		
		ESSCertID().freeASNObjects(oSC.certs);
	}


	bool SigningCertificate::isPoliciesPresent()const
	{
		return mPoliciesPresent;
	}
	
	const QList<PolicyInformation> & SigningCertificate::getPolicies() const 
	{
		return mPolicies;
	}
	
	const QList<ESSCertID> & SigningCertificate::getCerts() const
	{
		return mCerts;
	}

	void SigningCertificate::setPolicies(const QList<PolicyInformation> & iPolicies)
	{
		mPolicies = iPolicies;
		mPoliciesPresent = !iPolicies.isEmpty();
	}
	
	void SigningCertificate::setCerts(const QList<ESSCertID> & iCerts)
	{
		mCerts = iCerts;
	}

	void SigningCertificate::appendPolicy(const PolicyInformation & iPolicy)
	{
		mPoliciesPresent = false;	
		mPolicies.append(iPolicy);
	}
	
	void SigningCertificate::appendCert(const ESSCertID& iCert) 
	{
		mCerts.append(iCert);
	}


}
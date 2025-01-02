#include "SigningCertificateV2.h"

using namespace esya;

namespace esya
{

    SigningCertificateV2::SigningCertificateV2(void)
	: mPoliciesPresent(false)
	{
	}

    SigningCertificateV2::SigningCertificateV2( const ASN1T_PKCS7_SigningCertificateV2 & iSCv2)
	: mPoliciesPresent(false)
	{
        copyFromASNObject(iSCv2);
	}

    SigningCertificateV2::SigningCertificateV2( const QByteArray &iSCv2)
	: mPoliciesPresent(false)
	{
        constructObject(iSCv2);
	}

    SigningCertificateV2::SigningCertificateV2( const SigningCertificateV2 &iSCv2)
    :	mPoliciesPresent(iSCv2.isPoliciesPresent()),
        mPolicies(iSCv2.getPolicies()),
        mCerts(iSCv2.getCerts())
	{
	}

    SigningCertificateV2::~SigningCertificateV2()
	{	
	}


    SigningCertificateV2 & SigningCertificateV2::operator=(const SigningCertificateV2& iSCv2)
	{
        mPoliciesPresent	= iSCv2.isPoliciesPresent();
        mPolicies			= iSCv2.getPolicies();
        mCerts				= iSCv2.getCerts();
		return *this;
	}

    // TODO : ESSCertIDv2 == operator may not be corrected
    bool operator==(const SigningCertificateV2& iRHS,const SigningCertificateV2& iLHS)
	{
        if ( ( iRHS.getCerts()			!= iLHS.getCerts() ) ||
			 ( iRHS.isPoliciesPresent()	!= iLHS.isPoliciesPresent() ))
			 return false;

		if ( ( iRHS.isPoliciesPresent()) &&
			 ( iRHS.getPolicies() != iLHS.getPolicies()) )
			return false;

		return true;
	}

    bool operator!=(const SigningCertificateV2& iRHS, const SigningCertificateV2& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

    int SigningCertificateV2::copyFromASNObject(const ASN1T_PKCS7_SigningCertificateV2 & iSCv2)
	{
        mPoliciesPresent = iSCv2.m.policiesPresent;
		if (mPoliciesPresent)
            PolicyInformation().copyASNObjects<PolicyInformation>(iSCv2.policies,mPolicies);
		
        ESSCertIDv2().copyASNObjects<ESSCertIDv2>(iSCv2.certs,mCerts);
		return SUCCESS;
	}

    int SigningCertificateV2::copyToASNObject(ASN1T_PKCS7_SigningCertificateV2 & oSCv2) const
	{
        oSCv2.m.policiesPresent = mPoliciesPresent;
		if (mPoliciesPresent)
            PolicyInformation().copyASNObjects<PolicyInformation>(mPolicies,oSCv2.policies);
		
        ESSCertIDv2().copyASNObjects<ESSCertIDv2>(mCerts,oSCv2.certs);
		return SUCCESS;
	}

    void SigningCertificateV2::freeASNObject(ASN1T_PKCS7_SigningCertificateV2& oSCv2) const
	{
        if (oSCv2.m.policiesPresent)
            PolicyInformation().freeASNObjects(oSCv2.policies);
		
        ESSCertIDv2().freeASNObjects(oSCv2.certs);
	}


    bool SigningCertificateV2::isPoliciesPresent()const
	{
		return mPoliciesPresent;
	}
	
    const QList<PolicyInformation> & SigningCertificateV2::getPolicies() const
	{
		return mPolicies;
	}
	
    const QList<ESSCertIDv2> & SigningCertificateV2::getCerts() const
	{
		return mCerts;
	}

    void SigningCertificateV2::setPolicies(const QList<PolicyInformation> & iPolicies)
	{
		mPolicies = iPolicies;
		mPoliciesPresent = !iPolicies.isEmpty();
	}
	
    void SigningCertificateV2::setCerts(const QList<ESSCertIDv2> & iCerts)
	{
		mCerts = iCerts;
	}

    void SigningCertificateV2::appendPolicy(const PolicyInformation & iPolicy)
	{
		mPoliciesPresent = false;	
		mPolicies.append(iPolicy);
	}
	
    void SigningCertificateV2::appendCert(const ESSCertIDv2& iCert)
	{
		mCerts.append(iCert);
	}


}

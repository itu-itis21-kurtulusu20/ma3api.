#include "IetfAttrSyntax.h"


using namespace esya;

IetfAttrSyntax::IetfAttrSyntax(void)
:	mPolicyAuthorityPresent(false)
{
}

IetfAttrSyntax::IetfAttrSyntax(const QByteArray & iIetfAttrSyntax)
{
	constructObject(iIetfAttrSyntax);
}

IetfAttrSyntax::IetfAttrSyntax(const ASN1T_ATTRCERT_IetfAttrSyntax & iIetfAttrSyntax )
{
	copyFromASNObject(iIetfAttrSyntax);
}

IetfAttrSyntax::IetfAttrSyntax(const IetfAttrSyntax& iIetfAttrSyntax)
:	mValues(iIetfAttrSyntax.getValues()),
	mPolicyAuthorityPresent(iIetfAttrSyntax.isPolicyAuthorityPresent()),
	mPolicyAuthority(iIetfAttrSyntax.getPolicyAuthority())
{
}

IetfAttrSyntax& IetfAttrSyntax::operator=(const IetfAttrSyntax& iIetfAttrSyntax)
{
	mValues = iIetfAttrSyntax.getValues();
	mPolicyAuthorityPresent = iIetfAttrSyntax.isPolicyAuthorityPresent();
	mPolicyAuthority = iIetfAttrSyntax.getPolicyAuthority();

	return *this;
}

bool esya::operator==( const IetfAttrSyntax& iRHS, const IetfAttrSyntax& iLHS)
{
	if ( iRHS.getValues() != iLHS.getValues() )
		return false;

	if ( iRHS.isPolicyAuthorityPresent() != iLHS.isPolicyAuthorityPresent() )
		return false;

	if (	iRHS.isPolicyAuthorityPresent() &&
		 (  iRHS.getPolicyAuthority() != iLHS.getPolicyAuthority() ) )
		return false;
	
	return true;
}

bool esya::operator!=( const IetfAttrSyntax& iRHS, const IetfAttrSyntax& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int IetfAttrSyntax::copyFromASNObject(const ASN1T_ATTRCERT_IetfAttrSyntax & iIetfAttrSyntax)
{
	IetfAttrSyntax_values_element().copyIetfAttrSyntax_values_elements(iIetfAttrSyntax.values,mValues);
	mPolicyAuthorityPresent = ( iIetfAttrSyntax.m.policyAuthorityPresent == 1 ); 
	if (mPolicyAuthorityPresent)
	{
		mPolicyAuthority.copyFromASNObject(iIetfAttrSyntax.policyAuthority);
	}
	return SUCCESS;
}

int IetfAttrSyntax::copyToASNObject(ASN1T_ATTRCERT_IetfAttrSyntax &oIetfAttrSyntax)const
{
	IetfAttrSyntax_values_element().copyIetfAttrSyntax_values_elements(mValues,oIetfAttrSyntax.values);

	oIetfAttrSyntax.m.policyAuthorityPresent = mPolicyAuthorityPresent ? 1:0 ;
	if (mPolicyAuthorityPresent)
		mPolicyAuthority.copyToASNObject(oIetfAttrSyntax.policyAuthority);

	return SUCCESS;
}


void IetfAttrSyntax::freeASNObject(ASN1T_ATTRCERT_IetfAttrSyntax& oIetfAttrSyntax)const
{
	IetfAttrSyntax_values_element().freeASNObjects(oIetfAttrSyntax.values);

	if (oIetfAttrSyntax.m.policyAuthorityPresent == 1)
		GeneralNames().freeASNObject(oIetfAttrSyntax.policyAuthority);
}

bool IetfAttrSyntax::isPolicyAuthorityPresent()const
{
	return mPolicyAuthorityPresent;
}

const GeneralNames& IetfAttrSyntax::getPolicyAuthority()const
{
	return mPolicyAuthority;
}

const QList<IetfAttrSyntax_values_element>& IetfAttrSyntax::getValues()const
{
	return mValues;
}

void IetfAttrSyntax::setPolicyAuthorityPresent(bool iPAP)
{
	mPolicyAuthorityPresent = iPAP;
}

void IetfAttrSyntax::setPolicyAuthority(const GeneralNames& iPA)
{
	mPolicyAuthorityPresent = true;
	mPolicyAuthority = iPA;
}

void IetfAttrSyntax::setValues(const QList<IetfAttrSyntax_values_element>& iValues)
{
	mValues = iValues;
}

void IetfAttrSyntax::appendValue(const IetfAttrSyntax_values_element& iValue)
{
	mValues.append(iValue);
}

IetfAttrSyntax::~IetfAttrSyntax(void)
{
}
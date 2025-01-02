#include "ECParameters.h"

using namespace esya;

ECParameters::ECParameters(void)
: mType(PT_ImplicitlyCA)
{
}

ECParameters::ECParameters(const QByteArray & iECParameters)
{
	constructObject(iECParameters);
}

ECParameters::ECParameters(const ASN1T_ALGOS_ECParameters & iECParameters)
{
	copyFromASNObject(iECParameters);
}

ECParameters::ECParameters(const SpecifiedECDomain& iECParameters)
:	mType(PT_SpecifiedCurve),
	mSpecifiedCurve(iECParameters)
{
}

ECParameters::ECParameters(const ASN1TObjId& iNamedCurve)
:	mType(PT_NamedCurve),
	mNamedCurve(iNamedCurve)
{
}

ECParameters::ECParameters(const ECParameters& iECParameters)
:	mType(iECParameters.getType()),
	mSpecifiedCurve(iECParameters.getSpecifiedCurve()),
	mNamedCurve(iECParameters.getNamedCurve())
{
}

ECParameters& ECParameters::operator=(const ECParameters& iECParameters)
{
	mType			= iECParameters.getType();
	mSpecifiedCurve	= iECParameters.getSpecifiedCurve();
	mNamedCurve		= iECParameters.getNamedCurve();
	return *this;
}

bool esya::operator==(const ECParameters & iRHS, const ECParameters& iLHS)
{
	if (iRHS.getType() != iLHS.getType())
		return false;
	
	switch (iRHS.getType())
	{
		case ECParameters::PT_NamedCurve		: return (iRHS.getNamedCurve() == iLHS.getNamedCurve());
		case ECParameters::PT_SpecifiedCurve	: return (iRHS.getSpecifiedCurve() == iLHS.getSpecifiedCurve());
		default : return true;
	}

}

bool esya::operator!=(const ECParameters & iRHS, const ECParameters& iLHS)
{
	return (!(iRHS==iLHS));
}

int ECParameters::copyFromASNObject(const ASN1T_ALGOS_ECParameters & iECParameters) 
{
	mType = (PKParamType)iECParameters.t;
	switch (mType)
	{
		case PT_NamedCurve: 
			{
				mNamedCurve = *iECParameters.u.namedCurve;
				break;
			};
		case PT_SpecifiedCurve: 
			{
				mSpecifiedCurve.copyFromASNObject(*iECParameters.u.specifiedCurve);
				break;
			};
		default : break;
	}
	return SUCCESS;
}

const ECParameters::PKParamType&  ECParameters::getType()const
{
	return mType;
}

const SpecifiedECDomain&	ECParameters::getSpecifiedCurve()const 
{
	return mSpecifiedCurve;
}

const ASN1TObjId&	ECParameters::getNamedCurve()const
{
	return mNamedCurve;
}

void ECParameters::setSpecifiedCurve(const SpecifiedECDomain& iSpecifiedCurve)
{
	mType = PT_SpecifiedCurve;
	mSpecifiedCurve = iSpecifiedCurve;
}

void ECParameters::setNamedCurve(const ASN1TObjId& iNamedCurve)
{
	mType = PT_NamedCurve;
	mNamedCurve = iNamedCurve;
}

void ECParameters::setType(const PKParamType& iType)
{
	mType = iType;
}

int ECParameters::copyToASNObject(ASN1T_ALGOS_ECParameters & oECParameters) const
{
	oECParameters.t = mType;
	switch (mType)
	{
		case PT_NamedCurve: 
			{
				oECParameters.u.namedCurve = new ASN1TObjId(mNamedCurve);
				break;
			};
		case PT_SpecifiedCurve: 
			{
				oECParameters.u.specifiedCurve = mSpecifiedCurve.getASNCopy();
				break;
			};
		default : break;
	}
	return SUCCESS;
}

void ECParameters::freeASNObject(ASN1T_ALGOS_ECParameters & oECParameters)const
{
	switch (oECParameters.t)
	{
		case PT_NamedCurve: 
			{
				DELETE_MEMORY(oECParameters.u.namedCurve);
				break;
			};
		case PT_SpecifiedCurve: 
			{
				SpecifiedECDomain().freeASNObjectPtr(oECParameters.u.specifiedCurve);
				break;
			};
		default : break;
	}
}

ECParameters::~ECParameters(void)
{
}
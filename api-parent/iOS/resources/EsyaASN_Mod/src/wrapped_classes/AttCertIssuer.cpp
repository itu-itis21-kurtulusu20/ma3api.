#include "AttCertIssuer.h"

using namespace esya;

namespace esya
{

	AttCertIssuer::AttCertIssuer(void)
	{
	}

	AttCertIssuer::AttCertIssuer(const QByteArray & iAttCertIssuer)
	{
		constructObject(iAttCertIssuer);
	}

	AttCertIssuer::AttCertIssuer(const ASN1T_ATTRCERT_AttCertIssuer & iAttCertIssuer )
	{
		copyFromASNObject(iAttCertIssuer);
	}

	AttCertIssuer::AttCertIssuer(const AttCertIssuer& iAttCertIssuer)
	{
		mType = iAttCertIssuer.getType();
		switch(mType)
		{
		case CIT_V1Form:
			{
				mV1Form = iAttCertIssuer.getV1Form(); 
				break;
			}
		case CIT_V2Form:
			{
				mV2Form = iAttCertIssuer.getV2Form(); 
				break;
			}
		}
	}

	AttCertIssuer& AttCertIssuer::operator=(const AttCertIssuer& iAttCertIssuer)
	{
		mType = iAttCertIssuer.getType();
		switch(mType)
		{
		case CIT_V1Form:
			{
				mV1Form = iAttCertIssuer.getV1Form(); 
				break;
			}
		case CIT_V2Form:
			{
				mV2Form = iAttCertIssuer.getV2Form(); 
				break;
			}
		}

		return *this;
	}

	bool operator==( const AttCertIssuer& iRHS, const AttCertIssuer& iLHS)
	{
		if ( iRHS.getType() != iLHS.getType())
			return false;
		
		switch(iRHS.getType())
		{
		case AttCertIssuer::CIT_V1Form:
			{
				return ( iRHS.getV1Form() != iLHS.getV1Form()); 
			}
		case AttCertIssuer::CIT_V2Form:
			{
				return ( iRHS.getV2Form() != iLHS.getV2Form());
			}
		}
		return false;
	}

	bool operator!=( const AttCertIssuer& iRHS, const AttCertIssuer& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AttCertIssuer::copyFromASNObject(const ASN1T_ATTRCERT_AttCertIssuer & iAttCertIssuer)
	{
		mType = (CertIssuerType)iAttCertIssuer.t;
		
		switch(mType)
		{
		case CIT_V1Form:
			{
				mV1Form.copyFromASNObject(*iAttCertIssuer.u.v1Form);
			}
		case CIT_V2Form:
			{
				mV2Form.copyFromASNObject(*iAttCertIssuer.u.v2Form);
			}
		}
		return SUCCESS;
	}

	int AttCertIssuer::copyToASNObject(ASN1T_ATTRCERT_AttCertIssuer &oAttCertIssuer)const
	{
		oAttCertIssuer.t = mType;

		switch(mType)
		{
		case CIT_V1Form:
			{
				oAttCertIssuer.u.v1Form = mV1Form.getASNCopy();
			}
		case CIT_V2Form:
			{
				oAttCertIssuer.u.v2Form = mV2Form.getASNCopy();
			}
		}
		return SUCCESS;
	}

	void AttCertIssuer::freeASNObject(ASN1T_ATTRCERT_AttCertIssuer& oAttCertIssuer) const
	{
		switch(oAttCertIssuer.t)
		{
		case CIT_V1Form:
			{
				GeneralNames().freeASNObjectPtr(oAttCertIssuer.u.v1Form);
			}
		case CIT_V2Form:
			{
				V2Form().freeASNObjectPtr(oAttCertIssuer.u.v2Form);
			}
		}
	}


	const AttCertIssuer::CertIssuerType &	AttCertIssuer::getType()const
	{
		return mType;
	}

	const GeneralNames& AttCertIssuer::getV1Form()const 
	{
		return mV1Form;
	}

	const V2Form& AttCertIssuer::getV2Form()const 
	{
		return mV2Form;
	}

	void AttCertIssuer::setType(const CertIssuerType &	iType)
	{
		mType = iType;
	}

	void AttCertIssuer::setV1Form(const GeneralNames& iV1)
	{
		mType = CIT_V1Form;
		mV1Form = iV1;
	}

	void AttCertIssuer::setV2Form(const V2Form& iV2)
	{
		mType = CIT_V2Form;
		mV2Form = iV2;
	}

	AttCertIssuer::~AttCertIssuer(void)
	{
	}

}
#include "CountryName.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

CountryName::CountryName(void)
{
}

CountryName::CountryName( const ASN1T_EXP_CountryName &iCN)
{
	copyFromASNObject(iCN);
}


CountryName::CountryName( const CountryName::CountryNameType &iType, const QString& iCode)
: mCode(iCode),mType(iType)
{
}

CountryName::CountryName( const CountryName &iCN)
: mType(iCN.getType()), mCode(iCN.getCode())
{
}

CountryName::CountryName( const QByteArray &iCN)
{
	constructObject(iCN);
}

CountryName & CountryName::operator=(const CountryName& iCN)
{
	mType = iCN.getType();
	mCode= iCN.getCode();
	return(*this);
}

bool operator==(const CountryName& iRHS,const CountryName& iLHS)
{
	return ( (iRHS.getType() == iLHS.getType()) &&(iRHS.getCode() == iLHS.getCode()) );
}

bool operator!=(const CountryName& iRHS, const CountryName& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int CountryName::copyFromASNObject(const ASN1T_EXP_CountryName & iCN)
{
	mType = (CountryNameType)(iCN.t);
	switch (mType)
	{
	case CNT_X21DCC:
		{
			mCode = QString(iCN.u.x121_dcc_code);
			break;
		}
	case CNT_ISO3166ALPHA2:
		{
			mCode = QString(iCN.u.iso_3166_alpha2_code);
			break;
		}
	}
	return SUCCESS;
}

int CountryName::copyToASNObject(ASN1T_EXP_CountryName & oCN) const
{
	oCN.t  = mType;

	switch (mType)
	{
	case CNT_X21DCC:
		{
			oCN.u.x121_dcc_code = myStrDup(mCode);
			break;
		}
	case CNT_ISO3166ALPHA2:
		{
			oCN.u.iso_3166_alpha2_code = myStrDup(mCode);
			break;
		}
	}

	return SUCCESS;
}
	
void CountryName::freeASNObject(ASN1T_EXP_CountryName& oCN)const
{
	switch (mType)
	{
	case CNT_X21DCC:
		{
			DELETE_MEMORY_ARRAY(oCN.u.x121_dcc_code);
			break;
		}
	case CNT_ISO3166ALPHA2:
		{
			DELETE_MEMORY_ARRAY(oCN.u.iso_3166_alpha2_code);
			break;
		}
	}
	

}

const CountryName::CountryNameType& CountryName::getType() const 
{
	return mType;
}

void CountryName::setType(const CountryName::CountryNameType & iType)  
{
	mType = iType;
}

const QString& CountryName::getCode() const 
{
	return mCode;
}
	
void CountryName::setCode(const QString & iCode)  
{
	mCode = iCode;
}


CountryName::~CountryName()
{
}
NAMESPACE_END

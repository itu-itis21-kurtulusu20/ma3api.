#include "FieldID.h"

using namespace esya;

FieldID::FieldID(void)
{
	mFieldType.numids = 0 ;
}

FieldID::FieldID(const QByteArray & iFieldID)
{
	constructObject(iFieldID);
}

FieldID::FieldID(const QByteArray & iParameters, const ASN1TObjId &iFieldType)
: mParameters(iParameters), mFieldType(iFieldType)
{
}

FieldID::FieldID(const ASN1T_ALGOS_FieldID & iFieldID)
{
	copyFromASNObject(iFieldID);
}

FieldID::FieldID(const ASN1TObjId & iFieldType)
: mFieldType(iFieldType)
{
}

FieldID::FieldID(const FieldID& iFieldID)
:mParameters(iFieldID.getParameters()),mFieldType(iFieldID.getFieldType())
{
}

FieldID& FieldID::operator=(const FieldID& iFieldID)
{
	mParameters	= iFieldID.getParameters();
	mFieldType	= iFieldID.getFieldType();
	return *this;
}


bool esya::operator==(const FieldID & iRHS, const FieldID& iLHS)
{
	return ( iRHS.getFieldType() == iLHS.getFieldType() && iRHS.getParameters()==iRHS.getParameters() );
}

bool esya::operator!=(const FieldID & iRHS, const FieldID& iLHS)
{
	return ( ! ( iRHS == iLHS ));
}


int FieldID::copyFromASNObject(const ASN1T_ALGOS_FieldID & iFieldID) 
{
	mFieldType = iFieldID.fieldType;


	if (iFieldID.parameters.numocts>0)
		mParameters = QByteArray((char*)iFieldID.parameters.data,iFieldID.parameters.numocts);

	return SUCCESS;
}


const QByteArray &FieldID::getParameters()const 
{
	return mParameters;
}

const ASN1TObjId &FieldID::getFieldType() const
{
	return mFieldType;
}


QByteArray FieldID::getParamsAsOctets()const
{
	return ASNUtils::decodeOctetString(mParameters);
}

void FieldID::setParamsAsOctets(const QByteArray& iOctets)
{

	mParameters = ASNUtils::encodeOctetString(iOctets);
}

void FieldID::setParameters(const QByteArray& iParams)
{
	mParameters = iParams;	
}

int FieldID::copyToASNObject(ASN1T_ALGOS_FieldID & oFieldID) const 
{
	oFieldID.fieldType = mFieldType;
	
	if (mParameters.size()>0)
	{
		oFieldID.parameters.data = (OSOCTET*)myStrDup(mParameters.data(),mParameters.size());
		oFieldID.parameters.numocts = mParameters.size(); 
	}
	return SUCCESS;
}

void FieldID::freeASNObject(ASN1T_ALGOS_FieldID & oFieldID)const
{
	if (oFieldID.parameters.numocts >0&& oFieldID.parameters.data)
	{
		DELETE_MEMORY_ARRAY(oFieldID.parameters.data);
		oFieldID.parameters.numocts = 0;
	}
}

bool FieldID::isNull() const
{
	return (mFieldType.numids == 0 );
}

FieldID::~FieldID(void)
{
}

#include "SMIMECapability.h"
#include "ESeqOfList.h"


namespace esya
{

SMIMECapability::SMIMECapability(void)
: mParametersPresent(false)
{
}

SMIMECapability::SMIMECapability(const QByteArray & iSMIMECapability)
{
	constructObject(iSMIMECapability);
}

SMIMECapability::SMIMECapability(const QByteArray & iParameters, const ASN1TObjId & iCapabilityID)
:	mParameters(iParameters),
	mParametersPresent(true),
	mCapabilityID(iCapabilityID)
{
}

SMIMECapability::SMIMECapability(ASN1TObjId & iCapabilityID)
:	mParametersPresent(false),
	mCapabilityID(iCapabilityID)
{
}

SMIMECapability::SMIMECapability(const ASN1T_CMS_SMIMECapability & iSMIMECapability)
{
	copyFromASNObject(iSMIMECapability);
}

SMIMECapability::SMIMECapability(const SMIMECapability& iSMIMECapability)
:	mParameters(iSMIMECapability.getParameters()),
	mParametersPresent(iSMIMECapability.isParametersPresent()),
	mCapabilityID(iSMIMECapability.getCapabilityID())
{
}

SMIMECapability& SMIMECapability::operator=(const SMIMECapability& iSMIMECapability)
{
	mParameters			= iSMIMECapability.getParameters();
	mParametersPresent	= iSMIMECapability.isParametersPresent();
	mCapabilityID		= iSMIMECapability.getCapabilityID();

	return *this;
}

bool operator==(const SMIMECapability & iRHS, const SMIMECapability& iLHS)
{
	if (	(	iRHS.getCapabilityID()		!= iLHS.getCapabilityID()		) ||
			(	iRHS.isParametersPresent()	!= iLHS.isParametersPresent()	)		)
		return false;

	if (	  iRHS.isParametersPresent() && 
			( iRHS.getParameters() != iLHS.getParameters() )	)
		return false;

	return true;
}

bool operator!=(const SMIMECapability & iRHS, const SMIMECapability& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int SMIMECapability::copyFromASNObject(const ASN1T_CMS_SMIMECapability & iSMIMECapability) 
{
	mParametersPresent	= iSMIMECapability.m.parametersPresent;
	mCapabilityID		= iSMIMECapability.capabilityID;

	if (iSMIMECapability.m.parametersPresent)
		mParameters = QByteArray((const char*)iSMIMECapability.parameters.data,iSMIMECapability.parameters.numocts);

	return SUCCESS;
}

int SMIMECapability::copyToASNObject(ASN1T_CMS_SMIMECapability & oSMIMECapability) const
{
	oSMIMECapability.capabilityID = mCapabilityID;
	oSMIMECapability.m.parametersPresent = mParametersPresent;

	if (mParametersPresent)
	{
		oSMIMECapability.parameters.data = (ASN1OCTET*) myStrDup(mParameters.data(),mParameters.size());
		oSMIMECapability.parameters.numocts = mParameters.size();
	}

	return SUCCESS;
}

void SMIMECapability::freeASNObject(ASN1T_CMS_SMIMECapability & oSMIMECapability)const
{
	if ( oSMIMECapability.m.parametersPresent && oSMIMECapability.parameters.numocts > 0 )
	{
		DELETE_MEMORY_ARRAY(oSMIMECapability.parameters.data)
	}
}

int SMIMECapability::copyCapabilities(const ASN1TPDUSeqOfList & iCapabilities, QList<SMIMECapability>& oList)
{
	return copyASNObjects<SMIMECapability>(iCapabilities,oList);
}

int SMIMECapability::copyCapabilities(const QList<SMIMECapability> iList , ASN1TPDUSeqOfList& oCapabilities)
{
	return copyASNObjects<SMIMECapability>(iList,oCapabilities);
}

bool SMIMECapability::isParametersPresent()const
{
	return mParametersPresent; 
}

const ASN1TObjId&  SMIMECapability::getCapabilityID() const 
{
	return mCapabilityID;
}

const QByteArray&  SMIMECapability::getParameters()const 
{
	return mParameters;
}


void SMIMECapability::setParametersPresent(bool iPP )
{
	mParametersPresent = iPP;
}

void SMIMECapability::setCapabilityID(const ASN1TObjId& iCapabilityID)
{
	mCapabilityID = iCapabilityID;
}

void SMIMECapability::setParameters(const QByteArray& iParameters)
{
	setParametersPresent(true);
	mParameters = iParameters;
}

SMIMECapability::~SMIMECapability(void)
{
}


}
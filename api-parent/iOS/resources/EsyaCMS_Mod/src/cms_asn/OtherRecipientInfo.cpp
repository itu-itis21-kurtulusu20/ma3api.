#include "OtherRecipientInfo.h"

using namespace esya;

OtherRecipientInfo::OtherRecipientInfo(void)
{
}

OtherRecipientInfo::OtherRecipientInfo(const QByteArray & iORI)
{
	constructObject(iORI);
}

OtherRecipientInfo::OtherRecipientInfo(const ASN1T_CMS_OtherRecipientInfo & iORI)
{
	copyFromASNObject(iORI);
}

OtherRecipientInfo::OtherRecipientInfo(const ASN1TObjId iORIType,const QByteArray & iORIValue)
:	mORIType(iORIType),
	mORIValue(iORIValue)
{
}

OtherRecipientInfo::OtherRecipientInfo(const OtherRecipientInfo& iORI)
:	mORIType(iORI.getORIType()),
	mORIValue(iORI.getORIValue())
{
}

OtherRecipientInfo & OtherRecipientInfo::operator=(const OtherRecipientInfo & iORI)
{
	mORIType = iORI.getORIType();
	mORIValue = iORI.getORIValue();
	return *this;
}

bool esya::operator==(const OtherRecipientInfo & iRHS,const OtherRecipientInfo & iLHS)
{
	return (	( iRHS.getORIType()		== iLHS.getORIType()	) &&
				( iRHS.getORIValue()	== iLHS.getORIValue()	)	);
}

bool esya::operator!=(const OtherRecipientInfo & iRHS,const OtherRecipientInfo & iLHS )
{
	return ( ! ( iRHS == iLHS ) );
}

int OtherRecipientInfo::copyFromASNObject(const ASN1T_CMS_OtherRecipientInfo & iORI )
{
	mORIType = iORI.oriType;
	mORIValue = QByteArray((const char*) iORI.oriValue.data,iORI.oriValue.numocts );
	return SUCCESS;
}

int OtherRecipientInfo::copyToASNObject(ASN1T_CMS_OtherRecipientInfo& oORI)const
{
	oORI.oriType = mORIType;
	oORI.oriValue.data = (OSOCTET*) myStrDup(mORIValue.data(),mORIValue.size());
	oORI.oriValue.numocts = mORIValue.size();
	return SUCCESS;
}

void OtherRecipientInfo::freeASNObject(ASN1T_CMS_OtherRecipientInfo & oORI)const
{
	if ( oORI.oriValue.numocts )
		DELETE_MEMORY_ARRAY(oORI.oriValue.data )
}

const QByteArray	& OtherRecipientInfo::getORIValue()	const
{
	return mORIValue;
}

const ASN1TObjId	& OtherRecipientInfo::getORIType()	const
{
	return mORIType;
}

void OtherRecipientInfo::setORIValue(const QByteArray  &iOriValue)
{
	mORIValue = iOriValue;
}

void OtherRecipientInfo::setORIType(ASN1TObjId iOriType)
{
	mORIType = iOriType;
}

OtherRecipientInfo::~OtherRecipientInfo(void)
{
}

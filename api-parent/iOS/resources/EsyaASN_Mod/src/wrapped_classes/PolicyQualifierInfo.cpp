#include "PolicyQualifierInfo.h"

using namespace esya;
NAMESPACE_BEGIN(esya)


PolicyQualifierInfo::PolicyQualifierInfo(void)
{
}

PolicyQualifierInfo::PolicyQualifierInfo(const ASN1T_IMP_PolicyQualifierInfo & iPQI)
{
	copyFromASNObject(iPQI);
}

PolicyQualifierInfo::PolicyQualifierInfo(const QByteArray & iPQI )
{
	constructObject(iPQI);
}

PolicyQualifierInfo::PolicyQualifierInfo(const ASN1OBJID & iQualifierID, const QByteArray &iQualifier)
:	mQualifier(iQualifier),
	mQualifierID(iQualifierID)
{
}

PolicyQualifierInfo::PolicyQualifierInfo(const PolicyQualifierInfo &iPQI)
:	mQualifier(iPQI.getQualifier()),
	mQualifierID(iPQI.getQualifierID())
{
}


PolicyQualifierInfo& PolicyQualifierInfo::operator=(const PolicyQualifierInfo& iPQI)
{
	mQualifier		= iPQI.getQualifier();
	mQualifierID	= iPQI.getQualifierID();
	return *this;
}

bool operator==(const PolicyQualifierInfo & iRHS, const PolicyQualifierInfo & iLHS)
{
	return (	( iRHS.getQualifierID() == iLHS.getQualifierID()	) && 
				( iRHS.getQualifier()	== iLHS.getQualifier()		)	);
}

bool operator!=(const PolicyQualifierInfo & iRHS, const PolicyQualifierInfo & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PolicyQualifierInfo::copyFromASNObject(const ASN1T_IMP_PolicyQualifierInfo& iPQI)
{
	mQualifierID	= iPQI.policyQualifierId;
	mQualifier		= QByteArray((const char*)iPQI.qualifier.data,iPQI.qualifier.numocts);
	return SUCCESS;
}

int PolicyQualifierInfo::copyToASNObject(ASN1T_IMP_PolicyQualifierInfo & oPQI) const
{
	oPQI.policyQualifierId = mQualifierID;
	oPQI.qualifier.numocts = mQualifier.size();
	oPQI.qualifier.data	   = (OSOCTET*) myStrDup(mQualifier.data(),mQualifier.size());
	return SUCCESS;
}

void PolicyQualifierInfo::freeASNObject(ASN1T_IMP_PolicyQualifierInfo & oPQI)const
{
	if (oPQI.qualifier.numocts>0 && oPQI.qualifier.data )
		DELETE_MEMORY_ARRAY(oPQI.qualifier.data)

}

int PolicyQualifierInfo::copyQualifiers(const ASN1T_IMP_PolicyInformation_policyQualifiers & iPQIs, QList<PolicyQualifierInfo>& oList)
{
	return copyASNObjects<PolicyQualifierInfo>(iPQIs,oList);
}

int PolicyQualifierInfo::copyQualifiers(const QList<PolicyQualifierInfo> iList ,ASN1T_IMP_PolicyInformation_policyQualifiers & oPQIs)
{
	return copyASNObjects<PolicyQualifierInfo>(iList,oPQIs);
}

int PolicyQualifierInfo::copyQualifiers(const QByteArray & iASNBytes, QList<PolicyQualifierInfo>& oList)
{
	return copyASNObjects<ASN1T_IMP_PolicyInformation_policyQualifiers,ASN1C_IMP_PolicyInformation_policyQualifiers,PolicyQualifierInfo>(iASNBytes,oList);
}

int PolicyQualifierInfo::copyQualifiers(const QList<PolicyQualifierInfo>& iList , QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_IMP_PolicyInformation_policyQualifiers,ASN1C_IMP_PolicyInformation_policyQualifiers,PolicyQualifierInfo>(iList,oASNBytes);
}


const QByteArray& PolicyQualifierInfo::getQualifier() const
{
	return mQualifier;
}

const ASN1TObjId& PolicyQualifierInfo::getQualifierID() const
{
	return mQualifierID;
}

PolicyQualifierInfo::~PolicyQualifierInfo(void)
{
}
NAMESPACE_END

#include "PolicyMappingsElement.h"


using namespace esya;


PolicyMappingsElement::PolicyMappingsElement(void)
{
}

PolicyMappingsElement::PolicyMappingsElement(const ASN1T_IMP_PolicyMappings_element & iPME)
{
	copyFromASNObject(iPME);
}

PolicyMappingsElement::PolicyMappingsElement(const QByteArray & iPME )
{
	constructObject(iPME);
}

PolicyMappingsElement::PolicyMappingsElement(const ASN1TObjId & iIssuerDP, const ASN1TObjId & iSubjectDP)
:	mIssuerDomainPolicy(iIssuerDP),
	mSubjectDomainPolicy(iSubjectDP)
{
}

PolicyMappingsElement::PolicyMappingsElement(const PolicyMappingsElement &iPME)
:	mIssuerDomainPolicy(iPME.getIssuerDomainPolicy()),
	mSubjectDomainPolicy(iPME.getSubjectDomainPolicy())
{
}


PolicyMappingsElement& PolicyMappingsElement::operator=(const PolicyMappingsElement& iPME)
{
	mIssuerDomainPolicy		= iPME.getIssuerDomainPolicy();
	mSubjectDomainPolicy	= iPME.getSubjectDomainPolicy();
	return *this;
}

bool esya::operator==(const PolicyMappingsElement & iRHS, const PolicyMappingsElement & iLHS)
{
	return (	( iRHS.getIssuerDomainPolicy()	== iLHS.getIssuerDomainPolicy()	) && 
				( iRHS.getSubjectDomainPolicy()	== iLHS.getSubjectDomainPolicy())	);
}

bool esya::operator!=(const PolicyMappingsElement & iRHS, const PolicyMappingsElement & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PolicyMappingsElement::copyFromASNObject(const ASN1T_IMP_PolicyMappings_element& iPME)
{
	mIssuerDomainPolicy		= iPME.issuerDomainPolicy;
	mSubjectDomainPolicy	= iPME.subjectDomainPolicy;
	return SUCCESS;
}

int PolicyMappingsElement::copyToASNObject(ASN1T_IMP_PolicyMappings_element & oPME) const
{
	oPME.issuerDomainPolicy		= mIssuerDomainPolicy;
	oPME.subjectDomainPolicy	= mSubjectDomainPolicy;
	return SUCCESS;
}

void PolicyMappingsElement::freeASNObject(ASN1T_IMP_PolicyMappings_element & oPME)const
{
}

int PolicyMappingsElement::copyMappings(const ASN1T_IMP_PolicyMappings & iPMs, QList<PolicyMappingsElement>& oList)
{
	return copyASNObjects<PolicyMappingsElement>(iPMs,oList);
}

int PolicyMappingsElement::copyMappings(const QList<PolicyMappingsElement> iList ,ASN1T_IMP_PolicyMappings & oPMs)
{
	return copyASNObjects<PolicyMappingsElement>(iList,oPMs);
}

int PolicyMappingsElement::copyMappings(const QByteArray & iASNBytes, QList<PolicyMappingsElement>& oList)
{
	return copyASNObjects<ASN1T_IMP_PolicyMappings,ASN1C_IMP_PolicyMappings,PolicyMappingsElement>(iASNBytes,oList);
}

int PolicyMappingsElement::copyMappings(const QList<PolicyMappingsElement>& iList , QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_IMP_PolicyMappings,ASN1C_IMP_PolicyMappings,PolicyMappingsElement>(iList,oASNBytes);
}

const ASN1TObjId& PolicyMappingsElement::getIssuerDomainPolicy() const
{
	return mIssuerDomainPolicy;
}

const ASN1TObjId& PolicyMappingsElement::getSubjectDomainPolicy() const
{
	return mSubjectDomainPolicy;
}

void PolicyMappingsElement::setIssuerDomainPolicy(const ASN1TObjId& iIssuerDP) 
{
	mIssuerDomainPolicy = iIssuerDP;
}

void PolicyMappingsElement::setSubjectDomainPolicy(const ASN1TObjId& iSubjectDP) 
{
	mSubjectDomainPolicy = iSubjectDP;
}

PolicyMappingsElement::~PolicyMappingsElement(void)
{
}


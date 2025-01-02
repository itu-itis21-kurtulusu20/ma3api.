#include "ExtensionGenerator.h"

using namespace esya;

int ExtensionGenerator::getExtension(const QList<Extension> & iList, ASN1OBJID iExtnID, Extension& oExtension )
{
	for (int i = 0 ; i<iList.size(); i++ )
	{
		if ( (ASN1OBJID)iList[i].getExtensionId() == iExtnID )
		{
			oExtension = iList[i];
			return SUCCESS;
		}
	}
	return FAILURE;
}

int ExtensionGenerator::getAKIExtension(const QList<Extension> & iList, AuthorityKeyIdentifier & oAKI)
{
	return getExtensionObject(iList,IMP_id_ce_authorityKeyIdentifier,oAKI);
}

int ExtensionGenerator::getSKIExtension(const QList<Extension> & iList, SubjectKeyIdentifier & oSKI)
{
	return getExtensionObject(iList , IMP_id_ce_subjectKeyIdentifier,oSKI);
}

int ExtensionGenerator::getIDPExtension(const QList<Extension> & iList, IssuingDistributionPoint &oIDP)
{
	return getExtensionObject(iList,IMP_id_ce_issuingDistributionPoint,oIDP);
}

int ExtensionGenerator::getKeyUsageExtension( const QList<Extension> & iList,  KeyUsage & oKU )
{
	return getExtensionObject(iList,IMP_id_ce_keyUsage,oKU);
}

int ExtensionGenerator::getCRLNumberExtension(const QList<Extension> & iList,  CRLNumber & oCRLNumber)
{
	return getExtensionObject(iList,IMP_id_ce_cRLNumber,oCRLNumber);
}

int ExtensionGenerator::getDeltaCRLIndicatorExtension(const QList<Extension> & iList, CRLNumber & oCRLNumber)
{
	return getExtensionObject(iList,IMP_id_ce_deltaCRLIndicator,oCRLNumber);
}

int ExtensionGenerator::getBCExtension(const QList<Extension>& iList,BasicConstraints & oBC)
{
	return  getExtensionObject(iList,IMP_id_ce_basicConstraints,oBC);
}

int ExtensionGenerator::getAIAExtension(const QList<Extension> & iList, AuthorityInfoAccess & oAIA)
{
	return  getExtensionObject(iList,IMP_id_pe_authorityInfoAccess,oAIA);
}


int ExtensionGenerator::getCDPExtension(const QList<Extension> & iList, CRLDistributionPoints &oCDP)
{
	return getExtensionObject(iList,IMP_id_ce_cRLDistributionPoints,oCDP);
}


int ExtensionGenerator::getFreshestCRLExtension(const QList<Extension> & iList, QList<DistributionPoint> &oFreshestCRL)
{
	Extension freshestCRLExtension;
	int i = getExtension(iList,IMP_id_ce_freshestCRL,freshestCRLExtension);
	if (i == SUCCESS)
	{
		ASN1BERDecodeBuffer decBuf((OSOCTET*)freshestCRLExtension.getExtensionValue().data(),freshestCRLExtension.getExtensionValue().size());
		ASN1T_IMP_FreshestCRL freshestCRL;
		ASN1C_IMP_FreshestCRL cFreshestCRL(freshestCRL);
		int stat = cFreshestCRL.DecodeFrom(decBuf);
		if (stat != ASN_OK )
		{
			throw EException(QString("freshestCRL Decode Edilemedi. Hata : %1").arg(stat),__FILE__,__LINE__);
		}
		DistributionPoint().copyCDPs(freshestCRL,oFreshestCRL);
	}
	return i;
}

int ExtensionGenerator::getSANExtension(const QList<Extension>& iList, SubjectAlternativeName & oSAN)
{
	return getExtensionObject(iList,IMP_id_ce_subjectAltName,oSAN);
}

int ExtensionGenerator::getIANExtension(const QList<Extension>& iList, SubjectAlternativeName & oIAN)
{
	return getExtensionObject(iList,IMP_id_ce_issuerAltName,oIAN);
}

int ExtensionGenerator::getEKUExtension(const QList<Extension>& iList,ExtendedKeyUsage & oEKU)
{
	return getExtensionObject(iList,IMP_id_ce_extKeyUsage,oEKU);
}

int ExtensionGenerator::getCertificatePoliciesExtension(const QList<Extension>& iList,CertificatePolicies & oCP)
{
	return getExtensionObject(iList,IMP_id_ce_certificatePolicies,oCP);
}

int ExtensionGenerator::getPolicyMappingsExtension(const QList<Extension>& iList,PolicyMappings & oPM)
{
	return getExtensionObject(iList,IMP_id_ce_policyMappings,oPM);
}

int ExtensionGenerator::getPolicyConstraintsExtension(const QList<Extension>& iList,PolicyConstraints & oPC)
{
	return getExtensionObject(iList,IMP_id_ce_policyConstraints,oPC);
}

int ExtensionGenerator::getNameConstraintsExtension(const QList<Extension>& iList,NameConstraints & oNC)
{
	return getExtensionObject(iList,IMP_id_ce_nameConstraints,oNC);
}

int ExtensionGenerator::getQCStatementsExtension(const QList<Extension>& iList,QCStatements& oQCS)
{
	return getExtensionObject(iList,PKIXQUAL_id_pe_qcStatements,oQCS);
}

int ExtensionGenerator::getInhibitAnyPolicyExtension(const QList<Extension>& iList,InhibitAnyPolicy & oIAP)
{
	return getExtensionObject(iList,IMP_id_ce_inhibitAnyPolicy,oIAP);
}

int ExtensionGenerator::getCertificateIssuerExtension(const QList<Extension>& iList,CertificateIssuer & oCI)
{
	return getExtensionObject(iList,IMP_id_ce_certificateIssuer,oCI);
}
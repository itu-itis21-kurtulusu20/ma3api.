#include "EklentiFabrikasi.h"
#include "KeyUsage.h"
#include "AuthorityKeyIdentifier.h"
#include "AuthorityInfoAccess.h"
#include "SubjectAlternativeName.h"
#include "SubjectKeyIdentifier.h"
#include "ExtendedKeyUsage.h"
#include "BasicConstraints.h"
#include "CRLDistributionPoints.h"
#include "CRLNumber.h"
#include "NameConstraints.h"
#include "PolicyConstraints.h"
#include "QCStatements.h"

#include "IssuingDistributionPoint.h"
#include "CertificatePolicies.h"
#include "BilinmeyenEklenti.h"


using namespace esya;

/**
* \brief
* Verilen Eklenti için AY_Eklenti arayüzünü desteklyen uygun EASNWrapper nesnesini üretir
*
* \param 		const Extension & iExtension
* Eklenti
*
* \return   	AY_Eklenti*
*/
AY_Eklenti* EklentiFabrikasi::eklentiUret(const Extension& iExtension)
{
	const ASN1TObjId & extType = iExtension.getExtensionId();

	try
	{
		if ( extType == (ASN1TObjId) IMP_id_ce_keyUsage )
			return new KeyUsage(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_authorityKeyIdentifier)
			return new AuthorityKeyIdentifier(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_pe_authorityInfoAccess)
			return new AuthorityInfoAccess(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_cRLDistributionPoints)
			return new CRLDistributionPoints(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_cRLNumber)
			return new CRLNumber(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_basicConstraints)
			return new BasicConstraints(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_extKeyUsage)
			return new ExtendedKeyUsage(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_nameConstraints)
			return new NameConstraints(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_policyConstraints)
			return new PolicyConstraints(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_certificatePolicies)
			return new CertificatePolicies(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_subjectKeyIdentifier)
			return new SubjectKeyIdentifier(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_subjectAltName)
			return new SubjectAlternativeName(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) PKIXQUAL_id_pe_qcStatements)
			return new QCStatements(iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_freshestCRL)
			return new BilinmeyenEklenti(extType,iExtension.getExtensionValue());
		else if ( extType == (ASN1TObjId) IMP_id_ce_policyMappings)
			return new BilinmeyenEklenti(extType,iExtension.getExtensionValue());
		
		return new BilinmeyenEklenti(extType,iExtension.getExtensionValue());
	}
	catch (EException& exc)
	{
		return new BilinmeyenEklenti(extType,iExtension.getExtensionValue());
	}

}


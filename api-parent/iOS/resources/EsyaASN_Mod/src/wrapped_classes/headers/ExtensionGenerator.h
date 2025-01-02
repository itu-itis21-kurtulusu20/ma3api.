#ifndef __EXTENSIONGENERATOR__
#define __EXTENSIONGENERATOR__

#include "Extension.h"
#include "AccessDescription.h"
#include "DistributionPoint.h"
#include "KeyUsage.h"
#include "AuthorityKeyIdentifier.h"
#include "SubjectKeyIdentifier.h"
#include "SubjectAlternativeName.h"
#include "IssuingDistributionPoint.h"
#include "CRLNumber.h"
#include "GeneralName.h"
#include "BasicConstraints.h"
#include "KeyPurposeId.h"
#include "AuthorityInfoAccess.h"
#include "CRLDistributionPoints.h"
#include "ExtendedKeyUsage.h"
#include "CertificatePolicies.h"
#include "PolicyMappings.h"
#include "PolicyConstraints.h"
#include "InhibitAnyPolicy.h"
#include "CertificateIssuer.h"
#include "NameConstraints.h"
#include "QCStatements.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Extension Listesinde verilen ASN1 wrapper sýnýfýný bulur.
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT ExtensionGenerator
	{
		ExtensionGenerator(){};
	public:
		
		static int getExtension(const QList<Extension>& iList , ASN1OBJID iExtnID, Extension& oExtension );
		
		template<class T>
		static int getExtensionObject(const QList<Extension>& iList , ASN1OBJID iExtnID ,T& oObject )
		{
			Extension extension;
			int i = getExtension(iList,iExtnID,extension);
			if (i == SUCCESS)
			{
				oObject.constructObject(extension.getExtensionValue());
			}
			return i;
		}


		static int getAIAExtension(const QList<Extension>& iList , AuthorityInfoAccess & oAIA);
		static int getCDPExtension(const QList<Extension>& iList ,CRLDistributionPoints &oCDP);
		static int getKeyUsageExtension(const QList<Extension>& iList , KeyUsage & oKU );
		static int getAKIExtension(const QList<Extension>& iList ,AuthorityKeyIdentifier & oAKI);
		static int getSKIExtension(const QList<Extension>& iList ,SubjectKeyIdentifier & oSKI);
		static int getIDPExtension(const QList<Extension>& iList ,IssuingDistributionPoint &oIDP);
		static int getCRLNumberExtension(const QList<Extension>& iList ,CRLNumber & oCRLNumber);
		static int getDeltaCRLIndicatorExtension(const QList<Extension>& iList ,CRLNumber & oCRLNumber);	
		static int getFreshestCRLExtension(const QList<Extension>& iList ,QList<DistributionPoint> &oFreshestCRL);
		static int getSANExtension(const QList<Extension>& iList,SubjectAlternativeName & oSAN);
		static int getIANExtension(const QList<Extension>& iList,SubjectAlternativeName & oIAN);
		static int getBCExtension(const QList<Extension>& iList,BasicConstraints & oBC);
		static int getEKUExtension(const QList<Extension>& iList,ExtendedKeyUsage & oEKU);
		static int getCertificatePoliciesExtension(const QList<Extension>& iList,CertificatePolicies & oCP);
		static int getPolicyMappingsExtension(const QList<Extension>& iList,PolicyMappings & oPM);
		static int getPolicyConstraintsExtension(const QList<Extension>& iList,PolicyConstraints & oPC);
		static int getInhibitAnyPolicyExtension(const QList<Extension>& iList,InhibitAnyPolicy & oIAP);
		static int getCertificateIssuerExtension(const QList<Extension>& iList,CertificateIssuer & oCI);
		static int getNameConstraintsExtension(const QList<Extension>& iList,NameConstraints & oNC);
		static int getQCStatementsExtension(const QList<Extension>& iList,QCStatements& oQCS);

	public:
		
	};
}

#endif


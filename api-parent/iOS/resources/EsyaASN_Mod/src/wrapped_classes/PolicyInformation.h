
#ifndef __POLICYINFORMATION__
#define __POLICYINFORMATION__


#include "PolicyQualifierInfo.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT PolicyInformation  : public EASNWrapperTemplate<ASN1T_IMP_PolicyInformation,ASN1C_IMP_PolicyInformation>
	{

		ASN1TObjId mPolicyIdentifier;
		QList<PolicyQualifierInfo> mPolicyQualifiers;

	public:

		PolicyInformation(void);
		PolicyInformation(const ASN1T_IMP_PolicyInformation & iPI);
		PolicyInformation(const QByteArray & iPI );
		PolicyInformation(const ASN1OBJID & iPolicyIdentifier, const QList<PolicyQualifierInfo> &iPolicyQualifiers=QList<PolicyQualifierInfo>());
		PolicyInformation(const PolicyInformation &iPI);


		PolicyInformation& operator=(const PolicyInformation& iPI);
		friend bool operator==(const PolicyInformation & iRHS, const PolicyInformation & iLHS);
		friend bool operator!=(const PolicyInformation & iRHS, const PolicyInformation & iLHS);

		int copyFromASNObject(const ASN1T_IMP_PolicyInformation& iPI);
		int copyToASNObject(ASN1T_IMP_PolicyInformation & oPI) const;
		void freeASNObject(ASN1T_IMP_PolicyInformation & oPI)const;

		int copyPIs(const ASN1T_IMP_CertificatePolicies & iPIs, QList<PolicyInformation>& oList);
		int copyPIs(const QList<PolicyInformation> iList ,ASN1T_IMP_CertificatePolicies & oPIs);	
		int copyPIs(const QByteArray & iASNBytes, QList<PolicyInformation>& oList);
		int copyPIs(const QList<PolicyInformation>& oList , QByteArray & iASNBytes);

		virtual ~PolicyInformation(void);

		// GETTERS AND SETTERS

		const QList<PolicyQualifierInfo>&	getPolicyQualifiers() const;
		const ASN1TObjId&					getPolicyIdentifier() const;

	};

}

#endif


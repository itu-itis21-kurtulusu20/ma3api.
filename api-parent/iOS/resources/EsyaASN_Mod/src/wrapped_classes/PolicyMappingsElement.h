
#ifndef __POLICYMAPPINGSELEMENT__
#define __POLICYMAPPINGSELEMENT__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "Implicit.h"

namespace esya
{

	class Q_DECL_EXPORT PolicyMappingsElement : public EASNWrapperTemplate<ASN1T_IMP_PolicyMappings_element,ASN1C_IMP_PolicyMappings_element>
	{
		ASN1TObjId mIssuerDomainPolicy;
		ASN1TObjId mSubjectDomainPolicy;

	public:

		PolicyMappingsElement(void);
		PolicyMappingsElement(const ASN1T_IMP_PolicyMappings_element & iPME);
		PolicyMappingsElement(const QByteArray & iPQI );
		PolicyMappingsElement(const ASN1TObjId & iIssuerDP, const ASN1TObjId & iSubjectDP);
		PolicyMappingsElement(const PolicyMappingsElement &);


		PolicyMappingsElement& operator=(const PolicyMappingsElement&);

		int copyFromASNObject(const ASN1T_IMP_PolicyMappings_element& iPQI);
		int copyToASNObject(ASN1T_IMP_PolicyMappings_element & oPQI) const;
		void freeASNObject(ASN1T_IMP_PolicyMappings_element & oPME)const;

		int copyMappings(const ASN1T_IMP_PolicyMappings & iPMs, QList<PolicyMappingsElement>& oList);
		int copyMappings(const QList<PolicyMappingsElement> iList ,ASN1T_IMP_PolicyMappings & oPMs);	
		int copyMappings(const QByteArray & iASNBytes, QList<PolicyMappingsElement>& oList);
		int copyMappings(const QList<PolicyMappingsElement>& iList , QByteArray & oASNBytes);

		virtual ~PolicyMappingsElement(void);

		// GETTERS AND SETTERS

		const ASN1TObjId& getIssuerDomainPolicy() const;
		const ASN1TObjId& getSubjectDomainPolicy() const;

		void setIssuerDomainPolicy(const ASN1TObjId& iIssuerDP) ;
		void setSubjectDomainPolicy(const ASN1TObjId& iSubjectDP);

	};

	bool operator==(const PolicyMappingsElement & iRHS, const PolicyMappingsElement & iLHS);
	bool operator!=(const PolicyMappingsElement & iRHS, const PolicyMappingsElement & iLHS);


}

#endif


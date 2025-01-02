
#ifndef __POLICYQUALIFIERINFO__
#define __POLICYQUALIFIERINFO__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "Implicit.h"

namespace esya
{

	class Q_DECL_EXPORT PolicyQualifierInfo  : public EASNWrapperTemplate<ASN1T_IMP_PolicyQualifierInfo,ASN1C_IMP_PolicyQualifierInfo>
	{
		ASN1TObjId mQualifierID;
		QByteArray mQualifier;

	public:

		PolicyQualifierInfo(void);
		PolicyQualifierInfo(const ASN1T_IMP_PolicyQualifierInfo & iPQI);
		PolicyQualifierInfo(const QByteArray & iPQI );
		PolicyQualifierInfo(const ASN1OBJID & iQualifierID, const QByteArray &iQualifier);
		PolicyQualifierInfo(const PolicyQualifierInfo &);

		PolicyQualifierInfo& operator=(const PolicyQualifierInfo&);
		friend bool operator==(const PolicyQualifierInfo & iRHS, const PolicyQualifierInfo & iLHS);
		friend bool operator!=(const PolicyQualifierInfo & iRHS, const PolicyQualifierInfo & iLHS);

		int copyFromASNObject(const ASN1T_IMP_PolicyQualifierInfo& iPQI);
		int copyToASNObject(ASN1T_IMP_PolicyQualifierInfo & oPQI) const;
		void freeASNObject(ASN1T_IMP_PolicyQualifierInfo & oPQI)const;

		int copyQualifiers(const ASN1T_IMP_PolicyInformation_policyQualifiers & iPQIs, QList<PolicyQualifierInfo>& oList);
		int copyQualifiers(const QList<PolicyQualifierInfo> iList ,ASN1T_IMP_PolicyInformation_policyQualifiers & oPQIs);	
		int copyQualifiers(const QByteArray & iASNBytes, QList<PolicyQualifierInfo>& oList);
		int copyQualifiers(const QList<PolicyQualifierInfo>& oList , QByteArray & iASNBytes);

		virtual ~PolicyQualifierInfo(void);

		// GETTERS AND SETTERS
		
		const QByteArray& getQualifier() const;
		const ASN1TObjId& getQualifierID() const;

		
	};

}

#endif


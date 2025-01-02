
#ifndef __DISTRIBUTIONPOINTNAME__
#define __DISTRIBUTIONPOINTNAME__

#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "GeneralName.h"


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
	class Q_DECL_EXPORT DistributionPointName  : public EASNWrapperTemplate<ASN1T_IMP_DistributionPointName,ASN1C_IMP_DistributionPointName>
	{
	public:
		enum DPN_Type { FULLNAME = T_IMP_DistributionPointName_fullName , NAMER2CRLISSUER = T_IMP_DistributionPointName_nameRelativeToCRLIssuer};
	protected:	
		DPN_Type mType;

	   QList<GeneralName> mFullName;
	   RelativeDistinguishedName mNameR2CRLIssuer;

	public:

		DistributionPointName(const DistributionPointName &);
		DistributionPointName(const ASN1T_IMP_DistributionPointName & );
		DistributionPointName(const QByteArray & );
		DistributionPointName(void);


		DistributionPointName & operator=(const DistributionPointName&);
		friend bool operator==(const DistributionPointName& iRHS, const DistributionPointName& iLHS);
		friend bool operator!=(const DistributionPointName& iRHS, const DistributionPointName& iLHS);


		int copyFromASNObject(const ASN1T_IMP_DistributionPointName& iDPN);
		int copyToASNObject(ASN1T_IMP_DistributionPointName & oDPN) const;
		void freeASNObject(ASN1T_IMP_DistributionPointName & oDPN)const;

		virtual ~DistributionPointName(void);

		// GETTERS AND SETTERS

		const DPN_Type& getType() const;
		const QList<GeneralName>& getFullName() const;
		const RelativeDistinguishedName & getNameR2CRLIssuer() const;

	};

}

#endif


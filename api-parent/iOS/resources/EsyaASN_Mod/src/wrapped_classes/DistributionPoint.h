
#ifndef __DISTRIBUTIONPOINT__
#define __DISTRIBUTIONPOINT__

#include "EException.h"
#include "GeneralName.h"
#include "DistributionPointName.h"
#include "ReasonFlags.h"

#define AT_HTTP "HTTP"
#define AT_LDAP "LDAP"
#define AT_DN	"DN"

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
	class Q_DECL_EXPORT DistributionPoint  : public EASNWrapperTemplate<ASN1T_IMP_DistributionPoint,ASN1C_IMP_DistributionPoint>
	{
	  
	   bool mDPNPresent;
	   bool mReasonsPresent;
	   bool mCRLIssuerPresent;
	   
	   DistributionPointName mDPN;
	   ReasonFlags mReasons;
	   QList<GeneralName> mCRLIssuer;

		
		

	public:

		DistributionPoint(const DistributionPoint &);
		DistributionPoint(const ASN1T_IMP_DistributionPoint & );
		DistributionPoint(const QByteArray & );
		DistributionPoint(void);


		DistributionPoint & operator=(const DistributionPoint&);
		friend bool operator==(const DistributionPoint& iRHS, const DistributionPoint& iLHS);
		friend bool operator!=(const DistributionPoint& iRHS, const DistributionPoint& iLHS);

		int copyFromASNObject(const ASN1T_IMP_DistributionPoint& iDP);
		int copyToASNObject(ASN1T_IMP_DistributionPoint & oDP) const;
		void freeASNObject(ASN1T_IMP_DistributionPoint & oDP)const;

		int copyCDPs(const ASN1T_IMP_CRLDistributionPoints & iCDPs, QList<DistributionPoint>& oList);
		int copyCDPs(const QList<DistributionPoint> iList , ASN1T_IMP_CRLDistributionPoints& oCDPs);	

		virtual ~DistributionPoint(void);

		const bool isDPNPresent()const;
		const bool isReasonsPresent()const;
		const bool isCRLIssuerPresent()const;
		const DistributionPointName& getDPN() const;
		const ReasonFlags& getReasons() const;
		const QList<GeneralName> & getCRLIssuer() const;

		QList<QString> cdpAdresleriAl(const Name & aIssuer) const;

	};

}

#endif


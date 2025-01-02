
#ifndef __ISSUINGDISTRIBUTIONPOINT__
#define __ISSUINGDISTRIBUTIONPOINT__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "ReasonFlags.h"
#include "DistributionPointName.h"
#include "AY_Eklenti.h"

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
	class Q_DECL_EXPORT IssuingDistributionPoint  : public EASNWrapperTemplate<ASN1T_IMP_IssuingDistributionPoint,ASN1C_IMP_IssuingDistributionPoint> , AY_Eklenti
	{
	   bool mDistributionPointPresent : 1;
	   bool mOnlySomeReasonsPresent : 1;
	   bool mOnlyContainsUserCerts;
	   bool mOnlyContainsCACerts;
	   bool mOnlyContainsAttributeCerts;
	   bool mIndirectCRL;


	   DistributionPointName mDistributionPoint;
	   ReasonFlags mOnlySomeReasons;

	public:

		IssuingDistributionPoint(const IssuingDistributionPoint &);
		IssuingDistributionPoint(const ASN1T_IMP_IssuingDistributionPoint & );
		IssuingDistributionPoint(const QByteArray & );
		IssuingDistributionPoint(void);
		

		IssuingDistributionPoint & operator=(const IssuingDistributionPoint&);
		Q_DECL_EXPORT friend bool operator==(const IssuingDistributionPoint& iRHS, const IssuingDistributionPoint& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const IssuingDistributionPoint& iRHS, const IssuingDistributionPoint& iLHS);

		int copyFromASNObject(const ASN1T_IMP_IssuingDistributionPoint& iIDP);
		int copyToASNObject(ASN1T_IMP_IssuingDistributionPoint & oIDP) const;
		void freeASNObject(ASN1T_IMP_IssuingDistributionPoint & oIDP)const;

		virtual ~IssuingDistributionPoint(void);

		// GETTERS AND SETTERS

		void setDistributionPointPresent	( bool  iDPP);
		void setOnlySomeReasonsPresent		( bool  iOSRP);
		void setOnlyContainsUserCerts		( bool  iCUC);
		void setOnlyContainsCACerts			( bool  iCCC);
		void setOnlyContainsAttributeCerts	( bool  iCAC);
		void setIndirectCRL					( bool  iIC);
		void setDistributionPoint			( const DistributionPointName&	iDP);
		void setOnlySomeReasons				( const ReasonFlags&			iOSR);

		bool isDistributionPointPresent()	const;
		bool isOnlySomeReasonsPresent()		const;
		bool isOnlyContainsUserCerts()		const;
		bool isOnlyContainsCACerts()		const;
		bool isOnlyContainsAttributeCerts()	const;
		bool isIndirectCRL()				const;
		
		
		const DistributionPointName & getDistributionPoint()	const;
		const ReasonFlags			& getOnlySomeReasons()		const;


		/************************************************************************/
		/*					AY_EKLENTI FONKSIYONLARI                            */
		/************************************************************************/


		virtual QString eklentiAdiAl()			const ;
		virtual QString eklentiKisaDegerAl()	const ;
		virtual QString eklentiUzunDegerAl()	const ;

		virtual AY_Eklenti* kendiniKopyala() const ;

	};

}


#endif


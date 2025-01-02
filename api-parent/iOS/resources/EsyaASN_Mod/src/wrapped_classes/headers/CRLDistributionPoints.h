#ifndef __CRLDISTRIBUTIONPOINTS__
#define __CRLDISTRIBUTIONPOINTS__

#include "DistributionPoint.h"
#include "CDP.h"
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
	class Q_DECL_EXPORT CRLDistributionPoints  : public EASNWrapperTemplate<ASN1T_IMP_CRLDistributionPoints,ASN1C_IMP_CRLDistributionPoints> , public AY_Eklenti
	{
		QList<DistributionPoint> mList;

	public:
		CRLDistributionPoints(void);
		CRLDistributionPoints(const ASN1T_IMP_CRLDistributionPoints &);
		CRLDistributionPoints(const QByteArray &);
		CRLDistributionPoints(const CRLDistributionPoints&);
		CRLDistributionPoints(const QList<DistributionPoint>&);

		CRLDistributionPoints & operator=(const CRLDistributionPoints&);
		Q_DECL_EXPORT friend bool operator==(const CRLDistributionPoints & iRHS, const CRLDistributionPoints& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const CRLDistributionPoints & iRHS, const CRLDistributionPoints& iLHS);

		int copyFromASNObject(const ASN1T_IMP_CRLDistributionPoints &);
		int copyToASNObject(ASN1T_IMP_CRLDistributionPoints& )const;
		void freeASNObject(ASN1T_IMP_CRLDistributionPoints & )const;

		virtual ~CRLDistributionPoints(void);


		/////////////////////////////////////////////////////////////////

		const QList<DistributionPoint> &getList() const;

		virtual QString toString() const;
		QStringList		toStringList()const ;

		static int DPtoCDP(const DistributionPoint & iDP , const Name & iIssuer , CDP & oCDP);


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


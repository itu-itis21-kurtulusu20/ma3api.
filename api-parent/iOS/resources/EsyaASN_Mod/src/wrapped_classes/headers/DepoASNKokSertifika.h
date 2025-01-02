
#ifndef __DEPOASNKOKSERTIFIKA__
#define __DEPOASNKOKSERTIFIKA__

#include "DepoASNSilinecekKokSertifika.h"
#include "DepoASNEklenecekKokSertifika.h"

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
	class Q_DECL_EXPORT DepoASNKokSertifika : public EASNWrapperTemplate<ASN1T_SD_DepoASNKokSertifika,ASN1C_SD_DepoASNKokSertifika>
	{
		enum KS_Type	{	KS_eklenecekSertifika = T_SD_DepoASNKokSertifika_eklenecekSertifika, 
							KS_silinecekSertifika = T_SD_DepoASNKokSertifika_silinecekSertifika	} ;
		
		
		KS_Type mType;
		DepoASNEklenecekKokSertifika * mEklenecekSertifika;
		DepoASNSilinecekKokSertifika * mSilinecekSertifika;

	public:
		DepoASNKokSertifika(void);
		DepoASNKokSertifika(const ASN1T_SD_DepoASNKokSertifika & iKS);
		DepoASNKokSertifika(const QByteArray & iKS);
		DepoASNKokSertifika(DepoASNEklenecekKokSertifika *iEKS);
		DepoASNKokSertifika(DepoASNSilinecekKokSertifika *iSKS);
		DepoASNKokSertifika(const DepoASNKokSertifika &iKS);

		DepoASNKokSertifika & operator=(const DepoASNKokSertifika & iKS);
		friend bool operator==(const DepoASNKokSertifika & iRHS, const DepoASNKokSertifika& iLHS);
		friend bool operator!=(const DepoASNKokSertifika& iRHS, const DepoASNKokSertifika& iLHS);


		int copyFromASNObject(const ASN1T_SD_DepoASNKokSertifika& iKS);
		int copyToASNObject(ASN1T_SD_DepoASNKokSertifika & oKS) const;
		void freeASNObject(ASN1T_SD_DepoASNKokSertifika& oKS)const;

		int  copyDepoASNKokSertifikaList(const QList<DepoASNKokSertifika> iList ,ASN1TPDUSeqOfList & oKSs);
		int	copyDepoASNKokSertifikaList(const ASN1TPDUSeqOfList & iKSs, QList<DepoASNKokSertifika>& oList);

		virtual ~DepoASNKokSertifika(void);

		// GETTERS AND SETTERS
		
		KS_Type	getType() const;

		const	DepoASNEklenecekKokSertifika* getEklenecekSertifika() const ;
				DepoASNEklenecekKokSertifika* getEklenecekSertifika() ;

		const	DepoASNSilinecekKokSertifika* getSilinecekSertifika() const ;
				DepoASNSilinecekKokSertifika* getSilinecekSertifika() ;


		void setEklenecekSertifika(DepoASNEklenecekKokSertifika *);
		void setSilinecekSertifika(DepoASNSilinecekKokSertifika *);

	};
}

#endif 


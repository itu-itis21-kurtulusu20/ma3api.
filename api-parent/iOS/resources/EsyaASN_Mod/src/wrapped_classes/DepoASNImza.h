
#ifndef __DEPOASNIMZA__
#define __DEPOASNIMZA__


#include "DepoASNRawImza.h"
#include "DepoASNKokSertifika.h"


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
	class Q_DECL_EXPORT DepoASNImza	: public EASNWrapperTemplate<ASN1T_SD_DepoASNImza,ASN1C_SD_DepoASNImza>
	{
		DepoASNKokSertifika mImzalanan;
		DepoASNRawImza		mImza;

	public:
		DepoASNImza(void);
		DepoASNImza(const ASN1T_SD_DepoASNImza & iImza);
		DepoASNImza(const QByteArray & iImza);
		DepoASNImza(const DepoASNKokSertifika &iKS,const DepoASNRawImza & iRI);
		DepoASNImza(const DepoASNImza&iImza);

		DepoASNImza & operator=(const DepoASNImza & iImza);
		friend bool operator==(const DepoASNImza& iRHS, const DepoASNImza& iLHS);
		friend bool operator!=(const DepoASNImza& iRHS, const DepoASNImza& iLHS);


		int copyFromASNObject(const ASN1T_SD_DepoASNImza& iImza);
		int copyToASNObject(ASN1T_SD_DepoASNImza & oImza) const;
		void freeASNObject(ASN1T_SD_DepoASNImza& oImza)const;

		int  copyDepoASNImzaList(const QList<DepoASNImza> iList ,ASN1TPDUSeqOfList & oImzas);
		int	 copyDepoASNImzaList(const ASN1TPDUSeqOfList & iKSs, QList<DepoASNImza>& oList);

		virtual ~DepoASNImza(void);

		// GETTERS AND SETTERS

		const DepoASNKokSertifika	& getImzalanan()	const;		
		const DepoASNRawImza		& getImza()			const;

		void setImzalanan(const DepoASNKokSertifika	& iKS) ;
		void setImza(const DepoASNRawImza & iImza) ;

	};

}

#endif 


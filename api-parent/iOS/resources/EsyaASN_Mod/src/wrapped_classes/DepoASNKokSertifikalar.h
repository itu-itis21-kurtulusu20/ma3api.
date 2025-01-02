
#ifndef __DEPOASNKOKSERTIFIKALAR__
#define __DEPOASNKOKSERTIFIKALAR__

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
	class Q_DECL_EXPORT DepoASNKokSertifikalar : public EASNWrapperTemplate<ASN1T_SD_DepoASNKokSertifikalar,ASN1C_SD_DepoASNKokSertifikalar>
	{
		QList<DepoASNKokSertifika> mList;
	
	public:
		DepoASNKokSertifikalar(void);
		DepoASNKokSertifikalar(const ASN1T_SD_DepoASNKokSertifikalar & iKSList);
		DepoASNKokSertifikalar(const QByteArray & iKSList);
		DepoASNKokSertifikalar(const DepoASNKokSertifikalar &iKSList);

		DepoASNKokSertifikalar & operator=(const DepoASNKokSertifikalar& iKSList);
		
		friend bool operator==(const DepoASNKokSertifikalar & iRHS, const DepoASNKokSertifikalar& iLHS);
		friend bool operator!=(const DepoASNKokSertifikalar & iRHS, const DepoASNKokSertifikalar& iLHS);


		int copyFromASNObject(const ASN1T_SD_DepoASNKokSertifikalar& iKSList);
		int copyToASNObject(ASN1T_SD_DepoASNKokSertifikalar & oKSList) const;
		void freeASNObject(ASN1T_SD_DepoASNKokSertifikalar& oKSList)const;

		virtual ~DepoASNKokSertifikalar(void);

		// GETTERS AND SETTERS

		const QList<DepoASNKokSertifika> &getList() const ;

		void setList(const QList<DepoASNKokSertifika> &iList);
		void addKokSertifika(const DepoASNKokSertifika &iKS);

	};

}

#endif



#ifndef __DEPOASNIMZALAR__
#define __DEPOASNIMZALAR__

#include "DepoASNImza.h"


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
	class Q_DECL_EXPORT DepoASNImzalar : public EASNWrapperTemplate<ASN1T_SD_DepoASNImzalar,ASN1C_SD_DepoASNImzalar>
	{
		QList<DepoASNImza> mList;

	public:
		DepoASNImzalar(void);
		DepoASNImzalar(const ASN1T_SD_DepoASNImzalar & iImzaList);
		DepoASNImzalar(const QByteArray & iImzaList);
		DepoASNImzalar(const DepoASNImzalar &iImzaList);

		DepoASNImzalar & operator=(const DepoASNImzalar& iImzaList);

		friend bool operator==(const DepoASNImzalar & iRHS, const DepoASNImzalar& iLHS);
		friend bool operator!=(const DepoASNImzalar & iRHS, const DepoASNImzalar& iLHS);


		int copyFromASNObject(const ASN1T_SD_DepoASNImzalar& iImzaList);
		int copyToASNObject(ASN1T_SD_DepoASNImzalar & oImzaList) const;
		void freeASNObject(ASN1T_SD_DepoASNImzalar& oImzaList)const;

		virtual ~DepoASNImzalar(void);

		// GETTERS AND SETTERS

		const QList<DepoASNImza> &getList() const ;

		void setList(const QList<DepoASNImza> &iList);
		void addImza(const DepoASNImza &iImza);

	};

}

#endif



#ifndef __DEPOASNSILINECEKKOKSERTIFIKA__
#define __DEPOASNSILINECEKKOKSERTIFIKA__

#include "ASN_SertifikaDeposu.h"
#include "Name.h"
#include "ETime.h"
#include "KeyUsage.h"
#include "SubjectKeyIdentifier.h"
#include "SerialNumber.h"

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
	class Q_DECL_EXPORT DepoASNSilinecekKokSertifika : public EASNWrapperTemplate<ASN1T_SD_DepoASNSilinecekKokSertifika,ASN1C_SD_DepoASNSilinecekKokSertifika>
	{
		QByteArray				mKokSertifikaValue;
		SerialNumber			mKokSerialNumber;
		Name					mKokIssuerName;
		Name					mKokSubjectName;

	public:

		DepoASNSilinecekKokSertifika(void);
		DepoASNSilinecekKokSertifika(const ASN1T_SD_DepoASNSilinecekKokSertifika & iKS);
		DepoASNSilinecekKokSertifika(const QByteArray & iKS);
		DepoASNSilinecekKokSertifika(const DepoASNSilinecekKokSertifika &iKS);

		DepoASNSilinecekKokSertifika & operator=(const DepoASNSilinecekKokSertifika & iKS);
		friend bool operator==(const DepoASNSilinecekKokSertifika & iRHS, const DepoASNSilinecekKokSertifika& iLHS);
		friend bool operator!=(const DepoASNSilinecekKokSertifika& iRHS, const DepoASNSilinecekKokSertifika& iLHS);


		int copyFromASNObject(const ASN1T_SD_DepoASNSilinecekKokSertifika& iKS);
		int copyToASNObject(ASN1T_SD_DepoASNSilinecekKokSertifika & oKS) const;
		void freeASNObject(ASN1T_SD_DepoASNSilinecekKokSertifika& oKS)const;

		virtual ~DepoASNSilinecekKokSertifika(void);

		// GETTERS AND SETTERS

		const QByteArray			& getKokSertifikaValue() const ;
		const SerialNumber			& getKokSerialNumber() const ;
		const Name					& getKokIssuerName() const ;
		const Name					& getKokSubjectName() const ;

		void setKokSertifikaValue(const QByteArray & );
		void setKokSerialNumber(const SerialNumber & );
		void setKokIssuerName(const Name & );
		void setKokSubjectName(const Name & );

	};
}

#endif


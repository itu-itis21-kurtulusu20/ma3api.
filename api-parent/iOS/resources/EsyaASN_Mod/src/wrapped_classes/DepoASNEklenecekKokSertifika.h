
#ifndef __DEPOASNEKLENECEKKOKSERTIFIKA__
#define __DEPOASNEKLENECEKKOKSERTIFIKA__

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

	class Q_DECL_EXPORT DepoASNEklenecekKokSertifika : public EASNWrapperTemplate<ASN1T_SD_DepoASNEklenecekKokSertifika,ASN1C_SD_DepoASNEklenecekKokSertifika>
	{
	public:
		enum KS_Type	{	KS_kokSertifika = SD_KokSertifikaTipi::kokSertifika, KS_caprazSertifika = SD_KokSertifikaTipi::caprazSertifika , KS_smSertifikasi = SD_KokSertifikaTipi::smSertifikasi, KS_hizmetSertifikasi = SD_KokSertifikaTipi::hizmetSertifikasi } ;

		enum KGS_Type	{	KGS_kanuni = SD_KOKGuvenSeviyesi::kanuni , KGS_kurumsal = SD_KOKGuvenSeviyesi::kurumsal , KGS_kisisel = SD_KOKGuvenSeviyesi::kisisel } ;

	protected:
		QByteArray				mKokSertifikaValue;
		QByteArray				mKokSertifikaHash;
		SerialNumber			mKokSerialNumber;
		Name					mKokIssuerName;
		Name					mKokSubjectName;
		ETime					mKokStartDate;
		ETime					mKokEndDate;
		KeyUsage				mKokKeyUsage;
		SubjectKeyIdentifier	mKokSubjectKeyIdentifier;
		KS_Type					mKokSertifikaTipi;
		KGS_Type				mKokGuvenSeviyesi;

	public:

		DepoASNEklenecekKokSertifika(void);
		DepoASNEklenecekKokSertifika(const ASN1T_SD_DepoASNEklenecekKokSertifika & iKS);
		DepoASNEklenecekKokSertifika(const QByteArray & iKS);
		DepoASNEklenecekKokSertifika(const DepoASNEklenecekKokSertifika &iKS);

		DepoASNEklenecekKokSertifika & operator=(const DepoASNEklenecekKokSertifika & iKS);
		friend bool operator==(const DepoASNEklenecekKokSertifika & iRHS, const DepoASNEklenecekKokSertifika& iLHS);
		friend bool operator!=(const DepoASNEklenecekKokSertifika& iRHS, const DepoASNEklenecekKokSertifika& iLHS);

		int copyFromASNObject(const ASN1T_SD_DepoASNEklenecekKokSertifika& iKS);
		int copyToASNObject(ASN1T_SD_DepoASNEklenecekKokSertifika & oKS) const;
		void freeASNObject(ASN1T_SD_DepoASNEklenecekKokSertifika& oKS)const;

		virtual ~DepoASNEklenecekKokSertifika(void);

		// GETTERS AND SETTERS

		const QByteArray			& getKokSertifikaValue() const ;
		const QByteArray			& getKokSertifikaHash() const ;
		const SerialNumber			& getKokSerialNumber() const ;
		const Name					& getKokIssuerName() const ;
		const Name					& getKokSubjectName() const ;
		const ETime					& getKokStartDate() const ;
		const ETime					& getKokEndDate() const ;
		const KeyUsage				& getKokKeyUsage() const ;
		const SubjectKeyIdentifier	& getKokSubjectKeyIdentifier() const ;
		const KS_Type				& getKokSertifikaTipi() const ;
		const KGS_Type				& getKokGuvenSeviyesi() const ;


		void setKokSertifikaValue(const QByteArray & );
		void setKokSertifikaHash(const QByteArray & );
		void setKokSerialNumber(const SerialNumber & );
		void setKokIssuerName(const Name & );
		void setKokSubjectName(const Name & );
		void setKokStartDate(const ETime & );
		void setKokEndDate(const ETime & );
		void setKokKeyUsage(const KeyUsage & );
		void setKokSubjectKeyIdentifier(const SubjectKeyIdentifier	& );
		void setKokSertifikaTipi(const KS_Type & );
		void setKokGuvenSeviyesi(const KGS_Type	& ) ;

	};

}

#endif 


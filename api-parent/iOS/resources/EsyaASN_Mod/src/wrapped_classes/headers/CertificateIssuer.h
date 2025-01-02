#ifndef __CERTIFICATEISSUER__
#define __CERTIFICATEISSUER__

#include "GeneralName.h"
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
	class Q_DECL_EXPORT CertificateIssuer  : public EASNWrapperTemplate<ASN1T_IMP_CertificateIssuer,ASN1C_IMP_CertificateIssuer> , public AY_Eklenti
	{
		QList<GeneralName> mList;

	public:
		CertificateIssuer(void);
		CertificateIssuer(const ASN1T_IMP_CertificateIssuer &);
		CertificateIssuer(const QByteArray &);
		CertificateIssuer(const CertificateIssuer&);

		CertificateIssuer & operator=(const CertificateIssuer&);
		friend bool operator==(const CertificateIssuer & iRHS, const CertificateIssuer& iLHS);
		friend bool operator!=(const CertificateIssuer & iRHS, const CertificateIssuer& iLHS);

		int copyFromASNObject(const ASN1T_IMP_CertificateIssuer &);
		int copyToASNObject(ASN1T_IMP_CertificateIssuer& )const;
		void freeASNObject(ASN1T_IMP_CertificateIssuer & )const;

		virtual~CertificateIssuer(void);

		// GETTERS AND SETTERS

		const QList<GeneralName> &getList() const;

		void setList(const QList<GeneralName> & iList);
		void appendGeneralName(const GeneralName& iGN) ;

		virtual QString toString() const;
		QStringList toStringList() const ;

		bool hasIssuer(const Name& iIssuer)const;

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


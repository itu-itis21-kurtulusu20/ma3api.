#ifndef __INHIBITANYPOLICY__
#define __INHIBITANYPOLICY__

#include "EASNWrapperTemplate.h"
#include "Implicit.h"
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
	class Q_DECL_EXPORT InhibitAnyPolicy  : public EASNWrapperTemplate<ASN1T_IMP_InhibitAnyPolicy,ASN1C_IMP_InhibitAnyPolicy> ,public AY_Eklenti
	{
		int mValue;

	public:
		InhibitAnyPolicy(void);
		InhibitAnyPolicy(const QByteArray & );
		InhibitAnyPolicy(const ASN1T_IMP_InhibitAnyPolicy & );
		InhibitAnyPolicy(const InhibitAnyPolicy& );
		InhibitAnyPolicy(const int & );

		InhibitAnyPolicy & operator=(const InhibitAnyPolicy& iInhibitAnyPolicy);
		friend bool operator==( const InhibitAnyPolicy& iRHS, const InhibitAnyPolicy& iLHS);
		friend bool operator!=( const InhibitAnyPolicy& iRHS, const InhibitAnyPolicy& iLHS);

		int copyFromASNObject(const ASN1T_IMP_InhibitAnyPolicy & iInhibitAnyPolicy);
		int copyToASNObject(ASN1T_IMP_InhibitAnyPolicy &oInhibitAnyPolicy)const;
		void freeASNObject(ASN1T_IMP_InhibitAnyPolicy& )const;	

		virtual ~InhibitAnyPolicy(void);

		// GETTERS AND SETTERS

		const int& getValue() const ;

		virtual QString toString()const;

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


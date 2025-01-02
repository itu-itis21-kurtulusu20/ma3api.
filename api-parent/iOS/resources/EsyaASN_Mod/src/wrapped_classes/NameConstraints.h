
#ifndef __NAMECONSTRAINTS__
#define __NAMECONSTRAINTS__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "GeneralSubtree.h"
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
	class Q_DECL_EXPORT NameConstraints  : public EASNWrapperTemplate<ASN1T_IMP_NameConstraints,ASN1C_IMP_NameConstraints>, public AY_Eklenti
	{
		
		bool mPSTPresent ;
		bool mESTPresent ;
	   
		QList<GeneralSubtree> mPST;
		QList<GeneralSubtree> mEST;

	public:

		NameConstraints(const NameConstraints &);
		NameConstraints(const ASN1T_IMP_NameConstraints & );
		NameConstraints(const QByteArray & );
		NameConstraints(void);
		

		NameConstraints& operator=(const NameConstraints&);
		friend bool operator==(const NameConstraints& iRHS, const NameConstraints& iLHS);
		friend bool operator!=(const NameConstraints& iRHS, const NameConstraints& iLHS);


		int copyFromASNObject(const ASN1T_IMP_NameConstraints& iNC);
		int copyToASNObject(ASN1T_IMP_NameConstraints & oNC) const;
		void freeASNObject(ASN1T_IMP_NameConstraints & oNC)const;
		
		virtual ~NameConstraints(void);

		// GETTERS AND SETTERS
			
		const bool &isPSTPresent()const ;
		const bool &isESTPresent()const ;
	   
		const QList<GeneralSubtree> &getPST()const ;
		const QList<GeneralSubtree> &getEST()const ;

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


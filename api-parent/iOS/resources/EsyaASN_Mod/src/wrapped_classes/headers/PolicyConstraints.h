
#ifndef __POLICYCONSTRAINTS__
#define __POLICYCONSTRAINTS__



#include "EException.h"
#include <QByteArray>
#include <QString>
#include "ESeqOfList.h"
#include "GeneralName.h"
#include "ortak.h"
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
	class Q_DECL_EXPORT PolicyConstraints  : public EASNWrapperTemplate<ASN1T_IMP_PolicyConstraints,ASN1C_IMP_PolicyConstraints> , public AY_Eklenti
	{
	  
		bool mREPPresent;
		bool mIPMPresent;

		OSUINT32 mREP;
		OSUINT32 mIPM;

	public:

		PolicyConstraints(const PolicyConstraints & iPC);
		PolicyConstraints(const ASN1T_IMP_PolicyConstraints & iPC);
		PolicyConstraints(const QByteArray & iPC);
		PolicyConstraints(void);
		

		PolicyConstraints& operator=(const PolicyConstraints&);
		friend bool operator==(const PolicyConstraints& iRHS, const PolicyConstraints& iLHS);
		friend bool operator!=(const PolicyConstraints& iRHS, const PolicyConstraints& iLHS);

		int copyFromASNObject(const ASN1T_IMP_PolicyConstraints& iPC);
		int copyToASNObject(ASN1T_IMP_PolicyConstraints & oPC) const;
		void freeASNObject(ASN1T_IMP_PolicyConstraints & oPC)const;

		virtual ~PolicyConstraints(void);

		// GETTERS AND SETTERS

		const bool& isREPPresent() const;
		const bool& isIPMPresent() const;
		const OSUINT32& getREP() const;
		const OSUINT32& getIPM() const;

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


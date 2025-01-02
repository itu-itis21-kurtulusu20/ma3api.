
#ifndef __EXTKEYUSAGE__
#define __EXTKEYUSAGE__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
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
	class ExtKeyUsage  : public EASNWrapperTemplate<ASN1T_IMP_ExtKeyUsageSyntax,ASN1C_IMP_ExtKeyUsageSyntax>, public AY_Eklenti
	{
		
		QList<ASN1TObjId> mKeyPurposeIDs;

	public:

		ExtKeyUsage(const ExtKeyUsage &);
		ExtKeyUsage(const ASN1T_IMP_ExtKeyUsageSyntax & iEKU);
		ExtKeyUsage(const QByteArray & );
		ExtKeyUsage(void);

		ExtKeyUsage & operator=(const ExtKeyUsage&);
		friend bool operator==(const ExtKeyUsage& iRHS,const ExtKeyUsage& iLHS);
		friend bool operator!=(const ExtKeyUsage& iRHS, const ExtKeyUsage& iLHS);


		int copyFromASNObject(const ASN1T_IMP_ExtKeyUsageSyntax & iEKU);
		int copyToASNObject(ASN1T_IMP_ExtKeyUsageSyntax & oEKU) const;
		void freeASNObject(ASN1T_IMP_ExtKeyUsageSyntax & oEKU)const;

		virtual ~ExtKeyUsage(void);

		// GETTERS AND SETTERS

		const QList<ASN1TObjId>& getKeyPurposeIDs() const;

		const bool hasKeyPurposeID(const ASN1TObjId & )const;
		
		void addKeyPurposeID(const ASN1TObjId &);

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


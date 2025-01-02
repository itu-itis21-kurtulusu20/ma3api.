#ifndef __EXTENDEDKEYUSAGE__
#define __EXTENDEDKEYUSAGE__

#include "KeyPurposeId.h"
#include "AY_Eklenti.h"
#include "ortak.h"

namespace esya
{

	class Q_DECL_EXPORT ExtendedKeyUsage  : public EASNWrapperTemplate<ASN1T_IMP_ExtKeyUsageSyntax,ASN1C_IMP_ExtKeyUsageSyntax> , public AY_Eklenti
	{
		QList<KeyPurposeId> mList;

	public:
		ExtendedKeyUsage(void);
		ExtendedKeyUsage(const ASN1T_IMP_ExtKeyUsageSyntax &);
		ExtendedKeyUsage(const QByteArray &);
		ExtendedKeyUsage(const ExtendedKeyUsage&);
		ExtendedKeyUsage(const QList<KeyPurposeId>&);

		ExtendedKeyUsage & operator=(const ExtendedKeyUsage&);
		friend bool operator==(const ExtendedKeyUsage & iRHS, const ExtendedKeyUsage& iLHS);
		friend bool operator!=(const ExtendedKeyUsage & iRHS, const ExtendedKeyUsage& iLHS);

		int copyFromASNObject(const ASN1T_IMP_ExtKeyUsageSyntax &);
		int copyToASNObject(ASN1T_IMP_ExtKeyUsageSyntax& )const;
		void freeASNObject(ASN1T_IMP_ExtKeyUsageSyntax & )const;

		virtual ~ExtendedKeyUsage(void);


		/////////////////////////////////////////////////////////////////

		const QList<KeyPurposeId> &getList() const;

		virtual QString toString() const;

		const bool hasKeyPurposeID(const ASN1TObjId & iKeyPurpose)const;

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


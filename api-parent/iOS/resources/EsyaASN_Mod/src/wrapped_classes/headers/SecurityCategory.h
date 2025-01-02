#ifndef __SECURITYCATEGORY__
#define __SECURITYCATEGORY__


#include "attrcert.h"
#include <QByteArray>
#include "ortak.h"

namespace esya
{
	class Q_DECL_EXPORT SecurityCategory : public EASNWrapperTemplate<ASN1T_ATTRCERT_SecurityCategory,ASN1C_ATTRCERT_SecurityCategory>
	{
		ASN1TObjId	mType;
		QByteArray	mValue;
	
	public:
		SecurityCategory(void);
		SecurityCategory(const QByteArray & iSecurityCategory);
		SecurityCategory(const ASN1T_ATTRCERT_SecurityCategory & iSecurityCategory );
		SecurityCategory(const SecurityCategory& iSecurityCategory);

		SecurityCategory& operator=(const SecurityCategory& iSecurityCategory);
		friend bool operator==( const SecurityCategory& iRHS, const SecurityCategory& iLHS);
		friend bool operator!=( const SecurityCategory& iRHS, const SecurityCategory& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_SecurityCategory & iSecurityCategory);
		int copyToASNObject(ASN1T_ATTRCERT_SecurityCategory &oSecurityCategory)const;
		void freeASNObject(ASN1T_ATTRCERT_SecurityCategory& oSecurityCategory)const;

		int copySecurityCategories(const ASN1T_ATTRCERT__SetOfATTRCERT_SecurityCategory & iSecurityCategories, QList<SecurityCategory>& oList);
		int copySecurityCategories(const QList<SecurityCategory> iList ,ASN1T_ATTRCERT__SetOfATTRCERT_SecurityCategory & oSecurityCategories);	
		int copySecurityCategories(const QByteArray & iASNBytes, QList<SecurityCategory>& oList);

		virtual ~SecurityCategory(void);

		// GETTERS AND SETTERS

		const ASN1TObjId & getType()const;
		const QByteArray & getValue()const;

		void setType(const ASN1TObjId & iType);
		void setValue(const QByteArray & iValue);
	};

}

#endif


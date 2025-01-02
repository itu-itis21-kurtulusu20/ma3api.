#ifndef __IETFATTRSYNTAX__
#define __IETFATTRSYNTAX__


#include "IetfAttrSyntax_values_element.h"
#include "GeneralNames.h"


namespace esya
{

	class Q_DECL_EXPORT IetfAttrSyntax : public EASNWrapperTemplate<ASN1T_ATTRCERT_IetfAttrSyntax,ASN1C_ATTRCERT_IetfAttrSyntax>
	{
		bool mPolicyAuthorityPresent;
		GeneralNames mPolicyAuthority;
		QList<IetfAttrSyntax_values_element> mValues;

	public:
		IetfAttrSyntax(void);
		IetfAttrSyntax(const QByteArray & iIetfAttrSyntax);
		IetfAttrSyntax(const ASN1T_ATTRCERT_IetfAttrSyntax & iIetfAttrSyntax );
		IetfAttrSyntax(const IetfAttrSyntax& iIetfAttrSyntax);

		IetfAttrSyntax& operator=(const IetfAttrSyntax& iIetfAttrSyntax);
		friend bool operator==( const IetfAttrSyntax& iRHS, const IetfAttrSyntax& iLHS);
		friend bool operator!=( const IetfAttrSyntax& iRHS, const IetfAttrSyntax& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_IetfAttrSyntax & iIetfAttrSyntaxr);
		int copyToASNObject(ASN1T_ATTRCERT_IetfAttrSyntax &oIetfAttrSyntax)const;
		void freeASNObject(ASN1T_ATTRCERT_IetfAttrSyntax& oIetfAttrSyntax)const;

		virtual ~IetfAttrSyntax(void);

		// GETTERS AND SETTERS
		
		bool isPolicyAuthorityPresent()const;
		
		const GeneralNames& getPolicyAuthority()const;
		const QList<IetfAttrSyntax_values_element>& getValues()const;

		void setPolicyAuthorityPresent(bool iPAP);

		void setPolicyAuthority(const GeneralNames& iPA);
		void setValues(const QList<IetfAttrSyntax_values_element>& iValues);
		void appendValue(const IetfAttrSyntax_values_element& iValue);

	};

}

#endif


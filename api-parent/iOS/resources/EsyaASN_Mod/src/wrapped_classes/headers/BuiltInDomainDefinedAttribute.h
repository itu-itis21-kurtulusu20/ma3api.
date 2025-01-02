
#ifndef __BUILTINDOMAINDEFINEDATTRIBUTE__
#define __BUILTINDOMAINDEFINEDATTRIBUTE__

#include "Explicit.h"
#include "ortak.h"
#include "EException.h"


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
	class Q_DECL_EXPORT BuiltInDomainDefinedAttribute  : public EASNWrapperTemplate<ASN1T_EXP_BuiltInDomainDefinedAttribute,ASN1C_EXP_BuiltInDomainDefinedAttribute>
	{
		QString mType;
		QString mValue;

	public:

		BuiltInDomainDefinedAttribute(void);
		BuiltInDomainDefinedAttribute( const ASN1T_EXP_BuiltInDomainDefinedAttribute &);
		BuiltInDomainDefinedAttribute( const BuiltInDomainDefinedAttribute &);
		BuiltInDomainDefinedAttribute( const QByteArray &);
		BuiltInDomainDefinedAttribute( const QString &iType, const QString& iValue);

		BuiltInDomainDefinedAttribute & operator=(const BuiltInDomainDefinedAttribute&);

		Q_DECL_EXPORT friend bool operator==(const BuiltInDomainDefinedAttribute& iRHS,const BuiltInDomainDefinedAttribute& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const BuiltInDomainDefinedAttribute& iRHS, const BuiltInDomainDefinedAttribute& iLHS);

		int copyFromASNObject(const ASN1T_EXP_BuiltInDomainDefinedAttribute & iBDDA);
		int copyToASNObject(ASN1T_EXP_BuiltInDomainDefinedAttribute & oBDDA) const;
		void freeASNObject(ASN1T_EXP_BuiltInDomainDefinedAttribute& oBDDA)const;

		virtual ~BuiltInDomainDefinedAttribute(void);

		int copyBDDAs(const ASN1T_EXP_BuiltInDomainDefinedAttributes& iBDDAs, QList<BuiltInDomainDefinedAttribute>& oList)const;
		int copyBDDAs(const QList<BuiltInDomainDefinedAttribute> iList ,ASN1T_EXP_BuiltInDomainDefinedAttributes & oBDDAs)const;	
		int copyBDDAs(const QByteArray & iASNBytes, QList<BuiltInDomainDefinedAttribute>& oList)const;
		int copyBDDAs(const QList<BuiltInDomainDefinedAttribute>& iList , QByteArray & oASNBytes)const;

		void freeBDDAs(ASN1T_EXP_BuiltInDomainDefinedAttributes & oBDDAs)const;



		// GETTERS AND SETTERS

		const QString& getType() const ;	
		const QString& getValue() const ;	

		void setType(const QString & )  ;	
		void setValue(const QString & )  ;	

	};

}

#endif


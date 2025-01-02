
#ifndef __EXTENSIONATTRIBUTE__
#define __EXTENSIONATTRIBUTE__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "Explicit.h"
#include "EASNWrapperTemplate.h"


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
	class Q_DECL_EXPORT  ExtensionAttribute  : public EASNWrapperTemplate<ASN1T_EXP_ExtensionAttribute,ASN1C_EXP_ExtensionAttribute>
	{
		int			mExtensionAttributeType;
		QByteArray	mExtensionAttributeValue;
		
	public:
		ExtensionAttribute();
		ExtensionAttribute(const ExtensionAttribute &);
		ExtensionAttribute(const ASN1T_EXP_ExtensionAttribute & iEA);
		ExtensionAttribute(const QByteArray & );
		ExtensionAttribute(const int & eaType, const QByteArray & eaValue);

		ExtensionAttribute & operator=(const ExtensionAttribute&);
		Q_DECL_EXPORT friend bool operator==(const ExtensionAttribute & iRHS,const ExtensionAttribute & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const ExtensionAttribute & iRHS, const ExtensionAttribute & iLHS);


		int copyFromASNObject(const ASN1T_EXP_ExtensionAttribute& iEA);
		int copyToASNObject(ASN1T_EXP_ExtensionAttribute & oEA) const;
		void freeASNObject(ASN1T_EXP_ExtensionAttribute & oEA)const;


		int copyExtensionAttributes(const ASN1T_EXP_ExtensionAttributes & iEAs, QList<ExtensionAttribute>& oList)const;
		int copyExtensionAttributes(const QList<ExtensionAttribute> iList ,ASN1T_EXP_ExtensionAttributes & oEAs)const;	
		int copyExtensionAttributes(const QByteArray & iASNBytes, QList<ExtensionAttribute>& oList)const;
		int copyExtensionAttributes(const QList<ExtensionAttribute>& iList , QByteArray & oASNBytes)const;
		
		void freeExtensionAttributes(ASN1T_EXP_ExtensionAttributes & oEAs)const;


		void setExtensionAttributeValue(const QByteArray& );
		void setExtensionAttributeType(const int& ) ;

		const QByteArray&  getExtensionAttributeValue()const;
		int getExtensionAttributeType()const ;

	public:
		virtual ~ExtensionAttribute(void);
	};

}

#endif



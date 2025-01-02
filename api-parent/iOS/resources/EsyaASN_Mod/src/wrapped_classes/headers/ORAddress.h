
#ifndef __ORADDRESS__
#define __ORADDRESS__

#include "BuiltInDomainDefinedAttribute.h"	
#include "BuiltInStandardAttributes.h"	
#include "ExtensionAttribute.h"	

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
	class Q_DECL_EXPORT ORAddress  : public EASNWrapperTemplate<ASN1T_EXP_ORAddress,ASN1C_EXP_ORAddress>
	{
		bool mBuiltInDomainDefinedAttributesPresent;
		bool mExtensionAttributesPresent;

		BuiltInStandardAttributes				mBuiltInStandardAttributes;
		QList<BuiltInDomainDefinedAttribute>	mBuiltInDomainDefinedAttributes;
		QList<ExtensionAttribute>				mExtensionAttributes;		

	public:

		ORAddress(void);
		ORAddress( const ASN1T_EXP_ORAddress &);
		ORAddress( const ORAddress &);
		ORAddress( const QByteArray &);

		ORAddress & operator=(const ORAddress&);

		Q_DECL_EXPORT friend bool operator==(const ORAddress& iRHS,const ORAddress& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const ORAddress& iRHS, const ORAddress& iLHS);

		int copyFromASNObject(const ASN1T_EXP_ORAddress & iBSA);
		int copyToASNObject(ASN1T_EXP_ORAddress & oBSA) const;
		void freeASNObject(ASN1T_EXP_ORAddress& oBSA)const;

		virtual ~ORAddress(void);


		// GETTERS AND SETTERS

		bool isBuiltInDomainDefinedAttributesPresent()const;
		bool isExtensionAttributesPresent()const;

		const BuiltInStandardAttributes& 			getBuiltInStandardAttributes()const;
		const QList<BuiltInDomainDefinedAttribute>&	getBuiltInDomainDefinedAttributes()const;
		const QList<ExtensionAttribute>&			getExtensionAttributes()const;

		void setBuiltInStandardAttributes(const BuiltInStandardAttributes& );
		void setBuiltInDomainDefinedAttributes(const QList<BuiltInDomainDefinedAttribute>&	);
		void setExtensionAttributes(const QList<ExtensionAttribute>& );


	};

}

#endif


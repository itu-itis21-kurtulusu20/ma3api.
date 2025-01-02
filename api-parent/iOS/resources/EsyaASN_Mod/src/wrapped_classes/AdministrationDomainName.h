
#ifndef __ADMINISTRATIONDOMAINNAME__
#define __ADMINISTRATIONDOMAINNAME__

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
	class Q_DECL_EXPORT AdministrationDomainName  : public EASNWrapperTemplate<ASN1T_EXP_AdministrationDomainName ,ASN1C_EXP_AdministrationDomainName>
	{
	public:
		enum AdministrationDomainNameType { ADNT_NUMERIC = T_EXP_AdministrationDomainName_numeric , ADNT_PRINTABLE = T_EXP_AdministrationDomainName_printable};

	protected:
		AdministrationDomainNameType mType;
		QString mCode;

	public:

		AdministrationDomainName(void);
		AdministrationDomainName( const ASN1T_EXP_AdministrationDomainName &);
		AdministrationDomainName( const AdministrationDomainName &);
		AdministrationDomainName( const QByteArray &);
		AdministrationDomainName( const AdministrationDomainNameType &iType, const QString& iValue);

		AdministrationDomainName & operator=(const AdministrationDomainName&);

		Q_DECL_EXPORT friend bool operator==(const AdministrationDomainName& iRHS,const AdministrationDomainName& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const AdministrationDomainName& iRHS, const AdministrationDomainName& iLHS);

		int copyFromASNObject(const ASN1T_EXP_AdministrationDomainName & iADN);
		int copyToASNObject(ASN1T_EXP_AdministrationDomainName & oADN) const;
		void freeASNObject(ASN1T_EXP_AdministrationDomainName& oADN)const;

		virtual ~AdministrationDomainName(void);

		// GETTERS AND SETTERS

		const AdministrationDomainNameType& getType() const ;	
		const QString& getCode() const ;	

		void setType(const AdministrationDomainNameType& )  ;	
		void setCode(const QString & )  ;	

	};

}

#endif


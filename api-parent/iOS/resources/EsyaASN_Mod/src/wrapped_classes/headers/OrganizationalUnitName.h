
#ifndef __ORGANIZATIONALUNITNAME__
#define __ORGANIZATIONALUNITNAME__

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
	class Q_DECL_EXPORT OrganizationalUnitName  : public EASNWrapperTemplate<ASN1T_EXP_OrganizationalUnitName,ASN1C_EXP_OrganizationalUnitName>
	{
		QString mValue;

	public:

		OrganizationalUnitName(void);
		OrganizationalUnitName( const ASN1T_EXP_OrganizationalUnitName &);
		OrganizationalUnitName( const OrganizationalUnitName &);
		OrganizationalUnitName( const QByteArray &);
		OrganizationalUnitName( const QString &);

		OrganizationalUnitName& operator=(const OrganizationalUnitName&);

		Q_DECL_EXPORT friend bool operator==(const OrganizationalUnitName& iRHS,const OrganizationalUnitName& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const OrganizationalUnitName& iRHS, const OrganizationalUnitName& iLHS);

		bool operator<(const OrganizationalUnitName& iRHS)const;		
		
		int copyFromASNObject(const ASN1T_EXP_OrganizationalUnitName & iSN);
		int copyToASNObject(ASN1T_EXP_OrganizationalUnitName & oECI) const;
		void freeASNObject(ASN1T_EXP_OrganizationalUnitName& oSN)const;

		int copyOUNs(const ASN1T_EXP_OrganizationalUnitNames & iOUNs, QList<OrganizationalUnitName>& oList)const;
		int copyOUNs(const QList<OrganizationalUnitName> iList ,ASN1T_EXP_OrganizationalUnitNames & oOUNs)const;	
		int copyOUNs(const QByteArray & iASNBytes, QList<OrganizationalUnitName>& oList)const;
		int copyOUNs(const QList<OrganizationalUnitName>& iList , QByteArray & oASNBytes)const;

		void freeOUNs(ASN1T_EXP_OrganizationalUnitNames & oOUNs)const;



		virtual ~OrganizationalUnitName(void);

		// GETTERS AND SETTERS

		QString getValue() const ;	

		void setValue(const QString & )  ;	

	};

}

#endif


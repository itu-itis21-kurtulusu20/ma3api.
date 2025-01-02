
#ifndef __AUTHORITYKEYIDENTIFIER__
#define __AUTHORITYKEYIDENTIFIER__



#include "EException.h"
#include "ortak.h"
#include "ESeqOfList.h"
#include "myerrors.h"
#include "GeneralName.h"
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
	class Q_DECL_EXPORT AuthorityKeyIdentifier : public EASNWrapperTemplate<ASN1T_IMP_AuthorityKeyIdentifier,ASN1C_IMP_AuthorityKeyIdentifier>, public AY_Eklenti
	{
		bool mKeyIdentifierPresent ;
		bool mAuthorityCertIssuerPresent ;
		bool mAuthorityCertSerialNumberPresent ;

		QByteArray			mKeyIdentifier;
		QList<GeneralName>	mAuthorityCertIssuer;
		QString				mAuthorityCertSerialNumber;

	public:

		AuthorityKeyIdentifier(const AuthorityKeyIdentifier &);
		AuthorityKeyIdentifier(const ASN1T_IMP_AuthorityKeyIdentifier & );
		AuthorityKeyIdentifier(const QByteArray & );
		AuthorityKeyIdentifier(void);
		

		AuthorityKeyIdentifier & operator=(const AuthorityKeyIdentifier&);
		Q_DECL_EXPORT friend bool operator==(const AuthorityKeyIdentifier& iRHS, const AuthorityKeyIdentifier& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const AuthorityKeyIdentifier& iRHS, const AuthorityKeyIdentifier& iLHS);


		int copyFromASNObject(const ASN1T_IMP_AuthorityKeyIdentifier& iAKI);
		int copyToASNObject(ASN1T_IMP_AuthorityKeyIdentifier & oAKI) const;
		void freeASNObject(ASN1T_IMP_AuthorityKeyIdentifier & oAKI)const;

		virtual ~AuthorityKeyIdentifier(void);

		// GETTERS AND SETTERS

		const bool & isKeyIdentifierPresent()const ;
		const bool & isAuthorityCertIssuerPresent()const ;
		const bool & isAuthorityCertSerialNumberPresent()const ;

		const QByteArray &			getKeyIdentifier()const;
		const QList<GeneralName> & 	getAuthorityCertIssuer()const;
		const QString &				getAuthorityCertSerialNumber()const;

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


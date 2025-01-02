
#ifndef __SIGNERIDENTIFIER__
#define __SIGNERIDENTIFIER__


#include "cms.h"
//#include <QByteArray>
//#include <QString>
#include "ortak.h"
#include "myerrors.h"
#include "ECertificate.h"


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
	class Q_DECL_EXPORT SignerIdentifier  : public EASNWrapperTemplate<ASN1T_CMS_SignerIdentifier,ASN1C_CMS_SignerIdentifier>
	{
	public:
		enum SignerIdentifierType { issuerAndSerialNumber =  T_CMS_SignerIdentifier_issuerAndSerialNumber , subjectKeyIdentifier = T_CMS_SignerIdentifier_subjectKeyIdentifier };
	
	protected:
		SignerIdentifierType mType;

		Name			mIssuer;
		SerialNumber	mSerialNumber;
		QByteArray		mSubjectKeyIdentifier;
		


	public:

		SignerIdentifier(const SignerIdentifier &);
		SignerIdentifier(const ASN1T_CMS_SignerIdentifier & );
		SignerIdentifier(const QByteArray & );
		SignerIdentifier(void);
		SignerIdentifier(const Name & , const SerialNumber &);
		SignerIdentifier(const ECertificate & , const SignerIdentifierType &);


		SignerIdentifier & operator=(const SignerIdentifier&);
		Q_DECL_EXPORT friend bool operator==(const SignerIdentifier & iRHS, const SignerIdentifier& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const SignerIdentifier & iRHS, const SignerIdentifier& iLHS);

		int copyFromASNObject(const ASN1T_CMS_SignerIdentifier & );
		int copyToASNObject(ASN1T_CMS_SignerIdentifier &)  const;
		void freeASNObject(ASN1T_CMS_SignerIdentifier & oExtension)const;

		virtual ~SignerIdentifier(void);

		// GETTERS AND SETTERS

		const SignerIdentifierType& getType() const;
		const Name& getIssuer() const;
		const SerialNumber & getSerialNumber() const ;
		const QByteArray & getSubjectKeyIdentifier() const ;

		virtual QString toString()const;

		bool isMatch(const ECertificate & )const;
	};

}

#endif


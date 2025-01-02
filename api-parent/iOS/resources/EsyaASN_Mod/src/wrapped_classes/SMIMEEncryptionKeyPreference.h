#ifndef __SMIMEENCRYPTIONKEYPREFERENCES__
#define __SMIMEENCRYPTIONKEYPREFERENCES__

#include "RecipientKeyIdentifier.h"
#include "SubjectKeyIdentifier.h"
#include "IssuerAndSerialNumber.h"

namespace esya
{

	enum EKP_Type {	EKP_IssuerAndSerialNumber	= T_CMS_SMIMEEncryptionKeyPreference_issuerAndSerialNumber, 
					EKP_RecipientKeyID			= T_CMS_SMIMEEncryptionKeyPreference_receipentKeyId , 
					EKP_SubjectAltKeyIdentifier = T_CMS_SMIMEEncryptionKeyPreference_subjectAltKeyIdentifier };


	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT SMIMEEncryptionKeyPreference : public EASNWrapperTemplate<ASN1T_CMS_SMIMEEncryptionKeyPreference,ASN1C_CMS_SMIMEEncryptionKeyPreference>
	{
		EKP_Type mType;

		SubjectKeyIdentifier*	mSAKI;
		IssuerAndSerialNumber*	mISA;
		RecipientKeyIdentifier*	mRKI;

	public:
		SMIMEEncryptionKeyPreference(void);
		SMIMEEncryptionKeyPreference(const QByteArray & iEKP);
		SMIMEEncryptionKeyPreference(const ASN1T_CMS_SMIMEEncryptionKeyPreference & iEKP);
		SMIMEEncryptionKeyPreference(const SMIMEEncryptionKeyPreference& iEKP);

		SMIMEEncryptionKeyPreference& operator=(const SMIMEEncryptionKeyPreference& iEKP);
		friend bool operator==(const SMIMEEncryptionKeyPreference & iRHS, const SMIMEEncryptionKeyPreference& iLHS);
		friend bool operator!=(const SMIMEEncryptionKeyPreference & iRHS, const SMIMEEncryptionKeyPreference& iLHS);

		int copyFromASNObject(const ASN1T_CMS_SMIMEEncryptionKeyPreference & ) ;
		int copyToASNObject(ASN1T_CMS_SMIMEEncryptionKeyPreference & oEKP) const;
		void freeASNObject(ASN1T_CMS_SMIMEEncryptionKeyPreference & oEKP)const;

		virtual ~SMIMEEncryptionKeyPreference(void);

		// GETTERS AND SETTERS

		const EKP_Type&					getType()const;
		const SubjectKeyIdentifier*		getSAKI() const ;
		const IssuerAndSerialNumber*	getISA() const ;
		const RecipientKeyIdentifier*	getRKI() const ;

		void setType(const EKP_Type & ); 
		void setSAKI(SubjectKeyIdentifier* );
		void setISA(IssuerAndSerialNumber* );
		void setRKI(RecipientKeyIdentifier* );		
	};

}

#endif

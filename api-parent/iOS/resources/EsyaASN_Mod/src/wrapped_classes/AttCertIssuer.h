#ifndef __ATTCERTISSUER__
#define __ATTCERTISSUER__


#include "V2Form.h"


namespace esya
{

	class Q_DECL_EXPORT AttCertIssuer : public EASNWrapperTemplate<ASN1T_ATTRCERT_AttCertIssuer,ASN1C_ATTRCERT_AttCertIssuer>
	{
	public:
		enum CertIssuerType 
		{
			CIT_V1Form = T_ATTRCERT_AttCertIssuer_v1Form ,
			CIT_V2Form = T_ATTRCERT_AttCertIssuer_v2Form 
		};

	protected:
		CertIssuerType	mType;
		GeneralNames	mV1Form;
		V2Form			mV2Form;

	public:
		AttCertIssuer(void);
		AttCertIssuer(const QByteArray & iAttCertIssuer);
		AttCertIssuer(const ASN1T_ATTRCERT_AttCertIssuer & iAttCertIssuer );
		AttCertIssuer(const AttCertIssuer& iAttCertIssuerm);

		AttCertIssuer& operator=(const AttCertIssuer& iAttCertIssuer);
		friend bool operator==( const AttCertIssuer& iRHS, const AttCertIssuer& iLHS);
		friend bool operator!=( const AttCertIssuer& iRHS, const AttCertIssuer& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AttCertIssuer & iAttCertIssuer);
		int copyToASNObject(ASN1T_ATTRCERT_AttCertIssuer &oAttCertIssuerm)const;
		void freeASNObject(ASN1T_ATTRCERT_AttCertIssuer& oAttCertIssuer)const;

		virtual ~AttCertIssuer(void);

		// GETTERS AND SETTERS

		const CertIssuerType &	getType()const ;
		const GeneralNames& getV1Form()const ;
		const V2Form& getV2Form()const ;
	
		void setType(const CertIssuerType &	iType);
		void setV1Form(const GeneralNames& iV1);
		void setV2Form(const V2Form& iV2);

	};

}

#endif


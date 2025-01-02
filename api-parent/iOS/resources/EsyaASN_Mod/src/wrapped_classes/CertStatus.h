
#ifndef __CERTSTATUS__
#define __CERTSTATUS__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "RevokedInfo.h"

namespace esya
{

	#define ST_CS_GOOD		"Geçerli" 
	#define ST_CS_REVOKED	"Ýptal Edilmiþ" 
	#define ST_CS_UNKNOWN	"Bilinmiyor" 

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT CertStatus : public EASNWrapperTemplate<ASN1T_OCSP_CertStatus,ASN1C_OCSP_CertStatus>
	{

	public:
			
		enum CS_Type { CS_Good = T_OCSP_CertStatus_good , CS_Revoked = T_OCSP_CertStatus_revoked, CS_Unknown = T_OCSP_CertStatus_unknown };

	private:
		CS_Type			mType;
		RevokedInfo*	pRI;

	public:


		CertStatus(const CertStatus &iCS);
		CertStatus(const ASN1T_OCSP_CertStatus & iCS);
		CertStatus(const QByteArray & iCS);
		CertStatus(void);
		
		CertStatus & operator=(const CertStatus & iCS);
		Q_DECL_EXPORT friend bool operator==(const CertStatus & iRHS, const CertStatus & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const CertStatus & iRHS, const CertStatus & iLHS);


		int copyFromASNObject(const ASN1T_OCSP_CertStatus& iCS);
		int copyToASNObject(ASN1T_OCSP_CertStatus & oCS) const;
		void freeASNObject(ASN1T_OCSP_CertStatus& oCS)const;

		virtual~CertStatus(void);

		// GETTERS AND SETTERS

		const CertStatus::CS_Type& getType() const;
		const RevokedInfo* getRevokedInfo() const;

		void setType(const CS_Type & iType);
		void setRevokedInfo( RevokedInfo* iRI);	

		virtual QString toString()const;
	};

}

#endif


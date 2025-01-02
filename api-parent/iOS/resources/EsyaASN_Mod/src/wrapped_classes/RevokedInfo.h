
#ifndef __REVOKEDINFO__
#define __REVOKEDINFO__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "ocsp.h"

namespace esya
{

	class Q_DECL_EXPORT RevokedInfo  : public EASNWrapperTemplate<ASN1T_OCSP_RevokedInfo,ASN1C_OCSP_RevokedInfo>
	{
		bool		mRevocationReasonPresent;
		QString		mRevocationTime;
		OSUINT32	mRevocationReason;
		

	public:

		RevokedInfo(const RevokedInfo &iRI);
		RevokedInfo(const ASN1T_OCSP_RevokedInfo & iRI);
		RevokedInfo(const QByteArray & iRI);
		RevokedInfo(void);
		

		RevokedInfo & operator=(const RevokedInfo&iRI);
		Q_DECL_EXPORT friend bool operator==(const RevokedInfo & iRHS, const RevokedInfo & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const RevokedInfo & iRHS, const RevokedInfo & iLHS);


		int copyFromASNObject(const ASN1T_OCSP_RevokedInfo& iRI);
		int copyToASNObject(ASN1T_OCSP_RevokedInfo & oRI) const;
		void freeASNObject(ASN1T_OCSP_RevokedInfo& oRI)const;

		virtual ~RevokedInfo(void);

		// GETTERS AND SETTERS

		const bool& isRevocationReasonPresent() const;
		const OSUINT32& getRevocationReason() const;
		const QString& getRevocationTime() const ;

		void setRevocationReasonPresent(const bool & iRRP);
		void setRevocationReason(const OSUINT32 & iRR);	
		void setRevocationTime(const QString & iISN);
	};

}

#endif


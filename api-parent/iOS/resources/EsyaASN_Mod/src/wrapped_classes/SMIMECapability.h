#ifndef __SMIMECAPABILITY__
#define __SMIMECAPABILITY__

#include "cms.h"
#include "EException.h"
#include "ortak.h"

namespace esya
{


	class SMIMECapabilities;

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT SMIMECapability : public EASNWrapperTemplate<ASN1T_CMS_SMIMECapability,ASN1C_CMS_SMIMECapability>
	{
		bool mParametersPresent;

		QByteArray mParameters;
		ASN1TObjId mCapabilityID;


	public:
		SMIMECapability(void);
		SMIMECapability(const QByteArray & );
		SMIMECapability(const QByteArray & , const ASN1TObjId &);
		SMIMECapability(ASN1TObjId & );
		SMIMECapability(const ASN1T_CMS_SMIMECapability & );
		SMIMECapability(const SMIMECapability& );

		SMIMECapability& operator=(const SMIMECapability& );
		friend bool operator==(const SMIMECapability & iRHS, const SMIMECapability& iLHS);
		friend bool operator!=(const SMIMECapability & iRHS, const SMIMECapability& iLHS);

		int copyFromASNObject(const ASN1T_CMS_SMIMECapability & ) ;
		int copyToASNObject(ASN1T_CMS_SMIMECapability & oSMIMECapability) const;
		void freeASNObject(ASN1T_CMS_SMIMECapability & oSMIMECapability)const;

		int copyCapabilities(const ASN1TPDUSeqOfList & iCapabilities, QList<SMIMECapability>& oList);
		int copyCapabilities(const QList<SMIMECapability> iList , ASN1TPDUSeqOfList& oCapabilities);	

		virtual ~SMIMECapability(void);

		// GETTERS AND SETTERS

		bool isParametersPresent()const;

		const ASN1TObjId&  getCapabilityID() const ;
		const QByteArray&  getParameters()const ;

		void setParametersPresent(bool);		
		void setCapabilityID(const ASN1TObjId& );
		void setParameters(const QByteArray& );
	};

}

#endif

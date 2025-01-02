#ifndef __OTHERRECIPIENTINFO__
#define __OTHERRECIPIENTINFO__

#include "cms.h"
#include "AlgorithmIdentifier.h"
#include "EASNWrapperTemplate.h"

namespace esya
{
	class Q_DECL_EXPORT OtherRecipientInfo : public EASNWrapperTemplate<ASN1T_CMS_OtherRecipientInfo,ASN1C_CMS_OtherRecipientInfo>
	{
		ASN1TObjId	mORIType;
		QByteArray	mORIValue;

	public:
		OtherRecipientInfo(void);
		OtherRecipientInfo(const QByteArray & iORI);
		OtherRecipientInfo(const ASN1T_CMS_OtherRecipientInfo & iORI);
		OtherRecipientInfo(const ASN1TObjId ,const QByteArray & iORIValue);
		OtherRecipientInfo(const OtherRecipientInfo& iORI);

		OtherRecipientInfo & operator=(const OtherRecipientInfo & iORI);
		friend bool operator==(const OtherRecipientInfo & ,const OtherRecipientInfo & );
		friend bool operator!=(const OtherRecipientInfo & ,const OtherRecipientInfo & );

		int copyFromASNObject(const ASN1T_CMS_OtherRecipientInfo & iORI);
		int copyToASNObject(ASN1T_CMS_OtherRecipientInfo& oKTRI)const;
		void freeASNObject(ASN1T_CMS_OtherRecipientInfo & oORI)const;

		const QByteArray	& getORIValue()	const;
		const ASN1TObjId	& getORIType()	const;

		void setORIValue(const QByteArray  &iOriValue);
		void setORIType(ASN1TObjId iOriType);

		virtual ~OtherRecipientInfo(void);
	};
}

#endif 


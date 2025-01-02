#ifndef __RECIPIENTINFO__
#define __RECIPIENTINFO__

#include "KeyTransRecipientInfo.h"
#include "KeyAgreeRecipientInfo.h"
#include "PasswordRecipientInfo.h"
#include "OtherRecipientInfo.h"

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
	class Q_DECL_EXPORT RecipientInfo  : public EASNWrapperTemplate<ASN1T_CMS_RecipientInfo,ASN1C_CMS_RecipientInfo>
	{
	public:
		enum RInfoType 
		{
			T_KTRI	= 	T_CMS_RecipientInfo_ktri,
			T_KARI	=	T_CMS_RecipientInfo_kari,
			T_KEKRI	=	T_CMS_RecipientInfo_kekri,
			T_PWRI	=	T_CMS_RecipientInfo_pwri,
			T_ORI	=	T_CMS_RecipientInfo_ori
		};

	protected:
		RInfoType				mType;
		KeyTransRecipientInfo	mKTRI;
		KeyAgreeRecipientInfo	mKARI;
		PasswordRecipientInfo	mPWRI;
		OtherRecipientInfo		mORI;

	public:

		RecipientInfo(void);
		RecipientInfo( const ASN1T_CMS_RecipientInfo &iRI);
		RecipientInfo( const QByteArray &iRI);
		RecipientInfo( const RecipientInfo& iRI);
		RecipientInfo( const KeyTransRecipientInfo& iKTRI);
		RecipientInfo( const KeyAgreeRecipientInfo& iKARI);
		RecipientInfo( const PasswordRecipientInfo& iPWRI);
		RecipientInfo( const OtherRecipientInfo& iORI);


		RecipientInfo & operator=(const RecipientInfo & iRI);
		friend bool operator==(const RecipientInfo & ,const RecipientInfo & );
		friend bool operator!=(const RecipientInfo & ,const RecipientInfo & );

		int copyFromASNObject(const ASN1T_CMS_RecipientInfo & iRI);
		int copyToASNObject(ASN1T_CMS_RecipientInfo & oRI)const;
		void freeASNObject(ASN1T_CMS_RecipientInfo & oRI)const;

		int copyRIs(const ASN1T_CMS_RecipientInfos & iRIs, QList<RecipientInfo>& oList);
		int copyRIs(const QList<RecipientInfo> & iList ,ASN1T_CMS_RecipientInfos& oRIs);	
		int copyRIs(const QByteArray & iASNBytes ,QList<RecipientInfo>& oList);	
		int copyRIs(const QList<RecipientInfo>& oList, QByteArray & oASNBytes );	

		const KeyAgreeRecipientInfo	& getKARI()const;
		const KeyTransRecipientInfo	& getKTRI()const;
		const PasswordRecipientInfo	& getPWRI()const;
		const OtherRecipientInfo	& getORI()const;
		const RInfoType				& getType()const;

		void setKARI(const KeyAgreeRecipientInfo & iKTRI );
		void setKTRI(const KeyTransRecipientInfo & iKTRI );
		void setPWRI(const PasswordRecipientInfo & iPWRI );
		void setORI(const OtherRecipientInfo & iORI );

		virtual ~RecipientInfo(void);
	};

}

#endif


#ifndef __RECIPIENTKEYIDENTIFIER__
#define __RECIPIENTKEYIDENTIFIER__

#include "OtherKeyAttribute.h"
#include "SubjectKeyIdentifier.h"


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
	class Q_DECL_EXPORT RecipientKeyIdentifier : public EASNWrapperTemplate<ASN1T_CMS_RecipientKeyIdentifier,ASN1C_CMS_RecipientKeyIdentifier>
	{
		bool mDatePresent ;
		bool mOtherPresent ;

		SubjectKeyIdentifier	mSKI;
		QString					mDate;
		OtherKeyAttribute		mOther;


	public:
		RecipientKeyIdentifier(void);
		RecipientKeyIdentifier(const QByteArray & );
		RecipientKeyIdentifier(const ASN1T_CMS_RecipientKeyIdentifier & );
		RecipientKeyIdentifier(const RecipientKeyIdentifier& );

		RecipientKeyIdentifier& operator=(const RecipientKeyIdentifier& );
		friend bool operator==(const RecipientKeyIdentifier & iRHS, const RecipientKeyIdentifier& iLHS);
		friend bool operator!=(const RecipientKeyIdentifier & iRHS, const RecipientKeyIdentifier& iLHS);

		int copyFromASNObject(const ASN1T_CMS_RecipientKeyIdentifier & ) ;
		int copyToASNObject(ASN1T_CMS_RecipientKeyIdentifier & oRKI) const;
		void freeASNObject(ASN1T_CMS_RecipientKeyIdentifier & oRKI)const;

		virtual ~RecipientKeyIdentifier(void);

		// GETTERS AND SETTERS

		bool isDatePresent()const;
		bool isOtherPresent()const;

		const SubjectKeyIdentifier&		getSKI() const ;
		const QString&					getDate() const ;
		const OtherKeyAttribute&		getOther() const ;

		void setDatePresent(bool);
		void setOtherPresent(bool);
		
		void setSKI(const SubjectKeyIdentifier& );		
		void setDate(const QString& );
		void setOther(const OtherKeyAttribute& );
	};

}

#endif

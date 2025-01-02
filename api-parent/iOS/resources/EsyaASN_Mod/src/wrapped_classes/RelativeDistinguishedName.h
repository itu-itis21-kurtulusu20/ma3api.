#ifndef __RELATIVEDISTINGUISHEDNAME__
#define __RELATIVEDISTINGUISHEDNAME__


#include "Explicit.h"
#include "ESeqOfList.h"
#include "AttributeTypeAndValue.h"

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
	class Q_DECL_EXPORT RelativeDistinguishedName  : public EASNWrapperTemplate<ASN1T_EXP_RelativeDistinguishedName,ASN1C_EXP_RelativeDistinguishedName>
	{
		QList<AttributeTypeAndValue> mList;

	public:
		RelativeDistinguishedName(void);
		RelativeDistinguishedName(const ASN1T_EXP_RelativeDistinguishedName &);
		RelativeDistinguishedName(const QByteArray &);
		RelativeDistinguishedName(const RelativeDistinguishedName&);

		RelativeDistinguishedName & operator=(const RelativeDistinguishedName&);
		friend bool operator==(const RelativeDistinguishedName & ,const RelativeDistinguishedName & );
		friend bool operator!=(const RelativeDistinguishedName & iRHS, const RelativeDistinguishedName & iLHS);

		int copyFromASNObject(const ASN1T_EXP_RelativeDistinguishedName&);
		int copyToASNObject(ASN1T_EXP_RelativeDistinguishedName&) const ;
		void freeASNObject(ASN1T_EXP_RelativeDistinguishedName & oRDN)const;

		virtual ~RelativeDistinguishedName(void);

		// GETTERS AND SETTERS

		const QList<AttributeTypeAndValue> &getList() const;
		void setList(const QList<AttributeTypeAndValue> &);

		QString toString(bool iNormalized = false)const;

		QString getEmailAttribute()const;
		QString getTitleAttribute()const;
	};

}

#endif


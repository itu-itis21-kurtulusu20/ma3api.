#ifndef __NAME__
#define __NAME__

#include "RelativeDistinguishedName.h"


#define NAMEFILTER_CN				"cn="
#define NAMEFILTER_SN				"sn="
#define NAMEFILTER_O				"o="
#define NAMEFILTER_OU				"ou="
#define NAMEFILTER_SERIALNUMBER		"serialnumber="



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
	class Q_DECL_EXPORT Name  : public EASNWrapperTemplate<ASN1T_EXP_Name,ASN1C_EXP_Name>
	{
		QList<RelativeDistinguishedName> mList;

	public:
		Name(void);
		Name(const ASN1T_EXP_Name &);
		Name(const QByteArray &);
		Name(const QList<RelativeDistinguishedName>& iList);
		Name(const Name&);

		Name & operator=(const Name&);
		Q_DECL_EXPORT friend bool operator==(const Name & ,const Name & );
		Q_DECL_EXPORT friend bool operator!=(const Name & iRHS, const Name & iLHS);

		int copyFromASNObject(const ASN1T_EXP_Name&);
		int copyToASNObject(ASN1T_EXP_Name& )const;
		void freeASNObject(ASN1T_EXP_Name & )const;

		virtual ~Name(void);

		// GETTERS AND SETTERS

		const QList<RelativeDistinguishedName> &getList() const;

		void setList(const QList<RelativeDistinguishedName> &) ;
		void appendRDN(const RelativeDistinguishedName & iRDN);

		virtual QString toString(bool iNormalized = false) const;
		QStringList toStringList() const;

		QString toTitle() const;
		QString toTitle(const QString &iFilter) const;

		void fromCommonName(const QString & );
		void addTitle(const QString & iTitle);

		bool isSubNameOf(const Name & )const;

		QString getEmailAttribute()const;
		QString getTitleAttribute()const;
	};

}

#endif


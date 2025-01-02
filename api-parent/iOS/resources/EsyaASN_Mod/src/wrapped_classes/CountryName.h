
#ifndef __COUNTRYNAME__
#define __COUNTRYNAME__

#include "Explicit.h"
#include "ortak.h"
#include "EException.h"


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
	class Q_DECL_EXPORT CountryName  : public EASNWrapperTemplate<ASN1T_EXP_CountryName,ASN1C_EXP_CountryName>
	{
	public:
		enum CountryNameType { CNT_X21DCC = T_EXP_CountryName_x121_dcc_code , CNT_ISO3166ALPHA2 = T_EXP_CountryName_iso_3166_alpha2_code};

	protected:
		CountryNameType mType;
		QString mCode;

	public:

		CountryName(void);
		CountryName( const ASN1T_EXP_CountryName &);
		CountryName( const CountryName &);
		CountryName( const QByteArray &);
		CountryName( const CountryNameType &iType, const QString& iValue);

		CountryName & operator=(const CountryName&);

		Q_DECL_EXPORT friend bool operator==(const CountryName& iRHS,const CountryName& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const CountryName& iRHS, const CountryName& iLHS);

		int copyFromASNObject(const ASN1T_EXP_CountryName & iCN);
		int copyToASNObject(ASN1T_EXP_CountryName & oCN) const;
		void freeASNObject(ASN1T_EXP_CountryName& oCN)const;

		virtual ~CountryName(void);

		// GETTERS AND SETTERS

		const CountryNameType& getType() const ;	
		const QString& getCode() const ;	

		void setType(const CountryNameType& )  ;	
		void setCode(const QString & )  ;	

	};

}

#endif


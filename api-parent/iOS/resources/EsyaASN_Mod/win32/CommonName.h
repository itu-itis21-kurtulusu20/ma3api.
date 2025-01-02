#ifndef __COMMONNAME__
#define __COMMONNAME__


#include "ortak.h"
#include "Explicit.h"

#define NULL_PARAMS "\5\0"

namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author akif.ersoy
	*
	*/
	class Q_DECL_EXPORT CommonName : public EASNWrapperTemplate<ASN1T_EXP_CommonName,ASN1C_EXP_CommonName>
	{
		QString mCommonName;


	public:
		CommonName();
		CommonName(const QString & );
		CommonName(const ASN1T_EXP_CommonName & iCommonName);
		CommonName(const CommonName& iCommonName);
		CommonName(const QByteArray & iCommonName);

		CommonName& operator=(const CommonName& );
		Q_DECL_EXPORT friend bool operator==(const CommonName & iRHS, const CommonName& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const CommonName & iRHS, const CommonName& iLHS);

		int copyFromASNObject(const ASN1T_EXP_CommonName & ) ;
		int copyToASNObject(ASN1T_EXP_CommonName & oCommonName) const;
		void freeASNObject(ASN1T_EXP_CommonName & oCommonName)const;

		virtual ~CommonName(void);

		// GETTERS AND SETTERS

		const QString &getCommonName()const ;

		void setCommonName(const QString& );

		bool isNull() const;
	};

}

#endif  //__COMMONNAME__

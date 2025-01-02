#ifndef __PKCS7DATA__
#define __PKCS7DATA__


#include "pkcs12.h"
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
	class Q_DECL_EXPORT PKCS7Data  : public EASNWrapperTemplate<ASN1T_PKCS7_Data,ASN1C_PKCS7_Data>
	{
		QByteArray mData;
	public:
		PKCS7Data(void);
		PKCS7Data(const QByteArray &iData);
		PKCS7Data(const ASN1T_PKCS7_Data &iData);
		PKCS7Data(const PKCS7Data& iData);

		int copyFromASNObject(const ASN1T_PKCS7_Data &iData);
		int copyToASNObject(ASN1T_PKCS7_Data &oData)const;
		void freeASNObject( ASN1T_PKCS7_Data & oData)const;

		virtual ~PKCS7Data(void);

		// GETTERS AND SETTERS

		QByteArray getData()const;

		void setData(const QByteArray& iData);

	};

}

#endif


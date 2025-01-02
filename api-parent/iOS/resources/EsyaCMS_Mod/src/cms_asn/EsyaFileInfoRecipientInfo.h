
#ifndef __ESYAFILEINFORECIPIENTINFO__
#define __ESYAFILEINFORECIPIENTINFO__


#include "cms.h"
#include "esya.h"
#include "EASNWrapperTemplate.h"
#include "QByteArray"

namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* þifreli dosyalarýmýz için tanýmladýðýmýz Gizlilik Bilgisi ASN1 wrapper sýnýfý. Detaylar için esya.asn dökümanýna bakýnýz
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT ESYAFileInfoRecipientInfo : public EASNWrapperTemplate<ASN1T_ESYA_ESYAFileInfoRecipientInfo,ASN1C_ESYA_ESYAFileInfoRecipientInfo>
	{
	public :
		enum ESYAGizlilikBilgisi { EGB_Tanimlanmamis = ESYA_ESYAGizlilikBilgisi::tanimlanmamis, EGB_TasnifDisi = ESYA_ESYAGizlilikBilgisi::tasnifDisi , EGB_HizmeteOzel= ESYA_ESYAGizlilikBilgisi::hizmeteOzel,EGB_Ozel= ESYA_ESYAGizlilikBilgisi::ozel, EGB_Gizli = ESYA_ESYAGizlilikBilgisi::gizli};

		ESYAGizlilikBilgisi	mGizlilikBilgisi;


	public:
		ESYAFileInfoRecipientInfo(void);
		ESYAFileInfoRecipientInfo(const QByteArray & iEFIRI);
		ESYAFileInfoRecipientInfo(const ASN1T_ESYA_ESYAFileInfoRecipientInfo & iEFIRI);
		ESYAFileInfoRecipientInfo(const ESYAFileInfoRecipientInfo& iEFIRI);
		ESYAFileInfoRecipientInfo(const ESYAGizlilikBilgisi &iGizlilikBilgisi);


		ESYAFileInfoRecipientInfo & operator=(const ESYAFileInfoRecipientInfo & iEFIRI);
		friend bool operator==(const ESYAFileInfoRecipientInfo & ,const ESYAFileInfoRecipientInfo & );
		friend bool operator!=(const ESYAFileInfoRecipientInfo & ,const ESYAFileInfoRecipientInfo & );

		int copyFromASNObject(const ASN1T_ESYA_ESYAFileInfoRecipientInfo & iEFIRI);
		int copyToASNObject(ASN1T_ESYA_ESYAFileInfoRecipientInfo& oEFIRI)const;
		void freeASNObject(ASN1T_ESYA_ESYAFileInfoRecipientInfo & oEFIRI)const;

		const ESYAGizlilikBilgisi & getGizlilikBilgisi()		const;

		void setGizlilikBilgisi(const ESYAGizlilikBilgisi &iGizlilikBilgisi);

		static QString gizlilikBilgisi2String(const ESYAGizlilikBilgisi &);
		static QList<ESYAGizlilikBilgisi> listGizlilikBilgisi() ;

	public:
		~ESYAFileInfoRecipientInfo(void);
	};

}

#endif 


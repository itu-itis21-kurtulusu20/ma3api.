#ifndef __PRIVATEKEYINFO__
#define __PRIVATEKEYINFO__

#include "pkcs1pkcs8.h"
#include "ortak.h"
#include "AlgorithmIdentifier.h"
#include "EException.h"
#include "Attribute.h"

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
	class Q_DECL_EXPORT PrivateKeyInfo  : public EASNWrapperTemplate<ASN1T_PKCS18_PrivateKeyInfo,ASN1C_PKCS18_PrivateKeyInfo>
	{

	protected:

	   OSUINT32				mVersion;
	   AlgorithmIdentifier	mPKAlgorithm;
	   QByteArray			mPrivateKey;
	   QList<Attribute>     mAttributes;
		

	public:

		PrivateKeyInfo(void);
		PrivateKeyInfo( const ASN1T_PKCS18_PrivateKeyInfo &iPKI);
		PrivateKeyInfo( const PrivateKeyInfo &iPKI);
		PrivateKeyInfo( const QByteArray &iPKI);

		PrivateKeyInfo & operator=(const PrivateKeyInfo& iPKI);
		friend bool operator==(const PrivateKeyInfo& iRHS,const PrivateKeyInfo& iLHS);
		friend bool operator!=(const PrivateKeyInfo& iRHS, const PrivateKeyInfo& iLHS);

		int copyFromASNObject(const ASN1T_PKCS18_PrivateKeyInfo & iPKI);
		int copyToASNObject(ASN1T_PKCS18_PrivateKeyInfo & oPKI) const;
		void freeASNObject(ASN1T_PKCS18_PrivateKeyInfo& oPKI)const;

		virtual ~PrivateKeyInfo(void);

		// GETTERS AND SETTERS

		const OSUINT32 & getVersion()const;
		const AlgorithmIdentifier &	getPKAlgorithm()const ;
		const QByteArray & getPrivateKey() const ;
		const QList<Attribute>& getAttributes()const;

		void setVersion(const OSUINT32& );
		void setPKAlgorithm(const AlgorithmIdentifier& );
		void setPrivateKey(const QByteArray&);
		void addAttribute( const Attribute& );	


		bool isNull() const;
	};

}

#endif


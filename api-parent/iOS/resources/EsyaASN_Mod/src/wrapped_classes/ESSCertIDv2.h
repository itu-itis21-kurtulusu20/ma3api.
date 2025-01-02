#ifndef __ESSCERTIDV2__
#define __ESSCERTIDV2__

#include "PKCS7IssuerSerial.h"
#include "AlgorithmIdentifier.h"
#include "PKCS7Hash.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
    * \author dindaro and suleymanu
	*
	*/
    class Q_DECL_EXPORT ESSCertIDv2  : public EASNWrapperTemplate<ASN1T_PKCS7_ESSCertIDv2,ASN1C_PKCS7_ESSCertIDv2>
	{
		bool mIssuerSerialPresent;
        bool mHashAlgorithmPresent;
		PKCS7IssuerSerial mIssuerSerial;
        AlgorithmIdentifier mHashAlgorithm;
		PKCS7Hash mHash;
			
	public:

        ESSCertIDv2(void);
        ESSCertIDv2( const ASN1T_PKCS7_ESSCertIDv2 & iESSCertIDv2);
        ESSCertIDv2( const ESSCertIDv2 &iESSCertIDv2);
        ESSCertIDv2( const QByteArray &iESSCertIDv2);

        ESSCertIDv2 & operator=(const ESSCertIDv2&);
        friend bool operator==(const ESSCertIDv2& iRHS,const ESSCertIDv2& iLHS);
        friend bool operator!=(const ESSCertIDv2& iRHS, const ESSCertIDv2& iLHS);

        int copyFromASNObject(const ASN1T_PKCS7_ESSCertIDv2 & iESSCertIDv2);
        int copyToASNObject(ASN1T_PKCS7_ESSCertIDv2 & oESSCertIDv2) const;
        void freeASNObject(ASN1T_PKCS7_ESSCertIDv2& oESSCertIDv2)const;

        virtual ~ESSCertIDv2(void);

		// GETTERS AND SETTERS
		
		bool isIssuerSerialPresent()const;
        bool isHashAlgorithmPresent()const;
        const PKCS7IssuerSerial & getIssuerSerial() const;
        const AlgorithmIdentifier & getHashAlgorithm() const;
		const PKCS7Hash& getHash() const ;	

        void setIssuerSerial(const PKCS7IssuerSerial & );
        void setHashAlgorithm(const AlgorithmIdentifier &);
        void setHash(const PKCS7Hash & );
	
	};

}

#endif


#ifndef __DIGESTINFO__
#define __DIGESTINFO__

#include "pkcs7.h"
#include "ortak.h"
#include "AlgorithmIdentifier.h"

namespace esya
{

	class Q_DECL_EXPORT DigestInfo : public EASNWrapperTemplate<ASN1T_PKCS7_DigestInfo,ASN1C_PKCS7_DigestInfo>
	{
		AlgorithmIdentifier mDigestAlgorithm;
		QByteArray			mDigest;
		

	public:
		DigestInfo(void);
		DigestInfo(const QByteArray & iDI);
		DigestInfo(const AlgorithmIdentifier& iDigestAlg, const QByteArray & iDigest);
		DigestInfo(const ASN1T_PKCS7_DigestInfo & iDI );
		DigestInfo(const DigestInfo& iDI);

		DigestInfo& operator=(const DigestInfo& iDI);
		Q_DECL_EXPORT friend bool operator==( const DigestInfo& iRHS, const DigestInfo& iLHS);
		Q_DECL_EXPORT friend bool operator!=( const DigestInfo& iRHS, const DigestInfo& iLHS);

		int copyFromASNObject(const ASN1T_PKCS7_DigestInfo & iDI);
		int copyToASNObject(ASN1T_PKCS7_DigestInfo &oDI)const;
		void freeASNObject(ASN1T_PKCS7_DigestInfo& )const;

		virtual ~DigestInfo(void);

		// GETTERS AND SETTERS 

		const AlgorithmIdentifier & getDigestAlgorithm() const;
		const QByteArray getDigest() const ;

		void setDigestAlgorithm(const AlgorithmIdentifier& iDigestAlg);
		void setDigest(const QByteArray & iDigest) ;

		static QByteArray findDigest(const  QList<DigestInfo> & iDigestList , const AlgorithmIdentifier & iDigestAlg);
	};

}

#endif


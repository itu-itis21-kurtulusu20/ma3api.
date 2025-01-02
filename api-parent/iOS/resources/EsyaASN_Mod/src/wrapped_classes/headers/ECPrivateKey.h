#ifndef __ECPRIVATEKEY__
#define __ECPRIVATEKEY__

#include "algorithms.h"
#include "ortak.h"
#include "EBitString.h"
#include "ECParameters.h"

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
	class Q_DECL_EXPORT ECPrivateKey  : public EASNWrapperTemplate<ASN1T_ALGOS_ECPrivateKey,ASN1C_ALGOS_ECPrivateKey>
	{

	protected:
		bool			mParametersPresent;
		bool			mPublicKeyPresent;
		OSUINT8			mVersion;	
		QByteArray		mPrivateKey;
		ECParameters	mParameters;
		EBitString		mPublicKey;

	public:

		ECPrivateKey(void);
		ECPrivateKey( const ASN1T_ALGOS_ECPrivateKey &iEPK);
		ECPrivateKey( const ECPrivateKey &iEPK);
		ECPrivateKey( const QByteArray &iEPK);

		ECPrivateKey & operator=(const ECPrivateKey& iRPK);
		friend bool operator==(const ECPrivateKey& iRHS, const ECPrivateKey& iLHS);
		friend bool operator!=(const ECPrivateKey& iRHS, const ECPrivateKey& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_ECPrivateKey & iEPK);
		int copyToASNObject(ASN1T_ALGOS_ECPrivateKey & oEPK) const;
		void freeASNObject(ASN1T_ALGOS_ECPrivateKey& oRPK)const;

		virtual ~ECPrivateKey(void);

		// GETTERS AND SETTERS

		const bool	isParametersPresent()const ;
		const bool	isPublicKeyPresent()const ;

		const OSUINT8 getVersion()const;	
		const QByteArray &	getPrivateKey()const;
		const ECParameters& getParameters()const;
		const EBitString&	getPublicKey()const;

		void setParametersPresent(bool );
		void setPublicKeyPresent(bool);

		void setVersion(const OSUINT8 );	
		void setPrivateKey(const QByteArray &);
		void setParameters(const ECParameters&);
		void setPublicKey(const EBitString&	);

	};

} 

#endif


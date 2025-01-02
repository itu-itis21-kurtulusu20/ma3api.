#ifndef __SIGNERPARAM__
#define __SIGNERPARAM__

#include "ECertificate.h"
#include "BaseSigner.h"

namespace esya
{

	/**
	* \ingroup EsyaCMS
	* 
	* Imzac� parametrelerini tutar
	* 
	* \author dindaro
	*
	*/	
	class Q_DECL_EXPORT SignerParam
	{
		int					mVersion;
		ECertificate		mCert;
		AlgorithmIdentifier	mDigestAlg;
		QByteArray			mDigest;
		bool				mSMIMEAttributesIncluded;
		QList<QPair<ASN1TObjId,QByteArray> >	mAdditionalUnsignedAttributes;
        BaseSigner *mpSigner;



	
	public:
		SignerParam(void);
        SignerParam(int iVersion, const ECertificate & iCert, const AlgorithmIdentifier &iDigestAlg, BaseSigner *signer);
        SignerParam(int iVersion, const ECertificate & iCert, const AlgorithmIdentifier &iDigestAlg, const QList<QPair<ASN1TObjId,QByteArray> > & iAdditionalSignedAttributes, BaseSigner *signer);
        SignerParam(const SignerParam&);

		SignerParam & operator=(const SignerParam& );

		friend bool operator==(const SignerParam& iRHS, const SignerParam& iLHS);

		const int 					getVersion() const;
		const ECertificate &		getCert() const;
		const AlgorithmIdentifier &	getDigestAlg() const ;
		const QByteArray&			getDigest()const ;
		const bool					getSMIMEAttributesIncluded() const;
		const QList<QPair<ASN1TObjId,QByteArray > > & getAdditionalUnsignedAttributes() const;




		void setVersion( const int);
		void setCert( const ECertificate &);
		void setDigestAlg(const AlgorithmIdentifier &) ;
		void setDigest(const QByteArray&);
		void setSMIMEAttributesIncluded(const bool) ;	
		void setAdditionalUnsignedAttributes(const QList<QPair<ASN1TObjId,QByteArray > > & iASA) ;

        BaseSigner * getSigner() const;

	public:
		~SignerParam(void);
	};

}

#endif


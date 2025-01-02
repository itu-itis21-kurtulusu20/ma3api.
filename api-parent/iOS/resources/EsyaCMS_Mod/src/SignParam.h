#ifndef __SIGNPARAM__
#define __SIGNPARAM__

#include "SignerParam.h"
#include "DigestInfo.h"

namespace esya
{
	/**
	* \ingroup EsyaCMS
	* 
	* Imzalama parametrelerini tutar
	* 
	* \author dindaro
	*
	*/	
	class Q_DECL_EXPORT SignParam
	{
		bool				mAyrikImza;
		QList<SignerParam>	mSignerParams;


	public:
		SignParam(void);
		SignParam(bool mAyrikImza, const QList<SignerParam>& iSignerParams);	
		SignParam(const SignParam&);	

		SignParam & operator=(const SignParam& );

		friend bool operator==(const SignParam& iRHS, const SignParam& iLHS);

		const QList<SignerParam>& getSignerParams() const;
		const bool  isAyrikImza() const ;

		void setAyrikImza(const bool) ;
		void setSignerParams( const QList<SignerParam>&);
		void addSignerParam( const SignerParam&);

		const QList<AlgorithmIdentifier> getDigestAlgs()const ;

		void fillDigests(const QList<DigestInfo>& );

	public:
		~SignParam(void);
	};

}

#endif


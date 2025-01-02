#ifndef __ALGORITHMIDENTIFIER__
#define __ALGORITHMIDENTIFIER__

#include "pkcs12.h"
#include "ortak.h"
#include "ContentInfo.h"
#include "ESeqOfList.h"
#include "algorithms.h"
#include "myasndefs.h"

#define NULL_PARAMS "\5\0"

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
	class Q_DECL_EXPORT AlgorithmIdentifier : public EASNWrapperTemplate<ASN1T_EXP_AlgorithmIdentifier,ASN1C_EXP_AlgorithmIdentifier>
	{
		QByteArray mParameters;
		ASN1TObjId mAlgorithm; 
		bool mParametersPresent;


	public:
		AlgorithmIdentifier(void);
		AlgorithmIdentifier(const QByteArray & );
		AlgorithmIdentifier(const QByteArray & , const ASN1TObjId &);
		AlgorithmIdentifier(const ASN1TObjId &);
		AlgorithmIdentifier(const ASN1T_EXP_AlgorithmIdentifier & );
		AlgorithmIdentifier(const AlgorithmIdentifier& );

		AlgorithmIdentifier& operator=(const AlgorithmIdentifier& );
		Q_DECL_EXPORT friend bool operator==(const AlgorithmIdentifier & iRHS, const AlgorithmIdentifier& iLHS);
		Q_DECL_EXPORT friend bool operator!=(const AlgorithmIdentifier & iRHS, const AlgorithmIdentifier& iLHS);

		int copyFromASNObject(const ASN1T_EXP_AlgorithmIdentifier & ) ;
		int copyToASNObject(ASN1T_EXP_AlgorithmIdentifier & oAlgorithmIdentifier) const;
		void freeASNObject(ASN1T_EXP_AlgorithmIdentifier & oAlgorithmIdentifier)const;

		int copyAIs(const ASN1TPDUSeqOfList & iAIs, QList<AlgorithmIdentifier>& oList);
		int copyAIs(const QList<AlgorithmIdentifier>& iList, ASN1TPDUSeqOfList & oAIs);
		int copyAIs(const QByteArray&  iASNBytes,  QList<AlgorithmIdentifier> & oList );
		int copyAIs(const QList<AlgorithmIdentifier> & iList ,QByteArray& oASNBytes);

		virtual ~AlgorithmIdentifier(void);

		// GETTERS AND SETTERS

		const QByteArray &getParameters()const ;
		const ASN1TObjId &getAlgorithm() const;


		QByteArray getParamsAsOctets()const;
		void setParamsAsOctets(const QByteArray& );

		void setParameters(const QByteArray& );

		bool isParametersPresent() const;

		bool isNull() const;

	
	
	};

}

#endif

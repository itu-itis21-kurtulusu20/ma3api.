
#ifndef __SUBJECTPUBLICKEYINFO__
#define __SUBJECTPUBLICKEYINFO__

#include "EBitString.h"
#include "AlgorithmIdentifier.h"


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
	class Q_DECL_EXPORT SubjectPublicKeyInfo  : public EASNWrapperTemplate<ASN1T_EXP_SubjectPublicKeyInfo,ASN1C_EXP_SubjectPublicKeyInfo>
	{
		AlgorithmIdentifier mAlgorithm;
		EBitString			mSubjectPublicKey;

	public :
		SubjectPublicKeyInfo(void);
		SubjectPublicKeyInfo(const QByteArray & );
		SubjectPublicKeyInfo(const ASN1T_EXP_SubjectPublicKeyInfo & );
		SubjectPublicKeyInfo(const SubjectPublicKeyInfo&);
		SubjectPublicKeyInfo(const AlgorithmIdentifier & iAlgorithm, const EBitString& iSubjectPublicKey);

		SubjectPublicKeyInfo & operator=(const SubjectPublicKeyInfo & );
		Q_DECL_EXPORT friend bool operator==(const SubjectPublicKeyInfo & ,const SubjectPublicKeyInfo& );
		Q_DECL_EXPORT friend bool operator!=(const SubjectPublicKeyInfo& ,const SubjectPublicKeyInfo& );
		
		int copyFromASNObject(const ASN1T_EXP_SubjectPublicKeyInfo &);
		int copyToASNObject(ASN1T_EXP_SubjectPublicKeyInfo & oSPKI)const;
		void freeASNObject(ASN1T_EXP_SubjectPublicKeyInfo& oSPKI)const;

		virtual ~SubjectPublicKeyInfo();

		// GETTERS AND SETTERS

		const AlgorithmIdentifier& getAlgorithm()const;
		const EBitString& getSubjectPublicKey()const;

		AlgorithmIdentifier& getAlgorithm();

		void setAlgorithm(const AlgorithmIdentifier& );
		void setSubjectPublicKey(const EBitString& );
	};

}

#endif


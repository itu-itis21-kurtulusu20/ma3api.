#ifndef __CURVE__
#define __CURVE__

#include "pkcs12.h"
#include "ortak.h"
#include "ContentInfo.h"
#include "ESeqOfList.h"
#include "EASNWrapperTemplate.h"
#include "algorithms.h"
#include "myasndefs.h"
#include "EBitString.h"

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
	class Q_DECL_EXPORT Curve : public EASNWrapperTemplate<ASN1T_ALGOS_Curve,ASN1C_ALGOS_Curve>
	{
		QByteArray	mA;
		QByteArray	mB;
		bool		mSeedPresent;
		EBitString	mSeed;


	public:
		Curve(void);
		Curve(const QByteArray & );
		Curve(const ASN1T_ALGOS_Curve & );
		Curve(const Curve& );

		Curve& operator=(const Curve& );
		friend bool operator==(const Curve & iRHS, const Curve& iLHS);
		friend bool operator!=(const Curve & iRHS, const Curve& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_Curve & ) ;
		int copyToASNObject(ASN1T_ALGOS_Curve & oCurve) const;
		void freeASNObject(ASN1T_ALGOS_Curve & oCurve)const;

		virtual ~Curve(void);

		// GETTERS AND SETTERS

		const QByteArray &getA()const ;
		const QByteArray &getB()const ;
		const EBitString &getSeed() const;
		bool isSeedPresent()const;

		void setA(const QByteArray & );
		void setB(const QByteArray & );
		void setSeed(const EBitString &);
		void setSeedPresent(bool);

	};

}

#endif

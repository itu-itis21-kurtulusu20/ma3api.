#ifndef __SPECIFIEDECDOMAIN__
#define __SPECIFIEDECDOMAIN__

#include "Curve.h"
#include "FieldID.h"
#include <QString>
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
	class Q_DECL_EXPORT SpecifiedECDomain : public EASNWrapperTemplate<ASN1T_ALGOS_SpecifiedECDomain,ASN1C_ALGOS_SpecifiedECDomain>
	{
		bool				mCofactorPresent;
		bool				mHashPresent;
		int					mVersion;
		FieldID				mFieldID;
		Curve				mCurve;
		QByteArray			mBase;
		QString				mOrder;
		QString				mCofactor;
		AlgorithmIdentifier mHash;

	public:
		SpecifiedECDomain(void);
		SpecifiedECDomain(const QByteArray & );
		SpecifiedECDomain(const ASN1T_ALGOS_SpecifiedECDomain & );
		SpecifiedECDomain(const SpecifiedECDomain& );

		SpecifiedECDomain& operator=(const SpecifiedECDomain& );
		friend bool operator==(const SpecifiedECDomain & iRHS, const SpecifiedECDomain& iLHS);
		friend bool operator!=(const SpecifiedECDomain & iRHS, const SpecifiedECDomain& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_SpecifiedECDomain & ) ;
		int copyToASNObject(ASN1T_ALGOS_SpecifiedECDomain & oECParameters) const;
		void freeASNObject(ASN1T_ALGOS_SpecifiedECDomain & oSpecifiedECDomain)const;

		virtual ~SpecifiedECDomain(void);
		
		// GETTERS AND SETTERS

		const FieldID &						getFieldID()const ;
		const Curve &						getCurve()const ;
		const QByteArray &					getBase() const;
		const QString &						getOrder() const;		
		const QString &						getCofactor() const;		
		const AlgorithmIdentifier &			getHash() const;		
		bool						isCofactorPresent()const;
		bool						isHashPresent()const;

		void setFieldID(const FieldID & );
		void setCurve(const Curve &);
		void setBase(const QByteArray &);
		void setOrder(const QString &);
		void setCofactor(const QString &);
		void setHash(const AlgorithmIdentifier&);		
		void setCofactorPresent(bool);
		void setHashPresent(bool);
	};

}

#endif

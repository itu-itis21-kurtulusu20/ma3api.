#ifndef __ATTCERTVALIDITYPERIOD__
#define __ATTCERTVALIDITYPERIOD__


#include <QString>
#include "EASNWrapperTemplate.h"
#include "attrcert.h"

namespace esya
{

	class Q_DECL_EXPORT AttCertValidityPeriod : public EASNWrapperTemplate<ASN1T_ATTRCERT_AttCertValidityPeriod,ASN1C_ATTRCERT_AttCertValidityPeriod>
	{
		QString mNotBeforeTime;
		QString mNotAfterTime;

	public:
		AttCertValidityPeriod(void);
		AttCertValidityPeriod(const QByteArray & iAttCertValidityPeriod);
		AttCertValidityPeriod(const ASN1T_ATTRCERT_AttCertValidityPeriod & iAttCertValidityPeriod );
		AttCertValidityPeriod(const AttCertValidityPeriod& iAttCertValidityPeriod);

		AttCertValidityPeriod& operator=(const AttCertValidityPeriod& iAttCertValidityPeriod);
		friend bool operator==( const AttCertValidityPeriod& iRHS, const AttCertValidityPeriod& iLHS);
		friend bool operator!=( const AttCertValidityPeriod& iRHS, const AttCertValidityPeriod& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AttCertValidityPeriod & iAttCertValidityPeriodr);
		int copyToASNObject(ASN1T_ATTRCERT_AttCertValidityPeriod &oAttCertValidityPeriod)const;
		void freeASNObject(ASN1T_ATTRCERT_AttCertValidityPeriod& oAttCertValidityPeriod)const;

		virtual ~AttCertValidityPeriod(void);

		// GETTERS AND SETTERS

		const QString & getNotBeforeTime()const;
		const QString & getNotAfterTime()const;

		void setNotBeforeTime(const QString & );
		void setNotAfterTime(const QString & );

	};

}

#endif


#include "AttCertValidityPeriod.h"
#include "ortak.h"

using namespace esya;

namespace esya
{

	AttCertValidityPeriod::AttCertValidityPeriod(void)
	{
	}

	AttCertValidityPeriod::AttCertValidityPeriod(const QByteArray & iAttCertValidityPeriod)
	{
		constructObject(iAttCertValidityPeriod);
	}

	AttCertValidityPeriod::AttCertValidityPeriod(const ASN1T_ATTRCERT_AttCertValidityPeriod & iAttCertValidityPeriod )
	{
		copyFromASNObject(iAttCertValidityPeriod);
	}

	AttCertValidityPeriod::AttCertValidityPeriod(const AttCertValidityPeriod& iAttCertValidityPeriod)
	:	mNotBeforeTime(iAttCertValidityPeriod.getNotBeforeTime()),
		mNotAfterTime(iAttCertValidityPeriod.getNotAfterTime())
	{
	}

	AttCertValidityPeriod& AttCertValidityPeriod::operator=(const AttCertValidityPeriod& iAttCertValidityPeriod)
	{
		mNotBeforeTime	= iAttCertValidityPeriod.getNotBeforeTime();
		mNotAfterTime	= iAttCertValidityPeriod.getNotAfterTime();

		return *this;
	}

	bool operator==( const AttCertValidityPeriod& iRHS, const AttCertValidityPeriod& iLHS)
	{
		return ( ( iRHS.getNotBeforeTime()	== iLHS.getNotBeforeTime()	) && 
				 ( iRHS.getNotAfterTime()	== iLHS.getNotAfterTime()	)		);
	}

	bool operator!=( const AttCertValidityPeriod& iRHS, const AttCertValidityPeriod& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AttCertValidityPeriod::copyFromASNObject(const ASN1T_ATTRCERT_AttCertValidityPeriod & iAttCertValidityPeriod)
	{
		mNotBeforeTime	= QString(iAttCertValidityPeriod.notBeforeTime);
		mNotAfterTime	= QString(iAttCertValidityPeriod.notAfterTime);
		
		return SUCCESS;
	}

	int AttCertValidityPeriod::copyToASNObject(ASN1T_ATTRCERT_AttCertValidityPeriod &oAttCertValidityPeriod)const
	{
		oAttCertValidityPeriod.notBeforeTime	= myStrDup(mNotBeforeTime);
		oAttCertValidityPeriod.notAfterTime		= myStrDup(mNotAfterTime);
		
		return SUCCESS;
	}

	void AttCertValidityPeriod::freeASNObject(ASN1T_ATTRCERT_AttCertValidityPeriod& oAttCertValidityPeriod)const
	{
		DELETE_MEMORY_ARRAY(oAttCertValidityPeriod.notBeforeTime);
		DELETE_MEMORY_ARRAY(oAttCertValidityPeriod.notAfterTime);
	}

	const QString& AttCertValidityPeriod::getNotBeforeTime()const 
	{
		return mNotBeforeTime;
	}

	const QString& AttCertValidityPeriod::getNotAfterTime()const 
	{
		return mNotAfterTime;
	}

	void AttCertValidityPeriod::setNotBeforeTime(const QString& iNotBeforeTime)
	{
		mNotBeforeTime = iNotBeforeTime;
	}

	void AttCertValidityPeriod::setNotAfterTime(const QString& iNotAfterTime)
	{
		mNotAfterTime = iNotAfterTime;
	}

	AttCertValidityPeriod::~AttCertValidityPeriod(void)
	{
	}

}
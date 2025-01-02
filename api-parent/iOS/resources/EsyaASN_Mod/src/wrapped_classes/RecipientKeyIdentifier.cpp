#include "RecipientKeyIdentifier.h"

namespace esya
{


RecipientKeyIdentifier::RecipientKeyIdentifier(void)
:	mOtherPresent(false),
	mDatePresent(false)
{
}

RecipientKeyIdentifier::RecipientKeyIdentifier(const QByteArray & iRKI )
{
	constructObject(iRKI);
}

RecipientKeyIdentifier::RecipientKeyIdentifier(const ASN1T_CMS_RecipientKeyIdentifier & iRKI)
{
	copyFromASNObject(iRKI);
}

RecipientKeyIdentifier::RecipientKeyIdentifier(const RecipientKeyIdentifier& iRKI )
:	mOtherPresent(iRKI.isOtherPresent()),
	mDatePresent(iRKI.isDatePresent()),
	mOther(iRKI.getOther()),
	mDate(iRKI.getDate()),
	mSKI(iRKI.getSKI())
{
		
}

RecipientKeyIdentifier& RecipientKeyIdentifier::operator=(const RecipientKeyIdentifier& iRKI)
{
	mOtherPresent	= iRKI.isOtherPresent();
	mDatePresent	= iRKI.isDatePresent();
	mOther			= iRKI.getOther();
	mDate			= iRKI.getDate();
	mSKI			= iRKI.getSKI();

	return *this;
}

bool operator==(const RecipientKeyIdentifier & iRHS, const RecipientKeyIdentifier& iLHS)
{
	if (	(	iRHS.getSKI()			!= iLHS.getSKI()		) ||
			(	iRHS.isDatePresent()	!= iLHS.isDatePresent()	) ||		
			(	iRHS.isOtherPresent()	!= iLHS.isOtherPresent()	))
		return false;

	if (	  iRHS.isDatePresent() && 
			( iRHS.getDate() != iLHS.getDate() )	)
		return false;

	if (	  iRHS.isOtherPresent() && 
			( iRHS.getOther() != iLHS.getOther() )	)
		return false;


	return true;
}

bool operator!=(const RecipientKeyIdentifier & iRHS, const RecipientKeyIdentifier& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int RecipientKeyIdentifier::copyFromASNObject(const ASN1T_CMS_RecipientKeyIdentifier & iRKI) 
{
	mSKI.copyFromASNObject(iRKI.subjectKeyIdentifier);
	mDatePresent	= iRKI.m.datePresent;
	mOtherPresent	= iRKI.m.otherPresent;

	if (mOtherPresent)
		mOther.copyFromASNObject(iRKI.other);

	if (mDatePresent)
		mDate = QString(iRKI.date);

	return SUCCESS;

}

int RecipientKeyIdentifier::copyToASNObject(ASN1T_CMS_RecipientKeyIdentifier & oRKI) const
{
	mSKI.copyToASNObject(oRKI.subjectKeyIdentifier);
	oRKI.m.datePresent	= mDatePresent;
	oRKI.m.otherPresent = mOtherPresent;

	if (mDatePresent)
{		oRKI.date = myStrDup(mDate);

	if (mOtherPresent)
		mOther.copyToASNObject(oRKI.other);

	return SUCCESS;
}

}

void RecipientKeyIdentifier::freeASNObject(ASN1T_CMS_RecipientKeyIdentifier & oRKI)const
{
	SubjectKeyIdentifier().freeASNObject(oRKI.subjectKeyIdentifier);

	if (oRKI.m.otherPresent)
		OtherKeyAttribute().freeASNObject(oRKI.other);

	if (oRKI.m.datePresent)
		DELETE_MEMORY_ARRAY(oRKI.date);
}

bool RecipientKeyIdentifier::isDatePresent()const
{
	return mDatePresent;
}

bool RecipientKeyIdentifier::isOtherPresent()const
{
	return mOtherPresent;
}

const SubjectKeyIdentifier&	RecipientKeyIdentifier::getSKI() const 
{
	return mSKI;
}

const QString& RecipientKeyIdentifier::getDate() const 
{
	return mDate;
}

const OtherKeyAttribute& RecipientKeyIdentifier::getOther() const 
{
	return mOther;
}

void RecipientKeyIdentifier::setDatePresent(bool iDP)
{
	mDatePresent = iDP;	
}

void RecipientKeyIdentifier::setOtherPresent(bool iOP)
{
	mOtherPresent = iOP;
}

void RecipientKeyIdentifier::setSKI(const SubjectKeyIdentifier& iSKI)
{
	mSKI = iSKI;
}

void RecipientKeyIdentifier::setDate(const QString& iDate)
{
	mDate = iDate;
}

void RecipientKeyIdentifier::setOther(const OtherKeyAttribute& iOther)
{
	mOther = iOther;
}

RecipientKeyIdentifier::~RecipientKeyIdentifier(void)
{
}


}
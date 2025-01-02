#include "PersonalName.h"

NAMESPACE_BEGIN(esya)
using namespace esya;

PersonelName::PersonelName(void)
{
}

PersonelName::PersonelName( const ASN1T_EXP_PersonalName&iPN)
{
	copyFromASNObject(iPN);
}


PersonelName::PersonelName( const PersonelName &iPN)
:	mSurName(iPN.getSurName()),
	mGivenNamePresent(iPN.isGivenNamePresent()),
	mInitialsPresent(iPN.isInitialsPresent()),
	mGenerationQualifierPresent(iPN.isGenerationQualifierPresent())
{
	if (mGivenNamePresent)
		mGivenName = iPN.getGivenName();

	if (mInitialsPresent)
		mInitials = iPN.getInitials();

	if (mGenerationQualifierPresent)
		mGenerationQualifier = iPN.getGenerationQualifier();

}

PersonelName::PersonelName( const QByteArray &iPN)
{
	constructObject(iPN);
}

PersonelName & PersonelName::operator=(const PersonelName& iPN)
{
	mSurName  = iPN.getSurName();
	mGivenNamePresent  = iPN.isGivenNamePresent();
	mInitialsPresent  = iPN.isInitialsPresent();
	mGenerationQualifierPresent  = iPN.isGenerationQualifierPresent();

	if (mGivenNamePresent)
		mGivenName = iPN.getGivenName();

	if (mInitialsPresent)
		mInitials = iPN.getInitials();

	if (mGenerationQualifierPresent)
		mGenerationQualifier = iPN.getGenerationQualifier();

	return(*this);
}

bool operator==(const PersonelName& iRHS,const PersonelName& iLHS)
{
	if (	(iRHS.getSurName() != iLHS.getSurName())	|| 
			(iRHS.isGivenNamePresent() != iLHS.isGivenNamePresent()) ||
			(iRHS.isInitialsPresent() != iLHS.isInitialsPresent())	||
			(iRHS.isGenerationQualifierPresent() != iLHS.isGenerationQualifierPresent())
		)
		return false;
	
	if (iRHS.isGivenNamePresent() && (iRHS.getGivenName() != iLHS.getGivenName()))
		return false;
	if (iRHS.isInitialsPresent() && (iRHS.getInitials() != iLHS.getInitials()))
		return false;
	if (iRHS.isGenerationQualifierPresent() && (iRHS.getGenerationQualifier() != iLHS.getGenerationQualifier()))
		return false;
	
	return true;
}

bool operator!=(const PersonelName& iRHS, const PersonelName& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int PersonelName::copyFromASNObject(const ASN1T_EXP_PersonalName & iPN)
{
	mSurName	= QString(iPN.surname);

	if (mGivenNamePresent = (iPN.m.given_namePresent == 1))
		mGivenName	= QString(iPN.given_name);
	
	if (mInitialsPresent = (iPN.m.initialsPresent == 1))
		mInitials	= QString(iPN.initials);
	
	if (mGenerationQualifierPresent = (iPN.m.generation_qualifierPresent== 1))
		mGenerationQualifier  = QString(iPN.generation_qualifier);
	return SUCCESS;
}

int PersonelName::copyToASNObject(ASN1T_EXP_PersonalName & oPN ) const
{
	oPN.surname		= myStrDup(mSurName);
	

	if (oPN.m.given_namePresent = mGivenNamePresent)
		oPN.given_name  = myStrDup(mGivenName);

	if (oPN.m.initialsPresent= mInitialsPresent)
		oPN.initials    = myStrDup(mInitials);
	
	if (oPN.m.generation_qualifierPresent= mGenerationQualifierPresent)
		oPN.generation_qualifier = myStrDup(mGenerationQualifier);

	return SUCCESS;
}
	
void PersonelName::freeASNObject(ASN1T_EXP_PersonalName& oPN)const
{
	DELETE_MEMORY_ARRAY(oPN.surname);

	if (oPN.m.given_namePresent)
		DELETE_MEMORY_ARRAY(oPN.given_name);

	if (oPN.m.initialsPresent)
		DELETE_MEMORY_ARRAY(oPN.initials);
	
	if (oPN.m.generation_qualifierPresent)
		DELETE_MEMORY_ARRAY(oPN.generation_qualifier);
}

const QString& PersonelName::getGivenName() const 
{
	return mGivenName;
}

void PersonelName::setGivenName(const QString & iGN)  
{
	mGivenName = iGN;
	mGivenNamePresent = true;
}

const QString& PersonelName::getSurName() const 
{
	return mSurName;
}
	
void PersonelName::setSurName(const QString & iSN)  
{
	mSurName = iSN;
}

const QString& PersonelName::getInitials() const 
{
	return mInitials;
}

void PersonelName::setInitials(const QString& iInitials)
{
	mInitials = iInitials;
	mInitialsPresent = true;
}

const QString& PersonelName::getGenerationQualifier() const 
{
	return mGenerationQualifier;
}

void PersonelName::setGenerationQualifier(const QString& iGQ) 	
{
	mGenerationQualifier = iGQ;
	mGenerationQualifierPresent = true;
}

bool PersonelName::isGivenNamePresent()const
{
	return mGivenNamePresent;
}

void PersonelName::setGivenNamePresent(bool iGNP)
{
	mGivenNamePresent = iGNP;
}

bool PersonelName::isInitialsPresent()const
{
	return mInitialsPresent;
}

void PersonelName::setInitialsPresent(bool iIP)
{
	mInitialsPresent = iIP;
}

bool PersonelName::isGenerationQualifierPresent()const
{
	return mGenerationQualifierPresent;
}

void PersonelName::setGenerationQualifierPresent(bool iGQP)
{
	mGenerationQualifierPresent = iGQP;
}


PersonelName::~PersonelName()
{
}
NAMESPACE_END

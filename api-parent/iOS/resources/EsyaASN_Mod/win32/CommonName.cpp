#include "CommonName.h"

using namespace esya;

CommonName::CommonName()
: mCommonName(NULL)
{
}

CommonName::CommonName(const QString & iName)
: mCommonName(iName)
{
}


CommonName::CommonName(const ASN1T_EXP_CommonName & iCommonName)
{
	copyFromASNObject(iCommonName);
}


CommonName::CommonName(const CommonName& iCommonName)
:mCommonName(iCommonName.getCommonName())
{
}

CommonName::CommonName(const QByteArray & iCommonName)
{
	constructObject(iCommonName);
}

CommonName& CommonName::operator=(const CommonName& iCommonName)
{
	mCommonName	= iCommonName.getCommonName();
	return *this;
}


bool esya::operator==(const CommonName & iRHS, const CommonName& iLHS)
{
	return ( iRHS.getCommonName() == iLHS.getCommonName() );
}

bool esya::operator!=(const CommonName & iRHS, const CommonName& iLHS)
{
	return ( ! ( iRHS == iLHS ));
}


int CommonName::copyFromASNObject(const ASN1T_EXP_CommonName & iCommonName) 
{
	mCommonName = QString(iCommonName);
	
	return SUCCESS;
}


int CommonName::copyToASNObject(ASN1T_EXP_CommonName & oCommonName) const 
{
	oCommonName = myStrDup(mCommonName);

	return SUCCESS;
}

void CommonName::freeASNObject(ASN1T_EXP_CommonName & oCommonName)const
{
	DELETE_MEMORY_ARRAY(oCommonName);
}

const QString & CommonName::getCommonName()const 
{
	return mCommonName;
}


void CommonName::setCommonName(const QString & iName)
{
	mCommonName = iName;
}


bool CommonName::isNull() const
{
	return (mCommonName.isNull() );
}

CommonName::~CommonName(void)
{
}

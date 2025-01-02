#include "PBKDF2_Salt.h"

using namespace esya;

PBKDF2_Salt::PBKDF2_Salt(void)
{
}

PBKDF2_Salt::PBKDF2_Salt(const  QByteArray &iSalt)
{
	constructObject(iSalt);
}

PBKDF2_Salt::PBKDF2_Salt(const ASN1T_PKCS5_PBKDF2_params_salt & iSalt)
{
	copyFromASNObject(iSalt);
}

PBKDF2_Salt::PBKDF2_Salt(const PBKDF2_Salt & iSalt)
:	mType(iSalt.getType())
{
	if (mType == T_PKCS5_PBKDF2_params_salt_specified )
		mSpecified = iSalt.getSpecified();

	else if ( mType == T_PKCS5_PBKDF2_params_salt_otherSource )
		mOtherSource = iSalt.getOtherSource();

}


PBKDF2_Salt& PBKDF2_Salt::operator=(const PBKDF2_Salt& iSalt)
{
	mType = iSalt.getType();
	if (mType == T_PKCS5_PBKDF2_params_salt_specified )
		mSpecified = iSalt.getSpecified();

	else if ( mType == T_PKCS5_PBKDF2_params_salt_otherSource )
		mOtherSource = iSalt.getOtherSource();
	return *this;
}

bool esya::operator==(const PBKDF2_Salt & iRHS,const PBKDF2_Salt & iLHS)
{
	return !(	( iRHS.getType() != iLHS.getType() ) || 
				( (iRHS.getType() == T_PKCS5_PBKDF2_params_salt_specified) && (iRHS.getSpecified()!= iLHS.getSpecified()) ) ||
				( (iRHS.getType() == T_PKCS5_PBKDF2_params_salt_otherSource) && (iRHS.getOtherSource()!= iLHS.getOtherSource()) )   );
}

bool esya::operator!=(const PBKDF2_Salt & iRHS, const PBKDF2_Salt & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PBKDF2_Salt::copyFromASNObject(const ASN1T_PKCS5_PBKDF2_params_salt & iSalt)
{
	mType = iSalt.t;
	if ( iSalt.t == T_PKCS5_PBKDF2_params_salt_specified )
	{
		mSpecified = toByteArray(*(iSalt.u.specified));
	}
	else if ( iSalt.t == T_PKCS5_PBKDF2_params_salt_otherSource )
	{
		mOtherSource.copyFromASNObject(*(iSalt.u.otherSource)) ;
	}
	return SUCCESS;
}

int PBKDF2_Salt::copyToASNObject(ASN1T_PKCS5_PBKDF2_params_salt & oSalt) const
{
	oSalt.t = mType;
	
	if ( oSalt.t == T_PKCS5_PBKDF2_params_salt_specified )
	{
		oSalt.u.specified = new ASN1TDynOctStr();
		oSalt.u.specified->data = (OSOCTET*) myStrDup(mSpecified.data(),mSpecified.size());
		oSalt.u.specified->numocts = mSpecified.size();
	}
	else if ( oSalt.t == T_PKCS5_PBKDF2_params_salt_otherSource )
	{
		oSalt.u.otherSource = mOtherSource.getASNCopy();
	}
	return SUCCESS;
}

void PBKDF2_Salt::freeASNObject(ASN1T_PKCS5_PBKDF2_params_salt & oSalt)const
{
	if ( oSalt.t == T_PKCS5_PBKDF2_params_salt_specified )
	{
		if ( oSalt.u.specified )
			if ( oSalt.u.specified->numocts )
				DELETE_MEMORY_ARRAY(oSalt.u.specified->data)	
		DELETE_MEMORY(oSalt.u.specified)
	}
	else if ( oSalt.t == T_PKCS5_PBKDF2_params_salt_otherSource )
	{
		if (oSalt.u.otherSource)
			AlgorithmIdentifier().freeASNObjectPtr(oSalt.u.otherSource);
	}
}

const int	PBKDF2_Salt::getType()const
{
	return mType;
}

const QByteArray& PBKDF2_Salt::getSpecified()const
{
	return mSpecified;
}

const AlgorithmIdentifier&	PBKDF2_Salt::getOtherSource()const
{
	return mOtherSource;
}

void PBKDF2_Salt::setType(const int	iType)
{
	mType = iType;
}

void PBKDF2_Salt::setSpecified(const QByteArray& iSpecified)
{
	setType(T_PKCS5_PBKDF2_params_salt_specified);
	mSpecified = iSpecified;
}

void PBKDF2_Salt::setOtherSource(const AlgorithmIdentifier&	iOtherSource)
{
	setType(T_PKCS5_PBKDF2_params_salt_otherSource);
	mOtherSource = iOtherSource;
}


PBKDF2_Salt::~PBKDF2_Salt(void)
{
}

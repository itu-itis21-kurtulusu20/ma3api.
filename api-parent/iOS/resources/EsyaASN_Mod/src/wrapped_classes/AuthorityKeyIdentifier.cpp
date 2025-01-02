#include "AuthorityKeyIdentifier.h"
#include "OrtakDil.h"

using namespace esya;

AuthorityKeyIdentifier::AuthorityKeyIdentifier(void)
{
}

AuthorityKeyIdentifier::AuthorityKeyIdentifier(const AuthorityKeyIdentifier &iAKI)
:	mKeyIdentifierPresent(iAKI.isKeyIdentifierPresent()),
	mAuthorityCertIssuerPresent(iAKI.isAuthorityCertIssuerPresent()),
	mAuthorityCertSerialNumberPresent(iAKI.isAuthorityCertSerialNumberPresent()),
	mKeyIdentifier(iAKI.getKeyIdentifier()),
	mAuthorityCertIssuer(iAKI.getAuthorityCertIssuer()),
	mAuthorityCertSerialNumber(iAKI.getAuthorityCertSerialNumber())
{
}

AuthorityKeyIdentifier::AuthorityKeyIdentifier(const ASN1T_IMP_AuthorityKeyIdentifier & iAKI)
{
	copyFromASNObject(iAKI);
}

AuthorityKeyIdentifier::AuthorityKeyIdentifier(const QByteArray & iAKI)
{
	constructObject(iAKI);
}

AuthorityKeyIdentifier & AuthorityKeyIdentifier::operator=(const AuthorityKeyIdentifier& iAKI)
{
	mKeyIdentifierPresent				= iAKI.isKeyIdentifierPresent();
	mAuthorityCertIssuerPresent			= iAKI.isAuthorityCertIssuerPresent();
	mAuthorityCertSerialNumberPresent	= iAKI.isAuthorityCertSerialNumberPresent();
	if ( mKeyIdentifierPresent )
		mKeyIdentifier = iAKI.getKeyIdentifier();
	if ( mAuthorityCertIssuerPresent )
		mAuthorityCertIssuer = iAKI.getAuthorityCertIssuer();
	if ( mAuthorityCertSerialNumberPresent)
		mAuthorityCertSerialNumber = iAKI.getAuthorityCertSerialNumber();
	return (*this);
}

bool esya::operator==(const AuthorityKeyIdentifier& iRHS, const AuthorityKeyIdentifier& iLHS)
{
	if (	( iRHS.isKeyIdentifierPresent()				!= iLHS.isKeyIdentifierPresent() ) 
		||	( iRHS.isAuthorityCertIssuerPresent()		!= iLHS.isAuthorityCertIssuerPresent() ) 
		||	( iRHS.isAuthorityCertSerialNumberPresent()	!= iLHS.isAuthorityCertSerialNumberPresent() )  )
		return false;
	if ( iRHS.isKeyIdentifierPresent() && (iRHS.getKeyIdentifier() != iLHS.getKeyIdentifier()))
		return false;
	if ( iRHS.isAuthorityCertIssuerPresent())
	{
		const QList<GeneralName> & rList = iRHS.getAuthorityCertIssuer();
		const QList<GeneralName> & lList = iLHS.getAuthorityCertIssuer();
		if ( rList.size() != lList.size() ) return false;
		for (int i = 0 ; i < rList.size() ; i++ )
		{
			if ( rList[i] != lList[i]) return false;
		}
	}
	if ( iRHS.isAuthorityCertSerialNumberPresent())
	{
		if (iRHS.getAuthorityCertSerialNumber() != iLHS.getAuthorityCertSerialNumber())
			return false;
	}
	return true;
}

bool esya::operator!=(const AuthorityKeyIdentifier& iRHS, const AuthorityKeyIdentifier& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int AuthorityKeyIdentifier::copyFromASNObject(const ASN1T_IMP_AuthorityKeyIdentifier& iAKI)
{
	mKeyIdentifierPresent				= iAKI.m.keyIdentifierPresent;
	mAuthorityCertIssuerPresent			= iAKI.m.authorityCertIssuerPresent;
	mAuthorityCertSerialNumberPresent	= iAKI.m.authorityCertSerialNumberPresent;
	if ( mKeyIdentifierPresent )
		mKeyIdentifier = QByteArray((const char*)iAKI.keyIdentifier.data,iAKI.keyIdentifier.numocts);
	if ( mAuthorityCertIssuerPresent )
		GeneralName().copyGeneralNames(iAKI.authorityCertIssuer,mAuthorityCertIssuer);
	if ( mAuthorityCertSerialNumberPresent)
		mAuthorityCertSerialNumber = iAKI.authorityCertSerialNumber;
	return SUCCESS;	
}

int AuthorityKeyIdentifier::copyToASNObject(ASN1T_IMP_AuthorityKeyIdentifier & oAKI) const
{
	oAKI.m.keyIdentifierPresent				= mKeyIdentifierPresent;
	oAKI.m.authorityCertIssuerPresent		= mAuthorityCertIssuerPresent;
	oAKI.m.authorityCertSerialNumberPresent = mAuthorityCertSerialNumberPresent;
	if ( mKeyIdentifierPresent )
	{
		oAKI.keyIdentifier.data		= (OSOCTET*)myStrDup(mKeyIdentifier.data(),mKeyIdentifier.size());
		oAKI.keyIdentifier.numocts	= mKeyIdentifier.size();
	}
	if ( mAuthorityCertIssuerPresent )
		GeneralName().copyGeneralNames(mAuthorityCertIssuer,oAKI.authorityCertIssuer);

	if ( mAuthorityCertSerialNumberPresent)
		oAKI.authorityCertSerialNumber = myStrDup(mAuthorityCertSerialNumber );

	return SUCCESS;	
}

void AuthorityKeyIdentifier::freeASNObject(ASN1T_IMP_AuthorityKeyIdentifier & oAKI) const
{
	if ( oAKI.m.keyIdentifierPresent )
	{
		DELETE_MEMORY_ARRAY(oAKI.keyIdentifier.data)
	}
	if ( oAKI.m.authorityCertIssuerPresent )
		GeneralName().freeASNObjects(oAKI.authorityCertIssuer);

	if ( oAKI.m.authorityCertSerialNumberPresent )
		DELETE_MEMORY_ARRAY(oAKI.authorityCertSerialNumber)
}

const bool & AuthorityKeyIdentifier::isKeyIdentifierPresent()const 
{
	return mKeyIdentifierPresent;
}

const bool & AuthorityKeyIdentifier::isAuthorityCertIssuerPresent()const 
{
	return mAuthorityCertIssuerPresent;
}

const bool & AuthorityKeyIdentifier::isAuthorityCertSerialNumberPresent()const 
{
	return mAuthorityCertSerialNumberPresent;
}

const QByteArray & AuthorityKeyIdentifier::getKeyIdentifier()const
{
	return mKeyIdentifier;
}

const QList<GeneralName> & 	AuthorityKeyIdentifier::getAuthorityCertIssuer()const
{
	return mAuthorityCertIssuer;
}

const QString &	AuthorityKeyIdentifier::getAuthorityCertSerialNumber()const
{
	return mAuthorityCertSerialNumber;
}

AuthorityKeyIdentifier::~AuthorityKeyIdentifier(void)
{
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString AuthorityKeyIdentifier::eklentiAdiAl()			const 
{
	return DIL_EXT_YETKILI_ANAHTAR_TANIMLAYICISI;
}

QString AuthorityKeyIdentifier::eklentiKisaDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mKeyIdentifier);
}

QString AuthorityKeyIdentifier::eklentiUzunDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mKeyIdentifier);
}

AY_Eklenti* AuthorityKeyIdentifier::kendiniKopyala() const 
{
	return (AY_Eklenti* )new AuthorityKeyIdentifier(*this);
}

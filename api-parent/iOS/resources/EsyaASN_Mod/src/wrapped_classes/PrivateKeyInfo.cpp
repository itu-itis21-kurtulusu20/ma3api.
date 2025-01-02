#include "PrivateKeyInfo.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

PrivateKeyInfo::PrivateKeyInfo(void)
{
}

PrivateKeyInfo::PrivateKeyInfo( const ASN1T_PKCS18_PrivateKeyInfo &iPKI)
{
	copyFromASNObject(iPKI);
}

PrivateKeyInfo::PrivateKeyInfo( const PrivateKeyInfo &iPKI)
:	mPKAlgorithm(iPKI.getPKAlgorithm()),
	mPrivateKey(iPKI.getPrivateKey()),
	mVersion(iPKI.getVersion()),
	mAttributes(iPKI.getAttributes())
{
	
}

PrivateKeyInfo::PrivateKeyInfo( const QByteArray &iPKI)
{
	constructObject(iPKI);
}

PrivateKeyInfo & PrivateKeyInfo::operator=(const PrivateKeyInfo& iPKI)
{
	mPKAlgorithm	= iPKI.getPKAlgorithm();
	mPrivateKey		= iPKI.getPrivateKey();
	mVersion		= iPKI.getVersion();
	mAttributes		= iPKI.getAttributes();
	return *this;
}

bool operator==(const PrivateKeyInfo& iRHS,const PrivateKeyInfo& iLHS)
{
	return (	( iRHS.getPKAlgorithm() == iLHS.getPKAlgorithm())	&&
				( iRHS.getVersion()		== iLHS.getVersion()	)	&&
				( iRHS.getPrivateKey()	== iLHS.getPrivateKey() )		);
}

bool operator!=(const PrivateKeyInfo& iRHS, const PrivateKeyInfo& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int PrivateKeyInfo::copyFromASNObject(const ASN1T_PKCS18_PrivateKeyInfo & iPKI)
{
	mVersion = iPKI.version;
	mPKAlgorithm.copyFromASNObject(iPKI.privateKeyAlgorithm);
	mPrivateKey = toByteArray(iPKI.privateKey);
	if (iPKI.m.attributesPresent)
	{
		Attribute().copyAttributeList(iPKI.attributes,mAttributes);
	}
	return SUCCESS;
}

int PrivateKeyInfo::copyToASNObject(ASN1T_PKCS18_PrivateKeyInfo & oPKI) const
{
	oPKI.version = mVersion;
	mPKAlgorithm.copyToASNObject(oPKI.privateKeyAlgorithm);
	oPKI.privateKey.data = (OSOCTET*)myStrDup(mPrivateKey.data(),mPrivateKey.size());
	oPKI.privateKey.numocts = mPrivateKey.size();
	oPKI.m.attributesPresent = mAttributes.size()>0;
	if (oPKI.m.attributesPresent)
	{
		Attribute().copyAttributeList(mAttributes,oPKI.attributes);
	}
	return SUCCESS;
}
	
void PrivateKeyInfo::freeASNObject(ASN1T_PKCS18_PrivateKeyInfo& oPKI)const
{
	if (oPKI.m.attributesPresent)
	{
		Attribute().freeASNObjects(oPKI.attributes);
	}
	AlgorithmIdentifier().freeASNObject(oPKI.privateKeyAlgorithm);
	DELETE_MEMORY_ARRAY(oPKI.privateKey.data)
}

const OSUINT32 & PrivateKeyInfo::getVersion()const
{
	return mVersion;
}

const AlgorithmIdentifier &	PrivateKeyInfo::getPKAlgorithm()const 
{
	return mPKAlgorithm;
}

const QByteArray & PrivateKeyInfo::getPrivateKey() const 
{
	return mPrivateKey;
}

const QList<Attribute>& PrivateKeyInfo::getAttributes()const
{
	return mAttributes;
}

void PrivateKeyInfo::setVersion(const OSUINT32& iVersion)
{
	mVersion = iVersion;
}

void PrivateKeyInfo::setPKAlgorithm(const AlgorithmIdentifier& iPKAlgorithm)
{
	mPKAlgorithm = iPKAlgorithm;
}

void PrivateKeyInfo::setPrivateKey(const QByteArray& iPrivateKey)
{
	mPrivateKey = iPrivateKey;
}

void PrivateKeyInfo::addAttribute( const Attribute& iAttr)
{
	mAttributes.append(iAttr);
}


bool PrivateKeyInfo::isNull() const
{
	return mPrivateKey.isEmpty();
}

PrivateKeyInfo::~PrivateKeyInfo(void)
{
}

NAMESPACE_END

#include "EncryptedContentInfo.h"
#include "KriptoUtils.h"

using namespace esya;

EncryptedContentInfo::EncryptedContentInfo(void)
{
}

EncryptedContentInfo::EncryptedContentInfo( const QByteArray& iEncryptedContent, const AlgorithmIdentifier& iContentEncAlg, const ASN1T_CMS_ContentType iContentType)
:	mEncryptedContent(iEncryptedContent),
	mContentType(iContentType),
	mContentEncAlg(iContentEncAlg)
{
}
EncryptedContentInfo::EncryptedContentInfo( const ASN1T_PKCS7_EncryptedContentInfo & iECI)
{
	copyFromASNObject(iECI);
}

EncryptedContentInfo::EncryptedContentInfo( const EncryptedContentInfo &iECI)
:	mEncryptedContent(iECI.getEncryptedContent()),
	mContentType(iECI.getContentType()),
	mContentEncAlg(iECI.getContentEncAlg())
{
	
}

EncryptedContentInfo::EncryptedContentInfo( const QByteArray &iECI)
{
	constructObject(iECI);
}

EncryptedContentInfo & EncryptedContentInfo::operator=(const EncryptedContentInfo & iECI)
{
	mEncryptedContent	= iECI.getEncryptedContent();
	mContentType		= iECI.getContentType();
	mContentEncAlg		= iECI.getContentEncAlg();
	return *this;
}

bool esya::operator==(const EncryptedContentInfo& iRHS,const EncryptedContentInfo& iLHS)
{
	return (	(iRHS.getContentEncAlg() == iLHS.getContentEncAlg())&&
				(iRHS.getContentType() == iLHS.getContentType())&&
				(iRHS.getEncryptedContent() == iLHS.getEncryptedContent())	);
}

bool esya::operator!=(const EncryptedContentInfo& iRHS, const EncryptedContentInfo& iLHS)
{
	return ( !( iRHS == iLHS) );
}

int EncryptedContentInfo::copyFromASNObject(const ASN1T_PKCS7_EncryptedContentInfo & iECI)
{
	mContentType = iECI.contentType;
	mContentEncAlg.copyFromASNObject(iECI.contentEncryptionAlgorithm);
	if ( iECI.m.encryptedContentPresent )
		mEncryptedContent = toByteArray(iECI.encryptedContent);
	return SUCCESS;
}

int EncryptedContentInfo::copyToASNObject(ASN1T_PKCS7_EncryptedContentInfo & oECI) const
{
	oECI.contentType = mContentType;
	mContentEncAlg.copyToASNObject(oECI.contentEncryptionAlgorithm);
	oECI.m.encryptedContentPresent = (mEncryptedContent.size()>0);
	
	if (mEncryptedContent.size()>0)
	{
		oECI.encryptedContent.data = (OSOCTET*)myStrDup(mEncryptedContent.data(),mEncryptedContent.size());
		oECI.encryptedContent.numocts = mEncryptedContent.size();
	}
	return SUCCESS;

}

void EncryptedContentInfo::freeASNObject(ASN1T_PKCS7_EncryptedContentInfo& oECI)const
{
	AlgorithmIdentifier().freeASNObject(oECI.contentEncryptionAlgorithm);
	if (oECI.m.encryptedContentPresent)
	{
		DELETE_MEMORY_ARRAY(oECI.encryptedContent.data)
	}
}

QByteArray EncryptedContentInfo::getEncryptedContent() const 
{
	return mEncryptedContent;
}

const AlgorithmIdentifier& EncryptedContentInfo::getContentEncAlg() const 
{
	return mContentEncAlg;
}

const ASN1T_CMS_ContentType & EncryptedContentInfo::getContentType() const 
{
	return mContentType;
}
	
void EncryptedContentInfo::setContentEncAlg(const AlgorithmIdentifier& iEncAlg)
{
	mContentEncAlg = iEncAlg;
}
void EncryptedContentInfo::setContentType(const ASN1T_CMS_ContentType& iContentType)
{
	mContentType = iContentType;
}

void EncryptedContentInfo::setEncryptedContent(const QByteArray& iEncryptedContent)
{
	mEncryptedContent = iEncryptedContent;
}

QByteArray EncryptedContentInfo::decrypt(const QByteArray& iKey)const
{
	QByteArray plainData;
	try
	{
		plainData = KriptoUtils::decryptDataSymmetric(mEncryptedContent,iKey,mContentEncAlg);			
	}
	catch ( EException& exc )
	{
		throw exc.append("EncryptedContentInfo::decrypt()");		
	}
	return plainData;
}

QByteArray EncryptedContentInfo::decryptPBE(const QString& iParola)const
{
	QByteArray plainData;
	try
	{
		plainData = KriptoUtils::decryptPKCS12PBE(mEncryptedContent,iParola,mContentEncAlg);			
	}
	catch ( EException& exc )
	{
		throw exc.append("EncryptedContentInfo::decryptPBE()");		
	}
	return plainData;
}



EncryptedContentInfo::~EncryptedContentInfo(void)
{
}

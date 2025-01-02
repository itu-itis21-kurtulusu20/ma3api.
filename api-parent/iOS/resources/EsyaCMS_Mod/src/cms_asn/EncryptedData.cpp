#include "EncryptedData.h"
#include "ESeqOfList.h"

using namespace esya;

EncryptedData::EncryptedData(void)
{
}


EncryptedData::~EncryptedData(void)
{
}

EncryptedData::EncryptedData( const ASN1T_CMS_EncryptedData & iEncryptedData)
{
	copyFromASNObject(iEncryptedData);
}


EncryptedData::EncryptedData( const EncryptedData & iEncryptedData)
:	mVersion(iEncryptedData.getVersion()), 
	mEncryptedContentInfo(iEncryptedData.getEncryptedContentInfo()),
	mUnprotectedAttributes(iEncryptedData.getUnprotectedAttributes())
{
}


EncryptedData::EncryptedData( const QByteArray &iEncData )
{
	constructObject(iEncData);
}

EncryptedData & EncryptedData::operator=(const EncryptedData&iEncData)
{
	mVersion				= iEncData.getVersion();
	mUnprotectedAttributes	= iEncData.getUnprotectedAttributes();
	mEncryptedContentInfo   = iEncData.getEncryptedContentInfo();
	return *this;
}

bool esya::operator==(const EncryptedData& iRHS,const EncryptedData& iLHS)
{
	return ( ( iRHS.getEncryptedContentInfo() == iLHS.getEncryptedContentInfo() ) && 
			 (iRHS.getVersion()== iLHS.getVersion()) );
}

bool esya::operator!=(const EncryptedData& iRHS, const EncryptedData& iLHS)
{
	return (!(iRHS==iLHS));
}

int EncryptedData::copyFromASNObject(const ASN1T_CMS_EncryptedData & iEncData)
{
	mVersion = iEncData.version;
	mEncryptedContentInfo.copyFromASNObject(iEncData.encryptedContentInfo);
	
	if (iEncData.m.unprotectedAttrsPresent)
	{
		Attribute().copyAttributeList(iEncData.unprotectedAttrs,mUnprotectedAttributes);
	}
	return SUCCESS;

}

int EncryptedData::copyToASNObject(ASN1T_CMS_EncryptedData & oEncData) const
{
	oEncData.version = mVersion;
	mEncryptedContentInfo.copyToASNObject(oEncData.encryptedContentInfo);	
	oEncData.m.unprotectedAttrsPresent = (mUnprotectedAttributes.size()>0);
	if ( mUnprotectedAttributes.size()>0)
	{
		Attribute().copyAttributeList(mUnprotectedAttributes,oEncData.unprotectedAttrs);
	}
	return SUCCESS;
}
	
void EncryptedData::freeASNObject(ASN1T_CMS_EncryptedData & oEncData)const
{
	EncryptedContentInfo().freeASNObject(oEncData.encryptedContentInfo);
	if (oEncData.m.unprotectedAttrsPresent) 
		Attribute().freeASNObjects(oEncData.unprotectedAttrs);
}


EncryptedContentInfo EncryptedData::getEncryptedContentInfo() const 
{
	return mEncryptedContentInfo;
}

const ASN1T_CMS_CMSVersion & EncryptedData::getVersion()const
{
	return mVersion;
}

const QList<Attribute> & EncryptedData::getUnprotectedAttributes()const 
{
	return mUnprotectedAttributes;
}
	
QByteArray EncryptedData::getPlainData()
{
	// todo: Buraya crypto modulünden gelecek classlarla decrypt iþlemi eklenecek
	
	

	return QByteArray();
}

void EncryptedData::setEncryptedContentInfo(const EncryptedContentInfo & iECI)
{
	mEncryptedContentInfo = iECI;
}

void EncryptedData::setVersion(const int iVersion)
{
	mVersion = iVersion;
}

void EncryptedData::setUnprotectedAttributes(const QList<Attribute> & iUnpAttr)
{
	mUnprotectedAttributes = iUnpAttr;
}


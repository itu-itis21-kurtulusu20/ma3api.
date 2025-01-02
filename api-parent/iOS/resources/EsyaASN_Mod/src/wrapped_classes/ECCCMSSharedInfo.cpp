#include "ECCCMSSharedInfo.h"

using namespace esya;

ECCCMSSharedInfo::ECCCMSSharedInfo(const ECCCMSSharedInfo& iSI)
:	mKeyInfo(iSI.getKeyInfo()),
	mEntityUInfo(iSI.getEntityUInfo()),
	mEntityUInfoPresent(iSI.isEntityUInfoPresent()),
	mSuppPubInfo(iSI.getSuppPubInfo())
{}

ECCCMSSharedInfo::ECCCMSSharedInfo(const ASN1T_DERCMS_ECC_CMS_SharedInfo & iSI)
{
	copyFromASNObject(iSI);
}

ECCCMSSharedInfo::ECCCMSSharedInfo(const QByteArray & iSI)
{
	constructObject(iSI);
}

ECCCMSSharedInfo::ECCCMSSharedInfo(void)
:	mEntityUInfoPresent(false)
{}


ECCCMSSharedInfo& ECCCMSSharedInfo::operator=(const ECCCMSSharedInfo&iSI)
{
	mKeyInfo = iSI.getKeyInfo();
	mEntityUInfo = iSI.getEntityUInfo();
	mEntityUInfoPresent = iSI.isEntityUInfoPresent();
	mSuppPubInfo = iSI.getSuppPubInfo();
	return *this;
}

bool esya::operator==(const ECCCMSSharedInfo& iRHS,const ECCCMSSharedInfo& iLHS)
{
	if (iRHS.isEntityUInfoPresent()!= iLHS.isEntityUInfoPresent())
		return false;

	if (iRHS.isEntityUInfoPresent() && (iRHS.getEntityUInfo()!= iLHS.getEntityUInfo()) )
		return false;

	return ( ( iRHS.getSuppPubInfo()	== iLHS.getSuppPubInfo() ) &&
			 ( iRHS.getKeyInfo()		== iLHS.getKeyInfo())	);

}

bool esya::operator!=(const ECCCMSSharedInfo& iRHS, const ECCCMSSharedInfo& iLHS)
{
	return ( !( iRHS == iLHS ) ); 
}

int ECCCMSSharedInfo::copyFromASNObject(const ASN1T_DERCMS_ECC_CMS_SharedInfo& iSI)
{
	mKeyInfo.copyFromASNObject(iSI.keyInfo);
	mSuppPubInfo = QByteArray((const char*)iSI.suppPubInfo.data,iSI.suppPubInfo.numocts ) ;
	
	mEntityUInfoPresent = iSI.m.entityUInfoPresent;
	if (mEntityUInfoPresent)
		mEntityUInfo= QByteArray((const char*)iSI.entityUInfo.data,iSI.entityUInfo.numocts ) ;

	return SUCCESS;
}


int ECCCMSSharedInfo::copyToASNObject(ASN1T_DERCMS_ECC_CMS_SharedInfo & oSI) const
{
	mKeyInfo.copyToASNObject(oSI.keyInfo);
	
	oSI.suppPubInfo.numocts = mSuppPubInfo.size();
	oSI.suppPubInfo.data = (ASN1OCTET*)myStrDup(mSuppPubInfo.data(),oSI.suppPubInfo.numocts);

	oSI.m.entityUInfoPresent = mEntityUInfoPresent;
	if (mEntityUInfoPresent)
	{
		oSI.entityUInfo.numocts = mEntityUInfo.size();
		oSI.entityUInfo.data = (ASN1OCTET*)myStrDup(mEntityUInfo.data(),oSI.entityUInfo.numocts);
	}
	return SUCCESS;
}

void ECCCMSSharedInfo::freeASNObject(ASN1T_DERCMS_ECC_CMS_SharedInfo& oSI)const
{
	AlgorithmIdentifier().freeASNObject(oSI.keyInfo);
	
	DELETE_MEMORY_ARRAY(oSI.suppPubInfo.data);
	if (oSI.m.entityUInfoPresent)
		DELETE_MEMORY_ARRAY(oSI.entityUInfo.data);

}

bool ECCCMSSharedInfo::isEntityUInfoPresent()const
{
	return mEntityUInfoPresent;
}

const AlgorithmIdentifier& ECCCMSSharedInfo::getKeyInfo() const
{
	return mKeyInfo;
}

const QByteArray&  ECCCMSSharedInfo::getEntityUInfo()const
{
	return mEntityUInfo;
}

const QByteArray & ECCCMSSharedInfo::getSuppPubInfo() const 
{
	return mSuppPubInfo;
}

void ECCCMSSharedInfo::setKeyInfo(const AlgorithmIdentifier& iKeyInfo)
{
	mKeyInfo = iKeyInfo;
}

void ECCCMSSharedInfo::setEntityUInfo(const QByteArray&  iEntityUInfo)
{
	mEntityUInfoPresent =(!iEntityUInfo.isEmpty());
	mEntityUInfo = iEntityUInfo;
}

void ECCCMSSharedInfo::setSuppPubInfo(const QByteArray & iSuppPubInfo)
{
	mSuppPubInfo = iSuppPubInfo;
}

ECCCMSSharedInfo::~ECCCMSSharedInfo(void)
{
}
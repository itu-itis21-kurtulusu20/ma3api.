#include "EncapContentInfo.h"

using namespace esya;

EncapContentInfo::EncapContentInfo(void)
{
}

EncapContentInfo::EncapContentInfo(const ASN1T_CMS_EncapsulatedContentInfo & iECI)
{
	copyFromASNObject(iECI);
}

EncapContentInfo::EncapContentInfo(const QByteArray & iECI)
{
	constructObject(iECI);
}

int EncapContentInfo::copyFromASNObject(const ASN1T_CMS_EncapsulatedContentInfo & iECI)
{
	mEContentType = iECI.eContentType;	
	mEContentPresent = iECI.m.eContentPresent;
	if (mEContentPresent)
		mEContent	  =  toByteArray(iECI.eContent);
	

	return SUCCESS;
}


EncapContentInfo::EncapContentInfo(const EncapContentInfo & iECI)
:	mEContent(iECI.getEContent()),
	mEContentType(iECI.getEContentType()),
	mEContentPresent(iECI.getEContentPresent())
{
}

EncapContentInfo::EncapContentInfo(const QByteArray & iEContent,  const ASN1T_CMS_ContentType &iEContentType)
:	mEContent(iEContent), 
	mEContentType(iEContentType),
	mEContentPresent(true)
{
}

EncapContentInfo::EncapContentInfo(const ASN1T_CMS_ContentType &iEContentType)
:	mEContentType(iEContentType),
	mEContentPresent(false)
{
}

EncapContentInfo::EncapContentInfo(ASN1T_PKCS7_Data & iData)
{
	ASN1BEREncodeBuffer encBuf;
	ASN1C_PKCS7_Data cData(iData);
	int stat = cData.EncodeTo(encBuf);	
	if ( stat < ASN_OK )
	{
		throw EException(QString(" Data Encode edilemedi. Hata : %1").arg(stat));
	}

	QByteArray dataBytes( (char*)encBuf.GetMsgPtr(),stat);

	mEContentPresent = true; 
	mEContent		 = dataBytes;
	mEContentType	 = PKCS7_data;
}

EncapContentInfo& EncapContentInfo::operator=(const EncapContentInfo & iECI)
{
	mEContent			= iECI.getEContent();
	mEContentPresent	= iECI.getEContentPresent();
	mEContentType		= iECI.getEContentType();	
	return *this;
}


bool EncapContentInfo::getEContentPresent() const 
{
	return mEContentPresent;
}

void EncapContentInfo::setEContentPresent(const bool iECP) 
{
	mEContentPresent = iECP;
}



QByteArray EncapContentInfo::getEContent() const 
{
	return mEContent;
}


const ASN1T_PKCS7_ContentType & EncapContentInfo::getEContentType()const 
{
	return mEContentType;
}

void EncapContentInfo::setEContent(const  QByteArray & iEContent )
{
	mEContent = iEContent;
}

void EncapContentInfo::setEContentType(const  ASN1T_CMS_ContentType & iEContenType )
{
	mEContentType = iEContenType;
}

int EncapContentInfo::copyToASNObject(ASN1T_CMS_EncapsulatedContentInfo & oECI) const
{
	oECI.eContentType	=   mEContentType;
	oECI.eContent.numocts = mEContent.size();
	oECI.m.eContentPresent = (mEContent.size() > 0);
	if (oECI.m.eContentPresent)
		oECI.eContent.data	 =  (OSOCTET*)myStrDup((char*)mEContent.data(),oECI.eContent.numocts);
	return SUCCESS;
}

void EncapContentInfo::freeASNObject(ASN1T_CMS_EncapsulatedContentInfo & oECI) const
{
	if (oECI.m.eContentPresent && oECI.eContent.data && oECI.eContent.numocts>0)
	{
		DELETE_MEMORY_ARRAY(oECI.eContent.data)
	}
}

EncapContentInfo::~EncapContentInfo()
{
}
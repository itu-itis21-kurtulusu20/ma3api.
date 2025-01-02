#include "ContentInfo.h"
#include "ESeqOfList.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

ContentInfo::ContentInfo(void)
{
}

ContentInfo::ContentInfo(const ASN1T_PKCS7_ContentInfo & iContentInfo)
{
	copyFromASNObject(iContentInfo);
}

ContentInfo::ContentInfo(const QByteArray & iContentInfo)
{
	constructObject(iContentInfo);
}

ContentInfo::ContentInfo(const QString & iContentFile)
{
	loadFromFile(iContentFile);
}

int ContentInfo::copyFromASNObject(const ASN1T_PKCS7_ContentInfo & iContentInfo)
{
	mContentType = iContentInfo.contentType;	
	QByteArray contentBytes((char*)iContentInfo.content.data,iContentInfo.content.numocts);
	mContent = contentBytes;  
	return SUCCESS;
}

int ContentInfo::copyFromASNObject(const ASN1T_CMS_ContentInfo & iContentInfo)
{
	//return copyFromASNObject((const ASN1T_PKCS7_ContentInfo &)iContentInfo);
	mContentType = iContentInfo.contentType;	
	QByteArray contentBytes((char*)iContentInfo.content.data,iContentInfo.content.numocts);
	mContent = contentBytes;  
	return SUCCESS;
}


ContentInfo::ContentInfo(const ContentInfo & iContentInfo)
:mContent(iContentInfo.getContent()),mContentType(iContentInfo.getContentType())	
{
}



ContentInfo::ContentInfo(const QByteArray & iContent,  const ASN1T_PKCS7_ContentType &iContentType)
:mContent(iContent), mContentType(iContentType)
{
}

ContentInfo& ContentInfo::operator=(const ContentInfo& iCI)
{
	mContent		= iCI.getContent();
	mContentType	= iCI.getContentType();	
	return *this;
}

bool operator==( const ContentInfo& iRHS, const ContentInfo& iLHS)
{
	return (	iRHS.getContentType()	== iLHS.getContentType() && 
				iRHS.getContent()		== iLHS.getContent()		);
}

bool operator!=( const ContentInfo& iRHS, const ContentInfo& iLHS)
{
	return (!(iRHS==iLHS)); 
}


ContentInfo::ContentInfo(ASN1T_PKCS7_Data & iData)
{
	ASN1BEREncodeBuffer encBuf;
	ASN1C_PKCS7_Data cData(iData);
	int stat = cData.EncodeTo(encBuf);	
	if ( stat < ASN_OK )
	{
		throw EException(QString(" Data Encode edilemedi. Hata : %1").arg(stat));
	}

	QByteArray dataBytes( (char*)encBuf.GetMsgPtr(),stat);

	mContent = dataBytes;
	mContentType = PKCS7_data;
}



const QByteArray& ContentInfo::getContent() const 
{
	return mContent;
}


const ASN1T_PKCS7_ContentType & ContentInfo::getContentType()const 
{
	return mContentType;
}

void ContentInfo::setContent(const  QByteArray & iContent )
{
	mContent = iContent;
}

void ContentInfo::setContentType(const  ASN1T_PKCS7_ContentType & iContenType )
{
	mContentType = iContenType;
}

int ContentInfo::copyToASNObject(ASN1T_PKCS7_ContentInfo & oContentInfo) const
{
	oContentInfo.contentType		= mContentType;
	oContentInfo.content.numocts	= mContent.size();
	oContentInfo.content.data		= (OSOCTET*)myStrDup((char*)mContent.data(),oContentInfo.content.numocts);
	oContentInfo.m.contentPresent	= (mContent.size()>0) ; 
	return SUCCESS;
}

int ContentInfo::copyToASNObject(ASN1T_CMS_ContentInfo & oContentInfo) const
{
	return copyToASNObject((ASN1T_PKCS7_ContentInfo &)oContentInfo);
// 	oContentInfo.contentType		= mContentType;
// 	oContentInfo.content.numocts	= mContent.size();
// 	oContentInfo.content.data		= (OSOCTET*)myStrDup((char*)mContent.data(),oContentInfo.content.numocts);
// 	return SUCCESS;
}

void ContentInfo::freeASNObject(ASN1T_PKCS7_ContentInfo & oContentInfo)const
{
	if (oContentInfo.content.data && oContentInfo.content.numocts>0)
	{
		DELETE_MEMORY_ARRAY(oContentInfo.content.data)
	}
}

void ContentInfo::freeASNObject(ASN1T_CMS_ContentInfo & oContentInfo)const
{
	freeASNObject((ASN1T_PKCS7_ContentInfo &) oContentInfo);
// 	if (oContentInfo.content.data && oContentInfo.content.numocts>0)
// 	{
// 		DELETE_MEMORY_ARRAY(oContentInfo.content.data)
// 	}
}


void ContentInfo::freeContenInfos(ASN1TPDUSeqOfList & oCIs)
{
	freeASNObjects(oCIs);
}

int  ContentInfo::copyContentInfos(const QList<ContentInfo> iList ,ASN1TPDUSeqOfList & oCIs)
{
	for (int i = 0 ; i<iList.size() ; i++	)
	{
		ASN1T_PKCS7_ContentInfo* pCI = iList[i].getASNCopy();
		ESeqOfList::append(oCIs,pCI);
	}
	return SUCCESS;
}

int	ContentInfo::copyContentInfos(const ASN1TPDUSeqOfList & iCIs, QList<ContentInfo>& oList)
{
	for (int i = 0 ; i<iCIs.count ; i++	)
	{
		ASN1T_PKCS7_ContentInfo* pCI = (ASN1T_PKCS7_ContentInfo*)ESeqOfList::get(iCIs,i);
		oList.append(ContentInfo(*pCI));
	}
	return SUCCESS;

}

ContentInfo::~ContentInfo()
{
}
NAMESPACE_END


#include "EsyaFileInfoRecipientInfo.h"
#include "asn1compat.h"
#include "EsyaCMS_DIL.h"

using namespace esya;

ESYAFileInfoRecipientInfo::ESYAFileInfoRecipientInfo()
: mGizlilikBilgisi(EGB_Tanimlanmamis)
{
}

ESYAFileInfoRecipientInfo::ESYAFileInfoRecipientInfo(const QByteArray & iEFIRI)
{
	constructObject(iEFIRI);
}

ESYAFileInfoRecipientInfo::ESYAFileInfoRecipientInfo(const ASN1T_ESYA_ESYAFileInfoRecipientInfo & iEFIRI)
{
	copyFromASNObject(iEFIRI);
}

ESYAFileInfoRecipientInfo::ESYAFileInfoRecipientInfo(const ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi &iGizlilikBilgisi)
: mGizlilikBilgisi(iGizlilikBilgisi)
{
}

ESYAFileInfoRecipientInfo::ESYAFileInfoRecipientInfo(const ESYAFileInfoRecipientInfo& iEFIRI)
:	mGizlilikBilgisi(iEFIRI.getGizlilikBilgisi())
{
}


ESYAFileInfoRecipientInfo & ESYAFileInfoRecipientInfo::operator=(const ESYAFileInfoRecipientInfo & iEFIRI)
{
	mGizlilikBilgisi = iEFIRI.getGizlilikBilgisi();
	return *this;
}

bool operator==(const ESYAFileInfoRecipientInfo & iLHS,const ESYAFileInfoRecipientInfo & iRHS)
{
	return ( iLHS.getGizlilikBilgisi() == iRHS.getGizlilikBilgisi());
}

bool operator!=(const ESYAFileInfoRecipientInfo & iLHS,const ESYAFileInfoRecipientInfo & iRHS)
{
	return ( iLHS.getGizlilikBilgisi() != iRHS.getGizlilikBilgisi());
}

int ESYAFileInfoRecipientInfo::copyFromASNObject(const ASN1T_ESYA_ESYAFileInfoRecipientInfo & iEFIRI)
{
	mGizlilikBilgisi = (ESYAGizlilikBilgisi)iEFIRI.gizlilikBilgisi ;
	return SUCCESS;
}

int ESYAFileInfoRecipientInfo::copyToASNObject(ASN1T_ESYA_ESYAFileInfoRecipientInfo& oEFIRI)const
{
	oEFIRI.gizlilikBilgisi = (ASN1T_ESYA_ESYAGizlilikBilgisi)mGizlilikBilgisi;
	return SUCCESS;
}

void ESYAFileInfoRecipientInfo::freeASNObject(ASN1T_ESYA_ESYAFileInfoRecipientInfo & oEFIRI)const
{
}


const ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi & ESYAFileInfoRecipientInfo::getGizlilikBilgisi()		const
{
	return mGizlilikBilgisi;
}

void ESYAFileInfoRecipientInfo::setGizlilikBilgisi(const ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi &iGizlilikBilgisi)
{
	mGizlilikBilgisi = iGizlilikBilgisi;
}

QString ESYAFileInfoRecipientInfo::gizlilikBilgisi2String(const ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi & iGB)
{
	switch ( iGB)	
	{
		case EGB_Tanimlanmamis	: return DIL_GB_TANIMLANMAMIS;
		case EGB_Gizli			: return DIL_GB_GIZLI;
		case EGB_Ozel			: return DIL_GB_OZEL;
		case EGB_HizmeteOzel	: return DIL_GB_HIZMETEOZEL;
		case EGB_TasnifDisi		: return DIL_GB_TASNIFDISI;
		default					: return DIL_GB_TANIMLANMAMIS;
	}
}

QList<ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi> ESYAFileInfoRecipientInfo::listGizlilikBilgisi() 
{
	QList<ESYAFileInfoRecipientInfo::ESYAGizlilikBilgisi> gbList;
	gbList.append(EGB_Tanimlanmamis);
	gbList.append(EGB_Gizli);
	gbList.append(EGB_Ozel);
	gbList.append(EGB_HizmeteOzel);
	gbList.append(EGB_TasnifDisi);
	return gbList;
};

ESYAFileInfoRecipientInfo::~ESYAFileInfoRecipientInfo()
{

}

#include "RevokedCertificatesElement.h"

using namespace esya;

RevokedCertificatesElement::RevokedCertificatesElement(void)
{
}

RevokedCertificatesElement::RevokedCertificatesElement(const ASN1T_EXP_TBSCertList_revokedCertificates_element &iRCE)
{
	copyFromASNObject(iRCE);
}

RevokedCertificatesElement::RevokedCertificatesElement(const QByteArray & iRCE)
{
	constructObject(iRCE);
}

RevokedCertificatesElement::RevokedCertificatesElement(const RevokedCertificatesElement &iRCE)
:	mUserCertificate(iRCE.getUserCertificate()),
	mRevocationDate(iRCE.getRevocationDate()),
	mCRLEntryExtensions(iRCE.getCRLEntryExtensions())
{
}

RevokedCertificatesElement & RevokedCertificatesElement::operator=(const RevokedCertificatesElement& iRCE)
{
	mUserCertificate = iRCE.getUserCertificate();
	mRevocationDate  = iRCE.getRevocationDate();
	mCRLEntryExtensions   = iRCE.getCRLEntryExtensions();
	return (*this);
}

bool esya::operator==(const RevokedCertificatesElement & iRHS,const RevokedCertificatesElement & iLHS)
{
	return ( iRHS.getUserCertificate()==iLHS.getUserCertificate());
}

bool esya::operator!=(const RevokedCertificatesElement & iRHS, const RevokedCertificatesElement & iLHS)
{
	return (!(iRHS == iLHS));
}

int RevokedCertificatesElement::copyFromASNObject(const ASN1T_EXP_TBSCertList_revokedCertificates_element &iRCE )
{
	mUserCertificate = QString(iRCE.userCertificate);
	mRevocationDate = ETime(iRCE.revocationDate);
	if (iRCE.m.crlEntryExtensionsPresent)
	{
		Extension().copyExtensions(iRCE.crlEntryExtensions,mCRLEntryExtensions);
	}
	return SUCCESS;
}

int RevokedCertificatesElement::copyToASNObject(ASN1T_EXP_TBSCertList_revokedCertificates_element & oRCE) const
{
	mRevocationDate.copyToASNObject(oRCE.revocationDate);
	oRCE.userCertificate = myStrDup(mUserCertificate);	
	oRCE.m.crlEntryExtensionsPresent = (mCRLEntryExtensions.size()>0);
	if (oRCE.m.crlEntryExtensionsPresent)
	{
		Extension().copyExtensions(mCRLEntryExtensions,oRCE.crlEntryExtensions);
	}
	return SUCCESS;
}
	
void RevokedCertificatesElement::freeASNObject(ASN1T_EXP_TBSCertList_revokedCertificates_element & oRCE)const
{
	ETime().freeASNObject(oRCE.revocationDate);
	if ( oRCE.m.crlEntryExtensionsPresent )
		Extension().freeASNObjects(oRCE.crlEntryExtensions);
	DELETE_MEMORY_ARRAY(oRCE.userCertificate);
}

int RevokedCertificatesElement::copyRCEs(const ASN1T_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element & iRCEs, QList<RevokedCertificatesElement>& oList)
{
	return copyASNObjects<RevokedCertificatesElement>(iRCEs,oList);
}

int RevokedCertificatesElement::copyRCEs(const QList<RevokedCertificatesElement> iList ,ASN1T_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element & oRCEs)
{
	return copyASNObjects<RevokedCertificatesElement>(iList,oRCEs);
}

int RevokedCertificatesElement::copyRCEs(const QByteArray& iList ,QList<RevokedCertificatesElement> & oRCEs)
{
	return copyASNObjects<ASN1T_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element,ASN1C_EXP__SeqOfEXP_TBSCertList_revokedCertificates_element,RevokedCertificatesElement>(iList,oRCEs);
}

const QString& RevokedCertificatesElement::getUserCertificate() const
{
	return mUserCertificate;
}

const ETime& RevokedCertificatesElement::getRevocationDate() const
{
	return mRevocationDate;
}

const QList<Extension>& RevokedCertificatesElement::getCRLEntryExtensions() const 
{
	return mCRLEntryExtensions;
}

int RevokedCertificatesElement::silNedeniAl() const
{
	if( mCRLEntryExtensions.size()== 0 )
	{
		return  IMP_CRLReason::unspecified;
	}
	for(int i = 0; i < mCRLEntryExtensions.size();i ++)
	{
		if (mCRLEntryExtensions[i].getExtensionId() == ( ASN1TObjId ) IMP_id_ce_cRLReasons )
        {
			QByteArray value = mCRLEntryExtensions[i].getExtensionValue();
			ASN1BERDecodeBuffer decBuf((ASN1OCTET*)value.data(),value.size());
			ASN1T_IMP_CRLReason crlReason;
			ASN1C_IMP_CRLReason cCRLReason(crlReason);
			int stat = cCRLReason.DecodeFrom(decBuf);
			if ( stat == ASN_OK )
				return crlReason;
			else 
				return IMP_CRLReason::unspecified;

        }
	}
	return IMP_CRLReason::unspecified;
}

int RevokedCertificatesElement::readFromStream(OSRTStream * pStream )
{
	if (!pStream) return FAILURE;

	ASN1T_EXP_TBSCertList_revokedCertificates_element rce;
	int stat = asn1D_EXP_TBSCertList_revokedCertificates_element(pStream->getCtxtPtr(),&rce,ASN1EXPL,0); 

	if (stat != 0) 
	{
		throw EException("RevokedCertificatesElement Decode edilemedi");
	}

	return copyFromASNObject(rce);
}

void RevokedCertificatesElement::setUserCertificate(const QString& iUserCert)
{
	mUserCertificate = iUserCert;
}

void RevokedCertificatesElement::setRevocationDate(const ETime& iRD)
{
	mRevocationDate = iRD;
}

void RevokedCertificatesElement::setCRLEntryiExtensions(const QList<Extension>& iCE)
{
	mCRLEntryExtensions = iCE;
}


RevokedCertificatesElement::~RevokedCertificatesElement(void)
{

}

#include "TBSCertList.h"



using namespace esya;

TBSCertList::TBSCertList(void)
{
}

TBSCertList::TBSCertList(const ASN1T_EXP_TBSCertList & iTCL)
{
	copyFromASNObject(iTCL);	
}

TBSCertList::TBSCertList(const TBSCertList &  iTCL)
:	mVersionPresent(iTCL.isVersionPresent()),
	mVersion(iTCL.getVersion()),
	mNextUpdate(iTCL.getNextUpdate()),
	mIssuer(iTCL.getIssuer()),
	mNextUpdatePresent(iTCL.isNextUpdatePresent()),
	mCRLExtensions(iTCL.getCRLExtensions()),
	mThisUpdate(iTCL.getThisUpdate()),
	mSignature(iTCL.getSignature()),
	mRevokedCertificates(iTCL.getRevokedCertificates())
{
}

TBSCertList::TBSCertList(const  QByteArray &iTCL)
{
	constructObject(iTCL);
}

TBSCertList& TBSCertList::operator=(const TBSCertList& iTCL )
{
	mVersionPresent = iTCL.isVersionPresent();
	mVersion = iTCL.getVersion();
	mNextUpdate = iTCL.getNextUpdate();
	mIssuer = iTCL.getIssuer();
	mNextUpdatePresent = iTCL.isNextUpdatePresent();
	mCRLExtensions = iTCL.getCRLExtensions();
	mThisUpdate = iTCL.getThisUpdate();
	mSignature = iTCL.getSignature();
	mRevokedCertificates = iTCL.getRevokedCertificates();
	return (*this);
}

bool esya::operator==( const TBSCertList & iRHS , const TBSCertList& iLHS)
{
	if ( (iRHS.getIssuer() == iLHS.getIssuer()) &&
		 (iRHS.getThisUpdate() == iLHS.getThisUpdate()) &&
		 (iRHS.getNextUpdate() == iLHS.getNextUpdate()) &&
		 (iRHS.getCRLExtensions() == iLHS.getCRLExtensions()) &&
		 (iRHS.getRevokedCertificates() == iLHS.getRevokedCertificates())  )
		 return true;
	return false;
}
bool esya::operator!=( const TBSCertList & iRHS , const TBSCertList& iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int TBSCertList::copyFromASNObject(const ASN1T_EXP_TBSCertList & iTCL)
{
	mVersionPresent = iTCL.m.versionPresent;
	mNextUpdatePresent = iTCL.m.nextUpdatePresent;
	mVersion	= iTCL.version;
	mNextUpdate = ETime(iTCL.nextUpdate);
	mThisUpdate = ETime(iTCL.thisUpdate);
	mIssuer		= Name(iTCL.issuer);
	mSignature	= AlgorithmIdentifier(iTCL.signature_);
	if (iTCL.m.crlExtensionsPresent)
	{
		for (int i = 0 ; i < iTCL.crlExtensions.count ; i++ )
		{
			ASN1T_EXP_Extension * pExt = (ASN1T_EXP_Extension *)ESeqOfList::get(iTCL.crlExtensions,i);
			mCRLExtensions.append(Extension(*pExt));
		}
	}
	if (iTCL.m.revokedCertificatesPresent)
	{	
		OSRTDListNode * nodePtr =  iTCL.revokedCertificates.head;
		while(nodePtr)
		{
			ASN1T_EXP_TBSCertList_revokedCertificates_element * pRCE =(ASN1T_EXP_TBSCertList_revokedCertificates_element *) nodePtr->data;
			mRevokedCertificates.append(RevokedCertificatesElement(*pRCE));
			nodePtr = nodePtr->next;
		}
	}
	return SUCCESS;
}

int TBSCertList::copyToASNObject(ASN1T_EXP_TBSCertList &oTCL ) const
{
	oTCL.m.versionPresent = mVersionPresent;
	oTCL.m.nextUpdatePresent = mNextUpdatePresent;
	oTCL.m.crlExtensionsPresent = (mCRLExtensions.size() > 0 );
	oTCL.m.revokedCertificatesPresent= (mRevokedCertificates.size() > 0 );
	oTCL.version = mVersion;
	mNextUpdate.copyToASNObject(oTCL.nextUpdate);
	mThisUpdate.copyToASNObject(oTCL.thisUpdate);
	mIssuer.copyToASNObject(oTCL.issuer);
	mSignature.copyToASNObject(oTCL.signature_);
	
	if (oTCL.m.crlExtensionsPresent)
	{
		Extension().copyExtensions(mCRLExtensions,oTCL.crlExtensions);
	}
	if (oTCL.m.revokedCertificatesPresent)
	{
		RevokedCertificatesElement().copyRCEs(mRevokedCertificates,oTCL.revokedCertificates);
	}
	return SUCCESS;
}

void TBSCertList::freeASNObject(ASN1T_EXP_TBSCertList & oTCL) const
{
	Name().freeASNObject(oTCL.issuer);
	ETime().freeASNObject(oTCL.nextUpdate);
	ETime().freeASNObject(oTCL.thisUpdate);
	AlgorithmIdentifier().freeASNObject(oTCL.signature_);
	if ( oTCL.m.crlExtensionsPresent)
		Extension().freeASNObjects(oTCL.crlExtensions);
	if ( oTCL.m.revokedCertificatesPresent)
		RevokedCertificatesElement().freeASNObjects(oTCL.revokedCertificates);
}

const bool& TBSCertList::isVersionPresent()const
{
	return mVersionPresent;
}

const bool& TBSCertList::isNextUpdatePresent()const
{
	return mNextUpdatePresent;
}

const int&	TBSCertList::getVersion()const 
{
	return mVersion;
}

void TBSCertList::setVersion(const int iVersion)
{
	mVersion = iVersion;
}

void TBSCertList::setVersionPresent(const bool iVersionPresent)
{
	mVersionPresent= iVersionPresent;
}


const Name& TBSCertList::getIssuer()const
{
	return mIssuer;
}


void TBSCertList::setIssuer(const Name & iIssuer)
{
	mIssuer = iIssuer;
}


const ETime&	TBSCertList::getThisUpdate()const
{
	return mThisUpdate;
}

void TBSCertList::setThisUpdate(const ETime & iThisUpdate)
{
	mThisUpdate= iThisUpdate;
}


const ETime&	TBSCertList::getNextUpdate()const
{
	return mNextUpdate;
}

void TBSCertList::setNextUpdate(const ETime & iNextUpdate)
{
	mNextUpdate= iNextUpdate;
}

void TBSCertList::setNextUpdatePresent(const bool & iNextUpdatePresent)
{
	mNextUpdatePresent= iNextUpdatePresent;
}


const AlgorithmIdentifier&	TBSCertList::getSignature()const 
{
	return mSignature;
}

void TBSCertList::setSignature(const AlgorithmIdentifier & iSignature)
{
	mSignature= iSignature;
}


const QList<Extension>&	TBSCertList::getCRLExtensions()const
{
	return mCRLExtensions;
}

void TBSCertList::setCRLExtensions(const QList<Extension>& iCRLExtensions)
{
	mCRLExtensions = iCRLExtensions;
}



const QList<RevokedCertificatesElement> & TBSCertList::getRevokedCertificates()const
{
	return mRevokedCertificates;
}

void TBSCertList::setRevokedCertificates(const QList<RevokedCertificatesElement> & iRevokedCertificates)
{
	mRevokedCertificates = iRevokedCertificates;
}

int TBSCertList::getExtension(ASN1OBJID iExtnID, Extension& oExtension )const
{
	for (int i = 0 ; i<mCRLExtensions.size(); i++ )
	{
		if ( (ASN1OBJID)mCRLExtensions[i].getExtensionId() == iExtnID )
		{
			oExtension = mCRLExtensions[i];
			return SUCCESS;
		}
	}
	return FAILURE;
}


int TBSCertList::readFromStream(OSRTStream * pStream )
{
	if (!pStream) return FAILURE;

	ASN1T_EXP_TBSCertList tbsCertList;
	int stat = asn1D_EXP_TBSCertList (pStream->getCtxtPtr(),&tbsCertList,ASN1EXPL,0); 

	if (stat != 0) 
	{
		throw EException("TBSCertList Decode edilemedi");
	}

	return copyFromASNObject(tbsCertList);
}


TBSCertList::~TBSCertList(void)
{
}

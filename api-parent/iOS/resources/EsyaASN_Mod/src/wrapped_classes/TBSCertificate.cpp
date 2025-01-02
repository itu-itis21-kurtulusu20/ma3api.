#include "TBSCertificate.h"

using namespace esya;

TBSCertificate::TBSCertificate(void)
{
}

TBSCertificate::TBSCertificate(const QByteArray & iTBSCertificate)
{
	constructObject(iTBSCertificate);
}

TBSCertificate::TBSCertificate(const ASN1T_EXP_TBSCertificate &iTBSCertificate )
{
	copyFromASNObject(iTBSCertificate);
}

TBSCertificate::TBSCertificate(const TBSCertificate& iTBSCertificate)
:	mIssuer(iTBSCertificate.getIssuer()),
	mIssuerUniqueID(iTBSCertificate.getIssuerUniqueID()),
	mIssuerUniqueIDPresent(iTBSCertificate.isIssuerUniqueIDPresent()),
	mSerialNumber(iTBSCertificate.getSerialNumber()),
	mSubject(iTBSCertificate.getSubject()),
	mSubjectPublicKeyInfo(iTBSCertificate.getSubjectPublicKeyInfo()),
	mSubjectUniqueID(iTBSCertificate.getSubjectUniqueID()),
	mSubjectUniqueIDPresent(iTBSCertificate.isSubjectUniqueIDPresent()),
	mSignature(iTBSCertificate.getSignature()),
	mVersion(iTBSCertificate.getVersion()),
	mExtensions(iTBSCertificate.getExtensions()),
	mExtensionsPresent(iTBSCertificate.isExtensionsPresent()),
	mNotAfter(iTBSCertificate.getNotAfter()),
	mNotBefore(iTBSCertificate.getNotBefore())
{
	
}


TBSCertificate & TBSCertificate::operator=(const TBSCertificate &iTBSCertificate )
{
	mIssuer = iTBSCertificate.getIssuer();
	mIssuerUniqueID = iTBSCertificate.getIssuerUniqueID();
	mIssuerUniqueIDPresent = iTBSCertificate.isIssuerUniqueIDPresent();
	mSerialNumber = iTBSCertificate.getSerialNumber();
	mSubject = iTBSCertificate.getSubject();
	mSubjectPublicKeyInfo = iTBSCertificate.getSubjectPublicKeyInfo();
	mSubjectUniqueID = iTBSCertificate.getSubjectUniqueID();
	mSubjectUniqueIDPresent = iTBSCertificate.isSubjectUniqueIDPresent();
	mSignature = iTBSCertificate.getSignature();
	mVersion = iTBSCertificate.getVersion();
	mExtensions = iTBSCertificate.getExtensions();
	mExtensionsPresent = iTBSCertificate.isExtensionsPresent();
	mNotAfter = iTBSCertificate.getNotAfter();
	mNotBefore = iTBSCertificate.getNotBefore();
	return *this;
}


bool esya::operator==(const TBSCertificate &iRHS ,const TBSCertificate & iLHS)
{
	/* todo: Þimdilik issuer ve serial karþýlaþtýrýlmasý yeterli görüldü. Buna bakýlmalý*/
	return ( iRHS.getIssuer() == iLHS.getIssuer() && iRHS.getSerialNumber()== iLHS.getSerialNumber() );
}


bool esya::operator!=(const TBSCertificate &iRHS ,const TBSCertificate &iLHS )
{
	return !( iRHS == iLHS );
}

int TBSCertificate::copyToASNObject(ASN1T_EXP_TBSCertificate & oTBSCertificate)const 
{
	mIssuer.copyToASNObject(oTBSCertificate.issuer);
	mSubject.copyToASNObject(oTBSCertificate.subject);
	mNotAfter.copyToASNObject(oTBSCertificate.validity.notAfter);	
	mNotBefore.copyToASNObject(oTBSCertificate.validity.notBefore);	
	
	oTBSCertificate.version = mVersion;

	oTBSCertificate.m.subjectUniqueIDPresent = mSubjectUniqueIDPresent;
	oTBSCertificate.m.extensionsPresent = mExtensionsPresent;
	oTBSCertificate.m.issuerUniqueIDPresent = mIssuerUniqueIDPresent;

	if ( mIssuerUniqueIDPresent)
	{
		mIssuerUniqueID.copyToASNObject(oTBSCertificate.issuerUniqueID);
	}
	if ( mSubjectUniqueIDPresent)
	{
		mSubjectUniqueID.copyToASNObject(oTBSCertificate.subjectUniqueID);
	}
	
	mSubjectPublicKeyInfo.copyToASNObject(oTBSCertificate.subjectPublicKeyInfo);

	mSignature.copyToASNObject(oTBSCertificate.signature_);
	
	
	Extension().copyExtensions(mExtensions,oTBSCertificate.extensions);
	
	mSerialNumber.copyToASNObject(oTBSCertificate.serialNumber);
	
	return SUCCESS;
}

void TBSCertificate::freeASNObject(ASN1T_EXP_TBSCertificate & oTBSCertificate)const
{
	Name().freeASNObject(oTBSCertificate.issuer);
	Name().freeASNObject(oTBSCertificate.subject);
	ETime().freeASNObject(oTBSCertificate.validity.notAfter);
	ETime().freeASNObject(oTBSCertificate.validity.notBefore);
	AlgorithmIdentifier().freeASNObject(oTBSCertificate.signature_);
	AlgorithmIdentifier().freeASNObject(oTBSCertificate.subjectPublicKeyInfo.algorithm);
	Extension().freeASNObjects(oTBSCertificate.extensions);
	SerialNumber().freeASNObject(oTBSCertificate.serialNumber);
	SubjectPublicKeyInfo().freeASNObject(oTBSCertificate.subjectPublicKeyInfo);

	//issuerUniqueID
	if ( oTBSCertificate.m.issuerUniqueIDPresent)
	{
		DELETE_MEMORY_ARRAY(oTBSCertificate.issuerUniqueID.data)
	}
	
	//subjectUniqueID
	if ( oTBSCertificate.m.subjectUniqueIDPresent)
	{
		DELETE_MEMORY_ARRAY(oTBSCertificate.subjectUniqueID.data)
	}

}

int TBSCertificate::copyFromASNObject(const ASN1T_EXP_TBSCertificate &iTBSCertificate)
{
	mVersion = iTBSCertificate.version;
	
	mIssuer.copyFromASNObject( iTBSCertificate.issuer);
	mSubject.copyFromASNObject(iTBSCertificate.subject);
	mSubjectPublicKeyInfo.copyFromASNObject(iTBSCertificate.subjectPublicKeyInfo);
	mSignature.copyFromASNObject(iTBSCertificate.signature_);
	mNotAfter.copyFromASNObject(iTBSCertificate.validity.notAfter);
	mNotBefore.copyFromASNObject( iTBSCertificate.validity.notBefore);
	mSerialNumber.copyFromASNObject(iTBSCertificate.serialNumber);
	
	Extension().copyExtensions(iTBSCertificate.extensions,mExtensions);

	mIssuerUniqueIDPresent	= iTBSCertificate.m.issuerUniqueIDPresent == 1;
	mSubjectUniqueIDPresent = iTBSCertificate.m.subjectUniqueIDPresent == 1;
	mExtensionsPresent		= iTBSCertificate.m.extensionsPresent == 1;

	mIssuerUniqueID = EBitString(iTBSCertificate.issuerUniqueID);
	mSubjectUniqueID = EBitString(iTBSCertificate.subjectUniqueID);

	return SUCCESS;
}

bool TBSCertificate::isIssuerUniqueIDPresent()const 
{
	return mIssuerUniqueIDPresent;
}

bool TBSCertificate::isSubjectUniqueIDPresent()const
{
	return mSubjectUniqueIDPresent;
}

bool TBSCertificate::isExtensionsPresent() const
{
	return mExtensionsPresent;
}

int TBSCertificate::getVersion()const
{
	return mVersion;
}

void TBSCertificate::setVersion(int iVersion)
{
	mVersion = iVersion;
}

const SerialNumber& TBSCertificate::getSerialNumber()const
{
	return mSerialNumber;
}

const AlgorithmIdentifier & TBSCertificate::getSignature()const
{
	return mSignature;
}

const Name &TBSCertificate::getIssuer()const
{
	return mIssuer;
}

const Name &TBSCertificate::getSubject()const
{
	return mSubject;
}

const ETime &TBSCertificate::getNotBefore()const
{
	return mNotBefore;
}

const ETime &TBSCertificate::getNotAfter()const
{
	return mNotAfter;
}

const SubjectPublicKeyInfo &TBSCertificate::getSubjectPublicKeyInfo()const
{
	return mSubjectPublicKeyInfo;
}

const EBitString &TBSCertificate::getIssuerUniqueID()const
{
	return mIssuerUniqueID;
}

const EBitString &TBSCertificate::getSubjectUniqueID()const
{
	return mSubjectUniqueID;
}

const QList<Extension>& TBSCertificate::getExtensions()const
{
	return mExtensions;
}

int TBSCertificate::getExtension(ASN1OBJID iExtnID, Extension& oExtension )const
{
	for (int i = 0 ; i<mExtensions.size(); i++ )
	{
		if ( (ASN1OBJID)mExtensions[i].getExtensionId() == iExtnID )
		{
			oExtension = mExtensions[i];
			return SUCCESS;
		}
	}
	return FAILURE;
}

void  TBSCertificate::setSerialNumber(const SerialNumber & iSN)
{
	mSerialNumber = iSN;
}

void TBSCertificate::setSubject(const Name & iSubject)
{
	mSubject = iSubject;
}

void TBSCertificate::setIssuer(const Name & iIssuer)
{
	mIssuer = iIssuer;
}

void TBSCertificate::setExtensions(const QList<Extension>& iExtensions)
{
	mExtensions = iExtensions;
	mExtensionsPresent = !(mExtensions.isEmpty());
}

void TBSCertificate::setSubjectPublicKeyInfo(const SubjectPublicKeyInfo & iSPKI)
{
	mSubjectPublicKeyInfo = iSPKI;
}

void TBSCertificate::setSignature(const AlgorithmIdentifier & iSignature)
{
	mSignature = iSignature;
}


void TBSCertificate::setNotBefore(const ETime & iNB)
{
	mNotBefore = iNB;
}

void TBSCertificate::setNotAfter(const ETime &iNA)
{
	mNotAfter = iNA;
}


TBSCertificate::~TBSCertificate(void)
{
}

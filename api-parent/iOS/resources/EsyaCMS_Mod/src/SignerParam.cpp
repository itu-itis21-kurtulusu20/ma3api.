#include "SignerParam.h"

using namespace esya;

/**
* \brief
* SignerParam constructoru
*
*/
SignerParam::SignerParam(void)
    :mSMIMEAttributesIncluded(false)
{
}

/**
* \brief
* SignerParam constructoru
*
* \param 		int iVersion
* �mzac� Versiyonu
*
* \param 		const ECertificate & iCert
* �mzac� Sertifikas�
*
* \param 		const AlgorithmIdentifier & iDigestAlg
* �mzalama �zet Algoritmas�
*
* \param 		const AnahtarBilgisi & iAB
* �zamalama Anahtar Bilgisi
*
*/
SignerParam::SignerParam(int iVersion, const ECertificate & iCert,const AlgorithmIdentifier & iDigestAlg, BaseSigner * signer)
:	mVersion(iVersion),
	mCert(iCert),
	mDigestAlg(iDigestAlg),
    mSMIMEAttributesIncluded(false),
    mpSigner(signer)
{
    mAdditionalUnsignedAttributes = QList<QPair<ASN1TObjId,QByteArray> >();
}


/**
* \brief
* SignerParam constructoru
*
* \param 		int iVersion
* �mzac� Versiyonu
*
* \param 		const ECertificate & iCert
* �mzac� Sertifikas�
*
* \param 		const AlgorithmIdentifier & iDigestAlg
* �mzalama �zet Algoritmas�
*
* \param 		const AnahtarBilgisi & iAB
* �zamalama Anahtar Bilgisi
*
*/
SignerParam::SignerParam(int iVersion, const ECertificate & iCert,const AlgorithmIdentifier & iDigestAlg ,
                         const QList< QPair<ASN1TObjId,QByteArray > >& iASA, BaseSigner * signer )
:	mVersion(iVersion),
	mCert(iCert),
	mDigestAlg(iDigestAlg),
	mSMIMEAttributesIncluded(false),
    mAdditionalUnsignedAttributes(iASA),
    mpSigner(signer)
{
    /*
    mAB = AnahtarBilgisi(OzelAnahtarBilgisi(), AcikAnahtarBilgisi());
    mAdditionalUnsignedAttributes = QList<QPair<ASN1TObjId,QByteArray> >();
     **/
	
}


/**
* \brief
* SignerParam  kopya constructoru 
*
* \param 		const SignerParam & iSP
* �mzac� Parametresi
*
*/
SignerParam::SignerParam(const SignerParam& iSP)
:	mVersion(iSP.getVersion()),
	mCert(iSP.getCert()),
	mDigestAlg(iSP.getDigestAlg()),
	mDigest(iSP.getDigest()),
	mSMIMEAttributesIncluded(iSP.getSMIMEAttributesIncluded()),
    mAdditionalUnsignedAttributes(iSP.getAdditionalUnsignedAttributes()),
    mpSigner(iSP.getSigner())
{
	
}
BaseSigner * SignerParam::getSigner() const
{
    return mpSigner;
}

/**
* \brief
* SignerParam  atama operat�r�
*
* \param 		const SignerParam & iSP
* �mzac� Parametresi
*
*/
SignerParam & SignerParam::operator=(const SignerParam& iSP)
{
	mVersion	= iSP.getVersion();
	mCert		= iSP.getCert();
	mDigestAlg	= iSP.getDigestAlg();
	mDigest		= iSP.getDigest();
	mSMIMEAttributesIncluded = iSP.getSMIMEAttributesIncluded();
	mAdditionalUnsignedAttributes = iSP.getAdditionalUnsignedAttributes();
	return *this;			
}

/**
* \brief
* SignerParam  kar��la�t�rma operat�r�
*
*/
bool esya::operator==(const SignerParam& iRHS, const SignerParam& iLHS)
{
	return (	iRHS.getVersion()	== iLHS.getVersion() &&
				iRHS.getCert()		== iLHS.getCert() &&
				iRHS.getDigestAlg() == iLHS.getDigestAlg() && 
				iRHS.getDigest()	== iLHS.getDigest() );
}

/**
* \brief
* Versiyon alan�n� d�ner
*
* \return   	const int
* �mzac� versiyonu
*/
const int  SignerParam::getVersion() const
{
	return mVersion;
}

/**
* \brief
* SMIME �zellikleri Eklenmeli mi alan�n� d�ner
*
* \return   	const bool
* SMIME �zellikleri Eklenmeli mi
*/
const bool  SignerParam::getSMIMEAttributesIncluded() const
{
	return mSMIMEAttributesIncluded;
}


/**
* \brief
* Sertifika alan�n� d�ner
*
* \return   	const ECertificate&
* �mzac� sertifikas�
*/
const ECertificate & SignerParam::getCert() const
{
	return mCert;
}


/**
* \brief
* �mzalama �zet Algoritmas� alan�n� d�ner
*
* \return   	const AlgorithmIdentifier&
* �mzalama �zet Algoritmas�
*/
const AlgorithmIdentifier &	SignerParam::getDigestAlg() const 
{
	return mDigestAlg;
}

/**
* \brief
* �mzalama �zet alan�n� d�ner
*
* \return   	const QByteArray&
* �mzalama �zeti
*/
const QByteArray &	SignerParam::getDigest() const 
{
	return mDigest;
}

/**
* \brief
* Versiyon alan�n� belirler
*
* \param const int iVersion
* �mzac� versiyonu
*/
void SignerParam::setVersion( const int  iVersion)
{
	mVersion = iVersion ;
}


/**
* \brief
* SMIME �zellikleri Eklenmeli mi alan�n� belirler
*
* \param const int iSMIMEAttributesIncluded
* SMIME �zellikleri Eklenmeli mi
*/
void  SignerParam::setSMIMEAttributesIncluded(const bool iSMIMEAttributesIncluded) 
{
	mSMIMEAttributesIncluded = iSMIMEAttributesIncluded;
}

/**
* \brief
* Ek �mzal� �zellikler alan�n� d�ner
*
* \return const QList<QPair<ASN1TObjId,QByteArray>>& 
* Ek �mzal� �zellikler alan�
*/
const QList<QPair<ASN1TObjId,QByteArray> >& SignerParam::getAdditionalUnsignedAttributes() const
{
	return mAdditionalUnsignedAttributes;
}

/**
* \brief
* Ek �mzal� �zellikler alan�n� belirler
*
* \param const QList<QPair<ASN1TObjId,QByteArray>>& 
* Ek �mzal� �zellikler alan�
*/
void SignerParam::setAdditionalUnsignedAttributes(const QList<QPair<ASN1TObjId,QByteArray> >& iASA)
{
	mAdditionalUnsignedAttributes = iASA;
}

/**
* \brief
* Sertifika alan�n� belirler
*
* \param const ECertificate& iCert
* �mzac� sertifikas�
*/
void SignerParam::setCert( const ECertificate &iCert)
{
	mCert = iCert;
}

/**
* \brief
* �mzalama �zet Algoritmas� alan�n� belirler
*
* \param const AlgorithmIdentifier&
* �mzalama �zet Algoritmas�
*/
void SignerParam::setDigestAlg(const AlgorithmIdentifier &iDigestAlg) 
{
	mDigestAlg = iDigestAlg;
}

/**
* \brief
* �mzalama �zet alan�n� belirler
*
* \param const QByteArray&
* �mzalama �zeti 
*/
void SignerParam::setDigest(const QByteArray &iDigest) 
{
	mDigest = iDigest;
}


/**
* \brief
* SignerParam destructoru
*/
SignerParam::~SignerParam(void)
{
}

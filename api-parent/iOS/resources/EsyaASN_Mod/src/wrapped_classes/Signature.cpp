#include "Signature.h"

using namespace esya;

Signature::Signature(void)
{
}

Signature::Signature(const Signature& iSignature)
:	mSignatureAlgorithm(iSignature.getSignatureAlgorithm()),
	mSignature(iSignature.getSignature()),
	mCerts(iSignature.getCerts())
{
}

Signature::Signature(const ASN1T_OCSP_Signature & iSignature)
{
	copyFromASNObject(iSignature);
}

Signature::Signature(const QByteArray & iSignature)
{
	constructObject(iSignature);
}

Signature& Signature::operator=(const Signature& iSignature)
{
	mCerts				= iSignature.getCerts();
	mSignature			= iSignature.getSignature();
	mSignatureAlgorithm = iSignature.getSignatureAlgorithm();
	return (*this);
}

bool esya::operator==(const Signature& iRHS,const Signature& iLHS)
{
	return (	iRHS.getSignature()				== iLHS.getSignature()			&& 
				iRHS.getSignatureAlgorithm()	== iLHS.getSignatureAlgorithm()		);
}

bool esya::operator!=(const Signature& iRHS, const Signature& iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}


int Signature::copyFromASNObject(const ASN1T_OCSP_Signature& iSignature)
{
	mSignature.copyFromASNObject(iSignature.signature_);
	mSignatureAlgorithm.copyFromASNObject(iSignature.signatureAlgorithm);
	if (iSignature.m.certsPresent)
		ECertificate().copyCertificates( iSignature.certs , mCerts );
	return SUCCESS;
}

int Signature::copyToASNObject(ASN1T_OCSP_Signature & oSignature) const
{
	oSignature.m.certsPresent = mCerts.size()>0;
	if (oSignature.m.certsPresent)
		ECertificate().copyCertificates(mCerts,oSignature.certs);

	mSignatureAlgorithm.copyToASNObject(oSignature.signatureAlgorithm);
	mSignature.copyToASNObject(oSignature.signature_);
	return SUCCESS;
}

void Signature::freeASNObject(ASN1T_OCSP_Signature & oSignature)const
{
	AlgorithmIdentifier().freeASNObject(oSignature.signatureAlgorithm);
	EBitString::freeASNObject(oSignature.signature_);
	if (oSignature.m.certsPresent)
		ECertificateData().freeASNObjects(oSignature.certs);
	
}

const AlgorithmIdentifier& Signature::getSignatureAlgorithm() const
{
	return mSignatureAlgorithm;
}

const QList<ECertificate>& Signature::getCerts()const
{
	return mCerts;
}

const EBitString & Signature::getSignature() const 
{
	return mSignature;
}

void Signature::setSignatureAlgorithm(const AlgorithmIdentifier &iSignatureAlg)
{
	mSignatureAlgorithm = iSignatureAlg;
}

void Signature::setSignature(const EBitString &iSignature)
{
	mSignature = iSignature;
}

void Signature::addCert(const ECertificate& iCert)
{
	mCerts.append(iCert);
}
	
Signature::~Signature(void)
{
}

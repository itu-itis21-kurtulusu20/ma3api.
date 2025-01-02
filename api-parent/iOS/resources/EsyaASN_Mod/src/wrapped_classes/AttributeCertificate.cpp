#include "AttributeCertificate.h"
#include "ortak.h"

using namespace esya;

namespace esya
{

	AttributeCertificate::AttributeCertificate(void)
	{
	}

	AttributeCertificate::AttributeCertificate(const QByteArray & iAttributeCertificate)
	{
		constructObject(iAttributeCertificate);
	}

	AttributeCertificate::AttributeCertificate(const ASN1T_ATTRCERT_AttributeCertificate & iAttributeCertificate )
	{
		copyFromASNObject(iAttributeCertificate);
	}

	AttributeCertificate::AttributeCertificate(const AttributeCertificate& iAttributeCertificate)
	:	mACInfo(iAttributeCertificate.getACInfo()),
		mSignatureAlgorithm(iAttributeCertificate.getSignatureAlgorithm()),
		mSignatureValue(iAttributeCertificate.getSignatureValue())
	{

	}

	AttributeCertificate& AttributeCertificate::operator=(const AttributeCertificate& iAttributeCertificate)
	{
		mACInfo = iAttributeCertificate.getACInfo();
		mSignatureAlgorithm = iAttributeCertificate.getSignatureAlgorithm();
		mSignatureValue = iAttributeCertificate.getSignatureValue();

		return *this;
	}

	bool operator==( const AttributeCertificate& iRHS, const AttributeCertificate& iLHS)
	{
		return ( ( iRHS.getACInfo()				== iLHS.getACInfo()	) && 
				 ( iRHS.getSignatureAlgorithm()	== iLHS.getSignatureAlgorithm()	)	&&
				 ( iRHS.getSignatureValue()		== iLHS.getSignatureValue()	)	
				);
	}

	bool operator!=( const AttributeCertificate& iRHS, const AttributeCertificate& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AttributeCertificate::copyFromASNObject(const ASN1T_ATTRCERT_AttributeCertificate & iAttributeCertificate)
	{
		mACInfo.copyFromASNObject(iAttributeCertificate.acinfo);
		mSignatureAlgorithm.copyFromASNObject(iAttributeCertificate.signatureAlgorithm);
		mSignatureValue.copyFromASNObject(iAttributeCertificate.signatureValue);

		return SUCCESS;
	}

	int AttributeCertificate::copyToASNObject(ASN1T_ATTRCERT_AttributeCertificate &oAttributeCertificate)const
	{
		mACInfo.copyToASNObject(oAttributeCertificate.acinfo);
		mSignatureAlgorithm.copyToASNObject(oAttributeCertificate.signatureAlgorithm);
		mSignatureValue.copyToASNObject(oAttributeCertificate.signatureValue);

		return SUCCESS;
	}

	void AttributeCertificate::freeASNObject(ASN1T_ATTRCERT_AttributeCertificate& oAttributeCertificate)const
	{
		AttributeCertificateInfo().freeASNObject(oAttributeCertificate.acinfo);
		AlgorithmIdentifier().freeASNObject(oAttributeCertificate.signatureAlgorithm);
		EBitString::freeASNObject(oAttributeCertificate.signatureValue);
	}

	AttributeCertificate::~AttributeCertificate(void)
	{
	}

	const AttributeCertificateInfo& AttributeCertificate::getACInfo()const
	{
		return mACInfo;
	}
	
	const AlgorithmIdentifier & AttributeCertificate::getSignatureAlgorithm()const
	{
		return mSignatureAlgorithm;
	}
	
	const EBitString & AttributeCertificate::getSignatureValue()const
	{
		return mSignatureValue;
	}

	void AttributeCertificate::setACInfo(const AttributeCertificateInfo& iACInfo)
	{
		mACInfo = iACInfo;
	}

	void AttributeCertificate::setSignatureAlgorithm(const AlgorithmIdentifier& iSignatureAlgorithm)
	{
		mSignatureAlgorithm = iSignatureAlgorithm;
	}

	void AttributeCertificate::setSignatureValue(const EBitString & iSignatureValue)
	{
		mSignatureValue = iSignatureValue;
	}


}
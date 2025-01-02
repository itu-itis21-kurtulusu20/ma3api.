#include "ESSCertIDv2.h"

using namespace esya;

namespace esya
{

    ESSCertIDv2::ESSCertIDv2(void)
        : mIssuerSerialPresent(false),
          mHashAlgorithmPresent(false)
	{
	}

    ESSCertIDv2::ESSCertIDv2( const ASN1T_PKCS7_ESSCertIDv2 & iESSCertIDv2)
        : mIssuerSerialPresent(false),
          mHashAlgorithmPresent(false)
	{
        copyFromASNObject(iESSCertIDv2);
	}

    ESSCertIDv2::ESSCertIDv2( const QByteArray &iESSCertIDv2)
        : mIssuerSerialPresent(false),
          mHashAlgorithmPresent(false)
	{
        constructObject(iESSCertIDv2);
	}

    ESSCertIDv2::ESSCertIDv2( const ESSCertIDv2 &iESSCertIDv2)
        : mIssuerSerialPresent(iESSCertIDv2.isIssuerSerialPresent()),
          mHashAlgorithmPresent(iESSCertIDv2.isHashAlgorithmPresent()),
          mIssuerSerial(iESSCertIDv2.getIssuerSerial()),
          mHashAlgorithm(iESSCertIDv2.getHashAlgorithm()),
          mHash(iESSCertIDv2.getHash())
	{
	}

    ESSCertIDv2::~ESSCertIDv2()
	{	
	}


    ESSCertIDv2 & ESSCertIDv2::operator=(const ESSCertIDv2& iESSCertIDv2)
	{
        mIssuerSerialPresent	= iESSCertIDv2.isIssuerSerialPresent();
        mHashAlgorithmPresent   = iESSCertIDv2.isHashAlgorithmPresent();
        mIssuerSerial			= iESSCertIDv2.getIssuerSerial();
        mHashAlgorithm          = iESSCertIDv2.getHashAlgorithm();
        mHash					= iESSCertIDv2.getHash();
		return *this;
	}

    // TO DO : to be implemented changes from ESSCertID to ESSCertIDv2
    bool operator==(const ESSCertIDv2& iRHS,const ESSCertIDv2& iLHS)
	{
		if ( ( iRHS.getHash()				!= iLHS.getHash() ) ||
			 ( iRHS.isIssuerSerialPresent()	!= iLHS.isIssuerSerialPresent() ))
			 return false;

		if ( ( iRHS.isIssuerSerialPresent()) &&
			 ( iRHS.getIssuerSerial() != iLHS.getIssuerSerial()) )
			return false;

		return true;
	}

    bool operator!=(const ESSCertIDv2& iRHS, const ESSCertIDv2& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

    int ESSCertIDv2::copyFromASNObject(const ASN1T_PKCS7_ESSCertIDv2 & iESSCertIDv2)
	{
        mIssuerSerialPresent = iESSCertIDv2.m.issuerSerialPresent;
		if (mIssuerSerialPresent)
            mIssuerSerial.copyFromASNObject(iESSCertIDv2.issuerSerial);

        mHashAlgorithmPresent = iESSCertIDv2.m.hashAlgorithmPresent;
        if(mHashAlgorithmPresent)
            mHashAlgorithm.copyFromASNObject(iESSCertIDv2.hashAlgorithm);
		
        mHash.copyFromASNObject(iESSCertIDv2.certHash);
		return SUCCESS;
	}

    int ESSCertIDv2::copyToASNObject(ASN1T_PKCS7_ESSCertIDv2 & oESSCertIDv2) const
	{
        oESSCertIDv2.m.issuerSerialPresent = mIssuerSerialPresent;
		if (mIssuerSerialPresent)
            mIssuerSerial.copyToASNObject(oESSCertIDv2.issuerSerial);

        oESSCertIDv2.m.hashAlgorithmPresent = mHashAlgorithmPresent;
        if(mHashAlgorithmPresent)
            mHashAlgorithm.copyToASNObject(oESSCertIDv2.hashAlgorithm);
		
        mHash.copyToASNObject(oESSCertIDv2.certHash);
		return SUCCESS;
	}

    void ESSCertIDv2::freeASNObject(ASN1T_PKCS7_ESSCertIDv2& oESSCertIDv2) const
	{
        if (oESSCertIDv2.m.issuerSerialPresent)
            PKCS7IssuerSerial().freeASNObject(oESSCertIDv2.issuerSerial);

        if(oESSCertIDv2.m.hashAlgorithmPresent)
            AlgorithmIdentifier().freeASNObject(oESSCertIDv2.hashAlgorithm);
		
        PKCS7Hash().freeASNObject(oESSCertIDv2.certHash);
	}

    bool ESSCertIDv2::isIssuerSerialPresent()const
	{
		return mIssuerSerialPresent;
	}

    bool ESSCertIDv2::isHashAlgorithmPresent()const
    {
        return mHashAlgorithmPresent;
    }

    const PKCS7IssuerSerial & ESSCertIDv2::getIssuerSerial() const
	{
		return mIssuerSerial;
	}

    const AlgorithmIdentifier & ESSCertIDv2::getHashAlgorithm() const
    {
        return mHashAlgorithm;
    }

    const PKCS7Hash & ESSCertIDv2::getHash() const
	{
		return mHash;
	}

    void ESSCertIDv2::setIssuerSerial(const PKCS7IssuerSerial & iIssuerSerial)
	{
		mIssuerSerialPresent = true;
		mIssuerSerial = iIssuerSerial;
	}

    void ESSCertIDv2::setHashAlgorithm(const AlgorithmIdentifier & iHashAlgorithm)
    {
        mHashAlgorithmPresent = true;
        mHashAlgorithm = iHashAlgorithm;
    }

    void ESSCertIDv2::setHash(const PKCS7Hash & iHash)
	{
		mHash = iHash;
	}

}

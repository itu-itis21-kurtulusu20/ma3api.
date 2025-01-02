#include "AttributeCertificateInfo.h"
#include "ortak.h"

using namespace esya;

namespace esya
{

	AttributeCertificateInfo::AttributeCertificateInfo(void)
	:	mIssuerUniqueIDPresent(false),
		mExtensionsPresent(false)
	{
	}

	AttributeCertificateInfo::AttributeCertificateInfo(const QByteArray & iAttributeCertificateInfo)
	{
		constructObject(iAttributeCertificateInfo);
	}

	AttributeCertificateInfo::AttributeCertificateInfo(const ASN1T_ATTRCERT_AttributeCertificateInfo & iAttributeCertificateInfo )
	{
		copyFromASNObject(iAttributeCertificateInfo);
	}

	AttributeCertificateInfo::AttributeCertificateInfo(const AttributeCertificateInfo& iAttributeCertificateInfo)
	:	mVersion(iAttributeCertificateInfo.getVersion()),
		mHolder(iAttributeCertificateInfo.getHolder()),
		mIssuer(iAttributeCertificateInfo.getIssuer()),
		mSignature(iAttributeCertificateInfo.getSignature()),
		mSerialNumber(iAttributeCertificateInfo.getSerialNumber()),
		mAttrCertValidityPeriod(iAttributeCertificateInfo.getAttrCertValidityPeriod()),
		mAttributes(iAttributeCertificateInfo.getAttributes()),
		mIssuerUniqueIDPresent(iAttributeCertificateInfo.isIssuerUniqueIDPresent()),
		mExtensionsPresent(iAttributeCertificateInfo.isExtensionsPresent()) 	
	{
		if ( mIssuerUniqueIDPresent )
			mIssuerUniqueID = iAttributeCertificateInfo.getIssuerUniqueID();
		if (mExtensionsPresent )
			mExtensions = iAttributeCertificateInfo.getExtensions();
	}

	AttributeCertificateInfo& AttributeCertificateInfo::operator=(const AttributeCertificateInfo& iAttributeCertificateInfo)
	{
		mVersion = iAttributeCertificateInfo.getVersion();
		mHolder = iAttributeCertificateInfo.getHolder();
		mIssuer = iAttributeCertificateInfo.getIssuer();
		mSignature = iAttributeCertificateInfo.getSignature();
		mSerialNumber = iAttributeCertificateInfo.getSerialNumber();
		mAttrCertValidityPeriod = iAttributeCertificateInfo.getAttrCertValidityPeriod();
		mAttributes = iAttributeCertificateInfo.getAttributes();
		mIssuerUniqueIDPresent = iAttributeCertificateInfo.isIssuerUniqueIDPresent();
		mExtensionsPresent = iAttributeCertificateInfo.isExtensionsPresent();

		if ( mIssuerUniqueIDPresent )
			mIssuerUniqueID = iAttributeCertificateInfo.getIssuerUniqueID();
		if (mExtensionsPresent )
			mExtensions = iAttributeCertificateInfo.getExtensions();

		return *this;
	}

	bool operator==( const AttributeCertificateInfo& iRHS, const AttributeCertificateInfo& iLHS)
	{
		return ( ( iRHS.getVersion()		== iLHS.getVersion()	) && 
				 ( iRHS.getHolder()			== iLHS.getHolder()	)	&&
				 ( iRHS.getIssuer()			== iLHS.getIssuer()	)	&&
				 ( iRHS.getSignature()	    == iLHS.getSignature()	)	&&
				 ( iRHS.getSerialNumber()	== iLHS.getSerialNumber()	)	&&
				 ( iRHS.getAttrCertValidityPeriod()	== iLHS.getAttrCertValidityPeriod()	)	&&
				 ( iRHS.getAttributes()				== iLHS.getAttributes()	)	&&
				 ( iRHS.isIssuerUniqueIDPresent()	== iLHS.isIssuerUniqueIDPresent()	)	&&
				 ( iRHS.isExtensionsPresent()	    == iLHS.isExtensionsPresent()	)	
				);
	}

	bool operator!=( const AttributeCertificateInfo& iRHS, const AttributeCertificateInfo& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AttributeCertificateInfo::copyFromASNObject(const ASN1T_ATTRCERT_AttributeCertificateInfo & iAttributeCertificateInfo)
	{
		mVersion = iAttributeCertificateInfo.version;
		mHolder.copyFromASNObject(iAttributeCertificateInfo.holder);
		mIssuer.copyFromASNObject(iAttributeCertificateInfo.issuer);
		mSignature.copyFromASNObject(iAttributeCertificateInfo.signature_);
		mSerialNumber.copyFromASNObject(iAttributeCertificateInfo.serialNumber);
		mAttrCertValidityPeriod.copyFromASNObject(iAttributeCertificateInfo.attrCertValidityPeriod);
		Attribute().copyAttributeList(iAttributeCertificateInfo.attributes,mAttributes ) ;
		mIssuerUniqueIDPresent = ( iAttributeCertificateInfo.m.issuerUniqueIDPresent == 1 );
		mExtensionsPresent = ( iAttributeCertificateInfo.m.extensionsPresent == 1 ) ;

		if ( mIssuerUniqueIDPresent )
			mIssuerUniqueID.copyFromASNObject(iAttributeCertificateInfo.issuerUniqueID);
		if (mExtensionsPresent )
			Extension().copyExtensions(iAttributeCertificateInfo.extensions, mExtensions);

		return SUCCESS;
	}

	int AttributeCertificateInfo::copyToASNObject(ASN1T_ATTRCERT_AttributeCertificateInfo &oAttributeCertificateInfo)const
	{
		oAttributeCertificateInfo.version = mVersion;
		mHolder.copyToASNObject(oAttributeCertificateInfo.holder);
		mIssuer.copyToASNObject(oAttributeCertificateInfo.issuer);
		mSignature.copyToASNObject(oAttributeCertificateInfo.signature_);
		mSerialNumber.copyToASNObject(oAttributeCertificateInfo.serialNumber);
		mAttrCertValidityPeriod.copyToASNObject(oAttributeCertificateInfo.attrCertValidityPeriod);
		Attribute().copyAttributeList(mAttributes,oAttributeCertificateInfo.attributes ) ;
		
		oAttributeCertificateInfo.m.issuerUniqueIDPresent = (mIssuerUniqueIDPresent ? 1:0);
		oAttributeCertificateInfo.m.extensionsPresent = (mExtensionsPresent ? 1:0);

		if ( mIssuerUniqueIDPresent )
			mIssuerUniqueID.copyToASNObject( oAttributeCertificateInfo.issuerUniqueID);
		if (mExtensionsPresent )
			Extension().copyExtensions(mExtensions,oAttributeCertificateInfo.extensions );
		return SUCCESS;
	}

	void AttributeCertificateInfo::freeASNObject(ASN1T_ATTRCERT_AttributeCertificateInfo& oAttributeCertificateInfo)const
	{
		Holder().freeASNObject(oAttributeCertificateInfo.holder);
		AttCertIssuer().freeASNObject(oAttributeCertificateInfo.issuer);
		AlgorithmIdentifier().freeASNObject(oAttributeCertificateInfo.signature_);
		SerialNumber().freeASNObject(oAttributeCertificateInfo.serialNumber);
		AttCertValidityPeriod().freeASNObject(oAttributeCertificateInfo.attrCertValidityPeriod);
		Attribute().freeASNObjects(oAttributeCertificateInfo.attributes) ;

		if ( oAttributeCertificateInfo.m.issuerUniqueIDPresent)
			EBitString::freeASNObject( oAttributeCertificateInfo.issuerUniqueID);
		if ( oAttributeCertificateInfo.m.extensionsPresent)
			Extension().freeExtensions(oAttributeCertificateInfo.extensions );
	}

	bool AttributeCertificateInfo::isIssuerUniqueIDPresent()const 
	{
		return mIssuerUniqueIDPresent;
	}

	bool AttributeCertificateInfo::isExtensionsPresent()const
	{
		return mExtensionsPresent;
	}


	int AttributeCertificateInfo::getVersion()const
	{
		return mVersion;
	}

	const Holder& AttributeCertificateInfo::getHolder()const
	{
		return mHolder;
	}

	const AttCertIssuer & AttributeCertificateInfo::getIssuer()const
	{
		return mIssuer;
	}

	const AlgorithmIdentifier & AttributeCertificateInfo::getSignature()const
	{
		return mSignature;
	}

	const SerialNumber & AttributeCertificateInfo::getSerialNumber()const
	{
		return mSerialNumber;
	}

	const AttCertValidityPeriod & AttributeCertificateInfo::getAttrCertValidityPeriod()const
	{
		return mAttrCertValidityPeriod;
	}

	const QList<Attribute>& AttributeCertificateInfo::getAttributes()const
	{
		return mAttributes;
	}

	const EBitString & AttributeCertificateInfo::getIssuerUniqueID()const
	{
		return mIssuerUniqueID;
	}

	const QList<Extension> & AttributeCertificateInfo::getExtensions()const
	{
		return mExtensions;
	}

	void AttributeCertificateInfo::setIssuerUniqueIDPresent(bool iIssuerUniqueIDPresent)
	{
		mIssuerUniqueIDPresent = iIssuerUniqueIDPresent;
	}

	void AttributeCertificateInfo::setExtensionsPresent(bool iExtensionsPresent)
	{
		mExtensionsPresent = iExtensionsPresent;
	}


	void AttributeCertificateInfo::setVersion(int iVersion)
	{
		mVersion = iVersion;
	}

	void AttributeCertificateInfo::setHolder(const Holder& iHolder)
	{
		mHolder = iHolder;
	}

	void AttributeCertificateInfo::setIssuer(const AttCertIssuer & iIssuer)
	{
		mIssuer = iIssuer;
	}	

	void AttributeCertificateInfo::setSignature(const AlgorithmIdentifier & iSignature)
	{
		mSignature = iSignature;
	}

	void AttributeCertificateInfo::setSerialNumber(const SerialNumber & iSerialNumber)
	{
		mSerialNumber = iSerialNumber;
	}

	void AttributeCertificateInfo::setAttrCertValidityPeriod(const AttCertValidityPeriod & iAttrCertValidityPeriod)
	{
		mAttrCertValidityPeriod = iAttrCertValidityPeriod;
	}

	void AttributeCertificateInfo::setAttributes(const QList<Attribute>& iAttributes)
	{
		mAttributes = iAttributes;
	}

	void AttributeCertificateInfo::setIssuerUniqueID(const EBitString & iIssuerUniqueID)
	{
		mIssuerUniqueIDPresent = true;
		mIssuerUniqueID;
	}

	void AttributeCertificateInfo::setExtensions(const QList<Extension> & iExtensions)
	{
		mExtensionsPresent = !iExtensions.isEmpty();
		mExtensions = iExtensions;
	}

	AttributeCertificateInfo::~AttributeCertificateInfo(void)
	{
	}

}
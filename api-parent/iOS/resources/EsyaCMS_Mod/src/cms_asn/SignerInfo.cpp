
#include "cmslibrary_global.h"

#include "SignerInfo.h"
#include "SignedData.h"
#include "DigestInfo.h"
#include "SMIMECapabilities.h"
#include "SMIMEEncryptionKeyPreference.h"
#include "SigningCertificate.h"
#include "SigningCertificateV2.h"
#include "ECMSException.h"
#include "KriptoUtils.h"
#include "Logger.h"
#include "AlgorithmList.h"

using namespace esya;


Q_DECL_EXPORT const ASN1T_CMS_CMSVersion SignerInfo::DEFAULT_VERSION = CMS_CMSVersion::v1 ;

SignerInfo::SignerInfo(SignerInfo * iParent, SignedData* iParentData)
: mVersion(DEFAULT_VERSION), mParent(iParent), mSignatureChanged(false), mParentData(iParentData)
{
}


SignerInfo::SignerInfo(const SignerInfo & iSignerInfo)
:	mVersion(iSignerInfo.getVersion()),
	mSID(iSignerInfo.getSID()),
	mDigestAlgorithm(iSignerInfo.getDigestAlgorithm()),
	mSignatureAlgorithm(iSignerInfo.getSignatureAlgorithm()),
	mSignedAttrs(iSignerInfo.getSignedAttributes()),
	mUnsignedAttrs(iSignerInfo.getUnsignedAttributes()),
	mSignature(iSignerInfo.getSignature()),
	mParent( (SignerInfo*) iSignerInfo.getParent()),
	mParentData( (SignedData*) iSignerInfo.getParentData()),
	mSignatureChanged(false)
{
	_fillSerialSigners();
}

SignerInfo::SignerInfo(const ASN1T_CMS_SignerInfo & iSignerInfo , SignerInfo * iParent ,SignedData*  iParentData )
: mParent(iParent), mSignatureChanged(false) , mParentData(iParentData)
{
	copyFromASNObject(iSignerInfo);
}

SignerInfo::SignerInfo(const QByteArray &iSignerInfo , SignerInfo * iParent ,SignedData* iParentData )
: mParent(iParent), mSignatureChanged(false),mParentData(iParentData)
{
	constructObject(iSignerInfo);
}


SignerInfo & SignerInfo::operator=(const SignerInfo& iSignerInfo)
{
	mVersion = iSignerInfo.getVersion();
	mSID = iSignerInfo.getSID();
	mDigestAlgorithm = iSignerInfo.getDigestAlgorithm();
	mSignatureAlgorithm = iSignerInfo.getSignatureAlgorithm();
	mSignedAttrs = iSignerInfo.getSignedAttributes();
	mUnsignedAttrs = iSignerInfo.getUnsignedAttributes();
	mSignature = iSignerInfo.getSignature();
	mSigners	= iSignerInfo.getSigners();
	mParent	= (SignerInfo*)iSignerInfo.getParent();
	mParentData =(SignedData*) iSignerInfo.getParentData();
	mSignatureChanged = true;
	return (*this);
}

bool esya::operator==(const SignerInfo & iRHS,const SignerInfo & iLHS)
{
	/* todo : SignerIdentifierlar� e�itse e�ittir diyoruz. ?? */
	return (iRHS.getSID() == iLHS.getSID());
}

bool esya::operator!=(const SignerInfo & iRHS, const SignerInfo & iLHS)
{
	return (!(iRHS==iLHS));
}


int SignerInfo::copyFromASNObject(const ASN1T_CMS_SignerInfo& iSignerInfo)
{
	mVersion =  iSignerInfo.version;
	mSID.copyFromASNObject(iSignerInfo.sid);
	mDigestAlgorithm.copyFromASNObject(iSignerInfo.digestAlgorithm);
	mSignatureAlgorithm.copyFromASNObject(iSignerInfo.signatureAlgorithm);
	mSignature = toByteArray(iSignerInfo.signature_);

	if ( iSignerInfo.m.signedAttrsPresent )
	{
		for (int i = 0 ; i < iSignerInfo.signedAttrs.count ; i++)
		{
			ASN1T_EXP_Attribute * pAttr = (ASN1T_EXP_Attribute * )ESeqOfList::get(iSignerInfo.signedAttrs,i);
			mSignedAttrs.append(Attribute(*pAttr));
		}
	}
	if ( iSignerInfo.m.unsignedAttrsPresent )
	{
		for (int i = 0 ; i < iSignerInfo.unsignedAttrs.count ; i++)
		{
			ASN1T_EXP_Attribute * pAttr = (ASN1T_EXP_Attribute * )ESeqOfList::get(iSignerInfo.unsignedAttrs,i);
			mUnsignedAttrs.append(Attribute(*pAttr));
		}
	}

	_fillSerialSigners();
	return SUCCESS;
}

int SignerInfo::copyToASNObject(ASN1T_CMS_SignerInfo &oSignerInfo) const
{
	oSignerInfo.version = mVersion;
	mSID.copyToASNObject(oSignerInfo.sid);
	mDigestAlgorithm.copyToASNObject(oSignerInfo.digestAlgorithm);
	mSignatureAlgorithm.copyToASNObject(oSignerInfo.signatureAlgorithm);
	oSignerInfo.signature_.data = (OSOCTET*)myStrDup(mSignature.data(),mSignature.size()); 
	oSignerInfo.signature_.numocts = mSignature.size(); 

	oSignerInfo.m.signedAttrsPresent	= ( mSignedAttrs.size() > 0);
	oSignerInfo.m.unsignedAttrsPresent	= ( mUnsignedAttrs.size() > 0);
	
	if ( oSignerInfo.m.signedAttrsPresent )
	{
		Attribute().copyAttributeList(mSignedAttrs,oSignerInfo.signedAttrs);
	}
	if ( oSignerInfo.m.unsignedAttrsPresent )
	{
		Attribute().copyAttributeList(mUnsignedAttrs,oSignerInfo.unsignedAttrs);
	}
	return SUCCESS;
}

void SignerInfo::freeASNObject(ASN1T_CMS_SignerInfo & oSignerInfo)const
{
	AlgorithmIdentifier().freeASNObject(oSignerInfo.digestAlgorithm);
	AlgorithmIdentifier().freeASNObject(oSignerInfo.signatureAlgorithm);	
	SignerIdentifier().freeASNObject(oSignerInfo.sid);
	DELETE_MEMORY_ARRAY(oSignerInfo.signature_.data)

	if ( oSignerInfo.m.signedAttrsPresent )
	{
		Attribute().freeASNObjects(oSignerInfo.signedAttrs);
	}

	if ( oSignerInfo.m.unsignedAttrsPresent )
	{
		Attribute().freeASNObjects(oSignerInfo.unsignedAttrs);	}
	return;
}

int SignerInfo::copySignerInfos(const ASN1T_CMS_SignerInfos & iSignerInfos, QList<SignerInfo>& oList, SignerInfo * iParent ,SignedData* iParentData)
{
	for (int i = 0 ; i<iSignerInfos.count ; i++	)
	{
		ASN1T_CMS_SignerInfo* pSignerInfo = (ASN1T_CMS_SignerInfo*)ESeqOfList::get(iSignerInfos,i);
		SignerInfo sInfo(*pSignerInfo,iParent,iParentData);
		oList.append(sInfo);
	}
	return SUCCESS;
}

int SignerInfo::copySignerInfos(const QList<SignerInfo> iList ,ASN1T_CMS_SignerInfos & oSignerInfos)
{
	return copyASNObjects<SignerInfo>(iList,oSignerInfos);
}

int SignerInfo::copySignerInfos(const QByteArray & iASNBytes,  QList<SignerInfo>& oList,SignerInfo* iParent ,SignedData* iParentData  )
{
	ASN1BERDecodeBuffer decBuf;
	decBuf.setBuffer((OSOCTET*)iASNBytes.data(), iASNBytes.size()) ;

	ASN1T_CMS_SignerInfos signerInfos;
	ASN1C_CMS_SignerInfos cSignerInfos(signerInfos);

	if ( cSignerInfos.DecodeFrom(decBuf) != ASN_OK )
	{
		throw EException("SEQ_Of_SigneroInfo decode edilemedi.",__FILE__,__LINE__);
	}

	SignerInfo::copySignerInfos(signerInfos,oList,iParent,iParentData);
}

int SignerInfo::copySignerInfos(const QList<SignerInfo> iList ,QByteArray & oASNBytes)
{
	return copyASNObjects<ASN1T_CMS_SignerInfos,ASN1C_CMS_SignerInfos,SignerInfo>(iList,oASNBytes);
}



const SignerInfo* SignerInfo::getParent() const
{
	return mParent;
}

SignerInfo* SignerInfo::getParent()
{
	return mParent;
}


const SignedData* SignerInfo::getParentData() const
{
	return mParentData;
}

SignedData* SignerInfo::getParentData()
{
	return mParentData;
}


const int& SignerInfo::getVersion() const
{
	return mVersion;
}

const SignerIdentifier& SignerInfo::getSID()const 
{
	return mSID;
}

const AlgorithmIdentifier& SignerInfo::getDigestAlgorithm() const
{
	return mDigestAlgorithm;
}

const AlgorithmIdentifier& SignerInfo::getSignatureAlgorithm() const
{
	return mSignatureAlgorithm;
}

const QList<Attribute> &  SignerInfo::getSignedAttributes() const 
{
	return mSignedAttrs;
}

const QList<Attribute> &  SignerInfo::getUnsignedAttributes() const 
{
	return mUnsignedAttrs;
}

QList<Attribute> &  SignerInfo::getUnsignedAttributes() 
{
	return mUnsignedAttrs;
}


const QByteArray& SignerInfo::getSignature()const
{
	return mSignature;
}

void SignerInfo::setVersion(const int iVersion)
{
	mVersion = iVersion;
}

void SignerInfo::setSID(const SignerIdentifier& iSID)
{
	mSID = iSID;
}

void SignerInfo::setDigestAlgorithm(const AlgorithmIdentifier& iDigestAlgorithm)
{
	mDigestAlgorithm = iDigestAlgorithm;
}

void SignerInfo::setSignatureAlgorithm(const AlgorithmIdentifier& iSignatureAlgorithm)
{
	mSignatureAlgorithm = iSignatureAlgorithm;
}

void SignerInfo::setSignature(const QByteArray & iSignature)
{
	mSignature = iSignature;	
}

void SignerInfo::addSignedAttribute( const Attribute&  iAttribute)
{
	mSignedAttrs.append(iAttribute);
	mSignatureChanged = true;
}

void SignerInfo::addUnsignedAttribute(const Attribute& iAttribute)
{
	mUnsignedAttrs.append(iAttribute);
}


QList<Attribute> SignerInfo::getSignedAttributes(const ASN1TObjId & iAttrType) const 
{
	QList<Attribute> attrList;
	for ( int i = 0 ; i< mSignedAttrs.size() ; i++)
	{
		if ( mSignedAttrs[i].getType() == iAttrType )
		{
			attrList.append(mSignedAttrs[i]);
		}
	}
	return attrList;	
}

QList<Attribute> SignerInfo::getUnsignedAttributes(const ASN1TObjId & iAttrType ) const
{
	QList<Attribute> attrList;
	for ( int i = 0 ; i< mUnsignedAttrs.size() ; i++)
	{
		if ( mUnsignedAttrs[i].getType() == iAttrType )
		{
			attrList.append(mUnsignedAttrs[i]);
		}
	}
	return attrList;	
}

QByteArray SignerInfo::getMessageDigest()const
{
	
	QList<Attribute> digests = getSignedAttributes(CMS_id_messageDigest);
	
	if ( digests.size() > 0 )
	{
		Attribute digest = digests[0];
		const AttributeValue &attrVal = digest.getAttributeValues()[0];
		QByteArray digestBytes = attrVal.getValue();
		ASN1BERDecodeBuffer decBuf( (OSOCTET *) digestBytes.data(),digestBytes.size());
		ASN1T_CMS_MessageDigest aDigest;
		ASN1C_CMS_MessageDigest cDigest(decBuf,aDigest);
		int asn1Status = cDigest.DecodeFrom(decBuf);
		if (asn1Status!= ASN_OK)
		{
			throw EException(QString(CMS_ASND_MESSAGEDIGEST_1).arg(asn1Status));
		}
		QByteArray qTemp((const char *)aDigest.data,aDigest.numocts);
		return qTemp;
	}

	return QByteArray();
}


QByteArray SignerInfo::getSignedAttrsBytes()const
{
	if (mSignedAttrs.size() == 0) return QByteArray();
	
	ASN1T_CMS_SignerInfo *pSignerInfo = getASNCopy();

	ASN1BEREncodeBuffer signedAttributesEncBuf;
	ASN1C_DERCMS_SignedAttributes cSignedAttributes(pSignerInfo->signedAttrs);
	int asn1Status = cSignedAttributes.EncodeTo(signedAttributesEncBuf);
	if (asn1Status <= ASN_OK)
	{
		throw EException(CMS_ASNE_SIGNEDATTRIBUTES,__FILE__,__LINE__);
	}
	QByteArray signedAttributes((const char*) signedAttributesEncBuf.getMsgPtr(),asn1Status);

	freeASNObjectPtr(pSignerInfo);
	return signedAttributes;
}



int SignerInfo::_updateSignature(BaseSigner* signer, const esya::ECertificate & iSignerCert,bool isSerial)
{
	 
	if ( mSignedAttrs.size() > 0 )
	{
		try
		{
			QByteArray data = getSignedAttrsBytes();

            Logger::log("SignerInfo::_updateSignature - sign");
            Logger::log("this is the check log at updatesignature");
            //QByteArray dataToBeSigned = KriptoUtils::calculateToBeSigned(data,"SHA-256");
            mSignature = signer->sign(data/*ToBeSigned*/);
            Logger::log("signature value ->");
            if(mSignature.isNull())
                Logger::log("signature value is null");
            else if(mSignature.isEmpty())
                Logger::log("signature value is empty");
            else {
                //Logger::log(QString(mSignature));
                Logger::log("sigval is something");
                Logger::log(QString::number(mSignature.count()));
            }
            Logger::log("SignerInfo::constructBESSigner - burasi da sign'dan sonrasi iste");
            //TO DO mSignature = KriptoUtils::calculateSignature(iAB, iSignerCert, data , mDigestAlgorithm);
		}
		catch (EException& exc)
		{
			exc.append("signSignedAttibutes()",__FILE__,__LINE__);
			throw exc;
		}
	}
	else
	{
		throw EException(CMS_SIGNEDATTRIBUTES_NOT_FOUND,__FILE__,__LINE__);		
	}
	mSignatureChanged = true;
	return SUCCESS;
}


int SignerInfo::addContentTypeSignedAttr()
{
	ASN1BEREncodeBuffer encBufForContentType;
    ASN1T_CMS_ContentType contType = CMS_id_data;
	ASN1C_CMS_ContentType cContType(encBufForContentType,contType);
	int asn1Status = cContType.Encode();
	if( asn1Status <= 0)
	{
		throw EException(CMS_ASNE_CONTENTTYPE,__FILE__,__LINE__);
	}
	addSignedAttribute(QByteArray((const char*)encBufForContentType.getMsgPtr(),asn1Status),CMS_id_contentType);

	return SUCCESS;
}

int SignerInfo::addMsgDigestSignedAttr(const QByteArray& iByteDigest)
{
	ASN1T_CMS_Digest digest;
	digest.data = (unsigned char*)iByteDigest.data();
	digest.numocts = iByteDigest.length();
	return addMsgDigestSignedAttr(digest);
}

int SignerInfo::addMsgDigestSignedAttr(const QString& iFileName)
{
	ASN1T_CMS_Digest digest;
	QByteArray byteDigest;
	try
	{
		QFile pdStream(iFileName);
        byteDigest;//TO DO = KriptoUtils::calculateStreamDigest( pdStream,mDigestAlgorithm);
	}
	catch (EException& exc)
	{
		exc.append("addMsgDigestSignedAttr()",__FILE__,__LINE__);
		throw exc;
	}
	
	digest.data = (unsigned char*)byteDigest.data();
	digest.numocts = byteDigest.length();
	return addMsgDigestSignedAttr(digest);
}

int SignerInfo::addMsgDigestSignedAttr(ASN1T_CMS_Digest & iDigest)
{
	ASN1BEREncodeBuffer encBuf;
	ASN1C_CMS_Digest cDigest(encBuf,iDigest);
	int asn1Status = cDigest.Encode();
	if( asn1Status <= 0)
	{
		throw EException(CMS_ASNE_MESSAGEDIGEST,__FILE__,__LINE__);
	}
	addSignedAttribute(QByteArray((const char*)encBuf.getMsgPtr(),asn1Status),CMS_id_messageDigest);
	return SUCCESS;
}

int SignerInfo::addSMIMECapabilitiesSignedAttribute()
{
	SMIMECapability sc;
	sc.setCapabilityID(ALGOS_sha1WithRSAEncryption);
	sc.setParameters(NULL_PARAMS);
	SMIMECapabilities scs;
	scs.appendCapability(sc);


	addSignedAttribute(scs.getEncodedBytes(),CMS_smimeCapabilities);
	return SUCCESS;
}

int SignerInfo::addSMIMEEncryptionKeyPreferenceSignedAttribute(const ECertificate& iEncCert)
{
	SMIMEEncryptionKeyPreference sekp;

	sekp.setISA(new IssuerAndSerialNumber(iEncCert));


	addSignedAttribute(sekp.getEncodedBytes(),CMS_id_aa_encrypKeyPref);
	return SUCCESS;
}

int SignerInfo::addTimeStampUnsignedAttr( const QByteArray& iTimeStampToken)
{
	addUnsignedAttribute(iTimeStampToken,PKCS7_id_aa_timeStampToken);
	return SUCCESS;	
}

int SignerInfo::addTimeSignedAttr( )
{
	/* Daha once zamani bu sekilde aliyorduk.
	// QT ile birlikte degistirdik.
	
	int asn1Status = 0; 
	time_t time;
	::time(&time);
	ASN1BEREncodeBuffer encBuf;
	ASN1VisibleString time_buf;
	ASN1CUTCTime time_(encBuf,time_buf); 
	time_.setTime(time,true);
	time_.setUTC(true);
	*/
	//QString stDate = QDateTime::currentDateTime().addSecs(-8).toUTC().toString("yyMMddhhmmssZ");
	QString stDate = QDateTime::currentDateTime().toUTC().toString(UTC_FORMAT);
	ETime etime(T_EXP_Time_utcTime,stDate);
	
	DEBUGLOGYAZ(ESYACMS_MOD,stDate);

	addSignedAttribute(etime.getEncodedBytes(),CMS_id_signingTime);
	return SUCCESS;
}

QDateTime SignerInfo::getSigningTime() const
{
	CMS_FUNC_BEGIN

	QList<Attribute> attrList = getSignedAttributes(CMS_id_signingTime);

	if ( attrList.isEmpty())
		return QDateTime();
	else 
	{
		QList<AttributeValue> avList = attrList[0].getAttributeValues();
		if ( avList.isEmpty())
			return QDateTime();

		ETime t(avList[0].getValue());
		return t.toDateTime();
	}

	CMS_FUNC_END
}

int SignerInfo::addSigningCertSignedAttr(const ECertificate &iSignerCert)
{
	CMS_FUNC_BEGIN
	try
	{
        QByteArray byteCertHash = KriptoUtils::calculateDigest( iSignerCert.getEncodedBytes() , "SHA-1");

		PKCS7Hash certHash;
		certHash.setData(byteCertHash);
		ESSCertID essCert;
		essCert.setHash(certHash);
		essCert.setIssuerSerial(PKCS7IssuerSerial(iSignerCert));
		SigningCertificate signingCert;
		signingCert.appendCert(essCert);
		addSignedAttribute(signingCert.getEncodedBytes(),PKCS7_id_aa_signingCertificate);

// 		ASN1T_EXP_Certificate * pCert = iSignerCert.getASNCopy(); 
// 
// 		ASN1BEREncodeBuffer encBuf;	//SigningCertificate'i encode etmek i�in kullan�lacak
// 		ASN1T_PKCS7_SigningCertificate imzaciSertifika;
// 		imzaciSertifika.m.policiesPresent = 0;
// 		ASN1C_PKCS7__SeqOfPKCS7_ESSCertID ESSCertIDs(imzaciSertifika.certs);
// 		ASN1T_PKCS7_ESSCertID essCertId;		
// 		essCertId.m.issuerSerialPresent = 1;
// 		essCertId.issuerSerial.serialNumber = pCert->tbsCertificate.serialNumber;
// 		ASN1T_IMP_GeneralName generalName;	//eklenecek name
// 		generalName.t = 5;	//directoryName
// 		generalName.u.directoryName = &(pCert->tbsCertificate.issuer);
// 
// 		ASN1C_IMP_GeneralNames generalNames(essCertId.issuerSerial.issuer);
// 		generalNames.Append(&generalName);
// 
// 
// 		essCertId.certHash.data = (unsigned char *)byteCertHash.data();
// 		essCertId.certHash.numocts = byteCertHash.size();
// 
// 		ESSCertIDs.Append(&essCertId);	//ESSCertIDS'e bir essCertId ekleniyor
// 
// 		ASN1C_PKCS7_SigningCertificate cImzaciSertifika(encBuf,imzaciSertifika);
// 		int asn1Status = cImzaciSertifika.Encode();
// 		if(asn1Status <= 0)
// 		{
// 			throw EException(CMS_ASNE_CERTIFICATE,__FILE__,__LINE__);
// 		}
//		ECertificate().freeASNObjectPtr(pCert);

	}
	catch (EException& exc)
	{
		exc.append("addSigningCertSignedAttr",__FILE__,__LINE__);	
		throw exc;	
	}

	CMS_FUNC_BEGIN
	return SUCCESS;
}

// burayi ben ekledim
int SignerInfo::addSigningCertV2SignedAttr(const ECertificate &iSignerCert)
{
    CMS_FUNC_BEGIN
    try
    {
        // TODO use a better way to pass algorithm names
        QByteArray byteCertHash = KriptoUtils::calculateDigest( iSignerCert.getEncodedBytes() , "SHA-256" /*AlgorithmList::DIGEST_SHA256*/);//TODO

        PKCS7Hash certHash;
        certHash.setData(byteCertHash);
        ESSCertIDv2 essCertV2;
        essCertV2.setHash(certHash);
        essCertV2.setIssuerSerial(PKCS7IssuerSerial(iSignerCert));
        essCertV2.setHashAlgorithm(AlgorithmIdentifier(ASN1TObjId(ALGOS_id_sha256)));
        SigningCertificateV2 signingCertV2;
        signingCertV2.appendCert(essCertV2);
        addSignedAttribute(signingCertV2.getEncodedBytes(),PKCS7_id_aa_signingCertificateV2);

// 		ASN1T_EXP_Certificate * pCert = iSignerCert.getASNCopy();
//
// 		ASN1BEREncodeBuffer encBuf;	//SigningCertificate'i encode etmek i�in kullan�lacak
// 		ASN1T_PKCS7_SigningCertificate imzaciSertifika;
// 		imzaciSertifika.m.policiesPresent = 0;
// 		ASN1C_PKCS7__SeqOfPKCS7_ESSCertID ESSCertIDs(imzaciSertifika.certs);
// 		ASN1T_PKCS7_ESSCertID essCertId;
// 		essCertId.m.issuerSerialPresent = 1;
// 		essCertId.issuerSerial.serialNumber = pCert->tbsCertificate.serialNumber;
// 		ASN1T_IMP_GeneralName generalName;	//eklenecek name
// 		generalName.t = 5;	//directoryName
// 		generalName.u.directoryName = &(pCert->tbsCertificate.issuer);
//
// 		ASN1C_IMP_GeneralNames generalNames(essCertId.issuerSerial.issuer);
// 		generalNames.Append(&generalName);
//
//
// 		essCertId.certHash.data = (unsigned char *)byteCertHash.data();
// 		essCertId.certHash.numocts = byteCertHash.size();
//
// 		ESSCertIDs.Append(&essCertId);	//ESSCertIDS'e bir essCertId ekleniyor
//
// 		ASN1C_PKCS7_SigningCertificate cImzaciSertifika(encBuf,imzaciSertifika);
// 		int asn1Status = cImzaciSertifika.Encode();
// 		if(asn1Status <= 0)
// 		{
// 			throw EException(CMS_ASNE_CERTIFICATE,__FILE__,__LINE__);
// 		}
//		ECertificate().freeASNObjectPtr(pCert);

    }
    catch (EException& exc)
    {
        exc.append("addSigningCertSignedAttr",__FILE__,__LINE__);
        throw exc;
    }

    CMS_FUNC_BEGIN
    return SUCCESS;
}

int SignerInfo::addSignedAttribute( const QByteArray &iAttrBytes , const ASN1TObjId &iAttrType)
{
	Attribute attr;
	AttributeValue attrValue;
	attr.setType(iAttrType);
	attrValue.setValue(iAttrBytes);
	attr.addAttributeValue(attrValue);

	addSignedAttribute(attr);
	return SUCCESS;
}

int SignerInfo::addUnsignedAttribute( const QByteArray &iAttrBytes , const ASN1TObjId &iAttrType)
{
	Attribute attr;
	AttributeValue attrValue;
	attr.setType(iAttrType);
	attrValue.setValue(iAttrBytes);
	attr.addAttributeValue(attrValue);

	addUnsignedAttribute(attr);
	return SUCCESS;
}


int  SignerInfo::addUnsignedAttributes( const QList< QPair<ASN1TObjId,QByteArray> >& iUnsignedAttributes)
{
	for (int i= 0 ; i<iUnsignedAttributes.size(); i++)
	{
		addUnsignedAttribute(iUnsignedAttributes[i].second,iUnsignedAttributes[i].first);
	}
	return SUCCESS;
}


bool SignerInfo::hasDigest(const QByteArray& iDigest)const
{
	return ( iDigest == getMessageDigest() );
}

const QList<SignerInfo>& SignerInfo::getSigners()const 
{
	return mSigners;
}


QList<SignerInfo>& SignerInfo::getSigners()
{
	return mSigners;
}


void SignerInfo::_fillSerialSigners()
{
	mSigners.clear();
	QList<Attribute>  counterSigns = getUnsignedAttributes(CMS_id_countersignature);

	/* Burada birden fazla counterSignature olabilece�i durumu da d���n�ld� */
	for (int i = 0 ; i < counterSigns.size() ; i++ )
	{
		QList<AttributeValue> attrVals =  counterSigns[i].getAttributeValues();
		for (int j = 0 ; j < attrVals.size() ; j++ )
		{
			SignerInfo signer = SignerInfo(attrVals[j].getValue(),this,this->getParentData());
			mSigners.append(signer);
		}
	}
}

/**
 * \brief
 *	�mzac�daki counterSignature attributeuna referans geri d�ner
 * e�er bu attribute yoksa olu�turur.
 * 
 */
Attribute& SignerInfo::_getCounterSignatureAttribute()
{
	QList<Attribute>& attrList =  getUnsignedAttributes();
	for (int i = 0; i< attrList.size() ; i++ )
	{
		if ( attrList[i].getType() == (ASN1TObjId)CMS_id_countersignature )
		{
			return attrList[i];
		}
	}
	Attribute attr(CMS_id_countersignature);
	addUnsignedAttribute(attr);
	return _getCounterSignatureAttribute();
}

/**
* \brief
* �mzac�y� seri BES imzalar
* 
* \param iAB
* seri imzac� Kapal� Anahtar Bilgisi
*
* \param iCert
* Seri imzac� sertifikas�
*
* \param iVersion
* Seri imzac� versiyonu
*
* 
*/
int SignerInfo::addSerialBESSigner(	int  iVersion, 
                                    BaseSigner *signer,
									const ECertificate & iCert ,
                                    const AlgorithmIdentifier& iDigestAlg )
{
	if ( !getParentData() )
	{
		throw EException("Ata SignedData bulunamad�",__FILE__,__LINE__);
	}
	try
	{
        QByteArray digest = KriptoUtils::calculateDigest( getSignature(),"SHA-256");//TODO
		
        SignerParam sp(iVersion,iCert,iDigestAlg, signer);
		sp.setDigest(digest);
		SignerInfo *pSigner = constructBESSigner(sp,true,false, this , this->getParentData() );

		addSerialSigner(*pSigner,iCert);

		DELETE_MEMORY(pSigner)
		return SUCCESS;
	}
	catch (EException& exc)
	{
		exc.append("addSerialSigner() : ",__FILE__,__LINE__);
		throw exc;
	}
}
/**
* \brief
* �mzac�y� seri imzalar
* 
* \param iSigner 
* seri imzac� objesi
*
* \param iCert
* Seri imzac� sertifikas�
*
* +-++++++++
*/
int SignerInfo::addSerialSigner( const SignerInfo& iSigner, const ECertificate & iCert )
{
	if ( !getParentData() )
	{
		throw EException("Ata SignedData bulunamad�",__FILE__,__LINE__);
	}
	try
	{
		SignerInfo signer(iSigner);
		signer.setParentData(getParentData());
		signer.setParent(this);
		
		Attribute & counterSign = _getCounterSignatureAttribute();
		
		counterSign.addAttributeValue(AttributeValue(signer.getEncodedBytes()));
		mSigners.append(signer);
		getParentData()->addCertificate(ECertChoices(iCert));


		if (mParent)
			mParent->_updateSigner();
		return SUCCESS;
	}
	catch (EException& exc)
	{
		exc.append("addSerialSigner() : ",__FILE__,__LINE__);
		throw exc;
	}
}



int SignerInfo::_updateSigner()
{
	if (mSigners.size()==0 ) return SUCCESS;
	Attribute& csAttr = _getCounterSignatureAttribute();
	csAttr.getAttributeValues().clear();
	
	for ( int i = 0 ; i < mSigners.size() ; i ++ )
	{
		csAttr.addAttributeValue(mSigners[i].getEncodedBytes());
	}
	
	if ( mParent )
		return mParent->_updateSigner();
	
	return SUCCESS;
}


void	SignerInfo::setParent(SignerInfo* iParent)
{
	mParent = iParent;
}

void	SignerInfo::setParentData(SignedData* iParentData)
{
	mParentData = iParentData;
}




SignerInfo * SignerInfo::constructBESSigner(	const SignerParam & iSP,  
												const bool isSerial , 
												const bool isStreamed , 
												SignerInfo * iParentSigner , 
												SignedData *iParentData)

{
	if ( !iParentData) throw EException("Ata �mzal� Veri NULL olamaz.",__FILE__,__LINE__);
	if ( isSerial && !iParentSigner) throw EException("Seri imzada ata-imzac� NULL olamaz.",__FILE__,__LINE__);
	try
	{
		const ECertificate& cert = iSP.getCert();

        AlgorithmIdentifier signatureAlg = AlgorithmIdentifier(ASN1TObjId(ALGOS_sha256WithRSAEncryption)); // TO DO  = KriptoUtils::createSignatureAlgorithm(cert.getTBSCertificate().getSubjectPublicKeyInfo().getAlgorithm(),iSP.getDigestAlg());

		if (signatureAlg.isNull())
            throw ECMSStdException("constructBESSigner(): Bilinmeyen İmza algoritması");
	
		SignerInfo *pSigner = new SignerInfo();
		pSigner->setParent(iParentSigner);
		pSigner->setParentData(iParentData);
		pSigner->setSignatureAlgorithm(signatureAlg);
		pSigner->setDigestAlgorithm(iSP.getDigestAlg());
		
		if ( ! isSerial )
		{	
			pSigner->addContentTypeSignedAttr();		//seri imzalarda content type attribute eklenmiyor! 03.11.2006 15:36
		}

		pSigner->addMsgDigestSignedAttr(iSP.getDigest());	
		pSigner->addTimeSignedAttr();
        //pSigner->addSigningCertSignedAttr(cert);
        pSigner->addSigningCertV2SignedAttr(cert);

		if (iSP.getSMIMEAttributesIncluded() ) 
		{
			pSigner->addSMIMECapabilitiesSignedAttribute();
			pSigner->addSMIMEEncryptionKeyPreferenceSignedAttribute(iSP.getCert());
		}

        Logger::log("SignerInfo::constructBESSigner - update signature");
        pSigner->_updateSignature(iSP.getSigner(), cert,isSerial);
        Logger::log("SignerInfo::constructBESSigner - set version");
		pSigner->setVersion(iSP.getVersion());
		pSigner->setSID(SignerIdentifier(cert, ( iSP.getVersion() == CMS_CMSVersion::v1 ) ?SignerIdentifier::issuerAndSerialNumber : SignerIdentifier::subjectKeyIdentifier ))	;

		pSigner->addUnsignedAttributes(iSP.getAdditionalUnsignedAttributes());

		return pSigner;
	}
	catch (EException & exc)
	{
		exc.append("constructBESSigner()",__FILE__,__LINE__);
		throw;
	}
}

bool SignerInfo::verifyChildren(const QList<ECertificate>& iCertificates, bool isStreamed)const
{
	for (int i = 0; i<mSigners.size(); i++  )
	{
		if (!mSigners[i].verifySigner(iCertificates,isStreamed,true))
			return false;
	}
	return true;
}

bool SignerInfo::verifySigner(const QList<ECertificate>& iCertificates ,bool isStreamed ,bool iVerifyChildren ) const
{
	if (!mParentData) return false;
	ECertificate cert;
	if (!( mParentData->getCertFromCertificates(mSID,cert) || SignedData::getCertFromCertificates(iCertificates,mSID,cert) ) )
	{
		// Signing Cert Bulunamad�;
		ERRORLOGYAZ(ESYACMS_MOD,"�mzac� sertifikas� bulunamad�.");		
		return false;
	}

	bool res ;
	
	/*
		todo: Burada Sertifika do�rulama i�lemi ger�ekle�tirilmeli
	*/

	// Ata �mzac� tan�ml� ise  bu bir seri imzad�r
	// Tan�ml� de�ilse paralel do�rulama yap�lacak
	if (!mParent)
	{
		res = _verifyParallel(isStreamed,cert);
	}
	else res = _verifySerial( cert);

	// �mza yanl��sa veya seri imzac�lar i�in 
	// do�rulama yap�lmayacaksa burada do�rulama biter.
	if (!res || !iVerifyChildren) return res;

	// Do�rulama i�lemi seri imzac�lar i�in devam edecek.
	return verifyChildren(iCertificates,isStreamed);
}

bool SignerInfo::_verifyParallel(bool isStreamed, const ECertificate &iCert )const
{
	try
	{
		QByteArray digest;
		if (isStreamed )
		{
            /* TO DO
			if (((StreamedSignedData*)mParentData)->getDigest(mDigestAlgorithm,digest) != SUCCESS)
			{	
				ERRORLOGYAZ(ESYACMS_MOD,"�mzac� i�in imzal� verinin �zeti bulunamad�.");
				return false;
			}
            */
		}
        else digest = KriptoUtils::calculateDigest(mParentData->getEncapContentInfo().getEContent(),"SHA-256"/*mDigestAlgorithm*/);//TODO

		if (!hasDigest(digest))
		{
			ERRORLOGYAZ(ESYACMS_MOD,"�mzac� i�in imzal� verinin �zeti bulunamad�." );
			return false;
		}

        bool verified; // TO DO =  KriptoUtils::verifySignature(iCert,getSignedAttrsBytes(),mSignature,mDigestAlgorithm);
		if (!verified)
		{
			ERRORLOGYAZ(ESYACMS_MOD,"�mzac� imzas� do�rulanamad�.");
			return false;
		}
		return true;
	}
	catch (EException & exc)
	{	
		exc.append("_verifyParallel()",__FILE__,__LINE__);
		throw exc;
	}
}

bool SignerInfo::_verifySerial(const ECertificate &iCert )const
{
	try
	{
        bool verified; // TO DO =  KriptoUtils::verifySignature(iCert,mParent->getSignature(),mSignature,mDigestAlgorithm);
		if (!verified)
		{
			ERRORLOGYAZ(ESYACMS_MOD,"�mzac� imzas� do�rulanamad�.");
			return false;
		}
		return true;
	}
	catch (EException & exc)
	{	
		exc.append("_verify()",__FILE__,__LINE__);
		throw exc;
	}
}


SignerInfo::~SignerInfo(void)
{
}

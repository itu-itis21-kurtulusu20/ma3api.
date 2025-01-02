#include "ECertificateList.h"
#include "ECertificate.h"
#include "CRLNumber.h"
#include "ExtensionGenerator.h"
#include "IssuingDistributionPoint.h"
#include "FileUtil.h"

using namespace esya;

#define TAG_CERTIFICATELIST				TAG_SEQUENCE
#define TAG_TBSCERTLIST					TAG_SEQUENCE
#define TAG_REVOKEDCERTIFICATES			TAG_SEQUENCE
#define TAG_REVOKEDCERTIFICATES_ELEMENT TAG_SEQUENCE
#define TAG_CRLEXTENSIONS				TAG_A0_READ


ECertificateList::ECertificateList(void)
:	mFullyLoaded(false)
{
}

ECertificateList::ECertificateList(const QString& iSILFile)
:	mFullyLoaded(false)	
{
	QByteArray silBytes = FileUtil::readFromFile(iSILFile);
	lazyLoad(silBytes);
}

ECertificateList::ECertificateList(const QByteArray & iCertificateList)
:	mFullyLoaded(false)	
{
	lazyLoad(iCertificateList);
}

ECertificateList::ECertificateList(const ASN1T_EXP_CertificateList &iCertificateList )
:	mFullyLoaded(true)	
{
	copyFromASNObject(iCertificateList);
}

ECertificateList::ECertificateList(const ECertificateList& iCertificateList)
:	mTBSCertList(iCertificateList.getTBSCertList()),
	mSignature(iCertificateList.getSignature()),
	mSignatureAlgorithm(iCertificateList.getSignatureAlgorithm()),
	mCRLData(iCertificateList.mCRLData),
	mRevokedData(iCertificateList.mRevokedData),
	mFullyLoaded(!iCertificateList.isLazyLoaded())
{
}


ECertificateList & ECertificateList::operator=(const ECertificateList &iCertificateList )
{
	mTBSCertList = iCertificateList.getTBSCertList();
	mSignature		= iCertificateList.getSignature();
	mSignatureAlgorithm = iCertificateList.getSignatureAlgorithm();
	mCRLData = iCertificateList.mCRLData;
	mRevokedData = iCertificateList.mRevokedData;
	mFullyLoaded = !iCertificateList.isLazyLoaded();

	return *this;
}


bool esya::operator==(const ECertificateList &iRHS ,const ECertificateList & iLHS)
{
	/* todo: Þimdilik TBS Certificate deðerleri karþýlaþtýrýlýyor*/
	return ( iRHS.getTBSCertList() == iLHS.getTBSCertList());
}


bool esya::operator!=(const ECertificateList &iRHS ,const ECertificateList &iLHS )
{
	return !( iRHS == iLHS );
}

int ECertificateList::copyToASNObject(ASN1T_EXP_CertificateList & oCertificateList)const 
{
	if (isLazyLoaded() && !mRevokedData.isEmpty())
	{
		ECertificateList crl(*this);
		crl.fullyLoad();
		crl.copyToASNObject(oCertificateList);
		return SUCCESS;
	}

	mTBSCertList.copyToASNObject(oCertificateList.tbsCertList);	
	mSignatureAlgorithm.copyToASNObject(oCertificateList.signatureAlgorithm);
	
	mSignature.copyToASNObject(oCertificateList.signature_);

	return SUCCESS;
}

void ECertificateList::freeASNObject(ASN1T_EXP_CertificateList & oCertificateList)const
{
	TBSCertList().freeASNObject(oCertificateList.tbsCertList);
	AlgorithmIdentifier().freeASNObject(oCertificateList.signatureAlgorithm);

	DELETE_MEMORY_ARRAY(oCertificateList.signature_.data)
}

int ECertificateList::copyCRL(const ASN1T_PKCS7_CertificateRevocationLists & iCRL, QList<ECertificateList>& oList)
{
	return copyASNObjects<ECertificateList>(iCRL,oList);
}

/**
	Listedeke extensionlarýn ASN kopyalarýný oluþturur.
	Oluþturulan Extensionlarýn hafýzasý daha sonra freeASNObject metodu ile
	geri verilmeli
*/
int ECertificateList::copyCRL(const QList<ECertificateList> iList ,ASN1T_PKCS7_CertificateRevocationLists & oCRL)
{
	return copyASNObjects<ECertificateList>(iList,oCRL);
}

int ECertificateList::copyFromASNObject(const ASN1T_EXP_CertificateList &iCertificateList)
{

	mTBSCertList = TBSCertList( iCertificateList.tbsCertList);
	mSignatureAlgorithm = AlgorithmIdentifier(iCertificateList.signatureAlgorithm);
	mSignature.copyFromASNObject(iCertificateList.signature_);
	return SUCCESS;
}

const TBSCertList& ECertificateList::getTBSCertList() const
{
	return mTBSCertList;
}
	
const AlgorithmIdentifier& ECertificateList::getSignatureAlgorithm()const 
{
	return mSignatureAlgorithm;
}

const EBitString & ECertificateList::getSignature()const
{
	return mSignature;
}

int ECertificateList::indexOf(const ECertificate& iCert,const QDateTime& iRevocationTime)
{
	const QList<RevokedCertificatesElement> & certList = getRevokedCertificates(); 
	bool indirectCRL = isIndirectCRL();
	for (int i = 0 ; i< certList.count();i++)
	{
		if (certList[i].getUserCertificate() == iCert.getTBSCertificate().getSerialNumber().getValue())
		{
			if (indirectCRL)
			{
				CertificateIssuer ci= getCertificateIssuer(certList[i]);
				// todo: buraya certIssuer eklentisi ile ilgili kod yazýlacak.
				if (!ci.hasIssuer(iCert.getTBSCertificate().getIssuer()))
				{
					SubjectAlternativeName ian;
					int ianFound = ExtensionGenerator::getIANExtension(iCert.getTBSCertificate().getExtensions(),ian);
					if (ianFound<0 || ian.getList()!=ci.getList())
						continue;
				}

			}
			if (iRevocationTime.isNull())
				return i;
			if (certList[i].getRevocationDate().toDateTime() <= iRevocationTime)
				return i;
		}
	}
	return -1;
}

const QList<RevokedCertificatesElement>& ECertificateList::getRevokedCertificates()
{
	if (!mFullyLoaded)
	{
		fullyLoad();
	}

	return mTBSCertList.getRevokedCertificates();
}

QString ECertificateList::getCRLNumber()const
{
	CRLNumber sn;
	int res = ExtensionGenerator::getCRLNumberExtension(mTBSCertList.getCRLExtensions(),sn);

	if (res == SUCCESS)
		return sn.getValue();

	return QString();
}

bool ECertificateList::isIndirectCRL()const
{
	IssuingDistributionPoint idp;
	int res = ExtensionGenerator::getIDPExtension(getTBSCertList().getCRLExtensions(),idp);

	if ( res != SUCCESS || !idp.isIndirectCRL())
		return false;

	return true;
}


ECertificateList::~ECertificateList(void)
{
}

CertificateIssuer ECertificateList::getCertificateIssuer(const RevokedCertificatesElement & iRCE)
{
	CertificateIssuer defaultCI,ci;
	GeneralName gn;
	gn.setDirectoryName(getTBSCertList().getIssuer());
	defaultCI.appendGeneralName(gn);

	if (!isIndirectCRL() || getRevokedCertificates().isEmpty())
		return defaultCI;

	int index =  getRevokedCertificates().indexOf(iRCE);
	if (index<0)
		return defaultCI;
	
	for (int i = index; i>=0;i--)
	{
		int ciFound = ExtensionGenerator::getCertificateIssuerExtension(getRevokedCertificates()[i].getCRLEntryExtensions(),ci);
		if (ciFound>=0)
			return ci;
	}	
	return defaultCI;
}


QString  ECertificateList::toString() const
{
	QString stCRL = QString("%1 %2").arg(mTBSCertList.getIssuer().toTitle()).arg(getCRLNumber());
	return stCRL;
}


void ECertificateList::_loadVersion(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray vBytes;
		pIN->pIN()->mark(1000);
		pIN->decodeOpenType(&vBytes);		
		ASN1BERDecodeBuffer decBuf((OSOCTET*)vBytes.data(),vBytes.size());
		ASN1T_EXP_Version version;
		ASN1C_EXP_Version cVersion(version);
		int stat = cVersion.DecodeFrom(decBuf);
		//rtMemFree(pIN->getCtxtPtr());
		if (stat == 0) 
		{
			mTBSCertList.setVersion(version); 
			mTBSCertList.setVersionPresent(true);
			return;
		}
		else if (stat != ASN_E_INVLEN) // todo: burasý ASN_E_IDNOTFOUND olcak
		{
			throw EException("_loadVersion() : ASN Decode hatasý.");
		}
		pIN->pIN()->reset();
	}
	catch (EException & exc)
	{
		throw exc.append("version could not be loaded");
	}
}

void ECertificateList::_loadSignature(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray saBytes;
		pIN->decodeOpenType(&saBytes);
		mTBSCertList.setSignature(AlgorithmIdentifier(saBytes));
		return ;
	}
	catch (EException& exc)
	{
		throw exc.append("_loadSignature() failed");
	}
}

void ECertificateList::_loadIssuer(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray iBytes;
		pIN->decodeOpenType(&iBytes);
		mTBSCertList.setIssuer(Name(iBytes));
		return ;
	}
	catch (EException& exc)
	{
		throw exc.append("_loadIssuer()");
	}
}


void ECertificateList::_loadThisUpdate(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray iBytes;
		pIN->decodeOpenType(&iBytes);
		mTBSCertList.setThisUpdate(ETime(iBytes));
		return ;
	}
	catch (EException& exc)
	{
		throw exc.append("_readIssuer()");
	}
}

void ECertificateList::_loadNextUpdate(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray bytes;
		ASN1TAG	 tag;
		int len;

		pIN->peekTagLen(tag,len);
		if ( tag == TAG_REVOKEDCERTIFICATES )
		{
			mTBSCertList.setNextUpdatePresent(false);
			return ;
		}

		pIN->decodeOpenType(&bytes);
		mTBSCertList.setNextUpdate(ETime(bytes));
		mTBSCertList.setNextUpdatePresent(true);
		return;
	}
	catch(...)
	{
		throw EException("_loadNextUpdate()ASN Decode Hatasý");
	}
}

void  ECertificateList::_loadCRLExtensions(EASNFileInputStream* pIN)
{
	ASN1TAG	tag;
	int		len;
	try
	{
		pIN->peekTagLen(tag,len);
		QByteArray bytes, ceBytes;		

		if ( tag == TAG_CRLEXTENSIONS )
		{	
			pIN->skipTag(TAG_CRLEXTENSIONS);
			QList<Extension> crlExtensions;
			pIN->decodeOpenType(&ceBytes);
			Extension().copyExtensions(ceBytes,crlExtensions);
			mTBSCertList.setCRLExtensions(crlExtensions);
			return ;
		}

	}
	catch (EException& exc)
	{
		throw exc.append("_loadCRLExtensions()");
	}
}

QByteArray ECertificateList::getTBSBytes()const
{
	if (mFullyLoaded)
		return mTBSCertList.getEncodedBytes();

	if (mCRLData.isEmpty())
		return QByteArray();

	EASNFileInputStream* pIN = new EASNFileInputStream(mCRLData);

	QByteArray tbsBytes;

	try
	{
		pIN->skipTag(TAG_CERTIFICATELIST);
		pIN->decodeOpenType(&tbsBytes);
		DELETE_MEMORY(pIN);
		return tbsBytes;
	}
	catch (...)
	{
		throw  EException("CRL streamden yüklenemedi.",__FILE__,__LINE__);
	}
}

void ECertificateList::_loadCRLSignature(EASNFileInputStream* pIN)
{
	ASN1TAG	tag;
	int		len;
	try
	{
		QByteArray bytes;		
		pIN->decodeOpenType(&bytes);
		mSignature.constructObject(bytes);
		return ;
	}
	catch (EException& exc)
	{
		throw exc.append("_loadCRLExtensions()");
	}
}

void ECertificateList::_loadCRLSignatureAlgorithm(EASNFileInputStream* pIN)
{
	try
	{
		QByteArray saBytes;
		pIN->decodeOpenType(&saBytes);
		mSignatureAlgorithm.constructObject(saBytes);
		return ;
	}
	catch (EException& exc)
	{
		throw exc.append("_loadSignature() failed");
	}
}

void ECertificateList::lazyLoad(const QByteArray & iCRLData)
{
    //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() basladi")
	QByteArray buffer;

	mCRLData = iCRLData;

	EASNFileInputStream* pIN = new EASNFileInputStream(iCRLData);

	QByteArray bytes;
	ASN1TAG	 tag;
	int len;

    //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() input stream olusturuldu. decode basliyor")

	try
	{
		pIN->skipTag(TAG_CERTIFICATELIST);
		pIN->skipTag(TAG_TBSCERTLIST);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() SÝL tag i okundu")

		_loadVersion(pIN);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Versiyon okundu")
		
		_loadSignature(pIN);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Signature okundu")

		_loadIssuer(pIN);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Issuer okundu")
		
		_loadThisUpdate(pIN);
		
        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() ThisUpdate okundu")
		
		_loadNextUpdate(pIN);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Next Update okundu")
		
		pIN->peekTagLen(tag,len);
		if ( tag == TAG_REVOKEDCERTIFICATES ) // revoked certificates var mý?
		{
            //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Revoked certificates okunacak")
			pIN->decodeOpenType(&buffer);//Skipping RevokedCertificates
            //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Revoked certificates okundu")
			mRevokedData = buffer;
		}
		
        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Revoked certificates alaný geçildi")
		
		_loadCRLExtensions(pIN);
		
        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() SÝL eeklentileri okundu")
		
		_loadCRLSignatureAlgorithm(pIN);
		
        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() Ýmza algoritmasý okundu")
		
		_loadCRLSignature(pIN);

        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::lazyLoad() SÝL imzasý okundu")

		DELETE_MEMORY(pIN);
	}
	catch (...)
	{
		throw  EException("CRL streamden yüklenemedi.",__FILE__,__LINE__);
	}
}

void ECertificateList::fullyLoad()
{
    //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::fullyLoad() yapýlacak")
	if (!mFullyLoaded && !mRevokedData.isEmpty())
	{
        //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::fullyLoad() yapýlacak")
		RevokedCertificatesElement().copyRCEs(mRevokedData,mTBSCertList.mRevokedCertificates);
		mRevokedData.clear();
		mCRLData.clear();
		mFullyLoaded = true;
	}

    //DEBUGLOGYAZ(ESYAASN_MOD,"ECertificateList::fullyLoad() bitti")
}

// const QByteArray&  ECertificateList::getCRLData()const
// {
// 	return mCRLData;
// }

bool ECertificateList::isLazyLoaded()const
{
	return (!mFullyLoaded);
}

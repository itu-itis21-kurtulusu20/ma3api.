#include "ECertificate.h"
#include "SubjectAlternativeName.h"
#include "ExtensionGenerator.h"
#include "CRLDistributionPoints.h"

using namespace esya;

inline uint qHash(const ECertificate &key)
{
	return qHash(key.getTBSCertificate().getSubject().toTitle()); 
}



ECertificateData::ECertificateData(void)
{
}

ECertificateData::ECertificateData(const QString& iCertFile)
{
	loadFromFile(iCertFile);
}

ECertificateData::ECertificateData(const QByteArray & iCertificate)
{
	constructObject(iCertificate);
}

ECertificateData::ECertificateData(const ASN1T_EXP_Certificate &iCertificate )
{
	copyFromASNObject(iCertificate);
}

ECertificateData::ECertificateData(const ECertificateData& iCertificate)
:	QSharedData(iCertificate),
	mTBSCertificate(iCertificate.mTBSCertificate),
	mSignature(iCertificate.mSignature),
	mSignatureAlgorithm(iCertificate.mSignatureAlgorithm)
{
}


ECertificateData & ECertificateData::operator=(const ECertificateData &iCertificate )
{
	mTBSCertificate = iCertificate.mTBSCertificate;
	mSignature		= iCertificate.mSignature;
	mSignatureAlgorithm = iCertificate.mSignatureAlgorithm;

	return *this;
}


bool esya::operator==(const ECertificateData &iRHS ,const ECertificateData & iLHS)
{
	/* todo: Þimdilik TBS Certificate deðerleri karþýlaþtýrýlýyor*/
	return ( iRHS.mTBSCertificate == iLHS.mTBSCertificate);
}


bool esya::operator!=(const ECertificateData &iRHS ,const ECertificateData &iLHS )
{
	return !( iRHS == iLHS );
}

bool ECertificateData::operator<(const ECertificateData & iCertificate )const
{

	if ( mTBSCertificate.getIssuer() != iCertificate.mTBSCertificate.getIssuer() )
		return ( mTBSCertificate.getIssuer().toString() < iCertificate.mTBSCertificate.getIssuer().toString()  ) ;

	else return (mTBSCertificate.getSerialNumber() < iCertificate.mTBSCertificate.getSerialNumber() );

}


int ECertificateData::copyToASNObject(ASN1T_EXP_Certificate & oCertificate)const 
{
	mTBSCertificate.copyToASNObject(oCertificate.tbsCertificate);	
	mSignatureAlgorithm.copyToASNObject(oCertificate.signatureAlgorithm);
	
	mSignature.copyToASNObject(oCertificate.signature_);
	return SUCCESS;
}

void ECertificateData::freeASNObject(ASN1T_EXP_Certificate & oCertificate)const
{
	TBSCertificate().freeASNObject(oCertificate.tbsCertificate);
	AlgorithmIdentifier().freeASNObject(oCertificate.signatureAlgorithm);
	EBitString::freeASNObject(oCertificate.signature_);
}

int ECertificateData::copyFromASNObject(const ASN1T_EXP_Certificate &iCertificate)
{
	mTBSCertificate.copyFromASNObject(iCertificate.tbsCertificate);
	mSignatureAlgorithm.copyFromASNObject(iCertificate.signatureAlgorithm);
	mSignature.copyFromASNObject(iCertificate.signature_);
	return SUCCESS;
}

int  ECertificateData::copyCertificates(const QList<ECertificateData> iList ,ASN1TPDUSeqOfList & oCerts)
{
	return copyASNObjects<ECertificateData>(iList,oCerts);
}

int	ECertificateData::copyCertificates(const ASN1TPDUSeqOfList & iCerts, QList<ECertificateData>& oList)
{
	return copyASNObjects<ECertificateData>(iCerts,oList);
}



const TBSCertificate& ECertificate::getTBSCertificate() const
{
	return d->mTBSCertificate;
}

TBSCertificate& ECertificate::getTBSCertificate() 
{
	return d->mTBSCertificate;
}
	
const AlgorithmIdentifier& ECertificate::getSignatureAlgorithm()const 
{
	return d->mSignatureAlgorithm;
}

const EBitString & ECertificate::getSignature()const
{
	return d->mSignature;
}

void ECertificate::setSignature(const EBitString & iSignature) 
{
	d->mSignature = iSignature;
}
void ECertificate::setSignatureAlgorithm(const AlgorithmIdentifier & iSignatureAlgorithm)
{
	d->mSignatureAlgorithm = iSignatureAlgorithm;
}

QString ECertificate::toString() const
{
	return d->mTBSCertificate.getSubject().toString();
}


bool ECertificate::kokSertifikaMi()const
{
	return ( isSelfSigned());
}

bool ECertificate::isSelfSigned()const
{
	if ( d->mTBSCertificate.getIssuer() != d->mTBSCertificate.getSubject())
		return false;

	AuthorityKeyIdentifier aki;
	int res = ExtensionGenerator::getAKIExtension(d->mTBSCertificate.getExtensions(),aki);

	if (res >= 0)
	{
		if (aki.isKeyIdentifierPresent())
		{
			SubjectKeyIdentifier ski;
			int res = ExtensionGenerator::getSKIExtension(d->mTBSCertificate.getExtensions(),ski);
			if ( res >= 0)
				return (ski.getKeyIdentifier() == aki.getKeyIdentifier());
			else return false;
		}
		else if (aki.isAuthorityCertSerialNumberPresent()) 
		{
			return (aki.getAuthorityCertSerialNumber() == d->mTBSCertificate.getSerialNumber());
		}
	}
	else return true;
}

bool ECertificate::isSelfIssued()const
{
	return (d->mTBSCertificate.getIssuer() == d->mTBSCertificate.getSubject());
}

bool ECertificate::isCACertificate()const
{
	BasicConstraints bc;
	int res = ExtensionGenerator::getBCExtension(d->mTBSCertificate.getExtensions(),bc);
	if ( res <0)
		return false;

	return bc.getCA();
}

QString ECertificate::getEPosta() const
{
	SubjectAlternativeName san;
	if ( ExtensionGenerator::getSANExtension(d->mTBSCertificate.getExtensions(),san) == FAILURE ) 
		return QString();

	return san.getEPosta();	

}

QList<CDP> ECertificate::getCDPs()const 
{
	QList<CDP> cdpList;
	CRLDistributionPoints crlDPs;

	
	int res = ExtensionGenerator::getCDPExtension(d->mTBSCertificate.getExtensions(),crlDPs);

	const QList<DistributionPoint> & dpList = crlDPs.getList();

	if ( res== FAILURE )
	{
		return QList<CDP>();
	}

	for ( int i = 0 ; i<dpList.size() ; i++ )
	{
		if(dpList[i].isDPNPresent())
		{
			CDP cdp;
			int res = CRLDistributionPoints::DPtoCDP(dpList[i], d->mTBSCertificate.getIssuer(),cdp);
			if(res != FAILURE)
			{
				cdpList.append(cdp);
			}
		}
	}
	return cdpList;
}

QList<Name> ECertificate::getCRLIssuers(const QString &aAdresTipi)const
{
	QList<CDP> cdpler = getCDPs();
	QList<Name> crlIssuerlar;
	if(cdpler.size() == 0)
	{
		return crlIssuerlar;
	}

	for(int i = 0; i< cdpler.size() ; i++)
	{
		if(cdpler[i].adresTipiAl() == aAdresTipi && cdpler[i].crlIssuerAl())
		{
			crlIssuerlar.append(*(cdpler[i].crlIssuerAl()));
		}
	}
	return crlIssuerlar;
}

QList<QString> ECertificate::getCDPAddresses(const QString &aAdresTipi)const
{
	QList<CDP> cdpler = getCDPs();
	QList<QString> cdpAddresses;
	if(cdpler.size() == 0)
	{
		return cdpAddresses;
	}

	for(int i = 0; i< cdpler.size() ; i++)
	{
		if(cdpler[i].adresTipiAl() == aAdresTipi)
		{
			cdpAddresses.append(cdpler[i].cdpAdresiAl());
		}
	}

	return cdpAddresses;
}

QList<Name> ECertificate::getCRLDizinIssuers()const 
{
	return getCRLIssuers(AT_LDAP);
}


QList<Eklenti> ECertificate::getEklentiler()const 
{
	QList<Eklenti> eklentiler;
	const QList<Extension> & extensions = d->mTBSCertificate.getExtensions();  
	for (int i= 0 ; i < extensions.size(); i++ )
	{
		Eklenti e(extensions[i]);
		eklentiler.append(e);
	}

	return eklentiler;
}


bool ECertificate::imzalamaSertifikasimi()const
{
	KeyUsage ku;
	int res = ExtensionGenerator::getKeyUsageExtension(d->mTBSCertificate.getExtensions(),ku);
	if (res != SUCCESS ) return false;

	return ( ku.isType(KeyUsage::KU_DigitalSignature) && ku.isType(KeyUsage::KU_NonRepudiation));
}

bool ECertificate::sifrelemeSertifikasimi()const
{
	KeyUsage ku;
	int res = ExtensionGenerator::getKeyUsageExtension(d->mTBSCertificate.getExtensions(),ku);
	if (res != SUCCESS ) return false;

	return ( ku.isType(KeyUsage::KU_KeyEncipherment));
}


void ECertificate::setTBSCertificate(const TBSCertificate & iTBSCertificate)
{
	d->mTBSCertificate = iTBSCertificate;
}

Name ECertificate::getCRLIssuer()const
{
	Name  crlIssuer;
	bool crlIssuerFound = false;
	CRLDistributionPoints cdp;
	int cdpFound = ExtensionGenerator::getCDPExtension( d->mTBSCertificate.getExtensions(), cdp);
	if	( cdpFound == SUCCESS)
	{
		for ( int i= 0; i<cdp.getList().size() ; i++ )
		{
			if ( cdp.getList()[i].isCRLIssuerPresent() )
			{
				crlIssuerFound = true;

				if (cdp.getList()[i].getCRLIssuer().isEmpty())
					return Name();// UNDEFINED
				else if (cdp.getList()[i].getCRLIssuer()[0].getType()!= GNT_DirectoryName)
					return Name();//UNDEFINED
				else
				{
					crlIssuer =  cdp.getList()[i].getCRLIssuer()[0].getDirectoryName();
					return crlIssuer;
				}
			}
		}
	}
	return d->mTBSCertificate.getIssuer(); // crlIssuer ile issuer ayný olmalý.
}

bool ECertificate::hasIndirectCRL()const
{
	return (getCRLIssuer() != d->mTBSCertificate.getIssuer() );
}

bool ECertificate::isOCSPCertificate()const
{
	ExtendedKeyUsage eku;
	int res = ExtensionGenerator::getEKUExtension( d->mTBSCertificate.getExtensions(),eku);
	if ( res != SUCCESS )
		return false;

	return KeyPurposeId::hasKeyPurpose(eku.getList(),IMP_id_kp_OCSPSigning);
}

int ECertificate::indexOfExtension(const ASN1TObjId& iExtnID)const
{
	const QList<Extension>& extensions = d->mTBSCertificate.getExtensions();
	
	for (int i = 0 ; i<extensions.size(); i++ )
	{
		if ( (ASN1OBJID)extensions[i].getExtensionId() == iExtnID )
		{
			return i;
		}
	}
	return -1;
}

bool ECertificate::isQualified()const
{
	QCStatements qcs;
	int res = ExtensionGenerator::getQCStatementsExtension(getTBSCertificate().getExtensions(),qcs);

	if (res!= SUCCESS  )
	{
		return false;
	}
	return true;
}

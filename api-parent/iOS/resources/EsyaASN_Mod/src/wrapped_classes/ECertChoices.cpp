#include "ECertChoices.h"

#include "rtContext.h"

#include "asn1CppTypes.h"

using namespace esya;

ECertChoices::ECertChoices(void)
{
}

ECertChoices::ECertChoices(const QByteArray & iCC)
{
	constructObject(iCC);
}

ECertChoices::ECertChoices(const ASN1T_CMS_CertificateChoices & iCC)
{
	copyFromASNObject(iCC);
}

ECertChoices::ECertChoices(const ECertChoices& iCC)
: mType(iCC.getType()) , mCertificate(iCC.getCertificate())
{
}

ECertChoices::ECertChoices(const ECertificate& iCert)
: mType(T_Certificate) , mCertificate(iCert)
{
}

ECertChoices & ECertChoices::operator=(const ECertChoices & iCC)
{
	mType = iCC.getType();
	mCertificate = iCC.getCertificate();
	return (*this);
}

bool esya::operator==(const ECertChoices & iRHS,const ECertChoices & iLHS )
{
	if (iRHS.getType() == iLHS.getType() && iRHS.getType() == T_Certificate )	
	{
		if ( iRHS.getCertificate() == iLHS.getCertificate() ) 
			return true;
	}

	false;
}

bool esya::operator!=(const ECertChoices & iRHS,const ECertChoices & iLHS)
{
	return ( !( iRHS == iLHS ) );
}

int ECertChoices::copyFromASNObject(const ASN1T_CMS_CertificateChoices & iCC)
{
	mType = (CertType)iCC.t;
	if ( mType == T_Certificate && iCC.u.certificate)
	{
		mCertificate.copyFromASNObject(*iCC.u.certificate);
	}
	else 
	{
		throw EException("Desteklenmeyen Sertifika Tipi",__FILE__,__LINE__);
	}
	return SUCCESS;
}

int ECertChoices::copyToASNObject(ASN1T_CMS_CertificateChoices & oCC)const
{
	oCC.t = mType;
	if ( mType == T_Certificate )
	{
		oCC.u.certificate = mCertificate.getASNCopy();
	}
	return SUCCESS;
}

void ECertChoices::freeASNObject(ASN1T_CMS_CertificateChoices & oCC)const
{
	if ( oCC.t == T_Certificate && oCC.u.certificate)
	{
		ECertificateData().freeASNObjectPtr(oCC.u.certificate);
	}
}

int ECertChoices::copyCS(const ASN1T_CMS_CertificateSet & iCS, QList<ECertChoices>& oList)
{
	return copyASNObjects<ECertChoices>(iCS,oList);
}

int ECertChoices::copyCS(const QList<ECertChoices> & iList ,ASN1T_CMS_CertificateSet& oCS)
{
	return copyASNObjects<ECertChoices>(iList,oCS);
}


/**
* \brief
* DIKKAT! bu fonksiyon certifikalarýn IMPLICIT olarak encode edildiðini varsyarak DECODE iþlemi yapar.
* 
*/
int ECertChoices::copyCS(const QByteArray & iASNBytes, QList<ECertChoices>& oList)
{
	ASN1BERDecodeBuffer decBuf ;
	decBuf.setBuffer((OSOCTET*)iASNBytes.data(), iASNBytes.size());

	int stat = 0;
	ASN1INT length;
	ASN1T_CMS_CertificateSet certSet;
	ASN1C_CMS_CertificateSet cCertSet(decBuf,certSet);
	//certSet.setContext(decBuf.getContext());
	///

	ASN1CTXT * pctxt = decBuf.getCtxtPtr();
	if (XD_PEEKTAG (pctxt, 0x80)) 
	{
		stat = xd_Tag1AndLen (pctxt, &length);
		if (stat != 0) 
		{		
			throw EException(QString(CMS_ASND_CERTIFICATESET_1).arg(stat),__FILE__,__LINE__);
		}
		stat = asn1D_CMS_CertificateSet (pctxt, 
			&certSet, ASN1IMPL, length);
		if (stat == 0) 
		{
			ECertChoices::copyCS(certSet,oList);			
			if (length == ASN_K_INDEFLEN) 
			{
				if (XD_MATCHEOC (pctxt)) XD_BUMPIDX (pctxt, 2);
				else 
				{
					LOG_RTERR (pctxt, ASN_E_INVLEN);
					throw EException(QString(CMS_ASND_CERTIFICATESET_1).arg(ASN_E_INVLEN),__FILE__,__LINE__);
				}
			}
		}
		else 
		{
			throw EException(QString(CMS_ASND_CERTIFICATESET_1).arg(stat),__FILE__,__LINE__);
		}
	}	
	return SUCCESS;
}

/**
* \brief
* DIKKAT! bu fonksiyon certifikalarýn IMPLICIT olarak ENCODE iþlemi yapar.
* 
*/

int ECertChoices::copyCS(const QList<ECertChoices>& iList, QByteArray & oASNBytes)
{
	ASN1BEREncodeBuffer encBuf;
	ASN1T_CMS_CertificateSet certSet;
	ASN1C_CMS_CertificateSet cCS(encBuf,certSet);

	ECertChoices::copyCS(iList,certSet);

	

	
	int ll =0;
	ll = xe_tag_len(encBuf.getCtxtPtr(), TM_CTXT|TM_CONS|0,
		asn1E_CMS_CertificateSet (encBuf.getCtxtPtr(), &certSet, ASN1IMPL));
	if (ll < 0)
	{
		throw EException(CMS_ASNE_CERTIFICATESET,__FILE__,__LINE__);
	}
	oASNBytes.clear();
	oASNBytes.append(QByteArray((const char*)encBuf.getMsgPtr(),ll));

	ECertChoices().freeASNObjects(certSet);
	return SUCCESS;
}

const CertType & ECertChoices::getType() const 
{
	return mType;
}

const ECertificate & ECertChoices::getCertificate() const
{
	return mCertificate;
}

ECertChoices::~ECertChoices(void)
{
}

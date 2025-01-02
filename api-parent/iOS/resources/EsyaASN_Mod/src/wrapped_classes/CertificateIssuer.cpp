#include "CertificateIssuer.h"
#include "OrtakDil.h"

using namespace esya;

CertificateIssuer::CertificateIssuer(void)
{
}

CertificateIssuer::CertificateIssuer(const ASN1T_IMP_CertificateIssuer &iCI)
{
	copyFromASNObject(iCI);
}

CertificateIssuer::CertificateIssuer(const QByteArray &iCI)
{
	constructObject(iCI);
}

CertificateIssuer::CertificateIssuer(const CertificateIssuer&iCI)
: mList(iCI.getList())
{
}

CertificateIssuer & CertificateIssuer::operator=(const CertificateIssuer&iCI)
{
	mList = iCI.getList();
	return *this;
}

bool esya::operator==(const CertificateIssuer & iRHS, const CertificateIssuer& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const CertificateIssuer & iRHS, const CertificateIssuer& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int CertificateIssuer::copyFromASNObject(const ASN1T_IMP_CertificateIssuer &iCI)
{
	GeneralName().copyGeneralNames(iCI,mList);
	return SUCCESS;
}

int CertificateIssuer::copyToASNObject(ASN1T_IMP_CertificateIssuer& oCI)const
{
	GeneralName().copyGeneralNames(mList,oCI);
	return SUCCESS;
}

void CertificateIssuer::freeASNObject(ASN1T_IMP_CertificateIssuer & oCI)const
{
	GeneralName().freeASNObjects(oCI);
}

/////////////////////////////////////////////////////////////////

const QList<GeneralName> &CertificateIssuer::getList() const
{
	return mList;
}

void CertificateIssuer::setList(const QList<GeneralName> & iList) 
{
	mList = iList;
}

void CertificateIssuer::appendGeneralName(const GeneralName& iGN) 
{
	mList.append(iGN);
}

bool CertificateIssuer::hasIssuer(const Name& iIssuer)const
{
	Q_FOREACH(GeneralName gn, mList)
	{
		if (gn.getType()== GNT_DirectoryName )
		{
			QString gnName = gn.getDirectoryName().toString();
			QString issuer = iIssuer.toString();
			if (gn.getDirectoryName() == iIssuer)
				return true;
		}
	}
	return false;
}

QString CertificateIssuer::toString() const
{
	return QString();
}

QStringList CertificateIssuer::toStringList() const 
{
	return QStringList(); 
}



/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString CertificateIssuer::eklentiAdiAl()			const 
{
	return DIL_EXT_SERTIFIKA_ILKELERI;
}

QString CertificateIssuer::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString CertificateIssuer::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* CertificateIssuer::kendiniKopyala() const 
{
	return (AY_Eklenti* )new CertificateIssuer(*this);
}


CertificateIssuer::~CertificateIssuer(void)
{
}

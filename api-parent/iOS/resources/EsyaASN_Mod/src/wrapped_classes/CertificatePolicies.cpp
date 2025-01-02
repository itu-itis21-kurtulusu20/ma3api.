#include "CertificatePolicies.h"
#include "OrtakDil.h"

using namespace esya;

CertificatePolicies::CertificatePolicies(void)
{
}

CertificatePolicies::CertificatePolicies(const ASN1T_IMP_CertificatePolicies &iCP)
{
	copyFromASNObject(iCP);
}

CertificatePolicies::CertificatePolicies(const QByteArray &iAIA)
{
	constructObject(iAIA);
}

CertificatePolicies::CertificatePolicies(const CertificatePolicies&iAIA)
: mList(iAIA.getList())
{
}

CertificatePolicies::CertificatePolicies(const QList<PolicyInformation>&iCP)
: mList(iCP)
{
}

CertificatePolicies & CertificatePolicies::operator=(const CertificatePolicies&iCP)
{
	mList = iCP.getList();
	return *this;
}

bool esya::operator==(const CertificatePolicies & iRHS, const CertificatePolicies& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const CertificatePolicies & iRHS, const CertificatePolicies& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int CertificatePolicies::copyFromASNObject(const ASN1T_IMP_CertificatePolicies &iCP)
{
	PolicyInformation().copyPIs(iCP,mList);
	return SUCCESS;
}

int CertificatePolicies::copyToASNObject(ASN1T_IMP_CertificatePolicies& oCP)const
{
	PolicyInformation().copyPIs(mList,oCP);
	return SUCCESS;
}

void CertificatePolicies::freeASNObject(ASN1T_IMP_CertificatePolicies & oCP)const
{
	PolicyInformation().freeASNObjects(oCP);
}

/////////////////////////////////////////////////////////////////

const QList<PolicyInformation> &CertificatePolicies::getList() const
{
	return mList;
}

QString CertificatePolicies::toString() const
{
	return QString();
}

QStringList CertificatePolicies::toStringList() const 
{
	return QStringList(); 
}


int CertificatePolicies::indexOf(const ASN1TObjId iPolicyId)const
{
	for (int i = 0 ; i < mList.size() ; i++)
	{
		if (mList[i].getPolicyIdentifier() == iPolicyId)
			return i;
	}
	return -1;
}

void  CertificatePolicies::appendPolicy(const ASN1TObjId iPolicyId)
{
	mList.append(PolicyInformation(iPolicyId));
}

void  CertificatePolicies::setList(const QList<PolicyInformation>& iList)
{
	mList = iList;	
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString CertificatePolicies::eklentiAdiAl()			const 
{
	return DIL_EXT_SERTIFIKA_ILKELERI;
}

QString CertificatePolicies::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString CertificatePolicies::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* CertificatePolicies::kendiniKopyala() const 
{
	return (AY_Eklenti* )new CertificatePolicies(*this);
}


CertificatePolicies::~CertificatePolicies(void)
{
}

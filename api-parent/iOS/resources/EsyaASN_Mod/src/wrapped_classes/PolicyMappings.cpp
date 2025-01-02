#include "PolicyMappings.h"
#include "OrtakDil.h"
#include <QStringList>

using namespace esya;

PolicyMappings::PolicyMappings()
{
}

PolicyMappings::PolicyMappings(const ASN1T_IMP_PolicyMappings& iPM)
{
	copyFromASNObject(iPM);
}

PolicyMappings::PolicyMappings(const QByteArray & iPM )
{
	constructObject(iPM);
}

PolicyMappings::PolicyMappings(const PolicyMappings&iPM)
:	mList(iPM.getList())
{
}


PolicyMappings& PolicyMappings::operator=(const PolicyMappings&iPM)
{
	mList = iPM.getList();
	return (*this);
}

bool esya::operator==(const PolicyMappings & iRHS, const PolicyMappings & iLHS)
{
	return (iRHS.getList() == iLHS.getList() );
}
bool esya::operator!=(const PolicyMappings & iRHS, const PolicyMappings & iLHS)
{
	return (iRHS.getList() != iLHS.getList() );
}

int PolicyMappings::copyFromASNObject(const ASN1T_IMP_PolicyMappings& iPM)
{
	return PolicyMappingsElement().copyMappings(iPM,mList);
}

int PolicyMappings::copyToASNObject(ASN1T_IMP_PolicyMappings & oPM) const
{
	return PolicyMappingsElement().copyMappings(mList,oPM);
}

void PolicyMappings::freeASNObject(ASN1T_IMP_PolicyMappings & oPM)const
{
	PolicyMappingsElement().freeASNObjects(oPM);
}

const QList<PolicyMappingsElement>& PolicyMappings::getList() const
{
	return mList;
}

void PolicyMappings::setList(const QList<PolicyMappingsElement>& iPM)
{
	mList = iPM;
}

void PolicyMappings::addMapping(const PolicyMappingsElement& iPME)
{
	mList.append(iPME);
}

PolicyMappings::~PolicyMappings(void)
{
}

QList<ASN1TObjId> PolicyMappings::getSubjectEquivalents(const ASN1TObjId & iIssuerDomainPolicy)const
{
	QList<ASN1TObjId> pList;

	Q_FOREACH(PolicyMappingsElement pme, mList)
	{
		if (pme.getIssuerDomainPolicy()== iIssuerDomainPolicy)
			pList.append(pme.getSubjectDomainPolicy());
	}

	return pList;
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString PolicyMappings::eklentiAdiAl() const 
{
	return DIL_EXT_ILKE_ESLESTIRMELERI;
}

QString PolicyMappings::eklentiKisaDegerAl()	const 
{
	return DIL_EXT_ILKE_ESLESTIRMELERI;
}

QString PolicyMappings::eklentiUzunDegerAl()	const 
{
	return DIL_EXT_ILKE_ESLESTIRMELERI;
}

AY_Eklenti* PolicyMappings::kendiniKopyala() const 
{
	return (AY_Eklenti* )new PolicyMappings(*this);
}
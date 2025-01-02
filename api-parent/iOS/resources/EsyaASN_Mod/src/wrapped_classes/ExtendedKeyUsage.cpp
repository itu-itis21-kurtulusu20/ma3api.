#include "ExtendedKeyUsage.h"
#include "EASNToStringUtils.h"
#include "OrtakDil.h"

using namespace esya;

ExtendedKeyUsage::ExtendedKeyUsage(void)
{
}

ExtendedKeyUsage::ExtendedKeyUsage(const ASN1T_IMP_ExtKeyUsageSyntax &iEKU)
{
	copyFromASNObject(iEKU);
}

ExtendedKeyUsage::ExtendedKeyUsage(const QByteArray &iEKU)
{
	constructObject(iEKU);
}

ExtendedKeyUsage::ExtendedKeyUsage(const ExtendedKeyUsage&iEKU)
: mList(iEKU.getList())
{
}

ExtendedKeyUsage::ExtendedKeyUsage(const QList<KeyPurposeId>&iEKU)
: mList(iEKU)
{
}

ExtendedKeyUsage& ExtendedKeyUsage::operator=(const ExtendedKeyUsage&iEKU)
{
	mList = iEKU.getList();
	return *this;
}

bool esya::operator==(const ExtendedKeyUsage & iRHS, const ExtendedKeyUsage& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const ExtendedKeyUsage & iRHS, const ExtendedKeyUsage& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int ExtendedKeyUsage::copyFromASNObject(const ASN1T_IMP_ExtKeyUsageSyntax &iEKU)
{
	KeyPurposeId().copyKPIs(iEKU,mList);
	return SUCCESS;
}

int ExtendedKeyUsage::copyToASNObject(ASN1T_IMP_ExtKeyUsageSyntax& oEKU)const
{
	KeyPurposeId().copyKPIs(mList,oEKU);
	return SUCCESS;
}

void ExtendedKeyUsage::freeASNObject(ASN1T_IMP_ExtKeyUsageSyntax & oEKU) const
{
	KeyPurposeId().freeASNObjects(oEKU);
}

/////////////////////////////////////////////////////////////////

const QList<KeyPurposeId> &ExtendedKeyUsage::getList() const
{
	return mList;
}

QString ExtendedKeyUsage::toString() const
{
	return QString();
}




ExtendedKeyUsage::~ExtendedKeyUsage(void)
{
}


const bool ExtendedKeyUsage::hasKeyPurposeID(const ASN1TObjId & iKeyPurpose)const
{
	return mList.contains(iKeyPurpose);
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString ExtendedKeyUsage::eklentiAdiAl() const 
{
	return DIL_EXT_GELISMIS_ANAHTAR_KULLANIMI;
}

QString ExtendedKeyUsage::eklentiKisaDegerAl()	const 
{
	QString lPurposeStr;

	for (int i = 0 ; i < mList.size() ;i++)
	{
		lPurposeStr += EASNToStringUtils::type2String(mList[i].getKeyPurposeId())+",";
	}

	return lPurposeStr;
}

QString ExtendedKeyUsage::eklentiUzunDegerAl()	const 
{
	QString lPurposeStr;

	for (int i = 0 ; i < mList.size() ;i++)
		{
			lPurposeStr += EASNToStringUtils::type2String(mList[i].getKeyPurposeId())+"\n";
		} 
	return lPurposeStr;
}

AY_Eklenti* ExtendedKeyUsage::kendiniKopyala() const 
{
	return (AY_Eklenti* )new ExtendedKeyUsage(*this);
}

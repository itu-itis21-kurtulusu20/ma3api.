#include "KeyPurposeId.h"
#include "ortak.h"
#include "ESeqOfList.h"


using namespace esya;
NAMESPACE_BEGIN(esya)
KeyPurposeId::KeyPurposeId(void)
{
}

KeyPurposeId::KeyPurposeId(const ASN1T_IMP_KeyPurposeId & iKPI)
{
	copyFromASNObject(iKPI);
}

KeyPurposeId::KeyPurposeId(const KeyPurposeId & iKPI)
:	mKeyPurposeId(iKPI.getKeyPurposeId())
{
}

KeyPurposeId::KeyPurposeId(const  QByteArray & iKPI)
{
	constructObject(iKPI);
}

KeyPurposeId& KeyPurposeId::operator=(const KeyPurposeId& iKPI )
{
	mKeyPurposeId = iKPI.getKeyPurposeId();
	return *this;
}

bool operator==(const KeyPurposeId & iRHS ,const KeyPurposeId & iLHS)
{
	return ( iRHS.getKeyPurposeId() == iLHS.getKeyPurposeId() );
}

bool operator!=(const KeyPurposeId & iRHS, const KeyPurposeId & iLHS)
{
	return ( !( iRHS == iLHS ) ) ;
}

int KeyPurposeId::copyFromASNObject(const ASN1T_IMP_KeyPurposeId & iKPI)
{
	mKeyPurposeId = iKPI;
	return SUCCESS;
}

int KeyPurposeId::copyToASNObject(ASN1T_IMP_KeyPurposeId & oKPI) const
{
	oKPI = mKeyPurposeId;
	return SUCCESS;
}

void KeyPurposeId::freeASNObject(ASN1T_IMP_KeyPurposeId & oKPI)const
{
}

int KeyPurposeId::copyKPIs(const ASN1TPDUSeqOfList & iKPIs, QList<KeyPurposeId>& oList)
{
	return copyASNObjects<KeyPurposeId>(iKPIs,oList);
}

int KeyPurposeId::copyKPIs(const QList<KeyPurposeId> iList ,ASN1TPDUSeqOfList & oKPIs)
{
	return copyASNObjects<KeyPurposeId>(iList,oKPIs);
}

bool KeyPurposeId::hasKeyPurpose(const QList<KeyPurposeId> iList , const ASN1TObjId & iKPI )
{
	for ( int i = 0 ; i < iList.size() ; i++ )
	{
		if ( iList[i].getKeyPurposeId() == iKPI )
		{
			return true;
		}
	}
	return false;
}

const ASN1TObjId& KeyPurposeId::getKeyPurposeId()const
{
	return mKeyPurposeId;
}

KeyPurposeId::~KeyPurposeId(void)
{
}
NAMESPACE_END

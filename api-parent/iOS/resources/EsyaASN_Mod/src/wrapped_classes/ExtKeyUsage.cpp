#include "ExtKeyUsage.h"
#include "OrtakDil.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

ExtKeyUsage::ExtKeyUsage(void)
{
}

ExtKeyUsage::ExtKeyUsage(const ExtKeyUsage &iEKU)
: mKeyPurposeIDs(iEKU.getKeyPurposeIDs())
{
}

ExtKeyUsage::ExtKeyUsage(const ASN1T_IMP_ExtKeyUsageSyntax & iEKU)
{
	copyFromASNObject(iEKU);
}

ExtKeyUsage::ExtKeyUsage(const QByteArray & iEKU)
{
	constructObject(iEKU);	
}


ExtKeyUsage & ExtKeyUsage::operator=(const ExtKeyUsage& iEKU)
{
	mKeyPurposeIDs = iEKU.getKeyPurposeIDs();
	return (*this);
}

bool operator==(const ExtKeyUsage& iRHS,const ExtKeyUsage& iLHS)
{
	const QList<ASN1TObjId> & rList = iRHS.getKeyPurposeIDs();
	const QList<ASN1TObjId> & lList = iLHS.getKeyPurposeIDs();
	if (rList.size() != lList.size()) return false;
	for (int i = 0 ; i< rList.size(); i++)
	{
		if ( rList[i] != lList[i] )
			return false;
	}
	return true;
}

bool operator!=(const ExtKeyUsage& iRHS, const ExtKeyUsage& iLHS)
{
	return (!(iRHS == iLHS ));	
}


int ExtKeyUsage::copyFromASNObject(const ASN1T_IMP_ExtKeyUsageSyntax & iEKU)
{
	for (int i = 0 ; i<iEKU.count ; i++ )	
	{
		ASN1TObjId * pKP = (ASN1TObjId *) ESeqOfList::get(iEKU,i);
		mKeyPurposeIDs.append(*pKP);
	}
	return SUCCESS;
}

int ExtKeyUsage::copyToASNObject(ASN1T_IMP_ExtKeyUsageSyntax & oEKU) const
{
	oEKU.count = 0 ;
	for (int i = 0 ; i<mKeyPurposeIDs.size() ; i++	)
	{
		ASN1TObjId* pKP = new ASN1TObjId( mKeyPurposeIDs[i] );
		ESeqOfList::append(oEKU,pKP);
	}
	return SUCCESS;
}

void ExtKeyUsage::freeASNObject(ASN1T_IMP_ExtKeyUsageSyntax & oEKU)const
{
	ASN1C_IMP_ExtKeyUsageSyntax cEKU(oEKU);
	for (int i = 0 ; i<cEKU.size() ; i++)
	{
		ASN1TObjId * pKP = (ASN1TObjId*)cEKU.get(i);
		DELETE_MEMORY(pKP)
	}
	ESeqOfList::free(oEKU);
}

const QList<ASN1TObjId>& ExtKeyUsage::getKeyPurposeIDs() const
{
	return mKeyPurposeIDs;
}

const bool ExtKeyUsage::hasKeyPurposeID(const ASN1TObjId & iKP)const
{
	return mKeyPurposeIDs.contains(iKP);
}


void ExtKeyUsage::addKeyPurposeID(const ASN1TObjId & iKP)
{
	mKeyPurposeIDs.append(iKP);
}


ExtKeyUsage::~ExtKeyUsage(void)
{
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString ExtKeyUsage::eklentiAdiAl() const 
{
	return DIL_EXT_ILKE_KISITLAMALARI;
}

QString ExtKeyUsage::eklentiKisaDegerAl()	const 
{
	return "";
}

QString ExtKeyUsage::eklentiUzunDegerAl()	const 
{
	return "";
}

AY_Eklenti* ExtKeyUsage::kendiniKopyala() const 
{
	return (AY_Eklenti* )new ExtKeyUsage(*this);
}
NAMESPACE_END

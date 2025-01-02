#include "SubjectKeyIdentifier.h"
#include "OrtakDil.h"


using namespace esya;

SubjectKeyIdentifier::SubjectKeyIdentifier(void)
{
}

SubjectKeyIdentifier::SubjectKeyIdentifier(const SubjectKeyIdentifier &iSKI)
: mKeyIdentifier(iSKI.getKeyIdentifier())
{

}


SubjectKeyIdentifier::SubjectKeyIdentifier(const ASN1T_IMP_SubjectKeyIdentifier & iSKI)
{
	copyFromASNObject(iSKI);
}

SubjectKeyIdentifier::SubjectKeyIdentifier(const QByteArray & iSKI)
{
	constructObject(iSKI);
}

SubjectKeyIdentifier & SubjectKeyIdentifier::operator=(const SubjectKeyIdentifier& iSKI)
{
	mKeyIdentifier = iSKI.getKeyIdentifier();
	return (*this);
}

bool esya::operator==(const SubjectKeyIdentifier& iRHS, const SubjectKeyIdentifier& iLHS)
{
	return ( iRHS.getKeyIdentifier() == iLHS.getKeyIdentifier() );
}

bool esya::operator!=(const SubjectKeyIdentifier& iRHS, const SubjectKeyIdentifier& iLHS)
{
	return  ( !( iRHS == iLHS ) );
}

int SubjectKeyIdentifier::copyFromASNObject(const ASN1T_IMP_SubjectKeyIdentifier& iSKI)
{
	mKeyIdentifier = QByteArray((const char*)iSKI.data , iSKI.numocts);
	return SUCCESS;
}

int SubjectKeyIdentifier::copyToASNObject(ASN1T_IMP_SubjectKeyIdentifier & oSKI) const
{
	oSKI.data = (OSOCTET*)myStrDup(mKeyIdentifier.data(),mKeyIdentifier.size());
	oSKI.numocts = mKeyIdentifier.size(); 
	return SUCCESS;
}

void SubjectKeyIdentifier::freeASNObject(ASN1T_IMP_SubjectKeyIdentifier & oSKI)const
{
	DELETE_MEMORY_ARRAY(oSKI.data)
}

const QByteArray & SubjectKeyIdentifier::getKeyIdentifier() const
{
	return mKeyIdentifier;
}

SubjectKeyIdentifier::~SubjectKeyIdentifier(void)
{
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString SubjectKeyIdentifier::eklentiAdiAl() const 
{
	return DIL_EXT_OZNE_ANAHTAR_TANIMLAYICISI;
}

QString SubjectKeyIdentifier::eklentiKisaDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mKeyIdentifier);
}

QString SubjectKeyIdentifier::eklentiUzunDegerAl()	const 
{
	return EASNToStringUtils::byteArrayToStr(mKeyIdentifier);
}

AY_Eklenti* SubjectKeyIdentifier::kendiniKopyala() const 
{
	return (AY_Eklenti* )new SubjectKeyIdentifier(*this);
}

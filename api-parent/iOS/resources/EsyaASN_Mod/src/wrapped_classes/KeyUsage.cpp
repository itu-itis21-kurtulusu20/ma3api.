#include "KeyUsage.h"
#include "EBitString.h"
#include "OrtakDil.h"


using namespace esya;



KeyUsage::KeyUsage(void)
: mNumBits(0)
{
}

KeyUsage::KeyUsage(const KeyUsage &iKU)
: mNumBits(iKU.getNumBits())
{
	memcpy(mData,iKU.getData(),DATASIZE);
}

KeyUsage::KeyUsage(const int iNumBits , const OSOCTET * iData)
: mNumBits(iNumBits) 
{
	memcpy(mData,iData,DATASIZE);
}

KeyUsage::KeyUsage(const ASN1T_IMP_KeyUsage & iKU)
{
	copyFromASNObject(iKU);
}

KeyUsage::KeyUsage(const QByteArray & iKU)
{
	constructObject(iKU);
}

KeyUsage & KeyUsage::operator=(const KeyUsage& iKU)
{
	mNumBits = iKU.getNumBits();
	memcpy(mData,iKU.getData(),DATASIZE);
	return (*this);
}

bool esya::operator==(const KeyUsage& iRHS, const KeyUsage& iLHS)
{
	return ( iRHS.getNumBits() == iLHS.getNumBits() && !memcmp(iRHS.getData(),iLHS.getData(),KeyUsage::DATASIZE) );
}

bool esya::operator!=(const KeyUsage& iRHS, const KeyUsage& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int KeyUsage::copyFromASNObject(const ASN1T_IMP_KeyUsage& iKU)
{
	mNumBits = iKU.numbits;
	memcpy(mData,iKU.data,DATASIZE);
	return SUCCESS;
}

int KeyUsage::copyToASNObject(ASN1T_IMP_KeyUsage & oKU) const
{
	oKU.numbits = mNumBits;
	memcpy(oKU.data,mData,DATASIZE);
	return SUCCESS;
}

void KeyUsage::freeASNObject(ASN1T_IMP_KeyUsage & oKU)const
{
	oKU.numbits = 0;
}

const int& KeyUsage::getNumBits() const
{
	return mNumBits;
}

const OSOCTET* KeyUsage::getData() const
{
	return mData;
}

void KeyUsage::setNumBits(int iNumBits)
{
	mNumBits = iNumBits;
}

void KeyUsage::setData(const OSOCTET *iData)
{
	memcpy(mData,iData,DATASIZE);	
}



bool KeyUsage::isType(const KeyUsageType iType) const
{
	if (iType>= mNumBits) return false;

	return getKUBits()[iType];
}

QBitArray KeyUsage::getKUBits() const
{
	return EBitString(mData,mNumBits).toBitArray();
}

QString KeyUsage::toBitString() const
{
	QString str;
	QBitArray bits = getKUBits();
	for (int i = 0; i < bits.size(); i++ )
	{
		str += bits[i] ? "1":"0";	
	}
	return str;
}

QString KeyUsage::bitStringToString(const QString &iBitString)
{
	
	if (iBitString.size() != KU_NUMBITS )
		return "";

	QString str("{ ");

	if ( iBitString[KU_DigitalSignature]=='1')
		str += DIL_KU_SAYISAL_IMZA_OLUSTURMA + " , ";
	if ( iBitString[KU_NonRepudiation]=='1')
		str += DIL_KU_INKAR_EDEMEMEZLIK+ " , ";
	if ( iBitString[KU_DataEncipherment]=='1')
		str += DIL_KU_VERI_SIFRELEME + " , ";
	if ( iBitString[KU_KeyEncipherment]=='1')
		str += DIL_KU_ANAHTAR_SIFRELEME + " , ";
	if ( iBitString[KU_KeyAgreement]=='1')
		str += DIL_KU_ANAHTAR_UYUSMASI + " , ";
	if ( iBitString[KU_KeyCertSign]=='1')
		str += DIL_KU_SERTIFIKA_IMZALAMA + " , ";
	if ( iBitString[KU_CRLSign]=='1')
		str += DIL_KU_SIL_IMZALAMA + " , ";
	if ( iBitString[KU_EncipherOnly]=='1')
		str += DIL_KU_SADECE_SIFRELEME + " , ";
	if ( iBitString[KU_DecipherOnly]=='1')
		str += DIL_KU_SADECE_SIFRE_COZME + " , ";

	if (str.size()>3)
		str.resize(str.size()-2);

	str.append(" }");
	return str;

}


QString KeyUsage::toString() const
{
	QString str("{ ");
	if ( isType(KU_DigitalSignature))
		str += DIL_KU_SAYISAL_IMZA_OLUSTURMA + " , ";
	if ( isType(KU_NonRepudiation))
		str += DIL_KU_INKAR_EDEMEMEZLIK+ " , ";
	if ( isType(KU_DataEncipherment))
		str += DIL_KU_VERI_SIFRELEME + " , ";
	if ( isType(KU_KeyEncipherment))
		str += DIL_KU_ANAHTAR_SIFRELEME + " , ";
	if ( isType(KU_KeyAgreement))
		str += DIL_KU_ANAHTAR_UYUSMASI + " , ";
	if ( isType(KU_KeyCertSign))
		str += DIL_KU_SERTIFIKA_IMZALAMA + " , ";
	if ( isType(KU_CRLSign))
		str += DIL_KU_SIL_IMZALAMA + " , ";
	if ( isType(KU_EncipherOnly))
		str += DIL_KU_SADECE_SIFRELEME + " , ";
	if ( isType(KU_DecipherOnly))
		str += DIL_KU_SADECE_SIFRE_COZME + " , ";

	if (str.size()>3)
		str.resize(str.size()-2);
	
	str.append(" }");
	return str;
}

QStringList KeyUsage::toStringList() const
{
	QStringList strList;
	if ( isType(KU_DigitalSignature))
		strList.append(DIL_KU_SAYISAL_IMZA_OLUSTURMA) ;
	if ( isType(KU_NonRepudiation))
		strList.append(DIL_KU_INKAR_EDEMEMEZLIK);
	if ( isType(KU_DataEncipherment))
		strList.append(DIL_KU_VERI_SIFRELEME);
	if ( isType(KU_KeyEncipherment))
		strList.append(DIL_KU_ANAHTAR_SIFRELEME);
	if ( isType(KU_KeyAgreement))
		strList.append(DIL_KU_ANAHTAR_UYUSMASI);
	if ( isType(KU_KeyCertSign))
		strList.append(DIL_KU_SERTIFIKA_IMZALAMA);
	if ( isType(KU_CRLSign))
		strList.append(DIL_KU_SIL_IMZALAMA);
	if ( isType(KU_EncipherOnly))
		strList.append(DIL_KU_SADECE_SIFRELEME);
	if ( isType(KU_DecipherOnly))
		strList.append(DIL_KU_SADECE_SIFRE_COZME);

	return strList;
}


KeyUsage::~KeyUsage(void)
{
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString KeyUsage::eklentiAdiAl() const 
{
	return DIL_EXT_ANAHTAR_KULLANIMI;
}

QString KeyUsage::eklentiKisaDegerAl()	const 
{
	QStringList lKeyUsageList = toStringList();
	return lKeyUsageList.join(",");
}

QString KeyUsage::eklentiUzunDegerAl()	const 
{
	QStringList lKeyUsageList = toStringList();
	return lKeyUsageList.join("\n");
}

AY_Eklenti* KeyUsage::kendiniKopyala() const 
{
	return (AY_Eklenti* )new KeyUsage(*this);
}

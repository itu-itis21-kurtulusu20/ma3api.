#include "SubjectAlternativeName.h"
#include "OrtakDil.h"

using namespace esya;

SubjectAlternativeName::SubjectAlternativeName(void)
{
}

SubjectAlternativeName::SubjectAlternativeName(const ASN1T_IMP_SubjectAltName &iSAN)
{
	copyFromASNObject(iSAN);
}

SubjectAlternativeName::SubjectAlternativeName(const QByteArray &iSAN)
{
	constructObject(iSAN);
}

SubjectAlternativeName::SubjectAlternativeName(const SubjectAlternativeName&iSAN)
: mList(iSAN.getList())
{
}

SubjectAlternativeName::SubjectAlternativeName(const QList<GeneralName>&iSAN)
: mList(iSAN)
{
}

SubjectAlternativeName & SubjectAlternativeName::operator=(const SubjectAlternativeName&iSAN)
{
	mList = iSAN.getList();
	return *this;
}

bool esya::operator==(const SubjectAlternativeName & iRHS, const SubjectAlternativeName& iLHS)
{
	return (iRHS.getList()==iLHS.getList());
}

bool esya::operator!=(const SubjectAlternativeName & iRHS, const SubjectAlternativeName& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int SubjectAlternativeName::copyFromASNObject(const ASN1T_IMP_SubjectAltName &iSAN)
{
	GeneralName().copyGeneralNames(iSAN,mList);
	return SUCCESS;
}

int SubjectAlternativeName::copyToASNObject(ASN1T_IMP_SubjectAltName& oSAN)const
{
	GeneralName().copyGeneralNames(mList,oSAN);
	return SUCCESS;
}

void SubjectAlternativeName::freeASNObject(ASN1T_IMP_SubjectAltName & oSAN)const
{
	GeneralName().freeASNObjects(oSAN);
}

/////////////////////////////////////////////////////////////////

const QList<GeneralName> &SubjectAlternativeName::getList() const
{
	return mList;
}

void SubjectAlternativeName::setList(const QList<GeneralName> &iList) 
{
	mList = iList;
}

void SubjectAlternativeName::appendGN(const GeneralName &iGN)
{
	mList.append(iGN);
}

QString SubjectAlternativeName::toString() const
{
	return toStringList().join(",");
}

QStringList SubjectAlternativeName::toStringList() const
{
	QStringList stList;

	for (int i = 0 ; i < mList.size(); i++)
	{
		stList.append(mList[i].toString());
	}
	return stList;
}


SubjectAlternativeName::~SubjectAlternativeName(void)
{
}

QString SubjectAlternativeName::getEPosta() const
{
	for (int i = 0 ; i < mList.size() ; i++ )
	{
		if ( mList[i].getType() == GNT_RFC822Name )
			return mList[i].getRFC822Name();
	}

	return QString();	
}

ORAddress SubjectAlternativeName::getX400Address() const
{
	for (int i = 0 ; i < mList.size() ; i++ )
	{
		if ( mList[i].getType() == GNT_X400Address )
			return mList[i].getX400Address();
	}

	return ORAddress();	
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString SubjectAlternativeName::eklentiAdiAl() const 
{
	return DIL_EXT_OZNE_DIGER_ADI;
}

QString SubjectAlternativeName::eklentiKisaDegerAl()	const 
{
	return toString();
}

QString SubjectAlternativeName::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* SubjectAlternativeName::kendiniKopyala() const 
{
	return (AY_Eklenti* )new SubjectAlternativeName(*this);
}

#include "RelativeDistinguishedName.h"

using namespace esya;


RelativeDistinguishedName::RelativeDistinguishedName(void)
{
}


RelativeDistinguishedName::RelativeDistinguishedName(const ASN1T_EXP_RelativeDistinguishedName &iRDN)
{
	copyFromASNObject(iRDN);	
}

RelativeDistinguishedName::RelativeDistinguishedName(const QByteArray &iRDN)
{
	constructObject(iRDN);
}

RelativeDistinguishedName::RelativeDistinguishedName(const RelativeDistinguishedName& iRDN)
:mList(iRDN.getList())
{
}

const QList<AttributeTypeAndValue> &RelativeDistinguishedName::getList() const 
{
	return mList;
}

void RelativeDistinguishedName::setList(const QList<AttributeTypeAndValue> & iList)
{
	mList = iList;
}

int RelativeDistinguishedName::copyFromASNObject(const ASN1T_EXP_RelativeDistinguishedName & iRDN)
{
	for ( int i = 0; i < iRDN.count; i++ )
	{
		ASN1T_EXP_AttributeTypeAndValue * pAttrVal = (ASN1T_EXP_AttributeTypeAndValue *)ESeqOfList::get(iRDN,i);
		AttributeTypeAndValue attrTV(*pAttrVal);
		mList.append(attrTV);
	}
	return SUCCESS;
}

RelativeDistinguishedName & RelativeDistinguishedName::operator=(const RelativeDistinguishedName& iRDN)
{
	mList = iRDN.getList();
	return *this;
}

bool esya::operator==(const RelativeDistinguishedName & iRHS,const RelativeDistinguishedName & iLHS)
{
	const QList<AttributeTypeAndValue> & rList = iRHS.getList();
	const QList<AttributeTypeAndValue> & lList = iLHS.getList();	
	
	if (rList.size()!= lList.size())
	{
		return false;
	}
	for (int i = 0; i< rList.size(); i++)
	{
		if (rList[i] != lList[i])
			return false;
	}

	return true;
}

bool esya::operator!=(const RelativeDistinguishedName & iRHS, const RelativeDistinguishedName & iLHS)
{
	return !(iRHS == iLHS);
}

int RelativeDistinguishedName::copyToASNObject(ASN1T_EXP_RelativeDistinguishedName & oRDN) const
{
	for (int i = 0 ; i< mList.size(); i++)
	{
		ESeqOfList::append(oRDN,mList[i].getASNCopy());
	}
	return SUCCESS;
}

void RelativeDistinguishedName::freeASNObject(ASN1T_EXP_RelativeDistinguishedName & oRDN) const
{
	ASN1C_EXP_RelativeDistinguishedName cRDN(oRDN);
	for (int i = 0 ; i< cRDN.size(); i++)
	{
		ASN1T_EXP_AttributeTypeAndValue * pAttrTV =(ASN1T_EXP_AttributeTypeAndValue *) cRDN.get(i); 
		AttributeTypeAndValue().freeASNObjectPtr(pAttrTV);
	}
	ESeqOfList::free(oRDN);
}


QString RelativeDistinguishedName::toString(bool iNormalized)const
{
      if ( mList.size()==0)
           return "";

      QString st = mList[0].toString(iNormalized);
      for (int i = 1; i < mList.size(); i++)
      {
           st += "+";
           st += mList[i].toString(iNormalized);
      }

      return st;
}

QString RelativeDistinguishedName::getEmailAttribute()const
{
	Q_FOREACH(AttributeTypeAndValue av, mList)
	{
		if (av.getAttributeType() == (ASN1TObjId)EXP_id_emailAddress)
			return EASNToStringUtils::emailAddress2String(av.getAttributeValue().getValue(),false);
	}
	return QString();
}

QString RelativeDistinguishedName::getTitleAttribute()const
{
	Q_FOREACH(AttributeTypeAndValue av, mList)
	{
		if (av.getAttributeType() == (ASN1TObjId)EXP_id_at_title)
			return EASNToStringUtils::title2String(av.getAttributeValue().getValue(),false);
	}
	return QString();
}

RelativeDistinguishedName::~RelativeDistinguishedName(void)
{
}

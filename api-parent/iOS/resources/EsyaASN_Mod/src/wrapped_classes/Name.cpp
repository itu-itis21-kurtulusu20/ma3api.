#include "Name.h"
#include "NameUtils.h"

using namespace esya;


Name::Name(void)
{
}


Name::Name(const ASN1T_EXP_Name &iName)
{
	copyFromASNObject(iName);	
}

Name::Name(const QByteArray &iName)
{
	constructObject(iName);
}


Name::Name(const QList<RelativeDistinguishedName>& iList)
: mList(iList)
{
}

Name::Name(const Name& iName)
:mList(iName.getList())
{
}

const QList<RelativeDistinguishedName> &Name::getList() const 
{
	return mList;
}

void Name::setList(const QList<RelativeDistinguishedName> &iList) 
{
	mList = iList;
}

void Name::appendRDN(const RelativeDistinguishedName & iRDN)
{
	mList.append(iRDN);
}

void Name::freeASNObject(ASN1T_EXP_Name & oName)const
{
	if (oName.u.rdnSequence)
	{
		for (int i = 0 ; i<oName.u.rdnSequence->count;i++ )
		{
			ASN1T_EXP_RelativeDistinguishedName * pRDN = (ASN1T_EXP_RelativeDistinguishedName *)ESeqOfList::get(*oName.u.rdnSequence,i);
			RelativeDistinguishedName().freeASNObjectPtr(pRDN);
		}
	}

	ESeqOfList::free(*oName.u.rdnSequence);
	DELETE_MEMORY(oName.u.rdnSequence)
}



int Name::copyFromASNObject(const ASN1T_EXP_Name & iName)
{
	mList.clear();
	if (iName.t != T_EXP_Name_rdnSequence || ! iName.u.rdnSequence)
	{
		throw EException("RDN: Desteklenmeyen Name tipi ");
	}
	for ( int i = 0; i < iName.u.rdnSequence->count; i++ )
	{
		ASN1T_EXP_RelativeDistinguishedName * pRDN = (ASN1T_EXP_RelativeDistinguishedName *)ESeqOfList::get(*iName.u.rdnSequence,i);
		RelativeDistinguishedName rDN(*pRDN);
		mList.append(rDN);
	}
	return SUCCESS;
}


int Name::copyToASNObject(ASN1T_EXP_Name& oName)const
{
	ASN1T_EXP_RDNSequence *pRDNSequence = new ASN1T_EXP_RDNSequence();
	//ASN1C_EXP_RDNSequence cRDNSequence(*pRDNSequence);

	for (int i = 0 ; i<mList.size();i++ )
	{
		ASN1T_EXP_RelativeDistinguishedName * pRDN = mList[i].getASNCopy();
		ESeqOfList::append(*pRDNSequence,(void*)pRDN);
	}
	
	oName.t = T_EXP_Name_rdnSequence;
	oName.u.rdnSequence = pRDNSequence;

	return SUCCESS;
}

Name & Name::operator=(const Name& iName)
{
	mList = iName.getList();
	return *this;
}

bool esya::operator==(const Name & iRHS,const Name & iLHS)
{
	return NameUtils::isEqual(iRHS,iLHS);
}

bool esya::operator!=(const Name & iRHS, const Name & iLHS)
{
	return !(iRHS == iLHS);
}


QString Name::toString(bool iNormalized ) const 
{
      if ( mList.size() == 0 )
           return "";

      int i = mList.size() - 1;
      QString st = mList[i--].toString(iNormalized );
      for (; i >= 0; i--)
      {
           st += ",";
           st += mList[i].toString(iNormalized);
      }

      return st;
}

QStringList Name::toStringList() const 
{
	QStringList sList;
	if ( mList.size() > 0 )
	{
		for (int i = mList.size()-1 ; i >= 0 ; i--)
		{
			sList<<mList[i].toString();
		}
	}

	return sList;
}


QString Name::toTitle(const QString &iFilter) const
{	
	QStringList fList = toStringList().filter(iFilter);
	QStringList retTitleList;

	for (int i=0 ; i <fList.size(); i++)
	{
		QStringList entry = fList[i].split("=");
		if (entry.size()==2)
		{
			retTitleList+=entry[1];			
		}			
	}

	return retTitleList.join(",");
}

QString Name::toTitle() const
{
#define returnTITLE(str)	if (!str.isEmpty())\
							{\
								title.append(str);\
								return title;\
							}

	QString title;

	QString cnSTR = toTitle(NAMEFILTER_CN);
	title.append(cnSTR);

	if ( cnSTR > 0 )
	{	
		title.append(toTitle(NAMEFILTER_SN));
		return title;
	}

	QString ouSTR = toTitle(NAMEFILTER_OU);
	returnTITLE(ouSTR)

	QString oSTR = toTitle(NAMEFILTER_O);
	returnTITLE(oSTR)

	QString seriNoSTR = toTitle(NAMEFILTER_SERIALNUMBER);
	returnTITLE(seriNoSTR)

	title.append(toTitle(""));

	return title;
}

void Name::fromCommonName(const QString & iCN)	
{

	QList<RelativeDistinguishedName> myrdnList;
	RelativeDistinguishedName myRDN;
	QList<AttributeTypeAndValue> myatvList;
	AttributeTypeAndValue myatv;
	AttributeValue myav;

	QByteArray encodedBytes = EASNToStringUtils::stringToCommonName(iCN);

	myav.setValue(encodedBytes);
	myatv.setAttributeType(EXP_id_at_commonName);
	myatv.setAttributeValue(myav);

	myatvList<<myatv;
	myRDN.setList(myatvList);
	myrdnList<<myRDN;

	setList(myrdnList);
}

void Name::addTitle(const QString & iTitle)
{
	QList<AttributeTypeAndValue> myatvList;
	AttributeTypeAndValue myatv;
	AttributeValue myav;
	QByteArray encodedBytes = EASNToStringUtils::stringToTitle(iTitle);

	myav.setValue(encodedBytes);
	myatv.setAttributeType(EXP_id_at_title);
	myatv.setAttributeValue(myav);
	myatvList<<myatv;
	
	RelativeDistinguishedName myRDN;
	myRDN.setList(myatvList);

	mList.append(myRDN);
}

bool Name::isSubNameOf(const Name & iName)const
{
	if  (mList.isEmpty()) // Empty Name is sub Name of all names! 
		return true;

	if (iName.getList().isEmpty() || iName.getList().size()>mList.size())
		return false;

	for (int i =0; i< iName.getList().size(); i++ )
	{
		if (mList[i] != iName.getList()[i])
			return false;
	}
	return true;
}

QString Name::getEmailAttribute()const
{
	if  (mList.isEmpty()) 
		return QString();

	Q_FOREACH(RelativeDistinguishedName rdn, mList)
	{
		QString email = rdn.getEmailAttribute();
		if (!email.trimmed().isEmpty())
			return email.trimmed();
	}

	return QString();
}

QString Name::getTitleAttribute()const
{
	if  (mList.isEmpty()) 
		return QString();

	Q_FOREACH(RelativeDistinguishedName rdn, mList)
	{
		QString title = rdn.getTitleAttribute();
		if (!title.trimmed().isEmpty())
			return title.trimmed();
	}

	return QString();
}



Name::~Name(void)
{
}

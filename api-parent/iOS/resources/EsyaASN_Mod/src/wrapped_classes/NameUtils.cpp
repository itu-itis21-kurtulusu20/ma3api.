#include "NameUtils.h"
#include "EASNToStringUtils.h"

using namespace esya;


const QString NameUtils::SPECIAL_CHARS = "()<>@,;:\\""[]";

/**
* \brief
* Ýki Name deðeri karþýlaþtýrýr.
*
* \param 		const Name & iRHS
* \param 		const Name & iLHS
*
* \return   	bool
* true : Eþit false : Farklý
*/
bool NameUtils::isEqual(const Name & iRHS, const Name & iLHS )
{
	if ( iRHS.getList().size() != iLHS.getList().size() ) return false;
	for (int i= 0 ; i <iRHS.getList().size() ; i++ )
	{
		if  ( !isEqual(iRHS.getList()[i],iLHS.getList()[i]) ) return false;
	}
	return true;
}

/**
* \brief
* Ýki RelativeDistinguishedName deðeri karþýlaþtýrýr.
*
* \param 		const RelativeDistinguishedName & iRHS
* \param 		const RelativeDistinguishedName & iLHS
*
* \return   	bool
* true : Eþit false : Farklý
*/
bool NameUtils::isEqual(const RelativeDistinguishedName & iRHS, const RelativeDistinguishedName & iLHS )
{
	if ( iRHS.getList().size() != iLHS.getList().size() ) return false;
	for (int i= 0 ; i <iRHS.getList().size() ; i++ )
	{
		if  ( !isEqual(iRHS.getList()[i],iLHS.getList()[i]) ) return false;
	}
	return true;
	
}

/**
* \brief
* Ýki AttributeTypeAndValue deðeri karþýlaþtýrýr.
*
* \param 		const AttributeTypeAndValue & iRHS
* \param 		const AttributeTypeAndValue & iLHS
*
* \return   	bool
* true : Eþit false : Farklý
*/
bool NameUtils::isEqual(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS )
{
	if ( iRHS.getAttributeType() != iLHS.getAttributeType()) return false;
	
// 	QString stValueR = EASNToStringUtils::value2String(iRHS.getAttributeType(),iRHS.getAttributeValue().getEncodedBytes(),true);
// 	QString stValueL = EASNToStringUtils::value2String(iLHS.getAttributeType(),iLHS.getAttributeValue().getEncodedBytes(),true);

	QString stValueR = EASNToStringUtils::value2String(iRHS.getAttributeType(),iRHS.getAttributeValue().getEncodedBytes(),false);
	stValueR = EASNToStringUtils::normalizePrintableString(stValueR);
	QString stValueL = EASNToStringUtils::value2String(iLHS.getAttributeType(),iLHS.getAttributeValue().getEncodedBytes(),false);
	stValueL = EASNToStringUtils::normalizePrintableString(stValueL);
	
	return (stValueR == stValueL);
}


bool NameUtils::validateEmailAddress(const QString & iEmailAddress)
{
	return true;
}

bool NameUtils::isSubNameOf(const QString & iName, const QString & iBaseName,const QString & iSeperator)
{
	if (iName == iBaseName)
		return true;
	
	if (iBaseName.startsWith("."))
		return iName.endsWith(iBaseName);

	

	int dotPos = iName.indexOf(iSeperator);

	if (dotPos<0) return (iName == iBaseName);

	QString tail = iName.right(iName.length()-dotPos-1);

	return (tail == iBaseName); 
}

bool NameUtils::isSubURINameOf(const QString & iURIName, const QString & iBaseURIName)
{
	if (iURIName == iBaseURIName)
		return true;

	if (iBaseURIName.startsWith("."))
		return (iURIName.indexOf(iBaseURIName)>=0);



	int dotPos = iURIName.indexOf("//");

	if (dotPos<0) return false;

	QString tail = iURIName.right(iURIName.length()-dotPos-2);

	return (tail.startsWith(iBaseURIName)); 
}
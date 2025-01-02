#include "EASNToStringUtils.h"
#include "asn1berStream.h"
#include "EException.h"
#include "OrtakDil.h"
#include "rtconv.h"

using namespace esya;

EASNToStringUtils * EASNToStringUtils::_mInstance = NULL;
QMutex EASNToStringUtils::msEASNToStringUtilsMutex;

EASNToStringUtils::EASNToStringUtils()
{
	_initHash();
}

EASNToStringUtils::~EASNToStringUtils()
{
	_mInstance = NULL;
}

EASNToStringUtils* EASNToStringUtils::getInstance()
{
	//msEASNToStringUtilsMutex.lock();
	if (!_mInstance)
		_mInstance = new EASNToStringUtils();
	//msEASNToStringUtilsMutex.unlock();
	return _mInstance;
}


void EASNToStringUtils::_initHash()
{	
#define INSERTOID(text,oid)	\
							mOIDHash.insert(oidToString(oid),text);

	char buf[MAXBUFSIZE];

	INSERTOID("cn",EXP_id_at_commonName);
	INSERTOID("c",EXP_id_at_countryName);
    INSERTOID("givenName", EXP_id_at_givenName);
    INSERTOID("l", EXP_id_at_localityName);
    INSERTOID("name", EXP_id_at_name);
    INSERTOID("ou", EXP_id_at_organizationalUnitName);
    INSERTOID("o", EXP_id_at_organizationName);
    INSERTOID("pseudonym", EXP_id_at_pseudonym);
    INSERTOID("serialNumber", EXP_id_at_serialNumber);
    INSERTOID("st", EXP_id_at_stateOrProvinceName);
    INSERTOID("stateOrProvince", EXP_id_at_stateOrProvinceName);
    INSERTOID("sn", EXP_id_at_surname);
    INSERTOID("title", EXP_id_at_title);
    INSERTOID("dc", EXP_id_domainComponent);
    INSERTOID("e", EXP_id_emailAddress);


	/*Extended Key Usages*/
	INSERTOID(DIL_EKU_ISTEMCI_YETKILENDIRMESI, IMP_id_kp_clientAuth);
	INSERTOID(DIL_EKU_EPOSTA_KORUMASI, IMP_id_kp_emailProtection);
	INSERTOID(DIL_EKU_AKILLIKART_GIRIS, IMP_id_ms_smartCardLogon);

}


QPair<QString,QString> EASNToStringUtils::tv2String(const ASN1OBJID &iType,const QByteArray & iValue,bool iNormalized)
{
	QString stValue = value2String(iType,iValue);
	if (iNormalized)
		stValue = normalizePrintableString(stValue);
	QString stType  = type2String(iType);
	
	return qMakePair(stType,stValue);
}

QString EASNToStringUtils::oidToString(const ASN1OBJID &iType)
{
	QString st = "";
	for (int i= 0 ; i<iType.numids;i++)
	{	
		st += QString("%1").arg(iType.subid[i]) ;
		if (i<iType.numids-1)
			st += QString(".");
	}
	return st;
}

ASN1TObjId EASNToStringUtils::strToOid(const QString &iOidStr)
{
	ASN1TObjId oid((const char*)iOidStr.toLocal8Bit().data());
	return oid;
}

QString EASNToStringUtils::type2String(const ASN1OBJID &iType)
{
	char buf[100000];
	ASN1TObjId oid = (ASN1TObjId)iType;
	//rtOIDToString(iType.numids,(OSUINT32*)iType.subid,buf,100000*sizeof(char));
	//QString oidSTR(buf);

	QString oidSTR = oidToString(oid);
	

	if ( ! OIDHASH.contains(oidSTR) )
	{
		return QString("%1:%2").arg(UNKNOWN_OID).arg(oidSTR);
	}
	return OIDHASH[oidSTR];
}



QString EASNToStringUtils::value2String(const ASN1OBJID &iType, const QByteArray & iValue, bool iNormalized)
{
	try
	{
		if ( iType == EXP_id_at_commonName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520CommonName,ASN1C_EXP_X520CommonName>(iValue);
			//return commonName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_countryName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520CountrySerialUTF8liUcubeName,ASN1C_EXP_X520CountrySerialUTF8liUcubeName>(iValue);
			//return countryName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_givenName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520name,ASN1C_EXP_X520name>(iValue);
			//return givenName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_localityName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520LocalityName,ASN1C_EXP_X520LocalityName>(iValue);
			//return localityName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_name )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520name,ASN1C_EXP_X520name>(iValue);
			//return name2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_organizationalUnitName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520OrganizationalUnitName,ASN1C_EXP_X520OrganizationalUnitName>(iValue);
			//return organizationalUnitName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_organizationName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520OrganizationName,ASN1C_EXP_X520OrganizationName>(iValue);		
			//return organizationName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_pseudonym )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520Pseudonym,ASN1C_EXP_X520Pseudonym>(iValue);		
			//return pseudonym2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_serialNumber )
		{
			return charPTRAttributeToString<ASN1T_EXP_X520SerialNumber,ASN1C_EXP_X520SerialNumber>(iValue);
			//return serialNumber2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_stateOrProvinceName )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520StateOrProvinceName,ASN1C_EXP_X520StateOrProvinceName>(iValue);		
			//return stateOrProvinceName2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_surname )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520name,ASN1C_EXP_X520name>(iValue);
			//return surname2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_at_title )
		{
			return genericStringAttributeToString<ASN1T_EXP_X520Title,ASN1C_EXP_X520Title>(iValue);
			//return title2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_domainComponent )
		{
			return charPTRAttributeToString<ASN1T_EXP_DomainComponent,ASN1C_EXP_DomainComponent>(iValue);
			//return domainComponent2String(iValue,iNormalized);
		}
		if ( iType == EXP_id_emailAddress )
		{
			return charPTRAttributeToString<ASN1T_EXP_EmailAddress,ASN1C_EXP_EmailAddress>(iValue);
			//return emailAddress2String(iValue,iNormalized);
		}
		else 
			return genericStringAttributeToString<ASN1T_EXP_DirectoryString,ASN1C_EXP_DirectoryString>(iValue);
			
			//return unknownToString(iValue,iNormalized);//Bilinmeyen Tip
	}
	catch(EException& exc)
	{
		return genericStringAttributeToString<ASN1T_EXP_DirectoryString,ASN1C_EXP_DirectoryString>(iValue);
	}

}


// QString EASNToStringUtils::value2String(const ASN1OBJID &iType, const QByteArray & iValue, bool iNormalized)
// {
// 	try
// 	{
// 		if ( iType == EXP_id_at_commonName )
// 		{
// 			return commonName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_countryName )
// 		{
// 			return countryName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_givenName )
// 		{
// 			return givenName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_localityName )
// 		{
// 			return localityName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_name )
// 		{
// 			return name2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_organizationalUnitName )
// 		{
// 			return organizationalUnitName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_organizationName )
// 		{
// 			return organizationName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_pseudonym )
// 		{
// 			return pseudonym2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_serialNumber )
// 		{
// 			return serialNumber2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_stateOrProvinceName )
// 		{
// 			return stateOrProvinceName2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_surname )
// 		{
// 			return surname2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_at_title )
// 		{
// 			return title2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_domainComponent )
// 		{
// 			return domainComponent2String(iValue,iNormalized);
// 		}
// 		if ( iType == EXP_id_emailAddress )
// 		{
// 			return emailAddress2String(iValue,iNormalized);
// 		}
// 		else 
// 			return unknownToString(iValue,iNormalized);//Bilinmeyen Tip
// 	}
// 	catch(EException& exc)
// 	{
// 		return unknownToString(iValue,iNormalized);
// 	}
// 
// }

QString EASNToStringUtils::unknownToString(const QByteArray & iValue,bool iNormalized)
{
	char buf[MAXBUFSIZE];

	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_DirectoryString unknown;
	ASN1C_EXP_DirectoryString cUnknown(unknown);
	int stat = cUnknown.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		return byteArrayToStr(iValue);		
	}

	switch (unknown.t)
	{
	case T_EXP_X520CommonName_bmpString:
		{
			rtBMPToCString(unknown.u.bmpString,buf,unknown.u.bmpString->nchars);
			return QString(buf);
		}
	case T_EXP_X520CommonName_printableString:
		{
			return (iNormalized ? normalizePrintableString(QString(unknown.u.printableString)):QString(unknown.u.printableString)  );
		}
	case T_EXP_X520CommonName_teletexString:
		{
			return QString(unknown.u.teletexString);
		}
	case T_EXP_X520CommonName_universalString:
		{
			rtUCSToCString(unknown.u.universalString,buf,unknown.u.universalString->nchars);
			return QString(buf);
		}
	case T_EXP_X520CommonName_utf8String:
		{
			QString st = QString::fromUtf8((const char*)unknown.u.utf8String);
			return (iNormalized ? normalizePrintableString(st):st);
		}
	}
	return QString();
}

QString EASNToStringUtils::commonName2String(const QByteArray & iValue,bool iNormalized)
{
	char buf[MAXBUFSIZE];

	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520CommonName commonName;
	ASN1C_EXP_X520CommonName cCommonName(commonName);
	int stat = cCommonName.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("CommonName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (commonName.t)
	{
		case T_EXP_X520CommonName_bmpString:
			{
				rtBMPToCString(commonName.u.bmpString,buf,commonName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520CommonName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(commonName.u.printableString)):QString(commonName.u.printableString)  );
			}
		case T_EXP_X520CommonName_teletexString:
			{
				return QString(commonName.u.teletexString);
			}
		case T_EXP_X520CommonName_universalString:
			{
				rtUCSToCString(commonName.u.universalString,buf,commonName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520CommonName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)commonName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::countryName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520CountrySerialUTF8liUcubeName countryName;
	ASN1C_EXP_X520CountrySerialUTF8liUcubeName cCountryName(countryName);
	int stat = cCountryName.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("CountryName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}

	switch (countryName.t)
	{
		case T_EXP_X520CountrySerialUTF8liUcubeName_bmpString:
			{
				rtBMPToCString(countryName.u.bmpString,buf,countryName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520CountrySerialUTF8liUcubeName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(countryName.u.printableString)):QString(countryName.u.printableString)  );
			}
		case T_EXP_X520CountrySerialUTF8liUcubeName_teletexString:
			{
				return QString(countryName.u.teletexString);
			}
		case T_EXP_X520CountrySerialUTF8liUcubeName_universalString:
			{
				rtUCSToCString(countryName.u.universalString,buf,countryName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520CountrySerialUTF8liUcubeName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)countryName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::givenName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520name givenName;
	ASN1C_EXP_X520name cGivenName(givenName);
	int stat = cGivenName.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("GivenName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (givenName.t)
	{
		case T_EXP_X520name_bmpString:
			{
				rtBMPToCString(givenName.u.bmpString,buf,givenName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(givenName.u.printableString)):QString(givenName.u.printableString)  );
			}
		case T_EXP_X520name_teletexString:
			{
				return QString(givenName.u.teletexString);
			}
		case T_EXP_X520name_universalString:
			{
				rtUCSToCString(givenName.u.universalString,buf,givenName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_utf8String:
			{
				QString st = QString::fromUtf8((const char*)givenName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}


	return QString();
}

QString EASNToStringUtils::localityName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520LocalityName localityName;
	ASN1C_EXP_X520LocalityName cLocalityName(localityName);
	int stat = cLocalityName.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("LocalityName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}

	switch (localityName.t)
	{
		case T_EXP_X520LocalityName_bmpString:
			{
				rtBMPToCString(localityName.u.bmpString,buf,localityName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520LocalityName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(localityName.u.printableString)):QString(localityName.u.printableString)  );
			}
		case T_EXP_X520LocalityName_teletexString:
			{
				return QString(localityName.u.teletexString);
			}
		case T_EXP_X520LocalityName_universalString:
			{
				rtUCSToCString(localityName.u.universalString,buf,localityName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520LocalityName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)localityName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::name2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520name givenName;
	ASN1C_EXP_X520name cGivenName(givenName);
	int stat = cGivenName.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("Name Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (givenName.t)
	{
		case T_EXP_X520name_bmpString:
			{
				rtBMPToCString(givenName.u.bmpString,buf,givenName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(givenName.u.printableString)):QString(givenName.u.printableString)  );
			}
		case T_EXP_X520name_teletexString:
			{
				return QString(givenName.u.teletexString);
			}
		case T_EXP_X520name_universalString:
			{
				rtUCSToCString(givenName.u.universalString,buf,givenName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_utf8String:
			{
				QString st = QString::fromUtf8((const char*)givenName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}

	return QString();
}

QString EASNToStringUtils::organizationalUnitName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520OrganizationalUnitName ouName;
	ASN1C_EXP_X520OrganizationalUnitName cOUName(ouName);
	int stat = cOUName.DecodeFrom(decBuf);

	if ( stat != ASN_OK )
	{
		throw EException(QString("OUName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (ouName.t)
	{
		case T_EXP_X520OrganizationalUnitName_bmpString:
			{
				rtBMPToCString(ouName.u.bmpString,buf,ouName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520OrganizationalUnitName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(ouName.u.printableString)):QString(ouName.u.printableString)  );
			}
		case T_EXP_X520OrganizationalUnitName_teletexString:
			{
				return QString(ouName.u.teletexString);
			}
		case T_EXP_X520OrganizationalUnitName_universalString:
			{
				rtUCSToCString(ouName.u.universalString,buf,ouName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520OrganizationalUnitName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)ouName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::organizationName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520OrganizationName oName;
	ASN1C_EXP_X520OrganizationName cOName(oName);
	int stat = cOName.DecodeFrom(decBuf);

	if ( stat != ASN_OK )
	{
		throw EException(QString("OName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (oName.t)
	{
	case T_EXP_X520OrganizationName_bmpString:
			{
				rtBMPToCString(oName.u.bmpString,buf,oName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520OrganizationName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(oName.u.printableString)):QString(oName.u.printableString)  );
			}
		case T_EXP_X520OrganizationName_teletexString:
			{
				return QString(oName.u.teletexString);
			}
		case T_EXP_X520OrganizationName_universalString:
			{
				rtUCSToCString(oName.u.universalString,buf,oName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520OrganizationName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)oName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}

	return QString();
}

QString EASNToStringUtils::pseudonym2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520Pseudonym psdnym;
	ASN1C_EXP_X520Pseudonym cPsdnym(psdnym);
	int stat = cPsdnym.DecodeFrom(decBuf);

	if ( stat != ASN_OK )
	{
		throw EException(QString("Pseudonym Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (psdnym.t)
	{
		case T_EXP_X520Pseudonym_bmpString:
			{
				rtBMPToCString(psdnym.u.bmpString,buf,psdnym.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520Pseudonym_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(psdnym.u.printableString)):QString(psdnym.u.printableString)  );
			}
		case T_EXP_X520Pseudonym_teletexString:
			{
				return QString(psdnym.u.teletexString);
			}
		case T_EXP_X520Pseudonym_universalString:
			{
				rtUCSToCString(psdnym.u.universalString,buf,psdnym.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520Pseudonym_utf8String:
			{
				QString st = QString::fromUtf8((const char*)psdnym.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::serialNumber2String(const QByteArray & iValue ,bool iNormalized)
{
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520SerialNumber sn;
	ASN1C_EXP_X520SerialNumber cSN(sn);
	int stat = cSN.DecodeFrom(decBuf);

	if ( stat != ASN_OK )
	{
		throw EException(QString("SerialNumber Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	return QString(sn);
}

QString EASNToStringUtils::stateOrProvinceName2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520Pseudonym sopName;
	ASN1C_EXP_X520Pseudonym cSOPName(sopName);
	int stat = cSOPName.DecodeFrom(decBuf);

	if ( stat != ASN_OK )
	{
		throw EException(QString("StateOrProvinceName Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (sopName.t)
	{
		case T_EXP_X520StateOrProvinceName_bmpString:
			{
				rtBMPToCString(sopName.u.bmpString,buf,sopName.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520StateOrProvinceName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(sopName.u.printableString)):QString(sopName.u.printableString)  );
			}
		case T_EXP_X520StateOrProvinceName_teletexString:
			{
				return QString(sopName.u.teletexString);
			}
		case T_EXP_X520StateOrProvinceName_universalString:
			{
				rtUCSToCString(sopName.u.universalString,buf,sopName.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520StateOrProvinceName_utf8String:
			{
				QString st = QString::fromUtf8((const char*)sopName.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::surname2String(const QByteArray &iValue , bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520name surname;
	ASN1C_EXP_X520name cSurname(surname);
	int stat = cSurname.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("Surname Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	switch (surname.t)
	{
		case T_EXP_X520name_bmpString:
			{
				rtBMPToCString(surname.u.bmpString,buf,surname.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(surname.u.printableString)):QString(surname.u.printableString)  );
			}
		case T_EXP_X520name_teletexString:
			{
				return QString(surname.u.teletexString);
			}
		case T_EXP_X520name_universalString:
			{
				rtUCSToCString(surname.u.universalString,buf,surname.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520name_utf8String:
			{
				QString st = QString::fromUtf8((const char*)surname.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}

	return QString();
}



QString EASNToStringUtils::title2String(const QByteArray &iValue , bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_X520Title title;
	ASN1C_EXP_X520Title cTitle(title);
	int stat = cTitle.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("Title Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	switch (title.t)
	{
		case T_EXP_X520Title_bmpString:
			{
				rtBMPToCString(title.u.bmpString,buf,title.u.bmpString->nchars);
				return QString(buf);
			}
		case T_EXP_X520StateOrProvinceName_printableString:
			{
				return (iNormalized ? normalizePrintableString(QString(title.u.printableString)):QString(title.u.printableString)  );
			}
		case T_EXP_X520Title_teletexString:
			{
				return QString(title.u.teletexString);
			}
		case T_EXP_X520Title_universalString:
			{
				rtUCSToCString(title.u.universalString,buf,title.u.universalString->nchars);
				return QString(buf);
			}
		case T_EXP_X520Title_utf8String:
			{
				QString st = QString::fromUtf8((const char*)title.u.utf8String);
				return (iNormalized ? normalizePrintableString(st):st);
			}
	}
	return QString();
}

QString EASNToStringUtils::domainComponent2String(const QByteArray & iValue, bool iNormalized)
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_DomainComponent dc;
	ASN1C_EXP_DomainComponent cDC(dc);
	int stat = cDC.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("DC Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}
	
	return QString((const char*)dc);
}

QString EASNToStringUtils::emailAddress2String(const QByteArray & iValue, bool iNormalized )
{
	char buf[MAXBUFSIZE];
	ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
	ASN1T_EXP_EmailAddress email;
	ASN1C_EXP_EmailAddress cEmail(email);
	int stat = cEmail.DecodeFrom(decBuf);
	if ( stat != ASN_OK )
	{
		throw EException(QString("Email Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}

	return QString((const char*)email);
}

void EASNToStringUtils::clearBIDI(QString& ioText)
{
	int i= 0;
	while(i<ioText.size())
	{
		if (ioText.at(i).direction()== QChar::DirB) // ???
			ioText.remove(i,1);
		else i++;
	}
}

QString EASNToStringUtils::normalizePrintableString(const QString& iPS)
{
	return iPS.normalized(QString::NormalizationForm_KC).simplified().toLower(); 

// 	const ushort * uPS = iPS.utf16();
// 	QString st(iPS.simplified().toLower());
// 
// 	QStringList stList = st.split(" ");
// 	st.clear();
// 	for (int i = 0 ; i< stList.size();i++)
// 	{
// 		stList[i][0] = stList[i][0].toUpper();
// 		st += QString("%1 ").arg(stList[i]);
// 	}
// 
// 	//todo : clearBIDI(st);
// 	
// 	return st.simplified();
}

QString EASNToStringUtils::byteArrayToStr(const QByteArray &iInputByteArray, bool iBoslukKoy )
{
	QString blokStr = "",result = "";
	for (int k=0;k<iInputByteArray.size();k++)
	{
		char num  = iInputByteArray[k];
		blokStr = QString("0%1").arg(QString().setNum(num,16)).right(2);
		if (iBoslukKoy && k > 0 ) result += " ";
		result += QString("%1").arg(blokStr);
	}
	return result;
}

QString EASNToStringUtils::hex2Str(const QString &iHex)
	{
		QString lRetStr="";
		QString lHexStr = iHex;
		lHexStr = (lHexStr.toUpper()).remove("0X");
		while (lHexStr.size()>0)
		{
			QString lAraStr=lHexStr.right(2);
			if (lAraStr == "0")
			{
				lAraStr = "00";
			}
			lRetStr=lAraStr+" "+lRetStr;
			lHexStr.chop(2);
		}		
		return lRetStr;
	}


QByteArray EASNToStringUtils::stringToCommonName(const QString & iValue)
{
	char buf[MAXBUFSIZE];

	ASN1BEREncodeBuffer encBuf;
	ASN1T_EXP_X520CommonName cn;
	ASN1C_EXP_X520CommonName cCommonName(cn);

	std::string st = iValue.toStdString();
	
	cn.t = T_EXP_X520CommonName_printableString; // default printable string yapýyor
	cn.u.utf8String =(OSUTF8CHAR*)st.c_str();

	int stat = cCommonName.EncodeTo(encBuf);
	if ( stat < 0)
	{
		throw EException(QString("CommonName Encode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}

	return QByteArray((char*) encBuf.getMsgPtr(),stat);
}

QByteArray EASNToStringUtils::stringToTitle(const QString & iValue)
{
	char buf[MAXBUFSIZE];

	ASN1BEREncodeBuffer encBuf;
	ASN1T_EXP_X520Title title;
	ASN1C_EXP_X520Title cTitle(title);

	std::string st = iValue.toStdString();

	title.t = T_EXP_X520Title_printableString; // default printable string yapýyor
	title.u.utf8String =(OSUTF8CHAR*)st.c_str();

	int stat = cTitle.EncodeTo(encBuf);
	if ( stat < 0)
	{
		throw EException(QString("Title Encode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
	}

	return QByteArray((char*) encBuf.getMsgPtr(),stat);
}





QString EASNToStringUtils::genericStringToString(ASNGenericStringType iType, const ASNGenericString & iString)
{
	char buf[MAXBUFSIZE];


	switch (iType)
	{
	case BMPString:
		{
			rtBMPToCString(iString.bmpString,buf,iString.bmpString->nchars);
			return QString(buf);
		}
	case PrintableString:
		{
			return QString(iString.printableString);
		}
	case TeletextString:
		{
			return QString(iString.teletexString);
		}
	case UniversalString:
		{
			rtUCSToCString(iString.universalString,buf,iString.universalString->nchars);
			return QString(buf);
		}
	case UTF8String:
		{
			QString st = QString::fromUtf8((const char*)iString.utf8String);
			return st;
		}
	}

	return "";
}
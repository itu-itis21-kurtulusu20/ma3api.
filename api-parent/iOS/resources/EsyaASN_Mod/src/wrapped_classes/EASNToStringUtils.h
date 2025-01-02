#ifndef __EASNTOSTRINGUTILS__
#define __EASNTOSTRINGUTILS__

#include "cms.h"
#include "AttributeValue.h"
#include <QPair>
#include <QHash>
#include <QMutex>

#define MAXBUFSIZE 100000
#define UNKNOWN_OID "OID"

namespace esya
{


#define OIDHASH EASNToStringUtils::getInstance()->mOIDHash


	typedef enum {	UTF8String		= T_EXP_X520CommonName_utf8String , 
					PrintableString = T_EXP_X520CommonName_printableString ,   
					TeletextString	= T_EXP_X520CommonName_teletexString,
					UniversalString	= T_EXP_X520CommonName_universalString ,
					BMPString		= T_EXP_X520CommonName_bmpString } ASNGenericStringType ;

	typedef    union {
		/* t = 1 */
		const OSUTF8CHAR* utf8String;
		/* t = 2 */
		const char* printableString;
		/* t = 3 */
		const char* teletexString;
		/* t = 4 */
		ASN1TUniversalString *universalString;
		/* t = 5 */
		ASN1TBMPString *bmpString;
	} ASNGenericString;

	class Q_DECL_EXPORT EASNToStringUtils
	{
		static QMutex msEASNToStringUtilsMutex;
		QHash<QString,QString> mOIDHash;

		void _initHash();

		EASNToStringUtils();
		~EASNToStringUtils();

	public:
		static QString oidToString(const ASN1OBJID &iType);
		static ASN1TObjId strToOid(const QString &iOidStr);

		static QPair<QString,QString> tv2String(const ASN1OBJID &iType,const QByteArray & iValue,bool iNormalized = false);
		static QString type2String(const ASN1OBJID &iType);
		static QString value2String(const  ASN1OBJID &iType, const QByteArray & iValue,bool iNormalized = false);

		static QString commonName2String(const QByteArray & ,bool iNormalized);
		static QString countryName2String(const QByteArray & ,bool iNormalized);
		static QString givenName2String(const QByteArray & ,bool iNormalized);
		static QString localityName2String(const QByteArray & ,bool iNormalized);
		static QString name2String(const QByteArray & , bool iNormalized);
		static QString organizationalUnitName2String(const QByteArray & ,bool iNormalized);
		static QString organizationName2String(const QByteArray & ,bool iNormalized);
		static QString pseudonym2String(const QByteArray & ,bool iNormalized);
		static QString serialNumber2String(const QByteArray & ,bool iNormalized);
		static QString stateOrProvinceName2String(const QByteArray & ,bool iNormalized);
		static QString surname2String(const QByteArray & ,bool iNormalized);
		static QString title2String(const QByteArray & ,bool iNormalized);
		static QString domainComponent2String(const QByteArray & ,bool iNormalized);
		static QString emailAddress2String(const QByteArray & ,bool iNormalized);
		static QString unknownToString(const QByteArray & ,bool iNormalized);

		static void clearBIDI(QString& ioText);
		
		static QString normalizePrintableString(const QString& iPS);

		static QString genericStringToString(ASNGenericStringType iType, const ASNGenericString & iString);

		static QString byteArrayToStr(const QByteArray &iInputByteArray, bool iBoslukKoy = true);
		static QString hex2Str(const QString & iHexValue);

		static QByteArray stringToCommonName(const QString & iValue);
		static QByteArray stringToTitle(const QString & iValue);


		template<class T, class C>
		static QString charPTRAttributeToString(const QByteArray& iValue)
		{
			ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
			T attr;
			C cAttr(attr);
			int stat = cAttr.DecodeFrom(decBuf);
			if ( stat != ASN_OK )
			{
				throw EException(QString("Attribute Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
			}
			return QString((const char*)attr);
		}		

		template<class T, class C>
		static QString genericStringAttributeToString(const QByteArray& iValue)
		{
			ASN1BERDecodeBuffer decBuf((OSOCTET*)iValue.data(),iValue.size());
			T attr;
			C cAttr(attr);
			int stat = cAttr.DecodeFrom(decBuf);
			if ( stat != ASN_OK )
			{
				throw EException(QString("Attribute Decode Edilemedi Hata : %1").arg(stat),__FILE__,__LINE__);
			}
			char buf[MAXBUFSIZE];

			ASNGenericStringType sType = (ASNGenericStringType)attr.t;

			switch (sType)
			{
			case BMPString:
				{
					rtBMPToCString(attr.u.bmpString,buf,attr.u.bmpString->nchars);
					return QString(buf);
				}
			case PrintableString:
				{
					return QString(attr.u.printableString);
				}
			case TeletextString:
				{
					return QString(attr.u.teletexString);
				}
			case UniversalString:
				{
					rtUCSToCString(attr.u.universalString,buf,attr.u.universalString->nchars);
					return QString(buf);
				}
			case UTF8String:
				{
					QString st = QString::fromUtf8((const char*)attr.u.utf8String);
					return st;
				}
			}

			return "";
		}


	public:

		static EASNToStringUtils* getInstance();
	
	private:
		static EASNToStringUtils *_mInstance;

	};



}

#endif


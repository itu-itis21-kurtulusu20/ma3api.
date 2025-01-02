#ifndef __MYASNDEFS__
#define __MYASNDEFS__


#include <QVector>
#include <QString>
#include <QByteArray>
#include "myerrors.h"
//#include "OSCStream.h"
#include "algorithms.h"
#include "cms.h"
#include "ortak.h"


#define TAG_PRIMITIVE_OCTETSTR 0x00000004
#define TAG_CONSTRUCTED_OCTETSTR 0x20000004
#define TAG_ENDOF_OCTETSTR 0x00

#define TAG_A0_READ 0xA0000000
#define TAG_80_READ 0x80000000



#define TAG_A0 0xA0
#define TAG_A1 0xA1

#define TAG_30 0x30
#define TAG_24 0x24
#define TAG_04 0x04

#define TAG_SEQUENCE 0x20000010

#define INDEFLEN ASN_K_INDEFLEN

#define TAG_SET_OF 0x20000011

#define OCTETSTR_BLOCK_SIZE 524288 

/************************************************************************/
/* SIGNED DATA TAG SABITLERI                                            */
/* Bu sabitler okunurken ve yazýlýrken baþka baþka deðerlere sahip      */
/************************************************************************/
#define TAG_CONTENTINFO TAG_SEQUENCE
#define TAG_CONTENT TAG_A0_READ
#define TAG_SIGNEDDATA  TAG_SEQUENCE
#define TAG_ENCAPCONTENTINFO  TAG_SEQUENCE
#define TAG_ENCAPCONTENT TAG_A0_READ

#define TAG_CONTENTINFO_WRITE TAG_30
#define TAG_CONTENT_WRITE TAG_A0
#define TAG_SIGNEDDATA_WRITE  TAG_30
#define TAG_ENCAPCONTENTINFO_WRITE  TAG_30
#define TAG_ENCAPCONTENT_WRITE TAG_A0



/************************************************************************/
/* ENVELOPED DATA TAG SABITLERI                                         */
/* Bu sabitler okunurken ve yazýlýrken baþka baþka deðerlere sahip      */
/************************************************************************/

#define TAG_ORIGINATORINFO TAG_SEQUENCE
#define TAG_ENCRYPTEDCONTENTINFO  TAG_SEQUENCE
#define TAG_ENVELOPEDEDDATA  TAG_SEQUENCE
#define TAG_UNPROTECTEDATTRS TAG_SEQUENCE
#define TAG_ENCRYPTEDCONTENT TAG_A0_READ

#define TAG_ENVELOPEDEDDATA_WRITE  TAG_30
#define TAG_ENCRYPTEDCONTENTINFO_WRITE  TAG_30
#define TAG_ENCAPCONTENT_WRITE TAG_A0

namespace esya
{

	class ASNUtils 
	{
		public :
		
		static ASN1OCTET _getLenOctetSmall(int len)
		{
			if (len >0xFF) len = len%0xFF;
			return len;
		};

		
		static QVector<ASN1OCTET> _getLenOctet(ASN1UINT aLen)
		{
			QString stLen,temp;
			QByteArray qbLen,qb ;
			QVector<ASN1OCTET> octets;
		
			int	len =0, a =aLen,lensize= 0x80; 

			if (aLen == ASN_K_INDEFLEN)
			{
				octets.append(0x80);
			}
			else if (aLen < 0x80)
			{
				octets.append( _getLenOctetSmall(aLen));
			}
			else 
			{
				while (a)
				{
					len = a %0x100;
					octets.insert(0,len);
					a   = a / 0x100;
					lensize++;
				}	
				octets.insert(0,lensize);
			}
			return octets;
		};

		static QByteArray getLenBytes(ASN1UINT aLen)
		{
			QVector<ASN1OCTET> octets = _getLenOctet(aLen);
			QByteArray lenBytes;
			for (int i = 0 ; i<octets.size();i++)
			{
				lenBytes.append(octets.at(i));
			}
			return lenBytes;
		};

		static QByteArray getTagLenBytes(ASN1TAG aTag, ASN1UINT aLen)
		{
			QByteArray tlBytes;

			unsigned char b = ( aTag & 0x00FF ) | ( (aTag>>8) & 0x00FF )|( (aTag>>16) & 0x00FF )|( (aTag>>24) & 0x00FF );

			tlBytes.append(b);
			tlBytes.append(getLenBytes(aLen));

			return tlBytes;
		};

		static QByteArray decodeOctetString(const QByteArray& iOctets)
		{	
			if (iOctets.size()== 0) return QByteArray();

			ASN1BERDecodeBuffer decBuf((OSOCTET*)iOctets.data(),iOctets.size());
			ASN1T_ALGOS_KEA_Parms_Id params;
			ASN1C_ALGOS_KEA_Parms_Id cParams(params);

			int stat = cParams.DecodeFrom(decBuf);
			if (stat != ASN_OK)
				QByteArray();

			return QByteArray((const char*)params.data,params.numocts);
		};

		static QByteArray encodeOctetString(const QByteArray& iOctets)	
		{
			ASN1BEREncodeBuffer encBuf;
			ASN1T_ALGOS_KEA_Parms_Id params;
			ASN1C_ALGOS_KEA_Parms_Id cParams(params);

			params.data	   = (OSOCTET*)myStrDup(iOctets.data(),iOctets.size());
			params.numocts = iOctets.size();
			
			int stat = cParams.EncodeTo(encBuf);
			
			QByteArray asnBytes((const char*)encBuf.GetMsgPtr(),stat);

			delete[](params.data);
			
			return asnBytes;
		};

		static QByteArray encodeOID(const ASN1TObjId& iOID)	
		{
			ASN1BEREncodeBuffer encBuf;
			ASN1T_CMS_ContentType oid = iOID;
			ASN1C_CMS_ContentType cOID(oid);

			int stat = cOID.EncodeTo(encBuf);

			QByteArray asnBytes((const char*)encBuf.GetMsgPtr(),stat);

			return asnBytes;
		};

	};
}
#endif


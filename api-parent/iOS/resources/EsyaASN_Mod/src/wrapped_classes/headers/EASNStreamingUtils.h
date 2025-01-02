#ifndef __EASNSTREAMINGUTILS__
#define __EASNSTREAMINGUTILS__

#include "ASN1BERDecodeStream.h"
#include "OSRTFileInputStream.h"
#include "OSRTMemoryInputStream.h"
#include "OutputStream.h"

#include "rtPrintStream.h"
#include "myasndefs.h"
#include "BlokIsleyici.h"


namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Stream iþlemleri ile ilgili metodlarý içeren bir utility sýnýfý
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EASNFileInputStream
	{
	private:
		FILE* mFP;
		OSRTFileInputStream *prtIN;
		OSRTMemoryInputStream *prtINmemory;
		ASN1BERDecodeStream * _pIN;
	public:
		EASNFileInputStream(const QByteArray& iData);		
		EASNFileInputStream(FILE*);		
		EASNFileInputStream(OSRTFileInputStream*);
		~EASNFileInputStream();

		ASN1BERDecodeStream * pIN() { return _pIN ;};

		void peekTagLen( ASN1TAG &oTag, int &oLen);
		void decodeTagLen(ASN1TAG *oTag, int *oLen);
		void skipTag(ASN1TAG iTag);
		void decodeUInt(ASN1UINT * oIntVal);
		void decodeObjID(ASN1TObjId * oObjID);
		void decodeOpenType(QByteArray * oBytes);
		void decodePrimitiveOctetString(ASN1TagType tagging ,QByteArray* oBytes );
		void decodeEoc();

		/* STREAMS */
		void processConstructedIndefLenOctetString( BlokIsleyici& iBP );
		void processConstructedDefLenOctetString(BlokIsleyici& iBP );
		void processDefLenOctetString(BlokIsleyici& iBP );
		void processInDefLenOctetString(BlokIsleyici& iBP );

		int byteIndex()const;
		int close();

		static FILE* unicodeFILEPtrOlustur(const QString& iFileName, const QString & iOpenMode = "rb");

	};

	class Q_DECL_EXPORT EASNStreamingUtils
	{
	public:

		static void CHECK_BUFFER(OSRTOutputStream *pOUT);


		static EASNFileInputStream *	createFileInputStream(FILE*  aFP);
		static EASNFileInputStream *	createFileInputStream(const QString& aFileName);
		static void						releaseInputStream(OSRTInputStream * pIN);

		static OutputStream*			createFileOutputStream(const  QString & aFileName);
		static void						releaseOutputStream(OutputStream & pOUT);

		/************************************************************************/
		/*								READ                                    */
		/************************************************************************/

	

		/************************************************************************/
		/*								WRITE                                   */
		/************************************************************************/

		static void write(OutputStream& pOUT,const ASN1OCTET* data, int len);
		static void write(OutputStream& pOUT, const QByteArray &data);
		static void writeTagLen(OutputStream& pOUT, ASN1TAG aTag, ASN1INT aLen);
		static void writeObjID(OutputStream& pOUT, const ASN1OBJID & iObjId );

		static void writePrimitiveOctetString(OutputStream& pOUT, const QByteArray & iOctets);
		static void writeOctetString(OutputStream& pOUT, const QByteArray & iOctets);
		static void writeOctetString(OutputStream& pOUT, QIODevice & iIN, ASN1TagType tagging);

	public:

	};

}

#endif


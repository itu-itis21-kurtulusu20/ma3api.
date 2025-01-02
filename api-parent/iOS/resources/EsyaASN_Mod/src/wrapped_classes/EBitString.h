#ifndef __EBITSTRING__
#define __EBITSTRING__

#include "ortak.h"
#include "asn1CppTypes.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 Bit String wrapper sýnýfý.
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT EBitString 
	{
		QByteArray	mData;
		int 		mNumBits;

		static QBitArray _toBitArray(ASN1OCTET iOctet, int iNumBits);
		static ASN1OCTET _toOctet(const QBitArray& bits, int iIndex);

	public :
		EBitString();
		EBitString(const ASN1OCTET iOctets[] ,int iNumBits);
		EBitString(const ASN1TDynBitStr&);		
		EBitString(const EBitString&);	
		EBitString(const QByteArray&, int  );
		EBitString(const QByteArray&);

		EBitString& operator=(const EBitString&);

		Q_DECL_EXPORT friend bool operator==(const EBitString& , const EBitString&);
		Q_DECL_EXPORT friend bool operator!=(const EBitString& , const EBitString&);

		int constructObject(const QByteArray & iBits);

		int copyToASNObject(ASN1TDynBitStr & oBitStr)const;
		int copyFromASNObject(const ASN1TDynBitStr & oBitStr);

		ASN1TDynBitStr* getASNCopy()const;

		static ASN1TDynBitStr* getASNCopyOf( const EBitString&);

		static void freeASNObject(ASN1TDynBitStr * oBitStr);
		static void freeASNObject(ASN1TDynBitStr & oBitStr);

		virtual QByteArray getEncodedBytes() const;
		QByteArray	getData()const;
		int			getNumBits()const;

		void setData(const QByteArray& );
		void setNumBits(int );

		QString		toString()const;
		QBitArray	toBitArray()const;

	};
}

#endif


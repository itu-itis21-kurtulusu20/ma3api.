
#ifndef __REASONFLAGS__
#define __REASONFLAGS__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "Implicit.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT  ReasonFlags  : public EASNWrapperTemplate<ASN1T_IMP_ReasonFlags,ASN1C_IMP_ReasonFlags>
	{
	public:
		static const int DATASIZE = 2; 
	protected: 
	   int mNumBits;
	   OSOCTET mData[DATASIZE];

	public:

		ReasonFlags(const ReasonFlags &);
		ReasonFlags(const ASN1T_IMP_ReasonFlags & );
		ReasonFlags(const QByteArray & );
		ReasonFlags(const int iNumBits , const OSUINT32 * &iData);
		ReasonFlags(void);
		

		ReasonFlags & operator=(const ReasonFlags&);
		friend bool operator==(const ReasonFlags& iRHS, const ReasonFlags& iLHS);
		friend bool operator!=(const ReasonFlags& iRHS, const ReasonFlags& iLHS);


		int copyFromASNObject(const ASN1T_IMP_ReasonFlags& iRF);
		int copyToASNObject(ASN1T_IMP_ReasonFlags & oRF) const;
		void freeASNObject(ASN1T_IMP_ReasonFlags & oRF)const;

		virtual ~ReasonFlags(void);

		// GETTERS AND SETTERS

		const int& getNumBits() const;
		const OSOCTET* getData() const;

		QBitArray getRFBits() const;

		QString toBitString() const;

		virtual QString toString() const;
	};

} 
#endif



#ifndef __KEYUSAGE__
#define __KEYUSAGE__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "GeneralName.h"
#include "AY_Eklenti.h"

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
	class Q_DECL_EXPORT KeyUsage  : public EASNWrapperTemplate<ASN1T_IMP_KeyUsage,ASN1C_IMP_KeyUsage> ,public AY_Eklenti
	{
	   static const int DATASIZE	= 2; 
	   static const int KU_NUMBITS	= 9; 
	 
	   int mNumBits;
	   OSOCTET mData[DATASIZE];

		
		

	public:

		enum KeyUsageType 
		{
			KU_DigitalSignature = 0,
			KU_NonRepudiation = 1,
			KU_KeyEncipherment = 2,
			KU_DataEncipherment = 3,
			KU_KeyAgreement = 4,
			KU_KeyCertSign = 5,
			KU_CRLSign = 6,
			KU_EncipherOnly = 7,
			KU_DecipherOnly = 8,
		} ;

		KeyUsage(const KeyUsage &);
		KeyUsage(const ASN1T_IMP_KeyUsage & );
		KeyUsage(const QByteArray & );
		KeyUsage(const int iNumBits , const OSOCTET * iData);
		KeyUsage(void);
		

		KeyUsage & operator=(const KeyUsage&);
		friend bool operator==(const KeyUsage& iRHS, const KeyUsage& iLHS);
		friend bool operator!=(const KeyUsage& iRHS, const KeyUsage& iLHS);


		int copyFromASNObject(const ASN1T_IMP_KeyUsage& iKU);
		int copyToASNObject(ASN1T_IMP_KeyUsage & oKU) const;
		void freeASNObject(ASN1T_IMP_KeyUsage & oKU)const;

		virtual ~KeyUsage(void);

		// GETTERS AND SETTERS

		const int& getNumBits() const;
		const OSOCTET* getData() const;

		void setNumBits(int iNumBits);
		void setData(const OSOCTET *iData);

		QBitArray getKUBits() const;
		bool isType(const KeyUsageType iType) const;

		QString		toString() const;
		QStringList toStringList() const;

		QString toBitString() const;
		static QString bitStringToString(const QString &iBitString);

		/************************************************************************/
		/*					AY_EKLENTI FONKSIYONLARI                            */
		/************************************************************************/

		virtual QString eklentiAdiAl()			const ;
		virtual QString eklentiKisaDegerAl()	const ;
		virtual QString eklentiUzunDegerAl()	const ;

		virtual AY_Eklenti* kendiniKopyala() const ;

	};

}

#endif


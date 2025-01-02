#ifndef __FIELDID__
#define __FIELDID__

#include "pkcs12.h"
#include "ortak.h"
#include "ContentInfo.h"
#include "ESeqOfList.h"
#include "algorithms.h"
#include "myasndefs.h"

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
	class Q_DECL_EXPORT FieldID : public EASNWrapperTemplate<ASN1T_ALGOS_FieldID,ASN1C_ALGOS_FieldID>
	{
		QByteArray mParameters;
		ASN1TObjId mFieldType; 


	public:
		FieldID(void);
		FieldID(const QByteArray & );
		FieldID(const QByteArray & , const ASN1TObjId &);
		FieldID(const ASN1TObjId &);
		FieldID(const ASN1T_ALGOS_FieldID & );
		FieldID(const FieldID& );

		FieldID& operator=(const FieldID& );
		friend bool operator==(const FieldID & iRHS, const FieldID& iLHS);
		friend bool operator!=(const FieldID & iRHS, const FieldID& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_FieldID & ) ;
		int copyToASNObject(ASN1T_ALGOS_FieldID & oFieldID) const;
		void freeASNObject(ASN1T_ALGOS_FieldID & oFieldID)const;

		virtual ~FieldID(void);

		// GETTERS AND SETTERS

		const QByteArray &getParameters()const ;
		const ASN1TObjId &getFieldType() const;

		QByteArray getParamsAsOctets()const;
		void setParamsAsOctets(const QByteArray& );

		void setParameters(const QByteArray& );

		bool isNull() const;

	};

}

#endif

#ifndef __QCSTATEMENT__
#define __QCSTATEMENT__

#include "pkcs12.h"
#include "ortak.h"
#include "ContentInfo.h"
#include "ESeqOfList.h"
#include "PKIXqualified.h"
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
	class Q_DECL_EXPORT QCStatement : public EASNWrapperTemplate<ASN1T_PKIXQUAL_QCStatement,ASN1C_PKIXQUAL_QCStatement>
	{
		bool	mStatementInfoPresent;
		
		QByteArray mStatementInfo;
		ASN1TObjId mStatementID; 


	public:
		QCStatement(void);
		QCStatement(const QByteArray & );
		QCStatement(const QByteArray & , const ASN1TObjId &);
		QCStatement(const ASN1TObjId &);
		QCStatement(const ASN1T_PKIXQUAL_QCStatement & );
		QCStatement(const QCStatement& );

		QCStatement& operator=(const QCStatement& );
		friend bool operator==(const QCStatement & iRHS, const QCStatement& iLHS);
		friend bool operator!=(const QCStatement & iRHS, const QCStatement& iLHS);

		int copyFromASNObject(const ASN1T_PKIXQUAL_QCStatement & ) ;
		int copyToASNObject(ASN1T_PKIXQUAL_QCStatement & oQS) const;
		void freeASNObject(ASN1T_PKIXQUAL_QCStatement & oQS)const;

		virtual ~QCStatement(void);

		// GETTERS AND SETTERS

		bool	isStatementInfoPresent()const;
		const QByteArray &getStatementInfo()const ;
		const ASN1TObjId &getStatementID() const;

		void setStatementID(const ASN1TObjId& );
		void setStatementInfo(const QByteArray& );

		QByteArray getStatementInfoAsOctets()const;
		void setStatementInfoAsOctets(const QByteArray& );

		int copyQSs(const ASN1T_PKIXQUAL_QCStatements & iQSs, QList<QCStatement>& oList);
		int copyQSs(const QList<QCStatement> iList ,ASN1T_PKIXQUAL_QCStatements & oQSs);
		int copyQSs(const QByteArray & iASNBytes, QList<QCStatement>& oList);
		int copyQSs(const QList<QCStatement>& iList , QByteArray & oASNBytes);

		virtual QString toString() const;

	};

}

#endif

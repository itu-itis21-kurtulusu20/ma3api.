#ifndef __QCSTATEMENTS__
#define __QCSTATEMENTS__

#include "QCStatement.h"
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
	class Q_DECL_EXPORT QCStatements  : public EASNWrapperTemplate<ASN1T_PKIXQUAL_QCStatements,ASN1C_PKIXQUAL_QCStatements> , public AY_Eklenti
	{
		QList<QCStatement> mList;

	public:
		QCStatements(void);
		QCStatements(const ASN1T_PKIXQUAL_QCStatements &);
		QCStatements(const QByteArray &);
		QCStatements(const QCStatements&);
		QCStatements(const QList<QCStatement>&);

		QCStatements & operator=(const QCStatements&);
		friend bool operator==(const QCStatements & iRHS, const QCStatements& iLHS);
		friend bool operator!=(const QCStatements & iRHS, const QCStatements& iLHS);

		int copyFromASNObject(const ASN1T_PKIXQUAL_QCStatements &);
		int copyToASNObject(ASN1T_PKIXQUAL_QCStatements& )const;
		void freeASNObject(ASN1T_PKIXQUAL_QCStatements & )const;

		virtual ~QCStatements(void);

		// GETTERS AND SETTERS

		const QList<QCStatement> &getList() const;
		void setList(const QList<QCStatement> &);

		QByteArray getEncodedBytes()const ;

		virtual QString toString() const;
		QStringList toStringList() const ;

		int indexOf(const ASN1TObjId iStatementId)const;

		void  appendStatement(const QCStatement &iQC);

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


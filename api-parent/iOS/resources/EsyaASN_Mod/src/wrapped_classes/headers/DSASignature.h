#ifndef __DSASIGNATURE__
#define __DSASIGNATURE__

#include "algorithms.h"
#include "ortak.h"

namespace esya
{

	class Q_DECL_EXPORT DSASignature : public EASNWrapperTemplate<ASN1T_ALGOS_DssSigValue,ASN1C_ALGOS_DssSigValue>
	{
		QString		mR;
		QString		mS;
		

	public:
		DSASignature(void);
		DSASignature(const QByteArray & iDS);
		DSASignature(const QString& iR, const QString & iS);
		DSASignature(const QByteArray& iR, const QByteArray& iS);
		DSASignature(const ASN1T_ALGOS_DssSigValue & iDS );
		DSASignature(const DSASignature& iDS);

		DSASignature& operator=(const DSASignature& iDI);
		Q_DECL_EXPORT friend bool operator==( const DSASignature& iRHS, const DSASignature& iLHS);
		Q_DECL_EXPORT friend bool operator!=( const DSASignature& iRHS, const DSASignature& iLHS);

		int copyFromASNObject(const ASN1T_ALGOS_DssSigValue & iDS);
		int copyToASNObject(ASN1T_ALGOS_DssSigValue &oDS)const;
		void freeASNObject(ASN1T_ALGOS_DssSigValue& )const;

		virtual ~DSASignature(void);

		// GETTERS AND SETTERS 
		const QString & getR() const;
		const QString & getS() const;

		void setR(const QString& iR);
		void setS(const QString & iS) ;

		static QByteArray convertToDER(const QByteArray & iP1363);
		static QByteArray convertToP1363(const QByteArray & iDER);

	};

}

#endif


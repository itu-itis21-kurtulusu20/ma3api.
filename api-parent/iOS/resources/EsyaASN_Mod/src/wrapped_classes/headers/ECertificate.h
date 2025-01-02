
#ifndef __ECERTIFICATE__
#define __ECERTIFICATE__


#include "TBSCertificate.h"
#include "CDP.h"
#include "Eklenti.h"

#include <QSharedData>
#include <QSharedDataPointer>



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
	class ECertificate;

	class Q_DECL_EXPORT ECertificateData : public QSharedData,public EASNWrapperTemplate<ASN1T_EXP_Certificate,ASN1C_EXP_Certificate>
	{

		friend class ECertificate;

		TBSCertificate		mTBSCertificate;
		EBitString			mSignature;
		AlgorithmIdentifier mSignatureAlgorithm;

	public:
		ECertificateData(void);
		ECertificateData(const QByteArray & );
		ECertificateData(const ASN1T_EXP_Certificate & );
		ECertificateData(const ECertificateData&);
		ECertificateData(const QString&);

		ECertificateData & operator=(const ECertificateData & );
		Q_DECL_EXPORT friend bool operator==(const ECertificateData & ,const ECertificateData & );
		Q_DECL_EXPORT friend bool operator!=(const ECertificateData & ,const ECertificateData & );


		bool operator<(const ECertificateData & )const;

		int copyFromASNObject(const ASN1T_EXP_Certificate &);
		int copyToASNObject(ASN1T_EXP_Certificate & oCertificate)const;
		void freeASNObject(ASN1T_EXP_Certificate & oCertificate)const;

		int  copyCertificates(const QList<ECertificateData> iList ,ASN1TPDUSeqOfList & oCerts);
		int	copyCertificates(const ASN1TPDUSeqOfList & iCerts, QList<ECertificateData>& oList);

		virtual ~ECertificateData(){};

	};

	class Q_DECL_EXPORT ECertificate
	{
		QSharedDataPointer<ECertificateData> d;

	public:
		ECertificate(void){ d = new ECertificateData();}
		ECertificate(const QByteArray & iCertBytes) { d = new ECertificateData(iCertBytes);}
		ECertificate(const ASN1T_EXP_Certificate & iCert){ d= new ECertificateData(iCert);}
		ECertificate(const ECertificate& iCert): d(iCert.d) {}
		ECertificate(const ECertificateData& iCertData){ d= new ECertificateData(iCertData);}
		ECertificate(const QString& iCertFile) { d = new ECertificateData(iCertFile);}

		ECertificate & operator=(const ECertificate & iCert) { d = iCert.d; return *this;}
		Q_DECL_EXPORT friend bool operator==(const ECertificate & iRHS,const ECertificate & iLHS) { return *(iRHS.d) == *(iLHS.d); }
		Q_DECL_EXPORT friend bool operator!=(const ECertificate & iRHS,const ECertificate & iLHS) { return !(iRHS == iLHS);}

		bool operator<(const ECertificate & iCert)const { return *d<*(iCert.d);}

		int copyFromASNObject(const ASN1T_EXP_Certificate &iCert)
		{
			return d->copyFromASNObject(iCert);
		};
		int copyToASNObject(ASN1T_EXP_Certificate & oCertificate)const
		{
			return d->copyToASNObject(oCertificate);
		};
		void freeASNObject(ASN1T_EXP_Certificate & oCertificate)const
		{
			d->freeASNObject(oCertificate);
		}

		QByteArray getEncodedBytes()const
		{
			return d->getEncodedBytes();	
		}

 		ASN1T_EXP_Certificate* getASNCopy()const
		{
			return d->getASNCopy();
		}
		
		int  copyCertificates(const QList<ECertificate> iList ,ASN1TPDUSeqOfList & oCerts)
		{
			QList<ECertificateData> certDataList;
			Q_FOREACH(ECertificate cert ,iList)
			{
				certDataList<<*(cert.d);
			}
			d->copyCertificates(certDataList,oCerts);
			return SUCCESS;
		}

 		int	copyCertificates(const ASN1TPDUSeqOfList & iCerts, QList<ECertificate>& oList)
		{
			QList<ECertificateData> cdList;
			d->copyCertificates(iCerts,cdList);
			Q_FOREACH(ECertificateData cd,cdList)
			{
				oList<<ECertificate(cd);
			}
			return SUCCESS;
		}

		int loadFromFile(const QString& iCertFile)
		{
			return d->loadFromFile(iCertFile);
		}

		int write2File(const QString& iCertFile)const
		{
			return d->write2File(iCertFile);
		}


		const TBSCertificate & getTBSCertificate() const;
		TBSCertificate & getTBSCertificate();

		const AlgorithmIdentifier & getSignatureAlgorithm() const;
		const EBitString & getSignature()const;

		void setTBSCertificate(const TBSCertificate & ) ;
		void setSignature(const EBitString & ) ;
		void setSignatureAlgorithm(const AlgorithmIdentifier & );


		QString toString() const;

		bool kokSertifikaMi()const;
		bool imzalamaSertifikasimi()const;
		bool sifrelemeSertifikasimi()const;

		QString getEPosta() const;

		QList<CDP> getCDPs()const ;

		QList<Name> getCRLIssuers(const QString &aAdresTipi)const ;
		QList<Name> getCRLDizinIssuers() const ;
		QList<QString> getCDPAddresses(const QString &) const ;


		QList<Eklenti> getEklentiler()const ;

		int indexOfExtension(const ASN1TObjId& )const;

		bool isOCSPCertificate()const;
		bool isCACertificate()const;
		bool isSelfSigned()const;
		bool isSelfIssued()const;
		

		Name getCRLIssuer()const;
		bool hasIndirectCRL()const;

		bool isQualified()const;
	};

}

Q_DECL_EXPORT inline uint qHash(const esya::ECertificate &);

#endif


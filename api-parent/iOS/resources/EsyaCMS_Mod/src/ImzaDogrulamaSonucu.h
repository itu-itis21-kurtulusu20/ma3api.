#ifndef __IMZADOGRULAMASONUCU__
#define __IMZADOGRULAMASONUCU__

#include <QString>
#include <QList>
#include "KontrolcuSonucu.h"
#include "SignerIdentifier.h"


namespace esya
{

	/**
	* \ingroup EsyaCMS
	* 
	* Ýmza doðrulama iþleminin sonucuyla alakalý bilgileri tutar
	* Alt imza doðrulama sonuçlarýný bir aðaç yapýsý þeklinde saklar.
	* 
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT ImzaDogrulamaSonucu
	{
	public:
		enum IDS_Type { GECERLI , ALT_IMZACI_KONTROLLERI_SORUNLU , KONTROLLER_SORUNLU , KONTROL_TAMAMLANAMADI , IMZALI_DOSYA_DEGIL};

	protected:
		QString						mKontrolAdi;
		QString						mAciklama;
		IDS_Type					mDogrulamaSonucu;
		QList<KontrolcuSonucu>		mKontrolDetaylari;	
		QList<ImzaDogrulamaSonucu>	mAltDogrulamaSonuclari;		


		SignerIdentifier *			pSID;
		ECertificate *				pSignerCert;


		static QString _indent(const QString &iBlock, int iLen);
		

		void _collectTBVCerts(QList<ECertificate>  & oTBVCerts);

	public:
		ImzaDogrulamaSonucu(void);
		ImzaDogrulamaSonucu(const ImzaDogrulamaSonucu &);
		ImzaDogrulamaSonucu(	const QString & iKontrolAdi ,
								const QString & iAciklama ,					
								const IDS_Type iDS = KONTROL_TAMAMLANAMADI ,
								const QList<KontrolcuSonucu>  & iKD= QList<KontrolcuSonucu>() ,		
								const QList<ImzaDogrulamaSonucu>  & iADS = QList<ImzaDogrulamaSonucu>() ,
								const SignerIdentifier* ipSID = NULL,
								const ECertificate* ipSignerCert = NULL );		

		
		ImzaDogrulamaSonucu& operator=(const ImzaDogrulamaSonucu &iIDS);


		const	QString &	getKontrolAdi() const ;
				QString &	getKontrolAdi() ;
		void				setKontrolAdi(const QString & );

		const	QString &	getAciklama() const ;
				QString &	getAciklama() ;
		void				setAciklama(const QString & );

		const IDS_Type & getDogrulamaSonucu() const;
			  IDS_Type & getDogrulamaSonucu();
		void			 setDogrulamaSonucu(const IDS_Type & );

		const	QList<KontrolcuSonucu>&	getKontrolDetaylari() const ;
				QList<KontrolcuSonucu>&	getKontrolDetaylari() ;
		void							setKontrolDetaylari(const QList<KontrolcuSonucu>& );
		void							addKontrolDetayi(const KontrolcuSonucu& );


		const	QList<ImzaDogrulamaSonucu>&	getAltDogrulamaSonuclari() const ;
				QList<ImzaDogrulamaSonucu>&	getAltDogrulamaSonuclari() ;
		void								setAltDogrulamaSonuclari(const QList<ImzaDogrulamaSonucu>& );
		void								addAltDogrulamaSonucu(const ImzaDogrulamaSonucu & );


		const SignerIdentifier *	getSID()  const;
		SignerIdentifier *				getSID();  		
		void							setSID(const SignerIdentifier * );		
		

		const ECertificate *	getSignerCert()  const;
		ECertificate *				getSignerCert();  		
		void						setSignerCert(const ECertificate * );		


		QString getDogrulamaSonucuAsString() const;

		QString toString() const;

		QList<ECertificate> getTBVCerts();

	public:
		~ImzaDogrulamaSonucu(void);
	};

}

#endif

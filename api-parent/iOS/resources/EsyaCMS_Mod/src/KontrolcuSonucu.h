#ifndef __KONTROLCUSONUCU__
#define __KONTROLCUSONUCU__

#include <QString>
#include <QList>

namespace esya
{
	/**
	* \ingroup EsyaCMS
	* 
	* Ýmza doðrulama iþlemindeki kontrolcülerin kontrol sonucuyla alakalý bilgileri tutar
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT KontrolcuSonucu
	{
	public:
		enum KS_Type { BASARILI , BASARISIZ , KONTROL_TAMAMLANAMADI };

	protected:
		QString	mKontrolAdi;
		QString	mAciklama;
		KS_Type	mKontrolSonucu;


	public:
		KontrolcuSonucu(void);
		KontrolcuSonucu(const KontrolcuSonucu&);
		KontrolcuSonucu(	const QString & iKontrolAdi ,
							const QString & iAciklama ,
							const KS_Type & iKS = KONTROL_TAMAMLANAMADI 
						);
			


		const	QString &	getKontrolAdi() const ;
				QString &	getKontrolAdi() ;
		void				setKontrolAdi(const QString & );

		const	QString &	getAciklama() const ;
				QString &	getAciklama() ;
		void				setAciklama(const QString & );


		const KS_Type & getKontrolSonucu() const;
			  KS_Type & getKontrolSonucu();
		void		    setKontrolSonucu(const KS_Type & );

		QString getKontrolSonucuAsString() const;

		QString toString() const;

	public:
		~KontrolcuSonucu(void);
	};

}

#endif


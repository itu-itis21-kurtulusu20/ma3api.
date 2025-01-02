#ifndef __IMZADOGRULAMAALGORITMASI__
#define __IMZADOGRULAMAALGORITMASI__

#include "SignedData.h"//#include "StreamedSignedData.h"
#include "ImzaKontrolcu.h"
#include "ImzaDogrulamaSonucu.h"
//#include "aciklamalar.h"
#include <QVarLengthArray>


namespace esya
{
	/**
	* \ingroup EsyaCMS
	* 
	* Ýmza doðrulamada yer alacak kontrol iþlemlerini belirler.Ýmza Kontrolcüleri bir liste halinde saklar ve
	* sýrayla çalýþtýrýr.
	* 
	*
	* \author dindaro
	*
	*/
	class ImzaDogrulamaAlgoritmasi
	{
	public:
		enum K_Type { PARALEL , SERI };
	protected:

		QList<ECertificate> mCertList;

		QList<ImzaKontrolcu*> mParalelKontrolculer;
		QList<ImzaKontrolcu*> mSeriKontrolculer;

		void _freeKontrolculer(QList<ImzaKontrolcu*> & );
	
		bool _kontrolYap( K_Type iType, const SignerInfo& iSI, ImzaDogrulamaSonucu & oIDS);

	public:
		ImzaDogrulamaAlgoritmasi(const QList<ECertificate>& iCertList = QList<ECertificate>());
	
		const	QList<ImzaKontrolcu*> & getParalelKontrolculer()const ;
				QList<ImzaKontrolcu*> & getParalelKontrolculer();
		void							addParalelKontrolcu(ImzaKontrolcu* );


		const	QList<ImzaKontrolcu*> & getSeriKontrolculer()const ;
				QList<ImzaKontrolcu*> & getSeriKontrolculer();
		void							addSeriKontrolcu(ImzaKontrolcu* );


		bool kontrolYap( const SignedData & iSD, ImzaDogrulamaSonucu & oIDS );
	

		const QList<ECertificate> & getCertList() const ;

		void setCertList(const QList<ECertificate> &);

	public:
		~ImzaDogrulamaAlgoritmasi(void);
	};

}
#endif


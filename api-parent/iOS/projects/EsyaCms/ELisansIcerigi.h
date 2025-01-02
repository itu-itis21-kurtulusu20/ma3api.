#ifndef __E_LISANS_ICERIGI__H__
#define __E_LISANS_ICERIGI__H__
#define ESYA_LISANS_TARIH_FORMAT "dd/MM/yyyy"
#include <QDateTime>
#include "EsyaGenelGUI_Ortak.h"
#include <QDomDocument>
#include <QMap>

NAMESPACE_BEGIN(esya)

class Q_DECL_EXPORT EUrunBilgi
{
	int mUrunID;
	QMap<QString,QString> mOzellikListesi;
public:
	EUrunBilgi(const int & iUrunID);
	EUrunBilgi(const int & iUrunID,const QMap<QString,QString> & iOzellikListesi);
	~EUrunBilgi();
	int getUrunID() const{return mUrunID;};
	const QMap<QString,QString> & getOzellikListesi() const{return mOzellikListesi;};
};

/**
 * \ingroup EsyaGenelGUI
 *
 *  Lisans dosyasýnýn içeriðini tutmak için kullanýlan sýnýf
 *  Kurum,Yetkili,EMail,Telefon,Baþlangýç Tarihi ve Bitiþ tarihi tutuluyor
 *
 * \date 06-26-2008
 *
 * \author ramazang
 *
 *
 */
class Q_DECL_EXPORT ELisansIcerigi
{
	public:
		 ELisansIcerigi(const QString & iTumLisansData);
		 ~ELisansIcerigi(void);
		 ELisansIcerigi(void);
		 void xmlLisansIcerigindenIlklendir(const QByteArray & iXMLLisansFile);
		 void setTumLisansData(QString iTumLisansData);
		 QString getYetkili();
		 QString getKurum();
		 QString getEPosta();
		 QString getTelefon();
		 QDateTime getBaslangicTarihi();
		 QDateTime getBitisTarihi();
		 QString getLisansID() const;		
		 const QList<EUrunBilgi> & getUrunBilgileri(){return mUrunBilgileri;};
		 void setUrunBilgileri(const QList<EUrunBilgi> & iUrunBilgileri){mUrunBilgileri=iUrunBilgileri;};
		const QList<QPair<QString,QString> > & getKartKontrolListesi();
		const QMap<int,QString> & getFonksiyonKisitListesi(){return mFonksiyonKisitListesi;};
private:
	QList<EUrunBilgi> mUrunBilgileri;
	QString mLisansID;
	QString mYetkili;
	QString mKurum;
	QString mEPosta;
	QString mTelefon;
	QDateTime mBaslangicTarihi;
	QDateTime mBitisTarihi;
	QList<QPair<QString,QString> > mKartKontrolListesi;
	QMap<int,QString> mFonksiyonKisitListesi;
	void _initFromTumLisansData(QString iTumLisansData);
	QString _getStringValueTagName(const QDomDocument & iDocument,const QString & iTagName);	
};
NAMESPACE_END
#endif

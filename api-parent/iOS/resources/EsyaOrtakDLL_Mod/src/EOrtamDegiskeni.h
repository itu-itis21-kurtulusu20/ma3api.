#ifndef _E_ORTAM_DEGISKENI_H_
#define _E_ORTAM_DEGISKENI_H_

#include <QString>
#include <QMap>

/**
 * \ingroup EsyaOrtakDLL
 *
 * Ortam deðiþkenleri okumak için kullanýlýr. 
 *
 * \version 1.0
 * first version
 *
 * \date 02-03-2010
 *
 * \author ramazang
 * 
 * 
 * \todo 
 *
 * \bug 
 *
 */
class Q_DECL_EXPORT EOrtamDegiskeni
{
	QString mDegiskenAdi;
	QString mDegiskenDegeri;
	bool mIsNull;
public:
	EOrtamDegiskeni(void);
	EOrtamDegiskeni(const QString & iDegiskenAdi,const QString & iDegiskenDegeri);
	~EOrtamDegiskeni(void);
	bool isNull();
	QString getDegiskenAdi() const;
	QString getDegiskenDegeri() const;
	static QMap<QString,QString>  ortamDegiskenMapGetir();
	static EOrtamDegiskeni degiskenDegerBul(const QString & iDegiskenAdi);
	static QString degiskenliStrCoz(const QString & iAyarTamDegeri);

};
#endif
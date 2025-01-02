#ifndef __EACIKDOSYA_H__
#define __EACIKDOSYA_H__

#include "esyaOrtak.h"
#include "EAyarException.h"
#include <QString>
#include <QDateTime>


NAMESPACE_BEGIN(esya)

/**
 * \ingroup EsyaOrtakDLL
 *
 * Ayarlarda tutulan açýk dosyalarý herbirini tutmak için kullanýlan entry sýnýfý 
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 * 
 * 
 * \todo 
 *
 * \bug 
 *
 */
class Q_DECL_EXPORT EAyarGDMAcikDosya
{
public:
	enum DosyaDurumu{DosyaAcildi};

	EAyarGDMAcikDosya(){};
	EAyarGDMAcikDosya(int iDosyaID,const QString& irDosyaYolu,const QDateTime& iTarih,DosyaDurumu iDurum);
	virtual ~EAyarGDMAcikDosya();

	int getDosyaID() const;
	const QString& getDosyaYolu() const;
	const QDateTime& getTarih() const;
	DosyaDurumu getDurum() const;

	friend bool operator==(const EAyarGDMAcikDosya& iRHS, const EAyarGDMAcikDosya& iLHS);

	friend QDataStream & operator>> ( QDataStream & in, EAyarGDMAcikDosya & oAD );
	friend QDataStream & operator<< ( QDataStream & out, const EAyarGDMAcikDosya & iAD );

private:
	int mDosyaID;
	QString mDosyaYolu;
	QDateTime mTarih;
	DosyaDurumu mDurum;

};

NAMESPACE_END

#endif

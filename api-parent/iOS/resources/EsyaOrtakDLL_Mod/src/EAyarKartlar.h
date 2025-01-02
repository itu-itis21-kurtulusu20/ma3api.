#ifndef __EAYARKARTLAR_H__
#define __EAYARKARTLAR_H__

#include "esyaOrtak.h"
#include <QString>
#include <QSqlDatabase>
#include <QVariant>
#include "EAyarException.h"

using namespace esya;
NAMESPACE_BEGIN(esya)

/**
 * \ingroup EsyaOrtakDLL
 *
 * Akýllý kartlarla ilgili ayarlarýn tutulmasý için kullanýlan enrty sýnýfý 
 *
 * \version 1.0
 * first version
 *
 * \date 05-15-2009
 *
 * \author ramazang
 * 
 * \todo 
 *
 * \bug 
 *
 */
class EAyarKartlar:public QObject
{	
	Q_OBJECT
public:
	Q_DECL_EXPORT EAyarKartlar(int iKartID,
		const QString &irKartAdi,
		const QString &irKartLib,
		const QString &iImzalamaCSPAdi,
		const QString &iSifrelemeCSPAdi,
		QObject * parent=NULL
		);
Q_DECL_EXPORT EAyarKartlar(const EAyarKartlar & iAyarKartlar,QObject * parent=NULL);
	Q_DECL_EXPORT int getKartID() const;
	Q_DECL_EXPORT const QString &getKartAdi() const;
	Q_DECL_EXPORT const QString &getKartLib() const;
	Q_DECL_EXPORT const QString &getImzalamaCSPAdi() const;
	Q_DECL_EXPORT const QString &getSifrelemeCSPAdi() const;

	Q_DECL_EXPORT static QList<EAyarKartlar> tumKartlariAl();
	Q_DECL_EXPORT static QList<EAyarKartlar *> tumKartlariAlPtr();
	Q_DECL_EXPORT EAyarKartlar & operator=(const EAyarKartlar & iAyarKartlar);

protected:
	int mKartID;
	QString mKartAdi;
	QString mKartLib;
	QString mImzalamaCSPAdi;
	QString mSifrelemeCSPAdi;
};

NAMESPACE_END

#endif

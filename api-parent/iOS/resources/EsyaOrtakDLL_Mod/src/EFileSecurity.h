#ifndef EFILESECURITY_H
#define EFILESECURITY_H


#if defined(WIN32)

#include "EWindowsFileSecurity.h"

#else 

#include "ELinuxFileSecurity.h"

#endif


#include <QString>
#include <QStringList>
#include <QFile>



class  Q_DECL_EXPORT EFileSecurityInfo
{
protected:
	QString mFileName;
	QString mOwner;
	QStringList mOkuyanKullanicilar;
	QStringList mOkuyanGruplar;
public:
	EFileSecurityInfo(const QString & iFileName = "") : mFileName(iFileName) {};
	
	const QString & getFileName()					const ;
	const QString & getOwner()						const;
	const QStringList & getOkuyanKullanicilar()	const;
	const QStringList & getOkuyanGruplar()		const;

	void setFileName(const QString & );
	void setOwner(const QString & );
	void setOkuyanKullanicilar(const QStringList & );
	void setOkuyanGruplar(const QStringList & );

};

class  Q_DECL_EXPORT EFileSecurity  
{

#if defined(WIN32)
	EWindowsFileSecurity mFileSecurity;
#else
	ELinuxFileSecurity	mFileSecurity;
#endif

public: 
	EFileSecurity();
	~EFileSecurity();

	EFileSecurityInfo getFileSecurityInfo(const QString& iFileName, QString & oHata);
	QString getCurrentUserName(QString & oHata) const;
	
	
	int storeOwnerInfo(const QString& iFileName,QString & oHata);	//kaynak dosya verilerek owner infoyu kaydeder.
	int restoreOwnerInfo(const QString& iFileName, QString & oHata);	//hedef dosya verilerek kaynak dosyanýn ownerý deðiþtirilir.


	//	static QString getDNS();

private:
	
};

#endif // EFILESECURITY_H

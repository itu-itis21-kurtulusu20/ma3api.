#ifndef EWINDOWSFILESECURITY_H
#define EWINDOWSFILESECURITY_H

#if defined(WIN32)


#include <windows.h>

#include <QString>


#define BUF_SIZE 1000

class  Q_DECL_EXPORT EWindowsFileSecurity
{
public:
	EWindowsFileSecurity();
	~EWindowsFileSecurity();


	int ownerAl( const QString & iFileName, QString & oOwner , QString & oHata );
	int okumaHakkiOlanlariAl(const QString & iFileName , QStringList & oGruplar ,QStringList & oKullanicilar ,QString & oHata );

	QString getUserName(QString & oHata)const;

	int storeOwnerInfo(const QString & iFileName,QString& oHata);
	int restoreOwnerInfo(const QString & iFileName,QString & oHata);


private:
	PSID pOwner;
	PSECURITY_DESCRIPTOR pSD;
};

#endif 

#endif // EWINDOWSFILESECURITY_H

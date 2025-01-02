
#ifndef WIN32 

#ifndef ELINUXFILESECURITY_H
#define ELINUXFILESECURITY_H

#include <QString>
#include "ESambaIstemcisi.h"
#include <QMap>


#define MOUNTTABLE_FILE "/etc/mtab"
#define SAMBAPREFIX "smb:"
#define SAMBALOCALPREFIX "smb://localhost"

class  Q_DECL_EXPORT ELinuxFileSecurity 
{
	QString					mFileName;
	QString					mSambaURL;
	ESambaIstemcisi			mSMB;

	static bool						msMountTableConstructed;
	static QMap<QString,QString>	msMountTable; 

	void _constructMountTable();

	QString _findInMountTable(const QString & iFileName);

public:
	ELinuxFileSecurity();
	~ELinuxFileSecurity();

	
	void setFileName(const QString & );
	
	int ownerAl(  QString & oOwner , QString & oHata );
	int okumaHakkiOlanlariAl( QStringList & oOkuyanlar ,QString & oHata );

	QString localPrefixOlustur();

	int storeOwnerInfo(const QString & iFileName , QString & oHata);
	int restoreOwnerInfo(const QString & iFileName, QString & oHata);

private:
	
};

#endif // ELINUXFILESECURITY_H


#endif
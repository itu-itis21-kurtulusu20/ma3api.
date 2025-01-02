#ifndef __EFILELOCKER_H__
#define __EFILELOCKER_H__

#include "esyaOrtak.h"
#include <QString>
#include "EException.h"

#if defined(WIN32)
#include <windows.h>
#else
#include <fcntl.h>
#endif

NAMESPACE_BEGIN(esya)

#define EFILELOCKERMODUL "EFileLockerModul"
#define EFILELOCKERERRORYAZ(x) ERRORLOGYAZ(EFILELOCKERMODUL,x)
#define EFILELOCKERDEBUGYAZ(x) DEBUGLOGYAZ(EFILELOCKERMODUL,x)




class  Q_DECL_EXPORT EFileLocker
{
public:
	enum Hatalar{
		HATAYOK,
		HATADOSYAACILAMADI,
		HATADOSYAKULLANIMDA,
		HATADOSYAKAPATILAMADI,
		HATABILINMEYENHATA
	};

    enum Durumlar{
        KILITYOK,
        KILITOKUMA,
        KILITYAZMA
    };

	EFileLocker(const QString &irFilePath);

	bool lock();
	bool unlock();
	Hatalar getSonHata() const;
    Durumlar getDurum() const;

	void setSadeceWriteLockAl(bool iSadeceWriteLockAl){mSadeceWriteLockAl=iSadeceWriteLockAl;};

	virtual ~EFileLocker();

private:
	QString mFilePath;
	bool mDosyaLockli;
	Hatalar mSonHata;
    Durumlar mDurum;
	bool mSadeceWriteLockAl;

#if defined(WIN32)
	HANDLE mWinHandle;
#else
	int mFileDescriptor;
#endif

	bool _lock();
	bool _unlock();
};


NAMESPACE_END
#endif //__EFILELOCKER_H__

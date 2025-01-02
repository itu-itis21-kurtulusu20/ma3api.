#include "EFileLocker.h"
#include "ELogger.h"

#if defined(WIN32)
#else
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#endif

NAMESPACE_BEGIN(esya)

#define EFL_FUNC_BEGIN        FUNCTRY_BEGIN(ESYAORTAK_MOD);\
	mSonHata = HATAYOK;

#define EFL_FUNC_END          FUNCTRY_END(ESYAORTAK_MOD)

EFileLocker::EFileLocker(const QString &irFilePath)
:mDosyaLockli(false),
mDurum(KILITYOK),
mFilePath(irFilePath),
mSadeceWriteLockAl(false)
{

}


bool EFileLocker::lock()
{
	EFL_FUNC_BEGIN;

	//daha onceden lockliysa bir sey yapmadan donelim
	if(mDosyaLockli)
		return true;

	mDosyaLockli = _lock();

	return mDosyaLockli;

	EFL_FUNC_END;
}

bool EFileLocker::unlock()
{
	EFL_FUNC_BEGIN;

	if(!mDosyaLockli)
		return true;

	bool unlocked = _unlock();

	mDosyaLockli = !unlocked;
	return unlocked;

	EFL_FUNC_END;
}

EFileLocker::Hatalar EFileLocker::getSonHata() const
{
	return mSonHata;
}

EFileLocker::Durumlar EFileLocker::getDurum() const
{
    return mDurum;
}


EFileLocker::~EFileLocker()
{
	unlock();
}

#if defined(WIN32)

bool EFileLocker::_lock()
{
	EFL_FUNC_BEGIN;
	LPCWSTR lPath = (LPCWSTR)mFilePath.constData();
	
	HANDLE hOFile=INVALID_HANDLE_VALUE;
	if(mSadeceWriteLockAl)
		hOFile= CreateFile(lPath,FILE_WRITE_DATA, FILE_SHARE_WRITE,NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	else
		hOFile= CreateFile(lPath,FILE_WRITE_DATA, FILE_SHARE_READ, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (hOFile==INVALID_HANDLE_VALUE)
	{
		int err = GetLastError();
		EFILELOCKERDEBUGYAZ(QString("_lock(): \"%1\" dosyasina WRITE HANDLE ALINAMADI. Error No: %2").arg(mFilePath).arg(err));
		switch(err) 
		{
			case ERROR_SHARING_VIOLATION:
				EFILELOCKERDEBUGYAZ(QString("_lock(): WRITE HANDLE alinamadi - ERROR_SHARING_VIOLATION"));
				mSonHata = HATADOSYAKULLANIMDA;
				break;
			case ERROR_ACCESS_DENIED:	
				if (mSadeceWriteLockAl)
				{						
						mSonHata = HATADOSYAKULLANIMDA;
						EFILELOCKERDEBUGYAZ(QString("_lock(): WRITE HANDLE alinamadi - ACCESS DENIED"));
						break;
				}				
				hOFile = CreateFile(lPath,FILE_READ_DATA, FILE_SHARE_READ, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
				if (hOFile==INVALID_HANDLE_VALUE)
				{
					err = GetLastError();
					EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasina READ HANDLE ALINAMADI. Error No: %2").arg(mFilePath).arg(err));
					if(err == ERROR_SHARING_VIOLATION)
					{
						EFILELOCKERERRORYAZ(QString("_lock(): READ HANDLE alinamadi - ERROR_SHARING_VIOLATION"));
						mSonHata = HATADOSYAKULLANIMDA;
					}
					else
					{
						EFILELOCKERERRORYAZ(QString("_lock(): Bilinmeyen baska bir hata sebebiyle READ Handle alinamadi!"));
						mSonHata = HATABILINMEYENHATA;
					}
				}
				else
				{
					mDurum = KILITOKUMA;
					EFILELOCKERDEBUGYAZ("_lock(): READ HANDLE alindi!");
				}
				break;
			default:
				EFILELOCKERERRORYAZ(QString("_lock(): Bilinmeyen baska bir hata sebebiyle WRITE Handle alinamadi!"));
				mSonHata = HATABILINMEYENHATA;
				break;
		}
	} // if (hOFile==INVALID_HANDLE_VALUE)
    else
	{
        mDurum = KILITYAZMA;
		EFILELOCKERDEBUGYAZ("_lock(): WRITE HANDLE alindi!");
    }

	//Son hataya bakarak handle alinmis mi anlayalim.
	if(mSonHata == HATAYOK)
	{
		mWinHandle = hOFile;
		return true;
	} // if(mSonHata == HATAYOK)
	return false;
	EFL_FUNC_END;
}



bool EFileLocker::_unlock()
{
	EFL_FUNC_BEGIN;

	if (! CloseHandle(mWinHandle) )
	{
		mSonHata = HATADOSYAKAPATILAMADI;
		return false;
	} // if (! CloseHandle(mWinHandle) )

    mDurum = KILITYOK;
	return true;

	EFL_FUNC_END;
}

#else

void openHataYaz(int iErr)
{
    QString mesaj;
    switch(iErr)
    {
        case EACCES:
            mesaj = "";
            break;
        case EEXIST:
            mesaj = "EEXIST";
            break;
        case EFAULT:
            mesaj = "EFAULT";
            break;
        case EFBIG:
            mesaj = "EFBIG";
            break;
        case EISDIR:
            mesaj = "EISDIR";
            break;
        case ELOOP:
            mesaj = "ELOOP";
            break;
        case EMFILE:
            mesaj = "EMFILE";
            break;
        case ENAMETOOLONG:
            mesaj = "ENAMETOOLONG";
            break;
        case ENFILE:
            mesaj = "ENFILE";
           break;
        case ENODEV:
            mesaj = "ENODEV";
           break;
        case ENOENT:
            mesaj = "ENOENT";
           break;
        case ENOMEM:
            mesaj = "ENOMEM";
           break;
        case ENOSPC:
            mesaj = "ENOSPC";
           break;
        case ENOTDIR:
            mesaj = "ENOTDIR";
           break;
        case ENXIO:
            mesaj = "ENXIO";
           break;
        case EPERM:
            mesaj = "EPERM";
           break;
        default:
            mesaj = "Bilinmiyor!";

    }
    EFILELOCKERDEBUGYAZ(QString("Open Hatasi: No=%1    Mesaj=%2").arg(iErr).arg(mesaj));
}


/*
bool _dosyayiDahaOnceLocklamisim(int iFD)
{
	EFILELOCKERERRORYAZ(QString("_dosyayiDahaOnceLocklamisim(): Dosya Descriptor = %1").arg(iFD));
	                 / * l_type   l_whence  l_start  l_len  l_pid   * /
	struct flock fl = { F_WRLCK, SEEK_SET, 0,       0,     0 };
    if (fcntl(iFD, F_GETLK, &fl) == -1) {
		EFILELOCKERERRORYAZ(QString("_dosyayiDahaOnceLocklamisim(): Dosya hakkinda lock bilgisi alinamadi!"));
		//mSonHata = HATADOSYAKULLANIMDA;
	} 
    else {
        if(fl.l_type == F_WRLCK)
        {
		    EFILELOCKERERRORYAZ(QString("_dosyayiDahaOnceLocklamisim(): Dosya %1 PID tarafindan F_WRLCK lockli!").arg(fl.l_pid));
        }
        else if(fl.l_type == F_UNLCK)
        {
		    EFILELOCKERERRORYAZ(QString("_dosyayiDahaOnceLocklamisim(): Dosyada lock yok!"));
        }
        else
        {
		    EFILELOCKERERRORYAZ(QString("_dosyayiDahaOnceLocklamisim(): Dosya %1 PID tarafindan ?????? lockli!").arg(fl.l_pid));
        }
    }

    return false;
}
*/

bool EFileLocker::_lock()
{
	EFL_FUNC_BEGIN;

                 /* l_type   l_whence  l_start  l_len  l_pid   */
	struct flock fl = { F_WRLCK, SEEK_SET, 0,       0,     0 };
	int fd=0;
    QByteArray tempArray = mFilePath.toLocal8Bit();
    const char * lPath = tempArray.constData(); //bu lPath tempArray hayatta oldugu surece hayatta kalir...


	if ((fd = open(lPath, O_RDWR)) == -1) {
        int myerr = errno;
		EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasi read-write olarak acilamadi!").arg(lPath));
        openHataYaz(myerr);
		mSonHata = HATADOSYAACILAMADI;
	} // if ((fd = open(lPath, O_RDWR)) == -1)
	/*else if(_dosyayiDahaOnceLocklamisim(fd))
    {
        EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasini daha once ayni process, baska instance ile ben locklamisim!").arg(lPath));
        mSonHata = HATADOSYAKULLANIMDA;
    } */
    else if (fcntl(fd, F_SETLK, &fl) == -1) {
		EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasi kullanimda, write lock konamadi!").arg(lPath));
		mSonHata = HATADOSYAKULLANIMDA;
	} // if (fcntl(fd, F_SETLK, &fl) == -1)
	else
	{
        mDurum = KILITYAZMA;
		EFILELOCKERDEBUGYAZ(QString("_lock(): \"%1\" dosyasi write locklandi!").arg(lPath));
	}
	
    //Write icin dosya acilamamissa, ayni seyi bir de read icin deneyelim
    if(mSonHata == HATADOSYAACILAMADI)
    {
        fl.l_type = F_RDLCK;
     	if ((fd = open(lPath, O_RDONLY)) == -1) {
            int myerr = errno;
    		EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasi read only olarak acilamadi!").arg(lPath));
            openHataYaz(myerr);
    		mSonHata = HATADOSYAACILAMADI;
    	} 
        else if (fcntl(fd, F_SETLK, &fl) == -1) {
    		EFILELOCKERERRORYAZ(QString("_lock(): \"%1\" dosyasi kullanimda, read lock konamadi!").arg(lPath));
    		mSonHata = HATADOSYAKULLANIMDA;
    	} 
    	else
    	{
            mDurum = KILITOKUMA;
    		EFILELOCKERDEBUGYAZ(QString("_lock(): \"%1\" dosyasi read locklandi!").arg(lPath));
            mSonHata = HATAYOK;
    	}
    }
	   



	//Son hataya bakarak handle alinmis mi anlayalim.
	if(mSonHata == HATAYOK)
	{
		mFileDescriptor = fd;
		return true;
	} // if(mSonHata == HATAYOK)
	else
	{
		//Eger hata ile karsilasildiysa, acilmis olan dosyayi kapayalim.
		if(fd > 0)
			close(fd);
		return false;
	}

	EFL_FUNC_END;
}



bool EFileLocker::_unlock()
{
	EFL_FUNC_BEGIN;

	if ( close(mFileDescriptor) )
	{
		mSonHata = HATADOSYAKAPATILAMADI;
		return false;
	} // if ( close(mFileDescriptor) ) 

	return true;

	EFL_FUNC_END;
}
#endif

NAMESPACE_END

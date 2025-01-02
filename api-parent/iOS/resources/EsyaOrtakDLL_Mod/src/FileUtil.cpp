#include "FileUtil.h"
#include <QFile>
#include "EException.h"
#include "ELogger.h"
#include "EAyarlar.h"
#include "EAyarTanimlari.h"
#include "EsyaOrtak_Ortak.h"
#include "EAyarAlici.h"
#include "EOrtamDegiskeni.h"
#include "ESingFilterManager.h"
#include <QDesktopServices>

#if defined(WIN32)
#include <shlobj.h>
#include <string.h>
#include <locale.h>
#include <Windows.h>
#include "winbase.h"
#include <QMutexLocker>
#include <QSysInfo>
#include <errno.h>

#else
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <sys/types.h>
//#include <stat-time.h>
#endif
#include <QDir>


NAMESPACE_BEGIN(esya)

QMutex FileUtil::mTmpFileCreateMutex;

FileUtil::FileUtil(void)
{
}

FileUtil::~FileUtil(void)
{
}

/***
*  Dosyanýn adýna ( uzantýlarýna ) bakarak tipini bulur.
*/
FileUtil::FileType FileUtil::getFileTypeByName(const  QString &iFileName)
{
	if (QFileInfo(iFileName).isDir())
	{							
		return getDirType(iFileName,false);
	}
	else 
		return getEntryTypeByNameIcon(iFileName);
/*
	if( RX_SIGNED.exactMatch(iFileName) )
		return FT_Signed;

	else if( RX_ENCRYPTED.exactMatch(iFileName) )
		return FT_Encrypted;

	else if( RX_SIGN_ENC.exactMatch(iFileName) )
		return FT_SignAndEncrypted;

	else if( RX_PROTECTED.exactMatch(iFileName) )
		return FT_Protected;	

	else if( RX_KERMEN_PROTECTED.exactMatch(iFileName) )
		return FT_KermenProtected;						

	else 
		if (QFileInfo(iFileName).isDir())
		{							
			return getDirType(iFileName,false);
		}
		return FT_OtherFile;*/
}

FileUtil::SelectionType FileUtil::getSelectionType(const QStringList &iFiles,const QStringList &iFolders)
{
	if (iFiles.size()==0 && iFolders.size()==0)
	{	
		return NoSelection;
	}

	foreach(QString lFileName,iFiles)
	{
		FileUtil::FileType lFileType = getFileTypeByName(lFileName);
		if (lFileType != FileUtil::FT_OtherFile)
		{
			if (lFileType == FileUtil::FT_Signed)
			{
				return SignedFile;
			}
			if (lFileType == FileUtil::FT_Encrypted)
			{
				return EncryptedFile;
			}
			if (lFileType == FileUtil::FT_SignAndEncrypted)
			{
				return SignEncryptFile;
			}
			if (lFileType == FileUtil::FT_Protected)
			{
				return ProtectedFile;
			}
			if (lFileType == FileUtil::FT_KermenProtected)
			{
				return KermenProtectedFile;
			}
		}			
	}
	foreach(QString lFolderName,iFolders)
	{
		FileUtil::FileType lFileType = getFileTypeByName(lFolderName);
		if (lFileType != FileUtil::FT_OtherFile)
		{
			if (lFileType == FileUtil::FT_Signed)
			{
				return SignedFile;
			}
			if (lFileType == FileUtil::FT_Encrypted)
			{
				return EncryptedFile;
			}
			if (lFileType == FileUtil::FT_SignAndEncrypted)
			{
				return SignEncryptFile;
			}
			if (lFileType == FileUtil::FT_Protected)
			{
				return ProtectedFile;
			}
			if (lFileType == FileUtil::FT_KermenProtected)
			{
				return KermenProtectedFile;
			}
		}			
	}
	return OtherFile;
}


FILE* FileUtil::unicodeFILEPtrOlustur(const QString& iFileName, const QString & iOpenMode,bool iLockFile)
{
	FILE * fp = NULL;

#if defined(WIN32)

	QString stMode(iOpenMode);
	const wchar_t* wbytes = (const wchar_t*)iFileName.utf16();
	const wchar_t* wmode = (const wchar_t*)stMode.utf16();
		
	int res = -1;

	if (!iLockFile)
	{
		fp = _wfsopen(wbytes,wmode,SH_DENYNO);// Okumak için açıyorsak locklamadan açalım
		if (fp == NULL)
		{
			errno_t err;
			_get_errno(&err);

			ERRORLOGYAZ(ESYAORTAK_MOD,QString("DOSYA ADI: %1 - HATA KODU: %2").arg(iFileName).arg(err));
		}
		return fp;
	}
	else 
	{
		res = _wfopen_s(&fp,wbytes,wmode);
		if (res != 0)
			fp= NULL;
	}
#else
	fp = fopen(iFileName.toUtf8().constData(),"rb");
#endif

	return fp;
}


qint64 FileUtil::writeToFile(QFile & oFile, const QByteArray &iData)
{
	OR_FUNC_BEGIN

		// Açýk dosya verilmiþse direk þu anki yerden yazmaya baþlayalým;  dosya açýk deðilse açalým
		if (!oFile.isOpen() || !oFile.open(QIODevice::WriteOnly))
			return -1;
	qint64 fileSize = oFile.write((char*)iData.constData(), iData.length());
	oFile.close();	
	return fileSize;
	
	OR_FUNC_END
}

qint64 FileUtil::writeToFile(const QString &iFileName, const QByteArray &iData)
{
	OR_FUNC_BEGIN

	QFile file(iFileName);
	if (!file.open(QIODevice::WriteOnly))
		return -1;
	qint64 fileSize = file.write((char*)iData.constData(), iData.length());
	file.close();
	
	return fileSize;
	OR_FUNC_END
	
}

void FileUtil::appendToFile(const QString &iFileName, const QByteArray &iData)
{
	OR_FUNC_BEGIN

	QFile file(iFileName);
	if (!file.open(QIODevice::Append))
		throw EException(QString("Dosya append icin acilamadi. DosyaAdi: %1").arg(iFileName));
	
	file.write((char*)iData.constData(), iData.length());
	file.close();

	OR_FUNC_END
}

QByteArray FileUtil::readFromFile(const QString &iFileName)
{
	OR_FUNC_BEGIN

		QFile file(iFileName);
	if (!file.open(QIODevice::ReadOnly))
	{
		throw EException(QString("%1 adli dosya bulunamadi!").arg(iFileName),__FILE__, __LINE__);
	}
	QByteArray data = file.readAll();
	file.close();

	DEBUGLOGYAZ(ESYAORTAK_MOD,"readFromFile() Tamamlandı");
	return data;
	OR_FUNC_END	
}

bool FileUtil::folderCompare(const QString& iLFolder, const QString& iRFolder)
{
	QDir dR(iRFolder), dL(iLFolder);
	if (!dR.exists() || !dL.exists())
		return false;

	QStringList stRList =dR.entryList(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);
	QStringList stLList =dL.entryList(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);

	if (stRList!= stLList)
		return false;

	QFileInfoList rFIList =dR.entryInfoList(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);
	QFileInfoList lFIList =dL.entryInfoList(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);

	bool res= false;
	for (int i = 0; i<rFIList.size(); i++)
	{
		if (rFIList[i].isFile())
		{		
			res = FileUtil::fileCompare(rFIList[i].absoluteFilePath(),lFIList[i].absoluteFilePath());
			if (!res)
				return false;
		}
		else if ( rFIList[i].isDir())
		{
			res = FileUtil::folderCompare(rFIList[i].absoluteFilePath(),lFIList[i].absoluteFilePath());
				if (!res)
					return false;
		}
	}
	return true;

}

bool  FileUtil::fileCompare(const QString & iLFile, const QString & iRFile )
{
#define CLOSE_FILES(x)\
	{\
	lFile.close();\
	rFile.close();\
	return x;\
}

	OR_FUNC_BEGIN	

	QFile lFile(iLFile) , rFile(iRFile);

	if (! ( lFile.open(QIODevice::ReadOnly) && rFile.open(QIODevice::ReadOnly) ) )
		throw EException(QString("Dosya okunmak için açýlamadý. DosyaAdi: %1").arg(iLFile));

	while ( !( rFile.atEnd() || lFile.atEnd()) )
	{
		if ( rFile.read(BLOCK_SIZE) != lFile.read(BLOCK_SIZE) )
			CLOSE_FILES(false)
	}
	bool res = (rFile.atEnd() && lFile.atEnd());
	CLOSE_FILES(res);

	OR_FUNC_END
}

bool FileUtil::_fileCopyWithSecAtt(const QString & iKaynakDosya, const QString & iHedefDosya)
{
	QFile destFile(iHedefDosya);
	QFile sourceFile(iKaynakDosya);
	sourceFile.open(QIODevice::ReadOnly);
	destFile.open(QIODevice::WriteOnly);
	QByteArray data;
	while (!sourceFile.atEnd())
	{
		data=sourceFile.read(BLOCK_SIZE);		
		if(destFile.write(data)<0)
		{
			destFile.close();
			sourceFile.close();
			return false;
		}
	}
	destFile.close();
	sourceFile.close();
	return true;
}

bool FileUtil::fileCopy(const QString & iKaynakDosya, const QString & iHedefDosya,const bool & iOverWriting,const bool & iSecAttributeAktar/* =true */)
{
	if(QDir::fromNativeSeparators(iKaynakDosya) == QDir::fromNativeSeparators(iHedefDosya))
	{//Hedef ile kaynak dosya aynı kopyalama işlemi yapılmıyor.
		return true;
	}	
	QFile hFile(iHedefDosya);
	if (!iOverWriting && QFile::exists(iHedefDosya))
	{
		return false;
	}
	if (iOverWriting && QFile::exists(iHedefDosya))
	{
		removeReadOnly(iHedefDosya);		
	}
	#ifdef WIN32
		int res = CopyFile((LPTSTR)iKaynakDosya.utf16(),(LPTSTR)iHedefDosya.utf16(),false);
		if (res == 0)
		{
			ESYA_ORTAK_FUNC_ERROR(QString("Dosya Kopyalanırken hata oluştu. CopyFile() Hata:%1 ").arg(GetLastError()));
		}
		return (res != 0);
	#else
		//Linux altında nasıl çalışacak
		return _fileCopyWithSecAtt(iKaynakDosya,iHedefDosya);
	#endif			
}

QString FileUtil::getExtensionOf(const FileUtil::FileType& iFileType)
{
	switch(iFileType)
	{
	case FT_Signed				: return FILE_EXT_SIGNED;			
	case FT_Encrypted			: return FILE_EXT_ENCRYPTED;			
	case FT_SignAndEncrypted	: return FILE_EXT_SIGN_ENC;			
	case FT_Protected			: return FILE_EXT_PROTECTED;			
	case FT_KermenProtected		: return FILE_EXT_KERMEN_PROTECTED;			
	default						: return QString();
	}
}

void FileUtil::addExtension(const QString& iExtension, QString & oFileName,bool iAddToEnd)
{
	OR_FUNC_BEGIN

	// Eklenecek uzantý deðeri boþ ise bir þey yapmaya gerek yok.
	if ( iExtension.isEmpty() ) return;  

	int i = 0;

	// dosya adý boþ veya ilk karakteri "." olamaz. !!! 
	if (oFileName.isEmpty() || ((i = oFileName.lastIndexOf(".")) == 0 )) throw EException(QString("Geçersiz Dosya Adý :%1").arg(oFileName));

	if (i < 0 || iAddToEnd ) i = oFileName.size();

	oFileName.insert(i,QString(".%1").arg(iExtension));

	OR_FUNC_END
}


void FileUtil::removeExtension(const QString& iExtension, QString & oFileName)
{
	OR_FUNC_BEGIN

		int extLen = iExtension.size();
	if ( extLen <= 0 || ! RX_FILE_EXT_OF(iExtension).exactMatch(oFileName)) return;

	int i = oFileName.lastIndexOf(iExtension,-1,Qt::CaseInsensitive);

	oFileName.remove(i-1,extLen+1);

	OR_FUNC_END
	return ;
}


/***                                                                    
*	belirtlien dizinde bir geçici dosya adý döner.
************************************************************************/
QString FileUtil::createTempFileName(const QString & iHedefDizin)
{
	#define TEMP_FILE_NAME "TEMP_"

	QMutexLocker mutexLocker(&FileUtil::mTmpFileCreateMutex);

	QDir fDir(iHedefDizin);

	if (!fDir.exists())
		throw EException(QString("Geçersiz dizin : %1").arg(iHedefDizin));

	QString tempFileName, fileName = TEMP_FILE_NAME;

	QString path;

	for(int i = 0 ; ; i++ )
	{
		tempFileName = QString("%1%2").arg(fileName).arg(i);
		path =QString("%1/%2").arg(fDir.absolutePath()).arg(tempFileName);

		if (fDir.exists(path)) continue;

		QFile f(path);
		if (f.open(QIODevice::WriteOnly))
		{
			f.close();
			break;
		}
	}
	return path;
}

/***                                                                    
*	temp dizininde geçici bir dosya adý döner.
************************************************************************/
QString FileUtil::createTempFileName()
{		
	QString geciciDizinAdi="";
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_ISLEMOZELLIKLERI,AYAR_ISLEMOZELLIKLERI_TEMPPATH);
		EAyar ayar = ayarAlici.ayarBul();
		geciciDizinAdi = ayar.getStringDeger();
	}
	catch (EException &exc)
	{
		ESYA_ORTAK_FUNC_ERROR("Ayarlardan Temp dizin yolu okunmaya çalışılırken hata oluştu"+exc.printStackTrace());	
	}
	if (geciciDizinAdi.isEmpty())
	{
		geciciDizinAdi=QDir::tempPath();
	}
	geciciDizinAdi = EOrtamDegiskeni::degiskenliStrCoz(geciciDizinAdi);
	QString retTempFileName = createTempFileName(geciciDizinAdi);
	return retTempFileName;
}



/***                                                                    
*	belirtilen dosyayla ayný dizinde bir geçici dosyaya 
belirtilen miktarda bytes yazmaya çalýþýr.
bir yazma hatasý alýrsa false döner.Almazsa true döner.
geri dönerken geçici dosyayý siler.
************************************************************************/
bool FileUtil::checkDiskSpace(const QString & iHedefDosya, qint64 iSize)
{
	OR_FUNC_BEGIN

	QFileInfo fi(iHedefDosya);
	QString dizinAdi = fi.absolutePath();

	double totalSpace, availableSpace;
	
	bool b = getFreeTotalSpace(dizinAdi,totalSpace,availableSpace);
	
	if (!b)
	{
		ESYA_ORTAK_FUNC_ERROR("Yeterli disk alanı hesaplanırken hata oluştu")	
		return false;
	}
	return (iSize <= availableSpace);
	
	OR_FUNC_END	
}


bool FileUtil::deleteFile(const QString & iFilePath,bool iRemoveReadOnlyAttr)
{
	OR_FUNC_BEGIN

	if (iRemoveReadOnlyAttr)
	{
		try
		{
			FileUtil::removeReadOnly(iFilePath);
		}
		catch (EException& exc)
		{				
		}
	}
	#ifdef WIN32	
	if(QSysInfo::windowsVersion()>QSysInfo::WV_2003)
	{
		QString destktopPath = QDesktopServices::storageLocation(QDesktopServices::DesktopLocation);		
		destktopPath = QDir::fromNativeSeparators(destktopPath);
		QString fileDirPath =  QFileInfo(iFilePath).absolutePath();
		fileDirPath = QDir::fromNativeSeparators(fileDirPath);
		if(fileDirPath == destktopPath)
		{
			QString lEntryPath = iFilePath;
			SHFILEOPSTRUCT fileOpt;
			ZeroMemory(&fileOpt,sizeof(SHFILEOPSTRUCT));
			fileOpt.hwnd = NULL;
			fileOpt.wFunc = FO_DELETE;
			fileOpt.pTo = (LPCWSTR)"\0\0";	
			fileOpt.fFlags = FOF_NOCONFIRMATION | FOF_SILENT;
			fileOpt.fAnyOperationsAborted = true; 
			fileOpt.hNameMappings = NULL; 
			fileOpt.lpszProgressTitle =(LPCWSTR)"";
			lEntryPath.replace("/","\\");
			TCHAR * lChar;
			lChar=(TCHAR *)malloc(sizeof(TCHAR*) * (lEntryPath.size()+1));
			TCHAR*   pCurrPos=lChar;
			wcscpy (pCurrPos, (LPCTSTR) lEntryPath.utf16());	
			pCurrPos = (wchar_t *)(wcschr ( lChar, '\0' )+1);
			*pCurrPos = '\0';   
			fileOpt.pFrom =(LPCWSTR)lChar;
			if(SHFileOperation(&fileOpt)!= 0)
			{ 
				int hata = GetLastError();			
				ESYA_ORTAK_FUNC_ERROR(QString("Dosya = %1,SHFileOperation () FILE_DELETE_ERROR Error=%2").arg(lEntryPath).arg(hata));
			}		
		}
		else
		{
			QFile::remove(iFilePath);
		}
	}
	else
	{
		QFile::remove(iFilePath);
	}
	#else
		QFile::remove(iFilePath);
	#endif
	return true;

	OR_FUNC_END
}

/**
 * Verilen dizini alt dizin ve dosyalarla birlikte siler.
 * Eğer isteniyorsa için de olan readonly dosyaların readonly özelliğini kaldırıp onları da siler
 * \param iFolderPath 
 * Silinecek dizin yolu
 * \param iRemoveReadOnlyAttr/* 
 * Readonly özelliğinin kaldırılıp kaldırılmayacağı
 * \return 
 */
bool FileUtil::deleteFolder(const QString & iFolderPath,bool iRemoveReadOnlyAttr/* =true */)
{
	ESYA_ORTAK_FUNC_BEGIN;	
	QFileInfo lFileInfo(iFolderPath);
	if (!lFileInfo.isDir())
	{
		ESYA_ORTAK_FUNC_END;
		return false;
	}
	QDir lDir(iFolderPath);
	lDir.setFilter(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);
	lDir.setSorting(QDir::Name | QDir::DirsFirst);
	QFileInfoList lInfoList = lDir.entryInfoList();

	// Dizinin içinde eleman varsa önce onlarý silelim
	foreach(QFileInfo lEntryInfo,lInfoList)
	{
		if (lEntryInfo.isDir())
		{
			FileUtil::deleteFolder(lEntryInfo.absoluteFilePath());
		}
		else
		{
			FileUtil::deleteFile(lEntryInfo.absoluteFilePath(),iRemoveReadOnlyAttr);			
		}
	}	
	if (iRemoveReadOnlyAttr)
	{
		try
		{
			FileUtil::removeReadOnly(lDir.absolutePath());
		}
		catch (EException& exc)
		{				
		}
	}	

	DEBUGLOGYAZ(ESYAORTAK_MOD,QString("Dizin Siliniyor: %1").arg(iFolderPath));
	if (!lDir.rmdir(iFolderPath))
	{
		ERRORLOGYAZ(ESYAORTAK_MOD,QString("Dizin Silinemedi: %1").arg(iFolderPath));
	}
	
	ESYA_ORTAK_FUNC_END;
	return true;	
}

/**
 * Bir dizini farklı bir yere kopyalamak için kullanılır.
 * \param iSourceFolder 
 * Taşınacak olan dizin
 * \param iToFolder 
 * Dizinin taşınacağı yeni dizin
 * \param iIsOverWriting 
 * Dizin varsa üstüne yazılıp yazılmayacağı
 * \return 
 * Hata durumunu döner
 */
bool FileUtil::copyFolder(const QString & iSourceFolder,const QString & iToFolder,bool iIsOverWriting)
{
	ESYA_ORTAK_FUNC_BEGIN;

	QDir lHedefDir(iToFolder);
	QDir lKaynakDir(iSourceFolder);
	QString lOlusacakDizinAdi =QDir(iSourceFolder).dirName();

	// HedefDizin yoksa kopyalama iþlemi geçersizdir
	if (!lHedefDir.exists())
	{
		ESYA_ORTAK_FUNC_END;
		return false;
	}	

	// Hedef dizinde böyle bir klasör zaten varsa üzerine yazabileceksek silelim
	if (lHedefDir.exists(lOlusacakDizinAdi))		
	{
		if(iIsOverWriting)
		{
			deleteFolder(iToFolder+"/"+lOlusacakDizinAdi);
		}
		else
		{
			ESYA_ORTAK_FUNC_END;
			return false;
		}
	}			

	lHedefDir.mkdir(lOlusacakDizinAdi);
	lKaynakDir.setFilter(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot);
	lKaynakDir.setSorting(QDir::Name | QDir::DirsFirst);
	QFileInfoList lInfoList = lKaynakDir.entryInfoList();

	//Kaynak Dizininin içindeki elemanlarý kopyalayalým.
	foreach(QFileInfo lEntryInfo,lInfoList)
	{
		if (lEntryInfo.isDir())
		{							
			FileUtil::copyFolder(lEntryInfo.absoluteFilePath(),iToFolder+"/"+lOlusacakDizinAdi,iIsOverWriting);
		}
		else
		{	
			FileUtil::fileCopy(lEntryInfo.absoluteFilePath(),iToFolder+"/"+lOlusacakDizinAdi+"/"+lEntryInfo.fileName(),iIsOverWriting);
		}
	}		
	ESYA_ORTAK_FUNC_END;
	// Kopyalama tamamlandý
	return true;
}

/**
 * Dizini başka dizin altına taşımak için kullanılır.
 * \param iSourceFolder 
 * Taşınacak dizin
 * \param iToFolder 
 * Dizinin taşınacağı hedef dizin
 * \param iIsOverWriting 
 * Üstüne yazma işleminin yapılıp yapılmayacağı
 * \return 
 * Hata oluşmamışsa true, hata oluşmuşssa false döner.
 */
bool FileUtil::moveFolder(const QString & iSourceFolder,const QString & iToFolder,bool iIsOverWriting)
{
	ESYA_ORTAK_FUNC_BEGIN;
	//Kaynak dizini kopyalama iþlemi baþarýlý olursa siliyoruz.
	bool lRet=FileUtil::copyFolder(iSourceFolder,iToFolder,iIsOverWriting);
	if (!lRet)
	{
		ESYA_ORTAK_FUNC_ERROR("Dizinin taşınmasında ilk aşama olan kopyalama aşamasında sorun oluştu")
		return false;
	}
	bool retBoolValue = deleteFolder(iSourceFolder);
	ESYA_ORTAK_FUNC_END;
	return retBoolValue;	
}

#ifndef WIN32

int fd_reopen (int desired_fd, char const *file, int flags, mode_t mode)
{
  int fd;

  close (desired_fd);
  fd = open (file, flags, mode);
  if (fd == desired_fd || fd < 0)
    return fd;
  else
    {
      int fd2 = fcntl (fd, F_DUPFD, desired_fd);      
      close (fd);      
      return fd2;
    }
}

#endif

void FileUtil::transferFileTimeAttributes(const QString& iSourceFile,const QString & iDestFile)
{

	if (iSourceFile == iDestFile)
	{
		return;
	}
	#if defined(WIN32)
	WIN32_FILE_ATTRIBUTE_DATA *wfad;
	wfad = (WIN32_FILE_ATTRIBUTE_DATA *)malloc(sizeof(WIN32_FILE_ATTRIBUTE_DATA));
	memset(wfad,0,sizeof(WIN32_FILE_ATTRIBUTE_DATA));

	if(!GetFileAttributesEx( (LPCWSTR)iSourceFile.utf16() , GetFileExInfoStandard, (void *)wfad))
	{
		//Dosya özellikleri alýnýrken hata olustu
		if(wfad)
			free(wfad);
		throw EException(QString("Kaynak Dosya Acilamadi. DosyaAdi: %1 ").arg(iSourceFile),__FILE__,__LINE__);
	}
	//Sonraki oluþan dosyaya özellikler aktarýlýyor
	HANDLE hFile = CreateFile((LPCWSTR)iDestFile.utf16(),GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
	if (hFile == INVALID_HANDLE_VALUE) 
	{ 
		//Dosyaya handle alýnýrken hata olustu
		if(wfad)
			free(wfad);
		throw EException(QString("Hedef Dosya Acilamadi : DosyaAdi: %1").arg(iDestFile),__FILE__,__LINE__);
	}
	else
	{
		if(!SetFileTime(hFile,&(wfad->ftCreationTime),&(wfad->ftLastAccessTime), &(wfad->ftLastWriteTime)))
		{
			//Dosyaya eski özellikleri koyulurken hata oluþtu
			if(wfad)
				free(wfad);

			CloseHandle(hFile);
			throw EException(QString("Hedef Dosya Acilamadi : DosyaAdi: %1").arg(iDestFile),__FILE__,__LINE__);
		}
		CloseHandle(hFile);
	}

	if(wfad)
		free(wfad);
#else
   struct stat lRetFileStat;
   int lRetCode = stat(iSourceFile.toUtf8().data(), &lRetFileStat);
  if(lRetCode == -1)
  {  
      throw EException("Kaynak dosyadan bilgiler alınırken hata olustu",__FILE__,__LINE__);
  }
     int fd = -1;
     fd = fd_reopen (STDIN_FILENO, iDestFile.toUtf8().data(),
		      O_WRONLY | O_CREAT | O_NONBLOCK | O_NOCTTY,
		      S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH);
      if (fd == -1)
    {
      throw EException("Hedef dosya açılırken hata olustu",__FILE__,__LINE__);
    }

   /*struct timespec timespec[2];
   struct timespec const *t;
   timespec[0] = get_stat_atime (&lRetFileStat);
   timespec[1] = get_stat_mtime (&lRetFileStat);
   t = timespec;
    bool ok;
     ok = (futimens (fd,t) == 0);
    if(!ok)
    {
      throw EException("Hedef dosyaya özellikler aktarılırken hata olustu",__FILE__,__LINE__);
    }
    */
#endif
}



void FileUtil::transferFileAttributes(const QString& iSourceFile,const QString & iDestFile)
{
#if defined(WIN32)

	DWORD fileAttributes =  GetFileAttributesW((LPCWSTR)iSourceFile.utf16());

	bool lSonuc = SetFileAttributesW((LPCWSTR)iDestFile.utf16(),fileAttributes);


#endif 
}


#if defined(WIN32)
//Buralarda log yazamiyorum. Yazmaya kalkarsam static initialization esnasinda
//bu fonksiyonlar cagrildigindan log yazmada hata ile karsilabiliyorum.
QString FileUtil::genelAyarPath()
{	
	if (QFile::exists(KERMEN_GENEL_AYARLAR_FILE_NAME))
	{
		return "."; 
	}
	TCHAR appdataSt[MAX_PATH];
	if(! SUCCEEDED(SHGetFolderPath(NULL, 
		CSIDL_COMMON_APPDATA, 
		NULL, 
		SHGFP_TYPE_CURRENT,
		appdataSt))) 
	{
		//AYARERRORLOGYAZ("COMMON_APPDATA dizininin degeri alinamadi!");
		appdataSt[0]=0;
	}
	QString appdata ( (QChar *)appdataSt,wcslen(appdataSt));
	appdata += "/UEKAE/ESYA";

	return appdata;
}

QString FileUtil::yerelAyarPath()
{	
	if (QFile::exists(KERMEN_GENEL_AYARLAR_FILE_NAME))
	{
		return "."; 
	}
	TCHAR appdataSt[MAX_PATH];
	if(!SUCCEEDED(SHGetFolderPath(NULL, 
		CSIDL_APPDATA | CSIDL_FLAG_CREATE, 
		NULL, 
		0, 
		appdataSt))) 
	{
		//AYARERRORLOGYAZ("APPDATA dizininin degeri alinamadi!");
		appdataSt[0]=0;
	}

	QString appdata ( (QChar *)appdataSt,wcslen(appdataSt));
	QString appdataDir="UEKAE/ESYA";
	QDir dizin(appdata);
	dizin.mkpath(appdataDir);

	appdata += ("/"+appdataDir);
	return appdata;
}
#else
QString FileUtil::genelAyarPath()
{
	QString genelAyarAramaPath = QDir::currentPath()+"/"+KERMEN_GENEL_AYARLAR_FILE_NAME;
	if (QFile::exists(genelAyarAramaPath))
	{
		return QDir::currentPath(); 
	}
	return ("/etc/xdg/UEKAE/ESYA");
}
QString FileUtil::yerelAyarPath()
{
	if (QFile::exists(KERMEN_GENEL_AYARLAR_FILE_NAME))
	{
		return "."; 
	}
	QString appdata = QDir::homePath();
	QDir dizin = QDir::home();
	QString appdataDir = ".config/UEKAE/ESYA";
	dizin.mkpath(appdataDir);
	return appdata+"/"+appdataDir;
}
#endif

/**
* Dizin imzali mi þifreli mi yoksa normal bir dizin mi olup olmadýðý bilgisini döner  
*/
FileUtil::FileType FileUtil::getDirType(const QString & iEntryPath,bool iAltDizinKontrol)
{	
	if(iEntryPath.size()< 4) 
	{//Sistem sürücüleri için
		return FileUtil::FT_OtherFile;
	}					

	//QFileInfoList lInfoList = getDirEntryInfoList(iEntryPath);
	QFileInfoList lInfoList = QDir(iEntryPath).entryInfoList();
	foreach(QFileInfo lFileInfo,lInfoList)
	{			
		QString lFileName = lFileInfo.fileName();
		if (lFileName.indexOf(".lnk")!=-1)
		{
			continue;
		}
		if (QFileInfo(lFileInfo.absoluteFilePath()).isFile())
		{				
			FileUtil::FileType lFileType = FileUtil::getFileTypeByName(lFileInfo.absoluteFilePath());			
			if (lFileType != FileUtil::FT_OtherFile)
			{					
				return lFileType;
			}
		}
		else
			if (lFileInfo.isDir())
			{					
				if(iAltDizinKontrol)
				{			
					FileUtil::FileType lFileType = getDirType(lFileInfo.absoluteFilePath(),iAltDizinKontrol);
					if (lFileType != FileUtil::FT_OtherFile)
					{
						return lFileType;
					}
				}		
			}
	}	
	return FileUtil::FT_OtherFile;
}

QFileInfoList FileUtil::getDirEntryInfoList(const QString & iDirPath)
{	
#ifdef WIN32
	QFileInfoList lRetInfoList;
	HANDLE ff;
	WIN32_FIND_DATA finfo;
	QFileInfo fi;
	bool first=true; 
	QString lDirPath=QString("%1%2%3").arg("\\\\?\\").arg(iDirPath).arg("\\*.*");
	/*
	TCHAR * lChar=(TCHAR *)malloc(sizeof(TCHAR*) * (iDirPath.size()+1));
	TCHAR*   pCurrPos=lChar;
	wcscpy (pCurrPos, (LPCTSTR) lDirPath.utf16());	
	pCurrPos = (wchar_t *)(wcschr ( lChar, '\0' )+1);
	*pCurrPos = '\0';   //En sonunda 2 tane boþ karakter olmalý . 64 00 00 00 00 00 þekilde
	*/
	LPCWSTR lDirName = (LPCTSTR) lDirPath.utf16(); 
	ff = FindFirstFile(lDirName, &finfo);
	QString fname;
	fname = QString::fromUtf16((unsigned short *)finfo.cFileName);	 
	if (fname!="." && fname!="..")
	{
		lRetInfoList.append(QFileInfo(iDirPath+"/"+fname));
	}	
	while (FindNextFile(ff, &finfo) != 0) 
	{	 
		fname = QString::fromUtf16((unsigned short *)finfo.cFileName);		 
		if (fname!="." && fname!="..")
		{
			lRetInfoList.append(QFileInfo(iDirPath+"/"+fname));
		}	
	}	
	FindClose(ff);							   
	return lRetInfoList;
#endif
	return QDir(iDirPath).entryInfoList();
}			 

bool FileUtil::isReadOnly(const QString & iFileName)
{
	DEBUGLOGYAZ("FileUtil",QString("isReadOnly(%1)-GIRIS").arg(iFileName));
#if defined(WIN32)
	DWORD dwAttrs = GetFileAttributesW((LPCWSTR)iFileName.utf16());
	if (dwAttrs==INVALID_FILE_ATTRIBUTES)
	{		
		int err = GetLastError();
		ERRORLOGYAZ("FileUtil",QString("isReadOnly(%1)-HATA = %2").arg(iFileName).arg(err));
		return false;
	}
	if (dwAttrs & FILE_ATTRIBUTE_READONLY) 
	{ 
		return true;
	} 		
#else
#endif
	DEBUGLOGYAZ("FileUtil",QString("isReadOnly(%1)-TAMAMLANDI").arg(iFileName));
	return false;
}

void FileUtil::setAsReadOnly(const QString & iFileName)
{
	DEBUGLOGYAZ("FileUtil",QString("setAsReadOnly(%1)-GIRIS").arg(iFileName));
#if defined(WIN32)

	int res = SetFileAttributesW((LPCWSTR)iFileName.utf16(),FILE_ATTRIBUTE_READONLY );

	if (!res)
	{
		int err = GetLastError();
		ERRORLOGYAZ("FileUtil",QString("setAsReadOnly(%1)-HATA = %2").arg(iFileName).arg(err));
		throw EException(QString(" FileUtil::setAsReadOnly() File Attribute atanamadı Hata:%1").arg(err),__FILE__,__LINE__);
	}
#else
#endif
	DEBUGLOGYAZ("FileUtil",QString("setAsReadOnly(%1)-TAMAMLANDI").arg(iFileName));
	return;
}

void FileUtil::removeReadOnly(const QString & iFileName)
{
	DEBUGLOGYAZ("FileUtil",QString("removeReadOnly(%1)-GIRIS").arg(iFileName));
#if defined(WIN32)

	int res = SetFileAttributesW((LPCWSTR)iFileName.utf16(),FILE_ATTRIBUTE_NORMAL );
	
	if (!res)
	{
		int err = GetLastError();
		ERRORLOGYAZ("FileUtil",QString("removeReadOnly(%1)-HATA = %2").arg(iFileName).arg(err));
		throw EException(QString(" FileUtil::removeReadOnly() File Attribute atanamadı Hata:%1").arg(err),__FILE__,__LINE__);
	}
#else
#endif
	DEBUGLOGYAZ("FileUtil",QString("removeReadOnly(%1)-TAMAMLANDI").arg(iFileName));
	return;
}

#ifdef WIN32
QString FileUtil::getWinSystem32Path()
{
	#define INFO_BUFFER_SIZE 32767
	TCHAR  infoBuf[INFO_BUFFER_SIZE];
	if( !GetSystemDirectory( infoBuf, INFO_BUFFER_SIZE ) )
	{
		return "";
	}
	return QString::fromUtf16((const ushort *)infoBuf);
}

QString FileUtil::getWindowsDir()
{
#define INFO_BUFFER_SIZE 32767
	TCHAR  infoBuf[INFO_BUFFER_SIZE];
	if( !GetWindowsDirectory( infoBuf, INFO_BUFFER_SIZE ) )
	{
		return "";
	}
	return QString::fromUtf16((const ushort *)infoBuf);
}
#endif




/**
* \brief
* Dosyanın imzalı olup olmadığını döner
*
* \return   	bool
* true : imzalı false : imzasız
*/
bool FileUtil::imzaliDosyaMi(const QString & iFileName)  
{
	return ( baslikOku(iFileName).indexOf(FileUtil_IMZALI_OID) >= 0  );
}

/**
* \brief
* Dosyanın şifreli olup olmadığını döner
*
* \return   	bool
* true : şifreli false : şifresiz
*/
bool FileUtil::sifreliDosyaMi(const QString & iFileName)  
{
	QByteArray baslik = baslikOku(iFileName);
	return ( baslik.indexOf(FileUtil_SIFRELI_OID) >= 0 );
}

/**
* \brief
* Dosyanın boş olup olmadığını döner
*
* \return   	bool
* true : boş  false : boş olmayan
*/
bool FileUtil::bosDosyaMi(const QString &iFileName)
{
	QFileInfo	fileInfo(iFileName);	
	return fileInfo.size()==0;
}

/**
* \brief
* Dosyanın başından belirli bir miktar byte başlık olarak okur
*
* \return   	QT_NAMESPACE::QByteArray
* Başlık değeri
*/
QByteArray FileUtil::baslikOku(const QString & iFileName)
{
	QFile file(iFileName);

	if (! file.open(QIODevice::ReadOnly))
		return QByteArray();

	QByteArray baslik = file.read(FileUtil_BASLIK_BOYU+FileUtil_BASLIK_BOYU); 

	file.close();
	return baslik;
}


#ifdef _WIN32

QString getErrorString(DWORD errCode)
{
	LPVOID lpMsgBuf;
	LPVOID lpDisplayBuf;

	FormatMessage(
		FORMAT_MESSAGE_ALLOCATE_BUFFER | 
		FORMAT_MESSAGE_FROM_SYSTEM |
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		errCode,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
		(LPTSTR) &lpMsgBuf,
		0, NULL );                                                  

	QString errorStr = QString::fromUtf16((const ushort *)lpMsgBuf);
	LocalFree(lpMsgBuf);

	return errorStr;
}

#endif


#ifdef _WIN32
	#include <windows.h>
#else // linux stuff
#include <sys/statvfs.h>
#endif // _WIN32

bool FileUtil::getFreeTotalSpace(const QString& iDirPath,double& ofTotal, double& ofFree)
{
#ifdef _WIN32
	//QString sCurDir = QDir::current().absolutePath();
	//QDir::setCurrent( iDirPath );
	ULARGE_INTEGER free,total;
	wchar_t * path =(wchar_t*) iDirPath.utf16();
	DEBUGLOGYAZ(ESYAORTAK_MOD,QString("Disk Space Check. Address : %1").arg(iDirPath) );
	bool bRes = ::GetDiskFreeSpaceEx(  path , &free , &total , NULL );
	//QDir::setCurrent( sCurDir );
	if ( !bRes )
	{
		DWORD errCode = GetLastError();
		DEBUGLOGYAZ(ESYAORTAK_MOD,QString("GetDiskFreeSpaceExA() fonksiyonu hata verdi. Hata Kodu: %1 Hata: %2").arg(errCode).arg(getErrorString(errCode)));
		return false;
	}

	ofFree = static_cast<double>( static_cast<__int64>(free.QuadPart) ) ;
	ofTotal = static_cast<double>( static_cast<__int64>(total.QuadPart) ) ;
	
	DEBUGLOGYAZ(ESYAORTAK_MOD,QString("Path:%1 Available Disk: %2 Total: %3").arg(iDirPath).arg(ofFree).arg(ofTotal));

#else //linux
	struct statvfs fiData;
	if(statvfs(iDirPath.toLocal8Bit(),&fiData)<0)
	{
		return false;
	}
	else
	{
		ofFree = (double)fiData.f_bavail*(double)fiData.f_bsize;
		ofTotal = (double)fiData.f_blocks*(double)fiData.f_bsize;
	}
#endif // _WIN32
	return true;
}

bool FileUtil::getEntryTypeIsKermenTypeIcon(const  QString &iEntryName)
{	
	if(iEntryName.contains(".m3s",Qt::CaseInsensitive)||
		iEntryName.contains(".m3i",Qt::CaseInsensitive)||
		iEntryName.contains(".mis",Qt::CaseInsensitive)||
		iEntryName.contains(".mpk",Qt::CaseInsensitive)||
		iEntryName.contains(".kpk",Qt::CaseInsensitive)||
		iEntryName.contains(". m3s",Qt::CaseInsensitive)||
		iEntryName.contains(". m3i",Qt::CaseInsensitive)||
		iEntryName.contains(". mis",Qt::CaseInsensitive)||
		iEntryName.contains(". mpk",Qt::CaseInsensitive)||
		iEntryName.contains(". kpk",Qt::CaseInsensitive)
		)
	{
		return true;
	}
	return false;
	/*
	QFileInfo fileInfo(iEntryName);
	if (fileInfo.isFile())
	{
		if( RX_SIGNED.exactMatch(iEntryName) )
			return true;

		else if( RX_ENCRYPTED.exactMatch(iEntryName) )
			return true;

		else if( RX_KERMEN_PROTECTED.exactMatch(iEntryName) )
			return true;

		else if( RX_SIGN_ENC.exactMatch(iEntryName) )
			return true;

		else if( RX_PROTECTED.exactMatch(iEntryName) )
			return true;
	}
	else 
	if (fileInfo.isDir())
	{							
		return getDirTypeIsKermenTypeIcon(iEntryName,false);
	}
	return false;
	*/
}

bool FileUtil::getDirTypeIsKermenTypeIcon(const  QString &iDirName,bool iAltDizinKontrol)
{
	return false;
	//<TODO> burda bu dizinin Driver dizini olduğunu nasıl ögrenebiliriz.
	if(iDirName.size()< 4) 
	{//Sistem sürücüleri için
		return false;
	}				

	QDir dir(iDirName);			
	QStringList dirKermenFileFilter = ESingFilterManager::getInstance()->getDirSearcKermenFileFilter();
	dir.setNameFilters(dirKermenFileFilter);
	QFileInfoList fileInfoList = dir.entryInfoList(QDir::Files | QDir::NoDotAndDotDot);
	if (!fileInfoList.isEmpty())
	{
		return true;
	}	
	if (iAltDizinKontrol)
	{	
		QFileInfoList fileInfoList = QDir(iDirName).entryInfoList(QDir::Dirs | QDir::NoDotAndDotDot);
		Q_FOREACH(QFileInfo fileInfo,fileInfoList)
		{
			bool retValue = getDirTypeIsKermenTypeIcon(fileInfo.absoluteFilePath(),false);
			if (retValue)
			{
				return true;
			}
		}
	}
	return false;
}

FileUtil::FileType FileUtil::getDirTypeByNameIcon(const  QString &iDirName,bool iAltDizinKontrol)
{		
	return FileUtil::FT_OtherFile;
	if(iDirName.size()< 4) 
	{//Sistem sürücüleri için
		return FileUtil::FT_OtherFile;
	}				

	QDir dir(iDirName);			
	QStringList dirKermenFileFilter = ESingFilterManager::getInstance()->getDirSearcKermenFileFilter();
	dir.setNameFilters(dirKermenFileFilter);
	QFileInfoList fileInfoList = dir.entryInfoList(QDir::Files | QDir::NoDotAndDotDot);
	foreach(QFileInfo fileInfo,fileInfoList)
	{			
		QString lFileName = fileInfo.fileName();
		if (lFileName.indexOf(".lnk")!=-1)
		{
			continue;
		}

		if (fileInfo.isFile())
		{				
			FileUtil::FileType lFileType = getEntryTypeByNameIcon(fileInfo.absoluteFilePath());			
			if (lFileType != FileUtil::FT_OtherFile)
			{					
				return lFileType;
			}
		}
	}
	if (iAltDizinKontrol)
	{	
		QFileInfoList fileInfoList = QDir(iDirName).entryInfoList(QDir::Dirs | QDir::NoDotAndDotDot);
		foreach(QFileInfo fileInfo,fileInfoList)
		{			
			FileUtil::FileType lFileType = getDirTypeByNameIcon(fileInfo.absoluteFilePath(),false);
			if (lFileType != FileUtil::FT_OtherFile)
			{
				return lFileType;			
			}		
		}
	}
	return FileUtil::FT_OtherFile;
}

FileUtil::FileType FileUtil::getEntryTypeByNameIcon(const  QString &iEntryName)
{
	if(iEntryName.contains(".m3s",Qt::CaseInsensitive)||iEntryName.contains(". m3s",Qt::CaseInsensitive))
	{
		return FileUtil::FT_Encrypted;
	}else if(iEntryName.contains(".m3i",Qt::CaseInsensitive)||iEntryName.contains(". m3i",Qt::CaseInsensitive))
	{
		return FileUtil::FT_Signed;
	}else if(iEntryName.contains(".mis",Qt::CaseInsensitive)||iEntryName.contains(". mis",Qt::CaseInsensitive))
	{
		return FileUtil::FT_SignAndEncrypted;
	}else if(iEntryName.contains(".mpk",Qt::CaseInsensitive)||iEntryName.contains(". mpk",Qt::CaseInsensitive))
	{
		return FileUtil::FT_Protected;
	}else if(iEntryName.contains(".kpk",Qt::CaseInsensitive)||iEntryName.contains(". kpk",Qt::CaseInsensitive))
	{
		return FileUtil::FT_KermenProtected;
	}
	return FileUtil::FT_OtherFile;
	
	/*
	QFileInfo fileInfo(iEntryName);
	if (fileInfo.isFile())
	{
		if( RX_SIGNED.exactMatch(iEntryName) )
			return FileUtil::FT_Signed;

		else if( RX_ENCRYPTED.exactMatch(iEntryName) )
			
		else if( RX_KERMEN_PROTECTED.exactMatch(iEntryName) )
			return FileUtil::FT_KermenProtected;

		else if( RX_SIGN_ENC.exactMatch(iEntryName) )
			return FileUtil::FT_SignAndEncrypted;

		else if( RX_PROTECTED.exactMatch(iEntryName) )
			return FileUtil::FT_Protected;						

	}
	else 
		if (fileInfo.isDir())
		{							
			return getDirTypeByNameIcon(iEntryName,false);
		}
		return FileUtil::FT_OtherFile;
	*/
}

bool FileUtil::isTooLongPath(const QString &iPath,int iPathLen)
{
	return (iPath.length()>iPathLen);
}

QStringList FileUtil::getTooLongPaths(const QString &iRootDir,int iPathLen)
{
	QStringList paths;
	QFileInfo root(iRootDir);
	if (!root.exists())
		return paths;
	
	if (!root.isDir())
	{
		if (isTooLongPath(root.absoluteFilePath(),iPathLen))
			paths.append(root.absoluteFilePath());
		return paths;
	}
	else
	{
		QDir rootDir(iRootDir);
		QFileInfoList children = rootDir.entryInfoList(QDir::Files | QDir::Dirs | QDir::NoSymLinks|QDir::NoDotAndDotDot);
		Q_FOREACH(QFileInfo child, children )
			paths += FileUtil::getTooLongPaths(child.absoluteFilePath());
	}
	return paths;
}

NAMESPACE_END

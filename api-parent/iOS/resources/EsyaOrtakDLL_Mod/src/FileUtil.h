#ifndef FILE_UTIL_H____
#define FILE_UTIL_H____

#include "esyaOrtak.h"
#include <QString>
#include <QByteArray>
#include <QStringList>
#include <QFile>
#include <QFileInfoList>
#include <QMutex>

#define FileUtil_BASLIK_BOYU 20
#define FileUtil_OID_BOYU 11
#define FileUtil_IMZALI_OID QByteArray("\x06\x09\x2A\x86\x48\x86\xF7\x0D\x01\x07\x02",FileUtil_OID_BOYU)
#define FileUtil_SIFRELI_OID QByteArray("\x06\x09\x2A\x86\x48\x86\xF7\x0D\x01\x07\x03",FileUtil_OID_BOYU)

NAMESPACE_BEGIN(esya)

//�mzalanm�� ya da �ifrelenmi� dosya tiplerinin belirte�leri //RMZ
#define FILE_EXT_SIGNED			"m3i"
#define FILE_EXT_ENCRYPTED		"m3s"
#define FILE_EXT_SIGN_ENC		"mis"
#define FILE_EXT_PROTECTED		"mpk"
#define FILE_EXT_KERMEN_PROTECTED		"kpk"


#define RX_FILE_EXT_OF(x)		QRegExp(QString(".+(\\.%1)(\\.[^\\.]+)?$").arg(x),Qt::CaseInsensitive)


#define RX_SIGNED			RX_FILE_EXT_OF(FILE_EXT_SIGNED)
#define RX_ENCRYPTED		RX_FILE_EXT_OF(FILE_EXT_ENCRYPTED)
#define RX_SIGN_ENC			RX_FILE_EXT_OF(FILE_EXT_SIGN_ENC)
#define RX_PROTECTED		RX_FILE_EXT_OF(FILE_EXT_PROTECTED)
#define RX_KERMEN_PROTECTED	RX_FILE_EXT_OF(FILE_EXT_KERMEN_PROTECTED)


#define	BLOCK_SIZE 500000


#define MAX_FILEPATH_LEN 251 // 255 - strlen(".mxx")

class  Q_DECL_EXPORT FileUtil
{
	static bool _fileCopyWithSecAtt(const QString & iKaynakDosya, const QString & iHedefDosya);
public:

	enum FileType 
	{
		FT_Signed ,
		FT_Encrypted ,
		FT_SignAndEncrypted,
		FT_Protected ,
		FT_OtherFile ,
		FT_KermenProtected 
	};

	enum SelectionType 
	{
		NoSelection = 0,
		SignedFile =  1,
		EncryptedFile = 2,
		FolderSelection = 3,
		SignEncryptFile = 4,
		ProtectedFile = 5,
		OtherFile = 6,
		KermenProtectedFile=7
	};

	 FileUtil(void);
	 virtual ~FileUtil(void);


	 static QMutex mTmpFileCreateMutex;
	 

	 static bool getEntryTypeIsKermenTypeIcon(const  QString &iEntryName);
	 static bool getDirTypeIsKermenTypeIcon(const  QString &iDirName,bool iAltDizinKontrol);

	 static FileType getEntryTypeByNameIcon(const  QString &iEntryName);
	 static FileType getDirTypeByNameIcon(const  QString &iDirName,bool iAltDizinKontrol);

	 static FileType getFileTypeByName(const  QString &iFiles);

	 static SelectionType getSelectionType(const  QStringList &iFiles,const QStringList &iFolders);

	static FILE* unicodeFILEPtrOlustur(const QString& iFileName , const QString & iOpenMode = "rb",bool iLockFile = true);

	static void appendToFile(const QString &iFileName, const QByteArray &iData);
	static qint64 writeToFile(const QString &iFileName, const QByteArray &iData);
	static qint64 writeToFile(QFile& oFile, const QByteArray &iData);
	static QByteArray readFromFile(const QString &iFileName);
	static bool fileCompare(const QString & iLFile, const QString & iRFile );
	static bool folderCompare(const QString& iLFolder, const QString& iRFolder);
	static bool fileCopy(const QString & iKaynakDosya,  const QString & iHedefDosya,const bool & iOverWriting,const bool & iSecAttributeAktar=true);


	static void addExtension(const QString& iExtension, QString & oFileName,bool iAddToEnd=false);
	static void removeExtension(const QString& iExtension, QString & oFileName);
	static QString getExtensionOf(const FileType& iFileType);

	static QString createTempFileName();
	static QString createTempFileName(const QString & iHedefDosya);
	static bool checkDiskSpace(const QString & iHedefDosya, qint64 iSize);

	static bool getFreeTotalSpace(const QString& sDirPath,double& ofTotal, double& ofFree);

	static bool deleteFile(const QString & iFolderPath,bool iRemoveReadOnlyAttr=true);
	static bool deleteFolder(const QString & iFolderPath,bool iRemoveReadOnlyAttr=true);
	static bool moveFolder(const QString & iSourceFolder,const QString & iToFolder,bool iIsOverWriting);
	static bool copyFolder(const QString & iSourceFolder,const QString & iToFolder,bool iIsOverWriting);

	static QString genelAyarPath();
	static QString yerelAyarPath();
#ifdef WIN32
	static QString getWinSystem32Path();
	static QString getWindowsDir();
#endif	

	static FileType getDirType(const QString & iDirPath,bool iAltDizinKontrol);		
	static QFileInfoList getDirEntryInfoList(const QString & iDirPath);


	static void transferFileTimeAttributes(const QString& iSourceFile,const QString & iDestFile);
	static void transferFileAttributes(const QString& iSourceFile,const QString & iDestFile);
	 

	static void removeReadOnly(const QString & iFileName);
	static void setAsReadOnly(const QString & iFileName);
	static bool isReadOnly(const QString & iFileName);

	static bool			bosDosyaMi(const QString & iFileName);

	static bool			imzaliDosyaMi(const QString & iFileName) ;
	static bool			sifreliDosyaMi(const QString & iFileName) ;
	static QByteArray	baslikOku(const QString & iFileName);

	static bool		isTooLongPath(const QString &iPath,int iPathlen = MAX_FILEPATH_LEN);
	static QStringList getTooLongPaths(const QString & iRootDir,int iPathlen = MAX_FILEPATH_LEN);	
};


NAMESPACE_END
#endif


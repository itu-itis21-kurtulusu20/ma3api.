#include "FileUtilTest.h"
#include <QDir>
#include <QDebug>

using namespace esya;


FileUtilTest::FileUtilTest()
{
}

FileUtilTest::~FileUtilTest()
{
}

void FileUtilTest::initTestCase()
	{	
		_testDizinOlustur(SILINECEK_DIZIN);
		_testDizinOlustur(TASINACAK_DIZIN);
		_testDizinOlustur(KOPYALANACAK_DIZIN);
	}

void FileUtilTest::testGetFreeTotalSpace()
{
	QString testDir = QDir::currentPath();
	double totalSize;
	double freeSize;
	bool isOk = FileUtil::getFreeTotalSpace(testDir,totalSize,freeSize);
	if(!isOk)
	{
		qWarning("getFreeTotalSpace()-Hata");
	}
	else
	{
		qDebug(qPrintable(QString("Entry =%1 , Free = %2 , Total = %3").arg(testDir).arg(freeSize).arg(totalSize)));
	}
}

void FileUtilTest::testRemoveExtension()
{
	QString extension = "m3i";
	QString fileName1 = "deneme";
	QString fileName2 = "deneme.txt";
	QString fileName3 = "deneme.m3i.txt";
	QString fileName4 = "deneme.m3i";
	QString fileName5 = "deneme.txt.m3i";
	QString fileName6 = "deneme.txt.dat.m3i";
	QString fileName7 = "deneme.m3i.txt.dat";
	QString fileName8 = "m3i.m3i.dat";
	QString fileName9 = "m3i.m3i";
	QString fileName10 = "d.";

	FileUtil::removeExtension(extension,fileName1);
	FileUtil::removeExtension(extension,fileName2);
	FileUtil::removeExtension(extension,fileName3);
	FileUtil::removeExtension(extension,fileName4);
	FileUtil::removeExtension(extension,fileName5);
	FileUtil::removeExtension(extension,fileName6);
	FileUtil::removeExtension(extension,fileName7);
	FileUtil::removeExtension(extension,fileName8);
	FileUtil::removeExtension(extension,fileName9);
	FileUtil::removeExtension(extension,fileName10);

	return;
}

void FileUtilTest::testAddExtension()
{
	QString extension = "ext";
	QString fileName1 = "deneme";
	QString fileName2 = "deneme.txt";
	QString fileName3 = "deneme.txt.dat";
	QString fileName4 = "d.";

	FileUtil::addExtension(extension,fileName1,true);
	FileUtil::addExtension(extension,fileName2,true);
	FileUtil::addExtension(extension,fileName3,true);
	FileUtil::addExtension(extension,fileName4,true);
	
	return;
}

void FileUtilTest::testCreateTempFile()
{
	QString fName = FileUtil::createTempFileName();
	QString fName2 = FileUtil::createTempFileName();
}


void FileUtilTest::testFileTypeByName()
{
// 	QString folderPath = QString::fromUtf8("\\\\rmz\\Paylas\\yasemin\\Yeni Klasöra2");
// 	FileUtil::FileType folderType = FileUtil::getFileTypeByName(folderPath);

	QString encFileName1 = QString("%1.%2.%3").arg("filename1").arg(FILE_EXT_ENCRYPTED).arg("ext1");
	QString encFileName2 = QString("%1.%2.%3.%4").arg("filename1").arg("filename2").arg(FILE_EXT_ENCRYPTED).arg("ext1");
	QString encFileName3 = QString("%1.%2.%3").arg("filename1").arg("M3S").arg("ext1");
	QString encFileName4 = QString("%1.%2").arg("filename1").arg(FILE_EXT_ENCRYPTED);
	QString encFileName5 = QString("%1.%2.%3").arg("filename1").arg("ex1").arg(FILE_EXT_ENCRYPTED);
	QString encFileName6 = QString("%1.%2").arg("filename1").arg(FILE_EXT_SIGNED);

	QString nEncFileName1 = QString("%1.%2").arg("fileName1").arg("ext1");
	QString nEncFileName2 = QString("%1.%2.%3.%4").arg("fileName1").arg(FILE_EXT_ENCRYPTED).arg("ext1").arg("ext2");
	QString nEncFileName3 = QString("%1.%2").arg(FILE_EXT_ENCRYPTED).arg("ext1");
	QString nEncFileName4 = QString("%1").arg(FILE_EXT_ENCRYPTED);


	FileUtil::FileType ft1 = FileUtil::getFileTypeByName(encFileName1);
	FileUtil::FileType ft2 = FileUtil::getFileTypeByName(encFileName2);
	FileUtil::FileType ft3 = FileUtil::getFileTypeByName(encFileName3);
	FileUtil::FileType ft4 = FileUtil::getFileTypeByName(encFileName4);
	FileUtil::FileType ft5 = FileUtil::getFileTypeByName(encFileName5);
	FileUtil::FileType ft6 = FileUtil::getFileTypeByName(encFileName6);

	FileUtil::FileType nft1 = FileUtil::getFileTypeByName(nEncFileName1);
	FileUtil::FileType nft2 = FileUtil::getFileTypeByName(nEncFileName2);
	FileUtil::FileType nft3 = FileUtil::getFileTypeByName(nEncFileName3);
	FileUtil::FileType nft4 = FileUtil::getFileTypeByName(nEncFileName4);



	QCOMPARE(ft1,FileUtil::FT_Encrypted);
	QCOMPARE(ft2,FileUtil::FT_Encrypted);
	QCOMPARE(ft3,FileUtil::FT_Encrypted);
	QCOMPARE(ft4,FileUtil::FT_Encrypted);
	QCOMPARE(ft5,FileUtil::FT_Encrypted);
	QCOMPARE(ft6,FileUtil::FT_Signed);

	QCOMPARE(nft1,FileUtil::FT_OtherFile);
	QCOMPARE(nft2,FileUtil::FT_OtherFile);
	QCOMPARE(nft3,FileUtil::FT_OtherFile);
	QCOMPARE(nft4,FileUtil::FT_OtherFile);
}



void FileUtilTest::testCheckDiskSpace()
{
	bool b = FileUtil::checkDiskSpace("deneme.txt",10000000);

	QVERIFY(b);
}
void FileUtilTest::testDeleteFolder()
{  
	QVERIFY(FileUtil::deleteFolder(QDir::home().absolutePath()+"/"+SILINECEK_DIZIN));
	QDir lDir(QDir::home().absolutePath()+"/"+SILINECEK_DIZIN);
	if (lDir.exists())
	{
		QFAIL("Dizin ba�ar�yla silinemedi.");
	}
}
void FileUtilTest::testCopyFolder()
{	
	QDir().home().mkpath(HEDEF_DIZIN);
	QVERIFY(FileUtil::copyFolder(QDir::home().absolutePath()+"/"+KOPYALANACAK_DIZIN,QDir::home().absolutePath()+"/"+HEDEF_DIZIN,true));
	QDir lDir(QDir::home().absolutePath()+"/"+HEDEF_DIZIN+"/"+KOPYALANACAK_DIZIN);
	if (lDir.exists())
	{	
		QVERIFY("Dosya ba�ar�yla kopyaland�");
	}
	QVERIFY(FileUtil::deleteFolder(QDir::home().absolutePath()+"/"+KOPYALANACAK_DIZIN));
	QVERIFY(FileUtil::deleteFolder(QDir::home().absolutePath()+"/"+HEDEF_DIZIN));
}
void FileUtilTest::testMoveFolder()
{
	QDir().home().mkpath(HEDEF_DIZIN);
	QVERIFY(FileUtil::moveFolder(QDir::home().absolutePath()+"/"+TASINACAK_DIZIN,QDir::home().absolutePath()+"/"+HEDEF_DIZIN,true));
	QDir lDir(QDir::home().absolutePath()+"/"+HEDEF_DIZIN+"/"+TASINACAK_DIZIN);
	if (lDir.exists())
	{	
		QDir lKaynakDir(QDir::home().absolutePath()+"/"+TASINACAK_DIZIN);
		if (lKaynakDir.exists())
		{
			QFAIL("Dosya ta��namad�");
		}
		else
		{
			QVERIFY("Dosya ba�ar�yla ta��nd�");
		}			
	}	
	QVERIFY(FileUtil::deleteFolder(QDir::home().absolutePath()+"/"+HEDEF_DIZIN));
}
void FileUtilTest::_testDizinOlustur(const QString &iDizinAdi)
{  
	QDir::home().mkdir(iDizinAdi);
	QDir lSilinecek(QDir::home().absolutePath()+"/"+iDizinAdi);
	lSilinecek.mkdir("Dizin1");
	lSilinecek.mkdir("Dizin2");

	QFile lFile(lSilinecek.path()+"/Dizin1/dosya1");
	lFile.open(QIODevice::WriteOnly);
	lFile.write(QByteArray("1.Dosyaya yaz�lanlar"));
	lFile.close();

	QFile lFile2(lSilinecek.path()+"/Dosya2");
	lFile2.open(QIODevice::WriteOnly);
	lFile2.write(QByteArray("2.Dosyaya yaz�lanlar"));
	lFile2.close();
}

void FileUtilTest::testFileTypeByNameIcon()
{	
	while(1)
	{		
		QString entryPath = "\\\\ma3ist";
		QFileInfoList lInfoList = QDir(entryPath).entryInfoList(QDir::Files | QDir::Dirs | QDir::NoDotAndDotDot,QDir::DirsLast);
		foreach(QFileInfo fileInfo,lInfoList)
		{	
			QString subPath = fileInfo.absoluteFilePath();
			FileUtil::FileType fileType = FileUtil::getEntryTypeByNameIcon(subPath);
			qDebug()<<subPath<<"-"<<fileType;
		}
	//	qDebug()<<QString("%1 tipi alınması gecen sure = ").arg(entryPath)<<zamanTutucu.gecenSureVerSn();
	}
}

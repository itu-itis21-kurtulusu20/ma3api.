#ifndef __FILEUTILTEST_H__
#define __FILEUTILTEST_H__

#include <QObject>
#include "FileUtil.h"
#include <QtTest>
#define SILINECEK_DIZIN     "silinecekTempDizin"
#define TASINACAK_DIZIN		"tasinacakTempDizin"
#define KOPYALANACAK_DIZIN  "kopyalancakTempDizin"
#define HEDEF_DIZIN         "hedefTempDizim"
namespace esya
{
	class FileUtilTest : public QObject
	{
		Q_OBJECT

	public:
		FileUtilTest(void);
		~FileUtilTest();
	
	private:
		void _testDizinOlustur(const QString & iDizinAdi);
	    
	private slots:
		void initTestCase();

		void testAddExtension();

		void testFileTypeByName(); 
		void testRemoveExtension();

		void testFileTypeByNameIcon(); 

		void testCreateTempFile();
		void testGetFreeTotalSpace();
		void testDeleteFolder();
		void testCopyFolder();
		void testMoveFolder();		
		void testCheckDiskSpace();		
	};


}

#endif // __\TEST\FILEUTILTEST_H

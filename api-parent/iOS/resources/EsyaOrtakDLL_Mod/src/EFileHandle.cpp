#include "EFileHandle.h"
#include "EException.h"

using namespace esya;

EFileHandle::EFileHandle(const QString & iFileHandle, const QString & iOpenMode,bool iLockFile)
{
	mFilePtr = FileUtil::unicodeFILEPtrOlustur(iFileHandle,iOpenMode,iLockFile);	

	if (! mFilePtr )
	{
		throw EException("Dosyaya handle alýnamadý",__FILE__,__LINE__);
	}
}


FILE * EFileHandle::getFilePtr()
{
	return mFilePtr;
}

EFileHandle::~EFileHandle()
{
	fclose(mFilePtr);
}

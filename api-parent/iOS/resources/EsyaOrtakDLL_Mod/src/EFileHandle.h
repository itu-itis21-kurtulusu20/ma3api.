#ifndef EFILEHANDLE_H
#define EFILEHANDLE_H

#include "FileUtil.h"

class Q_DECL_EXPORT EFileHandle 
{
	FILE * mFilePtr;

public:
	EFileHandle(const QString & iFilePath, const QString & iOpenMode ="rb",bool iLockFile = true);
	~EFileHandle();

	FILE * getFilePtr();

private:
	
};

#endif // EFILEHANDLE_H

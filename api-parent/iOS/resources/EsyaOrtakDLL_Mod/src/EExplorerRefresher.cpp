#include "EExplorerRefresher.h"
#ifdef WIN32
	#include "windows.h"
	#include "shlobj.h"
#endif

EExplorerRefresher::~EExplorerRefresher(void)
{
}

EExplorerRefresher::EExplorerRefresher(const QString & iEntry)
{	
	mEntryList<<iEntry;
}

EExplorerRefresher::EExplorerRefresher(const QStringList & iEntryList)
:mEntryList(iEntryList)
{
}

void EExplorerRefresher::refreshExplorer()
{
	for (int k=0;k<mEntryList.size();k++)
	{
		#ifdef WIN32
			const QString & entryPath = mEntryList.at(k);		
			WCHAR buf[_MAX_PATH];
			wcsncpy(buf, (const wchar_t * )entryPath.utf16(),_MAX_PATH);
			SHChangeNotify(SHCNE_UPDATEDIR, SHCNF_PATH, buf, NULL);
		#endif
	}
	#ifdef WIN32
	SHChangeNotify(SHCNE_ASSOCCHANGED,SHCNF_IDLIST,NULL,NULL);
	#endif
}

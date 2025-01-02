#include "EProcessInfo.h"
#include <QFileInfo>
#ifdef WIN32
#include "NTProcessInfo.h"
#define MAX_PI 200	// Max process
static smPROCESSINFO lpi[MAX_PI] = {0};
#include <windows.h>
#include <stdio.h>
#include <wchar.h>
#include <psapi.h>
// Link to Psapi.lib
#pragma comment(lib, "Psapi.lib")
#endif

NAMESPACE_BEGIN(esya)

EProcessInfo::EProcessInfo(const QString & iProgramYolu,const QString & iKomutSatiri,qlonglong iSessionID)
:mProgramYolu(iProgramYolu),mKomutSatiri(iKomutSatiri),mSessionID(iSessionID)


{
}

EProcessInfo::~EProcessInfo(void)
{
}

#ifdef WIN32
// Get current running processes
DWORD EnumProcesses2Array(smPROCESSINFO lpi[MAX_PI])
{
	DWORD dwPIDs[MAX_PI] = {0};
	DWORD dwArraySize	 = MAX_PI * sizeof(DWORD);
	DWORD dwSizeNeeded	 = 0;
	DWORD dwPIDCount	 = 0;

	//== only to have better chance to read processes =====
	if(!sm_EnableTokenPrivilege(SE_DEBUG_NAME)) {
		//	return 0;
	}

	// Get a list of Process IDs of current running processes
	if(EnumProcesses((DWORD*)&dwPIDs, dwArraySize, &dwSizeNeeded))
	{
		HMODULE hNtDll = sm_LoadNTDLLFunctions();

		if(hNtDll)
		{
			// Get detail info on each process
			dwPIDCount = dwSizeNeeded / sizeof(DWORD);
			for(DWORD p = 0; p < MAX_PI && p < dwPIDCount; p++)
			{
				if(sm_GetNtProcessInfo(dwPIDs[p], &lpi[p]))
				{
					// Do something else upon success
				}
			}
			sm_FreeNTDLLFunctions(hNtDll);
		}
	}

	// Return either PID count or MAX_PI whichever is smaller
	return (DWORD)(dwPIDCount > MAX_PI) ? MAX_PI : dwPIDCount;
}
#endif

QMultiMap<QString,EProcessInfo> EProcessInfo::currentCmdFiles()
{
	QMultiMap<QString,EProcessInfo> retMap;
#ifdef WIN32
	DWORD processCount	      = 0;
	processCount = EnumProcesses2Array(lpi);	
	for (int k=0;k<processCount;k++)
	{
		smPROCESSINFO prInf = lpi[k];
		QString komutSatiri=QString::fromUtf16((const ushort *)prInf.szCmdLine);		
		QString programYolu=QString::fromUtf16((const ushort *)prInf.szImgPath);
		qlonglong sessionID=prInf.dwSessionID;
		EProcessInfo processInfo(programYolu,komutSatiri,sessionID);
		QStringList komutSatiriList = komutSatiri.split("\"");
		foreach(QString komutSatiriParam,komutSatiriList)
		{
			if(programYolu!=komutSatiriParam)
			{
				if(QFileInfo(komutSatiriParam).isFile())
				{
					retMap.insert(komutSatiriParam.toLower(),processInfo);	
				}
			}			
		}		
	}
#endif
	return retMap;
}

QMultiMap<QString,EProcessInfo> EProcessInfo::currentProcesses()
{		
	QMultiMap<QString,EProcessInfo> retMap;
#ifdef WIN32
	DWORD processCount	      = 0;
	processCount = EnumProcesses2Array(lpi);
	for (int k=0;k<processCount;k++)
	{
		smPROCESSINFO prInf = lpi[k];
		QString komutSatiri=QString::fromUtf16((const ushort *)prInf.szCmdLine);		
		QString programYolu=QString::fromUtf16((const ushort *)prInf.szImgPath);

		QString programAdiKey = QFileInfo(programYolu).fileName();
		programAdiKey = programAdiKey.remove(".exe", Qt::CaseInsensitive);
		if(programAdiKey.isEmpty())
		{
			continue;
		}
		qlonglong sessionID=prInf.dwSessionID;
		EProcessInfo processInfo(programYolu,komutSatiri,sessionID);
		retMap.insert(programAdiKey.toLower(),processInfo);		
	}
#endif
	return retMap;

}

bool EProcessInfo::programCalisiyorMu(const QString & iProgramAdi)
{
	QString programAdi = iProgramAdi.toLower();
	return EProcessInfo::currentProcesses().contains(programAdi);
}

QList<EProcessInfo> EProcessInfo::programinCalisanOrnekleri(const QString & iProgramAdi)
{	
	QList<EProcessInfo> retList;
	QMultiMap<QString,EProcessInfo> calisanlar = EProcessInfo::currentProcesses();
	if (calisanlar.contains(iProgramAdi))
	{
		retList = calisanlar.values(iProgramAdi);
	}
	return retList;
}
QString EProcessInfo::getProgramYolu() const
{
	return mProgramYolu;
}

QString EProcessInfo::getKomutSatiri() const
{
	return mKomutSatiri;
}

qlonglong EProcessInfo::getSessionId() const
{
	return mSessionID;
}

int EProcessInfo::getCurrentProcessMemoryUsage()
{
	int retValue = 0;
#ifdef WIN32
	HANDLE hProcess;
	PROCESS_MEMORY_COUNTERS pmc;

	DWORD curProcessID = GetCurrentProcessId();
	hProcess = OpenProcess(  PROCESS_QUERY_INFORMATION | PROCESS_VM_READ,FALSE, curProcessID );
	if (hProcess == NULL)
	{
		return retValue;
	}
	if (GetProcessMemoryInfo(hProcess, &pmc, sizeof(pmc)))
	{
		retValue = pmc.WorkingSetSize;
	}
	retValue = retValue - (sizeof(retValue)+sizeof(hProcess)+sizeof(pmc)+sizeof(curProcessID));
	CloseHandle(hProcess);
#endif
	return retValue;
}


NAMESPACE_END

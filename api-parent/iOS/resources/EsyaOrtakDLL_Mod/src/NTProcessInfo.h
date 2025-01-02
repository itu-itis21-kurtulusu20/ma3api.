#ifdef WIN32
//***********************************************************************/
// Header definitions to access NtQueryInformationProcess in NTDLL.DLL
//
// Copyright © 2007 Steven Moore (OrionScorpion).  All Rights Reserved.
//
// NOTES: PEB_LDR_DATA, RTL_USER_PROCESS_PARAMETERS and PEB struct are
//        defined in Winternl.h and Ntddk.h.  The specs below are from
//        Microsoft MSDN web site as of Jul 2007.  I locally specified
//        them below since they can change in future versions and may
//        not reflect current winternl.h or ntddk.h
//***********************************************************************/
#ifndef _ORIONSCORPION_NTPROCESSINFO_H_
#define _ORIONSCORPION_NTPROCESSINFO_H_

//////////////////////////////////////////////////////////////////////////
// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once

/** Unicode Support
#pragma warning(disable : 4995)	// 'function': name was marked as #pragma deprecated

// Compile with UNICODE support
#ifndef _UNICODE	// C Unicode support
#define _UNICODE
#endif

#ifndef UNICODE		// Win32 API Unicode support
#define UNICODE
#endif

**/

// Modify the following defines if you have to target a platform prior to the ones specified below.
// Refer to MSDN for the latest info on corresponding values for different platforms.
#ifndef WINVER				// Allow use of features specific to Windows 95 and Windows NT 4 or later.
#define WINVER 0x0500		// Change this to the appropriate value to target Windows 98 and Windows 2000 or later.
#endif

#ifndef _WIN32_WINNT		// Allow use of features specific to Windows NT 4 or later.
#define _WIN32_WINNT 0x0500		// Change this to the appropriate value to target Windows 98 and Windows 2000 or later.
#endif						

#ifndef _WIN32_WINDOWS		// Allow use of features specific to Windows 98 or later.
#define _WIN32_WINDOWS 0x0490 // Change this to the appropriate value to target Windows Me or later.
#endif

//#ifndef _WIN32_IE			// Allow use of features specific to IE 4.0 or later.
//#define _WIN32_IE 0x0600	// Change this to the appropriate value to target IE 5.0 or later.
//#endif

#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers
// Windows Header Files:
#include <windows.h>
// C RunTime Header Files
#ifdef _DEBUG
#define CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#else
#include <stdlib.h>
#endif

//#include <malloc.h>
//#include <memory.h>
#include <tchar.h>

// TODO: reference additional headers your program requires here
#include <commctrl.h>

#pragma comment(lib, "comctl32.lib")
//////////////////////////////////////////////////////////////////////////

#include <winternl.h>
#include <psapi.h>

#define STRSAFE_LIB
#include <strsafe.h>

#pragma comment(lib, "strsafe.lib")
#pragma comment(lib, "rpcrt4.lib")
#pragma comment(lib, "psapi.lib")

#ifndef NTSTATUS
#define LONG NTSTATUS
#endif

// Unicode path usually prefix with '\\?\'
#define MAX_UNICODE_PATH	32767L

// Used in PEB struct
typedef ULONG smPPS_POST_PROCESS_INIT_ROUTINE;

// Used in PEB struct
typedef struct _smPEB_LDR_DATA {
	BYTE Reserved1[8];
	PVOID Reserved2[3];
	LIST_ENTRY InMemoryOrderModuleList;
} smPEB_LDR_DATA, *smPPEB_LDR_DATA;

// Used in PEB struct
typedef struct _smRTL_USER_PROCESS_PARAMETERS {
	BYTE Reserved1[16];
	PVOID Reserved2[10];
	UNICODE_STRING ImagePathName;
	UNICODE_STRING CommandLine;
} smRTL_USER_PROCESS_PARAMETERS, *smPRTL_USER_PROCESS_PARAMETERS;

// Used in PROCESS_BASIC_INFORMATION struct
typedef struct _smPEB {
	BYTE Reserved1[2];
	BYTE BeingDebugged;
	BYTE Reserved2[1];
	PVOID Reserved3[2];
	smPPEB_LDR_DATA Ldr;
	smPRTL_USER_PROCESS_PARAMETERS ProcessParameters;
	BYTE Reserved4[104];
	PVOID Reserved5[52];
	smPPS_POST_PROCESS_INIT_ROUTINE PostProcessInitRoutine;
	BYTE Reserved6[128];
	PVOID Reserved7[1];
	ULONG SessionId;
} smPEB, *smPPEB;

// Used with NtQueryInformationProcess
typedef struct _smPROCESS_BASIC_INFORMATION {
    LONG ExitStatus;
    smPPEB PebBaseAddress;
    ULONG_PTR AffinityMask;
    LONG BasePriority;
    ULONG_PTR UniqueProcessId;
    ULONG_PTR InheritedFromUniqueProcessId;
} smPROCESS_BASIC_INFORMATION, *smPPROCESS_BASIC_INFORMATION;

// NtQueryInformationProcess in NTDLL.DLL
typedef NTSTATUS (NTAPI *pfnNtQueryInformationProcess)(
	IN	HANDLE ProcessHandle,
    IN	PROCESSINFOCLASS ProcessInformationClass,
    OUT	PVOID ProcessInformation,
    IN	ULONG ProcessInformationLength,
    OUT	PULONG ReturnLength	OPTIONAL
    );

pfnNtQueryInformationProcess gNtQueryInformationProcess;

typedef struct _smPROCESSINFO
{
	DWORD	dwPID;
	DWORD	dwParentPID;
	DWORD	dwSessionID;
	DWORD	dwPEBBaseAddress;
	DWORD	dwAffinityMask;
	LONG	dwBasePriority;
	LONG	dwExitStatus;
	BYTE	cBeingDebugged;
	TCHAR	szImgPath[MAX_UNICODE_PATH];
	TCHAR	szCmdLine[MAX_UNICODE_PATH];
} smPROCESSINFO;

HMODULE sm_LoadNTDLLFunctions(void);
void sm_FreeNTDLLFunctions(IN HMODULE hNtDll);
BOOL sm_EnableTokenPrivilege(IN LPCTSTR pszPrivilege);
BOOL sm_GetNtProcessInfo(IN const DWORD dwPID, OUT smPROCESSINFO *ppi);

#endif	// _ORIONSCORPION_NTPROCESSINFO_H_
#endif //IFDEF WIN32
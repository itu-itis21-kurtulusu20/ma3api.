//
//  wintypes.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 1/1/14.
//  Copyright (c) 2014 ilksen ozcan. All rights reserved.
//

#ifndef akisIOSCIF_wintypes_h
#define akisIOSCIF_wintypes_h
#include <stdio.h>

#ifdef __cplusplus
extern "C"
{
#endif
	
#if !defined(WIN32)
	
#include <stdint.h>
	
#ifndef BYTE
	typedef unsigned char BYTE;
#endif
	typedef unsigned char UCHAR;
	typedef unsigned char *PUCHAR;
	typedef unsigned short USHORT;
	
#ifndef __COREFOUNDATION_CFPLUGINCOM__
	typedef unsigned long ULONG;
	typedef void *LPVOID;
	//typedef unsigned short BOOL;
	typedef signed char		BOOL;
#endif
	
	typedef unsigned long *PULONG;
	typedef const void *LPCVOID;
	typedef unsigned long DWORD;
	typedef unsigned long *PDWORD;
	typedef unsigned short WORD;
	typedef int		LONG;
	typedef int		RESPONSECODE;
	typedef const char *LPCSTR;
	typedef const BYTE *LPCBYTE;
	typedef unsigned char *LPBYTE;
	typedef unsigned long *LPDWORD;
	typedef char *LPSTR;
	
	/* these types are deprecated but still used by old drivers and applications
	 * You should use LPSTR instead */
	typedef char *LPTSTR ;
	typedef const char *LPCTSTR ;
	typedef char *LPCWSTR
#ifdef __GNUC__
	/* __attribute__ is a GCC only extension */
	__attribute__ ((deprecated))
#endif
	;
	
#else
#include <windows.h>
#endif
	
#ifdef __cplusplus
}
#endif


#endif

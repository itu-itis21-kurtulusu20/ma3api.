

#if defined(WIN32)


#define _WIN32_WINNT 0x0500


#include "AccCtrl.h"
#include "AclApi.h"


#include <QFile>
//#include "LDAPUtils.h"
#include "EWindowsFileSecurity.h"
#include "esyaOrtak.h"
#include <QStringList>
#include "ELogger.h"

EWindowsFileSecurity::EWindowsFileSecurity()
{

}

EWindowsFileSecurity::~EWindowsFileSecurity()
{

}



//************************************
// Method:    	ownerAl
// Access:    	public 
// Description:	Verilen dosyanin sahibini eposta olarak doner.
// Returns:   	int
// Qualifier: 	
// Parameter: 	const QString & iFileName Dosya Adi
// Parameter: 	QString & oOwner Dosya sahibi eposta adresi 
// Parameter: 	QString & oHata Olusabilecek hata degeri
//************************************
int EWindowsFileSecurity::ownerAl( const QString & iFileName, QString & oOwner , QString & oHata )
{

	if ( !QFile::exists(iFileName))
	{
		oHata =QString("Dosya Bulunamadi. DosyaAdi: %1" ).arg(iFileName);
		return FAILURE;
	}

	PSECURITY_DESCRIPTOR pSD = NULL;
	PSID psid;
	SID_NAME_USE snu;
	DWORD reqLength;
	BOOL b,ownerDefaulted;


	b = GetFileSecurity((LPCWSTR)iFileName.utf16(),OWNER_SECURITY_INFORMATION,0,0,&reqLength); 

	pSD = GlobalAlloc(GMEM_FIXED,reqLength);

	if ( (b = GetFileSecurity((LPCWSTR)iFileName.utf16(),OWNER_SECURITY_INFORMATION,pSD,reqLength,&reqLength)) == 0 ) 
	{
		DWORD err = GetLastError();
		oHata = QString("GetFileSecurity() fonksiyonu hatali calisti : Hata: %1").arg(err);
		return FAILURE;
	}	

	WCHAR ownerName[BUF_SIZE],domainName[BUF_SIZE];
	DWORD oSize = BUF_SIZE, dSize = BUF_SIZE;

	if ( (b = GetSecurityDescriptorOwner(pSD,&psid,&ownerDefaulted)) == 0 )
	{
		DWORD err = GetLastError();
		GlobalFree(pSD);
		oHata = QString("GetSecurityDescriptorOwner() fonksiyonu hatali calisti : Hata: %1").arg(err);
		return FAILURE;
	}

	if ((b = LookupAccountSid(NULL,psid,ownerName,&oSize,domainName,&dSize,&snu))== 0 )
	{
		DWORD err = GetLastError();
		GlobalFree(pSD);
		oHata = QString("ownerAl() >> LookupAccountSid()  fonksiyonu hatali calisti: FileName : %2 Hata: %1").arg(err).arg(iFileName);
		return FAILURE;
	}

	oOwner = QString::fromUtf16((const ushort*)ownerName,oSize);

	GlobalFree(pSD);
	return SUCCESS;

}

bool okumaHakkiVarmi(ACCESS_MASK iMask)
{
	bool b =	(iMask & FILE_READ_ACCESS)  &&			//  ReadData 0x0001
				(iMask & 0x0008)			&&			//  Read Extended Attributes
				//(iMask & 0x0020)			&&			//  Traverse Folder/ Execute File
				(iMask & 0x0080)			&&			//  Read Attributes
				(iMask & 0x20000);						//  Read Permission

	return b;
}

//************************************
// Method:    	okumaHakkiOlanlariAl
// Access:    	public 
// Description:	Verilen dosyaya okuma hakki olanlari eposta listesi olarak doner
// Returns:   	int
// Qualifier: 	
// Parameter: 	const QString & iFileName Dosya adi
// Parameter: 	QStringList & oList sonuc listesi
// Parameter: 	QString & oHata Olusabilecek hata degeri
//************************************
int EWindowsFileSecurity::okumaHakkiOlanlariAl(const QString & iFileName , QStringList & oGruplar ,QStringList & oKullanicilar ,QString & oHata )
{
	DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() GIRIS iFileName: %1").arg(iFileName) );
	if ( !QFile::exists(iFileName))
	{
		oHata = "Dosya Bulunamadi";
		return FAILURE;
	}



	PSECURITY_DESCRIPTOR pSD = NULL;
	PSID psid;
	SID_NAME_USE snu;
	PACL pDACL;
	DWORD reqLength;
	BOOL b,ownerDefaulted,daclDefaulted,daclPresent;

	b = GetFileSecurity((LPCWSTR)iFileName.utf16(),DACL_SECURITY_INFORMATION,0,0,&reqLength); 

	DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetFileSecurity metodu cagrýldý ") );

	pSD = GlobalAlloc(GMEM_FIXED,reqLength);

	if ( (b = GetFileSecurity((LPCWSTR)iFileName.utf16(),DACL_SECURITY_INFORMATION,pSD,reqLength,&reqLength)) == 0 ) 
	{
		GlobalFree(pSD);
		DWORD err = GetLastError();
		oHata = QString("GetFileSecurity() fonksiyonu hatali calisti: Hata: %1").arg(err);
		
		DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> Hata olustu: %1").arg(oHata));

		return FAILURE;
	}	

	//b = GetSecurityDescriptorOwner(pSD,&psid,&ownerDefaulted);

	DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetSecurityDescriptorDacl metodu cagrilacak"));

	if ( (b = GetSecurityDescriptorDacl(pSD,&daclPresent,&pDACL,&daclDefaulted)) == 0 )
	{
		DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetSecurityDescriptorDacl metodu cagrildi"));

		GlobalFree(pSD);
		DWORD err = GetLastError();
		oHata = QString("GetSecurityDescriptorDacl()  fonksiyonu hatali calisti: Hata: %1").arg(err);

		DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> Hata olustu: %1").arg(oHata));

		return FAILURE;
	}

	if ( daclPresent && pDACL)
	{
		ACL_SIZE_INFORMATION aclSizeInfo;

		DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetAclInformation metodu cagrilacak"));

		if ((b = GetAclInformation(pDACL,&aclSizeInfo,sizeof(ACL_SIZE_INFORMATION),AclSizeInformation)) == 0 )
		{
			DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetAclInformation metodu cagrildi"));

			GlobalFree(pSD);
			DWORD err = GetLastError();
			oHata = QString("GetAclInformation()  fonksiyonu hatali calisti: Hata: %1").arg(err);

			DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> Hata olustu: %1").arg(oHata));

			return FAILURE;
		}

		WCHAR *userName,*domainName;
		DWORD dsize = BUF_SIZE , usize = BUF_SIZE;
		userName	= (WCHAR*)GlobalAlloc(GMEM_FIXED,BUF_SIZE);
		domainName	= (WCHAR*)GlobalAlloc(GMEM_FIXED,BUF_SIZE);


		int aceCount = aclSizeInfo.AceCount;
		ACCESS_ALLOWED_ACE *pACE;

		DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> AceCount: %1").arg(aceCount));

		for ( int i = 0 ; i < aceCount ; i++)
		{
			
			DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetAclInformation metodu %1th kere cagrilacak").arg(i));

			if ((b = GetAce(pDACL,i,(LPVOID*)&pACE))== 0)
			{
				
				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> GetAclInformation metodu %1th kere cagrildi").arg(i));

				DWORD err = GetLastError();
				oHata = QString("GetAce()  fonksiyonu hatali calisti: Hata: %1").arg(err);

				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> Hata olustu: %1").arg(oHata));

				continue;
			}
			byte b = pACE->Header.AceType;
			if (b!= ACCESS_ALLOWED_ACE_TYPE)
			{
				
				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> %1th NOT ACCESS_ALLOWED_ACE_TYPE").arg(i));

				continue;
			}
			if (!okumaHakkiVarmi(pACE->Mask)) 
			{
				
				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> %1th okumaHakkýVarmi() false dondu").arg(i));

				continue;
			}

			DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> LookupAccountSid metodu %1. kere cagrilacak").arg(i));

			if ((b = LookupAccountSid(NULL,&pACE->SidStart,userName,&usize,domainName,&dsize,&snu))== 0 )
			{

				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> LookupAccountSid metodu %1. kere cagrildi").arg(i));

				DWORD err = GetLastError();
				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> LookupAccountSid() fonksiyonu bu SID icin  hatali calisti. bu SID icin kullanici getirilmeyecek. : FileName : %2 Hata: %1").arg(err).arg(iFileName));
				continue;
			}

			if ( snu == SidTypeUser )
			{

				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >>  Bulunup eklenen kullanici (Sid Type Kullanici) : %1").arg(QString::fromUtf16((const ushort*)userName,usize)));

				oKullanicilar << QString::fromUtf16((const ushort*)userName,usize);
			}
			else if (snu == SidTypeGroup)
			{

				DEBUGLOGYAZ(ESYAORTAK_MOD, QString("okumaHakkiOlanlariAl() >> Bulunup eklenen kullanici (Sid Type Grup): %1").arg(QString::fromUtf16(( const ushort *)userName,usize)));

				oGruplar << QString::fromUtf16(( const ushort *)userName,usize);
			}

			usize = dsize = BUF_SIZE;
		}

		GlobalFree(userName);
		GlobalFree(domainName);
	}

	GlobalFree(pSD);
	return SUCCESS;
}

QString  EWindowsFileSecurity::getUserName(QString & oHata)const
{
	#define INFO_BUFFER_SIZE 32767
	DWORD bufCharCount = INFO_BUFFER_SIZE;
	TCHAR  infoBuf[INFO_BUFFER_SIZE];

	if( !GetUserName( infoBuf, &bufCharCount ) )
	{
		oHata = "User Name Alýnamadý";
		return QString();
	}
	
	return QString::fromUtf16((const ushort*)infoBuf,bufCharCount-1).toLower();
}

int EWindowsFileSecurity::storeOwnerInfo(const QString & iFileName, QString & oHata)
{
	oHata.clear();

	pOwner	= NULL;
	pSD		= NULL;


	DWORD res = GetNamedSecurityInfo(	(LPWSTR)iFileName.utf16(),
										SE_FILE_OBJECT,
										OWNER_SECURITY_INFORMATION,
										&pOwner,
										NULL,
										NULL,
										NULL,
										&pSD
										);

	if (res != ERROR_SUCCESS)
	{
		WARNINGLOGYAZ(ESYAORTAK_MOD,QString("GetNamedSecurityInfo File Owner Info alýnamadý. Hata : %1").arg(res));
		oHata = QString("GetNamedSecurityInfoW() failed. Hata Kodu: %1").arg(res);
	}
	return res;
//	LocalFree(pSD);
	
}

BOOL SetPrivilege(
				  HANDLE hToken,          // token handle (NULL: current process)
				  LPCTSTR Privilege,      // Privilege to enable/disable
				  BOOL bEnablePrivilege   // TRUE to enable.  FALSE to disable
				  )
{
	TOKEN_PRIVILEGES tp;
	LUID luid;
	TOKEN_PRIVILEGES tpPrevious;
	DWORD cbPrevious=sizeof(TOKEN_PRIVILEGES);
	BOOL CloseAtEnd=FALSE;

	//
	// Retrieve a handle of the access token
	//
	if (hToken==NULL) {
		if (!OpenProcessToken(GetCurrentProcess(),
			TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY,
			&hToken)) {
				//printf("OpenProcessToken in SetPrivilege failed.\n");
				return FALSE;
		}
		CloseAtEnd=TRUE;
	}

	if (!LookupPrivilegeValue( NULL, Privilege, &luid )) 
	{
		//if (bVerbose) printf("LookupPrivilegeValue in SetPrivilege failed.\n");
		return FALSE;
	}

	//
	// first pass.  get current privilege setting
	//
	tp.PrivilegeCount           = 1;
	tp.Privileges[0].Luid       = luid;
	tp.Privileges[0].Attributes = 0;

	AdjustTokenPrivileges(
		hToken,
		FALSE,
		&tp,
		sizeof(TOKEN_PRIVILEGES),
		&tpPrevious,
		&cbPrevious
		);

	if (GetLastError() != ERROR_SUCCESS) {
		//if (bVerbose) printf("AdjustTokenPrivileges in SetPrivilege failed.\n");
		return FALSE;
	}

	//
	// second pass.  set privilege based on previous setting
	//
	tpPrevious.PrivilegeCount       = 1;
	tpPrevious.Privileges[0].Luid   = luid;

	if(bEnablePrivilege) {
		tpPrevious.Privileges[0].Attributes |= (SE_PRIVILEGE_ENABLED);
	}
	else {
		tpPrevious.Privileges[0].Attributes ^= (SE_PRIVILEGE_ENABLED &
			tpPrevious.Privileges[0].Attributes);
	}

	AdjustTokenPrivileges(
		hToken,
		FALSE,
		&tpPrevious,
		cbPrevious,
		NULL,
		NULL
		);

	if (GetLastError() != ERROR_SUCCESS) {
		//if (bVerbose) printf("AdjustTokenPrivileges in SetPrivilege failed.\n");
		return FALSE;
	}

	/* call CloseHandle or not ??? */
	if (CloseAtEnd) {
		CloseHandle(hToken);
	}

	return TRUE;
}


int EWindowsFileSecurity::restoreOwnerInfo(const QString & iFileName, QString & oHata)
{
	oHata.clear();
	if (pOwner)
	{
		// Enable the SE_TAKE_OWNERSHIP_NAME privilege.
		if (!SetPrivilege(NULL, SE_TAKE_OWNERSHIP_NAME, TRUE)) 
		{
			oHata = "SetPrivilege() failed. :  SE_TAKE_OWNERSHIP_NAME";
			return -1;
		} 
		if (!SetPrivilege(NULL,SE_RESTORE_NAME,TRUE)) 
		{
			oHata = "SetPrivilege() failed. :  SE_RESTORE_NAME";
			return -1;
		}
		if (!SetPrivilege(NULL,SE_BACKUP_NAME,TRUE)) 
		{
			oHata = "SetPrivilege() failed. :  SE_BACKUP_NAME";		
			return -1;
		}
		if (!SetPrivilege(NULL,SE_CHANGE_NOTIFY_NAME,TRUE)) 
		{
			oHata = "SetPrivilege() failed. :  SE_CHANGE_NOTIFY_NAME";		
			return -1;
		}


		DWORD res = SetNamedSecurityInfoW(	(LPWSTR)iFileName.utf16(),
											SE_FILE_OBJECT,
											OWNER_SECURITY_INFORMATION,
											pOwner,
											NULL,
											NULL,
											NULL
											);
		if (res != ERROR_SUCCESS)
		{
			WARNINGLOGYAZ(ESYAORTAK_MOD,QString("SetNamedSecurityInfo File Owner Info yazýlamadý. Hata : %1").arg(res));
			oHata = QString("SetNamedSecurityInfoW() failed. Hata Kodu: %1").arg(res);
		}

		LocalFree(pSD);
		LocalFree(pOwner);
		pOwner = 0;
		return res;
	}
}


#endif
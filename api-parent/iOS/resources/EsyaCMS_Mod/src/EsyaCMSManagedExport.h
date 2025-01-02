
#include "ECMSUtil.h"
#include "FileUtil.h"
#include "pkcs7.h"

using namespace esya;

#ifndef __stdcall
#define __stdcall
#endif

extern "C" Q_DECL_EXPORT int __stdcall denemeSimple()
{
	//ERRORLOGYAZ("TEST","denemesimple basladi");
	return 3;
}


extern "C" Q_DECL_EXPORT int __stdcall besImzalaKartsiz(	const char * iPrivKey,
															int  iPrivKeySize,
															const char * ipCertBytes,
															int	iCertBytesSize,
															const char * ipImzalanacak, 
															int iImzalanacakSize,
															char* opImzaSonucu,
															int * ioImzaSonucuSize)

{
	try
	{
		QByteArray imzaSonucu;
		QByteArray imzalanacakVeri(ipImzalanacak,iImzalanacakSize);

		QByteArray baCert(ipCertBytes,iCertBytesSize);
		ECertificate cert(baCert);

		OzelAnahtarBilgisi oa(QByteArray(iPrivKey,iPrivKeySize));

		ECMSUtil::signBES(imzalanacakVeri,cert,AnahtarBilgisi(oa,AcikAnahtarBilgisi()),imzaSonucu);

		if(*ioImzaSonucuSize < imzaSonucu.size())
			return -1;

		*ioImzaSonucuSize = imzaSonucu.size();

		memcpy(opImzaSonucu,imzaSonucu.constData(),imzaSonucu.size());

		return(0);
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}


extern "C" Q_DECL_EXPORT int __stdcall besImzala(	const char* ipDllName, 
													const char * ipLabel,
													int iSlotNo, 
													const char *ipKartParola,
													const char * ipCertBytes,
													int	iCertBytesSize,
													const char * ipImzalanacak, 
													int iImzalanacakSize,
													char* opImzaSonucu,
													int * ioImzaSonucuSize)

{
	try
	{
		QByteArray imzaSonucu;
		QByteArray imzalanacakVeri(ipImzalanacak,iImzalanacakSize);

		QByteArray baCert(ipCertBytes,iCertBytesSize);
		ECertificate cert(baCert);

		OzelAnahtarBilgisi oa(	QString(ipDllName), 
								QString(ipLabel),
								iSlotNo, 
								QString(ipKartParola));

		ECMSUtil::signBES(imzalanacakVeri,cert,AnahtarBilgisi(oa,AcikAnahtarBilgisi()),imzaSonucu);

		if(*ioImzaSonucuSize < imzaSonucu.size())
			return -1;

		*ioImzaSonucuSize = imzaSonucu.size();

		memcpy(opImzaSonucu,imzaSonucu.constData(),imzaSonucu.size());

		return(0);
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}

extern "C" Q_DECL_EXPORT int __stdcall besImzalaWithHandle(	int iKartHandle,
															const char * iKeyLabel,
															const char * ipCertBytes,
															int	iCertBytesSize,
															const char * ipImzalanacak, 
															int iImzalanacakSize,
															char* opImzaSonucu,
															int  iImzaSonucuSize)

{
	try
	{
		QByteArray imzaSonucu;
		QByteArray imzalanacakVeri(ipImzalanacak,iImzalanacakSize);

		QByteArray baCert(ipCertBytes,iCertBytesSize);
		ECertificate cert(baCert);

		OzelAnahtarBilgisi oa(	(EPKCS11*)iKartHandle,QString (iKeyLabel) );

		ECMSUtil::signBES(imzalanacakVeri,cert,AnahtarBilgisi(oa,AcikAnahtarBilgisi()),imzaSonucu,true);

		if(iImzaSonucuSize < imzaSonucu.size())
			return -1;

		memcpy(opImzaSonucu,imzaSonucu.constData(),imzaSonucu.size());

		return(imzaSonucu.size());
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}

extern "C" Q_DECL_EXPORT int __stdcall zamanDamgasiEkle(	const char * ipImzali, 
															int iImzaliSize,
															const char * ipZamanDamgasi, 
															int iZamanDamgasiSize,
															char* opImzaSonucu,
															int iImzaSonucuSize)

{
	try
	{
		QByteArray imzaSonucu;
		QByteArray imzali(ipImzali,iImzaliSize);
		QByteArray tsBytes(ipZamanDamgasi,iZamanDamgasiSize);

		ContentInfo ci(imzali);
		SignedData sd(ci);

		if (sd.getSignerInfos().isEmpty())
			return -2;

		SignerInfo & si = sd.getSignerInfos()[0];

		si.addUnsignedAttribute(tsBytes,PKCS7_id_aa_timeStampToken);

		ci.setContent(sd.getEncodedBytes());
		

		QByteArray hImzaSonucu = ci.getEncodedBytes();				

		if(iImzaSonucuSize < hImzaSonucu.size())
			return -1;

		memcpy(opImzaSonucu,hImzaSonucu.constData(),hImzaSonucu.size());

		return(hImzaSonucu.size());
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}

extern "C" Q_DECL_EXPORT int __stdcall hexStringeCevir(	const char * ipNormalBytes, 
														int iNormalBytesSize,
														char* opHexString,
														int iHexStringSize)

{
	try
	{
		QByteArray normalBytes(ipNormalBytes,iNormalBytesSize);
		QByteArray hexString = normalBytes.toHex();				

		if (iHexStringSize < hexString.size())
			return -1;

		memcpy(opHexString,hexString.constData(),hexString.size());

		return(hexString.size());
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}




extern "C" Q_DECL_EXPORT int __stdcall besDosyaImzala(	const char* ipDllName, 
														const char * ipLabel,
														int iSlotNo, 
														const char *ipKartParola,
														const char * ipCertBytes,
														int	iCertBytesSize,
														const char * ipImzalanacakDosyaAdi, 
														int ipImzalanacakDosyaAdiSize, 
														const char * ipImzaliDosyaAdi,
														int ipImzaliDosyaAdiSize
														)

{
	try
	{
		QString pdFileName = QString::fromUtf16((const ushort*)ipImzalanacakDosyaAdi,ipImzalanacakDosyaAdiSize);
		QString sdFileName = QString::fromUtf16((const ushort*)ipImzaliDosyaAdi,ipImzaliDosyaAdiSize);

		QByteArray baCert(ipCertBytes,iCertBytesSize);
		ECertificate cert(baCert);

		OzelAnahtarBilgisi oa(	QString(ipDllName), 
			QString(ipLabel),
			iSlotNo, 
			QString(ipKartParola));

		ECMSUtil::signFileBES(pdFileName,cert,AnahtarBilgisi(oa,AcikAnahtarBilgisi()),sdFileName,true);

		return SUCCESS;
	}
	catch (EException  &exc)
	{
		ERRORLOGYAZ(ESYACMS_MOD,exc.printStackTrace());
		return -1;	
	}
}

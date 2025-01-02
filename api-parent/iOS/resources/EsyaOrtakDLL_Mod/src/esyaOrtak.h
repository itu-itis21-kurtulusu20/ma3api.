#ifndef ESYAORTAK_H
#define ESYAORTAK_H

#ifndef NAMESPACE_BEGIN
#define NAMESPACE_BEGIN(x) namespace x{
#endif

#ifndef NAMESPACE_END
#define NAMESPACE_END }
#endif


#define FAILURE -1
#define SUCCESS  0
#define DELETE_MEMORY_ARRAY(ptr) if (ptr) delete []ptr; ptr = NULL;  
#define DELETE_MEMORY(ptr) if (ptr) delete ptr; ptr = NULL;  

typedef unsigned char byte;
typedef byte* bytePTR;


#ifndef NULL
#ifdef __cplusplus
#define NULL    0
#else
#define NULL    ((void *)0)
#endif
#endif

#ifndef WIN32
#define __stdcall
#endif

#ifdef WIN32
#define __EFUNC__ __FUNCTION__
#else
#define __EFUNC__ __FUNCTION__
#endif

#define FUNCTRY_BEGIN(x)	try {\
								DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" fonksiyonuna girildi."));

#define FUNCTRY_END(x)		DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" fonksiyonundan çýkýldý."));\
							}\
							catch (EException &exc)\
							{ \
								ERRORLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(exc.printStackTrace()))\
								throw exc.append(__EFUNC__);\
							}



#define OR_FUNC_BEGIN		FUNCTRY_BEGIN(ESYAORTAK_MOD)
#define OR_FUNC_END			FUNCTRY_END(ESYAORTAK_MOD)


#define ESYA_FUNC_BEGIN(x) 		DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" -GIRIS"));
#define ESYA_FUNC_END(x)		DEBUGLOGYAZ(x,QString("%1 : %2").arg(__EFUNC__).arg(" -TAMAMLANDI."));
#define ESYA_FUNC_ERROR(x,y)	ERRORLOGYAZ(x,QString("%1 : %2 - %3").arg(__EFUNC__).arg(" -HATA OLUSTU.").arg(y));
					

#define __E_FILE__ QFileInfo(QString(__FILE__)).fileName()


/************************************************************************/
/*							MODULE NAMES                                */
/************************************************************************/


#define ESYAORTAK_MOD				"EsyaOrtak_MOD"
#define ESYAORTAKGUI_MOD			"EsyaOrtakGUI_MOD"
#define ESYAGENELGUI_MOD			"EsyaGenelGUI_MOD"
#define ESYAASN_MOD					"EsyaASN_MOD"
#define ESYAHAB_MOD					"EsyaHAB_MOD"
#define ESYACMS_MOD					"EsyaCMS_MOD"
#define ESYADOSYAISLEMLERI_MOD		"EsyaDosyaIslemleri_MOD"
#define ESYA_DOSYAIZLEYICI_MOD		"EsyaDosyaIzleyici_MOD"
#define ESYA_SAHIPDEGISTIR_MOD		"EsyaSahipDegistir_MOD"
#define ESYA_UZANTISONAALICI_MOD	"EsyaUzantiSonaAlici_MOD"
#define ESYASERTIFIKADOGRULAMA_MOD	"EsyaSertifikaDogrulama_MOD"
#define ESYASERTIFIKADEPOSU_MOD		"EsyaSertifikaDeposu_MOD"
#define ESYAPKCS11_MOD				"EsyaPKCS11_MOD"
#define ESYAPKCS12_MOD				"EsyaPKCS12_MOD"
#define ESYAOCSP_MOD				"EsyaOCSP_MOD"
#define ESYAKONSOL_MOD				"EsyaKonsol_MOD"
#define ESYACMD_MOD					"EsyaCMD_MOD"
#define ESYAKRIPTO_MOD				"EsyaKripto_MOD"
#define ESYAKABUKEKLENTISI_MOD		"EsyaKabukEklentisi_MOD"
#define ESYASERTIFIKAYARDIMCISI_MOD	"EsyaSertifikaYardimcisi_MOD"
#define ESYAZAMANDAMGASI_MOD		"EsyaZamanDamgasi_MOD"
#define ESYAGDM_MOD					"EsyaGDM_MOD"
#define ESYAOUTLOOKEKLENTI_MOD		"EsyaOutlookEklentisi_MOD"
#define ESYATHUNDERBIRDEKLENTI_MOD	"EsyaThunderbirdEklentisi_MOD"
#endif



//
//  CardErrorCodes.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CardErrorCodes__
#define __akisIOSCIF__CardErrorCodes__

#include <iostream>
class CardErrorCodes {
    public :
    static const int BASARILI        = 0x9000;
    static const int OKUNACAK_DAHA_VERI_VAR        = 0x61C0;
    static const int EDC_HATASI        = 0x6281;
	static const int LE_DOSYA_SONUNU_GECIYOR        = 0x6282;
	static const int BELLEK_TEMIZLENDI        = 0x6287;
	static const int NMI_HATASI        = 0x641F;
    //static  const int PIN_HATALI        = 0x63CX;
	static const int PIN_HATALI        = 0x63C0;
	static const int SIT_TABLOSU_HATALI        = 0x6401;
	static const int DOSYA_BOYU_HATALI        = 0x6402;
	static const int BELLEK_DOLU_HATASI        = 0x6403;
	static const int DIZIN_YOK_HATASI        = 0x6404;
	static const int DIZIN_DOLU_HATASI        = 0x6405;
	static const int PIN_GIRILMEDI        = 0x6406;
	static const int ANAHTAR_HATALI        = 0x6410;
	static const int DOSYA_ACIK        = 0x6411;
	static const int HATALI_FID_TIPI        = 0x6412;
	static const int HATALI_FID_NO        = 0x6413;
	static const int ANAHTAR_ZATEN_VAR        = 0x6414;
	static const int ANAHTAR_DOSYASI_HATALI        = 0x6415;
	static const int ANAHTAR_OKUNAMAZ        = 0x6416;
	static const int ANAHTAR_DOLDU        = 0x6417;
	static const int MF_SECILMEDI        = 0x6418;
	static const int ANAHTAR_YOK        = 0x6419;
	static const int PIN_YOK		        = 0x641A;
	static const int YASAM_EVRESI_HATALI        = 0x641B;
	static const int BELLEK_HATASI         = 0x641C;
	static const int YANLIS_ALANA_YAZILDI        = 0x641D;
	static const int BASLANGICTA_BELLEK_HATASI        = 0x641E;
	static const int YETERLI_BELLEK_YOK         = 0x641F;
	static const int KART_HATALI        = 0x6420;
	static const int ISIM_HATASI        = 0x6421;
	static const int ZINCIR_BOYU_HATALI        = 0x6422;
	static const int ZINCIR_MESAJ_DESTEKLENMIYOR        = 0x6423;
	static const int UYUMSUZ_VERI        = 0x6425;
	static const int EEPROM_YAZMA_HATASI        = 0x6581;
	static const int EEPROM_ARDES_HATASI        = 0x6582;
	static const int CANLANDIRMA_EVRESI_HATASI        = 0x6600;
	static const int LC_HATASI_LE_HATASI        = 0x6700;
	static const int ERISIM_HATASI_LC_KAYIT_UYUMSUZLUGU_DOSYA_TIPI_UYUMSUZLUGU         = 0x6981;
	static const int GUVENLIK_KOSULU_SAGLANAMADI         = 0x6982;
	static const int ALGORITMA_HATASI_ISTENILEN_VERI_GECERSIZ        = 0x6984;
	static const int KULLANIM_KOSULLARI_SAGLANMIYOR         = 0x6985;
	static const int DOSYA_YUVASI_BOS        = 0x6986;
	static const int ISTENILEN_ANAHTAR_YUKLU_DEGIL         = 0x6987;
	static const int BILGI_VERILMIYOR        = 0x6A00;
	static const int FONKSIYON_DESTEKLENMIYOR        = 0x6A81;
	static const int DOSYA_YOK_HATASI        = 0x6A82;
	static const int KAYIT_BULUNAMADI        = 0x6A83;
	static const int DOSYADA_YER_YOK        = 0x6A84;
	static const int TLV_LC_UYUSMAZLIGI        = 0x6A85;
	static const int DESTEKLENMEYEN_PARAMETRE_HATALI_PARAMETRE        = 0x6A86;
	static const int PARAMETRELERLE_UYUSMAYAN_NC        = 0x6A87;
	static const int BASVURULAN_VERI_YOK        = 0x6A88;
	static const int DOSYA_ZATEN_VAR        = 0x6A89;
	static const int DIZIN_ZATEN_VAR_HATASI        = 0x6A8A;
    static const int KAYIT_YOK        = 0x6B00;
	static const int LE_DAHA_UZUN        = 0x6CC0;
	static const int INS_HATASI        = 0x6D00;
	static const int CLA_HATASI        = 0x6E00;
    
	static const int DLL_NULL_POINTER =	        0x2000;
	static const int DLL_FUNC_PARAM =		        0x2001;
	static const int DLL_MORE_DATA	=		        0x2002;
	static const int DLL_FUNC_NOT_SUPPORTED =	    0x2003;
	static const int DLL_BUFFER_SMALL		=	    0x2004;
	static const int ESTABLISH_CONTEXT	=		    0x0001;
	static const int RELEASE_CONTEXT		=		0x0002;
    static const int CONNECT		=				0x0003;
	static const int DISCONNECT	=				0x0004;
	static const int BEGIN_TRANSACTION =			0x0005;
	static const int END_TRANSACTION	 =			0x0006;
	static const int LIST_READERS		=			0x0007;
	static const int GET_ATTRIBUTE	=			    0x0008;
	static const int GET_STATUS_CHANGE =			0x0009;
	static const int CARD_TRANSMIT	=			    0x000A;
	static const int NO_READERS		=			0x000B;
	static const int SHARE_VIOLATION	=			0x000C;
	static const int NOT_AKIS			=			0x000D;
	static const int CARD_DISABLED	=			    0x000E;
	static const int ERR_VERSION		=			0x000F;
	static const int ERR_ATR			=			0x0010;
    static const int STATUS_WORD_NOT_FOUND =       0x0011;
    static const int CHK_DATA_NOT_FOUND =          0x0012;
    static const int CHK_INVALID       =           0x0013;
    static const int DLL_ALG_NOT_SUPPORTED =       0x0014;
};
#endif /* defined(__akisIOSCIF__CardErrorCodes__) */

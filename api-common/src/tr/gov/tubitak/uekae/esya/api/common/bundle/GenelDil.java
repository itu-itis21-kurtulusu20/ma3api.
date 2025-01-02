package tr.gov.tubitak.uekae.esya.api.common.bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.tools.IniFile;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;



public class GenelDil
{
     private static Logger logger = LoggerFactory.getLogger(GenelDil.class);

     private static String msDil;

     private static ResourceBundle msRB;

     static
     {
          _defaultDilYukle();
     }

     private static void _defaultDilYukle()
     {
          String dil = "tr";
          String filePath = System.getProperty("user.dir") + File.separator + "ma3.ini";

          if(FileUtil.exists(filePath))
          {
               IniFile inf = new IniFile(filePath);
               try
               {
                    inf.loadIni();
                    dil = inf.getValue("Konfigurasyon", "Dil");
                    // dil bossa veya desteklenen dillerden biri degilse dili dogrudan tr yap.
                    if(dil == null || dil.equals(""))
                         dil = "tr";
                    else if(!dil.equalsIgnoreCase("tr") && !dil.equalsIgnoreCase("en") && !dil.equalsIgnoreCase("az"))
                         dil = "en";
               } catch (Exception e)
               {
                    dil = "tr";
                    logger.debug("Warning in loading ini", e);
               }
          }
          setDil(dil);
     }

     public static void setDil(String aDil)
     {

          String path = "tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle";
          msRB = ResourceBundle.getBundle(path, new Locale(aDil));

          msDil = aDil;

          GUILanguageSettings.dilAyarla();
     }

     public static String getMDil()
     {
          return msDil;
     }

     public static final String ACIKLAMA="G.0";
     public static final String ACIKLAMA_GIRINIZ="G.5";
     public static final String AD="G.10";
     public static final String ADRES="G.15";
     public static final String AKTARIM_BILGILERI="G.16";
     public static final String AKTIF="G.19";
     public static final String AKTIF_YAP="G.20";
     public static final String ALGORITMA="G.25";
     public static final String ANAHTAR="G.30";
     public static final String ANAHTAR_ADI="G.31";
     public static final String ANAHTAR_BOYU="G.32";
     public static final String ANAHTAR_URETME="G.34";
     public static final String Anahtar_Bilgileri="G.35";
     public static final String ARA="G.40";
     public static final String ARAMA="G.41";
     public static final String ASKIDA="G.45";
     public static final String ASKIYAAL="G.46";
     public static final String AYARLAR="G.47";
     public static final String BASARILI="G.50";
     public static final String BASARISIZ="G.55";
     public static final String IL="G.60";
     public static final String ILCE="G.65";
     public static final String HEPSI="G.70";
     public static final String HAKKINDA="G.75";
     public static final String DOSYA_KAYDET="G.80";
     public static final String DOSYA_OKUNAMADI="G.85";
     public static final String DOSYA_SECILMEDI="G.90";
     public static final String DOSYA_YOLU="G.95";
     public static final String EPOSTA="G.100";
     public static final String EVET="G.105";
     public static final String GECERLI="G.110";
     public static final String KAYIT_MAKAMI="G.115";
     public static final String ESYA_KONTROL_MERKEZI="G.120";
     public static final String ESYA_KONTROL_MERKEZI_B="G.125";
     public static final String KONTROL_MERKEZI="G.130";
     public static final String IMZALA="G.135";
     public static final String SIFRELE="G.140";
     public static final String _0_1_ARALIGINDA_OLMALI="G.145";
     public static final String _0_ALGORITMASIYLA_SIFRECOZME_YAPILAMIYOR="G.150";
     public static final String _0_ISLEMI_ICIN_YETERLI_YETKINIZ_YOK="G.155";
     public static final String ANA_DIZIN="G.159";
     public static final String ANAH_URETILEMEDI="G.160";
     public static final String ANAHTAR_HATALI="G.165";
     public static final String ANAHTAR_SERTIFIKA_BETIGI="G.166";
     public static final String ANAHTAR_URETILIYOR="G.167";
     public static final String ARAMA_FILTRESI_HATALI="G.170";
     public static final String ARAMADA_BILINMEYEN_HATA="G.175";
     public static final String ARANAN_TKA_0_BULUNAMADI="G.180";
     public static final String ARGUMAN_BILINMIYOR="G.185";
     public static final String ASN1_DECODE_HATASI="G.190";
     public static final String ASN1_ENCODE_HATASI="G.195";
     public static final String ATTRIBUTE_BILINMIYOR="G.200";
     public static final String ATTRIBUTEA_VERILMEK_ISTENEN_DEGER_HATALI="G.205";
     public static final String BAKILACAK_YER="G.206";
     public static final String BASLIK_ANAHTAR_PARCALAMA="G.210";
     public static final String BASLIK_HAKKINDA="G.215";
     public static final String BASLIK_SIFRENIZ="G.220";
     public static final String BASLIK_SLOTLISTESI="G.225";
     public static final String BELGE="G.226.1";
     public static final String BELGE_NO="G.226.2";
     public static final String BELGE_YOLU="G.226.3";
     public static final String BILGI="G.227";
     public static final String BIRDEN_FAZLA_EMAIL="G.230";
     public static final String BLOKUZUNLUGU_0_ALGI_1_DESTEKLEMIYOR="G.235";
     public static final String CA_CERT_ERROR="G.240";
     public static final String CALISTIRILMAMASI_GEREKEN_FONKSIYON="G.245";
     public static final String CARD_NO_CONFLICT="G.250";
     public static final String CERT_HATALI="G.255";
     public static final String CERTSIGN="G.256";
     public static final String CKR_DEVICE_MEMORY_M="G.260";
     public static final String CKR_PIN_INCORRECT_M="G.265";
     public static final String CKR_PIN_INVALID_M="G.270";
     public static final String CKR_PIN_LOCKED_M="G.275";
     public static final String CKR_TOKEN_NOT_PRESENT_M="G.280";
     public static final String CKR_USER_ALREADY_LOGGED_IN_M="G.285";
     public static final String COMMONNAME="G.286";
     public static final String COUNTRYCITIZENSHIP="G.287";
     public static final String COUNTRYNAME="G.288";
     public static final String COUNTRYRESIDENCE="G.289";
     public static final String CRLSIGN="G.290";
     public static final String DATAENCIPHERMENT="G.291";
     public static final String DATEOFBIRTH="G.292";
     public static final String DECIPHERONLY="G.293";
     public static final String DEFAULT_KURUM="G.293.1";
     public static final String DEGISME_TARIHI="G.293.2";
     public static final String DENEME_YAZISI="G.294";
     public static final String DESTEKLENEN_MECHANIZMA_YOK="G.295";
     public static final String DETAYLAR="G.295.1";
     public static final String DIGITALSIGNATURE="G.296";
     public static final String DIL="G.297";
     public static final String DIZIN_ADI="G.297.1";
     public static final String DIZINSIF_VEYA_KULLADI_HATALI="G.298";
     public static final String DLL_LOAD_EDILEMEDI="G.298.1";
     public static final String DNSNAME="G.299";
     public static final String DOMAIN_COMPONENT="G.300";
     public static final String DOSYA_ADI="G.301.1";
     public static final String DOSYA_GORUNTULE="G.301.2";
     public static final String DOSYA_SEC="G.301.3";
     public static final String DOSYA_SECINIZ="G.301.4";
     public static final String DOSYA_TURU="G.301.5";
     public static final String DUGME_AC="G.302";
     public static final String DUGME_BITIR="G.303";
     public static final String DUGME_DETAY="G.304";
     public static final String DUGME_DIZIN_DNEKLE="G.305";
     public static final String DUGME_DIZINDENSEC="G.310";
     public static final String DUGME_DN_OLUSTUR="G.315";
     public static final String DUGME_EKLE="G.320";
     public static final String DUGME_ETKINLESTIR="G.325";
     public static final String DUGME_EVET="G.330";
     public static final String DUGME_GUNCELLE="G.335";
     public static final String DUGME_HAYIR="G.340";
     public static final String DUGME_INIYAZ="G.345";
     public static final String DUGME_KAYDET="G.350";
     public static final String DUGME_KOPYA_CIKAR="G.355";
     public static final String DUGME_ONAYLA="G.360";
     public static final String DUGME_PKCS10ISTEGI="G.365";
     public static final String DUGME_SERTIFIKAAL="G.370";
     public static final String DUGME_SERVISI_BASLAT="G.375";
     public static final String DUGME_SERVISI_DURDUR="G.380";
     public static final String DUGME_SIL="G.385";
     public static final String DUGME_SILYAYINLA="G.390";
     public static final String DUGME_TAMAM="G.395";
     public static final String DUGME_TARIH_SEC="G.400";
     public static final String DUGME_TEMIZLE="G.401";
     public static final String DUGME_TUMUNUKALDIR="G.405";
     public static final String DUGME_TUMUNUSEC="G.410";
     public static final String DUGME_VAZGEC="G.415";
     public static final String DUGME_VT_TEMIZLE="G.420";
     public static final String DUGME_YENILE="G.425";
     public static final String DURUM="G.426";
     public static final String ECDSAANAHTARBOYU_0_BILINMIYOR="G.430";
     public static final String EKLENECEK_NOKTA_BULUNAMADI="G.435";
     public static final String EKLERKEN_AYNI_TKA="G.440";
     public static final String EKLERKEN_BILINMEYEN_HATA="G.445";
     public static final String EKLERKEN_EKSIK_ATTRIBUTE="G.450";
     public static final String ENCIPHERONLY="G.451";
     public static final String ENCODEDECODE_HATASI="G.455";
     public static final String GENDER="G.456";
     public static final String GETATTRIBUTELIST_YANLIS_ARGUMANLA_CAGRILDI="G.460";
     public static final String GIVENNAME="G.461";
     public static final String GUNCELLEME="G.465";
     public static final String GUNCELLEME_IS_HATALI="G.470";
     public static final String GUNCELLEMEDE_HATA="G.475";
     public static final String GUNCELLENECEK_TKA_0_BULUNAMADI="G.480";
     public static final String GUNCELLERKEN_CIKACAK_DEGER_BULUNAMADI="G.485";
     public static final String GUNCELLERKEN_DEGER_EKLENEMIYOR="G.490";
     public static final String HATA="G.495";
     public static final String HATALI="G.496";
     public static final String HATASIZ="G.497";
     public static final String HAYIR="G.500";
     public static final String HIC_GELMEMELI="G.505";
     public static final String HSM_INI_0_OKUNAMADI="G.510";
     public static final String HSM_INIDEN_DEGERLER_OKUNAMADI="G.515";
     public static final String HSME_ULASILAMADI="G.520";

     public static final String ILKLENDIRME_ANAHTAR_GRUP_NO="G.521.5";
     public static final String ILKLENDIRME_BETIGI="G.521.10";
     public static final String ILKLENDIRME_KART_TIPI="G.521.15";
     public static final String ILKLENDIRME_SABLON_ADI="G.521.20";
     public static final String ILKLENDIRME_SABLON_MAJOR_SURUMU="G.521.25";
     public static final String ILKLENDIRME_SABLON_MINOR_SURUMU="G.521.30";

     public static final String IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU="G.524";
     public static final String IMZA_DOGRULAMA_HATASI="G.525";
     public static final String IMZALAMA="G.536";
     public static final String IMZALAMA_HATASI="G.530";
     public static final String IMZALAMADA_HATA="G.535";
     public static final String IMZALG_0_BILINMIYOR="G.540";
     public static final String IMZALG_0_ICIN_1_BOYUNDAANAH_OLMAZ="G.545";
     public static final String IMZALG_0_ICIN_OZET_1_BILINMIYOR="G.550";
     public static final String IMZALG_YOK="G.555";
     public static final String IMZALGOZET_YOK="G.560";
     public static final String IMZANAH_HATALI="G.565";
     public static final String IMZANAHBOY_YOK="G.570";
     public static final String IMZSERTSERINO_HATALI="G.575";
     public static final String INIT_EDILMEMIS="G.585";
     public static final String INIT_EDILMIS="G.590";
     public static final String INVALID_CERT="G.595";
     public static final String IP="G.596";
     public static final String IP_PORTTA_DIZIN_BULUNAMADI="G.600";
     public static final String ISLEM_TIPI="G.601";
     public static final String ISSUER="G.602";
     public static final String KART_TIPI="G.603";
     public static final String KART_TIPI_VE_PAROLA="G.604";
     public static final String KART_TIPLERI_OKUNAMADI="G.605";
     public static final String KARTA_LOGIN_OLUNAMADI="G.606";
     public static final String KARTA_ULASILAMADI="G.610";
     public static final String KARTINIZ_TAKILIMI="G.615";
     public static final String KARTTA_0_ANAHTARI_VAR="G.620";
     public static final String KARTTA_0_ANAHTARI_YOK="G.625";
     public static final String KARTTA_HATA_0="G.630";
     public static final String KARTTA_SIFRECOZME_HATA="G.635";
     public static final String KARTTA_SIFRELEME_YAPILAMADI="G.640";
     public static final String KARTTAN_BILGILER_ALINAMADI="G.645";
     public static final String KAYDEDILECEK_YER="G.645.1";
     public static final String KEYAGREEMENT="G.646";
     public static final String KEYCERTSIGN="G.647";
     public static final String KEYENCIPHERMENT="G.648";
     public static final String KIMLIK="G.648.1";
     public static final String KIMLIKNO="G.649";
     public static final String KIRMIZI="G.650";
     public static final String KRIPTO_GENEL_HATA="G.652";

     public static final String KRIPTOMOD_ANAH_BUTUN="G.651.1";
     public static final String KRIPTOMOD_ANAH_PARCALI_MOFN="G.651.3";

     public static final String KRIPTOMOD_HSM="G.651.5";

     public static final String KRITIKLOGOLUSTURULAMADI="G.653";
     public static final String KULLANIM_BILINMIYOR="G.655";
     public static final String LABEL_KARTPAROLASINIGIRINIZ="G.660";
     public static final String LABEL_SADECETAKILISLOTLAR="G.665";
     public static final String LABEL_SLOTLISTESI="G.670";
     public static final String LDAP_TIP_HATALI="G.675";
     public static final String LDAPA_BAGLANILAMADI="G.680";
     public static final String LDAPA_BAGLANTI_BULUNAMADI="G.685";
     public static final String LISTE_GIRILIRKEN_HATA="G.686.1";
     public static final String LISTELE="G.686.2";
     public static final String LOCALITYNAME="G.687";
     public static final String LOG="G.688.1";
     public static final String LOG_METNI="G.688.2";
     public static final String LOG_MODULU="G.688.3";
     public static final String LOG_OLAYTIPI="G.688.4";
     public static final String LOG_SONUCU="G.688.5";
     public static final String LOG_TARIHI="G.688.6";
     public static final String LUTFEN_DOLDURUNUZ="G.689";
     public static final String MAVI="G.690";
     public static final String MESAJ_AKILLIKARTBILGILERIALINAMADI="G.691";
     public static final String MESAJ_ANAHTAR_PARCALAR_BOLUNUYOR="G.695";
     public static final String MESAJ_BILINMEYENHATA="G.700";
     public static final String MESAJ_BOSALANKALMASIN="G.705";
     public static final String MESAJ_DOSYABULUNAMADI="G.710";
     public static final String MESAJ_ENAZ_0_BUYUKHARFOLMALI="G.715";
     public static final String MESAJ_ENAZ_0_KARAKTEROLMALI="G.720";
     public static final String MESAJ_ENAZ_0_KUCUKHARFOLMALI="G.725";
     public static final String MESAJ_ENAZ_0_OZELKARAKTEROLMALI="G.730";
     public static final String MESAJ_ENAZ_0_RAKAMOLMALI="G.735";
     public static final String MESAJ_ENCOK_0_KARAKTEROLMALI="G.736";
     public static final String MESAJ_ESNEKZDYOK = "G.737";
     public static final String MESAJ_HOSTADIBILINMIYOR="G.740";
     public static final String MESAJ_HOSTHATALI="G.745";
     public static final String MESAJ_IMPL="G.750";
     public static final String MESAJ_IMPLURETICI="G.755";
     public static final String MESAJ_IMPLVERSION="G.760";
     public static final String MESAJ_IPHATALI="G.765";
     public static final String MESAJ_LISANSBASLAMAMIS="G.770";
     public static final String MESAJ_LISANSBITMIS="G.775";
     public static final String MESAJ_SPEC="G.780";
     public static final String MESAJ_SPECURETICI="G.785";
     public static final String MESAJ_SPECVERSION="G.790";
     public static final String MOBIL_IMZA_KULLANICI_IPTALI = "G.791.1";
     public static final String MOBIL_COKLU_IMZA_KULLANICI_IPTALI = "G.791.2";
     public static final String MOBIL_IMZA_ZAMAN_ASIMI = "G.791.3";
     public static final String MOD_0_BILINMIYOR="G.795";
     public static final String MOD_SECILMEMIS="G.800";
     public static final String MOD_SECILMIS="G.805";
     public static final String MOFN_KART_OKUNMUS="G.810";
     public static final String NESNE_OKUNAMADI="G.811";
     public static final String NO="G.812";
     public static final String NO_DBCONNECTION="G.815";
     public static final String NO_SUCH_CARD_NO="G.820";
     public static final String NO_SUCH_USER="G.825";
     public static final String NONREPUDIATION="G.826";
     public static final String NUMARA="G.827";
     public static final String OID_0_BILINMIYOR="G.830";
     public static final String OID_ADI="G.831";
     public static final String OID_DEGERI="G.832";
     public static final String ONCA_0_FONKSIYONU_CAGRILMALI="G.835";
     public static final String ONCEKI="G.835.1";
     public static final String ONIZLEME="G.835.2";
     public static final String ORGANIZATIONAL_UNIT_NAME="G.836";
     public static final String ORGANIZATION_NAME="G.837";
     public static final String OZETALG_0_BILINMIYOR="G.840";
     public static final String WRAPALG_BILINMIYOR="G.841";
     public static final String AGREEMENTALG_BILINMIYOR = "G.843";
     public static final String DERIVATIONALG_BILINMIYOR = "G.844";
     public static final String PADDING_BILINMIYOR="G.845";
     public static final String PADDING_HATALI="G.850";
     public static final String PARAMETRELER="G.853";
     public static final String PAROLA="G.854";
     public static final String PAROLA_HATALI="G.855";
     public static final String PAROLA_SURESI_DOLMUS="G.860";
     public static final String PASIF="G.864";
     public static final String PASIF_YAP="G.865";
     public static final String PFX_UZANTISI="G.866";
     public static final String PLACEOFBIRTH="G.867";
     public static final String PRIVBYTES_HATALI="G.870";
     public static final String PRIVKEY_OLUSTURULAMADI="G.875";
     public static final String PSEUDONYM="G.876";
     public static final String PUBBYTES_HATALI="G.880";
     public static final String PUBKEY_YOK="G.885";
     public static final String PUBLICKEY_OKUNAMADI="G.890";
     public static final String RASTGELE="G.891";
     public static final String SONRAKI="G.892";
     public static final String SORU="G.895";
     public static final String STATE_OR_PROVINCE_NAME="G.897";
     public static final String SUBJECT="G.896";
     public static final String RB_HATA_44105="G.905";
     public static final String RB_HATA_44106="G.910";
     public static final String ROLE="G.911";
     public static final String ROLU_KAYITCI_OLAN_IMZ_SERT_BULUNAMADI="G.915";
     public static final String SCHEMA_HATASI="G.920";
     public static final String SEARCH_KONTROL_HATALI="G.925";
     public static final String SERTIFIKA_ALMA="G.926";
     public static final String SERTIFIKA_IPTALASKI="G.926.1";
     public static final String SERTIFIKA_IPTAL_TALEBI="G.926.2";
     public static final String SERTIFIKA_ISLEMLERI="G.926.3";
     public static final String SERTIFIKA_TALEP="G.926.4";
     public static final String SIFALG_0_BILINMIYOR="G.930";
     public static final String SIFALG_0_ICIN_1_BOYUNDAANAH_OLMAZ="G.935";
     public static final String SIFALG_YOK="G.940";
     public static final String SIFANAH_HATALI="G.945";
     public static final String SIFANAHBOY_YOK="G.950";
     public static final String SIFRE_COZME_HATASI="G.951";
     public static final String SIFRELEME_HATASI="G.952";
     public static final String SIFSERTSERINO_OKUMA_HATASI="G.955";
     public static final String SILERKEN_BILINMEYEN_HATA="G.960";
     public static final String SILINECEK_TKA_HATALI="G.965";
     public static final String SILINECEK_TKADA_ENTRY_VAR="G.970";
     public static final String SIMALG_BILINMIYOR="G.975";
     public static final String SIMALG_OID_BILINMIYOR="G.980";
     public static final String SIMMOD_BILINMIYOR="G.985";
     public static final String SOYAD="G.990";
     public static final String SUNIMZOBJE_DOGRU_INIT_EDILMEMIS="G.995";
     public static final String TC_KIMLIK_NO="G.1005";
     public static final String TIP="G.1006";
     public static final String TOOLTIP_ISLEMDEVAMEDIYOR="G.1010";
     public static final String TUM_DOSYALAR="G.1011";
     public static final String TUM_TABLOYU_IMZALA="G.1012";
     public static final String UPN="G.1013";
     public static final String UST_DIZIN="G.1014";
     public static final String UYARI="G.1015";
     public static final String VARSAYILAN="G.1016";
     public static final String VERILEN_ARAMA_NOKTASI_NULL_VEYA_BOS="G.1020";
     public static final String VERSION="G.1021";
     public static final String VT_ERROR="G.1025";
     public static final String X400NAME="G.1026";
     public static final String YAPILANDIRMADA_EKLENDI="G.1027";
     public static final String YENI_DIZIN_OLUSTUR="G.1028";
     public static final String YESIL="G.1029";
     public static final String YON_SAY_FARKLI="G.1030";
     public static final String YONSAY_MIN_0_OLMALI="G.1035";
     public static final String YONETICI_EPOSTA="G.1036";
     
     public static final String SERTIFIKA="G.1039";
     public static final String SERTIFIKA_AUTHORITY_KEY_ID="G.1040";
     public static final String SERTIFIKA_AIA="G.1041";
     public static final String SERTIFIKA_CDP="G.1042";
     public static final String SERTIFIKA_SAN="G.1043";
     public static final String SERTIFIKA_QC_STATEMENTS="G.1044";
     public static final String SERTIFIKA_BASIC_CONS="G.1045";
     public static final String SERTIFIKA_SUBJECT_DIRECTORY_ATTR="G.1046";
     public static final String ISSUER_ALT_NAME = "G.1047";
     public static final String SERTIFIKA_KEY_USAGE="G.1050";
     public static final String SERTIFIKA_SUBJECT_KEY_ID="G.1055";
     public static final String SERTIFIKA_SERIAL_NUMBER="G.1060";
     public static final String SERTIFIKA_POLICY_MAPPINGS="G.1065";
     public static final String SERTIFIKA_POLICIES="G.1070";
     public static final String SERTIFIKA_EXTENDED_KEY_USAGE="G.1071";
     public static final String SIL_ISSUING_DIST_POINT="G.1074";
     public static final String SIL_REASON_CODE="G.1075";
     public static final String SIL_REASON_CODE_IMZ="G.1076";
     public static final String SIL_REASON_CODE_SIF="G.1077";
     public static final String SIL_HOLD_INST_CODE="G.1078";
     public static final String EKU_CLIENT_AUTHENTICATION ="G.1081";
     public static final String EKU_SERVER_AUTHENTICATION ="G.1085";
     public static final String EKU_CODE_SIGNING ="G.1090";
     public static final String EKU_EMAIL_PROTECTION ="G.1095";
     public static final String EKU_IPSEC_TUNNEL ="G.1100";
     public static final String EKU_IPSEC_USER ="G.1105";
     public static final String EKU_IPSEC_END_SYSTEM ="G.1110";
     public static final String EKU_OCSP_SIGNING ="G.1115";
     public static final String EKU_TIME_STAMPING ="G.1120";
     public static final String EKU_DVCS ="G.1125";

     // Log Event Type (let) keys
     public static final String key_let_logYazmaGirisimi ="G.k.let.0";
     public static final String key_let_denetlenenOlaylarinDegistirilmesi ="G.k.let.1";
     public static final String key_let_denetlemeLoglariSilmeGirisimi ="G.k.let.2";
     public static final String key_let_sertifikaImzalanmasi ="G.k.let.3";
     public static final String key_let_sertifikaAskiyaAlinmasi ="G.k.let.4";
     public static final String key_let_sertifikaAskidanIndirilmesi ="G.k.let.5";
     public static final String key_let_sertifikaIptalEdilmesi ="G.k.let.6";
     public static final String key_let_sertifikaSablonuOlusturulmasi ="G.k.let.7";
     public static final String key_let_sertifikaSablonuSilinmesi ="G.k.let.8";
     public static final String key_let_sertifikaIstekMesajiGelmesi ="G.k.let.9";
     public static final String key_let_sertifikaAskiyaAlmaIstegi ="G.k.let.10";
     public static final String key_let_sertifikaAskidanIndirmeIstegi ="G.k.let.11";
     public static final String key_let_sertifikaIptalIstegi ="G.k.let.12";
     public static final String key_let_yetkiDegisiklikleri ="G.k.let.13";
     public static final String key_let_sistemParametreleriDegistirilmesi ="G.k.let.14";
     public static final String key_let_yetkiliEkleme ="G.k.let.15";
     public static final String key_let_silOlusturma ="G.k.let.16";
     public static final String key_let_kullaniciOlusturma ="G.k.let.17";
     public static final String key_let_cihazOlusturma ="G.k.let.18";
     public static final String key_let_kullaniciSilinmesi ="G.k.let.19";
     public static final String key_let_cihazSilinmesi ="G.k.let.20";
     public static final String key_let_kullaniciBilgilerindeDegisiklikYapilmasi ="G.k.let.21";
     public static final String key_let_cihazBilgilerindeDegisiklikYapilmasi ="G.k.let.22";
     public static final String key_let_sifrelemeAnahtariGonderilmesi ="G.k.let.23";
     public static final String key_let_sifrelemeAnahtarinaUlasim ="G.k.let.24";
     public static final String key_let_smServisininCalismasi ="G.k.let.25";
     public static final String key_let_smServisininDurmasi ="G.k.let.26";
     public static final String key_let_kontrolMerkezininCalismasi ="G.k.let.27";
     public static final String key_let_kontrolMerkezininDurmasi ="G.k.let.28";
     public static final String key_let_webKayitMakamininCalismasi ="G.k.let.29";
     public static final String key_let_webKayitMakamininDurmasi ="G.k.let.30";
     public static final String key_let_silServisininCalismasi ="G.k.let.31";
     public static final String key_let_silServisininDurmasi ="G.k.let.32";
     public static final String key_let_kayitcininSistemeGirmesi ="G.k.let.33";
     public static final String key_let_kayitcininSistemdenCikmasi ="G.k.let.34";
     public static final String key_let_ksmAnahtarDegisimi ="G.k.let.35";
     public static final String key_let_smAnahtarDegisimiIcinIstekUretilmesi ="G.k.let.36";
     public static final String key_let_smAnahtarDegisimi ="G.k.let.37";
     public static final String key_let_silKonfigurasyonuEklenmesi ="G.k.let.38";
     public static final String key_let_silKonfigurasyonuGuncellenmesi ="G.k.let.39";
     public static final String key_let_silSablonuEklenmesi ="G.k.let.40";
     public static final String key_let_silSablonuGuncellenmesi ="G.k.let.41";
     public static final String adresine_baglanilamadi = "infra.tsclient.1";




     public static String mesaj (String aHataKodu)
     {
          String msj = null;
          try
          {
               msj = msRB.getString(aHataKodu);
          } catch (Exception e)
          {
               System.out.println(aHataKodu+" kodlu bundle bulunamadi, ingilizce bundledaki deger donulecek.");
               logger.error(aHataKodu +" kodlu bundle bulunamadi, ingilizce bundledaki deger donulecek.", e);
               String path = "tr.gov.tubitak.uekae.esya.genel.bundle.GenelBundle";
               try
               {
                    msj = ResourceBundle.getBundle(path, new Locale("en")).getString(aHataKodu);
               } catch (RuntimeException e1)
               {
                    msj = "Bundle not found for key "+aHataKodu;
                    logger.error("Bundle not found for key "+ aHataKodu, e1);
               }
          }
          return msj;
     }


     public static String mesaj (String aHataKodu, String[] aArglar)
     {
          MessageFormat formatter = new MessageFormat(mesaj(aHataKodu));
          return (formatter.format(aArglar));
     }
}

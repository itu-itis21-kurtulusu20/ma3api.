using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.tsl.util
{
    public class TSL_DIL
    {
        public const string WINDOWSTITLE = "TSL Yöneticisi";

        public const string ENTRY = "0 Değer";
        public const string ADD_MULTIPLE_VALUES = "Çoklu Değer Ata";
        public const string ADD_MULTIPLE_POSTAL_ADDRESS = "Çoklu Posta Adresi Ata";
        public const string ADD_MULTIPLE_ELECTRONIC_ADDRESS = "Çoklu Elektronik Adres Ata";
        public const string ADD_CERTIFICATE = "Sertifika Seç";
        public const string CHOOSE_DIGITAL_ID = "Dijital Kimlik Seçiniz...";
        
        //ToolBar
        public const string NEW = "Yeni";
        public const string OPEN = "Aç";
        public const string SAVE = "Kaydet";
        public const string SIGN = "İmzala";
        public const string ABOUT = "Hakkında";
        
        //Context Menu
        public const string ADDTSP = "TSP Ekle";
        public const string REMOVETSP = "TSP Sil";
        public const string ADDSERVICE = "Servis Ekle";
        public const string REMOVESERVICE = "Servis Sil";
        
        //Scheme Information
        //Labels
        public const string SCHEME = "Şema";
        public const string SCHEME_INFORMATION = "Şema Bilgileri";
        public const string TSL_VERSION_ID = "TSL Versiyon Numarası";
        public const string TSL_SEQUENCE_NUMBER = "TSL Sıra Numarası";
        public const string TSL_TYPE = "TSL Tipi";
        public const string SCHEME_OPERATOR_NAME = "Şema Operatörü Adı";
        public const string SCHEME_OPERATOR_POSTAL_ADDRESS = "Şema Operatörü Posta Adresi";
        public const string SCHEME_OPERATOR_ELECTRONIC_ADDRESS = "Şema Operatörü Elektronik Adresi";
        public const string SCHEME_NAME = "Şema İsmi";
        public const string SCHEME_INFORMATION_URI = "Şema Bilgi URI";
        public const string STATUS_DETERMINATION_APPROACH = "Durum Belirleme Yaklaşımı";
        public const string SCHEME_TYPE_COMM_RULES = "Şema Tipi Toplum Kuralları";
        public const string SCHEME_TERRITORY = "Şema Bölgesi";
        public const string POLICY_LEGAL_NOTICE = "TSL Politik/Yasal Bildirimleri";
        public const string HISTORICAL_INFORMATION_PERIOD = "Tarihsel Bilgilendirme Periyodu";
        public const string LIST_ISSUE_DATE_TIME = "Yayınlanma Tarihi";
        public const string NEXT_UPDATE = "Sonraki Güncelleme";
        public const string DISTRIBUTION_POINTS = "Dağıtım Noktaları";

        //TSP Information
        //Labels
        public const string TRUST_SERVICE_PROVIDER = "Güven Hizmeti Sağlayıcısı";
        public const string TRUST_SERVICE_PROVIDER_INFORMATION = "TSP Bilgileri";
        public const string TSP_NAME = "TSP Adı";
        public const string TSP_TRADE_NAME = "TSP Ticari Adı";
        public const string TSP_POSTAL_ADDRESS = "TSP Posta Adresi";
        public const string TSP_ELECTRONİC_ADDRESS = "TSP Elektronik Adresi";
        public const string TSP_INFORMATION_URI = "TSP Bilgi URI";

        //Service Information
        //Labels
        public const string SERVICE = "Servis";
        public const string SERVICE_INFORMATION = "Servis Bilgileri";
        public const string SERVICE_TYPE_IDENTIFIER = "Servis Tipi Belirleyicisi";
        public const string SERVICE_NAME = "Servis Adı";
        public const string DIGITAL_ID = "Dijital Kimlik";
        public const string SERVICE_CURRENT_STATUS = "Servisin Güncel Durumu";
        public const string CURRENT_STATUS_STARTING_DATE = "Güncel Durumun Başlangıç Tarihi";
        public const string SCHEME_SERVICE_DEF_URI = "Şema Servisi Tanımlama URI";
        public const string SERVICE_SUPPLY_POINTS = "Servis Tedarik Notaları";
        public const string TSP_SERVICE_DEF_URI = "TSP Servisi Tanımlama URI";


        //Certificate Viewer
        public const string CERTIFICATEVIEWE_WINDOWSTITLE = "Sertifika Ekle";
        public const string CERTIFICATE_INFORMATION = "Sertifika Bilgileri";
        public const string ISSUER = "Yayıncı";
        public const string SERIAL_NUMBER = "Seri Numarası";
        public const string KEY_USAGE = "Anahtar Kullanımı";
        public const string CHANGE = "Değiştir...";
        public const string OK = "Tamam";
        public const string CLOSE = "Kapat";

        //Multi Lingual Popup
        public const string MULTILINGUALPOPUP_WINDOWSTITLE = "Çoklu Değer Ekle";

        //Multi Address Popup
        public const string MULTIADDRESSPOPUP_WINDOW_TITLE = "Posta Adresi Ekle";
        public const string STREET = "Sokak Adresi";
        public const string LOCALITY = "Şehir:";
        public const string POSTAL_CODE = "Posta Kodu";
        public const string COUNTRY = "Ülke";
 
        //Multi Address Link Popup
        public const string MULTIADDRESSLINKPOPUP_WINDOWSTITLE = "Elektronik Adres Ekle";
        public const string ADDRESS_TYPE = "Adres Tipi";
        public const string ADDRESS = "Address";
        public const string ADD = "Ekle";
        public const string REMOVE = "Çıkar";

        //Multi URI Popup
        public const string MULTIURIPOPUP_WINDOWSTITLE = "URI Ekle";

        //Remove Node
        public const string REMOVENODE_WINDOWSTITLE = "Emin misiniz?";
        public const string QUESTION = "Seçili elemanı ve bütün alt elemanlarını silmek istediğinizden emin misiniz?";
        public const string YES = "Evet";
        public const string NO = "Hayır";

        //Warning Popup
        public const string WARNINGPOPUP_WINDOWSTITLE = "Uyarı";

        //Messages
        public const string SAVED_SUCCESSFULLY = "TSL Dokümanı başarılı bir şekilde kaydedildi!";
        public const string SIGNED_SAVED_SUCCESSFULLY = "TSL Dokümanı başarılı bir şekilde imzalandı ve kaydedildi!";
        public const string UNKNOWN_ERROR = "Bilinmeyen bir hata oluştu!";
        public const string NOT_SAVED = "TSL Dokümanı kaydedileMEdi!";
        public const string NOT_SIGNED = "TSL Dokümanı imzalanaMAdı!";
        public const string ERROR_MESSAGE = "Hata Mesajı: ";
        public const string NODE_CANNOT_BE_FOUND = ": Aranılan node bulunamadı!";
        public const string NODE_MORE_THAN_ONE = ": Aranılan node birden fazla olamaz!";
        public const string DOCUMENT_IS_SIGNED = "İmzalanmak istenen TSL dokümanı zaten imzalı!";
        public const string CONFIG_FILE_COULDNOT_READ = "tsl-config.xml dosyasindan değerler okunamadi!";
        public const string OPERATION_CANCELLED = "İşlem İptal Edildi!";

        //SmartCardSelector
        public const string SMARTCARDSELECTOR_WINDOWSTITLE = "Kart Okuyucu Seçiniz";
        public const string SMARTCARDSELECTOR_WINDOWSTITLE2 = "Sertifika Seçiniz";
        public const string TERMINAL_LIST = "Okuyucu Listesi:";
        public const string CERTIFICATE_LIST = "Sertifika Listesi:";
        public const string CARD_PIN = "Kart Parolası:";

        //SplashScreen
        public const string SPLASHSCREEN_WINDOWSTITLE = "Başlıyor...";

        //AnimateImage
        public const string ANIMATEIMAGE_WINDOWSTITLE = "İmzalanıyor...";

    }
}

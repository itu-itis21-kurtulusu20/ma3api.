package tr.gov.tubitak.uekae.esya.api.common.bundle;



public class GUILanguageSettings
{


  public GUILanguageSettings()
  {
  }
/*
  static public void turkcelestir()
  {
    UIManager.put("FileChooser.saveButtonText","Kaydet");
    UIManager.put("FileChooser.saveButtonToolTipText","Kaydet");

    UIManager.put("FileChooser.openButtonText","Aç");
    UIManager.put("FileChooser.openButtonToolTipText","Aç");

    UIManager.put("FileChooser.cancelButtonText","Vazgeç");
    UIManager.put("FileChooser.cancelButtonToolTipText","Vazgeç");

    UIManager.put("FileChooser.acceptAllFileFilterText","Tüm Dosyalar(*.*)");
    UIManager.put("FileChooser.fileNameLabelText","Dosya Ad\u0131");
    UIManager.put("FileChooser.filesOfTypeLabelText","Dosya Türü");
    UIManager.put("FileChooser.lookInLabelText","Bak\u0131lacak Yer :");
    UIManager.put("FileChooser.saveInLabelText","Kaydedilecek Yer");
    UIManager.put("FileChooser.upFolderToolTipText","Üst Dizin");
    UIManager.put("FileChooser.homeFolderToolTipText","Ana Dizin");
    UIManager.put("FileChooser.newFolderToolTipText","Yeni Dizin Olu\u015Ftur");
    UIManager.put("FileChooser.listViewButtonToolTipText","Listele");
    UIManager.put("FileChooser.detailsViewButtonToolTipText","Detaylar");

    UIManager.put("OptionPane.okButtonText","Tamam");
    UIManager.put("OptionPane.yesButtonText","Evet");
    UIManager.put("OptionPane.noButtonText","Hay\u0131r");
    UIManager.put("OptionPane.cancelButtonText","\u0130ptal");

    UIManager.put("ColorChooser.cancelText","\u0130ptal");
    UIManager.put("ColorChooser.okText","Tamam");
    UIManager.put("ColorChooser.previewText","Önizleme");
    UIManager.put("ColorChooser.resetText","Varsay\u0131lan");
    UIManager.put("ColorChooser.rgbRedText","K\u0131rm\u0131z\u0131 (Red)");
    UIManager.put("ColorChooser.rgbBlueText","Mavi (Blue)");
    UIManager.put("ColorChooser.rgbGreenText","Ye\u015Fil (Green)");
    UIManager.put("ColorChooser.hsbRedText","K\u0131rm\u0131z\u0131 (R)");
    UIManager.put("ColorChooser.hsbBlueText","Mavi (B)");
    UIManager.put("ColorChooser.hsbGreenText","Ye\u015Fil (G)");
    UIManager.put("ColorChooser.sampleText","Deneme Yaz\u0131s\u0131  Deneme Yaz\u0131s\u0131");
    UIManager.put("ColorChooser.swatchesNameText","aaaaaaaa");
    UIManager.put("ColorChooser.swatchesRecentText","Önceki:");

  }
*/



  static public void dilAyarla()
  {
//    UIManager.put("FileChooser.saveButtonText",GenelDil.mesaj(GenelDil.DUGME_KAYDET));
//    UIManager.put("FileChooser.saveButtonToolTipText",GenelDil.mesaj(GenelDil.DUGME_KAYDET));
//
//    UIManager.put("FileChooser.openButtonText",GenelDil.mesaj(GenelDil.DUGME_AC));
//    UIManager.put("FileChooser.openButtonToolTipText",GenelDil.mesaj(GenelDil.DUGME_AC));
//
//    UIManager.put("FileChooser.cancelButtonText",GenelDil.mesaj(GenelDil.DUGME_VAZGEC));
//    UIManager.put("FileChooser.cancelButtonToolTipText",GenelDil.mesaj(GenelDil.DUGME_VAZGEC));
//
//    UIManager.put("FileChooser.acceptAllFileFilterText",GenelDil.mesaj(GenelDil.TUM_DOSYALAR));
//    UIManager.put("FileChooser.fileNameLabelText",GenelDil.mesaj(GenelDil.DOSYA_ADI));
//    UIManager.put("FileChooser.filesOfTypeLabelText",GenelDil.mesaj(GenelDil.DOSYA_TURU));
//    UIManager.put("FileChooser.lookInLabelText",GenelDil.mesaj(GenelDil.BAKILACAK_YER));
//    UIManager.put("FileChooser.saveInLabelText",GenelDil.mesaj(GenelDil.KAYDEDILECEK_YER));
//    UIManager.put("FileChooser.upFolderToolTipText",GenelDil.mesaj(GenelDil.UST_DIZIN));
//    UIManager.put("FileChooser.homeFolderToolTipText",GenelDil.mesaj(GenelDil.ANA_DIZIN));
//    UIManager.put("FileChooser.newFolderToolTipText",GenelDil.mesaj(GenelDil.YENI_DIZIN_OLUSTUR));
//    UIManager.put("FileChooser.listViewButtonToolTipText",GenelDil.mesaj(GenelDil.LISTELE));
//    UIManager.put("FileChooser.detailsViewButtonToolTipText",GenelDil.mesaj(GenelDil.DETAYLAR));
//
//    UIManager.put("OptionPane.okButtonText",GenelDil.mesaj(GenelDil.DUGME_TAMAM));
//    UIManager.put("OptionPane.yesButtonText",GenelDil.mesaj(GenelDil.DUGME_EVET));
//    UIManager.put("OptionPane.noButtonText",GenelDil.mesaj(GenelDil.DUGME_HAYIR));
//    UIManager.put("OptionPane.cancelButtonText",GenelDil.mesaj(GenelDil.DUGME_VAZGEC));
//
//    UIManager.put("ColorChooser.cancelText",GenelDil.mesaj(GenelDil.DUGME_VAZGEC));
//    UIManager.put("ColorChooser.okText",GenelDil.mesaj(GenelDil.DUGME_TAMAM));
//    UIManager.put("ColorChooser.previewText",GenelDil.mesaj(GenelDil.ONIZLEME));
//    UIManager.put("ColorChooser.resetText",GenelDil.mesaj(GenelDil.VARSAYILAN));
//    UIManager.put("ColorChooser.rgbRedText",GenelDil.mesaj(GenelDil.KIRMIZI));
//    UIManager.put("ColorChooser.rgbBlueText",GenelDil.mesaj(GenelDil.MAVI));
//    UIManager.put("ColorChooser.rgbGreenText",GenelDil.mesaj(GenelDil.YESIL));
//    UIManager.put("ColorChooser.hsbRedText",GenelDil.mesaj(GenelDil.KIRMIZI));
//    UIManager.put("ColorChooser.hsbBlueText",GenelDil.mesaj(GenelDil.MAVI));
//    UIManager.put("ColorChooser.hsbGreenText",GenelDil.mesaj(GenelDil.YESIL));
//    UIManager.put("ColorChooser.sampleText",GenelDil.mesaj(GenelDil.DENEME_YAZISI));
//    UIManager.put("ColorChooser.swatchesNameText","aaaaaaaa");
//    UIManager.put("ColorChooser.swatchesRecentText",GenelDil.mesaj(GenelDil.ONCEKI));

  }
//  public static void main(String[] args) {
//    System.out.println(UIManager.get("OptionPane.cancelButtonText"));
//    //Bu sekilde hepsini degil ama bir kismini goruntuleyebiliyoruz.
//    //Hepsini nasil gorecegimizi bulamadim...
//    javax.swing.UIDefaults x = UIManager.getDefaults();
//    java.util.Enumeration e = x.keys();
//    while(e.hasMoreElements())
//    {
//      String el = (String)e.nextElement();
//      if(el.startsWith("FileChooser"))
//        System.out.println(el);
//    }
//
//    x = UIManager.getDefaults();
//    e = x.keys();
//    while(e.hasMoreElements()) {
//      String el = (String) e.nextElement();
//      if(el.startsWith("FileChooser"))
//        System.out.println(el);
//    }
//
//  }

}

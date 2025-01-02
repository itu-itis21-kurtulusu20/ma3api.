package tr.gov.tubitak.uekae.esya.api.pades.example.optional;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.pades.ImageInfo;
import tr.gov.tubitak.uekae.esya.api.pades.SignaturePanel;
import tr.gov.tubitak.uekae.esya.api.pades.TextInfo;
import tr.gov.tubitak.uekae.esya.api.pades.example.PadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.*;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

import java.awt.*;
import java.io.*;


// visibleSignatureWithTextAndImage1() ve visibleSignatureWithTextAndImage2() fonksiyonlarında eklemek istediğiniz yazı pdf'e metin olarak ekleniyor. Burada yapabileceğiniz tasarımlar kısıtlı (örneğin resim içine yazı gömmek gibi). Yalnız metin görünürlük kalitesi yüksektir.
// visibleSignatureWithImage() fonksiyonunda sadece resim eklenmektedir.
// visibleSignatureWithText() fonksiyonunda sadece yazı metin olarak eklenmektedir.
// visibleSignatureWithEmbeddedTextInImage() fonksiyonunda eklemek istediğiniz yazı pdf'e resim olarak ekleniyor. Burada istediğiniz tasarımı yapabilirsiniz. Yalnız metin görünürlük kalitesi yakınlaştırmada düşüktür.
public class VisibleSignatureExample extends PadesSampleBase {

    @Test
    public void visibleSignatureWithTextAndImage1() throws Exception {

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // yeni satıra geçmek için "\n" kullanabilirsiniz.
        String visibleText = "Bu belge "+ eCertificate.getSubject().getCommonNameAttribute() + "\ntarafından \nelektronik olarak imzalanmıştır.";

        // x ve y koordinatları pdf dosyasına eklenecek görünümün başlangıç noktasını belirlemek içindir.
        // width ve height değerleri ile belirlenen başlangıç noktasından dikdörtgen oluşturulur.
        SignaturePanel signaturePanel = new SignaturePanel(1,25, 500, 800, 50);

        int width = 225;
        int height = 50;
        int fontSize = 12;

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        // width ve height ile resmin genişliğini ve uzunluğunu belirleriz.
        ImageInfo image = new ImageInfo(FileUtil.readBytes("EnterTheImagePath"), 0, 0, width, height);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        TextInfo text = new TextInfo(visibleText, new File("C:\\Windows\\Fonts\\Arial.ttf"), fontSize, Color.black, width+5, height-fontSize);

        VisibleSignature visibleSignature = new VisibleSignature();
        visibleSignature.setSignaturePanel(signaturePanel);
        visibleSignature.setImageInfo(image);
        visibleSignature.setTextInfo(text);

        PAdESContainer padesContainer = (PAdESContainer)SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestFile()), createContext());

        // add signature
        PAdESSignature signature = (PAdESSignature)padesContainer.createSignature(eCertificate);
        signature.setVisibleSignature(visibleSignature);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);
        signature.sign(signer);

        padesContainer.write(new FileOutputStream(getTestDataFolder() + "signed-visible.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestDataFolder() + "signed-visible.pdf"), createContext());
        ContainerValidationResult validationResult = readContainer.verifyAll();
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void visibleSignatureWithTextAndImage2() throws Exception {

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // yeni satıra geçmek için "\n" kullanabilirsiniz.
        String visibleText = "Bu belge "+ eCertificate.getSubject().getCommonNameAttribute() + "\ntarafından \nelektronik olarak imzalanmıştır.";

        // x ve y koordinatları pdf dosyasına eklenecek görünümün başlangıç noktasını belirlemek içindir.
        // width ve height değerleri ile belirlenen başlangıç noktasından dikdörtgen oluşturulur.
        SignaturePanel signaturePanel = new SignaturePanel(1,25, 500, 800, 200);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        // width ve height ile resmin genişliğini ve uzunluğunu belirleriz.
        ImageInfo image = new ImageInfo(FileUtil.readBytes("EnterTheImagePath"), 0, 100, 300, 100);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        TextInfo text = new TextInfo(visibleText, new File("C:\\Windows\\Fonts\\Arial.ttf"), 12, Color.black, 0, 80);

        VisibleSignature visibleSignature = new VisibleSignature();
        visibleSignature.setSignaturePanel(signaturePanel);
        visibleSignature.setImageInfo(image);
        visibleSignature.setTextInfo(text);

        PAdESContainer padesContainer = (PAdESContainer)SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestFile()), createContext());

        // add signature
        PAdESSignature signature = (PAdESSignature)padesContainer.createSignature(eCertificate);
        signature.setVisibleSignature(visibleSignature);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);
        signature.sign(signer);

        padesContainer.write(new FileOutputStream(getTestDataFolder() + "signed-visible.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestDataFolder() + "signed-visible.pdf"), createContext());
        ContainerValidationResult validationResult = readContainer.verifyAll();
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }


    @Test
    public void visibleSignatureWithImage() throws Exception {

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        int width = 225;
        int height = 50;

        // x ve y koordinatları pdf dosyasına eklenecek görünümün başlangıç noktasını belirlemek içindir.
        // width ve height değerleri ile belirlenen başlangıç noktasından dikdörtgen oluşturulur.
        SignaturePanel signaturePanel = new SignaturePanel(1,25, 500, width, height);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        // width ve height ile resmin genişliğini ve uzunluğunu belirleriz.
        ImageInfo image = new ImageInfo(FileUtil.readBytes("EnterTheImagePath"), 0, 0, width, height);

        VisibleSignature visibleSignature = new VisibleSignature();
        visibleSignature.setSignaturePanel(signaturePanel);
        visibleSignature.setImageInfo(image);

        PAdESContainer padesContainer = (PAdESContainer)SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestFile()), createContext());

        // add signature
        PAdESSignature signature = (PAdESSignature)padesContainer.createSignature(eCertificate);
        signature.setVisibleSignature(visibleSignature);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);
        signature.sign(signer);

        padesContainer.write(new FileOutputStream(getTestDataFolder() + "signed-visible.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestDataFolder() + "signed-visible.pdf"), createContext());
        ContainerValidationResult validationResult = readContainer.verifyAll();
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void visibleSignatureWithText() throws Exception {

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // yeni satıra geçmek için "\n" kullanabilirsiniz.
        String visibleText = "Bu belge "+ eCertificate.getSubject().getCommonNameAttribute() + "\ntarafından \nelektronik olarak imzalanmıştır.";

        int height = 100;
        int fontSize = 12;

        // x ve y koordinatları pdf dosyasına eklenecek görünümün başlangıç noktasını belirlemek içindir.
        // width ve height değerleri ile belirlenen başlangıç noktasından dikdörtgen oluşturulur.
        SignaturePanel signaturePanel = new SignaturePanel(1,25, 500, 225, height);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        TextInfo text = new TextInfo(visibleText, new File("C:\\Windows\\Fonts\\Arial.ttf"), fontSize, Color.black, 0, height-fontSize);

        VisibleSignature visibleSignature = new VisibleSignature();
        visibleSignature.setSignaturePanel(signaturePanel);
        visibleSignature.setTextInfo(text);

        PAdESContainer padesContainer = (PAdESContainer)SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestFile()), createContext());

        // add signature
        PAdESSignature signature = (PAdESSignature)padesContainer.createSignature(eCertificate);
        signature.setVisibleSignature(visibleSignature);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);
        signature.sign(signer);

        padesContainer.write(new FileOutputStream(getTestDataFolder() + "signed-visible.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestDataFolder() + "signed-visible.pdf"), createContext());
        ContainerValidationResult validationResult = readContainer.verifyAll();
        // close the container
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }


    @Test
    public void visibleSignatureWithEmbeddedTextInImage() throws Exception {

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        String visibleText = "Bu belge "+ eCertificate.getSubject().getCommonNameAttribute() + "tarafından elektronik olarak imzalanmıştır.";

        // x ve y koordinatları pdf dosyasına eklenecek görünümün başlangıç noktasını belirlemek içindir.
        // width ve height değerleri ile belirlenen başlangıç noktasından dikdörtgen oluşturulur.
        SignaturePanel signaturePanel = new SignaturePanel(1,0, 500, 800, 300);

        byte[] imageByte = VisibleSignatureImageCreator.createImage(new FileInputStream("EnterTheImagePath"), visibleText, signaturePanel,150, 150);

        // x ve y koordinatları yukarıda oluşturulan siganature panel'in içindeki başlangıç noktasını belirlemek için kullanılır.
        // width ve height ile resmin genişliğini ve uzunluğunu belirleriz.
        ImageInfo image = new ImageInfo(imageByte, 0, 0, signaturePanel.getWidth(), signaturePanel.getHeight());

        VisibleSignature visibleSignature = new VisibleSignature();
        visibleSignature.setSignaturePanel(signaturePanel);
        visibleSignature.setImageInfo(image);

        PAdESContainer padesContainer = (PAdESContainer)SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestFile()), createContext());

        // add signature
        PAdESSignature signature = (PAdESSignature)padesContainer.createSignature(eCertificate);
        signature.setVisibleSignature(visibleSignature);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);
        signature.sign(signer);

        padesContainer.write(new FileOutputStream(getTestDataFolder() + "signed-visible.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(
                getTestDataFolder() + "signed-visible.pdf"), createContext());
        ContainerValidationResult validationResult = readContainer.verifyAll();
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }
}

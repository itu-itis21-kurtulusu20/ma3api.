package dev.esya.api.pades.pdfbox;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.io.FileInputStream;
import java.util.List;

public class T05_SignatureType extends PAdESBaseTest{


    @Test
    public void getSignatureType() throws Exception {

        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-xlong-xlong-pdfBox.pdf");
        signatureContainer.read(fis);

        List<Signature> signatures = signatureContainer.getSignatures();

        for (Signature s : signatures) {

            PAdESSignature dssFor = (PAdESSignature)s;
            SignatureType signatureType = dssFor.getSignatureType();
            String pdfFieldName = dssFor.getPdfFieldName();

            System.out.println(pdfFieldName + "->" + signatureType);
        }

        // close the container
        signatureContainer.close();
    }


}

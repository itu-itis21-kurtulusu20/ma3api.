package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.pades.SignaturePanel;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.VisibleSignature;

import java.awt.*;
import java.io.*;
import java.util.List;

public class PDFBoxUtil {

    public static COSStream getAsStream(COSBase baseElement){

        COSStream cosStream = null;

        COSObject streamObject = (COSObject) baseElement;
        if(streamObject.getObject() instanceof COSStream)
            cosStream = (COSStream) streamObject.getObject();

        return cosStream;
    }

    public static byte[] getStreamBytes(COSStream cosStream){

        byte[] streamBytes = null;

        if(cosStream != null)
        {
            try {
                InputStream is = cosStream.createInputStream();
                streamBytes = StreamUtil.readAll(is);
            }catch (IOException e) {
                new ESYAException("Error in getting stream bytes..");
            }
        }
        return streamBytes;
    }

    public static COSArray createCOSArray(List<? extends BaseASNWrapper> wrappers) {

        Iterable<byte[]> data = PAdESUtil.convert(wrappers);

        COSArray cosArray = new COSArray();
        cosArray.setNeedToBeUpdated(true);

        if (data != null) {
            for (byte[] aData : data) {
                cosArray.add(createCOSStream(aData));
            }
        }
        return cosArray;
    }

    public static COSStream createCOSStream(byte[] data){

        COSStream cosStream = new COSStream();
        cosStream.setNeedToBeUpdated(true);

        final OutputStream unfilteredStream;
        try {
            unfilteredStream = cosStream.createOutputStream(COSName.FLATE_DECODE);
            unfilteredStream.write(data);
            unfilteredStream.flush();
            unfilteredStream.close();
        } catch (IOException e) {
            throw new ESYARuntimeException("PDFBox Error!", e);
        }

        return cosStream;
    }


    public static InputStream createVisualSignatureTemplate(PDDocument srcDoc, VisibleSignature visibleSignature) throws IOException
    {
        PDRectangle rect = PDFBoxUtil.createSignatureRectangle(srcDoc, visibleSignature.getSignaturePanel());

        int pageNum = visibleSignature.getSignaturePanel().getPageNum() - 1;

        try (PDDocument doc = new PDDocument())
        {
            PDPage page = new PDPage(srcDoc.getPage(pageNum).getMediaBox());
            doc.addPage(page);
            PDAcroForm acroForm = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(acroForm);
            PDSignatureField signatureField = new PDSignatureField(acroForm);
            PDAnnotationWidget widget = signatureField.getWidgets().get(0);
            List<PDField> acroFormFields = acroForm.getFields();
            acroForm.setSignaturesExist(true);
            acroForm.setAppendOnly(true);
            acroForm.getCOSObject().setDirect(true);
            acroFormFields.add(signatureField);

            widget.setRectangle(rect);

            PDStream stream = new PDStream(doc);
            PDFormXObject form = new PDFormXObject(stream);
            PDResources res = new PDResources();
            form.setResources(res);
            form.setFormType(1);
            PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
            form.setBBox(bbox);

            PDAppearanceDictionary appearance = new PDAppearanceDictionary();
            appearance.getCOSObject().setDirect(true);
            PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
            appearance.setNormalAppearance(appearanceStream);
            widget.setAppearance(appearance);

            try (PDPageContentStream cs = new PDPageContentStream(doc, appearanceStream))
            {
                cs.addRect(visibleSignature.getSignaturePanel().getX(), visibleSignature.getSignaturePanel().getY(), visibleSignature.getSignaturePanel().getWidth(), visibleSignature.getSignaturePanel().getHeight());
                cs.fill();

                if (visibleSignature.getImageInfo() != null) {
                    cs.saveGraphicsState();
                    byte[] imageByte = visibleSignature.getImageInfo().getImageByte();

                    PDImageXObject img = PDImageXObject.createFromByteArray(doc, imageByte, null);
                    cs.drawImage(img, visibleSignature.getImageInfo().getX(), visibleSignature.getImageInfo().getY(), visibleSignature.getImageInfo().getWidth(), visibleSignature.getImageInfo().getHeight());
                    cs.restoreGraphicsState();
                }

                if(visibleSignature.getTextInfo() != null) {

                    PDFont font = PDType0Font.load(doc, visibleSignature.getTextInfo().getFont());
                    String text = visibleSignature.getTextInfo().getText();
                    Color color = visibleSignature.getTextInfo().getColor();
                    int fontSize = visibleSignature.getTextInfo().getFontSize();

                    String[] line = text.split("\n");

                    // show text
                    cs.beginText();
                    cs.setFont(font, fontSize);
                    cs.setNonStrokingColor(color);
                    cs.newLineAtOffset(visibleSignature.getTextInfo().getX(), visibleSignature.getTextInfo().getY());
                    cs.setLeading(fontSize *1.25f);
                    for (int i = 0; i < line.length; i++) {
                        cs.showText(line[i]);
                        if(line.length-1 == i){
                            break;
                        }
                        cs.newLine();
                    }
                    cs.endText();
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    public static PDRectangle createSignatureRectangle(PDDocument doc, SignaturePanel humanRect)
    {
        float x = (float) humanRect.getX();
        float y = (float) humanRect.getY();
        float width = (float) humanRect.getWidth();
        float height = (float) humanRect.getHeight();
        PDPage page = doc.getPage(0);
        PDRectangle pageRect = page.getCropBox();
        PDRectangle rect = new PDRectangle();
        // signing should be at the same position regardless of page rotation.
        switch (page.getRotation())
        {
            case 90:
                rect.setLowerLeftY(x);
                rect.setUpperRightY(x + width);
                rect.setLowerLeftX(y);
                rect.setUpperRightX(y + height);
                break;
            case 180:
                rect.setUpperRightX(pageRect.getWidth() - x);
                rect.setLowerLeftX(pageRect.getWidth() - x - width);
                rect.setLowerLeftY(y);
                rect.setUpperRightY(y + height);
                break;
            case 270:
                rect.setLowerLeftY(pageRect.getHeight() - x - width);
                rect.setUpperRightY(pageRect.getHeight() - x);
                rect.setLowerLeftX(pageRect.getWidth() - y - height);
                rect.setUpperRightX(pageRect.getWidth() - y);
                break;
            case 0:
            default:
                rect.setLowerLeftX(x);
                rect.setUpperRightX(x + width);
                rect.setLowerLeftY(pageRect.getHeight() - y - height);
                rect.setUpperRightY(pageRect.getHeight() - y);
                break;
        }
        return rect;
    }
}

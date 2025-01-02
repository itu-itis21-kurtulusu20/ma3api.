package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import tr.gov.tubitak.uekae.esya.api.pades.ImageInfo;
import tr.gov.tubitak.uekae.esya.api.pades.SignaturePanel;
import tr.gov.tubitak.uekae.esya.api.pades.TextInfo;

public class VisibleSignature
{
    private SignaturePanel signaturePanel;
    private ImageInfo imageInfo;
    private TextInfo textInfo;

    public SignaturePanel getSignaturePanel() {
        return signaturePanel;
    }

    public void setSignaturePanel(SignaturePanel signaturePanel) {
        this.signaturePanel = signaturePanel;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public TextInfo getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(TextInfo textInfo) {
        this.textInfo = textInfo;
    }


}

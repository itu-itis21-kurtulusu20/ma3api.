package tr.gov.tubitak.uekae.esya.api.pades.example.optional;

import tr.gov.tubitak.uekae.esya.api.pades.SignaturePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class VisibleSignatureImageCreator {

    public static byte [] createImage(InputStream imageStream, String text, SignaturePanel imagePanel, int imageWidth, int imageHeight) throws IOException
    {

        BufferedImage bufferedImage = new BufferedImage(imagePanel.getWidth(), imagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // image kalitesini arttırmak için yapıldı. Siz farklı ayarlar yapabilirsiniz.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, imagePanel.getWidth(), imagePanel.getHeight());

        BufferedImage logo = ImageIO.read(imageStream);

        g2d.drawImage(logo,0,0, imageWidth, imageHeight,null);

        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g2d.setColor(Color.black);
        g2d.drawString(text, imageWidth,  imageHeight/2);
        g2d.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);

        return os.toByteArray();
    }
}

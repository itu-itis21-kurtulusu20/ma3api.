package tr.gov.tubitak.uekae.esya.api.pades;

import java.awt.Color;
import java.io.File;

public class TextInfo {

    private String text;
    private File fontFile;
    private int fontSize;
    private Color color;

    private int x;
    private int y;

    public TextInfo(String text, File fontFile, int fontSize, Color color, int x, int y) {
        this.text = text;
        this.fontFile = fontFile;
        this.fontSize = fontSize;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public File getFont() {
        return fontFile;
    }

    public void setFont(File fontFile) {
        this.fontFile = fontFile;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public File getFontFile() {
        return fontFile;
    }

    public void setFontFile(File fontFile) {
        this.fontFile = fontFile;
    }
}

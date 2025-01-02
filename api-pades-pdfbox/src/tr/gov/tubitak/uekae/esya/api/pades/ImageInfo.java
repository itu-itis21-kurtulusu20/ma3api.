package tr.gov.tubitak.uekae.esya.api.pades;

public class ImageInfo {

    private byte[] imageByte;

    private int x;
    private int y;

    private int width;
    private int height;

    public ImageInfo(byte[] imageByte, int x, int y, int width, int height) {
        this.imageByte = imageByte;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

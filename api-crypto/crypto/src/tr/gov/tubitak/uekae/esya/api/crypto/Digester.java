package tr.gov.tubitak.uekae.esya.api.crypto;



/**
 * @author ayetgin
 */

public interface Digester
{
    void update(byte[] aData);
    void update(byte[] aData, int aOffset, int aLength);
    byte[] digest();
}

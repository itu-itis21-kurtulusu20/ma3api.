package tr.gov.tubitak.uekae.esya.api.crypto.alg;


/**
 * @author ayetgin
 */

public class Padding
{
    public static Padding NONE = new Padding();
    public static Padding PKCS1 = new Padding();
    public static Padding PKCS7 = new Padding();


    protected Padding()
    {
    }
}

package tr.gov.tubitak.uekae.esya.api.asn;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1TagMatchFailedException;

import java.io.IOException;

public class EAsnUtil
{
    // Asn1BerDecodeBuffer.decodeTagAndLength Tag'i kontrol etmiyor, verdiğimiz parametre üzerine TAG bilgisini okuyor.
    // Asn1Tag.SEQUENCE değişkenini parametre olarak verdiğimizde Asn1Tag.SEQUENCE değerlerini bozuyor.
    // Bu yüzden kendi utility fonksiyonumuzu yazdık.
    public static int decodeTagAndLengthWithCheckingTag(Asn1BerDecodeBuffer decodeBuffer, Asn1Tag expectedTag) throws IOException
    {
        Asn1Tag temp = new Asn1Tag();
        int len = decodeBuffer.decodeTagAndLength(temp);
        if(temp.equals(expectedTag))
            return len;
        else
            throw new Asn1TagMatchFailedException(decodeBuffer, expectedTag, temp);
    }

}

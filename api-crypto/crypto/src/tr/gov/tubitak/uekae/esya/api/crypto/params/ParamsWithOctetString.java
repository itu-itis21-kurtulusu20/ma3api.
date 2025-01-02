package tr.gov.tubitak.uekae.esya.api.crypto.params;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

/*
 * Created by sura.emanet on 14.11.2017.
 */
public class ParamsWithOctetString implements AlgorithmParams
{
    public static Asn1OpenType Asn1OctetStringNULL = new Asn1OpenType(new byte[]{0x04,0x00});

    String _strParam;

    public ParamsWithOctetString(String param)
    {
        _strParam = param;
    }


    public byte[] getEncoded() throws CryptoException
    {
        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
        Asn1OctetString octetString = new Asn1OctetString(_strParam);
        octetString.encode(buff);
        return buff.getMsgCopy();
    }

}

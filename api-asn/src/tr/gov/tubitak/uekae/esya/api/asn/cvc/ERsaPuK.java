package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.RsaPuK;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 Created by IntelliJ IDEA.
 User: bilen.ogretmen
 Date: 5/4/11
 Time: 4:14 PM
 To change this template use File | Settings | File Templates.
 */
public class ERsaPuK extends BaseASNWrapper<RsaPuK> {

    protected static Logger logger = LoggerFactory.getLogger(ERsaPuK.class);

    public ERsaPuK(byte[] aEncodedRsaPuk) throws ESYAException {
        super(aEncodedRsaPuk, new RsaPuK());
    }

    public ERsaPuK() {
        super(new RsaPuK());
    }
//    public ERsaPuK(byte[] aModulus, byte[] aExponent) {
//        super(new RsaPuK());
//    }

    public ERsaPuK(BigInteger aModulus, BigInteger aExponent) throws ESYAException {
        super(new RsaPuK());
        byte[] modulus = aModulus.toByteArray();
        byte[] exponent = aExponent.toByteArray();
//        byte[] exponent = new byte[4];
//        if (aExponent.toByteArray().length > 4)
//            throw new ESYAException("Exponent length must be exactly 4. Found: " + exponent.length);
//
//        if (aExponent.toByteArray().length < 4) {
//            System.arraycopy(aExponent.toByteArray(), 0, exponent, 4 - aExponent.toByteArray().length, aExponent.toByteArray().length);
//        }
        setModulus(modulus);
        setExponent(exponent);
    }

    public ERsaPuK(RSAPublicKey aRsaPublicKey) throws ESYAException {
        this(aRsaPublicKey.getModulus(), aRsaPublicKey.getPublicExponent());
    }

    public void setModulus(byte[] aModulus) throws ESYAException {
        int offset = aModulus.length % 32;
        if (offset == 0)
            getObject().modulus = new Asn1OctetString(aModulus);
        else if (offset == 1) //bigInteger'lar icin isaret amacli eklenmis olabilir
        {
            byte[] modulus = new byte[aModulus.length - 1];
            System.arraycopy(aModulus, 1, modulus, 0, modulus.length);
            getObject().modulus = new Asn1OctetString(modulus);
        } else {
            throw new ESYAException("Modulus length is incompatible. Found:" + aModulus.length);
        }
    }

    public void setExponent(byte[] aExponent) throws ESYAException {
        byte[] exponent = new byte[4];
        if (aExponent.length > 4)
            throw new ESYAException("Exponent length must be exactly 4. Found: " + aExponent.length);

        if (aExponent.length < 4) {
            System.arraycopy(aExponent, 0, exponent, 4 - aExponent.length, aExponent.length);
            getObject().exponent = new Asn1OctetString(exponent);
        } else {
            getObject().exponent = new Asn1OctetString(aExponent);
        }
    }

    public byte[] getModulus() {
        if (getObject().modulus == null)
            return null;
        return getObject().modulus.value;
/*        byte[] value = getObject().modulus.value;
        if ((value[0] & 0x80) == 0x00)
            return value;
        else {
            byte[] modulus = new byte[value.length + 1];
            System.arraycopy(value, 0, modulus, 1, value.length);
            modulus[0] = 0; // sign bit
            return modulus;
        }*/
    }

    public RSAPublicKey getAsPublicKey() throws InvalidKeyException
    {
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(new BigInteger(1, getModulus()), new BigInteger(1, getExponent()));
        try
        {
            return (RSAPublicKey) KeyFactory.getInstance("RSA","SunRsaSign").generatePublic(rsaPublicKeySpec);
        }
        catch (Exception ex)
        {
            throw new InvalidKeyException("Error in creating public key..");
        }
    }

    public byte[] getExponent() {
        if (getObject().exponent == null)
            return null;
        return getObject().exponent.value;
    }

    public byte[] getTagLen() {

        Asn1BerEncodeBuffer encBufModulus = new Asn1BerEncodeBuffer();
        encBufModulus.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.PRIM, 1, getModulus().length);

        Asn1BerEncodeBuffer encBufExponent = new Asn1BerEncodeBuffer();
        encBufExponent.encodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.PRIM, 2, getExponent().length);

        Asn1BerEncodeBuffer encodeBufRsaPuK = new Asn1BerEncodeBuffer();
        encodeBufRsaPuK.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.CONS, 73, encBufModulus.getMsgLength() + encBufExponent.getMsgLength());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            encodeBufRsaPuK.write(stream);
            encBufModulus.write(stream);
            encBufExponent.write(stream);
            return stream.toByteArray();
        } catch (IOException e) {
            logger.warn("Warning in ERsaPuK", e);
            return null;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error("Error in ERsaPuK", e);
            }
        }
    }
}


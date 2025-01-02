package tr.gov.tubitak.uekae.esya.api.asn.passport;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.passport.ElcPuK;

import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;

/**
 * Created by ahmet.asa on 1.08.2017.
 */
/*
* EC Parameters type BSI baz alınarak IBM support a göre check edildi, kontrol edilmeli.
* */
public class EElcPuK extends BaseASNWrapper<ElcPuK>{

    public static final byte UNCOMPRESSED_POINT_TAG = 0x04;

    public EElcPuK(byte[] encodedElcPuk) throws ESYAException{
        super(encodedElcPuk, new ElcPuK());
    }

    public EElcPuK(ElcPuK aElcPuk)
    {
        super(aElcPuk);
    }

    public EElcPuK(){
        super(new ElcPuK());
    }

    // BS ve ASM ye gore all params eklenecek oid den sonra ** cer
    public EElcPuK(int[] aOid , ECPublicKey ecPublicKey, boolean addAllParam) throws ESYAException{
        super(new ElcPuK());

        ECParameterSpec spec = ecPublicKey.getParams();
        byte[] publicPoint = encodePoint(ecPublicKey.getW(),spec.getCurve() );

        if(addAllParam){
            ECField ecField = spec.getCurve().getField();
            if(ecField instanceof ECFieldFp){
                ECFieldFp fp = (ECFieldFp) ecField;
                byte[] modulus = trimByteArray(fp.getP().toByteArray());
                setPrimeModulus(modulus);
            }
            byte[] firstCoefficient = trimByteArray(spec.getCurve().getA().toByteArray());
            byte[] secondCoefficient = trimByteArray(spec.getCurve().getB().toByteArray());
            byte[] basePoint = encodePoint(spec.getGenerator(), spec.getCurve());
            byte[] basePointOrder = trimByteArray(spec.getOrder().toByteArray());
            int cofactor = spec.getCofactor();

            setFirsCoefficient(firstCoefficient);
            setSecondCoefficient(secondCoefficient);
            setBasePoint(basePoint);
            setBaseOrderPoint(basePointOrder);
            setCofactor(cofactor);

        }

        setPublicPoint(publicPoint);
        setAlgId(aOid);
    }

    public byte[] encodePoint(ECPoint ecPoint, EllipticCurve curve) {

        byte[] pointX = trimByteArray(ecPoint.getAffineX().toByteArray());
        byte[] pointY = trimByteArray(ecPoint.getAffineY().toByteArray());
        int n = 0;
        if (curve != null) {
            // get fieldsize in bytes (+7 to round up and >>3 to divide by 8)
            n = (curve.getField().getFieldSize() + 7) >> 3;
        } else {
            // Normally n is the curve field size and pointX and pointY has length n.
            // This will simply try to use this size in case we don't have access to the curve.
            n = pointX.length > pointY.length ? pointX.length : pointY.length;
        }

        // In case pointX.length or pointY.length greater
        // the points will be trimmed to the length n

        // pointX.length and pointY.length should be equal to n
        int paddingX_length = 0;
        int paddingY_length = 0;

        // If the length of x was smaller than n we need to pad x on the left with 0
        if (pointX.length < n)
            paddingX_length = n - pointX.length;

        // If the length of y was smaller than n we need to pad y on the left with 0
        if (pointY.length < n)
            paddingY_length = n - pointY.length;

        // the resulting array should be two times n (n << 1) plus 1
        byte[] encoded = new byte[1 + (n << 1)];
        // Initialize result with all zeros (needed for the padding)
        Arrays.fill(encoded, (byte) 0x00);

        // Add 0x04, required tag by the encoding
        encoded[0] = UNCOMPRESSED_POINT_TAG;

        System.arraycopy(pointX, 0, encoded, 1 + paddingX_length, n - paddingX_length);
        System.arraycopy(pointY, 0, encoded, 1 + n + paddingY_length, n - paddingY_length);

        return encoded;
    }

    protected static byte[] trimByteArray(byte[] data) {
        boolean numberFound = false;
        int pos = 0;
        // Locate the first position of a non-zero
        for (pos = 0; pos < data.length; pos++) {
            numberFound = data[pos] != 0;
            if (numberFound) {
                break;
            }
        }

        byte[] result = null;
        if (numberFound) {
            // Non-zero was found - remove leading zeroes
            result = new byte[data.length - pos];
            System.arraycopy(data, pos, result, 0, data.length - pos);
        } else {
            // Only zeroes were found - return one zero
            result = new byte[]{0x00};
        }
        return result;
    }

    public void setPrimeModulus(byte[] aModulus){
        getObject().p = new Asn1OctetString(aModulus);

    }
    public void setFirsCoefficient(byte[] aFirstCoefficient){
        getObject().a = new Asn1OctetString(aFirstCoefficient);
    }
    public void setSecondCoefficient(byte[] secondCoefficient){
        getObject().b = new Asn1OctetString(secondCoefficient);
    }
    public void setBasePoint(byte[] basePoint){
        getObject().g = new Asn1OctetString(basePoint);
    }
    public void setBaseOrderPoint(byte[] aBaseOrderPoint){
        getObject().r = new Asn1OctetString(aBaseOrderPoint);
    }
    public void setPublicPoint(byte[] publicPoint){
        getObject().y = new Asn1OctetString(publicPoint);
    }
    public void setAlgId(int[] algId){
        getObject().oid = new Asn1ObjectIdentifier(algId);

    }
    public void setCofactor(Integer aCofactor){
    getObject().f = new Asn1Integer(aCofactor);
    }




}

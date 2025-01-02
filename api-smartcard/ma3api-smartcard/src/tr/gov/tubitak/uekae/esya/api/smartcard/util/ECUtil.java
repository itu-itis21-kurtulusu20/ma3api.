package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters;
import tr.gov.tubitak.uekae.esya.asn.cms.ECC_CMS_SharedInfo;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

public class ECUtil {

    public static byte[] generatePKCS11ID(ECPublicKey ecPublicKey) {
        ECPoint point = ecPublicKey.getW();
        EllipticCurve curve = ecPublicKey.getParams().getCurve();
        byte[] encodedPoint = ECParameters.encodePoint(point, curve, false);

        try {
            return MessageDigest.getInstance("SHA-1").digest(encodedPoint);
        } catch (NoSuchAlgorithmException e) {
            throw new ESYARuntimeException("Algorithm could not found", e);
        }
    }

    //Generate shared info bytes that will be used in key derivation
    public static byte[] generateKeyAgreementSharedInfoBytes(int[] keyWrapAlgOid, int keyLength, byte[] ukm) {
        byte[] encodedSharedInfo;

        ECC_CMS_SharedInfo info = new ECC_CMS_SharedInfo();
        Asn1OpenType openType = new Asn1OpenType(new byte[]{5, 0});
        info.keyInfo = new AlgorithmIdentifier(new Asn1ObjectIdentifier(keyWrapAlgOid), openType);
        info.suppPubInfo = new Asn1OctetString(integerToBytes(keyLength));
        if (ukm != null)
            info.entityUInfo = new Asn1OctetString(ukm);

        encodedSharedInfo = AsnIO.derEncode(info);

        return encodedSharedInfo;
    }

    private static byte[] integerToBytes(int keySize)
    {
        byte[] val = new byte[4];

        val[0] = (byte)(keySize >> 24);
        val[1] = (byte)(keySize >> 16);
        val[2] = (byte)(keySize >> 8);
        val[3] = (byte)keySize;

        return val;
    }
}

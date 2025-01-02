package tr.gov.tubitak.uekae.esya.cmpapi;

import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;

import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 3, 2010
 * Time: 1:59:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestTrash {
    public static void main(String[] args) throws Asn1ValueParseException, CMPProtocolException {
//        tryPRNG();
        tryTemplates();
    }

    private static void tryTemplates() throws CMPProtocolException {

    }

    private static void tryPRNG() throws Asn1ValueParseException {
        byte[] transactionIDBytes = new byte[16];

        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(transactionIDBytes);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        System.out.println("transactionIDBytes.toString():"+transactionIDBytes.toString());
        Asn1OctetString transactionID = new Asn1OctetString(transactionIDBytes);
        Asn1OctetString transactionID2 = new Asn1OctetString(transactionIDBytes.toString());
        System.out.println("transactionID:"+transactionID.value.length+" - "+transactionID.toString());
        System.out.println("transactionID2:"+transactionID2.value.length+" - "+transactionID2.toString());
    }
}

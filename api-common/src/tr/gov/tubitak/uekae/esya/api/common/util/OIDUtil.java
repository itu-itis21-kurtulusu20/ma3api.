package tr.gov.tubitak.uekae.esya.api.common.util;


/**
 * Conversion util between int[] OID s and their urn representation.
 *
 * @author ahmety
 * date: Aug 28, 2009
 */

public class OIDUtil
{

    public static String concat(int[] aOID){
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < aOID.length; i++) {
            b.append(aOID[i]);
            if (i<aOID.length-1)
                b.append('.');
        }
        return b.toString();
    }

    public static String toURN(int[] aOID){
        StringBuffer b = new StringBuffer("urn:oid:");
        b.append(concat(aOID));
        return b.toString();
    }

    public static int[] fromURN(String aOID){
        return parse(aOID.substring("urn:oid:".length()));
    }

    public static int[] parse(String aInput){
        String[] numbers = aInput.split("\\.");
        int[] oid = new int[numbers.length];
        for(int i=0; i<numbers.length; i++){
            oid[i] = Integer.parseInt(numbers[i]);
        }
        return oid;
    }

    public static void main(String[] args)
    {
        int[] oid = fromURN("urn:oid:1.2.840.10045.3.1.7");
        String oidStr = toURN(oid);
        int[] parsedOid = fromURN(oidStr);
        String finalStr = toURN(parsedOid);
        System.out.println("> "+ finalStr);
    }
}

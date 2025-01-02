package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ahmety
 * date: Apr 20, 2009
 */
public class IdGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    private static final Map<String, Integer> idMap = Collections.synchronizedMap(new HashMap<String, Integer>());

    public static final String TYPE_SIGNATURE = "Signature";
    public static final String TYPE_SIGNATURE_VALUE = "Signature-Value";
    public static final String TYPE_REFERENCE = "Reference";
    public static final String TYPE_SIGNED_INFO = "Signed-Info";
    public static final String TYPE_KEY_INFO = "Key-Info";
    public static final String TYPE_OBJECT = "Object";
    public static final String TYPE_DATA = "Data";
    public static final String TYPE_SIGNED_PROPERTIES = "Signed-Properties";


    /**
     * Generates sequential ids for different needs. For "Signature" input,
     * this method generates "Signature-Id-1", "Signature-Id-2",
     * "Signature-Id-3" etc..
     */
    public String uret(String aIdType)
    {
        synchronized (idMap){
            Integer index = 0;
            if (idMap.containsKey(aIdType)){
                index  = idMap.get(aIdType);
            }
            index++;
            idMap.put(aIdType, index);
            return aIdType + "-Id-" + index;
        }
    }

    public void update(String id){
        synchronized (idMap){
            try {
                String key = id.substring(0, id.indexOf("-Id-"));
                Integer value = Integer.parseInt(id.substring(id.lastIndexOf('-')+ 1));

                if (idMap.containsKey(key)){
                    value = Math.max(idMap.get(key), value);
                }

                System.out.println("!! update : "+id+"; key: "+key+" : "+value);

                idMap.put(key, value);
            }
            catch (Exception e){
                logger.debug("Elsewhere generated id : "+id, e);
                // probably elsewhere generated ids, so no overlap should occur
                //x.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        IdGenerator id = new IdGenerator();
        System.out.println(id.uret(TYPE_SIGNATURE));
        System.out.println(id.uret(TYPE_SIGNATURE));
        System.out.println(id.uret("Signature"));
        System.out.println(id.uret("Signature"));
    }
}

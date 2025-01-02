package tr.gov.tubitak.uekae.esya.api.tsl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IdGenerator {
	private static final Logger logger = LoggerFactory
			.getLogger(IdGenerator.class);

	private static final Map<String, Integer> idMap = Collections
			.synchronizedMap(new HashMap<String, Integer>());

	public static String TYPE_SIGNATURE = "Signature";
	public static String TYPE_SIGNATURE_VALUE = "Signature-Value";
	public static String TYPE_REFERENCE = "Reference";
	public static String TYPE_SIGNED_INFO = "Signed-Info";
	public static String TYPE_KEY_INFO = "Key-Info";
	public static String TYPE_OBJECT = "Object";
	public static String TYPE_DATA = "Data";
	public static String TYPE_SIGNED_PROPERTIES = "Signed-Properties";
	public static String TYPE_TSLID = "ID";

	// / <summary>
	// / Generates sequential ids for different needs. For "Signature" input,
	// / this method generates "Signature-Id-1", "Signature-Id-2",
	// / "Signature-Id-3" etc..
	// / </summary>
	public String uret(String aIdType) {
		synchronized (idMap) {
			Integer index = 0;
			if (idMap.containsKey(aIdType)) {
				index = idMap.get(aIdType);
			}
			index++;
			idMap.put(aIdType, index);
			// return aIdType + "-Id-" + index;
			return aIdType + index;
		}
	}

	public void update(String id) {
		synchronized (idMap) {
			try {
				// yukarıda "-Id-" olmadan return ediliyor. Sonra "-Id-"
				// üstünden işlem yapmak makul mu?
				String key = id.substring(0, id.indexOf("ID"));
				Integer value = Integer.parseInt(id.substring(id
						.lastIndexOf('-') + 1));

				if (idMap.containsKey(key)) {
					value = Math.max(idMap.get(key), value);
				}

				System.out.println("!! update : " + id + "; key: " + key
						+ " : " + value);

				idMap.put(key, value);
			} catch (Exception x) {
				logger.debug("Elsewhere generated id : " + id, x);
				// probably elsewhere generated ids, so no overlap should occur
				// x.printStackTrace();
			}
		}
	}
}

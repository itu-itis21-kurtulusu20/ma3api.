package tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder for "id&lt;-&gt;xml element" relations for xml signature tree.
 *
 * @author ahmety
 * date: May 20, 2009
 */
public class IdRegistry
{
    private Map<String, Element> mRegistry = new HashMap<String, Element>();
    private Map<Element, String> mValue2Id = new HashMap<Element, String>();

    /**
     * Keeps id&lt;-&gt;element mappings. If same object added twice for different
     * ids, this is tracked and first one is dismissed. Registering null element
     * means erase of mapping.
     *
     * @param aId of element
     * @param aElement that maps to an Id
     */
    public void put(String aId, Element aElement){
        if (aElement==null)
            mRegistry.remove(aId);
        else {
            if (mValue2Id.containsKey(aElement)){
                String oldId = mValue2Id.get(aElement);
                mRegistry.remove(oldId);
                mValue2Id.remove(aElement);
            }
            mRegistry.put(aId, aElement);
            mValue2Id.put(aElement, aId);
        }
    }

    public Element get(String aId){
        return mRegistry.get(aId);
    }


}

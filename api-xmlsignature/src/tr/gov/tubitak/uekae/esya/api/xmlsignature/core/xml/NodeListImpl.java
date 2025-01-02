package tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Immutable NodeList helper imlementation.
 * @see org.w3c.dom.NodeList 
 *
 * @author ahmety
 * date: Jul 7, 2009
 */
public class NodeListImpl implements NodeList
{
    private List<Node> mList;

    public NodeListImpl(List<Node> aList)
    {
        mList = aList;
    }

    public Node item(int i)
    {
        return mList.get(i);
    }

    public int getLength()
    {
        return mList.size();  
    }
}

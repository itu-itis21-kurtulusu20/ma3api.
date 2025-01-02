package tr.gov.tubitak.uekae.esya.api.common.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author ayetgin
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